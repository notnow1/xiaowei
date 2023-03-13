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
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightOpponent;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiOpponentChoice;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiOpponentFinance;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.*;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightOpponentMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiOpponentChoiceMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiOpponentFinanceMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightOpponentService;
import net.qixiaowei.system.manage.api.dto.basic.*;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.*;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collector;
import java.util.stream.Collectors;


/**
 * MarketInsightOpponentService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-03-12
 */
@Service
public class MarketInsightOpponentServiceImpl implements IMarketInsightOpponentService {
    @Autowired
    private MarketInsightOpponentMapper marketInsightOpponentMapper;
    @Autowired
    private MiOpponentChoiceMapper miOpponentChoiceMapper;
    @Autowired
    private MiOpponentFinanceMapper miOpponentFinanceMapper;


    @Autowired
    private RemoteUserService remoteUserService;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
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

    @Autowired
    private RemoteIndicatorService remoteIndicatorService;

    /**
     * 查询市场洞察对手表
     *
     * @param marketInsightOpponentId 市场洞察对手表主键
     * @return 市场洞察对手表
     */
    @Override
    public MarketInsightOpponentDTO selectMarketInsightOpponentByMarketInsightOpponentId(Long marketInsightOpponentId) {
        MarketInsightOpponentDTO marketInsightOpponentDTO = marketInsightOpponentMapper.selectMarketInsightOpponentByMarketInsightOpponentId(marketInsightOpponentId);
        List<Map<String, Object>> dropList = PlanBusinessUnitCode.getDropList(marketInsightOpponentDTO.getBusinessUnitDecompose());
        marketInsightOpponentDTO.setBusinessUnitDecomposes(dropList);
        Long productId = marketInsightOpponentDTO.getProductId();
        Long areaId = marketInsightOpponentDTO.getAreaId();
        Long departmentId = marketInsightOpponentDTO.getDepartmentId();
        Long industryId = marketInsightOpponentDTO.getIndustryId();
        if (null != productId) {
            R<ProductDTO> productDTOR = remoteProductService.remoteSelectById(productId, SecurityConstants.INNER);
            ProductDTO data = productDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightOpponentDTO.setProductName(data.getProductName());
            }
        }
        if (null != areaId) {
            R<AreaDTO> areaDTOR = remoteAreaService.getById(areaId, SecurityConstants.INNER);
            AreaDTO data = areaDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightOpponentDTO.setAreaName(data.getAreaName());
            }
        }
        if (null != departmentId) {
            R<DepartmentDTO> departmentDTOR = remoteDepartmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
            DepartmentDTO data = departmentDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightOpponentDTO.setDepartmentName(data.getDepartmentName());
            }
        }
        if (null != industryId) {
            R<IndustryDTO> industryDTOR = remoteIndustryService.selectById(industryId, SecurityConstants.INNER);
            IndustryDTO data = industryDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightOpponentDTO.setIndustryName(data.getIndustryName());
            }
        }

        List<MiOpponentChoiceDTO> miOpponentChoiceDTOList = miOpponentChoiceMapper.selectMiOpponentChoiceByMarketInsightOpponentId(marketInsightOpponentId);
        //封装远程市场洞察对手选择赋值
        this.packMiOpponentChoices(miOpponentChoiceDTOList);
        marketInsightOpponentDTO.setMiOpponentChoiceDTOS(miOpponentChoiceDTOList);
        return marketInsightOpponentDTO;
    }

    /**
     * 封装远程市场洞察对手选择赋值
     *
     * @param miOpponentChoiceDTOList
     */
    private void packMiOpponentChoices(List<MiOpponentChoiceDTO> miOpponentChoiceDTOList) {
        if (StringUtils.isNotEmpty(miOpponentChoiceDTOList)) {
            List<Long> miOpponentChoiceIds = miOpponentChoiceDTOList.stream().map(MiOpponentChoiceDTO::getMiOpponentChoiceId).collect(Collectors.toList());

            //行业id集合
            List<Long> industryIds = miOpponentChoiceDTOList.stream().filter(f -> null != f.getIndustryId()).map(MiOpponentChoiceDTO::getIndustryId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(industryIds)) {
                R<List<IndustryDTO>> industryList = remoteIndustryService.selectByIds(industryIds, SecurityConstants.INNER);
                List<IndustryDTO> data = industryList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiOpponentChoiceDTO miOpponentChoiceDTO : miOpponentChoiceDTOList) {
                        if (null != miOpponentChoiceDTO.getIndustryId()) {
                            for (IndustryDTO datum : data) {
                                if (miOpponentChoiceDTO.getIndustryId().equals(datum.getIndustryId())) {
                                    miOpponentChoiceDTO.setIndustryName(datum.getIndustryName());
                                }
                            }
                        }
                    }
                }
            }
            //对比项目集合
            List<Long> comparisonItems = miOpponentChoiceDTOList.stream().filter(f -> null != f.getComparisonItem()).map(MiOpponentChoiceDTO::getComparisonItem).collect(Collectors.toList());
            //竞争对手类别集合
            List<Long> competitorCategorys = miOpponentChoiceDTOList.stream().filter(f -> null != f.getCompetitorCategory()).map(MiOpponentChoiceDTO::getCompetitorCategory).collect(Collectors.toList());
            //竞争战略类型集合
            List<Long> competitionStrategyTypes = miOpponentChoiceDTOList.stream().filter(f -> null != f.getCompetitionStrategyType()).map(MiOpponentChoiceDTO::getCompetitionStrategyType).collect(Collectors.toList());

            if (StringUtils.isNotEmpty(comparisonItems)) {
                R<List<DictionaryDataDTO>> dataByDictionaryDataList = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(comparisonItems, SecurityConstants.INNER);
                List<DictionaryDataDTO> data = dataByDictionaryDataList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiOpponentChoiceDTO miOpponentChoiceDTO : miOpponentChoiceDTOList) {
                        if (null != miOpponentChoiceDTO.getComparisonItem()) {
                            for (DictionaryDataDTO datum : data) {
                                if (miOpponentChoiceDTO.getComparisonItem().equals(datum.getDictionaryDataId())) {
                                    miOpponentChoiceDTO.setComparisonItemName(datum.getDictionaryLabel());
                                }
                            }
                        }
                    }
                }
            }

            if (StringUtils.isNotEmpty(competitorCategorys)) {
                R<List<DictionaryDataDTO>> dataByDictionaryDataList = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(competitorCategorys, SecurityConstants.INNER);
                List<DictionaryDataDTO> data = dataByDictionaryDataList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiOpponentChoiceDTO miOpponentChoiceDTO : miOpponentChoiceDTOList) {
                        if (null != miOpponentChoiceDTO.getCompetitorCategory()) {
                            for (DictionaryDataDTO datum : data) {
                                if (miOpponentChoiceDTO.getCompetitorCategory().equals(datum.getDictionaryDataId())) {
                                    miOpponentChoiceDTO.setCompetitorCategoryName(datum.getDictionaryLabel());
                                }
                            }
                        }
                    }
                }
            }


            if (StringUtils.isNotEmpty(competitionStrategyTypes)) {
                R<List<DictionaryDataDTO>> dataByDictionaryDataList = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(competitionStrategyTypes, SecurityConstants.INNER);
                List<DictionaryDataDTO> data = dataByDictionaryDataList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiOpponentChoiceDTO miOpponentChoiceDTO : miOpponentChoiceDTOList) {
                        if (null != miOpponentChoiceDTO.getCompetitionStrategyType()) {
                            for (DictionaryDataDTO datum : data) {
                                if (miOpponentChoiceDTO.getCompetitionStrategyType().equals(datum.getDictionaryDataId())) {
                                    miOpponentChoiceDTO.setCompetitionStrategyTypeName(datum.getDictionaryLabel());
                                }
                            }
                        }
                    }
                }
            }
            //封装市场洞察对手财务远程赋值
            packMiOpponentFinanceDTOList(miOpponentChoiceDTOList, miOpponentChoiceIds);
        }
    }

    /**
     * 封装市场洞察对手财务远程赋值
     * @param miOpponentChoiceDTOList
     * @param miOpponentChoiceIds
     */
    private void packMiOpponentFinanceDTOList(List<MiOpponentChoiceDTO> miOpponentChoiceDTOList, List<Long> miOpponentChoiceIds) {
        if (StringUtils.isNotEmpty(miOpponentChoiceIds)){
            List<MiOpponentFinanceDTO> miOpponentFinanceDTOList = miOpponentFinanceMapper.selectMiOpponentFinanceByMiOpponentChoiceIds(miOpponentChoiceIds);
            if (StringUtils.isNotEmpty(miOpponentFinanceDTOList)){
                //行业id集合
                List<Long> indicatorIds = miOpponentFinanceDTOList.stream().filter(f -> null != f.getIndicatorId()).map(MiOpponentFinanceDTO::getIndicatorId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(indicatorIds)) {
                    R<List<IndicatorDTO>> indicatorList = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);

                    List<IndicatorDTO> data = indicatorList.getData();
                    if (StringUtils.isNotNull(data)) {
                        for (MiOpponentFinanceDTO miOpponentFinanceDTO : miOpponentFinanceDTOList) {
                            if (null != miOpponentFinanceDTO.getIndicatorId()) {
                                for (IndicatorDTO datum : data) {
                                    if (miOpponentFinanceDTO.getIndicatorId().equals(datum.getIndicatorId())) {
                                        miOpponentFinanceDTO.setIndicatorName(datum.getIndicatorName());
                                    }
                                }
                            }
                        }
                    }
                }
            }
            //根据市场洞察对手选择ID分组
            Map<Long, List<MiOpponentFinanceDTO>> miOpponentFinanceDTOMapList = miOpponentFinanceDTOList.parallelStream().collect(Collectors.groupingBy(MiOpponentFinanceDTO::getMiOpponentChoiceId));
            if (StringUtils.isNotEmpty(miOpponentChoiceDTOList)&& StringUtils.isNotEmpty(miOpponentFinanceDTOMapList)){
                for (MiOpponentChoiceDTO miOpponentChoiceDTO : miOpponentChoiceDTOList) {
                    miOpponentChoiceDTO.setMiOpponentFinanceDTOS(miOpponentFinanceDTOMapList.get(miOpponentChoiceDTO.getMiOpponentChoiceId()));
                }
            }
        }
    }

    /**
     * 查询市场洞察对手表列表
     *
     * @param marketInsightOpponentDTO 市场洞察对手表
     * @return 市场洞察对手表
     */
    @Override
    public List<MarketInsightOpponentDTO> selectMarketInsightOpponentList(MarketInsightOpponentDTO marketInsightOpponentDTO) {
        List<String> createByList = new ArrayList<>();
        MarketInsightOpponent marketInsightOpponent = new MarketInsightOpponent();
        BeanUtils.copyProperties(marketInsightOpponentDTO, marketInsightOpponent);
        //高级搜索请求参数
        Map<String, Object> params = marketInsightOpponentDTO.getParams();
        this.queryemployeeName(params);
        marketInsightOpponentDTO.setParams(params);
        if (StringUtils.isNotNull(marketInsightOpponentDTO.getCreateByName())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(marketInsightOpponentDTO.getCreateByName());
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
        marketInsightOpponentDTO.setCreateBys(createByList);
        List<MarketInsightOpponentDTO> marketInsightOpponentDTOS = marketInsightOpponentMapper.selectMarketInsightOpponentList(marketInsightOpponent);
        if (StringUtils.isNotEmpty(marketInsightOpponentDTOS)) {
            Set<Long> createBys = marketInsightOpponentDTOS.stream().map(MarketInsightOpponentDTO::getCreateBy).collect(Collectors.toSet());
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
            List<UserDTO> userDTOList = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(userDTOList)) {
                for (MarketInsightOpponentDTO insightOpponentDTO : marketInsightOpponentDTOS) {
                    for (UserDTO userDTO : userDTOList) {
                        if (insightOpponentDTO.getCreateBy().equals(userDTO.getUserId())) {
                            insightOpponentDTO.setCreateByName(userDTO.getEmployeeName());
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotEmpty(marketInsightOpponentDTOS)) {
            List<Long> productIds = marketInsightOpponentDTOS.stream().filter(f -> null != f.getProductId()).map(MarketInsightOpponentDTO::getProductId).collect(Collectors.toList());
            List<Long> areaIds = marketInsightOpponentDTOS.stream().filter(f -> null != f.getAreaId()).map(MarketInsightOpponentDTO::getAreaId).collect(Collectors.toList());
            List<Long> departmentIds = marketInsightOpponentDTOS.stream().filter(f -> null != f.getDepartmentId()).map(MarketInsightOpponentDTO::getDepartmentId).collect(Collectors.toList());
            List<Long> industryIds = marketInsightOpponentDTOS.stream().filter(f -> null != f.getIndustryId()).map(MarketInsightOpponentDTO::getIndustryId).collect(Collectors.toList());

            if (StringUtils.isNotEmpty(productIds)) {
                R<List<ProductDTO>> productList = remoteProductService.getName(productIds, SecurityConstants.INNER);
                List<ProductDTO> data = productList.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (MarketInsightOpponentDTO insightOpponentDTO : marketInsightOpponentDTOS) {
                        Long productId = insightOpponentDTO.getProductId();
                        for (ProductDTO datum : data) {
                            if (null != productId) {
                                if (insightOpponentDTO.getProductId().equals(datum.getProductId())) {
                                    insightOpponentDTO.setProductName(datum.getProductName());
                                }
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(areaIds)) {
                R<List<AreaDTO>> areaList = remoteAreaService.selectAreaListByAreaIds(areaIds, SecurityConstants.INNER);
                List<AreaDTO> areaListData = areaList.getData();
                if (StringUtils.isNotNull(areaListData)) {
                    for (MarketInsightOpponentDTO insightOpponentDTO : marketInsightOpponentDTOS) {
                        Long areaId = insightOpponentDTO.getAreaId();
                        for (AreaDTO areaListDatum : areaListData) {
                            if (null != areaId) {
                                if (insightOpponentDTO.getAreaId().equals(areaListDatum.getAreaId())) {
                                    insightOpponentDTO.setAreaName(areaListDatum.getAreaName());
                                }
                            }

                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(departmentIds)) {
                R<List<DepartmentDTO>> departmentList = remoteDepartmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
                List<DepartmentDTO> data = departmentList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MarketInsightOpponentDTO insightOpponentDTO : marketInsightOpponentDTOS) {
                        Long departmentId = insightOpponentDTO.getDepartmentId();
                        for (DepartmentDTO datum : data) {
                            if (null != departmentId) {
                                if (insightOpponentDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                    insightOpponentDTO.setDepartmentName(datum.getDepartmentName());
                                }
                            }

                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(industryIds)) {
                R<List<IndustryDTO>> industryList = remoteIndustryService.selectByIds(industryIds, SecurityConstants.INNER);
                List<IndustryDTO> data = industryList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MarketInsightOpponentDTO insightOpponentDTO : marketInsightOpponentDTOS) {
                        Long industryId = insightOpponentDTO.getIndustryId();
                        for (IndustryDTO datum : data) {
                            if (null != industryId) {
                                if (insightOpponentDTO.getIndustryId().equals(datum.getIndustryId())) {
                                    insightOpponentDTO.setIndustryName(datum.getIndustryName());
                                }
                            }
                        }
                    }
                }
            }
        }

        return marketInsightOpponentDTOS;
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
     * 新增市场洞察对手表
     *
     * @param marketInsightOpponentDTO 市场洞察对手表
     * @return 结果
     */
    @Override
    @Transactional
    public MarketInsightOpponentDTO insertMarketInsightOpponent(MarketInsightOpponentDTO marketInsightOpponentDTO) {
        MarketInsightOpponent marketInsightOpponent = new MarketInsightOpponent();
        BeanUtils.copyProperties(marketInsightOpponentDTO, marketInsightOpponent);
        marketInsightOpponent.setCreateBy(SecurityUtils.getUserId());
        marketInsightOpponent.setCreateTime(DateUtils.getNowDate());
        marketInsightOpponent.setUpdateTime(DateUtils.getNowDate());
        marketInsightOpponent.setUpdateBy(SecurityUtils.getUserId());
        marketInsightOpponent.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            marketInsightOpponentMapper.insertMarketInsightOpponent(marketInsightOpponent);
        } catch (Exception e) {
            throw new ServiceException("新增市场洞察对手失败！");
        }
        marketInsightOpponentDTO.setMarketInsightOpponentId(marketInsightOpponent.getMarketInsightOpponentId());
        //市场洞察对手选择集合
        List<MiOpponentChoiceDTO> miOpponentChoiceDTOS = marketInsightOpponentDTO.getMiOpponentChoiceDTOS();
        //保存数据库
        List<MiOpponentChoice> miOpponentChoiceList = new ArrayList<>();
        if (StringUtils.isNotEmpty(miOpponentChoiceDTOS)) {
            for (int i = 0; i < miOpponentChoiceDTOS.size(); i++) {
                MiOpponentChoice miOpponentChoice = new MiOpponentChoice();
                BeanUtils.copyProperties(miOpponentChoiceDTOS.get(i), miOpponentChoice);
                //市场洞察对手ID
                miOpponentChoice.setMarketInsightOpponentId(marketInsightOpponent.getMarketInsightOpponentId());
                miOpponentChoice.setSort(i + 1);
                miOpponentChoice.setCreateBy(SecurityUtils.getUserId());
                miOpponentChoice.setCreateTime(DateUtils.getNowDate());
                miOpponentChoice.setUpdateTime(DateUtils.getNowDate());
                miOpponentChoice.setUpdateBy(SecurityUtils.getUserId());
                miOpponentChoice.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miOpponentChoiceList.add(miOpponentChoice);
            }
        }
        if (StringUtils.isNotEmpty(miOpponentChoiceList)) {
            try {
                miOpponentChoiceMapper.batchMiOpponentChoice(miOpponentChoiceList);
            } catch (Exception e) {
                throw new ServiceException("批量新增市场洞察对手选择失败");
            }
        }
        if (StringUtils.isNotEmpty(miOpponentChoiceList)) {
            for (int i = 0; i < miOpponentChoiceList.size(); i++) {
                //前台市场洞察对手财务集合
                List<MiOpponentFinanceDTO> miOpponentFinanceDTOS = miOpponentChoiceDTOS.get(i).getMiOpponentFinanceDTOS();
                //保存数据库
                List<MiOpponentFinance> miOpponentFinanceList = new ArrayList<>();
                if (StringUtils.isNotEmpty(miOpponentFinanceDTOS)) {
                    for (int i1 = 0; i1 < miOpponentFinanceDTOS.size(); i1++) {
                        MiOpponentFinance miOpponentFinance = new MiOpponentFinance();
                        BeanUtils.copyProperties(miOpponentFinanceDTOS.get(i1), miOpponentFinance);
                        //市场洞察对手ID
                        miOpponentFinance.setMarketInsightOpponentId(marketInsightOpponent.getMarketInsightOpponentId());
                        //市场洞察对手选择ID
                        miOpponentFinance.setMiOpponentChoiceId(miOpponentChoiceList.get(i).getMiOpponentChoiceId());
                        miOpponentFinance.setSort(i + 1);
                        miOpponentFinance.setCreateBy(SecurityUtils.getUserId());
                        miOpponentFinance.setCreateTime(DateUtils.getNowDate());
                        miOpponentFinance.setUpdateTime(DateUtils.getNowDate());
                        miOpponentFinance.setUpdateBy(SecurityUtils.getUserId());
                        miOpponentFinance.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        miOpponentFinanceList.add(miOpponentFinance);

                    }
                }
                if (StringUtils.isNotEmpty(miOpponentFinanceList)) {
                    try {
                        miOpponentFinanceMapper.batchMiOpponentFinance(miOpponentFinanceList);
                    } catch (Exception e) {
                        throw new ServiceException("批量新增市场洞察对手财务失败");
                    }
                }
            }

        }
        return marketInsightOpponentDTO;
    }

    /**
     * 修改市场洞察对手表
     *
     * @param marketInsightOpponentDTO 市场洞察对手表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateMarketInsightOpponent(MarketInsightOpponentDTO marketInsightOpponentDTO) {
        MarketInsightOpponent marketInsightOpponent = new MarketInsightOpponent();
        BeanUtils.copyProperties(marketInsightOpponentDTO, marketInsightOpponent);
        marketInsightOpponent.setUpdateTime(DateUtils.getNowDate());
        marketInsightOpponent.setUpdateBy(SecurityUtils.getUserId());
        return marketInsightOpponentMapper.updateMarketInsightOpponent(marketInsightOpponent);
    }

    /**
     * 逻辑批量删除市场洞察对手表
     *
     * @param marketInsightOpponentIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightOpponentByMarketInsightOpponentIds(List<Long> marketInsightOpponentIds) {
        int i = 0;
        try {
            i = marketInsightOpponentMapper.logicDeleteMarketInsightOpponentByMarketInsightOpponentIds(marketInsightOpponentIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("逻辑批量删除市场洞察对手失败！");
        }
        List<MiOpponentChoiceDTO> miOpponentChoiceDTOList = miOpponentChoiceMapper.selectMiOpponentChoiceByMarketInsightOpponentIds(marketInsightOpponentIds);
        if (StringUtils.isNotEmpty(miOpponentChoiceDTOList)) {
            List<Long> miOpponentChoiceIds = miOpponentChoiceDTOList.stream().map(MiOpponentChoiceDTO::getMiOpponentChoiceId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miOpponentChoiceIds)) {
                try {
                    miOpponentChoiceMapper.logicDeleteMiOpponentChoiceByMiOpponentChoiceIds(miOpponentChoiceIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察对手选择失败！");
                }
                List<MiOpponentFinanceDTO> miOpponentFinanceDTOList = miOpponentFinanceMapper.selectMiOpponentFinanceByMiOpponentChoiceIds(miOpponentChoiceIds);
                if (StringUtils.isNotEmpty(miOpponentFinanceDTOList)) {
                    List<Long> miOpponentFinanceIds = miOpponentFinanceDTOList.stream().map(MiOpponentFinanceDTO::getMiOpponentFinanceId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miOpponentFinanceIds)) {
                        try {
                            miOpponentFinanceMapper.logicDeleteMiOpponentFinanceByMiOpponentFinanceIds(miOpponentFinanceIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察对手财务失败！");
                        }
                    }
                }
            }

        }
        return i;
    }

    /**
     * 物理删除市场洞察对手表信息
     *
     * @param marketInsightOpponentId 市场洞察对手表主键
     * @return 结果
     */
    @Override
    public int deleteMarketInsightOpponentByMarketInsightOpponentId(Long marketInsightOpponentId) {
        return marketInsightOpponentMapper.deleteMarketInsightOpponentByMarketInsightOpponentId(marketInsightOpponentId);
    }

    /**
     * 逻辑删除市场洞察对手表信息
     *
     * @param marketInsightOpponentDTO 市场洞察对手表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightOpponentByMarketInsightOpponentId(MarketInsightOpponentDTO marketInsightOpponentDTO) {
        int i = 0;
        MarketInsightOpponent marketInsightOpponent = new MarketInsightOpponent();
        marketInsightOpponent.setMarketInsightOpponentId(marketInsightOpponentDTO.getMarketInsightOpponentId());
        marketInsightOpponent.setUpdateTime(DateUtils.getNowDate());
        marketInsightOpponent.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = marketInsightOpponentMapper.logicDeleteMarketInsightOpponentByMarketInsightOpponentId(marketInsightOpponent);
        } catch (Exception e) {
            throw new ServiceException("逻辑删除市场洞察对手失败");
        }
        List<MiOpponentChoiceDTO> miOpponentChoiceDTOList = miOpponentChoiceMapper.selectMiOpponentChoiceByMarketInsightOpponentId(marketInsightOpponentDTO.getMarketInsightOpponentId());
        if (StringUtils.isNotEmpty(miOpponentChoiceDTOList)) {
            List<Long> miOpponentChoiceIds = miOpponentChoiceDTOList.stream().map(MiOpponentChoiceDTO::getMiOpponentChoiceId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miOpponentChoiceIds)) {
                try {
                    miOpponentChoiceMapper.logicDeleteMiOpponentChoiceByMiOpponentChoiceIds(miOpponentChoiceIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察对手选择失败！");
                }
                List<MiOpponentFinanceDTO> miOpponentFinanceDTOList = miOpponentFinanceMapper.selectMiOpponentFinanceByMiOpponentChoiceIds(miOpponentChoiceIds);
                if (StringUtils.isNotEmpty(miOpponentFinanceDTOList)) {
                    List<Long> miOpponentFinanceIds = miOpponentFinanceDTOList.stream().map(MiOpponentFinanceDTO::getMiOpponentFinanceId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miOpponentFinanceIds)) {
                        try {
                            miOpponentFinanceMapper.logicDeleteMiOpponentFinanceByMiOpponentFinanceIds(miOpponentFinanceIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察对手财务失败！");
                        }
                    }
                }
            }

        }
        return i;
    }

    /**
     * 物理删除市场洞察对手表信息
     *
     * @param marketInsightOpponentDTO 市场洞察对手表
     * @return 结果
     */

    @Override
    public int deleteMarketInsightOpponentByMarketInsightOpponentId(MarketInsightOpponentDTO marketInsightOpponentDTO) {
        MarketInsightOpponent marketInsightOpponent = new MarketInsightOpponent();
        BeanUtils.copyProperties(marketInsightOpponentDTO, marketInsightOpponent);
        return marketInsightOpponentMapper.deleteMarketInsightOpponentByMarketInsightOpponentId(marketInsightOpponent.getMarketInsightOpponentId());
    }

    /**
     * 物理批量删除市场洞察对手表
     *
     * @param marketInsightOpponentDtos 需要删除的市场洞察对手表主键
     * @return 结果
     */

    @Override
    public int deleteMarketInsightOpponentByMarketInsightOpponentIds(List<MarketInsightOpponentDTO> marketInsightOpponentDtos) {
        List<Long> stringList = new ArrayList();
        for (MarketInsightOpponentDTO marketInsightOpponentDTO : marketInsightOpponentDtos) {
            stringList.add(marketInsightOpponentDTO.getMarketInsightOpponentId());
        }
        return marketInsightOpponentMapper.deleteMarketInsightOpponentByMarketInsightOpponentIds(stringList);
    }

    /**
     * 批量新增市场洞察对手表信息
     *
     * @param marketInsightOpponentDtos 市场洞察对手表对象
     */

    public int insertMarketInsightOpponents(List<MarketInsightOpponentDTO> marketInsightOpponentDtos) {
        List<MarketInsightOpponent> marketInsightOpponentList = new ArrayList();

        for (MarketInsightOpponentDTO marketInsightOpponentDTO : marketInsightOpponentDtos) {
            MarketInsightOpponent marketInsightOpponent = new MarketInsightOpponent();
            BeanUtils.copyProperties(marketInsightOpponentDTO, marketInsightOpponent);
            marketInsightOpponent.setCreateBy(SecurityUtils.getUserId());
            marketInsightOpponent.setCreateTime(DateUtils.getNowDate());
            marketInsightOpponent.setUpdateTime(DateUtils.getNowDate());
            marketInsightOpponent.setUpdateBy(SecurityUtils.getUserId());
            marketInsightOpponent.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            marketInsightOpponentList.add(marketInsightOpponent);
        }
        return marketInsightOpponentMapper.batchMarketInsightOpponent(marketInsightOpponentList);
    }

    /**
     * 批量修改市场洞察对手表信息
     *
     * @param marketInsightOpponentDtos 市场洞察对手表对象
     */

    public int updateMarketInsightOpponents(List<MarketInsightOpponentDTO> marketInsightOpponentDtos) {
        List<MarketInsightOpponent> marketInsightOpponentList = new ArrayList();

        for (MarketInsightOpponentDTO marketInsightOpponentDTO : marketInsightOpponentDtos) {
            MarketInsightOpponent marketInsightOpponent = new MarketInsightOpponent();
            BeanUtils.copyProperties(marketInsightOpponentDTO, marketInsightOpponent);
            marketInsightOpponent.setCreateBy(SecurityUtils.getUserId());
            marketInsightOpponent.setCreateTime(DateUtils.getNowDate());
            marketInsightOpponent.setUpdateTime(DateUtils.getNowDate());
            marketInsightOpponent.setUpdateBy(SecurityUtils.getUserId());
            marketInsightOpponentList.add(marketInsightOpponent);
        }
        return marketInsightOpponentMapper.updateMarketInsightOpponents(marketInsightOpponentList);
    }
}

