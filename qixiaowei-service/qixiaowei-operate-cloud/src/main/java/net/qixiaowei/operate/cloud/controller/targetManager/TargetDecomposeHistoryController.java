package net.qixiaowei.operate.cloud.controller.targetManager;

import java.util.List;

import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import lombok.SneakyThrows;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.CharsetKit;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeHistoryDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeHistoryExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeHistoryImportListener;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeHistoryService;
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
 * @author TANGMICHI
 * @since 2022-10-31
 */
@RestController
@RequestMapping("targetDecomposeHistory")
public class TargetDecomposeHistoryController extends BaseController {


    @Autowired
    private ITargetDecomposeHistoryService targetDecomposeHistoryService;


    //==============================滚动预测管理==================================//

    /**
     * 根据目标分解id查询目标分解历史版本表详情
     */
    @RequiresPermissions("operate:cloud:targetDecomposeHistory:info")
    @GetMapping("/infodecompose/{targetDecomposeId}")
    public AjaxResult targetDecomposeIdInfo(@PathVariable Long targetDecomposeId) {
        return AjaxResult.success(targetDecomposeHistoryService.targetDecomposeIdInfo(targetDecomposeId));
    }

    //==============================其他==================================//
    /**
     * 查询目标分解历史版本表详情
     */
    //@RequiresPermissions("operate:cloud:targetDecomposeHistory:info")
    @GetMapping("/infoHistordecomposey/{targetDecomposeHistoryId}")
    public AjaxResult info(@PathVariable Long targetDecomposeHistoryId) {
        TargetDecomposeHistoryDTO targetDecomposeHistoryDTO = targetDecomposeHistoryService.selectTargetDecomposeHistoryByTargetDecomposeHistoryId(targetDecomposeHistoryId);
        return AjaxResult.success(targetDecomposeHistoryDTO);
    }

    /**
     * 分页查询目标分解历史版本表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecomposeHistory:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        startPage();
        List<TargetDecomposeHistoryDTO> list = targetDecomposeHistoryService.selectTargetDecomposeHistoryList(targetDecomposeHistoryDTO);
        return getDataTable(list);
    }

    /**
     * 查询目标分解历史版本表列表
     */
    //@RequiresPermissions("operate:cloud:targetDecomposeHistory:list")
    @GetMapping("/list")
    public AjaxResult list(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        List<TargetDecomposeHistoryDTO> list = targetDecomposeHistoryService.selectTargetDecomposeHistoryList(targetDecomposeHistoryDTO);
        return AjaxResult.success(list);
    }

    /**
     * 导入目标分解历史版本表
     */
    @PostMapping("import")
    public AjaxResult importTargetDecomposeHistory(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (StringUtils.isBlank(filename)) {
            throw new RuntimeException("请上传文件!");
        }
        if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
            throw new RuntimeException("请上传正确的excel文件!");
        }
        InputStream inputStream;
        try {
            TargetDecomposeHistoryImportListener importListener = new TargetDecomposeHistoryImportListener(targetDecomposeHistoryService);
            inputStream = new BufferedInputStream(file.getInputStream());
            ExcelReaderBuilder builder = EasyExcel.read(inputStream, TargetDecomposeHistoryExcel.class, importListener);
            builder.doReadAll();
        } catch (IOException e) {
            throw new ServiceException("导入目标分解历史版本表Excel失败");
        }
        return AjaxResult.success("操作成功");
    }

    /**
     * 导出目标分解历史版本表
     */
    @SneakyThrows
    @GetMapping("export")
    public void exportTargetDecomposeHistory(@RequestParam Map<String, Object> targetDecomposeHistory, TargetDecomposeHistoryDTO targetDecomposeHistoryDTO, HttpServletResponse response) {
        List<TargetDecomposeHistoryExcel> targetDecomposeHistoryExcelList = targetDecomposeHistoryService.exportTargetDecomposeHistory(targetDecomposeHistoryDTO);
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding(CharsetKit.UTF_8);
        String fileName = URLEncoder.encode("目标分解历史版本表" + new SimpleDateFormat("yyyyMMdd").format(new Date()) + Math.round((Math.random() + 1) * 1000)
                , CharsetKit.UTF_8);
        response.setHeader("Content-disposition", "attachment;filename=" + fileName + ".xlsx");
        EasyExcel.write(response.getOutputStream(), TargetDecomposeHistoryExcel.class).sheet("目标分解历史版本表").doWrite(targetDecomposeHistoryExcelList);
    }
}
