package net.qixiaowei.system.manage.api.dto.user;

import java.util.*;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.integration.common.enums.user.UserType;
import net.qixiaowei.integration.common.xss.Xss;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;

/**
 * 用户表
 *
 * @author hzk
 * @since 2022-10-05
 */
@Data
@Accessors(chain = true)
public class UserDTO {

    //查询检验
    public interface QueryUserDTO extends Default {

    }

    //新增检验
    public interface AddUserDTO extends Default {

    }

    //删除检验
    public interface DeleteUserDTO extends Default {

    }

    //修改检验
    public interface UpdateUserDTO extends Default {

    }

    //重置密码校验
    public interface ResetPwdRUserDTO extends Default {

    }

    //自己修改信息校验
    public interface UpdateUserOfSelfDTO extends Default {

    }

    /**
     * ID
     */
    @NotNull(message = "用户ID不能为空", groups = {UserDTO.UpdateUserDTO.class, UserDTO.DeleteUserDTO.class, UserDTO.ResetPwdRUserDTO.class})
    private Long userId;
    /**
     * 用户类型:0其他;1系统管理员
     */
    private Integer userType;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 员工姓名
     */
    private String employeeName;
    /**
     * 员工工号
     */
    private String employeeCode;
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
     * 用户帐号
     */
    @Xss(message = "用户账号不能包含脚本字符")
    @NotBlank(message = "用户账号不能为空", groups = {UserDTO.AddUserDTO.class, UserDTO.UpdateUserDTO.class})
    @Size(min = 0, max = 120, message = "用户账号长度不能超过120个字符", groups = {UserDTO.AddUserDTO.class, UserDTO.UpdateUserDTO.class})
    private String userAccount;
    /**
     * 密码
     */
    @NotBlank(message = "密码不能为空", groups = {UserDTO.AddUserDTO.class, UserDTO.ResetPwdRUserDTO.class})
    @Size(min = 6, max = 120, message = "密码长度最低6位，且不能超过120个字符", groups = {UserDTO.AddUserDTO.class, UserDTO.UpdateUserDTO.class})
    private String password;
    /**
     * 用户名称
     */
    @Xss(message = "用户名称不能包含脚本字符")
    @Size(min = 0, max = 60, message = "用户名称长度不能超过60个字符", groups = {UserDTO.AddUserDTO.class, UserDTO.UpdateUserDTO.class})
    private String userName;
    /**
     * 手机号码
     */
    @NotBlank(message = "手机号码不能为空", groups = {UserDTO.AddUserDTO.class, UserDTO.UpdateUserOfSelfDTO.class})
    @Size(min = 0, max = 30, message = "手机号码长度不能超过30个字符", groups = {UserDTO.AddUserDTO.class, UserDTO.UpdateUserDTO.class, UserDTO.UpdateUserOfSelfDTO.class})
    private String mobilePhone;
    /**
     * 邮箱
     */
    @NotBlank(message = "邮箱不能为空", groups = {UserDTO.AddUserDTO.class, UserDTO.UpdateUserOfSelfDTO.class})
    @Email(message = "邮箱格式不正确")
    @Size(min = 0, max = 60, message = "邮箱长度不能超过60个字符", groups = {UserDTO.AddUserDTO.class, UserDTO.UpdateUserDTO.class, UserDTO.UpdateUserOfSelfDTO.class})
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
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy/MM/dd", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 拥有的角色对象
     */
    private List<RoleDTO> roles;

    /**
     * 要保存的角色组
     */
    private Set<Long> roleIds;

    /**
     * 角色ID，查询时使用
     */
    private Long roleId;

    public boolean isAdmin() {
        return isAdmin(this.userType);
    }

    public static boolean isAdmin(Integer userType) {
        return UserType.SYSTEM.getCode().equals(userType);
    }

    /**
     * 请求参数
     */
    private Map<String, Object> params;
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

