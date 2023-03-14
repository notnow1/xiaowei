package net.qixiaowei.strategy.cloud.api.dto.marketInsight;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
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
public class MiIndustryAttractionDTO extends BaseDTO {

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
    * 租户ID
    */
    private  Long tenantId;

}

