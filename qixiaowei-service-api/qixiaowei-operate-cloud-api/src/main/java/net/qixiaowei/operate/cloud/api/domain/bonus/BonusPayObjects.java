package net.qixiaowei.operate.cloud.api.domain.bonus;



import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;
import java.math.BigDecimal;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* 奖金发放对象表
* @author TANGMICHI
* @since 2022-12-08
*/
@Data
@Accessors(chain = true)
public class BonusPayObjects extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  bonusPayObjectsId;
     /**
     * 奖金发放申请ID
     */
     private  Long  bonusPayApplicationId;
     /**
     * 奖金发放对象:1部门;2员工
     */
     private  Integer  bonusPayObject;
     /**
     * 奖金发放对象ID
     */
     private  Long  bonusPayObjectId;
     /**
     * 奖项金额
     */
     private  BigDecimal  awardAmount;

}

