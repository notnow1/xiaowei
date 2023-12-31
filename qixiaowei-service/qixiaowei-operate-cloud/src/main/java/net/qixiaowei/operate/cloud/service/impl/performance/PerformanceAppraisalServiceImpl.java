package net.qixiaowei.operate.cloud.service.impl.performance;

import cn.hutool.core.util.PageUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import groovy.lang.Lazy;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.HttpStatus;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.PageUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.integration.common.utils.uuid.IdUtils;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalItems;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeDimension;
import net.qixiaowei.operate.cloud.api.dto.performance.*;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDimensionDTO;
import net.qixiaowei.operate.cloud.api.vo.target.DecomposeDetailCyclesVO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalExcel;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.DecomposeDetailCyclesMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetDecomposeDimensionMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetDecomposeMapper;
import net.qixiaowei.operate.cloud.service.performance.*;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * PerformanceAppraisalService业务层处理
 *
 * @author Graves
 * @since 2022-11-23
 */
@Service
public class PerformanceAppraisalServiceImpl implements IPerformanceAppraisalService {
    @Autowired
    private PerformanceAppraisalMapper performanceAppraisalMapper;

    @Autowired
    private IPerformanceAppraisalObjectsService performanceAppraisalObjectsService;

    @Autowired
    private TargetDecomposeMapper targetDecomposeMapper;

    @Autowired
    private TargetDecomposeDimensionMapper targetDecomposeDimensionMapper;

    @Autowired
    private DecomposeDetailCyclesMapper decomposeDetailCyclesMapper;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private IPerformanceAppraisalColumnsService performanceAppraisalColumnsService;

    @Autowired
    private IPerformancePercentageService performancePercentageService;

    @Autowired
    @Lazy
    private IPerformanceRankService performanceRankService;

    @Autowired
    private IPerformAppraisalObjectSnapService performAppraisalObjectSnapService;

    @Autowired
    private IPerformanceAppraisalItemsService performanceAppraisalItemsService;

    @Autowired
    private RemoteIndicatorService indicatorService;

    @Autowired
    private IPerformAppraisalEvaluateService performAppraisalEvaluateService;

    @Autowired
    private RedisService redisService;

