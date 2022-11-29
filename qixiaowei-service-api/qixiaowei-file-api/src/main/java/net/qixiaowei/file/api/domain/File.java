package net.qixiaowei.file.api.domain;


import net.qixiaowei.integration.common.domain.tenant.TenantEntity;
import lombok.Data;

/**
 * 文件表
 *
 * @author hzk
 * @since 2022-10-28
 */
@Data
public class File extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long fileId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 来源
     */
    private String source;
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

