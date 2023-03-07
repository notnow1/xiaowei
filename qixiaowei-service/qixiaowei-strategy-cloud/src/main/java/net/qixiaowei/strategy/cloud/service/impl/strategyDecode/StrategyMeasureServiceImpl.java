package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

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
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDetailDTO;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMeasureMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.*;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
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

    /**
     * 查询战略举措清单表
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 战略举措清单表
     */
    @Override
    public StrategyMeasureDTO selectStrategyMeasureByStrategyMeasureId(Long strategyMeasureId) {
        return strategyMeasureMapper.selectStrategyMeasureByStrategyMeasureId(strategyMeasureId);
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
        List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOS = strategyMeasureDTO.getStrategyMeasureDetailDTOS();

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

