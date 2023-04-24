package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
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
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasureTask;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetricsDetail;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.*;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyIndexDimensionMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMeasureMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.*;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDictionaryDataService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
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
    private RemoteUserService remoteUserService;

    @Autowired
    private IStrategyMeasureTaskService strategyMeasureTaskService;

    @Autowired
    private IStrategyMeasureDetailService strategyMeasureDetailService;

    @Autowired
    private IStrategyMetricsService strategyMetricsService;

    @Autowired
    private IStrategyMetricsDetailService strategyMetricsDetailService;

    @Autowired
    private IStrategyMetricsPlanService strategyMetricsPlanService;

    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    @Autowired
    private StrategyIndexDimensionMapper strategyIndexDimensionMapper;

    @Autowired
    private IStrategyIndexDimensionService strategyIndexDimensionService;

    @Autowired
    private RemoteDictionaryDataService remoteDictionaryDataService;

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
            strategyMeasureDTO.setBusinessUnitDecomposeName(PlanBusinessUnitCode.getBusinessUnitDecomposeName(businessUnitDecompose));
            strategyMeasureDTO.setBusinessUnitDecomposes(PlanBusinessUnitCode.getDropList(businessUnitDecompose));
        }
        setDecomposeValue(strategyMeasureDTO, businessUnitDecompose);
        // 详情
        List<StrategyMeasureDetailVO> strategyMeasureDetailVOS = strategyMeasureDetailService.selectStrategyMeasureDetailVOByStrategyMeasureId(strategyMeasureId);
        if (StringUtils.isEmpty(strategyMeasureDetailVOS)) {
            return strategyMeasureDTO;
        }
        // 来源赋值 strategyMeasureSource
        List<Long> strategyMeasureSources = strategyMeasureDetailVOS.stream().map(StrategyMeasureDetailVO::getStrategyMeasureSource).collect(Collectors.toList());
        R<List<DictionaryDataDTO>> strategyMeasureSourceDTOSR = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(strategyMeasureSources, SecurityConstants.INNER);
        List<DictionaryDataDTO> strategyMeasureSourceDTOS = strategyMeasureSourceDTOSR.getData();
        if (StringUtils.isNull(strategyMeasureSourceDTOS))
            throw new ServiceException("远程枚举失败 请联系管理员");
        for (StrategyMeasureDetailVO strategyMeasureDetailVO : strategyMeasureDetailVOS) {
            for (DictionaryDataDTO strategyMeasureSourceDTO : strategyMeasureSourceDTOS) {
                if (strategyMeasureSourceDTO.getDictionaryDataId().equals(strategyMeasureDetailVO.getStrategyMeasureSource())) {
                    strategyMeasureDetailVO.setStrategyMeasureSourceName(strategyMeasureSourceDTO.getDictionaryLabel());
                    break;
                }
            }
        }
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionService.selectStrategyIndexDimensionAllRootList(new StrategyIndexDimensionDTO());
        for (StrategyMeasureDetailVO strategyMeasureDetailVO : strategyMeasureDetailVOS) {
            for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
                if (strategyIndexDimensionDTO.getStrategyIndexDimensionId().equals(strategyMeasureDetailVO.getStrategyIndexDimensionId())) {
                    strategyMeasureDetailVO.setRootIndexDimensionName(strategyIndexDimensionDTO.getRootIndexDimensionName());
                    break;
                }
            }
        }
        List<DepartmentDTO> departmentDTOS;
        List<Long> dutyDepartmentIds = strategyMeasureDetailVOS.stream().map(StrategyMeasureDetailVO::getDutyDepartmentId).filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(dutyDepartmentIds)) {
            R<List<DepartmentDTO>> departmentDTOSR = departmentService.selectdepartmentIds(dutyDepartmentIds, SecurityConstants.INNER);
            departmentDTOS = departmentDTOSR.getData();
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
        }
        List<StrategyMeasureDetailVO> strategyMeasureDetailVOSList = getStrategyMeasureDetailVO(strategyMeasureDetailVOS);
        strategyMeasureDTO.setStrategyMeasureDetailVOS(strategyMeasureDetailVOS);
        return strategyMeasureDTO;
    }

    /**
     * 赋值-hasCodeAdd
     *
     * @param strategyMeasureDetailVOS 战略举措清单详情VOS
     * @return List
     */
    private List<StrategyMeasureDetailVO> getStrategyMeasureDetailVO(List<StrategyMeasureDetailVO> strategyMeasureDetailVOS) {
        Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupStrategyMeasureDetailVOS =
                strategyMeasureDetailVOS.stream().collect(Collectors.groupingBy(StrategyMeasureDetailVO::getSort, LinkedHashMap::new,
                        Collectors.groupingBy(StrategyMeasureDetailVO::getSerialNumber, LinkedHashMap::new, Collectors.toList())));
        for (Integer sort : groupStrategyMeasureDetailVOS.keySet()) {
            Map<Integer, List<StrategyMeasureDetailVO>> serialStrategyMeasureDetailVOS = groupStrategyMeasureDetailVOS.get(sort);
            int maxSerialNumber = 1;
            for (Integer serialNumber : serialStrategyMeasureDetailVOS.keySet()) {
                if (serialNumber > maxSerialNumber)
                    maxSerialNumber = serialNumber;
            }
            for (Integer serialNumber : serialStrategyMeasureDetailVOS.keySet()) {
                List<StrategyMeasureDetailVO> strategyMeasureDetailVOList = serialStrategyMeasureDetailVOS.get(serialNumber);
                if (serialNumber == maxSerialNumber) {
                    strategyMeasureDetailVOList.forEach(s -> {
                        s.setHasCodeAdd(true);
                    });
                } else {
                    strategyMeasureDetailVOList.forEach(s -> {
                        s.setHasCodeAdd(false);
                    });
                }
            }
        }
        List<StrategyMeasureDetailVO> strategyMeasureDetailVOSList = new ArrayList<>();
        for (Integer sort : groupStrategyMeasureDetailVOS.keySet()) {
            Map<Integer, List<StrategyMeasureDetailVO>> serialStrategyMeasureDetailVOS = groupStrategyMeasureDetailVOS.get(sort);
            for (Integer serialNumber : serialStrategyMeasureDetailVOS.keySet()) {
                List<StrategyMeasureDetailVO> strategyMeasureDetailVOList = serialStrategyMeasureDetailVOS.get(serialNumber);
                strategyMeasureDetailVOSList.addAll(strategyMeasureDetailVOList);
            }
        }
        return strategyMeasureDetailVOSList;
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
                if (StringUtils.isNotNull(areaDTO))
                    strategyMeasureDTO.setAreaName(areaDTO.getAreaName());
            }
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNotNull(departmentId)) {
                R<DepartmentDTO> departmentDTOR = departmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
                DepartmentDTO departmentDTO = departmentDTOR.getData();
                if (StringUtils.isNotNull(departmentDTO))
                    strategyMeasureDTO.setDepartmentName(departmentDTO.getDepartmentName());
            }
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNotNull(productId)) {
                R<ProductDTO> productDTOR = productService.remoteSelectById(productId, SecurityConstants.INNER);
                ProductDTO productDTO = productDTOR.getData();
                if (StringUtils.isNotNull(productDTO))
                    strategyMeasureDTO.setProductName(productDTO.getProductName());
            }
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNotNull(industryId)) {
                R<IndustryDTO> industryDTOR = industryService.selectById(industryId, SecurityConstants.INNER);
                IndustryDTO industryDTO = industryDTOR.getData();
                if (StringUtils.isNotNull(industryDTO))
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
        Map<String, Object> params = strategyMeasureDTO.getParams();
        if (StringUtils.isNull(params))
            params = new HashMap<>();
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        if (StringUtils.isNotNull(strategyMeasureDTO.getBusinessUnitName()))
            params.put("businessUnitName", strategyMeasureDTO.getBusinessUnitName());
        String createByName = strategyMeasureDTO.getCreateByName();
        List<String> createByList = new ArrayList<>();
        if (StringUtils.isNotEmpty(createByName)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(createByName);
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> employeeIds = userListData.stream().map(UserDTO::getUserId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(employeeIds)) {
                employeeIds.forEach(e -> createByList.add(String.valueOf(e)));
            } else {
                createByList.add("");
            }
        }
        params.put("createByList", createByList);
        this.queryEmployeeName(params);
        strategyMeasure.setParams(params);
        List<StrategyMeasureDTO> strategyMeasureDTOS = strategyMeasureMapper.selectStrategyMeasureList(strategyMeasure);
        if (StringUtils.isEmpty(strategyMeasureDTOS)) {
            return strategyMeasureDTOS;
        }
        // 赋值员工
        Set<Long> createBys = strategyMeasureDTOS.stream().map(StrategyMeasureDTO::getCreateBy).collect(Collectors.toSet());
        R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
        List<UserDTO> userDTOList = usersByUserIds.getData();
        if (StringUtils.isNotEmpty(userDTOList)) {
            for (StrategyMeasureDTO strategyMeasureDTO1 : strategyMeasureDTOS) {
                for (UserDTO userDTO : userDTOList) {
                    if (strategyMeasureDTO1.getCreateBy().equals(userDTO.getUserId())) {
                        strategyMeasureDTO1.setCreateByName(userDTO.getEmployeeName());
                    }
                }
            }
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
            if (StringUtils.isEmpty(businessUnitDecompose))
                throw new ServiceException("数据异常 维度不存在 请联系管理员");
            Long areaId = measureDTO.getAreaId();
            Long industryId = measureDTO.getIndustryId();
            Long productId = measureDTO.getProductId();
            Long departmentId = measureDTO.getDepartmentId();
            if (businessUnitDecompose.contains("region") && StringUtils.isNotEmpty(areaDTOS)) {
                List<AreaDTO> areaDTOS1 = areaDTOS.stream().filter(areaDTO -> areaDTO.getAreaId().equals(areaId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(areaDTOS1)) {
                    String areaName = areaDTOS1.get(0).getAreaName();
                    measureDTO.setAreaName(areaName);
                }
            }
            if (businessUnitDecompose.contains("department") && StringUtils.isNotEmpty(departmentDTOS)) {
                List<DepartmentDTO> departmentDTOS1 = departmentDTOS.stream().filter(departmentDTO -> departmentDTO.getDepartmentId().equals(departmentId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(departmentDTOS1)) {
                    String departmentName = departmentDTOS1.get(0).getDepartmentName();
                    measureDTO.setDepartmentName(departmentName);
                }
            }
            if (businessUnitDecompose.contains("product") && StringUtils.isNotEmpty(productDTOS)) {
                List<ProductDTO> productDTOS1 = productDTOS.stream().filter(productDTO -> productDTO.getProductId().equals(productId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(productDTOS1)) {
                    String productName = productDTOS1.get(0).getProductName();
                    measureDTO.setProductName(productName);
                }
            }
            if (businessUnitDecompose.contains("industry") && StringUtils.isNotEmpty(industryDTOS)) {
                List<IndustryDTO> industryDTOS1 = industryDTOS.stream().filter(industryDTO -> industryDTO.getIndustryId().equals(industryId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(industryDTOS1)) {
                    String industryName = industryDTOS1.get(0).getIndustryName();
                    measureDTO.setIndustryName(industryName);
                }
            }
        }
        return strategyMeasureDTOS;
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
     * 新增战略举措清单表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    @Override
    @Transactional
    public StrategyMeasureDTO insertStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        Long planBusinessUnitId = strategyMeasureDTO.getPlanBusinessUnitId();
        Integer planYear = strategyMeasureDTO.getPlanYear();
        if (StringUtils.isNull(planBusinessUnitId))
            throw new ServiceException("规划业务单元ID不能为空");
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        this.checkMeasure(planBusinessUnitDTO, strategyMeasureDTO);
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        // 将规划业务单元的规划业务单元维度赋进主表
        this.addStrategyMeasure(planBusinessUnitDTO, strategyMeasureDTO, strategyMeasure);
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
            List<StrategyMeasureDetail> strategyMeasureDetails = new ArrayList<>();
            if (StringUtils.isNotEmpty(strategyMeasureDetailDTOS))
                strategyMeasureDetails = strategyMeasureDetailService.insertStrategyMeasureDetails(strategyMeasureDetailDTOS);
            // 新增战略衡量指标详情
            this.addMetricsDetail(strategyMetricsId, strategyMeasureDetails, planYear);
            // 新增战略清单详情任务表
            this.addMeasureTask(strategyMeasureId, groupDetailMapS, strategyMeasureDetails);
        }
        strategyMeasureDTO.setStrategyMeasureId(strategyMeasureId);
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
                        strategyMeasureDetailId = strategyMeasureDetail.getStrategyMeasureDetailId();// todo break;
                    }
                }
                if (StringUtils.isNull(strategyMeasureDetailId))
                    throw new ServiceException("新增失败 请联系管理员");
                for (StrategyMeasureDetailVO taskDTO : measureTaskDTOS) {
                    StrategyMeasureTaskDTO strategyMeasureTaskDTO = new StrategyMeasureTaskDTO();
                    BeanUtils.copyProperties(taskDTO, strategyMeasureTaskDTO);
                    strategyMeasureTaskDTO.setStrategyMeasureDetailId(strategyMeasureDetailId);
                    strategyMeasureTaskDTO.setStrategyMeasureId(strategyMeasureId);
                    strategyMeasureTaskDTO.setSort(taskDTO.getTaskSort());
                    strategyMeasureTaskDTOS.add(strategyMeasureTaskDTO);
                }
            }
        }
        if (StringUtils.isNotEmpty(strategyMeasureTaskDTOS))
            strategyMeasureTaskService.insertStrategyMeasureTasks(strategyMeasureTaskDTOS);
    }

    /**
     * 新增战略衡量指标详情
     *
     * @param strategyMetricsId      战略衡量指标ID
     * @param strategyMeasureDetails 战略衡量指标详情
     * @param planYear               规划年份
     */
    private void addMetricsDetail(Long strategyMetricsId, List<StrategyMeasureDetail> strategyMeasureDetails, Integer planYear) {
        List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOS = new ArrayList<>();
        for (StrategyMeasureDetail strategyMeasureDetail : strategyMeasureDetails) {
            StrategyMetricsDetailDTO strategyMetricsDetailDTO = new StrategyMetricsDetailDTO();
            BeanUtils.copyProperties(strategyMeasureDetail, strategyMetricsDetailDTO);
            strategyMetricsDetailDTO.setStrategyMetricsId(strategyMetricsId);
            strategyMetricsDetailDTOS.add(strategyMetricsDetailDTO);
        }
        if (StringUtils.isNotEmpty(strategyMetricsDetailDTOS)) {
            List<StrategyMetricsDetail> strategyMetricsDetails = strategyMetricsDetailService.insertStrategyMetricsDetails(strategyMetricsDetailDTOS);
            List<StrategyMetricsPlanDTO> strategyMetricsPlanDTOS = new ArrayList<>();
            for (StrategyMetricsDetail strategyMetricsDetail : strategyMetricsDetails) {
                StrategyMetricsPlanDTO strategyMetricsPlanDTO = new StrategyMetricsPlanDTO();
                strategyMetricsPlanDTO.setPlanYear(planYear);
                strategyMetricsPlanDTOS.add(strategyMetricsPlanDTO);
                strategyMetricsPlanDTO.setStrategyMetricsId(strategyMetricsId);
                strategyMetricsPlanDTO.setStrategyMetricsDetailId(strategyMetricsDetail.getStrategyMetricsDetailId());
            }
            strategyMetricsPlanService.insertStrategyMetricsPlans(strategyMetricsPlanDTOS);
        }
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
        int sort = 0;
        for (int i = 0; i < strategyMeasureDetailVOS.size(); i++) {
            StrategyMeasureDetailVO strategyMeasureDetailVO = strategyMeasureDetailVOS.get(i);
            strategyMeasureDetailVO.setStrategyMeasureId(strategyMeasureId);
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
            if (StringUtils.isEmpty(employeeDTOS)) {
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
     * @param planBusinessUnitDTO 计划单元DTO
     * @param strategyMeasureDTO  战略清单DTO
     */
    private void checkMeasure(PlanBusinessUnitDTO planBusinessUnitDTO, StrategyMeasureDTO strategyMeasureDTO) {
        if (StringUtils.isNull(strategyMeasureDTO)) {
            throw new ServiceException("请传参");
        }
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
     * @param planBusinessUnitDTO 计划单元DTO
     * @param strategyMeasureDTO  清单DTO
     * @param strategyMeasure     清单
     */
    private void addStrategyMeasure(PlanBusinessUnitDTO planBusinessUnitDTO, StrategyMeasureDTO strategyMeasureDTO, StrategyMeasure strategyMeasure) {
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        strategyMeasure.setBusinessUnitDecompose(planBusinessUnitDTO.getBusinessUnitDecompose());
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
            if (StringUtils.isNull(areaId))
                throw new ServiceException("请选择区域");
            strategyMeasure.setAreaId(areaId);
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNull(departmentId))
                throw new ServiceException("请选择部门");
            strategyMeasure.setDepartmentId(departmentId);
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNull(productId))
                throw new ServiceException("请选择产品");
            strategyMeasure.setProductId(productId);
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNull(industryId))
                throw new ServiceException("请选择行业");
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
    @Transactional
    public int updateStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        this.checkStrategyMeasureOfUpdate(strategyMeasureDTO);
        // 编辑战略举措清单
        Long strategyMeasureId = editStrategyMeasure(strategyMeasureDTO);
        // 编辑战略衡量指标
        this.editStrategyMetrics(strategyMeasureDTO, strategyMeasureId);
        List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOBefore = strategyMeasureDetailService.selectStrategyMeasureDetailByStrategyMeasureId(strategyMeasureId);
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionService.selectStrategyIndexDimensionRootList(new StrategyIndexDimensionDTO());
        List<Long> strategyIndexDimensionIds = strategyIndexDimensionDTOS.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId).collect(Collectors.toList());
        List<StrategyMeasureDetailVO> strategyMeasureDetailVOS = strategyMeasureDTO.getStrategyMeasureDetailVOS();
        if (StringUtils.isEmpty(strategyMeasureDetailVOS))
            throw new ServiceException("战略清单详情表不正确");
        // 负责人赋值
        this.setDutyEmployeeValue(strategyMeasureDetailVOS);
        // 第一层排序
        this.setSortValue(strategyMeasureId, strategyMeasureDetailVOS, strategyIndexDimensionIds);
        List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOAfter = new ArrayList<>();
        Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupDetailMapS =
                strategyMeasureDetailVOS.stream().collect(Collectors.groupingBy(StrategyMeasureDetailVO::getSort, Collectors.groupingBy(StrategyMeasureDetailVO::getSerialNumber)));
        this.setTaskSortValue(strategyMeasureDetailDTOAfter, groupDetailMapS);
        List<StrategyMeasureTaskDTO> addStrategyMeasureTaskDTOS = new ArrayList<>();
        List<StrategyMeasureTaskDTO> editStrategyMeasureTaskDTOS = new ArrayList<>();
        // 新增
        addDetailAndTask(strategyMeasureId, strategyMeasureDetailDTOBefore, strategyMeasureDetailDTOAfter, groupDetailMapS, addStrategyMeasureTaskDTOS);
        // 删除
        List<Long> delStrategyMeasureTaskIds = delDetailAndTask(strategyMeasureDetailDTOBefore, strategyMeasureDetailDTOAfter);
        // 编辑
        editDetailAndTask(strategyMeasureId, strategyMeasureDetailDTOBefore, strategyMeasureDetailDTOAfter, groupDetailMapS, addStrategyMeasureTaskDTOS, editStrategyMeasureTaskDTOS, delStrategyMeasureTaskIds);
        if (StringUtils.isNotEmpty(addStrategyMeasureTaskDTOS))
            strategyMeasureTaskService.insertStrategyMeasureTasks(addStrategyMeasureTaskDTOS);
        if (StringUtils.isNotEmpty(editStrategyMeasureTaskDTOS))
            strategyMeasureTaskService.updateStrategyMeasureTasks(editStrategyMeasureTaskDTOS);
        if (StringUtils.isNotEmpty(delStrategyMeasureTaskIds))
            strategyMeasureTaskService.logicDeleteStrategyMeasureTaskByStrategyMeasureTaskIds(delStrategyMeasureTaskIds);
        return 1;
    }

    /**
     * 新增
     *
     * @param strategyMeasureId              战略清单ID
     * @param strategyMeasureDetailDTOBefore 战略清单详情-前
     * @param strategyMeasureDetailDTOAfter  战略清单详情-后
     * @param groupDetailMapS                分组的VO
     * @param addStrategyMeasureTaskDTOS     新增的任务
     */
    private void addDetailAndTask(Long strategyMeasureId, List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOBefore, List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOAfter,
                                  Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupDetailMapS, List<StrategyMeasureTaskDTO> addStrategyMeasureTaskDTOS) {
        List<StrategyMeasureDetailDTO> addStrategyMeasureDetailDTOS = strategyMeasureDetailDTOAfter.stream().filter(strategyMeasureDetailDTO ->
                !strategyMeasureDetailDTOBefore.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId)
                        .collect(Collectors.toList()).contains(strategyMeasureDetailDTO.getStrategyMeasureDetailId())).collect(Collectors.toList());
        List<StrategyMeasureDetail> strategyMeasureDetails = new ArrayList<>();
        if (StringUtils.isNotEmpty(addStrategyMeasureDetailDTOS))
            strategyMeasureDetails = strategyMeasureDetailService.insertStrategyMeasureDetails(addStrategyMeasureDetailDTOS);
        for (StrategyMeasureDetail addStrategyMeasureDetail : strategyMeasureDetails) {
            Long strategyMeasureDetailId = addStrategyMeasureDetail.getStrategyMeasureDetailId();
            int addSort = addStrategyMeasureDetail.getSort();
            int addSerialNumber = addStrategyMeasureDetail.getSerialNumber();
            for (Integer sorted : groupDetailMapS.keySet()) {
                if (addSort == sorted) {
                    Map<Integer, List<StrategyMeasureDetailVO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
                    for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                        if (addSerialNumber == serialNumber) {
                            List<StrategyMeasureDetailVO> measureTaskVOS = groupSerialNumberMapS.get(serialNumber);
                            List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS = new ArrayList<>();
                            for (StrategyMeasureDetailVO measureTaskVO : measureTaskVOS) {
                                StrategyMeasureTaskDTO strategyMeasureTaskDTO = new StrategyMeasureTaskDTO();
                                BeanUtils.copyProperties(measureTaskVO, strategyMeasureTaskDTO);
                                strategyMeasureTaskDTO.setStrategyMeasureDetailId(strategyMeasureDetailId);
                                strategyMeasureTaskDTO.setStrategyMeasureId(strategyMeasureId);
                                strategyMeasureTaskDTO.setSort(measureTaskVO.getTaskSort());
                                strategyMeasureTaskDTOS.add(strategyMeasureTaskDTO);
                            }
                            addStrategyMeasureTaskDTOS.addAll(strategyMeasureTaskDTOS);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * @param strategyMeasureDetailDTOBefore 战略清单详情-前
     * @param strategyMeasureDetailDTOAfter  战略清单详情-后
     * @return List
     */
    private List<Long> delDetailAndTask(List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOBefore, List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOAfter) {
        List<Long> delStrategyMeasureDetailIds = strategyMeasureDetailDTOBefore.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId).filter(strategyMeasureDetailId ->
                !strategyMeasureDetailDTOAfter.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId)
                        .collect(Collectors.toList()).contains(strategyMeasureDetailId)).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delStrategyMeasureDetailIds)) {
            strategyMeasureDetailService.logicDeleteStrategyMeasureDetailByStrategyMeasureDetailIds(delStrategyMeasureDetailIds);
            List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS = strategyMeasureTaskService.selectStrategyMeasureTaskByStrategyMeasureDetailIds(delStrategyMeasureDetailIds);
            return strategyMeasureTaskDTOS.stream().map(StrategyMeasureTaskDTO::getStrategyMeasureTaskId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * @param strategyMeasureId              战略清单ID
     * @param strategyMeasureDetailDTOBefore 战略清单详情-前
     * @param strategyMeasureDetailDTOAfter  战略清单详情-后
     * @param groupDetailMapS                分组后的VO
     * @param addStrategyMeasureTaskDTOS     新增的战略清单任务
     * @param editStrategyMeasureTaskDTOS    编辑的战略清单任务
     * @param delStrategyMeasureTaskIds      删除的战略清单任务
     */
    private void editDetailAndTask(Long strategyMeasureId, List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOBefore, List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOAfter,
                                   Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupDetailMapS, List<StrategyMeasureTaskDTO> addStrategyMeasureTaskDTOS,
                                   List<StrategyMeasureTaskDTO> editStrategyMeasureTaskDTOS, List<Long> delStrategyMeasureTaskIds) {
        List<StrategyMeasureDetailDTO> editStrategyMeasureDetailDTOS = strategyMeasureDetailDTOAfter.stream().filter(strategyMeasureDetailDTO ->
                strategyMeasureDetailDTOBefore.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId)
                        .collect(Collectors.toList()).contains(strategyMeasureDetailDTO.getStrategyMeasureDetailId())).collect(Collectors.toList());
        for (StrategyMeasureDetailDTO editStrategyMeasureDetailDTO : editStrategyMeasureDetailDTOS) {
            for (StrategyMeasureDetailDTO strategyMeasureDetailDTO : strategyMeasureDetailDTOBefore) {
                if (editStrategyMeasureDetailDTO.getSort().equals(strategyMeasureDetailDTO.getSort())
                        && editStrategyMeasureDetailDTO.getSerialNumber().equals(strategyMeasureDetailDTO.getSerialNumber())) {
                    editStrategyMeasureDetailDTO.setStrategyMeasureId(strategyMeasureDetailDTO.getStrategyMeasureId());
                }
            }
        }
        // 编辑
        if (StringUtils.isNotEmpty(editStrategyMeasureDetailDTOS))
            strategyMeasureDetailService.updateStrategyMeasureDetails(editStrategyMeasureDetailDTOS);
        List<Long> editStrategyMeasureDetailIds = editStrategyMeasureDetailDTOS.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId).collect(Collectors.toList());
        List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOSBefore = strategyMeasureTaskService.selectStrategyMeasureTaskByStrategyMeasureDetailIds(editStrategyMeasureDetailIds);
        // 任务
        for (StrategyMeasureDetailDTO editStrategyMeasureDetailDTO : editStrategyMeasureDetailDTOS) {
            Long strategyMeasureDetailId = editStrategyMeasureDetailDTO.getStrategyMeasureDetailId();
            int editSort = editStrategyMeasureDetailDTO.getSort();
            int editSerialNumber = editStrategyMeasureDetailDTO.getSerialNumber();
            for (Integer sorted : groupDetailMapS.keySet()) {
                if (editSort == sorted) {
                    Map<Integer, List<StrategyMeasureDetailVO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
                    for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                        if (serialNumber == editSerialNumber) {
                            List<StrategyMeasureTaskDTO> measureTaskBefore = strategyMeasureTaskDTOSBefore.stream().filter(s ->
                                    s.getStrategyMeasureDetailId().equals(strategyMeasureDetailId)).collect(Collectors.toList());
                            List<StrategyMeasureTaskDTO> measureTaskAfter = new ArrayList<>();
                            List<StrategyMeasureDetailVO> measureTaskVOS = groupSerialNumberMapS.get(serialNumber);
                            for (StrategyMeasureDetailVO measureTaskVO : measureTaskVOS) {
                                StrategyMeasureTaskDTO strategyMeasureTaskDTO = new StrategyMeasureTaskDTO();
                                BeanUtils.copyProperties(measureTaskVO, strategyMeasureTaskDTO);
                                strategyMeasureTaskDTO.setSort(measureTaskVO.getTaskSort());
                                measureTaskAfter.add(strategyMeasureTaskDTO);
                            }
                            for (StrategyMeasureTaskDTO strategyMeasureTaskDTO : measureTaskAfter) {
                                strategyMeasureTaskDTO.setStrategyMeasureId(strategyMeasureId);
                                strategyMeasureTaskDTO.setStrategyMeasureDetailId(strategyMeasureDetailId);
                            }
                            // 处理增删改
                            operateMeasureTask(addStrategyMeasureTaskDTOS, editStrategyMeasureTaskDTOS, delStrategyMeasureTaskIds, measureTaskBefore, measureTaskAfter);
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理详情任务的增删改
     *
     * @param addStrategyMeasureTaskDTOS  新增任务表
     * @param editStrategyMeasureTaskDTOS 编辑任务表
     * @param delStrategyMeasureTaskIds   删除任务表
     * @param measureTaskBefore           任务-前
     * @param measureTaskAfter            任务-后
     */
    private static void operateMeasureTask(List<StrategyMeasureTaskDTO> addStrategyMeasureTaskDTOS, List<StrategyMeasureTaskDTO> editStrategyMeasureTaskDTOS, List<Long> delStrategyMeasureTaskIds, List<StrategyMeasureTaskDTO> measureTaskBefore, List<StrategyMeasureTaskDTO> measureTaskAfter) {
        //新增
        List<StrategyMeasureTaskDTO> addMeasureTask = measureTaskAfter.stream().filter(m ->
                !measureTaskBefore.stream().map(StrategyMeasureTaskDTO::getStrategyMeasureTaskId).collect(Collectors.toList()).contains(m.getStrategyMeasureTaskId())).collect(Collectors.toList());
        //编辑
        List<StrategyMeasureTaskDTO> editMeasureTask = measureTaskAfter.stream().filter(m ->
                measureTaskBefore.stream().map(StrategyMeasureTaskDTO::getStrategyMeasureTaskId).collect(Collectors.toList()).contains(m.getStrategyMeasureTaskId())).collect(Collectors.toList());
        //删除
        List<Long> delMeasureTask = measureTaskBefore.stream().map(StrategyMeasureTaskDTO::getStrategyMeasureTaskId).
                filter(strategyMeasureTaskId ->
                        !measureTaskAfter.stream().map(StrategyMeasureTaskDTO::getStrategyMeasureTaskId).collect(Collectors.toList()).contains(strategyMeasureTaskId)).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(addMeasureTask)) {
            addStrategyMeasureTaskDTOS.addAll(addMeasureTask);
        }
        if (StringUtils.isNotEmpty(editMeasureTask)) {
            editStrategyMeasureTaskDTOS.addAll(editMeasureTask);
        }
        if (StringUtils.isNotEmpty(delMeasureTask)) {
            delStrategyMeasureTaskIds.addAll(delMeasureTask);
        }
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
        StrategyMetricsDTO strategyMetricsDTO = new StrategyMetricsDTO();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMetricsDTO);
        strategyMetricsService.editStrategyMetrics(strategyMetricsDTO);
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
    @Transactional
    public int logicDeleteStrategyMeasureByStrategyMeasureIds(List<Long> strategyMeasureIds) {
        if (StringUtils.isEmpty(strategyMeasureIds))
            throw new ServiceException("请选择战略举措清单");
        List<StrategyMeasureDTO> strategyMeasureDTOS = strategyMeasureMapper.selectStrategyMeasureByStrategyMeasureIds(strategyMeasureIds);
        if (StringUtils.isEmpty(strategyMeasureDTOS))
            throw new ServiceException("当前战略举措清单列表不存在");
        strategyMeasureMapper.logicDeleteStrategyMeasureByStrategyMeasureIds(strategyMeasureIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        // 战略举措详情列表
        List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOS = strategyMeasureDetailService.selectStrategyMeasureDetailByStrategyMeasureIds(strategyMeasureIds);
        if (StringUtils.isEmpty(strategyMeasureDetailDTOS))
            throw new ServiceException("当前战略举措清单详情列表不存在");
        List<Long> strategyMeasureDetailIds = strategyMeasureDetailDTOS.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId).collect(Collectors.toList());
        strategyMeasureDetailService.logicDeleteStrategyMeasureDetailByStrategyMeasureDetailIds(strategyMeasureDetailIds);
        // 战略举措详情任务表
        List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS = strategyMeasureTaskService.selectStrategyMeasureTaskByStrategyMeasureDetailIds(strategyMeasureDetailIds);
        if (StringUtils.isEmpty(strategyMeasureTaskDTOS))
            throw new ServiceException("当前战略举措清单详情任务列表不存在");
        List<Long> strategyMeasureTaskIds = strategyMeasureTaskDTOS.stream().map(StrategyMeasureTaskDTO::getStrategyMeasureTaskId).collect(Collectors.toList());
        strategyMeasureTaskService.logicDeleteStrategyMeasureTaskByStrategyMeasureTaskIds(strategyMeasureTaskIds);
        // 战略衡量指标
        List<StrategyMetricsDTO> strategyMetricsDTOS = strategyMetricsService.selectStrategyMetricsByStrategyMeasureIds(strategyMeasureIds);
        if (StringUtils.isEmpty(strategyMetricsDTOS))
            throw new ServiceException("当前战略衡量指标列表不存在");
        List<Long> strategyMetricsIds = strategyMetricsDTOS.stream().map(StrategyMetricsDTO::getStrategyMetricsId).collect(Collectors.toList());
        strategyMetricsService.logicDeleteStrategyMetricsByStrategyMetricsIds(strategyMetricsIds);
        // 战略衡量指标详情
        List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOS = strategyMetricsDetailService.selectStrategyMetricsDetailByStrategyMetricsIds(strategyMeasureIds);
        if (StringUtils.isEmpty(strategyMetricsDetailDTOS))
            throw new ServiceException("当前战略衡量指标详情列表不存在");
        List<Long> strategyMetricsDetailIds = strategyMetricsDetailDTOS.stream().map(StrategyMetricsDetailDTO::getStrategyMetricsDetailId).collect(Collectors.toList());
        strategyMetricsDetailService.logicDeleteStrategyMetricsDetailByStrategyMetricsDetailIds(strategyMetricsDetailIds);
        // 战略衡量指标详情任务
        List<StrategyMetricsPlanDTO> strategyMetricsPlanDTOS = strategyMetricsPlanService.selectStrategyMetricsPlanByStrategyMetricsDetailIds(strategyMetricsDetailIds);
        if (StringUtils.isEmpty(strategyMetricsPlanDTOS))
            return 1;
        List<Long> strategyMetricsPlanIds = strategyMetricsPlanDTOS.stream().map(StrategyMetricsPlanDTO::getStrategyMetricsPlanId).collect(Collectors.toList());
        strategyMetricsPlanService.logicDeleteStrategyMetricsPlanByStrategyMetricsPlanIds(strategyMetricsPlanIds);
        return 1;
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
     * 远程获取列表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return List
     */
    @Override
    public List<StrategyMeasureDTO> remoteStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        Map<String, Object> params = strategyMeasureDTO.getParams();
        strategyMeasure.setParams(params);
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        return strategyMeasureMapper.selectStrategyMeasureList(strategyMeasure);
    }

    /**
     * 远程引用查询详情
     *
     * @param strategyMeasureDetailVO 详情VO
     * @return 结果
     */
    @Override
    public List<StrategyMeasureTaskDTO> remoteDutyMeasure(StrategyMeasureDetailVO strategyMeasureDetailVO) {
        StrategyMeasureTask strategyMeasureTask = new StrategyMeasureTask();
        Map<String, Object> params = strategyMeasureDetailVO.getParams();
        strategyMeasureTask.setParams(params);
        BeanUtils.copyProperties(strategyMeasureDetailVO, strategyMeasureTask);
        return strategyMeasureMapper.remoteDutyMeasure(strategyMeasureTask);
    }

    /**
     * 逻辑删除战略举措清单表信息
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteStrategyMeasureByStrategyMeasureId(StrategyMeasureDTO strategyMeasureDTO) {
        Long strategyMeasureId = strategyMeasureDTO.getStrategyMeasureId();
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        strategyMeasure.setStrategyMeasureId(strategyMeasureId);
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        strategyMeasureMapper.logicDeleteStrategyMeasureByStrategyMeasureId(strategyMeasure);
        // 详情表
        List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOS = strategyMeasureDetailService.selectStrategyMeasureDetailByStrategyMeasureId(strategyMeasureId);
        if (StringUtils.isEmpty(strategyMeasureDetailDTOS))
            throw new ServiceException("数据异常 当前战略清单没有详情?");
        List<Long> strategyMeasureDetailIds = strategyMeasureDetailDTOS.stream().map(StrategyMeasureDetailDTO::getStrategyMeasureDetailId).collect(Collectors.toList());
        strategyMeasureDetailService.logicDeleteStrategyMeasureDetailByStrategyMeasureDetailIds(strategyMeasureDetailIds);
        // 详情任务表
        List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS = strategyMeasureTaskService.selectStrategyMeasureTaskByStrategyMeasureDetailIds(strategyMeasureDetailIds);
        if (StringUtils.isEmpty(strategyMeasureTaskDTOS))
            throw new ServiceException("数据异常 当前战略清单没有详情任务?");
        List<Long> strategyMeasureTaskIds = strategyMeasureTaskDTOS.stream().map(StrategyMeasureTaskDTO::getStrategyMeasureTaskId).collect(Collectors.toList());
        strategyMeasureTaskService.logicDeleteStrategyMeasureTaskByStrategyMeasureTaskIds(strategyMeasureTaskIds);
        // 战略衡量指标表
        StrategyMetricsDTO strategyMetricsDTO = strategyMetricsService.selectStrategyMetricsByStrategyMeasureId(strategyMeasureId);
        if (StringUtils.isNull(strategyMetricsDTO))
            throw new ServiceException("数据异常 当前战略衡量指标不存在?");
        Long strategyMetricsId = strategyMetricsDTO.getStrategyMetricsId();
        strategyMetricsService.logicDeleteStrategyMetricsByStrategyMetricsId(strategyMetricsDTO);
        // 战略衡量指标详情表
        List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOS = strategyMetricsDetailService.selectStrategyMetricsDetailByStrategyMetricsId(strategyMetricsId);
        if (StringUtils.isEmpty(strategyMetricsDetailDTOS))
            throw new ServiceException("数据异常 当前战略衡量指标没有详情任务?");
        List<Long> strategyMetricsDetailIds = strategyMetricsDetailDTOS.stream().map(StrategyMetricsDetailDTO::getStrategyMetricsDetailId).collect(Collectors.toList());
        strategyMetricsDetailService.logicDeleteStrategyMetricsDetailByStrategyMetricsDetailIds(strategyMetricsDetailIds);
        // 战略衡量指标计划表
        List<StrategyMetricsPlanDTO> strategyMetricsPlanDTOS = strategyMetricsPlanService.selectStrategyMetricsPlanByStrategyMetricsDetailIds(strategyMetricsDetailIds);
        if (StringUtils.isEmpty(strategyMetricsPlanDTOS))
            return 1;
        List<Long> strategyMetricsPlanIds = strategyMetricsPlanDTOS.stream().map(StrategyMetricsPlanDTO::getStrategyMetricsPlanId).collect(Collectors.toList());
        strategyMetricsPlanService.logicDeleteStrategyMetricsPlanByStrategyMetricsPlanIds(strategyMetricsPlanIds);
        return 1;
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

