package net.qixiaowei.operate.cloud.api.dto.salary;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.groups.Default;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 个人调薪计划表
 *
 * @author Graves
 * @since 2022-12-15
 */
@Data
@Accessors(chain = true)
public class EmpSalaryAdjustPlanDTO extends BaseDTO {

    //查询检验
    public interface QueryEmpSalaryAdjustPlanDTO extends Default {

    }

    //新增检验
    public interface AddEmpSalaryAdjustPlanDTO extends Default {

    }

    //删除检验
    public interface DeleteEmpSalaryAdjustPlanDTO extends Default {

    }

    //修改检验
    public interface UpdateEmpSalaryAdjustPlanDTO extends Default {

    }

    /**
     * ID
     */
    private Long empSalaryAdjustPlanId;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 工号
     */
    private String employeeCode;

    //==============================员工调整后的数据==================================//

    /**
     * 生效日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date effectiveDate;
    /**
     * 生效开始日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date effectiveDateStart;
    /**
     * 生效结束日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date effectiveDateEnd;
    /**
     * 调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
     */
    private String adjustmentType;
    /**
     * 调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
     */
    private List<Integer> adjustmentTypeList;
    /**
     * 调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
     */
    private String adjustmentTypeName;
    /**
     * 调整部门ID
     */
    private Long adjustDepartmentId;
    /**
     * 调整部门名称
     */
    private String adjustDepartmentName;
    /**
     * 调整岗位ID
     */
    private Long adjustPostId;
    /**
     * 调整岗位名称
     */
    private String adjustPostName;
    /**
     * 调整职级体系ID
     */
    private Long adjustOfficialRankSystemId;
    /**
     * 调整职级体系名称
     */
    private String adjustOfficialRankSystemName;
    /**
     * 调整职级
     */
    private Integer adjustOfficialRank;
    /**
     * 调整职级名称
     */
    private String adjustOfficialRankName;
    /**
     * 调整薪酬
     */
    private BigDecimal adjustEmolument;
    /**
     * 调整说明
     */
    private String adjustExplain;
    /**
     * 状态(0草稿;1生效)
     */
    private Integer status;
    /**
     * 是否提交(0保存;1提交)
     */
    private Integer isSubmit;
    /**
     * 近三次绩效结果
     */
    private List<Map<String, String>> performanceResultList;
    /**
     * 个人调薪记录
     */
    private List<EmpSalaryAdjustPlanDTO> employeeSalarySnapVOS;

    //==============================员工调整前的数据==================================//

    /**
     * 员工姓名
     */
    private String employeeName;
    /**
     * 入职日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private LocalDate employmentDate;
    /**
     * 入职开始日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date employmentDateStart;
    /**
     * 入职结束日期
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date employmentDateEnd;
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
     * 租户ID
     */
    private Long tenantId;

    /**
     * 当前员工工资
     */
    private BigDecimal employeeBasicWage;

    /**
     * 当前员工部门
     */
    private Long employeeDepartmentId;

    /**
     * 当前员工部门
     */
    private String employeeDepartmentName;

    /**
     * 当前部门主管
     */
    private Long employeeDepartmentLeaderId;

    /**
     * 当前部门主管
     */
    private String employeeDepartmentLeaderName;

    /**
     * 当前岗位
     */
    private Long employeePostId;

    /**
     * 当前岗位
     */
    private String employeePostName;

    /**
     * 当前职级
     */
    private Integer employeeOfficialRank;

    /**
     * 当前职级名称
     */
    private String employeeOfficialRankName;


}

