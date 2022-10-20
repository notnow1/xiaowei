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
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.log.enums.BusinessType;
import ${dtoPackage}.${entity}DTO;
import ${excelPackage}.${entity}Excel;
import ${excelImportListenerPackage}.${entity}ImportListener;
import ${servicePackage}.${table.serviceName};
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;



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
    * 查询${table.comment!}详情
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:info")
    @GetMapping("/info/{<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>}")
    public AjaxResult info(@PathVariable <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list> <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>){
    ${entity}DTO ${entity?uncap_first}DTO = ${entity?uncap_first}Service.select${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>);
        return AjaxResult.success(${entity?uncap_first}DTO);
    }

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
    return AjaxResult.success(${entity?uncap_first}Service.insert${entity}(${entity?uncap_first}DTO));
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
    * 逻辑删除${table.comment!}
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:remove")
    @Log(title = "删除${table.comment!}", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ${entity}DTO ${entity?uncap_first}DTO)
    {
    return toAjax(${entity?uncap_first}Service.logicDelete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>(${entity?uncap_first}DTO));
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
    * 逻辑批量删除${table.comment!}
    */
    @RequiresPermissions("${packageClass}:${entity?uncap_first}:removes")
    @Log(title = "批量删除${table.comment!}", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyType}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyType}<#elseif idType??>${field.propertyType}<#elseif field.convert>${field.propertyType}</#if></#if></#list>>  <#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s)
    {
    return toAjax(${entity?uncap_first}Service.logicDelete${entity}By<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName?cap_first}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName?cap_first}<#elseif idType??>${field.propertyName?cap_first}<#elseif field.convert>${field.propertyName?cap_first}</#if></#if></#list>s(<#list table.fields as field><#if field.keyFlag><#assign keyPropertyName="${field.propertyName}"/></#if><#if field.keyFlag><#-- 主键 --><#if field.keyIdentityFlag>${field.propertyName}<#elseif idType??>${field.propertyName}<#elseif field.convert>${field.propertyName}</#if></#if></#list>s));
    }

    /**
    * 导入${table.comment!}
    */
    @PostMapping("import")
    public AjaxResult importEmployee(MultipartFile file) {
    String filename = file.getOriginalFilename();
    if (StringUtils.isBlank(filename)) {
    throw new RuntimeException("请上传文件!");
    }
    if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
    throw new RuntimeException("请上传正确的excel文件!");
    }
    InputStream inputStream;
    try {
    ${entity}ImportListener importListener = new ${entity}ImportListener(${entity?uncap_first}Service);
    inputStream = new BufferedInputStream(file.getInputStream());
    ExcelReaderBuilder builder = EasyExcel.read(inputStream, ${entity}Excel.class, importListener);
    builder.doReadAll();
    } catch (IOException e) {
    throw new ServiceException("导入${table.comment!}Excel失败");
    }
    return AjaxResult.success("操作成功");
    }

    /**
    * 导出${table.comment!}
    */
    @SneakyThrows
    @GetMapping("export")
    public void exportUser(@RequestParam Map<String, Object> ${entity?uncap_first},${entity}DTO ${entity?uncap_first}DTO, HttpServletResponse response) {
    List<${entity}Excel> ${entity?uncap_first}ExcelList = ${entity?uncap_first}Service.export${entity}(${entity?uncap_first}DTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("${table.comment!}" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
        , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), ${entity}Excel.class).sheet("${table.comment!}").doWrite(${entity?uncap_first}ExcelList);
        }
}
