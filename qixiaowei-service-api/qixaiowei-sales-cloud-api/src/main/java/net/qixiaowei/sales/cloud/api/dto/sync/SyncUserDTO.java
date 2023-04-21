package net.qixiaowei.sales.cloud.api.dto.sync;

import lombok.Data;


/**
 * @author hzk
 * 销售云同步用户
 */
@Data
public class SyncUserDTO {
    /**
     * 姓名
     */
    private String realname;
    /**
     * 账号
     */
    private String username;
    /**
     * 用户ID
     */
    private Long userId;
    /**
     * 性别，0 未选择 1、男 2、女
     */
    private Integer sex;
    /**
     * 手机号
     */
    private String mobile;
    /**
     * 密码
     */
    private String password;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 部门ID
     */
    private Long deptId;
    /**
     * 状态,0禁用,1正常,2未激活
     */
    private Integer status;
    /**
     * 岗位
     */
    private String post;
    /**
     * 上级ID
     */
    private Long parentId;
    /**
     * 角色ID
     */
    private String roleId;
    /**
     * 员工编号
     */
    private String num;

}
