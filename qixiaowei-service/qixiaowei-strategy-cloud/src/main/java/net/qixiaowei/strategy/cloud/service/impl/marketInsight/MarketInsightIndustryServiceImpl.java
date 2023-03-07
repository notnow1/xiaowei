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
import net.qixiaowei.strategy.cloud.mapper.marketInsight.*;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightIndustryService;
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

import java.util.*;
import java.util.stream.Collectors;


/**
 * MarketInsightIndustryService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-03-03
 */
@Service
public class MarketInsightIndustryServiceImpl implements IMarketInsightIndustryService {
    @Autowired
    private MarketInsightIndustryMapper marketInsightIndustryMapper;
    @Autowired
    private MiIndustryDetailMapper miIndustryDetailMapper;

    @Autowired
    private MiIndustryEstimateMapper miIndustryEstimateMapper;

    @Autowired
    private MiIndustryAttractionMapper miIndustryAttractionMapper;

    @Autowired
    private MiIndustryAttractionDataMapper miIndustryAttractionDataMapper;
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
     * 查询市场洞察行业表
     *
     * @param marketInsightIndustryId 市场洞察行业表主键
     * @return 市场洞察行业表
     */
    @Override
    public MarketInsightIndustryDTO selectMarketInsightIndustryByMarketInsightIndustryId(Long marketInsightIndustryId) {
        MarketInsightIndustryDTO marketInsightIndustryDTO = marketInsightIndustryMapper.selectMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustryId);
        List<Map<String, Object>> dropList = PlanBusinessUnitCode.getDropList(marketInsightIndustryDTO.getBusinessUnitDecompose());
        marketInsightIndustryDTO.setBusinessUnitDecomposes(dropList);
        Long productId = marketInsightIndustryDTO.getProductId();
        Long areaId = marketInsightIndustryDTO.getAreaId();
        Long departmentId = marketInsightIndustryDTO.getDepartmentId();
        Long industryId = marketInsightIndustryDTO.getIndustryId();
        if (null != productId) {
            R<ProductDTO> productDTOR = remoteProductService.remoteSelectById(productId, SecurityConstants.INNER);
            ProductDTO data = productDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightIndustryDTO.setProductName(data.getProductName());
            }
        }
        if (null != areaId) {
            R<AreaDTO> areaDTOR = remoteAreaService.getById(areaId, SecurityConstants.INNER);
            AreaDTO data = areaDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightIndustryDTO.setAreaName(data.getAreaName());
            }
        }
        if (null != departmentId) {
            R<DepartmentDTO> departmentDTOR = remoteDepartmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
            DepartmentDTO data = departmentDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightIndustryDTO.setDepartmentName(data.getDepartmentName());
            }
        }
        if (null != industryId) {
            R<IndustryDTO> industryDTOR = remoteIndustryService.selectById(industryId, SecurityConstants.INNER);
            IndustryDTO data = industryDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightIndustryDTO.setIndustryName(data.getIndustryName());
            }
        }
        List<MiIndustryDetailDTO> miIndustryDetailDTOS = miIndustryDetailMapper.selectMiIndustryDetailByMarketInsightIndustryId(marketInsightIndustryId);
        if (StringUtils.isNotEmpty(miIndustryDetailDTOS)) {
            List<Long> miIndustryDetailIds = miIndustryDetailDTOS.stream().map(MiIndustryDetailDTO::getMiIndustryDetailId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miIndustryDetailIds)) {
                //根据市场洞察行业详情表主键集合批量查询市场洞察行业预估表
                List<MiIndustryEstimateDTO> miIndustryEstimateDTOS = miIndustryEstimateMapper.selectMiIndustryEstimateByMiIndustryDetailIds(miIndustryDetailIds);
                //根据市场洞察行业详情表主键集合批量查询市场洞察行业吸引力数据表
                List<MiIndustryAttractionDataDTO> miIndustryAttractionDataDTOS = miIndustryAttractionDataMapper.selectMiIndustryAttractionDataByMiIndustryDetailIds(miIndustryDetailIds);
                Map<Long, List<MiIndustryAttractionDataDTO>> miIndustryAttractionDataDTOMap = new HashMap<>();
                if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOS)) {
                    //根据详情id分组
                    miIndustryAttractionDataDTOMap = miIndustryAttractionDataDTOS.parallelStream().collect(Collectors.groupingBy(MiIndustryAttractionDataDTO::getMiIndustryDetailId));

                }
                if (StringUtils.isNotEmpty(miIndustryEstimateDTOS)) {
                    //根据详情id分组
                    Map<Long, List<MiIndustryEstimateDTO>> miIndustryEstimateDTOMap = miIndustryEstimateDTOS.parallelStream().collect(Collectors.groupingBy(MiIndustryEstimateDTO::getMiIndustryDetailId));
                    for (int i = 0; i < miIndustryDetailDTOS.size(); i++) {
                        if (i == 0) {
                            List<MiIndustryAttractionDTO> miIndustryAttractionDTOS = miIndustryAttractionMapper.selectMiIndustryAttractionByMarketInsightIndustryId(marketInsightIndustryId);
                            miIndustryDetailDTOS.get(i).setMiIndustryAttractionDTOS(miIndustryAttractionDTOS);
                        }
                        miIndustryDetailDTOS.get(i).setMiIndustryEstimateDTOS(miIndustryEstimateDTOMap.get(miIndustryDetailDTOS.get(i).getMiIndustryDetailId()));
                        if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOMap)) {
                            miIndustryDetailDTOS.get(i).setMiIndustryAttractionDataDTOS(miIndustryAttractionDataDTOMap.get(miIndustryDetailDTOS.get(i).getMiIndustryDetailId()));
                        }
                    }
                }
            }
        }

        return marketInsightIndustryDTO;
    }

    /**
     * 查询市场洞察行业表列表
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 市场洞察行业表
     */
    @Override
    public List<MarketInsightIndustryDTO> selectMarketInsightIndustryList(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        List<String> createByList = new ArrayList<>();
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        Map<String, Object> params = marketInsightIndustryDTO.getParams();
        BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
        this.queryemployeeName(params);
        marketInsightIndustry.setParams(params);
        if (StringUtils.isNotNull(marketInsightIndustryDTO.getCreateByName())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(marketInsightIndustryDTO.getCreateByName());
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
        marketInsightIndustry.setCreateBys(createByList);
        List<MarketInsightIndustryDTO> marketInsightIndustryDTOS = marketInsightIndustryMapper.selectMarketInsightIndustryList(marketInsightIndustry);
        if (StringUtils.isNotEmpty(marketInsightIndustryDTOS)) {
            Set<Long> createBys = marketInsightIndustryDTOS.stream().map(MarketInsightIndustryDTO::getCreateBy).collect(Collectors.toSet());
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
            List<UserDTO> userDTOList = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(userDTOList)) {
                for (MarketInsightIndustryDTO insightIndustryDTO : marketInsightIndustryDTOS) {
                    for (UserDTO userDTO : userDTOList) {
                        if (insightIndustryDTO.getCreateBy().equals(userDTO.getUserId())) {
                            insightIndustryDTO.setCreateByName(userDTO.getEmployeeName());
                        }
                    }
                }
            }
        }

        if (StringUtils.isNotEmpty(marketInsightIndustryDTOS)) {
            List<Long> productIds = marketInsightIndustryDTOS.stream().filter(f -> null != f.getProductId()).map(MarketInsightIndustryDTO::getProductId).collect(Collectors.toList());
            List<Long> areaIds = marketInsightIndustryDTOS.stream().filter(f -> null != f.getAreaId()).map(MarketInsightIndustryDTO::getAreaId).collect(Collectors.toList());
            List<Long> departmentIds = marketInsightIndustryDTOS.stream().filter(f -> null != f.getDepartmentId()).map(MarketInsightIndustryDTO::getDepartmentId).collect(Collectors.toList());
            List<Long> industryIds = marketInsightIndustryDTOS.stream().filter(f -> null != f.getIndustryId()).map(MarketInsightIndustryDTO::getIndustryId).collect(Collectors.toList());

            if (StringUtils.isNotEmpty(productIds)) {
                R<List<ProductDTO>> productList = remoteProductService.getName(productIds, SecurityConstants.INNER);
                List<ProductDTO> data = productList.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (MarketInsightIndustryDTO insightIndustryDTO : marketInsightIndustryDTOS) {
                        for (ProductDTO datum : data) {
                            if (insightIndustryDTO.getProductId().equals(datum.getProductId())) {
                                insightIndustryDTO.setProductName(datum.getProductName());
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(areaIds)) {
                R<List<AreaDTO>> areaList = remoteAreaService.selectAreaListByAreaIds(areaIds, SecurityConstants.INNER);
                List<AreaDTO> areaListData = areaList.getData();
                if (StringUtils.isNotNull(areaListData)) {
                    for (MarketInsightIndustryDTO insightIndustryDTO : marketInsightIndustryDTOS) {
                        for (AreaDTO areaListDatum : areaListData) {
                            if (insightIndustryDTO.getAreaId().equals(areaListDatum.getAreaId())) {
                                insightIndustryDTO.setAreaName(areaListDatum.getAreaName());
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(departmentIds)) {
                R<List<DepartmentDTO>> departmentList = remoteDepartmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
                List<DepartmentDTO> data = departmentList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MarketInsightIndustryDTO insightIndustryDTO : marketInsightIndustryDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (insightIndustryDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                insightIndustryDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(industryIds)) {
                R<List<IndustryDTO>> industryList = remoteIndustryService.selectByIds(industryIds, SecurityConstants.INNER);
                List<IndustryDTO> data = industryList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MarketInsightIndustryDTO insightIndustryDTO : marketInsightIndustryDTOS) {
                        for (IndustryDTO datum : data) {
                            if (insightIndustryDTO.getIndustryId().equals(datum.getIndustryId())) {
                                insightIndustryDTO.setIndustryName(datum.getIndustryName());
                            }
                        }
                    }
                }
            }
        }
        return marketInsightIndustryDTOS;
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
     * 新增市场洞察行业表
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 结果
     */
    @Override
    @Transactional
    public MarketInsightIndustryDTO insertMarketInsightIndustry(MarketInsightIndustryDTO marketInsightIndustryDTO) {

        //前台传入市场洞察行业详情集合
        List<MiIndustryDetailDTO> miIndustryDetailDTOS = marketInsightIndustryDTO.getMiIndustryDetailDTOS();
        //新增市场洞察行业详情集合
        List<MiIndustryDetail> miIndustryDetailList = new ArrayList<>();

        //新增市场洞察行业吸引力集合
        List<MiIndustryAttraction> miIndustryAttractionList = new ArrayList<>();
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
        List<MarketInsightIndustryDTO> marketInsightIndustryDTOS = marketInsightIndustryMapper.selectMarketInsightIndustryList(marketInsightIndustry);
        if (StringUtils.isNotEmpty(marketInsightIndustryDTOS)) {
            throw new ServiceException(marketInsightIndustry.getPlanYear() + "年数据已存在！");
        }
        marketInsightIndustry.setCreateBy(SecurityUtils.getUserId());
        marketInsightIndustry.setCreateTime(DateUtils.getNowDate());
        marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
        marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
        marketInsightIndustry.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        marketInsightIndustryMapper.insertMarketInsightIndustry(marketInsightIndustry);
        //封装新增数据
        this.addData(miIndustryDetailDTOS, miIndustryDetailList, miIndustryAttractionList, marketInsightIndustry);

        marketInsightIndustryDTO.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
        return marketInsightIndustryDTO;
    }

    /**
     * 封装新增数据
     *
     * @param miIndustryDetailDTOS
     * @param miIndustryDetailList
     * @param miIndustryAttractionList
     * @param marketInsightIndustry
     */
    private void addData(List<MiIndustryDetailDTO> miIndustryDetailDTOS, List<MiIndustryDetail> miIndustryDetailList, List<MiIndustryAttraction> miIndustryAttractionList, MarketInsightIndustry marketInsightIndustry) {
        if (StringUtils.isNotEmpty(miIndustryDetailDTOS)) {
            for (int i = 0; i < miIndustryDetailDTOS.size(); i++) {
                MiIndustryDetail miIndustryDetail = new MiIndustryDetail();
                BeanUtils.copyProperties(miIndustryDetailDTOS.get(i), miIndustryDetail);
                miIndustryDetail.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                miIndustryDetail.setCreateBy(SecurityUtils.getUserId());
                miIndustryDetail.setCreateTime(DateUtils.getNowDate());
                miIndustryDetail.setUpdateTime(DateUtils.getNowDate());
                miIndustryDetail.setUpdateBy(SecurityUtils.getUserId());
                miIndustryDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miIndustryDetail.setSort(i + 1);
                miIndustryDetailList.add(miIndustryDetail);
            }
        }
        if (StringUtils.isNotEmpty(miIndustryDetailList)) {
            try {
                miIndustryDetailMapper.batchMiIndustryDetail(miIndustryDetailList);
            } catch (Exception e) {
                throw new ServiceException("批量新增市场洞察行业详情失败");
            }
            for (int i = 0; i < miIndustryDetailList.size(); i++) {
                //新增市场洞察行业预估集
                List<MiIndustryEstimate> miIndustryEstimateList = new ArrayList<>();
                //前台传入市场洞察行业预估集
                List<MiIndustryEstimateDTO> miIndustryEstimateDTOS = miIndustryDetailDTOS.get(i).getMiIndustryEstimateDTOS();
                if (StringUtils.isNotEmpty(miIndustryEstimateDTOS)) {
                    for (MiIndustryEstimateDTO miIndustryEstimateDTO : miIndustryEstimateDTOS) {
                        MiIndustryEstimate miIndustryEstimate = new MiIndustryEstimate();
                        BeanUtils.copyProperties(miIndustryEstimateDTO, miIndustryEstimate);
                        miIndustryEstimate.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                        miIndustryEstimate.setMiIndustryDetailId(miIndustryDetailList.get(i).getMiIndustryDetailId());
                        miIndustryEstimate.setCreateBy(SecurityUtils.getUserId());
                        miIndustryEstimate.setCreateTime(DateUtils.getNowDate());
                        miIndustryEstimate.setUpdateTime(DateUtils.getNowDate());
                        miIndustryEstimate.setUpdateBy(SecurityUtils.getUserId());
                        miIndustryEstimate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        miIndustryEstimateList.add(miIndustryEstimate);
                    }
                }
                if (i == 0) {
                    //前台传入市场洞察行业吸引力集合
                    List<MiIndustryAttractionDTO> miIndustryAttractionDTOS = miIndustryDetailDTOS.get(i).getMiIndustryAttractionDTOS();
                    if (StringUtils.isNotEmpty(miIndustryAttractionDTOS)) {
                        for (int i1 = 0; i1 < miIndustryAttractionDTOS.size(); i1++) {
                            MiIndustryAttraction miIndustryAttraction = new MiIndustryAttraction();
                            BeanUtils.copyProperties(miIndustryAttractionDTOS.get(i), miIndustryAttraction);
                            miIndustryAttraction.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                            miIndustryAttraction.setCreateBy(SecurityUtils.getUserId());
                            miIndustryAttraction.setCreateTime(DateUtils.getNowDate());
                            miIndustryAttraction.setUpdateTime(DateUtils.getNowDate());
                            miIndustryAttraction.setUpdateBy(SecurityUtils.getUserId());
                            miIndustryAttraction.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            miIndustryAttraction.setSort(i + 1);
                            miIndustryAttractionList.add(miIndustryAttraction);
                        }
                    }
                }
                if (StringUtils.isNotEmpty(miIndustryEstimateList)) {
                    try {
                        miIndustryEstimateMapper.batchMiIndustryEstimate(miIndustryEstimateList);
                    } catch (Exception e) {
                        throw new ServiceException("批量新增市场洞察行业预估失败");
                    }
                }
            }

            if (StringUtils.isNotEmpty(miIndustryAttractionList)) {
                try {
                    miIndustryAttractionMapper.batchMiIndustryAttraction(miIndustryAttractionList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察行业吸引力失败");
                }
                for (int i = 0; i < miIndustryDetailList.size(); i++) {
                    //新增市场洞察行业吸引力数据集合
                    List<MiIndustryAttractionData> miIndustryAttractionDataList = new ArrayList<>();
                    //前台传入市场洞察行业吸引力数据集合
                    List<MiIndustryAttractionDataDTO> miIndustryAttractionDataDTOS = miIndustryDetailDTOS.get(i).getMiIndustryAttractionDataDTOS();
                    if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOS)) {
                        for (int i1 = 0; i1 < miIndustryAttractionDataDTOS.size(); i1++) {
                            MiIndustryAttractionData miIndustryAttractionData = new MiIndustryAttractionData();
                            BeanUtils.copyProperties(miIndustryAttractionDataDTOS.get(i), miIndustryAttractionData);
                            //市场洞察行业ID
                            miIndustryAttractionData.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                            //市场洞察行业吸引力ID
                            miIndustryAttractionData.setMiIndustryAttractionId(miIndustryAttractionList.get(i).getMiIndustryAttractionId());
                            //市场洞察行业详情ID
                            miIndustryAttractionData.setMiIndustryDetailId(miIndustryDetailDTOS.get(i).getMiIndustryDetailId());
                            miIndustryAttractionData.setCreateBy(SecurityUtils.getUserId());
                            miIndustryAttractionData.setCreateTime(DateUtils.getNowDate());
                            miIndustryAttractionData.setUpdateTime(DateUtils.getNowDate());
                            miIndustryAttractionData.setUpdateBy(SecurityUtils.getUserId());
                            miIndustryAttractionData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            miIndustryAttractionDataList.add(miIndustryAttractionData);
                        }
                    }
                    if (StringUtils.isNotEmpty(miIndustryAttractionDataList)) {
                        try {
                            miIndustryAttractionDataMapper.batchMiIndustryAttractionData(miIndustryAttractionDataList);
                        } catch (Exception e) {
                            throw new ServiceException("批量新增市场洞察行业吸引力数据失败");
                        }
                    }
                }

            }
        }
    }

    /**
     * 修改市场洞察行业表
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateMarketInsightIndustry(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        int i = 0;
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
        marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
        marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = marketInsightIndustryMapper.updateMarketInsightIndustry(marketInsightIndustry);
        } catch (Exception e) {
            throw new ServiceException("修改市场洞察行业失败");
        }
        //前台传入市场洞察行业详情集合
        List<MiIndustryDetailDTO> miIndustryDetailDTOS = marketInsightIndustryDTO.getMiIndustryDetailDTOS();
        //数据集存在市场洞察行业详情集合数据
        List<MiIndustryDetailDTO> miIndustryDetailDTOList = miIndustryDetailMapper.selectMiIndustryDetailByMarketInsightIndustryId(marketInsightIndustryDTO.getMarketInsightIndustryId());
        //根据市场洞察行业主表主键查询市场洞察行业吸引力表
        List<MiIndustryAttractionDTO> miIndustryAttractionDTOList = miIndustryAttractionMapper.selectMiIndustryAttractionByMarketInsightIndustryId(marketInsightIndustryDTO.getMarketInsightIndustryId());
        if (StringUtils.isNotEmpty(miIndustryDetailDTOList)) {
            if (StringUtils.isNotEmpty(miIndustryDetailDTOS)) {
                //新增市场洞察行业详情集合
                List<MiIndustryDetail> miIndustryDetailAddList = new ArrayList<>();
                //修改市场洞察行业详情集合
                List<MiIndustryDetail> miIndustryDetailUpdateList = new ArrayList<>();
                //新增修改市场洞察行业详情集合
                this.packAddAndUpdatemiIndustryDetail(i, marketInsightIndustry, miIndustryDetailDTOS, miIndustryDetailDTOList, miIndustryDetailAddList, miIndustryDetailUpdateList);
                //新增修改市场洞察行业预估集合
                this.packAddAndUpdatemiIndustryEstimateDTO(marketInsightIndustry, miIndustryDetailDTOS, miIndustryDetailAddList);
                //新增修改市场洞察行业吸引力数据集合
                this.packAddAndUpdatemiIndustryAttractionData(marketInsightIndustry, miIndustryDetailDTOS, miIndustryAttractionDTOList, miIndustryDetailAddList);
            } else {
                //修改数据时传入空数据，删除已存在的数据
                this.updateLogindeleteData(marketInsightIndustry, miIndustryDetailDTOList);
            }
        } else {
            if (StringUtils.isNotEmpty(miIndustryDetailDTOS)) {
                //新增市场洞察行业详情集合
                List<MiIndustryDetail> miIndustryDetailList = new ArrayList<>();

                //新增市场洞察行业吸引力集合
                List<MiIndustryAttraction> miIndustryAttractionList = new ArrayList<>();
                //封装新增数据
                this.addData(miIndustryDetailDTOS, miIndustryDetailList, miIndustryAttractionList, marketInsightIndustry);
            }
        }
        return i;
    }

    /**
     * 新增修改市场洞察行业吸引力数据集合
     *
     * @param marketInsightIndustry
     * @param miIndustryDetailDTOS
     * @param miIndustryAttractionDTOList
     * @param miIndustryDetailAddList
     */
    private void packAddAndUpdatemiIndustryAttractionData(MarketInsightIndustry marketInsightIndustry, List<MiIndustryDetailDTO> miIndustryDetailDTOS, List<MiIndustryAttractionDTO> miIndustryAttractionDTOList, List<MiIndustryDetail> miIndustryDetailAddList) {
        for (MiIndustryDetailDTO miIndustryDetailDTO : miIndustryDetailDTOS) {
            //新增市场洞察行业吸引力数据集合
            List<MiIndustryAttractionData> miIndustryAttractionDataAddList = new ArrayList<>();
            //修改市场洞察行业吸引力数据集合
            List<MiIndustryAttractionData> miIndustryAttractionDataUpdateList = new ArrayList<>();
            Long miIndustryDetailId = miIndustryDetailDTO.getMiIndustryDetailId();
            int num = 0;
            List<Long> miIndustryAttractionDataIds = new ArrayList<>();
            //前台传入数据市场洞察行业吸引力数据集合
            List<MiIndustryAttractionDataDTO> miIndustryAttractionDataDTOS = miIndustryDetailDTO.getMiIndustryAttractionDataDTOS();
            if (null != miIndustryDetailId) {
                List<MiIndustryAttractionDataDTO> miIndustryAttractionDataDTOList = new ArrayList<>();
                miIndustryAttractionDataDTOList = miIndustryAttractionDataMapper.selectMiIndustryAttractionDataByNiIndustryDetailId(miIndustryDetailId);
                if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOS)) {
                    if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOList)) {
                        //sterm流求差集
                        miIndustryAttractionDataIds = miIndustryAttractionDataDTOList.stream().filter(a ->
                                !miIndustryAttractionDataDTOS.stream().map(MiIndustryAttractionDataDTO::getMiIndustryAttractionDataId).collect(Collectors.toList()).contains(a.getMiIndustryAttractionDataId())
                        ).collect(Collectors.toList()).stream().map(MiIndustryAttractionDataDTO::getMiIndustryAttractionDataId).collect(Collectors.toList());
                    }
                    if (StringUtils.isNotEmpty(miIndustryAttractionDataIds)) {
                        try {
                            miIndustryAttractionDataMapper.logicDeleteMiIndustryAttractionDataByMiIndustryAttractionDataIds(miIndustryAttractionDataIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察行业吸引力数据失败");
                        }
                    }
                    for (int i1 = 0; i1 < miIndustryAttractionDataDTOS.size(); i1++) {
                        Long miIndustryAttractionDataId = miIndustryAttractionDataDTOS.get(i1).getMiIndustryAttractionDataId();
                        if (null != miIndustryAttractionDataId) {
                            MiIndustryAttractionData miIndustryAttractionData = new MiIndustryAttractionData();
                            BeanUtils.copyProperties(miIndustryAttractionDataDTOS.get(i1), miIndustryAttractionData);
                            miIndustryAttractionData.setUpdateTime(DateUtils.getNowDate());
                            miIndustryAttractionData.setUpdateBy(SecurityUtils.getUserId());
                            miIndustryAttractionDataUpdateList.add(miIndustryAttractionData);
                        } else {
                            MiIndustryAttractionData miIndustryAttractionData = new MiIndustryAttractionData();
                            BeanUtils.copyProperties(miIndustryAttractionDataDTOS.get(i1), miIndustryAttractionData);
                            //市场洞察行业ID
                            miIndustryAttractionData.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                            //市场洞察行业吸引力ID
                            miIndustryAttractionData.setMiIndustryAttractionId(miIndustryAttractionDTOList.get(i1).getMiIndustryAttractionId());
                            //市场洞察行业详情ID
                            miIndustryAttractionData.setMiIndustryDetailId(miIndustryDetailId);
                            miIndustryAttractionData.setCreateBy(SecurityUtils.getUserId());
                            miIndustryAttractionData.setCreateTime(DateUtils.getNowDate());
                            miIndustryAttractionData.setUpdateTime(DateUtils.getNowDate());
                            miIndustryAttractionData.setUpdateBy(SecurityUtils.getUserId());
                            miIndustryAttractionData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            miIndustryAttractionDataAddList.add(miIndustryAttractionData);
                        }
                    }
                }
            } else {
                if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOS)) {
                    for (int i1 = 0; i1 < miIndustryAttractionDataDTOS.size(); i1++) {
                        MiIndustryAttractionData miIndustryAttractionData = new MiIndustryAttractionData();
                        BeanUtils.copyProperties(miIndustryAttractionDataDTOS.get(i1), miIndustryAttractionData);
                        //市场洞察行业ID
                        miIndustryAttractionData.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                        //市场洞察行业吸引力ID
                        miIndustryAttractionData.setMiIndustryAttractionId(miIndustryAttractionDTOList.get(i1).getMiIndustryAttractionId());
                        try {
                            //市场洞察行业详情ID
                            miIndustryAttractionData.setMiIndustryDetailId(miIndustryDetailAddList.get(num).getMiIndustryDetailId());
                        } catch (Exception e) {
                            throw new ServiceException("数组越界异常" + miIndustryDetailAddList.size() + ":" + num);
                        }
                        miIndustryAttractionData.setCreateBy(SecurityUtils.getUserId());
                        miIndustryAttractionData.setCreateTime(DateUtils.getNowDate());
                        miIndustryAttractionData.setUpdateTime(DateUtils.getNowDate());
                        miIndustryAttractionData.setUpdateBy(SecurityUtils.getUserId());
                        miIndustryAttractionData.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        miIndustryAttractionDataAddList.add(miIndustryAttractionData);
                    }
                }
                num++;
            }
            if (StringUtils.isNotEmpty(miIndustryAttractionDataAddList)) {
                try {
                    miIndustryAttractionDataMapper.batchMiIndustryAttractionData(miIndustryAttractionDataAddList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察行业吸引力数据失败");
                }
            }
            if (StringUtils.isNotEmpty(miIndustryAttractionDataUpdateList)) {
                try {
                    miIndustryAttractionDataMapper.updateMiIndustryAttractionDatas(miIndustryAttractionDataUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("批量修改市场洞察行业吸引力数据失败");
                }
            }
        }
    }

    /**
     * 新增修改市场洞察行业预估集合
     *
     * @param marketInsightIndustry
     * @param miIndustryDetailDTOS
     * @param miIndustryDetailAddList
     */
    private void packAddAndUpdatemiIndustryEstimateDTO(MarketInsightIndustry marketInsightIndustry, List<MiIndustryDetailDTO> miIndustryDetailDTOS, List<MiIndustryDetail> miIndustryDetailAddList) {
        for (MiIndustryDetailDTO miIndustryDetailDTO : miIndustryDetailDTOS) {
            //新增
            List<MiIndustryEstimate> miIndustryEstimateAddList = new ArrayList<>();
            //修改
            List<MiIndustryEstimate> miIndustryEstimateUpdateList = new ArrayList<>();
            int num = 0;
            List<Long> miMacroEstimateIds = new ArrayList<>();
            Long miIndustryDetailId = miIndustryDetailDTO.getMiIndustryDetailId();
            //前台传入市场洞察行业预估集合
            List<MiIndustryEstimateDTO> miIndustryEstimateDTOS = miIndustryDetailDTO.getMiIndustryEstimateDTOS();
            if (null != miIndustryDetailId) {
                if (StringUtils.isNotEmpty(miIndustryEstimateDTOS)) {
                    List<MiIndustryEstimateDTO> miIndustryEstimateDTOList = new ArrayList<>();
                    miIndustryEstimateDTOList = miIndustryEstimateMapper.selectMiIndustryEstimateByMiIndustryDetailId(miIndustryDetailId);
                    if (StringUtils.isNotEmpty(miIndustryEstimateDTOList)) {
                        //sterm流求差集
                        miMacroEstimateIds = miIndustryEstimateDTOList.stream().filter(a ->
                                !miIndustryEstimateDTOS.stream().map(MiIndustryEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList()).contains(a.getMiMacroEstimateId())
                        ).collect(Collectors.toList()).stream().map(MiIndustryEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                            try {
                                miIndustryEstimateMapper.logicDeleteMiIndustryEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                            } catch (Exception e) {
                                throw new ServiceException("逻辑批量删除市场洞察行业预估失败");
                            }
                        }
                    }
                    for (MiIndustryEstimateDTO miIndustryEstimateDTO : miIndustryEstimateDTOS) {
                        Long miMacroEstimateId = miIndustryEstimateDTO.getMiMacroEstimateId();
                        if (null != miMacroEstimateId) {
                            MiIndustryEstimate miIndustryEstimate = new MiIndustryEstimate();
                            BeanUtils.copyProperties(miIndustryEstimateDTO, miIndustryEstimate);
                            miIndustryEstimate.setUpdateBy(SecurityUtils.getUserId());
                            miIndustryEstimate.setUpdateTime(DateUtils.getNowDate());
                            miIndustryEstimateUpdateList.add(miIndustryEstimate);
                        } else {
                            MiIndustryEstimate miIndustryEstimate = new MiIndustryEstimate();
                            BeanUtils.copyProperties(miIndustryEstimateDTO, miIndustryEstimate);
                            miIndustryEstimate.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                            miIndustryEstimate.setMiIndustryDetailId(miIndustryDetailId);
                            miIndustryEstimate.setCreateBy(SecurityUtils.getUserId());
                            miIndustryEstimate.setCreateTime(DateUtils.getNowDate());
                            miIndustryEstimate.setUpdateTime(DateUtils.getNowDate());
                            miIndustryEstimate.setUpdateBy(SecurityUtils.getUserId());
                            miIndustryEstimate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            miIndustryEstimateAddList.add(miIndustryEstimate);
                        }
                    }
                }
            } else {
                if (StringUtils.isNotEmpty(miIndustryEstimateDTOS)) {
                    for (MiIndustryEstimateDTO miIndustryEstimateDTO : miIndustryEstimateDTOS) {
                        MiIndustryEstimate miIndustryEstimate = new MiIndustryEstimate();
                        BeanUtils.copyProperties(miIndustryEstimateDTO, miIndustryEstimate);
                        miIndustryEstimate.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                        try {
                            miIndustryEstimate.setMiIndustryDetailId(miIndustryDetailAddList.get(num).getMiIndustryDetailId());
                        } catch (Exception e) {
                            throw new ServiceException("数组越界异常" + miIndustryDetailAddList.size() + ":" + num);
                        }
                        miIndustryEstimate.setCreateBy(SecurityUtils.getUserId());
                        miIndustryEstimate.setCreateTime(DateUtils.getNowDate());
                        miIndustryEstimate.setUpdateTime(DateUtils.getNowDate());
                        miIndustryEstimate.setUpdateBy(SecurityUtils.getUserId());
                        miIndustryEstimate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        miIndustryEstimateAddList.add(miIndustryEstimate);
                    }
                }
                num++;
            }
            if (StringUtils.isNotEmpty(miIndustryEstimateAddList)) {
                try {
                    miIndustryEstimateMapper.batchMiIndustryEstimate(miIndustryEstimateAddList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察行业预估失败");
                }
            }

            if (StringUtils.isNotEmpty(miIndustryEstimateUpdateList)) {
                try {
                    miIndustryEstimateMapper.updateMiIndustryEstimates(miIndustryEstimateUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("批量修改市场洞察行业预估失败");
                }
            }


        }
    }

    /**
     * 新增修改市场洞察行业详情集合
     *
     * @param i
     * @param marketInsightIndustry
     * @param miIndustryDetailDTOS
     * @param miIndustryDetailDTOList
     * @param miIndustryDetailAddList
     * @param miIndustryDetailUpdateList
     */
    private void packAddAndUpdatemiIndustryDetail(int i, MarketInsightIndustry marketInsightIndustry, List<MiIndustryDetailDTO> miIndustryDetailDTOS, List<MiIndustryDetailDTO> miIndustryDetailDTOList, List<MiIndustryDetail> miIndustryDetailAddList, List<MiIndustryDetail> miIndustryDetailUpdateList) {
        List<Long> miIndustryDetailIds = new ArrayList<>();
        //sterm流求差集
        miIndustryDetailIds = miIndustryDetailDTOList.stream().filter(a ->
                !miIndustryDetailDTOS.stream().map(MiIndustryDetailDTO::getMiIndustryDetailId).collect(Collectors.toList()).contains(a.getMiIndustryDetailId())
        ).collect(Collectors.toList()).stream().map(MiIndustryDetailDTO::getMiIndustryDetailId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(miIndustryDetailIds)) {
            miIndustryDetailMapper.logicDeleteMiIndustryDetailByMiIndustryDetailIds(miIndustryDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        }
        for (int i1 = 0; i1 < miIndustryDetailDTOS.size(); i1++) {
            Long miIndustryDetailId = miIndustryDetailDTOS.get(i1).getMiIndustryDetailId();
            if (null == miIndustryDetailId) {
                MiIndustryDetail miIndustryDetail = new MiIndustryDetail();
                BeanUtils.copyProperties(miIndustryDetailDTOS.get(i1), miIndustryDetail);
                miIndustryDetail.setMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
                miIndustryDetail.setCreateBy(SecurityUtils.getUserId());
                miIndustryDetail.setCreateTime(DateUtils.getNowDate());
                miIndustryDetail.setUpdateTime(DateUtils.getNowDate());
                miIndustryDetail.setUpdateBy(SecurityUtils.getUserId());
                miIndustryDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miIndustryDetail.setSort(i1 + 1);
                miIndustryDetailAddList.add(miIndustryDetail);
            } else {
                MiIndustryDetail miIndustryDetail = new MiIndustryDetail();
                BeanUtils.copyProperties(miIndustryDetailDTOS.get(i), miIndustryDetail);
                miIndustryDetail.setUpdateTime(DateUtils.getNowDate());
                miIndustryDetail.setUpdateBy(SecurityUtils.getUserId());
                miIndustryDetail.setSort(i1 + 1);
                miIndustryDetailUpdateList.add(miIndustryDetail);
            }

        }
        if (StringUtils.isNotEmpty(miIndustryDetailAddList)) {
            try {
                miIndustryDetailMapper.batchMiIndustryDetail(miIndustryDetailAddList);
            } catch (Exception e) {
                throw new ServiceException("批量新增市场洞察行业详情失败");
            }
        }
        if (StringUtils.isNotEmpty(miIndustryDetailUpdateList)) {
            try {
                miIndustryDetailMapper.updateMiIndustryDetails(miIndustryDetailUpdateList);
            } catch (Exception e) {
                throw new ServiceException("批量修改市场洞察行业详情失败");
            }
        }
    }

    /**
     * 修改数据时传入空数据，删除已存在的数据
     *
     * @param marketInsightIndustry
     * @param miIndustryDetailDTOList
     */
    private void updateLogindeleteData(MarketInsightIndustry marketInsightIndustry, List<MiIndustryDetailDTO> miIndustryDetailDTOList) {
        List<MiIndustryAttractionDTO> miIndustryAttractionDTOS = miIndustryAttractionMapper.selectMiIndustryAttractionByMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
        if (StringUtils.isNotEmpty(miIndustryAttractionDTOS)) {
            List<Long> miIndustryAttractionIds = miIndustryAttractionDTOS.stream().map(MiIndustryAttractionDTO::getMiIndustryAttractionId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miIndustryAttractionIds)) {
                try {
                    miIndustryAttractionMapper.logicDeleteMiIndustryAttractionByMiIndustryAttractionIds(miIndustryAttractionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察行业吸引力失败");
                }
            }
        }
        List<Long> miIndustryDetailIds = miIndustryDetailDTOList.stream().map(MiIndustryDetailDTO::getMiIndustryDetailId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(miIndustryDetailIds)) {
            try {
                miIndustryDetailMapper.logicDeleteMiIndustryDetailByMiIndustryDetailIds(miIndustryDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("逻辑批量删除市场洞察行业详情失败");
            }
            List<MiIndustryEstimateDTO> miIndustryEstimateDTOS = miIndustryEstimateMapper.selectMiIndustryEstimateByMiIndustryDetailIds(miIndustryDetailIds);
            if (StringUtils.isNotEmpty(miIndustryEstimateDTOS)) {
                List<Long> miMacroEstimateIds = miIndustryEstimateDTOS.stream().map(MiIndustryEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                    try {
                        miIndustryEstimateMapper.logicDeleteMiIndustryEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察行业预估失败");
                    }
                }
            }
            List<MiIndustryAttractionDataDTO> miIndustryAttractionDataDTOS = miIndustryAttractionDataMapper.selectMiIndustryAttractionDataByMiIndustryDetailIds(miIndustryDetailIds);
            if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOS)) {
                List<Long> miIndustryAttractionDataIds = miIndustryAttractionDataDTOS.stream().map(MiIndustryAttractionDataDTO::getMiIndustryAttractionDataId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miIndustryAttractionDataIds)) {
                    try {
                        miIndustryAttractionDataMapper.logicDeleteMiIndustryAttractionDataByMiIndustryAttractionDataIds(miIndustryAttractionDataIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察行业吸引力数据失败");
                    }
                }
            }
        }
    }

    /**
     * 逻辑批量删除市场洞察行业表
     *
     * @param marketInsightIndustryIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightIndustryByMarketInsightIndustryIds(List<Long> marketInsightIndustryIds) {
        int i = 0;
        try {
            i = marketInsightIndustryMapper.logicDeleteMarketInsightIndustryByMarketInsightIndustryIds(marketInsightIndustryIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("逻辑批量删除市场洞察行业失败");
        }
        List<MiIndustryDetailDTO> miIndustryDetailDTOS = miIndustryDetailMapper.selectMiIndustryDetailByMarketInsightIndustryIds(marketInsightIndustryIds);
        List<MiIndustryAttractionDTO> miIndustryAttractionDTOS = miIndustryAttractionMapper.selectMiIndustryAttractionByMarketInsightIndustryIds(marketInsightIndustryIds);
        if (StringUtils.isNotEmpty(miIndustryAttractionDTOS)) {
            List<Long> miIndustryAttractionIds = miIndustryAttractionDTOS.stream().map(MiIndustryAttractionDTO::getMiIndustryAttractionId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miIndustryAttractionIds)) {
                try {
                    miIndustryAttractionMapper.logicDeleteMiIndustryAttractionByMiIndustryAttractionIds(miIndustryAttractionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察行业吸引力失败");
                }
            }
        }
        if (StringUtils.isNotEmpty(miIndustryDetailDTOS)) {
            List<Long> miIndustryDetailIds = miIndustryDetailDTOS.stream().map(MiIndustryDetailDTO::getMiIndustryDetailId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miIndustryDetailIds)) {
                try {
                    miIndustryDetailMapper.logicDeleteMiIndustryDetailByMiIndustryDetailIds(miIndustryDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察行业详情失败");
                }
                List<MiIndustryEstimateDTO> miIndustryEstimateDTOS = miIndustryEstimateMapper.selectMiIndustryEstimateByMiIndustryDetailIds(miIndustryDetailIds);
                if (StringUtils.isNotEmpty(miIndustryEstimateDTOS)) {
                    List<Long> miMacroEstimateIds = miIndustryEstimateDTOS.stream().map(MiIndustryEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                        try {
                            miIndustryEstimateMapper.logicDeleteMiIndustryEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察行业预估失败");
                        }
                    }
                }
                List<MiIndustryAttractionDataDTO> miIndustryAttractionDataDTOS = miIndustryAttractionDataMapper.selectMiIndustryAttractionDataByMiIndustryDetailIds(miIndustryDetailIds);
                if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOS)) {
                    List<Long> miIndustryAttractionDataIds = miIndustryAttractionDataDTOS.stream().map(MiIndustryAttractionDataDTO::getMiIndustryAttractionDataId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miIndustryAttractionDataIds)) {
                        try {
                            miIndustryAttractionDataMapper.logicDeleteMiIndustryAttractionDataByMiIndustryAttractionDataIds(miIndustryAttractionDataIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察行业吸引力数据失败");
                        }
                    }
                }
            }
        }

        return i;
    }

    /**
     * 物理删除市场洞察行业表信息
     *
     * @param marketInsightIndustryId 市场洞察行业表主键
     * @return 结果
     */
    @Override
    public int deleteMarketInsightIndustryByMarketInsightIndustryId(Long marketInsightIndustryId) {
        return marketInsightIndustryMapper.deleteMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustryId);
    }

    /**
     * 逻辑删除市场洞察行业表信息
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightIndustryByMarketInsightIndustryId(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        int i = 0;
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        marketInsightIndustry.setMarketInsightIndustryId(marketInsightIndustryDTO.getMarketInsightIndustryId());
        marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
        marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = marketInsightIndustryMapper.logicDeleteMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustry);
        } catch (Exception e) {
            throw new ServiceException("逻辑删除市场洞察行业失败");
        }
        List<MiIndustryDetailDTO> miIndustryDetailDTOS = miIndustryDetailMapper.selectMiIndustryDetailByMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
        List<MiIndustryAttractionDTO> miIndustryAttractionDTOS = miIndustryAttractionMapper.selectMiIndustryAttractionByMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
        if (StringUtils.isNotEmpty(miIndustryAttractionDTOS)) {
            List<Long> miIndustryAttractionIds = miIndustryAttractionDTOS.stream().map(MiIndustryAttractionDTO::getMiIndustryAttractionId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miIndustryAttractionIds)) {
                try {
                    miIndustryAttractionMapper.logicDeleteMiIndustryAttractionByMiIndustryAttractionIds(miIndustryAttractionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察行业吸引力失败");
                }
            }
        }
        if (StringUtils.isNotEmpty(miIndustryDetailDTOS)) {
            List<Long> miIndustryDetailIds = miIndustryDetailDTOS.stream().map(MiIndustryDetailDTO::getMiIndustryDetailId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miIndustryDetailIds)) {
                try {
                    miIndustryDetailMapper.logicDeleteMiIndustryDetailByMiIndustryDetailIds(miIndustryDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察行业详情失败");
                }
                List<MiIndustryEstimateDTO> miIndustryEstimateDTOS = miIndustryEstimateMapper.selectMiIndustryEstimateByMiIndustryDetailIds(miIndustryDetailIds);
                if (StringUtils.isNotEmpty(miIndustryEstimateDTOS)) {
                    List<Long> miMacroEstimateIds = miIndustryEstimateDTOS.stream().map(MiIndustryEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                        try {
                            miIndustryEstimateMapper.logicDeleteMiIndustryEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察行业预估失败");
                        }
                    }
                }
                List<MiIndustryAttractionDataDTO> miIndustryAttractionDataDTOS = miIndustryAttractionDataMapper.selectMiIndustryAttractionDataByMiIndustryDetailIds(miIndustryDetailIds);
                if (StringUtils.isNotEmpty(miIndustryAttractionDataDTOS)) {
                    List<Long> miIndustryAttractionDataIds = miIndustryAttractionDataDTOS.stream().map(MiIndustryAttractionDataDTO::getMiIndustryAttractionDataId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miIndustryAttractionDataIds)) {
                        try {
                            miIndustryAttractionDataMapper.logicDeleteMiIndustryAttractionDataByMiIndustryAttractionDataIds(miIndustryAttractionDataIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察行业吸引力数据失败");
                        }
                    }
                }
            }
        }
        return i;
    }

    /**
     * 物理删除市场洞察行业表信息
     *
     * @param marketInsightIndustryDTO 市场洞察行业表
     * @return 结果
     */

    @Override
    public int deleteMarketInsightIndustryByMarketInsightIndustryId(MarketInsightIndustryDTO marketInsightIndustryDTO) {
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
        return marketInsightIndustryMapper.deleteMarketInsightIndustryByMarketInsightIndustryId(marketInsightIndustry.getMarketInsightIndustryId());
    }

    /**
     * 物理批量删除市场洞察行业表
     *
     * @param marketInsightIndustryDtos 需要删除的市场洞察行业表主键
     * @return 结果
     */

    @Override
    public int deleteMarketInsightIndustryByMarketInsightIndustryIds(List<MarketInsightIndustryDTO> marketInsightIndustryDtos) {
        List<Long> stringList = new ArrayList();
        for (MarketInsightIndustryDTO marketInsightIndustryDTO : marketInsightIndustryDtos) {
            stringList.add(marketInsightIndustryDTO.getMarketInsightIndustryId());
        }
        return marketInsightIndustryMapper.deleteMarketInsightIndustryByMarketInsightIndustryIds(stringList);
    }

    /**
     * 批量新增市场洞察行业表信息
     *
     * @param marketInsightIndustryDtos 市场洞察行业表对象
     */

    public int insertMarketInsightIndustrys(List<MarketInsightIndustryDTO> marketInsightIndustryDtos) {
        List<MarketInsightIndustry> marketInsightIndustryList = new ArrayList();

        for (MarketInsightIndustryDTO marketInsightIndustryDTO : marketInsightIndustryDtos) {
            MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
            BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
            marketInsightIndustry.setCreateBy(SecurityUtils.getUserId());
            marketInsightIndustry.setCreateTime(DateUtils.getNowDate());
            marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
            marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
            marketInsightIndustry.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            marketInsightIndustryList.add(marketInsightIndustry);
        }
        return marketInsightIndustryMapper.batchMarketInsightIndustry(marketInsightIndustryList);
    }

    /**
     * 批量修改市场洞察行业表信息
     *
     * @param marketInsightIndustryDtos 市场洞察行业表对象
     */

    public int updateMarketInsightIndustrys(List<MarketInsightIndustryDTO> marketInsightIndustryDtos) {
        List<MarketInsightIndustry> marketInsightIndustryList = new ArrayList();

        for (MarketInsightIndustryDTO marketInsightIndustryDTO : marketInsightIndustryDtos) {
            MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
            BeanUtils.copyProperties(marketInsightIndustryDTO, marketInsightIndustry);
            marketInsightIndustry.setCreateBy(SecurityUtils.getUserId());
            marketInsightIndustry.setCreateTime(DateUtils.getNowDate());
            marketInsightIndustry.setUpdateTime(DateUtils.getNowDate());
            marketInsightIndustry.setUpdateBy(SecurityUtils.getUserId());
            marketInsightIndustryList.add(marketInsightIndustry);
        }
        return marketInsightIndustryMapper.updateMarketInsightIndustrys(marketInsightIndustryList);
    }
}

