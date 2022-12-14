package net.qixiaowei.system.manage.controller.tenant;

import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;

import com.alibaba.excel.EasyExcel;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.excel.tenant.TenantExcel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.service.tenant.ITenantService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
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


    //==============================管理租户的管理员==================================//

    /**
     * 分页查询租户表列表
     */
    @RequiresPermissions("system:manage:tenant:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TenantDTO tenantDTO) {
        startPage();
        List<TenantDTO> list = tenantService.selectTenantList(tenantDTO);
        return getDataTable(list);
    }

    /**
     * 新增租户表
     */
    @RequiresPermissions("system:manage:tenant:add")
    @PostMapping("/add")
    public AjaxResult addSave(@Validated(TenantDTO.AddTenantDTO.class) @RequestBody TenantDTO tenantDTO) {
        return AjaxResult.success(tenantService.insertTenant(tenantDTO));
    }

    /**
     * 修改租户表
     */
    @RequiresPermissions(value = {"system:manage:tenant:edit", "system:manage:tenant:info"}, logical = Logical.OR)
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(TenantDTO.UpdateTenantDTO.class) @RequestBody TenantDTO tenantDTO) {
        return toAjax(tenantService.updateTenant(tenantDTO));
    }

    /**
     * 查询单个租户
     */
    @RequiresPermissions("system:manage:tenant:info")
    @GetMapping("/info/{tenantId}")
    public AjaxResult queryTenantDTO(@PathVariable Long tenantId) {
        return AjaxResult.success(tenantService.selectTenantByTenantId(tenantId));
    }

    /**
     * 导出租户
     */
    @SneakyThrows
    @RequiresPermissions("system:manage:tenant:export")
    @GetMapping("export")
    public void exportTenant(@RequestParam Map<String, Object> tenant, TenantDTO tenantDTO, HttpServletResponse response) {
        List<TenantExcel> tenantExcelList = tenantService.exportTenant(tenantDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("租户列表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TenantExcel.class).sheet("租户列表").doWrite(tenantExcelList);
    }

    /**
     * 逻辑删除租户表
     */
    @RequiresPermissions("system:manage:tenant:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(TenantDTO.DeleteTenantDTO.class) TenantDTO tenantDTO) {
        return toAjax(tenantService.logicDeleteTenantByTenantId(tenantDTO));
    }

    /**
     * 逻辑批量删除租户表
     */
    @RequiresPermissions("system:manage:tenant:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody @Validated(TenantDTO.DeleteTenantDTO.class) List<TenantDTO> TenantDtos) {
        return toAjax(tenantService.logicDeleteTenantByTenantIds(TenantDtos));
    }

    //==============================租户本身==================================//

    /**
     * 租户查询自己的登录界面信息
     */
    @GetMapping("/loginForm")
    public AjaxResult queryTenantLoginForm(HttpServletRequest request) {
        return AjaxResult.success(tenantService.queryTenantLoginForm(request));
    }

    /**
     * 租户查询自己的企业信息
     */
    @RequiresPermissions(value = {"system:manage:tenant:info:self", "system:manage:tenant:edit:self"}, logical = Logical.OR)
    @GetMapping("/info")
    public AjaxResult queryTenantInfoOfSelf() {
        return AjaxResult.success(tenantService.queryTenantInfoOfSelf());
    }

    /**
     * 租户修改自己的企业信息
     */
    @RequiresPermissions("system:manage:tenant:edit:self")
    @PostMapping("/updateInfo")
    public AjaxResult updateMyTenant(@RequestBody @Validated(TenantDTO.UpdateTenantInfoDTO.class) TenantDTO tenantDTO) {
        return AjaxResult.success(tenantService.updateMyTenant(tenantDTO));
    }

}
