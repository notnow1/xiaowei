package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
    @NotBlank(message = "企业名称不能为空！")
    private String companyName;
    /**
     * 企业ID
     */
    @NotNull(message = "企业ID不能为空！")
    private Long companyId;
    /**
     * 服务截止时间
     */
    @NotNull(message = "服务截止时间不能为空！")
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
    @NotNull(message = "角色ID不能为空！")
    private Long roleId;

}
