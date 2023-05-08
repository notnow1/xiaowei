package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;
import java.util.Set;

/**
 * @author hzk
 * 销售云同步注册
 */
@Data
public class SyncResisterDTO {
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
     * 注册手机号
     */
    @NotBlank(message = "手机号不可为空！")
    private String phone;
    /**
     * 密码
     */
    @NotBlank(message = "密码不可为空！")
    private String password;
    /**
     * 初始化菜单ID集合
     */
    private Set<Long> initMenuIds;
    /**
     * 用户ID
     */
    @NotNull(message = "用户ID不能为空！")
    private Long userId;
    /**
     * 角色ID
     */
    @NotNull(message = "角色ID不能为空！")
    private Long roleId;
    /**
     * 同步的角色
     */
    private List<SyncRoleDTO> roles;


}
