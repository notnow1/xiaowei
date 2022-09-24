package ${servicePackageImpl};

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import ${entityPackage}.${entity};
import ${dtoPackage}.${entity}DTO;
import ${mapperPackage}.${entity}Mapper;
import ${servicePackage}.I${entity}Service;


/**
* ${entity}Service业务层处理
* @author ${author}
* @since ${date}
*/
@Service
public class ${entity}ServiceImpl implements I${entity}Service{
    @Autowired
    private ${entity}Mapper ${entity?uncap_first}Mapper;

    /**
    * 查询${table.comment!}
    *
    * @param <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list> ${table.comment!}主键
    * @return ${table.comment!}
    */
    @Override
    public ${entity}DTO select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>)
    {
    return ${entity?uncap_first}Mapper.select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>);
    }

    /**
    * 查询${table.comment!}列表
    *
    * @param ${entity?uncap_first}DTO ${table.comment!}
    * @return ${table.comment!}
    */
    @Override
    public List<${entity}DTO> select${entity}List(${entity}DTO ${entity?uncap_first}DTO)
    {
    ${entity} ${entity?uncap_first}=new ${entity}();
    BeanUtils.copyProperties(${entity?uncap_first}DTO,${entity?uncap_first});
    return ${entity?uncap_first}Mapper.select${entity}List(${entity?uncap_first});
    }

    /**
    * 新增${table.comment!}
    *
    * @param ${entity?uncap_first}DTO ${table.comment!}
    * @return 结果
    */
    @Transactional
    @Override
    public int insert${entity}(${entity}DTO ${entity?uncap_first}DTO){
    ${entity} ${entity?uncap_first}=new ${entity}();
    BeanUtils.copyProperties(${entity?uncap_first}DTO,${entity?uncap_first});
    ${entity?uncap_first}.setCreateTime(DateUtils.getNowDate());
    ${entity?uncap_first}.setUpdateTime(DateUtils.getNowDate());
    return ${entity?uncap_first}Mapper.insert${entity}(${entity?uncap_first});
    }

    /**
    * 修改${table.comment!}
    *
    * @param ${entity?uncap_first}DTO ${table.comment!}
    * @return 结果
    */
    @Transactional
    @Override
    public int update${entity}(${entity}DTO ${entity?uncap_first}DTO)
    {
    ${entity} ${entity?uncap_first}=new ${entity}();
    BeanUtils.copyProperties(${entity?uncap_first}DTO,${entity?uncap_first});
    ${entity?uncap_first}.setCreateTime(DateUtils.getNowDate());
    ${entity?uncap_first}.setUpdateTime(DateUtils.getNowDate());
    return ${entity?uncap_first}Mapper.update${entity}(${entity?uncap_first});
    }

    /**
    * 批量删除${table.comment!}
    *
    * @param ${entity?uncap_first}Dtos 需要删除的${table.comment!}主键
    * @return 结果
    */

    @Transactional
    @Override
    public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(List<${entity}DTO> ${entity?uncap_first}Dtos){
            List<<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list>> stringList = new ArrayList();
            for (${entity}DTO ${entity?uncap_first}DTO : ${entity?uncap_first}Dtos) {
                stringList.add(${entity?uncap_first}DTO.get<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>());
            }
    return ${entity?uncap_first}Mapper.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(stringList);
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
     * 删除${table.comment!}信息
     *
     * @param  ${entity?uncap_first}DTO ${table.comment!}
     * @return 结果
     */
     @Transactional
     @Override
     public int delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(${entity}DTO ${entity?uncap_first}DTO)
     {
     ${entity} ${entity?uncap_first}=new ${entity}();
     BeanUtils.copyProperties(${entity?uncap_first}DTO,${entity?uncap_first});
     return ${entity?uncap_first}Mapper.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(${entity?uncap_first}.get<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>());
     }

    /**
    * 批量新增${table.comment!}信息
    *
    * @param ${entity?uncap_first}Dtos ${table.comment!}对象
    */
    @Transactional
    public int insert${entity}s(List<${entity}DTO> ${entity?uncap_first}Dtos){
      List<${entity}> ${entity?uncap_first}List = new ArrayList();

    for (${entity}DTO ${entity?uncap_first}DTO : ${entity?uncap_first}Dtos) {
      ${entity} ${entity?uncap_first} =new ${entity}();
      BeanUtils.copyProperties(${entity?uncap_first}DTO,${entity?uncap_first});
    ${entity?uncap_first}.setCreateTime(DateUtils.getNowDate());
    ${entity?uncap_first}.setUpdateTime(DateUtils.getNowDate());
      ${entity?uncap_first}List.add(${entity?uncap_first});
    }
    return ${entity?uncap_first}Mapper.batch${entity}(${entity?uncap_first}List);
    }

    /**
    * 批量修改${table.comment!}信息
    *
    * @param ${entity?uncap_first}Dtos ${table.comment!}对象
    */
    @Transactional
    public int update${entity}s(List<${entity}DTO> ${entity?uncap_first}Dtos){
    int num = 0;
    List<${entity}> ${entity?uncap_first}List = new ArrayList();
    for (${entity}DTO ${entity?uncap_first}DTO : ${entity?uncap_first}Dtos) {
       ${entity} ${entity?uncap_first} =new ${entity}();
       BeanUtils.copyProperties(${entity?uncap_first}DTO,${entity?uncap_first});
    ${entity?uncap_first}.setCreateTime(DateUtils.getNowDate());
    ${entity?uncap_first}.setUpdateTime(DateUtils.getNowDate());
       ${entity?uncap_first}List.add(${entity?uncap_first});
       num=num+${entity?uncap_first}Mapper.update${entity}(${entity?uncap_first});
    }
    return num;
    }
}

