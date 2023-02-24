package net.qixiaowei.system.manage.api.dto.user;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * 用户配置表
 *
 * @author hzk
 * @since 2023-01-30
 */
@Data
@Accessors(chain = true)
public class UserConfigDTO {

    //查询检验
    public interface QueryUserConfigDTO extends Default {

    }

    //新增检验
    public interface AddUserConfigDTO extends Default {

    }

    //删除检验
    public interface DeleteUserConfigDTO extends Default {

    }

    //修改检验
    public interface UpdateUserConfigDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "用户配置ID不能为空", groups = {UserConfigDTO.UpdateUserConfigDTO.class})
    private Long userConfigId;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 用户配置类型
     */
    private Integer userConfigType;
    /**
     * 用户配置值
     */
    private String userConfigValue;
    /**
     * 状态:0关闭;1启用
     */
    @NotNull(message = "用户配置状态不能为空", groups = {UserConfigDTO.UpdateUserConfigDTO.class})
    private Integer status;
    /**
     * 备注
     */
    private String remark;
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
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;


}

