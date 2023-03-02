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
* 市场洞察宏观表
* @author TANGMICHI
* @since 2023-02-28
*/
@Data
@Accessors(chain = true)
public class MarketInsightMacroDTO {

    //查询检验
    public interface QueryMarketInsightMacroDTO extends Default{

    }
    //新增检验
    public interface AddMarketInsightMacroDTO extends Default{

    }

    //删除检验
    public interface DeleteMarketInsightMacroDTO extends Default{

    }
    //修改检验
    public interface UpdateMarketInsightMacroDTO extends Default{

    }
    /**
    * ID
    */
    private  Long marketInsightMacroId;
    /**
     * ID集合
     */
    private  List<Long> marketInsightMacroIds;
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
     * 区域ID
     */
    private  String areaName;
    /**
    * 部门ID
    */
    private  Long departmentId;
    /**
     * 部门ID
     */
    private  String departmentName;
    /**
    * 产品ID
    */
    private  Long productId;

    /**
     * 产品ID
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
     * 市场洞察宏观详情表集合
     */
    private List<MiMacroDetailDTO>  miMacroDetailDTOS;
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

