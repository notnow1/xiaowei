package ${entityPackage};



import net.qixiaowei.integration.common.web.domain.BaseEntity;
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
public class ${entity} extends BaseEntity {

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.comment!?length gt 0>
        <#if "${field.comment}"!="创建人"&&"${field.comment}"!="创建时间" &&"${field.comment}"!="更新人" &&"${field.comment}"!="更新时间">
     /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if "${field.propertyName}"!="createBy"&&"${field.propertyName}"!="createTime" &&"${field.propertyName}"!="updateBy" &&"${field.propertyName}"!="updateTime">
     private <#if "${field.propertyType}"=="LocalDateTime"> Date <#else> ${field.propertyType}</#if>  ${field.propertyName};
    </#if>
</#list>

}