    /**
     * 查询绩效考核表详情
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 绩效考核表
     */
    @Override
    public PerformanceAppraisalDTO selectPerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId) {
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前考核任务已不存在");
        }
        setFieldName(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOList)) {
            return appraisal;
        }
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOList);
        return appraisal;
    }

    /**
     * 查询组织绩效任务考核详情-归档
     *
     * @param performanceAppraisalId 绩效考核表
     * @return List
     */
    @Override
    public PerformanceAppraisalDTO selectOrgAppraisalArchiveById(Long performanceAppraisalId) {
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前考核任务已不存在");
        }
        if (!appraisal.getAppraisalObject().equals(1)) {
            throw new ServiceException("当前考核对象不是组织的");
        }
        setFieldName(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        HashMap<String, BigDecimal> performanceRankMap = new HashMap<>();
        Integer sum = 0;
        int appraisalRank = 1;//名次
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectsDTO.setRank(appraisalRank);
            sum = setRankMap(performanceRankMap, performanceAppraisalObjectsDTO, sum);
            appraisalRank++;
        }
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOList);
        // 添加考核比例统计
        PerformanceAppraisalDTO appraisalDTO = countObjectFactorRank(performanceAppraisalId, appraisal, performanceRankMap, sum);
        if (appraisalDTO != null) return appraisalDTO;
        return appraisal;
    }

    /**
     * 查询绩效考核详情-归档-个人
     *
     * @param performanceAppraisalId 绩效考核表
     * @return List
     */
    @Override
    public PerformanceAppraisalDTO selectPerAppraisalArchiveById(Long performanceAppraisalId) {
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前考核任务已不存在");
        }
        if (!appraisal.getAppraisalObject().equals(2)) {
            throw new ServiceException("当前考核对象不是员工的");
        }
        setFieldName(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<EmployeeDTO> employeeData;
        HashMap<String, BigDecimal> performanceRankMap = new HashMap<>();
        Integer sum = 0;
        int appraisalRank = 1;//名次
        employeeData = getEmployeeData();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            sum = setRankMap(performanceRankMap, performanceAppraisalObjectsDTO, sum);
            for (EmployeeDTO employeeDTO : employeeData) {
                if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    performanceAppraisalObjectsDTO.setRank(appraisalRank);
                    performanceAppraisalObjectsDTO.setAppraisalObjectName(employeeDTO.getEmployeeName());
                    performanceAppraisalObjectsDTO.setAppraisalObjectCode(employeeDTO.getEmployeeCode());
                    performanceAppraisalObjectsDTO.setPostName(employeeDTO.getEmployeePostName());
                    performanceAppraisalObjectsDTO.setDepartmentName(employeeDTO.getEmployeeDepartmentName());
                    performanceAppraisalObjectsDTO.setDepartmentId(employeeDTO.getEmployeeDepartmentId());
                    String principalName = getPrincipalName(performanceAppraisalObjectsDTO.getAppraisalPrincipalId(), employeeData);//考核负责人名称
                    performanceAppraisalObjectsDTO.setAppraisalPrincipalName(principalName);
                    break;
                }
            }
            appraisalRank++;
        }
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOList);
        // 添加考核比例统计
        PerformanceAppraisalDTO appraisalDTO = countObjectFactorRank(performanceAppraisalId, appraisal, performanceRankMap, sum);
        if (appraisalDTO != null) return appraisalDTO;
        return appraisal;
    }

    /**
     * 根据考核人ID获取名称
     *
     * @param appraisalPrincipalId 考核人ID
     * @param employeeData         人员list
     * @return String
     */
    private String getPrincipalName(Long appraisalPrincipalId, List<EmployeeDTO> employeeData) {
        if (StringUtils.isNull(appraisalPrincipalId)) {
            return null;
        }
        for (EmployeeDTO employeeDatum : employeeData) {
            if (employeeDatum.getEmployeeId().equals(appraisalPrincipalId)) {
                return employeeDatum.getEmployeeName();
            }
        }
        return null;
    }

    /**
     * 查询组织绩效归档结果排名
     *
     * @param appraisalObjectsIds    考核对象ID集合
     * @param performanceAppraisalId 绩效考核ID
     * @param queryType              查询类型(1-全部，2-一级组织，0-自定义)
     * @return List
     */
    @Override
    public PerformanceAppraisalDTO selectOrgAppraisalRankByDTO(List<Long> appraisalObjectsIds, Long performanceAppraisalId, Integer queryType) {
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("请输入绩效考核ID");
        }
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前绩效考核已不存在");
        }
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = new ArrayList<>();
        if (queryType == 1) {
            performanceAppraisalObjectsDTOS = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        } else if (queryType == 0) {
            performanceAppraisalObjectsDTOS = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByIds(appraisalObjectsIds, performanceAppraisalId);
        } else if (queryType == 2) {// 一级组织
            List<DepartmentDTO> departmentByLevel = getDepartmentByLevel(appraisal);
            if (StringUtils.isEmpty(departmentByLevel)) {
                return appraisal;
            }
            List<Long> departmentIds = new ArrayList<>();
            for (DepartmentDTO departmentDTO : departmentByLevel) {
                departmentIds.add(departmentDTO.getParentDepartmentId());
            }
            performanceAppraisalObjectsDTOS = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByIds(departmentIds, performanceAppraisalId);
        }
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOS);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOS)) {
            return appraisal;
        }
        HashMap<String, BigDecimal> performanceRankMap = new HashMap<>();
        Integer sum = 0;
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            sum = setRankMap(performanceRankMap, performanceAppraisalObjectsDTO, sum);
        }
        // 添加考核比例统计
        PerformanceAppraisalDTO appraisalDTO = countObjectFactorRank(performanceAppraisalId, appraisal, performanceRankMap, sum);
        if (appraisalDTO != null) return appraisalDTO;
        return appraisal;
    }

    /**
     * 查询个人绩效归档结果排名
     *
     * @param performanceAppraisalDTO 考核对象DTO
     * @return List
     */
    @Override
    public PerformanceAppraisalDTO selectPerAppraisalRankByDTO(Map<String, List<Long>> performanceAppraisalDTO) {
        if (StringUtils.isNull(performanceAppraisalDTO.get("performanceAppraisalId"))) {
            throw new ServiceException("请输入绩效考核ID");
        }
        Long performanceAppraisalId = performanceAppraisalDTO.get("performanceAppraisalId").get(0);
        List<Long> departmentIds = performanceAppraisalDTO.get("departmentIds");
        List<String> departments = departmentIds.stream().map(Object::toString).collect(Collectors.toList());
        List<Long> postIds = performanceAppraisalDTO.get("postIds");
        List<String> posts = postIds.stream().map(Object::toString).collect(Collectors.toList());
        List<Long> rankNames = performanceAppraisalDTO.get("rankNames");
        Map<String, List<String>> idMaps = new HashMap<>();
        idMaps.put("departmentIds", departments);
        idMaps.put("postIds", posts);
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前绩效考核已不存在");
        }
        R<List<EmployeeDTO>> listR = employeeService.selectEmployeeByPDRIds(idMaps, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程失败 请联系管理员");
        }
        List<Long> appraisalObjectsIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(employeeDTOS)) {
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                appraisalObjectsIds.add(employeeDTO.getEmployeeId());
            }
        }
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByIds(appraisalObjectsIds, performanceAppraisalId);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOS)) {
            return appraisal;
        }
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOS);
        HashMap<String, BigDecimal> performanceRankMap = new HashMap<>();
        Integer sum = 0;
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            sum = setRankMap(performanceRankMap, performanceAppraisalObjectsDTO, sum);
        }
        // 添加考核比例统计
        PerformanceAppraisalDTO appraisalDTO = countObjectFactorRank(performanceAppraisalId, appraisal, performanceRankMap, sum);
        if (appraisalDTO != null) return appraisalDTO;
        return appraisal;
    }

    /**
     * 计算对象考核结果比例
     *
     * @param performanceAppraisalId 考核任务ID
     * @param appraisal              考核对象DTO
     * @param performanceRankMap     统计Map
     * @param sum                    总数
     * @return PerformanceAppraisalDTO
     */
    private PerformanceAppraisalDTO countObjectFactorRank(Long performanceAppraisalId, PerformanceAppraisalDTO appraisal, HashMap<String, BigDecimal> performanceRankMap, Integer sum) {
        performanceRankMap.remove(null);
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = selectPerformanceRankFactor(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            appraisal.setPerformanceAppraisalRankDTOS(new ArrayList<>());
            return null;
        }
        List<Map<String, Object>> performanceAppraisalRankList = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
            String performanceRankName = performanceRankFactorDTO.getPerformanceRankName();
            Long performanceRankFactorId = performanceRankFactorDTO.getPerformanceRankFactorId();
            Map<String, Object> map = new HashMap<>();
            if (StringUtils.isNotEmpty(performanceRankMap) && performanceRankMap.containsKey(performanceRankName)) {
                BigDecimal number = performanceRankMap.get(performanceRankName);
                BigDecimal proportion = number.divide(new BigDecimal(sum), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                map.put("performanceRankFactorId", performanceRankFactorId);
                map.put("performanceRankName", performanceRankName);
                map.put("number", number);
                map.put("proportion", proportion);
            } else {
                map.put("performanceRankFactorId", performanceRankFactorId);
                map.put("performanceRankName", performanceRankName);
                map.put("number", 0);
                map.put("proportion", 0);
            }
            performanceAppraisalRankList.add(map);
        }
        appraisal.setPerformanceAppraisalRankDTOS(performanceAppraisalRankList);
        return null;
    }

    /**
     * 根据等级获取部门信息
     *
     * @param appraisal 绩效
     * @return List
     */
    private List<DepartmentDTO> getDepartmentByLevel(PerformanceAppraisalDTO appraisal) {
        List<DepartmentDTO> departmentDTOS = getDepartmentData();
        R<List<DepartmentDTO>> listR = departmentService.selectDepartmentByLevel(1, SecurityConstants.INNER);
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        return listR.getData();
    }

    /**
     * 给绩效等级
     *
     * @param performanceRankMap             绩效比例Map
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @param sum                            总数
     */
    private static Integer setRankMap(HashMap<String, BigDecimal> performanceRankMap, PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, Integer sum) {
        Long appraisalResultId = performanceAppraisalObjectsDTO.getAppraisalResultId();
        String appraisalResultName = performanceAppraisalObjectsDTO.getAppraisalResult();
        if (StringUtils.isNotNull(appraisalResultId) && !appraisalResultId.equals(0L)) {
            sum += 1;
            if (!performanceRankMap.containsKey(appraisalResultName)) {
                performanceRankMap.put(appraisalResultName, BigDecimal.ONE);
            } else {
                BigDecimal performanceSum = performanceRankMap.get(appraisalResultName).add(BigDecimal.ONE);
                performanceRankMap.put(appraisalResultName, performanceSum);
            }
        }
        return sum;
    }

    /**
     * 查询绩效考核表列表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 绩效考核表
     */
    @DataScope(businessAlias = "pa")
    @Override
    public List<PerformanceAppraisalDTO> selectPerformanceAppraisalList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        Map<String, Object> params = performanceAppraisal.getParams();
        if (StringUtils.isNotEmpty(params)) {
            if (getEmployeeId(params) == 0 | getDepartmentId(params) == 0) {
                return new ArrayList<>();
            }
        }
        performanceAppraisal.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        if (StringUtils.isEmpty(performanceAppraisalDTOS)) {
            return performanceAppraisalDTOS;
        }
        this.handleResultOfPerformanceAppraisal(performanceAppraisalDTOS);
        return performanceAppraisalDTOS;
    }

    private void handleResultOfPerformanceAppraisal(List<PerformanceAppraisalDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(PerformanceAppraisalDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
                setFieldName(entity);
            });
        }
    }

    private void handleResultOfPerformanceAppraisalObjects(List<PerformanceAppraisalObjectsDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(PerformanceAppraisalObjectsDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
                setObjectFieldName(entity);
            });
            // 排序
            result.sort(Comparator.comparing(PerformanceAppraisalObjectsDTO::getCreateTime).reversed()
//                    .thenComparing(PerformanceAppraisalObjectsDTO::getCycleNumber)
            );
        }
    }

    /**
     * 获取高级搜索后的人员ID传入params
     *
     * @param params 请求参数
     */
    private int getEmployeeId(Map<String, Object> params) {
        Map<String, Object> params2 = new HashMap<>();
        for (String key : params.keySet()) {
            switch (key) {
                case "employeeNameEqual":
                    params2.put("employeeNameEqual", params.get("employeeNameEqual"));
                    break;
                case "employeeNameNotEqual":
                    params2.put("employeeNameNotEqual", params.get("employeeNameNotEqual"));
                    break;
                case "employeeNameLike":
                    params2.put("employeeNameLike", params.get("employeeNameLike"));
                    break;
                case "employeeNameNotLike":
                    params2.put("employeeNameNotLike", params.get("employeeNameNotLike"));
                    break;
                case "employeeCodeEqual":
                    params2.put("employeeCodeEqual", params.get("employeeCodeEqual"));
                    break;
                case "employeeCodeNotEqual":
                    params2.put("employeeCodeNotEqual", params.get("employeeCodeNotEqual"));
                    break;
                case "employeeCodeLike":
                    params2.put("employeeCodeLike", params.get("employeeCodeLike"));
                    break;
                case "employeeCodeNotLike":
                    params2.put("employeeCodeNotLike", params.get("employeeCodeNotLike"));
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isNotEmpty(params2)) {
            List<EmployeeDTO> employeeDTOS = empAdvancedSearch(params2);
            if (StringUtils.isEmpty(employeeDTOS)) {
                return 0;
            }
            List<Long> employeeIds = employeeDTOS.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
            params.put("employeeIds", employeeIds);
        }
        return 1;
    }

    /**
     * 获取高级搜索后的人员ID传入params
     *
     * @param params 请求参数
     */
    private int getDepartmentId(Map<String, Object> params) {
        Map<String, Object> params2 = new HashMap<>();
        for (String key : params.keySet()) {
            switch (key) {
                case "departmentNameEqual":
                    params2.put("departmentNameEqual", params.get("departmentNameEqual"));
                    break;
                case "departmentNameNotEqual":
                    params2.put("departmentNameNotEqual", params.get("departmentNameNotEqual"));
                    break;
                case "departmentNameLike":
                    params2.put("departmentNameLike", params.get("departmentNameLike"));
                    break;
                case "departmentNameNotLike":
                    params2.put("departmentNameNotLike", params.get("departmentNameNotLike"));
                    break;
                case "departmentCodeEqual":
                    params2.put("departmentCodeEqual", params.get("departmentCodeEqual"));
                    break;
                case "departmentCodeNotEqual":
                    params2.put("departmentCodeNotEqual", params.get("departmentCodeNotEqual"));
                    break;
                case "departmentCodeLike":
                    params2.put("departmentCodeLike", params.get("departmentCodeLike"));
                    break;
                case "departmentCodeNotLike":
                    params2.put("departmentCodeNotLike", params.get("departmentCodeNotLike"));
                    break;
                default:
                    break;
            }
        }
        if (StringUtils.isNotEmpty(params2)) {
            List<DepartmentDTO> departmentDTOS = depAdvancedSearch(params2);
            if (StringUtils.isEmpty(departmentDTOS)) {
                return 0;
            }
            List<Long> departmentIds = departmentDTOS.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
            params.put("departmentIds", departmentIds);
        }
        return 1;
    }

    /**
     * 人员远程高级搜索
     *
     * @param params 请求参数
     * @return List
     */
    private List<EmployeeDTO> empAdvancedSearch(Map<String, Object> params) {
        R<List<EmployeeDTO>> listR = employeeService.empAdvancedSearch(params, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("人员远程高级搜索失败 请联系管理员");
        }
        return employeeDTOS;
    }

    /**
     * 组织远程高级搜索
     *
     * @param params 请求参数
     * @return List
     */
    private List<DepartmentDTO> depAdvancedSearch(Map<String, Object> params) {
        R<List<DepartmentDTO>> listR = departmentService.depAdvancedSearch(params, SecurityConstants.INNER);
        List<DepartmentDTO> departmentDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("组织远程高级搜索失败 请联系管理员");
        }
        return departmentDTOS;
    }

    /**
     * 查询个人绩效归档
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 绩效考核表
     */
    @DataScope(businessAlias = "pa")
    @Override
    public List<PerformanceAppraisalDTO> selectPerAppraisalArchiveList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setAppraisalObject(2);
        performanceAppraisal.setAppraisalStatus(4);
        Map<String, Object> params = performanceAppraisal.getParams();
        performanceAppraisal.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        this.handleResultOfPerformanceAppraisal(performanceAppraisalDTOS);
        for (PerformanceAppraisalDTO appraisalDTO : performanceAppraisalDTOS) {
            if (StringUtils.isNull(appraisalDTO.getFilingDate())) {
                appraisalDTO.setIsFiling(0);
            } else {
                appraisalDTO.setIsFiling(1);
            }
        }
        return performanceAppraisalDTOS;
    }

    /**
     * 查询组织绩效归档
     *
     * @param performanceAppraisalDTO 查询条件
     * @return 绩效考核表
     */
    @DataScope(businessAlias = "pa")
    @Override
    public List<PerformanceAppraisalDTO> selectOrgAppraisalArchiveList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setAppraisalObject(1);
        performanceAppraisal.setAppraisalStatus(4);
        Map<String, Object> params = performanceAppraisal.getParams();
        performanceAppraisal.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        this.handleResultOfPerformanceAppraisal(performanceAppraisalDTOS);
        for (PerformanceAppraisalDTO appraisalDTO : performanceAppraisalDTOS) {
            if (StringUtils.isNull(appraisalDTO.getFilingDate())) {
                appraisalDTO.setIsFiling(0);
            } else {
                appraisalDTO.setIsFiling(1);
            }
        }
        return performanceAppraisalDTOS;
    }

    /**
     * 为字段命名
     *
     * @param appraisal 考核任务
     */
    private static void setFieldName(PerformanceAppraisalDTO appraisal) {
        // 考核周期类型/考核周期
        if (StringUtils.isNotNull(appraisal.getCycleType())) {
            switch (appraisal.getCycleType()) {
                case 1:
                    appraisal.setCycleTypeName("月度");
                    appraisal.setCycleNumberName(appraisal.getCycleNumber().toString() + "月");
                    break;
                case 2:
                    appraisal.setCycleTypeName("季度");
                    if (appraisal.getCycleNumber() == 1) {
                        appraisal.setCycleNumberName("一季度");
                    } else if (appraisal.getCycleNumber() == 2) {
                        appraisal.setCycleNumberName("二季度");
                    } else if (appraisal.getCycleNumber() == 3) {
                        appraisal.setCycleNumberName("三季度");
                    } else if (appraisal.getCycleNumber() == 4) {
                        appraisal.setCycleNumberName("四季度");
                    }
                    break;
                case 3:
                    appraisal.setCycleTypeName("半年度");
                    if (appraisal.getCycleNumber() == 1) {
                        appraisal.setCycleNumberName("上半年");
                    } else {
                        appraisal.setCycleNumberName("下半年");
                    }
                    break;
                case 4:
                    appraisal.setCycleTypeName("年度");
                    appraisal.setCycleNumberName("整年度");
                    break;
            }
        }
        switch (appraisal.getEvaluationType()) {
            case 1:
                appraisal.setEvaluationTypeName("月度");
                break;
            case 2:
                appraisal.setEvaluationTypeName("季度");
                break;
            case 3:
                appraisal.setEvaluationTypeName("半年度");
                break;
            case 4:
                appraisal.setEvaluationTypeName("年度");
                break;
        }
        // 考核阶段
        if (StringUtils.isNotNull(appraisal.getAppraisalStatus())) {
            switch (appraisal.getAppraisalStatus()) {
                case 1:
                    appraisal.setAppraisalStatusName("制定目标");
                    break;
                case 2:
                    appraisal.setAppraisalStatusName("评议");
                    break;
                case 3:
                    appraisal.setAppraisalStatusName("排名");
                    break;
                case 4:
                    appraisal.setAppraisalStatusName("归档");
                    break;
            }
        }
        // 考核流程
        if (StringUtils.isNotNull(appraisal.getAppraisalFlow())) {
            switch (appraisal.getAppraisalFlow()) {
                case 1:
                    appraisal.setAppraisalFlowName("系统流程");
                    break;
                case 2:
                    appraisal.setAppraisalFlowName("仅导入结果");
                    break;
            }
        }
        if (StringUtils.isNull(appraisal.getFilingDate())) {
            appraisal.setIsFiling(0);
        } else {
            appraisal.setIsFiling(1);
        }
    }

    /**
     * 新增绩效考核表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = checkAppraisal(performanceAppraisalDTO);
        Integer appraisalObject = performanceAppraisalDTO.getAppraisalObject();
        Integer appraisalYear = performanceAppraisalDTO.getAppraisalYear();
        Integer appraisalFlow = performanceAppraisalDTO.getAppraisalFlow();// 考核流程
        List<DepartmentDTO> departmentData = null;
        List<EmployeeDTO> employeeData = null;
        if (appraisalObject == 1) {// 组织
            departmentData = getDepartmentData();
        } else {
            employeeData = getEmployeeData();
        }
        Long performanceRankId = performanceAppraisalDTO.getPerformanceRankId();
        if (StringUtils.isNull(performanceRankId)) {
            throw new ServiceException("请输入绩效等级ID");
        }
        PerformanceRankDTO performanceRankDTO = performanceRankService.selectPerformanceRankByPerformanceRankId(performanceRankId);
        if (StringUtils.isNull(performanceRankDTO)) {
            throw new ServiceException("当前绩效等级不存在 请检查绩效配置");
        }
        if (performanceAppraisalDTO.getCycleType() < performanceAppraisalDTO.getEvaluationType()) {
            throw new ServiceException("评议周期不可以超过考核周期");
        }
        performanceAppraisalDTO.setPerformanceRankName(performanceRankDTO.getPerformanceRankName());
        // 周期性考核标记:0否;1是
        if (performanceAppraisalDTO.getCycleFlag().equals(1)) {
            Integer cycleType = performanceAppraisalDTO.getCycleType();
            int monthNow;
            int quarterNow;
            int yearNow = DateUtils.getYear();
            if (appraisalYear < yearNow) {
                throw new ServiceException("过去的年份无法创建考核任务");
            } else if (appraisalYear > yearNow) {
                monthNow = 1;
                quarterNow = 1;
                performanceAppraisalDTO.setCycleNumber(1);
            } else {
                monthNow = DateUtils.getMonth();
                quarterNow = DateUtils.getQuarter();
            }
            if (StringUtils.isNull(cycleType)) {
                throw new ServiceException("请选择周期类型");
            }
            return operateTime(performanceAppraisalDTO, performanceAppraisalObjectsDTOS, appraisalObject,
                    appraisalYear, appraisalFlow, departmentData, employeeData, cycleType, monthNow, quarterNow);
        }
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList;// 对象List
        Integer cycleNumber = performanceAppraisalDTO.getCycleNumber();
        LocalDate appraisalStartDate = performanceAppraisalDTO.getAppraisalStartDate();
        LocalDate appraisalEndDate = performanceAppraisalDTO.getAppraisalEndDate();
        if (StringUtils.isNull(cycleNumber) && StringUtils.isNull(appraisalEndDate) && StringUtils.isNull(appraisalStartDate)) {
            throw new ServiceException("开始时间,结束时间和周期不能为空");
        }
        PerformanceAppraisal performanceAppraisal = setAppraisalValue(performanceAppraisalDTO);
        performanceAppraisalMapper.insertPerformanceAppraisal(performanceAppraisal);
        if (appraisalObject == 1) {
            performanceAppraisalObjectsDTOList = matchDepartmentObject(departmentData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow);
        } else {
            performanceAppraisalObjectsDTOList = matchEmployeeObject(employeeData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow);
        }
        performanceAppraisalObjectsService.insertPerformanceAppraisalObjectsS(performanceAppraisalObjectsDTOList);
        List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS = setSnapValue(appraisalObject, performanceAppraisalObjectsDTOList);
        return performAppraisalObjectSnapService.insertPerformAppraisalObjectSnaps(performAppraisalObjectSnapDTOS);
    }

    /**
     * 处理时间
     *
     * @return Integer
     */
    private Integer operateTime(PerformanceAppraisalDTO performanceAppraisalDTO, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS,
                                Integer appraisalObject, Integer appraisalYear, Integer appraisalFlow, List<DepartmentDTO> departmentData,
                                List<EmployeeDTO> employeeData, Integer cycleType, int monthNow, int quarterNow) {

        switch (cycleType) {//周期类型:1月度;2季度;3半年度;4年度
            case 1://1月度
                for (int i = monthNow; i < 13; i++) {
                    addPerformances(DateUtils.getMonthStart(appraisalYear, i), DateUtils.getMonthLast(appraisalYear, i),
                            performanceAppraisalDTO, i, appraisalObject, departmentData,
                            performanceAppraisalObjectsDTOS, appraisalFlow, employeeData);
                }
                return 1;
            case 2: //2季度
                for (int i = quarterNow; i < 5; i++) {
                    addPerformances(DateUtils.getQuarterStart(appraisalYear, i), DateUtils.getQuarterLast(appraisalYear, i),
                            performanceAppraisalDTO, i, appraisalObject, departmentData,
                            performanceAppraisalObjectsDTOS, appraisalFlow, employeeData);
                }
                return 1;
            case 3: //3半年度
                for (int i = quarterNow; i < 3; i++) {
                    addPerformances(DateUtils.getHalfYearStart(appraisalYear, i), DateUtils.getHalfYearLast(appraisalYear, i),
                            performanceAppraisalDTO, i, appraisalObject, departmentData,
                            performanceAppraisalObjectsDTOS, appraisalFlow, employeeData);
                }
                return 1;
            case 4: //4年度
                List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList;// 对象List
                performanceAppraisalDTO.setCycleNumber(1);
                performanceAppraisalDTO.setAppraisalStartDate(DateUtils.toLocalDate(DateUtils.getYearStart(appraisalYear)));
                performanceAppraisalDTO.setAppraisalEndDate(DateUtils.toLocalDate(DateUtils.getYearLast(appraisalYear)));
                PerformanceAppraisal performanceAppraisal = setAppraisalValue(performanceAppraisalDTO);
                performanceAppraisalMapper.insertPerformanceAppraisal(performanceAppraisal);
                if (appraisalObject == 1) {
                    performanceAppraisalObjectsDTOList = matchDepartmentObject(departmentData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow);
                } else {
                    performanceAppraisalObjectsDTOList = matchEmployeeObject(employeeData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow);
                }
                performanceAppraisalObjectsService.insertPerformanceAppraisalObjectsS(performanceAppraisalObjectsDTOList);
                List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS = setSnapValue(appraisalObject, performanceAppraisalObjectsDTOList);
                return performAppraisalObjectSnapService.insertPerformAppraisalObjectSnaps(performAppraisalObjectSnapDTOS);
        }
        throw new ServiceException("周期类型异常");
    }

    /**
     * 为快照表赋值
     *
     * @param appraisalObject                    主表
     * @param performanceAppraisalObjectsDTOList 对象表
     */
    private List<PerformAppraisalObjectSnapDTO> setSnapValue(Integer appraisalObject, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList) {
        List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO = new PerformAppraisalObjectSnapDTO();
            performAppraisalObjectSnapDTO.setAppraisalObjectName(performanceAppraisalObjectsDTO.getAppraisalObjectName());
            performAppraisalObjectSnapDTO.setAppraisalObjectCode(performanceAppraisalObjectsDTO.getAppraisalObjectCode());
            performAppraisalObjectSnapDTO.setPerformAppraisalObjectsId(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
            if (appraisalObject == 2) {
                performAppraisalObjectSnapDTO.setDepartmentId(performanceAppraisalObjectsDTO.getDepartmentId());
                performAppraisalObjectSnapDTO.setDepartmentName(performanceAppraisalObjectsDTO.getDepartmentName());
                performAppraisalObjectSnapDTO.setPostId(performanceAppraisalObjectsDTO.getPostId());
                performAppraisalObjectSnapDTO.setPostName(performanceAppraisalObjectsDTO.getPostName());
                performAppraisalObjectSnapDTO.setOfficialRankSystemId(performanceAppraisalObjectsDTO.getOfficialRankSystemId());
                performAppraisalObjectSnapDTO.setOfficialRank(performanceAppraisalObjectsDTO.getOfficialRank());
                performAppraisalObjectSnapDTO.setOfficialRankName(performanceAppraisalObjectsDTO.getOfficialRankName());
            }
            performAppraisalObjectSnapDTOS.add(performAppraisalObjectSnapDTO);
        }
        return performAppraisalObjectSnapDTOS;
    }

    /**
     * @param startTime                       开始时间
     * @param endTime                         结束时间
     * @param performanceAppraisalDTO         考核任务DTO
     * @param i                               索引
     * @param appraisalObject                 对象类型-1组织/2人员
     * @param departmentData                  组织数据
     * @param employeeData                    人员数据
     * @param performanceAppraisalObjectsDTOS 对象信息
     * @param appraisalFlow                   考核类型
     */
    private void addPerformances(Date startTime, Date endTime, PerformanceAppraisalDTO performanceAppraisalDTO, int i,
                                 Integer appraisalObject, List<DepartmentDTO> departmentData, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS,
                                 Integer appraisalFlow, List<EmployeeDTO> employeeData) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = new ArrayList<>();// 对象List
        performanceAppraisalDTO.setAppraisalStartDate(DateUtils.toLocalDate(startTime));
        performanceAppraisalDTO.setAppraisalEndDate(DateUtils.toLocalDate(endTime));

        performanceAppraisalDTO.setCycleNumber(i);

        PerformanceAppraisal performanceAppraisal = setAppraisalValue(performanceAppraisalDTO);
        performanceAppraisalMapper.insertPerformanceAppraisal(performanceAppraisal);
        if (appraisalObject == 1) {
            performanceAppraisalObjectsDTOList.addAll(matchDepartmentObject(departmentData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow));
        } else {
            performanceAppraisalObjectsDTOList.addAll(matchEmployeeObject(employeeData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow));
        }
        performanceAppraisalObjectsService.insertPerformanceAppraisalObjectsS(performanceAppraisalObjectsDTOList);
        List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS = setSnapValue(appraisalObject, performanceAppraisalObjectsDTOList);
        performAppraisalObjectSnapService.insertPerformAppraisalObjectSnaps(performAppraisalObjectSnapDTOS);
    }

    /**
     * 新增校验
     *
     * @param performanceAppraisalDTO 考核对象
     * @return
     */
    private List<PerformanceAppraisalObjectsDTO> checkAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO) {
        String appraisalName = performanceAppraisalDTO.getAppraisalName();
        if (StringUtils.isNull(appraisalName)) {
            throw new ServiceException("考核任务名称不能为空");
        }
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalListByName(appraisalName);
        if (StringUtils.isNotEmpty(performanceAppraisalDTOS)) {
            throw new ServiceException("考核任务名称已存在");
        }
        // 人员-组织 对象
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = performanceAppraisalDTO.getPerformanceAppraisalObjectsDTOS();
        List<Long> performAppraisalObjectsIds = new ArrayList<>();
        // 顺便排序
        int sort = 0;
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOS)) {
            throw new ServiceException("考核任务范围不能为空");
        }
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            Long objectId = performanceAppraisalObjectsDTO.getAppraisalObjectId();
            if (performAppraisalObjectsIds.contains(objectId)) {
                throw new ServiceException("人员/组织对象不可以重复");
            }
            performanceAppraisalObjectsDTO.setSort(sort);
            sort += 1;
            performAppraisalObjectsIds.add(objectId);
        }
        return performanceAppraisalObjectsDTOS;
    }

    /**
     * 获取人员信息
     *
     * @return 结果
     */
    public List<EmployeeDTO> getEmployeeData() {
        R<List<EmployeeDTO>> listR = employeeService.selectRemoteList(new EmployeeDTO(), SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程获取失败");
        }
        if (StringUtils.isEmpty(employeeDTOS)) {
            throw new ServiceException("人员信息为空，请检查");
        }
        return employeeDTOS;
    }

    /**
     * 同步数据-制定
     *
     * @param performAppraisalObjectsId 对象ID
     * @param appraisalObject           考核对象?1组织:2员工
     * @return 结果
     */
    @Override
    public List<PerformanceAppraisalItemsDTO> migrationDevelopData(Long performAppraisalObjectsId, Integer appraisalObject) {
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
        if (StringUtils.isNull(performanceAppraisalObjectsDTO)) {
            throw new ServiceException("当前组织目标制定已不存在");
        }
        Integer appraisalYear = performanceAppraisalObjectsDTO.getAppraisalYear();
        Integer cycleType = performanceAppraisalObjectsDTO.getCycleType();
        Integer cycleNumber = performanceAppraisalObjectsDTO.getCycleNumber();
        Long objectId = performanceAppraisalObjectsDTO.getAppraisalObjectId();
        int timeDimension;
        switch (cycleType) {
            case 1:
                timeDimension = 4;
                break;
            case 2:
                timeDimension = 3;
                break;
            case 3:
                timeDimension = 2;
                break;
            case 4:
                timeDimension = 1;
                break;
            default:
                throw new ServiceException("当前数据异常 考核周期类型不匹配");
        }
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        Map<String, Object> params = new HashMap<>();
        //1组织;2员工
        if (appraisalObject == 1) {
            params.put("decompositionDimension", "department");
        } else if (appraisalObject == 2) {
            params.put("decompositionDimension", "salesman");
        } else {
            throw new ServiceException("请传入正确的考核对象参数");
        }
        targetDecomposeDimension.setParams(params);
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDTOS = targetDecomposeDimensionMapper.selectTargetDecomposeDimensionList(targetDecomposeDimension);
        if (StringUtils.isEmpty(targetDecomposeDimensionDTOS)) {
            throw new ServiceException("当前还没有" + (appraisalObject == 1 ? "部门" : "销售员") + "的分解维度");
        }
        Long targetDecomposeDimensionId = targetDecomposeDimensionDTOS.get(0).getTargetDecomposeDimensionId();
        TargetDecompose targetDecompose = new TargetDecompose();
        targetDecompose.setTargetYear(appraisalYear);
        targetDecompose.setTimeDimension(timeDimension);
        targetDecompose.setTargetDecomposeDimensionId(targetDecomposeDimensionId);
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
        if (StringUtils.isEmpty(targetDecomposeDTOS)) {
            return new ArrayList<>();
        }
        List<Long> targetDecomposeIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getTargetDecomposeId).collect(Collectors.toList());
        List<DecomposeDetailCyclesVO> decomposeDetailCyclesVOS = decomposeDetailCyclesMapper.selectTargetDecomposeCyclesByTargetDecomposeIds(targetDecomposeIds);
        List<Long> indicatorIds = decomposeDetailCyclesVOS.stream().map(DecomposeDetailCyclesVO::getIndicatorId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<IndicatorDTO> indicatorDTOS = getIndicator(indicatorIds);
        LinkedHashMap<Long, Map<Long, List<DecomposeDetailCyclesVO>>> groupDecomposeDetailCyclesVOS = decomposeDetailCyclesVOS.stream().collect(
                Collectors.groupingBy(DecomposeDetailCyclesVO::getTargetDecomposeId, LinkedHashMap::new,
                        Collectors.groupingBy(DecomposeDetailCyclesVO::getTargetDecomposeDetailsId)));
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = new ArrayList<>();
        for (Long targetDecomposeId : groupDecomposeDetailCyclesVOS.keySet()) {
            Map<Long, List<DecomposeDetailCyclesVO>> groupDetailCyclesVOS = groupDecomposeDetailCyclesVOS.get(targetDecomposeId);
            List<Long> objectIds = new ArrayList<>();
            for (Long targetDecomposeDetailsId : groupDetailCyclesVOS.keySet()) {
                List<DecomposeDetailCyclesVO> detailCyclesVOS = groupDetailCyclesVOS.get(targetDecomposeDetailsId);
                DecomposeDetailCyclesVO detailCyclesVO = detailCyclesVOS.get(0);
                objectIds.add(appraisalObject == 1 ? detailCyclesVO.getDepartmentId() : detailCyclesVO.getEmployeeId());
            }
            if (!objectIds.contains(objectId)) {
                continue;
            }
            PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO = new PerformanceAppraisalItemsDTO();
            for (Long targetDecomposeDetailsId : groupDetailCyclesVOS.keySet()) {
                List<DecomposeDetailCyclesVO> detailCyclesVOS = groupDetailCyclesVOS.get(targetDecomposeDetailsId);
                DecomposeDetailCyclesVO decomposeDetailCyclesVO = detailCyclesVOS.get(0);
                if (!Objects.equals(appraisalObject == 1 ? decomposeDetailCyclesVO.getDepartmentId() : decomposeDetailCyclesVO.getEmployeeId(), objectId)) {
                    continue;
                }
                boolean isTure = false;
                for (DecomposeDetailCyclesVO detailCyclesVO : detailCyclesVOS) {
                    if (detailCyclesVO.getCycleNumber().equals(cycleNumber)) {
                        isTure = true;
                        Long indicatorId = detailCyclesVO.getIndicatorId();
                        BigDecimal cycleTarget = detailCyclesVO.getCycleTarget();
                        String indicatorName = indicatorDTOS.stream().filter(indicatorDTO ->
                                indicatorDTO.getIndicatorId().equals(indicatorId)).collect(Collectors.toList()).get(0).getIndicatorName();
                        performanceAppraisalItemsDTO.setIndicatorId(indicatorId);
                        performanceAppraisalItemsDTO.setIndicatorName(indicatorName);
                        performanceAppraisalItemsDTO.setTargetValue(cycleTarget);
                        break;
                    }
                }
                if (isTure) {
                    break;
                }
            }
            performanceAppraisalItemsDTOS.add(performanceAppraisalItemsDTO);
        }
        return performanceAppraisalItemsDTOS;
    }

    /**
     * 同步数据-评议
     *
     * @param performAppraisalObjectsId 对象ID
     * @param appraisalObject           考核对象?1组织:2员工
     * @return 结果
     */
    @Override
    public List<PerformanceAppraisalItemsDTO> migrationReviewData(Long performAppraisalObjectsId, Integer appraisalObject) {
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
        if (StringUtils.isNull(performanceAppraisalObjectsDTO)) {
            throw new ServiceException("当前组织目标制定已不存在");
        }
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOList = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
        if (StringUtils.isEmpty(performanceAppraisalItemsDTOList)) {
            return new ArrayList<>();
        }
        List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOS = performAppraisalEvaluateService.selectPerformAppraisalEvaluateByPerformAppraisalObjectId(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
        Map<Long, List<PerformAppraisalEvaluateDTO>> evaluateMap = performAppraisalEvaluateDTOS.stream().collect(Collectors.groupingBy(PerformAppraisalEvaluateDTO::getPerformAppraisalItemsId));
        Integer appraisalYear = performanceAppraisalObjectsDTO.getAppraisalYear();
        Integer evaluationType = performanceAppraisalObjectsDTO.getEvaluationType();
        Integer cycleNumber = performanceAppraisalObjectsDTO.getCycleNumber();
        Integer cycleType = performanceAppraisalObjectsDTO.getCycleType();
        Long objectId = performanceAppraisalObjectsDTO.getAppraisalObjectId();
        int timeDimension;
        switch (evaluationType) {
            case 1:
                timeDimension = 4;
                break;
            case 2:
                timeDimension = 3;
                break;
            case 3:
                timeDimension = 2;
                break;
            case 4:
                timeDimension = 1;
                break;
            default:
                throw new ServiceException("当前数据异常 评议周期类型不匹配");
        }
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        Map<String, Object> params = new HashMap<>();
        //1组织;2员工
        if (appraisalObject == 1) {
            params.put("decompositionDimension", "department");
        } else if (appraisalObject == 2) {
            params.put("decompositionDimension", "salesman");
        } else {
            throw new ServiceException("请传入正确的考核对象参数");
        }
        targetDecomposeDimension.setParams(params);
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDTOS = targetDecomposeDimensionMapper.selectTargetDecomposeDimensionList(targetDecomposeDimension);
        if (StringUtils.isEmpty(targetDecomposeDimensionDTOS)) {
            throw new ServiceException("当前还没有" + (appraisalObject == 1 ? "部门" : "销售员") + "的分解维度");
        }
        Long targetDecomposeDimensionId = targetDecomposeDimensionDTOS.get(0).getTargetDecomposeDimensionId();
        TargetDecompose targetDecompose = new TargetDecompose();
        targetDecompose.setTargetYear(appraisalYear);
        targetDecompose.setTimeDimension(timeDimension);
        targetDecompose.setTargetDecomposeDimensionId(targetDecomposeDimensionId);
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
        if (StringUtils.isEmpty(targetDecomposeDTOS)) {
            return new ArrayList<>();
        }
        List<Long> targetDecomposeIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getTargetDecomposeId).collect(Collectors.toList());
        List<DecomposeDetailCyclesVO> decomposeDetailCyclesVOS = decomposeDetailCyclesMapper.selectTargetDecomposeCyclesByTargetDecomposeIds(targetDecomposeIds);
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = new ArrayList<>();
        if (StringUtils.isEmpty(evaluateMap)) {
            for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsDTOList) {
                Long indicatorId = performanceAppraisalItemsDTO.getIndicatorId();
                String indicatorName = performanceAppraisalItemsDTO.getIndicatorName();
                List<DecomposeDetailCyclesVO> decomposeDetailCyclesVOList = decomposeDetailCyclesVOS.stream()
                        .filter(d -> d.getCycleNumber().equals(cycleNumber))
                        .filter(d -> d.getIndicatorId().equals(indicatorId))
                        .collect(Collectors.toList());
                if (StringUtils.isEmpty(decomposeDetailCyclesVOList)) {
                    break;
                }
                PerformanceAppraisalItemsDTO performanceAppraisalItems = new PerformanceAppraisalItemsDTO();
                performanceAppraisalItems.setIndicatorId(indicatorId);
                performanceAppraisalItems.setIndicatorName(indicatorName);
                performanceAppraisalItems.setActualValue(Optional.ofNullable(decomposeDetailCyclesVOList.get(0).getCycleActual()).orElse(BigDecimal.ZERO));
                performanceAppraisalItemsDTOS.add(performanceAppraisalItems);
            }
            return performanceAppraisalItemsDTOS;
        }
        LinkedHashMap<Long, Map<Long, List<DecomposeDetailCyclesVO>>> groupDecomposeDetailCyclesVOS = decomposeDetailCyclesVOS.stream().collect(
                Collectors.groupingBy(DecomposeDetailCyclesVO::getTargetDecomposeId, LinkedHashMap::new,
                        Collectors.groupingBy(DecomposeDetailCyclesVO::getTargetDecomposeDetailsId)));
        for (Long performAppraisalItemsId : evaluateMap.keySet()) {
            List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOList = evaluateMap.get(performAppraisalItemsId);
            List<Integer> evaluateNumbers = performAppraisalEvaluateDTOList.stream().map(PerformAppraisalEvaluateDTO::getEvaluateNumber).collect(Collectors.toList());
            PerformAppraisalEvaluateDTO appraisalEvaluateDTO = performAppraisalEvaluateDTOList.get(0);
            Long indicatorId = appraisalEvaluateDTO.getIndicatorId();
            String indicatorName = appraisalEvaluateDTO.getIndicatorName();
            for (Long targetDecomposeId : groupDecomposeDetailCyclesVOS.keySet()) {
                Map<Long, List<DecomposeDetailCyclesVO>> groupDetailCyclesVOS = groupDecomposeDetailCyclesVOS.get(targetDecomposeId);
                List<Long> objectIds = new ArrayList<>();
                boolean isContinue = false;
                for (Long targetDecomposeDetailsId : groupDetailCyclesVOS.keySet()) {
                    List<DecomposeDetailCyclesVO> detailCyclesVOS = groupDetailCyclesVOS.get(targetDecomposeDetailsId);
                    DecomposeDetailCyclesVO detailCyclesVO = detailCyclesVOS.get(0);
                    Long cyclesIndicatorId = detailCyclesVO.getIndicatorId();
                    if (!Objects.equals(cyclesIndicatorId, indicatorId)) {
                        isContinue = true;
                    }
                    objectIds.add(appraisalObject == 1 ? detailCyclesVO.getDepartmentId() : detailCyclesVO.getEmployeeId());
                }
                if (isContinue) {
                    continue;
                }
                if (!objectIds.contains(objectId)) {
                    continue;
                }
                // 创建对象
                boolean isTure = false;
                PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO = new PerformanceAppraisalItemsDTO();
                performanceAppraisalItemsDTO.setIndicatorId(indicatorId);
                performanceAppraisalItemsDTO.setIndicatorName(indicatorName);
                for (Long targetDecomposeDetailsId : groupDetailCyclesVOS.keySet()) {
                    List<DecomposeDetailCyclesVO> detailCyclesVOS = groupDetailCyclesVOS.get(targetDecomposeDetailsId);
                    DecomposeDetailCyclesVO decomposeDetailCyclesVO = detailCyclesVOS.get(0);
                    if (!Objects.equals(appraisalObject == 1 ? decomposeDetailCyclesVO.getDepartmentId() : decomposeDetailCyclesVO.getEmployeeId(), objectId)) {
                        continue;
                    }
                    if (StringUtils.isEmpty(performAppraisalEvaluateDTOList) || evaluationType.equals(cycleType)) {
                        for (DecomposeDetailCyclesVO detailCyclesVO : detailCyclesVOS) {
                            if (Objects.equals(cycleNumber, detailCyclesVO.getCycleNumber())) {
                                performanceAppraisalItemsDTO.setActualValue(detailCyclesVO.getCycleActual());
                                isTure = true;
                                break;
                            }
                        }
                    } else {
                        BigDecimal sum = new BigDecimal(0);
                        List<Map<String, Object>> evaluateList = new ArrayList<>();
                        for (PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO : performAppraisalEvaluateDTOList) {
                            Integer evaluateNumber = performAppraisalEvaluateDTO.getEvaluateNumber();
                            Long performAppraisalEvaluateId = performAppraisalEvaluateDTO.getPerformAppraisalEvaluateId();
                            for (DecomposeDetailCyclesVO detailCyclesVO : detailCyclesVOS) {
                                if (Objects.equals(evaluateNumber, detailCyclesVO.getCycleNumber())) {
                                    Map<String, Object> map = new HashMap<>();
                                    String name = getName(evaluationType, evaluateNumber);
                                    BigDecimal cycleActual = Optional.ofNullable(detailCyclesVO.getCycleActual()).orElse(BigDecimal.ZERO);
                                    map.put("name", name);
                                    map.put("value", cycleActual);
                                    map.put("performAppraisalEvaluateId", performAppraisalEvaluateId);
                                    sum = sum.add(cycleActual);
                                    evaluateList.add(map);
                                    isTure = true;
                                }
                            }
                        }
                        if (isTure) {
                            performanceAppraisalItemsDTO.setActualValue(sum);
                            performanceAppraisalItemsDTO.setEvaluateList(evaluateList);
                        }
                    }
                    if (isTure) {
                        break;
                    }
                }
                if (isTure) {
                    performanceAppraisalItemsDTOS.add(performanceAppraisalItemsDTO);
                    break;
                }
            }
        }
        return performanceAppraisalItemsDTOS;
    }

    /**
     * 获取评议周期名称
     *
     * @param evaluationType 评议类型
     * @param evaluateNumber 评议周期
     * @return 结果
     */
    private static String getName(Integer evaluationType, Integer evaluateNumber) {
        String name;
        switch (evaluationType) {
            case 1:
                name = evaluateNumber + "月";
                break;
            case 2:
                if (evaluateNumber == 1) {
                    name = "一季度";
                } else if (evaluateNumber == 2) {
                    name = "二季度";
                } else if (evaluateNumber == 3) {
                    name = "三季度";
                } else {
                    name = "四季度";
                }
                break;
            case 3:
                if (evaluateNumber == 1) {
                    name = "上半年";
                } else {
                    name = "下半年";
                }
                break;
            case 4:
                name = "整年度";
                break;
            default:
                throw new ServiceException("当前数据异常 评议周期类型不匹配");
        }
        return name;
    }

    /**
     * 获取部门信息
     *
     * @return 结果
     */
    private List<DepartmentDTO> getDepartmentData() {
        //远程查找部门列表
        R<List<DepartmentDTO>> listR = departmentService.selectDepartment(new DepartmentDTO(), SecurityConstants.INNER);
        List<DepartmentDTO> departmentDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程获取失败");
        }
        if (StringUtils.isEmpty(departmentDTOS)) {
            throw new ServiceException("部门信息为空，请检查");
        }
        return departmentDTOS;
    }

    /**
     * 为绩效任务表赋值0
     *
     * @param performanceAppraisalDTO 绩效任务dto
     * @return
     */
    private static PerformanceAppraisal setAppraisalValue(PerformanceAppraisalDTO performanceAppraisalDTO) {
        Integer appraisalFlow = performanceAppraisalDTO.getAppraisalFlow();
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        if (appraisalFlow == 2) {// 仅导入
            performanceAppraisal.setAppraisalStatus(4);
        }
        performanceAppraisal.setSelfDefinedColumnsFlag(0);
        performanceAppraisal.setCreateBy(SecurityUtils.getUserId());
        performanceAppraisal.setCreateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        performanceAppraisal.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return performanceAppraisal;
    }

    /**
     * 给组织DTO对象赋值
     *
     * @param departmentDTOS                  组织
     * @param performanceAppraisalObjectsDTOS 对象列表
     * @param performanceAppraisal            考核DTO
     * @param appraisalFlow                   考核流程
     */
    private List<PerformanceAppraisalObjectsDTO> matchDepartmentObject(List<DepartmentDTO> departmentDTOS, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS,
                                                                       PerformanceAppraisal performanceAppraisal, Integer appraisalFlow) {
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            boolean j = true;
            for (DepartmentDTO departmentDTO : departmentDTOS) {
                if (departmentDTO.getDepartmentId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    performanceAppraisalObjectsDTO.setPerformanceAppraisalId(performanceAppraisal.getPerformanceAppraisalId());
                    performanceAppraisalObjectsDTO.setAppraisalObjectName(departmentDTO.getDepartmentName());
                    performanceAppraisalObjectsDTO.setAppraisalObjectCode(departmentDTO.getDepartmentCode());
                    j = false;
                    Integer appraisalStatus = performanceAppraisal.getAppraisalStatus();
                    if (appraisalFlow == 1) {// 系统流程
                        if (StringUtils.isNotNull(appraisalStatus)) {
                            switch (appraisalStatus) {
                                case 1:
                                    performanceAppraisalObjectsDTO.setAppraisalObjectStatus(1);
                                    break;
                                case 2:
                                    performanceAppraisalObjectsDTO.setAppraisalObjectStatus(3);
                                    break;
                                case 3:
                                    performanceAppraisalObjectsDTO.setAppraisalObjectStatus(5);
                                    break;
                                case 4:
                                    performanceAppraisalObjectsDTO.setAppraisalObjectStatus(0);
                                    break;
                            }
                        }
                    } else {
                        performanceAppraisalObjectsDTO.setAppraisalObjectStatus(0);
                    }
                    Long examinationLeaderId = departmentDTO.getExaminationLeaderId();
                    if (StringUtils.isNotNull(examinationLeaderId)) {
                        EmployeeDTO employeeById = getEmployeeById(examinationLeaderId);
                        performanceAppraisalObjectsDTO.setAppraisalPrincipalName(employeeById.getEmployeeName());
                        performanceAppraisalObjectsDTO.setAppraisalPrincipalId(employeeById.getEmployeeId());
                    }
                    break;
                }
            }
            if (j) {
                throw new ServiceException("考核任务范围不能为空");
            }
        }
        return performanceAppraisalObjectsDTOS;
    }

    /**
     * 根据ID获取人员信息
     *
     * @param examinationLeaderId 人员ID
     * @return EmployeeDTO
     */
    private EmployeeDTO getEmployeeById(Long examinationLeaderId) {
        R<EmployeeDTO> employeeDTOR = employeeService.selectByEmployeeId(examinationLeaderId, SecurityConstants.INNER);
        EmployeeDTO employeeDTO = employeeDTOR.getData();
        if (employeeDTOR.getCode() != 200) {
            throw new ServiceException(employeeDTOR.getMsg());
        }
        if (StringUtils.isNull(employeeDTO)) {
            throw new ServiceException("当前的考核负责人已不存在");
        }
        return employeeDTO;
    }

    /**
     * 给组织DTO对象赋值
     *
     * @param employeeDTOS                    人员
     * @param performanceAppraisalObjectsDTOS 对象列表
     * @param performanceAppraisal            考核DTO
     * @param appraisalFlow                   考核流程
     */
    private List<PerformanceAppraisalObjectsDTO> matchEmployeeObject(List<EmployeeDTO> employeeDTOS, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS,
                                                                     PerformanceAppraisal performanceAppraisal, Integer appraisalFlow) {
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            boolean j = true;
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    performanceAppraisalObjectsDTO.setPerformanceAppraisalId(performanceAppraisal.getPerformanceAppraisalId());
                    performanceAppraisalObjectsDTO.setAppraisalObjectCode(employeeDTO.getEmployeeCode());
                    performanceAppraisalObjectsDTO.setAppraisalObjectName(employeeDTO.getEmployeeName());
                    performanceAppraisalObjectsDTO.setDepartmentId(employeeDTO.getEmployeeDepartmentId());
                    performanceAppraisalObjectsDTO.setDepartmentId(employeeDTO.getEmployeeDepartmentId());
                    performanceAppraisalObjectsDTO.setDepartmentName(employeeDTO.getEmployeeDepartmentName());
                    performanceAppraisalObjectsDTO.setPostId(employeeDTO.getEmployeePostId());
                    performanceAppraisalObjectsDTO.setPostName(employeeDTO.getEmployeePostName());
                    performanceAppraisalObjectsDTO.setOfficialRankSystemId(employeeDTO.getOfficialRankSystemId());
                    performanceAppraisalObjectsDTO.setOfficialRank(employeeDTO.getEmployeeRank());
                    performanceAppraisalObjectsDTO.setOfficialRankName(employeeDTO.getEmployeeRankName());
                    j = false;
                    Integer appraisalStatus = performanceAppraisal.getAppraisalStatus();
                    if (appraisalFlow == 1) {// 系统流程
                        if (StringUtils.isNotNull(appraisalStatus)) {
                            switch (appraisalStatus) {
                                case 1:
                                    performanceAppraisalObjectsDTO.setAppraisalObjectStatus(1);
                                    break;
                                case 2:
                                    performanceAppraisalObjectsDTO.setAppraisalObjectStatus(3);
                                    break;
                                case 3:
                                    performanceAppraisalObjectsDTO.setAppraisalObjectStatus(5);
                                    break;
                                case 4:
                                    performanceAppraisalObjectsDTO.setAppraisalObjectStatus(0);
                                    break;
                            }
                        }
                    } else {
                        performanceAppraisalObjectsDTO.setAppraisalObjectStatus(0);
                    }
                    break;
                }
            }
            if (j) {
                throw new ServiceException("未找到匹配到的员工信息");
            }
        }
        return performanceAppraisalObjectsDTOS;
    }

    /**
     * 修改绩效考核表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    @Override
    @Transactional
    public int updatePerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalMapper.updatePerformanceAppraisal(performanceAppraisal);
    }

    /**
     * 逻辑批量删除绩效考核表
     *
     * @param performanceAppraisalIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeletePerformanceAppraisalByPerformanceAppraisalIds(List<Long> performanceAppraisalIds) {
        if (StringUtils.isEmpty(performanceAppraisalIds)) {
            throw new ServiceException("请选择要删除的考核任务ID");
        }
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds);
        if (StringUtils.isEmpty(performanceAppraisalDTOS) || performanceAppraisalDTOS.size() != performanceAppraisalIds.size()) {
            throw new ServiceException("要删除的考核任务已不存在");
        }
        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDTOS) {
            Integer appraisalFlow = performanceAppraisalDTO.getAppraisalFlow();
            Integer appraisalStatus = performanceAppraisalDTO.getAppraisalStatus();
            LocalDate filingDate = performanceAppraisalDTO.getFilingDate();
            if ((appraisalFlow == 1 && appraisalStatus > 1) || (appraisalFlow == 2 && StringUtils.isNotNull(filingDate))) {
                throw new ServiceException("当前阶段不可删除");
            }
        }
        performanceAppraisalMapper.logicDeletePerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        // 自定义字段
        List<Long> performanceAppraisalColumnIds = new ArrayList<>();
        List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDTOS = performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalIds(performanceAppraisalIds);
        if (StringUtils.isNotEmpty(performanceAppraisalColumnsDTOS)) {
            performanceAppraisalColumnIds = performanceAppraisalColumnsDTOS.stream().map(PerformanceAppraisalColumnsDTO::getPerformAppraisalColumnsId).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(performanceAppraisalColumnIds)) {
            performanceAppraisalColumnsService.logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(performanceAppraisalColumnIds);
        }
        // 考核对象
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalIds(performanceAppraisalIds);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOList)) {
            return 1;
        }
        List<Long> performanceAppraisalObjectsIds = performanceAppraisalObjectsDTOList.stream().map(PerformanceAppraisalObjectsDTO::getPerformAppraisalObjectsId).collect(Collectors.toList());
        performanceAppraisalObjectsService.logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(performanceAppraisalObjectsIds);
        // 快照
        List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS =
                performAppraisalObjectSnapService.selectPerformAppraisalObjectSnapByAppraisalObjectsIds(performanceAppraisalObjectsIds);
        if (StringUtils.isEmpty(performAppraisalObjectSnapDTOS)) {
            throw new ServiceException("快照表数据异常 请联系管理员");
        }
        List<Long> performanceAppraisalSnapIds = performAppraisalObjectSnapDTOS.stream().map(PerformAppraisalObjectSnapDTO::getAppraisalObjectSnapId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(performanceAppraisalSnapIds)) {
            performAppraisalObjectSnapService.logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(performanceAppraisalSnapIds);
        }
        // 指标
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectIds(performanceAppraisalObjectsIds);
        if (StringUtils.isEmpty(performanceAppraisalItemsDTOS)) {
            return 1;
        }
        List<Long> performanceAppraisalItemIds = performanceAppraisalItemsDTOS.stream().map(PerformanceAppraisalItemsDTO::getPerformAppraisalItemsId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(performanceAppraisalItemIds)) {
            performanceAppraisalItemsService.logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsIds(performanceAppraisalItemIds);
            // 实际值列表
            List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOList = performAppraisalEvaluateService.selectPerformAppraisalEvaluateByPerformAppraisalItemIds(performanceAppraisalItemIds);
            if (StringUtils.isNotEmpty(performAppraisalEvaluateDTOList)) {
                List<Long> performAppraisalEvaluateIds = performAppraisalEvaluateDTOList.stream().map(PerformAppraisalEvaluateDTO::getPerformAppraisalEvaluateId).collect(Collectors.toList());
                performAppraisalEvaluateService.logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(performAppraisalEvaluateIds);
            }
        }
        return 1;
    }

    /**
     * 物理删除绩效考核表信息
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deletePerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId) {
        return performanceAppraisalMapper.deletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
    }

    /**
     * 逻辑删除绩效考核表信息
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeletePerformanceAppraisalByPerformanceAppraisalId(PerformanceAppraisalDTO
                                                                               performanceAppraisalDTO) {
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("请输入考核任务ID");
        }
        PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(performanceAppraisalById)) {
            throw new ServiceException("当前考核任务不存在");
        }
        Integer appraisalFlow = performanceAppraisalById.getAppraisalFlow();
        Integer appraisalStatus = performanceAppraisalById.getAppraisalStatus();
        LocalDate filingDate = performanceAppraisalById.getFilingDate();
        if ((appraisalFlow == 1 && appraisalStatus > 1) || (appraisalFlow == 2 && StringUtils.isNotNull(filingDate))) {
            throw new ServiceException("当前阶段不可删除");
        }
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        performanceAppraisal.setPerformanceAppraisalId(performanceAppraisalDTO.getPerformanceAppraisalId());
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        performanceAppraisalMapper.logicDeletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisal);
        // 自定义字段
        List<Long> performanceAppraisalColumnIds = new ArrayList<>();
        List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDTOS = performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
        if (StringUtils.isNotEmpty(performanceAppraisalColumnsDTOS)) {
            performanceAppraisalColumnIds = performanceAppraisalColumnsDTOS.stream().map(PerformanceAppraisalColumnsDTO::getPerformAppraisalColumnsId).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(performanceAppraisalColumnIds)) {
            performanceAppraisalColumnsService.logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(performanceAppraisalColumnIds);
        }
        // 对象
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOList)) {
            return 1;
        }
        List<Long> performanceAppraisalObjectIds = performanceAppraisalObjectsDTOList.stream().map(PerformanceAppraisalObjectsDTO::getPerformAppraisalObjectsId).collect(Collectors.toList());
        performanceAppraisalObjectsService.logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(performanceAppraisalObjectIds);
        // 快照
        List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS = performAppraisalObjectSnapService.selectPerformAppraisalObjectSnapByAppraisalObjectsIds(performanceAppraisalObjectIds);
        if (StringUtils.isEmpty(performAppraisalObjectSnapDTOS)) {
            throw new ServiceException("快照表数据异常 请联系管理员");
        }
        List<Long> performanceAppraisalSnapIds = performAppraisalObjectSnapDTOS.stream().map(PerformAppraisalObjectSnapDTO::getAppraisalObjectSnapId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(performanceAppraisalSnapIds)) {
            performAppraisalObjectSnapService.logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(performanceAppraisalSnapIds);
        }
        // 指标
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectIds(performanceAppraisalObjectIds);
        if (StringUtils.isEmpty(performanceAppraisalItemsDTOS)) {
            return 1;
        }
        List<Long> performanceAppraisalItemIds = performanceAppraisalItemsDTOS.stream().map(PerformanceAppraisalItemsDTO::getPerformAppraisalItemsId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(performanceAppraisalItemIds)) {
            performanceAppraisalItemsService.logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsIds(performanceAppraisalItemIds);
            // 实际值列表
            List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOList = performAppraisalEvaluateService.selectPerformAppraisalEvaluateByPerformAppraisalItemIds(performanceAppraisalItemIds);
            if (StringUtils.isNotEmpty(performAppraisalEvaluateDTOList)) {
                List<Long> performAppraisalEvaluateIds = performAppraisalEvaluateDTOList.stream().map(PerformAppraisalEvaluateDTO::getPerformAppraisalEvaluateId).collect(Collectors.toList());
                performAppraisalEvaluateService.logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(performAppraisalEvaluateIds);
            }
        }
        return 1;
    }

    /**
     * 物理删除绩效考核表信息
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */

    @Override
    @Transactional
    public int deletePerformanceAppraisalByPerformanceAppraisalId(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        return performanceAppraisalMapper.deletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisal.getPerformanceAppraisalId());
    }

    /**
     * 物理批量删除绩效考核表
     *
     * @param performanceAppraisalDtos 需要删除的绩效考核表主键
     * @return 结果
     */

    @Override
    @Transactional
    public int deletePerformanceAppraisalByPerformanceAppraisalIds
    (List<PerformanceAppraisalDTO> performanceAppraisalDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDtos) {
            stringList.add(performanceAppraisalDTO.getPerformanceAppraisalId());
        }
        return performanceAppraisalMapper.deletePerformanceAppraisalByPerformanceAppraisalIds(stringList);
    }

    /**
     * 批量新增绩效考核表信息
     *
     * @param performanceAppraisalDtos 绩效考核表对象
     */
    @Override
    @Transactional
    public int insertPerformanceAppraisals(List<PerformanceAppraisal> performanceAppraisalDtos) {
        return performanceAppraisalMapper.batchPerformanceAppraisal(performanceAppraisalDtos);
    }

    /**
     * 批量修改绩效考核表信息
     *
     * @param performanceAppraisalDtos 绩效考核表对象
     */
    @Override
    @Transactional
    public int updatePerformanceAppraisals(List<PerformanceAppraisalDTO> performanceAppraisalDtos) {
        List<PerformanceAppraisal> performanceAppraisalList = new ArrayList<>();

        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDtos) {
            PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
            BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
            performanceAppraisal.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisal.setCreateTime(DateUtils.getNowDate());
            performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalList.add(performanceAppraisal);
        }
        return performanceAppraisalMapper.updatePerformanceAppraisals(performanceAppraisalList);
    }

    /**
     * 导入系统的组织绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    @Override
    @Transactional
    public Map<Object, Object> importSysOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("绩效考核ID为空");
        }
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(performanceAppraisalById)) {
            throw new ServiceException("当前绩效考核任务已不存在");
        }
        try {
            //1.更新绩效考核表
            PerformanceAppraisal appraisal = new PerformanceAppraisal();
            appraisal.setPerformanceAppraisalId(performanceAppraisalId);
            appraisal.setSelfDefinedColumnsFlag(0);// 是否自定义         组织绩效归档导入
            appraisal.setUpdateBy(SecurityUtils.getUserId());
            appraisal.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalMapper.updatePerformanceAppraisal(appraisal);
            List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
            List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalMapper.selectRankFactorByAppraisalId(performanceAppraisalId);
            Map<String, Long> factorObjectMap = new HashMap<>();
            if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
                throw new ServiceException("当前绩效等级系数已不存在 请检查绩效等级");
            }
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                factorObjectMap.put(performanceRankFactorDTO.getPerformanceRankName(), performanceRankFactorDTO.getPerformanceRankFactorId());
            }
            Map<String, Long> orgObjectMap = new HashMap<>();// Code ,对象ID
            Map<String, Long> idMap = new HashMap<>();// Code ,主ID
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                orgObjectMap.put(performanceAppraisalObjectsDTO.getAppraisalObjectCode(), performanceAppraisalObjectsDTO.getAppraisalObjectId());
                idMap.put(performanceAppraisalObjectsDTO.getAppraisalObjectCode(), performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
            }
            List<Map<Integer, String>> listMap = getMaps(file, 1);
            //2.更新绩效考核对象表
            List<EmployeeDTO> employeeListByCode = getEmployeeDTOS(listMap, 3);
            List<String> employeeCodeAndName = new ArrayList<>();
            for (EmployeeDTO employeeDTO : employeeListByCode) {
                employeeCodeAndName.add(employeeDTO.getEmployeeName() + "（" + employeeDTO.getEmployeeCode() + "）");
            }
            List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = new ArrayList<>();//需要更新的对象表lIst
            List<Object> errorExcelList = new ArrayList<>();
            List<Map<Integer, String>> successExcels = new ArrayList<>();
            for (Map<Integer, String> map : listMap) {
                StringBuilder errorNote = new StringBuilder();
                if (StringUtils.isNotNull(map.get(1)) && ExcelUtils.isNotNumber(map.get(1))) {
                    errorNote.append("评议总分数格式不正确；");
                }
                List<String> performanceRankNames = performanceRankFactorDTOS.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).collect(Collectors.toList());
                performanceRankNames.add("不考核");
                if (StringUtils.isNull(map.get(2))) {
                    errorNote.append("考核结果不可以为空；");
                }
                if (StringUtils.isNotNull(map.get(2)) && !performanceRankNames.contains(map.get(2))) {
                    errorNote.append("考核结果不存在；");
                }
                if (StringUtils.isNotBlank(map.get(3))) {
                    if (!employeeCodeAndName.contains(map.get(3))) {
                        errorNote.append("考核负责人不存在；");
                    }
                }
                if (errorNote.length() == 0) {
                    successExcels.add(map);
                } else {
                    List<String> errorList = new ArrayList<>();
                    errorList.add(errorNote.substring(0, errorNote.length() - 1).toString());
                    errorList.addAll(map.values());
                    errorExcelList.add(errorList);
                }
            }
            List<Object> successExcelList = new ArrayList<>();
            for (Map<Integer, String> map : successExcels) {
                if (StringUtils.isNull(map.get(0))) {
                    break;
                }
                PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = new PerformanceAppraisalObjectsDTO();
                String value = map.get(0);
                String substring = value.substring(value.lastIndexOf("（") + 1, value.lastIndexOf("）"));
                Long performanceObjectId = orgObjectMap.get(substring);
                Long performAppraisalObjectsId = idMap.get(substring);
                Long factorResultId;
                if (StringUtils.isNull(map.get(2))) {
                    factorResultId = null;
                } else {
                    if (map.get(2).equals("不考核")) {
                        factorResultId = 0L;
                    } else {
                        factorResultId = factorObjectMap.get(map.get(2));
                    }
                }
                if (StringUtils.isNull(map.get(1))) {
                    performanceAppraisalObjectsDTO.setEvaluationScore(BigDecimal.ZERO);
                } else {
                    performanceAppraisalObjectsDTO.setEvaluationScore(new BigDecimal(map.get(1)));
                }
                performanceAppraisalObjectsDTO.setAppraisalObjectId(performanceObjectId);
                performanceAppraisalObjectsDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
                performanceAppraisalObjectsDTO.setAppraisalResultId(factorResultId);
                performanceAppraisalObjectsDTO.setAppraisalResult(map.get(2));
                performanceAppraisalObjectsDTO.setRemark(map.get(4));
                if (StringUtils.isNotEmpty(employeeListByCode)) {
                    for (EmployeeDTO employeeDTO : employeeListByCode) {
                        if (StringUtils.isNotBlank(map.get(3))) {
                            String value2 = map.get(3);
                            if (employeeDTO.getEmployeeCode().equals(value2.substring(value2.lastIndexOf("（") + 1, value2.lastIndexOf("）")))) {
                                performanceAppraisalObjectsDTO.setAppraisalPrincipalId(employeeDTO.getEmployeeId());
                                performanceAppraisalObjectsDTO.setAppraisalPrincipalName(employeeDTO.getEmployeeName());
                                break;
                            }
                        }
                    }
                }
                performanceAppraisalObjectsDTOS.add(performanceAppraisalObjectsDTO);
                successExcelList.add(map);
            }
            try {
                if (StringUtils.isNotEmpty(performanceAppraisalObjectsDTOS)) {
                    performanceAppraisalObjectsService.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsDTOS);
                }
            } catch (Exception e) {
                throw new ServiceException("更新绩效考核表Excel失败");

            }
            String errorExcelId;
            String simpleUUID = IdUtils.simpleUUID();
            if (StringUtils.isNotEmpty(errorExcelList)) {
                errorExcelId = CacheConstants.ERROR_EXCEL_KEY + simpleUUID;
                redisService.setCacheObject(errorExcelId, errorExcelList, CacheConstants.ERROR_EXCEL_LOCK_TIME, TimeUnit.HOURS);
            }
            return ExcelUtils.parseExcelResult(successExcelList, errorExcelList, false, simpleUUID);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
    }

    /**
     * 获取list
     *
     * @param file 文件
     * @param type 类型
     * @return 结果
     */
    private List<Map<Integer, String>> getMaps(MultipartFile file, Integer type) throws IOException {
        List<Map<Integer, String>> listMap;
        int maxLength = type == 1 ? 5 : 9;
        ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
        listMap = read.sheet(0).doReadSync();
        if (StringUtils.isEmpty(listMap)) {
//            throw new ServiceException((type == 1 ? "组织" : "个人") + "绩效归档Excel没有数据 请检查");
            throw new ServiceException("模板被修改，请重新下载模板进行导入");
        }
        String sheetName = EasyExcel.read(file.getInputStream()).build().excelExecutor().sheetList().get(0).getSheetName();
        if (StringUtils.equals(sheetName, "个人绩效归档导入错误报告")) {
            Map<Integer, String> head = listMap.get(0);
            if (head.size() != maxLength + 1) {
//                throw new ServiceException((type == 1 ? "组织" : "个人") + "绩效归档模板不正确 请检查");
                throw new ServiceException("模板被修改，请重新下载模板进行导入");
            }
            List<Map<Integer, String>> objects = new ArrayList<>();
            for (Map<Integer, String> map1 : listMap) {
                Map<Integer, String> map2 = new TreeMap<>();
                for (int i = 1; i < map1.size(); i++) {
                    map2.put(i - 1, map1.get(i));
                }
                objects.add(map2);
            }
            listMap = objects;
        } else if (StringUtils.equals(sheetName, (type == 1 ? "组织" : "个人") + "绩效归档导入")) {
            Map<Integer, String> head = listMap.get(0);
            if (head.size() != maxLength) {
//                throw new ServiceException((type == 1 ? "组织" : "个人") + "绩效归档模板不正确 请检查");
                throw new ServiceException("模板被修改，请重新下载模板进行导入");
            }
        } else {
            throw new ServiceException("模板被修改，请重新下载模板进行导入");
        }
        return listMap;
    }

    /**
     * 判断系统模板是否正确
     *
     * @param head 表头
     */
    private void judgmentSysTemplate(Map<Integer, String> head) {
        Collection<String> valueCollection = head.values();
        List<String> headValueList = new ArrayList<>(valueCollection);
        if (!headValueList.contains("评议总分数")) {
            throw new ServiceException("请导入正确的系统绩效考核模板");
        }
    }

    /**
     * 判断自定义模板是否正确
     *
     * @param head 表头
     */
    private void judgmentCustomTemplate(Map<Integer, String> head) {
        Collection<String> valueCollection = head.values();
        List<String> headValueList = new ArrayList<>(valueCollection);
        if (headValueList.contains("评议总分")) {
            throw new ServiceException("请导入正确的自定义绩效考核模板");
        }
    }

    /**
     * 导入系统的个人绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    @Override
    @Transactional
    public Map<Object, Object> importSysPerPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("绩效考核ID为空");
        }
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(performanceAppraisalById)) {
            throw new ServiceException("当前绩效考核任务已不存在");
        }
        try {
            //1.更新绩效考核表
            PerformanceAppraisal appraisal = new PerformanceAppraisal();
            appraisal.setPerformanceAppraisalId(performanceAppraisalId);
            appraisal.setSelfDefinedColumnsFlag(0);// 是否自定义
            appraisal.setUpdateBy(SecurityUtils.getUserId());
            appraisal.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalMapper.updatePerformanceAppraisal(appraisal);
            List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
            List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalMapper.selectRankFactorByAppraisalId(performanceAppraisalId);
            Map<String, Long> factorObjectMap = new HashMap<>();
            if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
                throw new ServiceException("当前绩效等级系数已不存在 请检查绩效等级");
            }
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                factorObjectMap.put(performanceRankFactorDTO.getPerformanceRankName(), performanceRankFactorDTO.getPerformanceRankFactorId());
            }
            Map<String, Long> orgObjectMap = new HashMap<>();
            Map<String, Long> idMap = new HashMap<>();// Code ,主ID
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                orgObjectMap.put(performanceAppraisalObjectsDTO.getAppraisalObjectCode(), performanceAppraisalObjectsDTO.getAppraisalObjectId());
                idMap.put(performanceAppraisalObjectsDTO.getAppraisalObjectCode(), performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
            }
            List<Map<Integer, String>> listMap = getMaps(file, 2);
//          Map<Integer, String> head = listMap.get(0);
//          judgmentSysTemplate(head);
            //2.更新绩效考核对象表
            List<EmployeeDTO> employeeListByCode = getEmployeeDTOS(listMap, 7);
            List<String> employeeCodeAndName = new ArrayList<>();
            for (EmployeeDTO employeeDTO : employeeListByCode) {
                employeeCodeAndName.add(employeeDTO.getEmployeeName() + "（" + employeeDTO.getEmployeeCode() + "）");
            }
            List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = new ArrayList<>();//需要更新的对象表lIst
            List<Object> errorExcelList = new ArrayList<>();
            List<Map<Integer, String>> successExcels = new ArrayList<>();
            for (Map<Integer, String> map : listMap) {
                StringBuilder errorNote = new StringBuilder();
                if (StringUtils.isNotNull(map.get(5)) && ExcelUtils.isNotNumber(map.get(5))) {
                    errorNote.append("评议总分数格式不正确；");
                }
                List<String> performanceRankNames = performanceRankFactorDTOS.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).collect(Collectors.toList());
                performanceRankNames.add("不考核");
                if (StringUtils.isNull(map.get(6))) {
                    errorNote.append("考核结果不可以为空；");
                }
                if (StringUtils.isNotNull(map.get(6)) && !performanceRankNames.contains(map.get(6))) {
                    errorNote.append("考核结果不存在；");
                }
                if (StringUtils.isNotBlank(map.get(7))) {
                    if (!employeeCodeAndName.contains(map.get(7))) {
                        errorNote.append("考核负责人不存在；");
                    }
                }
                if (errorNote.length() == 0) {
                    successExcels.add(map);
                } else {
                    List<String> errorList = new ArrayList<>();
                    errorList.add(errorNote.substring(0, errorNote.length() - 1));
                    errorList.addAll(map.values());
                    errorExcelList.add(errorList);
                }
            }
            List<Object> successExcelList = new ArrayList<>();
            for (Map<Integer, String> map : successExcels) {
                if (StringUtils.isNull(map.get(0))) {
                    break;
                }
                PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = new PerformanceAppraisalObjectsDTO();
                Long performanceObjectId = orgObjectMap.get(map.get(0));
                Long performAppraisalObjectsId = idMap.get(map.get(0));
                Long factorResultId;
                if (StringUtils.isNull(map.get(6))) {
                    factorResultId = null;
                } else {
                    if (map.get(6).equals("不考核")) {
                        factorResultId = 0L;
                    } else {
                        factorResultId = factorObjectMap.get(map.get(6));
                    }
                }
                if (StringUtils.isNull(map.get(5))) {
                    performanceAppraisalObjectsDTO.setEvaluationScore(BigDecimal.ZERO);
                } else {
                    performanceAppraisalObjectsDTO.setEvaluationScore(new BigDecimal(map.get(5)));
                }
                performanceAppraisalObjectsDTO.setAppraisalObjectId(performanceObjectId);
                performanceAppraisalObjectsDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
                performanceAppraisalObjectsDTO.setAppraisalResultId(factorResultId);
                performanceAppraisalObjectsDTO.setAppraisalResult(map.get(6));
                performanceAppraisalObjectsDTO.setRemark(map.get(8));
                if (StringUtils.isNotEmpty(employeeListByCode)) {
                    for (EmployeeDTO employeeDTO : employeeListByCode) {
                        String value = map.get(7);
                        if (employeeDTO.getEmployeeCode().equals(value.substring(value.lastIndexOf("（") + 1, value.lastIndexOf("）")))) {
                            performanceAppraisalObjectsDTO.setAppraisalPrincipalId(employeeDTO.getEmployeeId());
                            performanceAppraisalObjectsDTO.setAppraisalPrincipalName(employeeDTO.getEmployeeName());
                            break;
                        }
                    }
                }
                performanceAppraisalObjectsDTOS.add(performanceAppraisalObjectsDTO);
                successExcelList.add(map);
            }
            try {
                if (StringUtils.isNotEmpty(performanceAppraisalObjectsDTOS)) {
                    performanceAppraisalObjectsService.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsDTOS);
                }
            } catch (Exception e) {
                throw new ServiceException("更新绩效考核表Excel失败");
            }
            String errorExcelId;
            String simpleUUID = IdUtils.simpleUUID();
            if (StringUtils.isNotEmpty(errorExcelList)) {
                errorExcelId = CacheConstants.ERROR_EXCEL_KEY + simpleUUID;
                redisService.setCacheObject(errorExcelId, errorExcelList, CacheConstants.ERROR_EXCEL_LOCK_TIME, TimeUnit.HOURS);
            }
            return ExcelUtils.parseExcelResult(successExcelList, errorExcelList, false, simpleUUID);
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
    }

    /**
     * 获取考核人员信息
     *
     * @param listMap listMap
     * @param key     值
     * @return List
     */
    private List<EmployeeDTO> getEmployeeDTOS(List<Map<Integer, String>> listMap, int key) {
        List<String> assessmentList = new ArrayList<>();//考核负责人Code集合
        for (Map<Integer, String> map : listMap) {
            String value = map.get(key);
            if (StringUtils.isNull(map.get(0)) || StringUtils.isNull(value)) {
                continue;
            }
            if (assessmentList.contains(value)) {
                continue;
            }
            if (value.contains("（") && value.contains("）")) {
                assessmentList.add(value.substring(value.lastIndexOf("（") + 1, value.lastIndexOf("）")));
            }
        }
        if (StringUtils.isEmpty(assessmentList)) {
            return new ArrayList<>();
        }
        R<List<EmployeeDTO>> listR = employeeService.selectByCodes(assessmentList, SecurityConstants.INNER);
        List<EmployeeDTO> employeeListByCode = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        return employeeListByCode;
    }

    /**
     * 导入自定义的组织绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    @Override
    @Transactional
    public void importCustomOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("绩效考核ID为空");
        }
        PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(performanceAppraisalById)) {
            throw new ServiceException("当前绩效考核任务已不存在");
        }
        //1.更新绩效考核表
        PerformanceAppraisal appraisal = new PerformanceAppraisal();
        appraisal.setPerformanceAppraisalId(performanceAppraisalId);
        appraisal.setSelfDefinedColumnsFlag(1);// 是否自定义
        appraisal.setUpdateBy(SecurityUtils.getUserId());
        appraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalMapper.updatePerformanceAppraisal(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalMapper.selectRankFactorByAppraisalId(performanceAppraisalId);
        Map<String, Long> factorObjectMap = new HashMap<>();
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("当前绩效等级系数已不存在 请检查绩效等级");
        }
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
            factorObjectMap.put(performanceRankFactorDTO.getPerformanceRankName(), performanceRankFactorDTO.getPerformanceRankFactorId());
        }
        Map<String, Long> orgObjectMap = new HashMap<>();
        Map<String, Long> idMap = new HashMap<>();// Code ,主ID
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            orgObjectMap.put(performanceAppraisalObjectsDTO.getAppraisalObjectCode(), performanceAppraisalObjectsDTO.getAppraisalObjectId());
            idMap.put(performanceAppraisalObjectsDTO.getAppraisalObjectCode(), performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
        }
        ExcelReaderBuilder read;
        try {
            read = EasyExcel.read(file.getInputStream());
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
        List<Map<Integer, String>> listMap = read.doReadAllSync();
        Map<Integer, String> head = listMap.get(1);
        if (head.containsValue(null)) {
            throw new ServiceException("导入失败 请导入正确的自定义绩效考核模板");
        }
        judgmentCustomTemplate(head);
        listMap.remove(2);
        listMap.remove(1);
        listMap.remove(0);
        if (StringUtils.isEmpty(listMap)) {
            throw new ServiceException("绩效考核Excel没有数据 请检查");
        }
        List<List<String>> dataList = new ArrayList<>();
        for (int i = 3; i < head.keySet().size(); i++) {
            dataList.add(new ArrayList<>());
        }
        //2.更新绩效考核对象表
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = new ArrayList<>();//需要更新的对象表lIst
        Map<Integer, String> nameMap = new HashMap<>();
        for (int i = 0; i < listMap.size(); i++) {
            if (StringUtils.isNull(listMap.get(i).get(0))) {
                break;
            }
            Map<Integer, String> valueMap = listMap.get(1);
            nameMap.put(i, valueMap.get(0));
            PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = new PerformanceAppraisalObjectsDTO();
            Long performanceObjectId = orgObjectMap.get(valueMap.get(0));
            Long performAppraisalObjectsId = idMap.get(valueMap.get(0));
            if (StringUtils.isNull(valueMap.get(2))) {
                performanceAppraisalObjectsDTO.setAppraisalResultId(null);
            } else {
                if (valueMap.get(2).equals("不考核")) {
                    performanceAppraisalObjectsDTO.setAppraisalResultId(0L);
                } else {
                    Long factorResultId = factorObjectMap.get(valueMap.get(2));
                    performanceAppraisalObjectsDTO.setAppraisalResultId(factorResultId);
                }
            }
            performanceAppraisalObjectsDTO.setAppraisalResult(valueMap.get(2));
            performanceAppraisalObjectsDTO.setAppraisalObjectId(performanceObjectId);
            performanceAppraisalObjectsDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
            performanceAppraisalObjectsDTOS.add(performanceAppraisalObjectsDTO);
            for (int j = 3; j < head.keySet().size(); j++) {
                dataList.get(j - 3).add(valueMap.get(j));
            }
        }
        List<Map<String, String>> maps = new ArrayList<>();
        for (List<String> data : dataList) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < data.size(); i++) {
                map.put(nameMap.get(i), data.get(i));
            }
            maps.add(map);
        }
        performanceAppraisalObjectsService.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsDTOS);
        List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsAfter = new ArrayList<>();
        for (int j = 3; j < head.keySet().size(); j++) {
            PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO = new PerformanceAppraisalColumnsDTO();
            String value = JSONObject.toJSON(maps.get(j - 3)).toString();
            performanceAppraisalColumnsDTO.setSort(j);
            performanceAppraisalColumnsDTO.setPerformanceAppraisalId(performanceAppraisalId);
            performanceAppraisalColumnsDTO.setColumnName(head.get(j));
            performanceAppraisalColumnsDTO.setColumnValue(value);
            performanceAppraisalColumnsAfter.add(performanceAppraisalColumnsDTO);
        }
        List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsBefore =
                performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
        for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : performanceAppraisalColumnsAfter) {
            for (PerformanceAppraisalColumnsDTO appraisalColumnsDTO : performanceAppraisalColumnsBefore) {
                if (appraisalColumnsDTO.getColumnName().equals(performanceAppraisalColumnsDTO.getColumnName())) {
                    performanceAppraisalColumnsDTO.setPerformAppraisalColumnsId(appraisalColumnsDTO.getPerformAppraisalColumnsId());
                    break;
                }
            }
        }
        // 交集
        List<PerformanceAppraisalColumnsDTO> updatePerformanceColumns =
                performanceAppraisalColumnsAfter.stream().filter(performanceAppraisalColumnsDTO ->
                        performanceAppraisalColumnsBefore.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                ).collect(Collectors.toList());
        // 差集 Before中After的补集
        List<PerformanceAppraisalColumnsDTO> delPerformanceColumns =
                performanceAppraisalColumnsBefore.stream().filter(performanceAppraisalColumnsDTO ->
                        !performanceAppraisalColumnsAfter.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                ).collect(Collectors.toList());
        // 差集 After中Before的补集
        List<PerformanceAppraisalColumnsDTO> addPerformanceColumns =
                performanceAppraisalColumnsAfter.stream().filter(performanceAppraisalColumnsDTO ->
                        !performanceAppraisalColumnsBefore.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(updatePerformanceColumns)) {
            performanceAppraisalColumnsService.updatePerformanceAppraisalColumnss(updatePerformanceColumns);
        }
        if (StringUtils.isNotEmpty(delPerformanceColumns)) {
            List<Long> deleteIds = new ArrayList<>();
            for (PerformanceAppraisalColumnsDTO delPerformanceColumn : delPerformanceColumns) {
                deleteIds.add(delPerformanceColumn.getPerformAppraisalColumnsId());
            }
            performanceAppraisalColumnsService.logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(deleteIds);
        }
        if (StringUtils.isNotEmpty(addPerformanceColumns)) {
            performanceAppraisalColumnsService.insertPerformanceAppraisalColumnss(addPerformanceColumns);
        }
    }

    /**
     * 导入自定义的个人绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    @Override
    @Transactional
    public void importCustomPerPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("绩效考核ID为空");
        }
        PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(performanceAppraisalById)) {
            throw new ServiceException("当前绩效考核任务已不存在");
        }
        //1.更新绩效考核表
        PerformanceAppraisal appraisal = new PerformanceAppraisal();
        appraisal.setPerformanceAppraisalId(performanceAppraisalId);
        appraisal.setSelfDefinedColumnsFlag(1);// 是否自定义
        appraisal.setUpdateBy(SecurityUtils.getUserId());
        appraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalMapper.updatePerformanceAppraisal(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalMapper.selectRankFactorByAppraisalId(performanceAppraisalId);
        Map<String, Long> factorObjectMap = new HashMap<>();
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("当前绩效等级系数已不存在 请检查绩效等级");
        }
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
            factorObjectMap.put(performanceRankFactorDTO.getPerformanceRankName(), performanceRankFactorDTO.getPerformanceRankFactorId());
        }
        List<EmployeeDTO> employeeData;
        Map<String, Long> orgObjectMap = new HashMap<>();
        Map<String, Long> idMap = new HashMap<>();// Code ,主ID
        employeeData = getEmployeeData();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            for (EmployeeDTO employeeDTO : employeeData) {
                if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    orgObjectMap.put(employeeDTO.getEmployeeCode(), performanceAppraisalObjectsDTO.getAppraisalObjectId());
                    idMap.put(employeeDTO.getEmployeeCode(), performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
                    break;
                }
            }
        }
        ExcelReaderBuilder read;
        try {
            read = EasyExcel.read(file.getInputStream());
        } catch (IOException e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
        List<Map<Integer, String>> listMap = read.doReadAllSync();
        Map<Integer, String> head = listMap.get(1);
        if (head.containsValue(null)) {
            throw new ServiceException("导入失败 请导入正确的自定义绩效考核模板");
        }
        judgmentCustomTemplate(head);
        listMap.remove(2);
        listMap.remove(1);
        listMap.remove(0);
        if (StringUtils.isEmpty(listMap)) {
            throw new ServiceException("导入失败 绩效考核Excel没有数据 请检查");
        }
        List<List<String>> dataList = new ArrayList<>();
        for (int i = 6; i < head.keySet().size(); i++) {
            dataList.add(new ArrayList<>());
        }
        //2.更新绩效考核对象表
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = new ArrayList<>();//需要更新的对象表lIst
        Map<Integer, String> nameMap = new HashMap<>();
        for (int i = 0; i < listMap.size(); i++) {
            if (StringUtils.isNull(listMap.get(i).get(0))) {
                break;
            }
            Map<Integer, String> valueMap = listMap.get(i);
            nameMap.put(i, valueMap.get(0));
            PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = new PerformanceAppraisalObjectsDTO();
            Long performanceObjectId = orgObjectMap.get(valueMap.get(0));
            Long performAppraisalObjectsId = idMap.get(valueMap.get(0));
            if (StringUtils.isNull(valueMap.get(5))) {
                performanceAppraisalObjectsDTO.setAppraisalResultId(null);
            } else {
                if (valueMap.get(5).equals("不考核")) {
                    performanceAppraisalObjectsDTO.setAppraisalResultId(0L);
                } else {
                    Long factorResultId = factorObjectMap.get(valueMap.get(5));
                    performanceAppraisalObjectsDTO.setAppraisalResultId(factorResultId);
                }
            }
            performanceAppraisalObjectsDTO.setAppraisalResult(valueMap.get(5));
            performanceAppraisalObjectsDTO.setAppraisalObjectId(performanceObjectId);

            performanceAppraisalObjectsDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
            performanceAppraisalObjectsDTOS.add(performanceAppraisalObjectsDTO);
            for (int j = 6; j < head.keySet().size(); j++) {
                dataList.get(j - 6).add(valueMap.get(j));
            }
        }
        List<Map<String, String>> maps = new ArrayList<>();
        for (List<String> data : dataList) {
            Map<String, String> map = new HashMap<>();
            for (int i = 0; i < data.size(); i++) {
                map.put(nameMap.get(i), data.get(i));
            }
            maps.add(map);
        }
        performanceAppraisalObjectsService.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsDTOS);
        List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsAfter = new ArrayList<>();
        for (int j = 6; j < head.keySet().size(); j++) {
            PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO = new PerformanceAppraisalColumnsDTO();
            String value = JSONObject.toJSON(maps.get(j - 6)).toString();
            performanceAppraisalColumnsDTO.setSort(j);
            performanceAppraisalColumnsDTO.setPerformanceAppraisalId(performanceAppraisalId);
            performanceAppraisalColumnsDTO.setColumnName(head.get(j));
            performanceAppraisalColumnsDTO.setColumnValue(value);
            performanceAppraisalColumnsAfter.add(performanceAppraisalColumnsDTO);
        }
        List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsBefore =
                performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
        for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : performanceAppraisalColumnsAfter) {
            for (PerformanceAppraisalColumnsDTO appraisalColumnsDTO : performanceAppraisalColumnsBefore) {
                if (appraisalColumnsDTO.getColumnName().equals(performanceAppraisalColumnsDTO.getColumnName())) {
                    performanceAppraisalColumnsDTO.setPerformAppraisalColumnsId(appraisalColumnsDTO.getPerformAppraisalColumnsId());
                    break;
                }
            }
        }
        // 交集
        List<PerformanceAppraisalColumnsDTO> updatePerformanceColumns =
                performanceAppraisalColumnsAfter.stream().filter(performanceAppraisalColumnsDTO ->
                        performanceAppraisalColumnsBefore.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                ).collect(Collectors.toList());
        // 差集 Before中After的补集
        List<PerformanceAppraisalColumnsDTO> delPerformanceColumns =
                performanceAppraisalColumnsBefore.stream().filter(performanceAppraisalColumnsDTO ->
                        !performanceAppraisalColumnsAfter.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                ).collect(Collectors.toList());
        // 差集 After中Before的补集
        List<PerformanceAppraisalColumnsDTO> addPerformanceColumns =
                performanceAppraisalColumnsAfter.stream().filter(performanceAppraisalColumnsDTO ->
                        !performanceAppraisalColumnsBefore.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(updatePerformanceColumns)) {
            performanceAppraisalColumnsService.updatePerformanceAppraisalColumnss(updatePerformanceColumns);
        }
        if (StringUtils.isNotEmpty(delPerformanceColumns)) {
            List<Long> deleteIds = new ArrayList<>();
            for (PerformanceAppraisalColumnsDTO delPerformanceColumn : delPerformanceColumns) {
                deleteIds.add(delPerformanceColumn.getPerformAppraisalColumnsId());
            }
            performanceAppraisalColumnsService.logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(deleteIds);
        }
        if (StringUtils.isNotEmpty(addPerformanceColumns)) {
            performanceAppraisalColumnsService.insertPerformanceAppraisalColumnss(addPerformanceColumns);
        }
    }

    /**
     * 导出Excel
     *
     * @param performanceAppraisalDTO 绩效考核DTO
     * @return List
     */
    @Override
    public List<PerformanceAppraisalExcel> exportPerformanceAppraisal(PerformanceAppraisalDTO
                                                                              performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOList = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        return new ArrayList<>();
    }


    /**
     * 组织自定义导出
     *
     * @param performanceAppraisalId    考核对象ID
     * @param performanceRankFactorDTOS 绩效考核等级
     * @return Collection
     */
    @Override
    public Collection<List<Object>> dataOrgCustomList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDTOList =
                performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);

        List<String> objectCodeList = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectsDTO.setAppraisalObjectCode(performanceAppraisalObjectsDTO.getAppraisalObjectCode());
            performanceAppraisalObjectsDTO.setAppraisalObjectName(performanceAppraisalObjectsDTO.getAppraisalObjectName());
            objectCodeList.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());
        }
        List<List<String>> objects = new ArrayList<>();
        for (int i = 0; i < performanceAppraisalObjectsDTOList.size(); i++) {
            List<String> columns = new ArrayList<>();
            for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : performanceAppraisalColumnsDTOList) {
                String columnValue = performanceAppraisalColumnsDTO.getColumnValue();
                Map<String, String> valueMap = JSON.parseObject(columnValue, new TypeReference<HashMap<String, String>>() {
                });
                columns.add(valueMap.get(objectCodeList.get(i)));
            }
            objects.add(columns);
        }
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < performanceAppraisalObjectsDTOList.size(); i++) {
            List<Object> data = new ArrayList<>();
            PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = performanceAppraisalObjectsDTOList.get(i);
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());//考核编码
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());//考核对象
            addResult(performanceRankFactorDTOS, data, performanceAppraisalObjectsDTO);
            data.addAll(objects.get(i));//自定义的值
            list.add(data);
        }
        return list;
    }

    /**
     * 组织系统导出
     *
     * @param performanceAppraisalId    考核对象ID
     * @param performanceRankFactorDTOS 绩效考核等级
     * @return Collection
     */
    @Override
    public Collection<List<Object>> dataOrgSysList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<Long> appraisalPrincipalIds = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            if (StringUtils.isNotNull(performanceAppraisalObjectsDTO.getAppraisalPrincipalId())) {
                appraisalPrincipalIds.add(performanceAppraisalObjectsDTO.getAppraisalPrincipalId());
            }
        }
        List<EmployeeDTO> employeeDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(appraisalPrincipalIds)) {
            employeeDTOS = getEmployeeDTOSByIds(appraisalPrincipalIds);
        }
        List<List<Object>> list = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            List<Object> data = new ArrayList<>();
