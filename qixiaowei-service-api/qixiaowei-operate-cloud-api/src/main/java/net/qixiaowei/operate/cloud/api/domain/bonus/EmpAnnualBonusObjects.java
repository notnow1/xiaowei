package net.qixiaowei.operate.cloud.api.domain.bonus;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 个人年终奖发放对象表
 *
 * @author TANGMICHI
 * @since 2022-12-15
 */
@Data
@Accessors(chain = true)
public class EmpAnnualBonusObjects extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * 主管ID
     */
    private Long responsibleEmployeeId;
    /**
     * 主管ID名称
     */
    private String responsibleEmployeeName;
    /**
     * 部门负责人id
     */
    private Long departmentLeaderId;

    /**
     * 部门负责人名称
     */
    private String departmentLeaderName;
    /**
     * ID
     */
    private Long empAnnualBonusObjectsId;
    /**
     * 个人年终奖ID
     */
    private Long employeeAnnualBonusId;
    /**
     * 员工ID
     */
    private Long employeeId;

    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 选中标记:0否;1是
     */
    private Integer choiceFlag;
    /**
     * 绩效等级ID
     */
    private Long performanceRankId;
    /**
     * 绩效等级系数ID
     */
    private Long performanceRankFactorId;
    /**
     * 绩效
     */
    private String performanceRank;
    /**
     * 绩效奖金系数
     */
    private BigDecimal performanceBonusFactor;
    /**
     * 考勤系数
     */
    private BigDecimal attendanceFactor;
    /**
     * 建议值
     */
    private BigDecimal recommendValue;
    /**
     * 评议值
     */
    private BigDecimal commentValue;
    /**
     * 状态:0草稿;1待初评;2待评议;3已评议
     */
    private Integer status;

}

