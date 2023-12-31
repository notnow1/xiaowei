package net.qixiaowei.operate.cloud.api.dto.bonus;

import java.math.BigDecimal;
import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

/**
* 个人年终奖表
* @author TANGMICHI
* @since 2022-12-02
*/
@Data
@Accessors(chain = true)
public class EmployeeAnnualBonusDTO extends BaseDTO {

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
     * 申请年终奖金额
     */
    private BigDecimal applyBonusAmount;
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
     * 管理团队评议名称
     */
    private  String commentEmployeeName;
    /**
    * 评议日期
    */
    @JsonFormat(pattern = "yyyy/MM/dd",timezone = "GMT+8")
    private Date commentDate;
    /**
    * 状态:0草稿;1待初评;2待评议;3已评议
    */
    private  Integer status;
    /**
     * 个人年终奖发放快照信息及发放对象表集合
     */
    private List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs;
    /**
     * 实时请求人员数据list
     */
    private List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOsTwo;
    /**
     * 是否可提交 0保存 1提交
     */
    private Integer submitFlag;
    /**
     * 详情展示数据 0草稿 1主管 2团队
     */
    private Integer inChargeTeamFlag;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
}

