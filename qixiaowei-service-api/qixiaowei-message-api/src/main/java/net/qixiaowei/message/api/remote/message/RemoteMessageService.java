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

import java.util.List;


/**
 * 消息服务
 */
@FeignClient(contextId = "remoteMessageService", value = ServiceNameConstants.MESSAGE_SERVICE, fallbackFactory = RemoteMessageFallbackFactory.class)
public interface RemoteMessageService {

    String API_PREFIX_MESSAGE = "/message";

    /**
     * 发送单个消息
     *
     * @param messageSendDTO 消息
     * @param source     请求来源
     * @return 结果
     */
    @PostMapping(API_PREFIX_MESSAGE + "/sendMessage")
    R<Boolean> sendMessage(@Validated @RequestBody MessageSendDTO messageSendDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 批量发送消息
     *
     * @param messageSendDTOS 消息列表
     * @param source     请求来源
     * @return 结果
     */
    @PostMapping(API_PREFIX_MESSAGE + "/sendMessages")
    R<Boolean> sendMessages(@Validated @RequestBody List<MessageSendDTO> messageSendDTOS, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
