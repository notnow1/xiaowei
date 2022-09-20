package ${servicePackage};

import java.util.List;
import ${entityPackage}.${entity};


/**
* ${entity}Service接口
* @author ${author}
* @since ${date}
*/
public interface I${entity}Service
{
/**
* 查询${table.comment!}
*
* @param <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list> ${table.comment!}主键
* @return ${table.comment!}
*/
public ${entity} select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>);

/**
* 查询${table.comment!}列表
*
* @param ${entity?uncap_first} ${table.comment!}
* @return ${table.comment!}集合
*/
public List<${entity}> select${entity}List(${entity} ${entity?uncap_first});

/**
* 新增${table.comment!}
*
* @param ${entity?uncap_first} ${table.comment!}
* @return 结果
*/
public int insert${entity}(${entity} ${entity?uncap_first});

/**
* 修改${table.comment!}
*
* @param ${entity?uncap_first} ${table.comment!}
* @return 结果
*/
public int update${entity}(${entity} ${entity?uncap_first});

/**
* 批量修改${table.comment!}
*
* @param ${entity?uncap_first}s ${table.comment!}
* @return 结果
*/
public int update${entity}s(List<${entity}> ${entity?uncap_first}s);

/**
* 批量新增${table.comment!}
*
* @param ${entity?uncap_first}s ${table.comment!}
* @return 结果
*/
public int insert${entity}s(List<${entity}> ${entity?uncap_first}s);

/**
* 批量删除${table.comment!}
*
* @param <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s 需要删除的${table.comment!}主键集合
* @return 结果
*/
public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(List<<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list>> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s);

/**
* 删除${table.comment!}信息
*
* @param <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list> ${table.comment!}主键
* @return 结果
*/
public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>);
}
