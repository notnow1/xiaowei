package net.qixiaowei.operate.cloud.api.factory;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteAreaService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;

public class RemoteAreaFactory implements FallbackFactory<RemoteAreaService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteAreaFactory.class);

    @Override
    public RemoteAreaService create(Throwable throwable) {
        log.error("区域服务调用失败:{}", throwable.getMessage());
        return new RemoteAreaService() {
            @Override
            public R<List<AreaDTO>> dropList(AreaDTO areaDTO) {
                return R.fail("获取区域配置失败:" + throwable.getMessage());
            }
        };
    }
}
