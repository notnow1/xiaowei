package net.qixiaowei.system.manage.api.vo;

import java.util.Date;
import java.util.List;
import javax.validation.constraints.*;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import net.qixiaowei.integration.common.xss.Xss;

/**
 * 用户对象 UserVO
 */
@Data
public class UserVO {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long userId;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 员工姓名
     */
    private String employeeName;
    /**
     * 用户帐号
     */
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空")
    @Size(min = 0, max = 120, message = "用户账号长度不能超过120个字符")
    private String userAccount;
    /**
     * 密码
     */
    private String password;
    /**
     * 用户名称
     */
    @Xss(message = "用户名称不能包含脚本字符")
    @Size(min = 0, max = 60, message = "用户名称长度不能超过60个字符")
    private String userName;
    /**
     * 手机号码
     */
    @Size(min = 0, max = 30, message = "手机号码长度不能超过30个字符")
    private String mobilePhone;
    /**
     * 邮箱
     */
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 60, message = "邮箱长度不能超过60个字符")
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
     * 删除标记:0未删除;1已删除
     */
    private Integer deleteFlag;
    /**
     * 创建人
     */
    private Long createBy;
    /**
     * 创建时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;
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
    private  Long  postId;
    /**
     * 岗位名称
     */
    private  String  postName;
    /**
     * 角色组
     */
    private Long[] roleIds;

    /**
     * 岗位组
     */
    private Long[] postIds;

    /**
     * 角色ID
     */
    private Long roleId;
    /**
     * 租户ID
     */
    private Long tenantId;
    /**
     * 租户名称
     */
    private  String tenantName;
    /**
     * 租户logo图片URL
     */
    private  String tenantLogo;


}
