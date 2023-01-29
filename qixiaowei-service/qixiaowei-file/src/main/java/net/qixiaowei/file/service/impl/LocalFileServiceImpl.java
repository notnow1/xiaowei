package net.qixiaowei.file.service.impl;

import net.qixiaowei.file.api.dto.FileDTO;
import net.qixiaowei.file.logic.FileLogic;
import net.qixiaowei.file.service.IFileService;
import net.qixiaowei.file.utils.FileUploadUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * 本地文件存储
 */
//@Primary
@Service
public class LocalFileServiceImpl implements IFileService {
    /**
     * 资源映射路径 前缀
     */
    @Value("${file.prefix}")
    public String localFilePrefix;

    /**
     * 域名或本机访问地址
     */
    @Value("${file.domain}")
    public String domain;

    /**
     * 上传文件存储在本地的根路径
     */
    @Value("${file.path}")
    private String localFilePath;

    @Autowired
    private FileLogic fileLogic;

    /**
     * 本地文件上传接口
     *
     * @param file 上传的文件
     * @return 访问地址
     * @throws Exception
     */
    @Override
    public FileDTO uploadFile(MultipartFile file, String source) throws Exception {
        //前面用租户id区分
        String name = FileLogic.PREFIX + SecurityUtils.getTenantId() + "/" + source + "/" + SecurityUtils.getUserId() + "/" + FileUploadUtils.upload(localFilePath, file);
        String url = domain + localFilePrefix + name;
        return fileLogic.saveFileRecord(file, source, url);
    }
}
