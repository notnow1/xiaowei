package net.qixiaowei.operate.cloud.api.factory.bonus;


import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteEmployeeAnnualBonusService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

/**
 * 个人年终奖服务降级处理
 */
public class RemoteEmployeeAnnualBonusFallbackFactory implements FallbackFactory<RemoteEmployeeAnnualBonusService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteEmployeeAnnualBonusFallbackFactory.class);

    @Override
    public RemoteEmployeeAnnualBonusService create(Throwable throwable) {
        log.error("目标分解服务调用失败:{}", throwable.getMessage());
        return new RemoteEmployeeAnnualBonusService() {

        };
    }
}
