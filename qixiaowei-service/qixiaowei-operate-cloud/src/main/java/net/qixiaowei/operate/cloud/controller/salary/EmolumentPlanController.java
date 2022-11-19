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
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;
import net.qixiaowei.operate.cloud.service.salary.IEmolumentPlanService;
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
@RequestMapping("emolumentPlan")
public class EmolumentPlanController extends BaseController
{


    @Autowired
    private IEmolumentPlanService emolumentPlanService;


    /**
     * 新增薪酬规划时预制数据
     */
    //@RequiresPermissions("operate:cloud:emolumentPlan:info")
    @GetMapping("/add/{planYear}")
    public AjaxResult prefabricateAddEmolumentPlan(@PathVariable int planYear){
        EmolumentPlanDTO emolumentPlanDTO = emolumentPlanService.prefabricateAddEmolumentPlan(planYear);
        return AjaxResult.success(emolumentPlanDTO);
    }

    /**
    * 查询薪酬规划表详情
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:info")
    @GetMapping("/info/{emolumentPlanId}")
    public AjaxResult info(@PathVariable Long emolumentPlanId){
    EmolumentPlanDTO emolumentPlanDTO = emolumentPlanService.selectEmolumentPlanByEmolumentPlanId(emolumentPlanId);
        return AjaxResult.success(emolumentPlanDTO);
    }

    /**
    * 分页查询薪酬规划表列表
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(EmolumentPlanDTO emolumentPlanDTO){
    startPage();
    List<EmolumentPlanDTO> list = emolumentPlanService.selectEmolumentPlanList(emolumentPlanDTO);
    return getDataTable(list);
    }

    /**
    * 查询薪酬规划表列表
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:list")
    @GetMapping("/list")
    public AjaxResult list(EmolumentPlanDTO emolumentPlanDTO){
    List<EmolumentPlanDTO> list = emolumentPlanService.selectEmolumentPlanList(emolumentPlanDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增薪酬规划表
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:add")
    @Log(title = "新增薪酬规划表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody EmolumentPlanDTO emolumentPlanDTO) {
    return AjaxResult.success(emolumentPlanService.insertEmolumentPlan(emolumentPlanDTO));
    }


    /**
    * 修改薪酬规划表
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:edit")
    @Log(title = "修改薪酬规划表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody EmolumentPlanDTO emolumentPlanDTO)
    {
    return toAjax(emolumentPlanService.updateEmolumentPlan(emolumentPlanDTO));
    }

    /**
    * 逻辑删除薪酬规划表
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:remove")
    @Log(title = "删除薪酬规划表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody EmolumentPlanDTO emolumentPlanDTO)
    {
    return toAjax(emolumentPlanService.logicDeleteEmolumentPlanByEmolumentPlanId(emolumentPlanDTO));
    }
    /**
    * 批量修改薪酬规划表
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:edits")
    @Log(title = "批量修改薪酬规划表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<EmolumentPlanDTO> emolumentPlanDtos)
    {
    return toAjax(emolumentPlanService.updateEmolumentPlans(emolumentPlanDtos));
    }

    /**
    * 批量新增薪酬规划表
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:insertEmolumentPlans")
    @Log(title = "批量新增薪酬规划表", businessType = BusinessType.INSERT)
    @PostMapping("/insertEmolumentPlans")
    public AjaxResult insertEmolumentPlans(@RequestBody List<EmolumentPlanDTO> emolumentPlanDtos)
    {
    return toAjax(emolumentPlanService.insertEmolumentPlans(emolumentPlanDtos));
    }

    /**
    * 逻辑批量删除薪酬规划表
    */
    //@RequiresPermissions("operate:cloud:emolumentPlan:removes")
    @Log(title = "批量删除薪酬规划表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  emolumentPlanIds)
    {
    return toAjax(emolumentPlanService.logicDeleteEmolumentPlanByEmolumentPlanIds(emolumentPlanIds));
    }

}
