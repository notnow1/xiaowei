package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
* 部门奖金包预算表
* @author TANGMICHI
* @since 2022-11-29
*/
@Data
@Accessors(chain = true)
public class DeptBonusBudgetDTO extends BaseDTO {

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
    @NotNull(message = "调整人数人数不能为空", groups = {DeptBonusBudgetDTO.DeleteDeptBonusBudgetDTO.class, DeptBonusBudgetDTO.UpdateDeptBonusBudgetDTO.class})
    private  Long deptBonusBudgetId;
    /**
    * 预算年度
    */
    private  Integer budgetYear;
    /**
     * 总奖金包预算
     */
    private BigDecimal amountBonusBudget;
    /**
     * 部门奖金预算是否可编辑
     */
    private Boolean deptBonusAddFlag;
    /**
    * 战略奖比例 2023/5/6修改名称(公司级奖金占比)
    */
    private BigDecimal strategyAwardPercentage;
    /**
     * 战略奖金额 2023/5/6修改名称(公司级奖金包)
     */
    private BigDecimal strategyAwardAmount;
    /**
     * 部门总奖金包
     */
    private BigDecimal deptAmountBonus;
    /**
     * 部门奖金预算明细表
     */
    private List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS;
    /**
     * 公司及预算明细表
     */
    private List<DeptBonusCompanyDTO> deptBonusCompanyDTOS;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;

}

