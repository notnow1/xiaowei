package net.qixiaowei.system.manage.controller.tenant;

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
import net.qixiaowei.system.manage.api.dto.tenant.TenantDomainApprovalDTO;
import net.qixiaowei.system.manage.service.tenant.ITenantDomainApprovalService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-09-24
*/
@RestController
@RequestMapping("tenantDomainApproval")
public class TenantDomainApprovalController extends BaseController
{


    @Autowired
    private ITenantDomainApprovalService tenantDomainApprovalService;

    /**
    * 分页查询租户域名申请列表
    */
    @RequiresPermissions("system:manage:tenantDomainApproval:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TenantDomainApprovalDTO tenantDomainApprovalDTO){
    startPage();
    List<TenantDomainApprovalDTO> list = tenantDomainApprovalService.selectTenantDomainApprovalList(tenantDomainApprovalDTO);
    return getDataTable(list);
    }

    /**
    * 查询租户域名申请列表
    */
    @RequiresPermissions("system:manage:tenantDomainApproval:list")
    @GetMapping("/list")
    public AjaxResult list(TenantDomainApprovalDTO tenantDomainApprovalDTO){
    List<TenantDomainApprovalDTO> list = tenantDomainApprovalService.selectTenantDomainApprovalList(tenantDomainApprovalDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增租户域名申请
    */
    @RequiresPermissions("system:manage:tenantDomainApproval:add")
    @Log(title = "新增租户域名申请", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody TenantDomainApprovalDTO tenantDomainApprovalDTO) {
    return toAjax(tenantDomainApprovalService.insertTenantDomainApproval(tenantDomainApprovalDTO));
    }


    /**
    * 修改租户域名申请
    */
    @RequiresPermissions("system:manage:tenantDomainApproval:edit")
    @Log(title = "修改租户域名申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TenantDomainApprovalDTO tenantDomainApprovalDTO)
    {
    return toAjax(tenantDomainApprovalService.updateTenantDomainApproval(tenantDomainApprovalDTO));
    }

    /**
    * 逻辑删除租户域名申请
    */
    @RequiresPermissions("system:manage:tenantDomainApproval:remove")
    @Log(title = "删除租户域名申请", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TenantDomainApprovalDTO tenantDomainApprovalDTO)
    {
    return toAjax(tenantDomainApprovalService.logicDeleteTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApprovalDTO));
    }
    /**
    * 批量修改租户域名申请
    */
    @RequiresPermissions("system:manage:tenantDomainApproval:edits")
    @Log(title = "批量修改租户域名申请", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TenantDomainApprovalDTO> tenantDomainApprovalDtos)
    {
    return toAjax(tenantDomainApprovalService.updateTenantDomainApprovals(tenantDomainApprovalDtos));
    }

    /**
    * 批量新增租户域名申请
    */
    @RequiresPermissions("system:manage:tenantDomainApproval:insertTenantDomainApprovals")
    @Log(title = "批量新增租户域名申请", businessType = BusinessType.INSERT)
    @PostMapping("/insertTenantDomainApprovals")
    public AjaxResult insertTenantDomainApprovals(@RequestBody List<TenantDomainApprovalDTO> tenantDomainApprovalDtos)
    {
    return toAjax(tenantDomainApprovalService.insertTenantDomainApprovals(tenantDomainApprovalDtos));
    }

    /**
    * 逻辑批量删除租户域名申请
    */
    @RequiresPermissions("system:manage:tenantDomainApproval:removes")
    @Log(title = "批量删除租户域名申请", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<TenantDomainApprovalDTO>  TenantDomainApprovalDtos)
    {
    return toAjax(tenantDomainApprovalService.logicDeleteTenantDomainApprovalByTenantDomainApprovalIds(TenantDomainApprovalDtos));
    }
}
