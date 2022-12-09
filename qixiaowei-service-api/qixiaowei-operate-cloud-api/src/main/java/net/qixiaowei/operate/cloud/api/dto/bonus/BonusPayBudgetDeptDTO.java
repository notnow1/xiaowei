package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 奖金发放预算部门表
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class BonusPayBudgetDeptDTO {

    //查询检验
    public interface QueryBonusPayBudgetDeptDTO extends Default{

    }
    //新增检验
    public interface AddBonusPayBudgetDeptDTO extends Default{

    }

    //删除检验
    public interface DeleteBonusPayBudgetDeptDTO extends Default{

    }
    //修改检验
    public interface UpdateBonusPayBudgetDeptDTO extends Default{

    }
    /**
    * ID
    */
    private  Long bonusPayBudgetDeptId;
    /**
    * 奖金发放申请ID
    */
    private  Long bonusPayApplicationId;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
     * 奖项类别,工资条ID
     */
    private  Long salaryItemId;
    /**
    * 奖金比例
    */
    private  BigDecimal bonusPercentage;
    /**
     * 奖项总金额
     */
    private  BigDecimal awardTotalAmount;
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
    /**
    * 租户ID
    */
    private  Long tenantId;

}

