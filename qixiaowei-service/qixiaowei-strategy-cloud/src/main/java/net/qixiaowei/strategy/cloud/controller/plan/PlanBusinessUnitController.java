package net.qixiaowei.strategy.cloud.controller.plan;

import java.util.List;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.service.plan.IPlanBusinessUnitService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author wangchen
 * @since 2023-02-17
 */
@RestController
@RequestMapping("planBusinessUnit")
public class PlanBusinessUnitController extends BaseController {


    @Autowired
    private IPlanBusinessUnitService planBusinessUnitService;


    /**
     * 查询规划业务单元详情
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:info")
    @GetMapping("/info/{planBusinessUnitId}")
    public AjaxResult info(@PathVariable Long planBusinessUnitId) {
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitService.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        return AjaxResult.success(planBusinessUnitDTO);
    }

    /**
     * 分页查询规划业务单元列表
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PlanBusinessUnitDTO planBusinessUnitDTO) {
        startPage();
        List<PlanBusinessUnitDTO> list = planBusinessUnitService.selectPlanBusinessUnitList(planBusinessUnitDTO);
        return getDataTable(list);
    }

    /**
     * 查询规划业务单元列表
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:list")
    @GetMapping("/list")
    public AjaxResult list(PlanBusinessUnitDTO planBusinessUnitDTO) {
        planBusinessUnitDTO.setStatus(1);
        List<PlanBusinessUnitDTO> list = planBusinessUnitService.selectPlanBusinessUnitList(planBusinessUnitDTO);
        return AjaxResult.success(list);
    }


    /**
     * 新增规划业务单元
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:add")
    @PostMapping("/add")
    @Log(title = "新增规划业务单元", businessType = BusinessType.PLAN_BUSINESS_UNIT, businessId = "planBusinessUnitId", operationType = OperationType.INSERT)
    public AjaxResult addSave(@RequestBody PlanBusinessUnitDTO planBusinessUnitDTO) {
        return AjaxResult.success(planBusinessUnitService.insertPlanBusinessUnit(planBusinessUnitDTO));
    }


    /**
     * 修改规划业务单元
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:edit")
    @Log(title = "修改规划业务单元", businessType = BusinessType.PLAN_BUSINESS_UNIT, businessId = "planBusinessUnitId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody PlanBusinessUnitDTO planBusinessUnitDTO) {
        return toAjax(planBusinessUnitService.updatePlanBusinessUnit(planBusinessUnitDTO));
    }

    /**
     * 逻辑删除规划业务单元
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody PlanBusinessUnitDTO planBusinessUnitDTO) {
        return toAjax(planBusinessUnitService.logicDeletePlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitDTO));
    }

    /**
     * 批量修改规划业务单元
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:edits")
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<PlanBusinessUnitDTO> planBusinessUnitDtos) {
        return toAjax(planBusinessUnitService.updatePlanBusinessUnits(planBusinessUnitDtos));
    }

    /**
     * 批量新增规划业务单元
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:insertPlanBusinessUnits")
    @PostMapping("/insertPlanBusinessUnits")
    public AjaxResult insertPlanBusinessUnits(@RequestBody List<PlanBusinessUnitDTO> planBusinessUnitDtos) {
        return toAjax(planBusinessUnitService.insertPlanBusinessUnits(planBusinessUnitDtos));
    }

    /**
     * 逻辑批量删除规划业务单元
     */
    @RequiresPermissions("strategy:cloud:planBusinessUnit:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> planBusinessUnitIds) {
        return toAjax(planBusinessUnitService.logicDeletePlanBusinessUnitByPlanBusinessUnitIds(planBusinessUnitIds));
    }

    /**
     * 生成规划业务单元编码
     *
     * @return 规划业务单元编码
     */
    @RequiresPermissions(value = {"strategy:cloud:planBusinessUnit:add", "strategy:cloud:planBusinessUnit:edit"}, logical = Logical.OR)
    @GetMapping("/generate/planBusinessUnitCode")
    public AjaxResult generatePlanBusinessUnitCode() {
        return AjaxResult.success("操作成功", planBusinessUnitService.generatePlanBusinessUnitCode());
    }

}
