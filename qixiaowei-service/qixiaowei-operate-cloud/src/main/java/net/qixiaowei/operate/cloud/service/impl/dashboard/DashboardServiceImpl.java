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
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDimensionDTO;
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
     * @param targetYear 目标年度
     * @return List
     */
    @Override
    public List<TargetAchieveRateDTO> targetAchieveRateList(Integer targetYear) {
        if (StringUtils.isNull(targetYear)) {
            targetYear = DateUtils.getYear();
        }
        TargetSetting targetSetting = new TargetSetting();
        targetSetting.setTargetYear(targetYear);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetAndOutSettingList(targetSetting);
        if (targetSettingDTOList.size() == 0) {
            List<TargetAchieveRateDTO> targetAchieveRateDTOS = new ArrayList<>();
            // 赋三个空数据
            for (int i = 0; i < 3; i++) {
                TargetAchieveRateDTO targetAchieveRateDTO = new TargetAchieveRateDTO();
                targetAchieveRateDTO.setTargetValue(BigDecimal.ZERO);
                targetAchieveRateDTO.setTargetValue(BigDecimal.ZERO);
                targetAchieveRateDTO.setActualTotal(BigDecimal.ZERO);
                targetAchieveRateDTO.setRate(BigDecimal.ZERO);
                switch (i) {
                    case 0:
                        targetAchieveRateDTO.setIndicatorName("订单额");
                    case 1:
                        targetAchieveRateDTO.setIndicatorName("收入");
                    case 2:
                        targetAchieveRateDTO.setIndicatorName("销售毛利润");
                }
                targetAchieveRateDTOS.add(targetAchieveRateDTO);
            }
            return targetAchieveRateDTOS;
        }
        List<Long> indicatorIds = targetSettingDTOList.stream().map(TargetSettingDTO::getIndicatorId).collect(Collectors.toList());
        List<IndicatorDTO> indicatorDTOS = getIndicator(indicatorIds);
        for (TargetSettingDTO targetSettingDTO : targetSettingDTOList) {
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                if (indicatorDTO.getIndicatorId().equals(targetSettingDTO.getIndicatorId())) {
                    //添加唯一标识 避免后面copy出现重复数据问题
                    targetSettingDTO.setIndicatorCode(indicatorDTO.getIndicatorCode());
                    if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.ORDER.getCode())) {
                        targetSettingDTO.setIndicatorName("订单额");
                    } else if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.INCOME.getCode())) {
                        targetSettingDTO.setIndicatorName("收入");
                    } else if (indicatorDTO.getIndicatorCode().equals(IndicatorCode.RECEIVABLE.getCode())) {
                        targetSettingDTO.setIndicatorName("销售毛利润");
                    } else {
                        targetSettingDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                    }
                    break;
                }
            }
        }
        List<String> indicatorCodes = targetSettingDTOList.stream().map(TargetSettingDTO::getIndicatorCode).collect(Collectors.toList());
        List<TargetAchieveRateDTO> targetAchieveRateDTOS = new ArrayList<>();
        for (TargetSettingDTO targetSettingDTO : targetSettingDTOList) {
            TargetAchieveRateDTO targetAchieveRate = new TargetAchieveRateDTO();
            BeanUtils.copyProperties(targetSettingDTO, targetAchieveRate);
            targetAchieveRateDTOS.add(targetAchieveRate);
        }
        List<String> indicatorCodeList = IndicatorCode.getCodesByIsPreset(1);
        indicatorCodeList.removeAll(indicatorCodes);
        if (StringUtils.isNotEmpty(indicatorCodeList)) {
            for (String code : indicatorCodeList) {
                TargetAchieveRateDTO targetAchieveRateDTO = new TargetAchieveRateDTO();
                targetAchieveRateDTO.setTargetValue(BigDecimal.ZERO);
                targetAchieveRateDTO.setActualTotal(BigDecimal.ZERO);
                if (code.equals(IndicatorCode.ORDER.getCode())) {
                    targetAchieveRateDTO.setIndicatorName("订单额");
                    targetAchieveRateDTO.setIndicatorCode(IndicatorCode.ORDER.getCode());
                } else if (code.equals(IndicatorCode.INCOME.getCode())) {
                    targetAchieveRateDTO.setIndicatorName("收入");
                    targetAchieveRateDTO.setIndicatorCode(IndicatorCode.INCOME.getCode());
                } else if (code.equals(IndicatorCode.RECEIVABLE.getCode())) {
                    targetAchieveRateDTO.setIndicatorName("销售毛利润");
                    targetAchieveRateDTO.setIndicatorCode(IndicatorCode.RECEIVABLE.getCode());
                }
                targetAchieveRateDTOS.add(targetAchieveRateDTO);
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
        return targetAchieveRateDTOS;
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
        //分解维度
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDTOS = new ArrayList<>();
        for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDTOS) {
            TargetDecomposeDimensionDTO targetDecomposeDimensionDTO = new TargetDecomposeDimensionDTO();
            targetDecomposeDimensionDTO.setTargetDecomposeDimensionId(targetDecomposeDTO.getTargetDecomposeDimensionId());
            targetDecomposeDimensionDTO.setDecompositionDimension(targetDecomposeDTO.getDecompositionDimension());
            targetDecomposeDimensionDTOS.add(targetDecomposeDimensionDTO);
        }
        //时间维度
        List<Integer> timeDimensions = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getTimeDimension).collect(Collectors.toList());
        map.put("indicatorDropList", indicatorDTOS);//指标下拉
        map.put("decomposeDropList", targetDecomposeDimensionDTOS);//分解维度下拉
        map.put("timeDropList", timeDimensions);//时间维度下拉
        return map;
    }

    /**
     * 关键经营指标月度达成分析列表
     *
     * @return List
     */
    @Override
    public List<TargetAchieveAnalysisDTO> targetAchieveAnalysisList(TargetAchieveAnalysisDTO targetAchieveAnalysisDTO) {
        Date timeDimensionStart = DateUtils.getYearStart(DateUtils.getYear());
        Date timeDimensionEnd = DateUtils.getNowDate();
        Map<String, Object> params = targetAchieveAnalysisDTO.getParams();
        if (StringUtils.isNotNull(params) && StringUtils.isNotNull(params.get("timeDimensionStart")) && StringUtils.isNotNull(params.get("timeDimensionEnd"))) {
            timeDimensionStart = DateUtils.parseDate(params.get("timeDimensionStart"));
            timeDimensionEnd = DateUtils.parseDate(params.get("timeDimensionEnd"));
        }
        TargetDecompose targetDecompose = new TargetDecompose();
        targetDecompose.setTargetYear(targetAchieveAnalysisDTO.getTargetYear());
        targetDecompose.setTimeDimension(targetAchieveAnalysisDTO.getTimeDimension());
        targetDecompose.setTargetDecomposeDimensionId(targetAchieveAnalysisDTO.getTargetDecomposeDimensionId());
        targetDecompose.setIndicatorId(targetAchieveAnalysisDTO.getIndicatorId());
        targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
        return null;
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
}
