package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

import java.math.BigDecimal;
import java.util.*;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiMacroDetail;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiMacroEstimate;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiMacroDetailDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiMacroEstimateDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiMacroDetailMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiMacroEstimateMapper;
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
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightMacro;
import net.qixiaowei.strategy.cloud.excel.marketInsight.MarketInsightMacroExcel;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightMacroMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightMacroService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;


/**
 * MarketInsightMacroService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-02-28
 */
@Service
public class MarketInsightMacroServiceImpl implements IMarketInsightMacroService {
    @Autowired
    private MarketInsightMacroMapper marketInsightMacroMapper;
    @Autowired
    private MiMacroDetailMapper miMacroDetailMapper;
    @Autowired
    private MiMacroEstimateMapper miMacroEstimateMapper;
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

    /**
     * 查询市场洞察宏观表
     *
     * @param marketInsightMacroId 市场洞察宏观表主键
     * @return 市场洞察宏观表
     */
    @Override
    public MarketInsightMacroDTO selectMarketInsightMacroByMarketInsightMacroId(Long marketInsightMacroId) {
        MarketInsightMacroDTO marketInsightMacroDTO = marketInsightMacroMapper.selectMarketInsightMacroByMarketInsightMacroId(marketInsightMacroId);
        List<Map<String, Object>> dropList = PlanBusinessUnitCode.getDropList(marketInsightMacroDTO.getBusinessUnitDecompose());
        marketInsightMacroDTO.setBusinessUnitDecomposes(dropList);
        Long productId = marketInsightMacroDTO.getProductId();
        Long areaId = marketInsightMacroDTO.getAreaId();
        Long departmentId = marketInsightMacroDTO.getDepartmentId();
        Long industryId = marketInsightMacroDTO.getIndustryId();
        if (null != productId) {
            R<ProductDTO> productDTOR = remoteProductService.remoteSelectById(productId, SecurityConstants.INNER);
            ProductDTO data = productDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightMacroDTO.setProductName(data.getProductName());
            }
        }
        if (null != areaId) {
            R<AreaDTO> areaDTOR = remoteAreaService.getById(areaId, SecurityConstants.INNER);
            AreaDTO data = areaDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightMacroDTO.setAreaName(data.getAreaName());
            }
        }
        if (null != departmentId) {
            R<DepartmentDTO> departmentDTOR = remoteDepartmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
            DepartmentDTO data = departmentDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightMacroDTO.setDepartmentName(data.getDepartmentName());
            }
        }
        if (null != industryId) {
            R<IndustryDTO> industryDTOR = remoteIndustryService.selectById(industryId, SecurityConstants.INNER);
            IndustryDTO data = industryDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                marketInsightMacroDTO.setIndustryName(data.getIndustryName());
            }
        }

        List<MiMacroDetailDTO> miMacroDetailDTOS = miMacroDetailMapper.selectMiMacroDetailByMarketInsightMacroId(marketInsightMacroId);
        if (StringUtils.isNotEmpty(miMacroDetailDTOS)) {

            List<Long> visualAngles = miMacroDetailDTOS.stream().filter(f -> null != f.getVisualAngle()).map(MiMacroDetailDTO::getVisualAngle).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(visualAngles)) {
                R<List<DictionaryDataDTO>> dataByDictionaryDataList = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(visualAngles, SecurityConstants.INNER);
                List<DictionaryDataDTO> data = dataByDictionaryDataList.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (MiMacroDetailDTO miMacroDetailDTO : miMacroDetailDTOS) {
                        if (null != miMacroDetailDTO.getVisualAngle()){
                            for (DictionaryDataDTO datum : data) {
                                if (miMacroDetailDTO.getVisualAngle().equals(datum.getDictionaryDataId())) {
                                    miMacroDetailDTO.setVisualAngleName(datum.getDictionaryLabel());
                                }
                            }
                        }
                    }
                }
            }
            //市场洞察宏观详情表主键集合
            List<Long> miMacroDetailIds = miMacroDetailDTOS.stream().map(MiMacroDetailDTO::getMiMacroDetailId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miMacroDetailIds)) {
                List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroEstimateMapper.selectMiMacroEstimateByMiMacroDetailIds(miMacroDetailIds);
                //根据市场洞察宏观详情表主键分组
                Map<Long, List<MiMacroEstimateDTO>> miMacroEstimateMapData = miMacroEstimateDTOS.parallelStream().collect(Collectors.groupingBy(MiMacroEstimateDTO::getMiMacroDetailId));
                for (MiMacroDetailDTO miMacroDetailDTO : miMacroDetailDTOS) {
                    miMacroDetailDTO.setMiMacroEstimateDTOS(miMacroEstimateMapData.get(miMacroDetailDTO.getMiMacroDetailId()));
                }
            }
        }
        marketInsightMacroDTO.setMiMacroDetailDTOS(miMacroDetailDTOS);
        return marketInsightMacroDTO;
    }

    /**
     * 查询市场洞察宏观表列表
     *
     * @param marketInsightMacroDTO 市场洞察宏观表
     * @return 市场洞察宏观表
     */
    @Override
    @DataScope(businessAlias = "mim")
    public List<MarketInsightMacroDTO> selectMarketInsightMacroList(MarketInsightMacroDTO marketInsightMacroDTO) {
        List<String> createByList = new ArrayList<>();
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacro);
        //高级搜索请求参数
        Map<String, Object> params = marketInsightMacroDTO.getParams();
        this.queryemployeeName(params);
        marketInsightMacro.setParams(params);
        if (StringUtils.isNotNull(marketInsightMacroDTO.getCreateByName())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(marketInsightMacroDTO.getCreateByName());
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> userIds = userListData.stream().filter(f -> f.getUserId() != null).map(UserDTO::getUserId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(userIds)) {
                userIds.forEach(e -> {
                    createByList.add(String.valueOf(e));
                });
            } else {
                createByList.add("");
            }
        }
        marketInsightMacro.setCreateBys(createByList);
        List<MarketInsightMacroDTO> marketInsightMacroDTOS = marketInsightMacroMapper.selectMarketInsightMacroList(marketInsightMacro);

        if (StringUtils.isNotEmpty(marketInsightMacroDTOS)) {
            List<Long> productIds = marketInsightMacroDTOS.stream().filter(f -> null != f.getProductId()).map(MarketInsightMacroDTO::getProductId).collect(Collectors.toList());
            List<Long> areaIds = marketInsightMacroDTOS.stream().filter(f -> null != f.getAreaId()).map(MarketInsightMacroDTO::getAreaId).collect(Collectors.toList());
            List<Long> departmentIds = marketInsightMacroDTOS.stream().filter(f -> null != f.getDepartmentId()).map(MarketInsightMacroDTO::getDepartmentId).collect(Collectors.toList());
            List<Long> industryIds = marketInsightMacroDTOS.stream().filter(f -> null != f.getIndustryId()).map(MarketInsightMacroDTO::getIndustryId).collect(Collectors.toList());

            if (StringUtils.isNotEmpty(productIds)) {
                R<List<ProductDTO>> productList = remoteProductService.getName(productIds, SecurityConstants.INNER);
                List<ProductDTO> data = productList.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (MarketInsightMacroDTO insightMacroDTO : marketInsightMacroDTOS) {
                        Long productId = insightMacroDTO.getProductId();
                        for (ProductDTO datum : data) {
                            if (null != productId){
                                if (insightMacroDTO.getProductId().equals(datum.getProductId())) {
                                    insightMacroDTO.setProductName(datum.getProductName());
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
                    for (MarketInsightMacroDTO insightMacroDTO : marketInsightMacroDTOS) {
                        Long areaId = insightMacroDTO.getAreaId();
                        for (AreaDTO areaListDatum : areaListData) {
                            if (null != areaId){
                                if (insightMacroDTO.getAreaId().equals(areaListDatum.getAreaId())){
                                    insightMacroDTO.setAreaName(areaListDatum.getAreaName());
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
                    for (MarketInsightMacroDTO insightMacroDTO : marketInsightMacroDTOS) {
                        Long departmentId = insightMacroDTO.getDepartmentId();
                        for (DepartmentDTO datum : data) {
                            if (null != departmentId){
                                if (insightMacroDTO.getDepartmentId().equals(datum.getDepartmentId())){
                                    insightMacroDTO.setDepartmentName(datum.getDepartmentName());
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
                    for (MarketInsightMacroDTO insightMacroDTO : marketInsightMacroDTOS) {
                        Long industryId = insightMacroDTO.getIndustryId();
                        for (IndustryDTO datum : data) {
                            if (null != industryId){
                                if (insightMacroDTO.getIndustryId().equals(datum.getIndustryId())){
                                    insightMacroDTO.setIndustryName(datum.getIndustryName());
                                }
                            }
                        }
                    }
                }
            }
        }
        return marketInsightMacroDTOS;
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
                    List<Long> employeeIds = data.stream().filter(f -> f.getEmployeeId() != null).map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(employeeIds)) {
                        R<List<UserDTO>> usersByUserIds = remoteUserService.selectByemployeeIds(employeeIds, SecurityConstants.INNER);
                        List<UserDTO> data1 = usersByUserIds.getData();
                        if (StringUtils.isNotEmpty(data1)){
                            params.put("createBys", data1.stream().map(UserDTO::getUserId).collect(Collectors.toList()));
                        }
                    }
                }
            }
        }
    }

    /**
     * 新增市场洞察宏观表
     *
     * @param marketInsightMacroDTO 市场洞察宏观表
     * @return 结果
     */
    @Override
    @Transactional
    public MarketInsightMacroDTO insertMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO) {
        MarketInsightMacro marketInsightMacroValidated = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacroValidated);
        List<MarketInsightMacroDTO> marketInsightMacroDTOS = marketInsightMacroMapper.selectMarketInsightMacroList(marketInsightMacroValidated);
        if (StringUtils.isNotEmpty(marketInsightMacroDTOS)) {
            throw new ServiceException("已存在该年份和规划业务单元数据！请重新输入！");
        }
        //前端传入市场洞察宏观详情表集合
        List<MiMacroDetailDTO> miMacroDetailDTOS = marketInsightMacroDTO.getMiMacroDetailDTOS();
        List<MiMacroDetail> miMacroDetailList = new ArrayList<>();
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacro);
        marketInsightMacro.setCreateBy(SecurityUtils.getUserId());
        marketInsightMacro.setCreateTime(DateUtils.getNowDate());
        marketInsightMacro.setUpdateTime(DateUtils.getNowDate());
        marketInsightMacro.setUpdateBy(SecurityUtils.getUserId());
        marketInsightMacro.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            marketInsightMacroMapper.insertMarketInsightMacro(marketInsightMacro);
        } catch (Exception e) {
            throw new ServiceException("新增市场洞察宏观失败");
        }
        if (StringUtils.isNotEmpty(miMacroDetailDTOS)) {
            for (MiMacroDetailDTO miMacroDetailDTO : miMacroDetailDTOS) {

                MiMacroDetail miMacroDetail = new MiMacroDetail();
                BeanUtils.copyProperties(miMacroDetailDTO, miMacroDetail);
                miMacroDetail.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
                miMacroDetail.setCreateBy(SecurityUtils.getUserId());
                miMacroDetail.setCreateTime(DateUtils.getNowDate());
                miMacroDetail.setUpdateTime(DateUtils.getNowDate());
                miMacroDetail.setUpdateBy(SecurityUtils.getUserId());
                miMacroDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miMacroDetailList.add(miMacroDetail);
            }
            if (StringUtils.isNotEmpty(miMacroDetailList)) {
                try {
                    miMacroDetailMapper.batchMiMacroDetail(miMacroDetailList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察宏观详情失败");
                }
                for (int i = 0; i < miMacroDetailDTOS.size(); i++) {
                    List<MiMacroEstimate> miMacroEstimateList = new ArrayList<>();
                    //前端传入市场洞察宏观预估表集合
                    List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroDetailDTOS.get(i).getMiMacroEstimateDTOS();
                    if (StringUtils.isNotEmpty(miMacroEstimateDTOS)) {
                        for (MiMacroEstimateDTO miMacroEstimateDTO : miMacroEstimateDTOS) {
                            MiMacroEstimate miMacroEstimate = new MiMacroEstimate();
                            BeanUtils.copyProperties(miMacroEstimateDTO, miMacroEstimate);
                            miMacroEstimate.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
                            miMacroEstimate.setMiMacroDetailId(miMacroDetailList.get(i).getMiMacroDetailId());
                            miMacroEstimate.setCreateBy(SecurityUtils.getUserId());
                            miMacroEstimate.setCreateTime(DateUtils.getNowDate());
                            miMacroEstimate.setUpdateTime(DateUtils.getNowDate());
                            miMacroEstimate.setUpdateBy(SecurityUtils.getUserId());
                            miMacroEstimate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            miMacroEstimateList.add(miMacroEstimate);
                        }
                    }
                    if (StringUtils.isNotEmpty(miMacroEstimateList)) {
                        try {
                            miMacroEstimateMapper.batchMiMacroEstimate(miMacroEstimateList);
                        } catch (Exception e) {
                            throw new ServiceException("批量新增市场洞察宏观预估失败");
                        }
                    }
                }
            }
        }
        marketInsightMacroDTO.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
        return marketInsightMacroDTO;
    }

    /**
     * 修改市场洞察宏观表
     *
     * @param marketInsightMacroDTO 市场洞察宏观表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO) {
        int i = 0;
        //前台传入市场洞察宏观详情集合
        List<MiMacroDetailDTO> miMacroDetailDTOS = marketInsightMacroDTO.getMiMacroDetailDTOS();
        //新增市场洞察宏观详情集合
        List<MiMacroDetail> miMacroDetailAddList = new ArrayList<>();
        //修改市场洞察宏观详情集合
        List<MiMacroDetail> miMacroDetailUpdateList = new ArrayList<>();
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacro);
        marketInsightMacro.setUpdateTime(DateUtils.getNowDate());
        marketInsightMacro.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = marketInsightMacroMapper.updateMarketInsightMacro(marketInsightMacro);
        } catch (Exception e) {
            throw new ServiceException("修改市场洞察宏观失败");
        }
        List<MiMacroDetailDTO> miMacroDetailListData = miMacroDetailMapper.selectMiMacroDetailByMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
        if (StringUtils.isNotEmpty(miMacroDetailListData)) {
            List<Long> miMacroDetailIds = new ArrayList<>();
            if (StringUtils.isNotEmpty(miMacroDetailDTOS)) {
                //sterm流求差集
                miMacroDetailIds = miMacroDetailListData.stream().filter(a ->
                        !miMacroDetailDTOS.stream().map(MiMacroDetailDTO::getMiMacroDetailId).collect(Collectors.toList()).contains(a.getMiMacroDetailId())
                ).collect(Collectors.toList()).stream().map(MiMacroDetailDTO::getMiMacroDetailId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miMacroDetailIds)) {
                    try {
                        miMacroDetailMapper.logicDeleteMiMacroDetailByMiMacroDetailIds(miMacroDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察宏观详情失败");
                    }
                    //根据市场洞察宏观详情表主键集合批量查询市场洞察宏观预估表
                    List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroEstimateMapper.selectMiMacroEstimateByMiMacroDetailIds(miMacroDetailIds);
                    if (StringUtils.isNotEmpty(miMacroEstimateDTOS)) {
                        List<Long> miMacroEstimateIds = miMacroEstimateDTOS.stream().map(MiMacroEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                            miMacroEstimateMapper.logicDeleteMiMacroEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        }
                    }
                }
                //批量修改新增市场洞察宏观详情
                this.packMiMacroDetailAll(miMacroDetailDTOS, miMacroDetailAddList, miMacroDetailUpdateList, marketInsightMacro);
                //批量修改新增市场洞察宏观预估
                this.packMiMacroEstimateAll(miMacroDetailDTOS, miMacroDetailAddList,  marketInsightMacro);
            } else {
                miMacroDetailIds = miMacroDetailListData.stream().map(MiMacroDetailDTO::getMiMacroDetailId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miMacroDetailIds)) {
                    try {
                        miMacroDetailMapper.logicDeleteMiMacroDetailByMiMacroDetailIds(miMacroDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察宏观详情失败");
                    }
                    //根据市场洞察宏观详情表主键集合批量查询市场洞察宏观预估表
                    List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroEstimateMapper.selectMiMacroEstimateByMiMacroDetailIds(miMacroDetailIds);
                    if (StringUtils.isNotEmpty(miMacroEstimateDTOS)) {
                        List<Long> miMacroEstimateIds = miMacroEstimateDTOS.stream().map(MiMacroEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                            miMacroEstimateMapper.logicDeleteMiMacroEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        }
                    }
                }
            }


        } else {
            if (StringUtils.isNotEmpty(miMacroDetailDTOS)) {
                for (MiMacroDetailDTO miMacroDetailDTO : miMacroDetailDTOS) {
                    MiMacroDetail miMacroDetail = new MiMacroDetail();
                    BeanUtils.copyProperties(miMacroDetailDTO, miMacroDetail);
                    miMacroDetail.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
                    miMacroDetail.setCreateBy(SecurityUtils.getUserId());
                    miMacroDetail.setCreateTime(DateUtils.getNowDate());
                    miMacroDetail.setUpdateTime(DateUtils.getNowDate());
                    miMacroDetail.setUpdateBy(SecurityUtils.getUserId());
                    miMacroDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    miMacroDetailAddList.add(miMacroDetail);
                }
                if (StringUtils.isNotEmpty(miMacroDetailAddList)) {
                    try {
                        miMacroDetailMapper.batchMiMacroDetail(miMacroDetailAddList);
                    } catch (Exception e) {
                        throw new ServiceException("批量新增市场洞察宏观详情失败！");
                    }

                    for (int i1 = 0; i1 < miMacroDetailAddList.size(); i1++) {
                        //新增市场洞察宏观预估集合
                        List<MiMacroEstimate> miMacroEstimateAddList = new ArrayList<>();
                        List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroDetailDTOS.get(i1).getMiMacroEstimateDTOS();
                        if (StringUtils.isNotEmpty(miMacroEstimateDTOS)) {
                            for (MiMacroEstimateDTO miMacroEstimateDTO : miMacroEstimateDTOS) {
                                MiMacroEstimate miMacroEstimate = new MiMacroEstimate();
                                BeanUtils.copyProperties(miMacroEstimateDTO, miMacroEstimate);
                                miMacroEstimate.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
                                miMacroEstimate.setMiMacroDetailId(miMacroDetailAddList.get(i1).getMiMacroDetailId());
                                miMacroEstimate.setCreateBy(SecurityUtils.getUserId());
                                miMacroEstimate.setCreateTime(DateUtils.getNowDate());
                                miMacroEstimate.setUpdateTime(DateUtils.getNowDate());
                                miMacroEstimate.setUpdateBy(SecurityUtils.getUserId());
                                miMacroEstimate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                                miMacroEstimateAddList.add(miMacroEstimate);
                            }
                        }
                        if (StringUtils.isNotEmpty(miMacroEstimateAddList)) {
                            try {
                                miMacroEstimateMapper.batchMiMacroEstimate(miMacroEstimateAddList);
                            } catch (Exception e) {
                                throw new ServiceException("批量新增市场洞察宏观预估失败！");
                            }
                        }
                    }

                }

            }
        }
        return i;
    }

    private void packMiMacroEstimateAll(List<MiMacroDetailDTO> miMacroDetailDTOS, List<MiMacroDetail> miMacroDetailAddList,  MarketInsightMacro marketInsightMacro) {
        for (MiMacroDetailDTO miMacroDetailDTO : miMacroDetailDTOS) {
            //新增市场洞察宏观预估集合
            List<MiMacroEstimate> miMacroEstimateAddList = new ArrayList<>();
            //修改市场洞察宏观预估集合
            List<MiMacroEstimate> miMacroEstimateUpdateList = new ArrayList<>();
            int num = 0;
            List<Long> miMacroEstimateIds = new ArrayList<>();
            Long miMacroDetailId = miMacroDetailDTO.getMiMacroDetailId();
            //传入市场洞察宏观预估集合
            List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroDetailDTO.getMiMacroEstimateDTOS();
            if (null != miMacroDetailId) {
                List<MiMacroEstimateDTO> miMacroEstimateListData = new ArrayList<>();
                miMacroEstimateListData = miMacroEstimateMapper.selectMiMacroEstimateByMiMacroDetailId(miMacroDetailId);
                if (StringUtils.isNotEmpty(miMacroEstimateDTOS)) {
                    if (StringUtils.isNotEmpty(miMacroEstimateListData)) {
                        //sterm流求差集
                        miMacroEstimateIds = miMacroEstimateListData.stream().filter(a ->
                                !miMacroEstimateDTOS.stream().map(MiMacroEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList()).contains(a.getMiMacroEstimateId())
                        ).collect(Collectors.toList()).stream().map(MiMacroEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                            miMacroEstimateMapper.logicDeleteMiMacroEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        }
                    }
                    for (MiMacroEstimateDTO miMacroEstimateDTO : miMacroEstimateDTOS) {
                        Long miMacroEstimateId = miMacroEstimateDTO.getMiMacroEstimateId();
                        if (null != miMacroEstimateId){
                            MiMacroEstimate miMacroEstimate = new MiMacroEstimate();
                            BeanUtils.copyProperties(miMacroEstimateDTO, miMacroEstimate);
                            miMacroEstimate.setUpdateBy(SecurityUtils.getUserId());
                            miMacroEstimate.setUpdateTime(DateUtils.getNowDate());
                            miMacroEstimateUpdateList.add(miMacroEstimate);
                        }else {
                            MiMacroEstimate miMacroEstimate = new MiMacroEstimate();
                            BeanUtils.copyProperties(miMacroEstimateDTO, miMacroEstimate);
                            miMacroEstimate.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
                            //详情id
                            miMacroEstimate.setMiMacroDetailId(miMacroDetailId);
                            miMacroEstimate.setCreateBy(SecurityUtils.getUserId());
                            miMacroEstimate.setCreateTime(DateUtils.getNowDate());
                            miMacroEstimate.setUpdateTime(DateUtils.getNowDate());
                            miMacroEstimate.setUpdateBy(SecurityUtils.getUserId());
                            miMacroEstimate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                            miMacroEstimateAddList.add(miMacroEstimate);
                        }

                    }
                }else {
                    List<MiMacroEstimateDTO> miMacroEstimateListData2 = new ArrayList<>();
                    miMacroEstimateListData2 = miMacroEstimateMapper.selectMiMacroEstimateByMiMacroDetailId(miMacroDetailId);
                    if (StringUtils.isNotEmpty(miMacroEstimateListData2)) {
                        List<Long> collect = miMacroEstimateListData2.stream().map(MiMacroEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(collect)) {
                            miMacroEstimateMapper.logicDeleteMiMacroEstimateByMiMacroEstimateIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        }
                    }
                }
            } else {
                if (StringUtils.isNotEmpty(miMacroEstimateDTOS) && StringUtils.isNotEmpty(miMacroDetailAddList)) {
                    for (MiMacroEstimateDTO miMacroEstimateDTO : miMacroEstimateDTOS) {
                        MiMacroEstimate miMacroEstimate = new MiMacroEstimate();
                        BeanUtils.copyProperties(miMacroEstimateDTO, miMacroEstimate);
                        miMacroEstimate.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
                        try {
                            miMacroEstimate.setMiMacroDetailId(miMacroDetailAddList.get(num).getMiMacroDetailId());
                        } catch (Exception e) {
                            throw new ServiceException("数组越界异常" + miMacroDetailAddList.size() + ":" + num);
                        }
                        miMacroEstimate.setCreateBy(SecurityUtils.getUserId());
                        miMacroEstimate.setCreateTime(DateUtils.getNowDate());
                        miMacroEstimate.setUpdateTime(DateUtils.getNowDate());
                        miMacroEstimate.setUpdateBy(SecurityUtils.getUserId());
                        miMacroEstimate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        miMacroEstimateAddList.add(miMacroEstimate);
                    }
                }
                num++;
            }
            if (StringUtils.isNotEmpty(miMacroEstimateUpdateList)) {
                try {
                    miMacroEstimateMapper.updateMiMacroEstimates(miMacroEstimateUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("批量修改市场洞察宏观预估失败！");
                }
            }
            if (StringUtils.isNotEmpty(miMacroEstimateAddList)) {
                try {
                    miMacroEstimateMapper.batchMiMacroEstimate(miMacroEstimateAddList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察宏观预估失败！");
                }
            }
        }

    }

    /**
     * 批量修改新增市场洞察宏观详情
     *
     * @param miMacroDetailDTOS
     * @param miMacroDetailAddList
     * @param miMacroDetailUpdateList
     * @param marketInsightMacro
     */
    private void packMiMacroDetailAll(List<MiMacroDetailDTO> miMacroDetailDTOS, List<MiMacroDetail> miMacroDetailAddList, List<MiMacroDetail> miMacroDetailUpdateList, MarketInsightMacro marketInsightMacro) {
        for (int i = 0; i < miMacroDetailDTOS.size(); i++) {
            Long miMacroDetailId = miMacroDetailDTOS.get(i).getMiMacroDetailId();
            if (StringUtils.isNotNull(miMacroDetailId)) {
                MiMacroDetail miMacroDetail = new MiMacroDetail();
                BeanUtils.copyProperties(miMacroDetailDTOS.get(i), miMacroDetail);
                miMacroDetail.setSort(i+1);
                miMacroDetail.setUpdateBy(SecurityUtils.getUserId());
                miMacroDetail.setUpdateTime(DateUtils.getNowDate());
                miMacroDetailUpdateList.add(miMacroDetail);
            } else {
                MiMacroDetail miMacroDetail = new MiMacroDetail();
                BeanUtils.copyProperties(miMacroDetailDTOS.get(i), miMacroDetail);
                miMacroDetail.setSort(i+1);
                miMacroDetail.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
                miMacroDetail.setCreateBy(SecurityUtils.getUserId());
                miMacroDetail.setCreateTime(DateUtils.getNowDate());
                miMacroDetail.setUpdateTime(DateUtils.getNowDate());
                miMacroDetail.setUpdateBy(SecurityUtils.getUserId());
                miMacroDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miMacroDetailAddList.add(miMacroDetail);
            }
        }

        if (StringUtils.isNotEmpty(miMacroDetailAddList)) {
            try {
                miMacroDetailMapper.batchMiMacroDetail(miMacroDetailAddList);
            } catch (Exception e) {
                throw new ServiceException("批量新增市场洞察宏观详情失败");
            }
        }
        if (StringUtils.isNotEmpty(miMacroDetailUpdateList)) {
            try {
                miMacroDetailMapper.updateMiMacroDetails(miMacroDetailUpdateList);
            } catch (Exception e) {
                throw new ServiceException("批量修改市场洞察宏观详情失败");
            }
        }
    }

    /**
     * 逻辑批量删除市场洞察宏观表
     *
     * @param marketInsightMacroIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightMacroByMarketInsightMacroIds(List<Long> marketInsightMacroIds) {
        int i = 0;
        if (StringUtils.isNotEmpty(marketInsightMacroIds)) {
            try {
                i = marketInsightMacroMapper.logicDeleteMarketInsightMacroByMarketInsightMacroIds(marketInsightMacroIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("逻辑批量删除市场洞察宏观失败");
            }
            List<MiMacroDetailDTO> miMacroDetailDTOS = miMacroDetailMapper.selectMiMacroDetailByMarketInsightMacroIds(marketInsightMacroIds);
            if (StringUtils.isNotEmpty(miMacroDetailDTOS)) {
                List<Long> miMacroDetailIds = miMacroDetailDTOS.stream().map(MiMacroDetailDTO::getMiMacroDetailId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(miMacroDetailIds)) {
                    try {
                        miMacroDetailMapper.logicDeleteMiMacroDetailByMiMacroDetailIds(miMacroDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除市场洞察宏观详情失败");
                    }
                    List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroEstimateMapper.selectMiMacroEstimateByMiMacroDetailIds(miMacroDetailIds);
                    if (StringUtils.isNotEmpty(miMacroEstimateDTOS)) {
                        List<Long> miMacroEstimateIds = miMacroEstimateDTOS.stream().map(MiMacroEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                            try {
                                miMacroEstimateMapper.logicDeleteMiMacroEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                            } catch (Exception e) {
                                throw new ServiceException("逻辑批量删除市场洞察宏观预估失败");
                            }
                        }
                    }
                }
            }
        }
        return i;
    }

    /**
     * 物理删除市场洞察宏观表信息
     *
     * @param marketInsightMacroId 市场洞察宏观表主键
     * @return 结果
     */
    @Override
    public int deleteMarketInsightMacroByMarketInsightMacroId(Long marketInsightMacroId) {
        return marketInsightMacroMapper.deleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacroId);
    }

    /**
     * 逻辑删除市场洞察宏观表信息
     *
     * @param marketInsightMacroDTO 市场洞察宏观表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightMacroByMarketInsightMacroId(MarketInsightMacroDTO marketInsightMacroDTO) {
        int i = 0;
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        marketInsightMacro.setMarketInsightMacroId(marketInsightMacroDTO.getMarketInsightMacroId());
        marketInsightMacro.setUpdateTime(DateUtils.getNowDate());
        marketInsightMacro.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = marketInsightMacroMapper.logicDeleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacro);
        } catch (Exception e) {
            throw new ServiceException("逻辑删除市场洞察宏观失败");
        }
        List<MiMacroDetailDTO> miMacroDetailDTOS = miMacroDetailMapper.selectMiMacroDetailByMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
        if (StringUtils.isNotEmpty(miMacroDetailDTOS)) {
            List<Long> miMacroDetailIds = miMacroDetailDTOS.stream().map(MiMacroDetailDTO::getMiMacroDetailId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miMacroDetailIds)) {
                try {
                    miMacroDetailMapper.logicDeleteMiMacroDetailByMiMacroDetailIds(miMacroDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除市场洞察宏观详情失败");
                }
                List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroEstimateMapper.selectMiMacroEstimateByMiMacroDetailIds(miMacroDetailIds);
                if (StringUtils.isNotEmpty(miMacroEstimateDTOS)) {
                    List<Long> miMacroEstimateIds = miMacroEstimateDTOS.stream().map(MiMacroEstimateDTO::getMiMacroEstimateId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(miMacroEstimateIds)) {
                        try {
                            miMacroEstimateMapper.logicDeleteMiMacroEstimateByMiMacroEstimateIds(miMacroEstimateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除市场洞察宏观预估失败");
                        }
                    }
                }
            }
        }
        return i;
    }

    /**
     * 物理删除市场洞察宏观表信息
     *
     * @param marketInsightMacroDTO 市场洞察宏观表
     * @return 结果
     */

    @Override
    public int deleteMarketInsightMacroByMarketInsightMacroId(MarketInsightMacroDTO marketInsightMacroDTO) {
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacro);
        return marketInsightMacroMapper.deleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
    }

    /**
     * 物理批量删除市场洞察宏观表
     *
     * @param marketInsightMacroDtos 需要删除的市场洞察宏观表主键
     * @return 结果
     */

    @Override
    public int deleteMarketInsightMacroByMarketInsightMacroIds(List<MarketInsightMacroDTO> marketInsightMacroDtos) {
        List<Long> stringList = new ArrayList();
        for (MarketInsightMacroDTO marketInsightMacroDTO : marketInsightMacroDtos) {
            stringList.add(marketInsightMacroDTO.getMarketInsightMacroId());
        }
        return marketInsightMacroMapper.deleteMarketInsightMacroByMarketInsightMacroIds(stringList);
    }

    /**
     * 导出Excel
     *
     * @param marketInsightMacroDTO
     * @return
     */
    @Override
    public List<MarketInsightMacroExcel> exportMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO) {
        List<MarketInsightMacroExcel> marketInsightMacroExcelList = new ArrayList<>();
        if (StringUtils.isNotNull(marketInsightMacroDTO)) {
            List<MiMacroDetailDTO> miMacroDetailDTOS = marketInsightMacroDTO.getMiMacroDetailDTOS();
            for (MiMacroDetailDTO miMacroDetailDTO : miMacroDetailDTOS) {
                MarketInsightMacroExcel marketInsightMacroExcel = new MarketInsightMacroExcel();
                BeanUtils.copyProperties(miMacroDetailDTO, marketInsightMacroExcel);
                marketInsightMacroExcel.setPlanPeriod(String.valueOf(miMacroDetailDTO.getPlanPeriod()));
                BigDecimal estimateOpportunityAmount = miMacroDetailDTO.getEstimateOpportunityAmount();
                if (null != estimateOpportunityAmount){
                    marketInsightMacroExcel.setEstimateOpportunityAmount(String.valueOf(miMacroDetailDTO.getEstimateOpportunityAmount().setScale(2, BigDecimal.ROUND_HALF_UP)));
                }else {
                    marketInsightMacroExcel.setEstimateOpportunityAmount("0.00");
                }

                marketInsightMacroExcelList.add(marketInsightMacroExcel);
            }
        }
        return marketInsightMacroExcelList;
    }

    /**
     * 看宏观远程调用列表查询是否被引用
     * @param marketInsightMacroDTO
     * @return
     */
    @Override
    public List<MarketInsightMacroDTO> remoteMarketInsightMacroList(MarketInsightMacroDTO marketInsightMacroDTO) {
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO,marketInsightMacro);
        return marketInsightMacroMapper.remoteMarketInsightMacroList(marketInsightMacro);
    }

    /**
     * 看宏观远程调用列表查询是否被引用
     * @param miMacroDetailDTO
     * @return
     */
    @Override
    public List<MiMacroDetailDTO> remoteMiMacroDetailList(MiMacroDetailDTO miMacroDetailDTO) {
        MiMacroDetail miMacroDetail = new MiMacroDetail();
        BeanUtils.copyProperties(miMacroDetailDTO,miMacroDetail);
        return   miMacroDetailMapper.remoteMiMacroDetailList(miMacroDetail);
    }
}

