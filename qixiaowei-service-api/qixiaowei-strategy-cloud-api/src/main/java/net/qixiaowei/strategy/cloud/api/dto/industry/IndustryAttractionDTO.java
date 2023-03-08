package net.qixiaowei.strategy.cloud.api.dto.industry;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.List;
import java.util.Map;

/**
* 行业吸引力表
* @author TANGMICHI
* @since 2023-02-20
*/
@Data
@Accessors(chain = true)
public class IndustryAttractionDTO {

    //查询检验
    public interface QueryIndustryAttractionDTO extends Default{

    }
    //新增检验
    public interface AddIndustryAttractionDTO extends Default{

    }

    //删除检验
    public interface DeleteIndustryAttractionDTO extends Default{

    }
    //修改检验
    public interface UpdateIndustryAttractionDTO extends Default{

    }
    /**
    * ID
    */
    @NotNull(message = "id不能为空", groups = {IndustryAttractionDTO.UpdateIndustryAttractionDTO.class,IndustryAttractionDTO.DeleteIndustryAttractionDTO.class})
    private  Long industryAttractionId;
    /**
    * 行业吸引力要素名称
    */
    @NotBlank(message = "行业吸引力要素名称不能为空", groups = {IndustryAttractionDTO.UpdateIndustryAttractionDTO.class,IndustryAttractionDTO.AddIndustryAttractionDTO.class})
    private  String attractionElementName;
    /**
    * 状态:0失效;1生效
    */
    @NotNull(message = "状态不能为空", groups = {IndustryAttractionDTO.UpdateIndustryAttractionDTO.class,IndustryAttractionDTO.AddIndustryAttractionDTO.class})
    private  Integer status;
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
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private  Date  createTime;
    /**
    * 更新人
    */
    private  Long updateBy;
    /**
    * 更新时间
    */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy/MM/dd HH:mm:ss")
    private  Date  updateTime;
    /**
    * 租户ID
    */
    private  Long tenantId;
    /**
     * 行业吸引力要素表集合
     */
    private List<IndustryAttractionElementDTO> industryAttractionElementDTOS;
    /**
    * 请求参数
    */
    private Map<String, Object> params;
}

