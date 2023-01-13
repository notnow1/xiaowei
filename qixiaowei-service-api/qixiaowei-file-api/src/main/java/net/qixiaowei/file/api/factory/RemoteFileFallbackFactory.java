package net.qixiaowei.file.api.factory;

import net.qixiaowei.file.api.RemoteFileService;
import net.qixiaowei.file.api.dto.FileDTO;
import net.qixiaowei.integration.common.domain.R;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.multipart.MultipartFile;


/**
 * 文件服务降级处理
 */
@Component
public class RemoteFileFallbackFactory implements FallbackFactory<RemoteFileService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteFileFallbackFactory.class);

    @Override
    public RemoteFileService create(Throwable throwable) {
        log.error("文件服务调用失败:{}", throwable.getMessage());
        return new RemoteFileService() {
            /**
             * 上传文件
             *
             * @param file      文件信息
             * @return 结果
             */
            @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
            @Override
            public R<FileDTO> upLoad(MultipartFile file, String uploadSource, String source) {
                return R.fail("上传文件失败:" + throwable.getMessage());
            }
        };
    }
}