//            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());//考核对象
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());//考核对象
            BigDecimal evaluationScore = performanceAppraisalObjectsDTO.getEvaluationScore();
            data.add(StringUtils.isNull(evaluationScore) ? "0.00" : evaluationScore);//分数
            addResult(performanceRankFactorDTOS, data, performanceAppraisalObjectsDTO);
            if (StringUtils.isNotEmpty(employeeDTOS) && StringUtils.isNotNull(performanceAppraisalObjectsDTO.getAppraisalPrincipalId())) {
                for (EmployeeDTO employeeDTO : employeeDTOS) {
                    if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalPrincipalId())) {
//                        data.add(employeeDTO.getEmployeeCode());//考核责任人工号
                        data.add(employeeDTO.getEmployeeName());//考核责任人姓名
                        break;
                    }
                }
            } else {
                data.add("");
            }
            String remark = performanceAppraisalObjectsDTO.getRemark();
            data.add(StringUtils.isNull(remark) ? "" : remark);
            list.add(data);
        }
        return list;
    }

    /**
     * 根据考核人ID集合获取人员集合
     *
     * @param appraisalPrincipalIds 考核人ID集合
     * @return List
     */
    private List<EmployeeDTO> getEmployeeDTOSByIds(List<Long> appraisalPrincipalIds) {
        R<List<EmployeeDTO>> listR = employeeService.selectByEmployeeIds(appraisalPrincipalIds, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        if (StringUtils.isEmpty(employeeDTOS)) {
            throw new ServiceException("部分考核负责人已被删除 请重新导入");
        }
        return employeeDTOS;
    }

    /**
     * @param performanceAppraisalId    考核对象ID
     * @param performanceRankFactorDTOS 绩效考核等级
     * @return Collection
     */
    @Override
    public Collection<List<Object>> dataPerCustomList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDTOList =
                performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
        List<String> objectCodeList = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectsDTO.setAppraisalObjectCode(performanceAppraisalObjectsDTO.getAppraisalObjectCode());
            performanceAppraisalObjectsDTO.setAppraisalObjectName(performanceAppraisalObjectsDTO.getAppraisalObjectName());
            performanceAppraisalObjectsDTO.setPostName(performanceAppraisalObjectsDTO.getPostName());
            performanceAppraisalObjectsDTO.setDepartmentName(performanceAppraisalObjectsDTO.getDepartmentName());
            performanceAppraisalObjectsDTO.setOfficialRankName(performanceAppraisalObjectsDTO.getOfficialRankName());
            objectCodeList.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());
        }
        List<List<String>> objects = new ArrayList<>();
        for (int i = 0; i < performanceAppraisalObjectsDTOList.size(); i++) {
            List<String> columns = new ArrayList<>();
            for (int j = 0; j < performanceAppraisalColumnsDTOList.size(); j++) {
                PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO = performanceAppraisalColumnsDTOList.get(j);
                String columnValue = performanceAppraisalColumnsDTO.getColumnValue();
                Map<String, String> valueMap = JSON.parseObject(columnValue, new TypeReference<HashMap<String, String>>() {
                });
                columns.add(valueMap.get(objectCodeList.get(i)));
            }
            objects.add(columns);
        }
        List<List<Object>> list = new ArrayList<>();
        for (int i = 0; i < performanceAppraisalObjectsDTOList.size(); i++) {
            List<Object> data = new ArrayList<>();
            PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = performanceAppraisalObjectsDTOList.get(i);
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());//考核编码
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());//考核对象
            data.add(performanceAppraisalObjectsDTO.getPostName());//岗位
            data.add(performanceAppraisalObjectsDTO.getDepartmentName());//部门
            data.add(performanceAppraisalObjectsDTO.getOfficialRankName());//个人职级
            addResult(performanceRankFactorDTOS, data, performanceAppraisalObjectsDTO);
            String remark = performanceAppraisalObjectsDTO.getRemark();
            data.add(StringUtils.isNull(remark) ? "" : remark);
            data.addAll(objects.get(i));//自定义的值
            list.add(data);
        }
        return list;
    }

    /**
     * 导出给结果赋值
     *
     * @param performanceRankFactorDTOS      绩效比例
     * @param data                           excel数据
     * @param performanceAppraisalObjectsDTO 对象DTO
     */
    private static void addResult(List<PerformanceRankFactorDTO> performanceRankFactorDTOS, List<Object> data, PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        Long appraisalResultId = performanceAppraisalObjectsDTO.getAppraisalResultId();
        if (StringUtils.isNotNull(appraisalResultId)) {
            boolean isResult = true;
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                if (performanceRankFactorDTO.getPerformanceRankFactorId().equals(appraisalResultId)) {
                    data.add(performanceRankFactorDTO.getPerformanceRankName());//结果
                    isResult = false;
                    break;
                }
            }
            if (isResult) {
                if (appraisalResultId == 0L) {
                    data.add("不考核");
                } else {
                    data.add("");
                }
            }
        } else {
            data.add("");
        }
    }

    /**
     * @param performanceAppraisalId    考核对象ID
     * @param performanceRankFactorDTOS 绩效考核等级
     * @return Collection
     */
    @Override
    public Collection<List<Object>> dataPerSysList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<Long> appraisalPrincipalIds = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            if (StringUtils.isNotNull(performanceAppraisalObjectsDTO.getAppraisalPrincipalId())) {
                appraisalPrincipalIds.add(performanceAppraisalObjectsDTO.getAppraisalPrincipalId());
            }
        }
        List<EmployeeDTO> employeeDTOS;
        if (StringUtils.isNotEmpty(appraisalPrincipalIds)) {
            employeeDTOS = getEmployeeDTOSByIds(appraisalPrincipalIds);
        } else {
            R<List<EmployeeDTO>> listR = employeeService.selectByEmployeeIds(appraisalPrincipalIds, SecurityConstants.INNER);
            employeeDTOS = listR.getData();
            if (listR.getCode() != 200) {
                throw new ServiceException("远程调用失败 请联系管理员");
            }
        }
        List<List<Object>> list = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            List<Object> data = new ArrayList<>();
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());//考核编码
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());//考核对象
            data.add(performanceAppraisalObjectsDTO.getDepartmentName());//部门
            data.add(performanceAppraisalObjectsDTO.getPostName());//岗位
            data.add(performanceAppraisalObjectsDTO.getOfficialRankName());//个人职级
            BigDecimal evaluationScore = performanceAppraisalObjectsDTO.getEvaluationScore();
            data.add(StringUtils.isNull(evaluationScore) ? "0.00" : evaluationScore);//分数
            addResult(performanceRankFactorDTOS, data, performanceAppraisalObjectsDTO);
            if (StringUtils.isNotEmpty(employeeDTOS) && StringUtils.isNotNull(performanceAppraisalObjectsDTO.getAppraisalPrincipalId())) {
                for (EmployeeDTO employeeDTO : employeeDTOS) {
                    if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalPrincipalId())) {
                        data.add(employeeDTO.getEmployeeName());//考核责任人姓名
                        break;
                    }
                }
            } else {
                data.add("");
            }
            String remark = performanceAppraisalObjectsDTO.getRemark();
            data.add(StringUtils.isNull(remark) ? "" : remark);
            list.add(data);
        }
        return list;
    }


    /**
     * 根据appraisalId查询对象列表
     *
     * @param appraisalId 绩效任务ID
     * @return List
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalObjectList(Long appraisalId) {
        if (StringUtils.isNull(appraisalId)) {
            throw new ServiceException("绩效考核任务ID为空 无法导出");
        }
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(appraisalId);
        List<DepartmentDTO> departmentData = getDepartmentData();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            for (DepartmentDTO departmentDTO : departmentData) {
                if (departmentDTO.getDepartmentId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    performanceAppraisalObjectsDTO.setAppraisalObjectName(departmentDTO.getDepartmentName());
                    performanceAppraisalObjectsDTO.setAppraisalObjectCode(departmentDTO.getDepartmentCode());
                    break;
                }
            }
        }
        return performanceAppraisalObjectsDTOList;
    }

    /**
     * 根据appraisalId查询对象列表
     *
     * @param appraisalId 绩效任务ID
     * @return List
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectPerAppraisalObjectList(Long appraisalId) {
        if (StringUtils.isNull(appraisalId)) {
            throw new ServiceException("绩效考核任务ID为空 无法导出");
        }
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(appraisalId);
        List<EmployeeDTO> employeeData = getEmployeeData();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            for (EmployeeDTO employeeDTO : employeeData) {
                if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    performanceAppraisalObjectsDTO.setAppraisalObjectCode(employeeDTO.getEmployeeCode());
                    performanceAppraisalObjectsDTO.setAppraisalObjectName(employeeDTO.getEmployeeName());
                    performanceAppraisalObjectsDTO.setPostName(employeeDTO.getEmployeePostName());//岗位i
                    performanceAppraisalObjectsDTO.setDepartmentName(employeeDTO.getEmployeeDepartmentName());//部门
                    performanceAppraisalObjectsDTO.setPerformanceRankName(employeeDTO.getEmployeeRankName());//个人职级
                    break;
                }
            }
        }
        return performanceAppraisalObjectsDTOList;
    }

    /**
     * 根据绩效考核ID获取绩效下拉列表
     *
     * @param appraisalId 绩效任务ID
     * @return List
     */
    @Override
    public List<PerformanceRankFactorDTO> selectPerformanceRankFactor(Long appraisalId) {
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalMapper.selectRankFactorByAppraisalId(appraisalId);
        if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
            throw new ServiceException("当前的绩效等级未配置 请先配置绩效等级");
        }
        return performanceRankFactorDTOS;
    }

    /**
     * 归档组织/个人
     *
     * @param performanceAppraisalId 绩效任务ID
     * @return int
     */
    @Override
    @Transactional
    public int archive(Long performanceAppraisalId) {
        PerformanceAppraisalDTO performanceAppraisalByPerformanceAppraisalId = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        LocalDate filingDate = performanceAppraisalByPerformanceAppraisalId.getFilingDate();
        if (StringUtils.isNotNull(filingDate)) {
            throw new ServiceException("当前组织绩效任务已归档");
        }
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        performanceAppraisal.setPerformanceAppraisalId(performanceAppraisalId);
        performanceAppraisal.setFilingDate(DateUtils.getLocalDate());
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalMapper.updatePerformanceAppraisal(performanceAppraisal);
    }

    /**
     * 根据绩效考核ID查找比例
     *
     * @param performanceAppraisalId 绩效考核id
     * @return
     */
    @Override
    public List<PerformancePercentageDTO> selectPerformancePercentageByPerformanceAppraisalId(Long performanceAppraisalId) {
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("绩效考核ID为空");
        }
        PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(performanceAppraisalById)) {
            throw new ServiceException("当前绩效考核任务已不存在");
        }
        Long performanceRankId = performanceAppraisalById.getPerformanceRankId();
        if (StringUtils.isNull(performanceRankId)) {
            throw new ServiceException("当前绩效考核任务数据异常 绩效等级数据丢失");
        }
        return performancePercentageService.selectPerformancePercentageByPersonId(performanceRankId);
    }

    /**
     * 查询绩效考核表列表-组织-制定
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    @Override
    @DataScope(businessAlias = "pa")
    public TableDataInfo selectOrgAppraisalDevelopList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        Integer pageNum = performanceAppraisalObjectsDTO.getPageNum();
        Integer pageSize = performanceAppraisalObjectsDTO.getPageSize();
        PerformanceAppraisal appraisalDTO = new PerformanceAppraisal();
        appraisalDTO.setAppraisalObject(1);
        appraisalDTO.setAppraisalStatus(1);
        Map<String, Object> params = performanceAppraisalObjectsDTO.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        appraisalDTO.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(appraisalDTO);
        if (StringUtils.isEmpty(performanceAppraisalDTOS)) {
            return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        List<Long> performanceAppraisalIds = performanceAppraisalDTOS.stream().map(PerformanceAppraisalDTO::getPerformanceAppraisalId).collect(Collectors.toList());
        Integer appraisalObjectStatus = performanceAppraisalObjectsDTO.getAppraisalObjectStatus();
        List<Integer> appraisalObjectStatuses = new ArrayList<>();
        if (StringUtils.isNull(appraisalObjectStatus)) {
            appraisalObjectStatuses.add(1);
            appraisalObjectStatuses.add(2);
        } else {
            appraisalObjectStatuses.add(appraisalObjectStatus);
        }
        performanceAppraisalObjectsDTO.setAppraisalObjectStatusList(appraisalObjectStatuses);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalIds(performanceAppraisalIds);
//        Map<String, Object> params2 = new HashMap<>();
//        for (String key : params.keySet()) {
//            switch (key) {
//                case "departmentNameEqual":
//                    params2.put("departmentNameEqual", params.get("departmentNameEqual"));
//                    break;
//                case "departmentNameNotEqual":
//                    params2.put("departmentNameNotEqual", params.get("departmentNameNotEqual"));
//                    break;
//            }
//        }
//        if (StringUtils.isNotEmpty(params2)) {
//            List<DepartmentDTO> departmentDTOS = depAdvancedSearch(params2);
//            if (StringUtils.isEmpty(departmentDTOS)) {
//                return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
//            }
//            List<Long> departmentIds = departmentDTOS.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
//            params.put("departmentIds", departmentIds);
//        }
        performanceAppraisalObjectsDTO.setParams(params);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalMapper.selectOrgAppraisalObjectList(performanceAppraisalObjectsDTO);
        this.handleResultOfPerformanceAppraisalObjects(performanceAppraisalObjectsDTOList);
        List<PerformanceAppraisalObjectsDTO> partition;
        if (pageNum > PageUtil.totalPage(performanceAppraisalObjectsDTOList.size(), pageSize)) {
            partition = new ArrayList<>();
        } else {
            partition = ListUtils.partition(performanceAppraisalObjectsDTOList, pageSize).get(pageNum - 1);
        }
        return PageUtils.tableDataInfo(HttpStatus.SUCCESS, partition, performanceAppraisalObjectsDTOList.size());
    }

    /**
     * 查询绩效考核表列表-个人-制定
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    @DataScope(businessAlias = "pa")
    @Override
    public TableDataInfo selectPerAppraisalDevelopList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        Integer pageNum = performanceAppraisalObjectsDTO.getPageNum();
        Integer pageSize = performanceAppraisalObjectsDTO.getPageSize();
        List<Long> performanceAppraisalIds = new ArrayList<>();
        PerformanceAppraisal appraisalDTO = new PerformanceAppraisal();
        appraisalDTO.setAppraisalObject(2);
        appraisalDTO.setAppraisalStatus(1);
        Map<String, Object> params = performanceAppraisalObjectsDTO.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        appraisalDTO.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(appraisalDTO);
        if (StringUtils.isEmpty(performanceAppraisalDTOS)) {
            return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDTOS) {
            performanceAppraisalIds.add(performanceAppraisalDTO.getPerformanceAppraisalId());
        }
        Integer appraisalObjectStatus = performanceAppraisalObjectsDTO.getAppraisalObjectStatus();
        List<Integer> appraisalObjectStatuses = new ArrayList<>();
        if (StringUtils.isNull(appraisalObjectStatus)) {
            appraisalObjectStatuses.add(1);
            appraisalObjectStatuses.add(2);
        } else {
            appraisalObjectStatuses.add(appraisalObjectStatus);
        }
        performanceAppraisalObjectsDTO.setAppraisalObjectStatusList(appraisalObjectStatuses);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalIds(performanceAppraisalIds);
        Map<String, Object> params2 = new HashMap<>();
        for (String key : params.keySet()) {
            switch (key) {
                case "employeeNameEqual":
                    params2.put("employeeNameEqual", params.get("employeeNameEqual"));
                    break;
                case "employeeNameNotEqual":
                    params2.put("employeeNameNotEqual", params.get("employeeNameNotEqual"));
                    break;
                case "employeeCodeEqual":
                    params2.put("employeeCodeEqual", params.get("employeeCodeEqual"));
                    break;
                case "employeeCodeNotEqual":
                    params2.put("employeeCodeNotEqual", params.get("employeeCodeNotEqual"));
                    break;
                case "employeeDepartmentIdEqual":
                    params2.put("employeeDepartmentIdEqual", params.get("employeeDepartmentIdEqual"));
                    break;
                case "employeeDepartmentIdNotEqual":
                    params2.put("employeeDepartmentIdNotEqual", params.get("employeeDepartmentIdNotEqual"));
                    break;
                case "employeePostIdEqual":
                    params2.put("employeePostIdEqual", params.get("employeePostIdEqual"));
                    break;
                case "employeePostIdNotEqual":
                    params2.put("employeePostIdNotEqual", params.get("employeePostIdNotEqual"));
                    break;
            }
        }
        if (StringUtils.isNotEmpty(params2)) {
            List<EmployeeDTO> employeeDTOS = empAdvancedSearch(params2);
            if (StringUtils.isEmpty(employeeDTOS)) {
                return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
            }
            List<Long> employeeIds = employeeDTOS.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
            params.put("employeeIds", employeeIds);
        }
        performanceAppraisalObjectsDTO.setParams(params);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalMapper.selectOrgAppraisalObjectList(performanceAppraisalObjectsDTO);
        this.handleResultOfPerformanceAppraisalObjects(performanceAppraisalObjectsDTOList);
        List<PerformanceAppraisalObjectsDTO> partition;
        if (pageNum > PageUtil.totalPage(performanceAppraisalObjectsDTOList.size(), pageSize)) {
            partition = new ArrayList<>();
        } else {
            partition = ListUtils.partition(performanceAppraisalObjectsDTOList, pageSize).get(pageNum - 1);
        }
        return PageUtils.tableDataInfo(HttpStatus.SUCCESS, partition, performanceAppraisalObjectsDTOList.size());
    }

    /**
     * 获取对象ID集合
     *
     * @param performanceAppraisalObjectsDTO 对象DTO
     * @param performanceAppraisalIds        绩效任务id集合
     * @param objectType                     类型
     * @return Integer
     */
    private Integer getStatus(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, List<Long> performanceAppraisalIds, Integer objectType) {
        PerformanceAppraisal appraisalDTO = new PerformanceAppraisal();
        appraisalDTO.setAppraisalObject(objectType);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(appraisalDTO);
        if (StringUtils.isEmpty(performanceAppraisalDTOS)) {

        }
        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDTOS) {
            performanceAppraisalIds.add(performanceAppraisalDTO.getPerformanceAppraisalId());
        }
        return performanceAppraisalObjectsDTO.getAppraisalObjectStatus();
    }

    /**
     * 查询绩效考核表-组织-制定-详情
     *
     * @param performAppraisalObjectsId 绩效考核对象ID
     * @return PerformanceAppraisalObjectsDTO
     */
    @Override
    public PerformanceAppraisalObjectsDTO selectOrgAppraisalDevelopById(Long performAppraisalObjectsId) {
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = performanceAppraisalMapper.selectOrgAppraisalObjectByObjectId(performAppraisalObjectsId);
        setObjectFieldName(performanceAppraisalObjectsDTO);
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performAppraisalObjectsId);
        if (StringUtils.isEmpty(performanceAppraisalItemsDTOS)) {
            return performanceAppraisalObjectsDTO;
        }
        performanceAppraisalObjectsDTO.setPerformanceAppraisalItemsDTOS(performanceAppraisalItemsDTOS);
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 查询绩效考核表-个人-制定-详情
     *
     * @param performAppraisalObjectsId 绩效考核对象ID
     * @return PerformanceAppraisalObjectsDTO
     */
    @Override
    public PerformanceAppraisalObjectsDTO selectPerAppraisalDevelopById(Long performAppraisalObjectsId) {
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = performanceAppraisalMapper.selectOrgAppraisalObjectByObjectId(performAppraisalObjectsId);
        setObjectFieldName(performanceAppraisalObjectsDTO);
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performAppraisalObjectsId);
        if (StringUtils.isEmpty(performanceAppraisalItemsDTOS)) {
            return performanceAppraisalObjectsDTO;
        }
        performanceAppraisalObjectsDTO.setPerformanceAppraisalItemsDTOS(performanceAppraisalItemsDTOS);
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 编辑-绩效考核-制定-组织
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return dto
     */
    @Override
    @Transactional
    public PerformanceAppraisalObjectsDTO updateOrgDevelopPerformanceAppraisal(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        Long performAppraisalObjectsId = performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId();
        if (StringUtils.isNull(performAppraisalObjectsId)) {
            throw new ServiceException("绩效考核对象ID不能为空");
        }
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTOByObjectId = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
        if (StringUtils.isNull(performanceAppraisalObjectsDTOByObjectId)) {
            throw new ServiceException("当前绩效考核对象已不存在");
        }
        performanceAppraisalObjectsDTO.setCycleType(performanceAppraisalObjectsDTOByObjectId.getCycleType());
        performanceAppraisalObjectsDTO.setEvaluationType(performanceAppraisalObjectsDTOByObjectId.getEvaluationType());
        Long performanceAppraisalId = performanceAppraisalObjectsDTOByObjectId.getPerformanceAppraisalId();
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        // 更新绩效考核任务
        updateDevelopAppraisal(performanceAppraisalId, performanceAppraisalObjectsDTO, performanceAppraisalObjectsDTOList, 2, 2);
        // 更新绩效考核对象
        updateDevelopObject(performanceAppraisalObjectsDTO, performAppraisalObjectsId);
        // 更新评议指标信息
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO1 = updateDevelopItem(performanceAppraisalObjectsDTO, performAppraisalObjectsId);
        if (performanceAppraisalObjectsDTO1 != null) return performanceAppraisalObjectsDTO1;
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 编辑绩效考核-制定-个人
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return DTO
     */
    @Override
    public PerformanceAppraisalObjectsDTO updatePerDevelopPerformanceAppraisal(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        Long performAppraisalObjectsId = performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId();
        if (StringUtils.isNull(performAppraisalObjectsId)) {
            throw new ServiceException("绩效考核对象ID不能为空");
        }
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTOByObjectId =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
        if (StringUtils.isNull(performanceAppraisalObjectsDTOByObjectId)) {
            throw new ServiceException("当前绩效考核对象已不存在");
        }
        Long performanceAppraisalId = performanceAppraisalObjectsDTOByObjectId.getPerformanceAppraisalId();
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        // 更新绩效考核任务
        updateDevelopAppraisal(performanceAppraisalId, performanceAppraisalObjectsDTO, performanceAppraisalObjectsDTOList, 2, 2);
        // 更新绩效考核对象
        updateDevelopObject(performanceAppraisalObjectsDTO, performAppraisalObjectsId);
        // 更新评议指标信息
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO1 = updateDevelopItem(performanceAppraisalObjectsDTO, performAppraisalObjectsId);
        if (performanceAppraisalObjectsDTO1 != null) return performanceAppraisalObjectsDTO1;
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 制定更新指标
     *
     * @param performanceAppraisalObjectsDTO 对象DTO
     * @param performAppraisalObjectsId      对象ID
     * @return PerformanceAppraisalObjectsDTO
     */
    private PerformanceAppraisalObjectsDTO updateDevelopItem(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, Long performAppraisalObjectsId) {
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsAfter = performanceAppraisalObjectsDTO.getPerformanceAppraisalItemsDTOS();
        if (StringUtils.isEmpty(performanceAppraisalItemsAfter)) {
            return performanceAppraisalObjectsDTO;
        }
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsBefore = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performAppraisalObjectsId);
        List<Long> indicatorIds = new ArrayList<>();
        if (StringUtils.isEmpty(performanceAppraisalItemsAfter)) {
            throw new ServiceException("请添加指标");
        }
        if (StringUtils.isNotEmpty(performanceAppraisalItemsAfter)) {
            BigDecimal weight = new BigDecimal(0);
            for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsAfter) {
                weight = weight.add(performanceAppraisalItemsDTO.getWeight());
                performanceAppraisalItemsDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
                indicatorIds.add(performanceAppraisalItemsDTO.getIndicatorId());
            }
            if (weight.compareTo(new BigDecimal(100)) != 0) {
                throw new ServiceException("所有考核指标权重之和应等于100%");
            }
        }
        if (indicatorIds.contains(null)) {
            throw new ServiceException("请选择指标");
        }
        List<IndicatorDTO> indicatorDTOS = getIndicator(indicatorIds);
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsAfter) {
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                if (indicatorDTO.getIndicatorId().equals(performanceAppraisalItemsDTO.getIndicatorId())) {
                    performanceAppraisalItemsDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                    performanceAppraisalItemsDTO.setIndicatorValueType(indicatorDTO.getIndicatorValueType());
                    performanceAppraisalItemsDTO.setExamineDirection(indicatorDTO.getExamineDirection());
                    break;
                }
            }
        }
        operateItemValue(performanceAppraisalObjectsDTO, performanceAppraisalItemsBefore, performanceAppraisalItemsAfter);
        return null;
    }

    /**
     * 制定更新对象
     *
     * @param performanceAppraisalObjectsDTO 对象DTO
     * @param performAppraisalObjectsId      对象ID
     */
    private void updateDevelopObject(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, Long performAppraisalObjectsId) {
        Long appraisalPrincipalId = performanceAppraisalObjectsDTO.getAppraisalPrincipalId();
        if (StringUtils.isNotNull(appraisalPrincipalId)) {
            List<Long> appraisalPrincipalIds = new ArrayList<>();
            appraisalPrincipalIds.add(appraisalPrincipalId);
            List<EmployeeDTO> employeeDTOS = getEmployeeDTOSByIds(appraisalPrincipalIds);
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (Objects.equals(appraisalPrincipalId, employeeDTO.getEmployeeId())) {
                    performanceAppraisalObjectsDTO.setAppraisalPrincipalName(employeeDTO.getEmployeeName());
                    break;
                }
            }
        }
        PerformanceAppraisalObjectsDTO performanceAppraisalObjects = new PerformanceAppraisalObjectsDTO();
        performanceAppraisalObjects.setPerformAppraisalObjectsId(performAppraisalObjectsId);
        performanceAppraisalObjects.setAppraisalPrincipalId(performanceAppraisalObjectsDTO.getAppraisalPrincipalId());
        performanceAppraisalObjects.setAppraisalPrincipalName(performanceAppraisalObjectsDTO.getAppraisalPrincipalName());
        if (performanceAppraisalObjectsDTO.getIsSubmit() == 0) {
            performanceAppraisalObjects.setAppraisalObjectStatus(2);
        } else if (performanceAppraisalObjectsDTO.getIsSubmit() == 1) {
            performanceAppraisalObjects.setAppraisalObjectStatus(3);
        }
        performanceAppraisalObjectsService.updatePerformanceAppraisalObjects(performanceAppraisalObjects);
    }

    /**
     * 制定更新任务
     *
     * @param performanceAppraisalId             任务ID
     * @param performanceAppraisalObjectsDTO     对象DTO
     * @param performanceAppraisalObjectsDTOList 对象List
     * @param appraisalStatus                    考核状态 待制定
     * @param appraisalStatus1                   考核状态 草稿
     */
    private void updateDevelopAppraisal(Long performanceAppraisalId, PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList, int appraisalStatus, int appraisalStatus1) {
        PerformanceAppraisalDTO appraisalDTO = new PerformanceAppraisalDTO();
        appraisalDTO.setPerformanceAppraisalId(performanceAppraisalId);
        if (performanceAppraisalObjectsDTO.getIsSubmit() == 1) {
            if (performanceAppraisalObjectsDTOList.size() == 1) {
                appraisalDTO.setAppraisalStatus(appraisalStatus);
                updatePerformanceAppraisal(appraisalDTO);
            } else if (performanceAppraisalObjectsDTOList.size() > 1) {
                int submitSum = 1;
                for (PerformanceAppraisalObjectsDTO appraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                    if (appraisalObjectsDTO.getAppraisalObjectStatus() > appraisalStatus1) {
                        submitSum += 1;
                    }
                }
                if (submitSum == performanceAppraisalObjectsDTOList.size()) {
                    appraisalDTO.setAppraisalStatus(appraisalStatus);
                    updatePerformanceAppraisal(appraisalDTO);
                }
            }
        }
    }

    /**
     * 查询绩效考核表列表-组织-评议
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    @DataScope(businessAlias = "pa")
    @Override
    public TableDataInfo selectOrgAppraisalReviewList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        Integer pageNum = performanceAppraisalObjectsDTO.getPageNum();
        Integer pageSize = performanceAppraisalObjectsDTO.getPageSize();
        PerformanceAppraisal appraisalDTO = new PerformanceAppraisal();
        appraisalDTO.setAppraisalObject(1);
        Map<String, Object> params = performanceAppraisalObjectsDTO.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        appraisalDTO.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(appraisalDTO);
        if (StringUtils.isEmpty(performanceAppraisalDTOS)) {
            return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        List<Long> performanceAppraisalIds = performanceAppraisalDTOS.stream().map(PerformanceAppraisalDTO::getPerformanceAppraisalId).collect(Collectors.toList());
        Integer appraisalObjectStatus = performanceAppraisalObjectsDTO.getAppraisalObjectStatus();
        List<Integer> appraisalObjectStatuses = new ArrayList<>();
        if (StringUtils.isNull(appraisalObjectStatus)) {
            appraisalObjectStatuses.add(3);
            appraisalObjectStatuses.add(4);
        } else {
            appraisalObjectStatuses.add(appraisalObjectStatus);
        }
        performanceAppraisalObjectsDTO.setAppraisalObjectStatusList(appraisalObjectStatuses);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalIds(performanceAppraisalIds);
        Map<String, Object> params2 = new HashMap<>();
        for (String key : params.keySet()) {
            switch (key) {
                case "employeeDepartmentIdEqual":
                    params2.put("employeeDepartmentIdEqual", params.get("employeeDepartmentIdEqual"));
                    break;
                case "employeeDepartmentIdNotEqual":
                    params2.put("employeeDepartmentIdNotEqual", params.get("employeeDepartmentIdNotEqual"));
                    break;
                case "employeePostIdEqual":
                    params2.put("employeePostIdEqual", params.get("employeePostIdEqual"));
                    break;
                case "employeePostIdNotEqual":
                    params2.put("employeePostIdNotEqual", params.get("employeePostIdNotEqual"));
                    break;
            }
        }
        if (StringUtils.isNotEmpty(params2)) {
            List<DepartmentDTO> departmentDTOS = depAdvancedSearch(params2);
            if (StringUtils.isEmpty(departmentDTOS)) {
                return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
            }
            List<Long> departmentIds = departmentDTOS.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
            params.put("departmentIds", departmentIds);
        }
        performanceAppraisalObjectsDTO.setParams(params);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalMapper.selectOrgAppraisalObjectList(performanceAppraisalObjectsDTO);
        this.handleResultOfPerformanceAppraisalObjects(performanceAppraisalObjectsDTOList);
        List<PerformanceAppraisalObjectsDTO> partition;
        if (pageNum > PageUtil.totalPage(performanceAppraisalObjectsDTOList.size(), pageSize)) {
            partition = new ArrayList<>();
        } else {
            partition = ListUtils.partition(performanceAppraisalObjectsDTOList, pageSize).get(pageNum - 1);
        }
        return PageUtils.tableDataInfo(HttpStatus.SUCCESS, partition, performanceAppraisalObjectsDTOList.size());
    }

    /**
     * 查询绩效考核表列表-个人-评议
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    @DataScope(businessAlias = "pa")
    @Override
    public TableDataInfo selectPerAppraisalReviewList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        Integer pageNum = performanceAppraisalObjectsDTO.getPageNum();
        Integer pageSize = performanceAppraisalObjectsDTO.getPageSize();
        List<Long> performanceAppraisalIds = new ArrayList<>();
        PerformanceAppraisal appraisalDTO = new PerformanceAppraisal();
        appraisalDTO.setAppraisalObject(2);
        Map<String, Object> params = performanceAppraisalObjectsDTO.getParams();
        if (StringUtils.isNull(params)) {
            params = new HashMap<>();
        }
        appraisalDTO.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(appraisalDTO);
        if (StringUtils.isEmpty(performanceAppraisalDTOS)) {
            return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDTOS) {
            performanceAppraisalIds.add(performanceAppraisalDTO.getPerformanceAppraisalId());
        }
        Integer appraisalObjectStatus = performanceAppraisalObjectsDTO.getAppraisalObjectStatus();
        List<Integer> appraisalObjectStatuses = new ArrayList<>();
        if (StringUtils.isNull(appraisalObjectStatus)) {
            appraisalObjectStatuses.add(3);
            appraisalObjectStatuses.add(4);
        } else {
            appraisalObjectStatuses.add(appraisalObjectStatus);
        }
        performanceAppraisalObjectsDTO.setAppraisalObjectStatusList(appraisalObjectStatuses);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalIds(performanceAppraisalIds);
        Map<String, Object> params2 = new HashMap<>();

        for (String key : params.keySet()) {
            switch (key) {
                case "employeeNameEqual":
                    params2.put("employeeNameEqual", params.get("employeeNameEqual"));
                    break;
                case "employeeNameNotEqual":
                    params2.put("employeeNameNotEqual", params.get("employeeNameNotEqual"));
                    break;
                case "employeeCodeEqual":
                    params2.put("employeeCodeEqual", params.get("employeeCodeEqual"));
                    break;
                case "employeeCodeNotEqual":
                    params2.put("employeeCodeNotEqual", params.get("employeeCodeNotEqual"));
                    break;
                case "employeeDepartmentIdEqual":
                    params2.put("employeeDepartmentIdEqual", params.get("employeeDepartmentIdEqual"));
                    break;
                case "employeeDepartmentIdNotEqual":
                    params2.put("employeeDepartmentIdNotEqual", params.get("employeeDepartmentIdNotEqual"));
                    break;
                case "employeePostIdEqual":
                    params2.put("employeePostIdEqual", params.get("employeePostIdEqual"));
                    break;
                case "employeePostIdNotEqual":
                    params2.put("employeePostIdNotEqual", params.get("employeePostIdNotEqual"));
                    break;
            }
        }
        if (StringUtils.isNotEmpty(params2)) {
            List<EmployeeDTO> employeeDTOS = empAdvancedSearch(params2);
            if (StringUtils.isEmpty(employeeDTOS)) {
                return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
            }
            List<Long> employeeIds = employeeDTOS.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
            params.put("employeeIds", employeeIds);
        }
        performanceAppraisalObjectsDTO.setParams(params);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalMapper.selectOrgAppraisalObjectList(performanceAppraisalObjectsDTO);
        this.handleResultOfPerformanceAppraisalObjects(performanceAppraisalObjectsDTOList);
        List<PerformanceAppraisalObjectsDTO> partition;
        if (pageNum > PageUtil.totalPage(performanceAppraisalObjectsDTOList.size(), pageSize)) {
            partition = new ArrayList<>();
        } else {
            partition = ListUtils.partition(performanceAppraisalObjectsDTOList, pageSize).get(pageNum - 1);
        }
        return PageUtils.tableDataInfo(HttpStatus.SUCCESS, partition, performanceAppraisalObjectsDTOList.size());
    }

    /**
     * 查询组织绩效考核表详情-评议-组织
     *
     * @param performAppraisalObjectsId 绩效考核对象ID
     * @return 绩效考核DTO
     */
    @Override
    public PerformanceAppraisalObjectsDTO selectOrgAppraisalReviewById(Long performAppraisalObjectsId) {
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = performanceAppraisalMapper.selectOrgAppraisalObjectByObjectId(performAppraisalObjectsId);
        setObjectFieldName(performanceAppraisalObjectsDTO);
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performAppraisalObjectsId);
        if (StringUtils.isEmpty(performanceAppraisalItemsDTOS)) {
            return performanceAppraisalObjectsDTO;
        }
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsDTOS) {
            Integer examineDirection = performanceAppraisalItemsDTO.getExamineDirection();
            countScore(performanceAppraisalItemsDTO, examineDirection);
        }
        setEvaluateValue(performanceAppraisalObjectsDTO, performanceAppraisalItemsDTOS);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalItemsDTOS(performanceAppraisalItemsDTOS);
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 查询组织绩效考核表详情-评议-个人
     *
     * @param performAppraisalObjectsId 绩效考核对象ID
     * @return 绩效考核表
     */
    @Override
    public PerformanceAppraisalObjectsDTO selectPerAppraisalReviewById(Long performAppraisalObjectsId) {
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = performanceAppraisalMapper.selectOrgAppraisalObjectByObjectId(performAppraisalObjectsId);
        setObjectFieldName(performanceAppraisalObjectsDTO);
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performAppraisalObjectsId);
        if (StringUtils.isEmpty(performanceAppraisalItemsDTOS)) {
            return performanceAppraisalObjectsDTO;
        }
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsDTOS) {
            Integer examineDirection = performanceAppraisalItemsDTO.getExamineDirection();
            countScore(performanceAppraisalItemsDTO, examineDirection);
        }
        setEvaluateValue(performanceAppraisalObjectsDTO, performanceAppraisalItemsDTOS);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalItemsDTOS(performanceAppraisalItemsDTOS);
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 给指标列表赋值
     *
     * @param performanceAppraisalObjectsDTO 对象DTO
     * @param performanceAppraisalItemsDTOS  指标列表
     */
    private void setEvaluateValue(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS) {
        Integer cycleType = performanceAppraisalObjectsDTO.getCycleType();
        Integer cycleNumber = performanceAppraisalObjectsDTO.getCycleNumber();
        Integer evaluationType = performanceAppraisalObjectsDTO.getEvaluationType();
        List<Long> performanceAppraisalItemIds = performanceAppraisalItemsDTOS.stream().map(PerformanceAppraisalItemsDTO::getPerformAppraisalItemsId).collect(Collectors.toList());
        List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOList =
                performAppraisalEvaluateService.selectPerformAppraisalEvaluateByPerformAppraisalItemIds(performanceAppraisalItemIds);
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsDTOS) {
            List<Map<String, Object>> evaluateList = new ArrayList<>();
            Long performAppraisalItemsId = performanceAppraisalItemsDTO.getPerformAppraisalItemsId();
            List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOS = performAppraisalEvaluateDTOList.stream().filter(p ->
                    p.getPerformAppraisalItemsId().equals(performAppraisalItemsId)).collect(Collectors.toList());
            for (PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO : performAppraisalEvaluateDTOS) {
                if (cycleType == 4) {//年度
                    if (evaluationType == 3) {
                        if (performAppraisalEvaluateDTO.getEvaluateNumber() == 1) {
                            setTimeValue(performAppraisalEvaluateDTO, "上半年", evaluateList);
                        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 2) {
                            setTimeValue(performAppraisalEvaluateDTO, "下半年", evaluateList);
                        }
                    } else if (evaluationType == 2) {//季度
                        setQuarterValue(evaluateList, performAppraisalEvaluateDTO);
                    } else if (evaluationType == 1) {//月度
                        setMonthValue(evaluateList, performAppraisalEvaluateDTO);
                    }
                } else if (cycleType == 3) {//半年度
                    if (evaluationType == 2) {//季度
                        setQuarterValue(evaluateList, performAppraisalEvaluateDTO);
                    } else if (evaluationType == 1) {//月度
                        setMonthValue(evaluateList, performAppraisalEvaluateDTO);
                    }
                } else if (cycleType == 2) {// 季度
                    if (evaluationType == 1) {//月度
                        setMonthValue(evaluateList, performAppraisalEvaluateDTO);
                    }
                }
            }
            Map<String, Object> sumMap = new HashMap<>();
            if (StringUtils.isNotEmpty(evaluateList)) {
                sumMap.put("value", performanceAppraisalItemsDTO.getActualValue());
                sumMap.put("name", "总计");
                evaluateList.add(sumMap);
            }
            performanceAppraisalItemsDTO.setEvaluateList(evaluateList);
        }
    }

    /**
     * 月份赋值
     *
     * @param evaluateList                实际值列表
     * @param performAppraisalEvaluateDTO 实际DTO
     */
    private void setQuarterValue(List<Map<String, Object>> evaluateList, PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO) {
        if (performAppraisalEvaluateDTO.getEvaluateNumber() == 1) {
            setTimeValue(performAppraisalEvaluateDTO, "一季度", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 2) {
            setTimeValue(performAppraisalEvaluateDTO, "二季度", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 3) {
            setTimeValue(performAppraisalEvaluateDTO, "三季度", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 4) {
            setTimeValue(performAppraisalEvaluateDTO, "四季度", evaluateList);
        }
    }

    /**
     * 月份赋值
     *
     * @param evaluateList                实际值列表
     * @param performAppraisalEvaluateDTO 实际DTO
     */
    private void setMonthValue(List<Map<String, Object>> evaluateList, PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO) {
        if (performAppraisalEvaluateDTO.getEvaluateNumber() == 1) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 2) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 3) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 4) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 5) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 6) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 7) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 8) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 9) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 10) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 11) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        } else if (performAppraisalEvaluateDTO.getEvaluateNumber() == 12) {
            setTimeValue(performAppraisalEvaluateDTO, performAppraisalEvaluateDTO.getEvaluateNumber() + "月", evaluateList);
        }
    }

    /**
     * 赋值
     *
     * @param performAppraisalEvaluateDTO 实际值dto
     * @param name                        什么名
     * @param evaluateList                实际值列表
     */
    private void setTimeValue(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO, String
            name, List<Map<String, Object>> evaluateList) {
        Map<String, Object> evaluateMap = new HashMap<>();
        evaluateMap.put("performAppraisalEvaluateId", performAppraisalEvaluateDTO.getPerformAppraisalEvaluateId());
        evaluateMap.put("value", performAppraisalEvaluateDTO.getActualValue());
        evaluateMap.put("name", name);
        evaluateList.add(evaluateMap);
    }

    /**
     * 计算分数
     *
     * @param performanceAppraisalItemsDTO 评议dto
     * @param examineDirection             分数
     *                                     1) 若指标的考核方向为正向，则为实际值/目标值*权重*100，实际值/目标值大于1.2时，按照1.2取值。
     *                                     2) 若指标的考核方向为负向，则为目标值/实际值*权重*100，实际值为0时分数为0。
     */
    private static void countScore(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO, Integer
            examineDirection) {
        if (StringUtils.isNotNull(examineDirection)) {
            if (examineDirection == 1) {// 正向
                BigDecimal targetValue = performanceAppraisalItemsDTO.getTargetValue();
                if (StringUtils.isNull(targetValue) || targetValue.compareTo(BigDecimal.ZERO) == 0) {
                    performanceAppraisalItemsDTO.setEvaluationScore(BigDecimal.ZERO);
                } else {
                    BigDecimal actualValue = performanceAppraisalItemsDTO.getActualValue();
                    if (StringUtils.isNull(actualValue)) actualValue = BigDecimal.ZERO;
                    BigDecimal weight = performanceAppraisalItemsDTO.getWeight();
                    if (StringUtils.isNull(weight)) weight = BigDecimal.ZERO;
                    BigDecimal proportion = actualValue.divide(targetValue, 2, RoundingMode.HALF_UP);
                    BigDecimal evaluationScore;
                    if (proportion.compareTo(BigDecimal.valueOf(1.2)) > 0)
                        evaluationScore = BigDecimal.valueOf(1.2).multiply(weight);
                    else
                        evaluationScore = proportion.multiply(weight);
                    performanceAppraisalItemsDTO.setEvaluationScore(evaluationScore);
                }
            } else {
                BigDecimal actualValue = performanceAppraisalItemsDTO.getActualValue();
                if (StringUtils.isNull(actualValue) || actualValue.compareTo(BigDecimal.ZERO) == 0) {
                    performanceAppraisalItemsDTO.setEvaluationScore(BigDecimal.ZERO);
                } else {
                    BigDecimal targetValue = performanceAppraisalItemsDTO.getTargetValue();
                    if (StringUtils.isNull(targetValue)) targetValue = BigDecimal.ZERO;
                    BigDecimal weight = performanceAppraisalItemsDTO.getWeight();
                    if (StringUtils.isNull(weight)) weight = BigDecimal.ZERO;
                    BigDecimal proportion = targetValue.divide(actualValue, 2, RoundingMode.HALF_UP);
                    BigDecimal evaluationScore;
                    if (proportion.compareTo(BigDecimal.valueOf(1.2)) > 0)
                        evaluationScore = BigDecimal.valueOf(1.2).multiply(weight);
                    else
                        evaluationScore = proportion.multiply(weight);
                    performanceAppraisalItemsDTO.setEvaluationScore(evaluationScore);
                }
            }
        }
    }

    /**
     * 编辑组织绩效考核评议表 -评议
     *
     * @param performanceAppraisalObjectsDTO 考核对象
     * @return 考核对象DTO
     */
    @Override
    @Transactional
    public PerformanceAppraisalObjectsDTO updateOrgReviewPerformanceAppraisal(PerformanceAppraisalObjectsDTO
                                                                                      performanceAppraisalObjectsDTO) {
        Long performAppraisalObjectsId = performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId();
        if (StringUtils.isNull(performAppraisalObjectsId)) {
            throw new ServiceException("绩效考核对象ID不能为空");
        }
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTOByObjectId = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
        if (StringUtils.isNull(performanceAppraisalObjectsDTOByObjectId)) {
            throw new ServiceException("当前绩效考核对象已不存在");
        }
        Long performanceAppraisalId = performanceAppraisalObjectsDTOByObjectId.getPerformanceAppraisalId();
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        // 更新绩效考核任务
        updateDevelopAppraisal(performanceAppraisalId, performanceAppraisalObjectsDTO, performanceAppraisalObjectsDTOList, 3, 4);
        // 更新绩效考核对象
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsAfter = performanceAppraisalObjectsDTO.getPerformanceAppraisalItemsDTOS();
        BigDecimal evaluationScore = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(performanceAppraisalItemsAfter)) {
            for (PerformanceAppraisalItemsDTO appraisalItemsDTO : performanceAppraisalItemsAfter) {
                if (StringUtils.isNotNull(appraisalItemsDTO.getEvaluationScore()) && !appraisalItemsDTO.getEvaluationScore().equals(BigDecimal.ZERO)) {
                    evaluationScore = evaluationScore.add(appraisalItemsDTO.getEvaluationScore());
                }
            }
        }
        Long appraisalPrincipalId = performanceAppraisalObjectsDTO.getAppraisalPrincipalId();
        if (StringUtils.isNotNull(appraisalPrincipalId)) {
            List<Long> appraisalPrincipalIds = new ArrayList<>();
            appraisalPrincipalIds.add(appraisalPrincipalId);
            List<EmployeeDTO> employeeDTOS = getEmployeeDTOSByIds(appraisalPrincipalIds);
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (Objects.equals(appraisalPrincipalId, employeeDTO.getEmployeeId())) {
                    performanceAppraisalObjectsDTO.setAppraisalPrincipalName(employeeDTO.getEmployeeName());
                    break;
                }
            }
        }
        PerformanceAppraisalObjectsDTO performanceAppraisalObjects = new PerformanceAppraisalObjectsDTO();
        performanceAppraisalObjects.setEvaluationScore(evaluationScore);
        performanceAppraisalObjects.setPerformAppraisalObjectsId(performAppraisalObjectsId);
        performanceAppraisalObjects.setAppraisalPrincipalId(performanceAppraisalObjectsDTO.getAppraisalPrincipalId());
        performanceAppraisalObjects.setAppraisalPrincipalName(performanceAppraisalObjectsDTO.getAppraisalPrincipalName());
        if (performanceAppraisalObjectsDTO.getIsSubmit() == 0) {
            performanceAppraisalObjects.setAppraisalObjectStatus(4);
        } else if (performanceAppraisalObjectsDTO.getIsSubmit() == 1) {
            performanceAppraisalObjects.setAppraisalObjectStatus(5);
        }
        performanceAppraisalObjectsService.updatePerformanceAppraisalObjects(performanceAppraisalObjects);
        // 更新评议指标信息
        if (StringUtils.isEmpty(performanceAppraisalItemsAfter)) {
            return performanceAppraisalObjectsDTO;
        }
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemList = new ArrayList<>();
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsAfter) {
            PerformanceAppraisalItemsDTO appraisalItemsDTO = new PerformanceAppraisalItemsDTO();
            appraisalItemsDTO.setPerformAppraisalItemsId(performanceAppraisalItemsDTO.getPerformAppraisalItemsId());
            appraisalItemsDTO.setRemark(performanceAppraisalItemsDTO.getRemark());
            appraisalItemsDTO.setActualValue(performanceAppraisalItemsDTO.getActualValue());
            appraisalItemsDTO.setEvaluateList(performanceAppraisalItemsDTO.getEvaluateList());
            performanceAppraisalItemList.add(appraisalItemsDTO);
        }
        this.setPerformanceAppraisalEvaluate(performanceAppraisalItemList, performanceAppraisalObjectsDTO);
        performanceAppraisalItemsService.updatePerformanceAppraisalItemsS(performanceAppraisalItemList);
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 编辑个人绩效考核表 -评议
     *
     * @param performanceAppraisalObjectsDTO 考核对象
     * @return 结果
     */
    @Override
    public PerformanceAppraisalObjectsDTO updatePerReviewPerformanceAppraisal(PerformanceAppraisalObjectsDTO
                                                                                      performanceAppraisalObjectsDTO) {
        Long performAppraisalObjectsId = performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId();
        if (StringUtils.isNull(performAppraisalObjectsId)) {
            throw new ServiceException("绩效考核对象ID不能为空");
        }
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTOByObjectId = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
        if (StringUtils.isNull(performanceAppraisalObjectsDTOByObjectId)) {
            throw new ServiceException("当前绩效考核对象已不存在");
        }
        Long performanceAppraisalId = performanceAppraisalObjectsDTOByObjectId.getPerformanceAppraisalId();
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        // 更新绩效考核任务
        updateDevelopAppraisal(performanceAppraisalId, performanceAppraisalObjectsDTO, performanceAppraisalObjectsDTOList, 3, 4);
        // 更新绩效考核对象
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsAfter = performanceAppraisalObjectsDTO.getPerformanceAppraisalItemsDTOS();
        BigDecimal evaluationScore = BigDecimal.ZERO;
        if (StringUtils.isNotEmpty(performanceAppraisalItemsAfter)) {
            for (PerformanceAppraisalItemsDTO appraisalItemsDTO : performanceAppraisalItemsAfter) {
                if (StringUtils.isNotNull(appraisalItemsDTO.getEvaluationScore()) && !appraisalItemsDTO.getEvaluationScore().equals(BigDecimal.ZERO)) {
                    evaluationScore = evaluationScore.add(appraisalItemsDTO.getEvaluationScore());
                }
            }
        }
        Long appraisalPrincipalId = performanceAppraisalObjectsDTO.getAppraisalPrincipalId();
        if (StringUtils.isNotNull(appraisalPrincipalId)) {
            List<Long> appraisalPrincipalIds = new ArrayList<>();
            appraisalPrincipalIds.add(appraisalPrincipalId);
            List<EmployeeDTO> employeeDTOS = getEmployeeDTOSByIds(appraisalPrincipalIds);
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (Objects.equals(appraisalPrincipalId, employeeDTO.getEmployeeId())) {
                    performanceAppraisalObjectsDTO.setAppraisalPrincipalName(employeeDTO.getEmployeeName());
                    break;
                }
            }
        }
        PerformanceAppraisalObjectsDTO performanceAppraisalObjects = new PerformanceAppraisalObjectsDTO();
        performanceAppraisalObjects.setEvaluationScore(evaluationScore);
        performanceAppraisalObjects.setPerformAppraisalObjectsId(performAppraisalObjectsId);
        performanceAppraisalObjects.setAppraisalPrincipalId(performanceAppraisalObjectsDTO.getAppraisalPrincipalId());
        performanceAppraisalObjects.setAppraisalPrincipalName(performanceAppraisalObjectsDTO.getAppraisalPrincipalName());
        if (performanceAppraisalObjectsDTO.getIsSubmit() == 0) {
            performanceAppraisalObjects.setAppraisalObjectStatus(4);
        } else if (performanceAppraisalObjectsDTO.getIsSubmit() == 1) {
            performanceAppraisalObjects.setAppraisalObjectStatus(5);
        }
        performanceAppraisalObjectsService.updatePerformanceAppraisalObjects(performanceAppraisalObjects);
        // 更新评议指标信息
        if (StringUtils.isEmpty(performanceAppraisalItemsAfter)) {
            return performanceAppraisalObjectsDTO;
        }
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemList = new ArrayList<>();
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsAfter) {
            PerformanceAppraisalItemsDTO appraisalItemsDTO = new PerformanceAppraisalItemsDTO();
            appraisalItemsDTO.setPerformAppraisalItemsId(performanceAppraisalItemsDTO.getPerformAppraisalItemsId());
            appraisalItemsDTO.setRemark(performanceAppraisalItemsDTO.getRemark());
            appraisalItemsDTO.setActualValue(performanceAppraisalItemsDTO.getActualValue());
            appraisalItemsDTO.setEvaluateList(performanceAppraisalItemsDTO.getEvaluateList());
            performanceAppraisalItemList.add(appraisalItemsDTO);
        }
        performanceAppraisalItemsService.updatePerformanceAppraisalItemsS(performanceAppraisalItemList);
        this.setPerformanceAppraisalEvaluate(performanceAppraisalItemList, performanceAppraisalObjectsDTO);
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 更新实际值
     *
     * @param performanceAppraisalItemList   指标列表
     * @param performanceAppraisalObjectsDTO 对象
     */
    private void setPerformanceAppraisalEvaluate(List<PerformanceAppraisalItemsDTO> performanceAppraisalItemList, PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOS = new ArrayList<>();
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemList) {
            List<Map<String, Object>> evaluateList = performanceAppraisalItemsDTO.getEvaluateList();
            if (StringUtils.isNotEmpty(evaluateList)) {
                this.operateReviewEvaluation(evaluateList, performAppraisalEvaluateDTOS, performanceAppraisalObjectsDTO);
                Map<String, Object> lastMap = evaluateList.get(evaluateList.size() - 1);
                if (performanceAppraisalObjectsDTO.getIsSubmit() == 0) {
                    performanceAppraisalItemsDTO.setActualValue(StringUtils.isNull(lastMap.get("value")) ? null : new BigDecimal(lastMap.get("value").toString()));
                } else if (performanceAppraisalObjectsDTO.getIsSubmit() == 1) {
                    performanceAppraisalItemsDTO.setActualValue(StringUtils.isNull(lastMap.get("value")) ? BigDecimal.ZERO : new BigDecimal(lastMap.get("value").toString()));
                }
            }
        }
        if (StringUtils.isNotEmpty(performAppraisalEvaluateDTOS)) {
            performAppraisalEvaluateService.updatePerformAppraisalEvaluates(performAppraisalEvaluateDTOS);
        }
    }

    /**
     * 处理评议的实际值内容
     *
     * @param evaluateList                   实际值列表
     * @param performAppraisalEvaluateDTOS   实际值列表
     * @param performanceAppraisalObjectsDTO 总计
     */
    private void operateReviewEvaluation(List<Map<String, Object>> evaluateList, List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOS, PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        for (Map<String, Object> map : evaluateList) {
            if (StringUtils.isNotNull(map.get("name")) && map.get("name").toString().equals("总计")) {
                continue;
            }
            if (StringUtils.isNull(map.get("performAppraisalEvaluateId"))) {
                throw new ServiceException("数据异常 请联系管理员");
            }
            Long performAppraisalEvaluateId = Long.parseLong(map.get("performAppraisalEvaluateId").toString());
            PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO = new PerformAppraisalEvaluateDTO();
            performAppraisalEvaluateDTO.setPerformAppraisalEvaluateId(performAppraisalEvaluateId);
            if (performanceAppraisalObjectsDTO.getIsSubmit() == 0) {
                performAppraisalEvaluateDTO.setActualValue(StringUtils.isNull(map.get("value")) ? null : new BigDecimal(map.get("value").toString()));
            } else if (performanceAppraisalObjectsDTO.getIsSubmit() == 1) {
                performAppraisalEvaluateDTO.setActualValue(StringUtils.isNull(map.get("value")) ? BigDecimal.ZERO : new BigDecimal(map.get("value").toString()));
            }
            performAppraisalEvaluateDTOS.add(performAppraisalEvaluateDTO);
        }
    }

    /**
     * 评议撤回
     *
     * @param performAppraisalObjectsId 考核对象ID
     * @return int
     */
    @Override
    public int withdraw(Long performAppraisalObjectsId) {
        if (StringUtils.isNull(performAppraisalObjectsId)) {
            throw new ServiceException("绩效考核对象ID不能为空");
        }
        PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTOByObjectId = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
        if (StringUtils.isNull(performanceAppraisalObjectsDTOByObjectId)) {
            throw new ServiceException("当前绩效考核对象已不存在");
        }
        Long performanceAppraisalId = performanceAppraisalObjectsDTOByObjectId.getPerformanceAppraisalId();
        // 更新绩效考核任务
        PerformanceAppraisalDTO appraisalDTO = new PerformanceAppraisalDTO();
        appraisalDTO.setPerformanceAppraisalId(performanceAppraisalId);
        appraisalDTO.setAppraisalStatus(1);
        updatePerformanceAppraisal(appraisalDTO);
        //更新 对象表状态
        PerformanceAppraisalObjectsDTO performanceAppraisalObjects = new PerformanceAppraisalObjectsDTO();
        performanceAppraisalObjects.setPerformAppraisalObjectsId(performAppraisalObjectsId);
        performanceAppraisalObjects.setAppraisalObjectStatus(2);
        performanceAppraisalObjects.setEvaluationScore(null);
        performanceAppraisalObjectsService.withdrawPerformanceAppraisalObjects(performanceAppraisalObjects);
        // 更新评议指标信息
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOS = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performAppraisalObjectsId);
        if (StringUtils.isEmpty(performanceAppraisalItemsDTOS)) {
            return 1;
        }
        List<PerformanceAppraisalItemsDTO> itemsDTOList = new ArrayList<>();
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsDTOS) {
            PerformanceAppraisalItemsDTO itemsDTO = new PerformanceAppraisalItemsDTO();
            itemsDTO.setPerformAppraisalItemsId(performanceAppraisalItemsDTO.getPerformAppraisalItemsId());
            itemsDTOList.add(itemsDTO);
        }
        performanceAppraisalItemsService.withdrawPerformanceAppraisalItems(itemsDTOList);
        List<Long> performanceAppraisalItemIds = performanceAppraisalItemsDTOS.stream().map(PerformanceAppraisalItemsDTO::getPerformAppraisalItemsId).collect(Collectors.toList());
        List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOList = performAppraisalEvaluateService.selectPerformAppraisalEvaluateByPerformAppraisalItemIds(performanceAppraisalItemIds);
        if (StringUtils.isNotEmpty(performAppraisalEvaluateDTOList)) {
            List<PerformAppraisalEvaluateDTO> evaluateDTOS = new ArrayList<>();
            for (PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO : performAppraisalEvaluateDTOList) {
                PerformAppraisalEvaluateDTO appraisalEvaluateDTO = new PerformAppraisalEvaluateDTO();
                appraisalEvaluateDTO.setPerformAppraisalEvaluateId(performAppraisalEvaluateDTO.getPerformAppraisalEvaluateId());
                appraisalEvaluateDTO.setActualValue(null);
                evaluateDTOS.add(appraisalEvaluateDTO);
            }
            performAppraisalEvaluateService.updatePerformAppraisalEvaluates(evaluateDTOS);
        }
        return 1;
    }

    /**
     * 根据绩效等级ID集合查询绩效考核
     *
     * @param performanceRankIds 绩效等级ID集合
     * @return List
     */
    @Override
    public List<PerformanceAppraisalDTO> selectPerformanceAppraisalByRankIds(List<Long> performanceRankIds) {
        return performanceAppraisalMapper.selectPerformanceAppraisalByRankIds(performanceRankIds);
    }

    /**
     * 查询绩效考核表列表-组织-排名
     *
     * @param performanceAppraisalDTO 考核对象
     * @return List
     */
    @DataScope(businessAlias = "pa")
    @Override
    public List<PerformanceAppraisalDTO> selectOrgAppraisalRankingList(PerformanceAppraisalDTO
                                                                               performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setAppraisalObject(1);
        performanceAppraisal.setAppraisalStatus(3);
        Map<String, Object> params = performanceAppraisal.getParams();
        performanceAppraisal.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        this.handleResultOfPerformanceAppraisal(performanceAppraisalDTOS);
        for (PerformanceAppraisalDTO appraisalDTO : performanceAppraisalDTOS) {
            if (StringUtils.isNull(appraisalDTO.getFilingDate())) {
                appraisalDTO.setIsFiling(0);
            } else {
                appraisalDTO.setIsFiling(1);
            }
        }
        return performanceAppraisalDTOS;
    }

    /**
     * 查询绩效考核表列表-个人-排名
     *
     * @param performanceAppraisalDTO 考核对象
     * @return
     */
    @DataScope(businessAlias = "pa")
    @Override
    public List<PerformanceAppraisalDTO> selectPerAppraisalRankingList(PerformanceAppraisalDTO
                                                                               performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setAppraisalObject(2);
        performanceAppraisal.setAppraisalStatus(3);
        Map<String, Object> params = performanceAppraisal.getParams();
        performanceAppraisal.setParams(params);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        this.handleResultOfPerformanceAppraisal(performanceAppraisalDTOS);
        for (PerformanceAppraisalDTO appraisalDTO : performanceAppraisalDTOS) {
            if (StringUtils.isNull(appraisalDTO.getFilingDate())) {
                appraisalDTO.setIsFiling(0);
            } else {
                appraisalDTO.setIsFiling(1);
            }
        }
        return performanceAppraisalDTOS;
    }

    /**
     * 查询绩效考核详情-组织-排名
     *
     * @param performanceAppraisalId 考核ID
     * @return 考核任务DTO
     */
    @Override
    public PerformanceAppraisalDTO selectOrgAppraisalRankingById(Long performanceAppraisalId) {
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前考核任务已不存在");
        }
        if (!appraisal.getAppraisalObject().equals(1)) {
            throw new ServiceException("当前考核对象不是组织的");
        }
        setFieldName(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        HashMap<String, BigDecimal> performanceRankMap = new HashMap<>();
        Integer sum = 0;
        int appraisalRank = 1;//名次
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectsDTO.setRank(appraisalRank);
            sum = setRankMap(performanceRankMap, performanceAppraisalObjectsDTO, sum);
            appraisalRank++;
        }
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOList);
        // 添加考核比例统计
        PerformanceAppraisalDTO appraisalDTO = countObjectFactorRank(performanceAppraisalId, appraisal, performanceRankMap, sum);
        if (appraisalDTO != null) return appraisalDTO;
        return appraisal;
    }

    /**
     * 查询绩效考核详情-人员-排名
     *
     * @param performanceAppraisalId 考核ID
     * @return 结果
     */
    @Override
    public PerformanceAppraisalDTO selectPerAppraisalRankingById(Long performanceAppraisalId) {
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前考核任务已不存在");
        }
        if (!appraisal.getAppraisalObject().equals(2)) {
            throw new ServiceException("当前考核对象不是个人的");
        }
        setFieldName(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        HashMap<String, BigDecimal> performanceRankMap = new HashMap<>();
        Integer sum = 0;
        int appraisalRank = 1;//名次
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectsDTO.setRank(appraisalRank);
            sum = setRankMap(performanceRankMap, performanceAppraisalObjectsDTO, sum);
            appraisalRank++;
        }
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOList);
        // 添加考核比例统计
        PerformanceAppraisalDTO appraisalDTO = countObjectFactorRank(performanceAppraisalId, appraisal, performanceRankMap, sum);
        if (appraisalDTO != null) return appraisalDTO;
        return appraisal;
    }

    /**
     * 编辑绩效考核-组织-排名
     *
     * @param performanceAppraisalDTO 考核对象DTO
     * @return 考核任务DTO
     */
    @Override
    @Transactional
    public int updateOrgRankingPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO) {
        Integer isSubmit = performanceAppraisalDTO.getIsSubmit();
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = performanceAppraisalDTO.getPerformanceAppraisalObjectsDTOS();
        checkRankingUpdate(isSubmit, performanceAppraisalId, performanceAppraisalObjectsDTOS);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = new ArrayList<>();
        // 更新绩效考核任务
        updateRankOperate(isSubmit, performanceAppraisalId, performanceAppraisalObjectsDTOS, performanceAppraisalObjectsDTOList);
        return performanceAppraisalObjectsService.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsDTOList);
    }

    /**
     * 编辑绩效考核-个人-排名
     *
     * @param performanceAppraisalDTO 考核对象DTO
     * @return
     */
    @Override
    public int updatePerRankingPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO) {
        Integer isSubmit = performanceAppraisalDTO.getIsSubmit();
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = performanceAppraisalDTO.getPerformanceAppraisalObjectsDTOS();
        checkRankingUpdate(isSubmit, performanceAppraisalId, performanceAppraisalObjectsDTOS);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = new ArrayList<>();
        // 更新绩效考核任务
        updateRankOperate(isSubmit, performanceAppraisalId, performanceAppraisalObjectsDTOS, performanceAppraisalObjectsDTOList);
        return performanceAppraisalObjectsService.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsDTOList);
    }

    /**
     * 排名更新操作
     *
     * @param isSubmit                           是否提交
     * @param performanceAppraisalId             考核任务ID
     * @param performanceAppraisalObjectsDTOS    考核对象List - 前
     * @param performanceAppraisalObjectsDTOList 考核对象List - 后
     */
    private void updateRankOperate(Integer isSubmit, Long
            performanceAppraisalId, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList) {
        if (isSubmit == 1) {
            PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
            performanceAppraisal.setPerformanceAppraisalId(performanceAppraisalId);
            performanceAppraisal.setAppraisalStatus(4);
            performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalMapper.updatePerformanceAppraisal(performanceAppraisal);
        }
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            PerformanceAppraisalObjectsDTO objectsDTO = new PerformanceAppraisalObjectsDTO();
            objectsDTO.setPerformAppraisalObjectsId(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
            objectsDTO.setRemark(performanceAppraisalObjectsDTO.getRemark());
            objectsDTO.setAppraisalResultId(performanceAppraisalObjectsDTO.getAppraisalResultId());
            objectsDTO.setAppraisalResult(performanceAppraisalObjectsDTO.getAppraisalResult());
            objectsDTO.setAppraisalPrincipalId(performanceAppraisalObjectsDTO.getAppraisalPrincipalId());
            objectsDTO.setAppraisalPrincipalName(performanceAppraisalObjectsDTO.getAppraisalPrincipalName());
            performanceAppraisalObjectsDTOList.add(objectsDTO);
        }
    }

    /**
     * 更新校验
     *
     * @param isSubmit                        是否提交
     * @param performanceAppraisalId          考核ID
     * @param performanceAppraisalObjectsDTOS 考核对象集合
     */
    private void checkRankingUpdate(Integer isSubmit, Long
            performanceAppraisalId, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS) {
        if (StringUtils.isNull(isSubmit)) {
            throw new ServiceException("请添加是否提交");
        }
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("考核任务ID不可以为空");
        }
        PerformanceAppraisalDTO appraisalDTO = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisalDTO)) {
            throw new ServiceException("绩效考核任务不可以为空");
        }
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            if (StringUtils.isNull(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId())) {
                throw new ServiceException("绩效考核对象ID为空");
            }
        }
    }

    /**
     * 获取指标
     *
     * @param indicatorIds 指标ID集合
     */
    private List<IndicatorDTO> getIndicator(List<Long> indicatorIds) {
        R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorDTOS = indicatorR.getData();
        if (indicatorR.getCode() != 200) {
            throw new ServiceException(indicatorR.getMsg());
        }
        if (StringUtils.isEmpty(indicatorDTOS)) {
            throw new ServiceException("该指标已不存在 请检查指标配置");
        }
        return indicatorDTOS;
    }

    /**
     * 处理评议指标信息
     *
     * @param performanceAppraisalObjectsDTO  对象
     * @param performanceAppraisalItemsBefore 库值
     * @param performanceAppraisalItemsAfter  后来的
     */
    private void operateItemValue(PerformanceAppraisalObjectsDTO
                                          performanceAppraisalObjectsDTO, List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsBefore, List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsAfter) {
        // 交集
        List<PerformanceAppraisalItemsDTO> updatePerformanceAppraisalItem =
                performanceAppraisalItemsAfter.stream().filter(performanceAppraisalItemsDTO ->
                        performanceAppraisalItemsBefore.stream().map(PerformanceAppraisalItemsDTO::getIndicatorId)
                                .collect(Collectors.toList()).contains(performanceAppraisalItemsDTO.getIndicatorId())
                ).collect(Collectors.toList());
        // 差集 Before中After的补集
        List<PerformanceAppraisalItemsDTO> delPerformanceAppraisalItem =
                performanceAppraisalItemsBefore.stream().filter(performanceAppraisalItemsDTO ->
                        !performanceAppraisalItemsAfter.stream().map(PerformanceAppraisalItemsDTO::getIndicatorId)
                                .collect(Collectors.toList()).contains(performanceAppraisalItemsDTO.getIndicatorId())
                ).collect(Collectors.toList());
        // 差集 After中Before的补集
        List<PerformanceAppraisalItemsDTO> addPerformanceAppraisalItem =
                performanceAppraisalItemsAfter.stream().filter(performanceAppraisalItemsDTO ->
                        !performanceAppraisalItemsBefore.stream().map(PerformanceAppraisalItemsDTO::getIndicatorId)
                                .collect(Collectors.toList()).contains(performanceAppraisalItemsDTO.getIndicatorId())
                ).collect(Collectors.toList());
        List<PerformanceAppraisalItems> addPerformanceAppraisalItems = new ArrayList<>();
        try {
            if (StringUtils.isNotEmpty(delPerformanceAppraisalItem)) {
                List<Long> performanceAppraisalItems = new ArrayList<>();
                for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : delPerformanceAppraisalItem) {
                    performanceAppraisalItems.add(performanceAppraisalItemsDTO.getPerformAppraisalItemsId());
                }
                performanceAppraisalItemsService.logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsIds(performanceAppraisalItems);
            }
            if (StringUtils.isNotEmpty(addPerformanceAppraisalItem)) {
                addPerformanceAppraisalItems = performanceAppraisalItemsService.insertPerformanceAppraisalItemss(addPerformanceAppraisalItem);
            }
            if (StringUtils.isNotEmpty(updatePerformanceAppraisalItem)) {
                performanceAppraisalItemsService.updatePerformanceAppraisalItemsS(updatePerformanceAppraisalItem);
            }
        } catch (ServiceException e) {
            throw new ServiceException("数据更新失败");
        }
        if (performanceAppraisalObjectsDTO.getIsSubmit() == 1) {
            this.operateEvaluation(updatePerformanceAppraisalItem, addPerformanceAppraisalItems, delPerformanceAppraisalItem, performanceAppraisalObjectsDTO);
        }
    }

    /**
     * 处理
     *
     * @param updatePerformanceAppraisalItem 更新
     * @param addPerformanceAppraisalItem    新增
     * @param delPerformanceAppraisalItem    删除
     * @param performanceAppraisalObjectsDTO 对象表
     */
    private void operateEvaluation
    (List<PerformanceAppraisalItemsDTO> updatePerformanceAppraisalItem, List<PerformanceAppraisalItems> addPerformanceAppraisalItem,
     List<PerformanceAppraisalItemsDTO> delPerformanceAppraisalItem, PerformanceAppraisalObjectsDTO
             performanceAppraisalObjectsDTO) {
        Integer cycleType = performanceAppraisalObjectsDTO.getCycleType();
        Integer evaluationType = performanceAppraisalObjectsDTO.getEvaluationType();
        Integer cycleNumber = performanceAppraisalObjectsDTO.getCycleNumber();
        Long performAppraisalObjectsId = performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId();
        if (StringUtils.isNotEmpty(addPerformanceAppraisalItem)) {
            List<PerformAppraisalEvaluateDTO> addPerformanceEvaluationList = new ArrayList<>();
            for (PerformanceAppraisalItems performanceAppraisalItems : addPerformanceAppraisalItem) {
                setCycleActualValue(performAppraisalObjectsId, addPerformanceEvaluationList, cycleType, evaluationType, cycleNumber, performanceAppraisalItems.getPerformAppraisalItemsId());
            }
            if (StringUtils.isNotEmpty(addPerformanceEvaluationList)) {
                performAppraisalEvaluateService.insertPerformAppraisalEvaluates(addPerformanceEvaluationList);
            }
        }
        if (StringUtils.isNotEmpty(updatePerformanceAppraisalItem)) {
            List<Long> performanceAppraisalItemIds = updatePerformanceAppraisalItem.stream().map(PerformanceAppraisalItemsDTO::getPerformAppraisalItemsId).collect(Collectors.toList());
            List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDTOBefore = performAppraisalEvaluateService.selectPerformAppraisalEvaluateByPerformAppraisalItemIds(performanceAppraisalItemIds);
            List<PerformAppraisalEvaluateDTO> delPerformanceEvaluationList = new ArrayList<>();
            List<PerformAppraisalEvaluateDTO> addPerformanceEvaluationList = new ArrayList<>();
            for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : updatePerformanceAppraisalItem) {
                List<PerformAppraisalEvaluateDTO> performanceEvaluationListAfter = new ArrayList<>();
                setCycleActualValue(performAppraisalObjectsId, performanceEvaluationListAfter, cycleType, evaluationType, cycleNumber, performanceAppraisalItemsDTO.getPerformAppraisalItemsId());
                List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateBefore = performAppraisalEvaluateDTOBefore.stream().filter(before ->
                        before.getPerformAppraisalItemsId().equals(performanceAppraisalItemsDTO.getPerformAppraisalItemsId())).collect(Collectors.toList());
                delPerformanceEvaluationList.addAll(performAppraisalEvaluateBefore.stream().filter(before ->
                        !performanceEvaluationListAfter.stream().map(PerformAppraisalEvaluateDTO::getEvaluateNumber)
                                .collect(Collectors.toList()).contains(before.getEvaluateNumber())).collect(Collectors.toList()));
                addPerformanceEvaluationList.addAll(performanceEvaluationListAfter.stream().filter(after ->
                        !performAppraisalEvaluateBefore.stream().map(PerformAppraisalEvaluateDTO::getEvaluateNumber)
                                .collect(Collectors.toList()).contains(after.getEvaluateNumber())).collect(Collectors.toList()));
            }
            if (StringUtils.isNotEmpty(addPerformanceEvaluationList)) {
                performAppraisalEvaluateService.insertPerformAppraisalEvaluates(addPerformanceEvaluationList);
            }
            if (StringUtils.isNotEmpty(delPerformanceEvaluationList)) {
                List<Long> delPerformanceEvaluationIds = delPerformanceEvaluationList.stream().map(PerformAppraisalEvaluateDTO::getPerformAppraisalEvaluateId).collect(Collectors.toList());
                performAppraisalEvaluateService.logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(delPerformanceEvaluationIds);
            }
        }
        if (StringUtils.isNotEmpty(delPerformanceAppraisalItem)) {
            List<Long> delPerformanceAppraisalItemIds = delPerformanceAppraisalItem.stream().map(PerformanceAppraisalItemsDTO::getPerformAppraisalItemsId).collect(Collectors.toList());
            List<PerformAppraisalEvaluateDTO> delPerformAppraisalEvaluateDTOS = performAppraisalEvaluateService.selectPerformAppraisalEvaluateByPerformAppraisalItemIds(delPerformanceAppraisalItemIds);
            List<Long> delPerformanceAppraisalEvaluateIds = delPerformAppraisalEvaluateDTOS.stream().map(PerformAppraisalEvaluateDTO::getPerformAppraisalEvaluateId).collect(Collectors.toList());
            performAppraisalEvaluateService.logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(delPerformanceAppraisalEvaluateIds);
        }
    }

    /**
     * 赋值
     *
     * @param performAppraisalObjectsId    对象ID
     * @param addPerformanceEvaluationList 新增的列表
     * @param cycleType                    周期
     * @param evaluationType               平移类型
     * @param cycleNumber                  周期
     * @param performAppraisalItemsId      指标iD
     */
    private static void setCycleActualValue(Long
                                                    performAppraisalObjectsId, List<PerformAppraisalEvaluateDTO> addPerformanceEvaluationList,
                                            Integer cycleType, Integer evaluationType, Integer cycleNumber, Long performAppraisalItemsId) {
        if (cycleType == 4) {//年度
            if (evaluationType == 3) {
                setEvaluateValue(1, 2, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
            } else if (evaluationType == 2) {
                setEvaluateValue(1, 4, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
            } else if (evaluationType == 1) {
                setEvaluateValue(1, 12, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
            }
        } else if (cycleType == 3) {//半年度
            if (cycleNumber == 1) {
                if (evaluationType == 2) {
                    setEvaluateValue(1, 2, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
                } else if (evaluationType == 1) {
                    setEvaluateValue(1, 6, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
                }
            } else if (cycleNumber == 2) {
                if (evaluationType == 2) {
                    setEvaluateValue(3, 4, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
                } else if (evaluationType == 1) {
                    setEvaluateValue(7, 12, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
                }
            }
        } else if (cycleType == 2) {//季度
            if (evaluationType == 1) {
                if (cycleNumber == 1) {
                    setEvaluateValue(1, 3, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
                } else if (cycleNumber == 2) {
                    setEvaluateValue(4, 6, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
                } else if (cycleNumber == 3) {
                    setEvaluateValue(7, 9, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
                } else if (cycleNumber == 4) {
                    setEvaluateValue(10, 12, performAppraisalItemsId, performAppraisalObjectsId, addPerformanceEvaluationList);
                }
            }
        }
    }

    /**
     * @param start                        开始时间
     * @param end                          结束时间
     * @param performAppraisalItemsId      绩效评议id
     * @param performAppraisalObjectsId    绩效对象ID
     * @param addPerformanceEvaluationList 绩效评议新增
     */
    private static void setEvaluateValue(int start, int end, Long performAppraisalItemsId, Long
            performAppraisalObjectsId, List<PerformAppraisalEvaluateDTO> addPerformanceEvaluationList) {
        for (int i = start; i < end + 1; i++) {
            PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO = new PerformAppraisalEvaluateDTO();
            performAppraisalEvaluateDTO.setEvaluateNumber(i);
            performAppraisalEvaluateDTO.setPerformAppraisalItemsId(performAppraisalItemsId);
            performAppraisalEvaluateDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
            addPerformanceEvaluationList.add(performAppraisalEvaluateDTO);
        }
    }

    /**
     * 为字段命名
     *
     * @param appraisalObjectsDTO 考核对象任务
     */
    private static void setObjectFieldName(PerformanceAppraisalObjectsDTO appraisalObjectsDTO) {
        // 考核周期类型/考核周期
        if (StringUtils.isNotNull(appraisalObjectsDTO)) {
            if (StringUtils.isNotNull(appraisalObjectsDTO.getCycleType()) && StringUtils.isNotNull(appraisalObjectsDTO.getEvaluationType())) {
                switch (appraisalObjectsDTO.getCycleType()) {
                    case 1:
                        appraisalObjectsDTO.setCycleTypeName("月度");
                        appraisalObjectsDTO.setCycleNumberName(appraisalObjectsDTO.getCycleNumber().toString() + "月");
                        break;
                    case 2:
                        appraisalObjectsDTO.setCycleTypeName("季度");
                        if (appraisalObjectsDTO.getCycleNumber() == 1) {
                            appraisalObjectsDTO.setCycleNumberName("一季度");
                        } else if (appraisalObjectsDTO.getCycleNumber() == 2) {
                            appraisalObjectsDTO.setCycleNumberName("二季度");
                        } else if (appraisalObjectsDTO.getCycleNumber() == 3) {
                            appraisalObjectsDTO.setCycleNumberName("三季度");
                        } else if (appraisalObjectsDTO.getCycleNumber() == 4) {
                            appraisalObjectsDTO.setCycleNumberName("四季度");
                        }
                        break;
                    case 3:
                        appraisalObjectsDTO.setCycleTypeName("半年度");
                        if (appraisalObjectsDTO.getCycleNumber() == 1) {
                            appraisalObjectsDTO.setCycleNumberName("上半年");
                        } else {
                            appraisalObjectsDTO.setCycleNumberName("下半年");
                        }
                        break;
                    case 4:
                        appraisalObjectsDTO.setCycleTypeName("年度");
                        appraisalObjectsDTO.setCycleNumberName("整年度");
                        break;
                }
                switch (appraisalObjectsDTO.getEvaluationType()) {
                    case 1:
                        appraisalObjectsDTO.setEvaluationTypeName("月度");
                        break;
                    case 2:
                        appraisalObjectsDTO.setEvaluationTypeName("季度");
                        break;
                    case 3:
                        appraisalObjectsDTO.setEvaluationTypeName("半年度");
                        break;
                    case 4:
                        appraisalObjectsDTO.setEvaluationTypeName("年度");
                        break;
                }
            }
            if (StringUtils.isNotNull(appraisalObjectsDTO.getAppraisalObjectStatus())) {
                // 考核阶段
                switch (appraisalObjectsDTO.getAppraisalObjectStatus()) {
                    case 1:
                        appraisalObjectsDTO.setAppraisalObjectStatusName("未制定");
                        break;
                    case 2:
                    case 4:
                        appraisalObjectsDTO.setAppraisalObjectStatusName("草稿");
                        break;
                    case 3:
                        appraisalObjectsDTO.setAppraisalObjectStatusName("未评议");
                        break;
                    case 5:
                        appraisalObjectsDTO.setAppraisalObjectStatusName("未排名");
                        break;
                }
            }
        }
    }

}

