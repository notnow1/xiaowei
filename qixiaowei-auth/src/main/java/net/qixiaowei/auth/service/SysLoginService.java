package net.qixiaowei.auth.service;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.UserVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.UserConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.UserStatus;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.RemoteUserService;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;

/**
 * 登录校验方法
 */
@Component
public class SysLoginService {
    @Autowired
    private RemoteUserService remoteUserService;

    @Autowired
    private SysPasswordService passwordService;

    @Autowired
    private SysRecordLogService recordLogService;

    /**
     * 登录
     */
    public LoginUserVO login(String userAccount, String password) {
        // 用户名或密码为空 错误
        if (StringUtils.isAnyBlank(userAccount, password)) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "用户/密码必须填写");
            throw new ServiceException("用户/密码必须填写");
        }
        // 密码如果不在指定范围内 错误
        if (password.length() < UserConstants.PASSWORD_MIN_LENGTH
                || password.length() > UserConstants.PASSWORD_MAX_LENGTH) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "用户密码不在指定范围");
            throw new ServiceException("用户密码不在指定范围");
        }
        // 用户名不在指定范围内 错误
        if (userAccount.length() < UserConstants.USERNAME_MIN_LENGTH
                || userAccount.length() > UserConstants.USERNAME_MAX_LENGTH) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // 查询用户信息
        R<LoginUserVO> userResult = remoteUserService.getUserInfo(userAccount, SecurityConstants.INNER);
        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("登录用户：" + userAccount + " 不存在");
        }
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        LoginUserVO userInfo = userResult.getData();
        UserDTO user = userInfo.getUserDTO();
        if (DBDeleteFlagConstants.DELETE_FLAG_ONE.equals(user.getDeleteFlag())) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("对不起，您的账号：" + userAccount + " 已被删除");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("对不起，您的账号：" + userAccount + " 已停用");
        }
        passwordService.validate(user, password);
//        recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    public void logout(String loginName) {
        recordLogService.recordLoginInfo(loginName, Constants.LOGOUT, "退出成功");
    }

}
