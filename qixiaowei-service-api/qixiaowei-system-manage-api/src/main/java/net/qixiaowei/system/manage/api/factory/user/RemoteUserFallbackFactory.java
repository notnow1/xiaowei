package net.qixiaowei.system.manage.api.factory.user;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Set;

/**
 * 用户服务降级处理
 */
@Component
public class RemoteUserFallbackFactory implements FallbackFactory<RemoteUserService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteUserFallbackFactory.class);

    @Override
    public RemoteUserService create(Throwable throwable) {
        log.error("用户服务调用失败:{}", throwable.getMessage());
        return new RemoteUserService() {
            @Override
            public R<LoginUserVO> getUserInfo(String userAccount, String domain, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<UserDTO> getUserInfoByUserId(Long userId, String source) {
                return R.fail("根据用户ID获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<UserDTO>> getUsersByUserIds(Set<Long> userIds, String source) {
                return R.fail("根据用户IDs获取用户列表失败:" + throwable.getMessage());
            }

            @Override
            public R<?> resetPwdOfUserId(Long userId, String password, String source) {
                return R.fail("通过用户ID重置密码失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(UserVO userVO, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }

            @Override
            public R<List<UserDTO>> selectByemployeeIds(List<Long> employeeIds, String source) {
                return R.fail("通过人员ID查询用户id失败:" + throwable.getMessage());
            }
        };
    }
}
