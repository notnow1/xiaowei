package net.qixiaowei.message.controller.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.message.api.dto.message.MessageContentConfigDTO;
import net.qixiaowei.message.service.message.IMessageContentConfigService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2022-12-08
 */
@RestController
@RequestMapping("messageContentConfig")
public class MessageContentConfigController extends BaseController {


    @Autowired
    private IMessageContentConfigService messageContentConfigService;


    /**
     * 查询消息内容配置表详情
     */
    @RequiresPermissions("message:messageContentConfig:info")
    @GetMapping("/info/{messageContentConfigId}")
    public AjaxResult info(@PathVariable Long messageContentConfigId) {
        MessageContentConfigDTO messageContentConfigDTO = messageContentConfigService.selectMessageContentConfigByMessageContentConfigId(messageContentConfigId);
        return AjaxResult.success(messageContentConfigDTO);
    }

    /**
     * 分页查询消息内容配置表列表
     */
    @RequiresPermissions("message:messageContentConfig:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MessageContentConfigDTO messageContentConfigDTO) {
        startPage();
        List<MessageContentConfigDTO> list = messageContentConfigService.selectMessageContentConfigList(messageContentConfigDTO);
        return getDataTable(list);
    }

    /**
     * 查询消息内容配置表列表
     */
    @RequiresPermissions("message:messageContentConfig:list")
    @GetMapping("/list")
    public AjaxResult list(MessageContentConfigDTO messageContentConfigDTO) {
        List<MessageContentConfigDTO> list = messageContentConfigService.selectMessageContentConfigList(messageContentConfigDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增消息内容配置表
     */
    @RequiresPermissions("message:messageContentConfig:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody MessageContentConfigDTO messageContentConfigDTO) {
        return AjaxResult.success(messageContentConfigService.insertMessageContentConfig(messageContentConfigDTO));
    }


    /**
     * 修改消息内容配置表
     */
    @RequiresPermissions("message:messageContentConfig:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody MessageContentConfigDTO messageContentConfigDTO) {
        return toAjax(messageContentConfigService.updateMessageContentConfig(messageContentConfigDTO));
    }

    /**
     * 逻辑删除消息内容配置表
     */
    @RequiresPermissions("message:messageContentConfig:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody MessageContentConfigDTO messageContentConfigDTO) {
        return toAjax(messageContentConfigService.logicDeleteMessageContentConfigByMessageContentConfigId(messageContentConfigDTO));
    }

    /**
     * 批量修改消息内容配置表
     */
    @RequiresPermissions("message:messageContentConfig:edits")
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<MessageContentConfigDTO> messageContentConfigDtos) {
        return toAjax(messageContentConfigService.updateMessageContentConfigs(messageContentConfigDtos));
    }

    /**
     * 批量新增消息内容配置表
     */
    @RequiresPermissions("message:messageContentConfig:insertMessageContentConfigs")
    @PostMapping("/insertMessageContentConfigs")
    public AjaxResult insertMessageContentConfigs(@RequestBody List<MessageContentConfigDTO> messageContentConfigDtos) {
        return toAjax(messageContentConfigService.insertMessageContentConfigs(messageContentConfigDtos));
    }

    /**
     * 逻辑批量删除消息内容配置表
     */
    @RequiresPermissions("message:messageContentConfig:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> messageContentConfigIds) {
        return toAjax(messageContentConfigService.logicDeleteMessageContentConfigByMessageContentConfigIds(messageContentConfigIds));
    }

}
