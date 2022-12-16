package net.qixiaowei.integration.tenant.service;

import java.util.List;

/**
 * @description: 租户相关接口
 * @Author: hzk
 * @date: 2022/12/16 15:29
 **/
public interface ITenantService {

    /**
     * @description: 获得有效租户的ID集合
     * @Author: hzk
     * @date: 2022/12/16 15:29
     * @param: []
     * @return: java.util.List<java.lang.Long>
     **/
    List<Long> getTenantIds();

}
