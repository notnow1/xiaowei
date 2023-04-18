package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;

import java.util.Date;
import java.util.Set;

/**
 * @author hzk
 * 销售云同步注册
 */
@Data
public class SyncTenantUpdateDTO {
    /**
     * 企业名称
     */
    private String companyName;
    /**
     * 企业ID
     */
    private Long companyId;
    /**
     * 服务截止时间
     */
    private Date endTime;
    /**
     * 新增菜单ID集合
     */
    private Set<Long> addMenuIds;
    /**
     * 删除菜单ID集合
     */
    private Set<Long> removeMenuIds;
    /**
     * 角色ID
     */
    private Long roleId;

}
