package net.qixiaowei.file.api.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * 单个文件返回实体
 */
@Data
public class File implements Serializable {

    /**
     * 文件名
     */
    private String name;

    /**
     * 文件路径
     */
    private String path;


}