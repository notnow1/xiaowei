package ${packageName}.controller;

import java.util.List;
import java.io.IOException;
import javax.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import ${packageName}.domain.${entity};
import ${packageName}.service.${table.serviceName};
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import org.springframework.web.bind.annotation.*;




/**
*
* @author ${author}
* @since ${date}
*/
@Controller
@RequestMapping("${requestMapping}")
public class ${table.controllerName} extends BaseController
{
private String prefix = "${requestMapping}";

@Autowired
private I${entity}Service ${entity?uncap_first}Service;

/**
* 分页查询${table.comment!}列表
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:pageList")
@GetMapping("/pageList")
public TableDataInfo pageList(${entity} ${entity?uncap_first}){
startPage();
List<${entity}> list = ${entity?uncap_first}Service.select${entity}List(${entity?uncap_first});
return getDataTable(list);
}

/**
* 查询${table.comment!}列表
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:list")
@GetMapping("/list")
public AjaxResult list(${entity} ${entity?uncap_first}){
List<${entity}> list = ${entity?uncap_first}Service.select${entity}List(${entity?uncap_first});
return AjaxResult.success(list);
}



/**
* 导出${table.comment!}列表
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:export")
@Log(title = "${table.comment!}", businessType = BusinessType.EXPORT)
@PostMapping("/export")
@ResponseBody
public AjaxResult export(${entity} ${entity?uncap_first})
{
List<${entity}> list = ${entity?uncap_first}Service.select${entity}List(${entity?uncap_first});
ExcelUtil<${entity}> util = new ExcelUtil<${entity}>(${entity}.class);
return util.exportExcel(list, "${table.comment!}数据");
}


/**
* 获取${table.comment!}详细信息
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:query")
@GetMapping(value = "/{<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>}")
public AjaxResult getInfo(@PathVariable("<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>")<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list>  <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>){
return AjaxResult.success(${entity?uncap_first}Service.select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>));
}


/**
* 新增${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:add")
@Log(title = "${table.comment!}", businessType = BusinessType.INSERT)
@PostMapping("/add")
@ResponseBody
public AjaxResult addSave(${entity} ${entity?uncap_first})
{
return toAjax(${entity?uncap_first}Service.insert${entity}(${entity?uncap_first}));
}


/**
* 修改${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:edit")
@Log(title = "${table.comment!}", businessType = BusinessType.UPDATE)
@PostMapping("/edit")
@ResponseBody
public AjaxResult editSave(${entity} ${entity?uncap_first})
{
return toAjax(${entity?uncap_first}Service.update${entity}(${entity?uncap_first}));
}

/**
* 删除${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:remove")
@Log(title = "${table.comment!}", businessType = BusinessType.UPDATE)
@PostMapping("/edit")
@ResponseBody
public AjaxResult remove(${entity} ${entity?uncap_first}s)
{
return toAjax(${entity?uncap_first}Service.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(${entity?uncap_first}s.get<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>()));
}

}
