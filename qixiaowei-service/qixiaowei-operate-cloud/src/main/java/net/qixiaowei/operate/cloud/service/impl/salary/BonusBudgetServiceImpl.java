package net.qixiaowei.operate.cloud.service.impl.salary;

import java.math.BigDecimal;
import java.util.*;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcome;
import net.qixiaowei.operate.cloud.api.dto.salary.BonusBudgetLaddertersDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.BonusBudgetParametersDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmolumentPlanMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeMapper;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.BonusBudget;
import net.qixiaowei.operate.cloud.api.dto.salary.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.mapper.salary.BonusBudgetMapper;
import net.qixiaowei.operate.cloud.service.salary.IBonusBudgetService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * BonusBudgetService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-11-26
 */
@Service
public class BonusBudgetServiceImpl implements IBonusBudgetService {
    @Autowired
    private BonusBudgetMapper bonusBudgetMapper;
    @Autowired
    private RemoteIndicatorService remoteIndicatorService;
    @Autowired
    private SalaryPayMapper salaryPayMapper;

    @Autowired
    private TargetOutcomeMapper targetOutcomeMapper;
    @Autowired
    private EmolumentPlanMapper emolumentPlanMapper;

    /**
     * 查询奖金预算表
     *
     * @param bonusBudgetId 奖金预算表主键
     * @return 奖金预算表
     */
    @Override
    public BonusBudgetDTO selectBonusBudgetByBonusBudgetId(Long bonusBudgetId) {
        return bonusBudgetMapper.selectBonusBudgetByBonusBudgetId(bonusBudgetId);
    }

    /**
     * 查询奖金预算表列表
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 奖金预算表
     */
    @Override
    public List<BonusBudgetDTO> selectBonusBudgetList(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        return bonusBudgetMapper.selectBonusBudgetList(bonusBudget);
    }

    /**
     * 新增奖金预算表
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    public BonusBudgetDTO insertBonusBudget(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        bonusBudget.setCreateBy(SecurityUtils.getUserId());
        bonusBudget.setCreateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        bonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        bonusBudgetMapper.insertBonusBudget(bonusBudget);
        bonusBudgetDTO.setBonusBudgetId(bonusBudget.getBonusBudgetId());
        return bonusBudgetDTO;
    }

    /**
     * 修改奖金预算表
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    public int updateBonusBudget(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        return bonusBudgetMapper.updateBonusBudget(bonusBudget);
    }

    /**
     * 逻辑批量删除奖金预算表
     *
     * @param bonusBudgetIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteBonusBudgetByBonusBudgetIds(List<Long> bonusBudgetIds) {
        return bonusBudgetMapper.logicDeleteBonusBudgetByBonusBudgetIds(bonusBudgetIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除奖金预算表信息
     *
     * @param bonusBudgetId 奖金预算表主键
     * @return 结果
     */
    @Override
    public int deleteBonusBudgetByBonusBudgetId(Long bonusBudgetId) {
        return bonusBudgetMapper.deleteBonusBudgetByBonusBudgetId(bonusBudgetId);
    }

    /**
     * 新增奖金预算预制数据
     *
     * @param budgetYear
     * @return
     */
    @Override
    public BonusBudgetDTO addBonusBudgetTamount(int budgetYear) {
        BonusBudgetDTO bonusBudgetDTO = new BonusBudgetDTO();
        //1总奖金包预算参数集合
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = new ArrayList<>();
        //封装总奖金包预算参数指标数据
        this.packBounParamIndicatorIds(budgetYear, bonusBudgetParametersDTOS);

        //3 封装总奖金包预算生成
        this.packPaymentBonusBudget(budgetYear,bonusBudgetDTO);
        bonusBudgetDTO.setBonusBudgetParametersDTOS(bonusBudgetParametersDTOS);
        return bonusBudgetDTO;
    }

