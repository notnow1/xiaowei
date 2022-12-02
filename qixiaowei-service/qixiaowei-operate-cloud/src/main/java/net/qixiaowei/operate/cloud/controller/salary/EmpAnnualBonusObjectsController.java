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
import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusObjectsDTO;

import net.qixiaowei.operate.cloud.service.salary.IEmpAnnualBonusObjectsService;
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
@RequestMapping("empAnnualBonusObjects")
public class EmpAnnualBonusObjectsController extends BaseController
{


    @Autowired
    private IEmpAnnualBonusObjectsService empAnnualBonusObjectsService;



    /**
    * 查询个人年终奖发放对象表详情
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:info")
    @GetMapping("/info/{empAnnualBonusObjectsId}")
    public AjaxResult info(@PathVariable Long empAnnualBonusObjectsId){
    EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO = empAnnualBonusObjectsService.selectEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(empAnnualBonusObjectsId);
        return AjaxResult.success(empAnnualBonusObjectsDTO);
    }

    /**
    * 分页查询个人年终奖发放对象表列表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO){
    startPage();
    List<EmpAnnualBonusObjectsDTO> list = empAnnualBonusObjectsService.selectEmpAnnualBonusObjectsList(empAnnualBonusObjectsDTO);
    return getDataTable(list);
    }

    /**
    * 查询个人年终奖发放对象表列表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:list")
    @GetMapping("/list")
    public AjaxResult list(EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO){
    List<EmpAnnualBonusObjectsDTO> list = empAnnualBonusObjectsService.selectEmpAnnualBonusObjectsList(empAnnualBonusObjectsDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增个人年终奖发放对象表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:add")
    @Log(title = "新增个人年终奖发放对象表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO) {
    return AjaxResult.success(empAnnualBonusObjectsService.insertEmpAnnualBonusObjects(empAnnualBonusObjectsDTO));
    }


    /**
    * 修改个人年终奖发放对象表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:edit")
    @Log(title = "修改个人年终奖发放对象表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO)
    {
    return toAjax(empAnnualBonusObjectsService.updateEmpAnnualBonusObjects(empAnnualBonusObjectsDTO));
    }

    /**
    * 逻辑删除个人年终奖发放对象表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:remove")
    @Log(title = "删除个人年终奖发放对象表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO)
    {
    return toAjax(empAnnualBonusObjectsService.logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsId(empAnnualBonusObjectsDTO));
    }
    /**
    * 批量修改个人年终奖发放对象表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:edits")
    @Log(title = "批量修改个人年终奖发放对象表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDtos)
    {
    return toAjax(empAnnualBonusObjectsService.updateEmpAnnualBonusObjectss(empAnnualBonusObjectsDtos));
    }

    /**
    * 批量新增个人年终奖发放对象表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:insertEmpAnnualBonusObjectss")
    @Log(title = "批量新增个人年终奖发放对象表", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmpAnnualBonusObjectss")
    public AjaxResult insertEmpAnnualBonusObjectss(@RequestBody List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDtos)
    {
    return toAjax(empAnnualBonusObjectsService.insertEmpAnnualBonusObjectss(empAnnualBonusObjectsDtos));
    }

    /**
    * 逻辑批量删除个人年终奖发放对象表
    */
    @RequiresPermissions("operate:cloud:empAnnualBonusObjects:removes")
    @Log(title = "批量删除个人年终奖发放对象表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  empAnnualBonusObjectsIds)
    {
    return toAjax(empAnnualBonusObjectsService.logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(empAnnualBonusObjectsIds));
    }


}
