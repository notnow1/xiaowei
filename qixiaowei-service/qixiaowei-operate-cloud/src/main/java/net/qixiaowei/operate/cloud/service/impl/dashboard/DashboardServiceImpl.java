package net.qixiaowei.operate.cloud.service.impl.dashboard;


import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveAnalysisDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetAchieveRateDTO;
import net.qixiaowei.operate.cloud.api.dto.dashboard.TargetLeaderboardDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetDecomposeMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingMapper;
import net.qixiaowei.operate.cloud.service.dashboard.IDashboardService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.system.RemoteRegionService;
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
    RemoteIndicatorService indicatorService;
    @Autowired
    TargetDecomposeMapper targetDecomposeMapper;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteIndustryService remoteIndustryService;
    @Autowired
    private RemoteRegionService remoteRegionService;

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
            noIndicatorIds(targetSetting, targetAchieveRateDTOS, codesByIsPreset, targetYear);
        } else { // 传了指标之后
            exitIndicatorIds(indicatorRateIds, targetSetting, targetAchieveRateDTOS, targetYear);
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
        List<TargetAchieveRateDTO> targetAchieveRateDTOS1 = new ArrayList<>();
        targetAchieveRateDTOS1.add(targetAchieveRateDTOS.stream().filter(targetAchieveRateDTO2
                -> StringUtils.equals(targetAchieveRateDTO2.getIndicatorCode(), IndicatorCode.ORDER.getCode())).collect(Collectors.toList()).get(0));
        targetAchieveRateDTOS1.add(targetAchieveRateDTOS.stream().filter(targetAchieveRateDTO2
                -> StringUtils.equals(targetAchieveRateDTO2.getIndicatorCode(), IndicatorCode.INCOME.getCode())).collect(Collectors.toList()).get(0));
        targetAchieveRateDTOS1.add(targetAchieveRateDTOS.stream().filter(targetAchieveRateDTO2
                -> StringUtils.equals(targetAchieveRateDTO2.getIndicatorCode(), IndicatorCode.RECEIVABLE.getCode())).collect(Collectors.toList()).get(0));
        targetAchieveRateDTOS.removeIf(s -> codesByIsPreset.contains(s.getIndicatorCode()));
        targetAchieveRateDTOS1.addAll(targetAchieveRateDTOS);
        // 排序sort
        List<TargetAchieveRateDTO> achieveRateDTOS = new ArrayList<>();
        if (StringUtils.isEmpty(indicatorRateIds)) {
            return targetAchieveRateDTOS1;
        }
        for (Long indicatorRateId : indicatorRateIds) {
            for (TargetAchieveRateDTO achieveRateDTO : targetAchieveRateDTOS1) {
                if (indicatorRateId.equals(achieveRateDTO.getIndicatorId())) {
                    TargetAchieveRateDTO rateDTO = new TargetAchieveRateDTO();
                    BeanUtils.copyProperties(achieveRateDTO, rateDTO);
                    achieveRateDTOS.add(rateDTO);
                    break;
                }
            }
        }
        return achieveRateDTOS;
    }

    /**
     * 存在指标ID集合的情况
     *
     * @param indicatorRateIds      指标ID集合
     * @param targetSetting         目标制定
     * @param targetAchieveRateDTOS 目标达成看板
     * @param targetYear            年份
     */
    private void exitIndicatorIds(List<Long> indicatorRateIds, TargetSetting targetSetting, List<TargetAchieveRateDTO> targetAchieveRateDTOS, Integer targetYear) {
        targetSetting.setIndicatorIds(indicatorRateIds);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetAndOutSettingList(targetSetting);
        List<IndicatorDTO> indicatorDTOS = getIndicator(indicatorRateIds);
        setIndicatorNames(indicatorDTOS, targetYear);
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

    /**
     * 没有指标集合的情况
     *
     * @param targetSetting         目标制定
     * @param targetAchieveRateDTOS 目标达成看板
     * @param codesByIsPreset       指标编码集合
     * @param targetYear
     */
    private void noIndicatorIds(TargetSetting targetSetting, List<TargetAchieveRateDTO> targetAchieveRateDTOS, List<String> codesByIsPreset, Integer targetYear) {
        List<IndicatorDTO> indicatorByCode = getIndicatorByCode(codesByIsPreset);
        setIndicatorNames(indicatorByCode, targetYear);//添加唯一标识 避免后面copy出现重复数据问题
        Map<String, Object> params = new HashMap<>();
        List<Integer> targetSettingTypeList = new ArrayList<>();
        targetSettingTypeList.add(1);
        targetSettingTypeList.add(2);
        targetSettingTypeList.add(3);
        params.put("targetSettingTypeList", targetSettingTypeList);
        targetSetting.setParams(params);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetAndOutSettingList(targetSetting);
        if (StringUtils.isEmpty(targetSettingDTOList)) {
            setNullDashboard(targetAchieveRateDTOS, indicatorByCode);
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
    }

    /**
     * 添加唯一标识 避免后面copy出现重复数据问题
     *
     * @param indicatorByCode 通过Code获取的指标
     */
    private void setIndicatorNames(List<IndicatorDTO> indicatorByCode, Integer targetYear) {
        for (IndicatorDTO indicatorDTO : indicatorByCode) {
            if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.ORDER.getCode())) {
                indicatorDTO.setIndicatorName("订单（不含税）");
            } else if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.INCOME.getCode())) {
                indicatorDTO.setIndicatorName("销售收入");
            } else if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.RECEIVABLE.getCode())) {
                indicatorDTO.setIndicatorName("回款金额（含税）");
            }
        }
    }

    /**
     * 赋值空的仪表盘
     *
     * @param targetAchieveRateDTOS 仪表盘dto
     * @param indicatorByCode       指标DTO
     */
    private void setNullDashboard(List<TargetAchieveRateDTO> targetAchieveRateDTOS, List<IndicatorDTO> indicatorByCode) {
        if (StringUtils.isNotEmpty(indicatorByCode)) {
            for (IndicatorDTO indicatorDTO : indicatorByCode) {
                TargetAchieveRateDTO targetAchieveRate = new TargetAchieveRateDTO();
                targetAchieveRate.setTargetValue(BigDecimal.ZERO);
                targetAchieveRate.setActualTotal(BigDecimal.ZERO);
                if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.ORDER.getCode())) {
                    targetAchieveRate.setIndicatorName("订单（不含税）");
                    targetAchieveRate.setIndicatorId(indicatorDTO.getIndicatorId());
                    targetAchieveRate.setIndicatorCode(IndicatorCode.ORDER.getCode());
                } else if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.INCOME.getCode())) {
                    targetAchieveRate.setIndicatorName("销售收入");
                    targetAchieveRate.setIndicatorId(indicatorDTO.getIndicatorId());
                    targetAchieveRate.setIndicatorCode(IndicatorCode.INCOME.getCode());
                } else if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.RECEIVABLE.getCode())) {
                    targetAchieveRate.setIndicatorName("回款金额（含税）");
                    targetAchieveRate.setIndicatorId(indicatorDTO.getIndicatorId());
                    targetAchieveRate.setIndicatorCode(IndicatorCode.RECEIVABLE.getCode());
                }
                targetAchieveRateDTOS.add(targetAchieveRate);
            }
        }
    }

    /**
     * 关键经营指标月度达成分析列表
     *
     * @return List
     */
    @Override
    public Map<String, Object> targetAchieveAnalysisList(TargetAchieveAnalysisDTO targetAchieveAnalysisDTO) {
        if (StringUtils.isNull(targetAchieveAnalysisDTO.getIndicatorId()) || StringUtils.isNull(targetAchieveAnalysisDTO.getTimeDimension())
                || StringUtils.isNull(targetAchieveAnalysisDTO.getTargetDecomposeDimensionId())) {
            List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS = targetDecomposeMapper.selectRecentDecompose();
            if (StringUtils.isEmpty(targetAchieveAnalysisDTOS)) {
                return new HashMap<>();
            }
            int timeDimension2 = targetAchieveAnalysisDTOS.get(0).getTimeDimension();
            Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis;
            groupByTargetAchieveAnalysis = targetAchieveAnalysisDTOS.stream().collect(Collectors.groupingBy(TargetAchieveAnalysisDTO::getTargetYear));
            return getAchieveAnalysisMap(timeDimension2, groupByTargetAchieveAnalysis, null);
        }
        Integer timeDimension = targetAchieveAnalysisDTO.getTimeDimension();
        Map<String, Object> params = new HashMap<>();
        params.put("targetYears", null);
        Integer startYear = DateUtils.getYear();
        Integer endYear = DateUtils.getYear();
        Integer cycleNumberStart = 1;
        Integer cycleNumberEnd = 1;
        int endCycleNumber = 2;
        if (timeDimension == 2) {
            cycleNumberEnd = 2;
        } else if (timeDimension == 3) {
            cycleNumberEnd = 4;
        } else if (timeDimension == 4) {
            cycleNumberEnd = 12;
        } else if (timeDimension == 5) {
            cycleNumberEnd = 52;
        }
        if (timeDimension == 1 && StringUtils.isNotNull(targetAchieveAnalysisDTO.getStartYear()) && StringUtils.isNotNull(targetAchieveAnalysisDTO.getEndYear())) {// 只有年度
            startYear = targetAchieveAnalysisDTO.getStartYear();
            endYear = targetAchieveAnalysisDTO.getEndYear();
        } else if (timeDimension != 1 && StringUtils.isNotNull(targetAchieveAnalysisDTO.getCycleNumberStart()) || StringUtils.isNotNull(targetAchieveAnalysisDTO.getCycleNumberEnd())
                || StringUtils.isNotNull(targetAchieveAnalysisDTO.getStartYear()) || StringUtils.isNotNull(targetAchieveAnalysisDTO.getEndYear())) {
            startYear = targetAchieveAnalysisDTO.getStartYear();
            endYear = targetAchieveAnalysisDTO.getEndYear();
            cycleNumberStart = targetAchieveAnalysisDTO.getCycleNumberStart();
            cycleNumberEnd = targetAchieveAnalysisDTO.getCycleNumberEnd();
        } else {
            params.put("timeStart", cycleNumberStart);
            params.put("timeEnd", cycleNumberEnd);
        }
        checkValue(startYear, endYear, cycleNumberStart, cycleNumberEnd);
        if (timeDimension == 3) {
            endCycleNumber = 4;
        } else if (timeDimension == 4) {
            endCycleNumber = 12;
        } else if (timeDimension == 5) {
            endCycleNumber = 52;
        }
        List<Integer> targetYears = new ArrayList<>();
        for (int i = startYear; i <= endYear; i++) {
            targetYears.add(i);
        }
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS = new ArrayList<>();
        if (timeDimension == 1) {// 只有年度
            TargetDecomposeDTO targetDecomposeDTO = createTargetDecompose(targetAchieveAnalysisDTO, timeDimension);
            params.put("targetYears", targetYears);
            targetDecomposeDTO.setParams(params);
            targetAchieveAnalysisDTOS.addAll(targetDecomposeMapper.selectAchieveAnalysisDecompose(targetDecomposeDTO));
            List<Integer> targetYears1 = targetAchieveAnalysisDTOS.stream().map(TargetAchieveAnalysisDTO::getTargetYear).collect(Collectors.toList());
            for (Integer targetYear : targetYears) {
                if (!targetYears1.contains(targetYear)) {
                    TargetAchieveAnalysisDTO targetAchieveAnalysisDTO1 = new TargetAchieveAnalysisDTO();
                    targetAchieveAnalysisDTO1.setTargetYear(targetYear);
                    targetAchieveAnalysisDTO1.setCycleTargetSum(BigDecimal.ZERO);
                    targetAchieveAnalysisDTO1.setCycleActualSum(BigDecimal.ZERO);
                    targetAchieveAnalysisDTO1.setCycleTarget(BigDecimal.ZERO);
                    targetAchieveAnalysisDTO1.setCycleTarget(BigDecimal.ZERO);
                    targetAchieveAnalysisDTO1.setCycleTarget(BigDecimal.ZERO);
                    targetAchieveAnalysisDTOS.add(targetAchieveAnalysisDTO1);
                }
            }
            Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis = targetAchieveAnalysisDTOS.stream()
                    .collect(Collectors.groupingBy(TargetAchieveAnalysisDTO::getTargetYear));
            return getAchieveAnalysisMap(timeDimension, groupByTargetAchieveAnalysis, targetYears);
        }
        if (endYear - startYear == 0) {
            TargetDecomposeDTO targetDecomposeDTO = createTargetDecompose(targetAchieveAnalysisDTO, timeDimension);
            targetDecomposeDTO.setTargetYear(endYear);
            params.put("timeStart", cycleNumberStart);
            params.put("timeEnd", cycleNumberEnd);
            targetDecomposeDTO.setParams(params);
            targetAchieveAnalysisDTOS = targetDecomposeMapper.selectAchieveAnalysisDecompose(targetDecomposeDTO);
        } else if (endYear - startYear == 1) {
            targetAchieveAnalysisDTOS = addTargetAchieveAnalysisDTOList(params, startYear, endYear, cycleNumberStart, cycleNumberEnd, endCycleNumber, targetAchieveAnalysisDTOS, targetAchieveAnalysisDTO);
        } else {
            List<Integer> queryYears = new ArrayList<>();
            targetAchieveAnalysisDTOS = addTargetAchieveAnalysisDTOList(params, startYear, endYear, cycleNumberStart, cycleNumberEnd, endCycleNumber, targetAchieveAnalysisDTOS, targetAchieveAnalysisDTO);
            for (int i = startYear + 1; i <= endYear - 1; i++) {
                queryYears.add(i);
            }
            TargetDecomposeDTO targetDecomposeDTO = createTargetDecompose(targetAchieveAnalysisDTO, timeDimension);
            params.put("targetYears", queryYears);
            targetDecomposeDTO.setParams(params);
            targetAchieveAnalysisDTOS.addAll(targetDecomposeMapper.selectAchieveAnalysisDecompose(targetDecomposeDTO));
        }
        if (StringUtils.isEmpty(targetAchieveAnalysisDTOS)) {
            return setMapZero(timeDimension, startYear, endYear, cycleNumberStart, cycleNumberEnd);
        }
        Map<Integer, List<TargetAchieveAnalysisDTO>> groupTargetAchieveAnalysisDTOS = targetAchieveAnalysisDTOS.stream().collect(Collectors.groupingBy(TargetAchieveAnalysisDTO::getTargetYear));
        List<Integer> targetYears1 = new ArrayList<>(groupTargetAchieveAnalysisDTOS.keySet());
        for (Integer targetYear : targetYears) {
            List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS1;
            targetAchieveAnalysisDTOS1 = getTargetAchieveAnalysisDTOS(startYear, endYear, cycleNumberStart, cycleNumberEnd, endCycleNumber, groupTargetAchieveAnalysisDTOS, targetYears1, targetYear);
            groupTargetAchieveAnalysisDTOS.put(targetYear, targetAchieveAnalysisDTOS1);
        }
        return getAchieveAnalysisMap(timeDimension, new TreeMap<>(groupTargetAchieveAnalysisDTOS), null);
    }

    /**
     * 给空Map赋0
     *
     * @param timeDimension    时间维度
     * @param startYear        开始年份
     * @param endYear          结束年份
     * @param cycleNumberStart 开始周期
     * @param cycleNumberEnd   结束周期
     * @return Map
     */
    private Map<String, Object> setMapZero(Integer timeDimension, Integer startYear, Integer endYear, Integer cycleNumberStart, Integer cycleNumberEnd) {
        int endCycleNumber = 1;
        switch (timeDimension) {
            case 2:
                endCycleNumber = 2;
                break;
            case 3:
                endCycleNumber = 4;
                break;
            case 4:
                endCycleNumber = 12;
                break;
            case 5:
                endCycleNumber = 52;
                break;
        }
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOArrayList = new ArrayList<>();
        for (int targetYear = startYear; targetYear <= endYear; targetYear++) {
            if (startYear.equals(endYear)) {
                for (int i = cycleNumberStart; i <= cycleNumberEnd; i++) {
                    setAnalysisZeroValue(timeDimension, targetAchieveAnalysisDTOArrayList, targetYear, i);
                }
            } else if (endYear - startYear == 1) {
                if (targetYear == startYear) {
                    for (int i = cycleNumberStart; i <= endCycleNumber; i++) {
                        setAnalysisZeroValue(timeDimension, targetAchieveAnalysisDTOArrayList, targetYear, i);
                    }
                }
                if (targetYear == endYear) {
                    for (int i = 1; i <= cycleNumberEnd; i++) {
                        setAnalysisZeroValue(timeDimension, targetAchieveAnalysisDTOArrayList, targetYear, i);
                    }
                }
            } else if (endYear - startYear > 1) {
                if (targetYear == startYear) {
                    for (int i = cycleNumberStart; i <= endCycleNumber; i++) {
                        setAnalysisZeroValue(timeDimension, targetAchieveAnalysisDTOArrayList, targetYear, i);
                    }
                } else if (targetYear == endYear) {
                    for (int i = 1; i <= cycleNumberEnd; i++) {
                        setAnalysisZeroValue(timeDimension, targetAchieveAnalysisDTOArrayList, targetYear, i);
                    }
                } else {
                    for (int i = 1; i <= endCycleNumber; i++) {
                        setAnalysisZeroValue(timeDimension, targetAchieveAnalysisDTOArrayList, targetYear, i);
                    }
                }
            }
        }
//            for (int i = startYear; i <= endYear; i++) {
//                for (int j = 1; j <= endCycleNumber; j++) {
//                    setAnalysisZeroValue(timeDimension, targetAchieveAnalysisDTOArrayList, i, j);
//                }
//            }
        Map<String, Object> hashMap = new HashMap<>();
        listToMap(targetAchieveAnalysisDTOArrayList, hashMap);
        return hashMap;
    }

    /**
     * 给第二个仪表盘空的情况赋值0
     *
     * @param timeDimension                     时间维度
     * @param targetAchieveAnalysisDTOArrayList 目标列表
     * @param targetYear                        年份
     * @param cycleNumber                       周期
     */
    private void setAnalysisZeroValue(Integer
                                              timeDimension, List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOArrayList, int targetYear,
                                      int cycleNumber) {
        TargetAchieveAnalysisDTO targetAchieveAnalysisDTO1 = new TargetAchieveAnalysisDTO();
        targetAchieveAnalysisDTO1.setCycleForecastSum(BigDecimal.ZERO);
        targetAchieveAnalysisDTO1.setCycleTargetSum(BigDecimal.ZERO);
        targetAchieveAnalysisDTO1.setCycleActualSum(BigDecimal.ZERO);
        targetAchieveAnalysisDTO1.setCompletionRate(BigDecimal.ZERO);
        targetAchieveAnalysisDTO1.setDeviationRate(BigDecimal.ZERO);
        setTimeValue(timeDimension, targetYear, cycleNumber, targetAchieveAnalysisDTO1);
        targetAchieveAnalysisDTOArrayList.add(targetAchieveAnalysisDTO1);
    }

    /**
     * 校验
     *
     * @param startYear        开始年份
     * @param endYear          结束年份
     * @param cycleNumberStart 开始周期
     * @param cycleNumberEnd   结束周期
     */
    private void checkValue(Integer startYear, Integer endYear, Integer cycleNumberStart, Integer cycleNumberEnd) {
        if (startYear > endYear) {
            throw new ServiceException("时间范围不规范 结束时间不可以小于开始时间");
        }
        if (startYear.equals(endYear) && cycleNumberStart > cycleNumberEnd) {
            throw new ServiceException("时间范围不规范 结束时间不可以小于开始时间");
        }
    }

    /**
     * 赋空对象
     *
     * @param startYear                      开始年
     * @param endYear                        结束年
     * @param cycleNumberStart               开始周期
     * @param cycleNumberEnd                 结束周期
     * @param endCycleNumber                 滚动周期
     * @param groupTargetAchieveAnalysisDTOS 分组后的DTO
     * @param targetYears1                   年份LIST
     * @param targetYear                     年份
     * @return list
     */
    private List<TargetAchieveAnalysisDTO> getTargetAchieveAnalysisDTOS(Integer startYear, Integer endYear, Integer
            cycleNumberStart, Integer cycleNumberEnd,
                                                                        int endCycleNumber, Map<Integer, List<TargetAchieveAnalysisDTO>> groupTargetAchieveAnalysisDTOS,
                                                                        List<Integer> targetYears1, Integer targetYear) {
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS1;
        if (!targetYears1.contains(targetYear)) {
            targetAchieveAnalysisDTOS1 = new ArrayList<>();
            if (startYear.equals(endYear)) {
                for (int i = cycleNumberStart; i <= cycleNumberEnd; i++) {
                    setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                }
            } else if (endYear - startYear == 1) {
                if (targetYear.equals(startYear)) {
                    for (int i = cycleNumberStart; i <= endCycleNumber; i++) {
                        setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                    }
                }
                if (targetYear.equals(endYear)) {
                    for (int i = 1; i <= cycleNumberEnd; i++) {
                        setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                    }
                }
            } else if (endYear - startYear > 1) {
                if (targetYear.equals(startYear)) {
                    for (int i = cycleNumberStart; i <= endCycleNumber; i++) {
                        setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                    }
                } else if (targetYear.equals(endYear)) {
                    for (int i = 1; i <= cycleNumberEnd; i++) {
                        setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                    }
                } else {
                    for (int i = 1; i <= endCycleNumber; i++) {
                        setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                    }
                }
            }
        } else {
            targetAchieveAnalysisDTOS1 = groupTargetAchieveAnalysisDTOS.get(targetYear);
            List<Integer> cycleNumbers = targetAchieveAnalysisDTOS1.stream().map(TargetAchieveAnalysisDTO::getCycleNumber).collect(Collectors.toList());
            if (startYear.equals(endYear)) {
                for (int i = cycleNumberStart; i <= cycleNumberEnd; i++) {
                    if (!cycleNumbers.contains(i)) {
                        setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                    }
                }
            } else if (endYear - startYear == 1) {
                if (targetYear.equals(startYear)) {
                    for (int i = cycleNumberStart; i <= endCycleNumber; i++) {
                        if (!cycleNumbers.contains(i)) {
                            setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                        }
                    }
                }
                if (targetYear.equals(endYear)) {
                    for (int i = 1; i <= cycleNumberEnd; i++) {
                        if (!cycleNumbers.contains(i)) {
                            setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                        }
                    }
                }
            } else if (endYear - startYear > 1) {
                if (targetYear.equals(startYear)) {
                    for (int i = cycleNumberStart; i <= endCycleNumber; i++) {
                        if (!cycleNumbers.contains(i)) {
                            setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                        }
                    }
                } else if (targetYear.equals(endYear)) {
                    for (int i = 1; i <= cycleNumberEnd; i++) {
                        if (!cycleNumbers.contains(i)) {
                            setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                        }
                    }
                } else {
                    for (int i = 1; i <= endCycleNumber; i++) {
                        if (!cycleNumbers.contains(i)) {
                            setTargetAchieveAnalysisZero(targetYear, targetAchieveAnalysisDTOS1, i);
                        }
                    }
                }
            }
        }
        return targetAchieveAnalysisDTOS1;
    }

    private void setTargetAchieveAnalysisZero(Integer
                                                      targetYear, List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS1, int i) {
        TargetAchieveAnalysisDTO targetAchieveAnalysisDTO1 = new TargetAchieveAnalysisDTO();
        targetAchieveAnalysisDTO1.setTargetYear(targetYear);
        targetAchieveAnalysisDTO1.setCycleNumber(i);
        targetAchieveAnalysisDTO1.setCycleActual(BigDecimal.ZERO);
        targetAchieveAnalysisDTO1.setCycleForecast(BigDecimal.ZERO);
        targetAchieveAnalysisDTO1.setCycleTarget(BigDecimal.ZERO);
        targetAchieveAnalysisDTO1.setDeviationRate(BigDecimal.ZERO);
        targetAchieveAnalysisDTO1.setCompletionRate(BigDecimal.ZERO);
        targetAchieveAnalysisDTOS1.add(targetAchieveAnalysisDTO1);
    }

    /**
     * 创建分解维度
     *
     * @param targetAchieveAnalysisDTO 关键经营指标DTO
     * @param timeDimension            时间维度
     * @return dto
     */
    private TargetDecomposeDTO createTargetDecompose(TargetAchieveAnalysisDTO targetAchieveAnalysisDTO, Integer
            timeDimension) {
        return getTargetDecomposeDTO(timeDimension, targetAchieveAnalysisDTO.getIndicatorId(), targetAchieveAnalysisDTO.getTargetDecomposeDimensionId());
    }

    /**
     * 添加dot
     */
    private List<TargetAchieveAnalysisDTO> addTargetAchieveAnalysisDTOList(Map<String, Object> params, Integer startYear, Integer
            endYear, Integer cycleNumberStart,
                                                                           Integer cycleNumberEnd, int endCycleNumber, List<
            TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS, TargetAchieveAnalysisDTO targetAchieveAnalysisDTO) {
        TargetDecomposeDTO targetDecomposeDTO = createTargetDecompose(targetAchieveAnalysisDTO, targetAchieveAnalysisDTO.getTimeDimension());
        targetDecomposeDTO.setTargetYear(startYear);
        params.put("timeStart", cycleNumberStart);
        params.put("timeEnd", endCycleNumber);
        targetDecomposeDTO.setParams(params);
        targetAchieveAnalysisDTOS = new ArrayList<>(targetDecomposeMapper.selectAchieveAnalysisDecompose(targetDecomposeDTO));
        targetDecomposeDTO = createTargetDecompose(targetAchieveAnalysisDTO, targetAchieveAnalysisDTO.getTimeDimension());
        targetDecomposeDTO.setTargetYear(endYear);
        params.put("timeStart", 1);
        params.put("timeEnd", cycleNumberEnd);
        targetDecomposeDTO.setParams(params);
        targetAchieveAnalysisDTOS.addAll(targetDecomposeMapper.selectAchieveAnalysisDecompose(targetDecomposeDTO));
        return targetAchieveAnalysisDTOS;
    }

    /**
     * 查询关键经营指标排行榜列表
     *
     * @param targetLeaderboardDTO 关键经营指标排行榜
     * @return Map
     */
    @Override
    public Map<String, Object> targetLeaderboardList(TargetLeaderboardDTO targetLeaderboardDTO) {
        if (StringUtils.isNull(targetLeaderboardDTO.getIndicatorId()) || StringUtils.isNull(targetLeaderboardDTO.getTimeDimension())
                || StringUtils.isNull(targetLeaderboardDTO.getTargetDecomposeDimensionId())) {
            List<TargetLeaderboardDTO> targetLeaderboardDTOList = targetDecomposeMapper.selectRecentDecompose2();
            if (StringUtils.isEmpty(targetLeaderboardDTOList)) {
                return new HashMap<>();
            }
            this.packRemote(targetLeaderboardDTOList);
            for (TargetLeaderboardDTO leaderboardDTO : targetLeaderboardDTOList) {
                splicingDetailName(leaderboardDTO);
            }
            Map<String, List<TargetLeaderboardDTO>> groupByTargetLeaderboard = targetLeaderboardDTOList.stream()
                    .collect(Collectors.groupingBy(TargetLeaderboardDTO::getTargetDecomposeDetailsName));
            int timeDimension2 = targetLeaderboardDTOList.get(0).getTimeDimension();
            return getLeaderboardMap(timeDimension2, groupByTargetLeaderboard);
        }
        Integer timeDimension = targetLeaderboardDTO.getTimeDimension();
        Map<String, Object> params = new HashMap<>();
        params.put("targetYears", null);
        Integer startYear = DateUtils.getYear();
        Integer endYear = DateUtils.getYear();
        Integer cycleNumberStart = 1;
        Integer cycleNumberEnd = 1;
        int endCycleNumber = 2;
        if (timeDimension == 2) {
            cycleNumberEnd = 2;
        } else if (timeDimension == 3) {
            cycleNumberEnd = 4;
        } else if (timeDimension == 4) {
            cycleNumberEnd = 12;
        } else if (timeDimension == 5) {
            cycleNumberEnd = 52;
        }
        if (timeDimension == 1 && StringUtils.isNull(targetLeaderboardDTO.getStartYear()) && StringUtils.isNull(targetLeaderboardDTO.getEndYear())) {
            throw new ServiceException("请传入完整的年份区间");
        }
        if (timeDimension == 1 && StringUtils.isNotNull(targetLeaderboardDTO.getStartYear()) && StringUtils.isNotNull(targetLeaderboardDTO.getEndYear())) {// 只有年度
            startYear = targetLeaderboardDTO.getStartYear();
            endYear = targetLeaderboardDTO.getEndYear();
        } else if (timeDimension != 1 && StringUtils.isNotNull(targetLeaderboardDTO.getCycleNumberStart()) || StringUtils.isNotNull(targetLeaderboardDTO.getCycleNumberEnd())
                || StringUtils.isNotNull(targetLeaderboardDTO.getStartYear()) || StringUtils.isNotNull(targetLeaderboardDTO.getEndYear())) {
            startYear = targetLeaderboardDTO.getStartYear();
            endYear = targetLeaderboardDTO.getEndYear();
            cycleNumberStart = targetLeaderboardDTO.getCycleNumberStart();
            cycleNumberEnd = targetLeaderboardDTO.getCycleNumberEnd();
        } else {
            params.put("timeStart", cycleNumberStart);
            params.put("timeEnd", cycleNumberEnd);
        }
        checkValue(startYear, endYear, cycleNumberStart, cycleNumberEnd);
        if (timeDimension == 3) {
            endCycleNumber = 4;
        } else if (timeDimension == 4) {
            endCycleNumber = 12;
        } else if (timeDimension == 5) {
            endCycleNumber = 52;
        }
        List<Integer> targetYears = new ArrayList<>();
        for (int i = startYear; i <= endYear; i++) {
            targetYears.add(i);
        }
        List<TargetLeaderboardDTO> targetLeaderboardDTOList = new ArrayList<>();
        if (timeDimension == 1) {// 只有年度
            TargetDecomposeDTO targetDecomposeDTO = getTargetDecomposeDTO(timeDimension, targetLeaderboardDTO.getIndicatorId(), targetLeaderboardDTO.getTargetDecomposeDimensionId());
            params.put("targetYears", targetYears);
            targetDecomposeDTO.setParams(params);
            targetLeaderboardDTOList.addAll(targetDecomposeMapper.selectLeaderboardDecompose(targetDecomposeDTO));
        } else { // 没有年度
            if (endYear - startYear == 0) {
                TargetDecomposeDTO targetDecomposeDTO = getTargetDecomposeDTO(timeDimension, targetLeaderboardDTO.getIndicatorId(), targetLeaderboardDTO.getTargetDecomposeDimensionId());
                targetDecomposeDTO.setTargetYear(endYear);
                params.put("timeStart", cycleNumberStart);
                params.put("timeEnd", cycleNumberEnd);
                targetDecomposeDTO.setParams(params);
                targetLeaderboardDTOList.addAll(targetDecomposeMapper.selectLeaderboardDecompose(targetDecomposeDTO));
            } else if (endYear - startYear == 1) {
                targetLeaderboardDTOList = createLeaderBoardDTOS(targetLeaderboardDTO, timeDimension, params, startYear, endYear, cycleNumberStart, cycleNumberEnd, endCycleNumber);
            } else {
                List<Integer> queryYears = new ArrayList<>();
                targetLeaderboardDTOList = createLeaderBoardDTOS(targetLeaderboardDTO, timeDimension, params, startYear, endYear, cycleNumberStart, cycleNumberEnd, endCycleNumber);
                TargetDecomposeDTO targetDecomposeDTO;
                for (int i = startYear + 1; i <= endYear - 1; i++) {
                    queryYears.add(i);
                }
                targetDecomposeDTO = getTargetDecomposeDTO(timeDimension, targetLeaderboardDTO.getIndicatorId(), targetLeaderboardDTO.getTargetDecomposeDimensionId());
                params.put("targetYears", queryYears);
                targetDecomposeDTO.setParams(params);
                targetLeaderboardDTOList.addAll(targetDecomposeMapper.selectLeaderboardDecompose(targetDecomposeDTO));
            }
        }
        if (StringUtils.isEmpty(targetLeaderboardDTOList)) {
            return new HashMap<>();
        }
        this.packRemote(targetLeaderboardDTOList);
        for (TargetLeaderboardDTO leaderboardDTO : targetLeaderboardDTOList) {
            splicingDetailName(leaderboardDTO);
        }
        Map<String, List<TargetLeaderboardDTO>> groupByTargetLeaderboard = targetLeaderboardDTOList.stream()
                .collect(Collectors.groupingBy(TargetLeaderboardDTO::getTargetDecomposeDetailsName));
        return getLeaderboardMap(timeDimension, groupByTargetLeaderboard);
    }

    /**
     * 拼接管理维度名称
     *
     * @param leaderboardDTO 排行榜dto
     */
    private void splicingDetailName(TargetLeaderboardDTO leaderboardDTO) {
        String departmentName = StringUtils.isNull(leaderboardDTO.getDepartmentName()) ? "" : leaderboardDTO.getDepartmentName() + ",";
        String employeeName = StringUtils.isNull(leaderboardDTO.getEmployeeName()) ? "" : leaderboardDTO.getEmployeeName() + ",";
        String industryName = StringUtils.isNull(leaderboardDTO.getIndustryName()) ? "" : leaderboardDTO.getIndustryName() + ",";
        String productName = StringUtils.isNull(leaderboardDTO.getProductName()) ? "" : leaderboardDTO.getProductName() + ",";
        String regionName = StringUtils.isNull(leaderboardDTO.getRegionName()) ? "" : leaderboardDTO.getRegionName() + ",";
        String areaName = StringUtils.isNull(leaderboardDTO.getAreaName()) ? "" : leaderboardDTO.getAreaName() + ",";
        String leaderboardName = departmentName + employeeName + industryName + productName + regionName + areaName;
        if (leaderboardName.length() > 0) {
            leaderboardDTO.setTargetDecomposeDetailsName(leaderboardName.substring(0, leaderboardName.length() - 1));
        }
    }

    /**
     * 创建仪表盘列表
     *
     * @return List
     */
    private List<TargetLeaderboardDTO> createLeaderBoardDTOS(TargetLeaderboardDTO targetLeaderboardDTO, Integer
            timeDimension, Map<String, Object> params, Integer startYear, Integer endYear, Integer
                                                                     cycleNumberStart, Integer cycleNumberEnd, int endCycleNumber) {
        List<TargetLeaderboardDTO> targetLeaderboardDTOS;
        TargetDecomposeDTO targetDecomposeDTO = getTargetDecomposeDTO(timeDimension, targetLeaderboardDTO.getIndicatorId(), targetLeaderboardDTO.getTargetDecomposeDimensionId());
        targetDecomposeDTO.setTargetYear(startYear);
        params.put("timeStart", cycleNumberStart);
        params.put("timeEnd", endCycleNumber);
        targetDecomposeDTO.setParams(params);
        targetLeaderboardDTOS = new ArrayList<>(targetDecomposeMapper.selectLeaderboardDecompose(targetDecomposeDTO));
        targetDecomposeDTO = getTargetDecomposeDTO(timeDimension, targetLeaderboardDTO.getIndicatorId(), targetLeaderboardDTO.getTargetDecomposeDimensionId());
        targetDecomposeDTO.setTargetYear(endYear);
        params.put("timeStart", 1);
        params.put("timeEnd", cycleNumberEnd);
        targetDecomposeDTO.setParams(params);
        targetLeaderboardDTOS.addAll(targetDecomposeMapper.selectLeaderboardDecompose(targetDecomposeDTO));
        return targetLeaderboardDTOS;
    }

    /**
     * 获取目标分解
     *
     * @param timeDimension         时间维度
     * @param targetLeaderboardDTO  仪表盘
     * @param targetLeaderboardDTO1 仪表盘
     * @return TargetDecomposeDTO
     */
    private TargetDecomposeDTO getTargetDecomposeDTO(Integer timeDimension, Long targetLeaderboardDTO, Long targetLeaderboardDTO1) {
        TargetDecomposeDTO targetDecomposeDTO = new TargetDecomposeDTO();
        targetDecomposeDTO.setTimeDimension(timeDimension);
        targetDecomposeDTO.setIndicatorId(targetLeaderboardDTO);
        targetDecomposeDTO.setTargetDecomposeDimensionId(targetLeaderboardDTO1);
        return targetDecomposeDTO;
    }

    /**
     * 封装远程调用数据-密持那边copy过来的
     *
     * @param targetLeaderboardDTOS 详情DTO
     */
    public void packRemote(List<TargetLeaderboardDTO> targetLeaderboardDTOS) {
        if (StringUtils.isNotEmpty(targetLeaderboardDTOS)) {
            //人员id集合
            List<Long> employeeIdCollect = targetLeaderboardDTOS.stream().map(TargetLeaderboardDTO::getEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            //部门id集合
            List<Long> departmentIdCollect = targetLeaderboardDTOS.stream().map(TargetLeaderboardDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            //省份id集合
            Set<Long> regionIdCollect = targetLeaderboardDTOS.stream().map(TargetLeaderboardDTO::getRegionId).distinct().filter(Objects::nonNull).collect(Collectors.toSet());

            //行业id集合
            List<Long> industryIdCollect = targetLeaderboardDTOS.stream().map(TargetLeaderboardDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            //人员远程
            if (StringUtils.isNotEmpty(employeeIdCollect)) {
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(employeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetLeaderboardDTO TargetLeaderboardDTO : targetLeaderboardDTOS) {
                        for (EmployeeDTO datum : data) {
                            if (TargetLeaderboardDTO.getEmployeeId().equals(datum.getEmployeeId())) {
                                TargetLeaderboardDTO.setEmployeeId(datum.getEmployeeId());
                                TargetLeaderboardDTO.setEmployeeName(datum.getEmployeeName());
                                break;
                            }
                        }
                    }
                }
            }
            //部门远程
            if (StringUtils.isNotEmpty(departmentIdCollect)) {
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(departmentIdCollect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetLeaderboardDTO TargetLeaderboardDTO : targetLeaderboardDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (TargetLeaderboardDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                TargetLeaderboardDTO.setDepartmentId(datum.getDepartmentId());
                                TargetLeaderboardDTO.setDepartmentName(datum.getDepartmentName());
                                break;
                            }
                        }
                    }
                }
            }
            //省份远程
            if (StringUtils.isNotEmpty(regionIdCollect)) {
                R<List<RegionDTO>> regionsByIds = remoteRegionService.getRegionsByIds(regionIdCollect, SecurityConstants.INNER);
                List<RegionDTO> data = regionsByIds.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetLeaderboardDTO TargetLeaderboardDTO : targetLeaderboardDTOS) {
                        for (RegionDTO datum : data) {
                            if (TargetLeaderboardDTO.getRegionId().equals(datum.getRegionId())) {
                                TargetLeaderboardDTO.setRegionId(datum.getRegionId());
                                TargetLeaderboardDTO.setRegionName(datum.getRegionName());
                                break;
                            }
                        }
                    }
                }
            }
            //行业远程
            if (StringUtils.isNotEmpty(industryIdCollect)) {
                R<List<IndustryDTO>> listR = remoteIndustryService.selectByIds(industryIdCollect, SecurityConstants.INNER);
                List<IndustryDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetLeaderboardDTO TargetLeaderboardDTO : targetLeaderboardDTOS) {
                        for (IndustryDTO datum : data) {
                            if (TargetLeaderboardDTO.getIndustryId().equals(datum.getIndustryId())) {
                                TargetLeaderboardDTO.setIndustryId(datum.getIndustryId());
                                TargetLeaderboardDTO.setIndustryName(datum.getIndustryName());
                                break;
                            }
                        }
                    }
                }

            }
        }
    }

    /**
     * @param timeDimension            时间维度
     * @param groupByTargetLeaderboard 分组后的排行榜
     * @return Map
     */
    private Map<String, Object> getLeaderboardMap(Integer
                                                          timeDimension, Map<String, List<TargetLeaderboardDTO>> groupByTargetLeaderboard) {
        List<TargetLeaderboardDTO> targetLeaderboardDTOS = new ArrayList<>();
        for (String targetDecomposeDetailsName : groupByTargetLeaderboard.keySet()) {
            TargetLeaderboardDTO targetLeaderboard = new TargetLeaderboardDTO();
            List<TargetLeaderboardDTO> targetLeaderboardDTOList = groupByTargetLeaderboard.get(targetDecomposeDetailsName);
            BigDecimal cycleActualSum = BigDecimal.ZERO;
            BigDecimal cycleTargetSum = BigDecimal.ZERO;
            BigDecimal cycleForecastSum = BigDecimal.ZERO;
            if (StringUtils.isNotEmpty(targetLeaderboardDTOList)) {
                for (TargetLeaderboardDTO targetLeaderboardDTO : targetLeaderboardDTOList) {
                    cycleActualSum = cycleActualSum.add(Optional.ofNullable(targetLeaderboardDTO.getCycleActual()).orElse(BigDecimal.ZERO));
                    cycleTargetSum = cycleTargetSum.add(Optional.ofNullable(targetLeaderboardDTO.getCycleTarget()).orElse(BigDecimal.ZERO));
                    cycleForecastSum = cycleForecastSum.add(Optional.ofNullable(targetLeaderboardDTO.getCycleForecast()).orElse(BigDecimal.ZERO));
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
                targetLeaderboard.setTargetDecomposeDetailsName(targetDecomposeDetailsName);
                targetLeaderboard.setCycleActualSum(cycleActualSum);
                targetLeaderboard.setCompletionRate(completionRate);
                targetLeaderboard.setDeviationRate(deviationRate);
                targetLeaderboardDTOS.add(targetLeaderboard);
            }
        }
        // 降序
        List<TargetLeaderboardDTO> targetLeaderboardDTORevers =
                targetLeaderboardDTOS.stream().sorted(Comparator.comparing(TargetLeaderboardDTO::getCycleActualSum).reversed()).collect(Collectors.toList());
        for (int i = 1; i < targetLeaderboardDTORevers.size() + 1; i++) {
            TargetLeaderboardDTO targetLeaderboardDTO = targetLeaderboardDTORevers.get(i - 1);
            targetLeaderboardDTO.setRanking(i);
        }
        List<BigDecimal> cycleActual = targetLeaderboardDTORevers.stream().map(TargetLeaderboardDTO::getCycleActualSum).collect(Collectors.toList());
        List<String> targetDecomposeDetailsNames = targetLeaderboardDTOS.stream().sorted(Comparator.comparing(TargetLeaderboardDTO::getCycleActualSum)).collect(Collectors.toList())
                .stream().map(TargetLeaderboardDTO::getTargetDecomposeDetailsName).collect(Collectors.toList());
        Map<String, Object> map = new HashMap<>();
        map.put("cycleActual", cycleActual);
        map.put("targetLeaderboardDTORevers", targetLeaderboardDTORevers);
        map.put("decompositionDimension", targetDecomposeDetailsNames);
        return map;
    }

    /**
     * List<Map>-→Map<String,List>
     *
     * @param timeDimension                时间维度
     * @param groupByTargetAchieveAnalysis 分组后的仪表盘
     * @param targetYears                  目标年度List
     * @return Map
     */
    private Map<String, Object> getAchieveAnalysisMap(Integer
                                                              timeDimension, Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis, List<Integer> targetYears) {
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOList;
        if (timeDimension == 1) {
            targetAchieveAnalysisDTOList = this.getTargetYearAchieveAnalysisDTOS(groupByTargetAchieveAnalysis, targetYears);
        } else {
            targetAchieveAnalysisDTOList = this.getTargetCycleNumberAchieveAnalysisDTOS(timeDimension, groupByTargetAchieveAnalysis);
        }
        Map<String, Object> hashMap = new HashMap<>();
        listToMap(targetAchieveAnalysisDTOList, hashMap);
        return hashMap;
    }

    /**
     * List<dto>→map
     *
     * @param targetAchieveAnalysisDTOList list
     * @param hashMap                      map
     */
    private void listToMap
    (List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOList, Map<String, Object> hashMap) {
        hashMap.put("forecast", targetAchieveAnalysisDTOList.stream().map(TargetAchieveAnalysisDTO::getCycleForecastSum).collect(Collectors.toList()));
        hashMap.put("cycleTarget", targetAchieveAnalysisDTOList.stream().map(TargetAchieveAnalysisDTO::getCycleTargetSum).collect(Collectors.toList()));
        hashMap.put("cycleActual", targetAchieveAnalysisDTOList.stream().map(TargetAchieveAnalysisDTO::getCycleActualSum).collect(Collectors.toList()));
        hashMap.put("completionRate", targetAchieveAnalysisDTOList.stream().map(TargetAchieveAnalysisDTO::getCompletionRate).collect(Collectors.toList()));
        hashMap.put("deviationRate", targetAchieveAnalysisDTOList.stream().map(TargetAchieveAnalysisDTO::getDeviationRate).collect(Collectors.toList()));
        hashMap.put("cycleNumberName", targetAchieveAnalysisDTOList.stream().map(TargetAchieveAnalysisDTO::getCycleNumberName).collect(Collectors.toList()));
    }

    /**
     * 计算年度时间维度的分解为度
     *
     * @param groupByTargetAchieveAnalysis 分组后的数据
     * @param targetYears                  目标年度List
     * @return List
     */
    private List<TargetAchieveAnalysisDTO> getTargetYearAchieveAnalysisDTOS
    (Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis, List<Integer> targetYears) {
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
                cycleTargetSum = cycleTargetSum.add(Optional.ofNullable(smallTargetAchieveAnalysisDTO.getCycleTarget()).orElse(BigDecimal.ZERO));
                cycleForecastSum = cycleForecastSum.add(Optional.ofNullable(smallTargetAchieveAnalysisDTO.getCycleForecast()).orElse(BigDecimal.ZERO));
                cycleActualSum = cycleActualSum.add(Optional.ofNullable(smallTargetAchieveAnalysisDTO.getCycleActual()).orElse(BigDecimal.ZERO));
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
        // 排序
        return targetAchieveAnalysisDTOList.stream().sorted(Comparator.comparingInt(TargetAchieveAnalysisDTO::getCycleNumber)).collect(Collectors.toList());
    }

    /**
     * 计算半年度、季度、月度、周等时间维度的分解为度
     *
     * @param timeDimension                时间维度
     * @param groupByTargetAchieveAnalysis 分组后的
     */
    private List<TargetAchieveAnalysisDTO> getTargetCycleNumberAchieveAnalysisDTOS(Integer
                                                                                           timeDimension, Map<Integer, List<TargetAchieveAnalysisDTO>> groupByTargetAchieveAnalysis) {
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOList = new ArrayList<>();
        for (Integer targetYear : groupByTargetAchieveAnalysis.keySet()) {
            List<TargetAchieveAnalysisDTO> smallTargetAchieveAnalysisDTOList = new ArrayList<>();
            List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS1 = groupByTargetAchieveAnalysis.get(targetYear);
            Map<Integer, List<TargetAchieveAnalysisDTO>> cycleNumberMap =
                    targetAchieveAnalysisDTOS1.stream().collect(Collectors.groupingBy(TargetAchieveAnalysisDTO::getCycleNumber));
            for (Integer integer : cycleNumberMap.keySet()) {
                List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS = cycleNumberMap.get(integer);
                TargetAchieveAnalysisDTO targetAchieveAnalysis = new TargetAchieveAnalysisDTO();
                BigDecimal cycleTargetSum = new BigDecimal(0);
                BigDecimal cycleForecastSum = new BigDecimal(0);
                BigDecimal cycleActualSum = new BigDecimal(0);
                setTimeValue(timeDimension, targetYear, integer, targetAchieveAnalysis);
                for (TargetAchieveAnalysisDTO smallTargetAchieveAnalysisDTO : targetAchieveAnalysisDTOS) {
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
                targetAchieveAnalysis.setCycleNumber(integer);
                targetAchieveAnalysis.setCycleTargetSum(cycleTargetSum);
                targetAchieveAnalysis.setCycleForecastSum(cycleForecastSum);
                targetAchieveAnalysis.setCycleActualSum(cycleActualSum);
                targetAchieveAnalysis.setCompletionRate(completionRate);
                targetAchieveAnalysis.setDeviationRate(deviationRate);
                smallTargetAchieveAnalysisDTOList.add(targetAchieveAnalysis);
            }
            targetAchieveAnalysisDTOList.addAll(smallTargetAchieveAnalysisDTOList.stream().sorted(Comparator.comparingInt(TargetAchieveAnalysisDTO::getCycleNumber)).collect(Collectors.toList()));
        }
        return targetAchieveAnalysisDTOList;
    }

    /**
     * 给时间轴赋值
     *
     * @param timeDimension         时间维度
     * @param targetYear            年份
     * @param cycleNumber           周期
     * @param targetAchieveAnalysis dto
     */
    private void setTimeValue(Integer timeDimension, Integer targetYear, Integer
            cycleNumber, TargetAchieveAnalysisDTO targetAchieveAnalysis) {
        switch (timeDimension) {
            case 1:
                targetAchieveAnalysis.setCycleNumberName(targetYear + "年度");
                break;
            case 2:
                if (cycleNumber == 1) {
                    targetAchieveAnalysis.setCycleNumberName(targetYear + "上半年");
                } else {
                    targetAchieveAnalysis.setCycleNumberName(targetYear + "下半年");
                }
                break;
            case 3:
                targetAchieveAnalysis.setCycleNumberName(targetYear + "年第" + cycleNumber + "季度");
                break;
            case 4:
                targetAchieveAnalysis.setCycleNumberName(targetYear + "年第" + cycleNumber + "月");
                break;
            case 5:
                targetAchieveAnalysis.setCycleNumberName(targetYear + "年第" + cycleNumber + "周");
                break;
        }
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

    /**
     * 目标达成获取指标列表
     *
     * @return List
     */
    @Override
    public List<Map<String, Object>> getDropList() {
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeIndicator();
        if (StringUtils.isEmpty(targetDecomposeDTOS)) {
            return new ArrayList<>();
        }
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
        return indicatorList;
    }

    /**
     * 关键经营指标排行榜下拉列表
     *
     * @param indicatorId 指标ID
     * @return Map
     */
    @Override
    public List<Map<String, Object>> targetDropList(Long indicatorId) {
        if (StringUtils.isNull(indicatorId)) {
            throw new ServiceException("请传输指标ID");
        }
        //分解维度
        List<Map<String, Object>> targetDecomposeDimensionDTOList = new ArrayList<>();
        TargetDecompose targetDecompose = new TargetDecompose();
        targetDecompose.setIndicatorId(indicatorId);
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
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
        return targetDecomposeDimensionDTOList;
    }

    /**
     * 获取时间维度下拉框
     *
     * @param indicatorId                指标ID
     * @param targetDecomposeDimensionId 分解维度ID
     * @return List
     */
    @Override
    public List<Map<String, Object>> timeDropList(Long indicatorId, Long targetDecomposeDimensionId) {
        TargetDecompose targetDecompose = new TargetDecompose();
        targetDecompose.setIndicatorId(indicatorId);
        targetDecompose.setTargetDecomposeDimensionId(targetDecomposeDimensionId);
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
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
            }
        }
        return timeDimensionDTOS;
    }

    /**
     * 最近一次的分解维度信息
     *
     * @return Map
     */
    @Override
    public Map<String, Object> getLastTimeDecompose() {
        Map<String, Object> map = new HashMap<>();
        List<TargetAchieveAnalysisDTO> targetAchieveAnalysisDTOS = targetDecomposeMapper.selectRecentDecompose();
        if (StringUtils.isEmpty(targetAchieveAnalysisDTOS)) {
            return null;
        }
        TargetAchieveAnalysisDTO targetAchieveAnalysisDTO = targetAchieveAnalysisDTOS.get(0);
        if (StringUtils.isNotNull(targetAchieveAnalysisDTO.getIndicatorId())) {
            map.put("indicatorId", targetAchieveAnalysisDTO.getIndicatorId());
        }
        Integer timeDimension = targetAchieveAnalysisDTO.getTimeDimension();
        Integer targetYear = targetAchieveAnalysisDTO.getTargetYear();
        if (StringUtils.isNotNull(timeDimension)) {
            switch (timeDimension) {
                case 1:
                    map.put("timeDimension", 1);
                    map.put("startTime", targetYear);
                    map.put("endTime", targetYear);
                    break;
                case 2:
                    map.put("timeDimension", 2);
                    map.put("startTime", targetYear + "/0" + 1);
                    map.put("endTime", targetYear + "/" + 2);
                    break;
                case 3:
                    map.put("timeDimension", 3);
                    map.put("startTime", targetYear + "/" + 1);
                    map.put("endTime", targetYear + "/" + 4);
                    break;
                case 4:
                    map.put("timeDimension", 4);
                    map.put("startTime", targetYear + "/0" + 1);
                    map.put("endTime", targetYear + "/" + 12);
                    break;
            }
        }
        Long decompositionDimensionId = targetAchieveAnalysisDTO.getTargetDecomposeDimensionId();
        if (StringUtils.isNotNull(decompositionDimensionId)) {
            map.put("decompositionDimensionId", decompositionDimensionId);
        }

        return map;
    }
}
