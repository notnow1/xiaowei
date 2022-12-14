package net.qixiaowei.system.manage.api.vo.tenant;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 租户登录界面返回vo
 *
 * @author hzk
 * @since 2022-12-14
 */
@Data
@Accessors(chain = true)
public class TenantLoginFormVO {

    /**
     * 租户登录背景图片URL
     */
    private String loginBackground;

}

