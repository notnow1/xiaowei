package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 部门调薪项表
 *
 * @author Graves
 * @since 2022-12-11
 */
@Data
@Accessors(chain = true)
public class DeptSalaryAdjustItemDTO {

    //查询检验
    public interface QueryDeptSalaryAdjustItemDTO extends Default {

    }

    //新增检验
    public interface AddDeptSalaryAdjustItemDTO extends Default {

    }

    //删除检验
    public interface DeleteDeptSalaryAdjustItemDTO extends Default {

    }

    //修改检验
    public interface UpdateDeptSalaryAdjustItemDTO extends Default {

    }

    /**
     * ID
     */
    private Long deptSalaryAdjustItemId;
    /**
     * 部门调薪计划ID
     */
    private Long deptSalaryAdjustPlanId;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 覆盖比例
     */
    private BigDecimal coveragePercentage;
    /**
     * 调整比例
     */
    private BigDecimal adjustmentPercentage;
    /**
     * 调整时间
     */
    @JsonFormat(pattern = "yyyy/MM", timezone = "GMT+8")
    private Date adjustmentTime;
    /**
     * 上年工资包
     */
    private BigDecimal lastSalary;
    /**
     * 新增工资包
     */
    private BigDecimal addSalary;
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
    /**
     * 租户ID
     */
    private Long tenantId;

}