    /**
     * 封装总奖金包预算生成
     * @param budgetYear
     * @param bonusBudgetDTO
     */
    private void packPaymentBonusBudget(int budgetYear,  BonusBudgetDTO bonusBudgetDTO) {
        EmolumentPlanDTO emolumentPlanDTO = emolumentPlanMapper.selectEmolumentPlanByPlanYear(budgetYear);
        if (StringUtils.isNotNull(emolumentPlanDTO)){
            //查询薪酬规划详情计算方法
            EmolumentPlanServiceImpl.queryCalculate(emolumentPlanDTO);
            //总薪酬包
            BigDecimal emolumentPackage = emolumentPlanDTO.getEmolumentPackage();
            bonusBudgetDTO.setPaymentBonusBudget(emolumentPackage);

            //er值
            BigDecimal er = emolumentPlanDTO.getEr();

        }

    }

    /**
     * 封装总奖金包预算阶梯数据
     * 1:先计算总奖金包预算总奖金包预算1 2 值
     * 2：计算表头目标值 挑战值等
     * 3：计算奖金驱动因素/比值（%） 公式=总奖金包预算参考值1÷目标值
     * 4：倒推其他行数据
     *
     * @param bonusBudgetParametersDTOS
     */
    @Override
    public BonusBudgetDTO selectBonusBudgetLadderters(List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        BonusBudgetDTO bonusBudgetDTO = new BonusBudgetDTO();
        //总奖金包预算阶梯集合
        List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS = new ArrayList<>();
        //封装总部门奖金预算 必须计算的数据方法
        this.packBounLadderNum(bonusBudgetDTO, bonusBudgetLaddertersDTOS, bonusBudgetParametersDTOS);
        return bonusBudgetDTO;
    }

