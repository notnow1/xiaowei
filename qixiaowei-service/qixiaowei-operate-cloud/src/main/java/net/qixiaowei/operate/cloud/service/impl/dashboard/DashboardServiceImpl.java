package net.qixiaowei.operate.cloud.service.impl.dashboard;


import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveAnalysisDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveRateDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetLeaderboardDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetDecomposeMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeDetailsMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingMapper;
import net.qixiaowei.operate.cloud.service.dashboard.IDashboardService;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class DashboardServiceImpl implements IDashboardService {

    @Autowired
    TargetSettingMapper targetSettingMapper;

    @Autowired
    TargetOutcomeMapper targetOutcomeMapper;
    @Autowired
    RemoteIndicatorService indicatorService;

    @Autowired
    TargetOutcomeDetailsMapper targetOutcomeDetailsMapper;

    @Autowired
    TargetDecomposeMapper targetDecomposeMapper;

    /**
     * 关键经营指标目标达成率看板
     *
     * @param targetAchieveRateDTO dto
     * @return List
     */
    @Override
    public List<TargetAchieveRateDTO> targetAchieveRateList(TargetAchieveRateDTO targetAchieveRateDTO) {
        Integer targetYear = DateUtils.getYear();
        if (StringUtils.isNotNull(targetAchieveRateDTO.getTargetYear())) {
            targetYear = targetAchieveRateDTO.getTargetYear();
        }
        List<Long> indicatorRateIds = targetAchieveRateDTO.getIndicatorIds();
        TargetSetting targetSetting = new TargetSetting();
        targetSetting.setTargetYear(targetYear);
        List<TargetAchieveRateDTO> targetAchieveRateDTOS = new ArrayList<>();
        List<String> codesByIsPreset = IndicatorCode.getCodesByIsPreset(1);
        if (StringUtils.isEmpty(indicatorRateIds)) {
            List<IndicatorDTO> indicatorByCode = getIndicatorByCode(codesByIsPreset);
            setIndicatorNames(indicatorByCode);//添加唯一标识 避免后面copy出现重复数据问题
            Map<String, Object> params = new HashMap<>();
            List<Integer> targetSettingTypeList = new ArrayList<>();
            targetSettingTypeList.add(1);
            targetSettingTypeList.add(2);
            targetSettingTypeList.add(3);
            params.put("targetSettingTypeList", targetSettingTypeList);
            targetSetting.setParams(params);
            List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetAndOutSettingList(targetSetting);
            if (StringUtils.isEmpty(targetSettingDTOList)) {
                setNullDashboard(targetAchieveRateDTOS, codesByIsPreset);
            } else {
                List<Long> indicatorIds = targetSettingDTOList.stream().map(TargetSettingDTO::getIndicatorId).collect(Collectors.toList());
                for (IndicatorDTO indicatorDTO : indicatorByCode) {
                    for (TargetSettingDTO targetSettingDTO : targetSettingDTOList) {
                        if (targetSettingDTO.getIndicatorId().equals(indicatorDTO.getIndicatorId())) {
                            targetSettingDTO.setIndicatorCode(indicatorDTO.getIndicatorCode());
                            targetSettingDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                        }
                    }
                }
                for (IndicatorDTO indicatorDTO : indicatorByCode) {
                    if (!indicatorIds.contains(indicatorDTO.getIndicatorId())) {
                        TargetSettingDTO targetSettingDTO = new TargetSettingDTO();
                        targetSettingDTO.setIndicatorId(indicatorDTO.getIndicatorId());
                        targetSettingDTO.setIndicatorCode(indicatorDTO.getIndicatorCode());
                        targetSettingDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                        targetSettingDTO.setTargetValue(BigDecimal.ZERO);
                        targetSettingDTO.setActualTotal(BigDecimal.ZERO);
                        targetSettingDTOList.add(targetSettingDTO);
                    }
                }
                for (TargetSettingDTO targetSettingDTO : targetSettingDTOList) {
                    TargetAchieveRateDTO targetAchieveRate = new TargetAchieveRateDTO();
                    BeanUtils.copyProperties(targetSettingDTO, targetAchieveRate);
                    targetAchieveRateDTOS.add(targetAchieveRate);
                }
            }
        } else { // 传了指标之后
            targetSetting.setIndicatorIds(indicatorRateIds);
            List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetAndOutSettingList(targetSetting);
            List<IndicatorDTO> indicatorDTOS = getIndicator(indicatorRateIds);
            setIndicatorNames(indicatorDTOS);//
            List<Long> indicatorIds = targetSettingDTOList.stream().map(TargetSettingDTO::getIndicatorId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(targetSettingDTOList)) {
                for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                    for (TargetSettingDTO targetSettingDTO : targetSettingDTOList) {
                        if (targetSettingDTO.getIndicatorId().equals(indicatorDTO.getIndicatorId())) {
                            targetSettingDTO.setIndicatorCode(indicatorDTO.getIndicatorCode());
                            targetSettingDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                            break;
                        }
                    }
                }
            }
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                if (!indicatorIds.contains(indicatorDTO.getIndicatorId())) {
                    TargetSettingDTO targetSettingDTO = new TargetSettingDTO();
                    targetSettingDTO.setIndicatorId(indicatorDTO.getIndicatorId());
                    targetSettingDTO.setIndicatorCode(indicatorDTO.getIndicatorCode());
                    targetSettingDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                    targetSettingDTO.setTargetValue(BigDecimal.ZERO);
                    targetSettingDTO.setActualTotal(BigDecimal.ZERO);
                    targetSettingDTOList.add(targetSettingDTO);
                }
            }
            for (TargetSettingDTO targetSettingDTO : targetSettingDTOList) {
                TargetAchieveRateDTO targetAchieveRate = new TargetAchieveRateDTO();
                BeanUtils.copyProperties(targetSettingDTO, targetAchieveRate);
                targetAchieveRateDTOS.add(targetAchieveRate);
            }
        }
        for (TargetAchieveRateDTO achieveRateDTO : targetAchieveRateDTOS) {
            if (StringUtils.isNotNull(achieveRateDTO.getActualTotal()) && StringUtils.isNotNull(achieveRateDTO.getTargetValue())) {
                if (achieveRateDTO.getActualTotal().compareTo(BigDecimal.ZERO) == 0 || achieveRateDTO.getTargetValue().compareTo(BigDecimal.ZERO) == 0) {
                    achieveRateDTO.setRate(BigDecimal.ZERO);
                } else {
                    BigDecimal rate = achieveRateDTO.getActualTotal().divide(achieveRateDTO.getTargetValue(), 4, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
                    achieveRateDTO.setRate(rate);
                }
            } else {
                achieveRateDTO.setTargetValue(BigDecimal.ZERO);
                achieveRateDTO.setActualTotal(BigDecimal.ZERO);
                achieveRateDTO.setRate(BigDecimal.ZERO);
            }
        }
        if (StringUtils.isEmpty(targetAchieveRateDTOS)) {
            return new ArrayList<>();
        }
        return targetAchieveRateDTOS.stream().sorted(Comparator.comparingLong(TargetAchieveRateDTO::getIndicatorId)).collect(Collectors.toList());
    }

    /**
     * 添加唯一标识 避免后面copy出现重复数据问题
     *
     * @param indicatorByCode 通过Code获取的指标
     */
    private void setIndicatorNames(List<IndicatorDTO> indicatorByCode) {
        for (IndicatorDTO indicatorDTO : indicatorByCode) {
            if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.ORDER.getCode())) {
                indicatorDTO.setIndicatorName("订单额");
            } else if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.INCOME.getCode())) {
                indicatorDTO.setIndicatorName("收入");
            } else if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.RECEIVABLE.getCode())) {
                indicatorDTO.setIndicatorName("销售毛利润");
            }
        }
    }

    /**
     * 赋值空的仪表盘
     *
     * @param targetAchieveRateDTOS 仪表盘dto
     * @param codesByIsPreset       一级指标
     */
    private void setNullDashboard(List<TargetAchieveRateDTO> targetAchieveRateDTOS, List<String> codesByIsPreset) {
        if (StringUtils.isNotEmpty(codesByIsPreset)) {
            for (String code : codesByIsPreset) {
                TargetAchieveRateDTO targetAchieveRate = new TargetAchieveRateDTO();
                targetAchieveRate.setTargetValue(BigDecimal.ZERO);
                targetAchieveRate.setActualTotal(BigDecimal.ZERO);
                if (code.equals(IndicatorCode.ORDER.getCode())) {
                    targetAchieveRate.setIndicatorName("订单额");
                    targetAchieveRate.setIndicatorCode(IndicatorCode.ORDER.getCode());
                } else if (code.equals(IndicatorCode.INCOME.getCode())) {
                    targetAchieveRate.setIndicatorName("收入");
                    targetAchieveRate.setIndicatorCode(IndicatorCode.INCOME.getCode());
                } else if (code.equals(IndicatorCode.RECEIVABLE.getCode())) {
                    targetAchieveRate.setIndicatorName("销售毛利润");
                    targetAchieveRate.setIndicatorCode(IndicatorCode.RECEIVABLE.getCode());
                }
                targetAchieveRateDTOS.add(targetAchieveRate);
            }
        }
    }

    /**
     * 目标达成获取指标列表
     *
     * @return List
     */
    @Override
    public Map<String, Object> getDropList() {
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeIndicator();
        if (StringUtils.isEmpty(targetDecomposeDTOS)) {
            Map<String, Object> map = new HashMap<>();
            map.put("indicatorDropList", new ArrayList<>());//指标下拉
            map.put("decomposeDropList", new ArrayList<>());//分解维度下拉
            map.put("timeDropList", new ArrayList<>());//时间维度下拉
            return map;
        }
        Map<String, Object> map = new HashMap<>();
        Set<Long> indicatorIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).collect(Collectors.toSet());
        List<IndicatorDTO> indicatorDTOS = getIndicator(new ArrayList<>(indicatorIds));
        List<Map<String, Object>> indicatorList = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDTOS) {
            Map<String, Object> indicator = new HashMap<>();
            indicator.put("indicatorId", indicatorDTO.getIndicatorId());
            indicator.put("indicatorCode", indicatorDTO.getIndicatorCode());
            indicator.put("indicatorName", indicatorDTO.getIndicatorName());
            indicatorList.add(indicator);
        }
        //分解维度
        List<Map<String, Object>> targetDecomposeDimensionDTOList = new ArrayList<>();
        //根据属性去重
        List<TargetDecomposeDTO> targetDecomposeDistinct = targetDecomposeDTOS.stream().collect(Collectors.collectingAndThen(
                Collectors.toCollection(() -> new TreeSet<>(
                        Comparator.comparing(
                                TargetDecomposeDTO::getTargetDecomposeDimensionId))), ArrayList::new));
        for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDistinct) {
            Map<String, Object> targetDecomposeDimensionMap = new HashMap<>();
            targetDecomposeDimensionMap.put("targetDecomposeDimensionId", targetDecomposeDTO.getTargetDecomposeDimensionId());
            targetDecomposeDimensionMap.put("decompositionDimension", targetDecomposeDTO.getDecompositionDimension());
            targetDecomposeDimensionDTOList.add(targetDecomposeDimensionMap);
        }
        //时间维度
        List<Map<String, Object>> timeDimensionDTOS = new ArrayList<>();
        Set<Integer> timeDimensions = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getTimeDimension).collect(Collectors.toSet());
        for (Integer timeDimension : timeDimensions) {
            //时间维度:1年度;2半年度;3季度;4月度;5周
            switch (timeDimension) {
                case 1:
                    Map<String, Object> timeMap1 = new HashMap<>();
                    timeMap1.put("value", 1);
                    timeMap1.put("label", "年度");
                    timeDimensionDTOS.add(timeMap1);
                    break;
                case 2:
                    Map<String, Object> timeMap2 = new HashMap<>();
                    timeMap2.put("value", 2);
                    timeMap2.put("label", "半年度");
                    timeDimensionDTOS.add(timeMap2);
                    break;
                case 3:
                    Map<String, Object> timeMap3 = new HashMap<>();
                    timeMap3.put("value", 3);
                    timeMap3.put("label", "季度");
                    timeDimensionDTOS.add(timeMap3);
                    break;
                case 4:
                    Map<String, Object> timeMap4 = new HashMap<>();
                    timeMap4.put("value", 4);
                    timeMap4.put("label", "月度");
                    timeDimensionDTOS.add(timeMap4);
                    break;
                case 5:
                    Map<String, Object> timeMap5 = new HashMap<>();
                    timeMap5.put("value", 5);
                    timeMap5.put("label", "周");
                    timeDimensionDTOS.add(timeMap5);
                    break;
            }
        }
        map.put("indicatorDropList", indicatorList);//指标下拉
        map.put("decomposeDropList", targetDecomposeDimensionDTOList);//分解维度下拉
        map.put("timeDropList", timeDimensionDTOS);//时间维度下拉
        return map;
    }

    /**
     * 关键经营指标月度达成分析列表
     *
     * @return List
     */
    @Override
    public Map<String, Object> targetAchieveAnalysisList(TargetAchieveAnalysisDTO targetAchieveAnalysisDTO) {
        if (StringUtils.isNotNull(targetAchieveAnalysisDTO.getIndicatorId()) && StringUtils.isNotNull(targetAchieveAnalysisDTO.getTimeDimension())
                && StringUtils.isNotNull(targetAchieveAnalysisDTO.getTargetDecomposeDimensionId())) {
            Integer timeDimension = targetAchieveAnalysisDTO.getTimeDimension();
            Map<String, Object> params = new HashMap<>();
            params.put("targetYears", null);
            Integer cycleNumberStart;
            Integer cycleNumberEnd;
            if (StringUtils.isNull(targetAchieveAnalysisDTO.getCycleNumberStart()) || StringUtils.isNull(targetAchieveAnalysisDTO.getCycleNumberEnd())) {
                params.put("timeStart", null);
                params.put("timeEnd", null);
                cycleNumberStart = DateUtils.getYear();
                cycleNumberEnd = DateUtils.getYear();
            } else {
                cycleNumberStart = targetAchieveAnalysisDTO.getCycleNumberStart();
                cycleNumberEnd = targetAchieveAnalysisDTO.getCycleNumberEnd();
                if (timeDimension == 1) {
                    params.put("timeStart", null);
                    params.put("timeEnd", null);
                } else {
                    params.put("timeStart", cycleNumberStart);
                    params.put("timeEnd", cycleNumberEnd);
                }
                if (cycleNumberStart > cycleNumberEnd) {
                    throw new ServiceException("时间范围不规范 结束时间不可以小于开始时间");
                }
            }
            TargetDecomposeDTO targetDecomposeDTO = new TargetDecomposeDTO();
            if (timeDimension == 1) {// 只有年度
                List<Integer> targetYears = new ArrayList<>();
                for (int i = cycleNumberStart; i <= cycleNumberEnd; i++) {
                    targetYears.add(i);
                }
                params.put("targetYears", targetYears);
                targetDecomposeDTO.setParams(params);
                List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS = targetDecomposeMapper.selectAchieveAnalysisDecompose(targetDecomposeDTO);
                if (StringUtils.isEmpty(targetAchieveAnalysisDTOS)) {
                    return new HashMap<>();
                }
                Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis = targetAchieveAnalysisDTOS.stream()
                        .collect(Collectors.groupingBy(TargetAchieveAnalysisDTO::getTargetYear));
                return getAchieveAnalysisMap(timeDimension, groupByTargetAchieveAnalysis, targetYears);
            } else { // 没有年度
                Integer targetYear = targetAchieveAnalysisDTO.getTargetYear();
                targetDecomposeDTO.setTargetYear(targetYear);
                targetDecomposeDTO.setTimeDimension(timeDimension);
                targetDecomposeDTO.setIndicatorId(targetAchieveAnalysisDTO.getIndicatorId());
                targetDecomposeDTO.setTargetDecomposeDimensionId(targetAchieveAnalysisDTO.getTargetDecomposeDimensionId());
                targetDecomposeDTO.setParams(params);
                List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS = targetDecomposeMapper.selectAchieveAnalysisDecompose(targetDecomposeDTO);
                Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis = targetAchieveAnalysisDTOS.stream()
                        .collect(Collectors.groupingBy(TargetAchieveAnalysisDTO::getCycleNumber));
                if (StringUtils.isEmpty(targetAchieveAnalysisDTOS)) {
                    return new HashMap<>();
                }
                return getAchieveAnalysisMap(timeDimension, groupByTargetAchieveAnalysis, null);
            }
        }
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS = targetDecomposeMapper.selectRecentDecompose();
        if (StringUtils.isEmpty(targetAchieveAnalysisDTOS)) {
            return new HashMap<>();
        }
        int timeDimension2 = targetAchieveAnalysisDTOS.get(0).getTimeDimension();
        Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis;
        if (timeDimension2 == 1) {
            groupByTargetAchieveAnalysis = targetAchieveAnalysisDTOS.stream().collect(Collectors.groupingBy(TargetAchieveAnalysisDTO::getTargetYear));
        } else {
            groupByTargetAchieveAnalysis = targetAchieveAnalysisDTOS.stream().collect(Collectors.groupingBy(TargetAchieveAnalysisDTO::getCycleNumber));
        }
        return getAchieveAnalysisMap(timeDimension2, groupByTargetAchieveAnalysis, null);
    }

    /**
     * 查询关键经营指标排行榜列表
     *
     * @param targetLeaderboardDTO 关键经营指标排行榜
     * @return Map
     */
    @Override
    public Map<String, Object> targetLeaderboardList(TargetLeaderboardDTO targetLeaderboardDTO) {
        if (StringUtils.isNotNull(targetLeaderboardDTO.getIndicatorId()) && StringUtils.isNotNull(targetLeaderboardDTO.getTimeDimension())
                && StringUtils.isNotNull(targetLeaderboardDTO.getTargetDecomposeDimensionId())) {
            Integer timeDimension = targetLeaderboardDTO.getTimeDimension();
            Map<String, Object> params = new HashMap<>();
            params.put("targetYears", null);
            Integer cycleNumberStart;
            Integer cycleNumberEnd;
            if (StringUtils.isNull(targetLeaderboardDTO.getCycleNumberStart()) || StringUtils.isNull(targetLeaderboardDTO.getCycleNumberEnd())) {
                params.put("timeStart", null);
                params.put("timeEnd", null);
                cycleNumberStart = DateUtils.getYear();
                cycleNumberEnd = DateUtils.getYear();
            } else {
                cycleNumberStart = targetLeaderboardDTO.getCycleNumberStart();
                cycleNumberEnd = targetLeaderboardDTO.getCycleNumberEnd();
                if (timeDimension == 1) {
                    params.put("timeStart", null);
                    params.put("timeEnd", null);
                } else {
                    params.put("timeStart", cycleNumberStart);
                    params.put("timeEnd", cycleNumberEnd);
                }
                if (cycleNumberStart > cycleNumberEnd) {
                    throw new ServiceException("时间范围不规范 结束时间不可以小于开始时间");
                }
            }
            TargetDecomposeDTO targetDecomposeDTO = new TargetDecomposeDTO();
            if (timeDimension == 1) {// 只有年度
                List<Integer> targetYears = new ArrayList<>();
                for (int i = cycleNumberStart; i <= cycleNumberEnd; i++) {
                    targetYears.add(i);
                }
                params.put("targetYears", targetYears);
                targetDecomposeDTO.setParams(params);
                List<TargetLeaderboardDTO> targetLeaderboardDTOList = targetDecomposeMapper.selectLeaderboardDecompose(targetDecomposeDTO);
                if (StringUtils.isEmpty(targetLeaderboardDTOList)) {
                    return new HashMap<>();
                }
                Map<Long, List<TargetLeaderboardDTO>> groupByTargetLeaderboard = targetLeaderboardDTOList.stream()
                        .collect(Collectors.groupingBy(TargetLeaderboardDTO::getTargetDecomposeDimensionId));
                return getLeaderboardMap(timeDimension, groupByTargetLeaderboard, targetYears);
            } else { // 没有年度
                Integer targetYear = targetLeaderboardDTO.getTargetYear();
                targetDecomposeDTO.setTargetYear(targetYear);
                targetDecomposeDTO.setTimeDimension(timeDimension);
                targetDecomposeDTO.setIndicatorId(targetLeaderboardDTO.getIndicatorId());
                targetDecomposeDTO.setTargetDecomposeDimensionId(targetLeaderboardDTO.getTargetDecomposeDimensionId());
                targetDecomposeDTO.setParams(params);
                List<TargetLeaderboardDTO> targetLeaderboardDTOList = targetDecomposeMapper.selectLeaderboardDecompose(targetDecomposeDTO);
                Map<Long, List<TargetLeaderboardDTO>> groupByTargetLeaderboard = targetLeaderboardDTOList.stream()
                        .collect(Collectors.groupingBy(TargetLeaderboardDTO::getTargetDecomposeDimensionId));
                if (StringUtils.isEmpty(groupByTargetLeaderboard)) {
                    return new HashMap<>();
                }
                return getLeaderboardMap(timeDimension, groupByTargetLeaderboard, null);
            }
        }
        List<TargetLeaderboardDTO> targetLeaderboardDTOList = targetDecomposeMapper.selectRecentDecompose2();
        if (StringUtils.isEmpty(targetLeaderboardDTOList)) {
            return new HashMap<>();
        }
        int timeDimension2 = targetLeaderboardDTOList.get(0).getTimeDimension();
        Map<Long, List<TargetLeaderboardDTO>> groupByTargetLeaderboard
                = targetLeaderboardDTOList.stream().collect(Collectors.groupingBy(TargetLeaderboardDTO::getTargetDecomposeDimensionId));
        return getLeaderboardMap(timeDimension2, groupByTargetLeaderboard, null);
    }

    /**
     * @param timeDimension            时间维度
     * @param groupByTargetLeaderboard 分组后的排行榜
     * @return Map
     */
    private Map<String, Object> getLeaderboardMap(Integer timeDimension, Map<Long, List<TargetLeaderboardDTO>> groupByTargetLeaderboard, List<Integer> targetYears) {
//        groupByTargetLeaderboard


        return null;
    }

    /**
     * 关键经营指标排行榜下拉列表
     *
     * @return Map
     */
    @Override
    public Map<String, Object> targetLeaderboardDropList() {

        return null;
    }

    /**
     * List<Map>-→Map<String,List>
     *
     * @param timeDimension                时间维度
     * @param groupByTargetAchieveAnalysis 分组后的仪表盘
     * @param targetYears                  目标年度List
     * @return Map
     */
    private Map<String, Object> getAchieveAnalysisMap(Integer timeDimension, Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis, List<Integer> targetYears) {
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOList;
        if (timeDimension == 1) {
            targetAchieveAnalysisDTOList = this.getTargetYearAchieveAnalysisDTOS(groupByTargetAchieveAnalysis, targetYears);
        } else {
            targetAchieveAnalysisDTOList = this.getTargetCycleNumberAchieveAnalysisDTOS(timeDimension, groupByTargetAchieveAnalysis);
        }
        // 排序
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDORITOSRevers =
                targetAchieveAnalysisDTOList.stream().sorted(Comparator.comparingInt(TargetAchieveAnalysisDTO::getCycleNumber)).collect(Collectors.toList());
        Map<String, Object> hashMap = new HashMap<>();
        hashMap.put("forecast", targetAchieveAnalysisDORITOSRevers.stream().map(TargetAchieveAnalysisDTO::getCycleForecastSum).collect(Collectors.toList()));
        hashMap.put("cycleTarget", targetAchieveAnalysisDORITOSRevers.stream().map(TargetAchieveAnalysisDTO::getCycleTargetSum).collect(Collectors.toList()));
        hashMap.put("cycleActual", targetAchieveAnalysisDORITOSRevers.stream().map(TargetAchieveAnalysisDTO::getCycleActualSum).collect(Collectors.toList()));
        hashMap.put("completionRate", targetAchieveAnalysisDORITOSRevers.stream().map(TargetAchieveAnalysisDTO::getCompletionRate).collect(Collectors.toList()));
        hashMap.put("deviationRate", targetAchieveAnalysisDORITOSRevers.stream().map(TargetAchieveAnalysisDTO::getDeviationRate).collect(Collectors.toList()));
        hashMap.put("cycleNumberName", targetAchieveAnalysisDORITOSRevers.stream().map(TargetAchieveAnalysisDTO::getCycleNumberName).collect(Collectors.toList()));
        return hashMap;
    }

    /**
     * 计算年度时间维度的分解为度
     *
     * @param groupByTargetAchieveAnalysis 分组后的数据
     * @param targetYears                  目标年度List
     * @return List
     */
    private List<TargetAchieveAnalysisDTO> getTargetYearAchieveAnalysisDTOS(Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis, List<Integer> targetYears) {
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOList = new ArrayList<>();
        if (StringUtils.isNotNull(targetYears)) {// 填补孔雀的年份
            for (Integer targetYear : targetYears) {
                if (!groupByTargetAchieveAnalysis.containsKey(targetYear)) {
                    List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS = new ArrayList<>();
                    TargetAchieveAnalysisDTO targetAchieveAnalysis = new TargetAchieveAnalysisDTO();
                    targetAchieveAnalysis.setCycleNumber(targetYear);
                    targetAchieveAnalysis.setCycleTarget(new BigDecimal(0));
                    targetAchieveAnalysis.setCycleForecast(new BigDecimal(0));
                    targetAchieveAnalysis.setCycleActual(new BigDecimal(0));
                    targetAchieveAnalysisDTOS.add(targetAchieveAnalysis);
                    groupByTargetAchieveAnalysis.put(targetYear, targetAchieveAnalysisDTOS);
                }
            }
        }
        for (Integer targetYear : groupByTargetAchieveAnalysis.keySet()) {
            List<TargetAchieveAnalysisDTO> smallTargetAchieveAnalysisDTOS = groupByTargetAchieveAnalysis.get(targetYear);
            TargetAchieveAnalysisDTO targetAchieveAnalysis = new TargetAchieveAnalysisDTO();
            targetAchieveAnalysis.setCycleNumberName(targetYear + "年度");
            BigDecimal cycleTargetSum = new BigDecimal(0);
            BigDecimal cycleForecastSum = new BigDecimal(0);
            BigDecimal cycleActualSum = new BigDecimal(0);
            for (TargetAchieveAnalysisDTO smallTargetAchieveAnalysisDTO : smallTargetAchieveAnalysisDTOS) {
                cycleTargetSum = cycleTargetSum.add(smallTargetAchieveAnalysisDTO.getCycleTarget());
                cycleForecastSum = cycleForecastSum.add(smallTargetAchieveAnalysisDTO.getCycleForecast());
                cycleActualSum = cycleActualSum.add(smallTargetAchieveAnalysisDTO.getCycleActual());
            }
            BigDecimal completionRate = BigDecimal.ZERO;
            BigDecimal deviationRate = BigDecimal.ZERO;
            //公式=实际值合计/目标值合计。
            if (cycleTargetSum.compareTo(BigDecimal.ZERO) != 0) {
                completionRate = cycleActualSum.multiply(new BigDecimal(100)).divide(cycleTargetSum, 2, RoundingMode.HALF_UP);
            }
            //公式=（实际值合计-预测值合计）/实际值合计。
            if (cycleActualSum.compareTo(BigDecimal.ZERO) != 0) {
                deviationRate = (cycleActualSum.subtract(cycleForecastSum)).multiply(new BigDecimal(100)).divide(cycleActualSum, 2, RoundingMode.HALF_UP);
            }
            targetAchieveAnalysis.setCycleNumber(targetYear);
            targetAchieveAnalysis.setCycleTargetSum(cycleTargetSum);
            targetAchieveAnalysis.setCycleForecastSum(cycleForecastSum);
            targetAchieveAnalysis.setCycleActualSum(cycleActualSum);
            targetAchieveAnalysis.setCompletionRate(completionRate);
            targetAchieveAnalysis.setDeviationRate(deviationRate);
            targetAchieveAnalysisDTOList.add(targetAchieveAnalysis);
        }
        return targetAchieveAnalysisDTOList;
    }

    /**
     * 计算半年度、季度、月度、周等时间维度的分解为度
     *
     * @param timeDimension                时间维度
     * @param groupByTargetAchieveAnalysis 分组后的
     */
    private List<TargetAchieveAnalysisDTO> getTargetCycleNumberAchieveAnalysisDTOS(Integer timeDimension, Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis) {
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOList = new ArrayList<>();
        for (Integer cycleNumber : groupByTargetAchieveAnalysis.keySet()) {
            List<TargetAchieveAnalysisDTO> smallTargetAchieveAnalysisDTOS = groupByTargetAchieveAnalysis.get(cycleNumber);
            TargetAchieveAnalysisDTO targetAchieveAnalysis = new TargetAchieveAnalysisDTO();
            switch (timeDimension) {
                case 1:
                    targetAchieveAnalysis.setCycleNumberName(smallTargetAchieveAnalysisDTOS.get(0).getTargetYear() + "年度");
                    break;
                case 2:
                    if (cycleNumber == 1) {
                        targetAchieveAnalysis.setCycleNumberName("上半年");
                    } else {
                        targetAchieveAnalysis.setCycleNumberName("下半年");
                    }
                    break;
                case 3:
                    targetAchieveAnalysis.setCycleNumberName("第" + cycleNumber + "季度");
                    break;
                case 4:
                    targetAchieveAnalysis.setCycleNumberName("第" + cycleNumber + "月");
                    break;
                case 5:
                    targetAchieveAnalysis.setCycleNumberName("第" + cycleNumber + "周");
                    break;
            }
            BigDecimal cycleTargetSum = new BigDecimal(0);
            BigDecimal cycleForecastSum = new BigDecimal(0);
            BigDecimal cycleActualSum = new BigDecimal(0);
            for (TargetAchieveAnalysisDTO smallTargetAchieveAnalysisDTO : smallTargetAchieveAnalysisDTOS) {
                cycleTargetSum = cycleTargetSum.add(smallTargetAchieveAnalysisDTO.getCycleTarget());
                cycleForecastSum = cycleForecastSum.add(smallTargetAchieveAnalysisDTO.getCycleForecast());
                cycleActualSum = cycleActualSum.add(smallTargetAchieveAnalysisDTO.getCycleActual());
            }
            BigDecimal completionRate = BigDecimal.ZERO;
            BigDecimal deviationRate = BigDecimal.ZERO;
            //公式=实际值合计/目标值合计。
            if (cycleTargetSum.compareTo(BigDecimal.ZERO) != 0) {
                completionRate = cycleActualSum.divide(cycleTargetSum, 2, RoundingMode.HALF_UP);
            }
            //公式=（实际值合计-预测值合计）/实际值合计。
            if (cycleActualSum.compareTo(BigDecimal.ZERO) != 0) {
                deviationRate = (cycleActualSum.subtract(cycleForecastSum)).divide(cycleActualSum, 2, RoundingMode.HALF_UP);
            }
            targetAchieveAnalysis.setCycleNumber(cycleNumber);
            targetAchieveAnalysis.setCycleTargetSum(cycleTargetSum);
            targetAchieveAnalysis.setCycleForecastSum(cycleForecastSum);
            targetAchieveAnalysis.setCycleActualSum(cycleActualSum);
            targetAchieveAnalysis.setCompletionRate(completionRate);
            targetAchieveAnalysis.setDeviationRate(deviationRate);
            targetAchieveAnalysisDTOList.add(targetAchieveAnalysis);
        }
        return targetAchieveAnalysisDTOList;
    }

    /**
     * 远程获取指标数据
     *
     * @param indicatorIds 指标ID集合
     */
    private List<IndicatorDTO> getIndicator(List<Long> indicatorIds) {
        if (StringUtils.isEmpty(indicatorIds)) {
            throw new ServiceException("目标制定数据异常 请联系管理员");
        }
        R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
        if (indicatorR.getCode() != 200) {
            throw new ServiceException("远程指标失败 请联系管理员");
        }
        return indicatorR.getData();
    }

    /**
     * 远程获取指标数据根据编码
     *
     * @param indicators 指标ID集合
     */
    private List<IndicatorDTO> getIndicatorByCode(List<String> indicators) {
        if (StringUtils.isEmpty(indicators)) {
            throw new ServiceException("目标制定数据异常 请联系管理员");
        }
        R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByCodeList(indicators, SecurityConstants.INNER);
        if (indicatorR.getCode() != 200) {
            throw new ServiceException("远程指标失败 请联系管理员");
        }
        return indicatorR.getData();
    }
}
