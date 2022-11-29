package net.qixiaowei.operate.cloud.controller.salary;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetItemsDTO;
import net.qixiaowei.operate.cloud.service.salary.IDeptBonusBudgetItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Map;



/**
*
* @author TANGMICHI
* @since 2022-11-29
*/
@RestController
@RequestMapping("deptBonusBudgetItems")
public class DeptBonusBudgetItemsController extends BaseController
{


    @Autowired
    private IDeptBonusBudgetItemsService deptBonusBudgetItemsService;



    /**
    * 查询部门奖金预算项目表详情
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:info")
    @GetMapping("/info/{deptBonusBudgetItemsId}")
    public AjaxResult info(@PathVariable Long deptBonusBudgetItemsId){
    DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO = deptBonusBudgetItemsService.selectDeptBonusBudgetItemsByDeptBonusBudgetItemsId(deptBonusBudgetItemsId);
        return AjaxResult.success(deptBonusBudgetItemsDTO);
    }

    /**
    * 分页查询部门奖金预算项目表列表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO){
    startPage();
    List<DeptBonusBudgetItemsDTO> list = deptBonusBudgetItemsService.selectDeptBonusBudgetItemsList(deptBonusBudgetItemsDTO);
    return getDataTable(list);
    }

    /**
    * 查询部门奖金预算项目表列表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:list")
    @GetMapping("/list")
    public AjaxResult list(DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO){
    List<DeptBonusBudgetItemsDTO> list = deptBonusBudgetItemsService.selectDeptBonusBudgetItemsList(deptBonusBudgetItemsDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增部门奖金预算项目表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:add")
    @Log(title = "新增部门奖金预算项目表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO) {
    return AjaxResult.success(deptBonusBudgetItemsService.insertDeptBonusBudgetItems(deptBonusBudgetItemsDTO));
    }


    /**
    * 修改部门奖金预算项目表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:edit")
    @Log(title = "修改部门奖金预算项目表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO)
    {
    return toAjax(deptBonusBudgetItemsService.updateDeptBonusBudgetItems(deptBonusBudgetItemsDTO));
    }

    /**
    * 逻辑删除部门奖金预算项目表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:remove")
    @Log(title = "删除部门奖金预算项目表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO)
    {
    return toAjax(deptBonusBudgetItemsService.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsId(deptBonusBudgetItemsDTO));
    }
    /**
    * 批量修改部门奖金预算项目表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:edits")
    @Log(title = "批量修改部门奖金预算项目表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDtos)
    {
    return toAjax(deptBonusBudgetItemsService.updateDeptBonusBudgetItemss(deptBonusBudgetItemsDtos));
    }

    /**
    * 批量新增部门奖金预算项目表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:insertDeptBonusBudgetItemss")
    @Log(title = "批量新增部门奖金预算项目表", businessType = BusinessType.INSERT)
    @PostMapping("/insertDeptBonusBudgetItemss")
    public AjaxResult insertDeptBonusBudgetItemss(@RequestBody List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDtos)
    {
    return toAjax(deptBonusBudgetItemsService.insertDeptBonusBudgetItemss(deptBonusBudgetItemsDtos));
    }

    /**
    * 逻辑批量删除部门奖金预算项目表
    */
    @RequiresPermissions("operate:cloud:deptBonusBudgetItems:removes")
    @Log(title = "批量删除部门奖金预算项目表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  deptBonusBudgetItemsIds)
    {
    return toAjax(deptBonusBudgetItemsService.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(deptBonusBudgetItemsIds));
    }


}
