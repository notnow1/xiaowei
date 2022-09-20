package ${servicePackageImpl};

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.transaction.annotation.Transactional;
import ${entityPackage}.${entity};
import ${mapperPackage}.${entity}Mapper;
import ${servicePackage}.I${entity}Service;

/**
* ${entity}Service业务层处理
* @author ${author}
* @since ${date}
*/
@Service
public class ${entity}ServiceImpl implements I${entity}Service
{
@Autowired
private ${entity}Mapper ${entity?uncap_first}Mapper;

/**
* 查询${table.comment!}
*
* @param <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list> ${table.comment!}主键
* @return ${table.comment!}
*/
@Override
public ${entity} select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>)
{
return ${entity?uncap_first}Mapper.select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>);
}

/**
* 查询${table.comment!}列表
*
* @param ${entity?uncap_first} ${table.comment!}
* @return ${table.comment!}
*/
@Override
public List<${entity}> select${entity}List(${entity} ${entity?uncap_first})
{
return ${entity?uncap_first}Mapper.select${entity}List(${entity?uncap_first});
}

/**
* 新增${table.comment!}
*
* @param ${entity?uncap_first} ${table.comment!}
* @return 结果
*/
@Transactional
@Override
public int insert${entity}(${entity} ${entity?uncap_first}){
${entity?uncap_first}.setCreateBy(${entity?uncap_first}.getCreateBy());
${entity?uncap_first}.setUpdateBy(${entity?uncap_first}.getUpdateBy());
${entity?uncap_first}.setCreateTime(DateUtils.getNowDate());
${entity?uncap_first}.setUpdateTime(DateUtils.getNowDate());
return ${entity?uncap_first}Mapper.insert${entity}(${entity?uncap_first});
}

/**
* 修改${table.comment!}
*
* @param ${entity?uncap_first} ${table.comment!}
* @return 结果
*/
@Transactional
@Override
public int update${entity}(${entity} ${entity?uncap_first})
{
${entity?uncap_first}.setCreateBy(${entity?uncap_first}.getCreateBy());
${entity?uncap_first}.setUpdateBy(${entity?uncap_first}.getUpdateBy());
${entity?uncap_first}.setCreateTime(DateUtils.getNowDate());
${entity?uncap_first}.setUpdateTime(DateUtils.getNowDate());
return ${entity?uncap_first}Mapper.update${entity}(${entity?uncap_first});
}

/**
* 批量删除${table.comment!}
*
* @param <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s 需要删除的${table.comment!}主键
* @return 结果
*/

@Transactional
@Override
public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(List<<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list>> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s){
return ${entity?uncap_first}Mapper.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s);
}

/**
* 删除${table.comment!}信息
*
* @param <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list> ${table.comment!}主键
* @return 结果
*/

@Transactional
@Override
public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>)
{
return ${entity?uncap_first}Mapper.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>);
}


/**
* 批量新增${table.comment!}信息
*
* @param ${entity?uncap_first}s ${table.comment!}对象
*/
@Transactional
public int insert${entity}s(List<${entity}> ${entity?uncap_first}s){
for (${entity} ${entity?uncap_first} : ${entity?uncap_first}s) {
${entity?uncap_first}.setCreateBy(${entity?uncap_first}.getCreateBy());
${entity?uncap_first}.setUpdateBy(${entity?uncap_first}.getUpdateBy());
${entity?uncap_first}.setCreateTime(DateUtils.getNowDate());
${entity?uncap_first}.setUpdateTime(DateUtils.getNowDate());
}
return ${entity?uncap_first}Mapper.batch${entity}(${entity?uncap_first}s);
}

/**
* 批量修改${table.comment!}信息
*
* @param ${entity?uncap_first}s ${table.comment!}对象
*/
@Transactional
public int update${entity}s(List<${entity}> ${entity?uncap_first}s){
int num = 0;
for (${entity} ${entity?uncap_first} : ${entity?uncap_first}s) {
${entity?uncap_first}.setCreateBy(${entity?uncap_first}.getCreateBy());
${entity?uncap_first}.setUpdateBy(${entity?uncap_first}.getUpdateBy());
${entity?uncap_first}.setCreateTime(DateUtils.getNowDate());
${entity?uncap_first}.setUpdateTime(DateUtils.getNowDate());
 num=num+${entity?uncap_first}Mapper.update${entity}(${entity?uncap_first});
}
return num;
}
}

