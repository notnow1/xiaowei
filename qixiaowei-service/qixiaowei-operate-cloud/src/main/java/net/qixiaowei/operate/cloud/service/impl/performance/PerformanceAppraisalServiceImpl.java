package net.qixiaowei.operate.cloud.service.impl.performance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.dto.performance.*;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalExcel;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalMapper;
import net.qixiaowei.operate.cloud.service.performance.*;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
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
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private IPerformanceAppraisalColumnsService performanceAppraisalColumnsService;

    @Autowired
    private IPerformancePercentageService performancePercentageService;

    @Autowired
    private IPerformanceRankService performanceRankService;

    @Autowired
    private IPerformAppraisalObjectSnapService performAppraisalObjectSnapService;

    @Autowired
    private IPerformanceAppraisalItemsService performanceAppraisalItemsService;

    @Autowired
    private RemoteIndicatorService indicatorService;

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
    public PerformanceAppraisalDTO selectPerAppraisalRankByDTO(Map<String, List<Object>> performanceAppraisalDTO) {
        Long performanceAppraisalId = (Long) performanceAppraisalDTO.get("performanceAppraisalId").get(0);
        List<Object> departmentIds = performanceAppraisalDTO.get("departmentIds");
        List<Object> postIds = performanceAppraisalDTO.get("postIds");
        List<Object> rankNames = performanceAppraisalDTO.get("rankNames");
        Map<String, List<String>> idMaps = new HashMap<>();
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("请输入绩效考核ID");
        }
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
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByIds(appraisalObjectsIds, performanceAppraisalId);
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
            throw new ServiceException("绩效等级为空 请检查");
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
    @Override
    public List<PerformanceAppraisalDTO> selectPerformanceAppraisalList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        if (StringUtils.isEmpty(performanceAppraisalDTOS)) {
            return performanceAppraisalDTOS;
        }
        performanceAppraisalDTOS.forEach(PerformanceAppraisalServiceImpl::setFieldName);
        return performanceAppraisalDTOS;
    }

    /**
     * 查询个人绩效归档
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 绩效考核表
     */
    @Override
    public List<PerformanceAppraisalDTO> selectPerAppraisalArchiveList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        performanceAppraisal.setAppraisalObject(2);
        performanceAppraisal.setAppraisalStatus(4);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        performanceAppraisalDTOS.forEach(PerformanceAppraisalServiceImpl::setFieldName);
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
    @Override
    public List<PerformanceAppraisalDTO> selectOrgAppraisalArchiveList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setAppraisalObject(1);
        performanceAppraisal.setAppraisalStatus(4);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        performanceAppraisalDTOS.forEach(PerformanceAppraisalServiceImpl::setFieldName);
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
                    appraisal.setCycleNumberName(appraisal.getCycleNumber().toString() + "季度");
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
                    appraisal.setCycleNumberName("一整年");
                    break;
            }
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
        performanceAppraisalDTO.setPerformanceRankName(performanceRankDTO.getPerformanceRankName());
        // 周期性考核标记:0否;1是
        if (performanceAppraisalDTO.getCycleFlag().equals(1)) {
            Integer cycleType = performanceAppraisalDTO.getCycleType();
            int monthNow;
            int quarterNow;
            int yearNow = DateUtils.getYear();
            if (appraisalYear < yearNow) {
                throw new ServiceException("过去的年份无法");
            } else if (appraisalYear > yearNow) {
                monthNow = 1;
                quarterNow = 1;
                performanceAppraisalDTO.setCycleNumber(1);
            } else {
                monthNow = DateUtils.getMonth();
                quarterNow = DateUtils.getQuarter();
            }
            Integer x = operateTime(performanceAppraisalDTO, performanceAppraisalObjectsDTOS, appraisalObject,
                    appraisalYear, appraisalFlow, departmentData, employeeData, cycleType, monthNow, quarterNow);
            if (x != null) return x;
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
        performanceAppraisalObjectsService.insertPerformanceAppraisalObjectss(performanceAppraisalObjectsDTOList);
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
        if (StringUtils.isNotNull(cycleType)) {
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
                    performanceAppraisalObjectsService.insertPerformanceAppraisalObjectss(performanceAppraisalObjectsDTOList);
                    List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS = setSnapValue(appraisalObject, performanceAppraisalObjectsDTOList);
                    return performAppraisalObjectSnapService.insertPerformAppraisalObjectSnaps(performAppraisalObjectSnapDTOS);
            }
        }
        return null;
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
        performanceAppraisalObjectsService.insertPerformanceAppraisalObjectss(performanceAppraisalObjectsDTOList);
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
            throw new ServiceException("考核任务名称重复");
        }
        // 人员-组织 对象
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = performanceAppraisalDTO.getPerformanceAppraisalObjectsDTOS();
        List<Long> performAppraisalObjectsIds = new ArrayList<>();
        // 顺便排序
        int sort = 0;
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOS)) {
            throw new ServiceException("请选择考核任务范围");
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
     * @return
     */
    private List<EmployeeDTO> getEmployeeData() {
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
     * 获取部门信息
     *
     * @return
     */
    private List<DepartmentDTO> getDepartmentData() {
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
        if (appraisalFlow == 1) {// 系统流程
            performanceAppraisal.setAppraisalStatus(1);
        } else {// 仅导入
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
                    if (appraisalFlow == 1) {// 系统流程
                        performanceAppraisalObjectsDTO.setAppraisalObjectStatus(1);
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
                throw new ServiceException("未找到匹配到的部门信息");
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
                    if (appraisalFlow == 1) {// 系统流程
                        performanceAppraisalObjectsDTO.setAppraisalObjectStatus(1);
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
        performanceAppraisalMapper.logicDeletePerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalIds(performanceAppraisalIds);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOList)) {
            return 1;
        }
        List<Long> performanceAppraisalObjectsIds = new ArrayList<>();
        List<Long> performanceAppraisalSnapIds = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectsIds.add(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
        }
        performanceAppraisalObjectsService.logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(performanceAppraisalObjectsIds);
        List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS = performAppraisalObjectSnapService.selectPerformAppraisalObjectSnapByAppraisalObjectsIds(performanceAppraisalObjectsIds);
        if (StringUtils.isEmpty(performAppraisalObjectSnapDTOS)) {
            throw new ServiceException("快照表数据异常 请联系管理员");
        }
        for (PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO : performAppraisalObjectSnapDTOS) {
            performanceAppraisalSnapIds.add(performAppraisalObjectSnapDTO.getAppraisalObjectSnapId());
        }
        return performAppraisalObjectSnapService.logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(performanceAppraisalSnapIds);
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
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        performanceAppraisal.setPerformanceAppraisalId(performanceAppraisalDTO.getPerformanceAppraisalId());
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        performanceAppraisalMapper.logicDeletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<Long> performanceAppraisalObjectIds = new ArrayList<>();
        List<Long> performanceAppraisalSnapIds = new ArrayList<>();
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOList)) {
            return 1;
        }
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectIds.add(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
        }
        performanceAppraisalObjectsService.logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(performanceAppraisalObjectIds);
        List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOS = performAppraisalObjectSnapService.selectPerformAppraisalObjectSnapByAppraisalObjectsIds(performanceAppraisalObjectIds);
        if (StringUtils.isEmpty(performAppraisalObjectSnapDTOS)) {
            throw new ServiceException("快照表数据异常 请联系管理员");
        }
        for (PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO : performAppraisalObjectSnapDTOS) {
            performanceAppraisalSnapIds.add(performAppraisalObjectSnapDTO.getAppraisalObjectSnapId());
        }
        return performAppraisalObjectSnapService.logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(performanceAppraisalSnapIds);
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
    @Transactional
    public int insertPerformanceAppraisals(List<PerformanceAppraisal> performanceAppraisalDtos) {
        return performanceAppraisalMapper.batchPerformanceAppraisal(performanceAppraisalDtos);
    }

    /**
     * 批量修改绩效考核表信息
     *
     * @param performanceAppraisalDtos 绩效考核表对象
     */
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
    public void importSysOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        try {
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
            Map<String, Long> orgObjectMap = new HashMap<>();// Code ,对象ID
            Map<String, Long> idMap = new HashMap<>();// Code ,主ID
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                orgObjectMap.put(performanceAppraisalObjectsDTO.getAppraisalObjectCode(), performanceAppraisalObjectsDTO.getAppraisalObjectId());
                idMap.put(performanceAppraisalObjectsDTO.getAppraisalObjectCode(), performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
            }
            ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
            List<Map<Integer, String>> listMap = read.doReadAllSync();
            listMap.remove(1);
            listMap.remove(0);
            if (StringUtils.isEmpty(listMap)) {
                throw new ServiceException("绩效考核Excel没有数据 请检查");
            }
            //2.更新绩效考核对象表
            List<EmployeeDTO> employeeDTOS = getEmployeeDTOS(listMap, 4);
            List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = new ArrayList<>();//需要更新的对象表lIst
            for (Map<Integer, String> map : listMap) {
                if (StringUtils.isNull(map.get(1))) {
                    break;
                }
                PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = new PerformanceAppraisalObjectsDTO();
                Long performanceObjectId = orgObjectMap.get(map.get(0));
                Long performAppraisalObjectsId = idMap.get(map.get(0));
                Long factorResultId;
                if (map.get(3).equals("不考核")) {
                    factorResultId = 0L;
                } else {
                    factorResultId = factorObjectMap.get(map.get(3));
                }
                performanceAppraisalObjectsDTO.setEvaluationScore(new BigDecimal(map.get(2)));
                performanceAppraisalObjectsDTO.setAppraisalObjectId(performanceObjectId);
                performanceAppraisalObjectsDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
                performanceAppraisalObjectsDTO.setAppraisalResultId(factorResultId);
                performanceAppraisalObjectsDTO.setAppraisalResult(map.get(3));
                for (EmployeeDTO employeeDTO : employeeDTOS) {
                    if (employeeDTO.getEmployeeCode().equals(map.get(4))) {
                        performanceAppraisalObjectsDTO.setAppraisalPrincipalId(employeeDTO.getEmployeeId());
                        performanceAppraisalObjectsDTO.setAppraisalPrincipalName(employeeDTO.getEmployeeName());
                        break;
                    }
                }
                performanceAppraisalObjectsDTOS.add(performanceAppraisalObjectsDTO);
            }
            performanceAppraisalObjectsService.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsDTOS);
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核表Excel失败");
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
    public void importSysPerPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        try {
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
            ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
            List<Map<Integer, String>> listMap = read.doReadAllSync();
            listMap.remove(1);
            listMap.remove(0);
            if (StringUtils.isEmpty(listMap)) {
                throw new ServiceException("绩效考核Excel没有数据 请检查");
            }
            //2.更新绩效考核对象表
            List<EmployeeDTO> employeeListByCode = getEmployeeDTOS(listMap, 7);
            List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = new ArrayList<>();//需要更新的对象表lIst
            for (Map<Integer, String> map : listMap) {
                if (StringUtils.isNull(map.get(0))) {
                    break;
                }
                PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = new PerformanceAppraisalObjectsDTO();
                Long performanceObjectId = orgObjectMap.get(map.get(0));
                Long performAppraisalObjectsId = idMap.get(map.get(0));
                Long factorResultId;
                if (map.get(6).equals("不考核")) {
                    factorResultId = 0L;
                } else {
                    factorResultId = factorObjectMap.get(map.get(6));
                }
                performanceAppraisalObjectsDTO.setEvaluationScore(new BigDecimal(map.get(5)));
                performanceAppraisalObjectsDTO.setAppraisalObjectId(performanceObjectId);
                performanceAppraisalObjectsDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
                performanceAppraisalObjectsDTO.setAppraisalResultId(factorResultId);
                performanceAppraisalObjectsDTO.setAppraisalResult(map.get(6));
                for (EmployeeDTO employeeDTO : employeeListByCode) {
                    if (employeeDTO.getEmployeeCode().equals(map.get(7))) {
                        performanceAppraisalObjectsDTO.setAppraisalPrincipalId(employeeDTO.getEmployeeId());
                        performanceAppraisalObjectsDTO.setAppraisalPrincipalName(employeeDTO.getEmployeeName());
                        break;
                    }
                }
                performanceAppraisalObjectsDTOS.add(performanceAppraisalObjectsDTO);
            }
            performanceAppraisalObjectsService.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsDTOS);
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
    }

    /**
     * 获取人员信息
     *
     * @param listMap listMap
     * @param key     值
     * @return
     */
    private List<EmployeeDTO> getEmployeeDTOS(List<Map<Integer, String>> listMap, int key) {
        List<String> assessmentList = new ArrayList<>();//考核负责人Code集合
        for (Map<Integer, String> map : listMap) {
            if (StringUtils.isNull(map.get(0))) {
                break;
            }
            assessmentList.add(map.get(key));
        }
        R<List<EmployeeDTO>> listR = employeeService.selectByCodes(assessmentList, SecurityConstants.INNER);
        List<EmployeeDTO> employeeListByCode = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        if (StringUtils.isEmpty(employeeListByCode)) {
            throw new ServiceException("当前人员信息为空 请检查");
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
        try {
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
            ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
            List<Map<Integer, String>> listMap = read.doReadAllSync();
            Map<Integer, String> head = listMap.get(2);
            if (head.containsValue(null)) {
                throw new ServiceException("请检查excel 表头不可以为空");
            }
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
                Map<Integer, String> valueMap = listMap.get(i);
                nameMap.put(i, valueMap.get(0));
                PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO = new PerformanceAppraisalObjectsDTO();
                Long performanceObjectId = orgObjectMap.get(valueMap.get(0));
                Long performAppraisalObjectsId = idMap.get(valueMap.get(0));
                if (valueMap.get(2).equals("不考核") || StringUtils.isNull(valueMap.get(2))) {
                    performanceAppraisalObjectsDTO.setAppraisalResultId(0L);
                    performanceAppraisalObjectsDTO.setAppraisalResult(null);
                } else {
                    Long factorResultId = factorObjectMap.get(valueMap.get(2));
                    performanceAppraisalObjectsDTO.setAppraisalResultId(factorResultId);
                    performanceAppraisalObjectsDTO.setAppraisalResult(valueMap.get(2));
                }
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
        } catch (IOException e) {
            throw new ServiceException("导入绩效考核表Excel失败");
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
        try {
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
            ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
            List<Map<Integer, String>> listMap = read.doReadAllSync();
            Map<Integer, String> head = listMap.get(2);
            if (head.containsValue(null)) {
                throw new ServiceException("导入失败 请检查excel 表头不可以为空");
            }
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
                if (valueMap.get(5).equals("不考核") || StringUtils.isNull(valueMap.get(5))) {
                    performanceAppraisalObjectsDTO.setAppraisalResultId(0L);
                } else {
                    Long factorResultId = factorObjectMap.get(valueMap.get(5));
                    performanceAppraisalObjectsDTO.setAppraisalResultId(factorResultId);
                    performanceAppraisalObjectsDTO.setAppraisalResult(valueMap.get(5));
                }
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
        } catch (IOException e) {
            throw new ServiceException("导入绩效考核表Excel失败");
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
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());//考核对象
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());//考核编码
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                if (performanceRankFactorDTO.getPerformanceRankFactorId().equals(performanceAppraisalObjectsDTO.getAppraisalResultId())) {
                    data.add(performanceRankFactorDTO.getPerformanceRankName());//结果
                    break;
                }
            }
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
    public Collection<List<Object>> dataOrgSysList(Long performanceAppraisalId, List<PerformanceRankFactorDTO> performanceRankFactorDTOS) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<Long> appraisalPrincipalIds = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            appraisalPrincipalIds.add(performanceAppraisalObjectsDTO.getAppraisalPrincipalId());
        }
        List<EmployeeDTO> employeeDTOS = getEmployeeDTOSByIds(appraisalPrincipalIds);
        List<List<Object>> list = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            List<Object> data = new ArrayList<>();
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());//考核对象
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());//考核对象
            data.add(performanceAppraisalObjectsDTO.getEvaluationScore());//分数
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                if (performanceRankFactorDTO.getPerformanceRankFactorId().equals(performanceAppraisalObjectsDTO.getAppraisalResultId())) {
                    data.add(performanceRankFactorDTO.getPerformanceRankName());//结果
                    break;
                }
            }
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalPrincipalId())) {
                    data.add(employeeDTO.getEmployeeCode());//考核责任人工号
                    data.add(employeeDTO.getEmployeeName());//考核责任人姓名
                    break;
                }
            }
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
            throw new ServiceException("当前获取的考核人信息为空 请检查员工配置");
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
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());//考核对象
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());//考核编码
            data.add(performanceAppraisalObjectsDTO.getPostName());//岗位
            data.add(performanceAppraisalObjectsDTO.getDepartmentName());//部门
            data.add(performanceAppraisalObjectsDTO.getOfficialRankName());//个人职级
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                if (performanceRankFactorDTO.getPerformanceRankFactorId().equals(performanceAppraisalObjectsDTO.getAppraisalResultId())) {
                    data.add(performanceRankFactorDTO.getPerformanceRankName());//结果
                    break;
                }
            }
            data.addAll(objects.get(i));//自定义的值
            list.add(data);
        }
        return list;
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
            appraisalPrincipalIds.add(performanceAppraisalObjectsDTO.getAppraisalPrincipalId());
        }
        R<List<EmployeeDTO>> listR = employeeService.selectByEmployeeIds(appraisalPrincipalIds, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        if (StringUtils.isEmpty(employeeDTOS)) {
            throw new ServiceException("当前获取的考核人信息有错 请检查员工配置");
        }
        List<List<Object>> list = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            List<Object> data = new ArrayList<>();
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectCode());//考核编码
            data.add(performanceAppraisalObjectsDTO.getAppraisalObjectName());//考核对象
            data.add(performanceAppraisalObjectsDTO.getPostName());//岗位
            data.add(performanceAppraisalObjectsDTO.getDepartmentName());//部门
            data.add(performanceAppraisalObjectsDTO.getOfficialRankName());//个人职级
            data.add(performanceAppraisalObjectsDTO.getEvaluationScore());//分数
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                if (performanceRankFactorDTO.getPerformanceRankFactorId().equals(performanceAppraisalObjectsDTO.getAppraisalResultId())) {
                    data.add(performanceRankFactorDTO.getPerformanceRankName());//结果
                    break;
                }
            }
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalPrincipalId())) {
                    data.add(employeeDTO.getEmployeeCode());//考核责任人工号
                    data.add(employeeDTO.getEmployeeName());//考核责任人姓名
                    break;
                }
            }
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
        return performanceAppraisalMapper.selectRankFactorByAppraisalId(appraisalId);
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
    public List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalDevelopList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        List<Long> performanceAppraisalIds = new ArrayList<>();
        Integer appraisalObjectStatus = getStatus(performanceAppraisalObjectsDTO, performanceAppraisalIds, 1);
        List<Integer> appraisalObjectStatuses = new ArrayList<>();
        if (StringUtils.isNull(appraisalObjectStatus)) {
            appraisalObjectStatuses.add(1);
            appraisalObjectStatuses.add(2);
        } else {
            appraisalObjectStatuses.add(appraisalObjectStatus);
        }
        performanceAppraisalObjectsDTO.setAppraisalObjectStatusList(appraisalObjectStatuses);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalIds(performanceAppraisalIds);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalMapper.selectOrgAppraisalObjectList(performanceAppraisalObjectsDTO);
        performanceAppraisalObjectsDTOList.forEach(PerformanceAppraisalServiceImpl::setObjectFieldName);
        return performanceAppraisalObjectsDTOList;
    }

    /**
     * 查询绩效考核表列表-组织-制定
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectPerAppraisalDevelopList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        List<Long> performanceAppraisalIds = new ArrayList<>();
        Integer appraisalObjectStatus = getStatus(performanceAppraisalObjectsDTO, performanceAppraisalIds, 2);
        List<Integer> appraisalObjectStatuses = new ArrayList<>();
        if (StringUtils.isNull(appraisalObjectStatus)) {
            appraisalObjectStatuses.add(1);
            appraisalObjectStatuses.add(2);
        } else {
            appraisalObjectStatuses.add(appraisalObjectStatus);
        }
        performanceAppraisalObjectsDTO.setAppraisalObjectStatusList(appraisalObjectStatuses);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalIds(performanceAppraisalIds);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalMapper.selectOrgAppraisalObjectList(performanceAppraisalObjectsDTO);
        performanceAppraisalObjectsDTOList.forEach(PerformanceAppraisalServiceImpl::setObjectFieldName);
        return performanceAppraisalObjectsDTOList;
    }

    /**
     * 获取对象ID集合
     *
     * @param performanceAppraisalObjectsDTO 对象DTO
     * @param performanceAppraisalIds        绩效任务id集合
     * @param objectType                     类型
     * @return
     */
    private Integer getStatus(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, List<Long> performanceAppraisalIds, Integer objectType) {
        PerformanceAppraisal appraisalDTO = new PerformanceAppraisal();
        appraisalDTO.setAppraisalObject(objectType);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(appraisalDTO);
        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDTOS) {
            performanceAppraisalIds.add(performanceAppraisalDTO.getPerformanceAppraisalId());
        }
        return performanceAppraisalObjectsDTO.getAppraisalObjectStatus();
    }

    /**
     * 查询绩效考核表列表-组织-制定-详情
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
     * 查询绩效考核表列表-个人-制定-详情
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
     * @return
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
     * @return
     */
    private PerformanceAppraisalObjectsDTO updateDevelopItem(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, Long performAppraisalObjectsId) {
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsAfter = performanceAppraisalObjectsDTO.getPerformanceAppraisalItemsDTOS();
        if (StringUtils.isEmpty(performanceAppraisalItemsAfter)) {
            return performanceAppraisalObjectsDTO;
        }
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsBefore = performanceAppraisalItemsService.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performAppraisalObjectsId);
        List<Long> indicatorIds = new ArrayList<>();
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsAfter) {
            performanceAppraisalItemsDTO.setPerformAppraisalObjectsId(performAppraisalObjectsId);
            indicatorIds.add(performanceAppraisalItemsDTO.getIndicatorId());
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
        operateItemValue(performanceAppraisalItemsBefore, performanceAppraisalItemsAfter);
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
        Long appraisalPrincipalId1 = performanceAppraisalObjectsDTO.getAppraisalPrincipalId();
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
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalReviewList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        List<Long> performanceAppraisalIds = new ArrayList<>();
        Integer appraisalObjectStatus = getStatus(performanceAppraisalObjectsDTO, performanceAppraisalIds, 1);
        List<Integer> appraisalObjectStatuses = new ArrayList<>();
        if (StringUtils.isNull(appraisalObjectStatus)) {
            appraisalObjectStatuses.add(3);
            appraisalObjectStatuses.add(4);
        } else {
            appraisalObjectStatuses.add(appraisalObjectStatus);
        }
        performanceAppraisalObjectsDTO.setAppraisalObjectStatusList(appraisalObjectStatuses);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalIds(performanceAppraisalIds);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalMapper.selectOrgAppraisalObjectList(performanceAppraisalObjectsDTO);
        performanceAppraisalObjectsDTOList.forEach(PerformanceAppraisalServiceImpl::setObjectFieldName);
        return performanceAppraisalObjectsDTOList;
    }

    /**
     * 查询绩效考核表列表-个人-评议
     *
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @return List
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectPerAppraisalReviewList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        List<Long> performanceAppraisalIds = new ArrayList<>();
        Integer appraisalObjectStatus = getStatus(performanceAppraisalObjectsDTO, performanceAppraisalIds, 2);
        List<Integer> appraisalObjectStatuses = new ArrayList<>();
        if (StringUtils.isNull(appraisalObjectStatus)) {
            appraisalObjectStatuses.add(3);
            appraisalObjectStatuses.add(4);
        } else {
            appraisalObjectStatuses.add(appraisalObjectStatus);
        }
        performanceAppraisalObjectsDTO.setAppraisalObjectStatusList(appraisalObjectStatuses);
        performanceAppraisalObjectsDTO.setPerformanceAppraisalIds(performanceAppraisalIds);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalMapper.selectOrgAppraisalObjectList(performanceAppraisalObjectsDTO);
        performanceAppraisalObjectsDTOList.forEach(PerformanceAppraisalServiceImpl::setObjectFieldName);
        return performanceAppraisalObjectsDTOList;
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
        performanceAppraisalObjectsDTO.setPerformanceAppraisalItemsDTOS(performanceAppraisalItemsDTOS);
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 计算分数
     *
     * @param performanceAppraisalItemsDTO 评议dto
     * @param examineDirection             分数
     *                                     1) 若指标的考核方向为正向，则为实际值/目标值*权重*100，实际值/目标值大于1.2时，按照1.2取值。
     *                                     2) 若指标的考核方向为负向，则为目标值/实际值*权重*100，实际值为0时分数为0。
     */
    private static void countScore(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO, Integer examineDirection) {
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
    public PerformanceAppraisalObjectsDTO updateOrgReviewPerformanceAppraisal(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
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
            appraisalItemsDTO.setActualValue(performanceAppraisalItemsDTO.getActualValue());
            performanceAppraisalItemList.add(appraisalItemsDTO);
        }
        performanceAppraisalItemsService.updatePerformanceAppraisalItemsS(performanceAppraisalItemList);
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 编辑个人绩效考核表 -评议
     *
     * @param performanceAppraisalObjectsDTO 考核对象
     * @return
     */
    @Override
    public PerformanceAppraisalObjectsDTO updatePerReviewPerformanceAppraisal(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
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
            appraisalItemsDTO.setActualValue(performanceAppraisalItemsDTO.getActualValue());
            performanceAppraisalItemList.add(appraisalItemsDTO);
        }
        performanceAppraisalItemsService.updatePerformanceAppraisalItemsS(performanceAppraisalItemList);
        return performanceAppraisalObjectsDTO;
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
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        // 更新绩效考核任务
        PerformanceAppraisalDTO appraisalDTO = new PerformanceAppraisalDTO();
        appraisalDTO.setPerformanceAppraisalId(performanceAppraisalId);
        if (performanceAppraisalObjectsDTOList.size() == 1) {
            appraisalDTO.setAppraisalStatus(1);
            updatePerformanceAppraisal(appraisalDTO);
        } else if (performanceAppraisalObjectsDTOList.size() > 2) {
            int submitSum = 1;
            for (PerformanceAppraisalObjectsDTO appraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                if (appraisalObjectsDTO.getAppraisalObjectStatus() < 3) {
                    submitSum += 1;
                }
            }
            if (submitSum == performanceAppraisalObjectsDTOList.size()) {
                appraisalDTO.setAppraisalStatus(1);
                updatePerformanceAppraisal(appraisalDTO);
            }
        }
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
        return performanceAppraisalItemsService.withdrawPerformanceAppraisalItems(itemsDTOList);
    }

    /**
     * 查询绩效考核表列表-组织-排名
     *
     * @param performanceAppraisalDTO 考核对象
     * @return List
     */
    @Override
    public List<PerformanceAppraisalDTO> selectOrgAppraisalRankingList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setAppraisalObject(1);
        performanceAppraisal.setAppraisalStatus(3);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        performanceAppraisalDTOS.forEach(PerformanceAppraisalServiceImpl::setFieldName);
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
    @Override
    public List<PerformanceAppraisalDTO> selectPerAppraisalRankingList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setAppraisalObject(2);
        performanceAppraisal.setAppraisalStatus(3);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        performanceAppraisalDTOS.forEach(PerformanceAppraisalServiceImpl::setFieldName);
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
     * @return
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
    private void updateRankOperate(Integer isSubmit, Long performanceAppraisalId, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList) {
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
    private void checkRankingUpdate(Integer isSubmit, Long performanceAppraisalId, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS) {
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
     * @param performanceAppraisalItemsBefore 库值
     * @param performanceAppraisalItemsAfter  后来的
     */
    private void operateItemValue(List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsBefore, List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsAfter) {
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
        try {
            if (StringUtils.isNotEmpty(delPerformanceAppraisalItem)) {
                List<Long> performanceAppraisalItems = new ArrayList<>();
                for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : delPerformanceAppraisalItem) {
                    performanceAppraisalItems.add(performanceAppraisalItemsDTO.getPerformAppraisalItemsId());
                }
                performanceAppraisalItemsService.logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsIds(performanceAppraisalItems);
            }
            if (StringUtils.isNotEmpty(addPerformanceAppraisalItem)) {
                performanceAppraisalItemsService.insertPerformanceAppraisalItemss(addPerformanceAppraisalItem);
            }
            if (StringUtils.isNotEmpty(updatePerformanceAppraisalItem)) {
                performanceAppraisalItemsService.updatePerformanceAppraisalItemsS(updatePerformanceAppraisalItem);
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.toString());
        }
    }

    /**
     * 为字段命名
     *
     * @param appraisalObjectsDTO 考核对象任务
     */
    private static void setObjectFieldName(PerformanceAppraisalObjectsDTO appraisalObjectsDTO) {
        // 考核周期类型/考核周期
        if (StringUtils.isNotNull(appraisalObjectsDTO) && StringUtils.isNotNull(appraisalObjectsDTO.getCycleType())) {
            switch (appraisalObjectsDTO.getCycleType()) {
                case 1:
                    appraisalObjectsDTO.setCycleTypeName("月度");
                    appraisalObjectsDTO.setCycleNumberName(appraisalObjectsDTO.getCycleNumber().toString() + "月");
                    break;
                case 2:
                    appraisalObjectsDTO.setCycleTypeName("季度");
                    appraisalObjectsDTO.setCycleNumberName(appraisalObjectsDTO.getCycleNumber().toString() + "季度");
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
                    appraisalObjectsDTO.setCycleNumberName("一整年");
                    break;
            }
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

