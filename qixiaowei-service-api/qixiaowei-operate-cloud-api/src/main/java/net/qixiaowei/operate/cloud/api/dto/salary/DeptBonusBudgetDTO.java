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
* 部门奖金包预算表
* @author TANGMICHI
* @since 2022-11-29
*/
@Data
@Accessors(chain = true)
public class DeptBonusBudgetDTO {

    //查询检验
    public interface QueryDeptBonusBudgetDTO extends Default{

    }
    //新增检验
    public interface AddDeptBonusBudgetDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptBonusBudgetDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptBonusBudgetDTO extends Default{

    }
    /**
    * ID
    */
    private  Long deptBonusBudgetId;
    /**
    * 预算年度
    */
    private  Integer budgetYear;
    /**
    * 战略奖比例
    */
    private BigDecimal strategyAwardPercentage;
    /**
     * 部门奖金预算明细表
     */
    private List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS;
    /**
     * 部门奖金预算项目表
     */
    private List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS;
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

