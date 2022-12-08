package net.qixiaowei.operate.cloud.controller.bonus;

import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptAnnualBonusDTO;
import net.qixiaowei.operate.cloud.service.bonus.IDeptAnnualBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;



/**
*
* @author TANGMICHI
* @since 2022-12-06
*/
@RestController
@RequestMapping("deptAnnualBonus")
public class DeptAnnualBonusController extends BaseController
{


    @Autowired
    private IDeptAnnualBonusService deptAnnualBonusService;



    /**
     * 部门年终奖预制数据
     */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:info")
    @GetMapping("/addPrefabricate/{annualBonusYear}")
    public AjaxResult addPrefabricate(@PathVariable int annualBonusYear){
        DeptAnnualBonusDTO deptAnnualBonusDTO = deptAnnualBonusService.addPrefabricate(annualBonusYear);
        return AjaxResult.success(deptAnnualBonusDTO);
    }
    /**
    * 查询部门年终奖表详情
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:info")
    @GetMapping("/info/{deptAnnualBonusId}")
    public AjaxResult info(@PathVariable Long deptAnnualBonusId){
    DeptAnnualBonusDTO deptAnnualBonusDTO = deptAnnualBonusService.selectDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusId);
        return AjaxResult.success(deptAnnualBonusDTO);
    }

    /**
    * 分页查询部门年终奖表列表
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DeptAnnualBonusDTO deptAnnualBonusDTO){
    startPage();
    List<DeptAnnualBonusDTO> list = deptAnnualBonusService.selectDeptAnnualBonusList(deptAnnualBonusDTO);
    return getDataTable(list);
    }

    /**
    * 查询部门年终奖表列表
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:list")
    @GetMapping("/list")
    public AjaxResult list(DeptAnnualBonusDTO deptAnnualBonusDTO){
    List<DeptAnnualBonusDTO> list = deptAnnualBonusService.selectDeptAnnualBonusList(deptAnnualBonusDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增部门年终奖表
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:add")
    @Log(title = "新增部门年终奖表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DeptAnnualBonusDTO deptAnnualBonusDTO) {
    return AjaxResult.success(deptAnnualBonusService.insertDeptAnnualBonus(deptAnnualBonusDTO));
    }


    /**
    * 修改部门年终奖表
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:edit")
    @Log(title = "修改部门年终奖表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DeptAnnualBonusDTO deptAnnualBonusDTO)
    {
    return toAjax(deptAnnualBonusService.updateDeptAnnualBonus(deptAnnualBonusDTO));
    }

    /**
    * 逻辑删除部门年终奖表
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:remove")
    @Log(title = "删除部门年终奖表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DeptAnnualBonusDTO deptAnnualBonusDTO)
    {
    return toAjax(deptAnnualBonusService.logicDeleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusDTO));
    }
    /**
    * 批量修改部门年终奖表
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:edits")
    @Log(title = "批量修改部门年终奖表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<DeptAnnualBonusDTO> deptAnnualBonusDtos)
    {
    return toAjax(deptAnnualBonusService.updateDeptAnnualBonuss(deptAnnualBonusDtos));
    }

    /**
    * 批量新增部门年终奖表
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:insertDeptAnnualBonuss")
    @Log(title = "批量新增部门年终奖表", businessType = BusinessType.INSERT)
    @PostMapping("/insertDeptAnnualBonuss")
    public AjaxResult insertDeptAnnualBonuss(@RequestBody List<DeptAnnualBonusDTO> deptAnnualBonusDtos)
    {
    return toAjax(deptAnnualBonusService.insertDeptAnnualBonuss(deptAnnualBonusDtos));
    }

    /**
    * 逻辑批量删除部门年终奖表
    */
    //@RequiresPermissions("operate:cloud:deptAnnualBonus:removes")
    @Log(title = "批量删除部门年终奖表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  deptAnnualBonusIds)
    {
    return toAjax(deptAnnualBonusService.logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(deptAnnualBonusIds));
    }
}
