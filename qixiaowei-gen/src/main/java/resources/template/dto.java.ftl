package ${dtoPackage};

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;

/**
* ${table.comment!}
* @author ${author}
* @since ${date}
*/
@Data
@Accessors(chain = true)
public class ${entity}DTO {
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    /**
    * ${field.comment}
    */
    private <#if "${field.propertyType}"=="LocalDateTime"> Date <#else> ${field.propertyType}</#if> ${field.propertyName};
</#list>

}

