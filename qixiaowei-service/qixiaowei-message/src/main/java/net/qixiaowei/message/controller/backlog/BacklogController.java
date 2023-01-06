package net.qixiaowei.message.controller.backlog;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.service.backlog.IBacklogService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2022-12-11
 */
@RestController
@RequestMapping("backlog")
public class BacklogController extends BaseController {


    @Autowired
    private IBacklogService backlogService;


    /**
     * 查询待办事项统计
     */
//    @RequiresPermissions("message:backlog:count")
    @GetMapping("/count")
    public AjaxResult count() {
        return AjaxResult.success(backlogService.count());
    }


    /**
     * 分页查询待办事项表列表
     */
//    @RequiresPermissions("message:backlog:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(BacklogDTO backlogDTO) {
        startPage();
        List<BacklogDTO> list = backlogService.selectBacklogList(backlogDTO);
        return getDataTable(list);
    }


    /**
     * 查询待办事项表详情
     */
    @RequiresPermissions("message:backlog:info")
    @GetMapping("/info/{backlogId}")
    public AjaxResult info(@PathVariable Long backlogId) {
        BacklogDTO backlogDTO = backlogService.selectBacklogByBacklogId(backlogId);
        return AjaxResult.success(backlogDTO);
    }


    /**
     * 查询待办事项表列表
     */
    @RequiresPermissions("message:backlog:list")
    @GetMapping("/list")
    public AjaxResult list(BacklogDTO backlogDTO) {
        List<BacklogDTO> list = backlogService.selectBacklogList(backlogDTO);
        return AjaxResult.success(list);
    }

    /**
     * 逻辑删除待办事项表
     */
    @RequiresPermissions("message:backlog:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody BacklogDTO backlogDTO) {
        return toAjax(backlogService.logicDeleteBacklogByBacklogId(backlogDTO));
    }

    /**
     * 逻辑批量删除待办事项表
     */
    @RequiresPermissions("message:backlog:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> backlogIds) {
        return toAjax(backlogService.logicDeleteBacklogByBacklogIds(backlogIds));
    }

}
