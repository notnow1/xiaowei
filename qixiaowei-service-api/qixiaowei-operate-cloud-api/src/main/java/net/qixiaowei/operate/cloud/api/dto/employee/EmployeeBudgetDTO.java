package net.qixiaowei.operate.cloud.api.dto.employee;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 人力预算表
 *
 * @author TANGMICHI
 * @since 2022-11-18
 */
@Data
@Accessors(chain = true)
public class EmployeeBudgetDTO extends BaseDTO {

    //查询检验
    public interface QueryEmployeeBudgetDTO extends Default {

    }

    //新增检验
    public interface AddEmployeeBudgetDTO extends Default {

    }

    //删除检验
    public interface DeleteEmployeeBudgetDTO extends Default {

    }

    //修改检验
    public interface UpdateEmployeeBudgetDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "ID不能为空", groups = {EmployeeBudgetDTO.UpdateEmployeeBudgetDTO.class})
    private Long employeeBudgetId;
    /**
     * 人力预算详情表id集合
     */
    private List<Long> employeeBudgetDetailsIds;

    /**
     * 预算年度
     */
    @NotNull(message = "预算年度不能为空", groups = {EmployeeBudgetDTO.AddEmployeeBudgetDTO.class, EmployeeBudgetDTO.UpdateEmployeeBudgetDTO.class})
    private Integer budgetYear;
    /**
     * 预算部门ID
     */
    @NotNull(message = "预算部门不能为空", groups = {EmployeeBudgetDTO.AddEmployeeBudgetDTO.class, EmployeeBudgetDTO.UpdateEmployeeBudgetDTO.class})
    private Long departmentId;

    /**
     * 岗位职级
     */
    private Integer officialRank;
    /**
     * 预算部门名称
     */
    private String departmentName;
    /**
     * 职级体系ID
     */
    @NotNull(message = "职级体系不能为空", groups = {EmployeeBudgetDTO.AddEmployeeBudgetDTO.class, EmployeeBudgetDTO.UpdateEmployeeBudgetDTO.class})
    private Long officialRankSystemId;

    /**
     * 职级体系名称
     */
    private String officialRankSystemName;
    /**
     * 预算周期:1季度;2月度
     */
    private Integer budgetCycle;
    /**
     * 上年期末人数
     */
    private Integer amountLastYear;
    /**
     * 本年新增人数
     */
    private Integer amountAdjust;
    /**
     * 平均新增数
     */
    private BigDecimal amountAverageAdjust;
    /**
     * 人力预算明细表集合
     */
    @NotEmpty(message = "人力预算明细表集合不能为空", groups = {EmployeeBudgetDTO.AddEmployeeBudgetDTO.class, EmployeeBudgetDTO.UpdateEmployeeBudgetDTO.class})
    @Valid
    private List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS;
    /**
     * 年度平均人数
     */
    private BigDecimal annualAverageNum;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
}

