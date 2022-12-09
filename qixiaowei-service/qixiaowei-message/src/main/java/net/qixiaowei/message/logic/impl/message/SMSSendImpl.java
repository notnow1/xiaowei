package net.qixiaowei.message.logic.impl.message;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.logic.IMessageSendStrategy;
import org.springframework.stereotype.Service;


/**
 * @description 短信发送实现类
 * @Author hzk
 * @Date 2022-12-09 17:45
 **/
@Service
@Slf4j
public class SMSSendImpl implements IMessageSendStrategy {


    @Override
    public MessageType getMessageType() {
        return MessageType.SMS;
    }

    @Override
    public Boolean consumer(MessageSendDTO messageSendDTO) {
        boolean handleFlag = true;
        try {

        } catch (Exception e) {
            log.error("短信处理失败:{}", e.getMessage());
            handleFlag = false;
        }
        return handleFlag;
    }
}
