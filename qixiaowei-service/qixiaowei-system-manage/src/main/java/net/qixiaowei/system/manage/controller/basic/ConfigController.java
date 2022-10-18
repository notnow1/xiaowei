package net.qixiaowei.system.manage.controller.basic;

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
import net.qixiaowei.system.manage.api.dto.basic.ConfigDTO;
import net.qixiaowei.system.manage.service.basic.IConfigService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author Graves
 * @since 2022-10-18
 */
@RestController
@RequestMapping("config")
public class ConfigController extends BaseController {


    @Autowired
    private IConfigService configService;

    /**
     * 分页查询配置表列表
     */
    @RequiresPermissions("system:manage:config:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(ConfigDTO configDTO) {
        startPage();
        List<ConfigDTO> list = configService.selectConfigList(configDTO);
        return getDataTable(list);
    }

    /**
     * 查询配置表列表
     */
    @RequiresPermissions("system:manage:config:list")
    @GetMapping("/list")
    public AjaxResult list(ConfigDTO configDTO) {
        List<ConfigDTO> list = configService.selectConfigList(configDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增配置表
     */
    @RequiresPermissions("system:manage:config:add")
    @Log(title = "新增配置表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody ConfigDTO configDTO) {
        return AjaxResult.success(configService.insertConfig(configDTO));
    }


    /**
     * 修改配置表
     */
    @RequiresPermissions("system:manage:config:edit")
    @Log(title = "修改配置表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody ConfigDTO configDTO) {
        return toAjax(configService.updateConfig(configDTO));
    }

    /**
     * 逻辑删除配置表
     */
    @RequiresPermissions("system:manage:config:remove")
    @Log(title = "删除配置表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody ConfigDTO configDTO) {
        return toAjax(configService.logicDeleteConfigByConfigId(configDTO));
    }

    /**
     * 批量修改配置表
     */
    @RequiresPermissions("system:manage:config:edits")
    @Log(title = "批量修改配置表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<ConfigDTO> configDtos) {
        return toAjax(configService.updateConfigs(configDtos));
    }

    /**
     * 批量新增配置表
     */
    @RequiresPermissions("system:manage:config:insertConfigs")
    @Log(title = "批量新增配置表", businessType = BusinessType.INSERT)
    @PostMapping("/insertConfigs")
    public AjaxResult insertConfigs(@RequestBody List<ConfigDTO> configDtos) {
        return toAjax(configService.insertConfigs(configDtos));
    }

    /**
     * 逻辑批量删除配置表
     */
    @RequiresPermissions("system:manage:config:removes")
    @Log(title = "批量删除配置表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> configIds) {
        return toAjax(configService.logicDeleteConfigByConfigIds(configIds));
    }
}
