package net.qixiaowei.strategy.cloud.api.domain.strategyDecode;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 战略指标维度表
* @author Graves
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class StrategyIndexDimension extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  strategyIndexDimensionId;
     /**
     * 父级战略指标维度ID
     */
     private  Long  parentIndexDimensionId;
     /**
     * 祖级列表ID，按层级用英文逗号隔开
     */
     private  String  ancestors;
     /**
     * 战略指标维度编码
     */
     private  String  indexDimensionCode;
     /**
     * 战略指标维度名称
     */
     private  String  indexDimensionName;
     /**
     * 排序
     */
     private  Integer  sort;
     /**
     * 层级
     */
     private  Integer  level;
     /**
     * 状态:0失效;1生效
     */
     private  Integer  status;

}

