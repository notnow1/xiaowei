<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE mapper PUBLIC "-//mybatis.org//DTD Mapper 3.0//EN" "http://mybatis.org/dtd/mybatis-3-mapper.dtd">
<mapper namespace="${mapperPackage}.${table.mapperName}">
    <!--    查询${table.comment!}-->
    <select id="select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>"
            resultType="${dtoPackage}.${entity}DTO">
        SELECT
        <#list table.commonFields as field>
            ${field.name},
        </#list>
        ${table.fieldNames}
        FROM ${table.name}
        WHERE <#list table.fields as field><#if field.keyFlag> ${field.name}=#${leftBrace}${entity?uncap_first}${rightBrace}</#if></#list>
    </select>

    <!--    查询${table.comment!}列表-->
    <select id="select${entity}List" resultType="${dtoPackage}.${entity}DTO">
        SELECT
        <#list table.commonFields as field>
            ${field.name},
        </#list>
        ${table.fieldNames}
        FROM ${table.name}
        WHERE 1=1
        <#list table.fields as field>
            <#if "${field.propertyType}"!="LocalDateTime">
                <if test="${entity?uncap_first}.${field.propertyName} != null and ${entity?uncap_first}.${field.propertyName} != ''">
                    ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
                </if>
            <#else>
                <if test="${entity?uncap_first}.${field.propertyName} != null">
                    ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
                </if>
            </#if>
        </#list>
    </select>
    <!--新增${table.comment!}-->
    <insert id="insert${entity}">
        INSERT INTO ${table.name} (<#list table.commonFields as field>${field.name},</#list>${table.fieldNames})
        VALUES
        (<#list table.fields as field><#if !field_has_next>#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}<#else>#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace},</#if></#list>
        )
    </insert>
    <!--修改${table.comment!}-->
    <update id="update${entity}">
        UPDATE ${table.name}
        SET <#list table.commonFields as field>${field.name}="",</#list>${table.fieldNames}
        WHERE 1=1
        <#list table.fields as field >
            <#if field.keyFlag><#--生成普通字段 -->
                and ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
            </#if>
        </#list>
    </update>
    <!--删除${table.comment!}-->
    <delete id="delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>">
        DELETE FROM ${table.name}
        WHERE 1=1
        <#list table.fields as field>
            <#if "${field.propertyType}"!="LocalDateTime">
                <if test="${entity?uncap_first}.${field.propertyName} != null and ${entity?uncap_first}.${field.propertyName} != ''">
                    ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
                </if>
            <#else>
                <if test="${entity?uncap_first}.${field.propertyName} != null">
                    ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
                </if>
            </#if>
        </#list>
    </delete>
    <!--批量删除${table.comment!}-->
    <delete id="delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s">
        DELETE FROM ${table.name}
        WHERE <#list table.fields as field><#if field.keyFlag> ${field.name} IN
            <foreach item="item"
                     collection="<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s"
                     index="index" open="(" separator="," close=")">
                #${leftBrace}
                item.<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>${rightBrace}
            </foreach>
        </#if></#list>
    </delete>
    <!--批量新增${table.comment!}-->
    <insert id="batch${entity}">
        INSERT INTO ${table.name} (<#list table.commonFields as field>${field.name},</#list>${table.fieldNames})
        VALUES (
        <foreach item="item" index="index"
                 collection="<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s"
                 separator=",">
            <#list table.fields as field><#if !field_has_next>#${leftBrace}item.${field.propertyName}${rightBrace}<#else>#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace},</#if></#list>
        </foreach>
        )
    </insert>
</mapper>


