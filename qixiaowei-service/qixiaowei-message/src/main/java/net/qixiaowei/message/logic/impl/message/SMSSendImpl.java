package net.qixiaowei.message.logic.impl.message;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.message.api.domain.message.MessageContentConfig;
import net.qixiaowei.message.api.dto.message.MessageContentConfigDTO;
import net.qixiaowei.message.api.dto.message.MessageReceiverDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.config.AliyunSMSConfig;
import net.qixiaowei.message.logic.IMessageSendStrategy;
import net.qixiaowei.message.mapper.message.MessageContentConfigMapper;
import net.qixiaowei.message.util.SendSMSUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * @description 短信发送实现类
 * @Author hzk
 * @Date 2022-12-09 17:45
 **/
@Service
@Slf4j
public class SMSSendImpl implements IMessageSendStrategy {

    @Autowired
    private MessageContentConfigMapper messageContentConfigMapper;

    @Autowired
    private AliyunSMSConfig aliyunSMSConfig;

    @Override
    public MessageType getMessageType() {
        return MessageType.SMS;
    }

    @Override
    public Boolean consumer(MessageSendDTO messageSendDTO) {
        boolean handleFlag = true;
        try {
            Boolean handleContent = messageSendDTO.getHandleContent();
            String messageContent = messageSendDTO.getMessageContent();
            String messageParam = messageSendDTO.getMessageParam();
            Integer messageType = messageSendDTO.getMessageType();
            Integer businessType = messageSendDTO.getBusinessType();
            Integer businessSubtype = messageSendDTO.getBusinessSubtype();
            Integer receiveRole = messageSendDTO.getReceiveRole();
            List<MessageReceiverDTO> messageReceivers = messageSendDTO.getMessageReceivers();
            //如果需要处理内容，则找到模版去处理
            if (handleContent) {
                MessageContentConfig messageContentConfig = new MessageContentConfig();
                messageContentConfig.setMessageType(messageType);
                messageContentConfig.setBusinessType(businessType);
                messageContentConfig.setBusinessSubtype(businessSubtype);
                messageContentConfig.setReceiveRole(receiveRole);
                List<MessageContentConfigDTO> messageContentConfigDTOS = messageContentConfigMapper.selectMessageContentConfigList(messageContentConfig);
                if (StringUtils.isNotEmpty(messageContentConfigDTOS)) {
                    MessageContentConfigDTO messageContentConfigDTO = messageContentConfigDTOS.get(0);
                    String messageTemplate = messageContentConfigDTO.getMessageTemplate();
                    if (StringUtils.isNotEmpty(messageTemplate)) {
                        messageContent = messageTemplate;
                    }
                }
            }
            if (StringUtils.isNotEmpty(messageReceivers)) {
                List<String> receiveUsers = new ArrayList<>();
                for (MessageReceiverDTO messageReceiver : messageReceivers) {
                    String user = messageReceiver.getUser();
                    receiveUsers.add(user);
                }
                //发送短信
                handleFlag = SendSMSUtils.sendSMS(aliyunSMSConfig, receiveUsers, messageContent, messageParam);
            }
        } catch (Exception e) {
            log.error("短信处理失败:{}", e.getMessage());
            handleFlag = false;
        }
        return handleFlag;
    }
}
