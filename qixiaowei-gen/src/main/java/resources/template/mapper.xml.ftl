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
        and delete_flag=0
    </select>

    <!--    查询${table.comment!}列表-->
    <select id="select${entity}List" resultType="${dtoPackage}.${entity}DTO">
        SELECT
        <#list table.commonFields as field>
            ${field.name},
        </#list>
        ${table.fieldNames}
        FROM ${table.name}
        WHERE delete_flag=0
        <#list table.fields as field>
            <#if "${field.propertyType}"!="LocalDateTime">
                <if test="${entity?uncap_first}.${field.propertyName} != null and ${entity?uncap_first}.${field.propertyName} != ''">
                    and ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
                </if>
            <#else>
                <if test="${entity?uncap_first}.${field.propertyName} != null">
                    and ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
                </if>
            </#if>
        </#list>
    </select>
    <!--新增${table.comment!}-->
    <insert id="insert${entity}" useGeneratedKeys="true" keyProperty="<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>">
        INSERT INTO ${table.name} (<#list table.fields as field><#if !field.keyFlag><#if !field_has_next>${field.name}<#else>${field.name},</#if></#if></#list>)
        VALUES
        (<#list table.fields as field><#if !field.keyFlag><#if !field_has_next>#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}<#else>#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace},</#if></#if></#list>)
    </insert>
    <!--修改${table.comment!}-->
    <update id="update${entity}">
        UPDATE ${table.name}
        SET
        <#list table.fields as field>
            <#if !field_has_next>
                <#if "${field.propertyType}"!="LocalDateTime">
                    <if test="${entity?uncap_first}.${field.propertyName} != null and ${entity?uncap_first}.${field.propertyName} != ''">
                        ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
                    </if>
                <#else>
                    <if test="${entity?uncap_first}.${field.propertyName} != null">
                        ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
                    </if>
                </#if>
            <#else>
                <#if "${field.propertyType}"!="LocalDateTime">
                    <if test="${entity?uncap_first}.${field.propertyName} != null and ${entity?uncap_first}.${field.propertyName} != ''">
                        ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace},
                    </if>
                <#else>
                    <if test="${entity?uncap_first}.${field.propertyName} != null">
                        ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace},
                    </if>
                </#if>
            </#if>
        </#list>
        WHERE
        <#list table.fields as field >
            <#if field.keyFlag><#--生成普通字段 -->
                 ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
            </#if>
        </#list>
    </update>
    <!--逻辑删除${table.comment!}-->
    <update id="logicDelete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>">
        UPDATE ${table.name}
        SET     delete_flag= 1,
        <#list table.fields as field >
            <#if field.name=="update_by"><#--生成普通字段 -->
                ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace},
            </#if>
            <#if field.name=="update_time"><#--生成普通字段 -->
                ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
            </#if>
        </#list>
        WHERE
        <#list table.fields as field >
            <#if field.keyFlag><#--生成普通字段 -->
                ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
            </#if>
        </#list>
    </update>
    <!--逻辑批量删除${table.comment!}-->
    <update id="logicDelete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s">
        UPDATE ${table.name}
        SET     delete_flag= 1,
        <#list table.fields as field >
            <#if field.name=="update_by"><#--生成普通字段 -->
                ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace},
            </#if>
            <#if field.name=="update_time"><#--生成普通字段 -->
                ${field.name}=#${leftBrace}${entity?uncap_first}.${field.propertyName}${rightBrace}
            </#if>
        </#list>
        WHERE
        <#list table.fields as field><#if field.keyFlag> ${field.name} IN
            <foreach item="item"
                     collection="<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s"
                     index="index" open="(" separator="," close=")">
                #${leftBrace}item${rightBrace}
            </foreach>
        </#if></#list>
    </update>
    <!--批量新增${table.comment!}-->
    <insert id="batch${entity}">
        INSERT INTO ${table.name} (<#list table.fields as field><#if !field.keyFlag><#if !field_has_next>${field.name}<#else>${field.name},</#if></#if></#list>)
        VALUES
        <foreach item="item" index="index"
                 collection="${entity?uncap_first}s"
                 separator=",">
            (<#list table.fields as field><#if !field.keyFlag><#if !field_has_next>#${leftBrace}item.${field.propertyName}${rightBrace}<#else>#${leftBrace}item.${field.propertyName}${rightBrace},</#if></#if></#list>)
        </foreach>
    </insert>

    <!--物理删除${table.comment!}-->
    <delete id="delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>">
        DELETE FROM ${table.name}
        WHERE <#list table.fields as field><#if field.keyFlag> ${field.name}=#${leftBrace}${entity?uncap_first}${rightBrace}</#if></#list>

    </delete>
    <!--物理批量删除${table.comment!}-->
    <delete id="delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s">
        DELETE FROM ${table.name}
        WHERE <#list table.fields as field><#if field.keyFlag> ${field.name} IN
            <foreach item="item"
                     collection="<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s"
                     index="index" open="(" separator="," close=")">
                #${leftBrace}item${rightBrace}
            </foreach>
        </#if></#list>
    </delete>
    <!--批量修改${table.comment!}-->
    <update id="update${entity}s">
        update ${table.name}
        <trim prefix="set" suffixOverrides=",">
        <#list table.fields as field >
            <trim prefix="${field.name}=case" suffix="end,">
                <foreach collection="${entity?uncap_first}List" item="item" index="index">
                     <#if !field.keyFlag>
                         <#if "${field.propertyType}"!="LocalDateTime">
                             <if test="item.${field.propertyName} != null and item.${field.propertyName} != ''">
                                 when <#list table.fields as field><#if field.keyFlag>${field.name}</#if></#list>=<#list table.fields as field><#if field.keyFlag>#${leftBrace}item.${field.propertyName}${rightBrace}</#if></#list> then #${leftBrace}item.${field.name}${rightBrace}
                             </if>
                         <#else>
                             <if test="item.${field.propertyName} != null">
                                 when <#list table.fields as field><#if field.keyFlag>${field.name}</#if></#list>=<#list table.fields as field><#if field.keyFlag>#${leftBrace}item.${field.propertyName}${rightBrace}</#if></#list> then #${leftBrace}item.${field.name}${rightBrace}
                             </if>
                         </#if>
                     </#if>
                </foreach>
            </trim>
        </#list>
        </trim>
        where
        <foreach collection="${entity?uncap_first}List" separator="or" item="item" index="index">
            <#list table.fields as field >
                <#if field.keyFlag><#--生成普通字段 -->
                    ${field.name}=#${leftBrace}item.${field.propertyName}${rightBrace}
                </#if>
            </#list>
        </foreach>
    </update>
</mapper>


