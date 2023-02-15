package net.qixiaowei.operate.cloud.service.impl.targetManager;

import cn.hutool.json.JSONUtil;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.integration.common.enums.targetManager.DecompositionDimension;
import net.qixiaowei.integration.common.enums.targetManager.TargetDecomposeDimensionCode;
import net.qixiaowei.integration.common.enums.targetManager.TargetDecomposeType;
import net.qixiaowei.integration.common.enums.targetManager.TimeDimension;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.Convert;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.api.dto.message.MessageReceiverDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import net.qixiaowei.message.api.remote.message.RemoteMessageService;
import net.qixiaowei.operate.cloud.api.domain.targetManager.*;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeDetailsExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeExcel;
import net.qixiaowei.operate.cloud.mapper.product.ProductMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.*;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
import net.qixiaowei.system.manage.api.dto.basic.*;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.system.RemoteRegionService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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

    @Autowired
    private RemoteBacklogService remoteBacklogService;

    @Autowired
    private RemoteMessageService remoteMessageService;
    @Autowired
    private RemoteUserService userService;

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
            if (StringUtils.isNotNull(data)) {
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
                BigDecimal amountTarget = targetDecomposeDetailsDTO.getAmountTarget();
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = new ArrayList<>();
                decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                    //周期预测值
                    BigDecimal cycleForecast = decomposeDetailCyclesDTO.getCycleForecast();
                    //周期实际值
                    BigDecimal cycleActual = decomposeDetailCyclesDTO.getCycleActual();
                    //周期目标值
                    BigDecimal cycleTarget = decomposeDetailCyclesDTO.getCycleTarget();
                    //预测与目标偏差率
                    BigDecimal cycleForecastDeviationRate = new BigDecimal("0");
                    //目标完成率
                    BigDecimal cyclePercentageComplete = new BigDecimal("0");
                    int nowForecastYear = packForecastYearType(targetDecomposeDTO);
                    Integer cycleNumber = decomposeDetailCyclesDTO.getCycleNumber();
                    //传入年份
                    Integer targetYear = targetDecomposeDTO.getTargetYear();
                    //当前年份
                    int year = DateUtils.getYear();

                    //判断年份
                    if (targetYear > year) {
                        if (null != decomposeDetailCyclesDTO.getCycleForecast() && decomposeDetailCyclesDTO.getCycleForecast().compareTo(BigDecimal.ZERO) != 0) {
                            //预测值
                            forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleForecast());
                        }
                    } else if (targetYear < year) {
                        if (null != decomposeDetailCyclesDTO.getCycleActual() && decomposeDetailCyclesDTO.getCycleActual().compareTo(BigDecimal.ZERO) != 0) {
                            //预测值
                            forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleActual());
                        }
                    } else {
                        //实际值
                        if (cycleNumber < nowForecastYear) {
                            if (null != decomposeDetailCyclesDTO.getCycleActual() && decomposeDetailCyclesDTO.getCycleActual().compareTo(BigDecimal.ZERO) != 0) {
                                //预测值
                                forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleActual());
                            }
                        } else {
                            if (null != decomposeDetailCyclesDTO.getCycleForecast() && decomposeDetailCyclesDTO.getCycleForecast().compareTo(BigDecimal.ZERO) != 0) {
                                //预测值
                                forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleForecast());
                            }
                        }
                    }

                    if (null != cycleActual && cycleActual.compareTo(BigDecimal.ZERO) != 0) {
                        //实际值
                        actualTotal = actualTotal.add(cycleActual);
                    }


                    if (cycleActual != null && cycleActual.compareTo(new BigDecimal("0")) != 0 &&
                            cycleForecast != null && cycleForecast.compareTo(new BigDecimal("0")) != 0) {
                        cycleForecastDeviationRate = cycleActual.subtract(cycleForecast).setScale(10, BigDecimal.ROUND_HALF_UP).divide(cycleActual, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(10, BigDecimal.ROUND_HALF_UP);
                    }


                    //预测与目标偏差率
                    decomposeDetailCyclesDTO.setCycleForecastDeviationRate(cycleForecastDeviationRate);

                    if (cycleActual != null && cycleActual.compareTo(new BigDecimal("0")) != 0) {
                        if (cycleTarget != null && cycleTarget.compareTo(new BigDecimal("0")) != 0) {
                            cyclePercentageComplete = cycleActual.divide(cycleTarget, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                        }
                    }
                    //目标完成率
                    decomposeDetailCyclesDTO.setCyclePercentageComplete(cyclePercentageComplete);
                    //预测总和
                    if (cyclePercentageComplete.compareTo(new BigDecimal("0")) != 0) {
                        forecastDeviationRateSum = forecastDeviationRateSum.add(cycleForecastDeviationRate.abs());
                    }
                    //目标总和
                    if (decomposeDetailCyclesDTO.getCyclePercentageComplete() != null && decomposeDetailCyclesDTO.getCyclePercentageComplete().compareTo(new BigDecimal("0")) != 0) {
                        targetPercentageCompleteSum = targetPercentageCompleteSum.add(decomposeDetailCyclesDTO.getCyclePercentageComplete());
                    }

                }

                if (actualTotal.compareTo(BigDecimal.ZERO) != 0) {
                    //被除数 不能为0和空
                    if (null != amountTarget && amountTarget.compareTo(BigDecimal.ZERO) != 0) {
                        //保留一位小数
                        targetPercentageComplete = actualTotal.divide(amountTarget, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                    }
                }
                //预测平均数
                if (forecastDeviationRateSum.compareTo(BigDecimal.ZERO) != 0) {
                    forecastDeviationRateAve = forecastDeviationRateSum.divide(new BigDecimal(String.valueOf(decomposeDetailCyclesDTOList.size())), 10, BigDecimal.ROUND_HALF_UP);
                }
                //目标完成平均数
                if (targetPercentageCompleteSum.compareTo(BigDecimal.ZERO) != 0) {
                    targetPercentageCompleteAve = targetPercentageCompleteSum.divide(new BigDecimal(String.valueOf(decomposeDetailCyclesDTOList.size())), 10, BigDecimal.ROUND_HALF_UP);
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
     * @param backlogId
     * @return
     */
    @Override
    public TargetDecomposeDTO selectRollTargetDecomposeByTargetDecomposeId(Long targetDecomposeId, Long backlogId) {
        //目标分解主表数据
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在！");
        } else {
            R<IndicatorDTO> R = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
            IndicatorDTO data = R.getData();
            if (StringUtils.isNotNull(data)) {
                targetDecomposeDTO.setIndicatorName(data.getIndicatorName());
            }
            this.packDecompositionDimension(targetDecomposeDTO);
            String forecastCycle = this.packForecastCycle(targetDecomposeDTO);
            targetDecomposeDTO.setForecastCycle(forecastCycle);
            this.packversion(targetDecomposeDTO);
        }
        //目标分解详情数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = new ArrayList<>();
        if (null == backlogId) {
            targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeId);
        } else {
            //目标分解详情数据
            targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByPowerTargetDecomposeId(targetDecomposeId, SecurityUtils.getEmployeeId());
        }

        this.packRemote(targetDecomposeDetailsDTOList);
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                //年度预测值
                BigDecimal forecastYear = new BigDecimal("0");
                //累计实际值
                BigDecimal actualTotal = new BigDecimal("0");
                //目标完成率
                BigDecimal targetPercentageComplete = new BigDecimal("0");
                //分解目标（汇总目标值）
                BigDecimal amountTarget = targetDecomposeDetailsDTO.getAmountTarget();

                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = new ArrayList<>();
                decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                    int nowForecastYear = packForecastYearType(targetDecomposeDTO);
                    Integer cycleNumber = decomposeDetailCyclesDTO.getCycleNumber();
                    //传入年份
                    Integer targetYear = targetDecomposeDTO.getTargetYear();
                    //当前年份
                    int year = DateUtils.getYear();

                    //判断年份
                    if (targetYear > year) {
                        if (null != decomposeDetailCyclesDTO.getCycleForecast() && decomposeDetailCyclesDTO.getCycleForecast().compareTo(BigDecimal.ZERO) != 0) {
                            //预测值
                            forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleForecast());
                        }
                    } else if (targetYear < year) {
                        if (null != decomposeDetailCyclesDTO.getCycleActual() && decomposeDetailCyclesDTO.getCycleActual().compareTo(BigDecimal.ZERO) != 0) {
                            //预测值
                            forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleActual());
                        }
                    } else {
                        //实际值
                        if (cycleNumber < nowForecastYear) {
                            if (null != decomposeDetailCyclesDTO.getCycleActual() && decomposeDetailCyclesDTO.getCycleActual().compareTo(BigDecimal.ZERO) != 0) {
                                //预测值
                                forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleActual());
                            }
                        } else {
                            if (null != decomposeDetailCyclesDTO.getCycleForecast() && decomposeDetailCyclesDTO.getCycleForecast().compareTo(BigDecimal.ZERO) != 0) {
                                //预测值
                                forecastYear = forecastYear.add(decomposeDetailCyclesDTO.getCycleForecast());
                            }
                        }
                    }

                    if (null != decomposeDetailCyclesDTO.getCycleActual() && decomposeDetailCyclesDTO.getCycleActual().compareTo(BigDecimal.ZERO) != 0) {
                        //实际值
                        actualTotal = actualTotal.add(decomposeDetailCyclesDTO.getCycleActual());
                    }
                }

                if (null != actualTotal && actualTotal.compareTo(BigDecimal.ZERO) != 0) {
                    //被除数 不能为0和空
                    if (null != amountTarget && amountTarget.compareTo(BigDecimal.ZERO) != 0) {
                        //保留一位小数
                        targetPercentageComplete = actualTotal.divide(amountTarget, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                    }
                }
                targetDecomposeDetailsDTO.setForecastYear(forecastYear);
                targetDecomposeDetailsDTO.setAmountTarget(amountTarget);
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
     * 封装根据时间类型当前时间顺序
     *
     * @param targetDecomposeDTO
     * @return
     */
    private int packForecastYearType(TargetDecomposeDTO targetDecomposeDTO) {
        int i = 0;
        if (StringUtils.isNotNull(targetDecomposeDTO)) {
            Integer timeDimension = targetDecomposeDTO.getTimeDimension();
            if (timeDimension == 2) {
                if (DateUtils.getMonth() <= 6) {
                    return 1;
                } else {
                    return 2;
                }
            } else if (timeDimension == 3) {
                return DateUtils.getQuarter();
            } else if (timeDimension == 4) {
                return DateUtils.getMonth();
            } else if (timeDimension == 5) {
                return DateUtils.getDayOfWeek();
            } else if (timeDimension == 1) {
                return 53;
            } else {
                throw new ServiceException("不正确的时间维度 请配置正确的数据！");
            }

        } else {
            throw new ServiceException("时间维度为空 请配置正确的数据");
        }

    }

    /**
     * 远程调用根据id目标分解id查询数据
     *
     * @param targetDecomposeId 目标分解表主键
     * @return
     */
    @Override
    public TargetDecomposeDTO selectTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNotNull(targetDecomposeDTO)) {
            R<IndicatorDTO> R = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
            IndicatorDTO data = R.getData();
            if (StringUtils.isNotNull(data)) {
                targetDecomposeDTO.setIndicatorName(data.getIndicatorName());
            }
        }
        this.packDecompositionDimension(targetDecomposeDTO);
        return targetDecomposeDTO;
    }

    /**
     * 远程调用根据id集合目标分解id查询数据
     *
     * @param targetDecomposeIds 目标分解表主键集合
     * @return
     */
    @Override
    public List<TargetDecomposeDTO> selectTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            List<Long> collect = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(collect, SecurityConstants.INNER);
                List<IndicatorDTO> data = listR.getData();
                for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDTOS) {
                    if (StringUtils.isNotEmpty(data)) {
                        for (IndicatorDTO datum : data) {
                            if (targetDecomposeDTO.getIndicatorId().equals(datum.getIndicatorId())) {
                                targetDecomposeDTO.setIndicatorName(datum.getIndicatorName());
                            }
                        }
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
            forecastCycle = "年度";
        } else if (targetDecomposeDTO.getTimeDimension() == 2) {
            if (DateUtils.getMonth() <= 6) {
                forecastCycle = "上半年";
            } else {
                forecastCycle = "下半年";
            }
        } else if (targetDecomposeDTO.getTimeDimension() == 3) {
            forecastCycle = Convert.int2chineseNum(DateUtils.getQuarter()) + "季度";
        } else if (targetDecomposeDTO.getTimeDimension() == 4) {
            forecastCycle = DateUtils.getMonth() + "月";
        } else if (targetDecomposeDTO.getTimeDimension() == 5) {
            forecastCycle = DateUtils.getDayOfWeek() + "周";
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
        targetDecompose.setTenantId(SecurityUtils.getTenantId());
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectResultList(targetDecompose);
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            List<Long> indicatorIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).collect(Collectors.toList());
            R<List<IndicatorDTO>> listR1 = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR1.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                    for (IndicatorDTO datum : data) {
                        if (decomposeDTO.getIndicatorId().equals(datum.getIndicatorId())) {
                            decomposeDTO.setIndicatorName(datum.getIndicatorName());
                        }
                    }
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
                        forecastDeviationRate = forecastDeviation.divide(decomposeTarget, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
                    }
                }
                //预测与目标偏差率
                decomposeDTO.setForecastDeviationRate(forecastDeviationRate);
                if (actualTotal != null && actualTotal.compareTo(new BigDecimal("0")) != 0) {
                    if (decomposeTarget != null && decomposeTarget.compareTo(new BigDecimal("0")) != 0) {
                        targetPercentageComplete = actualTotal.divide(decomposeTarget, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100"));
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
                    List<TargetDecomposeDTO> targetDecomposeDTOS1 = listMap.get(aLong);
                    if (StringUtils.isNotEmpty(targetDecomposeDTOS1)) {
                        ////目标完成率平均值
                        for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS1) {
                            BigDecimal targetPercentageCompleteSum = new BigDecimal("0");
                            BigDecimal targetPercentageCompleteAve = new BigDecimal("0");
                            TargetDecomposeDTO targetDecomposeDTO1 = this.selectResultTargetDecomposeByTargetDecomposeId(decomposeDTO.getTargetDecomposeId());
                            if (StringUtils.isNotNull(targetDecomposeDTO1)) {
                                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = new ArrayList<>();
                                List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO1.getTargetDecomposeDetailsDTOS();
                                if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
                                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                                        List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO.getDecomposeDetailCyclesDTOS();
                                        decomposeDetailCyclesDTOList.addAll(decomposeDetailCyclesDTOS);

                                    }
                                    if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOList) && decomposeDetailCyclesDTOList.get(0) != null) {
                                        //根据周期数分组
                                        Map<Integer, List<DecomposeDetailCyclesDTO>> cycleNumberMap = decomposeDetailCyclesDTOList.parallelStream().collect(Collectors.groupingBy(DecomposeDetailCyclesDTO::getCycleNumber));
                                        for (Integer key : cycleNumberMap.keySet()) {
                                            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList1 = cycleNumberMap.get(key);
                                            //sterm流求和 实际值合计
                                            BigDecimal cycleActualSum = decomposeDetailCyclesDTOList1.stream().map(DecomposeDetailCyclesDTO::getCycleActual).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                                            //sterm流求和 目标值合计
                                            BigDecimal cycleTargetSum = decomposeDetailCyclesDTOList1.stream().map(DecomposeDetailCyclesDTO::getCycleTarget).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                                            if (cycleActualSum.compareTo(new BigDecimal("0")) != 0 && cycleTargetSum.compareTo(new BigDecimal("0")) != 0) {
                                                targetPercentageCompleteSum = targetPercentageCompleteSum.add(cycleActualSum.divide(cycleTargetSum, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")));
                                            }
                                        }
                                    }
                                    if (targetPercentageCompleteSum.compareTo(new BigDecimal("0")) != 0 && StringUtils.isNotEmpty(targetDecomposeDetailsDTOS.get(0).getDecomposeDetailCyclesDTOS())) {
                                        targetPercentageCompleteAve = targetPercentageCompleteSum.divide(new BigDecimal(String.valueOf(targetDecomposeDetailsDTOS.get(0).getDecomposeDetailCyclesDTOS().size())), 10, BigDecimal.ROUND_HALF_UP);
                                    }
                                    //目标完成率平均值
                                    decomposeDTO.setTargetPercentageCompleteAve(targetPercentageCompleteAve);
                                }
                            }
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
        //分解目标值
        BigDecimal decomposeTarget = new BigDecimal("0");
        //已分解
        BigDecimal decomposed = new BigDecimal("0");
        //目标分解主表数据
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNotNull(targetDecomposeDTO)) {
            R<IndicatorDTO> R = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
            IndicatorDTO data = R.getData();
            if (StringUtils.isNotNull(data)) {
                targetDecomposeDTO.setIndicatorName(data.getIndicatorName());
            }
            decomposeTarget = targetDecomposeDTO.getDecomposeTarget();
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
            decomposed = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getAmountTarget).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            //已分解
            targetDecomposeDTO.setDecomposed(decomposed);
            if (decomposeTarget.compareTo(new BigDecimal("0")) != 0) {
                //未分解
                targetDecomposeDTO.setUndecomposed(decomposeTarget.subtract(decomposed));
            }

            return targetDecomposeDTO.setTargetDecomposeDetailsDTOS(targetDecomposeDetailsDTOList);
        } else {
            //已分解
            targetDecomposeDTO.setDecomposed(decomposed);
            //未分解
            targetDecomposeDTO.setUndecomposed(new BigDecimal("0"));
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
        this.getIndicatorId(targetDecomposeDTO.getParams());
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
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectRollPageList(targetDecompose);

        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                //累计实际值
                BigDecimal actualTotal = new BigDecimal("0");
                List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(decomposeDTO.getTargetDecomposeId());
                if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
                    List<Long> collect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(collect)) {
                        List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsIds(collect);
                        if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOList)) {
                            //求和
                            actualTotal = actualTotal.add(decomposeDetailCyclesDTOList.stream().map(DecomposeDetailCyclesDTO::getCycleActual).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                        }
                    }
                }
                //累计实际值
                decomposeDTO.setActualTotal(actualTotal);
            }
            List<Long> indicatorIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).collect(Collectors.toList());

            //远程获取指标名称
            R<List<IndicatorDTO>> listR1 = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR1.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                    for (IndicatorDTO datum : data) {
                        if (decomposeDTO.getIndicatorId().equals(datum.getIndicatorId())) {
                            decomposeDTO.setIndicatorName(datum.getIndicatorName());
                        }
                    }
                }

            }
        }
        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                String forecastCycle = this.packForecastCycle(decomposeDTO);
                decomposeDTO.setForecastCycle(forecastCycle);
                this.packDecompositionDimension(decomposeDTO);
            }
        }
        //模糊指标名称
        String indicatorName = targetDecomposeDTO.getIndicatorName();
        if (StringUtils.isNotBlank(indicatorName)) {
            List<TargetDecomposeDTO> targetDecomposeDTOList = new ArrayList<>();
            //模糊查询
            Pattern pattern = Pattern.compile(indicatorName);
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                String indicatorName1 = decomposeDTO.getIndicatorName();
                if (StringUtils.isNotBlank(indicatorName1)) {
                    Matcher matcher = pattern.matcher(indicatorName1);
                    if (matcher.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                        targetDecomposeDTOList.add(decomposeDTO);
                    }
                }
            }
            return targetDecomposeDTOList;
        }
        return targetDecomposeDTOS;
    }

    /**
     * 获取高级搜索后的组织ID传入params
     *
     * @param params 请求参数
     */
    private void getIndicatorId(Map<String, Object> params) {
        if (StringUtils.isNotEmpty(params)) {
            IndicatorDTO indicatorDTO = new IndicatorDTO();

            Map<String, Object> params2 = new HashMap<>();

            for (String key : params.keySet()) {
                switch (key) {
                    case "indicatorNameEqual":
                        params2.put("indicatorNameEqual", params.get("indicatorNameEqual"));
                        break;
                    case "indicatorNameNotEqual":
                        params2.put("indicatorNameNotEqual", params.get("indicatorNameNotEqual"));
                        break;
                }
            }
            if (StringUtils.isNotEmpty(params2)) {
                indicatorDTO.setParams(params2);
                //远程查找指标列表
                R<List<IndicatorDTO>> indicatorAllData = remoteIndicatorService.getIndicatorAllData(indicatorDTO, SecurityConstants.INNER);
                if (indicatorAllData.getCode() != 200) {
                    throw new ServiceException("远程调用组织失败 请联系管理员");
                }
                List<IndicatorDTO> data = indicatorAllData.getData();
                List<Long> indicatorIds = new ArrayList<>();
                if (StringUtils.isNotEmpty(data)) {
                    indicatorIds = data.stream().map(IndicatorDTO::getIndicatorId).distinct().collect(Collectors.toList());
                }
                params.put("indicatorIds", indicatorIds);
            }
        }
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
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                //远程获取指标名称
                R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
                List<IndicatorDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (IndicatorDTO datum : data) {
                        if (decomposeDTO.getIndicatorId().equals(datum.getIndicatorId())) {
                            decomposeDTO.setIndicatorName(datum.getIndicatorName());
                        }
                    }

                }
                this.packDecompositionDimension(decomposeDTO);
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
        //validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.ORDER.getCode());
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(targetDecomposeDTO.getTargetYear() + "年已创建该维度目标分解，无需重复创建。");
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
        this.sendMessage(targetDecomposeDTO, 1);
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 发送消息
     *
     * @param targetDecomposeDTO    主表DTO
     * @param TARGET_DECOMPOSE_CODE 编码及
     */
    private void sendMessage(TargetDecomposeDTO targetDecomposeDTO, int TARGET_DECOMPOSE_CODE) {
        Integer isSubmit = targetDecomposeDTO.getIsSubmit();
        if (StringUtils.isNotNull(isSubmit)) {
            if (isSubmit == 1) {
                List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
                List<Long> principalEmployeeIdCollect =
                        targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getPrincipalEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
                List<UserDTO> userDTOS = getUserByCreateBys(SecurityUtils.getUserId());
                List<EmployeeDTO> employeeDTOS = getEmployeeDTOS(principalEmployeeIdCollect);
                if (StringUtils.isNotEmpty(employeeDTOS) && StringUtils.isNotEmpty(userDTOS)) {
                    List<MessageSendDTO> messageSendDTOS = new ArrayList<>();
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                        for (EmployeeDTO employeeDTO : employeeDTOS) {
                            if (StringUtils.isNull(employeeDTO.getUserId())) {
                                continue;
                            }
                            if (targetDecomposeDetailsDTO.getPrincipalEmployeeId().equals(employeeDTO.getEmployeeId())) {
                                UserDTO user = userDTOS.get(0);
                                Long targetDecomposeId = targetDecomposeDTO.getTargetDecomposeId();
                                MessageSendDTO messageSendDTO = new MessageSendDTO();
                                messageSendDTO.setMessageType(MessageType.PRIVATE_MESSAGE.getCode());
                                switch (TARGET_DECOMPOSE_CODE) {
                                    case 1:
                                        messageSendDTO.setBusinessType(BusinessSubtype.TARGET_DECOMPOSE_ORDER_NOTIFY.getParentBusinessType().getCode());
                                        messageSendDTO.setBusinessSubtype(BusinessSubtype.TARGET_DECOMPOSE_ORDER_NOTIFY.getCode());
                                        messageSendDTO.setMessageTitle(BusinessSubtype.TARGET_DECOMPOSE_ORDER_NOTIFY.getInfo());
                                        break;
                                    case 2:
                                        messageSendDTO.setBusinessType(BusinessSubtype.TARGET_DECOMPOSE_INCOME_NOTIFY.getParentBusinessType().getCode());
                                        messageSendDTO.setBusinessSubtype(BusinessSubtype.TARGET_DECOMPOSE_INCOME_NOTIFY.getCode());
                                        messageSendDTO.setMessageTitle(BusinessSubtype.TARGET_DECOMPOSE_INCOME_NOTIFY.getInfo());
                                        break;
                                    case 3:
                                        messageSendDTO.setBusinessType(BusinessSubtype.TARGET_DECOMPOSE_RECOVERY_NOTIFY.getParentBusinessType().getCode());
                                        messageSendDTO.setBusinessSubtype(BusinessSubtype.TARGET_DECOMPOSE_RECOVERY_NOTIFY.getCode());
                                        messageSendDTO.setMessageTitle(BusinessSubtype.TARGET_DECOMPOSE_RECOVERY_NOTIFY.getInfo());
                                        break;
                                    case 4:
                                        messageSendDTO.setBusinessType(BusinessSubtype.TARGET_DECOMPOSE_CUSTOM_NOTIFY.getParentBusinessType().getCode());
                                        messageSendDTO.setBusinessSubtype(BusinessSubtype.TARGET_DECOMPOSE_CUSTOM_NOTIFY.getCode());
                                        messageSendDTO.setMessageTitle(BusinessSubtype.TARGET_DECOMPOSE_CUSTOM_NOTIFY.getInfo());
                                        break;
                                }

                                messageSendDTO.setBusinessId(targetDecomposeId);

                                messageSendDTO.setHandleContent(true);
                                Map<String, Object> paramMap = new HashMap<>();
                                //员工姓名
                                paramMap.put("employee_name", StringUtils.isEmpty(user.getEmployeeName()) ? "" : user.getEmployeeName());
                                //员工工号
                                paramMap.put("employee_code", StringUtils.isEmpty(user.getEmployeeCode()) ? "" : user.getEmployeeCode());
                                //目标年度
                                paramMap.put("target_year", targetDecomposeDTO.getTargetYear());
                                //分解维度
                                paramMap.put("decomposition_dimension", targetDecomposeDTO.getDecompositionDimension());
                                //时间维度
                                paramMap.put("time_dimension", TimeDimension.getInfo(targetDecomposeDTO.getTimeDimension()));
                                String messageParam = JSONUtil.toJsonStr(paramMap);
                                messageSendDTO.setMessageParam(messageParam);
                                messageSendDTO.setSendUserId(user.getUserId());
                                List<MessageReceiverDTO> messageReceivers = new ArrayList<>();
                                MessageReceiverDTO messageReceiverDTO = new MessageReceiverDTO();
                                messageReceiverDTO.setUserId(employeeDTO.getUserId());
                                messageReceivers.add(messageReceiverDTO);
                                messageSendDTO.setMessageReceivers(messageReceivers);
                                messageSendDTOS.add(messageSendDTO);
                                break;
                            }
                        }
                    }
                    if (StringUtils.isNotEmpty(messageSendDTOS)) {
                        R<?> r = remoteMessageService.sendMessages(messageSendDTOS, SecurityConstants.INNER);
                    }
                }
            }
        }
    }

    /**
     * 根据创建人获取用户信息
     *
     * @param createBy 创建人
     * @return List
     */
    private List<UserDTO> getUserByCreateBys(Long createBy) {
        Set<Long> createBySets = new HashSet<>();
        createBySets.add(createBy);
        R<List<UserDTO>> usersByUserIds = userService.getUsersByUserIds(createBySets, SecurityConstants.INNER);
        List<UserDTO> userDTOS = usersByUserIds.getData();
        if (usersByUserIds.getCode() != 200) {
            throw new ServiceException("远程访问用户信息失败");
        }
        if (StringUtils.isEmpty(userDTOS)) {
            throw new ServiceException("部分用户信息已被删除 请检查用户配置");
        }
        return userDTOS;
    }

    /**
     * 获取用户信息
     *
     * @return userDTO
     */
    private List<EmployeeDTO> getEmployeeDTOS(List<Long> principalEmployeeIdCollect) {
        R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(principalEmployeeIdCollect, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程访问用户信息失败");
        }
        if (StringUtils.isEmpty(employeeDTOS)) {
            throw new ServiceException("当前滚动预测负责人已被删除 请检查员工配置");
        }
        return employeeDTOS;
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
        //validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //分解类型
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.INCOME.getCode());
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(targetDecomposeDTO.getTargetYear() + "年已创建该维度目标分解，无需重复创建。");
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
        this.sendMessage(targetDecomposeDTO, 2);
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
        //validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.RECEIVABLE.getCode());
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(targetDecomposeDTO.getTargetYear() + "年已创建该维度目标分解，无需重复创建。");
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
        this.sendMessage(targetDecomposeDTO, 3);
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
        //validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setTargetDecomposeType(TargetDecomposeType.CUSTOM.getCode());
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(targetDecomposeDTO.getTargetYear() + "年已创建该维度目标分解，无需重复创建。");
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
        this.sendMessage(targetDecomposeDTO, 4);
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
        BigDecimal forecastYear = new BigDecimal("0");
        //传入数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                BigDecimal forecastYear1 = targetDecomposeDetailsDTO.getForecastYear();
                if (forecastYear1 != null) {
                    forecastYear = forecastYear.add(forecastYear1);
                }

            }
        }
        //已录入
        targetDecompose.setStatus(Constants.ONE);
        targetDecompose.setForecastYear(forecastYear);
        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            i = targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
        Long backlogId = targetDecomposeDTO.getBacklogId();
        if (null != backlogId) {
            if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
                for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
                    //待办事项表
                    BacklogDTO backlogDTO = new BacklogDTO();
                    backlogDTO.setBusinessType(BusinessSubtype.ROLLING_PREDICTION_MANAGE_BACKLOG.getParentBusinessType().getCode());
                    backlogDTO.setBusinessSubtype(BusinessSubtype.ROLLING_PREDICTION_MANAGE_BACKLOG.getCode());
                    backlogDTO.setBusinessId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                    backlogDTO.setUserId(SecurityUtils.getUserId());
                    remoteBacklogService.handled(backlogDTO, SecurityConstants.INNER);
                }
            }

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
                BigDecimal cycleTarget = decomposeDetailCyclesDTO.getCycleTarget();
                if (null != cycleTarget) {
                    validAmountTarget = validAmountTarget.add(cycleTarget);
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
//        this.validTargetDecomposeData(targetDecomposeDTO);
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
        this.sendMessage(targetDecomposeDTO, 1);
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
//        this.validTargetDecomposeData(targetDecomposeDTO);
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
        this.sendMessage(targetDecomposeDTO, 2);
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
//        this.validTargetDecomposeData(targetDecomposeDTO);
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
        this.sendMessage(targetDecomposeDTO, 3);
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
//        this.validTargetDecomposeData(targetDecomposeDTO);
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
        this.sendMessage(targetDecomposeDTO, 4);
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

    @Override
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

    @Override
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
    public TargetDecomposeDTO excelParseObject(TargetDecomposeDTO targetDecomposeDTO, MultipartFile file) {

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
                    } else if (StringUtils.equals(map.get(key), "分解维度")) {
                        timeDimensionKey.set(key + 1);
                    }
                });

                if (i > 1) {
                    List<String> list = new ArrayList<>();
                    map.forEach((key, value) -> {
                        if (0 != employeeNameKey.get()) {
                            if (key.equals(employeeNameKey.get())) {
                                employeeCodes.add(map.get(employeeNameKey.get()));
                                mapAllData.put(DecompositionDimension.EMPLOYEE.getInfo(), employeeCodes);
                            }

                        }
                        if (0 != areaCodesKey.get()) {
                            if (key.equals(areaCodesKey.get())) {
                                areaCodes.add(map.get(areaCodesKey.get()));
                                mapAllData.put(DecompositionDimension.AREA.getInfo(), areaCodes);
                            }

                        }
                        if (0 != departmentCodeKey.get()) {
                            if (key.equals(departmentCodeKey.get())) {
                                departmentCodes.add(map.get(departmentCodeKey.get()));
                                mapAllData.put(DecompositionDimension.DEPARTMENT.getInfo(), departmentCodes);
                            }

                        }
                        if (0 != industryCodeKey.get()) {
                            if (key.equals(industryCodeKey.get())) {
                                industryCodes.add(map.get(industryCodeKey.get()));
                                mapAllData.put(DecompositionDimension.INDUSTRY.getInfo(), industryCodes);
                            }

                        }
                        if (0 != provinceNameKey.get()) {
                            if (key.equals(provinceNameKey.get())) {
                                provinceNames.add(map.get(provinceNameKey.get()));
                                mapAllData.put(DecompositionDimension.REGION.getInfo(), provinceNames);
                            }

                        }
                        if (0 != productCodeKey.get()) {
                            if (key.equals(productCodeKey.get())) {
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
            return this.packExcelData(targetDecomposeDTO, mapAllData, targetDecomposeDetailsDTOS, cyclesExcelData);


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
        return list.get(list.size() - 1);
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
        Long principalEmployeeId = targetDecomposeDTO.getPrincipalEmployeeId();
        R<EmployeeDTO> employeeDTOR = remoteEmployeeService.selectByEmployeeId(principalEmployeeId, SecurityConstants.INNER);
        if (R.SUCCESS != employeeDTOR.getCode()) {
            throw new ServiceException("找不到移交预测负责人");
        }
        EmployeeDTO data = employeeDTOR.getData();
        if (StringUtils.isNull(data)) {
            throw new ServiceException("找不到移交预测负责人");
        }
        Long principalUserId = data.getUserId();
        Long userId = SecurityUtils.getUserId();
        Long employeeId = SecurityUtils.getEmployeeId();
        UserDTO userDTO = SecurityUtils.getLoginUser().getUserDTO();
        List<MessageSendDTO> messageSendDTOS = new ArrayList<>();
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
        if (StringUtils.isEmpty(targetDecomposeDTOS)) {
            throw new ServiceException("找不到要移交预测负责人的目标分解记录");
        }
        //目标分解详情集合
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeIds(targetDecomposeIds);
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                //比对人员id 只移交自己的
                if (employeeId.equals(targetDecomposeDetailsDTO.getPrincipalEmployeeId())) {
                    TargetDecomposeDetails targetDecomposeDetails = new TargetDecomposeDetails();
                    targetDecomposeDetails.setTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                    targetDecomposeDetails.setPrincipalEmployeeId(principalEmployeeId);
                    targetDecomposeDetailsList.add(targetDecomposeDetails);
                }
            }
        }
        if (StringUtils.isNotEmpty(targetDecomposeDetailsList)) {
            try {
                int i = targetDecomposeDetailsMapper.updateTargetDecomposeDetailss(targetDecomposeDetailsList);
                // todo 发送通知
                for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                    Long targetDecomposeId = decomposeDTO.getTargetDecomposeId();
                    MessageSendDTO messageSendDTO = new MessageSendDTO();
                    messageSendDTO.setMessageType(MessageType.PRIVATE_MESSAGE.getCode());
                    messageSendDTO.setBusinessType(BusinessSubtype.ROLLING_PREDICTION_MANAGE_TRANSFER.getParentBusinessType().getCode());
                    messageSendDTO.setBusinessSubtype(BusinessSubtype.ROLLING_PREDICTION_MANAGE_TRANSFER.getCode());
                    messageSendDTO.setBusinessId(targetDecomposeId);
                    messageSendDTO.setMessageTitle(BusinessSubtype.ROLLING_PREDICTION_MANAGE_TRANSFER.getInfo());
                    messageSendDTO.setHandleContent(true);
                    Map<String, Object> paramMap = new HashMap<>();
                    //员工姓名
                    paramMap.put("employee_name", userDTO.getEmployeeName());
                    //员工工号
                    paramMap.put("employee_code", userDTO.getEmployeeCode());
                    //目标年度
                    paramMap.put("target_year", decomposeDTO.getTargetYear());
                    //分解维度
                    paramMap.put("decomposition_dimension", decomposeDTO.getDecompositionDimension());
                    //时间维度
                    paramMap.put("time_dimension", TimeDimension.getInfo(decomposeDTO.getTimeDimension()));
                    //分解类型
                    paramMap.put("target_decompose_type", TargetDecomposeType.getInfo(decomposeDTO.getTargetDecomposeType()));
                    String messageParam = JSONUtil.toJsonStr(paramMap);
                    messageSendDTO.setMessageParam(messageParam);
                    messageSendDTO.setSendUserId(userId);
                    List<MessageReceiverDTO> messageReceivers = new ArrayList<>();
                    MessageReceiverDTO messageReceiverDTO = new MessageReceiverDTO();
                    messageReceiverDTO.setUserId(principalUserId);
                    messageReceivers.add(messageReceiverDTO);
                    messageSendDTO.setMessageReceivers(messageReceivers);
                    messageSendDTOS.add(messageSendDTO);
                }
                if (StringUtils.isNotEmpty(messageSendDTOS)) {
                    R<?> r = remoteMessageService.sendMessages(messageSendDTOS, SecurityConstants.INNER);
                }
                return i;
            } catch (Exception e) {
                throw new ServiceException("移交预测负责人失败");
            }
        } else {
            throw new ServiceException("暂无可移交的数据！");
        }
    }

    /**
     * 远程调用根据目标分解id查询目标分解详情数据
     *
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
     *
     * @param targetDecomposeDetailsDTO
     * @return
     */
    @Override
    public List<TargetDecomposeDetailsDTO> getDecomposeDetails(TargetDecomposeDetailsDTO targetDecomposeDetailsDTO) {
        TargetDecomposeDetails targetDecomposeDetails = new TargetDecomposeDetails();
        BeanUtils.copyProperties(targetDecomposeDetailsDTO, targetDecomposeDetails);
        return targetDecomposeDetailsMapper.selectTargetDecomposeDetailsList(targetDecomposeDetails);
    }

    /**
     * 目标分解是否被引用
     *
     * @param departmentId
     * @return
     */
    @Override
    public List<TargetDecompose> queryDeptDecompose(Long departmentId) {
        List<TargetDecompose> targetDecomposes = targetDecomposeMapper.queryDeptDecompose(departmentId);
        if (StringUtils.isNotEmpty(targetDecomposes)) {
            //指标id集合
            List<Long> collect = targetDecomposes.stream().map(TargetDecompose::getIndicatorId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            //远程调用指标赋值名称
            if (StringUtils.isNotEmpty(collect)) {
                R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(collect, SecurityConstants.INNER);
                List<IndicatorDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetDecompose targetDecompose : targetDecomposes) {
                        for (IndicatorDTO datum : data) {
                            if (targetDecompose.getIndicatorId().equals(datum.getIndicatorId())) {
                                targetDecompose.setIndicatorName(datum.getIndicatorName());
                            }
                        }
                    }
                }
            }
        }


        return targetDecomposes;
    }

    /**
     * 查询目标分解预制数据年份
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public TargetDecomposeDTO selectMaxYear(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecomposeDTO targetDecomposeDTO2 = new TargetDecomposeDTO();
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        Integer targetDecomposeType = targetDecomposeDTO.getTargetDecomposeType();
        if (StringUtils.isNotNull(targetDecomposeType)) {
            if (targetDecomposeType == 1) {
                R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.ORDER.getCode(), SecurityConstants.INNER);
                IndicatorDTO data = indicatorDTOR.getData();
                if (StringUtils.isNotNull(data)) {
                    targetDecomposeDTO.setIndicatorId(data.getIndicatorId());
                }
            } else if (targetDecomposeType == 2) {
                R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.INCOME.getCode(), SecurityConstants.INNER);
                IndicatorDTO data = indicatorDTOR.getData();
                if (StringUtils.isNotNull(data)) {
                    targetDecomposeDTO.setIndicatorId(data.getIndicatorId());
                }
            } else if (targetDecomposeType == 3) {
                R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.RECEIVABLE.getCode(), SecurityConstants.INNER);
                IndicatorDTO data = indicatorDTOR.getData();
                if (StringUtils.isNotNull(data)) {
                    targetDecomposeDTO.setIndicatorId(data.getIndicatorId());
                }
            }

        }
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectMaxYear(targetDecompose);
        if (StringUtils.isNull(targetDecomposeDTO1)) {
            targetDecomposeDTO2.setDecommpFlag(false);
            return targetDecomposeDTO2;
        } else {
            targetDecomposeDTO1.setDecommpFlag(true);
            return targetDecomposeDTO1;
        }


    }

    /**
     * 目标分解操作列导出详情数据
     *
     * @param targetDecomposeId
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeDetailsExcel> exportTargetDecomposeDetails(Long targetDecomposeId, TargetDecomposeDTO targetDecomposeDTO) {
        List<Map<String, String>> fileNameList = targetDecomposeDTO.getFileNameList();
        List<TargetDecomposeDetailsExcel> targetDecomposeDetailsExcelList = new ArrayList<>();
        //详情表
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeId);
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            this.packRemote(targetDecomposeDetailsDTOList);
            List<Long> targetDecomposeDetailsIds = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
            //周期表
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsIds(targetDecomposeDetailsIds);
            //根据周期数分组
            Map<Integer, List<DecomposeDetailCyclesDTO>> cycleNumberMap = decomposeDetailCyclesDTOList.parallelStream().collect(Collectors.groupingBy(DecomposeDetailCyclesDTO::getCycleNumber));
            //根据目标详情id分组
            Map<Long, List<DecomposeDetailCyclesDTO>> decomposeDetailCyclesMap = decomposeDetailCyclesDTOList.parallelStream().collect(Collectors.groupingBy(DecomposeDetailCyclesDTO::getTargetDecomposeDetailsId));

            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                TargetDecomposeDetailsExcel targetDecomposeDetailsExcel = new TargetDecomposeDetailsExcel();
                //分解维度数据集合
                List<String> decompositionDimensions = new ArrayList<>();
                if (StringUtils.isNotEmpty(fileNameList)) {
                    //产品名称
                    String productName = targetDecomposeDetailsDTO.getProductName();
                    //区域名称
                    String areaName = targetDecomposeDetailsDTO.getAreaName();
                    //员工名称
                    String employeeName = targetDecomposeDetailsDTO.getEmployeeName();
                    //省份名称
                    String regionName = targetDecomposeDetailsDTO.getRegionName();
                    //行业名称
                    String industryName = targetDecomposeDetailsDTO.getIndustryName();
                    //部门名称
                    String departmentName = targetDecomposeDetailsDTO.getDepartmentName();
                    for (Map<String, String> stringStringMap : fileNameList) {
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("employeeId", stringStringMap.get("value"))) {

                            decompositionDimensions.add(employeeName);
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("areaId", stringStringMap.get("value"))) {
                            decompositionDimensions.add(areaName);
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("departmentId", stringStringMap.get("value"))) {
                            decompositionDimensions.add(departmentName);
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("productId", stringStringMap.get("value"))) {
                            decompositionDimensions.add(productName);
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("regionId", stringStringMap.get("value"))) {
                            decompositionDimensions.add(regionName);
                        }
                        if (StringUtils.isNotBlank(stringStringMap.get("value")) && StringUtils.equals("industryId", stringStringMap.get("value"))) {
                            decompositionDimensions.add(industryName);
                        }
                    }

                }

                //分解维度数据集合
                targetDecomposeDetailsExcel.setDecompositionDimensions(decompositionDimensions);
                //汇总金额
                targetDecomposeDetailsExcel.setAmountTarget(targetDecomposeDetailsDTO.getAmountTarget().toString());
                //负责人名称
                targetDecomposeDetailsExcel.setPrincipalEmployeeName(targetDecomposeDetailsDTO.getPrincipalEmployeeName());
                if (StringUtils.isNotEmpty(decomposeDetailCyclesMap)) {
                    List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList1 = decomposeDetailCyclesMap.get(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                    if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOList1)) {
                        //周期目标值集合
                        List<String> cycleTargets = new ArrayList<>();
                        //周期目标值总值集合
                        List<String> cycleTargetSum = new ArrayList<>();
                        for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList1) {
                            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList2 = cycleNumberMap.get(decomposeDetailCyclesDTO.getCycleNumber());
                            if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOList2)) {
                                cycleTargetSum.add(decomposeDetailCyclesDTOList2.stream().map(DecomposeDetailCyclesDTO::getCycleTarget).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
                            }
                            cycleTargets.add(decomposeDetailCyclesDTO.getCycleTarget().toString());
                        }
                        targetDecomposeDetailsExcel.setCycleTargets(cycleTargets);
                        targetDecomposeDetailsExcel.setCycleTargetSum(cycleTargetSum);
                    }
                }
                //汇总金额合计
                targetDecomposeDetailsExcel.setAmountTargetSum(targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getAmountTarget).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add).toString());
                targetDecomposeDetailsExcelList.add(targetDecomposeDetailsExcel);
            }

        }
        return targetDecomposeDetailsExcelList;
    }

    /**
     * 根据区域ID集合查询目标分解数据
     * 1.员工ID
     * 2.区域ID
     * 3.部门ID
     * 4.产品ID
     * 5.省份ID
     * 6.行业ID
     * 7.负责人ID
     *
     * @param map map
     * @return List
     */
    @Override
    public List<TargetDecomposeDetailsDTO> selectByIds(Map<Integer, List<Long>> map) {
        List<Long> employeeIds = new ArrayList<>();
        List<Long> areaIds = new ArrayList<>();
        List<Long> departmentIds = new ArrayList<>();
        List<Long> productIds = new ArrayList<>();
        List<Long> regionIds = new ArrayList<>();
        List<Long> industryIds = new ArrayList<>();
        List<Long> principalEmployeeIds = new ArrayList<>();
        if (StringUtils.isNotNull(map) && map.size() == 1) {
            for (Integer integer : map.keySet()) {
                switch (integer) {
                    case 1:
                        employeeIds = map.get(integer);
                        break;
                    case 2:
                        areaIds = map.get(integer);
                        break;
                    case 3:
                        departmentIds = map.get(integer);
                        break;
                    case 4:
                        productIds = map.get(integer);
                        break;
                    case 5:
                        regionIds = map.get(integer);
                        break;
                    case 6:
                        industryIds = map.get(integer);
                        break;
                    case 7:
                        principalEmployeeIds = map.get(integer);
                        break;
                }
                break;
            }
        }
        return targetDecomposeDetailsMapper.selectByIds(employeeIds, areaIds, departmentIds, productIds, regionIds, industryIds, principalEmployeeIds);
    }

    /**
     * 根据指标ID查询目标分解
     *
     * @param indicatorIds 指标ID集合
     * @return List
     */
    @Override
    public List<TargetDecomposeDTO> selectByIndicatorIds(List<Long> indicatorIds) {
        return targetDecomposeMapper.selectByIndicatorIds(indicatorIds);
    }

    /**
     * 导入解析滚动预测
     *
     * @param list
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public TargetDecomposeDTO importProduct(List<DecomposeDetailCyclesDTO> list, TargetDecomposeDTO targetDecomposeDTO) {
        if (StringUtils.isNull(targetDecomposeDTO)) {
            throw new ServiceException("数据不存在 请刷新页面重试！");
        }
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
            for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
                List<List<DecomposeDetailCyclesDTO>> subList = new ArrayList<>();
                Integer timeDimension = targetDecomposeDTO.getTimeDimension();
                if (StringUtils.isNotEmpty(list)) {
                    if (timeDimension == 1) {
                        subList = getSubList(1, list);
                    } else if (timeDimension == 2) {
                        subList = getSubList(2, list);

                    } else if (timeDimension == 3) {
                        subList = getSubList(4, list);
                    } else if (timeDimension == 4) {
                        subList = getSubList(12, list);
                    } else if (timeDimension == 5) {
                        subList = getSubList(52, list);
                    }
                }
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTOS.get(i).getDecomposeDetailCyclesDTOS();
                if (StringUtils.isNotEmpty(subList)) {
                    List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList1 = subList.get(i);
                    for (int i1 = 0; i1 < decomposeDetailCyclesDTOList1.size(); i1++) {
                        decomposeDetailCyclesDTOS.get(i1).setCycleForecast(decomposeDetailCyclesDTOList1.get(i1).getCycleForecast());
                        decomposeDetailCyclesDTOS.get(i1).setCycleActual(decomposeDetailCyclesDTOList1.get(i1).getCycleActual());
                    }
                }
            }

        }
        return targetDecomposeDTO;
    }

    /**
     * 将list拆分成指定数量的小list
     * 注: 使用的subList方式,返回的是list的内部类,不可做元素的删除,修改,添加操作
     *
     * @param length 数量
     * @param list   大list
     * @return
     */
    public List<List<DecomposeDetailCyclesDTO>> getSubList(int length, List<DecomposeDetailCyclesDTO> list) {
        int size = list.size();
        int temp = size / length + 1;
        boolean result = size % length == 0;
        List<List<DecomposeDetailCyclesDTO>> subList = new ArrayList<>();
        for (int i = 0; i < temp; i++) {
            if (i == temp - 1) {
                if (result) {
                    break;
                }
                subList.add(list.subList(length * i, size));
            } else {
                subList.add(list.subList(length * i, length * (i + 1)));
            }
        }
        return subList;
    }

    /**
     * 封装远程调用数据
     *
     * @param targetDecomposeDetailsDTOList 目标分解详情DTO列表
     */
    public void packRemote(List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList) {
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            //人员id集合
            List<Long> employeeIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            //人员id集合滚动预测负责人
            List<Long> principalEmployeeIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getPrincipalEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            //部门id集合
            List<Long> departmentIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            //省份id集合
            Set<Long> regionIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getRegionId).distinct().filter(Objects::nonNull).collect(Collectors.toSet());

            //行业id集合
            List<Long> industryIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            //人员远程
            if (StringUtils.isNotEmpty(employeeIdCollect)) {
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(employeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (EmployeeDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getEmployeeId().equals(datum.getEmployeeId())) {
                                targetDecomposeDetailsDTO.setEmployeeId(datum.getEmployeeId());
                                targetDecomposeDetailsDTO.setEmployeeName(datum.getEmployeeName());
                            }
                        }
                    }
                }
            }
            //人员远程
            if (StringUtils.isNotEmpty(principalEmployeeIdCollect)) {
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(principalEmployeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (EmployeeDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getPrincipalEmployeeId().equals(datum.getEmployeeId())) {
                                targetDecomposeDetailsDTO.setPrincipalEmployeeId(datum.getEmployeeId());
                                targetDecomposeDetailsDTO.setPrincipalEmployeeName(datum.getEmployeeName());
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
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (DepartmentDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                targetDecomposeDetailsDTO.setDepartmentId(datum.getDepartmentId());
                                targetDecomposeDetailsDTO.setDepartmentName(datum.getDepartmentName());
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
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (RegionDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getRegionId().equals(datum.getRegionId())) {
                                targetDecomposeDetailsDTO.setRegionId(datum.getRegionId());
                                targetDecomposeDetailsDTO.setRegionName(datum.getRegionName());
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
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (IndustryDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getIndustryId().equals(datum.getIndustryId())) {
                                targetDecomposeDetailsDTO.setIndustryId(datum.getIndustryId());
                                targetDecomposeDetailsDTO.setIndustryName(datum.getIndustryName());
                            }
                        }
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
            List<Long> indicatorIds = targetDecomposeDTOList.stream().map(TargetDecomposeDTO::getIndicatorId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOList) {
                    for (IndicatorDTO datum : data) {
                        if (decomposeDTO.getIndicatorId().equals(datum.getIndicatorId())) {
                            decomposeDTO.setIndicatorName(datum.getIndicatorName());
                        }
                    }
                }
            }
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
    public TargetDecomposeDTO packExcelData(TargetDecomposeDTO targetDecomposeDTO, Map<String, List<String>> mapAllData, List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS, List<List<String>> cyclesExcelData) {

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
                        if (list.size() - 1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setEmployeeName("");
                            } else {
                                targetDecomposeDetailsDTO.setEmployeeName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        } else {
                            EmployeeDTO employeeDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), EmployeeDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setEmployeeName(employeeDTO.getEmployeeName());
                                targetDecomposeDetailsDTOS.get(i).setEmployeeId(employeeDTO.getEmployeeId());
                            } else {
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
                        if (list.size() - 1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setAreaName("");
                            } else {
                                targetDecomposeDetailsDTO.setAreaName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        } else {
                            AreaDTO areaDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), AreaDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setAreaName(areaDTO.getAreaName());
                                targetDecomposeDetailsDTOS.get(i).setAreaId(areaDTO.getAreaId());
                            } else {
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
                        if (list.size() - 1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setDepartmentName("");
                            } else {
                                targetDecomposeDetailsDTO.setDepartmentName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }
                        } else {
                            DepartmentDTO departmentDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), DepartmentDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setDepartmentName(departmentDTO.getDepartmentName());
                                targetDecomposeDetailsDTOS.get(i).setDepartmentId(departmentDTO.getDepartmentId());
                            } else {
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
                        if (list.size() - 1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setIndustryName("");
                            } else {
                                targetDecomposeDetailsDTO.setIndustryName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }
                        } else {
                            IndustryDTO industryDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), IndustryDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setIndustryName(industryDTO.getIndustryName());
                                targetDecomposeDetailsDTOS.get(i).setIndustryId(industryDTO.getIndustryId());
                            } else {
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
                        if (list.size() - 1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setRegionName("");
                            } else {
                                targetDecomposeDetailsDTO.setRegionName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }

                        } else {
                            RegionDTO regionDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), RegionDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setRegionName(regionDTO.getProvinceName());
                                targetDecomposeDetailsDTOS.get(i).setRegionId(regionDTO.getRegionId());
                            } else {
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
                        if (list.size() - 1 < i) {
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setProductName("");
                            } else {
                                targetDecomposeDetailsDTO.setProductName("");
                                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
                            }
                        } else {
                            ProductDTO productDTO = JSON.toJavaObject(JSONObject.parseObject(JSONObject.toJSONString(list.get(i))), ProductDTO.class);
                            if (targetDecomposeDetailsDTOS.size() == maxSize) {
                                targetDecomposeDetailsDTOS.get(i).setProductName(productDTO.getProductName());
                                targetDecomposeDetailsDTOS.get(i).setProductId(productDTO.getProductId());
                            } else {
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
        if (StringUtils.isEmpty(targetDecomposeDetailsDTOS) && StringUtils.isNotEmpty(cyclesExcelData)) {
            for (int i = 0; i < cyclesExcelData.size(); i++) {
                TargetDecomposeDetailsDTO targetDecomposeDetailsDTO = new TargetDecomposeDetailsDTO();
                targetDecomposeDetailsDTO.setEmployeeName("");
                targetDecomposeDetailsDTOS.add(targetDecomposeDetailsDTO);
            }
        }
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOS)) {
            //详情周期数据
            for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = new ArrayList<>();
                int dime = 0;
                if (targetDecomposeDTO.getTargetYear() <= DateUtils.getYear()) {
                    dime = this.packDecomposeDetailCycles(targetDecomposeDTO);
                }
                List<String> list = cyclesExcelData.get(i);
                for (int i1 = 0; i1 < list.size(); i1++) {
                    if (i1 >= dime - 1) {
                        DecomposeDetailCyclesDTO decomposeDetailCyclesDTO = new DecomposeDetailCyclesDTO();
                        //周期
                        decomposeDetailCyclesDTO.setCycleNumber(i1 + 1);
                        if (StringUtils.isNotBlank(list.get(i1))) {
                            //周期目标值
                            decomposeDetailCyclesDTO.setCycleTarget(new BigDecimal(list.get(i1)));
                        }
                        decomposeDetailCyclesDTOS.add(decomposeDetailCyclesDTO);
                    } else {
                        DecomposeDetailCyclesDTO decomposeDetailCyclesDTO = new DecomposeDetailCyclesDTO();
                        //周期
                        decomposeDetailCyclesDTO.setCycleNumber(i1 + 1);
                        if (StringUtils.isNotBlank(list.get(i1))) {
                            //周期目标值
                            decomposeDetailCyclesDTO.setCycleTarget(new BigDecimal("0"));
                        }
                        decomposeDetailCyclesDTOS.add(decomposeDetailCyclesDTO);
                    }
                }
                targetDecomposeDetailsDTOS.get(i).setDecomposeDetailCyclesDTOS(decomposeDetailCyclesDTOS);
            }

            //excel一行都是空的数据
            List<TargetDecomposeDetailsDTO> deleteTargetExcelNullData = new ArrayList<>();
            //去除无用数据
            for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTOS.get(i).getDecomposeDetailCyclesDTOS();
                //封装删除excel为空的数据
                boolean flag = this.packDeleteExcelNullData(targetDecomposeDetailsDTOS.get(i));
                if (flag) {
                    //周期数据必须全部为空
                    List<BigDecimal> collect = decomposeDetailCyclesDTOS.stream().map(DecomposeDetailCyclesDTO::getCycleTarget).filter(getCycleTarget -> getCycleTarget != null).collect(Collectors.toList());
                    if (StringUtils.isEmpty(collect)) {
                        deleteTargetExcelNullData.add(targetDecomposeDetailsDTOS.get(i));
                    }
                }
            }

            targetDecomposeDetailsDTOS.removeAll(deleteTargetExcelNullData);
        }

        //解析完成数据
        targetDecomposeDTO.setTargetDecomposeDetailsDTOS(targetDecomposeDetailsDTOS);
        return targetDecomposeDTO;
    }

    /**
     * 返回是否是新增还是修改数据 填装数据不一样
     *
     * @param targetDecomposeDTO
     * @return
     */
    private int packDecomposeDetailCycles(TargetDecomposeDTO targetDecomposeDTO) {
        //时间维度
        Integer timeDimension = targetDecomposeDTO.getTimeDimension();
        //时间维度
        //半年度
        if (2 == timeDimension) {
            int month = DateUtils.getMonth();
            if (month > 6) {
                return 1;
            } else {
                return 0;
            }
        }
        //季度
        if (3 == timeDimension) {
            return DateUtils.getQuarter();
        }
        //月度
        if (4 == timeDimension) {
            return DateUtils.getMonth();
        }
        //周
        if (5 == timeDimension) {
            return DateUtils.getDayOfWeek();
        }
        return 0;

    }

    /**
     * 封装删除excel为空的数据
     *
     * @param targetDecomposeDetailsDTO
     */
    private boolean packDeleteExcelNullData(TargetDecomposeDetailsDTO targetDecomposeDetailsDTO) {
        boolean flag = false;
        if (null == targetDecomposeDetailsDTO.getEmployeeId() &&
                null == targetDecomposeDetailsDTO.getDepartmentId() &&
                null == targetDecomposeDetailsDTO.getRegionId() &&
                null == targetDecomposeDetailsDTO.getAreaId() &&
                null == targetDecomposeDetailsDTO.getProductId() &&
                null == targetDecomposeDetailsDTO.getIndustryId()) {
            flag = true;
        }
        return flag;
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
                        for (String s : list) {
                            for (EmployeeDTO employeeDTO : employeeDTOS) {
                                if (StringUtils.equals(s, employeeDTO.getEmployeeCode())) {
                                    EmployeeData.add(JSONObject.parseObject(JSONObject.toJSONString(employeeDTO)));
                                }
                            }
                        }
                        mapAllEndData.put(key, EmployeeData);
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
                        for (String s : list) {
                            for (AreaDTO areaDTO : areaDTOS) {
                                if (StringUtils.equals(s, areaDTO.getAreaCode())) {
                                    areaData.add(JSONObject.parseObject(JSONObject.toJSONString(areaDTO)));
                                }
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
                        for (String s : list) {
                            for (DepartmentDTO departmentDTO : departmentDTOS) {
                                if (StringUtils.equals(s, departmentDTO.getDepartmentCode())) {
                                    DepartmentData.add(JSONObject.parseObject(JSONObject.toJSONString(departmentDTO)));
                                }
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
                        for (String s : list) {
                            for (IndustryDTO industryDTO : industryDTOS) {
                                if (StringUtils.equals(s, industryDTO.getIndustryCode())) {
                                    industryData.add(JSONObject.parseObject(JSONObject.toJSONString(industryDTO)));
                                }
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
                        //最终省份数据
                        List<Object> regionData = new ArrayList<>();
                        for (String s : list) {
                            for (RegionDTO regionDTO : regionDTOS) {
                                if (StringUtils.equals(s, regionDTO.getProvinceName())) {
                                    regionData.add(JSONObject.parseObject(JSONObject.toJSONString(regionDTO)));
                                }
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
                        //最终产品数据
                        List<Object> productData = new ArrayList<>();
                        for (String s : list) {
                            for (ProductDTO productDTO : productDTOS) {
                                if (StringUtils.equals(s, productDTO.getProductCode())) {
                                    productData.add(JSONObject.parseObject(JSONObject.toJSONString(productDTO)));
                                }
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

