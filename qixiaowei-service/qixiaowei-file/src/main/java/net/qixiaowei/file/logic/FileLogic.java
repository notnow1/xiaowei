package net.qixiaowei.file.logic;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.file.api.domain.File;
import net.qixiaowei.file.api.dto.FileDTO;
import net.qixiaowei.file.mapper.FileMapper;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.common.utils.file.FileTypeUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;


/**
 * 文件逻辑
 *
 * @author hzk
 * @since 2022-10-28
 */
@Component
@Slf4j
public class FileLogic {

    public static final String PREFIX = "tenant_";

    @Autowired
    private FileMapper fileMapper;

    public FileDTO saveFileRecord(MultipartFile file, String source, String fileUrl) {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        File fileInsert = new File();
        fileInsert.setFileName(FilenameUtils.getBaseName(file.getOriginalFilename()));
        fileInsert.setFileFormat(FileTypeUtils.getExtension(file));
        fileInsert.setFilePath(fileUrl);
        fileInsert.setFileSize(file.getSize());
        fileInsert.setSource(source);
        fileInsert.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        fileInsert.setCreateBy(userId);
        fileInsert.setCreateTime(nowDate);
        fileInsert.setUpdateBy(userId);
        fileInsert.setUpdateTime(nowDate);
        fileMapper.insertFile(fileInsert);
        FileDTO fileDTO = new FileDTO();
        BeanUtils.copyProperties(fileInsert, fileDTO);
        return fileDTO;
    }

}
