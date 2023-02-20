package net.qixiaowei.system.manage.api.vo.tenant;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 租户注册返回vo
 *
 * @author hzk
 * @since 2022-12-14
 */
@Data
@Accessors(chain = true)
public class TenantRegisterResponseVO {

    /**
     * 域名
     */
    private String domain;

}

