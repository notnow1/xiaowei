package net.qixiaowei.operate.cloud.api.dto.salary;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 奖金预算表
* @author TANGMICHI
* @since 2022-11-26
*/
@Data
@Accessors(chain = true)
public class BonusBudgetDTO {

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
    private  Long bonusBudgetId;
    /**
    * 预算年度
    */
    private  Integer budgetYear;
    /**
     * 总奖金包预算参数集合
     */
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
     * 总工资包预算
     */
    private BigDecimal basicWageBonusBudget;
    /**
     * 涨薪包预算
     */
    private BigDecimal raiseSalaryBonusBudget;
    /**
    * 总奖金包预算
    */
    private BigDecimal amountBonusBudget;
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

