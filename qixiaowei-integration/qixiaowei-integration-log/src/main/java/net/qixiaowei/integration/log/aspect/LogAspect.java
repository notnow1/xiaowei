package net.qixiaowei.integration.log.aspect;

import java.util.Collection;
import java.util.Date;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.alibaba.fastjson2.JSONObject;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.filter.PropertyPreExcludeFilter;
import net.qixiaowei.integration.log.service.AsyncLogService;
import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Aspect;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;
import com.alibaba.fastjson2.JSON;
import net.qixiaowei.integration.common.utils.ServletUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.ip.IpUtils;
import net.qixiaowei.integration.log.enums.BusinessStatus;
import net.qixiaowei.integration.security.utils.SecurityUtils;

/**
 * 操作日志记录处理
 */
@Aspect
@Component
public class LogAspect {
    private static final Logger log = LoggerFactory.getLogger(LogAspect.class);

    /**
     * 排除敏感属性字段
     */
    public static final String[] EXCLUDE_PROPERTIES = {"password", "oldPassword", "newPassword", "confirmPassword"};

    @Autowired
    private AsyncLogService asyncLogService;

    /**
     * 处理完请求后执行
     *
     * @param joinPoint 切点
     */
    @AfterReturning(pointcut = "@annotation(controllerLog)", returning = "jsonResult")
    public void doAfterReturning(JoinPoint joinPoint, Log controllerLog, Object jsonResult) {
        handleLog(joinPoint, controllerLog, null, jsonResult);
    }

    /**
     * 拦截异常操作
     *
     * @param joinPoint 切点
     * @param e         异常
     */
    @AfterThrowing(value = "@annotation(controllerLog)", throwing = "e")
    public void doAfterThrowing(JoinPoint joinPoint, Log controllerLog, Exception e) {
        handleLog(joinPoint, controllerLog, e, null);
    }

    protected void handleLog(final JoinPoint joinPoint, Log controllerLog, final Exception e, Object jsonResult) {
        try {
            Date nowDate = DateUtils.getNowDate();
            OperationLogDTO operationLogDTO = new OperationLogDTO();
            Long businessId;
            //设置操作时间
            operationLogDTO.setOperationTime(nowDate);
            //设置操作用户信息
            this.setUserInfo(operationLogDTO);
            //设置请求信息
            this.setRequestInfo(joinPoint, operationLogDTO);
            int status = BusinessStatus.SUCCESS.ordinal();
            //错误处理
            if (StringUtils.isNotNull(e)) {
                status = BusinessStatus.FAIL.ordinal();
                String errorMessage = StringUtils.substring(e.getMessage(), 0, 2048);
                operationLogDTO.setErrorMessage(errorMessage);
            }
            // 处理设置注解上的参数
            this.getControllerMethodDescription(joinPoint, controllerLog, operationLogDTO, jsonResult);
            //设置状态
            operationLogDTO.setStatus(status);
            // 保存数据库
            asyncLogService.addOperationLogLog(operationLogDTO);
        } catch (Exception exp) {
            // 记录本地异常日志
            log.error("==前置通知异常==");
            log.error("异常信息:{}", exp.getMessage());
            exp.printStackTrace();
        }
    }

    /**
     * 获取注解中对方法的描述信息 用于Controller层注解
     *
     * @param log             日志
     * @param operationLogDTO 操作日志
     * @throws Exception
     */
    public void getControllerMethodDescription(JoinPoint joinPoint, Log log, OperationLogDTO operationLogDTO, Object jsonResult) throws Exception {
        // 设置action动作
        operationLogDTO.setOperationType(log.operationType().ordinal());
        BusinessType businessType = log.businessType();
        //设置业务类型
        operationLogDTO.setBusinessType(businessType.getCode());
        // 设置标题
        operationLogDTO.setTitle(log.title());
        // 是否需要保存request，参数和值
        if (log.isSaveRequestData()) {
            // 获取参数的信息，传入到数据库中。
            setRequestValue(joinPoint, operationLogDTO);
        }
        // 是否需要保存response，参数和值
        if (log.isSaveResponseData() && StringUtils.isNotNull(jsonResult)) {
            String resultData = StringUtils.substring(JSON.toJSONString(jsonResult), 0, 4096);
            operationLogDTO.setResultData(resultData);
        }
        //设置业务ID
        this.setBusinessId(joinPoint, log, operationLogDTO, jsonResult);
    }

