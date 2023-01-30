package net.qixiaowei.system.manage.controller.user;

import java.util.List;

import net.qixiaowei.system.manage.api.vo.user.UserConfigVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.user.UserConfigDTO;
import net.qixiaowei.system.manage.service.user.IUserConfigService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2023-01-30
 */
@RestController
@RequestMapping("userConfig")
public class UserConfigController extends BaseController {


    @Autowired
    private IUserConfigService userConfigService;


    /**
     * 查询用户配置表详情
     */
    @GetMapping("/info/{userConfigId}")
    public AjaxResult info(@PathVariable Long userConfigId) {
        UserConfigDTO userConfigDTO = userConfigService.selectUserConfigByUserConfigId(userConfigId);
        return AjaxResult.success(userConfigDTO);
    }

    /**
     * 分页查询用户配置表列表
     */
    @GetMapping("/pageList")
    public TableDataInfo pageList(UserConfigDTO userConfigDTO) {
        startPage();
        List<UserConfigVO> list = userConfigService.selectUserConfigList(userConfigDTO);
        return getDataTable(list);
    }

    /**
     * 查询用户配置表列表
     */
    @GetMapping("/list")
    public AjaxResult list(UserConfigDTO userConfigDTO) {
        List<UserConfigVO> list = userConfigService.selectUserConfigList(userConfigDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增用户配置表
     */
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody UserConfigDTO userConfigDTO) {
        return AjaxResult.success(userConfigService.insertUserConfig(userConfigDTO));
    }


    /**
     * 修改用户配置表
     */
    @PostMapping("/edit")
    public AjaxResult editSave(@Validated(UserConfigDTO.UpdateUserConfigDTO.class) @RequestBody UserConfigDTO userConfigDTO) {
        return toAjax(userConfigService.updateUserConfig(userConfigDTO));
    }

    /**
     * 逻辑删除用户配置表
     */
    @RequiresPermissions("system:manage:userConfig:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody UserConfigDTO userConfigDTO) {
        return toAjax(userConfigService.logicDeleteUserConfigByUserConfigId(userConfigDTO));
    }

    /**
     * 批量修改用户配置表
     */
    @RequiresPermissions("system:manage:userConfig:edits")
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<UserConfigDTO> userConfigDtos) {
        return toAjax(userConfigService.updateUserConfigs(userConfigDtos));
    }

    /**
     * 批量新增用户配置表
     */
    @RequiresPermissions("system:manage:userConfig:insertUserConfigs")
    @PostMapping("/insertUserConfigs")
    public AjaxResult insertUserConfigs(@RequestBody List<UserConfigDTO> userConfigDtos) {
        return toAjax(userConfigService.insertUserConfigs(userConfigDtos));
    }

    /**
     * 逻辑批量删除用户配置表
     */
    @RequiresPermissions("system:manage:userConfig:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> userConfigIds) {
        return toAjax(userConfigService.logicDeleteUserConfigByUserConfigIds(userConfigIds));
    }

}
