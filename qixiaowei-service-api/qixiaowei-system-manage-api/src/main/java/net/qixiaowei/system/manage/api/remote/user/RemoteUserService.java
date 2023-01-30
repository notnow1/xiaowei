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

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

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
    R<LoginUserVO> getUserInfo(@RequestParam("userAccount") String userAccount, @RequestParam("domain") String domain, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过用户ID查询用户信息
     *
     * @param userId 用户ID
     * @return 结果
     */
    @GetMapping(API_PREFIX_USER + "/infoByUserId")
    R<UserDTO> getUserInfoByUserId(@RequestParam("userId") Long userId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过用户ID集合查询用户列表信息
     *
     * @param userIds 用户IDs
     * @return 结果
     */
    @PostMapping(API_PREFIX_USER + "/usersByUserIds")
    R<List<UserDTO>> getUsersByUserIds(@RequestBody Set<Long> userIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过用户ID重置密码
     *
     * @param userId   用户ID
     * @param password 密码
     * @return 结果
     */
    @PostMapping(API_PREFIX_USER + "/resetPwdOfUserId")
    R<?> resetPwdOfUserId(@RequestParam("userId") Long userId, @RequestParam("password") String password, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 注册用户信息
     *
     * @param userVO 用户信息
     * @param source 请求来源
     * @return 结果
     */
    @PostMapping(API_PREFIX_USER + "/user/register")
    R<Boolean> registerUserInfo(@RequestBody UserVO userVO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 通过人员ID集合查询用户id
     *
     * @param employeeIds 人员IDs
     * @return 结果
     */
    @PostMapping(API_PREFIX_USER + "/selectByemployeeIds")
    R<List<UserDTO>> selectByemployeeIds(@RequestBody List<Long> employeeIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
