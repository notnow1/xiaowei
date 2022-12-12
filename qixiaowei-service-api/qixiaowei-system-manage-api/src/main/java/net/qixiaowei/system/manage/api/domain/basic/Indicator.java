package net.qixiaowei.system.manage.api.domain.basic;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;


/**
 * 指标表
 *
 * @author Graves
 * @since 2022-09-28
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Accessors(chain = true)
public class Indicator extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long indicatorId;
    /**
     * 父级指标ID
     */
    private Long parentIndicatorId;
    /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
    private String ancestors;
    /**
     * 指标类型:1财务指标；2业务指标
     */
    private Integer indicatorType;
    /**
     * 指标编码
     */
    private String indicatorCode;
    /**
     * 指标名称
     */
    private String indicatorName;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 层级
     */
    private Integer level;
    /**
     * 指标分类ID
     */
    private Long indicatorCategoryId;
    /**
     * 指标值类型:1金额;2比率
     */
    private Integer indicatorValueType;
    /**
     * 必选标记:0非必选;1必选
     */
    private Integer choiceFlag;
    /**
     * 考核方向:0反向;1正向
     */
    private Integer examineDirection;
    /**
     * 指标定义
     */
    private String indicatorDefinition;
    /**
     * 指标公式
     */
    private String indicatorFormula;
    /**
     * 驱动因素标记:0否;1是
     */
    private Integer drivingFactorFlag;

}

