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
* 市场洞察客户选择表
* @author TANGMICHI
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class MiCustomerChoiceDTO {

    //查询检验
    public interface QueryMiCustomerChoiceDTO extends Default{

    }
    //新增检验
    public interface AddMiCustomerChoiceDTO extends Default{

    }

    //删除检验
    public interface DeleteMiCustomerChoiceDTO extends Default{

    }
    //修改检验
    public interface UpdateMiCustomerChoiceDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miCustomerChoiceId;
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
    * 准入标记:0否;1是
    */
    private  Integer admissionFlag;
    /**
    * 客户类别
    */
    private  Long customerCategory;
    /**
     * 客户类别名称
     */
    private  String customerCategoryName;
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

