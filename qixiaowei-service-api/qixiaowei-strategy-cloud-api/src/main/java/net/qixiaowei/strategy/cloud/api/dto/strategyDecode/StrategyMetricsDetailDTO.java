package net.qixiaowei.strategy.cloud.api.dto.strategyDecode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 战略衡量指标详情表
 *
 * @author Graves
 * @since 2023-03-07
 */
@Data
@Accessors(chain = true)
public class StrategyMetricsDetailDTO {

    //查询检验
    public interface QueryStrategyMetricsDetailDTO extends Default {

    }

    //新增检验
    public interface AddStrategyMetricsDetailDTO extends Default {

    }

    //删除检验
    public interface DeleteStrategyMetricsDetailDTO extends Default {

    }

    //修改检验
    public interface UpdateStrategyMetricsDetailDTO extends Default {

    }

    /**
     * ID
     */
    private Long strategyMetricsDetailId;
    /**
     * 战略衡量指标ID
     */
    private Long strategyMetricsId;
    /**
     * 战略指标维度ID
     */
    private Long strategyIndexDimensionId;
    /**
     * 战略指标维度名称
     */
    private String indexDimensionName;
    /**
     * 别名-父级战略指标维度名称
     */
    private String rootIndexDimensionName;
    /**
     * 战略指标维度编码
     */
    private String indexDimensionCode;
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
     * 指标ID
     */
    private Long indicatorId;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 指标值类型:1金额;2比率
     */
    private Integer indicatorValueType;
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
    /**
     * 规划期列表
     */
    private List<Map<String, Object>> periodList;
}

