package net.qixiaowei.operate.cloud.api.domain.targetManager;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 目标分解维度配置
* @author Graves
* @since 2022-09-26
*/
@Data
@Accessors(chain = true)
public class TargetDecomposeDimension extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  targetDecomposeDimensionId;
     /**
     * 分解维度(region,salesman,department,product,province,industry)
     */
     private  String  decompositionDimension;
     /**
     * 排序
     */
     private  Integer  sort;

}

