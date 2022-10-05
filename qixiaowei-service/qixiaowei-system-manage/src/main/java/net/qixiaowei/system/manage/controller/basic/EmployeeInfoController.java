package net.qixiaowei.system.manage.controller.basic;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeInfoDTO;
import net.qixiaowei.system.manage.service.basic.IEmployeeInfoService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-09-30
*/
@RestController
@RequestMapping("employeeInfo")
public class EmployeeInfoController extends BaseController
{


    @Autowired
    private IEmployeeInfoService employeeInfoService;

    /**
    * 分页查询员工信息列表
    */
    @RequiresPermissions("system:manage:employeeInfo:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeInfoDTO employeeInfoDTO){
    startPage();
    List<EmployeeInfoDTO> list = employeeInfoService.selectEmployeeInfoList(employeeInfoDTO);
    return getDataTable(list);
    }

    /**
    * 查询员工信息列表
    */
    @RequiresPermissions("system:manage:employeeInfo:list")
    @GetMapping("/list")
    public AjaxResult list(EmployeeInfoDTO employeeInfoDTO){
    List<EmployeeInfoDTO> list = employeeInfoService.selectEmployeeInfoList(employeeInfoDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增员工信息
    */
    @RequiresPermissions("system:manage:employeeInfo:add")
    @Log(title = "新增员工信息", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody EmployeeInfoDTO employeeInfoDTO) {
    return toAjax(employeeInfoService.insertEmployeeInfo(employeeInfoDTO));
    }


    /**
    * 修改员工信息
    */
    @RequiresPermissions("system:manage:employeeInfo:edit")
    @Log(title = "修改员工信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody EmployeeInfoDTO employeeInfoDTO)
    {
    return toAjax(employeeInfoService.updateEmployeeInfo(employeeInfoDTO));
    }

    /**
    * 逻辑删除员工信息
    */
    @RequiresPermissions("system:manage:employeeInfo:remove")
    @Log(title = "删除员工信息", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmployeeInfoDTO employeeInfoDTO)
    {
    return toAjax(employeeInfoService.logicDeleteEmployeeInfoByEmployeeInfoId(employeeInfoDTO));
    }
    /**
    * 批量修改员工信息
    */
    @RequiresPermissions("system:manage:employeeInfo:edits")
    @Log(title = "批量修改员工信息", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<EmployeeInfoDTO> employeeInfoDtos)
    {
    return toAjax(employeeInfoService.updateEmployeeInfos(employeeInfoDtos));
    }

    /**
    * 批量新增员工信息
    */
    @RequiresPermissions("system:manage:employeeInfo:insertEmployeeInfos")
    @Log(title = "批量新增员工信息", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmployeeInfos")
    public AjaxResult insertEmployeeInfos(@RequestBody List<EmployeeInfoDTO> employeeInfoDtos)
    {
    return toAjax(employeeInfoService.insertEmployeeInfos(employeeInfoDtos));
    }

    /**
    * 逻辑批量删除员工信息
    */
    @RequiresPermissions("system:manage:employeeInfo:removes")
    @Log(title = "批量删除员工信息", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<EmployeeInfoDTO>  EmployeeInfoDtos)
    {
    return toAjax(employeeInfoService.logicDeleteEmployeeInfoByEmployeeInfoIds(EmployeeInfoDtos));
    }
}
