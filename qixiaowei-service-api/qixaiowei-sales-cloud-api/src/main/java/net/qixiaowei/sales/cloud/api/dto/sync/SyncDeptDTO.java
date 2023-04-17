package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;

/**
 * @author hzk
 * 销售云同步部门
 */
@Data
public class SyncDeptDTO {
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 上级部门ID，0为最上级
     */
    private Long parentId;
    /**
     * 部门名称
     */
    private String name;
    /**
     * 部门负责人
     */
    private Long ownerUserId;
    /**
     * 排序
     */
    private Integer num;
}
