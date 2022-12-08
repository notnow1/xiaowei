package net.qixiaowei.operate.cloud.controller.bonus;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusSnapshotDTO;

import net.qixiaowei.operate.cloud.service.bonus.IEmpAnnualBonusSnapshotService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
*
* @author TANGMICHI
* @since 2022-12-02
*/
@RestController
@RequestMapping("empAnnualBonusSnapshot")
public class EmpAnnualBonusSnapshotController extends BaseController
{


    @Autowired
    private IEmpAnnualBonusSnapshotService empAnnualBonusSnapshotService;



    /**
    * 查询个人年终奖发放快照信息表详情
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:info")
    @GetMapping("/info/{empAnnualBonusSnapshotId}")
    public AjaxResult info(@PathVariable Long empAnnualBonusSnapshotId){
    EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO = empAnnualBonusSnapshotService.selectEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(empAnnualBonusSnapshotId);
        return AjaxResult.success(empAnnualBonusSnapshotDTO);
    }

    /**
    * 分页查询个人年终奖发放快照信息表列表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO){
    startPage();
    List<EmpAnnualBonusSnapshotDTO> list = empAnnualBonusSnapshotService.selectEmpAnnualBonusSnapshotList(empAnnualBonusSnapshotDTO);
    return getDataTable(list);
    }

    /**
    * 查询个人年终奖发放快照信息表列表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:list")
    @GetMapping("/list")
    public AjaxResult list(EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO){
    List<EmpAnnualBonusSnapshotDTO> list = empAnnualBonusSnapshotService.selectEmpAnnualBonusSnapshotList(empAnnualBonusSnapshotDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增个人年终奖发放快照信息表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:add")
    @Log(title = "新增个人年终奖发放快照信息表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO) {
    return AjaxResult.success(empAnnualBonusSnapshotService.insertEmpAnnualBonusSnapshot(empAnnualBonusSnapshotDTO));
    }


    /**
    * 修改个人年终奖发放快照信息表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:edit")
    @Log(title = "修改个人年终奖发放快照信息表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO)
    {
    return toAjax(empAnnualBonusSnapshotService.updateEmpAnnualBonusSnapshot(empAnnualBonusSnapshotDTO));
    }

    /**
    * 逻辑删除个人年终奖发放快照信息表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:remove")
    @Log(title = "删除个人年终奖发放快照信息表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO)
    {
    return toAjax(empAnnualBonusSnapshotService.logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotId(empAnnualBonusSnapshotDTO));
    }
    /**
    * 批量修改个人年终奖发放快照信息表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:edits")
    @Log(title = "批量修改个人年终奖发放快照信息表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDtos)
    {
    return toAjax(empAnnualBonusSnapshotService.updateEmpAnnualBonusSnapshots(empAnnualBonusSnapshotDtos));
    }

    /**
    * 批量新增个人年终奖发放快照信息表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:insertEmpAnnualBonusSnapshots")
    @Log(title = "批量新增个人年终奖发放快照信息表", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmpAnnualBonusSnapshots")
    public AjaxResult insertEmpAnnualBonusSnapshots(@RequestBody List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDtos)
    {
    return toAjax(empAnnualBonusSnapshotService.insertEmpAnnualBonusSnapshots(empAnnualBonusSnapshotDtos));
    }

    /**
    * 逻辑批量删除个人年终奖发放快照信息表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusSnapshot:removes")
    @Log(title = "批量删除个人年终奖发放快照信息表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  empAnnualBonusSnapshotIds)
    {
    return toAjax(empAnnualBonusSnapshotService.logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(empAnnualBonusSnapshotIds));
    }

}
