package ${entityPackage};

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import net.qixiaowei.integration.common.web.domain.BaseEntity;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import java.time.LocalDateTime;
import lombok.experimental.Accessors;

/**
* ${table.comment!}
* @author ${author}
* @since ${date}
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ${entity} extends BaseEntity {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>

    <#if field.comment!?length gt 0>
    /**
    * ${field.comment}
    */
    </#if>
    private ${field.propertyType} ${field.propertyName};
</#list>

}

