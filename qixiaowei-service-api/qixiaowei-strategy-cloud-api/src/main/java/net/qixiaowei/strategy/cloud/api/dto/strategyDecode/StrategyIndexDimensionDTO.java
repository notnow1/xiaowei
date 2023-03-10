package net.qixiaowei.strategy.cloud.api.dto.strategyDecode;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.Map;

/**
 * 战略指标维度表
 *
 * @author Graves
 * @since 2023-03-03
 */
@Data
@Accessors(chain = true)
public class StrategyIndexDimensionDTO {

    //查询检验
    public interface QueryStrategyIndexDimensionDTO extends Default {

    }

    //新增检验
    public interface AddStrategyIndexDimensionDTO extends Default {

    }

    //删除检验
    public interface DeleteStrategyIndexDimensionDTO extends Default {

    }

    //修改检验
    public interface UpdateStrategyIndexDimensionDTO extends Default {

    }

    /**
     * ID
     */
    private Long strategyIndexDimensionId;
    /**
     * 父级战略指标维度ID
     */
    private Long parentIndexDimensionId;
    /**
     * 父级战略指标维度名称
     */
    private String  parentIndexDimensionName;
    /**
     * 父级战略指标维度名称
     */
    private String rootIndexDimensionName;
    /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
    private String ancestors;
    /**
     * 战略指标维度编码
     */
    private String indexDimensionCode;
    /**
     * 战略指标维度名称
     */
    private String indexDimensionName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * 备注
     */
    private String remark;
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
     * 请求参数
     */
    private List<StrategyIndexDimensionDTO> children;
    /**
     * 子类
     */
    private List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS;
}

