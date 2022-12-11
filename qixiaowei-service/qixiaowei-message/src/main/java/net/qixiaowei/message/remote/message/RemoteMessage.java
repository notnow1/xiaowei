package net.qixiaowei.message.remote.message;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.api.remote.message.RemoteMessageService;
import net.qixiaowei.message.logic.manager.MessageConsumerManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @Author hzk
 * @Date 2022-12-09 17:39
 **/
@RestController
@RequestMapping("/message")
public class RemoteMessage implements RemoteMessageService {

    @Autowired
    private MessageConsumerManager messageConsumerManager;

    @Override
    @InnerAuth
    @PostMapping("/sendMessage")
    public R<?> sendMessage(@Validated @RequestBody MessageSendDTO messageSendDTO, String source) {
        Integer messageType = messageSendDTO.getMessageType();
        return R.ok(messageConsumerManager.handleConsumer(messageType, messageSendDTO));
    }

    @Override
    @InnerAuth
    @PostMapping("/sendMessages")
    public R<?> sendMessages(@Validated @RequestBody List<MessageSendDTO> messageSendDTOS, String source) {
        boolean sendFlag = true;
        for (MessageSendDTO messageSendDTO : messageSendDTOS) {
            Integer messageType = messageSendDTO.getMessageType();
            Boolean handleConsumer = messageConsumerManager.handleConsumer(messageType, messageSendDTO);
            if (!handleConsumer) {
                sendFlag = false;
            }
        }
        return R.ok(sendFlag);
    }
}
