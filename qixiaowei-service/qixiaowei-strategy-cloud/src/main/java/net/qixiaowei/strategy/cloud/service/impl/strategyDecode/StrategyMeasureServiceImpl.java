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
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyIndexDimensionDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDetailDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
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
    private IStrategyMetricsPlanService strategyMetricsPlanService;

    @Autowired
    private IStrategyMetricsDetailService strategyMetricsDetailService;

    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    @Autowired
    private StrategyIndexDimensionMapper strategyIndexDimensionMapper;

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
    public StrategyMeasureDTO insertStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO) {
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
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        // 将规划业务单元的规划业务单元维度赋进主表
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        strategyMeasure.setCreateBy(SecurityUtils.getUserId());
        strategyMeasure.setCreateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        strategyMeasure.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMeasureMapper.insertStrategyMeasure(strategyMeasure);
        Long strategyMeasureId = strategyMeasure.getStrategyMeasureId();

        // 详情
        List<StrategyMeasureDetailVO> strategyMeasureDetailVOS = strategyMeasureDTO.getStrategyMeasureDetailVOS();
        // 负责人赋值
        if (StringUtils.isNotEmpty(strategyMeasureDetailVOS)) {
            List<Long> strategyIndexDimensionIds = strategyMeasureDetailVOS.stream().map(StrategyMeasureDetailVO::getStrategyIndexDimensionId).filter(Objects::nonNull).collect(Collectors.toList());
            List<Long> dutyEmployeeIds = strategyMeasureDetailVOS.stream().map(StrategyMeasureDetailVO::getDutyEmployeeId).filter(Objects::nonNull).collect(Collectors.toList());
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
            if (StringUtils.isEmpty(strategyIndexDimensionIds)) {
                throw new ServiceException("请选择维度");
            }
            List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionIds(strategyIndexDimensionIds);
            int sort = 0;
            for (int i = 0; i < strategyMeasureDetailVOS.size(); i++) {
                StrategyMeasureDetailVO strategyMeasureDetailVO = strategyMeasureDetailVOS.get(i);
                strategyMeasureDetailVO.setStrategyMeasureId(strategyMeasureId);
                // 任务排序
                strategyMeasureDetailVO.setTaskSort(i);
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
            List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOS = new ArrayList<>();
            Map<Integer, Map<Integer, List<StrategyMeasureDetailVO>>> groupDetailMapS =
                    strategyMeasureDetailVOS.stream().collect(Collectors.groupingBy(StrategyMeasureDetailVO::getSort, Collectors.groupingBy(StrategyMeasureDetailVO::getSerialNumber)));
            for (Integer sorted : groupDetailMapS.keySet()) {
                Map<Integer, List<StrategyMeasureDetailVO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
                for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                    StrategyMeasureDetailDTO strategyMeasureDetailDTO = new StrategyMeasureDetailDTO();
                    StrategyMeasureDetailVO measureDetailVO = groupSerialNumberMapS.get(serialNumber).get(0);
                    BeanUtils.copyProperties(measureDetailVO, strategyMeasureDetailDTO);
                    strategyMeasureDetailDTOS.add(strategyMeasureDetailDTO);
                }
            }

            List<StrategyMeasureDetail> strategyMeasureDetails = strategyMeasureDetailService.insertStrategyMeasureDetails(strategyMeasureDetailDTOS);
            for (StrategyMeasureDetailVO strategyMeasureDetailVO : strategyMeasureDetailVOS) {
                for (StrategyMeasureDetail strategyMeasureDetail : strategyMeasureDetails) {
                    if (strategyMeasureDetailVO.getSort().equals(strategyMeasureDetail.getSort()) && strategyMeasureDetailVO.getSerialNumber().equals(strategyMeasureDetail.getSerialNumber())) {
                        strategyMeasureDetailVO.setStrategyMeasureDetailId(strategyMeasureDetail.getStrategyMeasureDetailId());
                        break;
                    }
                }
            }

            List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS = new ArrayList<>();
            for (StrategyMeasureDetailVO strategyMeasureDetailVO : strategyMeasureDetailVOS) {
                StrategyMeasureTaskDTO strategyMeasureTaskDTO = new StrategyMeasureTaskDTO();
                BeanUtils.copyProperties(strategyMeasureDetailVO, strategyMeasureTaskDTO);
                strategyMeasureTaskDTOS.add(strategyMeasureTaskDTO);
            }

        }
        return strategyMeasureDTO;
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
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        BeanUtils.copyProperties(strategyMeasureDTO, strategyMeasure);
        strategyMeasure.setUpdateTime(DateUtils.getNowDate());
        strategyMeasure.setUpdateBy(SecurityUtils.getUserId());
        return strategyMeasureMapper.updateStrategyMeasure(strategyMeasure);
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

