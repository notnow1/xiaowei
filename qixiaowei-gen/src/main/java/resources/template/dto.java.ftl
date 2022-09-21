package ${dtoPackage};

import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.annotation.IdType;
import java.util.Date;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;
import java.time.LocalDateTime;

/**
* ${table.comment!}
* @author ${author}
* @since ${date}
*/
@Data
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class ${entity}Dto {
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    /**
    * ${field.comment}
    */
    private ${field.propertyType} ${field.propertyName};
</#list>

}

