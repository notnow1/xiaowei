package net.qixiaowei.integration.tenant.job;

import cn.hutool.core.exceptions.ExceptionUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import com.xxl.job.core.context.XxlJobHelper;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.tenant.annotation.IgnoreTenant;
import net.qixiaowei.integration.tenant.service.ITenantService;
import net.qixiaowei.integration.tenant.utils.TenantUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description: 多租户日志处理切面
 * @Author: hzk
 * @date: 2022/12/16 18:55
 * @param:
 * @return:
 **/

@Aspect
@Slf4j
public class TenantJobAspect {

    @Autowired
    private ITenantService tenantService;

    @Around("@annotation(xxlJob)")
    public Object around(ProceedingJoinPoint joinPoint, XxlJob xxlJob) throws Throwable {
        //如果是忽略租户，则跳过
        IgnoreTenant ignoreTenant = getMethodAnnotation(joinPoint, IgnoreTenant.class);
        if (StringUtils.isNotNull(ignoreTenant)) {
            return joinPoint.proceed();
        }
        this.execute(joinPoint, xxlJob);
        //JobHandler 无返回
        return null;
    }

    private void execute(ProceedingJoinPoint joinPoint, XxlJob xxlJob) {
        // 获得正常的租户列表
        List<Long> tenantIds = tenantService.getTenantIds();
        if (StringUtils.isEmpty(tenantIds)) {
            return;
        }
        //循环租户执行
        Map<Long, String> results = new ConcurrentHashMap<>();
        tenantIds.forEach(tenantId -> {
            TenantUtils.execute(tenantId, () -> {
                try {
                    joinPoint.proceed();
                } catch (Throwable e) {
                    results.put(tenantId, ExceptionUtil.getRootCauseMessage(e));
                    XxlJobHelper.log(StrUtil.format("[多租户({}) 执行定时任务({})，发生异常：{}]",
                            tenantId, xxlJob.value(), ExceptionUtils.getStackTrace(e)));
                }
            });
        });
        // 如果results非空，说明发生了异常，标记XXL-Job执行失败
        if (StringUtils.isNotEmpty(results)) {
            XxlJobHelper.handleFail(JSONUtil.toJsonStr(results));
        }
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Annotation> T getClassAnnotation(ProceedingJoinPoint joinPoint, Class<T> annotationClass) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getDeclaringClass().getAnnotation(annotationClass);
    }

    @SuppressWarnings("SameParameterValue")
    private static <T extends Annotation> T getMethodAnnotation(ProceedingJoinPoint joinPoint, Class<T> annotationClass) {
        return ((MethodSignature) joinPoint.getSignature()).getMethod().getAnnotation(annotationClass);
    }

}
