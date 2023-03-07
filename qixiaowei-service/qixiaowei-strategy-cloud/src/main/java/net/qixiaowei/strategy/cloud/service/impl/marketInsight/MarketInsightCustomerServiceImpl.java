package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightCustomer;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightCustomerMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightCustomerService;
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

import java.util.*;
import java.util.stream.Collectors;


/**
 * MarketInsightCustomerService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-03-07
 */
@Service
public class MarketInsightCustomerServiceImpl implements IMarketInsightCustomerService {
    @Autowired
    private MarketInsightCustomerMapper marketInsightCustomerMapper;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;

    @Autowired
    private RemoteUserService remoteUserService;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteIndustryService remoteIndustryService;
    @Autowired
    private RemoteProductService remoteProductService;
    @Autowired
    private RemoteAreaService remoteAreaService;
    /**
     * 查询市场洞察客户表
     *
     * @param marketInsightCustomerId 市场洞察客户表主键
     * @return 市场洞察客户表
     */
    @Override
    public MarketInsightCustomerDTO selectMarketInsightCustomerByMarketInsightCustomerId(Long marketInsightCustomerId) {
        MarketInsightCustomerDTO marketInsightCustomerDTO = marketInsightCustomerMapper.selectMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomerId);
        List<Map<String, Object>> dropList = PlanBusinessUnitCode.getDropList(marketInsightCustomerDTO.getBusinessUnitDecompose());
        marketInsightCustomerDTO.setBusinessUnitDecomposes(dropList);
        Long productId = marketInsightCustomerDTO.getProductId();
        Long areaId = marketInsightCustomerDTO.getAreaId();
        Long departmentId = marketInsightCustomerDTO.getDepartmentId();
        Long industryId = marketInsightCustomerDTO.getIndustryId();
        if (null != productId) {
            R<ProductDTO> productDTOR = remoteProductService.remoteSelectById(productId, SecurityConstants.INNER);
            ProductDTO data = productDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightCustomerDTO.setProductName(data.getProductName());
            }
        }
        if (null != areaId) {
            R<AreaDTO> areaDTOR = remoteAreaService.getById(areaId, SecurityConstants.INNER);
            AreaDTO data = areaDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightCustomerDTO.setAreaName(data.getAreaName());
            }
        }
        if (null != departmentId) {
            R<DepartmentDTO> departmentDTOR = remoteDepartmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
            DepartmentDTO data = departmentDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightCustomerDTO.setDepartmentName(data.getDepartmentName());
            }
        }
        if (null != industryId) {
            R<IndustryDTO> industryDTOR = remoteIndustryService.selectById(industryId, SecurityConstants.INNER);
            IndustryDTO data = industryDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightCustomerDTO.setIndustryName(data.getIndustryName());
            }
        }

        return marketInsightCustomerDTO;
    }

    /**
     * 查询市场洞察客户表列表
     *
     * @param marketInsightCustomerDTO 市场洞察客户表
     * @return 市场洞察客户表
     */
    @Override
    public List<MarketInsightCustomerDTO> selectMarketInsightCustomerList(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        List<String> createByList = new ArrayList<>();
        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        BeanUtils.copyProperties(marketInsightCustomerDTO, marketInsightCustomer);
        Map<String, Object> params = marketInsightCustomerDTO.getParams();
        this.queryemployeeName(params);
        marketInsightCustomer.setParams(params);
        if (StringUtils.isNotNull(marketInsightCustomerDTO.getCreateByName())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(marketInsightCustomerDTO.getCreateByName());
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> employeeIds = userListData.stream().map(UserDTO::getEmployeeId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(employeeIds)) {
                employeeIds.forEach(e -> {
                    createByList.add(String.valueOf(e));
                });
            } else {
                createByList.add("");
            }
        }
        marketInsightCustomer.setCreateBys(createByList);
        List<MarketInsightCustomerDTO> marketInsightCustomerDTOS = marketInsightCustomerMapper.selectMarketInsightCustomerList(marketInsightCustomer);
        if (StringUtils.isNotEmpty(marketInsightCustomerDTOS)) {
            Set<Long> createBys = marketInsightCustomerDTOS.stream().map(MarketInsightCustomerDTO::getCreateBy).collect(Collectors.toSet());
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
            List<UserDTO> userDTOList = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(userDTOList)) {
                for (MarketInsightCustomerDTO insightCustomerDTO : marketInsightCustomerDTOS) {
                    for (UserDTO userDTO : userDTOList) {
                        if (insightCustomerDTO.getCreateBy().equals(userDTO.getUserId())) {
                            insightCustomerDTO.setCreateByName(userDTO.getEmployeeName());
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotEmpty(marketInsightCustomerDTOS)) {
            List<Long> productIds = marketInsightCustomerDTOS.stream().filter(f -> null != f.getProductId()).map(MarketInsightCustomerDTO::getProductId).collect(Collectors.toList());
            List<Long> areaIds = marketInsightCustomerDTOS.stream().filter(f -> null != f.getAreaId()).map(MarketInsightCustomerDTO::getAreaId).collect(Collectors.toList());
            List<Long> departmentIds = marketInsightCustomerDTOS.stream().filter(f -> null != f.getDepartmentId()).map(MarketInsightCustomerDTO::getDepartmentId).collect(Collectors.toList());
            List<Long> industryIds = marketInsightCustomerDTOS.stream().filter(f -> null != f.getIndustryId()).map(MarketInsightCustomerDTO::getIndustryId).collect(Collectors.toList());

            if (StringUtils.isNotEmpty(productIds)) {
                R<List<ProductDTO>> productList = remoteProductService.getName(productIds, SecurityConstants.INNER);
                List<ProductDTO> data = productList.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (MarketInsightCustomerDTO insightCustomerDTO : marketInsightCustomerDTOS) {
                        for (ProductDTO datum : data) {
                            if (insightCustomerDTO.getProductId().equals(datum.getProductId())) {
                                insightCustomerDTO.setProductName(datum.getProductName());
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(areaIds)) {
                R<List<AreaDTO>> areaList = remoteAreaService.selectAreaListByAreaIds(areaIds, SecurityConstants.INNER);
                List<AreaDTO> areaListData = areaList.getData();
                if (StringUtils.isNotNull(areaListData)) {
                    for (MarketInsightCustomerDTO insightCustomerDTO : marketInsightCustomerDTOS) {
                        for (AreaDTO areaListDatum : areaListData) {
                            if (insightCustomerDTO.getAreaId().equals(areaListDatum.getAreaId())) {
                                insightCustomerDTO.setAreaName(areaListDatum.getAreaName());
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(departmentIds)) {
                R<List<DepartmentDTO>> departmentList = remoteDepartmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
                List<DepartmentDTO> data = departmentList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MarketInsightCustomerDTO insightCustomerDTO : marketInsightCustomerDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (insightCustomerDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                insightCustomerDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(industryIds)) {
                R<List<IndustryDTO>> industryList = remoteIndustryService.selectByIds(industryIds, SecurityConstants.INNER);
                List<IndustryDTO> data = industryList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MarketInsightCustomerDTO insightCustomerDTO : marketInsightCustomerDTOS) {
                        for (IndustryDTO datum : data) {
                            if (insightCustomerDTO.getIndustryId().equals(datum.getIndustryId())) {
                                insightCustomerDTO.setIndustryName(datum.getIndustryName());
                            }
                        }
                    }
                }
            }
        }
        return marketInsightCustomerDTOS;
    }
    /**
     * 封装高级查询人员id
     *
     * @param params
     */
    private void queryemployeeName(Map<String, Object> params) {
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
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectRemoteList(employeeDTO, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    List<Long> employeeIds = data.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(employeeIds)) {
                        params.put("createBys", employeeIds);
                    }
                }
            }
        }
    }
    /**
     * 新增市场洞察客户表
     *
     * @param marketInsightCustomerDTO 市场洞察客户表
     * @return 结果
     */
    @Override
    public MarketInsightCustomerDTO insertMarketInsightCustomer(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        BeanUtils.copyProperties(marketInsightCustomerDTO, marketInsightCustomer);
        marketInsightCustomer.setCreateBy(SecurityUtils.getUserId());
        marketInsightCustomer.setCreateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
        marketInsightCustomer.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        marketInsightCustomerMapper.insertMarketInsightCustomer(marketInsightCustomer);
        marketInsightCustomerDTO.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
        return marketInsightCustomerDTO;
    }

    /**
     * 修改市场洞察客户表
     *
     * @param marketInsightCustomerDTO 市场洞察客户表
     * @return 结果
     */
    @Override
    public int updateMarketInsightCustomer(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        BeanUtils.copyProperties(marketInsightCustomerDTO, marketInsightCustomer);
        marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
        return marketInsightCustomerMapper.updateMarketInsightCustomer(marketInsightCustomer);
    }

    /**
     * 逻辑批量删除市场洞察客户表
     *
     * @param marketInsightCustomerIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(List<Long> marketInsightCustomerIds) {
        return marketInsightCustomerMapper.logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(marketInsightCustomerIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除市场洞察客户表信息
     *
     * @param marketInsightCustomerId 市场洞察客户表主键
     * @return 结果
     */
    @Override
    public int deleteMarketInsightCustomerByMarketInsightCustomerId(Long marketInsightCustomerId) {
        return marketInsightCustomerMapper.deleteMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomerId);
    }

    /**
     * 逻辑删除市场洞察客户表信息
     *
     * @param marketInsightCustomerDTO 市场洞察客户表
     * @return 结果
     */
    @Override
    public int logicDeleteMarketInsightCustomerByMarketInsightCustomerId(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        marketInsightCustomer.setMarketInsightCustomerId(marketInsightCustomerDTO.getMarketInsightCustomerId());
        marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
        return marketInsightCustomerMapper.logicDeleteMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomer);
    }

    /**
     * 物理删除市场洞察客户表信息
     *
     * @param marketInsightCustomerDTO 市场洞察客户表
     * @return 结果
     */

    @Override
    public int deleteMarketInsightCustomerByMarketInsightCustomerId(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        BeanUtils.copyProperties(marketInsightCustomerDTO, marketInsightCustomer);
        return marketInsightCustomerMapper.deleteMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
    }

    /**
     * 物理批量删除市场洞察客户表
     *
     * @param marketInsightCustomerDtos 需要删除的市场洞察客户表主键
     * @return 结果
     */

    @Override
    public int deleteMarketInsightCustomerByMarketInsightCustomerIds(List<MarketInsightCustomerDTO> marketInsightCustomerDtos) {
        List<Long> stringList = new ArrayList();
        for (MarketInsightCustomerDTO marketInsightCustomerDTO : marketInsightCustomerDtos) {
            stringList.add(marketInsightCustomerDTO.getMarketInsightCustomerId());
        }
        return marketInsightCustomerMapper.deleteMarketInsightCustomerByMarketInsightCustomerIds(stringList);
    }

    /**
     * 批量新增市场洞察客户表信息
     *
     * @param marketInsightCustomerDtos 市场洞察客户表对象
     */

    public int insertMarketInsightCustomers(List<MarketInsightCustomerDTO> marketInsightCustomerDtos) {
        List<MarketInsightCustomer> marketInsightCustomerList = new ArrayList();

        for (MarketInsightCustomerDTO marketInsightCustomerDTO : marketInsightCustomerDtos) {
            MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
            BeanUtils.copyProperties(marketInsightCustomerDTO, marketInsightCustomer);
            marketInsightCustomer.setCreateBy(SecurityUtils.getUserId());
            marketInsightCustomer.setCreateTime(DateUtils.getNowDate());
            marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
            marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
            marketInsightCustomer.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            marketInsightCustomerList.add(marketInsightCustomer);
        }
        return marketInsightCustomerMapper.batchMarketInsightCustomer(marketInsightCustomerList);
    }

    /**
     * 批量修改市场洞察客户表信息
     *
     * @param marketInsightCustomerDtos 市场洞察客户表对象
     */

    public int updateMarketInsightCustomers(List<MarketInsightCustomerDTO> marketInsightCustomerDtos) {
        List<MarketInsightCustomer> marketInsightCustomerList = new ArrayList();

        for (MarketInsightCustomerDTO marketInsightCustomerDTO : marketInsightCustomerDtos) {
            MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
            BeanUtils.copyProperties(marketInsightCustomerDTO, marketInsightCustomer);
            marketInsightCustomer.setCreateBy(SecurityUtils.getUserId());
            marketInsightCustomer.setCreateTime(DateUtils.getNowDate());
            marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
            marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
            marketInsightCustomerList.add(marketInsightCustomer);
        }
        return marketInsightCustomerMapper.updateMarketInsightCustomers(marketInsightCustomerList);
    }
}

