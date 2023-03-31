package net.qixiaowei.strategy.cloud.api.factory;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.remote.RemoteStrategyCloudInitDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

public class RemoteStrategyCloudInitDataFallbackFactory implements FallbackFactory<RemoteStrategyCloudInitDataService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteStrategyCloudInitDataFallbackFactory.class);

    @Override
    public RemoteStrategyCloudInitDataService create(Throwable throwable) {
        log.error("初始化数据服务调用失败:{}", throwable.getMessage());
        return new RemoteStrategyCloudInitDataService() {
            @Override
            public R<Boolean> initData(Long userId, String source) {
                return R.fail("初始化数据服务失败:" + throwable.getMessage());
            }
        };
    }
}
