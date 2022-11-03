package net.qixiaowei.operate.cloud.api.factory;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteTargetDecomposeHistoryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

public class RemoteTargetDecomposeHistoryFallbackFactory implements FallbackFactory<RemoteTargetDecomposeHistoryService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteTargetDecomposeHistoryFallbackFactory.class);


    @Override
    public RemoteTargetDecomposeHistoryService create(Throwable throwable) {
        log.error("服务调用失败:{}", throwable.getMessage());
        return new RemoteTargetDecomposeHistoryService() {
            @Override
            public void cronCreateHistoryList(int timeDimension) {
                R.fail("生成目标分解历史数据失败:" + throwable.getMessage());
            }
        };
    }
}
