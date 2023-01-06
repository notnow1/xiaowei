package net.qixiaowei.auth.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.utils.ServletUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.ip.IpUtils;
import net.qixiaowei.system.manage.api.remote.log.RemoteOperationLogService;
import net.qixiaowei.system.manage.api.domain.SysLoginInfo;

/**
 * 记录日志方法
 */
@Component
public class SysRecordLogService {
    @Autowired
    private RemoteOperationLogService remoteOperationLogService;

    /**
     * 记录登录信息
     *
     * @param username 用户名
     * @param status   状态
     * @param message  消息内容
     * @return
     */
    public void recordLoginInfo(String username, String status, String message) {
        SysLoginInfo loginInfo = new SysLoginInfo();
        loginInfo.setUserName(username);
        loginInfo.setIpaddr(IpUtils.getIpAddr(ServletUtils.getRequest()));
        loginInfo.setMsg(message);
        // 日志状态
        if (StringUtils.equalsAny(status, Constants.LOGIN_SUCCESS, Constants.LOGOUT, Constants.REGISTER)) {
            loginInfo.setStatus(Constants.LOGIN_SUCCESS_STATUS);
        } else if (Constants.LOGIN_FAIL.equals(status)) {
            loginInfo.setStatus(Constants.LOGIN_FAIL_STATUS);
        }
//        remoteOperationLogService.saveLoginInfo(loginInfo, SecurityConstants.INNER);
    }
}
