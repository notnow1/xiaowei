package net.qixiaowei.system.manage.api.dto.field;

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
* 字段配置表
* @author hzk
* @since 2023-02-08
*/
@Data
@Accessors(chain = true)
public class FieldConfigDTO {

    //查询检验
    public interface QueryFieldConfigDTO extends Default{

    }
    //新增检验
    public interface AddFieldConfigDTO extends Default{

    }

    //删除检验
    public interface DeleteFieldConfigDTO extends Default{

    }
    //修改检验
    public interface UpdateFieldConfigDTO extends Default{

    }
    /**
    * ID
    */
    private  Long fieldConfigId;
    /**
    * 业务类型
    */
    private  Integer businessType;
    /**
    * 字段名称
    */
    private  String fieldName;
    /**
    * 字段标签
    */
    private  String fieldLabel;
    /**
    * 字段类型
    */
    private  Integer fieldType;
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

}