    /**
     * 设置操作用户的信息到log中
     *
     * @param operationLogDTO 操作日志
     */
    private void setBusinessId(JoinPoint joinPoint, Log log, OperationLogDTO operationLogDTO, Object jsonResult) {
        String businessIdFlag = log.businessId();
        Long businessId = null;
        String requestMethod = operationLogDTO.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            Object[] argValues = joinPoint.getArgs();
            if (argValues != null && argValues.length > 0) {
                for (Object argValue : argValues) {
                    String argValueOfJsonString = JSON.toJSONString(argValue);
                    JSONObject argValueOfJsonObject = JSON.parseObject(argValueOfJsonString);
                    if (argValueOfJsonObject.containsKey(businessIdFlag)) {
                        Object o = argValueOfJsonObject.get(businessIdFlag);
                        if (StringUtils.isNotNull(o)) {
                            if (o instanceof Integer) {
                                Integer integer = (Integer) o;
                                businessId = integer.longValue();
                            } else if (o instanceof Long) {
                                businessId = (Long) o;
                            }
                            break;
                        }
                    }
                }
            }
        }
        if (StringUtils.isNull(businessId) && StringUtils.isNotNull(jsonResult) && jsonResult instanceof AjaxResult) {
            String jsonString = JSON.toJSONString(jsonResult);
            JSONObject jsonObject = JSON.parseObject(jsonString);
            JSONObject data = jsonObject.getJSONObject(AjaxResult.DATA_TAG);
            if (StringUtils.isNotNull(data)) {
                Object o = data.get(businessIdFlag);
                if (StringUtils.isNotNull(o)) {
                    if (o instanceof Integer) {
                        Integer integer = (Integer) o;
                        businessId = integer.longValue();
                    } else if (o instanceof Long) {
                        businessId = (Long) o;
                    }
                }
            }
        }
        if (StringUtils.isNull(businessId)) {
            businessId = 0L;
        }
        operationLogDTO.setBusinessId(businessId);
    }

    /**
     * 设置操作用户的信息到log中
     *
     * @param operationLogDTO 操作日志
     */
    private void setUserInfo(OperationLogDTO operationLogDTO) {
        String userAccount = SecurityUtils.getUserAccount();
        operationLogDTO.setOperatorUserAccount(userAccount);
        LoginUserVO loginUser = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUser)) {
            UserDTO userDTO = loginUser.getUserDTO();
            if (StringUtils.isNotNull(userDTO)) {
                String employeeName = userDTO.getEmployeeName();
                String employeeCode = userDTO.getEmployeeCode();
                String departmentName = userDTO.getDepartmentName();
                String postName = userDTO.getPostName();
                operationLogDTO.setOperatorEmployeeName(employeeName);
                operationLogDTO.setOperatorEmployeeCode(employeeCode);
                operationLogDTO.setOperatorDepartmentName(departmentName);
                operationLogDTO.setOperatorPostName(postName);
            }
        }
    }

    /**
     * 设置请求的信息
     *
     * @param operationLogDTO 操作日志
     * @throws Exception 异常
     */
    private void setRequestInfo(JoinPoint joinPoint, OperationLogDTO operationLogDTO) throws Exception {
        String className = joinPoint.getTarget().getClass().getName();
        String methodName = joinPoint.getSignature().getName();
        String method = className + "." + methodName + "()";
        HttpServletRequest request = ServletUtils.getRequest();
        String operatorIp = IpUtils.getIpAddr(request);
        String requestMethod = request.getMethod();
        String requestURI = request.getRequestURI();
        String userAgent = ServletUtils.getHeader(request, "User-Agent");
        operationLogDTO.setUserAgent(userAgent);
        operationLogDTO.setRequestMethod(requestMethod);
        operationLogDTO.setRequestUrl(requestURI);
        operationLogDTO.setMethod(method);
        operationLogDTO.setOperatorIp(operatorIp);
    }

    /**
     * 获取请求的参数，放到log中
     *
     * @param operationLogDTO 操作日志
     * @throws Exception 异常
     */
    private void setRequestValue(JoinPoint joinPoint, OperationLogDTO operationLogDTO) throws Exception {
        String requestMethod = operationLogDTO.getRequestMethod();
        if (HttpMethod.PUT.name().equals(requestMethod) || HttpMethod.POST.name().equals(requestMethod)) {
            String params = argsArrayToString(joinPoint.getArgs());
            String requestParam = StringUtils.substring(params, 0, 2048);
            operationLogDTO.setRequestParam(requestParam);
        }
    }

    /**
     * 参数拼装
     */
    private String argsArrayToString(Object[] paramsArray) {
        String params = "";
        if (paramsArray != null && paramsArray.length > 0) {
            for (Object o : paramsArray) {
                if (StringUtils.isNotNull(o) && !isFilterObject(o)) {
                    try {
                        String jsonObj = JSON.toJSONString(o, excludePropertyPreFilter());
                        params += jsonObj.toString() + " ";
                    } catch (Exception e) {
                    }
                }
            }
        }
        return params.trim();
    }

    /**
     * 忽略敏感属性
     */
    public PropertyPreExcludeFilter excludePropertyPreFilter() {
        return new PropertyPreExcludeFilter().addExcludes(EXCLUDE_PROPERTIES);
    }

    /**
     * 判断是否需要过滤的对象。
     *
     * @param o 对象信息。
     * @return 如果是需要过滤的对象，则返回true；否则返回false。
     */
    @SuppressWarnings("rawtypes")
    public boolean isFilterObject(final Object o) {
        Class<?> clazz = o.getClass();
        if (clazz.isArray()) {
            return clazz.getComponentType().isAssignableFrom(MultipartFile.class);
        } else if (Collection.class.isAssignableFrom(clazz)) {
            Collection collection = (Collection) o;
            for (Object value : collection) {
                return value instanceof MultipartFile;
            }
        } else if (Map.class.isAssignableFrom(clazz)) {
            Map map = (Map) o;
            for (Object value : map.entrySet()) {
                Map.Entry entry = (Map.Entry) value;
                return entry.getValue() instanceof MultipartFile;
            }
        }
        return o instanceof MultipartFile || o instanceof HttpServletRequest || o instanceof HttpServletResponse
                || o instanceof BindingResult;
    }
}
