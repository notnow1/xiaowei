package net.qixiaowei.system.manage.api.vo.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户个人信息对象 UserProfileVO
 */
@Data
public class UserProfileVO implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long userId;
    /**
     * 用户帐号
     */
    private String userAccount;
    /**
     * 用户名称
     */
    private String userName;
    /**
     * 手机号码
     */
    private String mobilePhone;
    /**
     * 邮箱
     */
    private String email;
    /**
     * 头像
     */
    private String avatar;
    /**
     * 备注
     */
    private String remark;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 员工姓名
     */
    private String employeeName;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 部门名称
     */
    private String departmentName;
    /**
     * 岗位ID
     */
    private Long postId;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 租户名称
     */
    private String tenantName;
    /**
     * 租户logo图片URL
     */
    private String tenantLogo;


}
