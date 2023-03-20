package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.Area;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.AreaMapper;
import net.qixiaowei.operate.cloud.service.targetManager.IAreaService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.api.remote.businessDesign.RemoteBusinessDesignService;
import net.qixiaowei.strategy.cloud.api.remote.gap.RemoteGapAnalysisService;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteAnnualKeyWorkService;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMeasureService;
import net.qixiaowei.strategy.cloud.api.remote.strategyDecode.RemoteStrategyMetricsService;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import net.qixiaowei.system.manage.api.remote.system.RemoteRegionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * AreaService业务层处理
 *
 * @author Graves
 * @since 2022-10-07
 */
@Service
public class AreaServiceImpl implements IAreaService {
    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private RemoteOfficialRankSystemService officialRankSystemService;

    @Autowired
    private ITargetDecomposeService targetDecomposeService;

    @Autowired
    private RemoteRegionService regionService;

    @Autowired
    private RemoteAnnualKeyWorkService remoteAnnualKeyWorkService;

    @Autowired
    private RemoteStrategyMeasureService remoteStrategyMeasureService;

    @Autowired
    private RemoteStrategyMetricsService remoteStrategyMetricsService;

    @Autowired
    private RemoteGapAnalysisService remoteGapAnalysisService;

    @Autowired
    private RemoteBusinessDesignService remoteBusinessDesignService;

    /**
     * 查询区域表
     *
     * @param areaId 区域表主键
     * @return 区域表
     */
    @Override
    public AreaDTO selectAreaByAreaId(Long areaId) {
        return areaMapper.selectAreaByAreaId(areaId);
    }

    /**
     * 查询区域表列表
     *
     * @param areaDTO 区域表
     * @return 区域表
     */
    @Override
    public List<AreaDTO> selectAreaList(AreaDTO areaDTO) {
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        List<AreaDTO> areaDTOS = areaMapper.selectAreaList(area);
        this.handleResult(areaDTOS);
        return areaDTOS;
    }

