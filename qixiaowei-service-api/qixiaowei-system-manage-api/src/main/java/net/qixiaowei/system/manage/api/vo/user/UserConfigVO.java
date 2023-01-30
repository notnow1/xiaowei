package net.qixiaowei.system.manage.api.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户配置VO
 *
 * @author hzk
 * @since 2023-01-30
 */
@Data
@Accessors(chain = true)
public class UserConfigVO {

    /**
     * ID
     */
    private Long userConfigId;
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
    private Integer status;
    /**
     * 备注
     */
    private String remark;

}

