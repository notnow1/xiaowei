package net.qixiaowei.strategy.cloud.api.dto.field;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.Date;

/**
 * 字段列表配置表
 *
 * @author hzk
 * @since 2023-02-08
 */
@Data
@Accessors(chain = true)
public class FieldListConfigDTO {

    //查询检验
    public interface QueryFieldListConfigDTO extends Default {

    }

    //新增检验
    public interface AddFieldListConfigDTO extends Default {

    }

    //删除检验
    public interface DeleteFieldListConfigDTO extends Default {

    }

    //修改检验
    public interface UpdateFieldListConfigDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "字段列表配置ID不能为空", groups = {UpdateFieldListConfigDTO.class})
    private Long fieldListConfigId;
    /**
     * 字段配置ID
     */
    private Long fieldConfigId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 字段宽度
     */
    private Integer fieldWidth;
    /**
     * 排序
     */
    private Integer sort;
    /**
     * 显示标记:0否;1是
     */
    private Integer showFlag;
    /**
     * 固定标记:0否;1是
     */
    private Integer fixationFlag;
    /**
     * 强制显示:0否;1是
     */
    private Integer showForce;
    /**
     * 强制固定:0否;1是
     */
    private Integer fixationForce;
    /**
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
    /**
     * 租户ID
     */
    private Long tenantId;

}

