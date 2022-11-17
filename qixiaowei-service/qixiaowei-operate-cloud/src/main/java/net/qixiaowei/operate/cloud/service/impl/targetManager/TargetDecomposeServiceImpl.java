package net.qixiaowei.operate.cloud.service.impl.targetManager;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.enums.targetManager.DecompositionDimension;
import net.qixiaowei.integration.common.enums.targetManager.TargetDecomposeDimensionCode;
import net.qixiaowei.integration.common.enums.targetManager.TargetDecomposeType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.Convert;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.*;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeExcel;
import net.qixiaowei.operate.cloud.mapper.product.ProductMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.*;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;


/**
 * TargetDecomposeService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
@Service
public class TargetDecomposeServiceImpl implements ITargetDecomposeService {
    @Autowired
    private TargetDecomposeMapper targetDecomposeMapper;
    @Autowired
    private RemoteIndicatorService remoteIndicatorService;
    @Autowired
    private DecomposeDetailCyclesMapper decomposeDetailCyclesMapper;
    @Autowired
    private TargetDecomposeDetailsMapper targetDecomposeDetailsMapper;

    @Autowired
    private TargetDecomposeHistoryMapper targetDecomposeHistoryMapper;
    @Autowired
    private DecomposeDetailsSnapshotMapper decomposeDetailsSnapshotMapper;
    @Autowired
    private DetailCyclesSnapshotMapper detailCyclesSnapshotMapper;
    @Autowired
    private TargetDecomposeDimensionMapper targetDecomposeDimensionMapper;

    @Autowired
    private ProductMapper productMapper;
    @Autowired
    private AreaMapper areaMapper;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteIndustryService remoteIndustryService;
    @Autowired
    private RemoteRegionService remoteRegionService;

    /**
     * 查询经营结果分析报表详情
     *
     * @param targetDecomposeId 目标分解表主键
     * @return
     */
    @Override
    public TargetDecomposeDTO selectResultTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        //目标分解主表数据
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在！");
        } else {
            R<IndicatorDTO> R = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
            IndicatorDTO data = R.getData();
            if (StringUtils.isNotNull(data)){
                targetDecomposeDTO.setIndicatorName(data.getIndicatorName());
            }
            this.packDecompositionDimension(targetDecomposeDTO);
        }
        //目标分解详情数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeId);
        this.packRemote(targetDecomposeDetailsDTOList);
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                //年度预测值
                BigDecimal forecastYear = new BigDecimal("0");
                //累计实际值
                BigDecimal actualTotal = new BigDecimal("0");
                //目标完成率
                BigDecimal targetPercentageComplete = new BigDecimal("0");
                //目标完成率总和
                BigDecimal targetPercentageCompleteSum = new BigDecimal("0");
                //目标完成率平均值
                BigDecimal targetPercentageCompleteAve = new BigDecimal("0");
                //预测与目标偏差率总和
                BigDecimal forecastDeviationRateSum = new BigDecimal("0");
                //预测与目标偏差率平均值
                BigDecimal forecastDeviationRateAve = new BigDecimal("0");
                //分解目标值
                BigDecimal decomposeTarget = targetDecomposeDetailsDTO.getDecomposeTarget();
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = new ArrayList<>();
                decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                    //周期预测值
                    BigDecimal cycleForecast = decomposeDetailCyclesDTO.getCycleForecast();
                    //周期实际值
                    BigDecimal cycleActual = decomposeDetailCyclesDTO.getCycleActual();
                    //周期目标值
                    BigDecimal cycleTarget = decomposeDetailCyclesDTO.getCycleTarget();
                    //预测偏差
                    BigDecimal cycleForecastDeviation = new BigDecimal("0");
                    //预测与目标偏差率
                    BigDecimal cycleForecastDeviationRate = new BigDecimal("0");
                    //目标完成率
                    BigDecimal cyclePercentageComplete = new BigDecimal("0");
                    if (null != cycleForecast && cycleForecast.compareTo(BigDecimal.ZERO) != 0) {
                        //预测值
                        forecastYear = forecastYear.add(cycleForecast);
                    }
                    if (null != cycleActual && cycleActual.compareTo(BigDecimal.ZERO) != 0) {
                        //实际值
                        actualTotal = actualTotal.add(cycleActual);
                    }


                    if (cycleForecast != null) {
                        if (cycleTarget != null) {
                            cycleForecastDeviation = cycleForecast.subtract(cycleTarget).setScale(2);
                        }
                    }
                    //预测偏差
                    decomposeDetailCyclesDTO.setCycleForecastDeviation(cycleForecastDeviation);

                    if (cycleForecastDeviation != null && cycleForecastDeviation.compareTo(new BigDecimal("0")) != 0) {
                        if (cycleTarget != null && cycleTarget.compareTo(new BigDecimal("0")) != 0) {
                            cycleForecastDeviationRate = cycleForecastDeviation.divide(cycleTarget, BigDecimal.ROUND_CEILING);
                        }
                    }
                    //预测与目标偏差率
                    decomposeDetailCyclesDTO.setCycleForecastDeviationRate(cycleForecastDeviationRate);

                    if (cycleActual != null && cycleActual.compareTo(new BigDecimal("0")) != 0) {
                        if (cycleTarget != null && cycleTarget.compareTo(new BigDecimal("0")) != 0) {
                            cyclePercentageComplete = cycleActual.divide(cycleTarget, BigDecimal.ROUND_CEILING);
                        }
                    }
                    //目标完成率
                    decomposeDetailCyclesDTO.setCyclePercentageComplete(cyclePercentageComplete);
                    //预测总和
                    if (decomposeDetailCyclesDTO.getCycleForecastDeviation() != null && decomposeDetailCyclesDTO.getCycleForecastDeviation().compareTo(new BigDecimal("0")) != 0) {
                        forecastDeviationRateSum = forecastDeviationRateSum.add(decomposeDetailCyclesDTO.getCycleForecastDeviation());
                    }
                    //目标总和
                    if (decomposeDetailCyclesDTO.getCyclePercentageComplete() != null && decomposeDetailCyclesDTO.getCyclePercentageComplete().compareTo(new BigDecimal("0")) != 0) {
                        targetPercentageCompleteSum = targetPercentageCompleteSum.add(decomposeDetailCyclesDTO.getCyclePercentageComplete());
                    }

                }

                if (null != actualTotal && actualTotal.compareTo(BigDecimal.ZERO) != 0) {
                    //被除数 不能为0和空
                    if (null != decomposeTarget && decomposeTarget.compareTo(BigDecimal.ZERO) != 0) {
                        //保留一位小数
                        targetPercentageComplete = actualTotal.divide(targetDecomposeDetailsDTO.getDecomposeTarget(), BigDecimal.ROUND_CEILING);
                    }
                }
                //预测平均数
                if (null != forecastDeviationRateSum && forecastDeviationRateSum.compareTo(BigDecimal.ZERO) != 0) {
                    forecastDeviationRateAve = forecastDeviationRateSum.divide(new BigDecimal(String.valueOf(decomposeDetailCyclesDTOList.size() - 1)), BigDecimal.ROUND_CEILING);
                }
                //目标完成平均数
                if (null != targetPercentageCompleteAve && targetPercentageCompleteAve.compareTo(BigDecimal.ZERO) != 0) {
                    targetPercentageCompleteAve = targetPercentageCompleteSum.divide(new BigDecimal(String.valueOf(decomposeDetailCyclesDTOList.size() - 1)), BigDecimal.ROUND_CEILING);
                }
                targetDecomposeDetailsDTO.setForecastDeviationRateAve(forecastDeviationRateAve);
                targetDecomposeDetailsDTO.setTargetPercentageCompleteAve(targetPercentageCompleteAve);
                targetDecomposeDetailsDTO.setForecastYear(forecastYear);
                targetDecomposeDetailsDTO.setActualTotal(actualTotal);
                targetDecomposeDetailsDTO.setTargetPercentageComplete(targetPercentageComplete);
                //目标分解周欺数据集合
                targetDecomposeDetailsDTO.setDecomposeDetailCyclesDTOS(decomposeDetailCyclesDTOList);
            }
            return targetDecomposeDTO.setTargetDecomposeDetailsDTOS(targetDecomposeDetailsDTOList);
        } else {
            return targetDecomposeDTO;
        }
    }

    /**
     * 查询滚动预测表详情
     *
     * @param targetDecomposeId 目标分解表主键
     * @return
     */
    @Override
    public TargetDecomposeDTO selectRollTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        //目标分解主表数据
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在！");
        } else {
            R<IndicatorDTO> R = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
            IndicatorDTO data = R.getData();
            if (StringUtils.isNotNull(data)){
                targetDecomposeDTO.setIndicatorName(data.getIndicatorName());
            }
            this.packDecompositionDimension(targetDecomposeDTO);
            String forecastCycle = this.packForecastCycle(targetDecomposeDTO);
            targetDecomposeDTO.setForecastCycle(forecastCycle);
            this.packversion(targetDecomposeDTO);
        }
        //目标分解详情数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeId);
        this.packRemote(targetDecomposeDetailsDTOList);
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                //年度预测值
                BigDecimal forecastYear = new BigDecimal("0");
                //累计实际值
                BigDecimal actualTotal = new BigDecimal("0");
                //分解目标值
                BigDecimal decomposeTarget = targetDecomposeDetailsDTO.getDecomposeTarget();
                //目标完成率
                BigDecimal targetPercentageComplete = new BigDecimal("0");
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = new ArrayList<>();
                decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                    if (null != decomposeDetailCyclesDTO.getCycleForecast() && decomposeDetailCyclesDTO.getCycleForecast().compareTo(BigDecimal.ZERO) != 0) {
                        //预测值
                        forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleForecast());
                    }
                    if (null != decomposeDetailCyclesDTO.getCycleActual() && decomposeDetailCyclesDTO.getCycleActual().compareTo(BigDecimal.ZERO) != 0) {
                        //实际值
                        actualTotal = actualTotal.add(decomposeDetailCyclesDTO.getCycleActual());
                    }
                }

                if (null != actualTotal && actualTotal.compareTo(BigDecimal.ZERO) != 0) {
                    //被除数 不能为0和空
                    if (null != decomposeTarget && decomposeTarget.compareTo(BigDecimal.ZERO) != 0) {
                        //保留一位小数
                        targetPercentageComplete = actualTotal.divide(targetDecomposeDetailsDTO.getDecomposeTarget()).setScale(1);
                    }
                }
                targetDecomposeDetailsDTO.setForecastYear(forecastYear);
                targetDecomposeDetailsDTO.setActualTotal(actualTotal);
                targetDecomposeDetailsDTO.setTargetPercentageComplete(targetPercentageComplete);
                //目标分解周欺数据集合
                targetDecomposeDetailsDTO.setDecomposeDetailCyclesDTOS(decomposeDetailCyclesDTOList);
            }
            return targetDecomposeDTO.setTargetDecomposeDetailsDTOS(targetDecomposeDetailsDTOList);
        } else {
            return targetDecomposeDTO;
        }
    }

    /**
     * 远程调用根据id目标分解id查询数据
     * @param targetDecomposeId 目标分解表主键
     * @return
     */
    @Override
    public TargetDecomposeDTO selectTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNotNull(targetDecomposeDTO)){
            R<IndicatorDTO> R = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
            IndicatorDTO data = R.getData();
            if (StringUtils.isNotNull(data)){
                targetDecomposeDTO.setIndicatorName(data.getIndicatorName());
            }
        }
        return targetDecomposeDTO;
    }

    /**
     * 远程调用根据id集合目标分解id查询数据
     * @param targetDecomposeIds 目标分解表主键集合
     * @return
     */
    @Override
    public List<TargetDecomposeDTO> selectTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)){
            List<Long> collect = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getTargetDecomposeId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)){
                R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(collect, SecurityConstants.INNER);
                List<IndicatorDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)){
                    for (int i = 0; i < targetDecomposeDTOS.size(); i++) {
                        targetDecomposeDTOS.get(i).setIndicatorName(data.get(i).getIndicatorName());
                    }
                }
            }

        }
        return targetDecomposeDTOS;
    }

    /**
     * 封装版本号
     *
     * @param targetDecomposeDTO
     * @return
     */
    private void packversion(TargetDecomposeDTO targetDecomposeDTO) {
        String versionNum = "V";
        List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDTOS = targetDecomposeHistoryMapper.selectTargetDecomposeHistoryByTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
        if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)) {
            if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)) {
                TargetDecomposeHistoryDTO targetDecomposeHistoryDTO = targetDecomposeHistoryDTOS.get(targetDecomposeHistoryDTOS.size() - 1);
                String version = targetDecomposeHistoryDTO.getVersion();
                String substring = version.substring(1, 2);
                int veri = Integer.parseInt(substring);
                versionNum = "V" + (veri + 1) + ".0";
            }
        }
        if (StringUtils.equals(versionNum, "V")) {
            targetDecomposeDTO.setVersion("V1.0");
        } else {
            targetDecomposeDTO.setVersion(versionNum);
        }

    }

    /**
     * 封装分解维度
     *
     * @param targetDecomposeDTO
     */
    private void packDecompositionDimension(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        targetDecomposeDimension.setTargetDecomposeDimensionId(targetDecomposeDTO.getTargetDecomposeDimensionId());

        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDTOS = targetDecomposeDimensionMapper.selectTargetDecomposeDimensionList(targetDecomposeDimension);
        StringBuilder targetDecomposeDimensionName;
        for (TargetDecomposeDimensionDTO decomposeDimensionDTO : targetDecomposeDimensionDTOS) {
            targetDecomposeDimensionName = new StringBuilder("");
            List<Map<String, String>> fileNameList = new ArrayList<>();
            String decompositionDimension = decomposeDimensionDTO.getDecompositionDimension();
            if (StringUtils.isNotEmpty(decompositionDimension)) {
                for (String dimension : decompositionDimension.split(",")) {
                    String info = TargetDecomposeDimensionCode.selectInfo(dimension);
                    String filedName = TargetDecomposeDimensionCode.selectFiledName(dimension);
                    String filedValue = TargetDecomposeDimensionCode.selectFiledValue(dimension);
                    TargetDecomposeDimensionCode.selectFiledName(dimension);
                    if (StringUtils.isNotNull(info)) {
                        targetDecomposeDimensionName.append(info).append("+");
                    }
                    Map<String, String> fileNameMap = new HashMap<>();
                    fileNameMap.put("label", info);
                    fileNameMap.put("value", filedName);
                    fileNameMap.put("name", filedValue);
                    fileNameList.add(fileNameMap);
                }
                String substring = targetDecomposeDimensionName.substring(0, targetDecomposeDimensionName.length() - 1);
                targetDecomposeDTO.setFileNameList(fileNameList);
                targetDecomposeDTO.setDecompositionDimension(substring);
            }
        }
    }

    /**
     * 返回预测周期字段
     *
     * @param targetDecomposeDTO
     * @return
     */
    private String packForecastCycle(TargetDecomposeDTO targetDecomposeDTO) {
        String forecastCycle = null;
        if (targetDecomposeDTO.getTimeDimension() == 1) {
            int year = DateUtils.getYear();
            forecastCycle = year + "年";
        } else if (targetDecomposeDTO.getTimeDimension() == 2) {
            if (DateUtils.getMonth() <= 6) {
                forecastCycle = "上半年";
            } else {
                forecastCycle = "下半年";
            }
        } else if (targetDecomposeDTO.getTimeDimension() == 3) {
            forecastCycle = Convert.int2chineseNum(DateUtils.getQuarter()) + "季度";
        } else if (targetDecomposeDTO.getTimeDimension() == 4) {
            forecastCycle = Convert.int2chineseNum(DateUtils.getMonth()) + "月";
        } else if (targetDecomposeDTO.getTimeDimension() == 5) {
            forecastCycle = Convert.int2chineseNum(DateUtils.getDayOfWeek()) + "周";
        }
        return forecastCycle;
    }

    /**
     * 查询目标分解(销售订单)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    @Override
    public TargetDecomposeDTO selectOrderTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        return this.packSelectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 查询目标分解(销售收入)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    @Override
    public TargetDecomposeDTO selectIncomeTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        return this.packSelectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 查询目标分解(销售回款)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    @Override
    public TargetDecomposeDTO selectReturnedTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        return this.packSelectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 查询目标分解(自定义)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    @Override
    public TargetDecomposeDTO selectCustomTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        return this.packSelectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 查询经营结果分析报表列表
     *
     * @param targetDecomposeDTO 目标分解(经营结果分析)表
     * @return
     */
    @Override
    public List<TargetDecomposeDTO> resultList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //指标code集合
        List<String> list = new ArrayList<>();
        //订单（不含税）
        list.add(IndicatorCode.ORDER.getCode());
        //销售收入
        list.add(IndicatorCode.INCOME.getCode());
        //回款金额（含税）
        list.add(IndicatorCode.RECEIVABLE.getCode());
        R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByCodeList(list, SecurityConstants.INNER);
        if (StringUtils.isEmpty(listR.getData())) {
            throw new ServiceException("指标不存在 请联系管理员！");
        } else {
            List<Long> collect = listR.getData().stream().map(IndicatorDTO::getIndicatorId).collect(Collectors.toList());
            targetDecompose.setIndicatorIds(collect);
        }
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectResultList(targetDecompose);
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)){
            List<Long> indicatorIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).collect(Collectors.toList());
            R<List<IndicatorDTO>> listR1 = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR1.getData();
            if (StringUtils.isNotEmpty(data)){
                for (int i = 0; i < targetDecomposeDTOS.size(); i++) {
                    targetDecomposeDTOS.get(i).setIndicatorName(data.get(i).getIndicatorName());
                }
            }
        }
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                //年度预测
                BigDecimal forecastYear = decomposeDTO.getForecastYear();
                //年度实际
                BigDecimal actualTotal = decomposeDTO.getActualTotal();
                //目标分解值
                BigDecimal decomposeTarget = decomposeDTO.getDecomposeTarget();
                //预测偏差
                BigDecimal forecastDeviation = new BigDecimal("0");
                //预测与目标偏差率
                BigDecimal forecastDeviationRate = new BigDecimal("0");
                //目标完成率
                BigDecimal targetPercentageComplete = new BigDecimal("0");
                if (forecastYear != null) {
                    if (decomposeTarget != null) {
                        forecastDeviation = forecastYear.subtract(decomposeTarget).setScale(2);
                    }
                }
                //预测偏差
                decomposeDTO.setForecastDeviation(forecastDeviation);
                if (forecastDeviation != null && forecastDeviation.compareTo(new BigDecimal("0")) != 0) {
                    if (decomposeTarget != null && decomposeTarget.compareTo(new BigDecimal("0")) != 0) {
                        forecastDeviationRate = forecastDeviation.divide(decomposeTarget, BigDecimal.ROUND_CEILING);
                    }
                }
                //预测与目标偏差率
                decomposeDTO.setForecastDeviationRate(forecastDeviationRate);
                if (actualTotal != null && actualTotal.compareTo(new BigDecimal("0")) != 0) {
                    if (decomposeTarget != null && decomposeTarget.compareTo(new BigDecimal("0")) != 0) {
                        targetPercentageComplete = actualTotal.divide(decomposeTarget, BigDecimal.ROUND_CEILING);
                    }
                }
                //目标完成率
                decomposeDTO.setTargetPercentageComplete(targetPercentageComplete);
            }
            //指标id
            List<Long> collect1 = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).distinct().collect(Collectors.toList());
            //根据指标id分组
            Map<Long, List<TargetDecomposeDTO>> listMap = targetDecomposeDTOS.parallelStream().collect(Collectors.groupingBy(TargetDecomposeDTO::getIndicatorId));
            if (StringUtils.isNotEmpty(collect1)) {
                targetDecomposeDTOS.clear();
                for (Long aLong : collect1) {
                    BigDecimal targetPercentageCompleteAve = new BigDecimal("0");
                    List<TargetDecomposeDTO> targetDecomposeDTOS1 = listMap.get(aLong);
                    if (StringUtils.isNotEmpty(targetDecomposeDTOS1)) {
                        BigDecimal sum = targetDecomposeDTOS1.stream()
                                .filter(friend -> friend.getTargetPercentageComplete() != null)
                                .map(TargetDecomposeDTO::getTargetPercentageComplete)
                                .reduce(BigDecimal.ZERO, BigDecimal::add);
                        targetPercentageCompleteAve = sum.divide(new BigDecimal(String.valueOf(targetDecomposeDTOS1.size() - 1)), BigDecimal.ROUND_CEILING);
                        //目标完成率平均值
                        for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS1) {
                            decomposeDTO.setTargetPercentageCompleteAve(targetPercentageCompleteAve);
                        }
                    }
                    targetDecomposeDTOS.addAll(targetDecomposeDTOS1);
                }
            }

        }

        return targetDecomposeDTOS;
    }

    /**
     * 封装统一查询
     *
     * @param targetDecomposeId
     * @return
     */
    public TargetDecomposeDTO packSelectTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        //目标分解主表数据
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNotNull(targetDecomposeDTO)){
            R<IndicatorDTO> R = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
            IndicatorDTO data = R.getData();
            if (StringUtils.isNotNull(data)){
                targetDecomposeDTO.setIndicatorName(data.getIndicatorName());
            }
        }
        this.packDecompositionDimension(targetDecomposeDTO);
        //目标分解详情数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeId);
        this.packRemote(targetDecomposeDetailsDTOList);
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                //目标分解周欺数据集合
                targetDecomposeDetailsDTO.setDecomposeDetailCyclesDTOS(decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId()));
            }
            return targetDecomposeDTO.setTargetDecomposeDetailsDTOS(targetDecomposeDetailsDTOList);
        } else {
            return targetDecomposeDTO;
        }
    }

    /**
     * 查询目标分解(销售订单)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 目标分解(销售订单)表
     */
    @Override
    public List<TargetDecomposeDTO> selectOrderList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);

        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.ORDER.getCode());
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                decomposeDTO.setIndicatorName(IndicatorCode.ORDER.getInfo());
                this.packDecompositionDimension(decomposeDTO);
            }
        }
        return targetDecomposeDTOS;
    }

    /**
     * 分页查询滚动预测表列表
     *
     * @param targetDecomposeDTO 滚动预测表列表
     * @return
     */
    @Override
    public List<TargetDecomposeDTO> rollPageList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //指标code集合
        List<String> list = new ArrayList<>();
        //订单（不含税）
        list.add(IndicatorCode.ORDER.getCode());
        //销售收入
        list.add(IndicatorCode.INCOME.getCode());
        //回款金额（含税）
        list.add(IndicatorCode.RECEIVABLE.getCode());
        R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByCodeList(list, SecurityConstants.INNER);
        if (StringUtils.isEmpty(listR.getData())) {
            throw new ServiceException("指标不存在 请联系管理员！");
        } else {
            List<Long> collect = listR.getData().stream().map(IndicatorDTO::getIndicatorId).collect(Collectors.toList());
            targetDecompose.setIndicatorIds(collect);
        }
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectRollPageList(targetDecompose);
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)){
            List<Long> indicatorIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).collect(Collectors.toList());

            //远程获取指标名称
            R<List<IndicatorDTO>> listR1 = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR1.getData();
            if (StringUtils.isNotEmpty(data)){
                for (int i = 0; i < targetDecomposeDTOS.size(); i++) {
                    targetDecomposeDTOS.get(i).setIndicatorName(data.get(i).getIndicatorName());
                }
            }
        }
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                this.packDecompositionDimension(decomposeDTO);
            }
        }
        return targetDecomposeDTOS;
    }

    /**
     * 查询目标分解(销售收入)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售收入)表
     * @return 目标分解(销售收入)表
     */
    @Override
    public List<TargetDecomposeDTO> selectIncomeList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);

        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.INCOME.getCode());
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                decomposeDTO.setIndicatorName(IndicatorCode.INCOME.getInfo());
                this.packDecompositionDimension(decomposeDTO);
            }
        }
        return targetDecomposeDTOS;
    }

    /**
     * 查询目标分解(销售回款)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 目标分解(销售回款)表
     */
    @Override
    public List<TargetDecomposeDTO> selectReturnedList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.RECEIVABLE.getCode());
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);

        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                decomposeDTO.setIndicatorName(IndicatorCode.RECEIVABLE.getInfo());
                this.packDecompositionDimension(decomposeDTO);
            }
        }
        return targetDecomposeDTOS;
    }

    /**
     * 查询目标分解(自定义)表列表
     *
     * @param targetDecomposeDTO 目标分解(自定义)表
     * @return 目标分解(自定义)表
     */
    @Override
    public List<TargetDecomposeDTO> selectCustomList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.CUSTOM.getCode());
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);

        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            List<Long> indicatorIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).collect(Collectors.toList());

            for (int i = 0; i < targetDecomposeDTOS.size(); i++) {
                //远程获取指标名称
                R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
                List<IndicatorDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)){
                    targetDecomposeDTOS.get(i).setIndicatorName(data.get(i).getIndicatorName());
                }
                this.packDecompositionDimension(targetDecomposeDTOS.get(i));
            }

        }
        return targetDecomposeDTOS;
    }

    /**
     * 新增目标分解(销售订单)表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetDecomposeDTO insertOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.ORDER.getCode());
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(Calendar.getInstance().get(Calendar.YEAR) + "年已创建该维度目标分解，无需重复创建。");
        }
        targetDecompose.setCreateBy(SecurityUtils.getUserId());
        targetDecompose.setCreateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //待录入
        targetDecompose.setStatus(Constants.ZERO);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.ORDER.getCode(), SecurityConstants.INNER);
        if (StringUtils.isNull(indicatorDTOR.getData())) {
            throw new ServiceException("指标不存在！请联系管理员");
        }
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());

        try {
            targetDecomposeMapper.insertTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("插入目标分解主表失败");

        }
        //插入周期表和详细信息表
        this.packInsertTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        // todo 发送通知
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 新增目标分解(销售收入)表
     *
     * @param targetDecomposeDTO 目标分解(销售收入)表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetDecomposeDTO insertIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.INCOME.getCode());
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(Calendar.getInstance().get(Calendar.YEAR) + "年已创建该维度目标分解，无需重复创建。");
        }
        targetDecompose.setCreateBy(SecurityUtils.getUserId());
        targetDecompose.setCreateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //待录入
        targetDecompose.setStatus(Constants.ZERO);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.INCOME.getCode(), SecurityConstants.INNER);
        if (StringUtils.isNull(indicatorDTOR.getData())) {
            throw new ServiceException("指标不存在！请联系管理员");
        }
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());

        try {
            targetDecomposeMapper.insertTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("插入目标分解主表失败");

        }
        //插入周期表和详细信息表
        this.packInsertTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        // todo 发送通知
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 新增目标分解(销售回款)表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetDecomposeDTO insertReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.RECEIVABLE.getCode());
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(Calendar.getInstance().get(Calendar.YEAR) + "年已创建该维度目标分解，无需重复创建。");
        }
        targetDecompose.setCreateBy(SecurityUtils.getUserId());
        targetDecompose.setCreateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //待录入
        targetDecompose.setStatus(Constants.ZERO);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.RECEIVABLE.getCode(), SecurityConstants.INNER);
        if (StringUtils.isNull(indicatorDTOR.getData())) {
            throw new ServiceException("指标不存在！请联系管理员");
        }
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.RECEIVABLE.getCode());
        try {
            targetDecomposeMapper.insertTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("插入目标分解主表失败");

        }
        //插入周期表和详细信息表
        this.packInsertTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        // todo 发送通知
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 新增目标分解(自定义)表
     *
     * @param targetDecomposeDTO 目标分解(自定义)表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetDecomposeDTO insertCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.CUSTOM.getCode());
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(Calendar.getInstance().get(Calendar.YEAR) + "年已创建该维度目标分解，无需重复创建。");
        }
        targetDecompose.setCreateBy(SecurityUtils.getUserId());
        targetDecompose.setCreateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //待录入
        targetDecompose.setStatus(Constants.ZERO);
        try {
            targetDecomposeMapper.insertTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("插入目标分解主表失败");

        }
        //插入周期表和详细信息表
        this.packInsertTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        // todo 发送通知
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 修改滚动预测详情
     *
     * @param targetDecomposeDTO 修改滚动预测详情
     * @return
     */
    @Override
    @Transactional
    public int updateRollTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        int i = 0;
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            i = targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
        return i;
    }

    /**
     * 修改经营结果分析报表详情
     *
     * @param targetDecomposeDTO 修改滚动预测详情
     * @return
     */
    @Override
    @Transactional
    public int updateResultTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        int i = 0;
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            i = targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
        return i;
    }

    /**
     * 校检数据
     *
     * @param targetDecomposeDTO
     */
    private void validTargetDecomposeData(TargetDecomposeDTO targetDecomposeDTO) {
        //分解目标值
        BigDecimal decomposeTarget = targetDecomposeDTO.getDecomposeTarget();
        //分解目标值比对
        BigDecimal validDecomposeTarget = new BigDecimal("0");

        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
            //汇总金额
            BigDecimal amountTarget = targetDecomposeDetailsDTO.getAmountTarget();
            if (null != amountTarget) {
                validDecomposeTarget = validDecomposeTarget.add(amountTarget);
            }
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO.getDecomposeDetailCyclesDTOS();
            //行的汇总金额
            BigDecimal validAmountTarget = new BigDecimal("0");
            for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                BigDecimal cycleForecast = decomposeDetailCyclesDTO.getCycleForecast();
                if (null != cycleForecast) {
                    validAmountTarget = validAmountTarget.add(cycleForecast);
                }
            }
            if (null != amountTarget) {
                if (amountTarget.compareTo(validAmountTarget) != 0) {
                    throw new ServiceException("汇总金额与行的预测总值不匹配！！！");
                }
            }

        }
        if (null != decomposeTarget) {
            if (decomposeTarget.compareTo(validDecomposeTarget) != 0) {
                throw new ServiceException("分解目标值与汇总金额总值不匹配！！！");
            }
        }

    }

    /**
     * 封装插入目标分解详情表和目标分解详细信息周期表
     *
     * @param targetDecomposeDTO
     * @param targetDecompose
     */
    public void packInsertTargetDecomposeData(TargetDecomposeDTO targetDecomposeDTO, TargetDecompose targetDecompose) {


        //目标分解详情表集合
        List<TargetDecomposeDetails> targetDecomposeDetailsList = new ArrayList<>();
        //目标分解详细信息周期表集合
        List<DecomposeDetailCycles> decomposeDetailCyclesList = new ArrayList<>();

        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //插入目标分解详情表
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            //目标分解详情表
            TargetDecomposeDetails targetDecomposeDetails = new TargetDecomposeDetails();
            BeanUtils.copyProperties(targetDecomposeDetailsDTOS.get(i), targetDecomposeDetails);
            //目标分解id
            targetDecomposeDetails.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
            targetDecomposeDetails.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeDetails.setCreateTime(DateUtils.getNowDate());
            targetDecomposeDetails.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeDetails.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            //目标分解详情表集合
            targetDecomposeDetailsList.add(targetDecomposeDetails);
        }
        try {
            if (StringUtils.isNotEmpty(targetDecomposeDetailsList)) {
                targetDecomposeDetailsMapper.batchTargetDecomposeDetails(targetDecomposeDetailsList);
            }
        } catch (Exception e) {
            throw new ServiceException("插入目标分解详情表失败");
        }
        //插入目标分解详细信息周期表
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            int cycleNumber = 1;
            //目标分解详细信息周期
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = targetDecomposeDetailsDTOS.get(i).getDecomposeDetailCyclesDTOS();
            for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                //目标分解详细信息周期表
                DecomposeDetailCycles decomposeDetailCycles = new DecomposeDetailCycles();
                BeanUtils.copyProperties(decomposeDetailCyclesDTO, decomposeDetailCycles);
                decomposeDetailCycles.setCycleNumber(cycleNumber);
                //目标分解id
                decomposeDetailCycles.setTargetDecomposeDetailsId(targetDecomposeDetailsList.get(i).getTargetDecomposeDetailsId());
                decomposeDetailCycles.setCreateBy(SecurityUtils.getUserId());
                decomposeDetailCycles.setCreateTime(DateUtils.getNowDate());
                decomposeDetailCycles.setUpdateTime(DateUtils.getNowDate());
                decomposeDetailCycles.setUpdateBy(SecurityUtils.getUserId());
                decomposeDetailCycles.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                cycleNumber++;
                //目标分解详细信息周期表集合
                decomposeDetailCyclesList.add(decomposeDetailCycles);
            }
        }
        try {
            if (StringUtils.isNotEmpty(decomposeDetailCyclesList)) {
                decomposeDetailCyclesMapper.batchDecomposeDetailCycles(decomposeDetailCyclesList);
            }
        } catch (Exception e) {
            throw new ServiceException("插入目标分解详细信息周期表失败");
        }
    }

    /**
     * 封装修改目标分解详情表和目标分解详细信息周期表
     *
     * @param targetDecomposeDTO
     * @param targetDecompose
     */
    public void packUpdateTargetDecomposeData(TargetDecomposeDTO targetDecomposeDTO, TargetDecompose targetDecompose) {

        //新增目标分解详情表集合
        List<TargetDecomposeDetails> targetDecomposeDetailsAddList = new ArrayList<>();
        //修改目标分解详情表集合
        List<TargetDecomposeDetails> targetDecomposeDetailsUpdateList = new ArrayList<>();
        //目标分解详情表所有集合
        List<TargetDecomposeDetails> targetDecomposeDetailsAllList = new ArrayList<>();


        //新增目标分解详细信息周期表集合
        List<DecomposeDetailCycles> decomposeDetailCyclesAddList = new ArrayList<>();
        //修改目标分解详细信息周期表集合
        List<DecomposeDetailCycles> decomposeDetailCyclesUpdateList = new ArrayList<>();
        //删除修改的目标分解详情表数据
        List<Long> longs = this.packUpdateTargetDecomposeDetails(targetDecomposeDTO);
        //传入数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //插入目标分解详情表
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            //目标分解详情表
            TargetDecomposeDetails targetDecomposeDetails = new TargetDecomposeDetails();
            BeanUtils.copyProperties(targetDecomposeDetailsDTOS.get(i), targetDecomposeDetails);
            //目标分解id
            targetDecomposeDetails.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
            if (null == targetDecomposeDetailsDTOS.get(i).getTargetDecomposeDetailsId()) {
                targetDecomposeDetails.setCreateBy(SecurityUtils.getUserId());
                targetDecomposeDetails.setCreateTime(DateUtils.getNowDate());
                targetDecomposeDetails.setUpdateTime(DateUtils.getNowDate());
                targetDecomposeDetails.setUpdateBy(SecurityUtils.getUserId());
                targetDecomposeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                //新增目标分解详情表集合
                targetDecomposeDetailsAddList.add(targetDecomposeDetails);
            } else {
                targetDecomposeDetails.setUpdateBy(SecurityUtils.getUserId());
                targetDecomposeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                //修改目标分解详情表集合
                targetDecomposeDetailsUpdateList.add(targetDecomposeDetails);
            }
        }

        try {
            if (StringUtils.isNotEmpty(targetDecomposeDetailsAddList)) {
                targetDecomposeDetailsMapper.batchTargetDecomposeDetails(targetDecomposeDetailsAddList);
            }
        } catch (Exception e) {
            throw new ServiceException("插入目标分解详情表失败");
        }

        try {
            if (StringUtils.isNotEmpty(targetDecomposeDetailsUpdateList)) {
                targetDecomposeDetailsMapper.updateTargetDecomposeDetailss(targetDecomposeDetailsUpdateList);
            }
        } catch (Exception e) {
            throw new ServiceException("修改目标分解详情表失败");
        }
        targetDecomposeDetailsAllList.addAll(targetDecomposeDetailsAddList);
        targetDecomposeDetailsAllList.addAll(targetDecomposeDetailsUpdateList);
        targetDecomposeDetailsAllList.sort(Comparator.comparing(TargetDecomposeDetails::getTargetDecomposeDetailsId));
        //修改目标分解详细信息周期表
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            //删除不存在的数据
            if (StringUtils.isNotEmpty(longs)) {
                try {
                    decomposeDetailCyclesMapper.logicDeleteDecomposeDetailCyclesByTargetDecomposeDetailsIds(longs, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除目标分解详细信息周期表失败");
                }
            }
            int cycleNumber = 1;
            //接收目标分解详细信息周期数据
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = targetDecomposeDetailsDTOS.get(i).getDecomposeDetailCyclesDTOS();
            for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                //目标分解详细信息周期表
                DecomposeDetailCycles decomposeDetailCycles = new DecomposeDetailCycles();
                BeanUtils.copyProperties(decomposeDetailCyclesDTO, decomposeDetailCycles);
                decomposeDetailCycles.setCycleNumber(cycleNumber);
                if (null == targetDecomposeDetailsDTOS.get(i).getTargetDecomposeDetailsId()) {
                    //目标分解id
                    decomposeDetailCycles.setTargetDecomposeDetailsId(targetDecomposeDetailsAllList.get(i).getTargetDecomposeDetailsId());
                    decomposeDetailCycles.setCreateBy(SecurityUtils.getUserId());
                    decomposeDetailCycles.setCreateTime(DateUtils.getNowDate());
                    decomposeDetailCycles.setUpdateTime(DateUtils.getNowDate());
                    decomposeDetailCycles.setUpdateBy(SecurityUtils.getUserId());
                    decomposeDetailCycles.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    //目标分解详细信息周期表集合
                    decomposeDetailCyclesAddList.add(decomposeDetailCycles);
                } else {
                    decomposeDetailCycles.setUpdateTime(DateUtils.getNowDate());
                    decomposeDetailCycles.setUpdateBy(SecurityUtils.getUserId());
                    //目标分解详细信息周期表集合
                    decomposeDetailCyclesUpdateList.add(decomposeDetailCycles);
                }
                cycleNumber++;
            }
        }
        if (StringUtils.isNotEmpty(decomposeDetailCyclesAddList)) {
            try {
                decomposeDetailCyclesMapper.batchDecomposeDetailCycles(decomposeDetailCyclesAddList);
            } catch (Exception e) {
                throw new ServiceException("插入目标分解详细信息周期表失败");
            }
        }
        if (StringUtils.isNotEmpty(decomposeDetailCyclesUpdateList)) {
            try {
                decomposeDetailCyclesMapper.updateDecomposeDetailCycless(decomposeDetailCyclesUpdateList);
            } catch (Exception e) {
                throw new ServiceException("修改目标分解详细信息周期表失败");
            }
        }
    }

    /**
     * 删除修改的目标分解详情表数据
     *
     * @param targetDecomposeDTO
     */
    private List<Long> packUpdateTargetDecomposeDetails(TargetDecomposeDTO targetDecomposeDTO) {
        //传入数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //数据库已存在的数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());

        //sterm流求差集
        List<Long> collect = targetDecomposeDetailsDTOList.stream().filter(a ->
                !targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList()).contains(a.getTargetDecomposeDetailsId())
        ).collect(Collectors.toList()).stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
        //删除数据
        if (StringUtils.isNotEmpty(collect)) {
            try {
                targetDecomposeDetailsMapper.logicDeleteTargetDecomposeDetailsByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解详情表失败");
            }
        }
        return collect;
    }

    /**
     * 修改目标分解(销售订单)表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        int i = 0;
        //校检数据
        this.validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.ORDER.getCode());

        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            i = targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
        // todo 发送通知
        return i;
    }

    /**
     * 修改目标分解(销售收入)表
     *
     * @param targetDecomposeDTO 目标分解(销售收入)表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        int i = 0;
        //校检数据
        this.validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.INCOME.getCode());

        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            i = targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
        //todo 发送通知
        return i;
    }

    /**
     * 修改目标分解(销售回款)表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        int i = 0;
        //校检数据
        this.validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.RECEIVABLE.getCode());

        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            i = targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
        //todo 发送通知
        return i;
    }

    /**
     * 修改目标分解(自定义)表
     *
     * @param targetDecomposeDTO 目标分解(自定义)表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        int i = 0;
        //校检数据
        this.validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.CUSTOM.getCode());

        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            i = targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
        //todo 发送通知
        return i;
    }

    /**
     * 逻辑批量删除目标分解(销售订单)表
     *
     * @param targetDecomposeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteOrderTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
    }

    /**
     * 逻辑批量删除目标分解(销售收入)表
     *
     * @param targetDecomposeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteIncomeTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
    }

    /**
     * 逻辑批量删除目标分解(销售回款)表
     *
     * @param targetDecomposeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteReturnedTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
    }

    /**
     * 逻辑批量删除目标分解(自定义)表
     *
     * @param targetDecomposeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteCustomTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
    }


    /**
     * 逻辑删除目标分解(销售订单)表信息
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteOrderTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeId(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(销售收入)表信息
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteIncomeTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeId(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(销售回款)表信息
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteReturnedTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeId(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(自定义)表信息
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteCustomTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeId(targetDecomposeDTO);
    }

    /**
     * 封装统一删除接口
     *
     * @param targetDecomposeDTO
     * @return
     */
    public int packLogicDeleteTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        targetDecompose.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //目标分解详情表
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        //目标分解详情表主键id集合
        List<Long> collect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            try {
                targetDecomposeDetailsMapper.logicDeleteTargetDecomposeDetailsByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解详情表失败");
            }
            try {
                decomposeDetailCyclesMapper.logicDeleteDecomposeDetailCyclesByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解周期表失败");
            }
        }
        //删除历史版本数据
        this.packLogicDeleteTargetDecomposeHistoryData(targetDecomposeDTO);
        return targetDecomposeMapper.logicDeleteTargetDecomposeByTargetDecomposeId(targetDecompose);
    }

    /**
     * 封装统一删除历史版本数据
     */
    private void packLogicDeleteTargetDecomposeHistoryData(TargetDecomposeDTO targetDecomposeDTO) {
        //目标分解历史版本表集合
        List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDTOS = new ArrayList<>();
        //目标分解详情快照表集合
        List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS = new ArrayList<>();
        try {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistoryDTOS = targetDecomposeHistoryMapper.selectTargetDecomposeHistoryList(targetDecomposeHistory);
            //删除历史版本
            targetDecomposeHistoryMapper.logicDeleteTargetDecomposeHistoryByTargetDecomposeId(targetDecomposeHistory);
        } catch (Exception e) {
            throw new ServiceException("删除关联历史版本失败");
        }

        if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)) {
            List<Long> collect1 = targetDecomposeHistoryDTOS.stream().map(TargetDecomposeHistoryDTO::getTargetDecomposeHistoryId).collect(Collectors.toList());
            try {

                if (StringUtils.isNotEmpty(collect1)) {
                    decomposeDetailsSnapshotDTOS = decomposeDetailsSnapshotMapper.selectDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(collect1);
                    //删除历史版本
                    decomposeDetailsSnapshotMapper.logicDeleteDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
                }

            } catch (Exception e) {
                throw new ServiceException("删除关联历史版本详情失败");
            }
        }
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)) {
            List<Long> collect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getDecomposeDetailsSnapshotId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    detailCyclesSnapshotMapper.logicDeleteDetailCyclesSnapshotByDecomposeDetailsSnapshotIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除目标分解详情周期快照失败");
                }
            }

        }
    }

    /**
     * 封装统一批量删除接口
     *
     * @param targetDecomposeIds
     * @return
     */
    public int packLogicDeleteTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        //目标分解详情表
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeIds(targetDecomposeIds);
        //目标分解详情表主键id集合
        List<Long> collect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            try {
                targetDecomposeDetailsMapper.logicDeleteTargetDecomposeDetailsByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解详情表失败");
            }
            try {
                decomposeDetailCyclesMapper.logicDeleteDecomposeDetailCyclesByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解周期表失败");
            }
        }
        this.packLogicDeleteTargetDecomposeHistoryDatas(targetDecomposeIds);
        return targetDecomposeMapper.logicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 封装统一批量删除历史版本数据
     *
     * @param targetDecomposeIds
     */
    private void packLogicDeleteTargetDecomposeHistoryDatas(List<Long> targetDecomposeIds) {
        //目标分解历史版本表集合
        List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDTOS = new ArrayList<>();
        //目标分解详情快照表集合
        List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS = new ArrayList<>();
        try {
            targetDecomposeHistoryDTOS = targetDecomposeHistoryMapper.selectTargetDecomposeHistoryByTargetDecomposeIds(targetDecomposeIds);
            //删除历史版本
            targetDecomposeHistoryMapper.logicDeleteTargetDecomposeHistoryByTargetDecomposeIds(targetDecomposeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除关联历史版本失败");
        }

        if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)) {
            List<Long> collect1 = targetDecomposeHistoryDTOS.stream().map(TargetDecomposeHistoryDTO::getTargetDecomposeHistoryId).collect(Collectors.toList());
            try {

                if (StringUtils.isNotEmpty(collect1)) {
                    decomposeDetailsSnapshotDTOS = decomposeDetailsSnapshotMapper.selectDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(collect1);
                    //删除历史版本
                    decomposeDetailsSnapshotMapper.logicDeleteDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
                }

            } catch (Exception e) {
                throw new ServiceException("删除关联历史版本详情失败");
            }
        }
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)) {
            List<Long> collect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getDecomposeDetailsSnapshotId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    detailCyclesSnapshotMapper.logicDeleteDetailCyclesSnapshotByDecomposeDetailsSnapshotIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除目标分解详情周期快照失败");
                }
            }

        }
    }

    /**
     * 批量新增目标分解(销售订单)表信息
     *
     * @param targetDecomposeDtos 目标分解(销售订单)表对象
     */

    public int insertTargetDecomposes(List<TargetDecomposeDTO> targetDecomposeDtos) {
        List<TargetDecompose> targetDecomposeList = new ArrayList();

        for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDtos) {
            TargetDecompose targetDecompose = new TargetDecompose();
            BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
            targetDecompose.setCreateBy(SecurityUtils.getUserId());
            targetDecompose.setCreateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateBy(SecurityUtils.getUserId());
            targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeList.add(targetDecompose);
        }
        return targetDecomposeMapper.batchTargetDecompose(targetDecomposeList);
    }

    /**
     * 批量修改目标分解(销售订单)表信息
     *
     * @param targetDecomposeDtos 目标分解(销售订单)表对象
     */

    public int updateTargetDecomposes(List<TargetDecomposeDTO> targetDecomposeDtos) {
        List<TargetDecompose> targetDecomposeList = new ArrayList();

        for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDtos) {
            TargetDecompose targetDecompose = new TargetDecompose();
            BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
            targetDecompose.setCreateBy(SecurityUtils.getUserId());
            targetDecompose.setCreateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeList.add(targetDecompose);
        }
        return targetDecomposeMapper.updateTargetDecomposes(targetDecomposeList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetDecompose(List<TargetDecomposeExcel> list) {
        List<TargetDecompose> targetDecomposeList = new ArrayList<>();
        list.forEach(l -> {
            TargetDecompose targetDecompose = new TargetDecompose();
            BeanUtils.copyProperties(l, targetDecompose);
            targetDecompose.setCreateBy(SecurityUtils.getUserId());
            targetDecompose.setCreateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateBy(SecurityUtils.getUserId());
            targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeList.add(targetDecompose);
        });
        try {
            targetDecomposeMapper.batchTargetDecompose(targetDecomposeList);
        } catch (Exception e) {
            throw new ServiceException("导入目标分解(销售订单)表失败");
        }
    }

    /**
     * 解析Excel
     *
     * @param file
     * @return
     */
    @Override
    public TargetDecomposeDTO excelParseObject(MultipartFile file) {
        //返回详情数据
        TargetDecomposeDTO targetDecomposeDTO = new TargetDecomposeDTO();
        //目标分解详情数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = new ArrayList<>();
        //分解维度顺序排序
        Map<String, List<String>> mapAllData = new LinkedHashMap<>();
        //Excel目标分解周期数据
        List<List<String>> cyclesExcelData = new ArrayList<>();
        try {
            //构建读取器
            ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
            List<Map<Integer, String>> listMap = read.doReadAllSync();
            //人员列下标
            AtomicReference<Integer> employeeNameKey = new AtomicReference<>(0);
            //人员code集合
            List<String> employeeCodes = new ArrayList<>();
            //区域下标
            AtomicReference<Integer> areaCodesKey = new AtomicReference<>(0);
            //区域code集合
            List<String> areaCodes = new ArrayList<>();
            //部门下标
            AtomicReference<Integer> departmentCodeKey = new AtomicReference<>(0);
            //部门code集合
            List<String> departmentCodes = new ArrayList<>();

            //行业下标
            AtomicReference<Integer> industryCodeKey = new AtomicReference<>(0);
            //行业code集合
            List<String> industryCodes = new ArrayList<>();

            //省份下标
            AtomicReference<Integer> provinceNameKey = new AtomicReference<>(0);
            //省份名称集合
            List<String> provinceNames = new ArrayList<>();

            //产品下标
            AtomicReference<Integer> productCodeKey = new AtomicReference<>(0);
            //产品code集合
            List<String> productCodes = new ArrayList<>();

            //时间维度下标
            AtomicReference<Integer> timeDimensionKey = new AtomicReference<>(0);
            for (int i = 0; i < listMap.size(); i++) {
                Map<Integer, String> map = listMap.get(i);
                map.forEach((key, value) -> {
                    if (StringUtils.equals(map.get(key), DecompositionDimension.EMPLOYEE.getInfo())) {

                        employeeNameKey.set(key);
                    } else if (StringUtils.equals(map.get(key), DecompositionDimension.AREA.getInfo())) {
                        areaCodesKey.set(key);
                    } else if (StringUtils.equals(map.get(key), DecompositionDimension.DEPARTMENT.getInfo())) {
                        departmentCodeKey.set(key);
                    } else if (StringUtils.equals(map.get(key), DecompositionDimension.INDUSTRY.getInfo())) {
                        industryCodeKey.set(key);
                    } else if (StringUtils.equals(map.get(key), DecompositionDimension.REGION.getInfo())) {
                        provinceNameKey.set(key);
                    } else if (StringUtils.equals(map.get(key), DecompositionDimension.PRODUCT.getInfo())) {
                        productCodeKey.set(key);
                    } else if (StringUtils.equals(map.get(key), "时间维度")) {
                        timeDimensionKey.set(key);
                    }
                });

                if (i > 1) {
                    List<String> list = new ArrayList<>();
                    map.forEach((key, value) -> {
                        if (0 != employeeNameKey.get()) {
                            if (key == employeeNameKey.get()){
                                employeeCodes.add(map.get(employeeNameKey.get()));
                                mapAllData.put(DecompositionDimension.EMPLOYEE.getInfo(), employeeCodes);
                            }

                        }
                        if (0 != areaCodesKey.get()) {
                            if (key == areaCodesKey.get()){
                                areaCodes.add(map.get(areaCodesKey.get()));
                                mapAllData.put(DecompositionDimension.AREA.getInfo(), areaCodes);
                            }

                        }
                        if (0 != departmentCodeKey.get()) {
                            if (key == departmentCodeKey.get()){
                                departmentCodes.add(map.get(departmentCodeKey.get()));
                                mapAllData.put(DecompositionDimension.DEPARTMENT.getInfo(), departmentCodes);
                            }

                        }
                        if (0 != industryCodeKey.get()) {
                            if (key == industryCodeKey.get()){
                                industryCodes.add(map.get(industryCodeKey.get()));
                                mapAllData.put(DecompositionDimension.INDUSTRY.getInfo(), industryCodes);
                            }

                        }
                        if (0 != provinceNameKey.get()) {
                            if (key == provinceNameKey.get()){
                                provinceNames.add(map.get(provinceNameKey.get()));
                                mapAllData.put(DecompositionDimension.REGION.getInfo(), provinceNames);
                            }

                        }
                        if (0 != productCodeKey.get()) {
                            if (key == productCodeKey.get()){
                                productCodes.add(map.get(productCodeKey.get()));
                                mapAllData.put(DecompositionDimension.PRODUCT.getInfo(), productCodes);
                            }

                        }
                        if (key >= timeDimensionKey.get()) {
                            list.add(map.get(key));
                        }
                    });
                    cyclesExcelData.add(list);
                }

            }
           return this.packExcelData(targetDecomposeDTO, mapAllData, targetDecomposeDetailsDTOS,cyclesExcelData);


        } catch (IOException e) {
            throw new ServiceException("导入人员信息配置Excel失败");
        }
    }

    /**
     * 取出最大的list数据
     *
     * @param mapAllData
     * @return
     */
    private int packExcelListData(Map<String, List<String>> mapAllData) {
        List<Integer> list = new ArrayList<>();
        mapAllData.forEach((key, value) -> {
            list.add(mapAllData.get(key).size());
        });
        list.sort(Integer::compareTo);
        return list.get(list.size()-1);
    }

    /**
     * 目标分解(销售订单)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeExcel> exportOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        targetDecomposeDTO.setTargetDecomposeType(TargetDecomposeType.ORDER.getCode());
        return this.packExportOrderTargetDecompose(targetDecomposeDTO);
    }

    /**
     * 目标分解(销售收入)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeExcel> exportIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        targetDecomposeDTO.setTargetDecomposeType(TargetDecomposeType.INCOME.getCode());
        return this.packExportOrderTargetDecompose(targetDecomposeDTO);
    }

    /**
     * 目标分解(销售回款)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeExcel> exportReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        targetDecomposeDTO.setTargetDecomposeType(TargetDecomposeType.RECEIVABLE.getCode());
        return this.packExportOrderTargetDecompose(targetDecomposeDTO);
    }

    /**
     * 目标分解(自定义)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeExcel> exportCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        targetDecomposeDTO.setTargetDecomposeType(TargetDecomposeType.CUSTOM.getCode());
        return this.packExportOrderTargetDecompose(targetDecomposeDTO);
    }


    /**
     * 移交预测负责人
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    @Transactional
    public int turnOverPrincipalEmployee(TargetDecomposeDTO targetDecomposeDTO) {
        List<TargetDecomposeDetails> targetDecomposeDetailsList = new ArrayList<>();
        List<Long> targetDecomposeIds = targetDecomposeDTO.getTargetDecomposeIds();
        //目标分解详情集合
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeIds(targetDecomposeIds);
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                //比对人员id 只移交自己的
                if (targetDecomposeDetailsDTO.getPrincipalEmployeeId() == SecurityUtils.getEmployeeId()) {
                    TargetDecomposeDetails targetDecomposeDetails = new TargetDecomposeDetails();
                    targetDecomposeDetails.setTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                    targetDecomposeDetails.setPrincipalEmployeeId(targetDecomposeDTO.getPrincipalEmployeeId());
                    targetDecomposeDetailsList.add(targetDecomposeDetails);
                }
            }
        }
        if (StringUtils.isNotEmpty(targetDecomposeDetailsList)) {
            try {
                return targetDecomposeDetailsMapper.updateTargetDecomposeDetailss(targetDecomposeDetailsList);
                // todo 发送通知
            } catch (Exception e) {
                throw new ServiceException("移交预测负责人失败");
            }
        } else {
            throw new ServiceException("暂无可移交的数据！");
        }
    }

    /**
     * 远程调用根据目标分解id查询目标分解详情数据
     * @param targetDecomposeId
     * @return
     */
    @Override
    public List<TargetDecomposeDetailsDTO> selectTargetDecomposeDetailsByTargetDecomposeId(Long targetDecomposeId) {
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeId);
        this.packRemote(targetDecomposeDetailsDTOList);
        return targetDecomposeDetailsDTOList;
    }

    /**
     * 传入实体类根据条件查询
     * @param targetDecomposeDetailsDTO
     * @return
     */
    @Override
    public List<TargetDecomposeDetailsDTO> getDecomposeDetails(TargetDecomposeDetailsDTO targetDecomposeDetailsDTO) {
        TargetDecomposeDetails targetDecomposeDetails = new TargetDecomposeDetails();
        BeanUtils.copyProperties(targetDecomposeDetailsDTO,targetDecomposeDetails);
        return targetDecomposeDetailsMapper.selectTargetDecomposeDetailsList(targetDecomposeDetails);
    }

    /**
     * 封装远程调用数据
     * @param targetDecomposeDetailsDTOList
     * @return
     */
    public void packRemote(List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList) {
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)){
            //人员id集合
            List<Long> employeeIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getEmployeeId).collect(Collectors.toList());

            //人员id集合滚动预测负责人
            List<Long> principalEmployeeIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getPrincipalEmployeeId).collect(Collectors.toList());

            //部门id集合
            List<Long> departmentIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getDepartmentId).collect(Collectors.toList());

            //省份id集合
            Set<Long> regionIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getRegionId).collect(Collectors.toSet());

            //行业id集合
            List<Long> industryIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getIndustryId).collect(Collectors.toList());
            //人员远程
            if (StringUtils.isNotEmpty(employeeIdCollect)){
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(employeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)){
                    for (int i = 0; i < targetDecomposeDetailsDTOList.size(); i++) {
                        targetDecomposeDetailsDTOList.get(i).setEmployeeId(data.get(i).getEmployeeId());
                        targetDecomposeDetailsDTOList.get(i).setEmployeeName(data.get(i).getEmployeeName());
                    }
                }
            }
            //人员远程
            if (StringUtils.isNotEmpty(principalEmployeeIdCollect)){
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(principalEmployeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)){
                    for (int i = 0; i < targetDecomposeDetailsDTOList.size(); i++) {
                        targetDecomposeDetailsDTOList.get(i).setPrincipalEmployeeId(data.get(i).getEmployeeId());
                        targetDecomposeDetailsDTOList.get(i).setPrincipalEmployeeName(data.get(i).getEmployeeName());
                    }
                }
            }
            //部门远程
            if (StringUtils.isNotEmpty(departmentIdCollect)){
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(departmentIdCollect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)){
                    for (int i = 0; i < targetDecomposeDetailsDTOList.size(); i++) {
                       targetDecomposeDetailsDTOList.get(i).setDepartmentId(data.get(i).getDepartmentId());
                        targetDecomposeDetailsDTOList.get(i).setDepartmentName(data.get(i).getDepartmentName());
                    }
                }
            }
            //省份远程
            if (StringUtils.isNotEmpty(regionIdCollect)){
                R<List<RegionDTO>> regionsByIds = remoteRegionService.getRegionsByIds(regionIdCollect, SecurityConstants.INNER);
                List<RegionDTO> data = regionsByIds.getData();
                if (StringUtils.isNotEmpty(data)){
                    for (int i = 0; i < targetDecomposeDetailsDTOList.size(); i++) {
                        targetDecomposeDetailsDTOList.get(i).setRegionId(data.get(i).getRegionId());
                        targetDecomposeDetailsDTOList.get(i).setRegionName(data.get(i).getRegionName());
                    }
                }
            }
            //行业远程
            if (StringUtils.isNotEmpty(industryIdCollect)){
                R<List<IndustryDTO>> listR = remoteIndustryService.selectByIds(industryIdCollect, SecurityConstants.INNER);
                List<IndustryDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)){
                    for (int i = 0; i < targetDecomposeDetailsDTOList.size(); i++) {
                        targetDecomposeDetailsDTOList.get(i).setIndustryId(data.get(i).getIndustryId());
                        targetDecomposeDetailsDTOList.get(i).setIndustryName(data.get(i).getIndustryName());
                    }
                }

            }
        }
    }
    /**
     * 统一封装目标分解导出
     *
     * @param targetDecomposeDTO
     * @return
     */
    public List<TargetDecomposeExcel> packExportOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //excelList
        List<TargetDecomposeExcel> targetDecomposeExcelList = new ArrayList<>();
        List<TargetDecomposeDTO> targetDecomposeDTOList = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
        if (StringUtils.isNotEmpty(targetDecomposeDTOList)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOList) {
                //excel实体类
                TargetDecomposeExcel targetDecomposeExcel = new TargetDecomposeExcel();
                BeanUtils.copyProperties(decomposeDTO, targetDecomposeExcel);
                //时间维度
                if (1 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("年度");
                } else if (2 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("半年度");
                } else if (3 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("季度");
                } else if (4 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("月度");
                } else if (5 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("周");
                }
                BigDecimal decomposeTarget = targetDecomposeExcel.getDecomposeTarget();
                BigDecimal targetValue = targetDecomposeExcel.getTargetValue();
                if (null != decomposeTarget && null != targetValue) {
                    //目标差异
                    targetDecomposeExcel.setTargetDifference(decomposeTarget.subtract(targetValue));
                }
                targetDecomposeExcelList.add(targetDecomposeExcel);
            }
        }
        return targetDecomposeExcelList;
    }

    /**
     * 动态生成excel模板数据
     *
     * @param targetDecomposeDTO
     * @param mapAllData
     * @param targetDecomposeDetailsDTOS
     * @param cyclesExcelData
     * @return
     */
    public TargetDecomposeDTO packExcelData(TargetDecomposeDTO targetDecomposeDTO, Map<String, List<String>> mapAllData, List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS,  List<List<String>> cyclesExcelData) {

        //对下标进行排序
        int maxSize = this.packExcelListData(mapAllData);

        Map<String, List<Object>> mapAllEndData = this.packExcelDecompositionDimensionData(mapAllData);

        mapAllEndData.forEach((key, value) -> {
            List<Object> list = mapAllEndData.get(key);
            Collections.reverse(list);

            if (StringUtils.equals(key, DecompositionDimension.EMPLOYEE.getInfo())) {
                if (StringUtils.isNotEmpty(list)) {
                    for (int i = 0; i < Math.max(list.size(), maxSize); i++) {
                        TargetDecomposeDetailsDTO targetDecomposeDetailsDTO = new TargetDecomposeDetailsDTO();
                        if (list.size()-1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setEmployeeName("");
                            }else {
                                targetDecomposeDetailsDTO.setEmployeeName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        } else {
                            EmployeeDTO employeeDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), EmployeeDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setEmployeeName(employeeDTO.getEmployeeName());
                                targetDecomposeDetailsDTOS.get(i).setEmployeeId(employeeDTO.getEmployeeId());
                            }else {
                                //人员名称
                                targetDecomposeDetailsDTO.setEmployeeName(employeeDTO.getEmployeeName());
                                //人员id
                                targetDecomposeDetailsDTO.setEmployeeId(employeeDTO.getEmployeeId());
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }
                        }

                    }
                }

            } else if (StringUtils.equals(key, DecompositionDimension.AREA.getInfo())) {
                if (StringUtils.isNotEmpty(list)) {
                    for (int i = 0; i < Math.max(list.size(), maxSize); i++) {
                        TargetDecomposeDetailsDTO targetDecomposeDetailsDTO = new TargetDecomposeDetailsDTO();
                        if (list.size()-1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setAreaName("");
                            }else {
                                targetDecomposeDetailsDTO.setAreaName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        } else {
                            AreaDTO areaDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), AreaDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setAreaName(areaDTO.getAreaName());
                                targetDecomposeDetailsDTOS.get(i).setAreaId(areaDTO.getAreaId());
                            }else {
                                //区域名称
                                targetDecomposeDetailsDTO.setAreaName(areaDTO.getAreaName());
                                //区域id
                                targetDecomposeDetailsDTO.setAreaId(areaDTO.getAreaId());
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }
                        }
                    }
                }

            } else if (StringUtils.equals(key, DecompositionDimension.DEPARTMENT.getInfo())) {
                if (StringUtils.isNotEmpty(list)) {
                    for (int i = 0; i < Math.max(list.size(), maxSize); i++) {
                        TargetDecomposeDetailsDTO targetDecomposeDetailsDTO = new TargetDecomposeDetailsDTO();
                        if (list.size()-1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setDepartmentName("");
                            }else {
                                targetDecomposeDetailsDTO.setDepartmentName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }
                        } else {
                            DepartmentDTO departmentDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), DepartmentDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setDepartmentName(departmentDTO.getDepartmentName());
                                targetDecomposeDetailsDTOS.get(i).setDepartmentId(departmentDTO.getDepartmentId());
                            }else {
                                //部门名称
                                targetDecomposeDetailsDTO.setDepartmentName(departmentDTO.getDepartmentName());
                                //部门id
                                targetDecomposeDetailsDTO.setDepartmentId(departmentDTO.getDepartmentId());
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        }

                    }
                }

            } else if (StringUtils.equals(key, DecompositionDimension.INDUSTRY.getInfo())) {
                if (StringUtils.isNotEmpty(list)) {
                    for (int i = 0; i < Math.max(list.size(), maxSize); i++) {
                        TargetDecomposeDetailsDTO targetDecomposeDetailsDTO = new TargetDecomposeDetailsDTO();
                        if (list.size()-1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setIndustryName("");
                            }else {
                                targetDecomposeDetailsDTO.setIndustryName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }
                        } else {
                            IndustryDTO industryDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), IndustryDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setIndustryName(industryDTO.getIndustryName());
                                targetDecomposeDetailsDTOS.get(i).setIndustryId(industryDTO.getIndustryId());
                            }else {
                                //行业名称
                                targetDecomposeDetailsDTO.setIndustryName(industryDTO.getIndustryName());
                                //行业id
                                targetDecomposeDetailsDTO.setIndustryId(industryDTO.getIndustryId());
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        }
                    }
                }

            } else if (StringUtils.equals(key, DecompositionDimension.REGION.getInfo())) {
                if (StringUtils.isNotEmpty(list)) {
                    for (int i = 0; i < Math.max(list.size(), maxSize); i++) {
                        TargetDecomposeDetailsDTO targetDecomposeDetailsDTO = new TargetDecomposeDetailsDTO();
                        if (list.size()-1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setRegionName("");
                            }else {
                                targetDecomposeDetailsDTO.setRegionName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        } else {
                            RegionDTO regionDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), RegionDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setRegionName(regionDTO.getProvinceName());
                                targetDecomposeDetailsDTOS.get(i).setRegionId(regionDTO.getRegionId());
                            }else {
                                //省份名称
                                targetDecomposeDetailsDTO.setRegionName(regionDTO.getProvinceName());
                                //省份id
                                targetDecomposeDetailsDTO.setRegionId(regionDTO.getRegionId());
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        }
                    }
                }

            } else if (StringUtils.equals(key, DecompositionDimension.PRODUCT.getInfo())) {
                if (StringUtils.isNotEmpty(list)) {
                    for (int i = 0; i < Math.max(list.size(), maxSize); i++) {
                        TargetDecomposeDetailsDTO targetDecomposeDetailsDTO = new TargetDecomposeDetailsDTO();
                        if (list.size()-1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setProductName("");
                            }else {
                                targetDecomposeDetailsDTO.setProductName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }
                        } else {
                            ProductDTO productDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), ProductDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize){
                                targetDecomposeDetailsDTOS.get(i).setProductName(productDTO.getProductName());
                                targetDecomposeDetailsDTOS.get(i).setProductId(productDTO.getProductId());
                            }else {
                                //产品名称
                                targetDecomposeDetailsDTO.setProductName(productDTO.getProductName());
                                //产品id
                                targetDecomposeDetailsDTO.setProductId(productDTO.getProductId());
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        }
                    }
                }

            }
        });
        //详情周期数据
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = new ArrayList<>();
            List<String> list = cyclesExcelData.get(i);
            for (int i1 = 0; i1 < list.size(); i1++) {
                DecomposeDetailCyclesDTO decomposeDetailCyclesDTO = new DecomposeDetailCyclesDTO();
                //周期
                decomposeDetailCyclesDTO.setCycleNumber(i1+1);
                if (StringUtils.isNotBlank(list.get(i1))){
                    //预测值
                    decomposeDetailCyclesDTO.setCycleForecast(new BigDecimal(list.get(i1)));
                }
                decomposeDetailCyclesDTOS.add(decomposeDetailCyclesDTO);
            }
            targetDecomposeDetailsDTOS.get(i).setDecomposeDetailCyclesDTOS(decomposeDetailCyclesDTOS);
        }
        //解析完成数据
        targetDecomposeDTO.setTargetDecomposeDetailsDTOS(targetDecomposeDetailsDTOS);
        return targetDecomposeDTO;
    }

    /**
     * 封装分解维度数据
     *
     * @param mapAllData
     * @return
     */
    public Map<String, List<Object>> packExcelDecompositionDimensionData(Map<String, List<String>> mapAllData) {
        Map<String, List<Object>> mapAllEndData = new LinkedHashMap<>();
        if (StringUtils.isNotEmpty(mapAllData)) {
            mapAllData.forEach((key, value) -> {
                if (StringUtils.equals(key, DecompositionDimension.EMPLOYEE.getInfo())) {
                    List<String> list = mapAllData.get(key);
                    //远程调用查询是否存在
                    R<List<EmployeeDTO>> listR = remoteEmployeeService.selectCodeList(mapAllData.get(key), SecurityConstants.INNER);
                    List<EmployeeDTO> employeeDTOS = listR.getData();
                    if (StringUtils.isNotEmpty(employeeDTOS)) {
                        //数据库数据去重
                        List<String> list1 = employeeDTOS.stream().map(EmployeeDTO::getEmployeeCode).distinct().collect(Collectors.toList());
                        //差集
                        List<String> reduce1 = list.stream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
                        //删除差集
                        list.removeAll(reduce1);
                        //最终人员数据
                        List<Object> EmployeeData = new ArrayList<>();
                        for (EmployeeDTO employeeDTO : employeeDTOS) {
                            if (list.contains(employeeDTO.getEmployeeCode())) {
                                EmployeeData.add(JSONObject.parseObject(JSONObject.toJSONString(employeeDTO)));
                            }
                        }

                        mapAllEndData.put(key,EmployeeData);
                    }
                } else if (StringUtils.equals(key, DecompositionDimension.AREA.getInfo())) {
                    //excel数据
                    List<String> list = mapAllData.get(key);
                    //数据库数据
                    List<AreaDTO> areaDTOS = areaMapper.selectAreaListByAreaCodes(list);
                    if (StringUtils.isNotEmpty(areaDTOS)) {
                        //数据库数据去重
                        List<String> list1 = areaDTOS.stream().map(AreaDTO::getAreaCode).distinct().collect(Collectors.toList());
                        //差集
                        List<String> reduce1 = list.stream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
                        //删除差集
                        list.removeAll(reduce1);
                        //最终区域数据
                        List<Object> areaData = new ArrayList<>();
                        for (AreaDTO areaDTO : areaDTOS) {
                            if (list.contains(areaDTO.getAreaCode())) {
                                areaData.add(JSONObject.parseObject(JSONObject.toJSONString(areaDTO)));
                            }
                        }
                        mapAllEndData.put(key, Collections.singletonList(areaData));
                    }
                } else if (StringUtils.equals(key, DecompositionDimension.DEPARTMENT.getInfo())) {
                    //excel数据
                    List<String> list = mapAllData.get(key);
                    //远程调用查询是否存在
                    R<List<DepartmentDTO>> listR = remoteDepartmentService.selectCodeList(list, SecurityConstants.INNER);
                    List<DepartmentDTO> departmentDTOS = listR.getData();
                    if (StringUtils.isNotEmpty(departmentDTOS)) {
                        //数据库数据去重
                        List<String> list1 = departmentDTOS.stream().map(DepartmentDTO::getDepartmentCode).distinct().collect(Collectors.toList());
                        //差集
                        List<String> reduce1 = list.stream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
                        //删除差集
                        list.removeAll(reduce1);
                        //最终部门数据
                        List<Object> DepartmentData = new ArrayList<>();
                        for (DepartmentDTO departmentDTO : departmentDTOS) {
                            if (list.contains(departmentDTO.getDepartmentCode())) {
                                DepartmentData.add(JSONObject.parseObject(JSONObject.toJSONString(departmentDTO)));
                            }
                        }
                        mapAllEndData.put(key, DepartmentData);
                    }
                } else if (StringUtils.equals(key, DecompositionDimension.INDUSTRY.getInfo())) {
                    //excel数据
                    List<String> list = mapAllData.get(key);
                    //远程调用查询是否存在
                    R<List<IndustryDTO>> listR = remoteIndustryService.selectCodeList(list, SecurityConstants.INNER);
                    List<IndustryDTO> industryDTOS = listR.getData();
                    if (StringUtils.isNotEmpty(industryDTOS)) {
                        //数据库数据去重
                        List<String> list1 = industryDTOS.stream().map(IndustryDTO::getIndustryCode).distinct().collect(Collectors.toList());
                        //差集
                        List<String> reduce1 = list.stream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
                        //删除差集
                        list.removeAll(reduce1);
                        //最终行业数据
                        List<Object> industryData = new ArrayList<>();
                        for (IndustryDTO industryDTO : industryDTOS) {
                            if (list.contains(industryDTO.getIndustryCode())) {
                                industryData.add(JSONObject.parseObject(JSONObject.toJSONString(industryDTO)));
                            }
                        }
                        mapAllEndData.put(key, industryData);
                    }
                } else if (StringUtils.equals(key, DecompositionDimension.REGION.getInfo())) {
                    //excel数据
                    List<String> list = mapAllData.get(key);
                    //远程调用查询是否存在
                    R<List<RegionDTO>> listR = remoteRegionService.selectCodeList(list, SecurityConstants.INNER);
                    List<RegionDTO> regionDTOS = listR.getData();
                    if (StringUtils.isNotEmpty(regionDTOS)) {
                        //数据库数据去重
                        List<String> list1 = regionDTOS.stream().map(RegionDTO::getProvinceName).distinct().collect(Collectors.toList());
                        //差集
                        List<String> reduce1 = list.stream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
                        //删除差集
                        list.removeAll(reduce1);
                        //最终行业数据
                        List<Object> regionData = new ArrayList<>();
                        for (RegionDTO regionDTO : regionDTOS) {
                            if (list.contains(regionDTO.getProvinceName())) {
                                regionData.add(JSONObject.parseObject(JSONObject.toJSONString(regionDTO)));
                            }
                        }
                        mapAllEndData.put(key, regionData);
                    }
                } else if (StringUtils.equals(key, DecompositionDimension.PRODUCT.getInfo())) {
                    //excel数据
                    List<String> list = mapAllData.get(key);
                    //数据库数据
                    List<ProductDTO> productDTOS = productMapper.selectProductByProductCodes(list);
                    if (StringUtils.isNotEmpty(productDTOS)) {
                        //数据库数据去重
                        List<String> list1 = productDTOS.stream().map(ProductDTO::getProductCode).distinct().collect(Collectors.toList());
                        //差集
                        List<String> reduce1 = list.stream().filter(item -> !list1.contains(item)).collect(Collectors.toList());
                        //删除差集
                        list.removeAll(reduce1);
                        //最终区域数据
                        List<Object> productData = new ArrayList<>();
                        for (ProductDTO productDTO : productDTOS) {
                            if (list.contains(productDTO.getProductCode())) {
                                productData.add(JSONObject.parseObject(JSONObject.toJSONString(productDTO)));
                            }
                        }
                        mapAllEndData.put(key, productData);
                    }
                }
            });
        }
        return mapAllEndData;
    }
}

