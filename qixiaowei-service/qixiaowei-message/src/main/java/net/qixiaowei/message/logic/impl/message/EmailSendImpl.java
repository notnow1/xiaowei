package net.qixiaowei.message.logic.impl.message;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.mail.MailAccount;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.message.api.domain.message.MessageContentConfig;
import net.qixiaowei.message.api.dto.message.MessageContentConfigDTO;
import net.qixiaowei.message.api.dto.message.MessageReceiverDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.config.MailboxConfig;
import net.qixiaowei.message.logic.IMessageSendStrategy;
import net.qixiaowei.message.mapper.message.MessageContentConfigMapper;
import net.qixiaowei.message.util.SendMailUtils;
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

    @Autowired
    private MailboxConfig mailboxConfig;

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
            String messageTitle = messageSendDTO.getMessageTitle();
            Integer messageType = messageSendDTO.getMessageType();
            Integer businessType = messageSendDTO.getBusinessType();
            Integer businessSubtype = messageSendDTO.getBusinessSubtype();
            Integer receiveRole = messageSendDTO.getReceiveRole();
            List<MessageReceiverDTO> messageReceivers = messageSendDTO.getMessageReceivers();
            Boolean isHtml = messageSendDTO.getIsHtml();
            //如果需要处理内容且发送参数不为空，则找到模版去处理
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
            if (StringUtils.isNotEmpty(messageReceivers)) {
                List<String> receiveUsers = new ArrayList<>();
                for (MessageReceiverDTO messageReceiver : messageReceivers) {
                    String user = messageReceiver.getUser();
                    receiveUsers.add(user);
                }
                MailAccount account = new MailAccount();
                account.setSslEnable(true);
                account.setAuth(true);
                account.setHost(mailboxConfig.getHost());
                account.setPort(mailboxConfig.getPort());
                account.setFrom(mailboxConfig.getAccount());
                account.setPass(mailboxConfig.getPassword());
                //发送email
                handleFlag = SendMailUtils.sendEmail(account, receiveUsers, messageTitle, messageContent, isHtml);
            }
        } catch (Exception e) {
            log.error("邮件处理失败:{}", e.getMessage());
            handleFlag = false;
        }
        return handleFlag;
    }
}