    /**
     * 封装总部门奖金预算 必须计算的数据方法
     *
     * @param bonusBudgetDTO
     * @param bonusBudgetLaddertersDTOS
     * @param bonusBudgetParametersDTOS
     */
    private void packBounLadderNum(BonusBudgetDTO bonusBudgetDTO, List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        //总奖金包预算总奖金包预算1
        BigDecimal amountBonusBudgetReferenceValueOne = new BigDecimal("0");
        //总奖金包预算总奖金包预算2
        BigDecimal amountBonusBudgetReferenceValueTwo = new BigDecimal("0");
        //挑战值
        BigDecimal bonusChallengeValue = new BigDecimal("0");
        //保底值
        BigDecimal bonusGuaranteedValue = new BigDecimal("0");
        //目标值
        BigDecimal bonusTargetValue = new BigDecimal("0");
        //飞跃值
        BigDecimal bonusLeapValue = new BigDecimal("0");
        //梦想值
        BigDecimal bonusDreamValue = new BigDecimal("0");
        //最低值
        BigDecimal bonusMinValue = new BigDecimal("0");
        //奖金驱动因素/比值（%） 第四行 公式=总奖金包预算参考值1÷目标值
        BigDecimal bonusProportionRatio = new BigDecimal("0");
        //奖金驱动因素/比值（%）的行间差额 公式=各项（奖金驱动因素的奖金占比浮动差值×权重）的和
        BigDecimal bonusProportionDifference = new BigDecimal("0");
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                //目标值
                BigDecimal targetValue = bonusBudgetParametersDTO.getTargetValue();
                //挑战值
                BigDecimal challengeValue = bonusBudgetParametersDTO.getChallengeValue();
                //保底值
                BigDecimal guaranteedValue = bonusBudgetParametersDTO.getGuaranteedValue();
                //奖金占比基准值
                BigDecimal bonusProportionStandard = bonusBudgetParametersDTO.getBonusProportionStandard();
                //奖金占比浮动差值
                BigDecimal bonusProportionVariation = bonusBudgetParametersDTO.getBonusProportionVariation();
                //奖金权重(%)
                BigDecimal bonusWeight = bonusBudgetParametersDTO.getBonusWeight();
                //预算准确率
                BigDecimal targetValue1 = bonusBudgetParametersDTO.getTargetValue();
                //总奖金包预算总奖金包预算1
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) > 0 &&
                        null != bonusProportionStandard && bonusProportionStandard.compareTo(new BigDecimal("0")) > 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) > 0) {
                    BigDecimal multiply = targetValue.multiply(bonusProportionStandard).multiply(bonusWeight);
                    //总奖金包预算总奖金包预算1 公式=各项（奖金驱动因素的目标值×奖金占比基准值×权重）的和
                    amountBonusBudgetReferenceValueOne = amountBonusBudgetReferenceValueOne.add(multiply);
                }
                //总奖金包预算总奖金包预算2
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) > 0 &&
                        null != bonusProportionStandard && bonusProportionStandard.compareTo(new BigDecimal("0")) > 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) > 0 &&
                        null != targetValue1 && targetValue1.compareTo(new BigDecimal("0")) > 0) {
                    BigDecimal multiply = targetValue.multiply(bonusProportionStandard).multiply(bonusWeight).multiply(targetValue1);
                    //总奖金包预算总奖金包预算1 公式=各项（奖金驱动因素的目标值×奖金占比基准值×权重×预算准确率）的和
                    amountBonusBudgetReferenceValueTwo = amountBonusBudgetReferenceValueTwo.add(multiply);
                }
                //目标值 公式 =各项（奖金驱动因素的目标值×权重）之和
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) > 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) > 0) {
                    BigDecimal multiply = targetValue.multiply(bonusWeight);
                    //目标值 公式 =各项（奖金驱动因素的目标值×权重）之和
                    bonusTargetValue = bonusTargetValue.add(multiply);
                }
                //挑战值 公式 =各项（奖金驱动因素的挑战值×权重）之和
                if (null != challengeValue && challengeValue.compareTo(new BigDecimal("0")) > 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) > 0) {
                    BigDecimal multiply = challengeValue.multiply(bonusWeight);
                    //挑战值 公式 =各项（奖金驱动因素的挑战值×权重）之和
                    bonusChallengeValue = bonusChallengeValue.add(multiply);
                }
                //保底值 公式 =各项（奖金驱动因素的保底值×权重）之和
                if (null != guaranteedValue && guaranteedValue.compareTo(new BigDecimal("0")) > 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) > 0) {
                    BigDecimal multiply = guaranteedValue.multiply(bonusWeight);
                    //保底值 公式 =各项（奖金驱动因素的保底值×权重）之和
                    bonusGuaranteedValue = bonusGuaranteedValue.add(multiply);
                }
                //奖金驱动因素/比值（%）的行间差额 公式 =各项（奖金驱动因素的奖金占比浮动差值×权重）的和
                if (null != bonusProportionVariation && bonusProportionVariation.compareTo(new BigDecimal("0")) > 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) > 0) {
                    BigDecimal multiply = bonusProportionVariation.multiply(bonusWeight).divide(new BigDecimal("100"));
                    //奖金驱动因素/比值（%）的行间差额 公式 =各项（奖金驱动因素的奖金占比浮动差值×权重）的和
                    bonusProportionDifference = bonusProportionDifference.add(multiply);
                }
            }
            //最低值 公式=保底值×0.8
            if (null != bonusGuaranteedValue && bonusGuaranteedValue.compareTo(new BigDecimal("0")) > 0) {
                bonusMinValue = bonusGuaranteedValue.multiply(new BigDecimal("0.8"));
            }
            //飞跃值 公式=挑战值×1.2
            if (null != bonusChallengeValue && bonusChallengeValue.compareTo(new BigDecimal("0")) > 0) {
                bonusLeapValue = bonusChallengeValue.multiply(new BigDecimal("1.2"));
            }
            //梦想值 公式=挑战值×1.5
            if (null != bonusChallengeValue && bonusChallengeValue.compareTo(new BigDecimal("0")) > 0) {
                bonusDreamValue = bonusChallengeValue.multiply(new BigDecimal("1.5"));
            }
            if (null != amountBonusBudgetReferenceValueOne && amountBonusBudgetReferenceValueOne.compareTo(new BigDecimal("0")) > 0 &&
                    null != bonusTargetValue && bonusTargetValue.compareTo(new BigDecimal("0")) > 0) {
                //奖金驱动因素/比值 公式=总奖金包预算参考值1÷目标值
                bonusProportionRatio = amountBonusBudgetReferenceValueOne.divide(bonusTargetValue).multiply(new BigDecimal("100"));
            }
            //总奖金包预算总奖金包预算1
            bonusBudgetDTO.setAmountBonusBudgetReferenceValueOne(amountBonusBudgetReferenceValueOne);
            //总奖金包预算总奖金包预算2
            bonusBudgetDTO.setAmountBonusBudgetReferenceValueTwo(amountBonusBudgetReferenceValueTwo);
            //挑战值
            bonusBudgetDTO.setBonusChallengeValue(bonusChallengeValue);
            //目标值
            bonusBudgetDTO.setBonusTargetValue(bonusTargetValue);
            //保底值
            bonusBudgetDTO.setBonusGuaranteedValue(bonusGuaranteedValue);
            //飞跃值
            bonusBudgetDTO.setBonusLeapValue(bonusLeapValue);
            //梦想值
            bonusBudgetDTO.setBonusDreamValue(bonusDreamValue);
            //最低值
            bonusBudgetDTO.setBonusMinValue(bonusMinValue);
        }
        if (StringUtils.isNotNull(bonusBudgetDTO)) {
            //封装总部门奖金预算阶梯数据集合
            this.packBonusBudgetLaddertersDTO(bonusBudgetDTO, bonusBudgetLaddertersDTOS, bonusProportionRatio, bonusProportionDifference);
        }

    }

    /**
     * 封装总部门奖金预算阶梯数据集合
     *
     * @param bonusBudgetDTO
     * @param bonusBudgetLaddertersDTOS
     * @param bonusProportionRatio
     * @param bonusProportionDifference
     */
    private void packBonusBudgetLaddertersDTO(BonusBudgetDTO bonusBudgetDTO, List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS, BigDecimal bonusProportionRatio, BigDecimal bonusProportionDifference) {
        //挑战值
        BigDecimal bonusChallengeValue = bonusBudgetDTO.getBonusChallengeValue();
        //目标值
        BigDecimal bonusTargetValue = bonusBudgetDTO.getBonusTargetValue();
        //保底值
        BigDecimal bonusGuaranteedValue = bonusBudgetDTO.getBonusGuaranteedValue();
        //飞跃值
        BigDecimal bonusLeapValue = bonusBudgetDTO.getBonusLeapValue();
        //梦想值
        BigDecimal bonusDreamValue = bonusBudgetDTO.getBonusDreamValue();
        //最低值
        BigDecimal bonusMinValue = bonusBudgetDTO.getBonusMinValue();
        //阶梯图的七行
        if (null != bonusProportionRatio && bonusProportionRatio.compareTo(new BigDecimal("0")) > 0 &&
                null != bonusProportionDifference && bonusProportionDifference.compareTo(new BigDecimal("0")) > 0) {
            for (int i = 0; i < 8; i++) {
                BonusBudgetLaddertersDTO bonusBudgetLaddertersDTO = new BonusBudgetLaddertersDTO();
                if (i == 0) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.add(bonusProportionDifference.multiply(new BigDecimal("3"))));
                } else if (i == 2) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.add(bonusProportionDifference.multiply(new BigDecimal("2"))));
                } else if (i == 3) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.add(bonusProportionDifference));
                } else if (i == 4) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio);
                } else if (i == 5) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.subtract(bonusProportionDifference));
                } else if (i == 6) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.subtract(bonusProportionDifference.multiply(new BigDecimal("2"))));
                } else if (i == 7) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.subtract(bonusProportionDifference.multiply(new BigDecimal("3"))));
                }
                bonusBudgetLaddertersDTOS.add(bonusBudgetLaddertersDTO);
            }
        }
        //倒推阶梯图的七行其他数据
        if (StringUtils.isNotEmpty(bonusBudgetLaddertersDTOS)) {
            for (BonusBudgetLaddertersDTO bonusBudgetLaddertersDTO : bonusBudgetLaddertersDTOS) {
                //奖金驱动因素/比值（%）
                BigDecimal bonusProportionRatio1 = bonusBudgetLaddertersDTO.getBonusProportionRatio();
                //挑战值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal challengeValue = new BigDecimal("0");
                //目标值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal targetValue = new BigDecimal("0");
                //保底值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal guaranteedValue = new BigDecimal("0");
                //飞跃值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal leapValue = new BigDecimal("0");
                //梦想值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal dreamValue = new BigDecimal("0");
                //最低值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal minValue = new BigDecimal("0");
                //挑战值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0"))>0 &&
                    null != bonusChallengeValue && bonusChallengeValue.compareTo(new BigDecimal("0"))>0){
                    challengeValue= bonusProportionRatio1.multiply(bonusChallengeValue);
                    bonusBudgetLaddertersDTO.setChallengeValue(challengeValue);
                }
                //目标值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0"))>0 &&
                        null != bonusTargetValue && bonusTargetValue.compareTo(new BigDecimal("0"))>0){
                    targetValue= bonusProportionRatio1.multiply(bonusTargetValue);
                    bonusBudgetLaddertersDTO.setTargetValue(targetValue);
                }
                //保底值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0"))>0 &&
                        null != bonusGuaranteedValue && bonusGuaranteedValue.compareTo(new BigDecimal("0"))>0){
                    guaranteedValue= bonusProportionRatio1.multiply(bonusGuaranteedValue);
                    bonusBudgetLaddertersDTO.setGuaranteedValue(guaranteedValue);
                }
                //飞跃值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0"))>0 &&
                        null != bonusLeapValue && bonusLeapValue.compareTo(new BigDecimal("0"))>0){
                    leapValue= bonusProportionRatio1.multiply(bonusLeapValue);
                    bonusBudgetLaddertersDTO.setLeapValue(leapValue);
                }
                //梦想值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0"))>0 &&
                        null != bonusDreamValue && bonusDreamValue.compareTo(new BigDecimal("0"))>0){
                    dreamValue= bonusProportionRatio1.multiply(bonusDreamValue);
                    bonusBudgetLaddertersDTO.setDreamValue(dreamValue);
                }
                //最低值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0"))>0 &&
                        null != bonusMinValue && bonusMinValue.compareTo(new BigDecimal("0"))>0){
                    minValue= bonusProportionRatio1.multiply(bonusMinValue);
                    bonusBudgetLaddertersDTO.setMinValue(minValue);
                }
            }
        }
    }

    /**
     * 封装总奖金包预算参数指标数据
     *
     * @param budgetYear
     * @param bonusBudgetParametersDTOS
     */
    private void packBounParamIndicatorIds(int budgetYear, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        //当前月份倒推12个月的“奖金”部分合计
        BigDecimal bonusActualSum = salaryPayMapper.selectBonusActualNum(budgetYear, DateUtils.getMonth());


        //远程调用指标是否驱动因素为“是”列表
        R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIsDriverList(SecurityConstants.INNER);
        //封装奖金驱动因素实际数
        Map<Long, BigDecimal> indicatorIdBonusMap = new HashMap<>();
        List<IndicatorDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {
            for (IndicatorDTO datum : data) {
                BonusBudgetParametersDTO bonusBudgetParametersDTO = new BonusBudgetParametersDTO();
                //指标id
                bonusBudgetParametersDTO.setIndicatorId(datum.getIndicatorId());
                //奖金占比基准值(%)
                bonusBudgetParametersDTO.setBonusProportionStandard(new BigDecimal("0"));
                //预计目标达成率(%)
                bonusBudgetParametersDTO.setTargetCompletionRate(new BigDecimal("0"));
                bonusBudgetParametersDTOS.add(bonusBudgetParametersDTO);
            }
            //指标id集合
            List<Long> collect = bonusBudgetParametersDTOS.stream().map(BonusBudgetParametersDTO::getIndicatorId).filter(Objects::nonNull).collect(Collectors.toList());
            TargetOutcome targetOutcome = new TargetOutcome();
            //目标年度
            targetOutcome.setTargetYear(budgetYear);
            //指标id集合
            if (StringUtils.isNotEmpty(collect)) {
                targetOutcome.setIndicatorIds(collect);
                targetOutcome.setLimitYear(collect.size() * 2);
            }

            //查询总奖金包预算参数的指标目标 挑战 保底 和奖金驱动因素实际数：从关键经营结果取值，当前月份倒推12个月的合计
            List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS = targetOutcomeMapper.selectDrivingFactor(targetOutcome);
            if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOS)) {
                //封装奖金驱动因素实际数
                indicatorIdBonusMap = packMonth(targetOutcomeDetailsDTOS, DateUtils.getMonth());

            }
            if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
                for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                    BigDecimal bonusProportionStandard = new BigDecimal("0");
                    //奖金驱动因素实际数
                    BigDecimal bonusProportionDrivingFactor = indicatorIdBonusMap.get(bonusBudgetParametersDTO.getIndicatorId());
                    //奖金占比基准值 公式=奖金包实际数÷奖金驱动因素实际数
                    if (null != bonusProportionDrivingFactor && bonusProportionDrivingFactor.compareTo(new BigDecimal("0")) != 0 &&
                            null != bonusActualSum && bonusActualSum.compareTo(new BigDecimal("0")) != 0) {
                         bonusProportionStandard = bonusActualSum.divide(bonusProportionDrivingFactor, BigDecimal.ROUND_CEILING).multiply(new BigDecimal("100"));
                        //奖金占比基准值(%)
                        bonusBudgetParametersDTO.setBonusProportionStandard(bonusProportionStandard);

                    }

                }
            }
        }


    }

    /**
     * 封装奖金驱动因素实际数
     *
     * @param targetOutcomeDetailsDTOS
     * @param month
     */
    private Map<Long, BigDecimal> packMonth(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS, int month) {
        //根据指标id 奖金驱动因素实际数Map
        Map<Long, BigDecimal> bonusMap = new HashMap<>();

        //根据指标id分组
        Map<Long, List<TargetOutcomeDetailsDTO>> indicatorIdMap = targetOutcomeDetailsDTOS.parallelStream().collect(Collectors.groupingBy(TargetOutcomeDetailsDTO::getIndicatorId));
        if (StringUtils.isNotEmpty(indicatorIdMap)) {
            for (Long aLong : indicatorIdMap.keySet()) {
                List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS1 = indicatorIdMap.get(aLong);
                //奖金驱动因素实际数
                BigDecimal bonusProportionDrivingFactor = new BigDecimal("0");
                if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOS1)) {
                    //月份数据map集合
                    List<Map<Integer, BigDecimal>> listMap = new ArrayList<>();
                    for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOS1) {
                        //所有月份对应的实际值
                        Map<Integer, BigDecimal> map = new HashMap<>();
                        //一月实际值
                        map.put(1, targetOutcomeDetailsDTO.getActualJanuary());
                        //二月实际值
                        map.put(2, targetOutcomeDetailsDTO.getActualFebruary());
                        //三月实际值
                        map.put(3, targetOutcomeDetailsDTO.getActualMarch());
                        //四月实际值
                        map.put(4, targetOutcomeDetailsDTO.getActualApril());
                        //五月实际值
                        map.put(5, targetOutcomeDetailsDTO.getActualMay());
                        //六月实际值
                        map.put(6, targetOutcomeDetailsDTO.getActualJune());
                        //七月实际值
                        map.put(7, targetOutcomeDetailsDTO.getActualJuly());
                        //八月实际值
                        map.put(8, targetOutcomeDetailsDTO.getActualAugust());
                        //九月实际值
                        map.put(9, targetOutcomeDetailsDTO.getActualSeptember());
                        //十月实际值
                        map.put(10, targetOutcomeDetailsDTO.getActualOctober());
                        //十一月实际值
                        map.put(11, targetOutcomeDetailsDTO.getActualNovember());
                        //十二月实际值
                        map.put(12, targetOutcomeDetailsDTO.getActualDecember());
                        listMap.add(map);
                    }
                    if (listMap.size() == 1) {
                        for (Map<Integer, BigDecimal> integerBigDecimalMap : listMap) {
                            for (Integer key : integerBigDecimalMap.keySet()) {
                                bonusProportionDrivingFactor = bonusProportionDrivingFactor.add(integerBigDecimalMap.get(key));
                            }

                        }
                    } else {
                        for (int i = 0; i < listMap.size(); i++) {
                            for (Integer key : listMap.get(i).keySet()) {
                                if (i == 0) {
                                    if (key < month) {
                                        bonusProportionDrivingFactor = bonusProportionDrivingFactor.add(listMap.get(i).get(key));
                                    }
                                } else if (i == 1) {
                                    if (key > month) {
                                        bonusProportionDrivingFactor = bonusProportionDrivingFactor.add(listMap.get(i).get(key));
                                    }
                                }
                            }
                        }
                    }

                }
                //添加
                bonusMap.put(aLong, bonusProportionDrivingFactor);
            }
        }


        return bonusMap;
    }

    /**
     * 逻辑删除奖金预算表信息
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    public int logicDeleteBonusBudgetByBonusBudgetId(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        bonusBudget.setBonusBudgetId(bonusBudgetDTO.getBonusBudgetId());
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        return bonusBudgetMapper.logicDeleteBonusBudgetByBonusBudgetId(bonusBudget);
    }

    /**
     * 物理删除奖金预算表信息
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */

    @Override
    public int deleteBonusBudgetByBonusBudgetId(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        return bonusBudgetMapper.deleteBonusBudgetByBonusBudgetId(bonusBudget.getBonusBudgetId());
    }

    /**
     * 物理批量删除奖金预算表
     *
     * @param bonusBudgetDtos 需要删除的奖金预算表主键
     * @return 结果
     */

    @Override
    public int deleteBonusBudgetByBonusBudgetIds(List<BonusBudgetDTO> bonusBudgetDtos) {
        List<Long> stringList = new ArrayList();
        for (BonusBudgetDTO bonusBudgetDTO : bonusBudgetDtos) {
            stringList.add(bonusBudgetDTO.getBonusBudgetId());
        }
        return bonusBudgetMapper.deleteBonusBudgetByBonusBudgetIds(stringList);
    }

    /**
     * 批量新增奖金预算表信息
     *
     * @param bonusBudgetDtos 奖金预算表对象
     */

    public int insertBonusBudgets(List<BonusBudgetDTO> bonusBudgetDtos) {
        List<BonusBudget> bonusBudgetList = new ArrayList();

        for (BonusBudgetDTO bonusBudgetDTO : bonusBudgetDtos) {
            BonusBudget bonusBudget = new BonusBudget();
            BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
            bonusBudget.setCreateBy(SecurityUtils.getUserId());
            bonusBudget.setCreateTime(DateUtils.getNowDate());
            bonusBudget.setUpdateTime(DateUtils.getNowDate());
            bonusBudget.setUpdateBy(SecurityUtils.getUserId());
            bonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            bonusBudgetList.add(bonusBudget);
        }
        return bonusBudgetMapper.batchBonusBudget(bonusBudgetList);
    }

    /**
     * 批量修改奖金预算表信息
     *
     * @param bonusBudgetDtos 奖金预算表对象
     */

    public int updateBonusBudgets(List<BonusBudgetDTO> bonusBudgetDtos) {
        List<BonusBudget> bonusBudgetList = new ArrayList();

        for (BonusBudgetDTO bonusBudgetDTO : bonusBudgetDtos) {
            BonusBudget bonusBudget = new BonusBudget();
            BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
            bonusBudget.setCreateBy(SecurityUtils.getUserId());
            bonusBudget.setCreateTime(DateUtils.getNowDate());
            bonusBudget.setUpdateTime(DateUtils.getNowDate());
            bonusBudget.setUpdateBy(SecurityUtils.getUserId());
            bonusBudgetList.add(bonusBudget);
        }
        return bonusBudgetMapper.updateBonusBudgets(bonusBudgetList);
    }
}

