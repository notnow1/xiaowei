package net.qixiaowei.strategy.cloud.controller.industry;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.controller.BaseController;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.service.industry.IIndustryAttractionService;
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
* @since 2023-02-20
*/
@RestController
@RequestMapping("industryAttraction")
public class IndustryAttractionController extends BaseController
{


    @Autowired
    private IIndustryAttractionService industryAttractionService;



    /**
    * 查询行业吸引力表详情
    */
    @RequiresPermissions("strategy:cloud:industryAttraction:info")
    @GetMapping("/info/{industryAttractionId}")
    public AjaxResult info(@PathVariable Long industryAttractionId){
    IndustryAttractionDTO industryAttractionDTO = industryAttractionService.selectIndustryAttractionByIndustryAttractionId(industryAttractionId);
        return AjaxResult.success(industryAttractionDTO);
    }

    /**
    * 分页查询行业吸引力表列表
    */
    @RequiresPermissions("strategy:cloud:industryAttraction:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(IndustryAttractionDTO industryAttractionDTO){
    startPage();
    List<IndustryAttractionDTO> list = industryAttractionService.selectIndustryAttractionList(industryAttractionDTO);
    return getDataTable(list);
    }

    /**
    * 查询行业吸引力表列表
    */
    @RequiresPermissions("strategy:cloud:industryAttraction:list")
    @GetMapping("/list")
    public AjaxResult list(IndustryAttractionDTO industryAttractionDTO){
    List<IndustryAttractionDTO> list = industryAttractionService.selectIndustryAttractionList(industryAttractionDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增行业吸引力表
    */
    @RequiresPermissions("strategy:cloud:industryAttraction:add")
    @Log(title = "新增行业吸引力表", businessType = BusinessType.INDUSTRY_ATTRACTION, businessId = "industryAttractionId", operationType = OperationType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody IndustryAttractionDTO industryAttractionDTO) {
    return AjaxResult.success(industryAttractionService.insertIndustryAttraction(industryAttractionDTO));
    }


    /**
    * 修改行业吸引力表
    */
    @RequiresPermissions("strategy:cloud:industryAttraction:edit")
    @Log(title = "修改行业吸引力表", businessType = BusinessType.INDUSTRY_ATTRACTION, businessId = "industryAttractionId", operationType = OperationType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody IndustryAttractionDTO industryAttractionDTO)
    {
    return toAjax(industryAttractionService.updateIndustryAttraction(industryAttractionDTO));
    }

    /**
    * 逻辑删除行业吸引力表
    */
    @RequiresPermissions("strategy:cloud:industryAttraction:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody IndustryAttractionDTO industryAttractionDTO)
    {
    return toAjax(industryAttractionService.logicDeleteIndustryAttractionByIndustryAttractionId(industryAttractionDTO));
    }
    /**
    * 逻辑批量删除行业吸引力表
    */
    @RequiresPermissions("strategy:cloud:industryAttraction:removes")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  industryAttractionIds)
    {
    return toAjax(industryAttractionService.logicDeleteIndustryAttractionByIndustryAttractionIds(industryAttractionIds));
    }
}
