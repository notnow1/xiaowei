package net.qixiaowei.operate.cloud.api.dto.bonus;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;

/**
* 部门奖金预算项目表
* @author TANGMICHI
* @since 2022-12-01
*/
@Data
@Accessors(chain = true)
public class DeptBonusCompanyDTO {

    //查询检验
    public interface QueryDeptBonusCompanyDTO extends Default{

    }
    //新增检验
    public interface AddDeptBonusCompanyDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptBonusCompanyDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptBonusCompanyDTO extends Default{

    }
    /**
    * ID
    */
    private  Long deptBonusBudgetItemsId;
    /**
    * 部门奖金包预算ID
    */
    private  Long deptBonusBudgetId;
    /**
    * 部门奖金预算明细ID
    */
    private  Long deptBonusBudgetDetailsId;
    /**
    * 工资项ID
    */
    private  Long salaryItemId;
    /**
     * 工资项名称
     */
    private  String salaryItemName;
    /**
    * 公司奖金占比
    */
    private  BigDecimal bonusCompanyPercentage;
    /**
     * 公司奖金金额
     */
    private BigDecimal bonusCompanyAmount;
    /**
    * 排序
    */
    private  Integer sort;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

