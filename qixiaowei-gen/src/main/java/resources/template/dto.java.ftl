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

    //查询检验
    public interface Query${entity}DTO extends Default{

    }
    //新增检验
    public interface Add${entity}DTO extends Default{

    }

    //新增检验
    public interface Delete${entity}DTO extends Default{

    }
    //修改检验
    public interface Update${entity}DTO extends Default{

    }
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    /**
    * ${field.comment}
    */
    private <#if "${field.propertyType}"=="LocalDateTime"> Date <#else> ${field.propertyType}</#if> ${field.propertyName};
</#list>

}

