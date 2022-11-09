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
import net.qixiaowei.system.manage.api.dto.tenant.TenantContractDTO;
import net.qixiaowei.system.manage.service.tenant.ITenantContractService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-10-09
*/
@RestController
@RequestMapping("tenantContract")
public class TenantContractController extends BaseController
{


    @Autowired
    private ITenantContractService tenantContractService;


    /**
    * 查询租户合同信息详情
    */
    @RequiresPermissions("system:manage:tenantContract:info")
    @GetMapping("/info/{tenantContractId}")
    public AjaxResult info(@PathVariable Long tenantContractId){
    TenantContractDTO tenantContractDTO = tenantContractService.selectTenantContractByTenantContractId(tenantContractId);
        return AjaxResult.success(tenantContractDTO);
    }

    /**
    * 分页查询租户合同信息列表
    */
    @RequiresPermissions("system:manage:tenantContract:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TenantContractDTO tenantContractDTO){
    startPage();
    List<TenantContractDTO> list = tenantContractService.selectTenantContractList(tenantContractDTO);
    return getDataTable(list);
    }

    /**
    * 查询租户合同信息列表
    */
    @RequiresPermissions("system:manage:tenantContract:list")
    @GetMapping("/list")
    public AjaxResult list(TenantContractDTO tenantContractDTO){
    List<TenantContractDTO> list = tenantContractService.selectTenantContractList(tenantContractDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增租户合同信息
    */
    @RequiresPermissions("system:manage:tenantContract:add")
    @Log(title = "新增租户合同信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody TenantContractDTO tenantContractDTO) {
    return toAjax(tenantContractService.insertTenantContract(tenantContractDTO));
    }


    /**
    * 修改租户合同信息
    */
    @RequiresPermissions("system:manage:tenantContract:edit")
    @Log(title = "修改租户合同信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TenantContractDTO tenantContractDTO)
    {
    return toAjax(tenantContractService.updateTenantContract(tenantContractDTO));
    }

    /**
    * 逻辑删除租户合同信息
    */
    @RequiresPermissions("system:manage:tenantContract:remove")
    @Log(title = "删除租户合同信息", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TenantContractDTO tenantContractDTO)
    {
    return toAjax(tenantContractService.logicDeleteTenantContractByTenantContractId(tenantContractDTO));
    }
    /**
    * 批量修改租户合同信息
    */
    @RequiresPermissions("system:manage:tenantContract:edits")
    @Log(title = "批量修改租户合同信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TenantContractDTO> tenantContractDtos)
    {
    return toAjax(tenantContractService.updateTenantContracts(tenantContractDtos));
    }

    /**
    * 批量新增租户合同信息
    */
    @RequiresPermissions("system:manage:tenantContract:insertTenantContracts")
    @Log(title = "批量新增租户合同信息", businessType = BusinessType.INSERT)
    @PostMapping("/insertTenantContracts")
    public AjaxResult insertTenantContracts(@RequestBody List<TenantContractDTO> tenantContractDtos)
    {
    return toAjax(tenantContractService.insertTenantContracts(tenantContractDtos));
    }

    /**
    * 逻辑批量删除租户合同信息
    */
    @RequiresPermissions("system:manage:tenantContract:removes")
    @Log(title = "批量删除租户合同信息", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<TenantContractDTO>  TenantContractDtos)
    {
    return toAjax(tenantContractService.logicDeleteTenantContractByTenantContractIds(TenantContractDtos));
    }
}
