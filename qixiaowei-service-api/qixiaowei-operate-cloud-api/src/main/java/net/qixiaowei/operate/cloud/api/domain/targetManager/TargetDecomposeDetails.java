package net.qixiaowei.operate.cloud.api.domain.targetManager;



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;

/**
* 目标分解详情表
* @author TANGMICHI
* @since 2022-10-28
*/
@Data
@Accessors(chain = true)
public class TargetDecomposeDetails extends BaseEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  targetDecomposeDetailsId;
     /**
     * 目标分解ID
     */
     private  Long  targetDecomposeId;
     /**
     * 员工ID
     */
     private  Long  employeeId;
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
     * 省份ID
     */
     private  Long  regionId;
     /**
     * 行业ID
     */
     private  Long  industryId;
     /**
     * 负责人ID
     */
     private  Long  principalEmployeeId;
     /**
     * 汇总目标值
     */
     private BigDecimal amountTarget;
     /**
     * 删除标记:0未删除;1已删除
     */
     private  Integer  deleteFlag;

}

