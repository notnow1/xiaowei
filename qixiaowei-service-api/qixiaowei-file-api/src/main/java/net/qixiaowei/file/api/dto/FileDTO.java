package net.qixiaowei.file.api.dto;

import lombok.Data;

import java.io.Serializable;


/**
 * 文件表
 *
 * @author hzk
 * @since 2022-10-28
 */
@Data
public class FileDTO implements Serializable {

    /**
     * 文件名称
     */
    private String fileName;
    /**
     * 文件格式
     */
    private String fileFormat;
    /**
     * 大小
     */
    private Long fileSize;
    /**
     * 路径
     */
    private String filePath;

}

