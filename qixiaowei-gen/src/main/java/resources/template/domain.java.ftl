package ${entityPackage};



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import java.math.BigDecimal;
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
        <#if "${field.propertyName}"!="createBy"&&"${field.propertyName}"!="createTime" &&"${field.propertyName}"!="updateBy" &&"${field.propertyName}"!="updateTime" &&"${field.propertyName}"!="remark" &&"${field.propertyName}"!="deleteFlag" && field_has_next>
     /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if "${field.propertyName}"!="createBy"&&"${field.propertyName}"!="createTime" &&"${field.propertyName}"!="updateBy" &&"${field.propertyName}"!="updateTime"&&"${field.propertyName}"!="remark" &&"${field.propertyName}"!="deleteFlag" && (field_has_next && "${field.propertyName}"!="tenantId")>
     private <#if "${field.propertyType}"=="LocalDateTime" || "${field.propertyType}"=="LocalDate"> Date <#else> ${field.propertyType}</#if>  ${field.propertyName};
    </#if>
</#list>

}

