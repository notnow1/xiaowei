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
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightSelf;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiSelfAbilityAccess;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightOpponentDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiOpponentChoiceDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiSelfAbilityAccessDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightSelfMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiSelfAbilityAccessMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightSelfService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.*;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * MarketInsightSelfService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-03-13
 */
@Service
public class MarketInsightSelfServiceImpl implements IMarketInsightSelfService {
    @Autowired
    private MarketInsightSelfMapper marketInsightSelfMapper;
    @Autowired
    private MiSelfAbilityAccessMapper miSelfAbilityAccessMapper;

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
     * 查询市场洞察自身表
     *
     * @param marketInsightSelfId 市场洞察自身表主键
     * @return 市场洞察自身表
     */
    @Override
    public MarketInsightSelfDTO selectMarketInsightSelfByMarketInsightSelfId(Long marketInsightSelfId) {
        MarketInsightSelfDTO insightSelfDTO = marketInsightSelfMapper.selectMarketInsightSelfByMarketInsightSelfId(marketInsightSelfId);
        List<Map<String, Object>> dropList = PlanBusinessUnitCode.getDropList(insightSelfDTO.getBusinessUnitDecompose());
        insightSelfDTO.setBusinessUnitDecomposes(dropList);
        Long productId = insightSelfDTO.getProductId();
        Long areaId = insightSelfDTO.getAreaId();
        Long departmentId = insightSelfDTO.getDepartmentId();
        Long industryId = insightSelfDTO.getIndustryId();
        if (null != productId) {
            R<ProductDTO> productDTOR = remoteProductService.remoteSelectById(productId, SecurityConstants.INNER);
            ProductDTO data = productDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                insightSelfDTO.setProductName(data.getProductName());
            }
        }
        if (null != areaId) {
            R<AreaDTO> areaDTOR = remoteAreaService.getById(areaId, SecurityConstants.INNER);
            AreaDTO data = areaDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                insightSelfDTO.setAreaName(data.getAreaName());
            }
        }
        if (null != departmentId) {
            R<DepartmentDTO> departmentDTOR = remoteDepartmentService.selectdepartmentId(departmentId, SecurityConstants.INNER);
            DepartmentDTO data = departmentDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                insightSelfDTO.setDepartmentName(data.getDepartmentName());
            }
        }
        if (null != industryId) {
            R<IndustryDTO> industryDTOR = remoteIndustryService.selectById(industryId, SecurityConstants.INNER);
            IndustryDTO data = industryDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                insightSelfDTO.setIndustryName(data.getIndustryName());
            }
        }
        
        List<MiSelfAbilityAccessDTO> miSelfAbilityAccessDTOS = miSelfAbilityAccessMapper.selectMiSelfAbilityAccessByMarketInsightSelfId(marketInsightSelfId);
        if (StringUtils.isNotEmpty(miSelfAbilityAccessDTOS)){
            //能力要素集合
            List<Long> capacityFactors = miSelfAbilityAccessDTOS.stream().filter(f -> null != f.getCapacityFactor()).map(MiSelfAbilityAccessDTO::getCapacityFactor).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(capacityFactors)) {
                R<List<DictionaryDataDTO>> dataByDictionaryDataList = remoteDictionaryDataService.selectDictionaryDataByDictionaryDataIds(capacityFactors, SecurityConstants.INNER);
                List<DictionaryDataDTO> data = dataByDictionaryDataList.getData();
                if (StringUtils.isNotNull(data)) {
                    for (MiSelfAbilityAccessDTO miSelfAbilityAccessDTO : miSelfAbilityAccessDTOS) {
                        if (null != miSelfAbilityAccessDTO.getCapacityFactor()) {
                            for (DictionaryDataDTO datum : data) {
                                if (miSelfAbilityAccessDTO.getCapacityFactor().equals(datum.getDictionaryDataId())) {
                                    miSelfAbilityAccessDTO.setCapacityFactorName(datum.getDictionaryLabel());
                                }
                            }
                        }
                    }
                }
            }
        }
        insightSelfDTO.setMiSelfAbilityAccessDTOS(miSelfAbilityAccessDTOS);
        return insightSelfDTO;
    }

    /**
     * 查询市场洞察自身表列表
     *
     * @param marketInsightSelfDTO 市场洞察自身表
     * @return 市场洞察自身表
     */
    @Override
    public List<MarketInsightSelfDTO> selectMarketInsightSelfList(MarketInsightSelfDTO marketInsightSelfDTO) {
        List<String> createByList = new ArrayList<>();
        MarketInsightSelf marketInsightSelf = new MarketInsightSelf();
        BeanUtils.copyProperties(marketInsightSelfDTO, marketInsightSelf);
        //高级搜索请求参数
        Map<String, Object> params = marketInsightSelfDTO.getParams();
        this.queryemployeeName(params);
        marketInsightSelfDTO.setParams(params);
        if (StringUtils.isNotNull(marketInsightSelfDTO.getCreateByName())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(marketInsightSelfDTO.getCreateByName());
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> employeeIds = userListData.stream().filter(f -> f.getUserId() != null).map(UserDTO::getUserId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(employeeIds)) {
                employeeIds.forEach(e -> {
                    createByList.add(String.valueOf(e));
                });
            } else {
                createByList.add("");
            }
        }
        marketInsightSelfDTO.setCreateBys(createByList);
        List<MarketInsightSelfDTO> marketInsightSelfDTOS = marketInsightSelfMapper.selectMarketInsightSelfList(marketInsightSelf);
        if (StringUtils.isNotEmpty(marketInsightSelfDTOS)) {
            List<Long> productIds = marketInsightSelfDTOS.stream().filter(f -> null != f.getProductId()).map(MarketInsightSelfDTO::getProductId).collect(Collectors.toList());
            List<Long> areaIds = marketInsightSelfDTOS.stream().filter(f -> null != f.getAreaId()).map(MarketInsightSelfDTO::getAreaId).collect(Collectors.toList());
            List<Long> departmentIds = marketInsightSelfDTOS.stream().filter(f -> null != f.getDepartmentId()).map(MarketInsightSelfDTO::getDepartmentId).collect(Collectors.toList());
            List<Long> industryIds = marketInsightSelfDTOS.stream().filter(f -> null != f.getIndustryId()).map(MarketInsightSelfDTO::getIndustryId).collect(Collectors.toList());

            if (StringUtils.isNotEmpty(productIds)) {
                R<List<ProductDTO>> productList = remoteProductService.getName(productIds, SecurityConstants.INNER);
                List<ProductDTO> data = productList.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (MarketInsightSelfDTO insightSelfDTO : marketInsightSelfDTOS) {
                        Long productId = insightSelfDTO.getProductId();
                        for (ProductDTO datum : data) {
                            if (null != productId) {
                                if (insightSelfDTO.getProductId().equals(datum.getProductId())) {
                                    insightSelfDTO.setProductName(datum.getProductName());
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
                    for (MarketInsightSelfDTO insightSelfDTO : marketInsightSelfDTOS) {
                        Long areaId = insightSelfDTO.getAreaId();
                        for (AreaDTO areaListDatum : areaListData) {
                            if (null != areaId) {
                                if (insightSelfDTO.getAreaId().equals(areaListDatum.getAreaId())) {
                                    insightSelfDTO.setAreaName(areaListDatum.getAreaName());
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
                    for (MarketInsightSelfDTO insightSelfDTO : marketInsightSelfDTOS) {
                        Long departmentId = insightSelfDTO.getDepartmentId();
                        for (DepartmentDTO datum : data) {
                            if (null != departmentId) {
                                if (insightSelfDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                    insightSelfDTO.setDepartmentName(datum.getDepartmentName());
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
                    for (MarketInsightSelfDTO insightSelfDTO : marketInsightSelfDTOS) {
                        Long industryId = insightSelfDTO.getIndustryId();
                        for (IndustryDTO datum : data) {
                            if (null != industryId) {
                                if (insightSelfDTO.getIndustryId().equals(datum.getIndustryId())) {
                                    insightSelfDTO.setIndustryName(datum.getIndustryName());
                                }
                            }
                        }
                    }
                }
            }
        }
        return marketInsightSelfDTOS;
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
                    List<Long> employeeIds = data.stream().filter(f ->f.getUserId() != null).map(EmployeeDTO::getUserId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(employeeIds)) {
                        params.put("createBys", employeeIds);
                    }
                }
            }
        }
    }
    /**
     * 新增市场洞察自身表
     *
     * @param marketInsightSelfDTO 市场洞察自身表
     * @return 结果
     */
    @Override
    @Transactional
    public MarketInsightSelfDTO insertMarketInsightSelf(MarketInsightSelfDTO marketInsightSelfDTO) {
        int i = 0;
        MarketInsightSelf marketInsightSelf = new MarketInsightSelf();
        BeanUtils.copyProperties(marketInsightSelfDTO, marketInsightSelf);
        List<MarketInsightSelfDTO> marketInsightSelfDTOS = marketInsightSelfMapper.selectMarketInsightSelfList(marketInsightSelf);
        if (StringUtils.isNotEmpty(marketInsightSelfDTOS)){
            throw new ServiceException(marketInsightSelfDTO.getPlanYear()+"年数据已存在！");
        }

        marketInsightSelf.setCreateBy(SecurityUtils.getUserId());
        marketInsightSelf.setCreateTime(DateUtils.getNowDate());
        marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
        marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
        marketInsightSelf.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            i = marketInsightSelfMapper.insertMarketInsightSelf(marketInsightSelf);
        } catch (Exception e) {
            throw new ServiceException("新增市场洞察自身失败！");
        }
        //前台传入市场洞察自身能力评估集合
        List<MiSelfAbilityAccessDTO> miSelfAbilityAccessDTOS = marketInsightSelfDTO.getMiSelfAbilityAccessDTOS();
        //保存市场洞察自身能力评估集合
        List<MiSelfAbilityAccess> miSelfAbilityAccessList = new ArrayList<>();
        if (StringUtils.isNotEmpty(miSelfAbilityAccessDTOS)){
            for (int i1 = 0; i1 < miSelfAbilityAccessDTOS.size(); i1++) {
                MiSelfAbilityAccess miSelfAbilityAccess = new MiSelfAbilityAccess();
                BeanUtils.copyProperties(miSelfAbilityAccessDTOS.get(i1),miSelfAbilityAccess);
                miSelfAbilityAccess.setMarketInsightSelfId(marketInsightSelf.getMarketInsightSelfId());
                miSelfAbilityAccess.setSort(i1+1);
                miSelfAbilityAccess.setCreateBy(SecurityUtils.getUserId());
                miSelfAbilityAccess.setCreateTime(DateUtils.getNowDate());
                miSelfAbilityAccess.setUpdateTime(DateUtils.getNowDate());
                miSelfAbilityAccess.setUpdateBy(SecurityUtils.getUserId());
                miSelfAbilityAccess.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miSelfAbilityAccessList.add(miSelfAbilityAccess);
            }
        }
        if (StringUtils.isNotEmpty(miSelfAbilityAccessList)){
            try {
                miSelfAbilityAccessMapper.batchMiSelfAbilityAccess(miSelfAbilityAccessList);
            } catch (Exception e) {
                throw new ServiceException("批量新增市场洞察自身能力评估失败！");
            }
        }
        marketInsightSelfDTO.setMarketInsightSelfId(marketInsightSelf.getMarketInsightSelfId());
        return marketInsightSelfDTO;
    }

    /**
     * 修改市场洞察自身表
     *
     * @param marketInsightSelfDTO 市场洞察自身表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateMarketInsightSelf(MarketInsightSelfDTO marketInsightSelfDTO) {
        MarketInsightSelf marketInsightSelf = new MarketInsightSelf();
        BeanUtils.copyProperties(marketInsightSelfDTO, marketInsightSelf);
        marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
        marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
        return marketInsightSelfMapper.updateMarketInsightSelf(marketInsightSelf);
    }

    /**
     * 逻辑批量删除市场洞察自身表
     *
     * @param marketInsightSelfIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightSelfByMarketInsightSelfIds(List<Long> marketInsightSelfIds) {
        return marketInsightSelfMapper.logicDeleteMarketInsightSelfByMarketInsightSelfIds(marketInsightSelfIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除市场洞察自身表信息
     *
     * @param marketInsightSelfId 市场洞察自身表主键
     * @return 结果
     */
    @Override
    public int deleteMarketInsightSelfByMarketInsightSelfId(Long marketInsightSelfId) {
        return marketInsightSelfMapper.deleteMarketInsightSelfByMarketInsightSelfId(marketInsightSelfId);
    }

    /**
     * 逻辑删除市场洞察自身表信息
     *
     * @param marketInsightSelfDTO 市场洞察自身表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteMarketInsightSelfByMarketInsightSelfId(MarketInsightSelfDTO marketInsightSelfDTO) {
        int i = 0;
        MarketInsightSelf marketInsightSelf = new MarketInsightSelf();
        marketInsightSelf.setMarketInsightSelfId(marketInsightSelfDTO.getMarketInsightSelfId());
        marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
        marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = marketInsightSelfMapper.logicDeleteMarketInsightSelfByMarketInsightSelfId(marketInsightSelf);
        } catch (Exception e) {
            throw new ServiceException("逻辑删除市场洞察自身表失败！");
        }
        List<MiSelfAbilityAccessDTO> miSelfAbilityAccessDTOList = miSelfAbilityAccessMapper.selectMiSelfAbilityAccessByMarketInsightSelfId(marketInsightSelfDTO.getMarketInsightSelfId());
        if (StringUtils.isNotEmpty(miSelfAbilityAccessDTOList)){
            List<Long> miSelfAbilityAccessIds = miSelfAbilityAccessDTOList.stream().map(MiSelfAbilityAccessDTO::getMiSelfAbilityAccessId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miSelfAbilityAccessIds)){
                miSelfAbilityAccessMapper.logicDeleteMiSelfAbilityAccessByMiSelfAbilityAccessIds(miSelfAbilityAccessIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
            }
        }
        return i;
    }

    /**
     * 物理删除市场洞察自身表信息
     *
     * @param marketInsightSelfDTO 市场洞察自身表
     * @return 结果
     */

    @Override
    public int deleteMarketInsightSelfByMarketInsightSelfId(MarketInsightSelfDTO marketInsightSelfDTO) {
        MarketInsightSelf marketInsightSelf = new MarketInsightSelf();
        BeanUtils.copyProperties(marketInsightSelfDTO, marketInsightSelf);
        return marketInsightSelfMapper.deleteMarketInsightSelfByMarketInsightSelfId(marketInsightSelf.getMarketInsightSelfId());
    }

    /**
     * 物理批量删除市场洞察自身表
     *
     * @param marketInsightSelfDtos 需要删除的市场洞察自身表主键
     * @return 结果
     */

    @Override
    public int deleteMarketInsightSelfByMarketInsightSelfIds(List<MarketInsightSelfDTO> marketInsightSelfDtos) {
        List<Long> stringList = new ArrayList();
        for (MarketInsightSelfDTO marketInsightSelfDTO : marketInsightSelfDtos) {
            stringList.add(marketInsightSelfDTO.getMarketInsightSelfId());
        }
        return marketInsightSelfMapper.deleteMarketInsightSelfByMarketInsightSelfIds(stringList);
    }

    /**
     * 批量新增市场洞察自身表信息
     *
     * @param marketInsightSelfDtos 市场洞察自身表对象
     */

    public int insertMarketInsightSelfs(List<MarketInsightSelfDTO> marketInsightSelfDtos) {
        List<MarketInsightSelf> marketInsightSelfList = new ArrayList();

        for (MarketInsightSelfDTO marketInsightSelfDTO : marketInsightSelfDtos) {
            MarketInsightSelf marketInsightSelf = new MarketInsightSelf();
            BeanUtils.copyProperties(marketInsightSelfDTO, marketInsightSelf);
            marketInsightSelf.setCreateBy(SecurityUtils.getUserId());
            marketInsightSelf.setCreateTime(DateUtils.getNowDate());
            marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
            marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
            marketInsightSelf.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            marketInsightSelfList.add(marketInsightSelf);
        }
        return marketInsightSelfMapper.batchMarketInsightSelf(marketInsightSelfList);
    }

    /**
     * 批量修改市场洞察自身表信息
     *
     * @param marketInsightSelfDtos 市场洞察自身表对象
     */

    public int updateMarketInsightSelfs(List<MarketInsightSelfDTO> marketInsightSelfDtos) {
        List<MarketInsightSelf> marketInsightSelfList = new ArrayList();

        for (MarketInsightSelfDTO marketInsightSelfDTO : marketInsightSelfDtos) {
            MarketInsightSelf marketInsightSelf = new MarketInsightSelf();
            BeanUtils.copyProperties(marketInsightSelfDTO, marketInsightSelf);
            marketInsightSelf.setCreateBy(SecurityUtils.getUserId());
            marketInsightSelf.setCreateTime(DateUtils.getNowDate());
            marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
            marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
            marketInsightSelfList.add(marketInsightSelf);
        }
        return marketInsightSelfMapper.updateMarketInsightSelfs(marketInsightSelfList);
    }
}

