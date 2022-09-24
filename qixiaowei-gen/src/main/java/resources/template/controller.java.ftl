package ${controllerPackage};

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import ${dtoPackage}.${entity}DTO;
import ${servicePackage}.${table.serviceName};
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author ${author}
* @since ${date}
*/
@RestController
@RequestMapping("${requestMapping}")
public class ${table.controllerName} extends BaseController
{


    @Autowired
    private I${entity}Service ${entity?uncap_first}Service;

    /**
    * 分页查询${table.comment!}列表
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(${entity}DTO ${entity?uncap_first}DTO){
    startPage();
    List<${entity}DTO> list = ${entity?uncap_first}Service.select${entity}List(${entity?uncap_first}DTO);
    return getDataTable(list);
    }

    /**
    * 查询${table.comment!}列表
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:list")
    @GetMapping("/list")
    public AjaxResult list(${entity}DTO ${entity?uncap_first}DTO){
    List<${entity}DTO> list = ${entity?uncap_first}Service.select${entity}List(${entity?uncap_first}DTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增${table.comment!}
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:add")
    @Log(title = "新增${table.comment!}", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ${entity}DTO ${entity?uncap_first}DTO) {
    return toAjax(${entity?uncap_first}Service.insert${entity}(${entity?uncap_first}DTO));
    }


    /**
    * 修改${table.comment!}
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:edit")
    @Log(title = "修改${table.comment!}", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ${entity}DTO ${entity?uncap_first}DTO)
    {
    return toAjax(${entity?uncap_first}Service.update${entity}(${entity?uncap_first}DTO));
    }

    /**
    * 删除${table.comment!}
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:remove")
    @Log(title = "删除${table.comment!}", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ${entity}DTO ${entity?uncap_first}DTO)
    {
    return toAjax(${entity?uncap_first}Service.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(${entity?uncap_first}DTO));
    }
    /**
    * 批量修改${table.comment!}
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:edits")
    @Log(title = "批量修改${table.comment!}", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<${entity}DTO> ${entity?uncap_first}Dtos)
    {
    return toAjax(${entity?uncap_first}Service.update${entity}s(${entity?uncap_first}Dtos));
    }

    /**
    * 批量新增${table.comment!}
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:insert${entity}s")
    @Log(title = "批量新增${table.comment!}", businessType = BusinessType.INSERT)
    @PostMapping("/insert${entity}s")
    public AjaxResult insert${entity}s(@RequestBody List<${entity}DTO> ${entity?uncap_first}Dtos)
    {
    return toAjax(${entity?uncap_first}Service.insert${entity}s(${entity?uncap_first}Dtos));
    }

    /**
    * 批量删除${table.comment!}
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:removes")
    @Log(title = "批量删除${table.comment!}", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<${entity}DTO>  ${entity}Dtos)
    {
    return toAjax(${entity?uncap_first}Service.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(${entity}Dtos));
    }
}
