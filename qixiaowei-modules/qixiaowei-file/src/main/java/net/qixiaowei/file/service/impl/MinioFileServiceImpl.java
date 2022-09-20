package net.qixiaowei.file.service.impl;

import net.qixiaowei.file.config.MinioConfig;
import net.qixiaowei.file.service.IFileService;
import net.qixiaowei.file.utils.FileUploadUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

/**
 * Minio 文件存储
 */
@Service
@Primary
public class MinioFileServiceImpl implements IFileService {
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public String uploadFile(MultipartFile file) throws Exception {
        //todo 前面用租户id区分，区分不同租户的文件上传
        String fileName = FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        return "/" + fileName;
    }
}
