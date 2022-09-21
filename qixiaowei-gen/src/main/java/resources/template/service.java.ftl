package ${servicePackage};

import java.util.List;
import ${dtoPackage}.${entity}Dto;


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
public ${entity}Dto select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>);

/**
* 查询${table.comment!}列表
*
* @param ${entity?uncap_first}Dto ${table.comment!}
* @return ${table.comment!}集合
*/
public List<${entity}Dto> select${entity}List(${entity}Dto ${entity?uncap_first}Dto);

/**
* 新增${table.comment!}
*
* @param ${entity?uncap_first}Dto ${table.comment!}
* @return 结果
*/
public int insert${entity}(${entity}Dto ${entity?uncap_first}Dto);

/**
* 修改${table.comment!}
*
* @param ${entity?uncap_first}Dto ${table.comment!}
* @return 结果
*/
public int update${entity}(${entity}Dto ${entity?uncap_first}Dto);

/**
* 批量修改${table.comment!}
*
* @param ${entity?uncap_first}Dtos ${table.comment!}
* @return 结果
*/
public int update${entity}s(List<${entity}Dto> ${entity?uncap_first}Dtos);

/**
* 批量新增${table.comment!}
*
* @param ${entity?uncap_first}Dtos ${table.comment!}
* @return 结果
*/
public int insert${entity}s(List<${entity}Dto> ${entity?uncap_first}Dtos);

/**
* 批量删除${table.comment!}
*
* @param ${entity}Dtos 需要删除的${table.comment!}集合
* @return 结果
*/
public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(List<${entity}Dto> ${entity}Dtos);

/**
* 删除${table.comment!}信息
*
* @param ${entity?uncap_first}Dto
* @return 结果
*/
public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(${entity}Dto ${entity?uncap_first}Dto);

/**
* 删除${table.comment!}信息
*
* @param <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list> ${table.comment!}主键
* @return 结果
*/
public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>);
}
