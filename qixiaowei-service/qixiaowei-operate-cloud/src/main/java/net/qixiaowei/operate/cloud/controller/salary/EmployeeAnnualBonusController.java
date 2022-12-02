package net.qixiaowei.operate.cloud.controller.salary;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.operate.cloud.api.dto.salary.EmployeeAnnualBonusDTO;

import net.qixiaowei.operate.cloud.service.salary.IEmployeeAnnualBonusService;
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
* @since 2022-12-02
*/
@RestController
@RequestMapping("employeeAnnualBonus")
public class EmployeeAnnualBonusController extends BaseController
{


    @Autowired
    private IEmployeeAnnualBonusService employeeAnnualBonusService;



    /**
    * 查询个人年终奖表详情
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:info")
    @GetMapping("/info/{employeeAnnualBonusId}")
    public AjaxResult info(@PathVariable Long employeeAnnualBonusId){
    EmployeeAnnualBonusDTO employeeAnnualBonusDTO = employeeAnnualBonusService.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId);
        return AjaxResult.success(employeeAnnualBonusDTO);
    }

    /**
    * 分页查询个人年终奖表列表
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmployeeAnnualBonusDTO employeeAnnualBonusDTO){
    startPage();
    List<EmployeeAnnualBonusDTO> list = employeeAnnualBonusService.selectEmployeeAnnualBonusList(employeeAnnualBonusDTO);
    return getDataTable(list);
    }

    /**
    * 查询个人年终奖表列表
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:list")
    @GetMapping("/list")
    public AjaxResult list(EmployeeAnnualBonusDTO employeeAnnualBonusDTO){
    List<EmployeeAnnualBonusDTO> list = employeeAnnualBonusService.selectEmployeeAnnualBonusList(employeeAnnualBonusDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增个人年终奖表
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:add")
    @Log(title = "新增个人年终奖表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
    return AjaxResult.success(employeeAnnualBonusService.insertEmployeeAnnualBonus(employeeAnnualBonusDTO));
    }


    /**
    * 修改个人年终奖表
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:edit")
    @Log(title = "修改个人年终奖表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO)
    {
    return toAjax(employeeAnnualBonusService.updateEmployeeAnnualBonus(employeeAnnualBonusDTO));
    }

    /**
    * 逻辑删除个人年终奖表
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:remove")
    @Log(title = "删除个人年终奖表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmployeeAnnualBonusDTO employeeAnnualBonusDTO)
    {
    return toAjax(employeeAnnualBonusService.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusDTO));
    }
    /**
    * 批量修改个人年终奖表
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:edits")
    @Log(title = "批量修改个人年终奖表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos)
    {
    return toAjax(employeeAnnualBonusService.updateEmployeeAnnualBonuss(employeeAnnualBonusDtos));
    }

    /**
    * 批量新增个人年终奖表
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:insertEmployeeAnnualBonuss")
    @Log(title = "批量新增个人年终奖表", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmployeeAnnualBonuss")
    public AjaxResult insertEmployeeAnnualBonuss(@RequestBody List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos)
    {
    return toAjax(employeeAnnualBonusService.insertEmployeeAnnualBonuss(employeeAnnualBonusDtos));
    }

    /**
    * 逻辑批量删除个人年终奖表
    */
    @RequiresPermissions("operate:cloud:employeeAnnualBonus:removes")
    @Log(title = "批量删除个人年终奖表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  employeeAnnualBonusIds)
    {
    return toAjax(employeeAnnualBonusService.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(employeeAnnualBonusIds));
    }


}
