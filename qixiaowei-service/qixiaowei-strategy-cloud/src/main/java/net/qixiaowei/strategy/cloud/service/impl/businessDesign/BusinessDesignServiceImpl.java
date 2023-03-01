package net.qixiaowei.strategy.cloud.service.impl.businessDesign;

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
import net.qixiaowei.strategy.cloud.api.domain.businessDesign.BusinessDesign;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignAxisConfigDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.mapper.businessDesign.BusinessDesignMapper;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignAxisConfigService;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignParamService;
import net.qixiaowei.strategy.cloud.service.businessDesign.IBusinessDesignService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;


/**
 * BusinessDesignService业务层处理
 *
 * @author Graves
 * @since 2023-02-28
 */
@Service
public class BusinessDesignServiceImpl implements IBusinessDesignService {
    @Autowired
    private BusinessDesignMapper businessDesignMapper;

    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    @Autowired
    private IBusinessDesignAxisConfigService businessDesignAxisConfigService;

    @Autowired
    private IBusinessDesignParamService businessDesignParamService;

    @Autowired
    private RemoteProductService productService;

    @Autowired
    private RemoteAreaService areaService;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemoteIndustryService industryService;

    /**
     * 查询业务设计表
     *
     * @param businessDesignId 业务设计表主键
     * @return 业务设计表
     */
    @Override
    public BusinessDesignDTO selectBusinessDesignByBusinessDesignId(Long businessDesignId) {
        BusinessDesignDTO businessDesignDTO = businessDesignMapper.selectBusinessDesignByBusinessDesignId(businessDesignId);
        if (StringUtils.isNull(businessDesignDTO)) {
            throw new ServiceException("当前的业务设计已不存在");
        }
        List<BusinessDesignParamDTO> businessDesignParamDTOS = businessDesignParamService.selectBusinessDesignParamByBusinessDesignId(businessDesignId);
        if (StringUtils.isNotEmpty(businessDesignParamDTOS)) {
//                综合毛利率综合订单额计算
            for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOS) {
                BigDecimal synthesizeGrossMargin = businessDesignParamDTO.getHistoryAverageRate().multiply(businessDesignParamDTO.getHistoryWeight())
                        .add(businessDesignParamDTO.getForecastRate().multiply(businessDesignParamDTO.getForecastWeight()));
                businessDesignParamDTO.setSynthesizeGrossMargin(synthesizeGrossMargin);
                BigDecimal synthesizeOrderAmount = businessDesignParamDTO.getHistoryOrderAmount().multiply(businessDesignParamDTO.getHistoryOrderWeight())
                        .add(businessDesignParamDTO.getForecastOrderAmount().multiply(businessDesignParamDTO.getForecastOrderWeight()));
                businessDesignParamDTO.setSynthesizeOrderAmount(synthesizeOrderAmount);
            }
            businessDesignDTO.setBusinessDesignParamDTOS(businessDesignParamDTOS);
        }
        return businessDesignDTO;
    }

    /**
     * 查询业务设计表列表
     *
     * @param businessDesignDTO 业务设计表
     * @return 业务设计表
     */
    @Override
    public List<BusinessDesignDTO> selectBusinessDesignList(BusinessDesignDTO businessDesignDTO) {
        BusinessDesign businessDesign = new BusinessDesign();
        Map<String, Object> params = businessDesignDTO.getParams();
        businessDesign.setParams(params);
        BeanUtils.copyProperties(businessDesignDTO, businessDesign);
        List<BusinessDesignDTO> businessDesignDTOS = businessDesignMapper.selectBusinessDesignList(businessDesign);
        if (StringUtils.isEmpty(businessDesignDTOS)) {
            return businessDesignDTOS;
        }
        List<Long> areaIds = businessDesignDTOS.stream().map(BusinessDesignDTO::getAreaId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> departmentIds = businessDesignDTOS.stream().map(BusinessDesignDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> productIds = businessDesignDTOS.stream().map(BusinessDesignDTO::getProductId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> industryIds = businessDesignDTOS.stream().map(BusinessDesignDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
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

        for (BusinessDesignDTO designDTO : businessDesignDTOS) {
            String businessUnitDecompose = designDTO.getBusinessUnitDecompose();
            Long areaId = designDTO.getAreaId();
            Long industryId = designDTO.getIndustryId();
            Long productId = designDTO.getProductId();
            Long departmentId = designDTO.getDepartmentId();
            if (businessUnitDecompose.contains("region") && StringUtils.isNotEmpty(areaDTOS)) {
                String areaName = areaDTOS.stream().filter(areaDTO -> areaDTO.getAreaId().equals(areaId)).collect(Collectors.toList()).get(0).getAreaName();
                designDTO.setAreaName(areaName);
            }
            if (businessUnitDecompose.contains("department") && StringUtils.isNotEmpty(departmentDTOS)) {
                String departmentName = departmentDTOS.stream().filter(departmentDTO -> departmentDTO.getDepartmentId().equals(departmentId)).collect(Collectors.toList()).get(0).getDepartmentName();
                designDTO.setDepartmentName(departmentName);
            }
            if (businessUnitDecompose.contains("product") && StringUtils.isNotEmpty(productDTOS)) {
                String productName = productDTOS.stream().filter(productDTO -> productDTO.getProductId().equals(productId)).collect(Collectors.toList()).get(0).getProductName();
                designDTO.setProductName(productName);
            }
            if (businessUnitDecompose.contains("industry") && StringUtils.isNotEmpty(industryDTOS)) {
                String industryName = industryDTOS.stream().filter(industryDTO -> industryDTO.getIndustryId().equals(industryId)).collect(Collectors.toList()).get(0).getIndustryName();
                designDTO.setIndustryName(industryName);
            }
        }
        return businessDesignDTOS;
    }

    /**
     * 新增业务设计表
     *
     * @param businessDesignDTO 业务设计表
     * @return 结果
     */
    @Override
    public BusinessDesignDTO insertBusinessDesign(BusinessDesignDTO businessDesignDTO) {
        if (StringUtils.isNull(businessDesignDTO)) {
            throw new ServiceException("请传参");
        }
        Long planBusinessUnitId = businessDesignDTO.getPlanBusinessUnitId();
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO)) {
            throw new ServiceException("当前的规划业务单元已经不存在");
        }
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        if (StringUtils.isNull(businessUnitDecompose)) {
            throw new ServiceException("规划业务单元维度数据异常 请检查规划业务单元配置");
        }
        // 判断是否选择校验：region,department,product,industry,company
        BusinessDesign businessDesignParams = getBusinessDesignParams(businessDesignDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = businessDesignDTO.getPlanYear();
        businessDesignParams.setPlanYear(planYear);
        businessDesignParams.setPlanBusinessUnitId(businessDesignDTO.getPlanBusinessUnitId());
        List<BusinessDesignDTO> businessDesignDTOS = businessDesignMapper.selectBusinessDesignList(businessDesignParams);
        if (StringUtils.isNotEmpty(businessDesignDTOS)) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
        BusinessDesign businessDesign = new BusinessDesign();
        BeanUtils.copyProperties(businessDesignDTO, businessDesign);
        businessDesign.setBusinessUnitDecompose(businessUnitDecompose);
        businessDesign.setCreateBy(SecurityUtils.getUserId());
        businessDesign.setCreateTime(DateUtils.getNowDate());
        businessDesign.setUpdateTime(DateUtils.getNowDate());
        businessDesign.setUpdateBy(SecurityUtils.getUserId());
        businessDesign.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        businessDesignMapper.insertBusinessDesign(businessDesign);
        Long businessDesignId = businessDesign.getBusinessDesignId();
        // 业务设计参数表
        List<BusinessDesignParamDTO> businessDesignParamDTOS = businessDesignDTO.getBusinessDesignParamDTOS();
        if (StringUtils.isNotEmpty(businessDesignParamDTOS)) {
            for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOS) {
                Long paramRelationId = businessDesignParamDTO.getParamRelationId();
                String paramName = businessDesignParamDTO.getParamName();
            }
            businessDesignParamService.insertBusinessDesignParams(businessDesignParamDTOS);
        }
        // 业务设计轴配置表
        List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDTOS = businessDesignDTO.getBusinessDesignAxisConfigDTOS();

        return businessDesignDTO;
    }

    /**
     * 校验入参的ID是否与维度匹配
     *
     * @param businessDesignDTO     业务设计DTO
     * @param businessUnitDecompose 维度
     * @return 业务设计入参DTO
     */
    private BusinessDesign getBusinessDesignParams(BusinessDesignDTO businessDesignDTO, String businessUnitDecompose) {
        Long areaId = businessDesignDTO.getAreaId();
        Long industryId = businessDesignDTO.getIndustryId();
        Long productId = businessDesignDTO.getProductId();
        Long departmentId = businessDesignDTO.getDepartmentId();
        BusinessDesign businessDesignParams = new BusinessDesign();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNull(areaId)) {
                throw new ServiceException("请选择区域");
            }
            businessDesignParams.setAreaId(areaId);
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNull(departmentId)) {
                throw new ServiceException("请选择部门");
            }
            businessDesignParams.setDepartmentId(departmentId);
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNull(productId)) {
                throw new ServiceException("请选择产品");
            }
            businessDesignParams.setProductId(productId);
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNull(industryId)) {
                throw new ServiceException("请选择行业");
            }
            businessDesignParams.setIndustryId(industryId);
        }
        return businessDesignParams;
    }

    /**
     * 修改业务设计表
     *
     * @param businessDesignDTO 业务设计表
     * @return 结果
     */
    @Override
    public int updateBusinessDesign(BusinessDesignDTO businessDesignDTO) {
        BusinessDesign businessDesign = new BusinessDesign();
        Long businessDesignId = businessDesignDTO.getBusinessDesignId();
        BusinessDesignDTO businessDesignById = businessDesignMapper.selectBusinessDesignByBusinessDesignId(businessDesignId);
        if (StringUtils.isNull(businessDesignById)) {
            throw new ServiceException("当前的业务设计不存在");
        }
        String businessUnitDecompose = businessDesignById.getBusinessUnitDecompose();
        // 判断是否选择校验：region,department,product,industry,company
        BusinessDesign businessDesignParams = getBusinessDesignParams(businessDesignDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = businessDesignById.getPlanYear();
        businessDesignParams.setPlanYear(planYear);
        businessDesignParams.setPlanBusinessUnitId(businessDesignById.getPlanBusinessUnitId());
        List<BusinessDesignDTO> businessDesignDTOS = businessDesignMapper.selectBusinessDesignList(businessDesignParams);
        if (StringUtils.isNotEmpty(businessDesignDTOS) && businessDesignId.equals(businessDesignDTOS.get(0).getBusinessDesignId())) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
        businessDesign.setUpdateTime(DateUtils.getNowDate());
        businessDesign.setUpdateBy(SecurityUtils.getUserId());
        BeanUtils.copyProperties(businessDesignDTO, businessDesign);
        businessDesignMapper.updateBusinessDesign(businessDesign);
        // 业务设计参数表
        List<BusinessDesignParamDTO> businessDesignParamDTOS = businessDesignDTO.getBusinessDesignParamDTOS();

        // 业务设计轴配置表
        List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDTOS = businessDesignDTO.getBusinessDesignAxisConfigDTOS();
        return 1;
    }

    /**
     * 逻辑批量删除业务设计表
     *
     * @param businessDesignIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteBusinessDesignByBusinessDesignIds(List<Long> businessDesignIds) {
        if (StringUtils.isEmpty(businessDesignIds)) {
            throw new ServiceException("请选择业务设计ID集合");
        }
        List<BusinessDesignDTO> businessDesignDTOS = businessDesignMapper.selectBusinessDesignByBusinessDesignIds(businessDesignIds);
        if (StringUtils.isEmpty(businessDesignDTOS)) {
            throw new ServiceException("当前的业务设计数据已不存在");
        }
        if (businessDesignDTOS.size() == businessDesignIds.size()) {
            throw new ServiceException("存在业务设计数据已不存在");
        }
        // 业务设计参数表
        businessDesignMapper.logicDeleteBusinessDesignByBusinessDesignIds(businessDesignIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<BusinessDesignParamDTO> businessDesignParamDTOS = businessDesignParamService.selectBusinessDesignParamByBusinessDesignIds(businessDesignIds);
        if (StringUtils.isNotEmpty(businessDesignParamDTOS)) {
            List<Long> businessDesignParamIds = businessDesignParamDTOS.stream().map(BusinessDesignParamDTO::getBusinessDesignParamId).collect(Collectors.toList());
            businessDesignParamService.logicDeleteBusinessDesignParamByBusinessDesignParamIds(businessDesignParamIds);
        }
        // 业务设计轴配置表
        List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDTOS = businessDesignAxisConfigService.selectBusinessDesignAxisConfigByBusinessDesignIds(businessDesignIds);
        if (StringUtils.isNotEmpty(businessDesignAxisConfigDTOS)) {
            List<Long> businessDesignAxisConfigIds = businessDesignAxisConfigDTOS.stream().map(BusinessDesignAxisConfigDTO::getBusinessDesignAxisConfigId).collect(Collectors.toList());
            businessDesignAxisConfigService.logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(businessDesignAxisConfigIds);
        }
        return 1;
    }

    /**
     * 物理删除业务设计表信息
     *
     * @param businessDesignId 业务设计表主键
     * @return 结果
     */
    @Override
    public int deleteBusinessDesignByBusinessDesignId(Long businessDesignId) {
        return businessDesignMapper.deleteBusinessDesignByBusinessDesignId(businessDesignId);
    }

    /**
     * 逻辑删除业务设计表信息
     *
     * @param businessDesignDTO 业务设计表
     * @return 结果
     */
    @Override
    public int logicDeleteBusinessDesignByBusinessDesignId(BusinessDesignDTO businessDesignDTO) {
        if (StringUtils.isNull(businessDesignDTO)) {
            throw new ServiceException("请传入业务设计参数");
        }
        Long businessDesignId = businessDesignDTO.getBusinessDesignId();
        BusinessDesign businessDesign = new BusinessDesign();
        businessDesign.setBusinessDesignId(businessDesignId);
        businessDesign.setUpdateTime(DateUtils.getNowDate());
        businessDesign.setUpdateBy(SecurityUtils.getUserId());
        businessDesignMapper.logicDeleteBusinessDesignByBusinessDesignId(businessDesign);
        // 业务设计参数表
        List<BusinessDesignParamDTO> businessDesignParamDTOS = businessDesignParamService.selectBusinessDesignParamByBusinessDesignId(businessDesignId);
        if (StringUtils.isNotEmpty(businessDesignParamDTOS)) {
            List<Long> businessDesignParamIds = businessDesignParamDTOS.stream().map(BusinessDesignParamDTO::getBusinessDesignParamId).collect(Collectors.toList());
            businessDesignParamService.logicDeleteBusinessDesignParamByBusinessDesignParamIds(businessDesignParamIds);
        }
        // 业务设计轴配置表
        List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDTOS = businessDesignAxisConfigService.selectBusinessDesignAxisConfigByBusinessDesignId(businessDesignId);
        if (StringUtils.isNotEmpty(businessDesignAxisConfigDTOS)) {
            List<Long> businessDesignAxisConfigIds = businessDesignAxisConfigDTOS.stream().map(BusinessDesignAxisConfigDTO::getBusinessDesignAxisConfigId).collect(Collectors.toList());
            businessDesignAxisConfigService.logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(businessDesignAxisConfigIds);
        }
        return 1;
    }

    /**
     * 物理删除业务设计表信息
     *
     * @param businessDesignDTO 业务设计表
     * @return 结果
     */

    @Override
    public int deleteBusinessDesignByBusinessDesignId(BusinessDesignDTO businessDesignDTO) {
        BusinessDesign businessDesign = new BusinessDesign();
        BeanUtils.copyProperties(businessDesignDTO, businessDesign);
        return businessDesignMapper.deleteBusinessDesignByBusinessDesignId(businessDesign.getBusinessDesignId());
    }

    /**
     * 物理批量删除业务设计表
     *
     * @param businessDesignDtos 需要删除的业务设计表主键
     * @return 结果
     */

    @Override
    public int deleteBusinessDesignByBusinessDesignIds(List<BusinessDesignDTO> businessDesignDtos) {
        List<Long> stringList = new ArrayList<>();
        for (BusinessDesignDTO businessDesignDTO : businessDesignDtos) {
            stringList.add(businessDesignDTO.getBusinessDesignId());
        }
        return businessDesignMapper.deleteBusinessDesignByBusinessDesignIds(stringList);
    }

    /**
     * 批量新增业务设计表信息
     *
     * @param businessDesignDtos 业务设计表对象
     */

    public int insertBusinessDesigns(List<BusinessDesignDTO> businessDesignDtos) {
        List<BusinessDesign> businessDesignList = new ArrayList<>();
        for (BusinessDesignDTO businessDesignDTO : businessDesignDtos) {
            BusinessDesign businessDesign = new BusinessDesign();
            BeanUtils.copyProperties(businessDesignDTO, businessDesign);
            businessDesign.setCreateBy(SecurityUtils.getUserId());
            businessDesign.setCreateTime(DateUtils.getNowDate());
            businessDesign.setUpdateTime(DateUtils.getNowDate());
            businessDesign.setUpdateBy(SecurityUtils.getUserId());
            businessDesign.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            businessDesignList.add(businessDesign);
        }
        return businessDesignMapper.batchBusinessDesign(businessDesignList);
    }

    /**
     * 批量修改业务设计表信息
     *
     * @param businessDesignDtos 业务设计表对象
     */

    public int updateBusinessDesigns(List<BusinessDesignDTO> businessDesignDtos) {
        List<BusinessDesign> businessDesignList = new ArrayList<>();

        for (BusinessDesignDTO businessDesignDTO : businessDesignDtos) {
            BusinessDesign businessDesign = new BusinessDesign();
            BeanUtils.copyProperties(businessDesignDTO, businessDesign);
            businessDesign.setCreateBy(SecurityUtils.getUserId());
            businessDesign.setCreateTime(DateUtils.getNowDate());
            businessDesign.setUpdateTime(DateUtils.getNowDate());
            businessDesign.setUpdateBy(SecurityUtils.getUserId());
            businessDesignList.add(businessDesign);
        }
        return businessDesignMapper.updateBusinessDesigns(businessDesignList);
    }

}

