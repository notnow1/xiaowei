package net.qixiaowei.system.manage.controller.tenant;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.system.manage.excel.tenant.TenantExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;


/**
 * @author TANGMICHI
 * @since 2022-09-24
 */
@RestController
@RequestMapping("tenant")
public class TenantController extends BaseController {


    @Autowired
    private ITenantService tenantService;


    /**
     * 导出租户
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportTenant(@RequestParam Map<String, Object> tenant,TenantDTO tenantDTO, HttpServletResponse response) {
        List<TenantExcel> tenantExcelList = tenantService.exportTenant(tenantDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("租户列表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TenantExcel.class).sheet("租户列表").doWrite(tenantExcelList);
    }
    /**
     * 查询单个租户
     */
    //@RequiresPermissions("system:manage:tenant:pageList")
    @GetMapping("/info/{tenantId}")
    public AjaxResult queryTenantDTO(@PathVariable Long tenantId) {
        return AjaxResult.success(tenantService.selectTenantByTenantId(tenantId));
    }

    /**
     * 查询单个租户
     */
    //@RequiresPermissions("system:manage:tenant:pageList")
    @GetMapping("/info")
    public AjaxResult queryTenantDTO() {
        return AjaxResult.success(tenantService.selectTenantByTenantId());
    }
    /**
     * 修改企业信息
     */
    //@RequiresPermissions("system:manage:tenant:pageList")
    @PostMapping("/updateInfo")
    public AjaxResult updateMyTenantDTO(@RequestBody @Validated(TenantDTO.UpdateTenantInfoDTO.class) TenantDTO tenantDTO) {
        return AjaxResult.success(tenantService.updateMyTenant(tenantDTO));
    }

    /**
     * 分页查询租户表列表
     */
    //@RequiresPermissions("system:manage:tenant:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TenantDTO tenantDTO) {
        startPage();
        List<TenantDTO> list = tenantService.selectTenantList(tenantDTO);
        return getDataTable(list);
    }

    /**
     * 查询租户表列表
     */
    //@RequiresPermissions("system:manage:tenant:list")
    @GetMapping("/list")
    public AjaxResult list(TenantDTO tenantDTO) {
        List<TenantDTO>  list = tenantService.selectTenantList(tenantDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增租户表
     */
    //@RequiresPermissions("system:manage:tenant:add")
    // @Log(title = "新增租户表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@Validated(TenantDTO.AddTenantDTO.class) @RequestBody TenantDTO tenantDTO) {
        return AjaxResult.success(tenantService.insertTenant(tenantDTO));
    }


    /**
     * 修改租户表
     */
    //@RequiresPermissions("system:manage:tenant:edit")
//    @Log(title = "修改租户表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(TenantDTO.UpdateTenantDTO.class) @RequestBody TenantDTO tenantDTO) {
        return toAjax(tenantService.updateTenant(tenantDTO));
    }

    /**
     * 逻辑删除租户表
     */
    //@RequiresPermissions("system:manage:tenant:remove")
    @Log(title = "删除租户表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(TenantDTO.DeleteTenantDTO.class) TenantDTO tenantDTO) {
        return toAjax(tenantService.logicDeleteTenantByTenantId(tenantDTO));
    }

    /**
     * 批量修改租户表
     */
    //@RequiresPermissions("system:manage:tenant:edits")
    @Log(title = "批量修改租户表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<TenantDTO> tenantDtos) {
        return toAjax(tenantService.updateTenants(tenantDtos));
    }

    /**
     * 批量新增租户表
     */
    //@RequiresPermissions("system:manage:tenant:insertTenants")
    @Log(title = "批量新增租户表", businessType = BusinessType.INSERT)
    @PostMapping("/insertTenants")
    public AjaxResult insertTenants(@RequestBody List<TenantDTO> tenantDtos) {
        return toAjax(tenantService.insertTenants(tenantDtos));
    }

    /**
     * 逻辑批量删除租户表
     */
    //@RequiresPermissions("system:manage:tenant:removes")
    @Log(title = "批量删除租户表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody @Validated(TenantDTO.DeleteTenantDTO.class) List<TenantDTO> TenantDtos) {
        return toAjax(tenantService.logicDeleteTenantByTenantIds(TenantDtos));
    }
}
