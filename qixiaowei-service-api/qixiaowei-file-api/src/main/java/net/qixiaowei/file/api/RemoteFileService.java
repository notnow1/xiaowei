package net.qixiaowei.file.api;

import net.qixiaowei.file.api.dto.FileDTO;
import net.qixiaowei.file.api.factory.RemoteFileFallbackFactory;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

/**
 * 文件服务
 */
@FeignClient(contextId = "remoteFileService", value = ServiceNameConstants.FILE_SERVICE, fallbackFactory = RemoteFileFallbackFactory.class)
public interface RemoteFileService {

    String API_PREFIX_FILE = "/file";

    /**
     * 上传文件
     *
     * @param file         文件信息
     * @param uploadSource 上传的来源
     * @return 结果
     */
    @PostMapping(value = API_PREFIX_FILE + "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    R<FileDTO> upLoad(@RequestPart(value = "file") MultipartFile file, @RequestParam(name = "uploadSource", required = false, defaultValue = "common") String uploadSource, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 下载文件
     *
     * @param filePath 文件路径
     * @return 结果
     */
    @GetMapping(value = API_PREFIX_FILE + "/downloadByFilePath")
    R<InputStream> downloadByFilePath(@RequestParam(value = "filePath") String filePath, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
