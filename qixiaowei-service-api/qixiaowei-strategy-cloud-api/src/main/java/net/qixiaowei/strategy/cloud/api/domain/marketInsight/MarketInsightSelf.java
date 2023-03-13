package net.qixiaowei.strategy.cloud.api.domain.marketInsight;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 市场洞察自身表
* @author TANGMICHI
* @since 2023-03-13
*/
@Data
@Accessors(chain = true)
public class MarketInsightSelf extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  marketInsightSelfId;
     /**
     * 规划年度
     */
     private  Integer  planYear;
     /**
     * 规划业务单元ID
     */
     private  Long  planBusinessUnitId;
     /**
     * 规划业务单元维度(region,department,product,industry)
     */
     private  String  businessUnitDecompose;
     /**
     * 区域ID
     */
     private  Long  areaId;
     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 产品ID
     */
     private  Long  productId;
     /**
     * 行业ID
     */
     private  Long  industryId;

}

