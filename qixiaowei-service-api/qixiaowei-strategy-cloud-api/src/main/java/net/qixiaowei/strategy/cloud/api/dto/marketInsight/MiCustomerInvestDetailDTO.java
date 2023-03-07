package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Map;

/**
* 市场洞察客户投资详情表
* @author TANGMICHI
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class MiCustomerInvestDetailDTO {

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
    * 排序
    */
    private  Integer sort;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
    * 创建时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;

    /**
    * 请求参数
    */
    private Map<String, Object> params;
}

