package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.util.Date;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 部门年终奖表
* @author TANGMICHI
* @since 2022-12-06
*/
@Data
@Accessors(chain = true)
public class DeptAnnualBonusDTO {

    //查询检验
    public interface QueryDeptAnnualBonusDTO extends Default{

    }
    //新增检验
    public interface AddDeptAnnualBonusDTO extends Default{

    }

    //删除检验
    public interface DeleteDeptAnnualBonusDTO extends Default{

    }
    //修改检验
    public interface UpdateDeptAnnualBonusDTO extends Default{

    }
    /**
    * ID
    */
    private  Long deptAnnualBonusId;
    /**
    * 年终奖年度
    */
    private  Integer annualBonusYear;
    /**
    * 最终可发总奖金包 旧：公司年终奖总包
    */
    private  BigDecimal companyAnnualBonus;
    /**
    * 可发经营奖总包 旧：部门年终奖总包
    */
    private  BigDecimal departmentAnnualBonus;

    /**
     * 年初总奖金包预算（不考虑目标完成率） 旧：总奖金包预算
     */
    private BigDecimal beYearAmountBonusBudget;
    /**
     * 年底应发总奖金包（根据实际业绩测算) 旧：总奖金包实际
     */
    private BigDecimal endYearSalaryAmountBonus;

    /**
     * 年初可发总奖金包预算
     */
    private BigDecimal beYearDeveAmountBonus;
    /**
     * 战略奖实发
     */
    private BigDecimal strategyDeveAward;



    /**
     * 部门年终奖经营绩效结果表集合
     */
    private List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOs;
    /**
     * 部门年终奖系数表集合
     */
    private List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOs;

    /**
     *部门可发年终奖集合
     */
    private List<DeptAnnualBonusCanGrantDTO> deptAnnualBonusCanGrantDTOs;
    /**
    * 状态
    */
    private  Integer status;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  createTime;

    /**
     * 评议年度
     */
    private  String  createTimeYear;

    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

