package net.qixiaowei.strategy.cloud.api.domain.strategyDecode;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

import java.util.Date;

/**
 * 年度重点工作详情表
 *
 * @author Graves
 * @since 2023-03-14
 */
@Data
@Accessors(chain = true)
public class AnnualKeyWorkDetail extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long annualKeyWorkDetailId;
    /**
     * 年度重点工作ID
     */
    private Long annualKeyWorkId;
    /**
     * 任务编号
     */
    private String taskNumber;
    /**
     * 任务名称
     */
    private String taskName;
    /**
     * 所属部门
     */
    private Long departmentId;
    /**
     * 部门主管
     */
    private Long departmentEmployeeId;
    /**
     * 任务描述
     */
    private String taskDescription;
    /**
     * 闭环标准
     */
    private String closeStandard;
    /**
     * 任务开始时间
     */
    private Date taskStartTime;
    /**
     * 任务结束时间
     */
    private Date taskEndTime;
    /**
     * 责任人
     */
    private Long dutyEmployeeId;
    /**
     * 责任人员姓名
     */
    private String dutyEmployeeName;
    /**
     * 责任人员编码
     */
    private String dutyEmployeeCode;

}

