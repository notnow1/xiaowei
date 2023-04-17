package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;

import java.util.Set;

/**
 * @author hzk
 * 销售云同步用户状态
 */
@Data
public class SyncUserStatusDTO {
    /**
     * ids
     */
    private Set<Long> ids;
    /**
     * 状态
     */
    private Integer status;
    /**
     * 密码
     */
    private String password;

}
