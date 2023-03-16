package net.qixiaowei.strategy.cloud.api.dto.strategyDecode;

import java.time.LocalDate;
import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.Map;

/**
 * 年度重点工作详情表
 *
 * @author Graves
 * @since 2023-03-16
 */
@Data
@Accessors(chain = true)
public class AnnualKeyWorkDetailDTO {

    //查询检验
    public interface QueryAnnualKeyWorkDetailDTO extends Default {

    }

    //新增检验
    public interface AddAnnualKeyWorkDetailDTO extends Default {

    }

    //删除检验
    public interface DeleteAnnualKeyWorkDetailDTO extends Default {

    }

    //修改检验
    public interface UpdateAnnualKeyWorkDetailDTO extends Default {

    }

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
     * 所属部门名称
     */
    private String departmentName;
    /**
     * 部门主管
     */
    private Long departmentEmployeeId;
    /**
     * 部门主管名称
     */
    private String departmentEmployeeName;
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
    private LocalDate taskStartTime;
    /**
     * 任务结束时间
     */
    private LocalDate taskEndTime;
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
    /**
     * 排序
     */
    private Integer sort;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 租户ID
     */
    private Long tenantId;

    /**
     * 请求参数
     */
    private Map<String, Object> params;
}

