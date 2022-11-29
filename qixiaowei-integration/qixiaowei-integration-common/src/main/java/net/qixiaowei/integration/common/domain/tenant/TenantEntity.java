package net.qixiaowei.integration.common.domain.tenant;

import net.qixiaowei.integration.common.web.domain.BaseEntity;

/**
 * @description: 租户实体
 * @Author: hzk
 * @date: 2022/11/28 17:45
 **/

public class TenantEntity extends BaseEntity {

    /**
     * 租户ID
     */
    private Long tenantId;

    public Long getTenantId() {
        return tenantId;
    }

    public void setTenantId(Long tenantId) {
        this.tenantId = tenantId;
    }
}

