package net.qixiaowei.operate.cloud.controller.bonus;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.bonus.EmpAnnualBonusSnapshotDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.EmployeeAnnualBonusDTO;
import net.qixiaowei.operate.cloud.service.bonus.IEmployeeAnnualBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author TANGMICHI
 * @since 2022-12-02
 */
@RestController
@RequestMapping("employeeAnnualBonus")
public class EmployeeAnnualBonusController extends BaseController {


    @Autowired
    private IEmployeeAnnualBonusService employeeAnnualBonusService;


    /**
     * 分页查询个人年终奖表列表
     */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        startPage();
        List<EmployeeAnnualBonusDTO> list = employeeAnnualBonusService.selectEmployeeAnnualBonusList(employeeAnnualBonusDTO);
        return getDataTable(list);
    }

    /**
     * 保存个人年终奖表
     */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return AjaxResult.success(employeeAnnualBonusService.insertEmployeeAnnualBonus(employeeAnnualBonusDTO));
    }

    /**
     * 修改个人年终奖表
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:add")
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return AjaxResult.success(employeeAnnualBonusService.edit(employeeAnnualBonusDTO));
    }

    /**
     * 主管修改个人年终奖表
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:add")
    @PostMapping("/inChargeEdit")
    public AjaxResult inChargeEdit(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return AjaxResult.success(employeeAnnualBonusService.inChargeEdit(employeeAnnualBonusDTO));
    }
    /**
     * 团队修改个人年终奖表
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:add")
    @PostMapping("/teamEdit")
    public AjaxResult teamEdit(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return AjaxResult.success(employeeAnnualBonusService.teamEdit(employeeAnnualBonusDTO));
    }



    /**
     * 个人年终奖表选择部门后预制数据
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:list")
    @PostMapping("/addPrefabricate")
    public AjaxResult addPrefabricate(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        EmployeeAnnualBonusDTO employeeAnnualBonusDTO1 = employeeAnnualBonusService.addPrefabricate(employeeAnnualBonusDTO);
        return AjaxResult.success(employeeAnnualBonusDTO1);
    }

    /**
     * 实时查询个人年终奖表详情
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:info")
    @PostMapping("/realTimeDetails")
    public AjaxResult realTimeDetails(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        List<EmpAnnualBonusSnapshotDTO> list = employeeAnnualBonusService.realTimeDetails(employeeAnnualBonusDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询个人年终奖表详情
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:info")
    @GetMapping("/info/{employeeAnnualBonusId}")
    public AjaxResult info(@PathVariable Long employeeAnnualBonusId) {
        EmployeeAnnualBonusDTO employeeAnnualBonusDTO = employeeAnnualBonusService.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId);
        return AjaxResult.success(employeeAnnualBonusDTO);
    }

    /**
     * 查询个人年终奖表列表
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:list")
    @GetMapping("/list")
    public AjaxResult list(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        List<EmployeeAnnualBonusDTO> list = employeeAnnualBonusService.selectEmployeeAnnualBonusList(employeeAnnualBonusDTO);
        return AjaxResult.success(list);
    }

    /**
     * 逻辑删除个人年终奖表
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:remove")
    //@Log(title = "删除个人年终奖表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return toAjax(employeeAnnualBonusService.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusDTO));
    }

    /**
     * 批量修改个人年终奖表
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:edits")
    //@Log(title = "批量修改个人年终奖表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos) {
        return toAjax(employeeAnnualBonusService.updateEmployeeAnnualBonuss(employeeAnnualBonusDtos));
    }

    /**
     * 批量新增个人年终奖表
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:insertEmployeeAnnualBonuss")
    //@Log(title = "批量新增个人年终奖表", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmployeeAnnualBonuss")
    public AjaxResult insertEmployeeAnnualBonuss(@RequestBody List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos) {
        return toAjax(employeeAnnualBonusService.insertEmployeeAnnualBonuss(employeeAnnualBonusDtos));
    }

    /**
     * 逻辑批量删除个人年终奖表
     */
    //@RequiresPermissions("operate:cloud:employeeAnnualBonus:removes")
    //@Log(title = "批量删除个人年终奖表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> employeeAnnualBonusIds) {
        return toAjax(employeeAnnualBonusService.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(employeeAnnualBonusIds));
    }


}
