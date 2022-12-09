package net.qixiaowei.message.logic.impl.message;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.message.api.domain.message.MessageContentConfig;
import net.qixiaowei.message.api.dto.message.MessageContentConfigDTO;
import net.qixiaowei.message.api.dto.message.MessageReceiverDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.logic.IMessageSendStrategy;
import net.qixiaowei.message.mapper.message.MessageContentConfigMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


/**
 * @description 邮件消息发送实现类
 * @Author hzk
 * @Date 2022-12-09 17:55
 **/
@Service
@Slf4j
public class EmailSendImpl implements IMessageSendStrategy {


    @Autowired
    private MessageContentConfigMapper messageContentConfigMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.EMAIL;
    }

    @Override
    public Boolean consumer(MessageSendDTO messageSendDTO) {
        boolean handleFlag = true;
        try {
            Boolean handleContent = messageSendDTO.getHandleContent();
            String messageContent = messageSendDTO.getMessageContent();
            String messageParam = messageSendDTO.getMessageParam();
            List<MessageReceiverDTO> messageReceivers = messageSendDTO.getMessageReceivers();
            //如果需要处理内容且发送参数不为空，则找到模版去处理
            if (handleContent) {
                MessageContentConfig messageContentConfig = new MessageContentConfig();
                messageContentConfig.setMessageType(messageSendDTO.getMessageType());
                messageContentConfig.setBusinessType(messageSendDTO.getBusinessType());
                messageContentConfig.setBusinessSubtype(messageSendDTO.getBusinessSubtype());
                messageContentConfig.setReceiveRole(messageSendDTO.getReceiveRole());
                List<MessageContentConfigDTO> messageContentConfigDTOS = messageContentConfigMapper.selectMessageContentConfigList(messageContentConfig);
                if (StringUtils.isNotEmpty(messageContentConfigDTOS)) {
                    MessageContentConfigDTO messageContentConfigDTO = messageContentConfigDTOS.get(0);
                    String messageTemplate = messageContentConfigDTO.getMessageTemplate();
                    if (StringUtils.isNotEmpty(messageTemplate) && StringUtils.isNotEmpty(messageParam)) {
                        messageContent = StrUtil.format(messageTemplate, JSONUtil.toBean(messageParam, Map.class));
                    }
                    String receiveUser = messageContentConfigDTO.getReceiveUser();
                    if (StringUtils.isNotEmpty(receiveUser)) {
                        List<String> receiveUsers = StrUtil.splitTrim(receiveUser, StrUtil.COMMA, -1);
                        if (StringUtils.isNotEmpty(receiveUsers)) {
                            List<MessageReceiverDTO> extUsers = receiveUsers.stream()
                                    .map((item) -> {
                                        MessageReceiverDTO messageReceiverDTO = new MessageReceiverDTO();
                                        messageReceiverDTO.setUser(item);
                                        return messageReceiverDTO;
                                    }).collect(Collectors.toList());

                            if (StringUtils.isEmpty(messageReceivers)) {
                                messageReceivers = new ArrayList<>();
                            }
                            if (StringUtils.isNotEmpty(extUsers)) {
                                messageReceivers.addAll(extUsers);
                            }
                        }
                    }
                }
            }
            //发送email
        } catch (Exception e) {
            log.error("邮件处理失败:{}", e.getMessage());
            handleFlag = false;
        }
        return handleFlag;
    }
}