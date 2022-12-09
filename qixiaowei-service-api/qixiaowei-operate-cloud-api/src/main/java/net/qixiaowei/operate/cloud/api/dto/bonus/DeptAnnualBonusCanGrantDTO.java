package net.qixiaowei.operate.cloud.api.dto.bonus;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.List;

/**
* 部门可发年终奖
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class DeptAnnualBonusCanGrantDTO {

    //查询检验
    public interface QueryDeptAnnualBonusCanGrantDTO extends Default{

    }
    //新增检验
    public interface AddDeptAnnualBonusCanGrantDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptAnnualBonusCanGrantDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptAnnualBonusCanGrantDTO extends Default{

    }
    /**
    * ID
    */
    private  Long deptAnnualBonusFactorId;
    /**
    * 部门年终奖ID
    */
    private  Long deptAnnualBonusId;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
     * 部门名称
     */
    private  String departmentName;
    /**
     * 年初奖金预算-奖金占比
     */
    private BigDecimal beYearCanGrantBonusBudgetPro;

    /**
     * 年初奖金预算-金额
     */
    private BigDecimal beYearCanGrantBonusBudgetAmount;


    /**
     * 可发经营奖总包-奖金占比
     */
    private BigDecimal beYearCanGrantManagePro;
    /**
     * 可发经营奖总包-金额
     */
    private BigDecimal beYearCanGrantManageAmount;

    /**
     *部门奖金包发放奖金项目
     */
    private List<DeptAnnualBonusItemDTO> deptAnnualBonusItemDTOS;

    /**
     * 可分配年终奖
     */
    private  BigDecimal distributeBonus;
    /**
     * 可分配年终奖参考值
     */
    private  BigDecimal distributeBonusReference;
}

