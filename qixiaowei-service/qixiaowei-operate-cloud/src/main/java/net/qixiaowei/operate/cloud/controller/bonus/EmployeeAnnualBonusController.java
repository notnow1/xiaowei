package net.qixiaowei.operate.cloud.controller.bonus;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
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
    @Log(title = "新增个人年终奖生成", businessType = BusinessType.EMPLOYEE_ANNUAL_BONUS, businessId = "employeeAnnualBonusId", operationType = OperationType.INSERT)
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return AjaxResult.success(employeeAnnualBonusService.insertEmployeeAnnualBonus(employeeAnnualBonusDTO));
    }

    /**
     * 修改个人年终奖表
     */
    @Log(title = "保存个人年终奖生成", businessType = BusinessType.EMPLOYEE_ANNUAL_BONUS, businessId = "employeeAnnualBonusId", operationType = OperationType.UPDATE)
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:edit")
    @PostMapping("/edit")
    public AjaxResult edit(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return AjaxResult.success(employeeAnnualBonusService.edit(employeeAnnualBonusDTO));
    }

    /**
     * 查询个人年终奖表详情-注：权限前台控制，后端暂时不控制，因为详情在主管和团队待办中同时用到了
     *
     * @RequiresPermissions("operate:cloud:employeeAnnualBonus:info")
     */
    @GetMapping("/info/{employeeAnnualBonusId}")
    public AjaxResult info(@PathVariable Long employeeAnnualBonusId, @RequestParam(required = false) Integer inChargeTeamFlag) {
        EmployeeAnnualBonusDTO employeeAnnualBonusDTO = employeeAnnualBonusService.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId, inChargeTeamFlag);
        return AjaxResult.success(employeeAnnualBonusDTO);
    }

    /**
     * 主管修改个人年终奖表
     */
    @PostMapping("/inChargeEdit")
    public AjaxResult inChargeEdit(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return AjaxResult.success(employeeAnnualBonusService.inChargeEdit(employeeAnnualBonusDTO));
    }

    /**
     * 团队修改个人年终奖表
     */
    @PostMapping("/teamEdit")
    public AjaxResult teamEdit(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return AjaxResult.success(employeeAnnualBonusService.teamEdit(employeeAnnualBonusDTO));
    }

    /**
     * 逻辑删除个人年终奖表
     */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        return toAjax(employeeAnnualBonusService.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusDTO));
    }

    /**
     * 逻辑批量删除个人年终奖表
     */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> employeeAnnualBonusIds) {
        return toAjax(employeeAnnualBonusService.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(employeeAnnualBonusIds));
    }

    /**
     * 个人年终奖表选择部门后预制数据
     */
    @RequiresPermissions(value = {"operate:cloud:employeeAnnualBonus:add", "operate:cloud:employeeAnnualBonus:edit"}, logical = Logical.OR)
    @PostMapping("/addPrefabricate")
    public AjaxResult addPrefabricate(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        EmployeeAnnualBonusDTO employeeAnnualBonusDTO1 = employeeAnnualBonusService.addPrefabricate(employeeAnnualBonusDTO);
        return AjaxResult.success(employeeAnnualBonusDTO1);
    }

    /**
     * 实时查询个人年终奖表详情
     */
    @RequiresPermissions(value = {"operate:cloud:employeeAnnualBonus:add", "operate:cloud:employeeAnnualBonus:edit"}, logical = Logical.OR)
    @PostMapping("/realTimeDetails")
    public AjaxResult realTimeDetails(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        List<EmpAnnualBonusSnapshotDTO> list = employeeAnnualBonusService.realTimeDetails(employeeAnnualBonusDTO);
        return AjaxResult.success(list);
    }

    /**
     * 查询个人年终奖表列表
     */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:pageList")
    @GetMapping("/list")
    public AjaxResult list(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        List<EmployeeAnnualBonusDTO> list = employeeAnnualBonusService.selectEmployeeAnnualBonusList(employeeAnnualBonusDTO);
        return AjaxResult.success(list);
    }


}
