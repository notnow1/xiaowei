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
* 市场洞察行业详情表
* @author TANGMICHI
* @since 2023-03-03
*/
@Data
@Accessors(chain = true)
public class MiIndustryDetailDTO {

    //查询检验
    public interface QueryMiIndustryDetailDTO extends Default{

    }
    //新增检验
    public interface AddMiIndustryDetailDTO extends Default{

    }

    //删除检验
    public interface DeleteMiIndustryDetailDTO extends Default{

    }
    //修改检验
    public interface UpdateMiIndustryDetailDTO extends Default{

    }
    /**
    * ID
    */
    private  Long miIndustryDetailId;
    /**
    * 市场洞察行业ID
    */
    private  Long marketInsightIndustryId;
    /**
    * 行业ID
    */
    private  Long industryId;
    /**
     * 行业名称
     */
    private  String industryName;
    /**
    * 行业类型
    */
    private  Long industryType;
    /**
    * 规划期
    */
    private  Integer planPeriod;
    /**
    * 整体空间
    */
    private  BigDecimal overallSpace;
    /**
    * 可参与空间
    */
    private  BigDecimal participateSpace;
    /**
    * 目标市场空间
    */
    private  BigDecimal targetMarketSpace;
    /**
    * 排序
    */
    private  Integer sort;
    /**
     * 市场洞察行业预估集
     */
    private List<MiIndustryEstimateDTO> miIndustryEstimateDTOS;

    /**
     *市场洞察行业吸引力集合
     */
    private List<MiIndustryAttractionDTO> miIndustryAttractionDTOS;
    /**
     *市场洞察行业吸引力数据集合
     */
    private List<MiIndustryAttractionDataDTO> miIndustryAttractionDataDTOS;
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
