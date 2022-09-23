package net.qixiaowei.message.api.factory;

import net.qixiaowei.message.api.RemoteService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 服务降级处理
 */
@Component
public class RemoteFallbackFactory implements FallbackFactory<RemoteService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteFallbackFactory.class);

    @Override
    public RemoteService create(Throwable throwable) {
        log.error("服务调用失败:{}", throwable.getMessage());
        return new RemoteService() {

        };
    }
}
