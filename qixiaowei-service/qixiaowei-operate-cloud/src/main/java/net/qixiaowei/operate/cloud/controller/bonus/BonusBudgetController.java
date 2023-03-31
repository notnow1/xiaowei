package net.qixiaowei.operate.cloud.controller.bonus;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetParametersDTO;
import net.qixiaowei.operate.cloud.service.bonus.IBonusBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2022-11-26
 */
@RestController
@RequestMapping("bonusBudget")
public class BonusBudgetController extends BaseController {


    @Autowired
    private IBonusBudgetService bonusBudgetService;

    /**
     * 分页查询奖金预算表列表
     */
    @RequiresPermissions("operate:cloud:bonusBudget:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(BonusBudgetDTO bonusBudgetDTO) {
        startPage();
        List<BonusBudgetDTO> list = bonusBudgetService.selectBonusBudgetList(bonusBudgetDTO);
        return getDataTable(list);
    }

    /**
     * 新增奖金预算表
     */
    @Log(title = "新增总奖金包预算", businessType = BusinessType.BONUS_BUDGET, businessId = "bonusBudgetId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:bonusBudget:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody BonusBudgetDTO bonusBudgetDTO) {
        return AjaxResult.success(bonusBudgetService.insertBonusBudget(bonusBudgetDTO));
    }

    /**
     * 修改奖金预算表
     */
    @Log(title = "保存总奖金包预算", businessType = BusinessType.BONUS_BUDGET, businessId = "bonusBudgetId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:bonusBudget:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(BonusBudgetDTO.UpdateBonusBudgetDTO.class) BonusBudgetDTO bonusBudgetDTO) {
        return toAjax(bonusBudgetService.updateBonusBudget(bonusBudgetDTO));
    }

    /**
     * 查询奖金预算表详情
     */
    @RequiresPermissions("operate:cloud:bonusBudget:info")
    @GetMapping("/info/{bonusBudgetId}")
    public AjaxResult info(@PathVariable Long bonusBudgetId) {
        BonusBudgetDTO bonusBudgetDTO = bonusBudgetService.selectBonusBudgetByBonusBudgetId(bonusBudgetId);
        return AjaxResult.success(bonusBudgetDTO);
    }

    /**
     * 逻辑删除奖金预算表
     */
    @RequiresPermissions("operate:cloud:bonusBudget:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(BonusBudgetDTO.DeleteBonusBudgetDTO.class) BonusBudgetDTO bonusBudgetDTO) {
        return toAjax(bonusBudgetService.logicDeleteBonusBudgetByBonusBudgetId(bonusBudgetDTO));
    }

    /**
     * 逻辑批量删除奖金预算表
     */
    @RequiresPermissions("operate:cloud:bonusBudget:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> bonusBudgetIds) {
        return toAjax(bonusBudgetService.logicDeleteBonusBudgetByBonusBudgetIds(bonusBudgetIds));
    }

    /**
     * 返回最大年份
     */
    @RequiresPermissions(value = {"operate:cloud:bonusBudget:add", "operate:cloud:bonusBudget:edit"}, logical = Logical.OR)
    @GetMapping("/queryBonusBudgetYear")
    public AjaxResult queryLatelyBudgetYear() {
        int planYear = bonusBudgetService.queryBonusBudgetYear();
        return AjaxResult.success(planYear);
    }

    /**
     * 新增奖金预算预制数据
     */
    @RequiresPermissions(value = {"operate:cloud:bonusBudget:add", "operate:cloud:bonusBudget:edit"}, logical = Logical.OR)
    @GetMapping("/add/{budgetYear}")
    public AjaxResult addBonusBudgetTamount(@PathVariable int budgetYear) {
        BonusBudgetDTO bonusBudgetDTO = bonusBudgetService.addBonusBudgetTamount(budgetYear);
        if (StringUtils.isNull(bonusBudgetDTO)) {
            return AjaxResult.success(new BonusBudgetDTO());
        }
        return AjaxResult.success(bonusBudgetDTO);
    }

    /**
     * 新增奖金预算新增指标带出数据
     */
    //@RequiresPermissions(value = {"operate:cloud:bonusBudget:add", "operate:cloud:bonusBudget:edit"}, logical = Logical.OR)
    @PostMapping("/add/indicatorTamount")
    public AjaxResult addBonusBudgetIndicatorTamount(@RequestBody BonusBudgetDTO bonusBudgetDTO) {
        BonusBudgetParametersDTO bonusBudgetParametersDTO = bonusBudgetService.addBonusBudgetIndicatorTamount(bonusBudgetDTO);
        if (StringUtils.isNull(bonusBudgetParametersDTO)) {
            return AjaxResult.success(new BonusBudgetParametersDTO());
        }
        return AjaxResult.success(bonusBudgetParametersDTO);
    }

    /**
     * 新增总奖金包预算阶梯预制数据
     */
    @RequiresPermissions(value = {"operate:cloud:bonusBudget:add", "operate:cloud:bonusBudget:edit"}, logical = Logical.OR)
    @PostMapping("/add/bonusBudgetLadderters")
    public AjaxResult selectBonusBudgetLadderters(@RequestBody List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        BonusBudgetDTO bonusBudgetDTO = bonusBudgetService.selectBonusBudgetLadderters(bonusBudgetParametersDTOS);
        return AjaxResult.success(bonusBudgetDTO);
    }


    /**
     * 查询奖金预算表列表
     */
    @RequiresPermissions("operate:cloud:bonusBudget:pageList")
    @GetMapping("/list")
    public AjaxResult list(BonusBudgetDTO bonusBudgetDTO) {
        List<BonusBudgetDTO> list = bonusBudgetService.selectBonusBudgetList(bonusBudgetDTO);
        return AjaxResult.success(list);
    }


    /**
     * 查询已制定的总奖金预算(返回年份List)
     *
     * @param bonusBudgetDTO
     * @return
     */
    @RequiresPermissions("operate:cloud:bonusBudget:add")
    @GetMapping("/getExistYear")
    public AjaxResult getExistYear(BonusBudgetDTO bonusBudgetDTO) {
        List<String> listYears = new ArrayList<>();
        List<BonusBudgetDTO> list = bonusBudgetService.selectBonusBudgetList(bonusBudgetDTO);
        if (StringUtils.isNotEmpty(list)) {
            listYears = list.stream().filter(f -> f.getBudgetYear() != null).map(BonusBudgetDTO::getBudgetYear).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.toList());
        }
        return AjaxResult.success(listYears);
    }

}
