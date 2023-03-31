package net.qixiaowei.strategy.cloud.service.impl.businessDesign;

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
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
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

    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private RemoteEmployeeService employeeService;

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
        String businessUnitDecompose = businessDesignDTO.getBusinessUnitDecompose();
        if (StringUtils.isNotEmpty(businessUnitDecompose)) {
            businessDesignDTO.setBusinessUnitDecomposeName(PlanBusinessUnitCode.getBusinessUnitDecomposeName(businessUnitDecompose));
            businessDesignDTO.setBusinessUnitDecomposes(PlanBusinessUnitCode.getDropList(businessUnitDecompose));
        }
        setDecomposeValue(businessDesignDTO);
        List<BusinessDesignParamDTO> businessDesignParamDTOS = businessDesignParamService.selectBusinessDesignParamByBusinessDesignId(businessDesignId);
        // 综合毛利率综合订单额计算
        if (StringUtils.isNotEmpty(businessDesignParamDTOS)) {
            for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOS) {
                BigDecimal synthesizeGrossMargin = businessDesignParamDTO.getHistoryAverageRate().multiply(businessDesignParamDTO.getHistoryWeight().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                        .add(businessDesignParamDTO.getForecastRate().multiply(businessDesignParamDTO.getForecastWeight()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                businessDesignParamDTO.setSynthesizeGrossMargin(synthesizeGrossMargin);
                BigDecimal synthesizeOrderAmount = businessDesignParamDTO.getHistoryOrderAmount().multiply(businessDesignParamDTO.getHistoryOrderWeight().divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP))
                        .add(businessDesignParamDTO.getForecastOrderAmount().multiply(businessDesignParamDTO.getForecastOrderWeight()).divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP));
                businessDesignParamDTO.setSynthesizeOrderAmount(synthesizeOrderAmount);
                Integer paramDimension = businessDesignParamDTO.getParamDimension();
                if (StringUtils.isNotNull(paramDimension)) {
                    switch (paramDimension) {
                        case 1:
                            businessDesignParamDTO.setParamDimensionName("产品");
                            break;
                        case 2:
                            businessDesignParamDTO.setParamDimensionName("客户");
                            break;
                        case 3:
                            businessDesignParamDTO.setParamDimensionName("区域");
                            break;
                    }
                }
            }
            businessDesignDTO.setBusinessDesignParamDTOS(businessDesignParamDTOS);
        } else {
            businessDesignDTO.setBusinessDesignParamDTOS(new ArrayList<>());
        }
        // 业务设计轴配置表
        List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDTOS = businessDesignAxisConfigService.selectBusinessDesignAxisConfigByBusinessDesignId(businessDesignId);
        if (StringUtils.isNotEmpty(businessDesignAxisConfigDTOS)) {
            Map<Integer, Map<Integer, List<BusinessDesignAxisConfigDTO>>> groupBusinessDesignAxisConfigDTOS = businessDesignAxisConfigDTOS.stream().
                    collect(Collectors.groupingBy(BusinessDesignAxisConfigDTO::getParamDimension, Collectors.groupingBy(BusinessDesignAxisConfigDTO::getCoordinateAxis)));
            List<Map<String, Object>> businessDesignAxisConfigMap = new ArrayList<>();
            for (Integer groupDesign : groupBusinessDesignAxisConfigDTOS.keySet()) {
                Map<String, Object> businessDesignAxisMap = new HashMap<>();
                businessDesignAxisMap.put("paramDimension", groupDesign);
                Map<Integer, List<BusinessDesignAxisConfigDTO>> groupLargeAxisConfigDTOS = groupBusinessDesignAxisConfigDTOS.get(groupDesign);
                for (Integer groupLargeDesign : groupLargeAxisConfigDTOS.keySet()) {
                    BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO = groupLargeAxisConfigDTOS.get(groupLargeDesign).get(0);
                    if (groupLargeDesign == 1) {
                        businessDesignAxisMap.put("upperValueX", businessDesignAxisConfigDTO.getUpperValue());
                        businessDesignAxisMap.put("lowerValueX", businessDesignAxisConfigDTO.getLowerValue());
                    } else {
                        businessDesignAxisMap.put("upperValueY", businessDesignAxisConfigDTO.getUpperValue());
                        businessDesignAxisMap.put("lowerValueY", businessDesignAxisConfigDTO.getLowerValue());
                    }
                }
                businessDesignAxisConfigMap.add(businessDesignAxisMap);
            }
            businessDesignDTO.setBusinessDesignAxisConfigMap(businessDesignAxisConfigMap);
        } else {
            businessDesignDTO.setBusinessDesignAxisConfigMap(new ArrayList<>());
        }
        return businessDesignDTO;
    }

    /**
     * 根据维度进行赋值
     *
     * @param businessDesignDTO 业务设计DTO
     */
    private void setDecomposeValue(BusinessDesignDTO businessDesignDTO) {
        Long areaId = businessDesignDTO.getAreaId();
        Long departmentId = businessDesignDTO.getDepartmentId();
        Long productId = businessDesignDTO.getProductId();
        Long industryId = businessDesignDTO.getIndustryId();
        AreaDTO areaDTO;
        if (StringUtils.isNotNull(areaId)) {
            R<AreaDTO> areaDTOR = areaService.getById(areaId, SecurityConstants.INNER);
            areaDTO = areaDTOR.getData();
            if (StringUtils.isNotNull(areaDTO))
                businessDesignDTO.setAreaName(areaDTO.getAreaName());
        }
        DepartmentDTO departmentDTO;
        if (StringUtils.isNotNull(departmentId)) {
            R<DepartmentDTO> departmentDTOR = departmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
            departmentDTO = departmentDTOR.getData();
            if (StringUtils.isNotNull(departmentDTO))
                businessDesignDTO.setDepartmentName(departmentDTO.getDepartmentName());
        }
        ProductDTO productDTO;
        if (StringUtils.isNotNull(productId)) {
            R<ProductDTO> productDTOR = productService.remoteSelectById(productId, SecurityConstants.INNER);
            productDTO = productDTOR.getData();
            if (StringUtils.isNotNull(productDTO))
                businessDesignDTO.setProductName(productDTO.getProductName());
        }
        IndustryDTO industryDTO;
        if (StringUtils.isNotNull(industryId)) {
            R<IndustryDTO> industryDTOR = industryService.selectById(industryId, SecurityConstants.INNER);
            industryDTO = industryDTOR.getData();
            if (StringUtils.isNotNull(industryDTO))
                businessDesignDTO.setIndustryName(industryDTO.getIndustryName());
        }
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
        if (StringUtils.isNull(params))
            params = new HashMap<>();
        BeanUtils.copyProperties(businessDesignDTO, businessDesign);
        if (StringUtils.isNotNull(businessDesignDTO.getBusinessUnitName()))
            params.put("businessUnitName", businessDesignDTO.getBusinessUnitName());
        String createByName = businessDesignDTO.getCreateByName();
        List<String> createByList = new ArrayList<>();
        if (StringUtils.isNotEmpty(createByName)) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(createByName);
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> employeeIds = userListData.stream().map(UserDTO::getUserId).filter(Objects::nonNull).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(employeeIds)) {
                employeeIds.forEach(e -> createByList.add(String.valueOf(e)));
            } else {
                createByList.add("");
            }
        }
        params.put("createByList", createByList);
        this.queryEmployeeName(params);
        businessDesign.setParams(params);
        List<BusinessDesignDTO> businessDesignDTOS = businessDesignMapper.selectBusinessDesignList(businessDesign);
        if (StringUtils.isEmpty(businessDesignDTOS)) {
            return businessDesignDTOS;
        }
        // map label value
        for (BusinessDesignDTO designDTO : businessDesignDTOS) {
            String businessUnitDecompose = designDTO.getBusinessUnitDecompose();
            designDTO.setBusinessUnitDecomposes(PlanBusinessUnitCode.getDropList(businessUnitDecompose));
        }
        // 赋值员工
        Set<Long> createBys = businessDesignDTOS.stream().map(BusinessDesignDTO::getCreateBy).collect(Collectors.toSet());
        R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
        List<UserDTO> userDTOList = usersByUserIds.getData();
        if (StringUtils.isNotEmpty(userDTOList)) {
            for (BusinessDesignDTO businessDesignDTO1 : businessDesignDTOS) {
                for (UserDTO userDTO : userDTOList) {
                    if (businessDesignDTO1.getCreateBy().equals(userDTO.getUserId())) {
                        businessDesignDTO1.setCreateByName(userDTO.getEmployeeName());
                    }
                }
            }
        }
        List<Long> areaIds = businessDesignDTOS.stream().map(BusinessDesignDTO::getAreaId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> departmentIds = businessDesignDTOS.stream().map(BusinessDesignDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> productIds = businessDesignDTOS.stream().map(BusinessDesignDTO::getProductId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
        List<Long> industryIds = businessDesignDTOS.stream().map(BusinessDesignDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
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

        for (BusinessDesignDTO designDTO : businessDesignDTOS) {
            String businessUnitDecompose = designDTO.getBusinessUnitDecompose();
            Long areaId = designDTO.getAreaId();
            Long industryId = designDTO.getIndustryId();
            Long productId = designDTO.getProductId();
            Long departmentId = designDTO.getDepartmentId();
            if (businessUnitDecompose.contains("region") && StringUtils.isNotEmpty(areaDTOS)) {
                List<AreaDTO> areaDTOS1 = areaDTOS.stream().filter(areaDTO -> areaDTO.getAreaId().equals(areaId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(areaDTOS1)) {
                    String areaName = areaDTOS1.get(0).getAreaName();
                    designDTO.setAreaName(areaName);
                }
            }
            if (businessUnitDecompose.contains("department") && StringUtils.isNotEmpty(departmentDTOS)) {
                List<DepartmentDTO> departmentDTOS1 = departmentDTOS.stream().filter(departmentDTO -> departmentDTO.getDepartmentId().equals(departmentId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(departmentDTOS1)) {
                    String departmentName = departmentDTOS1.get(0).getDepartmentName();
                    designDTO.setDepartmentName(departmentName);
                }
            }
            if (businessUnitDecompose.contains("product") && StringUtils.isNotEmpty(productDTOS)) {
                List<ProductDTO> productDTOS1 = productDTOS.stream().filter(productDTO -> productDTO.getProductId().equals(productId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(productDTOS1)) {
                    String productName = productDTOS1.get(0).getProductName();
                    designDTO.setProductName(productName);
                }
            }
            if (businessUnitDecompose.contains("industry") && StringUtils.isNotEmpty(industryDTOS)) {
                List<IndustryDTO> industryDTOS1 = industryDTOS.stream().filter(industryDTO -> industryDTO.getIndustryId().equals(industryId)).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(industryDTOS1)) {
                    String industryName = industryDTOS1.get(0).getIndustryName();
                    designDTO.setIndustryName(industryName);
                }
            }
        }
        return businessDesignDTOS;
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
     * 新增业务设计表
     *
     * @param businessDesignDTO 业务设计表
     * @return 结果
     */
    @Override
    @Transactional
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
        // 校验业务设计参数是否匹配坐标轴
        List<Map<String, Object>> businessDesignAxisConfigMap = businessDesignDTO.getBusinessDesignAxisConfigMap();
        Set<Integer> paramDimensionTop = businessDesignParamDTOS.stream().map(BusinessDesignParamDTO::getParamDimension).collect(Collectors.toSet());
        for (int i = businessDesignAxisConfigMap.size() - 1; i >= 0; i--) {
            Map<String, Object> map = businessDesignAxisConfigMap.get(i);
            Integer paramDimension = Integer.valueOf(map.get("paramDimension").toString());
            if (!paramDimensionTop.contains(paramDimension)) {
                businessDesignAxisConfigMap.remove(map);
            }
        }
        if (StringUtils.isNotEmpty(businessDesignParamDTOS)) {
            // 普通校验
            int sort = 0;
            for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOS) {
                if (StringUtils.isNull(businessDesignParamDTO.getParamDimension()))
                    throw new ServiceException("请选择维度");
                businessDesignParamDTO.setBusinessDesignId(businessDesignId);
                businessDesignParamDTO.setSort(sort);
                sort++;
            }
            List<Long> nullParamRelationIds = businessDesignParamDTOS.stream().filter(b -> b.getParamDimension().equals(1) || b.getParamDimension().equals(3))
                    .map(BusinessDesignParamDTO::getParamRelationId).filter(Objects::isNull).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(nullParamRelationIds))
                throw new ServiceException("请选择产品或者区域");
            // 产品的关联ID
            List<Long> productIds = businessDesignParamDTOS.stream().filter(b -> b.getParamDimension() == 1).map(BusinessDesignParamDTO::getParamRelationId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(productIds)) {
                R<List<ProductDTO>> productDTOSR = productService.getName(productIds, SecurityConstants.INNER);
                List<ProductDTO> productDTOS = productDTOSR.getData();
                if (StringUtils.isEmpty(productDTOS))
                    throw new ServiceException("当前产品已不存在");
                for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOS) {
                    if (businessDesignParamDTO.getParamDimension() != 1)
                        break;
                    Long paramRelationId = businessDesignParamDTO.getParamRelationId();
                    if (StringUtils.isNull(paramRelationId))
                        throw new ServiceException("请选择产品");
                    for (ProductDTO productDTO : productDTOS) {
                        if (productDTO.getProductId().equals(paramRelationId)) {
                            businessDesignParamDTO.setParamName(productDTO.getProductName());
                            break;
                        }
                    }
                }
            }
            // 区域的关联ID
            List<Long> areaIds = businessDesignParamDTOS.stream().filter(b -> b.getParamDimension() == 3).map(BusinessDesignParamDTO::getParamRelationId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(areaIds)) {
                R<List<AreaDTO>> areaDTOSR = areaService.selectAreaListByAreaIds(areaIds, SecurityConstants.INNER);
                List<AreaDTO> areaDTOS = areaDTOSR.getData();
                if (StringUtils.isEmpty(areaDTOS))
                    throw new ServiceException("当前区域已不存在");
                for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOS) {
                    Long paramRelationId = businessDesignParamDTO.getParamRelationId();
                    if (businessDesignParamDTO.getParamDimension() != 3)
                        break;
                    if (StringUtils.isNull(paramRelationId))
                        throw new ServiceException("请选择区域");
                    for (AreaDTO areaDTO : areaDTOS) {
                        if (areaDTO.getAreaId().equals(paramRelationId)) {
                            businessDesignParamDTO.setParamName(areaDTO.getAreaName());
                            break;
                        }
                    }
                }
            }
            businessDesignParamService.insertBusinessDesignParams(businessDesignParamDTOS);
        }
        // 业务设计轴配置表
        if (StringUtils.isNotEmpty(businessDesignAxisConfigMap)) {
            List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDTOS = new ArrayList<>();
            BusinessDesignAxisConfigDTO businessDesignAxisConfigDTO;
            for (Map<String, Object> map : businessDesignAxisConfigMap) {
                businessDesignAxisConfigDTO = new BusinessDesignAxisConfigDTO();
                businessDesignAxisConfigDTO.setCoordinateAxis(1);
                businessDesignAxisConfigDTO.setBusinessDesignId(businessDesignId);
                businessDesignAxisConfigDTO.setParamDimension(Integer.valueOf(map.get("paramDimension").toString()));
                businessDesignAxisConfigDTO.setUpperValue(new BigDecimal(map.get("upperValueX").toString()));
                businessDesignAxisConfigDTO.setLowerValue(new BigDecimal(map.get("lowerValueX").toString()));
                businessDesignAxisConfigDTOS.add(businessDesignAxisConfigDTO);
                businessDesignAxisConfigDTO = new BusinessDesignAxisConfigDTO();
                businessDesignAxisConfigDTO.setCoordinateAxis(2);
                businessDesignAxisConfigDTO.setBusinessDesignId(businessDesignId);
                businessDesignAxisConfigDTO.setParamDimension(Integer.valueOf(map.get("paramDimension").toString()));
                businessDesignAxisConfigDTO.setUpperValue(new BigDecimal(map.get("upperValueY").toString()));
                businessDesignAxisConfigDTO.setLowerValue(new BigDecimal(map.get("lowerValueY").toString()));
                businessDesignAxisConfigDTOS.add(businessDesignAxisConfigDTO);
            }
            businessDesignAxisConfigService.insertBusinessDesignAxisConfigs(businessDesignAxisConfigDTOS);
        }
        businessDesignDTO.setBusinessDesignId(businessDesignId);
        return businessDesignDTO;
    }

    /**
     * 校验入参的ID是否与维度匹配
     *
     * @param businessDesignDTO     业务设计DTO
     * @param businessUnitDecompose 维度
     * @return 业务设计入参DTO
     */
    private BusinessDesign getBusinessDesignParams(BusinessDesignDTO businessDesignDTO, String
            businessUnitDecompose) {
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
    @Transactional
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
        if (StringUtils.isNotEmpty(businessDesignDTOS) && !businessDesignId.equals(businessDesignDTOS.get(0).getBusinessDesignId())) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
        businessDesignDTO.setUpdateTime(DateUtils.getNowDate());
        businessDesignDTO.setUpdateBy(SecurityUtils.getUserId());
        BeanUtils.copyProperties(businessDesignDTO, businessDesign);
        businessDesignMapper.updateBusinessDesign(businessDesign);
        // 处理业务设计参数表
        dealBusinessParams(businessDesignDTO, businessDesignId);
        // 处理业务设计轴配置表
        dealBusinessAxisConfig(businessDesignDTO, businessDesignId);
        return 1;
    }

    /**
     * 处理业务设计轴配置表
     *
     * @param businessDesignDTO 业务设计DTO
     * @param businessDesignId  业务设计ID
     */
    private void dealBusinessAxisConfig(BusinessDesignDTO businessDesignDTO, Long businessDesignId) {
        List<Map<String, Object>> businessDesignAxisConfigMap = businessDesignDTO.getBusinessDesignAxisConfigMap();
        List<BusinessDesignAxisConfigDTO> businessDesignAxisConfigDTOS = businessDesignAxisConfigService.selectBusinessDesignAxisConfigByBusinessDesignId(businessDesignId);
        List<BusinessDesignAxisConfigDTO> addBusinessDesignAxisConfigDTOS = new ArrayList<>();
        List<BusinessDesignAxisConfigDTO> editBusinessDesignAxisConfigDTOS = new ArrayList<>();
        List<Long> delBusinessDesignAxisConfigIds;
        Set<Long> businessDesignAxisConfigIds = new HashSet<>();
        if (StringUtils.isEmpty(businessDesignAxisConfigMap)) {
            delBusinessDesignAxisConfigIds = businessDesignAxisConfigDTOS.stream().map(BusinessDesignAxisConfigDTO::getBusinessDesignAxisConfigId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(delBusinessDesignAxisConfigIds)) {
                businessDesignAxisConfigService.logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(delBusinessDesignAxisConfigIds);
            }
        } else {
            for (Map<String, Object> businessDesignAxisConfigDTO : businessDesignAxisConfigMap) {
                Integer paramDimension = Integer.valueOf(businessDesignAxisConfigDTO.get("paramDimension").toString());
                if (StringUtils.isEmpty(businessDesignAxisConfigDTOS)) {
                    insertBusinessDesignAxisConfigs(businessDesignId, addBusinessDesignAxisConfigDTOS, businessDesignAxisConfigDTO);
                } else {
                    List<BusinessDesignAxisConfigDTO> dealBusinessDesignAxisConfigDTOS = businessDesignAxisConfigDTOS.stream().filter(designAxisConfigDTO
                            -> Objects.equals(paramDimension, designAxisConfigDTO.getParamDimension())).collect(Collectors.toList());
                    if (StringUtils.isEmpty(dealBusinessDesignAxisConfigDTOS)) {
                        insertBusinessDesignAxisConfigs(businessDesignId, addBusinessDesignAxisConfigDTOS, businessDesignAxisConfigDTO);
                    } else {
                        for (BusinessDesignAxisConfigDTO dealBusinessDesignAxisConfigDTO : dealBusinessDesignAxisConfigDTOS) {
                            BusinessDesignAxisConfigDTO designAxisConfigDTO = new BusinessDesignAxisConfigDTO();
                            designAxisConfigDTO.setBusinessDesignAxisConfigId(dealBusinessDesignAxisConfigDTO.getBusinessDesignAxisConfigId());
                            businessDesignAxisConfigIds.add(dealBusinessDesignAxisConfigDTO.getBusinessDesignAxisConfigId());
                            if (dealBusinessDesignAxisConfigDTO.getCoordinateAxis() == 1) { // x轴
                                designAxisConfigDTO.setUpperValue(new BigDecimal(businessDesignAxisConfigDTO.get("upperValueX").toString()));
                                designAxisConfigDTO.setLowerValue(new BigDecimal(businessDesignAxisConfigDTO.get("lowerValueX").toString()));
                            } else {
                                designAxisConfigDTO.setUpperValue(new BigDecimal(businessDesignAxisConfigDTO.get("upperValueY").toString()));
                                designAxisConfigDTO.setLowerValue(new BigDecimal(businessDesignAxisConfigDTO.get("lowerValueY").toString()));
                            }
                            editBusinessDesignAxisConfigDTOS.add(designAxisConfigDTO);
                        }
                    }
                }
            }
            delBusinessDesignAxisConfigIds = businessDesignAxisConfigDTOS.stream().map(BusinessDesignAxisConfigDTO::getBusinessDesignAxisConfigId)
                    .filter(businessDesignAxisConfigId -> !businessDesignAxisConfigIds.contains(businessDesignAxisConfigId)).collect(Collectors.toList());
        }
        if (StringUtils.isNotEmpty(editBusinessDesignAxisConfigDTOS)) {
            businessDesignAxisConfigService.updateBusinessDesignAxisConfigs(editBusinessDesignAxisConfigDTOS);
        }
        if (StringUtils.isNotEmpty(addBusinessDesignAxisConfigDTOS)) {
            businessDesignAxisConfigService.insertBusinessDesignAxisConfigs(addBusinessDesignAxisConfigDTOS);
        }
        if (StringUtils.isNotEmpty(delBusinessDesignAxisConfigIds)) {
            businessDesignAxisConfigService.logicDeleteBusinessDesignAxisConfigByBusinessDesignAxisConfigIds(delBusinessDesignAxisConfigIds);
        }
    }

    /**
     * 给新增的赋值
     *
     * @param businessDesignId                业务设计ID
     * @param addBusinessDesignAxisConfigDTOS 新增的业务设计
     * @param businessDesignAxisConfigDTO     业务设计DTO
     */
    private static void insertBusinessDesignAxisConfigs(Long
                                                                businessDesignId, List<BusinessDesignAxisConfigDTO> addBusinessDesignAxisConfigDTOS, Map<String, Object> businessDesignAxisConfigDTO) {
        BusinessDesignAxisConfigDTO designAxisConfigDTO = new BusinessDesignAxisConfigDTO();
        designAxisConfigDTO.setCoordinateAxis(1);
        designAxisConfigDTO.setBusinessDesignId(businessDesignId);
        designAxisConfigDTO.setParamDimension(Integer.valueOf(businessDesignAxisConfigDTO.get("paramDimension").toString()));
        designAxisConfigDTO.setUpperValue(new BigDecimal(businessDesignAxisConfigDTO.get("upperValueX").toString()));
        designAxisConfigDTO.setLowerValue(new BigDecimal(businessDesignAxisConfigDTO.get("lowerValueX").toString()));
        addBusinessDesignAxisConfigDTOS.add(designAxisConfigDTO);
        designAxisConfigDTO = new BusinessDesignAxisConfigDTO();
        designAxisConfigDTO.setCoordinateAxis(2);
        designAxisConfigDTO.setBusinessDesignId(businessDesignId);
        designAxisConfigDTO.setParamDimension(Integer.valueOf(businessDesignAxisConfigDTO.get("paramDimension").toString()));
        designAxisConfigDTO.setUpperValue(new BigDecimal(businessDesignAxisConfigDTO.get("upperValueY").toString()));
        designAxisConfigDTO.setLowerValue(new BigDecimal(businessDesignAxisConfigDTO.get("lowerValueY").toString()));
        addBusinessDesignAxisConfigDTOS.add(designAxisConfigDTO);
    }

    /**
     * 处理业务设计参数表
     *
     * @param businessDesignDTO 业务设计DTO
     * @param businessDesignId  业务设计ID
     */
    private void dealBusinessParams(BusinessDesignDTO businessDesignDTO, Long businessDesignId) {
        List<BusinessDesignParamDTO> businessDesignParamDTOSAfter = businessDesignDTO.getBusinessDesignParamDTOS();
        List<Long> nullParamRelationIds = businessDesignParamDTOSAfter.stream().filter(b -> b.getParamDimension().equals(1) || b.getParamDimension().equals(3))
                .map(BusinessDesignParamDTO::getParamRelationId).filter(Objects::isNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(nullParamRelationIds)) {
            throw new ServiceException("请选择产品或者区域");
        }
        // 校验业务设计参数是否匹配坐标轴
        List<Map<String, Object>> businessDesignAxisConfigMap = businessDesignDTO.getBusinessDesignAxisConfigMap();
        Set<Integer> paramDimensionTop = businessDesignParamDTOSAfter.stream().map(BusinessDesignParamDTO::getParamDimension).collect(Collectors.toSet());
        for (int i = businessDesignAxisConfigMap.size() - 1; i >= 0; i--) {
            Map<String, Object> map = businessDesignAxisConfigMap.get(i);
            Integer paramDimension = Integer.valueOf(map.get("paramDimension").toString());
            if (!paramDimensionTop.contains(paramDimension)) {
                businessDesignAxisConfigMap.remove(map);
            }
        }
        // 产品的关联ID
        List<Long> productIds = businessDesignParamDTOSAfter.stream().filter(b -> b.getParamDimension() == 1).map(BusinessDesignParamDTO::getParamRelationId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(productIds)) {
            R<List<ProductDTO>> productDTOSR = productService.getName(productIds, SecurityConstants.INNER);
            List<ProductDTO> productDTOS = productDTOSR.getData();
            if (StringUtils.isEmpty(productDTOS)) {
                throw new ServiceException("当前产品已不存在");
            }
            for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOSAfter) {
                if (businessDesignParamDTO.getParamDimension() != 1)  // 产品
                    break;
                Long paramRelationId = businessDesignParamDTO.getParamRelationId();
                if (StringUtils.isNull(paramRelationId)) {
                    throw new ServiceException("请选择产品");
                }
                for (ProductDTO productDTO : productDTOS) {
                    if (productDTO.getProductId().equals(paramRelationId)) {
                        businessDesignParamDTO.setParamName(productDTO.getProductName());
                        break;
                    }
                }

            }
            // 区域的关联ID
            List<Long> areaIds = businessDesignParamDTOSAfter.stream().filter(b -> b.getParamDimension() == 3).map(BusinessDesignParamDTO::getParamRelationId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(areaIds)) {
                R<List<AreaDTO>> areaDTOSR = areaService.selectAreaListByAreaIds(areaIds, SecurityConstants.INNER);
                List<AreaDTO> areaDTOS = areaDTOSR.getData();
                if (StringUtils.isEmpty(areaDTOS)) {
                    throw new ServiceException("当前区域已不存在");
                }
                for (BusinessDesignParamDTO businessDesignParamDTO : businessDesignParamDTOSAfter) {
                    if (businessDesignParamDTO.getParamDimension() != 3)
                        break;
                    Long paramRelationId = businessDesignParamDTO.getParamRelationId();
                    if (StringUtils.isNull(paramRelationId)) {
                        throw new ServiceException("请选择区域");
                    }
                    // 产品
                    for (AreaDTO areaDTO : areaDTOS) {
                        if (areaDTO.getAreaId().equals(paramRelationId)) {
                            businessDesignParamDTO.setParamName(areaDTO.getAreaName());
                            break;
                        }
                    }
                }
            }
        }
        for (
                int i = 0; i < businessDesignParamDTOSAfter.size(); i++) {
            businessDesignParamDTOSAfter.get(i).setSort(i);
            businessDesignParamDTOSAfter.get(i).setBusinessDesignId(businessDesignId);
        }

        List<BusinessDesignParamDTO> businessDesignParamDTOSBefore = businessDesignParamService.selectBusinessDesignParamByBusinessDesignId(businessDesignId);
        List<BusinessDesignParamDTO> updateBusinessDesignParamDTOS = businessDesignParamDTOSAfter.stream().filter(businessDesignParamDTO ->
                businessDesignParamDTOSBefore.stream().map(BusinessDesignParamDTO::getBusinessDesignParamId).collect(Collectors.toList())
                        .contains(businessDesignParamDTO.getBusinessDesignParamId())).collect(Collectors.toList());
        List<BusinessDesignParamDTO> addBusinessDesignParamDTOS = businessDesignParamDTOSAfter.stream().filter(businessDesignParamDTO ->
                !businessDesignParamDTOSBefore.stream().map(BusinessDesignParamDTO::getBusinessDesignParamId).collect(Collectors.toList())
                        .contains(businessDesignParamDTO.getBusinessDesignParamId())).collect(Collectors.toList());
        List<BusinessDesignParamDTO> delBusinessDesignParamDTOS = businessDesignParamDTOSBefore.stream().filter(businessDesignParamDTO ->
                !businessDesignParamDTOSAfter.stream().map(BusinessDesignParamDTO::getBusinessDesignParamId).collect(Collectors.toList())
                        .contains(businessDesignParamDTO.getBusinessDesignParamId())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(updateBusinessDesignParamDTOS)) {
            businessDesignParamService.updateBusinessDesignParams(updateBusinessDesignParamDTOS);
        }
        if (StringUtils.isNotEmpty(delBusinessDesignParamDTOS)) {
            List<Long> businessDesignParamIds = delBusinessDesignParamDTOS.stream().map(BusinessDesignParamDTO::getBusinessDesignParamId).collect(Collectors.toList());
            businessDesignParamService.logicDeleteBusinessDesignParamByBusinessDesignParamIds(businessDesignParamIds);
        }
        if (StringUtils.isNotEmpty(addBusinessDesignParamDTOS)) {
            businessDesignParamService.insertBusinessDesignParams(addBusinessDesignParamDTOS);
        }

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
     * 最近一次的业务设计
     *
     * @param planYear 规划年份
     * @return 业务设计
     */
    @Override
    public BusinessDesignDTO selectBusinessDesignRecently(Integer planYear) {
        return businessDesignMapper.selectBusinessDesignRecently(planYear);
    }

    /**
     * 业务设计
     *
     * @param businessDesignDTO 业务设计
     * @return 结果
     */
    @Override
    public List<BusinessDesignDTO> remoteBusinessDesign(BusinessDesignDTO businessDesignDTO) {
        BusinessDesign businessDesign = new BusinessDesign();
        Map<String, Object> params = businessDesignDTO.getParams();
        businessDesign.setParams(params);
        BeanUtils.copyProperties(businessDesignDTO, businessDesign);
        return businessDesignMapper.selectBusinessDesignList(businessDesign);
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

