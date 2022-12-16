package net.qixiaowei.operate.cloud.api.dto.bonus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
* 奖金发放台账
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class BonusPayStandingDTO {

    //查询检验
    public interface QueryBonusPayStandingDTO extends Default{

    }
    //新增检验
    public interface AddBonusPayStandingDTO extends Default{

    }

    //删除检验
    public interface DeleteBonusPayStandingDTO extends Default{

    }
    //修改检验
    public interface UpdateBonusPayStandingDTO extends Default{

    }


    /**
     * 部门ID
     */
    private  Long  departmentId;
    /**
     * 部门名称
     */
    private  String  departmentName;
    /**
     * 奖项类别,工资条ID
     */
    private  Long salaryItemId;
    /**
     * 奖项名称
     */
    private  String awardName;
    /**
     * 年初预算包
     */
    private BigDecimal beYearAmountBonusBudget;

    /**
     * 一月实际值
     */
    private BigDecimal amountJanuary;
    /**
     * 二月实际值
     */
    private BigDecimal amountFebruary;
    /**
     * 三月实际值
     */
    private BigDecimal amountMarch;
    /**
     * 四月实际值
     */
    private BigDecimal amountApril;
    /**
     * 五月实际值
     */
    private BigDecimal amountMay;
    /**
     * 六月实际值
     */
    private BigDecimal amountJune;
    /**
     * 七月实际值
     */
    private BigDecimal amountJuly;
    /**
     * 八月实际值
     */
    private BigDecimal amountAugust;
    /**
     * 九月实际值
     */
    private BigDecimal amountSeptember;
    /**
     * 十月实际值
     */
    private BigDecimal amountOctober;
    /**
     * 十一月实际值
     */
    private BigDecimal amountNovember;
    /**
     * 十二月实际值
     */
    private BigDecimal amountDecember;




}

