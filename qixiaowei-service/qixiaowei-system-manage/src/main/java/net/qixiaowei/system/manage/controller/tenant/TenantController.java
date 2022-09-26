package net.qixiaowei.system.manage.controller.tenant;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


/**
*
* @author TANGMICHI
* @since 2022-09-24
*/
@RestController
@RequestMapping("tenant")
public class TenantController extends BaseController
{


    @Autowired
    private ITenantService tenantService;

    /**
    * 分页查询租户表列表
    */
    //@RequiresPermissions("system:manage:tenant:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TenantDTO tenantDTO){
    startPage();
    List<TenantDTO> list = tenantService.selectTenantList(tenantDTO);
    return getDataTable(list);
    }

    /**
    * 查询租户表列表
    */
    //@RequiresPermissions("system:manage:tenant:list")
    @GetMapping("/list")
    public AjaxResult list(TenantDTO tenantDTO){
    List<TenantDTO> list = tenantService.selectTenantList(tenantDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增租户表
    */
    //@RequiresPermissions("system:manage:tenant:add")
    // @Log(title = "新增租户表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@Validated(TenantDTO.AddTenantDTO.class) @RequestBody  TenantDTO tenantDTO) {
        AjaxResult ajaxResult = toAjax(tenantService.insertTenant(tenantDTO));
        return ajaxResult;
    }



    /**
    * 修改租户表
    */
    //@RequiresPermissions("system:manage:tenant:edit")
//    @Log(title = "修改租户表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(TenantDTO.UpdateTenantDTO.class) @RequestBody TenantDTO tenantDTO)
    {
    return toAjax(tenantService.updateTenant(tenantDTO));
    }

    /**
    * 逻辑删除租户表
    */
    //@RequiresPermissions("system:manage:tenant:remove")
    @Log(title = "删除租户表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TenantDTO tenantDTO)
    {
    return toAjax(tenantService.logicDeleteTenantByTenantId(tenantDTO));
    }
    /**
    * 批量修改租户表
    */
    //@RequiresPermissions("system:manage:tenant:edits")
    @Log(title = "批量修改租户表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TenantDTO> tenantDtos)
    {
    return toAjax(tenantService.updateTenants(tenantDtos));
    }

    /**
    * 批量新增租户表
    */
    //@RequiresPermissions("system:manage:tenant:insertTenants")
    @Log(title = "批量新增租户表", businessType = BusinessType.INSERT)
    @PostMapping("/insertTenants")
    public AjaxResult insertTenants(@RequestBody List<TenantDTO> tenantDtos)
    {
    return toAjax(tenantService.insertTenants(tenantDtos));
    }

    /**
    * 逻辑批量删除租户表
    */
    //@RequiresPermissions("system:manage:tenant:removes")
    @Log(title = "批量删除租户表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<TenantDTO>  TenantDtos)
    {
    return toAjax(tenantService.logicDeleteTenantByTenantIds(TenantDtos));
    }
}
