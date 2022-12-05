package net.qixiaowei.operate.cloud.api.dto.salary;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 个人年终奖表
* @author TANGMICHI
* @since 2022-12-02
*/
@Data
@Accessors(chain = true)
public class EmployeeAnnualBonusDTO {

    //查询检验
    public interface QueryEmployeeAnnualBonusDTO extends Default{

    }
    //新增检验
    public interface AddEmployeeAnnualBonusDTO extends Default{

    }

    //删除检验
    public interface DeleteEmployeeAnnualBonusDTO extends Default{

    }
    //修改检验
    public interface UpdateEmployeeAnnualBonusDTO extends Default{

    }
    /**
    * ID
    */
    private  Long employeeAnnualBonusId;
    /**
    * 年终奖年度
    */
    private  Integer annualBonusYear;
    /**
    * 一级部门ID
    */
    private  Long departmentId;
    /**
     * 部门名称
     */
    private  String departmentName;
    /**
    * 申请部门ID
    */
    private  Long applyDepartmentId;
    /**
     * 申请部门名称
     */
    private  String applyDepartmentName;
    /**
    * 申请人ID
    */
    private  Long applyEmployeeId;
    /**
     * 申请人名称
     */
    private  String applyEmployeeName;
    /**
    * 分配年终奖金额
    */
    private BigDecimal distributeBonusAmount;
    /**
    * 发起评议流程标记:0否;1是
    */
    private  Integer commentFlag;
    /**
    * 评议环节:1管理团队评议;2主管初评+管理团队评议
    */
    private  Integer commentStep;
    /**
    * 管理团队评议人
    */
    private  Long commentEmployeeId;
    /**
    * 评议日期
    */
    private LocalDate commentDate;
    /**
    * 状态:0草稿;1待初评;2待评议;3已评议
    */
    private  Integer status;
    /**
     * 个人年终奖发放快照信息及发放对象表集合
     */
    private List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs;

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
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;

}

