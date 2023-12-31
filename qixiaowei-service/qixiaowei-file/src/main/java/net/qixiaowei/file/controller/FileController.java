package net.qixiaowei.file.controller;

import net.qixiaowei.file.api.dto.FileDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.file.service.IFileService;

import java.io.InputStream;

/**
 * 文件请求处理
 */
@RestController
public class FileController {
    private static final Logger log = LoggerFactory.getLogger(FileController.class);

    @Autowired
    private IFileService fileService;

    /**
     * 文件上传请求
     */
    @PostMapping("upload")
    public R<FileDTO> upload(@RequestParam("file") MultipartFile file, @RequestHeader(name = "source", required = false, defaultValue = "common") String source) {
        try {
            return R.ok(fileService.uploadFile(file, source));
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }

    /**
     * 下载文件
     */
    @GetMapping("/download/{filePath}")
    public R<InputStream> download(@RequestParam(value = "filePath") String filePath) {
        return R.ok(fileService.downloadFile(filePath));
    }

}