package net.qixiaowei.operate.cloud.api.dto.salary;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 奖金预算参数表
 * @author TANGMICHI
 * @since 2022-11-26
 */
@Data
@Accessors(chain = true)
public class BonusBudgetParametersDTO {

    //查询检验
    public interface QueryBonusBudgetParametersDTO extends Default{

    }
    //新增检验
    public interface AddBonusBudgetParametersDTO extends Default{

    }

    //删除检验
    public interface DeleteBonusBudgetParametersDTO extends Default{

    }
    //修改检验
    public interface UpdateBonusBudgetParametersDTO extends Default{

    }
    /**
     * ID
     */
    private  Long bonusBudgetParametersId;
    /**
     * 奖金预算ID
     */
    private  Long bonusBudgetId;
    /**
     * 指标ID
     */
    private  Long indicatorId;
    /**
     * 指标名称
     */
    private  String indicatorName;
    /**
     * 奖金权重(%)
     */
    private  BigDecimal bonusWeight;
    /**
     * 奖金占比基准值(%)
     */
    private  BigDecimal bonusProportionStandard;
    /**
     *奖金增长率后一年
     */
    private BigDecimal bonusGrowthRateAfterOne;
    /**
     * 奖金增长率后二年
     */
    private BigDecimal bonusGrowthRateAfterTwo;


    /**
     * 奖金驱动因素实际数
     */
    private  BigDecimal bonusProportionDrivingFactor;
    /**
     * 奖金占比浮动差值
     */
    private BigDecimal bonusProportionVariation;
    /**
     * 挑战值
     */
    private  BigDecimal challengeValue;
    /**
     * 目标值
     */
    private  BigDecimal targetValue;
    /**
     * 保底值
     */
    private  BigDecimal guaranteedValue;
    /**
     * 预计目标达成率(%)
     */
    private  BigDecimal targetCompletionRate;
    /**
     * 预算年后一年业绩增长率
     */
    private  BigDecimal performanceAfterOne;
    /**
     * 预算年后二年业绩增长率
     */
    private  BigDecimal performanceAfterTwo;
    /**
     * 预算年后一年奖金折让系数
     */
    private  BigDecimal bonusAllowanceAfterOne;
    /**
     * 预算年后二年奖金折让系数
     */
    private  BigDecimal bonusAllowanceAfterTwo;
    /**
     * 删除标记:0未删除;1已删除
     */
    private  Integer deleteFlag;
    /**
     * 创建人
     */
    private  Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
     * 更新人
     */
    private  Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

