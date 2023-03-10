package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import com.alibaba.nacos.shaded.com.google.common.collect.ImmutableMap;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasure;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasureDetail;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.*;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyIndexDimensionMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMeasureMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.*;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * StrategyMeasureService业务层处理
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
public class StrategyMeasureServiceImpl implements IStrategyMeasureService {

    public static Map<String, String> BUSINESS_UNIT_DECOMPOSE_MAP = ImmutableMap.of(
            "region", "区域",
            "department", "部门",
            "product", "产品",
            "industry", "行业",
            "company", "公司级"
    );

    @Autowired
    private StrategyMeasureMapper strategyMeasureMapper;

    @Autowired
    private RemoteAreaService areaService;

    @Autowired
    private RemoteProductService productService;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemoteIndustryService industryService;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private IStrategyMeasureTaskService strategyMeasureTaskService;

    @Autowired
    private IStrategyMeasureDetailService strategyMeasureDetailService;

    @Autowired
    private IStrategyMetricsService strategyMetricsService;

    @Autowired
    private IStrategyMetricsDetailService strategyMetricsDetailService;

    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    @Autowired
    private StrategyIndexDimensionMapper strategyIndexDimensionMapper;

    @Autowired
    private IStrategyIndexDimensionService strategyIndexDimensionService;

