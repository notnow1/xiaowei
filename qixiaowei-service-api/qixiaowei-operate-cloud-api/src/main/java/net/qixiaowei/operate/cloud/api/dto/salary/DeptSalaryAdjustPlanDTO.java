package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 部门调薪计划表
 *
 * @author Graves
 * @since 2022-12-11
 */
@Data
@Accessors(chain = true)
public class DeptSalaryAdjustPlanDTO extends BaseDTO {

    //查询检验
    public interface QueryDeptSalaryAdjustPlanDTO extends Default {

    }

    //新增检验
    public interface AddDeptSalaryAdjustPlanDTO extends Default {

    }

    //删除检验
    public interface DeleteDeptSalaryAdjustPlanDTO extends Default {

    }

    //修改检验
    public interface UpdateDeptSalaryAdjustPlanDTO extends Default {

    }

    /**
     * ID
     */
    private Long deptSalaryAdjustPlanId;
    /**
     * 预算年度
     */
    private Integer planYear;
    /**
     * 调薪总数
     */
    private BigDecimal salaryAdjustTotal;
    /**
     * 涨薪包预算
     */
    private BigDecimal raiseSalaryBonusBudget;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 部门调薪项集合
     */
    private List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOS;

}

