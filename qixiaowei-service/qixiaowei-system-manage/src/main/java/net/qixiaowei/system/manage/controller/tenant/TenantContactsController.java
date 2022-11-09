package net.qixiaowei.system.manage.controller.tenant;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.tenant.TenantContactsDTO;
import net.qixiaowei.system.manage.service.tenant.ITenantContactsService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-10-09
*/
@RestController
@RequestMapping("tenantContacts")
public class TenantContactsController extends BaseController
{


    @Autowired
    private ITenantContactsService tenantContactsService;


    /**
    * 查询租户联系人表详情
    */
    @RequiresPermissions("system:manage:tenantContacts:info")
    @GetMapping("/info/{tenantContactsId}")
    public AjaxResult info(@PathVariable Long tenantContactsId){
    TenantContactsDTO tenantContactsDTO = tenantContactsService.selectTenantContactsByTenantContactsId(tenantContactsId);
        return AjaxResult.success(tenantContactsDTO);
    }

    /**
    * 分页查询租户联系人表列表
    */
    @RequiresPermissions("system:manage:tenantContacts:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TenantContactsDTO tenantContactsDTO){
    startPage();
    List<TenantContactsDTO> list = tenantContactsService.selectTenantContactsList(tenantContactsDTO);
    return getDataTable(list);
    }

    /**
    * 查询租户联系人表列表
    */
    @RequiresPermissions("system:manage:tenantContacts:list")
    @GetMapping("/list")
    public AjaxResult list(TenantContactsDTO tenantContactsDTO){
    List<TenantContactsDTO> list = tenantContactsService.selectTenantContactsList(tenantContactsDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增租户联系人表
    */
    @RequiresPermissions("system:manage:tenantContacts:add")
    @Log(title = "新增租户联系人表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody TenantContactsDTO tenantContactsDTO) {
    return toAjax(tenantContactsService.insertTenantContacts(tenantContactsDTO));
    }


    /**
    * 修改租户联系人表
    */
    @RequiresPermissions("system:manage:tenantContacts:edit")
    @Log(title = "修改租户联系人表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TenantContactsDTO tenantContactsDTO)
    {
    return toAjax(tenantContactsService.updateTenantContacts(tenantContactsDTO));
    }

    /**
    * 逻辑删除租户联系人表
    */
    @RequiresPermissions("system:manage:tenantContacts:remove")
    @Log(title = "删除租户联系人表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TenantContactsDTO tenantContactsDTO)
    {
    return toAjax(tenantContactsService.logicDeleteTenantContactsByTenantContactsId(tenantContactsDTO));
    }
    /**
    * 批量修改租户联系人表
    */
    @RequiresPermissions("system:manage:tenantContacts:edits")
    @Log(title = "批量修改租户联系人表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TenantContactsDTO> tenantContactsDtos)
    {
    return toAjax(tenantContactsService.updateTenantContactss(tenantContactsDtos));
    }

    /**
    * 批量新增租户联系人表
    */
    @RequiresPermissions("system:manage:tenantContacts:insertTenantContactss")
    @Log(title = "批量新增租户联系人表", businessType = BusinessType.INSERT)
    @PostMapping("/insertTenantContactss")
    public AjaxResult insertTenantContactss(@RequestBody List<TenantContactsDTO> tenantContactsDtos)
    {
    return toAjax(tenantContactsService.insertTenantContactss(tenantContactsDtos));
    }

    /**
    * 逻辑批量删除租户联系人表
    */
    @RequiresPermissions("system:manage:tenantContacts:removes")
    @Log(title = "批量删除租户联系人表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<TenantContactsDTO>  TenantContactsDtos)
    {
    return toAjax(tenantContactsService.logicDeleteTenantContactsByTenantContactsIds(TenantContactsDtos));
    }
}
