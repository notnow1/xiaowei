package net.qixiaowei.strategy.cloud.api.domain.gap;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 差距分析表
* @author Graves
* @since 2023-02-24
*/
@Data
@Accessors(chain = true)
public class GapAnalysis extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  gapAnalysisId;
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
     /**
     * 经营历史年份
     */
     private  Integer  operateHistoryYear;

}

