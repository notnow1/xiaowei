package net.qixiaowei.file.controller;

import net.qixiaowei.file.api.domain.File;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.file.FileUtils;
import net.qixiaowei.file.service.IFileService;

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
    public R<File> upload(MultipartFile file) {
        try {
            // 上传并返回访问地址
            String url = fileService.uploadFile(file);
            File sysFile = new File();
            sysFile.setName(FileUtils.getName(url));
            sysFile.setPath(url);
            return R.ok(sysFile);
        } catch (Exception e) {
            log.error("上传文件失败", e);
            return R.fail(e.getMessage());
        }
    }
}