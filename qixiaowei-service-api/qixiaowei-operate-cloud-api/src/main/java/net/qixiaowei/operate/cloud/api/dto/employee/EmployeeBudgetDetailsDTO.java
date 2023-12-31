package net.qixiaowei.operate.cloud.api.dto.employee;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
 * 人力预算明细表
 * @author TANGMICHI
 * @since 2022-11-22
 */
@Data
@Accessors(chain = true)
public class EmployeeBudgetDetailsDTO extends BaseDTO {

    //查询检验
    public interface QueryEmployeeBudgetDetailsDTO extends Default{

    }
    //新增检验
    public interface AddEmployeeBudgetDetailsDTO extends Default{

    }

    //删除检验
    public interface DeleteEmployeeBudgetDetailsDTO extends Default{

    }
    //修改检验
    public interface UpdateEmployeeBudgetDetailsDTO extends Default{

    }
    /**
     * ID
     */
    private  Long employeeBudgetDetailsId;
    /**
     * 人力预算详情表id集合
     */
    private  List<Long> employeeBudgetDetailsIds;
    /**
     * 人力预算ID
     */
    private  Long employeeBudgetId;
    /**
     * 预算年度
     */
    private  Integer budgetYear;
    /**
     * 预算部门ID
     */
    private  Long departmentId;
    /**
     * 预算部门名称
     */
    private  String departmentName;
    /**
     * 职级体系ID
     */
    private  Long officialRankSystemId;

    /**
     * 职级体系名称
     */
    private  String officialRankSystemName;
    /**
     * 岗位职级
     */
    @NotNull(message = "岗位职级不能为空", groups = {EmployeeBudgetDetailsDTO.AddEmployeeBudgetDetailsDTO.class, EmployeeBudgetDetailsDTO.UpdateEmployeeBudgetDetailsDTO.class})
    private  Integer officialRank;

    /**
     * 岗位职级名称
     */
    private  String officialRankName;
    /**
     * 上年期末人数
     */
    @NotNull(message = "上年期末人数不能为空", groups = {EmployeeBudgetDetailsDTO.AddEmployeeBudgetDetailsDTO.class, EmployeeBudgetDetailsDTO.UpdateEmployeeBudgetDetailsDTO.class})
    private  Integer numberLastYear;

    /**
     * 上年平均工资
     */
    private  BigDecimal agePayAmountLastYear;
    /**
     * 上年平均工资来源 0:工资表 1:职级确认薪酬
     */
    private  int agePayAmountLastYearFlag;
    /**
     * 增人/减人工资包
     */
    private  BigDecimal increaseAndDecreasePay;
    /**
     * 本年新增人数
     */
    @NotNull(message = "本年新增人数不能为空", groups = {EmployeeBudgetDetailsDTO.AddEmployeeBudgetDetailsDTO.class, EmployeeBudgetDetailsDTO.UpdateEmployeeBudgetDetailsDTO.class})
    private  Integer countAdjust;
    /**
     * 平均新增数
     */
    @NotNull(message = "平均新增数不能为空", groups = {EmployeeBudgetDetailsDTO.AddEmployeeBudgetDetailsDTO.class, EmployeeBudgetDetailsDTO.UpdateEmployeeBudgetDetailsDTO.class})
    private BigDecimal averageAdjust;
    /**
     * 平均规划新增人数
     */
    private BigDecimal averageAdjustPlan;
    /**
     * 年度平均人数
     */
    @NotNull(message = "年度平均人数不能为空", groups = {EmployeeBudgetDetailsDTO.AddEmployeeBudgetDetailsDTO.class, EmployeeBudgetDetailsDTO.UpdateEmployeeBudgetDetailsDTO.class})
    private BigDecimal annualAverageNum;
    /**
     * 年度规划人数
     */
    private Integer annualPlanningNum;
    /**
     * 年末总计
     */
    private Integer endYearSum;

    /**
     * 人力预算调整表集合
     */
    @NotEmpty(message = "人力预算明细表集合不能为空",groups = {EmployeeBudgetDetailsDTO.AddEmployeeBudgetDetailsDTO.class, EmployeeBudgetDetailsDTO.UpdateEmployeeBudgetDetailsDTO.class})
    @Valid
    private List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS;


}

