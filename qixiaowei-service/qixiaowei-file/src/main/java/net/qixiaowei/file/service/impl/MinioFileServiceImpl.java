package net.qixiaowei.file.service.impl;

import io.minio.GetObjectArgs;
import io.minio.RemoveObjectArgs;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.file.api.dto.FileDTO;
import net.qixiaowei.file.config.MinioConfig;
import net.qixiaowei.file.logic.FileLogic;
import net.qixiaowei.file.service.IFileService;
import net.qixiaowei.file.utils.FileUploadUtils;
import net.qixiaowei.integration.common.config.FileConfig;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;

import javax.annotation.Resource;
import java.io.InputStream;

/**
 * Minio 文件存储
 */
@Service
@Primary
@Slf4j
public class MinioFileServiceImpl implements IFileService {
    @Autowired
    private MinioConfig minioConfig;

    @Autowired
    private MinioClient client;

    @Autowired
    private FileLogic fileLogic;

    @Resource
    private FileConfig fileConfig;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public FileDTO uploadFile(MultipartFile file, String source) throws Exception {
        //前面用租户id区分+用户ID
        String fileName = FileLogic.PREFIX + SecurityUtils.getTenantId() + "/" + source + "/" + SecurityUtils.getUserId() + "/" + FileUploadUtils.extractFilename(file);
        PutObjectArgs args = PutObjectArgs.builder()
                .bucket(minioConfig.getBucketName())
                .object(fileName)
                .stream(file.getInputStream(), file.getSize(), -1)
                .contentType(file.getContentType())
                .build();
        client.putObject(args);
        String url = "/" + fileName;
        FileDTO fileDTO = fileLogic.saveFileRecord(file, source, url);
        fileDTO.setFilePath(fileConfig.getDomain() + url);
        return fileDTO;
    }

    /**
     * @description: 下载文件
     * @Author: hzk
     * @date: 2022/11/10 19:50
     * @param: [filePath]
     * @return: java.io.InputStream
     **/
    @Override
    public InputStream downloadFile(String filePath) {
        try {
            GetObjectArgs getObjectArgs = GetObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(filePath)
                    .build();
            return client.getObject(getObjectArgs);
        } catch (Exception e) {
            log.error("minio download file error:{}", e.getMessage());
            throw new ServiceException("文件下载异常");
        }
    }

    /**
     * @description: 删除文件
     * @Author: hzk
     * @date: 2022/11/10 19:50
     * @param: [fileDTO]
     * @return: void
     **/
    public void deleteFile(FileDTO fileDTO) {
        String key = fileDTO.getFilePath();
        try {
            RemoveObjectArgs removeObjectArgs = RemoveObjectArgs.builder()
                    .bucket(minioConfig.getBucketName())
                    .object(key)
                    .build();
            client.removeObject(removeObjectArgs);
        } catch (Exception e) {

        }
    }


}
