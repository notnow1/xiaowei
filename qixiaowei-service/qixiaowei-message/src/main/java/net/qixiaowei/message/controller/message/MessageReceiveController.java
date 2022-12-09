package net.qixiaowei.message.controller.message;

import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.message.api.dto.message.MessageReceiveDTO;
import net.qixiaowei.message.service.message.IMessageReceiveService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2022-12-08
 */
@RestController
@RequestMapping("messageReceive")
public class MessageReceiveController extends BaseController {


    @Autowired
    private IMessageReceiveService messageReceiveService;


    /**
     * 分页查询消息接收表列表
     */
//    @RequiresPermissions("message:messageReceive:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(MessageReceiveDTO messageReceiveDTO) {
        startPage();
        List<MessageReceiveDTO> list = messageReceiveService.selectMessageReceiveList(messageReceiveDTO);
        return getDataTable(list);
    }

    /**
     * 查询消息接收表详情
     */
//    @RequiresPermissions("message:messageReceive:info")
    @GetMapping("/info/{messageReceiveId}")
    public AjaxResult info(@PathVariable Long messageReceiveId) {
        MessageReceiveDTO messageReceiveDTO = messageReceiveService.selectMessageReceiveByMessageReceiveId(messageReceiveId);
        return AjaxResult.success(messageReceiveDTO);
    }

    /**
     * 修改已读消息
     */
//    @RequiresPermissions("message:messageReceive:read")
    @PostMapping("/read")
    public AjaxResult read(@Validated(MessageReceiveDTO.UpdateMessageReceiveDTO.class) @RequestBody MessageReceiveDTO messageReceiveDTO) {
        return toAjax(messageReceiveService.read(messageReceiveDTO));
    }

    /**
     * 批量修改已读消息
     */
//    @RequiresPermissions("message:messageReceive:read")
    @PostMapping("/readAll")
    public AjaxResult readAll() {
        return toAjax(messageReceiveService.readAll());
    }

    /**
     * 逻辑删除消息接收表
     */
//    @RequiresPermissions("message:messageReceive:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@Validated(MessageReceiveDTO.DeleteMessageReceiveDTO.class) @RequestBody MessageReceiveDTO messageReceiveDTO) {
        return toAjax(messageReceiveService.logicDeleteMessageReceiveByMessageReceiveId(messageReceiveDTO));
    }

    /**
     * 逻辑批量删除消息接收表
     */
//    @RequiresPermissions("message:messageReceive:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> messageReceiveIds) {
        if (StringUtils.isEmpty(messageReceiveIds)) {
            throw new ServiceException("缺少必要参数，删除失败");
        }
        return toAjax(messageReceiveService.logicDeleteMessageReceiveByMessageReceiveIds(messageReceiveIds));
    }


}
