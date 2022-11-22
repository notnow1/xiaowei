package net.qixiaowei.operate.cloud.controller.employee;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.product.ProductUnitDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetExcel;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetImportListener;
import net.qixiaowei.operate.cloud.service.employee.IEmployeeBudgetService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;



/**
*
* @author TANGMICHI
* @since 2022-11-18
*/
@RestController
@RequestMapping("employeeBudget")
public class EmployeeBudgetController extends BaseController
{


    @Autowired
    private IEmployeeBudgetService employeeBudgetService;



    /**
    * 查询人力预算表详情
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:info")
    @GetMapping("/info/{employeeBudgetId}")
    public AjaxResult info(@PathVariable Long employeeBudgetId){
    EmployeeBudgetDTO employeeBudgetDTO = employeeBudgetService.selectEmployeeBudgetByEmployeeBudgetId(employeeBudgetId);
        return AjaxResult.success(employeeBudgetDTO);
    }

    /**
    * 分页查询人力预算表列表
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeBudgetDTO employeeBudgetDTO){
    startPage();
    List<EmployeeBudgetDTO> list = employeeBudgetService.selectEmployeeBudgetList(employeeBudgetDTO);
    return getDataTable(list);
    }

    /**
    * 查询人力预算表列表
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:list")
    @GetMapping("/list")
    public AjaxResult list(EmployeeBudgetDTO employeeBudgetDTO){
    List<EmployeeBudgetDTO> list = employeeBudgetService.selectEmployeeBudgetList(employeeBudgetDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增人力预算表
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:add")
    //@Log(title = "新增人力预算表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(EmployeeBudgetDTO.AddEmployeeBudgetDTO.class) EmployeeBudgetDTO employeeBudgetDTO) {
    return AjaxResult.success(employeeBudgetService.insertEmployeeBudget(employeeBudgetDTO));
    }


    /**
    * 修改人力预算表
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:edit")
    //@Log(title = "修改人力预算表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(EmployeeBudgetDTO.UpdateEmployeeBudgetDTO.class) EmployeeBudgetDTO employeeBudgetDTO)
    {
    return toAjax(employeeBudgetService.updateEmployeeBudget(employeeBudgetDTO));
    }

    /**
    * 逻辑删除人力预算表
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:remove")
    //@Log(title = "删除人力预算表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmployeeBudgetDTO employeeBudgetDTO)
    {
    return toAjax(employeeBudgetService.logicDeleteEmployeeBudgetByEmployeeBudgetId(employeeBudgetDTO));
    }
    /**
    * 批量修改人力预算表
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:edits")
    //@Log(title = "批量修改人力预算表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<EmployeeBudgetDTO> employeeBudgetDtos)
    {
    return toAjax(employeeBudgetService.updateEmployeeBudgets(employeeBudgetDtos));
    }

    /**
    * 批量新增人力预算表
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:insertEmployeeBudgets")
    //@Log(title = "批量新增人力预算表", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmployeeBudgets")
    public AjaxResult insertEmployeeBudgets(@RequestBody List<EmployeeBudgetDTO> employeeBudgetDtos)
    {
    return toAjax(employeeBudgetService.insertEmployeeBudgets(employeeBudgetDtos));
    }

    /**
    * 逻辑批量删除人力预算表
    */
    //@RequiresPermissions("operate:cloud:employeeBudget:removes")
    //@Log(title = "批量删除人力预算表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  employeeBudgetIds)
    {
    return toAjax(employeeBudgetService.logicDeleteEmployeeBudgetByEmployeeBudgetIds(employeeBudgetIds));
    }

    /**
    * 导入人力预算表
    */
    @PostMapping("import")
    public AjaxResult importEmployeeBudget(MultipartFile file) {
    String filename = file.getOriginalFilename();
    if (StringUtils.isBlank(filename)) {
    throw new RuntimeException("请上传文件!");
    }
    if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
    throw new RuntimeException("请上传正确的excel文件!");
    }
    InputStream inputStream;
    try {
    EmployeeBudgetImportListener importListener = new EmployeeBudgetImportListener(employeeBudgetService);
    inputStream = new BufferedInputStream(file.getInputStream());
    ExcelReaderBuilder builder = EasyExcel.read(inputStream, EmployeeBudgetExcel.class, importListener);
    builder.doReadAll();
    } catch (IOException e) {
    throw new ServiceException("导入人力预算表Excel失败");
    }
    return AjaxResult.success("操作成功");
    }

    /**
    * 导出人力预算表
    */
    @SneakyThrows
    @GetMapping("export")
    public void exportEmployeeBudget(@RequestParam Map<String, Object> employeeBudget,EmployeeBudgetDTO employeeBudgetDTO, HttpServletResponse response) {
    List<EmployeeBudgetExcel> employeeBudgetExcelList = employeeBudgetService.exportEmployeeBudget(employeeBudgetDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("人力预算表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
        , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), EmployeeBudgetExcel.class).sheet("人力预算表").doWrite(employeeBudgetExcelList);
        }
}
