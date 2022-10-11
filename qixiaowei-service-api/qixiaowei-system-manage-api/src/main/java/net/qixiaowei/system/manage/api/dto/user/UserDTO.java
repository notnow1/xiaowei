package net.qixiaowei.system.manage.api.dto.user;

import java.util.Date;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.Date;
import java.util.List;
import javax.validation.groups.Default;

import com.fasterxml.jackson.annotation.JsonFormat;
import net.qixiaowei.system.manage.api.domain.system.Role;
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

    //新增检验
    public interface DeleteUserDTO extends Default {

    }

    //修改检验
    public interface UpdateUserDTO extends Default {

    }

    /**
     * ID
     */
    private Long userId;
    /**
     * 员工ID
     */
    private Long employeeId;
    /**
     * 用户帐号
     */
    private String userAccount;
    /**
     * 密码
     */
    private String password;
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
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date createTime;
    /**
     * 更新人
     */
    private Long updateBy;
    /**
     * 更新时间
     */
    @JsonFormat(pattern = "yyyy-MM-dd hh:mm:ss", timezone = "GMT+8")
    private Date updateTime;

    /**
     * 角色对象
     */
    private List<RoleDTO> roles;



}

