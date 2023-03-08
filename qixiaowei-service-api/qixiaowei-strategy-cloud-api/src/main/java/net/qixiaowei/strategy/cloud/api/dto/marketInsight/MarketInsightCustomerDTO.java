package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.Map;

/**
* 市场洞察客户表
* @author TANGMICHI
* @since 2023-03-07
*/
@Data
@Accessors(chain = true)
public class MarketInsightCustomerDTO {

    //查询检验
    public interface QueryMarketInsightCustomerDTO extends Default{

    }
    //新增检验
    public interface AddMarketInsightCustomerDTO extends Default{

    }

    //删除检验
    public interface DeleteMarketInsightCustomerDTO extends Default{

    }
    //修改检验
    public interface UpdateMarketInsightCustomerDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "id不能为空", groups = {MarketInsightCustomerDTO.UpdateMarketInsightCustomerDTO.class, MarketInsightCustomerDTO.DeleteMarketInsightCustomerDTO.class})
    private  Long marketInsightCustomerId;
    /**
    * 规划年度
    */
    @NotNull(message = "规划年度不能为空", groups = {MarketInsightCustomerDTO.UpdateMarketInsightCustomerDTO.class, MarketInsightCustomerDTO.AddMarketInsightCustomerDTO.class})
    private  Integer planYear;
    /**
    * 规划业务单元ID
    */
    @NotNull(message = "规划业务单元ID不能为空", groups = {MarketInsightCustomerDTO.UpdateMarketInsightCustomerDTO.class, MarketInsightCustomerDTO.AddMarketInsightCustomerDTO.class})
    private  Long planBusinessUnitId;
    /**
     * 规划业务单元名称
     */
    private String planBusinessUnitName;
    /**
    * 规划业务单元维度(region,department,product,industry)
    */
    private  String businessUnitDecompose;
    /**
    * 区域ID
    */
    private  Long areaId;
    /**
     * 区域名称
     */
    private  String areaName;
    /**
    * 部门ID
    */
    private  Long departmentId;

    /**
     * 部门名称
     */
    private  String departmentName;
    /**
    * 产品ID
    */
    private  Long productId;
    /**
     * 产品名称
     */
    private  String productName;
    /**
    * 行业ID
    */
    private  Long industryId;
    /**
     * 行业名称
     */
    private  String industryName;
    /**
     * 规划业务单元维度列表
     */
    List<Map<String, Object>> businessUnitDecomposes;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
     * 创建人
     */
    private  String createByName;
    /**
     * 市场洞察客户选择表集合
     */
    private List<MiCustomerChoiceDTO>  miCustomerChoiceDTOS;
    /**
     * 市场洞察客户投资计划集合
     */
    private List<MiCustomerInvestPlanDTO>  miCustomerInvestPlanDTOS;
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

