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
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetrics;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetricsDetail;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyIndexDimensionDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDetailDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsPlanDTO;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyIndexDimensionMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMetricsDetailMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMetricsMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMetricsPlanMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyIndexDimensionService;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMetricsDetailService;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMetricsPlanService;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyMetricsService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * StrategyMetricsService业务层处理
 *
 * @author Graves
 * @since 2023-03-07
 */
@Service
public class StrategyMetricsServiceImpl implements IStrategyMetricsService {

    public static Map<String, String> BUSINESS_UNIT_DECOMPOSE_MAP = ImmutableMap.of(
            "region", "区域",
            "department", "部门",
            "product", "产品",
            "industry", "行业",
            "company", "公司级"
    );

    @Autowired
    private StrategyMetricsMapper strategyMetricsMapper;

    @Autowired
    private RemoteIndicatorService indicatorService;

    @Autowired
    private IStrategyMetricsDetailService strategyMetricsDetailService;

    @Autowired
    private StrategyIndexDimensionMapper strategyIndexDimensionMapper;

    @Autowired
    private RemoteAreaService areaService;

    @Autowired
    private RemoteProductService productService;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemoteIndustryService industryService;

    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    @Autowired
    private StrategyMetricsDetailMapper strategyMetricsDetailMapper;

    @Autowired
    private StrategyMetricsPlanMapper strategyMetricsPlanMapper;

    @Autowired
    private IStrategyMetricsPlanService strategyMetricsPlanService;

    @Autowired
    private IStrategyIndexDimensionService strategyIndexDimensionService;

