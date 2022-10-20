package net.qixiaowei.system.manage.api.factory;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.RemoteUserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;
import net.qixiaowei.system.manage.api.vo.UserVO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;

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
            public R<LoginUserVO> getUserInfo(String username, String source) {
                return R.fail("获取用户失败:" + throwable.getMessage());
            }

            @Override
            public R<Boolean> registerUserInfo(UserVO userVO, String source) {
                return R.fail("注册用户失败:" + throwable.getMessage());
            }
        };
    }
}
