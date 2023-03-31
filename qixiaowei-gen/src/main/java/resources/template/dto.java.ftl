package ${dtoPackage};

import java.util.Date;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import java.math.BigDecimal;
import javax.validation.groups.Default;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Map;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
/**
* ${table.comment!}
* @author ${author}
* @since ${date}
*/
@Data
@Accessors(chain = true)
public class ${entity}DTO extends BaseDTO{

    //查询检验
    public interface Query${entity}DTO extends Default{

    }
    //新增检验
    public interface Add${entity}DTO extends Default{

    }

    //删除检验
    public interface Delete${entity}DTO extends Default{

    }
    //修改检验
    public interface Update${entity}DTO extends Default{

    }
<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if "${field.propertyName}"!="createBy"&&"${field.propertyName}"!="createTime" &&"${field.propertyName}"!="updateBy" &&"${field.propertyName}"!="updateTime"&&"${field.propertyName}"!="remark">
    /**
    * ${field.comment}
    */
    </#if>
    <#if "${field.propertyType}"=="LocalDateTime"&&("${field.propertyName}"!="createTime" && "${field.propertyName}"!="updateTime")>
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss",timezone = "GMT+8")
    </#if>
    <#if "${field.propertyName}"!="createBy"&&"${field.propertyName}"!="createTime" &&"${field.propertyName}"!="updateBy" &&"${field.propertyName}"!="updateTime"&&"${field.propertyName}"!="remark">
    private <#if "${field.propertyType}"=="LocalDateTime" || "${field.propertyType}"=="LocalDate" > Date <#else> ${field.propertyType}</#if>  ${field.propertyName};
    </#if>
</#list>

}

