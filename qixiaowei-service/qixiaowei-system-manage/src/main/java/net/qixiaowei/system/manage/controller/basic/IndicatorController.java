package net.qixiaowei.system.manage.controller.basic;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.service.basic.IIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author Graves
 * @since 2022-09-28
 */
@RestController
@RequestMapping("indicator")
public class IndicatorController extends BaseController {


    @Autowired
    private IIndicatorService indicatorService;


    /**
     * 分页查询指标表列表
     */
    @RequiresPermissions("system:manage:indicator:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndicatorDTO indicatorDTO) {
        startPage();
        Integer indicatorType = indicatorDTO.getIndicatorType();
        if (StringUtils.isNull(indicatorType)) {
            indicatorDTO.setIndicatorType(1);
        }
        List<IndicatorDTO> list = indicatorService.selectIndicatorList(indicatorDTO);
        return getDataTable(list);
    }

    /**
     * 查询指标表树状图
     */
    @GetMapping("/treeList")
    public AjaxResult treeList(IndicatorDTO indicatorDTO) {
        Integer indicatorType = indicatorDTO.getIndicatorType();
        indicatorDTO.setIndicatorType(null);
        if (!CheckObjectIsNullUtils.isNull(indicatorDTO) || StringUtils.isNotEmpty(indicatorDTO.getParams())) {
            indicatorDTO.setIndicatorType(indicatorType);
            return AjaxResult.success(indicatorService.selectIndicatorList(indicatorDTO));
        }
        indicatorDTO.setIndicatorType(indicatorType);
        return AjaxResult.success(indicatorService.selectTreeList(indicatorDTO));
    }

    /**
     * 查询绩效的指标表树状图下拉
     */
    @GetMapping("/performanceAppraisal/treeList")
    public AjaxResult performanceTreeList(IndicatorDTO indicatorDTO) {
        return AjaxResult.success(indicatorService.performanceTreeList(indicatorDTO));
    }


    /**
     * 获取上级指标
     */
    @RequiresPermissions(value = {"system:manage:indicator:treeList", "system:manage:indicator:pageList"}, logical = Logical.OR)
    @GetMapping("/getSuperIndicator/{indicatorId}")
    public AjaxResult getSuperIndicator(@PathVariable Long indicatorId) {
        return AjaxResult.success(indicatorService.getSuperIndicator(indicatorId));
    }

    /**
     * 生成指标编码
     *
     * @return 指标编码
     */
    @RequiresPermissions(value = {"system:manage:indicator:add", "system:manage:indicator:edit"}, logical = Logical.OR)
    @GetMapping("/generate/indicatorCode")
    public AjaxResult generateIndicatorCode(@RequestParam(value = "indicatorType", required = false, defaultValue = "1") Integer indicatorType) {
        return AjaxResult.success("操作成功", indicatorService.generateIndicatorCode(indicatorType));
    }

    /**
     * 新增指标表
     */
    @Log(title = "新增指标", businessType = BusinessType.INDICATOR, businessId = "indicatorId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:indicator:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndicatorDTO indicatorDTO) {
        return AjaxResult.success("新增成功", indicatorService.insertIndicator(indicatorDTO));
    }

    /**
     * 修改指标表
     */
    @Log(title = "编辑指标", businessType = BusinessType.INDICATOR, businessId = "indicatorId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:indicator:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndicatorDTO indicatorDTO) {
        return toAjax(indicatorService.updateIndicator(indicatorDTO));
    }

    /**
     * 指标详情
     */
    @RequiresPermissions(value = {"system:manage:indicator:info", "system:manage:indicator:edit"}, logical = Logical.OR)
    @GetMapping("/info/{indicatorId}")
    public AjaxResult info(@PathVariable Long indicatorId) {
        return AjaxResult.success(indicatorService.detailIndicator(indicatorId));
    }

    /**
     * 逻辑删除指标表
     */
    @RequiresPermissions("system:manage:indicator:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndicatorDTO indicatorDTO) {
        Long indicatorId = indicatorDTO.getIndicatorId();
        if (StringUtils.isNull(indicatorId)) {
            return AjaxResult.error("指标id不能为空！");
        }
        return toAjax(indicatorService.logicDeleteIndicatorByIndicatorId(indicatorId));
    }

    /**
     * 逻辑批量删除指标表
     */
    @RequiresPermissions("system:manage:indicator:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> indicatorIds) {
        return toAjax(indicatorService.logicDeleteIndicatorByIndicatorIds(indicatorIds));
    }

    /**
     * 获取指标最大层级
     */
    @RequiresPermissions(value = {"system:manage:indicator:pageList", "system:manage:indicator:treeList"}, logical = Logical.OR)
    @GetMapping("/selectLevel")
    public AjaxResult level() {
        return AjaxResult.success(indicatorService.getLevel());
    }


    /**
     * 查询指标表列表
     */
    @GetMapping("/list")
    public AjaxResult list(IndicatorDTO indicatorDTO) {
        List<IndicatorDTO> list = indicatorService.selectIndicatorList(indicatorDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询仪表盘指标表列表
     */
    @GetMapping("/dashboard/list")
    public AjaxResult dashboardList(@RequestParam("targetYear") Integer targetYear) {
        return AjaxResult.success(indicatorService.selectIndicatorDashboardList(targetYear));
    }

}
