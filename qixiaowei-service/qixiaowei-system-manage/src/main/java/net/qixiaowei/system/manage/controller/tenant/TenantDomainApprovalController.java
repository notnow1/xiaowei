package net.qixiaowei.system.manage.controller.tenant;

import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDomainApprovalDTO;
import net.qixiaowei.system.manage.service.tenant.ITenantDomainApprovalService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-10-09
 */
@RestController
@RequestMapping("tenantDomainApproval")
public class TenantDomainApprovalController extends BaseController {


    @Autowired
    private ITenantDomainApprovalService tenantDomainApprovalService;


    /**
     * 处理租户域名申请
     */
    @PostMapping("/process")
    public AjaxResult process(@RequestBody TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        if (StringUtils.isNull(tenantDomainApprovalDTO.getTenantDomainApprovalId()) || StringUtils.isNull(tenantDomainApprovalDTO.getApprovalStatus())) {
            throw new ServiceException("缺少必要参数");
        }
        return toAjax(tenantDomainApprovalService.process(tenantDomainApprovalDTO));
    }


    /**
     * 查询租户域名申请详情
     */
//    @RequiresPermissions("system:manage:tenantDomainApproval:info")
    @GetMapping("/info/{tenantDomainApprovalId}")
    public AjaxResult info(@PathVariable Long tenantDomainApprovalId) {
        TenantDomainApprovalDTO tenantDomainApprovalDTO = tenantDomainApprovalService.selectTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApprovalId);
        return AjaxResult.success(tenantDomainApprovalDTO);
    }

    /**
     * 分页查询租户域名申请列表
     */
    @RequiresPermissions("system:manage:tenantDomainApproval:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        startPage();
        List<TenantDomainApprovalDTO> list = tenantDomainApprovalService.selectTenantDomainApprovalList(tenantDomainApprovalDTO);
        return getDataTable(list);
    }

    /**
     * 查询租户域名申请列表
     */
    @RequiresPermissions("system:manage:tenantDomainApproval:list")
    @GetMapping("/list")
    public AjaxResult list(TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        List<TenantDomainApprovalDTO> list = tenantDomainApprovalService.selectTenantDomainApprovalList(tenantDomainApprovalDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增租户域名申请
     */
    @RequiresPermissions("system:manage:tenantDomainApproval:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        return toAjax(tenantDomainApprovalService.insertTenantDomainApproval(tenantDomainApprovalDTO));
    }


    /**
     * 修改租户域名申请
     */
    @RequiresPermissions("system:manage:tenantDomainApproval:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        return toAjax(tenantDomainApprovalService.updateTenantDomainApproval(tenantDomainApprovalDTO));
    }

    /**
     * 逻辑删除租户域名申请
     */
    @RequiresPermissions("system:manage:tenantDomainApproval:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody TenantDomainApprovalDTO tenantDomainApprovalDTO) {
        return toAjax(tenantDomainApprovalService.logicDeleteTenantDomainApprovalByTenantDomainApprovalId(tenantDomainApprovalDTO));
    }

    /**
     * 批量修改租户域名申请
     */
    @RequiresPermissions("system:manage:tenantDomainApproval:edits")
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TenantDomainApprovalDTO> tenantDomainApprovalDtos) {
        return toAjax(tenantDomainApprovalService.updateTenantDomainApprovals(tenantDomainApprovalDtos));
    }

    /**
     * 批量新增租户域名申请
     */
    @RequiresPermissions("system:manage:tenantDomainApproval:insertTenantDomainApprovals")
    @PostMapping("/insertTenantDomainApprovals")
    public AjaxResult insertTenantDomainApprovals(@RequestBody List<TenantDomainApprovalDTO> tenantDomainApprovalDtos) {
        return toAjax(tenantDomainApprovalService.insertTenantDomainApprovals(tenantDomainApprovalDtos));
    }

    /**
     * 逻辑批量删除租户域名申请
     */
    @RequiresPermissions("system:manage:tenantDomainApproval:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<TenantDomainApprovalDTO> TenantDomainApprovalDtos) {
        return toAjax(tenantDomainApprovalService.logicDeleteTenantDomainApprovalByTenantDomainApprovalIds(TenantDomainApprovalDtos));
    }
}
