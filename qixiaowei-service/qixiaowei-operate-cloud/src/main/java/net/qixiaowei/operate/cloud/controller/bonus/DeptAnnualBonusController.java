package net.qixiaowei.operate.cloud.controller.bonus;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.bonus.DeptAnnualBonusDTO;
import net.qixiaowei.operate.cloud.service.bonus.IDeptAnnualBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author TANGMICHI
 * @since 2022-12-06
 */
@RestController
@RequestMapping("deptAnnualBonus")
public class DeptAnnualBonusController extends BaseController {


    @Autowired
    private IDeptAnnualBonusService deptAnnualBonusService;


    /**
     * 分页查询部门年终奖表列表
     */
    @RequiresPermissions("operate:cloud:deptAnnualBonus:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DeptAnnualBonusDTO deptAnnualBonusDTO) {
        startPage();
        List<DeptAnnualBonusDTO> list = deptAnnualBonusService.selectDeptAnnualBonusList(deptAnnualBonusDTO);
        return getDataTable(list);
    }

    /**
     * 新增部门年终奖表
     */
    @RequiresPermissions("operate:cloud:deptAnnualBonus:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DeptAnnualBonusDTO deptAnnualBonusDTO) {
        return AjaxResult.success(deptAnnualBonusService.insertDeptAnnualBonus(deptAnnualBonusDTO));
    }

    /**
     * 修改部门年终奖表
     */
    @RequiresPermissions("operate:cloud:deptAnnualBonus:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DeptAnnualBonusDTO deptAnnualBonusDTO) {
        return toAjax(deptAnnualBonusService.updateDeptAnnualBonus(deptAnnualBonusDTO));
    }

    /**
     * 查询部门年终奖表详情
     */
    @RequiresPermissions("operate:cloud:deptAnnualBonus:info")
    @GetMapping("/info/{deptAnnualBonusId}")
    public AjaxResult info(@PathVariable Long deptAnnualBonusId) {
        DeptAnnualBonusDTO deptAnnualBonusDTO = deptAnnualBonusService.selectDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusId);
        return AjaxResult.success(deptAnnualBonusDTO);
    }

    /**
     * 逻辑删除部门年终奖表
     */
    @RequiresPermissions("operate:cloud:deptAnnualBonus:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DeptAnnualBonusDTO deptAnnualBonusDTO) {
        return toAjax(deptAnnualBonusService.logicDeleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusDTO));
    }

    /**
     * 逻辑批量删除部门年终奖表
     */
    @RequiresPermissions("operate:cloud:deptAnnualBonus:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> deptAnnualBonusIds) {
        return toAjax(deptAnnualBonusService.logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(deptAnnualBonusIds));
    }

    /**
     * 部门年终奖预制数据
     */
   @RequiresPermissions(value = {"operate:cloud:deptAnnualBonus:add", "operate:cloud:deptAnnualBonus:edit"}, logical = Logical.OR)
    @GetMapping("/addPrefabricate/{annualBonusYear}")
    public AjaxResult addPrefabricate(@PathVariable int annualBonusYear) {
        DeptAnnualBonusDTO deptAnnualBonusDTO = deptAnnualBonusService.addPrefabricate(annualBonusYear);
        return AjaxResult.success(deptAnnualBonusDTO);
    }

    /**
     * 查询部门年终奖表列表
     */
    @RequiresPermissions("operate:cloud:deptAnnualBonus:pageList")
    @GetMapping("/list")
    public AjaxResult list(DeptAnnualBonusDTO deptAnnualBonusDTO) {
        List<DeptAnnualBonusDTO> list = deptAnnualBonusService.selectDeptAnnualBonusList(deptAnnualBonusDTO);
        return AjaxResult.success(list);
    }

}
