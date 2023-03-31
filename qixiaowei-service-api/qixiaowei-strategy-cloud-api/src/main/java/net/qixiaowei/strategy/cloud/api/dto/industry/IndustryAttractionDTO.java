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
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
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
public class IndustryAttractionDTO extends BaseDTO {

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
    * 租户ID
    */
    private  Long tenantId;
    /**
     * 行业吸引力要素表集合
     */
    private List<IndustryAttractionElementDTO> industryAttractionElementDTOS;
}

