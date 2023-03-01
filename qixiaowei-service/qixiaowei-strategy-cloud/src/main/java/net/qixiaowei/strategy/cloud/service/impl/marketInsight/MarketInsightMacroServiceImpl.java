package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

import java.util.*;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiMacroDetail;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiMacroEstimate;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiMacroDetailDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiMacroEstimateDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiMacroDetailMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MiMacroEstimateMapper;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightMacro;
import net.qixiaowei.strategy.cloud.excel.marketInsight.MarketInsightMacroExcel;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightMacroMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightMacroService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


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

    /**
     * 查询市场洞察宏观表
     *
     * @param marketInsightMacroId 市场洞察宏观表主键
     * @return 市场洞察宏观表
     */
    @Override
    public MarketInsightMacroDTO selectMarketInsightMacroByMarketInsightMacroId(Long marketInsightMacroId) {
        MarketInsightMacroDTO marketInsightMacroDTO = marketInsightMacroMapper.selectMarketInsightMacroByMarketInsightMacroId(marketInsightMacroId);
        List<MiMacroDetailDTO> miMacroDetailDTOS = miMacroDetailMapper.selectMiMacroDetailByMarketInsightMacroId(marketInsightMacroId);
        if (StringUtils.isNotEmpty(miMacroDetailDTOS)){
            //市场洞察宏观详情表主键集合
            List<Long> miMacroDetailIds = miMacroDetailDTOS.stream().map(MiMacroDetailDTO::getMiMacroDetailId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(miMacroDetailIds)){
                List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroEstimateMapper.selectMiMacroEstimateByMiMacroDetailIds(miMacroDetailIds);
                //根据市场洞察宏观详情表主键分组
                Map<Long, List<MiMacroEstimateDTO>> miMacroEstimateMapData = miMacroEstimateDTOS.parallelStream().collect(Collectors.groupingBy(MiMacroEstimateDTO::getMiMacroDetailId));
                for (MiMacroDetailDTO miMacroDetailDTO : miMacroDetailDTOS) {
                    miMacroDetailDTO.setMiMacroEstimateDTOS(miMacroEstimateMapData.get(miMacroDetailDTO.getMiMacroDetailId()));
                }
            }
        }
        marketInsightMacroDTO.setMiMacroDetailDTOS(miMacroDetailDTOS);
        return marketInsightMacroMapper.selectMarketInsightMacroByMarketInsightMacroId(marketInsightMacroId);
    }

    /**
     * 查询市场洞察宏观表列表
     *
     * @param marketInsightMacroDTO 市场洞察宏观表
     * @return 市场洞察宏观表
     */
    @Override
    public List<MarketInsightMacroDTO> selectMarketInsightMacroList(MarketInsightMacroDTO marketInsightMacroDTO) {
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacro);
        //高级搜索请求参数
        Map<String, Object> params = marketInsightMacroDTO.getParams();
        this.queryemployeeName(params);
        List<MarketInsightMacroDTO> marketInsightMacroDTOS = marketInsightMacroMapper.selectMarketInsightMacroList(marketInsightMacro);
        if (StringUtils.isNotEmpty(marketInsightMacroDTOS)) {
            Set<Long> createBys = marketInsightMacroDTOS.stream().map(MarketInsightMacroDTO::getCreateBy).collect(Collectors.toSet());
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
            List<UserDTO> userDTOList = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(userDTOList)) {
                for (MarketInsightMacroDTO insightMacroDTO : marketInsightMacroDTOS) {
                    for (UserDTO userDTO : userDTOList) {
                        if (insightMacroDTO.getCreateBy().equals(userDTO.getUserId())) {
                            insightMacroDTO.setCreateByName(userDTO.getEmployeeName());
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
                    List<Long> employeeIds = data.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(employeeIds)) {
                        params.put("createBys", employeeIds);
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
    public MarketInsightMacroDTO insertMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO) {
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
        if (StringUtils.isNotEmpty(miMacroDetailDTOS)){
            for (MiMacroDetailDTO miMacroDetailDTO : miMacroDetailDTOS) {

                MiMacroDetail miMacroDetail = new MiMacroDetail();
                BeanUtils.copyProperties(miMacroDetailDTO,miMacroDetail);
                miMacroDetail.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
                miMacroDetail.setCreateBy(SecurityUtils.getUserId());
                miMacroDetail.setCreateTime(DateUtils.getNowDate());
                miMacroDetail.setUpdateTime(DateUtils.getNowDate());
                miMacroDetail.setUpdateBy(SecurityUtils.getUserId());
                miMacroDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                miMacroDetailList.add(miMacroDetail);
            }
            if (StringUtils.isNotEmpty(miMacroDetailList)){
                try {
                    miMacroDetailMapper.batchMiMacroDetail(miMacroDetailList);
                } catch (Exception e) {
                    throw new ServiceException("批量新增市场洞察宏观详情失败");
                }
                List<MiMacroEstimate> miMacroEstimateList = new ArrayList<>();
                for (int i = 0; i < miMacroDetailDTOS.size(); i++) {
                    //前端传入市场洞察宏观预估表集合
                    List<MiMacroEstimateDTO> miMacroEstimateDTOS = miMacroDetailDTOS.get(i).getMiMacroEstimateDTOS();
                    if (StringUtils.isNotEmpty(miMacroEstimateDTOS)){
                        for (MiMacroEstimateDTO miMacroEstimateDTO : miMacroEstimateDTOS) {
                            MiMacroEstimate miMacroEstimate = new MiMacroEstimate();
                            BeanUtils.copyProperties(miMacroEstimateDTO,miMacroEstimate);
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
                    if (StringUtils.isNotEmpty(miMacroEstimateList)){
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
    public int updateMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO) {
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacro);
        marketInsightMacro.setUpdateTime(DateUtils.getNowDate());
        marketInsightMacro.setUpdateBy(SecurityUtils.getUserId());
        return marketInsightMacroMapper.updateMarketInsightMacro(marketInsightMacro);
    }

    /**
     * 逻辑批量删除市场洞察宏观表
     *
     * @param marketInsightMacroIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteMarketInsightMacroByMarketInsightMacroIds(List<Long> marketInsightMacroIds) {
        return marketInsightMacroMapper.logicDeleteMarketInsightMacroByMarketInsightMacroIds(marketInsightMacroIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
    public int logicDeleteMarketInsightMacroByMarketInsightMacroId(MarketInsightMacroDTO marketInsightMacroDTO) {
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        marketInsightMacro.setMarketInsightMacroId(marketInsightMacroDTO.getMarketInsightMacroId());
        marketInsightMacro.setUpdateTime(DateUtils.getNowDate());
        marketInsightMacro.setUpdateBy(SecurityUtils.getUserId());
        return marketInsightMacroMapper.logicDeleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacro);
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
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacro);
        List<MarketInsightMacroDTO> marketInsightMacroDTOList = marketInsightMacroMapper.selectMarketInsightMacroList(marketInsightMacro);
        List<MarketInsightMacroExcel> marketInsightMacroExcelList = new ArrayList<>();
        return marketInsightMacroExcelList;
    }
}

