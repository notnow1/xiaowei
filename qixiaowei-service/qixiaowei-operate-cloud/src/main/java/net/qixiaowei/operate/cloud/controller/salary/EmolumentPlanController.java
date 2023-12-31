package net.qixiaowei.operate.cloud.controller.salary;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;
import net.qixiaowei.operate.cloud.service.salary.IEmolumentPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * @author TANGMICHI
 * @since 2022-11-18
 */
@RestController
@RequestMapping("emolumentPlan")
public class EmolumentPlanController extends BaseController {


    @Autowired
    private IEmolumentPlanService emolumentPlanService;


    /**
     * 分页查询薪酬规划表列表
     */
    @RequiresPermissions("operate:cloud:emolumentPlan:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmolumentPlanDTO emolumentPlanDTO) {
        startPage();
        List<EmolumentPlanDTO> list = emolumentPlanService.selectEmolumentPlanList(emolumentPlanDTO);
        return getDataTable(list);
    }

    /**
     * 新增薪酬规划表
     */
    @Log(title = "新增薪酬规划", businessType = BusinessType.EMOLUMENT_PLAN, businessId = "emolumentPlanId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:emolumentPlan:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(EmolumentPlanDTO.AddEmolumentPlanDTO.class) EmolumentPlanDTO emolumentPlanDTO) {
        return AjaxResult.success(emolumentPlanService.insertEmolumentPlan(emolumentPlanDTO));
    }

    /**
     * 修改薪酬规划表
     */
    @Log(title = "保存薪酬规划", businessType = BusinessType.EMOLUMENT_PLAN, businessId = "emolumentPlanId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:emolumentPlan:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody EmolumentPlanDTO emolumentPlanDTO) {
        return toAjax(emolumentPlanService.updateEmolumentPlan(emolumentPlanDTO));
    }

    /**
     * 查询薪酬规划表详情
     */
    @RequiresPermissions(value = {"operate:cloud:emolumentPlan:info", "operate:cloud:emolumentPlan:edit"}, logical = Logical.OR)
    @GetMapping("/info/{emolumentPlanId}")
    public AjaxResult info(@PathVariable Long emolumentPlanId) {
        EmolumentPlanDTO emolumentPlanDTO = emolumentPlanService.selectEmolumentPlanByEmolumentPlanId(emolumentPlanId);
        return AjaxResult.success(emolumentPlanDTO);
    }

    /**
     * 逻辑删除薪酬规划表
     */
    @RequiresPermissions("operate:cloud:emolumentPlan:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(EmolumentPlanDTO.DeleteEmolumentPlanDTO.class) EmolumentPlanDTO emolumentPlanDTO) {
        return toAjax(emolumentPlanService.logicDeleteEmolumentPlanByEmolumentPlanId(emolumentPlanDTO));
    }

    /**
     * 逻辑批量删除薪酬规划表
     */
    @RequiresPermissions("operate:cloud:emolumentPlan:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> emolumentPlanIds) {
        return toAjax(emolumentPlanService.logicDeleteEmolumentPlanByEmolumentPlanIds(emolumentPlanIds));
    }

    /**
     * 返回最大年份
     */
    @RequiresPermissions(value = {"operate:cloud:emolumentPlan:add", "operate:cloud:emolumentPlan:edit"}, logical = Logical.OR)
    @GetMapping("/queryLatelyBudgetYear")
    public AjaxResult queryLatelyBudgetYear() {
        int planYear = emolumentPlanService.queryLatelyBudgetYear();
        return AjaxResult.success(planYear);
    }

    /**
     * 新增薪酬规划时预制数据
     */
    @RequiresPermissions(value = {"operate:cloud:emolumentPlan:add", "operate:cloud:emolumentPlan:edit"}, logical = Logical.OR)
    @GetMapping("/add/{planYear}")
    public AjaxResult prefabricateAddEmolumentPlan(@PathVariable int planYear) {
        EmolumentPlanDTO emolumentPlanDTO = emolumentPlanService.prefabricateAddEmolumentPlan(planYear);
        if (StringUtils.isNull(emolumentPlanDTO)) {
            return AjaxResult.success(new EmolumentPlanDTO());
        }
        return AjaxResult.success(emolumentPlanDTO);
    }

    /**
     * 查询薪酬规划表列表
     */
    @GetMapping("/list")
    public AjaxResult list(EmolumentPlanDTO emolumentPlanDTO) {
        List<EmolumentPlanDTO> list = emolumentPlanService.selectEmolumentPlanList(emolumentPlanDTO);
        return AjaxResult.success(list);
    }


    /**
     * 查询已制定的薪酬规划(返回年份List)
     * @param emolumentPlanDTO
     * @return
     */
    @RequiresPermissions("operate:cloud:emolumentPlan:add")
    @GetMapping("/getExistYear")
    public AjaxResult getExistYear(EmolumentPlanDTO emolumentPlanDTO) {
        List<String> listYears = new ArrayList<>();
        List<EmolumentPlanDTO> list = emolumentPlanService.selectEmolumentPlanList(emolumentPlanDTO);
        if (StringUtils.isNotEmpty(list)){
            listYears = list.stream().filter(f -> f.getPlanYear() != null).map(EmolumentPlanDTO::getPlanYear).collect(Collectors.toList()).stream().map(String::valueOf).collect(Collectors.toList());
        }
        return AjaxResult.success(listYears);
    }
}
