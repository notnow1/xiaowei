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
     * 三级项目(奖项名称)
     */
    private String thirdLevelItem;
    /**
     * 年初预算包
     */
    private BigDecimal beYearAmountBonusBudget;

    /**
     * 一月金额
     */
    private BigDecimal amountJanuary;
    /**
     * 二月金额
     */
    private BigDecimal amountFebruary;
    /**
     * 三月金额
     */
    private BigDecimal amountMarch;
    /**
     * 四月金额
     */
    private BigDecimal amountApril;
    /**
     * 五月金额
     */
    private BigDecimal amountMay;
    /**
     * 六月金额
     */
    private BigDecimal amountJune;
    /**
     * 七月金额
     */
    private BigDecimal amountJuly;
    /**
     * 八月金额
     */
    private BigDecimal amountAugust;
    /**
     * 九月金额
     */
    private BigDecimal amountSeptember;
    /**
     * 十月金额
     */
    private BigDecimal amountOctober;
    /**
     * 十一月金额
     */
    private BigDecimal amountNovember;
    /**
     * 十二月金额
     */
    private BigDecimal amountDecember;
    /**
     * 累计发放
     */
    private BigDecimal amountTotal;

}

