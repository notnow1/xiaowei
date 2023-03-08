package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionElementDTO;

import java.util.List;
import java.util.Map;

/**
* 市场洞察行业吸引力表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryAttractionDTO {

    //查询检验
    public interface QueryMiIndustryAttractionDTO extends Default{

    }
    //新增检验
    public interface AddMiIndustryAttractionDTO extends Default{

    }

    //删除检验
    public interface DeleteMiIndustryAttractionDTO extends Default{

    }
    //修改检验
    public interface UpdateMiIndustryAttractionDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miIndustryAttractionId;
    /**
    * 市场洞察行业ID
    */
    private  Long marketInsightIndustryId;
    /**
    * 行业吸引力ID
    */
    private  Long industryAttractionId;
    /**
     * 行业吸引力要素名称
     */
    private  String  attractionElementName;
    /**
    * 排序
    */
    private  Integer sort;
    /**
     * 行业吸引力要素表集合
     */
    private List<IndustryAttractionElementDTO> industryAttractionElementDTOS;
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

