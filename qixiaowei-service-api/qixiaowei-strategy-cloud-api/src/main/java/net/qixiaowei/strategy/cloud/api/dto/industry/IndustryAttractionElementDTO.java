package net.qixiaowei.strategy.cloud.api.dto.industry;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;

import java.util.Map;

/**
* 行业吸引力要素表
* @author TANGMICHI
* @since 2023-02-20
*/
@Data
@Accessors(chain = true)
public class IndustryAttractionElementDTO extends BaseDTO {

    //查询检验
    public interface QueryIndustryAttractionElementDTO extends Default{

    }
    //新增检验
    public interface AddIndustryAttractionElementDTO extends Default{

    }

    //删除检验
    public interface DeleteIndustryAttractionElementDTO extends Default{

    }
    //修改检验
    public interface UpdateIndustryAttractionElementDTO extends Default{

    }
    /**
    * ID
    */
    private  Long industryAttractionElementId;
    /**
    * 行业吸引力ID
    */
    private  Long industryAttractionId;
    /**
    * 评估标准名称
    */
    private  String assessStandardName;
    /**
    * 评估标准说明
    */
    private  String assessStandardDescription;
    /**
    * 显示颜色
    */
    private  String displayColor;
    /**
    * 排序
    */
    private  Integer sort;
    /**
    * 状态:0失效;1生效
    */
    private  Integer status;
    /**
    * 删除标记:0未删除;1已删除
    */
    private  Integer deleteFlag;
    /**
    * 租户ID
    */
    private  Long tenantId;
}