    /**
     * 查询战略衡量指标表
     *
     * @param strategyMetricsId 战略衡量指标表主键
     * @return 战略衡量指标表
     */
    @Override
    public StrategyMetricsDTO selectStrategyMetricsByStrategyMetricsId(Long strategyMetricsId) {
        StrategyMetricsDTO strategyMetricsDTO = strategyMetricsMapper.selectStrategyMetricsByStrategyMetricsId(strategyMetricsId);
        if (StringUtils.isNull(strategyMetricsDTO)) {
            throw new ServiceException("战略衡量指标表已不存在");
        }
        Integer planPeriod = strategyMetricsDTO.getPlanPeriod();
        if (StringUtils.isNotNull(planPeriod))
            strategyMetricsDTO.setPlanPeriodName(planPeriod + "年");
        String businessUnitDecompose = strategyMetricsDTO.getBusinessUnitDecompose();
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
            strategyMetricsDTO.setBusinessUnitDecomposes(businessUnitDecomposes);
            strategyMetricsDTO.setBusinessUnitDecomposeName(businessUnitDecomposeNames.substring(0, businessUnitDecomposeNames.length() - 1));
        }
        setDecomposeValue(strategyMetricsDTO, businessUnitDecompose);
        // 详情
        List<StrategyMetricsDetailDTO> strategyMetricsDetailDTO = strategyMetricsDetailMapper.selectStrategyMetricsDetailByStrategyMetricsId(strategyMetricsId);
        if (StringUtils.isEmpty(strategyMetricsDetailDTO)) {
            return strategyMetricsDTO;
        }
        // 获取指标
        List<Long> indicatorIds = strategyMetricsDetailDTO.stream().map(StrategyMetricsDetailDTO::getIndicatorId).filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(indicatorIds)) {
            R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> indicatorDTOS = indicatorR.getData();
            if (StringUtils.isNotEmpty(indicatorDTOS)) {
                for (StrategyMetricsDetailDTO detailDTO : strategyMetricsDetailDTO) {
                    for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                        if (detailDTO.getIndicatorId().equals(indicatorDTO.getIndicatorId())) {
                            detailDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                            break;
                        }
                    }
                }
            }
        }

        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionService.selectStrategyIndexDimensionRootList();
        for (StrategyMetricsDetailDTO detailDTO : strategyMetricsDetailDTO) {
            for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
                if (strategyIndexDimensionDTO.getStrategyIndexDimensionId().equals(detailDTO.getStrategyIndexDimensionId())) {
                    detailDTO.setRootIndexDimensionName(strategyIndexDimensionDTO.getRootIndexDimensionName());
                    break;
                }
            }
        }
        List<Long> strategyMetricsDetailIds = strategyMetricsDetailDTO.stream().map(StrategyMetricsDetailDTO::getStrategyMetricsDetailId).collect(Collectors.toList());
        List<StrategyMetricsPlanDTO> strategyMetricsPlanDTOS = strategyMetricsPlanMapper.selectStrategyMetricsPlanByStrategyMetricsDetailIds(strategyMetricsDetailIds);
        for (StrategyMetricsDetailDTO metricsDetailDTO : strategyMetricsDetailDTO) {
            List<Map<String, Object>> periodList = new ArrayList<>();
            Long strategyMetricsDetailId = metricsDetailDTO.getStrategyMetricsDetailId();
            List<StrategyMetricsPlanDTO> sonStrategyMetricsPlanDTOS = strategyMetricsPlanDTOS.stream()
                    .filter(s -> s.getStrategyMetricsDetailId().equals(strategyMetricsDetailId)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(sonStrategyMetricsPlanDTOS)) {
                for (StrategyMetricsPlanDTO sonStrategyMetricsPlanDTO : sonStrategyMetricsPlanDTOS) {
                    Map<String, Object> period = new HashMap<>();
                    period.put("value", sonStrategyMetricsPlanDTO.getPlanValue());
                    period.put("year", sonStrategyMetricsPlanDTO.getPlanYear());
                    periodList.add(period);
                }
            }
            metricsDetailDTO.setPeriodList(periodList);
        }
        strategyMetricsDTO.setStrategyMetricsDetailDTOS(strategyMetricsDetailDTO);
        return strategyMetricsDTO;
    }

    /**
     * 根据维度进行赋值
     *
     * @param strategyMetricsDTO    衡量指标DTO
     * @param businessUnitDecompose 业务单元维度
     */
    private void setDecomposeValue(StrategyMetricsDTO strategyMetricsDTO, String businessUnitDecompose) {
        Long areaId = strategyMetricsDTO.getAreaId();
        Long departmentId = strategyMetricsDTO.getDepartmentId();
        Long productId = strategyMetricsDTO.getProductId();
        Long industryId = strategyMetricsDTO.getIndustryId();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNotNull(areaId)) {
                R<AreaDTO> areaDTOR = areaService.getById(areaId, SecurityConstants.INNER);
                AreaDTO areaDTO = areaDTOR.getData();
                if (StringUtils.isNull(areaDTO)) {
                    throw new ServiceException("当前区域配置的信息已删除 请联系管理员");
                }
                strategyMetricsDTO.setAreaName(areaDTO.getAreaName());
            }
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNotNull(departmentId)) {
                R<DepartmentDTO> departmentDTOR = departmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
                DepartmentDTO departmentDTO = departmentDTOR.getData();
                if (StringUtils.isNull(departmentDTO)) {
                    throw new ServiceException("当前部门配置的信息已删除 请联系管理员");
                }
                strategyMetricsDTO.setDepartmentName(departmentDTO.getDepartmentName());
            }
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNotNull(productId)) {
                R<ProductDTO> productDTOR = productService.remoteSelectById(productId, SecurityConstants.INNER);
                ProductDTO productDTO = productDTOR.getData();
                if (StringUtils.isNull(productDTO)) {
                    throw new ServiceException("当前产品配置的信息已删除 请联系管理员");
                }
                strategyMetricsDTO.setProductName(productDTO.getProductName());
            }
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNotNull(industryId)) {
                R<IndustryDTO> industryDTOR = industryService.selectById(industryId, SecurityConstants.INNER);
                IndustryDTO industryDTO = industryDTOR.getData();
                if (StringUtils.isNull(industryDTO)) {
                    throw new ServiceException("当前行业配置的信息已删除 请联系管理员");
                }
                strategyMetricsDTO.setIndustryName(industryDTO.getIndustryName());
            }
        }
    }

    /**
     * 查询战略衡量指标表列表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 战略衡量指标表
     */
    @Override
    public List<StrategyMetricsDTO> selectStrategyMetricsList(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        Map<String, Object> params = strategyMetrics.getParams();
        strategyMetrics.setParams(params);
        BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
        List<StrategyMetricsDTO> strategyMetricsDTOS = strategyMetricsMapper.selectStrategyMetricsList(strategyMetrics);
        if (StringUtils.isEmpty(strategyMetricsDTOS)) {
            return strategyMetricsDTOS;
        }
        List<Long> areaIds = strategyMetricsDTOS.stream().map(StrategyMetricsDTO::getAreaId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> departmentIds = strategyMetricsDTOS.stream().map(StrategyMetricsDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> productIds = strategyMetricsDTOS.stream().map(StrategyMetricsDTO::getProductId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> industryIds = strategyMetricsDTOS.stream().map(StrategyMetricsDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
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
        for (StrategyMetricsDTO metricsDTO : strategyMetricsDTOS) {
            String businessUnitDecompose = metricsDTO.getBusinessUnitDecompose();
            if (StringUtils.isEmpty(businessUnitDecompose))
                throw new ServiceException("数据异常 维度不存在 请联系管理员");
            Long areaId = metricsDTO.getAreaId();
            Long industryId = metricsDTO.getIndustryId();
            Long productId = metricsDTO.getProductId();
            Long departmentId = metricsDTO.getDepartmentId();
            if (businessUnitDecompose.contains("region") && StringUtils.isNotEmpty(areaDTOS)) {
                List<AreaDTO> areaDTOS1 = areaDTOS.stream().filter(areaDTO -> areaDTO.getAreaId().equals(areaId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(areaDTOS1)) {
                    String areaName = areaDTOS1.get(0).getAreaName();
                    metricsDTO.setAreaName(areaName);
                }
            }
            if (businessUnitDecompose.contains("department") && StringUtils.isNotEmpty(departmentDTOS)) {
                List<DepartmentDTO> departmentDTOS1 = departmentDTOS.stream().filter(departmentDTO -> departmentDTO.getDepartmentId().equals(departmentId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(departmentDTOS1)) {
                    String departmentName = departmentDTOS1.get(0).getDepartmentName();
                    metricsDTO.setDepartmentName(departmentName);
                }
            }
            if (businessUnitDecompose.contains("product") && StringUtils.isNotEmpty(productDTOS)) {
                List<ProductDTO> productDTOS1 = productDTOS.stream().filter(productDTO -> productDTO.getProductId().equals(productId)).collect(Collectors.toList());
                if (StringUtils.isEmpty(productDTOS1)) {
                    String productName = productDTOS1.get(0).getProductName();
                    metricsDTO.setProductName(productName);
                }
            }
            if (businessUnitDecompose.contains("industry") && StringUtils.isNotEmpty(industryDTOS)) {
                List<IndustryDTO> industryDTOS1 = industryDTOS.stream().filter(industryDTO -> industryDTO.getIndustryId().equals(industryId)).collect(Collectors.toList());
                if (StringUtils.isEmpty(industryDTOS1)) {
                    String industryName = industryDTOS1.get(0).getIndustryName();
                    metricsDTO.setIndustryName(industryName);
                }
            }
        }
        return strategyMetricsDTOS;
    }

    /**
     * 新增战略衡量指标表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */
    @Override
    public Long insertStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
        strategyMetrics.setCreateBy(SecurityUtils.getUserId());
        strategyMetrics.setCreateTime(DateUtils.getNowDate());
        strategyMetrics.setUpdateTime(DateUtils.getNowDate());
        strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
        strategyMetrics.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyMetricsMapper.insertStrategyMetrics(strategyMetrics);
        return strategyMetrics.getStrategyMetricsId();
    }

    /**
     * 修改战略衡量指标表
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO) {
        this.checkStrategyMetricsOfUpdate(strategyMetricsDTO);
        // 更新衡量指标表
        Long strategyMetricsId = editStrategyMetrics(strategyMetricsDTO);
        List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOBefore = strategyMetricsDetailMapper.selectStrategyMetricsDetailByStrategyMetricsId(strategyMetricsId);
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionService.selectStrategyIndexDimensionRootList();
        List<Long> strategyIndexDimensionIds = strategyIndexDimensionDTOS.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId).collect(Collectors.toList());
        List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOAfter = strategyMetricsDTO.getStrategyMetricsDetailDTOS();
        if (StringUtils.isEmpty(strategyMetricsDetailDTOAfter))
            throw new ServiceException("战略衡量指标表不正确");
        setSortValue(strategyMetricsId, strategyMetricsDetailDTOAfter, strategyIndexDimensionIds);
        Map<Integer, Map<Integer, List<StrategyMetricsDetailDTO>>> groupDetailMapS =
                strategyMetricsDetailDTOAfter.stream().collect(Collectors.groupingBy(StrategyMetricsDetailDTO::getSort, Collectors.groupingBy(StrategyMetricsDetailDTO::getSerialNumber)));
        List<StrategyMetricsPlanDTO> addStrategyMetricsPlanDTOS = new ArrayList<>();
        List<StrategyMetricsPlanDTO> editStrategyMetricsPlanDTOS = new ArrayList<>();
        // 新增
        addDetailAndPlan(strategyMetricsId, strategyMetricsDetailDTOBefore, strategyMetricsDetailDTOAfter, groupDetailMapS, addStrategyMetricsPlanDTOS);
        // 删除
        List<Long> delStrategyMetricsPlanIds = delDetailAndPlan(strategyMetricsDetailDTOBefore, strategyMetricsDetailDTOAfter);
        // 编辑
        editDetailAndPlan(strategyMetricsId, strategyMetricsDetailDTOBefore, strategyMetricsDetailDTOAfter, groupDetailMapS, strategyMetricsDTO,
                addStrategyMetricsPlanDTOS, editStrategyMetricsPlanDTOS, delStrategyMetricsPlanIds);
        if (StringUtils.isNotEmpty(addStrategyMetricsPlanDTOS))
            strategyMetricsPlanService.insertStrategyMetricsPlans(addStrategyMetricsPlanDTOS);
        if (StringUtils.isNotEmpty(editStrategyMetricsPlanDTOS))
            strategyMetricsPlanService.updateStrategyMetricsPlans(editStrategyMetricsPlanDTOS);
        if (StringUtils.isNotEmpty(delStrategyMetricsPlanIds))
            strategyMetricsPlanService.logicDeleteStrategyMetricsPlanByStrategyMetricsPlanIds(delStrategyMetricsPlanIds);
        return 1;
    }

    /**
     * @param strategyMetricsId              战略衡量指标ID
     * @param strategyMetricsDetailDTOBefore 战略衡量指标详情-前
     * @param strategyMetricsDetailDTOAfter  战略衡量指标详情-后
     * @param groupDetailMapS                分组后的VO
     * @param strategyMetricsDTO             战略衡量指标DTO
     * @param addStrategyMetricsPlanDTOS     新增的战略衡量指标任务
     * @param delStrategyMetricsPlanIds      删除的战略衡量指标任务
     * @param editStrategyMetricsPlanDTOS    编辑的战略衡量指标任务
     */
    private void editDetailAndPlan(Long strategyMetricsId, List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOBefore, List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOAfter,
                                   Map<Integer, Map<Integer, List<StrategyMetricsDetailDTO>>> groupDetailMapS, StrategyMetricsDTO strategyMetricsDTO, List<StrategyMetricsPlanDTO> addStrategyMetricsPlanDTOS,
                                   List<StrategyMetricsPlanDTO> editStrategyMetricsPlanDTOS, List<Long> delStrategyMetricsPlanIds) {
        List<StrategyMetricsDetailDTO> editStrategyMetricsDetailDTOS = strategyMetricsDetailDTOAfter.stream().filter(strategyMetricsDetailDTO ->
                strategyMetricsDetailDTOBefore.stream().map(StrategyMetricsDetailDTO::getStrategyMetricsDetailId)
                        .collect(Collectors.toList()).contains(strategyMetricsDetailDTO.getStrategyMetricsDetailId())).collect(Collectors.toList());
        for (StrategyMetricsDetailDTO editStrategyMetricsDetailDTO : editStrategyMetricsDetailDTOS) {
            for (StrategyMetricsDetailDTO strategyMetricsDetailDTO : strategyMetricsDetailDTOBefore) {
                if (editStrategyMetricsDetailDTO.getSort().equals(strategyMetricsDetailDTO.getSort())
                        && editStrategyMetricsDetailDTO.getSerialNumber().equals(strategyMetricsDetailDTO.getSerialNumber())) {
                    editStrategyMetricsDetailDTO.setStrategyMetricsId(strategyMetricsDetailDTO.getStrategyMetricsId());
                }
            }
        }
        // 编辑
        if (StringUtils.isNotEmpty(editStrategyMetricsDetailDTOS))
            strategyMetricsDetailService.updateStrategyMetricsDetails(editStrategyMetricsDetailDTOS);
        List<Long> editStrategyMetricsDetailIds = editStrategyMetricsDetailDTOS.stream().map(StrategyMetricsDetailDTO::getStrategyMetricsDetailId).collect(Collectors.toList());
        List<StrategyMetricsPlanDTO> strategyMetricsPlanDTOSBefore = strategyMetricsPlanMapper.selectStrategyMetricsPlanByStrategyMetricsDetailIds(editStrategyMetricsDetailIds);
        // 任务
        if (StringUtils.isNotNull(strategyMetricsDTO.getPlanPeriod()) && strategyMetricsDTO.getPlanPeriod() != 0) {
            for (StrategyMetricsDetailDTO editStrategyMetricsDetailDTO : editStrategyMetricsDetailDTOS) {
                Long strategyMetricsDetailId = editStrategyMetricsDetailDTO.getStrategyMetricsDetailId();
                int editSort = editStrategyMetricsDetailDTO.getSort();
                int editSerialNumber = editStrategyMetricsDetailDTO.getSerialNumber();
                for (Integer sorted : groupDetailMapS.keySet()) {
                    if (editSort == sorted) {
                        Map<Integer, List<StrategyMetricsDetailDTO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
                        for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                            if (serialNumber == editSerialNumber) {
                                List<StrategyMetricsPlanDTO> measurePlanBefore = strategyMetricsPlanDTOSBefore.stream().filter(s ->
                                        s.getStrategyMetricsDetailId().equals(strategyMetricsDetailId)).collect(Collectors.toList());
                                StrategyMetricsDetailDTO strategyMetricsDetailDTO = groupSerialNumberMapS.get(serialNumber).get(0);
                                List<Map<String, Object>> periodList = strategyMetricsDetailDTO.getPeriodList();
                                List<StrategyMetricsPlanDTO> measurePlanAfter = new ArrayList<>();
                                for (Map<String, Object> period : periodList) {
                                    StrategyMetricsPlanDTO strategyMetricsPlanDTO = new StrategyMetricsPlanDTO();
                                    if (StringUtils.isNotNull(period.get("value")))
                                        strategyMetricsPlanDTO.setPlanValue(new BigDecimal(Optional.ofNullable(period.get("value").toString()).orElse("0")));
                                    if (StringUtils.isNotNull(period.get("year")))
                                        strategyMetricsPlanDTO.setPlanYear(Integer.parseInt(period.get("year").toString()));
                                    measurePlanAfter.add(strategyMetricsPlanDTO);
                                }
                                for (StrategyMetricsPlanDTO strategyMetricsPlanDTO : measurePlanAfter) {
                                    strategyMetricsPlanDTO.setStrategyMetricsId(strategyMetricsId);
                                    strategyMetricsPlanDTO.setStrategyMetricsDetailId(strategyMetricsDetailId);
                                }
                                // 处理增删改
                                operateMetricsPlan(addStrategyMetricsPlanDTOS, editStrategyMetricsPlanDTOS, delStrategyMetricsPlanIds, measurePlanBefore, measurePlanAfter);

                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 处理衡量的增删改
     *
     * @param addStrategyMetricsPlanDTOS  新增任务表
     * @param editStrategyMetricsPlanDTOS 编辑任务表
     * @param delStrategyMetricsPlanIds   删除任务表
     * @param measurePlanBefore           任务-前
     * @param measurePlanAfter            任务-后
     */
    private void operateMetricsPlan
    (List<StrategyMetricsPlanDTO> addStrategyMetricsPlanDTOS, List<StrategyMetricsPlanDTO> editStrategyMetricsPlanDTOS,
     List<Long> delStrategyMetricsPlanIds, List<StrategyMetricsPlanDTO> measurePlanBefore, List<StrategyMetricsPlanDTO> measurePlanAfter) {
        //新增
        List<StrategyMetricsPlanDTO> addMeasurePlan = measurePlanAfter.stream().filter(m ->
                !measurePlanBefore.stream().map(StrategyMetricsPlanDTO::getPlanYear).collect(Collectors.toList()).contains(m.getPlanYear())).collect(Collectors.toList());
        //编辑
        List<StrategyMetricsPlanDTO> editMeasurePlan = measurePlanAfter.stream().filter(m ->
                measurePlanBefore.stream().map(StrategyMetricsPlanDTO::getPlanYear).collect(Collectors.toList()).contains(m.getPlanYear())).collect(Collectors.toList());
        //删除
        List<Long> delMeasurePlan = measurePlanBefore.stream().filter(m ->
                !measurePlanAfter.stream().map(StrategyMetricsPlanDTO::getPlanYear).collect(Collectors.toList())
                        .contains(m.getPlanYear())).map(StrategyMetricsPlanDTO::getStrategyMetricsPlanId).collect(Collectors.toList());
        for (StrategyMetricsPlanDTO strategyMetricsPlanDTO : editMeasurePlan) {
            for (StrategyMetricsPlanDTO metricsPlanDTO : measurePlanBefore) {
                if (strategyMetricsPlanDTO.getPlanYear().equals(metricsPlanDTO.getPlanYear())) {
                    strategyMetricsPlanDTO.setStrategyMetricsPlanId(metricsPlanDTO.getStrategyMetricsPlanId());
                    break;
                }
            }
        }
        if (StringUtils.isNotEmpty(addMeasurePlan)) {
            addStrategyMetricsPlanDTOS.addAll(addMeasurePlan);
        }
        if (StringUtils.isNotEmpty(editMeasurePlan)) {
            editStrategyMetricsPlanDTOS.addAll(editMeasurePlan);
        }
        if (StringUtils.isNotEmpty(delMeasurePlan)) {
            delStrategyMetricsPlanIds.addAll(delMeasurePlan);
        }
    }

    /**
     * @param strategyMetricsDetailDTOBefore 战略衡量指标详情-前
     * @param strategyMetricsDetailDTOAfter  战略衡量指标详情-后
     * @return List
     */
    private List<Long> delDetailAndPlan
    (List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOBefore, List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOAfter) {
        List<Long> delStrategyMetricsDetailIds = strategyMetricsDetailDTOBefore.stream().map(StrategyMetricsDetailDTO::getStrategyMetricsDetailId).filter(strategyMetricsDetailId ->
                !strategyMetricsDetailDTOAfter.stream().map(StrategyMetricsDetailDTO::getStrategyMetricsDetailId)
                        .collect(Collectors.toList()).contains(strategyMetricsDetailId)).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delStrategyMetricsDetailIds)) {
            strategyMetricsDetailService.logicDeleteStrategyMetricsDetailByStrategyMetricsDetailIds(delStrategyMetricsDetailIds);
            List<StrategyMetricsPlanDTO> strategyMetricsPlanDTOS = strategyMetricsPlanMapper.selectStrategyMetricsPlanByStrategyMetricsDetailIds(delStrategyMetricsDetailIds);
            return strategyMetricsPlanDTOS.stream().map(StrategyMetricsPlanDTO::getStrategyMetricsPlanId).collect(Collectors.toList());
        }
        return new ArrayList<>();
    }

    /**
     * 新增
     *
     * @param strategyMetricsId              战略衡量指标ID
     * @param strategyMetricsDetailDTOBefore 战略衡量指标详情-前
     * @param strategyMetricsDetailDTOAfter  战略衡量指标详情-后
     * @param groupDetailMapS                分组的VO
     * @param addStrategyMetricsPlanDTOS     新增的任务
     */
    private void addDetailAndPlan(Long strategyMetricsId, List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOBefore, List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOAfter,
                                  Map<Integer, Map<Integer, List<StrategyMetricsDetailDTO>>> groupDetailMapS, List<StrategyMetricsPlanDTO> addStrategyMetricsPlanDTOS) {
        List<StrategyMetricsDetailDTO> addStrategyMetricsDetailDTOS = strategyMetricsDetailDTOAfter.stream().filter(strategyMetricsDetailDTO ->
                !strategyMetricsDetailDTOBefore.stream().map(StrategyMetricsDetailDTO::getStrategyMetricsDetailId)
                        .collect(Collectors.toList()).contains(strategyMetricsDetailDTO.getStrategyMetricsDetailId())).collect(Collectors.toList());
        List<StrategyMetricsDetail> strategyMetricsDetails = new ArrayList<>();
        if (StringUtils.isNotEmpty(addStrategyMetricsDetailDTOS))
            strategyMetricsDetails = strategyMetricsDetailService.insertStrategyMetricsDetails(addStrategyMetricsDetailDTOS);
        for (StrategyMetricsDetail addStrategyMetricsDetail : strategyMetricsDetails) {
            Long strategyMetricsDetailId = addStrategyMetricsDetail.getStrategyMetricsDetailId();
            int addSort = addStrategyMetricsDetail.getSort();
            int addSerialNumber = addStrategyMetricsDetail.getSerialNumber();
            for (Integer sorted : groupDetailMapS.keySet()) {
                if (addSort == sorted) {
                    Map<Integer, List<StrategyMetricsDetailDTO>> groupSerialNumberMapS = groupDetailMapS.get(sorted);
                    for (Integer serialNumber : groupSerialNumberMapS.keySet()) {
                        if (addSerialNumber == serialNumber) {
                            StrategyMetricsDetailDTO metricsPlanDTO = groupSerialNumberMapS.get(serialNumber).get(0);
                            List<Map<String, Object>> periodList = metricsPlanDTO.getPeriodList();
                            List<StrategyMetricsPlanDTO> strategyMetricsPlanDTOS = new ArrayList<>();
                            for (Map<String, Object> period : periodList) {
                                StrategyMetricsPlanDTO strategyMetricsPlanDTO = new StrategyMetricsPlanDTO();
                                strategyMetricsPlanDTO.setStrategyMetricsDetailId(strategyMetricsDetailId);
                                strategyMetricsPlanDTO.setStrategyMetricsId(strategyMetricsId);
                                if (StringUtils.isNotNull(period.get("value")))
                                    strategyMetricsPlanDTO.setPlanValue(new BigDecimal(period.get("value").toString()));
                                if (StringUtils.isNotNull(period.get("year")))
                                    strategyMetricsPlanDTO.setPlanYear(Integer.parseInt(period.get("year").toString()));
                                strategyMetricsPlanDTOS.add(strategyMetricsPlanDTO);
                            }
                            addStrategyMetricsPlanDTOS.addAll(strategyMetricsPlanDTOS);
                            break;
                        }
                    }
                }
            }
        }
    }

    /**
     * 对详情排序
     *
     * @param strategyMetricsId         清单ID
     * @param strategyMetricsDetailDTOS 清单详情VO
     * @param strategyIndexDimensionIds 清单ID集合
     */
    private void setSortValue(Long
                                      strategyMetricsId, List<StrategyMetricsDetailDTO> strategyMetricsDetailDTOS, List<Long> strategyIndexDimensionIds) {
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionIds(strategyIndexDimensionIds);
        int sort = 0;
        for (int i = 0; i < strategyMetricsDetailDTOS.size(); i++) {
            StrategyMetricsDetailDTO strategyMetricsDetailDTO = strategyMetricsDetailDTOS.get(i);
            strategyMetricsDetailDTO.setStrategyMetricsId(strategyMetricsId);
//            任务排序
//            for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
//                if (strategyMetricsDetailVO.getStrategyIndexDimensionId().equals(strategyIndexDimensionDTO.getStrategyIndexDimensionId())) {
//                    String serialNumberName = strategyMetricsDetailVO.getSerialNumberName();
//                    String indexDimensionCode = strategyIndexDimensionDTO.getIndexDimensionCode();
//                    if (StringUtils.isNotEmpty(serialNumberName)) {
//                        if (!serialNumberName.contains(indexDimensionCode))
//                            throw new ServiceException("编码不匹配 请联系管理员");
//                        Integer serialNumber = Integer.valueOf(serialNumberName.substring(indexDimensionCode.length()));
//                        strategyMetricsDetailVO.setSerialNumber(serialNumber);
//                        break;
//                    }
//                }
//            }
            // 详情排序
            strategyMetricsDetailDTO.setSort(sort);
            if (i == strategyMetricsDetailDTOS.size() - 1)
                break;
            if (!strategyMetricsDetailDTOS.get(i + 1).getStrategyIndexDimensionId().equals(strategyMetricsDetailDTO.getStrategyIndexDimensionId()))
                sort += 1;
        }
    }

    /**
     * 更新衡量指标表
     *
     * @param strategyMetricsDTO 衡量指标DTO
     */
    public Long editStrategyMetrics(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
        strategyMetrics.setUpdateTime(DateUtils.getNowDate());
        strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
        strategyMetricsMapper.updateStrategyMetrics(strategyMetrics);
        return strategyMetrics.getStrategyMetricsId();
    }

    /**
     * 更新校验
     *
     * @param strategyMetricsDTO 战略衡量指标表
     */
    private void checkStrategyMetricsOfUpdate(StrategyMetricsDTO strategyMetricsDTO) {
        if (StringUtils.isNull(strategyMetricsDTO)) {
            throw new ServiceException("请传参");
        }
        Long strategyMetricsId = strategyMetricsDTO.getStrategyMetricsId();
        Long planBusinessUnitId = strategyMetricsDTO.getPlanBusinessUnitId();
        StrategyMetricsDTO strategyMetricsById = strategyMetricsMapper.selectStrategyMetricsByStrategyMetricsId(strategyMetricsId);
        if (StringUtils.isNull(strategyMetricsById)) {
            throw new ServiceException("当前战略衡量指标已经不存在");
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
        StrategyMetrics strategyMetricsParams = getStrategyMetricsParams(strategyMetricsDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = strategyMetricsById.getPlanYear();
        strategyMetricsParams.setPlanYear(planYear);
        strategyMetricsParams.setPlanBusinessUnitId(strategyMetricsById.getPlanBusinessUnitId());
        List<StrategyMetricsDTO> strategyMetricsDTOS = strategyMetricsMapper.selectStrategyMetricsList(strategyMetricsParams);
        if (StringUtils.isNotEmpty(strategyMetricsDTOS) && !strategyMetricsId.equals(strategyMetricsDTOS.get(0).getStrategyMetricsId())) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
    }

    /**
     * 获取战略衡量指标的入参
     * 校验入参的ID是否与维度匹配
     *
     * @param strategyMetricsDTO    量指标DTO
     * @param businessUnitDecompose 维度
     * @return 战略举措入参DTO
     */
    private StrategyMetrics getStrategyMetricsParams(StrategyMetricsDTO strategyMetricsDTO, String
            businessUnitDecompose) {
        Long areaId = strategyMetricsDTO.getAreaId();
        Long industryId = strategyMetricsDTO.getIndustryId();
        Long productId = strategyMetricsDTO.getProductId();
        Long departmentId = strategyMetricsDTO.getDepartmentId();
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNull(areaId))
                throw new ServiceException("请选择区域");
            strategyMetrics.setAreaId(areaId);
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNull(departmentId))
                throw new ServiceException("请选择部门");
            strategyMetrics.setDepartmentId(departmentId);
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNull(productId))
                throw new ServiceException("请选择产品");
            strategyMetrics.setProductId(productId);
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNull(industryId))
                throw new ServiceException("请选择行业");
            strategyMetrics.setIndustryId(industryId);
        }
        return strategyMetrics;
    }

    /**
     * 逻辑批量删除战略衡量指标表
     *
     * @param strategyMetricsIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMetricsByStrategyMetricsIds(List<Long> strategyMetricsIds) {
        return strategyMetricsMapper.logicDeleteStrategyMetricsByStrategyMetricsIds(strategyMetricsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略衡量指标表信息
     *
     * @param strategyMetricsId 战略衡量指标表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyMetricsByStrategyMetricsId(Long strategyMetricsId) {
        return strategyMetricsMapper.deleteStrategyMetricsByStrategyMetricsId(strategyMetricsId);
    }

    /**
     * 根据清单ID查询战略衡量指标
     *
     * @param strategyMeasureId 清单ID
     * @return 战略衡量指标
     */
    @Override
    public StrategyMetricsDTO selectStrategyMetricsByStrategyMeasureId(Long strategyMeasureId) {
        return strategyMetricsMapper.selectStrategyMetricsByStrategyMeasureId(strategyMeasureId);
    }

    /**
     * 逻辑删除战略衡量指标表信息
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyMetricsByStrategyMetricsId(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        strategyMetrics.setStrategyMetricsId(strategyMetricsDTO.getStrategyMetricsId());
        strategyMetrics.setUpdateTime(DateUtils.getNowDate());
        strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
        return strategyMetricsMapper.logicDeleteStrategyMetricsByStrategyMetricsId(strategyMetrics);
    }

    /**
     * 物理删除战略衡量指标表信息
     *
     * @param strategyMetricsDTO 战略衡量指标表
     * @return 结果
     */

    @Override
    public int deleteStrategyMetricsByStrategyMetricsId(StrategyMetricsDTO strategyMetricsDTO) {
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
        return strategyMetricsMapper.deleteStrategyMetricsByStrategyMetricsId(strategyMetrics.getStrategyMetricsId());
    }

    /**
     * 物理批量删除战略衡量指标表
     *
     * @param strategyMetricsDtos 需要删除的战略衡量指标表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyMetricsByStrategyMetricsIds(List<StrategyMetricsDTO> strategyMetricsDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyMetricsDTO strategyMetricsDTO : strategyMetricsDtos) {
            stringList.add(strategyMetricsDTO.getStrategyMetricsId());
        }
        return strategyMetricsMapper.deleteStrategyMetricsByStrategyMetricsIds(stringList);
    }

    /**
     * 批量新增战略衡量指标表信息
     *
     * @param strategyMetricsDtos 战略衡量指标表对象
     */

    public int insertStrategyMetricss(List<StrategyMetricsDTO> strategyMetricsDtos) {
        List<StrategyMetrics> strategyMetricsList = new ArrayList<>();

        for (StrategyMetricsDTO strategyMetricsDTO : strategyMetricsDtos) {
            StrategyMetrics strategyMetrics = new StrategyMetrics();
            BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
            strategyMetrics.setCreateBy(SecurityUtils.getUserId());
            strategyMetrics.setCreateTime(DateUtils.getNowDate());
            strategyMetrics.setUpdateTime(DateUtils.getNowDate());
            strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
            strategyMetrics.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyMetricsList.add(strategyMetrics);
        }
        return strategyMetricsMapper.batchStrategyMetrics(strategyMetricsList);
    }

    /**
     * 批量修改战略衡量指标表信息
     *
     * @param strategyMetricsDtos 战略衡量指标表对象
     */

    public int updateStrategyMetricss(List<StrategyMetricsDTO> strategyMetricsDtos) {
        List<StrategyMetrics> strategyMetricsList = new ArrayList<>();

        for (StrategyMetricsDTO strategyMetricsDTO : strategyMetricsDtos) {
            StrategyMetrics strategyMetrics = new StrategyMetrics();
            BeanUtils.copyProperties(strategyMetricsDTO, strategyMetrics);
            strategyMetrics.setCreateBy(SecurityUtils.getUserId());
            strategyMetrics.setCreateTime(DateUtils.getNowDate());
            strategyMetrics.setUpdateTime(DateUtils.getNowDate());
            strategyMetrics.setUpdateBy(SecurityUtils.getUserId());
            strategyMetricsList.add(strategyMetrics);
        }
        return strategyMetricsMapper.updateStrategyMetricss(strategyMetricsList);
    }
}

