package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;


/**
* 奖金预算表
* @author TANGMICHI
* @since 2022-11-26
*/
@Data
@Accessors(chain = true)
public class BonusBudgetDTO extends BaseDTO {

    //查询检验
    public interface QueryBonusBudgetDTO extends Default{

    }
    //新增检验
    public interface AddBonusBudgetDTO extends Default{

    }

    //删除检验
    public interface DeleteBonusBudgetDTO extends Default{

    }
    //修改检验
    public interface UpdateBonusBudgetDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "奖金预算id不能为空", groups = {BonusBudgetDTO.DeleteBonusBudgetDTO.class, BonusBudgetDTO.UpdateBonusBudgetDTO.class})
    private  Long bonusBudgetId;
    /**
    * 预算年度
    */
    private  Integer budgetYear;
    /**
     * 总奖金包预算参数集合
     */
    @NotEmpty(message = "奖金驱动因素不能为空", groups = {BonusBudgetDTO.DeleteBonusBudgetDTO.class, BonusBudgetDTO.UpdateBonusBudgetDTO.class})
    @Valid
    private List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS;
    /**
     * 总薪酬包预算
     */
    private BigDecimal paymentBonusBudget;

    /**
     * 弹性薪酬包预算
     */
    private BigDecimal elasticityBonusBudget;

    /**
     * 总工资包预算自己定义的(因这个字段是不会保存数据库的 后面修改需求后 字段名没对上 取的别名返回的)
     */
    private BigDecimal basicWageBonusBudget;
    /**
     * 总工资包预算数据库
     */
    private BigDecimal amountWageBudget;
    /**
     * 涨薪包预算
     */
    private BigDecimal raiseSalaryBonusBudget;
    /**
    * 总奖金包预算
    */
    private BigDecimal amountBonusBudget;
    /**
     * 总奖金包预算总奖金包预算1
     */
    private BigDecimal amountBonusBudgetReferenceValueOne;
    /**
     * 总奖金包预算总奖金包预算2
     */
    private BigDecimal amountBonusBudgetReferenceValueTwo;
    /**
    * 预算年度前一年的总奖金包
    */
    private  BigDecimal bonusBeforeOne;

    /**
     * 奖金包实际数
     */
    private  BigDecimal bonusActualSum;

    /**
     * 总奖金包预算阶梯集合
     */
    private List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS;

    /**
     * 未来三年奖金趋势集合
     */
    private List<FutureBonusBudgetLaddertersDTO> futureBonusBudgetLaddertersDTOS;
    /**
     * 挑战值
     */
    private  BigDecimal bonusChallengeValue;
    /**
     * 目标值
     */
    private  BigDecimal bonusTargetValue;
    /**
     * 保底值
     */
    private  BigDecimal bonusGuaranteedValue;
    /**
     * 飞跃值
     */
    private  BigDecimal bonusLeapValue;
    /**
     * 梦想值
     */
    private  BigDecimal bonusDreamValue;
    /**
     * 最低值
     */
    private  BigDecimal bonusMinValue;
    /**
     * 是否可编辑权限 0:不可编辑 1:可编辑
     */
    private Integer editFlag;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
     * 指标ID
     */
    private  Long  indicatorId;
    /**
     * 指标id集合
     */
    private  List<Long>  indicatorIds;
}

