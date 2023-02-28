package net.qixiaowei.auth.service;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import net.qixiaowei.auth.form.LoginBody;
import net.qixiaowei.auth.form.RegisterBody;
import net.qixiaowei.auth.form.ResetPasswordBody;
import net.qixiaowei.integration.common.constant.*;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.message.api.dto.message.MessageReceiverDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.api.remote.message.RemoteMessageService;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.tenant.TenantRegisterResponseVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
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
import java.util.concurrent.TimeUnit;

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

    @Autowired
    private RedisService redisService;

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
        if (StringUtils.isNull(userResult)) {
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
     * @description: 发送验证码
     * @Author: hzk
     * @date: 2023/2/20 15:47
     * @param: [userAccount]
     * @return: void
     **/
    public void sendSms(String userAccount) {
        //去除空格
        userAccount = userAccount.trim();
        // 效验手机号正则
        String regex = "^1[3456789]\\d{9}$";
        if (!ReUtil.isMatch(regex, userAccount)) {
            throw new ServiceException("请输入正确的手机号码！");
        }
        //校验是否注册过
        this.checkUserAccountExists(userAccount);
        //发送验证码
        String key = CacheConstants.SMS_SEND_KEY + userAccount;
        //60秒的短信间隔，10分钟的有效期
        long keyExpireTime = 600;
        if (redisService.hasKey(key) && redisService.getExpire(key) > (keyExpireTime - 60)) {
            throw new ServiceException("短信已发送，请稍候再试！");
        }
        String code = RandomUtil.randomNumbers(6);
        this.sendSms(userAccount, code);
        redisService.setCacheObject(key, code, keyExpireTime, TimeUnit.SECONDS);
    }


    public TenantRegisterResponseVO register(RegisterBody registerBody) {
        String code = registerBody.getCode();
        String userAccount = registerBody.getUserAccount().trim();
        String key = CacheConstants.SMS_SEND_KEY + userAccount;
        //校验验证码
        if (StringUtils.isEmpty(code) || !redisService.hasKey(key) || !code.equals(redisService.getCacheObject(key))) {
            throw new ServiceException("手机验证码错误，请确认！");
        }
        //校验是否注册过
        this.checkUserAccountExists(userAccount);
        TenantDTO tenantDTO = new TenantDTO();
        tenantDTO.setAdminAccount(userAccount);
        tenantDTO.setAdminPassword(registerBody.getPassword());
        tenantDTO.setAdminEmail(registerBody.getEmail());
        tenantDTO.setTenantName(registerBody.getTenantName());
        tenantDTO.setTenantIndustry(registerBody.getIndustryId());
        //执行注册
        R<TenantRegisterResponseVO> tenantRegisterResponseVOR = remoteUserService.registerUserInfo(tenantDTO, SecurityConstants.INNER);
        if (R.SUCCESS != tenantRegisterResponseVOR.getCode()) {
            throw new ServiceException(tenantRegisterResponseVOR.getMsg());
        }
        redisService.deleteObject(key);
        return tenantRegisterResponseVOR.getData();
    }


    /**
     * @description: 发送验证码
     * @Author: hzk
     * @date: 2023/2/20 15:47
     * @param: [userAccount, code]
     * @return: void
     **/
    private void sendSms(String userAccount, String code) {
        MessageSendDTO messageSendDTO = new MessageSendDTO();
        messageSendDTO.setMessageType(MessageType.SMS.getCode());
        messageSendDTO.setBusinessType(BusinessSubtype.TENANT_ACCOUNT_REGISTER.getParentBusinessType().getCode());
        messageSendDTO.setBusinessSubtype(BusinessSubtype.TENANT_ACCOUNT_REGISTER.getCode());
        messageSendDTO.setBusinessId(0L);
        messageSendDTO.setSendUserId(0L);
        List<MessageReceiverDTO> messageReceivers = new ArrayList<>();
        MessageReceiverDTO messageReceiverDTO = new MessageReceiverDTO();
        messageReceiverDTO.setUserId(0L);
        messageReceiverDTO.setUser(userAccount);
        messageReceivers.add(messageReceiverDTO);
        messageSendDTO.setMessageReceivers(messageReceivers);
        messageSendDTO.setMessageTitle(BusinessSubtype.RESET_PASSWORD_USER_ID.getInfo());
        Map<String, Object> paramMap = new HashMap<>();
        //验证码
        paramMap.put("code", code);
        String messageParam = JSONUtil.toJsonStr(paramMap);
        messageSendDTO.setMessageParam(messageParam);
        messageSendDTO.setHandleContent(true);
        R<Boolean> sendMessage = remoteMessageService.sendMessage(messageSendDTO, SecurityConstants.INNER);
        if (R.SUCCESS != sendMessage.getCode()) {
            throw new ServiceException("发送验证码失败，请稍候再试！");
        }
        Boolean result = sendMessage.getData();
        if (!result) {
            throw new ServiceException("发送验证码失败，请稍候再试！");
        }
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

    /**
     * @description: 校验用户帐号是否存在。
     * @Author: hzk
     * @date: 2023/2/20 16:24
     * @param: [userAccount]
     * @return: void
     **/
    private void checkUserAccountExists(String userAccount) {
        R<Boolean> checkUserAccountExists = remoteUserService.checkUserAccountExists(userAccount, SecurityConstants.INNER);
        Boolean userAccountExists = checkUserAccountExists.getData();
        if (R.SUCCESS != checkUserAccountExists.getCode() || StringUtils.isNull(userAccountExists)) {
            throw new ServiceException("系统异常，请稍后再试");
        }
        if (userAccountExists) {
            throw new ServiceException("您的手机号码已开通帐号，不可重复申请");
        }
    }

    public void logout(String loginName) {
        recordLogService.recordLoginInfo(loginName, Constants.LOGOUT, "退出成功");
    }

}
