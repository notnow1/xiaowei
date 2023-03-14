package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import java.util.List;
import java.util.Map;

/**
* 市场洞察客户投资计划表
* @author TANGMICHI
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class MiCustomerInvestPlanDTO extends BaseDTO {

    //查询检验
    public interface QueryMiCustomerInvestPlanDTO extends Default{

    }
    //新增检验
    public interface AddMiCustomerInvestPlanDTO extends Default{

    }

    //删除检验
    public interface DeleteMiCustomerInvestPlanDTO extends Default{

    }
    //修改检验
    public interface UpdateMiCustomerInvestPlanDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miCustomerInvestPlanId;
    /**
    * 市场洞察客户ID
    */
    private  Long marketInsightCustomerId;
    /**
    * 行业ID
    */
    private  Long industryId;
    /**
     * 行业名称
     */
    private  String industryName;
    /**
    * 客户名称
    */
    private  String customerName;
    /**
    * 客户类别
    */
    private  Long customerCategory;
    /**
     * 客户类别名称
     */
    private  String customerCategoryName;
    /**
    * 现有市场占有率
    */
    private  BigDecimal existMarketShare;
    /**
    * 上年销售额
    */
    private  BigDecimal previousYearSales;
    /**
    * 规划期
    */
    private  Integer planPeriod;
    /**
    * 未来可参与市场空间
    */
    private  BigDecimal futurePartMarketSpace;
    /**
    * 排序
    */
    private  Integer sort;
    /**
     * 市场洞察客户投资详情集合
     */
    private List<MiCustomerInvestDetailDTO>  miCustomerInvestDetailDTOS;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

