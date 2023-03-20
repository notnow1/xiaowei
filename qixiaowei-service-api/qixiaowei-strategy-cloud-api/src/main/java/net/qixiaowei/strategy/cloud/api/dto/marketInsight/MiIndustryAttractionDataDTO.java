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
* 市场洞察行业吸引力数据表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryAttractionDataDTO extends BaseDTO {

    //查询检验
    public interface QueryMiIndustryAttractionDataDTO extends Default{

    }
    //新增检验
    public interface AddMiIndustryAttractionDataDTO extends Default{

    }

    //删除检验
    public interface DeleteMiIndustryAttractionDataDTO extends Default{

    }
    //修改检验
    public interface UpdateMiIndustryAttractionDataDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miIndustryAttractionDataId;
    /**
    * 市场洞察行业ID
    */
    private  Long marketInsightIndustryId;
    /**
    * 市场洞察行业详情ID
    */
    private  Long miIndustryDetailId;
    /**
    * 市场洞察行业吸引力ID
    */
    private  Long miIndustryAttractionId;
    /**
    * 行业吸引力要素ID
    */
    private  Long industryAttractionElementId;
    /**
     * 评估标准名称
     */
    private  String assessStandardName;
    /**
     * 评估标准说明
     */
    private  String  assessStandardDescription;
    /**
     * 显示颜色
     */
    private  String  displayColor;
    /**
     * 字体颜色
     */
    private String fontColor;

    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;

}

