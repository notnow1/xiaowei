package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

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
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.*;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.*;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightCustomerMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiCustomerChoiceMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiCustomerInvestDetailMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiCustomerInvestPlanMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightCustomerService;
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

import java.math.BigDecimal;
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
    private MiCustomerChoiceMapper miCustomerChoiceMapper;
    @Autowired
    private MiCustomerInvestDetailMapper miCustomerInvestDetailMapper;
    @Autowired
    private MiCustomerInvestPlanMapper miCustomerInvestPlanMapper;


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
    @Autowired
    private RemoteDictionaryDataService remoteDictionaryDataService;

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
        packRemoteDetail(marketInsightCustomerDTO);
        //根据市场洞察客户主表主键查询市场洞察客户选择表
        List<MiCustomerChoiceDTO> miCustomerChoiceDTOS = miCustomerChoiceMapper.selectMiCustomerChoiceByMarketInsightCustomerId(marketInsightCustomerId);
        if (StringUtils.isNotEmpty(miCustomerChoiceDTOS)) {
            //客户类别集合
            List<Long> customerCategorys = miCustomerChoiceDTOS.stream().filter(f -> null != f.getCustomerCategory()).map(MiCustomerChoiceDTO::getCustomerCategory).collect(Collectors.toList());
            //行业id
            List<Long> industryIds = miCustomerChoiceDTOS.stream().filter(f -> null != f.getIndustryId()).map(MiCustomerChoiceDTO::getIndustryId).collect(Collectors.toList());

            if (StringUtils.isNotEmpty(customerCategorys)) {
                R<List<DictionaryDataDTO>> dataByDictionaryDataList = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(customerCategorys, SecurityConstants.INNER);
                List<DictionaryDataDTO> data = dataByDictionaryDataList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiCustomerChoiceDTO miCustomerChoiceDTO : miCustomerChoiceDTOS) {
                        for (DictionaryDataDTO datum : data) {
                            if (miCustomerChoiceDTO.getCustomerCategory().equals(datum.getDictionaryDataId())) {
                                miCustomerChoiceDTO.setCustomerCategoryName(datum.getDictionaryLabel());
                            }
                        }
                    }
                }
            }

            if (StringUtils.isNotEmpty(industryIds)) {
                R<List<IndustryDTO>> industryList = remoteIndustryService.selectByIds(industryIds, SecurityConstants.INNER);
                List<IndustryDTO> data = industryList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiCustomerChoiceDTO miCustomerChoiceDTO : miCustomerChoiceDTOS) {
                        for (IndustryDTO datum : data) {
                            if (miCustomerChoiceDTO.getIndustryId().equals(datum.getIndustryId())) {
                                miCustomerChoiceDTO.setIndustryName(datum.getIndustryName());
                            }
                        }
                    }
                }
            }

        }
        //根据市场洞察客户主表主键查询市场洞察客户投资计划表
        List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOS = miCustomerInvestPlanMapper.selectMiCustomerInvestPlanByMarketInsightCustomerId(marketInsightCustomerId);
        if (StringUtils.isNotEmpty(miCustomerInvestPlanDTOS)) {
            //主键id集合
            List<Long> miCustomerInvestPlanIds = miCustomerInvestPlanDTOS.stream().map(MiCustomerInvestPlanDTO::getMiCustomerInvestPlanId).collect(Collectors.toList());

            //客户类别集合
            List<Long> customerCategorys = miCustomerInvestPlanDTOS.stream().filter(f -> null != f.getCustomerCategory()).map(MiCustomerInvestPlanDTO::getCustomerCategory).collect(Collectors.toList());

            if (StringUtils.isNotEmpty(customerCategorys)) {
                R<List<DictionaryDataDTO>> dataByDictionaryDataList = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(customerCategorys, SecurityConstants.INNER);
                List<DictionaryDataDTO> data = dataByDictionaryDataList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiCustomerInvestPlanDTO miCustomerInvestPlanDTO : miCustomerInvestPlanDTOS) {
                        for (DictionaryDataDTO datum : data) {
                            if (miCustomerInvestPlanDTO.getCustomerCategory().equals(datum.getDictionaryDataId())) {
                                miCustomerInvestPlanDTO.setCustomerCategoryName(datum.getDictionaryLabel());
                            }
                        }
                    }
                }
            }
            //行业id
            List<Long> industryIds = miCustomerInvestPlanDTOS.stream().filter(f -> null != f.getIndustryId()).map(MiCustomerInvestPlanDTO::getIndustryId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(industryIds)) {
                R<List<IndustryDTO>> industryList = remoteIndustryService.selectByIds(industryIds, SecurityConstants.INNER);
                List<IndustryDTO> data = industryList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiCustomerInvestPlanDTO miCustomerInvestPlanDTO : miCustomerInvestPlanDTOS) {
                        for (IndustryDTO datum : data) {
                            if (miCustomerInvestPlanDTO.getIndustryId().equals(datum.getIndustryId())) {
                                miCustomerInvestPlanDTO.setIndustryName(datum.getIndustryName());
                            }
                        }
                    }
                }
            }
            //封装详情市场洞察客户投资详情集合
            this.packPlanDetail(miCustomerInvestPlanDTOS, miCustomerInvestPlanIds);
        }
        marketInsightCustomerDTO.setMiCustomerChoiceDTOS(miCustomerChoiceDTOS);
        marketInsightCustomerDTO.setMiCustomerInvestPlanDTOS(miCustomerInvestPlanDTOS);
        return marketInsightCustomerDTO;
    }

    /**
     * 封装详情市场洞察客户投资详情集合
     * @param miCustomerInvestPlanDTOS
     * @param miCustomerInvestPlanIds
     */
    private void packPlanDetail(List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOS, List<Long> miCustomerInvestPlanIds) {
        if (StringUtils.isNotEmpty(miCustomerInvestPlanIds)) {
            //根据市场洞察客户投资表主键集合批量查询市场洞察客户投资详情表
            List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOS = miCustomerInvestDetailMapper.selectMiCustomerInvestDetailByMiCustomerInvestPlanIds(miCustomerInvestPlanIds);
            if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOS)) {
                List<Long> productIds = miCustomerInvestDetailDTOS.stream().filter(f -> null != f.getProductId()).map(MiCustomerInvestDetailDTO::getProductId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(productIds)) {
                    R<List<ProductDTO>> productDTOList = remoteProductService.getName(productIds, SecurityConstants.INNER);
                    List<ProductDTO> data = productDTOList.getData();
                    if (StringUtils.isNotEmpty(data)) {
                        for (MiCustomerInvestDetailDTO miCustomerInvestDetailDTO : miCustomerInvestDetailDTOS) {
                            for (ProductDTO datum : data) {
                                if (datum.getProductId().equals(miCustomerInvestDetailDTO.getProductId())) {
                                    miCustomerInvestDetailDTO.setProductName(datum.getProductName());
                                    miCustomerInvestDetailDTO.setProductUnitName(datum.getProductUnitName());
                                }
                            }
                        }
                    }
                }
                for (MiCustomerInvestDetailDTO miCustomerInvestDetailDTO : miCustomerInvestDetailDTOS) {
                    //公式=客户投资计划×预计市场占有率。
                    BigDecimal partMarketSpace = new BigDecimal("0");
                    //客户投资计划金额
                    BigDecimal customerInvestPlanAmount = miCustomerInvestDetailDTO.getCustomerInvestPlanAmount();
                    //预计市场占有率
                    BigDecimal estimateMarketShare = miCustomerInvestDetailDTO.getEstimateMarketShare();
                    if ((null != customerInvestPlanAmount && customerInvestPlanAmount.compareTo(new BigDecimal("0")) != 0) && (null != estimateMarketShare && estimateMarketShare.compareTo(new BigDecimal("0")) != 0)) {
                        partMarketSpace = customerInvestPlanAmount.multiply(estimateMarketShare).setScale(10, BigDecimal.ROUND_HALF_UP);
                    }
                    miCustomerInvestDetailDTO.setPartMarketSpace(partMarketSpace);
                }
                //根据年份分组
                Map<Integer, List<MiCustomerInvestDetailDTO>> miCustomerInvestDetailYearMap = miCustomerInvestDetailDTOS.stream().filter(f-> null != f.getPlanYear()).collect(Collectors.groupingBy(MiCustomerInvestDetailDTO::getPlanYear));
                if (StringUtils.isNotEmpty(miCustomerInvestDetailYearMap)){
                    for (Integer key : miCustomerInvestDetailYearMap.keySet()) {
                        //合计可参与市场空间
                        BigDecimal amountTo = new BigDecimal("0");
                        List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOS1 = miCustomerInvestDetailYearMap.get(key);
                        if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOS1)){
                            for (MiCustomerInvestDetailDTO miCustomerInvestDetailDTO : miCustomerInvestDetailDTOS1) {
                                //可参与市场空间
                                BigDecimal partMarketSpace = miCustomerInvestDetailDTO.getPartMarketSpace();
                                amountTo=amountTo.add(partMarketSpace);
                            }
                            for (MiCustomerInvestDetailDTO miCustomerInvestDetailDTO : miCustomerInvestDetailDTOS1) {
                                miCustomerInvestDetailDTO.setAmountTo(amountTo);
                            }
                        }
                    }
                }
                //转回list
                List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOList = miCustomerInvestDetailYearMap.values().stream().flatMap(List::stream).collect(Collectors.toList());
                //根据投资id分组
                Map<Long, List<MiCustomerInvestDetailDTO>> miCustomerInvestDetailDTOMapList = miCustomerInvestDetailDTOList.stream().collect(Collectors.groupingBy(MiCustomerInvestDetailDTO::getMiCustomerInvestPlanId));
                if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOMapList)&& StringUtils.isNotEmpty(miCustomerInvestPlanDTOS)){
                    for (MiCustomerInvestPlanDTO miCustomerInvestPlanDTO : miCustomerInvestPlanDTOS) {
                        miCustomerInvestPlanDTO.setMiCustomerInvestDetailDTOS(miCustomerInvestDetailDTOMapList.get(miCustomerInvestPlanDTO.getMiCustomerInvestPlanId()));
                    }
                }

            }
        }
    }

    /**
     * 封装详情远程主表赋值名称
     *
     * @param marketInsightCustomerDTO
     */
    private void packRemoteDetail(MarketInsightCustomerDTO marketInsightCustomerDTO) {
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
        //远程封装赋值名称
        this.packRemote(marketInsightCustomerDTOS);
        return marketInsightCustomerDTOS;
    }

    /**
     * 远程封装赋值名称
     *
     * @param marketInsightCustomerDTOS
     */
    private void packRemote(List<MarketInsightCustomerDTO> marketInsightCustomerDTOS) {
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
    @Transactional
    public MarketInsightCustomerDTO insertMarketInsightCustomer(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        int i = 0;
        //前台传入市场洞察客户选择表集合
        List<MiCustomerChoiceDTO> miCustomerChoiceDTOS = marketInsightCustomerDTO.getMiCustomerChoiceDTOS();
        //保存数据
        List<MiCustomerChoice> miCustomerChoiceList = new ArrayList<>();

        //前台传入市场洞察客户投资计划集合
        List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOS = marketInsightCustomerDTO.getMiCustomerInvestPlanDTOS();
        //保存数据
        List<MiCustomerInvestPlan> miCustomerInvestPlanList = new ArrayList<>();
        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        BeanUtils.copyProperties(marketInsightCustomerDTO, marketInsightCustomer);
        marketInsightCustomer.setCreateBy(SecurityUtils.getUserId());
        marketInsightCustomer.setCreateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
        marketInsightCustomer.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            i = marketInsightCustomerMapper.insertMarketInsightCustomer(marketInsightCustomer);
        } catch (Exception e) {
            throw new ServiceException("新增市场洞察客户失败");
        }
        if (StringUtils.isNotEmpty(miCustomerChoiceDTOS)) {
            for (int i1 = 0; i1 < miCustomerChoiceDTOS.size(); i1++) {
                MiCustomerChoice miCustomerChoice = new MiCustomerChoice();
                BeanUtils.copyProperties(miCustomerChoiceDTOS.get(i1), miCustomerChoice);
                miCustomerChoice.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
                miCustomerChoice.setSort(i1 + 1);
                miCustomerChoice.setCreateBy(SecurityUtils.getUserId());
                miCustomerChoice.setCreateTime(DateUtils.getNowDate());
                miCustomerChoice.setUpdateTime(DateUtils.getNowDate());
                miCustomerChoice.setUpdateBy(SecurityUtils.getUserId());
                miCustomerChoice.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miCustomerChoiceList.add(miCustomerChoice);
            }
        }

        if (StringUtils.isNotEmpty(miCustomerInvestPlanDTOS)) {
            for (int i1 = 0; i1 < miCustomerInvestPlanDTOS.size(); i1++) {
                MiCustomerInvestPlan miCustomerInvestPlan = new MiCustomerInvestPlan();
                BeanUtils.copyProperties(miCustomerInvestPlanDTOS.get(i1), miCustomerInvestPlan);
                miCustomerInvestPlan.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
                miCustomerInvestPlan.setSort(i1 + 1);
                miCustomerInvestPlan.setCreateBy(SecurityUtils.getUserId());
                miCustomerInvestPlan.setCreateTime(DateUtils.getNowDate());
                miCustomerInvestPlan.setUpdateTime(DateUtils.getNowDate());
                miCustomerInvestPlan.setUpdateBy(SecurityUtils.getUserId());
                miCustomerInvestPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miCustomerInvestPlanList.add(miCustomerInvestPlan);
            }
        }
        if (StringUtils.isNotEmpty(miCustomerChoiceList)) {
            try {
                miCustomerChoiceMapper.batchMiCustomerChoice(miCustomerChoiceList);
            } catch (Exception e) {
                throw new ServiceException("批量新增市场洞察客户选择失败");
            }
        }
        if (StringUtils.isNotEmpty(miCustomerInvestPlanList)) {
            try {
                miCustomerInvestPlanMapper.batchMiCustomerInvestPlan(miCustomerInvestPlanList);
            } catch (Exception e) {
                throw new ServiceException("批量新增市场洞察客户选择失败");
            }
            for (int i1 = 0; i1 < miCustomerInvestPlanList.size(); i1++) {
                //前台传入市场洞察客户投资详情集合
                List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOS = miCustomerInvestPlanDTOS.get(i1).getMiCustomerInvestDetailDTOS();
                List<MiCustomerInvestDetail> miCustomerInvestDetailList = new ArrayList<>();
                if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOS)) {
                    for (int i2 = 0; i2 < miCustomerInvestDetailDTOS.size(); i2++) {
                        MiCustomerInvestDetail miCustomerInvestDetail = new MiCustomerInvestDetail();
                        BeanUtils.copyProperties(miCustomerInvestDetailDTOS.get(i2), miCustomerInvestDetail);
                        miCustomerInvestDetail.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
                        miCustomerInvestDetail.setMiCustomerInvestPlanId(miCustomerInvestPlanList.get(i1).getMiCustomerInvestPlanId());
                        miCustomerInvestDetail.setSort(i1 + 1);
                        miCustomerInvestDetail.setCreateBy(SecurityUtils.getUserId());
                        miCustomerInvestDetail.setCreateTime(DateUtils.getNowDate());
                        miCustomerInvestDetail.setUpdateTime(DateUtils.getNowDate());
                        miCustomerInvestDetail.setUpdateBy(SecurityUtils.getUserId());
                        miCustomerInvestDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        miCustomerInvestDetailList.add(miCustomerInvestDetail);
                    }

                }
                if (StringUtils.isNotEmpty(miCustomerInvestDetailList)) {
                    try {
                        miCustomerInvestDetailMapper.batchMiCustomerInvestDetail(miCustomerInvestDetailList);
                    } catch (Exception e) {
                        throw new ServiceException("批量新增市场洞察客户投资详情失败");
                    }
                }
            }
        }
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
    @Transactional
    public int updateMarketInsightCustomer(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        int i = 0;
        //前台传入市场洞察客户选择表集合数据
        List<MiCustomerChoiceDTO> miCustomerChoiceDTOS = marketInsightCustomerDTO.getMiCustomerChoiceDTOS();
        //新增市场洞察客户选择表集合数据
        List<MiCustomerChoice> miCustomerChoiceAddList = new ArrayList<>();
        //修改市场洞察客户选择表集合数据
        List<MiCustomerChoice> miCustomerChoiceUpdateList = new ArrayList<>();

        //前台传入市场洞察客户投资计划集合数据
        List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOS = marketInsightCustomerDTO.getMiCustomerInvestPlanDTOS();
        //新增市场洞察客户投资计划集合数据
        List<MiCustomerInvestPlan> miCustomerInvestPlanAddList = new ArrayList<>();
        //修改市场洞察客户投资计划集合数据
        List<MiCustomerInvestPlan> miCustomerInvestPlanUpdateList = new ArrayList<>();

        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        BeanUtils.copyProperties(marketInsightCustomerDTO, marketInsightCustomer);
        marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = marketInsightCustomerMapper.updateMarketInsightCustomer(marketInsightCustomer);
        } catch (Exception e) {
            throw new ServiceException("修改市场洞察客户失败");
        }
        //修改新增市场洞察客户选择表集合数据
       this.packMiCustomerChoiceUpdates(miCustomerChoiceDTOS, miCustomerChoiceAddList, miCustomerChoiceUpdateList, marketInsightCustomer);
        //修改新增市场洞察客户投资计划集合数据
        packMiCustomerInvestPlanUpdates(miCustomerInvestPlanDTOS, miCustomerInvestPlanAddList, miCustomerInvestPlanUpdateList, marketInsightCustomer);
        return i;
    }

    /**
     * 修改新增市场洞察客户投资计划集合数据
     * @param miCustomerInvestPlanDTOS
     * @param miCustomerInvestPlanAddList
     * @param miCustomerInvestPlanUpdateList
     * @param marketInsightCustomer
     */
    private void packMiCustomerInvestPlanUpdates(List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOS, List<MiCustomerInvestPlan> miCustomerInvestPlanAddList, List<MiCustomerInvestPlan> miCustomerInvestPlanUpdateList, MarketInsightCustomer marketInsightCustomer) {
        if (StringUtils.isNotEmpty(miCustomerInvestPlanDTOS)){
            //根据市场洞察客户主表主键查询市场洞察客户投资计划表
            List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOList = miCustomerInvestPlanMapper.selectMiCustomerInvestPlanByMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
            if (StringUtils.isNotEmpty(miCustomerInvestPlanDTOList)){
                List<Long> miCustomerInvestPlanIds = new ArrayList<>();
                //sterm流求差集
                miCustomerInvestPlanIds = miCustomerInvestPlanDTOList.stream().filter(a ->
                        !miCustomerInvestPlanDTOS.stream().map(MiCustomerInvestPlanDTO::getMiCustomerInvestPlanId).collect(Collectors.toList()).contains(a.getMiCustomerInvestPlanId())
                ).collect(Collectors.toList()).stream().map(MiCustomerInvestPlanDTO::getMiCustomerInvestPlanId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miCustomerInvestPlanIds)){
                    try {
                        miCustomerInvestPlanMapper.logicDeleteMiCustomerInvestPlanByMiCustomerInvestPlanIds(miCustomerInvestPlanIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察客户投资计划失败");
                    }
                }
            }
            for (int i1 = 0; i1 < miCustomerInvestPlanDTOS.size(); i1++) {
                Long miCustomerInvestPlanId = miCustomerInvestPlanDTOS.get(i1).getMiCustomerInvestPlanId();
                if (null == miCustomerInvestPlanId){
                    MiCustomerInvestPlan miCustomerInvestPlan = new MiCustomerInvestPlan();
                    BeanUtils.copyProperties(miCustomerInvestPlanDTOS.get(i1),miCustomerInvestPlan);
                    miCustomerInvestPlan.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
                    miCustomerInvestPlan.setSort(i1 + 1);
                    miCustomerInvestPlan.setCreateBy(SecurityUtils.getUserId());
                    miCustomerInvestPlan.setCreateTime(DateUtils.getNowDate());
                    miCustomerInvestPlan.setUpdateTime(DateUtils.getNowDate());
                    miCustomerInvestPlan.setUpdateBy(SecurityUtils.getUserId());
                    miCustomerInvestPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    miCustomerInvestPlanAddList.add(miCustomerInvestPlan);
                }else {
                    MiCustomerInvestPlan miCustomerInvestPlan = new MiCustomerInvestPlan();
                    BeanUtils.copyProperties(miCustomerInvestPlanDTOS.get(i1),miCustomerInvestPlan);
                    miCustomerInvestPlan.setSort(i1 + 1);
                    miCustomerInvestPlan.setUpdateTime(DateUtils.getNowDate());
                    miCustomerInvestPlan.setUpdateBy(SecurityUtils.getUserId());
                    miCustomerInvestPlanUpdateList.add(miCustomerInvestPlan);
                }
            }
            if (StringUtils.isNotEmpty(miCustomerInvestPlanAddList)){
                try {
                    miCustomerInvestPlanMapper.batchMiCustomerInvestPlan(miCustomerInvestPlanAddList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察客户投资计划失败");
                }
            }
            if (StringUtils.isNotEmpty(miCustomerInvestPlanUpdateList)){
                try {
                    miCustomerInvestPlanMapper.updateMiCustomerInvestPlans(miCustomerInvestPlanUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("批量修改市场洞察客户投资计划失败");
                }
            }
            //新增修改市场洞察客户投资详情集合
            this.packMiCustomerInvestDetailUpdates(miCustomerInvestPlanDTOS, miCustomerInvestPlanAddList, marketInsightCustomer);


        }else {
            //根据市场洞察客户主表主键查询市场洞察客户投资计划表
            List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOList = miCustomerInvestPlanMapper.selectMiCustomerInvestPlanByMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
            if (StringUtils.isNotEmpty(miCustomerInvestPlanDTOList)){
                List<Long> miCustomerInvestPlanIds = miCustomerInvestPlanDTOList.stream().map(MiCustomerInvestPlanDTO::getMiCustomerInvestPlanId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miCustomerInvestPlanIds)){
                    try {
                        miCustomerInvestPlanMapper.logicDeleteMiCustomerInvestPlanByMiCustomerInvestPlanIds(miCustomerInvestPlanIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察客户投资计划失败");
                    }
                }

                List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOList = miCustomerInvestDetailMapper.selectMiCustomerInvestDetailByMiCustomerInvestPlanIds(miCustomerInvestPlanIds);
                if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOList)){
                    List<Long> miCustomerInvestDetailIds = miCustomerInvestDetailDTOList.stream().map(MiCustomerInvestDetailDTO::getMiCustomerInvestDetailId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miCustomerInvestDetailIds)){
                        try {
                            miCustomerInvestDetailMapper.logicDeleteMiCustomerInvestDetailByMiCustomerInvestDetailIds(miCustomerInvestDetailIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察客户投资详情失败");
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装新增修改市场洞察客户投资详情集合
     * @param miCustomerInvestPlanDTOS
     * @param miCustomerInvestPlanAddList
     * @param marketInsightCustomer
     */
    private void packMiCustomerInvestDetailUpdates(List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOS, List<MiCustomerInvestPlan> miCustomerInvestPlanAddList, MarketInsightCustomer marketInsightCustomer) {
        for (int i = 0; i < miCustomerInvestPlanDTOS.size(); i++) {
            //新增市场洞察客户投资详情集合
            List<MiCustomerInvestDetail> miCustomerInvestDetailAddList = new ArrayList<>();
            //修改市场洞察客户投资详情集合
            List<MiCustomerInvestDetail> miCustomerInvestDetailUpdateList = new ArrayList<>();
            int num = 0 ;
            //id
            Long miCustomerInvestPlanId = miCustomerInvestPlanDTOS.get(i).getMiCustomerInvestPlanId();
            //前台传入市场洞察客户投资详情集合
            List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOS = miCustomerInvestPlanDTOS.get(i).getMiCustomerInvestDetailDTOS();
            if (null != miCustomerInvestPlanId){
                if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOS)){
                    List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOList = new ArrayList<>();
                     miCustomerInvestDetailDTOList = miCustomerInvestDetailMapper.selectMiCustomerInvestDetailByMiCustomerInvestPlanId(miCustomerInvestPlanId);
                    if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOList)){
                        List<Long> miCustomerInvestDetailIds = new ArrayList<>();
                        //sterm流求差集
                        miCustomerInvestDetailIds = miCustomerInvestDetailDTOList.stream().filter(a ->
                                !miCustomerInvestDetailDTOS.stream().map(MiCustomerInvestDetailDTO::getMiCustomerInvestDetailId).collect(Collectors.toList()).contains(a.getMiCustomerInvestDetailId())
                        ).collect(Collectors.toList()).stream().map(MiCustomerInvestDetailDTO::getMiCustomerInvestDetailId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(miCustomerInvestDetailIds)){
                            try {
                                miCustomerInvestDetailMapper.logicDeleteMiCustomerInvestDetailByMiCustomerInvestDetailIds(miCustomerInvestDetailIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
                            } catch (Exception e) {
                                throw new ServiceException("逻辑批量删除市场洞察客户投资详情失败");
                            }
                        }
                    }
                    for (int i1 = 0; i1 < miCustomerInvestDetailDTOS.size(); i1++) {
                        Long miCustomerInvestDetailId = miCustomerInvestDetailDTOS.get(i1).getMiCustomerInvestDetailId();
                        if (null != miCustomerInvestDetailId){
                            MiCustomerInvestDetail miCustomerInvestDetail = new MiCustomerInvestDetail();
                            BeanUtils.copyProperties(miCustomerInvestDetailDTOS.get(i1),miCustomerInvestDetail);
                            miCustomerInvestDetail.setSort(i1 + 1);
                            miCustomerInvestDetail.setUpdateTime(DateUtils.getNowDate());
                            miCustomerInvestDetail.setUpdateBy(SecurityUtils.getUserId());
                            miCustomerInvestDetailUpdateList.add(miCustomerInvestDetail);
                        }else {
                            MiCustomerInvestDetail miCustomerInvestDetail = new MiCustomerInvestDetail();
                            BeanUtils.copyProperties(miCustomerInvestDetailDTOS.get(i1),miCustomerInvestDetail);
                            miCustomerInvestDetail.setMiCustomerInvestPlanId(miCustomerInvestPlanId);
                            miCustomerInvestDetail.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
                            miCustomerInvestDetail.setSort(i1 + 1);
                            miCustomerInvestDetail.setCreateBy(SecurityUtils.getUserId());
                            miCustomerInvestDetail.setCreateTime(DateUtils.getNowDate());
                            miCustomerInvestDetail.setUpdateTime(DateUtils.getNowDate());
                            miCustomerInvestDetail.setUpdateBy(SecurityUtils.getUserId());
                            miCustomerInvestDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            miCustomerInvestDetailAddList.add(miCustomerInvestDetail);
                        }
                    }

                }else {
                    List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOList = new ArrayList<>();
                    miCustomerInvestDetailDTOList = miCustomerInvestDetailMapper.selectMiCustomerInvestDetailByMiCustomerInvestPlanId(miCustomerInvestPlanId);
                    if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOList)){
                        List<Long> collect = miCustomerInvestDetailDTOList.stream().map(MiCustomerInvestDetailDTO::getMiCustomerInvestDetailId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(collect)){
                            try {
                                miCustomerInvestDetailMapper.logicDeleteMiCustomerInvestDetailByMiCustomerInvestDetailIds(collect,SecurityUtils.getUserId(),DateUtils.getNowDate());
                            } catch (Exception e) {
                                throw new ServiceException("逻辑批量删除市场洞察客户投资详情失败");
                            }
                        }
                    }
                }
            }else {
                if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOS)) {
                    for (int i1 = 0; i1 < miCustomerInvestDetailDTOS.size(); i1++) {
                        MiCustomerInvestDetail miCustomerInvestDetail = new MiCustomerInvestDetail();
                        BeanUtils.copyProperties(miCustomerInvestDetailDTOS.get(i1),miCustomerInvestDetail);
                        try {
                            miCustomerInvestDetail.setMiCustomerInvestPlanId(miCustomerInvestPlanAddList.get(num).getMiCustomerInvestPlanId());
                        } catch (Exception e) {
                            throw new ServiceException("数组越界异常" + miCustomerInvestPlanAddList.size() + ":" + num);
                        }
                        miCustomerInvestDetail.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
                        miCustomerInvestDetail.setSort(i1 + 1);
                        miCustomerInvestDetail.setCreateBy(SecurityUtils.getUserId());
                        miCustomerInvestDetail.setCreateTime(DateUtils.getNowDate());
                        miCustomerInvestDetail.setUpdateTime(DateUtils.getNowDate());
                        miCustomerInvestDetail.setUpdateBy(SecurityUtils.getUserId());
                        miCustomerInvestDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        miCustomerInvestDetailAddList.add(miCustomerInvestDetail);
                    }
                }
                num++;
            }
            if (StringUtils.isNotEmpty(miCustomerInvestDetailAddList)){
                try {
                    miCustomerInvestDetailMapper.batchMiCustomerInvestDetail(miCustomerInvestDetailAddList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察客户投资详情失败");
                }
            }
            if (StringUtils.isNotEmpty(miCustomerInvestDetailUpdateList)){
                try {
                    miCustomerInvestDetailMapper.updateMiCustomerInvestDetails(miCustomerInvestDetailUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("批量修改市场洞察客户投资详情失败");
                }
            }
        }
    }

    /**
     * 修改新增市场洞察客户选择表集合数据
     * @param miCustomerChoiceDTOS
     * @param miCustomerChoiceAddList
     * @param miCustomerChoiceUpdateList
     * @param marketInsightCustomer
     */
    private void packMiCustomerChoiceUpdates(List<MiCustomerChoiceDTO> miCustomerChoiceDTOS, List<MiCustomerChoice> miCustomerChoiceAddList, List<MiCustomerChoice> miCustomerChoiceUpdateList, MarketInsightCustomer marketInsightCustomer) {
        if (StringUtils.isNotEmpty(miCustomerChoiceDTOS)){
            //根据市场洞察客户主表主键查询市场洞察客户选择表
            List<MiCustomerChoiceDTO> miCustomerChoiceDTOSList = miCustomerChoiceMapper.selectMiCustomerChoiceByMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
            if (StringUtils.isNotEmpty(miCustomerChoiceDTOSList)){
                List<Long> miCustomerChoiceIds = new ArrayList<>();
                //sterm流求差集
                miCustomerChoiceIds = miCustomerChoiceDTOSList.stream().filter(a ->
                        !miCustomerChoiceDTOS.stream().map(MiCustomerChoiceDTO::getMiCustomerChoiceId).collect(Collectors.toList()).contains(a.getMiCustomerChoiceId())
                ).collect(Collectors.toList()).stream().map(MiCustomerChoiceDTO::getMiCustomerChoiceId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miCustomerChoiceIds)){
                    try {
                        miCustomerChoiceMapper.logicDeleteMiCustomerChoiceByMiCustomerChoiceIds(miCustomerChoiceIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察客户选择失败");
                    }
                }
            }
            for (int i1 = 0; i1 < miCustomerChoiceDTOS.size(); i1++) {
                Long miCustomerChoiceId = miCustomerChoiceDTOS.get(i1).getMiCustomerChoiceId();
                if (null == miCustomerChoiceId){
                    MiCustomerChoice miCustomerChoice = new MiCustomerChoice();
                    BeanUtils.copyProperties(miCustomerChoiceDTOS.get(i1),miCustomerChoice);
                    miCustomerChoice.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
                    miCustomerChoice.setSort(i1 + 1);
                    miCustomerChoice.setCreateBy(SecurityUtils.getUserId());
                    miCustomerChoice.setCreateTime(DateUtils.getNowDate());
                    miCustomerChoice.setUpdateTime(DateUtils.getNowDate());
                    miCustomerChoice.setUpdateBy(SecurityUtils.getUserId());
                    miCustomerChoice.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    miCustomerChoiceAddList.add(miCustomerChoice);
                }else {
                    MiCustomerChoice miCustomerChoice = new MiCustomerChoice();
                    BeanUtils.copyProperties(miCustomerChoiceDTOS.get(i1),miCustomerChoice);
                    miCustomerChoice.setSort(i1 + 1);
                    miCustomerChoice.setUpdateTime(DateUtils.getNowDate());
                    miCustomerChoice.setUpdateBy(SecurityUtils.getUserId());
                    miCustomerChoiceUpdateList.add(miCustomerChoice);
                }
            }
            if (StringUtils.isNotEmpty(miCustomerChoiceAddList)){
                try {
                    miCustomerChoiceMapper.batchMiCustomerChoice(miCustomerChoiceAddList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察客户选择失败");
                }
            }
            if (StringUtils.isNotEmpty(miCustomerChoiceUpdateList)){
                try {
                    miCustomerChoiceMapper.updateMiCustomerChoices(miCustomerChoiceUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("批量修改市场洞察客户选择失败");
                }
            }
        }else {
            List<MiCustomerChoiceDTO> miCustomerChoiceDTOSList = miCustomerChoiceMapper.selectMiCustomerChoiceByMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
            if (StringUtils.isNotEmpty(miCustomerChoiceDTOSList)){
                List<Long> miCustomerChoiceIds = miCustomerChoiceDTOSList.stream().map(MiCustomerChoiceDTO::getMiCustomerChoiceId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miCustomerChoiceIds)){
                    try {
                        miCustomerChoiceMapper.logicDeleteMiCustomerChoiceByMiCustomerChoiceIds(miCustomerChoiceIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察客户选择失败");
                    }
                }
            }
        }
    }

    /**
     * 逻辑批量删除市场洞察客户表
     *
     * @param marketInsightCustomerIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(List<Long> marketInsightCustomerIds) {
        int i = 0;
        try {
            i = marketInsightCustomerMapper.logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(marketInsightCustomerIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("逻辑批量删除市场洞察客户失败");
        }
        List<MiCustomerChoiceDTO> miCustomerChoiceDTOS = miCustomerChoiceMapper.selectMiCustomerChoiceByMarketInsightCustomerIds(marketInsightCustomerIds);
        if (StringUtils.isNotEmpty(miCustomerChoiceDTOS)) {
            List<Long> miCustomerChoiceIds = miCustomerChoiceDTOS.stream().map(MiCustomerChoiceDTO::getMiCustomerChoiceId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miCustomerChoiceIds)) {
                try {
                    miCustomerChoiceMapper.logicDeleteMiCustomerChoiceByMiCustomerChoiceIds(miCustomerChoiceIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察客户选择失败");
                }

            }
        }

        List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOS = miCustomerInvestPlanMapper.selectMiCustomerInvestPlanByMarketInsightCustomerIds(marketInsightCustomerIds);
        if (StringUtils.isNotEmpty(miCustomerInvestPlanDTOS)) {
            List<Long> miCustomerInvestPlanIds = miCustomerInvestPlanDTOS.stream().map(MiCustomerInvestPlanDTO::getMiCustomerInvestPlanId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miCustomerInvestPlanIds)) {
                try {
                    miCustomerInvestPlanMapper.logicDeleteMiCustomerInvestPlanByMiCustomerInvestPlanIds(miCustomerInvestPlanIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察客户投资计划失败");
                }
                List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOS = miCustomerInvestDetailMapper.selectMiCustomerInvestDetailByMiCustomerInvestPlanIds(miCustomerInvestPlanIds);
                if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOS)) {
                    List<Long> miCustomerInvestDetailIds = miCustomerInvestDetailDTOS.stream().map(MiCustomerInvestDetailDTO::getMiCustomerInvestDetailId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miCustomerInvestDetailIds)) {
                        try {
                            miCustomerInvestDetailMapper.logicDeleteMiCustomerInvestDetailByMiCustomerInvestDetailIds(miCustomerInvestDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察客户投资详情失败");
                        }
                    }
                }
            }
        }
        return i;
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
    @Transactional
    public int logicDeleteMarketInsightCustomerByMarketInsightCustomerId(MarketInsightCustomerDTO marketInsightCustomerDTO) {
        int i = 0;
        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        marketInsightCustomer.setMarketInsightCustomerId(marketInsightCustomerDTO.getMarketInsightCustomerId());
        marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = marketInsightCustomerMapper.logicDeleteMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomer);
        } catch (Exception e) {
            throw new ServiceException("逻辑删除市场洞察客户失败");
        }
        List<MiCustomerChoiceDTO> miCustomerChoiceDTOS = miCustomerChoiceMapper.selectMiCustomerChoiceByMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
        if (StringUtils.isNotEmpty(miCustomerChoiceDTOS)) {
            List<Long> miCustomerChoiceIds = miCustomerChoiceDTOS.stream().map(MiCustomerChoiceDTO::getMiCustomerChoiceId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miCustomerChoiceIds)) {
                try {
                    miCustomerChoiceMapper.logicDeleteMiCustomerChoiceByMiCustomerChoiceIds(miCustomerChoiceIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察客户选择失败");
                }

            }
        }

        List<MiCustomerInvestPlanDTO> miCustomerInvestPlanDTOS = miCustomerInvestPlanMapper.selectMiCustomerInvestPlanByMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
        if (StringUtils.isNotEmpty(miCustomerInvestPlanDTOS)) {
            List<Long> miCustomerInvestPlanIds = miCustomerInvestPlanDTOS.stream().map(MiCustomerInvestPlanDTO::getMiCustomerInvestPlanId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miCustomerInvestPlanIds)) {
                try {
                    miCustomerInvestPlanMapper.logicDeleteMiCustomerInvestPlanByMiCustomerInvestPlanIds(miCustomerInvestPlanIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察客户投资计划失败");
                }
                List<MiCustomerInvestDetailDTO> miCustomerInvestDetailDTOS = miCustomerInvestDetailMapper.selectMiCustomerInvestDetailByMiCustomerInvestPlanIds(miCustomerInvestPlanIds);
                if (StringUtils.isNotEmpty(miCustomerInvestDetailDTOS)) {
                    List<Long> miCustomerInvestDetailIds = miCustomerInvestDetailDTOS.stream().map(MiCustomerInvestDetailDTO::getMiCustomerInvestDetailId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miCustomerInvestDetailIds)) {
                        try {
                            miCustomerInvestDetailMapper.logicDeleteMiCustomerInvestDetailByMiCustomerInvestDetailIds(miCustomerInvestDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察客户投资详情失败");
                        }
                    }
                }
            }
        }
        return i;
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

