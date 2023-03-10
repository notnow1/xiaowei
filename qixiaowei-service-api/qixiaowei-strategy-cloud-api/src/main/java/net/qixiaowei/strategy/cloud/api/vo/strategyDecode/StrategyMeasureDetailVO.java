package net.qixiaowei.strategy.cloud.api.vo.strategyDecode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;

/**
 * 战略举措清单VO
 */
@Data
@Accessors(chain = true)
public class StrategyMeasureDetailVO {

    /**
     * 战略举措清单ID
     */
    private Long strategyMeasureId;
    /**
     * 详情ID
     */
    private Long strategyMeasureDetailId;
    /**
     * 战略指标维度ID
     */
    private Long strategyIndexDimensionId;
    /**
     * 战略指标维度名称
     */
    private Long indexDimensionName;
    /**
     * 别名-父级战略指标维度名称
     */
    private String rootIndexDimensionName;
    /**
     * 战略指标维度编码
     */
    private Integer indexDimensionCode;
    /**
     * 序列号
     */
    private Integer serialNumber;
    /**
     * 编码名称
     */
    private String serialNumberName;
    /**
     * 战略举措名称
     */
    private String strategyMeasureName;
    /**
     * 战略举措来源
     */
    private Long strategyMeasureSource;
    /**
     * 战略举措来源
     */
    private String strategyMeasureSourceName;
    /**
     * 排序
     */
    private Integer sort;


    /**
     * 任务ID
     */
    private Long strategyMeasureTaskId;
    /**
     * 关键任务
     */
    private String keyTask;
    /**
     * 闭环标准
     */
    private String closeStandard;
    /**
     * 责任部门
     */
    private Long dutyDepartmentId;
    /**
     * 责任部门名称
     */
    private String dutyDepartmentName;
    /**
     * 责任人员ID
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
     * 任务排序
     */
    private Integer taskSort;


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
}
