package net.qixiaowei.system.manage.api.remote.user;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.api.factory.user.RemoteUserFallbackFactory;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

/**
 * 用户服务
 */
@FeignClient(contextId = "remoteUserService", value = ServiceNameConstants.SYSTEM_MANAGE_SERVICE, fallbackFactory = RemoteUserFallbackFactory.class)
public interface RemoteUserService {

    String API_PREFIX_USER = "/user";

    /**
     * 通过用户帐号查询用户信息
     *
     * @param userAccount 用户帐号
     * @param source      请求来源
     * @return 结果
     */
    @GetMapping(API_PREFIX_USER + "/info")
    R<LoginUserVO> getUserInfo(@RequestParam("userAccount") String userAccount, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过用户帐号查询用户信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping(API_PREFIX_USER + "/infoByUserId")
    R<UserDTO> getUserInfoByUserId(@RequestParam("userId") Long userId);

    /**
     * 注册用户信息
     *
     * @param userVO 用户信息
     * @param source  请求来源
     * @return 结果
     */
    @PostMapping(API_PREFIX_USER + "/user/register")
    R<Boolean> registerUserInfo(@RequestBody UserVO userVO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
