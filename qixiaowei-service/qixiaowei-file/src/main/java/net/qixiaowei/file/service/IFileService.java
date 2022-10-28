package net.qixiaowei.file.service;

import net.qixiaowei.file.api.dto.FileDTO;
import org.springframework.web.multipart.MultipartFile;

/**
 * 文件上传接口
 */
public interface IFileService {
    /**
     * 文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    FileDTO uploadFile(MultipartFile file, String source) throws Exception;
}
