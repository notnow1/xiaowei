package net.qixiaowei.system.manage.api.vo.basic;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
public class EmployeeSalaryPlanVO {
    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long employeeId;

    /**
     * 工号
     */
    private String employeeCode;
    /**
     * 姓名
     */
    private String employeeName;
    /**
     * 员工部门ID
     */
    private Long departmentId;
    /**
     * 员工部门名称
     */
    private String departmentName;
    /**
     * 员工岗位ID
     */
    private Long postId;
    /**
     * 员工岗位名称
     */
    private String postName;
    /**
     * 岗位职级名称
     */
    private String postRankName;
    /**
     * 员工职级
     */
    private Integer officialRank;

    private String officialRankName;
    /**
     * 员工基本工资
     */
    private BigDecimal basicWage;
    /**
     * 员工基本工资-调整后
     */
    private BigDecimal adjustEmolument;
    /**
     * 入职日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date employmentDate;
    /**
     * 职级体系名称
     */
    private String officialRankSystemName;
    /**
     * 职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 司龄
     */
    private String seniority;
    /**
     * 用工状态:0离职;1在职
     */
    private Integer employmentStatus;
    private String employmentStatusName;
    /**
     * 部门负责人id
     */
    private Long departmentLeaderId;
    /**
     * 主管
     */
    private String departmentLeaderName;
    /**
     * 近三次绩效结果集合
     */
    private List<Map<String, String>> performanceResultList;
    /**
     * 个人调薪记录
     */
    private List<EmployeeSalarySnapVO> employeeSalarySnapVOS;
}
