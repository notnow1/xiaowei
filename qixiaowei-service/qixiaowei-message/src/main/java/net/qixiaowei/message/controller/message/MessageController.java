package net.qixiaowei.message.controller.message;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.message.api.dto.message.MessageDTO;
import net.qixiaowei.message.service.message.IMessageService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2022-12-09
 */
@RestController
@RequestMapping("message")
public class MessageController extends BaseController {


    @Autowired
    private IMessageService messageService;


    /**
     * 查询消息表详情
     */
//    @RequiresPermissions("message:message:info")
    @GetMapping("/info/{messageId}")
    public AjaxResult info(@PathVariable Long messageId) {
        MessageDTO messageDTO = messageService.selectMessageByMessageId(messageId);
        return AjaxResult.success(messageDTO);
    }

    /**
     * 分页查询消息表列表
     */
//    @RequiresPermissions("message:message:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MessageDTO messageDTO) {
        startPage();
        List<MessageDTO> list = messageService.selectMessageList(messageDTO);
        return getDataTable(list);
    }

    /**
     * 查询消息表列表
     */
//    @RequiresPermissions("message:message:list")
    @GetMapping("/list")
    public AjaxResult list(MessageDTO messageDTO) {
        List<MessageDTO> list = messageService.selectMessageList(messageDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增消息表
     */
    @RequiresPermissions("message:message:add")
    @Log(title = "新增消息表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody MessageDTO messageDTO) {
        return AjaxResult.success(messageService.insertMessage(messageDTO));
    }


    /**
     * 修改消息表
     */
    @RequiresPermissions("message:message:edit")
    @Log(title = "修改消息表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody MessageDTO messageDTO) {
        return toAjax(messageService.updateMessage(messageDTO));
    }

    /**
     * 逻辑删除消息表
     */
    @RequiresPermissions("message:message:remove")
    @Log(title = "删除消息表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody MessageDTO messageDTO) {
        return toAjax(messageService.logicDeleteMessageByMessageId(messageDTO));
    }

    /**
     * 批量修改消息表
     */
    @RequiresPermissions("message:message:edits")
    @Log(title = "批量修改消息表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<MessageDTO> messageDtos) {
        return toAjax(messageService.updateMessages(messageDtos));
    }

    /**
     * 批量新增消息表
     */
    @RequiresPermissions("message:message:insertMessages")
    @Log(title = "批量新增消息表", businessType = BusinessType.INSERT)
    @PostMapping("/insertMessages")
    public AjaxResult insertMessages(@RequestBody List<MessageDTO> messageDtos) {
        return toAjax(messageService.insertMessages(messageDtos));
    }

    /**
     * 逻辑批量删除消息表
     */
    @RequiresPermissions("message:message:removes")
    @Log(title = "批量删除消息表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> messageIds) {
        return toAjax(messageService.logicDeleteMessageByMessageIds(messageIds));
    }

}
