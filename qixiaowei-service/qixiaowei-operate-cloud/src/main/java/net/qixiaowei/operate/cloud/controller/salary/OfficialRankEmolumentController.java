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
import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import net.qixiaowei.operate.cloud.excel.salary.OfficialRankEmolumentExcel;
import net.qixiaowei.operate.cloud.excel.salary.OfficialRankEmolumentImportListener;
import net.qixiaowei.operate.cloud.service.salary.IOfficialRankEmolumentService;
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
* @author Graves
* @since 2022-11-30
*/
@RestController
@RequestMapping("officialRankEmolument")
public class OfficialRankEmolumentController extends BaseController
{


    @Autowired
    private IOfficialRankEmolumentService officialRankEmolumentService;



    /**
    * 查询职级薪酬表详情
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:info")
    @GetMapping("/info")
    public AjaxResult info(OfficialRankEmolumentDTO officialRankEmolumentDTO){
    return AjaxResult.success(officialRankEmolumentService.selectOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO));
    }

    /**
    * 分页查询职级薪酬表列表
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(OfficialRankEmolumentDTO officialRankEmolumentDTO){
    startPage();
    List<OfficialRankEmolumentDTO> list = officialRankEmolumentService.selectOfficialRankEmolumentList(officialRankEmolumentDTO);
    return getDataTable(list);
    }

    /**
    * 查询职级薪酬表列表
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:list")
    @GetMapping("/list")
    public AjaxResult list(OfficialRankEmolumentDTO officialRankEmolumentDTO){
    List<OfficialRankEmolumentDTO> list = officialRankEmolumentService.selectOfficialRankEmolumentList(officialRankEmolumentDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增职级薪酬表
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:add")
    @Log(title = "新增职级薪酬表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody OfficialRankEmolumentDTO officialRankEmolumentDTO) {
    return AjaxResult.success(officialRankEmolumentService.insertOfficialRankEmolument(officialRankEmolumentDTO));
    }


    /**
    * 修改职级薪酬表
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:edit")
    @Log(title = "修改职级薪酬表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody OfficialRankEmolumentDTO officialRankEmolumentDTO)
    {
    return toAjax(officialRankEmolumentService.updateOfficialRankEmolument(officialRankEmolumentDTO));
    }

    /**
    * 逻辑删除职级薪酬表
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:remove")
    @Log(title = "删除职级薪酬表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody OfficialRankEmolumentDTO officialRankEmolumentDTO)
    {
    return toAjax(officialRankEmolumentService.logicDeleteOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO));
    }
    /**
    * 批量修改职级薪酬表
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:edits")
    @Log(title = "批量修改职级薪酬表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<OfficialRankEmolumentDTO> officialRankEmolumentDtos)
    {
    return toAjax(officialRankEmolumentService.updateOfficialRankEmoluments(officialRankEmolumentDtos));
    }

    /**
    * 批量新增职级薪酬表
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:insertOfficialRankEmoluments")
    @Log(title = "批量新增职级薪酬表", businessType = BusinessType.INSERT)
    @PostMapping("/insertOfficialRankEmoluments")
    public AjaxResult insertOfficialRankEmoluments(@RequestBody List<OfficialRankEmolumentDTO> officialRankEmolumentDtos)
    {
    return toAjax(officialRankEmolumentService.insertOfficialRankEmoluments(officialRankEmolumentDtos));
    }

    /**
    * 逻辑批量删除职级薪酬表
    */
   // @RequiresPermissions("operate:cloud:officialRankEmolument:removes")
    @Log(title = "批量删除职级薪酬表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long>  officialRankEmolumentIds)
    {
    return toAjax(officialRankEmolumentService.logicDeleteOfficialRankEmolumentByOfficialRankEmolumentIds(officialRankEmolumentIds));
    }

    /**
    * 导入职级薪酬表
    */
    @PostMapping("import")
    public AjaxResult importOfficialRankEmolument(MultipartFile file) {
    String filename = file.getOriginalFilename();
    if (StringUtils.isBlank(filename)) {
    throw new RuntimeException("请上传文件!");
    }
    if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
    throw new RuntimeException("请上传正确的excel文件!");
    }
    InputStream inputStream;
    try {
    OfficialRankEmolumentImportListener importListener = new OfficialRankEmolumentImportListener(officialRankEmolumentService);
    inputStream = new BufferedInputStream(file.getInputStream());
    ExcelReaderBuilder builder = EasyExcel.read(inputStream, OfficialRankEmolumentExcel.class, importListener);
    builder.doReadAll();
    } catch (IOException e) {
    throw new ServiceException("导入职级薪酬表Excel失败");
    }
    return AjaxResult.success("操作成功");
    }

    /**
    * 导出职级薪酬表
    */
    @SneakyThrows
    @GetMapping("export")
    public void exportOfficialRankEmolument(@RequestParam Map<String, Object> officialRankEmolument,OfficialRankEmolumentDTO officialRankEmolumentDTO, HttpServletResponse response) {
    List<OfficialRankEmolumentExcel> officialRankEmolumentExcelList = officialRankEmolumentService.exportOfficialRankEmolument(officialRankEmolumentDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("职级薪酬表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
        , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), OfficialRankEmolumentExcel.class).sheet("职级薪酬表").doWrite(officialRankEmolumentExcelList);
        }
}
