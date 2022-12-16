package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;

/**
 * 个人调薪快照表
 *
 * @author Graves
 * @since 2022-12-15
 */
@Data
@Accessors(chain = true)
public class EmpSalaryAdjustSnapDTO {

    //查询检验
    public interface QueryEmpSalaryAdjustSnapDTO extends Default {

    }

    //新增检验
    public interface AddEmpSalaryAdjustSnapDTO extends Default {

    }

    //删除检验
    public interface DeleteEmpSalaryAdjustSnapDTO extends Default {

    }

    //修改检验
    public interface UpdateEmpSalaryAdjustSnapDTO extends Default {

    }

    /**
     * ID
     */
    private Long empSalaryAdjustSnapId;
    /**
     * 个人调薪计划ID
     */
    private Long empSalaryAdjustPlanId;
    /**
     * 员工姓名
     */
    private String employeeName;
    /**
     * 入职日期
     */
    private LocalDate employmentDate;
    /**
     * 司龄
     */
    private String seniority;
    /**
     * 原部门ID
     */
    private Long departmentId;
    /**
     * 原部门名称
     */
    private String departmentName;
    /**
     * 部门负责人ID
     */
    private Long departmentLeaderId;
    /**
     * 部门负责人姓名
     */
    private String departmentLeaderName;
    /**
     * 原岗位ID
     */
    private Long postId;
    /**
     * 原岗位名称
     */
    private String postName;
    /**
     * 原职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 原职级体系名称
     */
    private String officialRankSystemName;
    /**
     * 原职级
     */
    private Integer officialRank;
    /**
     * 原职级名称
     */
    private String officialRankName;
    /**
     * 基本工资(当前薪酬)
     */
    private BigDecimal basicWage;
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

