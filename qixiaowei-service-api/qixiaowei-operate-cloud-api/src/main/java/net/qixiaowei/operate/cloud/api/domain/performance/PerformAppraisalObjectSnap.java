package net.qixiaowei.operate.cloud.api.domain.performance;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 绩效考核对象快照表
* @author Graves
* @since 2022-12-05
*/
@Data
@Accessors(chain = true)
public class PerformAppraisalObjectSnap extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  appraisalObjectSnapId;
     /**
     * 绩效考核对象ID
     */
     private  Long  performAppraisalObjectsId;
     /**
     * 考核对象名称
     */
     private  String  appraisalObjectName;
     /**
     * 考核对象编码
     */
     private  String  appraisalObjectCode;
     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 部门名称
     */
     private  String  departmentName;
     /**
     * 岗位ID
     */
     private  Long  postId;
     /**
     * 岗位名称
     */
     private  String  postName;
     /**
     * 职级体系ID
     */
     private  Long  officialRankSystemId;
     /**
     * 职级
     */
     private  Integer  officialRank;
     /**
     * 职级名称
     */
     private  String  officialRankName;

}

