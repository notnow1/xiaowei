package net.qixiaowei.message.api.remote.message;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.message.api.dto.message.MessageDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.api.factory.message.RemoteMessageFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;


/**
 * 服务
 */
@FeignClient(contextId = "remoteMessageService", value = ServiceNameConstants.MESSAGE_SERVICE, fallbackFactory = RemoteMessageFallbackFactory.class)
public interface RemoteMessageService {

    String API_PREFIX_MESSAGE = "/message";

    @PostMapping("/sendMessage")
    R<Boolean> sendMessage(@Validated @RequestBody MessageSendDTO messageSendDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
