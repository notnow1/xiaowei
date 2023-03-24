package net.qixiaowei.strategy.cloud.api.dto.strategyDecode;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import javax.validation.groups.Default;
import java.util.List;

/**
 * 战略指标维度表
 *
 * @author Graves
 * @since 2023-03-03
 */
@Data
@Accessors(chain = true)
public class StrategyIndexDimensionDTO extends BaseDTO {

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
    private String parentIndexDimensionName;
    /**
     * 别名-父级战略指标维度名称
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
     * 租户ID
     */
    private Long tenantId;
    /**
     * 请求参数
     */
    private List<StrategyIndexDimensionDTO> children;
    /**
     * 子类
     */
    private List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS;
}

