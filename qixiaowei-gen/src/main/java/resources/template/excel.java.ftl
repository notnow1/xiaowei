package ${excelPackage};



import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;
import java.util.Date;
import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;

/**
* ${table.comment!}
* @author ${author}
* @since ${date}
*/
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ${entity}Excel{

    private static final long serialVersionUID = 1L;

<#-- ----------  BEGIN 字段循环遍历  ---------->
<#list table.fields as field>
    <#if field.comment!?length gt 0>
        <#if "${field.propertyName}"!="createBy"&&"${field.propertyName}"!="createTime" &&"${field.propertyName}"!="updateBy" &&"${field.propertyName}"!="updateTime" &&"${field.propertyName}"!="remark">
     /**
     * ${field.comment}
     */
        </#if>
    </#if>
    <#if "${field.propertyName}"!="createBy"&&"${field.propertyName}"!="createTime" &&"${field.propertyName}"!="updateBy" &&"${field.propertyName}"!="updateTime"&&"${field.propertyName}"!="remark">
     @ExcelIgnore
     @ExcelProperty("${field.comment}")
     <#if "${field.propertyType}"=="LocalDateTime" || "${field.propertyType}"=="LocalDate">
     @DateTimeFormat(value = "yyyy/MM/dd")
     </#if>
     private <#if "${field.propertyType}"=="LocalDateTime" || "${field.propertyType}"=="LocalDate"> Date <#else> ${field.propertyType}</#if>  ${field.propertyName};
    </#if>
</#list>

}

