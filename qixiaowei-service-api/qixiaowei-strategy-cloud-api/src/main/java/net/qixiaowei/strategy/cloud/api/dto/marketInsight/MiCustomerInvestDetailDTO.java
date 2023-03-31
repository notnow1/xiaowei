package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import java.util.Map;

/**
* 市场洞察客户投资详情表
* @author TANGMICHI
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class MiCustomerInvestDetailDTO extends BaseDTO {

    //查询检验
    public interface QueryMiCustomerInvestDetailDTO extends Default{

    }
    //新增检验
    public interface AddMiCustomerInvestDetailDTO extends Default{

    }

    //删除检验
    public interface DeleteMiCustomerInvestDetailDTO extends Default{

    }
    //修改检验
    public interface UpdateMiCustomerInvestDetailDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miCustomerInvestDetailId;
    /**
    * 市场洞察客户ID
    */
    private  Long marketInsightCustomerId;
    /**
    * 市场洞察客户投资计划ID
    */
    private  Long miCustomerInvestPlanId;
    /**
    * 规划年度
    */
    private  Integer planYear;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
     * 产品名称
     */
    private  String productName;
    /**
     * 产品单位名称
     */
    private  String  productUnitName;

    /**
    * 年需求总量
    */
    private  String totalAnnualDemand;
    /**
    * 客户投资计划金额
    */
    private  BigDecimal customerInvestPlanAmount;
    /**
    * 预计市场占有率
    */
    private  BigDecimal estimateMarketShare;

    /**
     * 可参与市场空间
     */
    private BigDecimal partMarketSpace;

    /**
     * 合计
     */
    private BigDecimal amountTo;
    /**
    * 排序
    */
    private  Integer sort;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;
}

