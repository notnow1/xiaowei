package net.qixiaowei.system.manage.api.vo.user;

import lombok.Data;
import net.qixiaowei.system.manage.api.vo.UserVO;

import java.io.Serializable;
import java.util.Set;

/**
 * 用户对象 UserVO
 */
@Data
public class UserInfoVO implements Serializable {

    private static final long serialVersionUID = 1L;

    private Set<String> roles;

    private Set<String> permissions;

    private UserVO user;

}
