package net.qixiaowei.message.api.factory.message;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.api.remote.message.RemoteMessageService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;


/**
 * 消息服务降级处理
 */
@Component
public class RemoteMessageFallbackFactory implements FallbackFactory<RemoteMessageService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteMessageFallbackFactory.class);

    @Override
    public RemoteMessageService create(Throwable throwable) {
        log.error("消息服务调用失败:{}", throwable.getMessage());
        return new RemoteMessageService() {
            @Override
            public R<Boolean> sendMessage(MessageSendDTO messageSendDTO, String source) {
                return R.fail("发送消息失败:" + throwable.getMessage());
            }
        };
    }
}