    /**
     * 查询战略举措清单表
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 战略举措清单表
     */
    @Override
    public StrategyMeasureDTO selectStrategyMeasureByStrategyMeasureId(Long strategyMeasureId) {
        StrategyMeasureDTO strategyMeasureDTO = strategyMeasureMapper.selectStrategyMeasureByStrategyMeasureId(strategyMeasureId);
        if (StringUtils.isNull(strategyMeasureDTO)) {
            throw new ServiceException("战略举措清单表已不存在");
        }
        String businessUnitDecompose = strategyMeasureDTO.getBusinessUnitDecompose();
        if (StringUtils.isNotEmpty(businessUnitDecompose)) {
            StringBuilder businessUnitDecomposeNames = new StringBuilder();
            List<String> businessUnitDecomposeList = Arrays.asList(businessUnitDecompose.split(";"));
            businessUnitDecomposeList.forEach(decompose -> {
                if (BUSINESS_UNIT_DECOMPOSE_MAP.containsKey(decompose)) {
                    businessUnitDecomposeNames.append(BUSINESS_UNIT_DECOMPOSE_MAP.get(decompose)).append(";");
                }
            });
            List<Map<String, Object>> businessUnitDecomposes = new ArrayList<>();
            for (String business : businessUnitDecomposeList) {
                Map<String, Object> businessUnitDecomposeMap = new HashMap<>();
                businessUnitDecomposeMap.put("label", BUSINESS_UNIT_DECOMPOSE_MAP.get(business));
                businessUnitDecomposeMap.put("value", business);
                businessUnitDecomposes.add(businessUnitDecomposeMap);
            }
            strategyMeasureDTO.setBusinessUnitDecomposes(businessUnitDecomposes);
            strategyMeasureDTO.setBusinessUnitDecomposeName(businessUnitDecomposeNames.substring(0, businessUnitDecomposeNames.length() - 1));
        }
        setDecomposeValue(strategyMeasureDTO, businessUnitDecompose);
        // 详情
        List<StrategyMeasureDetailVO> strategyMeasureDetailVOS = strategyMeasureDetailService.selectStrategyMeasureDetailVOByStrategyMeasureId(strategyMeasureId);
        if (StringUtils.isEmpty(strategyMeasureDetailVOS)) {
            return strategyMeasureDTO;
        }
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionService.selectStrategyIndexDimensionRootList();
        for (StrategyMeasureDetailVO strategyMeasureDetailVO : strategyMeasureDetailVOS) {
            for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
                if (strategyMeasureDetailVO.getStrategyMeasureId().equals(strategyIndexDimensionDTO.getStrategyIndexDimensionId())) {
                    strategyMeasureDetailVO.setRootIndexDimensionName(strategyIndexDimensionDTO.getRootIndexDimensionName());
                }
            }
        }
        List<Long> dutyDepartmentIds = strategyMeasureDetailVOS.stream().map(StrategyMeasureDetailVO::getDutyDepartmentId).collect(Collectors.toList());
        R<List<DepartmentDTO>> departmentDTOSR = departmentService.selectdepartmentIds(dutyDepartmentIds, SecurityConstants.INNER);
        List<DepartmentDTO> departmentDTOS = departmentDTOSR.getData();
        if (StringUtils.isEmpty(departmentDTOS)) {
            throw new ServiceException("当前部门已不存在");
        }
        for (StrategyMeasureDetailVO strategyMeasureDetailVO : strategyMeasureDetailVOS) {
            for (DepartmentDTO departmentDTO : departmentDTOS) {
                if (departmentDTO.getDepartmentId().equals(strategyMeasureDetailVO.getDutyDepartmentId())) {
                    strategyMeasureDetailVO.setDutyDepartmentName(departmentDTO.getDepartmentName());
                    break;
                }
            }
        }
        strategyMeasureDTO.setStrategyMeasureDetailVOS(strategyMeasureDetailVOS);
        return strategyMeasureDTO;
    }

    /**
     * 根据维度进行赋值
     *
     * @param strategyMeasureDTO    清单DTO
     * @param businessUnitDecompose 业务单元维度
     */
    private void setDecomposeValue(StrategyMeasureDTO strategyMeasureDTO, String businessUnitDecompose) {
        Long areaId = strategyMeasureDTO.getAreaId();
        Long departmentId = strategyMeasureDTO.getDepartmentId();
        Long productId = strategyMeasureDTO.getProductId();
        Long industryId = strategyMeasureDTO.getIndustryId();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNotNull(areaId)) {
                R<AreaDTO> areaDTOR = areaService.getById(areaId, SecurityConstants.INNER);
                AreaDTO areaDTO = areaDTOR.getData();
                if (StringUtils.isNull(areaDTO)) {
                    throw new ServiceException("当前区域配置的信息已删除 请联系管理员");
                }
                strategyMeasureDTO.setAreaName(areaDTO.getAreaName());
            }
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNotNull(departmentId)) {
                R<DepartmentDTO> departmentDTOR = departmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
                DepartmentDTO departmentDTO = departmentDTOR.getData();
                if (StringUtils.isNull(departmentDTO)) {
                    throw new ServiceException("当前部门配置的信息已删除 请联系管理员");
                }
                strategyMeasureDTO.setDepartmentName(departmentDTO.getDepartmentName());
            }
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNotNull(productId)) {
                R<ProductDTO> productDTOR = productService.remoteSelectById(productId, SecurityConstants.INNER);
                ProductDTO productDTO = productDTOR.getData();
                if (StringUtils.isNull(productDTO)) {
                    throw new ServiceException("当前产品配置的信息已删除 请联系管理员");
                }
                strategyMeasureDTO.setProductName(productDTO.getProductName());
            }
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNotNull(industryId)) {
                R<IndustryDTO> industryDTOR = industryService.selectById(industryId, SecurityConstants.INNER);
                IndustryDTO industryDTO = industryDTOR.getData();
                if (StringUtils.isNull(industryDTO)) {
                    throw new ServiceException("当前行业配置的信息已删除 请联系管理员");
                }
                strategyMeasureDTO.setIndustryName(industryDTO.getIndustryName());
            }
        }
    }

    /**
     * 查询战略举措清单表列表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 战略举措清单表
     */
    @Override
    public List<StrategyMeasureDTO> selectStrategyMeasureList(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        Map<String, Object> params = strategyMeasure.getParams();
        strategyMeasure.setParams(params);
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        List<StrategyMeasureDTO> strategyMeasureDTOS = strategyMeasureMapper.selectStrategyMeasureList(strategyMeasure);
        if (StringUtils.isEmpty(strategyMeasureDTOS)) {
            return strategyMeasureDTOS;
        }
        List<Long> areaIds = strategyMeasureDTOS.stream().map(StrategyMeasureDTO::getAreaId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> departmentIds = strategyMeasureDTOS.stream().map(StrategyMeasureDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> productIds = strategyMeasureDTOS.stream().map(StrategyMeasureDTO::getProductId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> industryIds = strategyMeasureDTOS.stream().map(StrategyMeasureDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<AreaDTO> areaDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(areaIds)) {
            R<List<AreaDTO>> areaDTOSR = areaService.selectAreaListByAreaIds(areaIds, SecurityConstants.INNER);
            areaDTOS = areaDTOSR.getData();
            if (StringUtils.isNull(areaDTOS)) {
                throw new ServiceException("当前区域配置的信息已删除 请联系管理员");
            }
        }
        List<DepartmentDTO> departmentDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(departmentIds)) {
            R<List<DepartmentDTO>> departmentDTOSR = departmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
            departmentDTOS = departmentDTOSR.getData();
            if (StringUtils.isNull(departmentDTOS)) {
                throw new ServiceException("当前部门配置的信息已删除 请联系管理员");
            }
        }
        List<ProductDTO> productDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(productIds)) {
            R<List<ProductDTO>> productDTOSR = productService.getName(productIds, SecurityConstants.INNER);
            productDTOS = productDTOSR.getData();
            if (StringUtils.isNull(productDTOS)) {
                throw new ServiceException("当前产品配置的信息已删除 请联系管理员");
            }
        }
        List<IndustryDTO> industryDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(industryIds)) {
            R<List<IndustryDTO>> industryDTOSR = industryService.selectByIds(industryIds, SecurityConstants.INNER);
            industryDTOS = industryDTOSR.getData();
            if (StringUtils.isNull(industryDTOS)) {
                throw new ServiceException("当前行业配置的信息已删除 请联系管理员");
            }
        }
        for (StrategyMeasureDTO measureDTO : strategyMeasureDTOS) {
            String businessUnitDecompose = measureDTO.getBusinessUnitDecompose();
            Long areaId = measureDTO.getAreaId();
            Long industryId = measureDTO.getIndustryId();
            Long productId = measureDTO.getProductId();
            Long departmentId = measureDTO.getDepartmentId();
            if (businessUnitDecompose.contains("region") && StringUtils.isNotEmpty(areaDTOS)) {
                String areaName = areaDTOS.stream().filter(areaDTO -> areaDTO.getAreaId().equals(areaId)).collect(Collectors.toList()).get(0).getAreaName();
                measureDTO.setAreaName(areaName);
            }
            if (businessUnitDecompose.contains("department") && StringUtils.isNotEmpty(departmentDTOS)) {
                String departmentName = departmentDTOS.stream().filter(departmentDTO -> departmentDTO.getDepartmentId().equals(departmentId)).collect(Collectors.toList()).get(0).getDepartmentName();
                measureDTO.setDepartmentName(departmentName);
            }
            if (businessUnitDecompose.contains("product") && StringUtils.isNotEmpty(productDTOS)) {
                String productName = productDTOS.stream().filter(productDTO -> productDTO.getProductId().equals(productId)).collect(Collectors.toList()).get(0).getProductName();
                measureDTO.setProductName(productName);
            }
            if (businessUnitDecompose.contains("industry") && StringUtils.isNotEmpty(industryDTOS)) {
                String industryName = industryDTOS.stream().filter(industryDTO -> industryDTO.getIndustryId().equals(industryId)).collect(Collectors.toList()).get(0).getIndustryName();
                measureDTO.setIndustryName(industryName);
            }
        }
        return strategyMeasureDTOS;
    }

    /**
     * 新增战略举措清单表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    @Override
    @Transactional
    public StrategyMeasureDTO insertStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        this.checkMeasure(strategyMeasureDTO);
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        // 将规划业务单元的规划业务单元维度赋进主表
        this.addStrategyMeasure(strategyMeasureDTO, strategyMeasure);
        Long strategyMeasureId = strategyMeasure.getStrategyMeasureId();
        strategyMeasureDTO.setStrategyMeasureId(strategyMeasureId);
        // 新增战略指标衡量
        StrategyMetricsDTO strategyMetricsDTO = new StrategyMetricsDTO();
        BeanUtils.copyProperties(strategyMeasure, strategyMetricsDTO);
        Long strategyMetricsId = strategyMetricsService.insertStrategyMetrics(strategyMetricsDTO);
        // 详情
        List<StrategyMeasureDetailVO> strategyMeasureDetailVOS = strategyMeasureDTO.getStrategyMeasureDetailVOS();
        if (StringUtils.isNotEmpty(strategyMeasureDetailVOS)) {
            List<Long> strategyIndexDimensionIds = strategyMeasureDetailVOS.stream().map(StrategyMeasureDetailVO::getStrategyIndexDimensionId).filter(Objects::nonNull).collect(Collectors.toList());
            if (StringUtils.isEmpty(strategyIndexDimensionIds)) {
                throw new ServiceException("请选择维度");
            }
            // 负责人赋值
            this.setDutyEmployeeValue(strategyMeasureDetailVOS);
            this.setSortValue(strategyMeasureId, strategyMeasureDetailVOS, strategyIndexDimensionIds);
            List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOS = new ArrayList<>();
            Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupDetailMapS =
                    strategyMeasureDetailVOS.stream().collect(Collectors.groupingBy(StrategyMeasureDetailVO::getSort, Collectors.groupingBy(StrategyMeasureDetailVO::getSerialNumber)));
            this.setTaskSortValue(strategyMeasureDetailDTOS, groupDetailMapS);
            List<StrategyMeasureDetail> strategyMeasureDetails = strategyMeasureDetailService.insertStrategyMeasureDetails(strategyMeasureDetailDTOS);
            // 新增战略衡量指标详情
            this.addMetricsDetail(strategyMetricsId, strategyMeasureDetails);
            // 新增战略清单详情任务表
            this.addMeasureTask(strategyMeasureId, groupDetailMapS, strategyMeasureDetails);
        }
        return strategyMeasureDTO;
    }

    /**
     * 新增战略清单详情任务表
     *
     * @param strategyMeasureId      战略举措清单ID
     * @param groupDetailMapS        分组详情表
     * @param strategyMeasureDetails 新增后的战略清单详情
     */
    private void addMeasureTask(Long strategyMeasureId, Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupDetailMapS, List<StrategyMeasureDetail> strategyMeasureDetails) {
        List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS = new ArrayList<>();
        for (Integer sorted : groupDetailMapS.keySet()) {
            Map<Integer, List<StrategyMeasureDetailVO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
            for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                List<StrategyMeasureDetailVO> measureTaskDTOS = groupSerialNumberMapS.get(serialNumber);
                StrategyMeasureDetailVO measureTaskDTO = measureTaskDTOS.get(0);
                Long strategyMeasureDetailId = null;
                for (StrategyMeasureDetail strategyMeasureDetail : strategyMeasureDetails) {
                    if (strategyMeasureDetail.getSort().equals(measureTaskDTO.getSort()) && strategyMeasureDetail.getSerialNumber().equals(measureTaskDTO.getSerialNumber())) {
                        strategyMeasureDetailId = strategyMeasureDetail.getStrategyMeasureDetailId();
                    }
                }
                if (StringUtils.isNull(strategyMeasureDetailId))
                    throw new ServiceException("新增失败 请联系管理员");
                for (StrategyMeasureDetailVO taskDTO : measureTaskDTOS) {
                    taskDTO.setStrategyMeasureDetailId(strategyMeasureDetailId);
                    taskDTO.setStrategyMeasureId(strategyMeasureId);
                    StrategyMeasureTaskDTO strategyMeasureTaskDTO = new StrategyMeasureTaskDTO();
                    BeanUtils.copyProperties(taskDTO, strategyMeasureTaskDTO);
                    strategyMeasureTaskDTOS.add(strategyMeasureTaskDTO);
                }
            }
        }
        strategyMeasureTaskService.insertStrategyMeasureTasks(strategyMeasureTaskDTOS);
    }

    /**
     * 新增战略衡量指标详情
     *
     * @param strategyMetricsId      战略衡量指标ID
     * @param strategyMeasureDetails 战略衡量指标详情
     */
    private void addMetricsDetail(Long strategyMetricsId, List<StrategyMeasureDetail> strategyMeasureDetails) {
        List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOS = new ArrayList<>();
        for (StrategyMeasureDetail strategyMeasureDetail : strategyMeasureDetails) {
            StrategyMetricsDetailDTO strategyMetricsDetailDTO = new StrategyMetricsDetailDTO();
            BeanUtils.copyProperties(strategyMeasureDetail, strategyMetricsDetailDTO);
            strategyMetricsDetailDTO.setStrategyMetricsId(strategyMetricsId);
            strategyMetricsDetailDTOS.add(strategyMetricsDetailDTO);
        }
        strategyMetricsDetailService.insertStrategyMetricsDetails(strategyMetricsDetailDTOS);
    }

    /**
     * 排序战略清单详情任务表
     *
     * @param strategyMeasureDetailDTOS 战略详情DTO集合
     * @param groupDetailMapS           分组后的详情Map
     */
    private void setTaskSortValue(List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOS, Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupDetailMapS) {
        for (Integer sorted : groupDetailMapS.keySet()) {
            Map<Integer, List<StrategyMeasureDetailVO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
            for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                StrategyMeasureDetailDTO strategyMeasureDetailDTO = new StrategyMeasureDetailDTO();
                List<StrategyMeasureDetailVO> measureTaskDTOS = groupSerialNumberMapS.get(serialNumber);
                StrategyMeasureDetailVO measureDetailVO = measureTaskDTOS.get(0);
                BeanUtils.copyProperties(measureDetailVO, strategyMeasureDetailDTO);
                strategyMeasureDetailDTOS.add(strategyMeasureDetailDTO);
                for (int i = 0; i < measureTaskDTOS.size(); i++) {
                    measureTaskDTOS.get(i).setTaskSort(i);
                }
            }
        }
    }

    /**
     * 对详情排序
     *
     * @param strategyMeasureId         清单ID
     * @param strategyMeasureDetailVOS  清单详情VO
     * @param strategyIndexDimensionIds 清单ID集合
     */
    private void setSortValue(Long strategyMeasureId, List<StrategyMeasureDetailVO> strategyMeasureDetailVOS, List<Long> strategyIndexDimensionIds) {
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionIds(strategyIndexDimensionIds);
        int sort = 0;
        for (int i = 0; i < strategyMeasureDetailVOS.size(); i++) {
            StrategyMeasureDetailVO strategyMeasureDetailVO = strategyMeasureDetailVOS.get(i);
            strategyMeasureDetailVO.setStrategyMeasureId(strategyMeasureId);
            // 任务排序
            // 序列号赋值也可排序
            for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
                if (strategyMeasureDetailVO.getStrategyIndexDimensionId().equals(strategyIndexDimensionDTO.getStrategyIndexDimensionId())) {
                    String serialNumberName = strategyMeasureDetailVO.getSerialNumberName();
                    String indexDimensionCode = strategyIndexDimensionDTO.getIndexDimensionCode();
                    if (StringUtils.isNotEmpty(serialNumberName)) {
                        if (!serialNumberName.contains(indexDimensionCode))
                            throw new ServiceException("编码不匹配 请联系管理员");
                        Integer serialNumber = Integer.valueOf(serialNumberName.substring(indexDimensionCode.length(), serialNumberName.length() - 1));
                        strategyMeasureDetailVO.setSerialNumber(serialNumber);
                        break;
                    }
                }
            }
            // 详情排序
            strategyMeasureDetailVO.setSort(sort);
            if (i == strategyMeasureDetailVOS.size() - 1)
                break;
            if (!strategyMeasureDetailVOS.get(i + 1).getStrategyIndexDimensionId().equals(strategyMeasureDetailVO.getStrategyIndexDimensionId()))
                sort += 1;
        }
    }

    /**
     * 负责人赋值
     *
     * @param strategyMeasureDetailVOS 清单详情VO
     */
    private void setDutyEmployeeValue(List<StrategyMeasureDetailVO> strategyMeasureDetailVOS) {
        List<Long> dutyEmployeeIds = strategyMeasureDetailVOS.stream().map(StrategyMeasureDetailVO::getDutyEmployeeId).filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(dutyEmployeeIds)) {
            R<List<EmployeeDTO>> employeeDTOSR = employeeService.selectByEmployeeIds(dutyEmployeeIds, SecurityConstants.INNER);
            List<EmployeeDTO> employeeDTOS = employeeDTOSR.getData();
            if (StringUtils.isNotEmpty(employeeDTOS)) {
                throw new ServiceException("当前负责人已不存在 请检查员工配置");
            }
            for (StrategyMeasureDetailVO strategyMeasureDetailVO : strategyMeasureDetailVOS) {
                for (EmployeeDTO employeeDTO : employeeDTOS) {
                    if (employeeDTO.getEmployeeId().equals(strategyMeasureDetailVO.getDutyEmployeeId())) {
                        strategyMeasureDetailVO.setDutyEmployeeName(employeeDTO.getEmployeeName());
                        strategyMeasureDetailVO.setDutyEmployeeCode(employeeDTO.getEmployeeCode());
                        break;
                    }
                }
            }
        }
    }

    /**
     * 战略清单校验
     *
     * @param strategyMeasureDTO 战略清单DTO
     */
    private void checkMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        if (StringUtils.isNull(strategyMeasureDTO)) {
            throw new ServiceException("请传参");
        }
        Long planBusinessUnitId = strategyMeasureDTO.getPlanBusinessUnitId();
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO)) {
            throw new ServiceException("当前的规划业务单元已经不存在");
        }
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        if (StringUtils.isNull(businessUnitDecompose)) {
            throw new ServiceException("规划业务单元维度数据异常 请检查规划业务单元配置");
        }
        // 判断是否选择校验：region,department,product,industry,company
        StrategyMeasure strategyMeasureParams = getStrategyMeasureParams(strategyMeasureDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = strategyMeasureDTO.getPlanYear();
        strategyMeasureParams.setPlanYear(planYear);
        strategyMeasureParams.setPlanBusinessUnitId(strategyMeasureDTO.getPlanBusinessUnitId());
        List<StrategyMeasureDTO> strategyMeasureDTOS = strategyMeasureMapper.selectStrategyMeasureList(strategyMeasureParams);
        if (StringUtils.isNotEmpty(strategyMeasureDTOS)) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
    }

    /**
     * 新增清单表
     *
     * @param strategyMeasureDTO 清单DTO
     * @param strategyMeasure    清单
     */
    private void addStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO, StrategyMeasure strategyMeasure) {
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        strategyMeasure.setCreateBy(SecurityUtils.getUserId());
        strategyMeasure.setCreateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        strategyMeasure.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMeasureMapper.insertStrategyMeasure(strategyMeasure);
    }

    /**
     * 获取战略举措的入参
     * 校验入参的ID是否与维度匹配
     *
     * @param strategyMeasureDTO    战略举措DTO
     * @param businessUnitDecompose 维度
     * @return 战略举措入参DTO
     */
    private StrategyMeasure getStrategyMeasureParams(StrategyMeasureDTO strategyMeasureDTO, String businessUnitDecompose) {
        Long areaId = strategyMeasureDTO.getAreaId();
        Long industryId = strategyMeasureDTO.getIndustryId();
        Long productId = strategyMeasureDTO.getProductId();
        Long departmentId = strategyMeasureDTO.getDepartmentId();
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNull(areaId)) {
                throw new ServiceException("请选择区域");
            }
            strategyMeasure.setAreaId(areaId);
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNull(departmentId)) {
                throw new ServiceException("请选择部门");
            }
            strategyMeasure.setDepartmentId(departmentId);
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNull(productId)) {
                throw new ServiceException("请选择产品");
            }
            strategyMeasure.setProductId(productId);
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNull(industryId)) {
                throw new ServiceException("请选择行业");
            }
            strategyMeasure.setIndustryId(industryId);
        }
        return strategyMeasure;
    }

    /**
     * 修改战略举措清单表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    @Override
    public int updateStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        this.checkStrategyMeasureOfUpdate(strategyMeasureDTO);
        // 编辑战略举措清单
        Long strategyMeasureId = editStrategyMeasure(strategyMeasureDTO);
        // 编辑战略衡量指标
        this.editStrategyMetrics(strategyMeasureDTO, strategyMeasureId);
        List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOBefore = strategyMeasureDetailService.selectStrategyMeasureDetailByStrategyMeasureId(strategyMeasureId);
        for (StrategyMeasureDetailDTO strategyMeasureDetailDTO : strategyMeasureDetailDTOBefore) {
            Integer serialNumber = strategyMeasureDetailDTO.getSerialNumber();
        }
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionService.selectStrategyIndexDimensionRootList();
        List<Long> strategyIndexDimensionIds = strategyIndexDimensionDTOS.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId).collect(Collectors.toList());
        List<StrategyMeasureDetailVO> strategyMeasureDetailVOS = strategyMeasureDTO.getStrategyMeasureDetailVOS();
        if (StringUtils.isNotEmpty(strategyMeasureDetailVOS)) {
            // 负责人赋值
            this.setDutyEmployeeValue(strategyMeasureDetailVOS);
            // 第一层排序
            this.setSortValue(strategyMeasureId, strategyMeasureDetailVOS, strategyIndexDimensionIds);
            List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOAfter = new ArrayList<>();
            Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupDetailMapS =
                    strategyMeasureDetailVOS.stream().collect(Collectors.groupingBy(StrategyMeasureDetailVO::getSort, Collectors.groupingBy(StrategyMeasureDetailVO::getSerialNumber)));
            this.setTaskSortValue(strategyMeasureDetailDTOAfter, groupDetailMapS);
            // 为空直接全部删除
            List<Long> strategyMeasureDetailBeforeIds;
            if (StringUtils.isEmpty(strategyMeasureDetailDTOAfter) && StringUtils.isNotEmpty(strategyMeasureDetailDTOBefore)) {
                strategyMeasureDetailBeforeIds = strategyMeasureDetailDTOBefore.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId).collect(Collectors.toList());
                strategyMeasureDetailService.logicDeleteStrategyMeasureDetailByStrategyMeasureDetailIds(strategyMeasureDetailBeforeIds);
                strategyMeasureTaskService.logicDeleteStrategyMeasureTaskByStrategyMeasureDetailIds(strategyMeasureDetailBeforeIds);
            }
            // 新增
            List<StrategyMeasureDetailDTO> addStrategyMeasureDetailDTOS = strategyMeasureDetailDTOAfter.stream().filter(strategyMeasureDetailDTO ->
                    !strategyMeasureDetailDTOBefore.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId)
                            .collect(Collectors.toList()).contains(strategyMeasureDetailDTO.getStrategyMeasureDetailId())).collect(Collectors.toList());
            List<StrategyMeasureDetail> strategyMeasureDetails = strategyMeasureDetailService.insertStrategyMeasureDetails(addStrategyMeasureDetailDTOS);
            List<StrategyMeasureTaskDTO> addStrategyMeasureTaskDTOS = new ArrayList<>();
            for (StrategyMeasureDetail addStrategyMeasureDetail : strategyMeasureDetails) {
                Long strategyMeasureDetailId = addStrategyMeasureDetail.getStrategyMeasureDetailId();
                int addSort = addStrategyMeasureDetail.getSort();
                int addSerialNumber = addStrategyMeasureDetail.getSerialNumber();
                for (Integer sorted : groupDetailMapS.keySet()) {
                    if (addSort == sorted) {
                        Map<Integer, List<StrategyMeasureDetailVO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
                        for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                            if (serialNumber == addSerialNumber) {
                                List<StrategyMeasureDetailVO> measureTaskVOS = groupSerialNumberMapS.get(serialNumber);
                                List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS = new ArrayList<>();
                                for (StrategyMeasureDetailVO measureTaskVO : measureTaskVOS) {
                                    StrategyMeasureTaskDTO strategyMeasureTaskDTO = new StrategyMeasureTaskDTO();
                                    strategyMeasureTaskDTO.setStrategyMeasureDetailId(strategyMeasureDetailId);
                                    strategyMeasureTaskDTO.setStrategyMeasureId(strategyMeasureId);
                                    BeanUtils.copyProperties(measureTaskVO, strategyMeasureTaskDTO);
                                    strategyMeasureTaskDTOS.add(strategyMeasureTaskDTO);
                                }
                                addStrategyMeasureTaskDTOS.addAll(strategyMeasureTaskDTOS);
                            }
                        }
                    }
                }
            }
            // 新增任务Mapper
            // 编辑详情
            List<StrategyMeasureDetailDTO> editStrategyMeasureDetailDTOS = strategyMeasureDetailDTOAfter.stream().filter(strategyMeasureDetailDTO ->
                    strategyMeasureDetailDTOBefore.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId)
                            .collect(Collectors.toList()).contains(strategyMeasureDetailDTO.getStrategyMeasureDetailId())).collect(Collectors.toList());
            List<Long> editStrategyMeasureDetailIds = editStrategyMeasureDetailDTOS.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId).collect(Collectors.toList());
            List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOSBefore = strategyMeasureTaskService.selectStrategyMeasureTaskByStrategyMeasureIds(editStrategyMeasureDetailIds);
            for (StrategyMeasureDetailDTO editStrategyMeasureDetailDTO : editStrategyMeasureDetailDTOS) {
                Long strategyMeasureDetailId = editStrategyMeasureDetailDTO.getStrategyMeasureDetailId();
                int editSort = editStrategyMeasureDetailDTO.getSort();
                int editSerialNumber = editStrategyMeasureDetailDTO.getSerialNumber();
                for (Integer sorted : groupDetailMapS.keySet()) {
                    if (editSort == sorted) {
                        Map<Integer, List<StrategyMeasureDetailVO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
                        for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                            if (serialNumber == editSerialNumber) {
                                List<StrategyMeasureDetailVO> measureTaskVOS = groupSerialNumberMapS.get(serialNumber);

                            }
                        }
                    }
                }
            }

            // 新增

        }

        Map<Integer, List<StrategyMeasureDetailDTO>> groupStrategyMeasureDetail = strategyMeasureDetailDTOBefore.stream().collect(Collectors.groupingBy(StrategyMeasureDetailDTO::getSerialNumber));

        return 1;
    }

    /**
     * 编辑战略衡量指标
     *
     * @param strategyMeasureDTO 战略清单DTO
     * @param strategyMeasureId  战略清单ID
     */
    private void editStrategyMetrics(StrategyMeasureDTO strategyMeasureDTO, Long strategyMeasureId) {
        StrategyMetricsDTO strategyMetricsByMeasureId = strategyMetricsService.selectStrategyMetricsByStrategyMeasureId(strategyMeasureId);
        if (StringUtils.isNull(strategyMetricsByMeasureId)) {
            throw new ServiceException("数据异常 战略衡量指标数据丢失");
        }
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMetricsByMeasureId);
        strategyMetricsService.updateStrategyMetrics(strategyMetricsByMeasureId);
    }

    /**
     * 更新战略清单表
     *
     * @param strategyMeasureDTO 战略清单DTO
     */
    private Long editStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        strategyMeasureMapper.updateStrategyMeasure(strategyMeasure);
        return strategyMeasure.getStrategyMeasureId();
    }

    /**
     * 更新校验
     *
     * @param strategyMeasureDTO 战略清单DTO
     */
    private void checkStrategyMeasureOfUpdate(StrategyMeasureDTO strategyMeasureDTO) {
        if (StringUtils.isNull(strategyMeasureDTO)) {
            throw new ServiceException("请传参");
        }
        Long strategyMeasureId = strategyMeasureDTO.getStrategyMeasureId();
        Long planBusinessUnitId = strategyMeasureDTO.getPlanBusinessUnitId();
        StrategyMeasureDTO strategyMeasureById = strategyMeasureMapper.selectStrategyMeasureByStrategyMeasureId(strategyMeasureId);
        if (StringUtils.isNull(strategyMeasureById)) {
            throw new ServiceException("当前战略举措清单已经不存在");
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
        StrategyMeasure strategyMeasureParams = getStrategyMeasureParams(strategyMeasureDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = strategyMeasureById.getPlanYear();
        strategyMeasureParams.setPlanYear(planYear);
        strategyMeasureParams.setPlanBusinessUnitId(strategyMeasureById.getPlanBusinessUnitId());
        List<StrategyMeasureDTO> strategyMeasureDTOS = strategyMeasureMapper.selectStrategyMeasureList(strategyMeasureParams);
        if (StringUtils.isNotEmpty(strategyMeasureDTOS) && !strategyMeasureId.equals(strategyMeasureDTOS.get(0).getStrategyMeasureId())) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
    }

    /**
     * 逻辑批量删除战略举措清单表
     *
     * @param strategyMeasureIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMeasureByStrategyMeasureIds(List<Long> strategyMeasureIds) {
        return strategyMeasureMapper.logicDeleteStrategyMeasureByStrategyMeasureIds(strategyMeasureIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略举措清单表信息
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyMeasureByStrategyMeasureId(Long strategyMeasureId) {
        return strategyMeasureMapper.deleteStrategyMeasureByStrategyMeasureId(strategyMeasureId);
    }

    /**
     * 逻辑删除战略举措清单表信息
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMeasureByStrategyMeasureId(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        strategyMeasure.setStrategyMeasureId(strategyMeasureDTO.getStrategyMeasureId());
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        return strategyMeasureMapper.logicDeleteStrategyMeasureByStrategyMeasureId(strategyMeasure);
    }

    /**
     * 物理删除战略举措清单表信息
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */

    @Override
    public int deleteStrategyMeasureByStrategyMeasureId(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        return strategyMeasureMapper.deleteStrategyMeasureByStrategyMeasureId(strategyMeasure.getStrategyMeasureId());
    }

    /**
     * 物理批量删除战略举措清单表
     *
     * @param strategyMeasureDtos 需要删除的战略举措清单表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyMeasureByStrategyMeasureIds(List<StrategyMeasureDTO> strategyMeasureDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyMeasureDTO strategyMeasureDTO : strategyMeasureDtos) {
            stringList.add(strategyMeasureDTO.getStrategyMeasureId());
        }
        return strategyMeasureMapper.deleteStrategyMeasureByStrategyMeasureIds(stringList);
    }

    /**
     * 批量新增战略举措清单表信息
     *
     * @param strategyMeasureDtos 战略举措清单表对象
     */

    public int insertStrategyMeasures(List<StrategyMeasureDTO> strategyMeasureDtos) {
        List<StrategyMeasure> strategyMeasureList = new ArrayList<>();

        for (StrategyMeasureDTO strategyMeasureDTO : strategyMeasureDtos) {
            StrategyMeasure strategyMeasure = new StrategyMeasure();
            BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
            strategyMeasure.setCreateBy(SecurityUtils.getUserId());
            strategyMeasure.setCreateTime(DateUtils.getNowDate());
            strategyMeasure.setUpdateTime(DateUtils.getNowDate());
            strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
            strategyMeasure.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyMeasureList.add(strategyMeasure);
        }
        return strategyMeasureMapper.batchStrategyMeasure(strategyMeasureList);
    }

    /**
     * 批量修改战略举措清单表信息
     *
     * @param strategyMeasureDtos 战略举措清单表对象
     */

    public int updateStrategyMeasures(List<StrategyMeasureDTO> strategyMeasureDtos) {
        List<StrategyMeasure> strategyMeasureList = new ArrayList<>();

        for (StrategyMeasureDTO strategyMeasureDTO : strategyMeasureDtos) {
            StrategyMeasure strategyMeasure = new StrategyMeasure();
            BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
            strategyMeasure.setCreateBy(SecurityUtils.getUserId());
            strategyMeasure.setCreateTime(DateUtils.getNowDate());
            strategyMeasure.setUpdateTime(DateUtils.getNowDate());
            strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
            strategyMeasureList.add(strategyMeasure);
        }
        return strategyMeasureMapper.updateStrategyMeasures(strategyMeasureList);
    }

}

