package net.qixiaowei.operate.cloud.api.factory;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.remote.RemoteOperateCloudInitDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

public class RemoteOperateCloudInitDataFallbackFactory implements FallbackFactory<RemoteOperateCloudInitDataService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteOperateCloudInitDataFallbackFactory.class);

    @Override
    public RemoteOperateCloudInitDataService create(Throwable throwable) {
        log.error("初始化数据服务调用失败:{}", throwable.getMessage());
        return new RemoteOperateCloudInitDataService() {
            @Override
            public R<Boolean> initData(String source) {
                return R.fail("初始化数据服务失败:" + throwable.getMessage());
            }
        };
    }
}
