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
* 市场洞察对手表
* @author TANGMICHI
* @since 2023-03-12
*/
@Data
@Accessors(chain = true)
public class MarketInsightOpponentDTO {

    //查询检验
    public interface QueryMarketInsightOpponentDTO extends Default{

    }
    //新增检验
    public interface AddMarketInsightOpponentDTO extends Default{

    }

    //删除检验
    public interface DeleteMarketInsightOpponentDTO extends Default{

    }
    //修改检验
    public interface UpdateMarketInsightOpponentDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "id不能为空", groups = {MarketInsightOpponentDTO.UpdateMarketInsightOpponentDTO.class, MarketInsightOpponentDTO.DeleteMarketInsightOpponentDTO.class})
    private  Long marketInsightOpponentId;
    /**
    * 规划年度
    */
    @NotNull(message = "规划年度不能为空", groups = {MarketInsightOpponentDTO.AddMarketInsightOpponentDTO.class, MarketInsightOpponentDTO.UpdateMarketInsightOpponentDTO.class})
    private  Integer planYear;
    /**
    * 规划业务单元ID
    */
    private  Long planBusinessUnitId;
    /**
    * 规划业务单元维度(region,department,product,industry)
    */
    private  String businessUnitDecompose;

    /**
     * 规划业务单元维度列表
     */
    List<Map<String, Object>> businessUnitDecomposes;
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
     * 行业ID
     */
    private  String industryName;
    /**
     * 市场洞察对手选择集合
     */
    private List<MiOpponentChoiceDTO> miOpponentChoiceDTOS;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 创建人
    */
    private  Long createBy;
    /**
     * 员工ID集合
     */
    private List<String> createBys;
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

