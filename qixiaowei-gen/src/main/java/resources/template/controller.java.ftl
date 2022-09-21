package ${controllerPackage};

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
import ${entityPackage}.${entity};
import ${dtoPackage}.${entity}Dto;
import ${servicePackage}.${table.serviceName};
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
public TableDataInfo pageList(${entity}Dto ${entity?uncap_first}Dto){
startPage();
List<${entity}Dto> list = ${entity?uncap_first}Service.select${entity}List(${entity?uncap_first}Dto);
return getDataTable(list);
}

/**
* 查询${table.comment!}列表
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:list")
@GetMapping("/list")
public AjaxResult list(${entity}Dto ${entity?uncap_first}Dto){
List<${entity}Dto> list = ${entity?uncap_first}Service.select${entity}List(${entity?uncap_first}Dto);
return AjaxResult.success(list);
}



/**
* 导出${table.comment!}列表
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:export")
@Log(title = "导出${table.comment!}", businessType = BusinessType.EXPORT)
@PostMapping("/export")
@ResponseBody
public AjaxResult export(${entity}Dto ${entity?uncap_first}Dto)
{
List<${entity}Dto> list = ${entity?uncap_first}Service.select${entity}List(${entity?uncap_first}Dto);
ExcelUtil<${entity}Dto> util = new ExcelUtil<${entity}Dto>(${entity}Dto.class);
return util.exportExcel(list, "${table.comment!}数据");
}




/**
* 新增${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:add")
@Log(title = "新增${table.comment!}", businessType = BusinessType.INSERT)
@PostMapping("/add")
@ResponseBody
public AjaxResult addSave(${entity}Dto ${entity?uncap_first}Dto) {
return toAjax(${entity?uncap_first}Service.insert${entity}(${entity?uncap_first}Dto));
}


/**
* 修改${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:edit")
@Log(title = "修改${table.comment!}", businessType = BusinessType.UPDATE)
@PostMapping("/edit")
@ResponseBody
public AjaxResult editSave(${entity}Dto ${entity?uncap_first}Dto)
{
return toAjax(${entity?uncap_first}Service.update${entity}(${entity?uncap_first}Dto));
}

/**
* 删除${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:remove")
@Log(title = "删除${table.comment!}", businessType = BusinessType.DELETE)
@PostMapping("/remove")
@ResponseBody
public AjaxResult remove(${entity}Dto ${entity?uncap_first}Dto)
{
return toAjax(${entity?uncap_first}Service.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(${entity?uncap_first}Dto));
}
/**
* 批量修改${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:edits")
@Log(title = "批量修改${table.comment!}", businessType = BusinessType.UPDATE)
@PostMapping("/edits")
@ResponseBody
public AjaxResult editSaves(List<${entity}Dto> ${entity?uncap_first}Dtos)
{
return toAjax(${entity?uncap_first}Service.update${entity}s(${entity?uncap_first}Dtos));
}

/**
* 批量新增${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:insert${entity}s")
@Log(title = "批量新增${table.comment!}", businessType = BusinessType.INSERT)
@PostMapping("/insert${entity}s")
@ResponseBody
public AjaxResult insert${entity}s(List<${entity}Dto> ${entity?uncap_first}Dtos)
{
return toAjax(${entity?uncap_first}Service.insert${entity}s(${entity?uncap_first}Dtos));
}

/**
* 批量删除${table.comment!}
*/
@RequiresPermissions("${packageClass}:${entity?uncap_first}:removes")
@Log(title = "批量删除${table.comment!}", businessType = BusinessType.DELETE)
@PostMapping("/removes")
@ResponseBody
public AjaxResult removes(List<${entity}Dto>  ${entity}Dtos)
{
return toAjax(${entity?uncap_first}Service.delete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(${entity}Dtos));
}
}
