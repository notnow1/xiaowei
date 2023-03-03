package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.util.List;
import java.util.Map;

/**
* 市场洞察行业表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MarketInsightIndustryDTO {

    //查询检验
    public interface QueryMarketInsightIndustryDTO extends Default{

    }
    //新增检验
    public interface AddMarketInsightIndustryDTO extends Default{

    }

    //删除检验
    public interface DeleteMarketInsightIndustryDTO extends Default{

    }
    //修改检验
    public interface UpdateMarketInsightIndustryDTO extends Default{

    }
    /**
    * ID
    */
    private  Long marketInsightIndustryId;
    /**
    * 规划年度
    */
    private  Integer planYear;
    /**
    * 规划业务单元ID
    */
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
     * 市场洞察行业详情集合
     */
    private List<MiIndustryDetailDTO> miIndustryDetailDTOS;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
     * 创建人名称
     */
    private  String createByName;
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

