package net.qixiaowei.file.remote;

import net.qixiaowei.file.api.RemoteFileService;
import net.qixiaowei.file.api.dto.FileDTO;
import net.qixiaowei.file.service.IFileService;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * @Author hzk
 * @Date 2022-10-28 13:52
 **/
@RestController
@RequestMapping("/file")
public class RemoteFile implements RemoteFileService {

    @Autowired
    private IFileService fileService;

    @InnerAuth
    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public R<FileDTO> upLoad(@RequestPart(value = "file") MultipartFile file, @RequestParam(name = "source", required = false, defaultValue = "common") String source) {
        try {
            return R.ok(fileService.uploadFile(file, source));
        } catch (Exception e) {
            return R.fail(e.getMessage());
        }
    }
}
