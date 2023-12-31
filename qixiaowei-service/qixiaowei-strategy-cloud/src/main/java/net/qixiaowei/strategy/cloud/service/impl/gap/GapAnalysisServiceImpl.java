package net.qixiaowei.strategy.cloud.service.impl.gap;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.excel.read.builder.ExcelReaderSheetBuilder;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysis;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOpportunityDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisPerformanceDTO;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.excel.gap.GapAnalysisOperateImportListener;
import net.qixiaowei.strategy.cloud.mapper.gap.GapAnalysisMapper;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisOperateService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisOpportunityService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisPerformanceService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * GapAnalysisService业务层处理
 *
 * @author Graves
 * @since 2023-02-24
 */
@Service
public class GapAnalysisServiceImpl implements IGapAnalysisService {
    @Autowired
    private GapAnalysisMapper gapAnalysisMapper;

    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    @Autowired
    private RemoteIndicatorService indicatorService;

    @Autowired
    private RemoteAreaService areaService;

    @Autowired
    private RemoteProductService productService;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemoteIndustryService industryService;

    @Autowired
    private IGapAnalysisOperateService gapAnalysisOperateService;

    @Autowired
    private IGapAnalysisPerformanceService gapAnalysisPerformanceService;

    @Autowired
    private IGapAnalysisOpportunityService gapAnalysisOpportunityService;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private RemoteUserService remoteUserService;


