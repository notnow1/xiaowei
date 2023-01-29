package net.qixiaowei.auth.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.json.JSONUtil;
import net.qixiaowei.auth.form.ResetPasswordBody;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.message.api.dto.message.MessageReceiverDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.api.remote.message.RemoteMessageService;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.UserConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.user.UserStatus;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    @Autowired
    private RemoteMessageService remoteMessageService;

    /**
     * 登录
     */
    public LoginUserVO login(HttpServletRequest request, String userAccount, String password) {
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
        // 用户名不在指定范围内 错误 20230111 需求更改，帐号长度范围改为1-20
        if (userAccount.length() > UserConstants.USERNAME_MAX_LENGTH) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "用户名不在指定范围");
            throw new ServiceException("用户名不在指定范围");
        }
        // 查询用户信息
        String serverName = StringUtils.isNotEmpty(request.getHeader("proxyHost")) ? request.getHeader("proxyHost") : request.getServerName();
        R<LoginUserVO> userResult = remoteUserService.getUserInfo(userAccount, serverName, SecurityConstants.INNER);
        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "登录用户不存在");
            throw new ServiceException("您输入的账号或密码有误，请重新输入。");
        }
        if (R.FAIL == userResult.getCode()) {
            throw new ServiceException(userResult.getMsg());
        }
        LoginUserVO userInfo = userResult.getData();
        UserDTO user = userInfo.getUserDTO();
        if (DBDeleteFlagConstants.DELETE_FLAG_ONE.equals(user.getDeleteFlag())) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "对不起，您的账号已被删除");
            throw new ServiceException("您输入的账号或密码有误，请重新输入。");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
//            recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_FAIL, "用户已停用，请联系管理员");
            throw new ServiceException("您输入的账号或密码有误，请重新输入。");
        }
        passwordService.validate(user, password);
//        recordLogService.recordLoginInfo(userAccount, Constants.LOGIN_SUCCESS, "登录成功");
        return userInfo;
    }

    /**
     * @description: 重置密码
     * @Author: hzk
     * @date: 2023/1/12 11:09
     * @param: [request, resetPasswordBody]
     * @return: void
     **/
    public void resetPwd(HttpServletRequest request, ResetPasswordBody resetPasswordBody) {
        String userAccount = resetPasswordBody.getUserAccount();
        String email = resetPasswordBody.getEmail();
        // 用户名或邮箱为空 错误
        if (StringUtils.isAnyBlank(userAccount, email)) {
            throw new ServiceException("用户/邮箱必须填写");
        }
        // 用户名不在指定范围内 错误
        if (userAccount.length() > UserConstants.USERNAME_MAX_LENGTH) {
            throw new ServiceException("用户名不在指定范围");
        }
        // 查询用户信息
        String serverName = StringUtils.isNotEmpty(request.getHeader("proxyHost")) ? request.getHeader("proxyHost") : request.getServerName();
        R<LoginUserVO> userResult = remoteUserService.getUserInfo(userAccount, serverName, SecurityConstants.INNER);
        if (StringUtils.isNull(userResult) || StringUtils.isNull(userResult.getData())) {
            passwordService.validateOfReset(userAccount, null);
            throw new ServiceException("您输入的账号与邮箱不匹配，请重新填写。");
        }
        if (R.FAIL == userResult.getCode()) {
            passwordService.validateOfReset(userAccount, null);
            throw new ServiceException(userResult.getMsg());
        }
        LoginUserVO userInfo = userResult.getData();
        UserDTO user = userInfo.getUserDTO();
        passwordService.validateOfReset(userAccount, user.getTenantId());
        if (DBDeleteFlagConstants.DELETE_FLAG_ONE.equals(user.getDeleteFlag())) {
            throw new ServiceException("您输入的账号与邮箱不匹配，请重新填写。");
        }
        if (UserStatus.DISABLE.getCode().equals(user.getStatus())) {
            throw new ServiceException("您输入的账号与邮箱不匹配，请重新填写。");
        }
        String emailOfUser = user.getEmail();
        if (!email.equals(emailOfUser)) {
            throw new ServiceException("您输入的账号与邮箱不匹配，请重新填写。");
        }
        Long userId = user.getUserId();
        //密码随机生成，6位数的数字、字母组合
        String password = RandomUtil.randomString(6);
        R<?> resetPwdOfUserId = remoteUserService.resetPwdOfUserId(userId, password, SecurityConstants.INNER);
        if (R.SUCCESS != resetPwdOfUserId.getCode()) {
            throw new ServiceException("重置密码失败：" + resetPwdOfUserId.getMsg());
        }
        //发送邮件
        this.sendEmail(userId, emailOfUser, password);
    }

    /**
     * @description: 把重置密码发送邮件给用户
     * @Author: hzk
     * @date: 2023/1/10 17:30
     * @param: [userId, email, password]
     * @return: void
     **/
    private void sendEmail(Long userId, String email, String password) {
        MessageSendDTO messageSendDTO = new MessageSendDTO();
        messageSendDTO.setMessageType(MessageType.EMAIL.getCode());
        messageSendDTO.setBusinessType(BusinessSubtype.RESET_PASSWORD_USER_ID.getParentBusinessType().getCode());
        messageSendDTO.setBusinessSubtype(BusinessSubtype.RESET_PASSWORD_USER_ID.getCode());
        messageSendDTO.setBusinessId(userId);
        messageSendDTO.setSendUserId(userId);
        List<MessageReceiverDTO> messageReceivers = new ArrayList<>();
        MessageReceiverDTO messageReceiverDTO = new MessageReceiverDTO();
        messageReceiverDTO.setUserId(userId);
        messageReceiverDTO.setUser(email);
        messageReceivers.add(messageReceiverDTO);
        messageSendDTO.setMessageReceivers(messageReceivers);
        messageSendDTO.setMessageTitle(BusinessSubtype.RESET_PASSWORD_USER_ID.getInfo());
        Map<String, Object> paramMap = new HashMap<>();
        //密码
        paramMap.put("password", password);
        String messageParam = JSONUtil.toJsonStr(paramMap);
        messageSendDTO.setMessageParam(messageParam);
        messageSendDTO.setHandleContent(true);
        R<Boolean> sendMessage = remoteMessageService.sendMessage(messageSendDTO, SecurityConstants.INNER);
        if (R.SUCCESS != sendMessage.getCode()) {
            throw new ServiceException("重置密码失败：邮件发送失败。");
        }
        Boolean result = sendMessage.getData();
        if (!result) {
            throw new ServiceException("重置密码失败：邮件发送失败。");
        }
    }

    public void logout(String loginName) {
        recordLogService.recordLoginInfo(loginName, Constants.LOGOUT, "退出成功");
    }

}
