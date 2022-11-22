package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

/**
 * 工资发薪表
 *
 * @author Graves
 * @since 2022-11-17
 */
@Data
@Accessors(chain = true)
public class SalaryPayDTO {

    //查询检验
    public interface QuerySalaryPayDTO extends Default {

    }

    //新增检验
    public interface AddSalaryPayDTO extends Default {

    }

    //删除检验
    public interface DeleteSalaryPayDTO extends Default {

    }

    //修改检验
    public interface UpdateSalaryPayDTO extends Default {

    }

    /**
     * ID
     */
    private Long salaryPayId;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 员工工号
     */
    private String employeeCode;
    /**
     * 员工名称
     */
    private String employeeName;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 岗位职级
     */
    private String postRank;
    /**
     * 岗位职级名称
     */
    private String postRankName;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 发薪年份
     */
    private Integer payYear;
    /**
     * 发薪月份
     */
    private Integer payMonth;
    /**
     * 工资金额
     */
    private BigDecimal salaryAmount;
    /**
     * 津贴金额
     */
    private BigDecimal allowanceAmount;
    /**
     * 福利金额
     */
    private BigDecimal welfareAmount;
    /**
     * 奖金金额
     */
    private BigDecimal bonusAmount;
    /**
     * 代扣代缴金额
     */
    private BigDecimal withholdRemitTax;
    /**
     * 其他扣款金额
     */
    private BigDecimal otherDeductions;
    /**
     * 发薪金额
     */
    private BigDecimal payAmount;
    /**
     * 月度工资数据
     */
    private List<SalaryPayDTO> salaryPayDTOList;
    /**
     * 月度工资数据详情
     */
    private List<SalaryItemDTO> salaryItemDTOList;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}

