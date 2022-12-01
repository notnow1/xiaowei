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
 * @author Graves
 * @since 2022-11-30
 */
@RestController
@RequestMapping("officialRankEmolument")
public class OfficialRankEmolumentController extends BaseController {


    @Autowired
    private IOfficialRankEmolumentService officialRankEmolumentService;


    /**
     * 查询职级薪酬表详情
     */
    // @RequiresPermissions("operate:cloud:officialRankEmolument:info")
    @GetMapping("/info")
    public AjaxResult info(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return AjaxResult.success(officialRankEmolumentService.selectOfficialRankEmolumentByOfficialRankEmolumentId(officialRankEmolumentDTO));
    }

    /**
     * 查看该职级的分解信息
     */
    // @RequiresPermissions("operate:cloud:officialRankEmolument:list")
    @GetMapping("/decomposeInfo")
    public AjaxResult list(OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return AjaxResult.success(officialRankEmolumentService.selectOfficialDecomposeList(officialRankEmolumentDTO));
    }

    /**
     * 修改职级薪酬表
     */
    // @RequiresPermissions("operate:cloud:officialRankEmolument:edit")
    @Log(title = "修改职级薪酬表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody OfficialRankEmolumentDTO officialRankEmolumentDTO) {
        return toAjax(officialRankEmolumentService.updateOfficialRankEmolument(officialRankEmolumentDTO));
    }

}
