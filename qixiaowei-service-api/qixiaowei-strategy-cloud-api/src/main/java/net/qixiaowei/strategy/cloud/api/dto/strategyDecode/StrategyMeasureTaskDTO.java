package net.qixiaowei.strategy.cloud.api.dto.strategyDecode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.Map;

/**
 * 战略举措清单任务表
 *
 * @author Graves
 * @since 2023-03-07
 */
@Data
@Accessors(chain = true)
public class StrategyMeasureTaskDTO {

    //查询检验
    public interface QueryStrategyMeasureTaskDTO extends Default {

    }

    //新增检验
    public interface AddStrategyMeasureTaskDTO extends Default {

    }

    //删除检验
    public interface DeleteStrategyMeasureTaskDTO extends Default {

    }

    //修改检验
    public interface UpdateStrategyMeasureTaskDTO extends Default {

    }

    /**
     * ID
     */
    private Long strategyMeasureTaskId;
    /**
     * 战略举措清单ID
     */
    private Long strategyMeasureId;
    /**
     * 战略举措清单详情ID
     */
    private Long strategyMeasureDetailId;
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

