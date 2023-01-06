package net.qixiaowei.system.manage.controller.log;

import java.util.List;

import net.qixiaowei.system.manage.api.vo.log.OperationLogVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.log.OperationLogDTO;
import net.qixiaowei.system.manage.service.log.IOperationLogService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author hzk
 * @since 2023-01-04
 */
@RestController
@RequestMapping("operationLog")
public class OperationLogController extends BaseController {


    @Autowired
    private IOperationLogService operationLogService;


    /**
     * 查询操作日志表详情
     */
    @RequiresPermissions("system:manage:operationLog:info")
    @GetMapping("/info/{operationLogId}")
    public AjaxResult info(@PathVariable Long operationLogId) {
        OperationLogDTO operationLogDTO = operationLogService.selectOperationLogByOperationLogId(operationLogId);
        return AjaxResult.success(operationLogDTO);
    }

    /**
     * 分页查询操作日志表列表
     */
    @GetMapping("/pageList")
    public TableDataInfo pageList(@Validated(OperationLogDTO.QueryOperationLogDTO.class) OperationLogDTO operationLogDTO) {
        startPage();
        List<OperationLogVO> list = operationLogService.selectOperationLogList(operationLogDTO);
        return getDataTable(list);
    }

    /**
     * 查询操作日志表列表
     */
    @GetMapping("/list")
    public AjaxResult list(@Validated(OperationLogDTO.QueryOperationLogDTO.class) OperationLogDTO operationLogDTO) {
        List<OperationLogVO> list = operationLogService.selectOperationLogList(operationLogDTO);
        return AjaxResult.success(list);
    }

    /**
     * 修改操作日志表
     */
    @RequiresPermissions("system:manage:operationLog:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody OperationLogDTO operationLogDTO) {
        return toAjax(operationLogService.updateOperationLog(operationLogDTO));
    }

    /**
     * 逻辑删除操作日志表
     */
    @RequiresPermissions("system:manage:operationLog:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody OperationLogDTO operationLogDTO) {
        return toAjax(operationLogService.logicDeleteOperationLogByOperationLogId(operationLogDTO));
    }

    /**
     * 逻辑批量删除操作日志表
     */
    @RequiresPermissions("system:manage:operationLog:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> operationLogIds) {
        return toAjax(operationLogService.logicDeleteOperationLogByOperationLogIds(operationLogIds));
    }

}
