package net.qixiaowei.operate.cloud.api.domain.salary;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;

import java.math.BigDecimal;

import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;

/**
 * 个人调薪计划表
 *
 * @author Graves
 * @since 2022-12-15
 */
@Data
@Accessors(chain = true)
public class EmpSalaryAdjustPlan extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long empSalaryAdjustPlanId;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 生效日期
     */
    private Date effectiveDate;
    /**
     * 调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
     */
    private String adjustmentType;
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

}