    /**
     * 查询差距分析表
     *
     * @param gapAnalysisId 差距分析表主键
     * @return 差距分析表
     */
    @Override
    public GapAnalysisDTO selectGapAnalysisByGapAnalysisId(Long gapAnalysisId) {
        GapAnalysisDTO gapAnalysisDTO = gapAnalysisMapper.selectGapAnalysisByGapAnalysisId(gapAnalysisId);
        String businessUnitDecompose = gapAnalysisDTO.getBusinessUnitDecompose();
        if (StringUtils.isNotEmpty(businessUnitDecompose)) {
            gapAnalysisDTO.setBusinessUnitDecomposeName(PlanBusinessUnitCode.getBusinessUnitDecomposeName(businessUnitDecompose));
            gapAnalysisDTO.setBusinessUnitDecomposes(PlanBusinessUnitCode.getDropList(businessUnitDecompose));
        }
        if (StringUtils.isNull(gapAnalysisDTO.getOperateHistoryYear())) {
            gapAnalysisDTO.setOperateHistoryYear(3);
        }
        setDecomposeValue(gapAnalysisDTO, businessUnitDecompose);
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS = gapAnalysisOperateService.selectGapAnalysisOperateByGapAnalysisId(gapAnalysisId);
        //历史经营情况
        List<GapAnalysisOperateDTO> GapAnalysisDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(gapAnalysisOperateDTOS)) {
            Map<Integer, Map<Integer, List<GapAnalysisOperateDTO>>> groupMap = gapAnalysisOperateDTOS.stream()
                    .collect(Collectors.groupingBy(GapAnalysisOperateDTO::getSort, Collectors.groupingBy(GapAnalysisOperateDTO::getOperateYear)));
            List<IndicatorDTO> indicatorDTOS = getIndicatorDTOS(gapAnalysisOperateDTOS);
            for (Integer sort : groupMap.keySet()) {
                GapAnalysisOperateDTO analysisOperateDTO = new GapAnalysisOperateDTO();
                analysisOperateDTO.setSort(sort);
                Map<Integer, List<GapAnalysisOperateDTO>> yearListMap = groupMap.get(sort);
                List<GapAnalysisOperateDTO> operateList = new ArrayList<>();
                String indicatorName = null;
                Long indicatorId = null;
                for (Integer operateYear : yearListMap.keySet()) {
                    GapAnalysisOperateDTO gapAnalysisOperateDTO = yearListMap.get(operateYear).get(0);
                    if (StringUtils.isNotNull(gapAnalysisOperateDTO.getIndicatorId())) {
                        IndicatorDTO indicator =
                                indicatorDTOS.stream().filter(indicatorDTO ->
                                        indicatorDTO.getIndicatorId().equals(gapAnalysisOperateDTO.getIndicatorId())).collect(Collectors.toList()).get(0);
                        indicatorName = indicator.getIndicatorName();
                        indicatorId = indicator.getIndicatorId();
                    }
                    BigDecimal targetValue = gapAnalysisOperateDTO.getTargetValue();
                    BigDecimal actualValue = gapAnalysisOperateDTO.getActualValue();
                    BigDecimal completionRate;
                    if (StringUtils.isNull(targetValue) || targetValue.compareTo(BigDecimal.ZERO) == 0)
                        completionRate = BigDecimal.ZERO;
                    else
                        completionRate = Optional.ofNullable(actualValue).orElse(BigDecimal.ZERO).multiply(new BigDecimal(100)).divide(targetValue, 2, RoundingMode.HALF_UP);
                    gapAnalysisOperateDTO.setCompletionRate(completionRate);
                    Map<String, Object> operateMap = new HashMap<>();
                    operateMap.put("operateYear", operateYear);
                    gapAnalysisOperateDTO.setOperateMap(operateMap);
                    operateList.add(gapAnalysisOperateDTO);
                }
                analysisOperateDTO.setSonGapAnalysisOperateDTOS(operateList.stream().sorted(Comparator.comparing(GapAnalysisOperateDTO::getOperateYear).reversed()).collect(Collectors.toList()));
                analysisOperateDTO.setIndicatorId(indicatorId);
                analysisOperateDTO.setIndicatorName(indicatorName);
                GapAnalysisDTOS.add(analysisOperateDTO);
            }
        }
        gapAnalysisDTO.setGapAnalysisOperateDTOS(GapAnalysisDTOS);
        // 机会差距
        List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDTOS = gapAnalysisOpportunityService.selectGapAnalysisOpportunityByGapAnalysisId(gapAnalysisId);
        if (StringUtils.isNotEmpty(gapAnalysisOpportunityDTOS)) {
            gapAnalysisDTO.setGapAnalysisOpportunityDTOS(gapAnalysisOpportunityDTOS);
        }
        // 业绩差距
        List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDTOS = gapAnalysisPerformanceService.selectGapAnalysisPerformanceByGapAnalysisId(gapAnalysisId);
        if (StringUtils.isNotEmpty(gapAnalysisPerformanceDTOS)) {
            gapAnalysisDTO.setGapAnalysisPerformanceDTOS(gapAnalysisPerformanceDTOS);
        }
        return gapAnalysisDTO;
    }

    /**
     * 获取指标列表
     *
     * @param gapAnalysisOperateDTOS 获取指标列表
     * @return List
     */
    private List<IndicatorDTO> getIndicatorDTOS(List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS) {
        List<Long> indicatorIds = gapAnalysisOperateDTOS.stream().map(GapAnalysisOperateDTO::getIndicatorId).filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(indicatorIds)) {
            R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> indicatorDTOS = indicatorR.getData();
            if (StringUtils.isNull(indicatorDTOS)) {
                throw new ServiceException("数据异常 指标不存在 请联系管理员");
            }
            return indicatorDTOS;
        }
        return new ArrayList<>();
    }

    /**
     * 根据维度进行赋值
     *
     * @param gapAnalysisDTO        差距分析DTO
     * @param businessUnitDecompose 业务单元维度
     */
    private void setDecomposeValue(GapAnalysisDTO gapAnalysisDTO, String businessUnitDecompose) {
        Long areaId = gapAnalysisDTO.getAreaId();
        Long departmentId = gapAnalysisDTO.getDepartmentId();
        Long productId = gapAnalysisDTO.getProductId();
        Long industryId = gapAnalysisDTO.getIndustryId();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNotNull(areaId)) {
                R<AreaDTO> areaDTOR = areaService.getById(areaId, SecurityConstants.INNER);
                AreaDTO areaDTO = areaDTOR.getData();
                if (StringUtils.isNotNull(areaDTO))
                    gapAnalysisDTO.setAreaName(areaDTO.getAreaName());
            }
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNotNull(departmentId)) {
                R<DepartmentDTO> departmentDTOR = departmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
                DepartmentDTO departmentDTO = departmentDTOR.getData();
                if (StringUtils.isNotNull(departmentDTO))
                    gapAnalysisDTO.setDepartmentName(departmentDTO.getDepartmentName());
            }
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNotNull(productId)) {
                R<ProductDTO> productDTOR = productService.remoteSelectById(productId, SecurityConstants.INNER);
                ProductDTO productDTO = productDTOR.getData();
                if (StringUtils.isNotNull(productDTO))
                    gapAnalysisDTO.setProductName(productDTO.getProductName());
            }
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNotNull(industryId)) {
                R<IndustryDTO> industryDTOR = industryService.selectById(industryId, SecurityConstants.INNER);
                IndustryDTO industryDTO = industryDTOR.getData();
                if (StringUtils.isNotNull(industryDTO))
                    gapAnalysisDTO.setIndustryName(industryDTO.getIndustryName());
            }
        }
    }

    /**
     * 查询差距分析表列表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 差距分析表
     */
    @DataScope(businessAlias = "ga")
    @Override
    public List<GapAnalysisDTO> selectGapAnalysisList(GapAnalysisDTO gapAnalysisDTO) {
        GapAnalysis gapAnalysis = new GapAnalysis();
        Map<String, Object> params = gapAnalysisDTO.getParams();
        if (StringUtils.isNull(params))
            params = new HashMap<>();
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        if (StringUtils.isNotEmpty(gapAnalysisDTO.getBusinessUnitName()))
            params.put("businessUnitName", gapAnalysisDTO.getBusinessUnitName());
        String createByName = gapAnalysisDTO.getCreateByName();
        List<String> createByList = new ArrayList<>();
        if (StringUtils.isNotEmpty(createByName)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(createByName);
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> employeeIds = userListData.stream().map(UserDTO::getUserId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(employeeIds)) {
                employeeIds.forEach(e -> {
                    createByList.add(String.valueOf(e));
                });
            } else {
                createByList.add("");
            }
        }
        params.put("createByList", createByList);
        this.queryEmployeeName(params);
        gapAnalysis.setParams(params);
        List<GapAnalysisDTO> gapAnalysisDTOS = gapAnalysisMapper.selectGapAnalysisList(gapAnalysis);
        if (StringUtils.isEmpty(gapAnalysisDTOS)) {
            return gapAnalysisDTOS;
        }
        // map label value
        for (GapAnalysisDTO gapAnalysisDTO1 : gapAnalysisDTOS) {
            String businessUnitDecompose = gapAnalysisDTO1.getBusinessUnitDecompose();
            gapAnalysisDTO1.setBusinessUnitDecomposes(PlanBusinessUnitCode.getDropList(businessUnitDecompose));
        }
        // 赋值员工
        Set<Long> createBys = gapAnalysisDTOS.stream().map(GapAnalysisDTO::getCreateBy).collect(Collectors.toSet());
        R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
        List<UserDTO> userDTOList = usersByUserIds.getData();
        if (StringUtils.isNotEmpty(userDTOList)) {
            for (GapAnalysisDTO gapAnalysisDTO1 : gapAnalysisDTOS) {
                for (UserDTO userDTO : userDTOList) {
                    if (gapAnalysisDTO1.getCreateBy().equals(userDTO.getUserId())) {
                        gapAnalysisDTO1.setCreateByName(userDTO.getEmployeeName());
                    }
                }
            }
        }
        List<Long> areaIds = gapAnalysisDTOS.stream().map(GapAnalysisDTO::getAreaId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> departmentIds = gapAnalysisDTOS.stream().map(GapAnalysisDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> productIds = gapAnalysisDTOS.stream().map(GapAnalysisDTO::getProductId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> industryIds = gapAnalysisDTOS.stream().map(GapAnalysisDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<AreaDTO> areaDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(areaIds)) {
            R<List<AreaDTO>> areaDTOSR = areaService.selectAreaListByAreaIds(areaIds, SecurityConstants.INNER);
            areaDTOS = areaDTOSR.getData();
            if (StringUtils.isEmpty(areaDTOS)) {
                throw new ServiceException("当前区域配置的信息已删除 请联系管理员");
            }
        }
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(departmentIds)) {
            R<List<DepartmentDTO>> departmentDTOSR = departmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
            departmentDTOS = departmentDTOSR.getData();
            if (StringUtils.isEmpty(departmentDTOS)) {
                throw new ServiceException("当前部门配置的信息已删除 请联系管理员");
            }
        }
        List<ProductDTO> productDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(productIds)) {
            R<List<ProductDTO>> productDTOSR = productService.getName(productIds, SecurityConstants.INNER);
            productDTOS = productDTOSR.getData();
            if (StringUtils.isEmpty(productDTOS)) {
                throw new ServiceException("当前产品配置的信息已删除 请联系管理员");
            }
        }
        List<IndustryDTO> industryDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(industryIds)) {
            R<List<IndustryDTO>> industryDTOSR = industryService.selectByIds(industryIds, SecurityConstants.INNER);
            industryDTOS = industryDTOSR.getData();
            if (StringUtils.isEmpty(industryDTOS)) {
                throw new ServiceException("当前行业配置的信息已删除 请联系管理员");
            }
        }
        for (GapAnalysisDTO analysisDTO : gapAnalysisDTOS) {
            String businessUnitDecompose = analysisDTO.getBusinessUnitDecompose();
            Long areaId = analysisDTO.getAreaId();
            Long industryId = analysisDTO.getIndustryId();
            Long productId = analysisDTO.getProductId();
            Long departmentId = analysisDTO.getDepartmentId();
            if (businessUnitDecompose.contains("region") && StringUtils.isNotEmpty(areaDTOS)) {
                List<AreaDTO> areaDTOS1 = areaDTOS.stream().filter(areaDTO -> areaDTO.getAreaId().equals(areaId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(areaDTOS1)) {
                    String areaName = areaDTOS1.get(0).getAreaName();
                    analysisDTO.setAreaName(areaName);
                }
            }
            if (businessUnitDecompose.contains("department") && StringUtils.isNotEmpty(departmentDTOS)) {
                List<DepartmentDTO> departmentDTOS1 = departmentDTOS.stream().filter(departmentDTO -> departmentDTO.getDepartmentId().equals(departmentId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(departmentDTOS1)) {
                    String departmentName = departmentDTOS1.get(0).getDepartmentName();
                    analysisDTO.setDepartmentName(departmentName);
                }
            }
            if (businessUnitDecompose.contains("product") && StringUtils.isNotEmpty(productDTOS)) {
                List<ProductDTO> productDTOS1 = productDTOS.stream().filter(productDTO -> productDTO.getProductId().equals(productId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(productDTOS1)) {
                    String productName = productDTOS1.get(0).getProductName();
                    analysisDTO.setProductName(productName);
                }
            }
            if (businessUnitDecompose.contains("industry") && StringUtils.isNotEmpty(industryDTOS)) {
                List<IndustryDTO> industryDTOS1 = industryDTOS.stream().filter(industryDTO -> industryDTO.getIndustryId().equals(industryId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(industryDTOS1)) {
                    String industryName = industryDTOS1.get(0).getIndustryName();
                    analysisDTO.setIndustryName(industryName);
                }
            }
        }
        return gapAnalysisDTOS;
    }

    /**
     * 封装高级查询人员id
     *
     * @param params 参数
     */
    private void queryEmployeeName(Map<String, Object> params) {
        Map<String, Object> params2 = new HashMap<>();
        if (StringUtils.isNotEmpty(params)) {
            for (String key : params.keySet()) {
                switch (key) {
                    case "createByNameEqual":
                        params2.put("employeeNameEqual", params.get("createByNameEqual"));
                        break;
                    case "createByNameNotEqual":
                        params2.put("employeeNameNotEqual", params.get("createByNameNotEqual"));
                        break;
                    case "createByNameLike":
                        params2.put("employeeNameLike", params.get("createByNameLike"));
                        break;
                    case "createByNameNotLike":
                        params2.put("employeeNameNotLike", params.get("createByNameNotLike"));
                        break;
                    default:
                        break;
                }
            }
            if (StringUtils.isNotEmpty(params2)) {
                EmployeeDTO employeeDTO = new EmployeeDTO();
                employeeDTO.setParams(params2);
                R<List<EmployeeDTO>> listR = employeeService.selectRemoteList(employeeDTO, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    List<Long> employeeIds = data.stream().filter(f -> f.getEmployeeId() != null).map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(employeeIds)) {
                        R<List<UserDTO>> usersByUserIds = remoteUserService.selectByemployeeIds(employeeIds, SecurityConstants.INNER);
                        List<UserDTO> data1 = usersByUserIds.getData();
                        if (StringUtils.isNotEmpty(data1)) {
                            params.put("createBys", data1.stream().map(UserDTO::getUserId).collect(Collectors.toList()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 新增差距分析表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */
    @Override
    @Transactional
    public GapAnalysisDTO insertGapAnalysis(GapAnalysisDTO gapAnalysisDTO) {
        if (StringUtils.isNull(gapAnalysisDTO)) {
            throw new ServiceException("请传参");
        }
        Long planBusinessUnitId = gapAnalysisDTO.getPlanBusinessUnitId();
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO)) {
            throw new ServiceException("当前的规划业务单元已经不存在");
        }
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        if (StringUtils.isNull(businessUnitDecompose)) {
            throw new ServiceException("规划业务单元维度数据异常 请检查规划业务单元配置");
        }
        // 判断是否选择校验：region,department,product,industry,company
        GapAnalysis gapAnalysisParams = getGapAnalysisParams(gapAnalysisDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = gapAnalysisDTO.getPlanYear();
        gapAnalysisParams.setPlanYear(planYear);
        gapAnalysisParams.setPlanBusinessUnitId(gapAnalysisDTO.getPlanBusinessUnitId());
        List<GapAnalysisDTO> gapAnalysisDTOS = gapAnalysisMapper.selectGapAnalysisList(gapAnalysisParams);
        if (StringUtils.isNotEmpty(gapAnalysisDTOS)) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
        GapAnalysis gapAnalysis = new GapAnalysis();
        // 将规划业务单元的规划业务单元维度赋进主表
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        gapAnalysis.setBusinessUnitDecompose(businessUnitDecompose);
        gapAnalysis.setCreateBy(SecurityUtils.getUserId());
        gapAnalysis.setCreateTime(DateUtils.getNowDate());
        gapAnalysis.setUpdateTime(DateUtils.getNowDate());
        gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
        gapAnalysis.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        gapAnalysisMapper.insertGapAnalysis(gapAnalysis);
        Long gapAnalysisId = gapAnalysis.getGapAnalysisId();
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS = gapAnalysisDTO.getGapAnalysisOperateDTOS();
        // 历史经营情况
        addAnalysisOperateList(gapAnalysisId, gapAnalysisOperateDTOS, gapAnalysisDTO);
        // 业绩差距
        List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDTOS = gapAnalysisDTO.getGapAnalysisPerformanceDTOS();// 业绩差距
        if (StringUtils.isNotEmpty(gapAnalysisPerformanceDTOS)) {
            int sort = 0;
            for (GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO : gapAnalysisPerformanceDTOS) {
                gapAnalysisPerformanceDTO.setGapAnalysisId(gapAnalysisId);
                gapAnalysisPerformanceDTO.setSort(sort);
                sort++;
            }
            List<Long> proposeEmployeeIds = gapAnalysisPerformanceDTOS.stream().map(GapAnalysisPerformanceDTO::getProposeEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(proposeEmployeeIds)) {
                R<List<EmployeeDTO>> employeeDTOSR = employeeService.selectByEmployeeIds(proposeEmployeeIds, SecurityConstants.INNER);
                List<EmployeeDTO> employeeDTOS = employeeDTOSR.getData();
                if (StringUtils.isEmpty(employeeDTOS)) {
                    throw new ServiceException("当前提出人已不存在 请检查员工配置");
                }
                for (GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO : gapAnalysisPerformanceDTOS) {
                    for (EmployeeDTO employeeDTO : employeeDTOS) {
                        if (employeeDTO.getEmployeeId().equals(gapAnalysisPerformanceDTO.getProposeEmployeeId())) {
                            gapAnalysisPerformanceDTO.setProposeEmployeeName(employeeDTO.getEmployeeName());
                            gapAnalysisPerformanceDTO.setProposeEmployeeCode(employeeDTO.getEmployeeCode());
                            break;
                        }
                    }
                }
            }
            gapAnalysisPerformanceService.insertGapAnalysisPerformances(gapAnalysisPerformanceDTOS);
        }
        // 机会差距
        List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDTOS = gapAnalysisDTO.getGapAnalysisOpportunityDTOS();// 机会差距
        if (StringUtils.isNotEmpty(gapAnalysisOpportunityDTOS)) {
            int sort = 0;
            for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityDTOS) {
                gapAnalysisOpportunityDTO.setGapAnalysisId(gapAnalysisId);
                gapAnalysisOpportunityDTO.setSort(sort);
                sort++;
            }
            List<Long> proposeEmployeeIds = gapAnalysisOpportunityDTOS.stream().map(GapAnalysisOpportunityDTO::getProposeEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(proposeEmployeeIds)) {
                R<List<EmployeeDTO>> employeeDTOSR = employeeService.selectByEmployeeIds(proposeEmployeeIds, SecurityConstants.INNER);
                List<EmployeeDTO> employeeDTOS = employeeDTOSR.getData();
                if (StringUtils.isEmpty(employeeDTOS)) {
                    throw new ServiceException("当前提出人已不存在 请检查员工配置");
                }
                for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityDTOS) {
                    for (EmployeeDTO employeeDTO : employeeDTOS) {
                        if (employeeDTO.getEmployeeId().equals(gapAnalysisOpportunityDTO.getProposeEmployeeId())) {
                            gapAnalysisOpportunityDTO.setProposeEmployeeName(employeeDTO.getEmployeeName());
                            gapAnalysisOpportunityDTO.setProposeEmployeeCode(employeeDTO.getEmployeeCode());
                            break;
                        }
                    }
                }
            }
            gapAnalysisOpportunityService.insertGapAnalysisOpportunitys(gapAnalysisOpportunityDTOS);
        }
        gapAnalysisDTO.setGapAnalysisId(gapAnalysisId);
        return gapAnalysisDTO;
    }

    /**
     * 新增历史经营情况
     *
     * @param gapAnalysisId          差距分析ID
     * @param gapAnalysisOperateDTOS 历史经营情况列表
     * @param gapAnalysisDTO         差距分析DTO
     */
    private void addAnalysisOperateList(Long gapAnalysisId, List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS, GapAnalysisDTO gapAnalysisDTO) {
        if (StringUtils.isNotEmpty(gapAnalysisOperateDTOS)) {
            Integer planYear = gapAnalysisDTO.getPlanYear();
            List<Long> indicatorIds = gapAnalysisOperateDTOS.stream().map(GapAnalysisOperateDTO::getIndicatorId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(indicatorIds)) {
                R<List<IndicatorDTO>> indicatorByIdR = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
                List<IndicatorDTO> indicatorById = indicatorByIdR.getData();
                if (StringUtils.isEmpty(indicatorById)) {
                    throw new ServiceException("历史经营情况列表指标已不存在 请检查指标配置");
                }
                List<Long> indicatorIdsById = indicatorById.stream().map(IndicatorDTO::getIndicatorId).collect(Collectors.toList());
                for (GapAnalysisOperateDTO gapAnalysisOperateDTO : gapAnalysisOperateDTOS) {
                    if (StringUtils.isNull(gapAnalysisOperateDTO.getIndicatorId())) {
                        throw new ServiceException("指标不可以为空");
                    }
                    if (StringUtils.isNotNull(gapAnalysisOperateDTO.getIndicatorId()) && !indicatorIdsById.contains(gapAnalysisOperateDTO.getIndicatorId())) {
                        throw new ServiceException("历史经营情况列表指标已不存在 请检查指标配置");
                    }
                }
                if (gapAnalysisOperateDTOS.size() != indicatorIds.size()) {
                    throw new ServiceException("请不要选择相同的指标");
                }
            }
            List<GapAnalysisOperateDTO> gapAnalysisOperates = new ArrayList<>();
            for (int i = 0; i < gapAnalysisOperateDTOS.size(); i++) {
                GapAnalysisOperateDTO gapAnalysisOperateDTO = gapAnalysisOperateDTOS.get(i);
                Long indicatorId = gapAnalysisOperateDTO.getIndicatorId();
                List<GapAnalysisOperateDTO> gapAnalysisOperateDTOSList = gapAnalysisOperateDTO.getSonGapAnalysisOperateDTOS();
                for (int j = 1; j < gapAnalysisOperateDTOSList.size() + 1; j++) {
                    GapAnalysisOperateDTO gapAnalysisOperate = new GapAnalysisOperateDTO();
                    GapAnalysisOperateDTO analysisOperateDTO = gapAnalysisOperateDTOSList.get(j - 1);
                    gapAnalysisOperate.setOperateYear(planYear - j);
                    gapAnalysisOperate.setGapAnalysisId(gapAnalysisId);
                    gapAnalysisOperate.setIndicatorId(indicatorId);
                    gapAnalysisOperate.setTargetValue(analysisOperateDTO.getTargetValue());
                    gapAnalysisOperate.setActualValue(analysisOperateDTO.getActualValue());
                    gapAnalysisOperate.setSort(i);
                    gapAnalysisOperates.add(gapAnalysisOperate);
                }
            }
            gapAnalysisOperateService.insertGapAnalysisOperates(gapAnalysisOperates);
        }
    }

    /**
     * 获取差距分析的入参
     * 校验入参的ID是否与维度匹配
     *
     * @param gapAnalysisDTO        差距分析DTO
     * @param businessUnitDecompose 维度
     * @return 差距分析入参DTO
     */
    private GapAnalysis getGapAnalysisParams(GapAnalysisDTO gapAnalysisDTO, String businessUnitDecompose) {
        Long areaId = gapAnalysisDTO.getAreaId();
        Long industryId = gapAnalysisDTO.getIndustryId();
        Long productId = gapAnalysisDTO.getProductId();
        Long departmentId = gapAnalysisDTO.getDepartmentId();
        GapAnalysis gapAnalysisParams = new GapAnalysis();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNull(areaId)) {
                throw new ServiceException("请选择区域");
            }
            gapAnalysisParams.setAreaId(areaId);
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNull(departmentId)) {
                throw new ServiceException("请选择部门");
            }
            gapAnalysisParams.setDepartmentId(departmentId);
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNull(productId)) {
                throw new ServiceException("请选择产品");
            }
            gapAnalysisParams.setProductId(productId);
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNull(industryId)) {
                throw new ServiceException("请选择行业");
            }
            gapAnalysisParams.setIndustryId(industryId);
        }
        return gapAnalysisParams;
    }

    /**
     * 修改差距分析表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateGapAnalysis(GapAnalysisDTO gapAnalysisDTO) {
        if (StringUtils.isNull(gapAnalysisDTO)) {
            throw new ServiceException("请传参");
        }
        Long gapAnalysisId = gapAnalysisDTO.getGapAnalysisId();
        Long planBusinessUnitId = gapAnalysisDTO.getPlanBusinessUnitId();
        GapAnalysisDTO gapAnalysisById = gapAnalysisMapper.selectGapAnalysisByGapAnalysisId(gapAnalysisId);
        if (StringUtils.isNull(gapAnalysisById)) {
            throw new ServiceException("当前差距分析已经不存在");
        }
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO)) {
            throw new ServiceException("当前的规划业务单元已经不存在");
        }
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        if (StringUtils.isNull(businessUnitDecompose)) {
            throw new ServiceException("规划业务单元维度数据异常 请检查规划业务单元配置");
        }
        // 判断是否选择校验：region,department,product,industry,company
        GapAnalysis gapAnalysisParams = getGapAnalysisParams(gapAnalysisDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = gapAnalysisById.getPlanYear();
        gapAnalysisParams.setPlanYear(planYear);
        gapAnalysisParams.setPlanBusinessUnitId(gapAnalysisById.getPlanBusinessUnitId());
        List<GapAnalysisDTO> gapAnalysisDTOS = gapAnalysisMapper.selectGapAnalysisList(gapAnalysisParams);
        if (StringUtils.isNotEmpty(gapAnalysisDTOS) && !gapAnalysisId.equals(gapAnalysisDTOS.get(0).getGapAnalysisId())) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
        GapAnalysis gapAnalysis = new GapAnalysis();
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        gapAnalysis.setUpdateTime(DateUtils.getNowDate());
        gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
        gapAnalysisMapper.updateGapAnalysis(gapAnalysis);
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS = gapAnalysisDTO.getGapAnalysisOperateDTOS();
        // 编辑历史经营情况
        if (StringUtils.isNotEmpty(gapAnalysisOperateDTOS)) {
            editAnalysisOperateList(gapAnalysisId, gapAnalysisOperateDTOS, gapAnalysisDTO, gapAnalysisById);
        }
        // 处理机会差距和业绩差距
        dealGapAnalysis(gapAnalysisDTO, gapAnalysisId);
        return 1;
    }

    /**
     * 处理差距分析
     *
     * @param gapAnalysisDTO 差距分析DTO
     * @param gapAnalysisId  差距分析ID
     */
    private void dealGapAnalysis(GapAnalysisDTO gapAnalysisDTO, Long gapAnalysisId) {
        // 机会差距
        List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDTOListAfter = gapAnalysisDTO.getGapAnalysisOpportunityDTOS();
        if (StringUtils.isNotEmpty(gapAnalysisOpportunityDTOListAfter)) {
            int sort = 0;
            for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityDTOListAfter) {
                gapAnalysisOpportunityDTO.setGapAnalysisId(gapAnalysisId);
                gapAnalysisOpportunityDTO.setSort(sort);
                sort++;
            }
        }
        if (StringUtils.isNotEmpty(gapAnalysisOpportunityDTOListAfter)) {
            List<Long> proposeEmployeeIds = gapAnalysisOpportunityDTOListAfter.stream().map(GapAnalysisOpportunityDTO::getProposeEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(proposeEmployeeIds)) {
                R<List<EmployeeDTO>> employeeDTOSR = employeeService.selectByEmployeeIds(proposeEmployeeIds, SecurityConstants.INNER);
                List<EmployeeDTO> employeeDTOS = employeeDTOSR.getData();
                if (StringUtils.isEmpty(employeeDTOS)) {
                    throw new ServiceException("当前提出人已不存在 请检查员工配置");
                }
                for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityDTOListAfter) {
                    for (EmployeeDTO employeeDTO : employeeDTOS) {
                        if (employeeDTO.getEmployeeId().equals(gapAnalysisOpportunityDTO.getProposeEmployeeId())) {
                            gapAnalysisOpportunityDTO.setProposeEmployeeName(employeeDTO.getEmployeeName());
                            gapAnalysisOpportunityDTO.setProposeEmployeeCode(employeeDTO.getEmployeeCode());
                            break;
                        }
                    }
                }
            }
            List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityBefore = gapAnalysisOpportunityService.selectGapAnalysisOpportunityByGapAnalysisId(gapAnalysisId);
            List<GapAnalysisOpportunityDTO> editOpportunityS = gapAnalysisOpportunityDTOListAfter.stream().filter(gapAnalysisOpportunityDTO ->
                    gapAnalysisOpportunityBefore.stream().map(GapAnalysisOpportunityDTO::getGapAnalysisOpportunityId).collect(Collectors.toList())
                            .contains(gapAnalysisOpportunityDTO.getGapAnalysisOpportunityId())).collect(Collectors.toList());

            List<GapAnalysisOpportunityDTO> delOpportunityS = gapAnalysisOpportunityBefore.stream().filter(gapAnalysisOpportunityDTO ->
                    !gapAnalysisOpportunityDTOListAfter.stream().map(GapAnalysisOpportunityDTO::getGapAnalysisOpportunityId).collect(Collectors.toList())
                            .contains(gapAnalysisOpportunityDTO.getGapAnalysisOpportunityId())).collect(Collectors.toList());

            List<GapAnalysisOpportunityDTO> addOpportunityS = gapAnalysisOpportunityDTOListAfter.stream().filter(gapAnalysisOpportunityDTO ->
                    !gapAnalysisOpportunityBefore.stream().map(GapAnalysisOpportunityDTO::getGapAnalysisOpportunityId).collect(Collectors.toList())
                            .contains(gapAnalysisOpportunityDTO.getGapAnalysisOpportunityId())).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(editOpportunityS)) {
                for (GapAnalysisOpportunityDTO editOpportunity : editOpportunityS) {
                    for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityBefore) {
                        if (editOpportunity.getSort().equals(gapAnalysisOpportunityDTO.getSort())) {
                            editOpportunity.setGapAnalysisOpportunityId(gapAnalysisOpportunityDTO.getGapAnalysisOpportunityId());
                            break;
                        }
                    }
                }
                gapAnalysisOpportunityService.updateGapAnalysisOpportunitys(editOpportunityS);
            }
            if (StringUtils.isNotEmpty(delOpportunityS)) {
                List<Long> delOpportunityIds = editOpportunityS.stream().map(GapAnalysisOpportunityDTO::getGapAnalysisOpportunityId).collect(Collectors.toList());
                gapAnalysisOpportunityService.logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(delOpportunityIds);
            }
            if (StringUtils.isNotEmpty(addOpportunityS)) {
                gapAnalysisOpportunityService.insertGapAnalysisOpportunitys(addOpportunityS);
            }
        }
        // 业绩差距
        List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDTOSAfter = gapAnalysisDTO.getGapAnalysisPerformanceDTOS();
        if (StringUtils.isNotEmpty(gapAnalysisPerformanceDTOSAfter)) {
            int sort = 0;
            for (GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO : gapAnalysisPerformanceDTOSAfter) {
                gapAnalysisPerformanceDTO.setGapAnalysisId(gapAnalysisId);
                gapAnalysisPerformanceDTO.setSort(sort);
                sort++;
            }
        }
        if (StringUtils.isNotEmpty(gapAnalysisPerformanceDTOSAfter)) {
            List<Long> proposeEmployeeIds = gapAnalysisPerformanceDTOSAfter.stream().map(GapAnalysisPerformanceDTO::getProposeEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(proposeEmployeeIds)) {
                R<List<EmployeeDTO>> employeeDTOSR = employeeService.selectByEmployeeIds(proposeEmployeeIds, SecurityConstants.INNER);
                List<EmployeeDTO> employeeDTOS = employeeDTOSR.getData();
                if (StringUtils.isEmpty(employeeDTOS)) {
                    throw new ServiceException("当前提出人已不存在 请检查员工配置");
                }
                for (GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO : gapAnalysisPerformanceDTOSAfter) {
                    for (EmployeeDTO employeeDTO : employeeDTOS) {
                        if (employeeDTO.getEmployeeId().equals(gapAnalysisPerformanceDTO.getProposeEmployeeId())) {
                            gapAnalysisPerformanceDTO.setProposeEmployeeName(employeeDTO.getEmployeeName());
                            gapAnalysisPerformanceDTO.setProposeEmployeeCode(employeeDTO.getEmployeeCode());
                            break;
                        }
                    }
                }
            }

            List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDTOBefore = gapAnalysisPerformanceService.selectGapAnalysisPerformanceByGapAnalysisId(gapAnalysisId);
            List<GapAnalysisPerformanceDTO> editPerformanceS = gapAnalysisPerformanceDTOSAfter.stream().filter(gapAnalysisPerformanceDTO ->
                    gapAnalysisPerformanceDTOBefore.stream().map(GapAnalysisPerformanceDTO::getGapAnalysisPerformanceId).collect(Collectors.toList())
                            .contains(gapAnalysisPerformanceDTO.getGapAnalysisPerformanceId())).collect(Collectors.toList());
            List<GapAnalysisPerformanceDTO> delPerformanceS = gapAnalysisPerformanceDTOBefore.stream().filter(gapAnalysisPerformanceDTO ->
                    !gapAnalysisPerformanceDTOSAfter.stream().map(GapAnalysisPerformanceDTO::getGapAnalysisPerformanceId).collect(Collectors.toList())
                            .contains(gapAnalysisPerformanceDTO.getGapAnalysisPerformanceId())).collect(Collectors.toList());
            List<GapAnalysisPerformanceDTO> addPerformanceS = gapAnalysisPerformanceDTOSAfter.stream().filter(gapAnalysisPerformanceDTO ->
                    !gapAnalysisPerformanceDTOBefore.stream().map(GapAnalysisPerformanceDTO::getGapAnalysisPerformanceId).collect(Collectors.toList())
                            .contains(gapAnalysisPerformanceDTO.getGapAnalysisPerformanceId())).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(editPerformanceS)) {
                for (GapAnalysisPerformanceDTO editPerformance : editPerformanceS) {
                    for (GapAnalysisPerformanceDTO analysisPerformanceDTO : gapAnalysisPerformanceDTOBefore) {
                        if (editPerformance.getSort().equals(analysisPerformanceDTO.getSort())) {
                            editPerformance.setGapAnalysisPerformanceId(analysisPerformanceDTO.getGapAnalysisPerformanceId());
                            break;
                        }
                    }
                }
                gapAnalysisPerformanceService.updateGapAnalysisPerformances(editPerformanceS);
            }
            if (StringUtils.isNotEmpty(delPerformanceS)) {
                List<Long> delPerformanceIds = delPerformanceS.stream().map(GapAnalysisPerformanceDTO::getGapAnalysisPerformanceId).collect(Collectors.toList());
                gapAnalysisPerformanceService.logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(delPerformanceIds);
            }
            if (StringUtils.isNotEmpty(addPerformanceS)) {
                gapAnalysisPerformanceService.insertGapAnalysisPerformances(addPerformanceS);
            }
        }
    }

    /**
     * 编辑历史经营情况
     *
     * @param gapAnalysisId          差距分析ID
     * @param gapAnalysisOperateDTOS 历史经营情况DTO
     * @param gapAnalysisDTO         差距分析DTO
     * @param gapAnalysisById        根据ID查找DTO
     */
    private void editAnalysisOperateList(Long gapAnalysisId, List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS, GapAnalysisDTO gapAnalysisDTO, GapAnalysisDTO gapAnalysisById) {
        List<Long> indicatorIds = gapAnalysisOperateDTOS.stream().map(GapAnalysisOperateDTO::getIndicatorId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        Integer planYear = gapAnalysisDTO.getPlanYear();
        Integer operateHistoryYear = gapAnalysisDTO.getOperateHistoryYear();
        if (StringUtils.isNotEmpty(indicatorIds)) {
            R<List<IndicatorDTO>> indicatorByIdR = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> indicatorById = indicatorByIdR.getData();
            if (StringUtils.isEmpty(indicatorById)) {
                throw new ServiceException("历史经营情况列表指标已不存在 请检查指标配置");
            }
            List<Long> indicatorIdsById = indicatorById.stream().map(IndicatorDTO::getIndicatorId).collect(Collectors.toList());
            for (GapAnalysisOperateDTO gapAnalysisOperateDTO : gapAnalysisOperateDTOS) {
                if (StringUtils.isNull(gapAnalysisOperateDTO.getIndicatorId())) {
                    throw new ServiceException("指标不可以为空");
                }
                if (StringUtils.isNotNull(gapAnalysisOperateDTO.getIndicatorId()) && !indicatorIdsById.contains(gapAnalysisOperateDTO.getIndicatorId())) {
                    throw new ServiceException("历史经营情况列表指标已不存在 请检查指标配置");
                }
            }
            if (gapAnalysisOperateDTOS.size() != indicatorIds.size()) {
                throw new ServiceException("请不要选择相同的指标");
            }
        }
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOSAfter = new ArrayList<>();
        for (int i = 0; i < gapAnalysisOperateDTOS.size(); i++) {
            GapAnalysisOperateDTO gapAnalysisOperateDTO = gapAnalysisOperateDTOS.get(i);
            List<GapAnalysisOperateDTO> gapAnalysisOperateDTOSList = gapAnalysisOperateDTO.getSonGapAnalysisOperateDTOS();
            for (int j = 1; j < gapAnalysisOperateDTOSList.size() + 1; j++) {
                GapAnalysisOperateDTO analysisOperateDTO = gapAnalysisOperateDTOSList.get(j - 1);
                GapAnalysisOperateDTO gapAnalysisOperate = new GapAnalysisOperateDTO();
                gapAnalysisOperate.setGapAnalysisOperateId(analysisOperateDTO.getGapAnalysisOperateId());
                gapAnalysisOperate.setOperateYear(planYear - j);
                gapAnalysisOperate.setGapAnalysisId(gapAnalysisId);
                gapAnalysisOperate.setIndicatorId(gapAnalysisOperateDTO.getIndicatorId());
                gapAnalysisOperate.setTargetValue(analysisOperateDTO.getTargetValue());
                gapAnalysisOperate.setActualValue(analysisOperateDTO.getActualValue());
                gapAnalysisOperate.setSort(i);
                gapAnalysisOperateDTOSAfter.add(gapAnalysisOperate);
            }
        }
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOSBefore = gapAnalysisOperateService.selectGapAnalysisOperateByGapAnalysisId(gapAnalysisId);
        Map<Integer, List<GapAnalysisOperateDTO>> beforeGroupBySort = gapAnalysisOperateDTOSBefore.stream().collect(Collectors.groupingBy(GapAnalysisOperateDTO::getSort));
        Map<Integer, List<GapAnalysisOperateDTO>> afterGroupBySort = gapAnalysisOperateDTOSAfter.stream().collect(Collectors.groupingBy(GapAnalysisOperateDTO::getSort));
        Set<Integer> beforeSorts = beforeGroupBySort.keySet();
        Set<Integer> afterSorts = afterGroupBySort.keySet();
        List<Integer> editSorts = beforeSorts.stream().filter(afterSorts::contains).collect(Collectors.toList());
        List<Integer> addSorts = afterSorts.stream().filter(afterSort -> !beforeSorts.contains(afterSort)).collect(Collectors.toList());
        List<Integer> delSorts = beforeSorts.stream().filter(beforeSort -> !afterSorts.contains(beforeSort)).collect(Collectors.toList());
        //删除List
        List<GapAnalysisOperateDTO> delAnalysisOperateDTOS = gapAnalysisOperateDTOSBefore.stream().filter(gapAnalysisOperateDTO -> delSorts.contains(gapAnalysisOperateDTO.getSort())).collect(Collectors.toList());
        //新增List
        List<GapAnalysisOperateDTO> addAnalysisOperateDTOS = gapAnalysisOperateDTOSAfter.stream().filter(gapAnalysisOperateDTO -> addSorts.contains(gapAnalysisOperateDTO.getSort())).collect(Collectors.toList());
        //编辑List
        List<GapAnalysisOperateDTO> editAnalysisOperateDTOS = new ArrayList<>();

        for (Integer editSort : editSorts) {
            List<GapAnalysisOperateDTO> editAnalysisOperateDTOSBefore = beforeGroupBySort.get(editSort);
            List<GapAnalysisOperateDTO> editAnalysisOperateDTOSAfter = afterGroupBySort.get(editSort);
            for (int i = planYear - 1; i >= planYear - operateHistoryYear; i--) {
                List<Integer> integers = new ArrayList<>();
                integers.add(i);
                if (editAnalysisOperateDTOSBefore.stream().map(GapAnalysisOperateDTO::getOperateYear).collect(Collectors.toList()).contains(i)
                        && editAnalysisOperateDTOSAfter.stream().map(GapAnalysisOperateDTO::getOperateYear).collect(Collectors.toList()).contains(i)) {
                    GapAnalysisOperateDTO analysisOperateDTOAfter = editAnalysisOperateDTOSAfter.stream().filter(e -> integers.contains(e.getOperateYear())).collect(Collectors.toList()).get(0);
                    GapAnalysisOperateDTO analysisOperateDTOBefore = editAnalysisOperateDTOSBefore.stream().filter(e -> integers.contains(e.getOperateYear())).collect(Collectors.toList()).get(0);
                    analysisOperateDTOAfter.setGapAnalysisOperateId(analysisOperateDTOBefore.getGapAnalysisOperateId());
                    editAnalysisOperateDTOS.add(analysisOperateDTOAfter);
                } else if (editAnalysisOperateDTOSBefore.stream().map(GapAnalysisOperateDTO::getOperateYear).collect(Collectors.toList()).contains(i)
                        && !editAnalysisOperateDTOSAfter.stream().map(GapAnalysisOperateDTO::getOperateYear).collect(Collectors.toList()).contains(i)) {
                    GapAnalysisOperateDTO analysisOperateDTOBefore = editAnalysisOperateDTOSBefore.stream().filter(e -> integers.contains(e.getOperateYear())).collect(Collectors.toList()).get(0);
                    delAnalysisOperateDTOS.add(analysisOperateDTOBefore);
                } else if (!editAnalysisOperateDTOSBefore.stream().map(GapAnalysisOperateDTO::getOperateYear).collect(Collectors.toList()).contains(i)
                        && editAnalysisOperateDTOSAfter.stream().map(GapAnalysisOperateDTO::getOperateYear).collect(Collectors.toList()).contains(i)) {
                    GapAnalysisOperateDTO analysisOperateDTOAfter = editAnalysisOperateDTOSAfter.stream().filter(e -> integers.contains(e.getOperateYear())).collect(Collectors.toList()).get(0);
                    addAnalysisOperateDTOS.add(analysisOperateDTOAfter);
                }
            }
        }
        if (StringUtils.isNotEmpty(editAnalysisOperateDTOS)) {
            gapAnalysisOperateService.updateGapAnalysisOperates(editAnalysisOperateDTOS);
        }
        if (StringUtils.isNotEmpty(addAnalysisOperateDTOS)) {
            gapAnalysisOperateService.insertGapAnalysisOperates(addAnalysisOperateDTOS);
        }
        if (StringUtils.isNotEmpty(delAnalysisOperateDTOS)) {
            List<Long> delIds = delAnalysisOperateDTOS.stream().map(GapAnalysisOperateDTO::getGapAnalysisOperateId).collect(Collectors.toList());
            gapAnalysisOperateService.logicDeleteGapAnalysisOperateByGapAnalysisOperateIds(delIds);
        }
    }

    /**
     * 逻辑批量删除差距分析表
     *
     * @param gapAnalysisIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisByGapAnalysisIds(List<Long> gapAnalysisIds) {
        if (StringUtils.isEmpty(gapAnalysisIds)) {
            throw new ServiceException("请选择需要删除的差距分析");
        }
        gapAnalysisMapper.logicDeleteGapAnalysisByGapAnalysisIds(gapAnalysisIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS = gapAnalysisOperateService.selectGapAnalysisOperateByGapAnalysisIds(gapAnalysisIds);
        if (StringUtils.isNotEmpty(gapAnalysisOperateDTOS)) {
            List<Long> gapAnalysisOperateIds = gapAnalysisOperateDTOS.stream().map(GapAnalysisOperateDTO::getGapAnalysisOperateId).collect(Collectors.toList());
            gapAnalysisOperateService.logicDeleteGapAnalysisOperateByGapAnalysisOperateIds(gapAnalysisOperateIds);
        }
        List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDTOS = gapAnalysisOpportunityService.selectGapAnalysisOpportunityByGapAnalysisIds(gapAnalysisIds);
        if (StringUtils.isNotEmpty(gapAnalysisOpportunityDTOS)) {
            List<Long> gapAnalysisOpportunityIds = gapAnalysisOpportunityDTOS.stream().map(GapAnalysisOpportunityDTO::getGapAnalysisOpportunityId).collect(Collectors.toList());
            gapAnalysisOpportunityService.logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(gapAnalysisOpportunityIds);
        }
        List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDTOS = gapAnalysisPerformanceService.selectGapAnalysisPerformanceByGapAnalysisIds(gapAnalysisIds);
        if (StringUtils.isNotEmpty(gapAnalysisPerformanceDTOS)) {
            List<Long> gapAnalysisPerformanceIds = gapAnalysisPerformanceDTOS.stream().map(GapAnalysisPerformanceDTO::getGapAnalysisPerformanceId).collect(Collectors.toList());
            gapAnalysisPerformanceService.logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(gapAnalysisPerformanceIds);
        }
        return 1;
    }

    /**
     * 物理删除差距分析表信息
     *
     * @param gapAnalysisId 差距分析表主键
     * @return 结果
     */
    @Override
    public int deleteGapAnalysisByGapAnalysisId(Long gapAnalysisId) {
        return gapAnalysisMapper.deleteGapAnalysisByGapAnalysisId(gapAnalysisId);
    }

    /**
     * 定义表头模板
     *
     * @param operateHistoryYear 历史经营年份
     * @param operateYear        年度
     * @return List
     */
    @Override
    public List<List<String>> headTemplate(Integer operateHistoryYear, Integer operateYear, Map<Integer, List<String>> selectMap) {
        if (StringUtils.isNull(operateHistoryYear)) {
            throw new ServiceException("请选择历史经营情况 历史年度");
        }
        R<List<IndicatorDTO>> indicatorDTOSR = indicatorService.selectIndicatorList(new IndicatorDTO(), SecurityConstants.INNER);
        List<IndicatorDTO> indicatorDTOS = indicatorDTOSR.getData();
        if (StringUtils.isEmpty(indicatorDTOS)) {
            throw new ServiceException("远程获取指标失败 请联系管理员");
        }
        return GapAnalysisOperateImportListener.headTemplate(operateHistoryYear, operateYear, indicatorDTOS, selectMap);
    }

    /**
     * 解析EXCEL
     *
     * @param operateHistoryYear 历史经营年份
     * @param operateYear2       经营年度
     * @param file               excel文件
     */
    @Override
    public List<GapAnalysisOperateDTO> excelParseObject(Integer operateHistoryYear, Integer operateYear2, MultipartFile file) {
        try {
            ExcelReaderBuilder read;
            try {
                read = EasyExcel.read(file.getInputStream());
            } catch (Exception e) {
                throw new ServiceException("导入绩效考核表Excel失败");
            }
            ExcelReaderSheetBuilder sheet = read.sheet(0);
            List<Map<Integer, String>> listMap = sheet.doReadSync();
            Map<Integer, String> head = listMap.get(0);
            head.remove(0);
            if (head.containsValue(null)) {
                throw new ServiceException("请检查excel 表头不可以为空");
            }
            if (StringUtils.isEmpty(listMap)) {
                throw new ServiceException("绩效考核Excel没有数据 请检查");
            }
            Set<Integer> operateYearList = new HashSet<>();
            for (int i = operateYear2 - 1; i >= operateYear2 - operateHistoryYear; i--) {
                operateYearList.add(i);
            }
            Set<Object> operateYears = new HashSet<>();
            for (Integer integer : head.keySet()) {
                String operateYear1 = head.get(integer);
                if (StringUtils.isEmpty(operateYear1)) {
                    throw new ServiceException("请在规定年份内输入数据");
                }
                operateYears.add(Integer.valueOf(operateYear1.substring(0, operateYear1.length() - 1)));
            }
            if (operateYears.size() != operateYearList.size() || !operateYears.containsAll(operateYearList)) {
                throw new ServiceException("模板格式不正确");
            }
            // 获取指标列表
            List<String> indicatorNames = new ArrayList<>();
            for (int i = 2; i < listMap.size(); i++) {
                if (StringUtils.isNull(listMap.get(i).get(0)))
                    continue;
                if (indicatorNames.contains(listMap.get(i).get(0))) {
                    throw new ServiceException("导入失败 同一指标在相同年度下仅可存在一条数据");
                }
                indicatorNames.add(listMap.get(i).get(0));
            }
            List<IndicatorDTO> indicatorByNames = new ArrayList<>();
            if (StringUtils.isNotEmpty(indicatorNames)) {
                R<List<IndicatorDTO>> indicatorByNamesR = indicatorService.selectIndicatorByNames(indicatorNames, SecurityConstants.INNER);
                indicatorByNames = indicatorByNamesR.getData();
                if (StringUtils.isEmpty(indicatorByNames)) {
                    throw new ServiceException("当前指标不存在");
                }
            }
            List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS = new ArrayList<>();
            for (int i = 2; i < listMap.size(); i++) {
                Map<Integer, String> map = listMap.get(i);
                int timer = 0;
                if (StringUtils.isNull(map.get(0))) {
                    continue;
                }
                IndicatorDTO indicator = indicatorByNames.stream().filter(indicatorDTO -> StringUtils.equals(indicatorDTO.getIndicatorName(), map.get(0))).collect(Collectors.toList()).get(0);
                GapAnalysisOperateDTO gapAnalysisOperateDTO = new GapAnalysisOperateDTO();
                gapAnalysisOperateDTO.setIndicatorId(indicator.getIndicatorId());
                gapAnalysisOperateDTO.setIndicatorName(indicator.getIndicatorName());
                gapAnalysisOperateDTO.setSort(i - 2);
                List<GapAnalysisOperateDTO> gapAnalysisOperateDTOList = new ArrayList<>();
                GapAnalysisOperateDTO analysisOperateDTO = new GapAnalysisOperateDTO();
                for (int j = 1; j < operateHistoryYear * 2 + 1; j++) {
                    String value = map.get(j);
                    if (timer == 0) {
                        analysisOperateDTO = new GapAnalysisOperateDTO();
                        if (StringUtils.isEmpty(value)) {
                            analysisOperateDTO.setTargetValue(BigDecimal.ZERO);
                        } else {
                            analysisOperateDTO.setTargetValue(new BigDecimal(value));
                        }
                        timer = 1;
                    } else {
                        if (StringUtils.isEmpty(value)) {
                            analysisOperateDTO.setActualValue(BigDecimal.ZERO);
                            analysisOperateDTO.setCompletionRate(BigDecimal.ZERO);
                        } else {
                            analysisOperateDTO.setActualValue(new BigDecimal(value));
                            BigDecimal targetValue = analysisOperateDTO.getTargetValue();
                            if (targetValue.compareTo(BigDecimal.ZERO) != 0) {
                                BigDecimal completionRate = analysisOperateDTO.getActualValue().multiply(new BigDecimal(100)).divide(targetValue, 2, RoundingMode.HALF_UP);
                                analysisOperateDTO.setCompletionRate(completionRate);
                            } else {
                                analysisOperateDTO.setCompletionRate(BigDecimal.ZERO);
                            }
                        }
                        Map<String, Object> operateMap = new HashMap<>();
                        String operateYear1 = head.get(j);
                        Integer operateYear = Integer.valueOf(operateYear1.substring(0, operateYear1.length() - 1));
                        operateMap.put("operateYear", operateYear);
                        analysisOperateDTO.setOperateMap(operateMap);
                        analysisOperateDTO.setSort(i - 2);
                        analysisOperateDTO.setIndicatorId(indicator.getIndicatorId());
                        analysisOperateDTO.setIndicatorName(indicator.getIndicatorName());
                        gapAnalysisOperateDTOList.add(analysisOperateDTO);
                        timer = 0;
                    }
                }
                gapAnalysisOperateDTO.setSonGapAnalysisOperateDTOS(gapAnalysisOperateDTOList);
                gapAnalysisOperateDTOS.add(gapAnalysisOperateDTO);
            }
            return gapAnalysisOperateDTOS;
        } catch (ServiceException e) {
            throw e;
        } catch (Exception e) {
            throw new ServiceException("模板格式不正确");
        }
    }

    /**
     * 远程获取列表
     *
     * @param gapAnalysisDTO 差距分析DTO
     * @return 结果
     */
    @Override
    public List<GapAnalysisDTO> remoteGapAnalysis(GapAnalysisDTO gapAnalysisDTO) {
        GapAnalysis gapAnalysis = new GapAnalysis();
        Map<String, Object> params = gapAnalysisDTO.getParams();
        gapAnalysis.setParams(params);
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        return gapAnalysisMapper.selectGapAnalysisList(gapAnalysis);
    }

    /**
     * 差距分析远程指标引用
     *
     * @param gapAnalysisDTO 差距分析
     * @return 结果
     */
    @Override
    public List<GapAnalysisOperateDTO> remoteOperateList(GapAnalysisDTO gapAnalysisDTO) {
        GapAnalysisOperateDTO analysisOperateDTO = new GapAnalysisOperateDTO();
        Map<String, Object> params = gapAnalysisDTO.getParams();
        analysisOperateDTO.setParams(params);
        return gapAnalysisOperateService.selectGapAnalysisOperateList(analysisOperateDTO);
    }

    /**
     * 逻辑删除差距分析表信息
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisByGapAnalysisId(GapAnalysisDTO gapAnalysisDTO) {
        if (StringUtils.isNull(gapAnalysisDTO.getGapAnalysisId())) {
            throw new ServiceException("请传入要删除的ID");
        }
        GapAnalysis gapAnalysis = new GapAnalysis();
        gapAnalysis.setGapAnalysisId(gapAnalysisDTO.getGapAnalysisId());
        gapAnalysis.setUpdateTime(DateUtils.getNowDate());
        gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
        gapAnalysisMapper.logicDeleteGapAnalysisByGapAnalysisId(gapAnalysis);
        Long gapAnalysisId = gapAnalysisDTO.getGapAnalysisId();
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOList = gapAnalysisOperateService.selectGapAnalysisOperateByGapAnalysisId(gapAnalysisId);
        if (StringUtils.isNotEmpty(gapAnalysisOperateDTOList)) {
            List<Long> gapAnalysisOperateIds = gapAnalysisOperateDTOList.stream().map(GapAnalysisOperateDTO::getGapAnalysisOperateId).collect(Collectors.toList());
            gapAnalysisOperateService.logicDeleteGapAnalysisOperateByGapAnalysisOperateIds(gapAnalysisOperateIds);
        }
        List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDTOS = gapAnalysisOpportunityService.selectGapAnalysisOpportunityByGapAnalysisId(gapAnalysisId);
        if (StringUtils.isNotEmpty(gapAnalysisOpportunityDTOS)) {
            List<Long> gapAnalysisOpportunityIds = gapAnalysisOpportunityDTOS.stream().map(GapAnalysisOpportunityDTO::getGapAnalysisOpportunityId).collect(Collectors.toList());
            gapAnalysisOpportunityService.logicDeleteGapAnalysisOpportunityByGapAnalysisOpportunityIds(gapAnalysisOpportunityIds);
        }
        List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDTOS = gapAnalysisPerformanceService.selectGapAnalysisPerformanceByGapAnalysisId(gapAnalysisId);
        if (StringUtils.isNotEmpty(gapAnalysisPerformanceDTOS)) {
            List<Long> gapAnalysisPerformanceIds = gapAnalysisPerformanceDTOS.stream().map(GapAnalysisPerformanceDTO::getGapAnalysisPerformanceId).collect(Collectors.toList());
            gapAnalysisPerformanceService.logicDeleteGapAnalysisPerformanceByGapAnalysisPerformanceIds(gapAnalysisPerformanceIds);
        }
        return 1;
    }

    /**
     * 物理删除差距分析表信息
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisByGapAnalysisId(GapAnalysisDTO gapAnalysisDTO) {
        GapAnalysis gapAnalysis = new GapAnalysis();
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        return gapAnalysisMapper.deleteGapAnalysisByGapAnalysisId(gapAnalysis.getGapAnalysisId());
    }

    /**
     * 物理批量删除差距分析表
     *
     * @param gapAnalysisDtoS 需要删除的差距分析表主键
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisByGapAnalysisIds(List<GapAnalysisDTO> gapAnalysisDtoS) {
        List<Long> stringList = new ArrayList<>();
        for (GapAnalysisDTO gapAnalysisDTO : gapAnalysisDtoS) {
            stringList.add(gapAnalysisDTO.getGapAnalysisId());
        }
        return gapAnalysisMapper.deleteGapAnalysisByGapAnalysisIds(stringList);
    }

    /**
     * 批量新增差距分析表信息
     *
     * @param gapAnalysisDtos 差距分析表对象
     */

    public int insertGapAnalysiss(List<GapAnalysisDTO> gapAnalysisDtos) {
        List<GapAnalysis> gapAnalysisList = new ArrayList<>();

        for (GapAnalysisDTO gapAnalysisDTO : gapAnalysisDtos) {
            GapAnalysis gapAnalysis = new GapAnalysis();
            BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
            gapAnalysis.setCreateBy(SecurityUtils.getUserId());
            gapAnalysis.setCreateTime(DateUtils.getNowDate());
            gapAnalysis.setUpdateTime(DateUtils.getNowDate());
            gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysis.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            gapAnalysisList.add(gapAnalysis);
        }
        return gapAnalysisMapper.batchGapAnalysis(gapAnalysisList);
    }

    /**
     * 批量修改差距分析表信息
     *
     * @param gapAnalysisDtoS 差距分析表对象
     */

    public int insertGapAnalysisS(List<GapAnalysisDTO> gapAnalysisDtoS) {
        List<GapAnalysis> gapAnalysisList = new ArrayList<>();

        for (GapAnalysisDTO gapAnalysisDTO : gapAnalysisDtoS) {
            GapAnalysis gapAnalysis = new GapAnalysis();
            BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
            gapAnalysis.setCreateBy(SecurityUtils.getUserId());
            gapAnalysis.setCreateTime(DateUtils.getNowDate());
            gapAnalysis.setUpdateTime(DateUtils.getNowDate());
            gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisList.add(gapAnalysis);
        }
        return gapAnalysisMapper.updateGapAnalysiss(gapAnalysisList);
    }

}

