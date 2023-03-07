package net.qixiaowei.strategy.cloud.api.dto.strategyDecode;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.groups.Default;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 战略举措清单表
 *
 * @author Graves
 * @since 2023-03-07
 */
@Data
@Accessors(chain = true)
public class StrategyMeasureDTO {

    //查询检验
    public interface QueryStrategyMeasureDTO extends Default {

    }

    //新增检验
    public interface AddStrategyMeasureDTO extends Default {

    }

    //删除检验
    public interface DeleteStrategyMeasureDTO extends Default {

    }

    //修改检验
    public interface UpdateStrategyMeasureDTO extends Default {

    }

    /**
     * ID
     */
    private Long strategyMeasureId;
    /**
     * 规划年度
     */
    private Integer planYear;
    /**
     * 规划业务单元ID
     */
    private Long planBusinessUnitId;
    /**
     * 规划业务单元维度(region,department,product,industry)
     */
    private String businessUnitDecompose;
    /**
     * 区域ID
     */
    private Long areaId;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 产品ID
     */
    private Long productId;
    /**
     * 行业ID
     */
    private Long industryId;
    /**
     * 区域名称
     */
    private String areaName;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 产品名称
     */
    private String productName;
    /**
     * 行业名称
     */
    private String industryName;
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
     * 战略举措清单详情表
     */
    private List<StrategyMeasureDetailDTO> strategyMeasureDetailDTOS;
    /**
     * 战略举措清单任务表
     */
    private List<StrategyMeasureTaskDTO> strategyMeasureTaskDTOS;

}