    @Override
    public void handleResult(List<AreaDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(AreaDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 生成区域编码
     *
     * @return 区域编码
     */
    @Override
    public String generateAreaCode() {
        String areaCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.AREA.getCode();
        List<String> areaCodes = areaMapper.getAreaCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(areaCodes)) {
            for (String code : areaCodes) {
                if (StringUtils.isEmpty(code) || code.length() != 5) {
                    continue;
                }
                code = code.replaceFirst(prefixCodeRule, "");
                try {
                    int codeOfNumber = Integer.parseInt(code);
                    if (number != codeOfNumber) {
                        break;
                    }
                    number++;
                } catch (Exception ignored) {
                }
            }
        }
        if (number > 1000) {
            throw new ServiceException("流水号溢出，请联系管理员");
        }
        areaCode = "000" + number;
        areaCode = prefixCodeRule + areaCode.substring(areaCode.length() - 3);
        return areaCode;
    }

    /**
     * 新增区域表
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertArea(AreaDTO areaDTO) {
        String areaCode = areaDTO.getAreaCode();
        String areaName = areaDTO.getAreaName();
        String regionIds = areaDTO.getRegionIds();
        String regionNames = areaDTO.getRegionNames();
        if (StringUtils.isEmpty(areaCode)) {
            throw new ServiceException("区域编码不能为空");
        }
        if (StringUtils.isEmpty(areaName)) {
            throw new ServiceException("区域名称不能为空");
        }
        if (StringUtils.isEmpty(regionIds) && StringUtils.isEmpty(regionNames)) {
            throw new ServiceException("省份不可以为空");
        }
        AreaDTO areaByCode = areaMapper.checkUnique(areaCode);
        if (StringUtils.isNotNull(areaByCode)) {
            throw new ServiceException("区域编码不可重复");
        }
//        String[] regionIdList = regionIds.split(",");
//        Set<Long> regionIdSet = new HashSet<>();
//        for (String s : regionIdList) {
//            regionIdSet.add(Long.parseLong(s));
//        }
//        R<List<RegionDTO>> regionsByIds = regionService.getRegionsByIds(regionIdSet, SecurityConstants.INNER);
//        List<RegionDTO> regionDTOS = regionsByIds.getData();
//        if (StringUtils.isEmpty(regionDTOS)) {
//            throw new ServiceException("当前区域不存在");
//        }
//        StringBuilder regionSB = new StringBuilder();
//        for (RegionDTO regionDTO : regionDTOS) {
//            regionSB.append(regionDTO.getRegionName()).append("；");
//        }
//        areaDTO.setRegionNames(regionSB.substring(0, regionSB.length() - 1));
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        area.setCreateBy(SecurityUtils.getUserId());
        area.setCreateTime(DateUtils.getNowDate());
        area.setUpdateTime(DateUtils.getNowDate());
        area.setUpdateBy(SecurityUtils.getUserId());
        area.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return areaMapper.insertArea(area);
    }

    /**
     * 修改区域表
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateArea(AreaDTO areaDTO) {
        Long areaId = areaDTO.getAreaId();
        String areaCode = areaDTO.getAreaCode();
        String areaName = areaDTO.getAreaName();
        String regionIds = areaDTO.getRegionIds();
        String regionNames = areaDTO.getRegionNames();
        if (StringUtils.isEmpty(areaCode)) {
            throw new ServiceException("区域编码不能为空");
        }
        if (StringUtils.isEmpty(areaName)) {
            throw new ServiceException("区域名称不能为空");
        }
        if (StringUtils.isEmpty(regionIds) || StringUtils.isEmpty(regionNames)) {
            throw new ServiceException("省份不可以为空");
        }
        AreaDTO areaByCode = areaMapper.checkUnique(areaCode);
        if (StringUtils.isNotNull(areaByCode)) {
            if (!areaByCode.getAreaId().equals(areaId)) {
                throw new ServiceException("区域编码不可重复");
            }
        }
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        area.setUpdateTime(DateUtils.getNowDate());
        area.setUpdateBy(SecurityUtils.getUserId());
        return areaMapper.updateArea(area);
    }

    /**
     * 物理删除区域表信息
     *
     * @param areaId 区域表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAreaByAreaId(Long areaId) {
        return areaMapper.deleteAreaByAreaId(areaId);
    }

    /**
     * 查询分解维度区域下拉列表
     *
     * @param areaDTO
     * @return ID, Name
     */
    @Override
    public List<AreaDTO> selectDropList(AreaDTO areaDTO) {
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        return areaMapper.dropList(area);
    }

    /**
     * 查询区域配置列表通过IDS
     *
     * @param areaIds 区域表集合
     * @return 结果
     */
    @Override
    public List<AreaDTO> selectAreaListByAreaIds(List<Long> areaIds) {
        return areaMapper.selectAreaListByAreaIds(areaIds);
    }

    /**
     * 逻辑删除区域表信息
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteAreaByAreaDTO(AreaDTO areaDTO) {
        Long areaId = areaDTO.getAreaId();
        if (StringUtils.isNull(areaId)) {
            throw new ServiceException("区域ID不能为空");
        }
        AreaDTO areaById = areaMapper.selectAreaByAreaId(areaId);
        if (StringUtils.isNull(areaById)) {
            throw new ServiceException("当前区域配置不存在");
        }
        List<AreaDTO> areaByIds = new ArrayList<>();
        areaByIds.add(areaById);
        List<Long> areaIds = new ArrayList<>();
        areaIds.add(areaId);
        isQuote(areaIds, areaByIds);
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        return areaMapper.logicDeleteAreaByAreaId(area, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 引用校验
     *
     * @param areaIds   区域ID集合
     * @param areaByIds 区域DTO
     */
    private void isQuote(List<Long> areaIds, List<AreaDTO> areaByIds) {
        StringBuilder quoteReminder = new StringBuilder("");
        R<List<OfficialRankDecomposeDTO>> listR = officialRankSystemService.selectOfficialDecomposeByDimensions(areaIds, 2, SecurityConstants.INNER);
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用职级分解失败 请联系管理员");
        }
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = listR.getData();
        if (StringUtils.isNotEmpty(officialRankDecomposeDTOS)) {
            StringBuilder areaNames = new StringBuilder("");
            for (AreaDTO areaById : areaByIds) {
                for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDTOS) {
                    if (officialRankDecomposeDTO.getDecomposeDimension().equals(areaById.getAreaId())) {
                        areaNames.append(areaById.getAreaName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("区域配置【").append(areaNames.deleteCharAt(areaNames.length() - 1)).append("】已被职级配置中的【职级分解】引用 无法删除\n");
        }
        Map<Integer, List<Long>> map = new HashMap<>();
        map.put(2, areaIds);
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeService.selectByIds(map);
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
            StringBuilder areaNames = new StringBuilder("");
            for (AreaDTO areaById : areaByIds) {
                for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                    if (targetDecomposeDetailsDTO.getAreaId().equals(areaById.getAreaId())) {
                        areaNames.append(areaById.getAreaName()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("区域配置【").append(areaNames.deleteCharAt(areaNames.length() - 1)).append("】正在被目标分解中的【分解维度-区域】引用 无法删除\n");
        }
        if (quoteReminder.length() != 0) {
            throw new ServiceException(quoteReminder.toString());
        }
        // 战略云引用
        isStrategyQuote(areaIds);
    }

    /**
     * 战略云引用
     *
     * @param areaIds 区域ID集合
     */
    private void isStrategyQuote(List<Long> areaIds) {
        Map<String, Object> params;
        AnnualKeyWorkDTO annualKeyWorkDTO = new AnnualKeyWorkDTO();
        params = new HashMap<>();
        params.put("areaIdEqual", areaIds);
        annualKeyWorkDTO.setParams(params);
        R<List<AnnualKeyWorkDTO>> remoteAnnualKeyWorkR = remoteAnnualKeyWorkService.remoteAnnualKeyWork(annualKeyWorkDTO, SecurityConstants.INNER);
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = remoteAnnualKeyWorkR.getData();
        if (remoteAnnualKeyWorkR.getCode() != 200) {
            throw new ServiceException("远程调用年度重点工作失败");
        }
        if (StringUtils.isNotEmpty(annualKeyWorkDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        StrategyMeasureDTO strategyMeasureDTO = new StrategyMeasureDTO();
        params = new HashMap<>();
        params.put("areaIdEqual", areaIds);
        strategyMeasureDTO.setParams(params);
        R<List<StrategyMeasureDTO>> remoteStrategyMeasureR = remoteStrategyMeasureService.remoteStrategyMeasure(strategyMeasureDTO, SecurityConstants.INNER);
        List<StrategyMeasureDTO> strategyMeasureDTOS = remoteStrategyMeasureR.getData();
        if (remoteStrategyMeasureR.getCode() != 200) {
            throw new ServiceException("远程调用战略举措清单失败");
        }
        if (StringUtils.isNotEmpty(strategyMeasureDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        StrategyMetricsDTO strategyMetricsDTO = new StrategyMetricsDTO();
        params = new HashMap<>();
        params.put("areaIdEqual", areaIds);
        strategyMetricsDTO.setParams(params);
        R<List<StrategyMetricsDTO>> remoteStrategyMetricsR = remoteStrategyMetricsService.remoteStrategyMetrics(strategyMetricsDTO, SecurityConstants.INNER);
        List<StrategyMetricsDTO> strategyMetricsDTOS = remoteStrategyMetricsR.getData();
        if (remoteStrategyMetricsR.getCode() != 200) {
            throw new ServiceException("远程调用战略衡量指标失败");
        }
        if (StringUtils.isNotEmpty(strategyMetricsDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        GapAnalysisDTO gapAnalysisDTO = new GapAnalysisDTO();
        params = new HashMap<>();
        params.put("areaIdEqual", areaIds);
        gapAnalysisDTO.setParams(params);
        R<List<GapAnalysisDTO>> remoteGapAnalysisR = remoteGapAnalysisService.remoteGapAnalysis(gapAnalysisDTO, SecurityConstants.INNER);
        List<GapAnalysisDTO> gapAnalysisDTOS = remoteGapAnalysisR.getData();
        if (remoteGapAnalysisR.getCode() != 200) {
            throw new ServiceException("远程调用差距分析失败");
        }
        if (StringUtils.isNotEmpty(gapAnalysisDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        BusinessDesignDTO businessDesignDTO = new BusinessDesignDTO();
        params = new HashMap<>();
        params.put("areaIdEqual", areaIds);
        businessDesignDTO.setParams(params);
        R<List<BusinessDesignDTO>> remoteBusinessDesignR = remoteBusinessDesignService.remoteBusinessDesign(businessDesignDTO, SecurityConstants.INNER);
        List<BusinessDesignDTO> businessDesignDTOS = remoteBusinessDesignR.getData();
        if (remoteBusinessDesignR.getCode() != 200) {
            throw new ServiceException("远程调用业务设计失败");
        }
        if (StringUtils.isNotEmpty(businessDesignDTOS)) {
            throw new ServiceException("数据被引用!");
        }
        BusinessDesignParamDTO businessDesignParamDTO = new BusinessDesignParamDTO();
        params = new HashMap<>();
        params.put("areaIds", areaIds);
        businessDesignParamDTO.setParams(params);
        R<List<BusinessDesignParamDTO>> BusinessDesignParamDTOSR = remoteBusinessDesignService.remoteBusinessDesignParams(businessDesignParamDTO, SecurityConstants.INNER);
        List<BusinessDesignParamDTO> BusinessDesignParamDTOS = BusinessDesignParamDTOSR.getData();
        if (BusinessDesignParamDTOSR.getCode() != 200) {
            throw new ServiceException("远程调用失败");
        }
        if (StringUtils.isNotEmpty(BusinessDesignParamDTOS)) {
            throw new ServiceException("数据被引用!");
        }

    }

    /**
     * 逻辑批量删除区域表
     *
     * @param areaIds 需要删除的区域表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteAreaByAreaIds(List<Long> areaIds) {
        if (StringUtils.isEmpty(areaIds)) {
            throw new ServiceException("区域id不能为空");
        }
        List<AreaDTO> areaByIds = areaMapper.selectAreaListByAreaIds(areaIds);
        if (areaByIds.size() < areaIds.size()) {
            throw new ServiceException("区域配置已不存在");
        }
        isQuote(areaIds, areaByIds);
        return areaMapper.logicDeleteAreaByAreaIds(areaIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除区域表信息
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAreaByAreaId(AreaDTO areaDTO) {
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        return areaMapper.deleteAreaByAreaId(area.getAreaId());
    }

    /**
     * 物理批量删除区域表
     *
     * @param areaDtos 需要删除的区域表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAreaByAreaIds(List<AreaDTO> areaDtos) {
        List<Long> stringList = new ArrayList<>();
        for (AreaDTO areaDTO : areaDtos) {
            stringList.add(areaDTO.getAreaId());
        }
        return areaMapper.deleteAreaByAreaIds(stringList);
    }

    /**
     * 批量新增区域表信息
     *
     * @param areaDtos 区域表对象
     */
    @Override
    @Transactional
    public int insertAreas(List<AreaDTO> areaDtos) {
        List<Area> areaList = new ArrayList<>();

        for (AreaDTO areaDTO : areaDtos) {
            Area area = new Area();
            BeanUtils.copyProperties(areaDTO, area);
            area.setCreateBy(SecurityUtils.getUserId());
            area.setCreateTime(DateUtils.getNowDate());
            area.setUpdateTime(DateUtils.getNowDate());
            area.setUpdateBy(SecurityUtils.getUserId());
            area.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            areaList.add(area);
        }
        return areaMapper.batchArea(areaList);
    }

    /**
     * 批量修改区域表信息
     *
     * @param areaDtoS 区域表对象
     */
    @Override
    @Transactional
    public int updateAreas(List<AreaDTO> areaDtoS) {
        List<Area> areaList = new ArrayList<>();

        for (AreaDTO areaDTO : areaDtoS) {
            Area area = new Area();
            BeanUtils.copyProperties(areaDTO, area);
            area.setCreateBy(SecurityUtils.getUserId());
            area.setCreateTime(DateUtils.getNowDate());
            area.setUpdateTime(DateUtils.getNowDate());
            area.setUpdateBy(SecurityUtils.getUserId());
            areaList.add(area);
        }
        return areaMapper.updateAreas(areaList);
    }
}

