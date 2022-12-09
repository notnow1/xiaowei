package net.qixiaowei.message.logic.impl.message;

import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.message.api.domain.message.Message;
import net.qixiaowei.message.api.domain.message.MessageContentConfig;
import net.qixiaowei.message.api.domain.message.MessageReceive;
import net.qixiaowei.message.api.dto.message.MessageContentConfigDTO;
import net.qixiaowei.message.api.dto.message.MessageReceiverDTO;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.logic.IMessageSendStrategy;
import net.qixiaowei.message.mapper.message.MessageContentConfigMapper;
import net.qixiaowei.message.mapper.message.MessageMapper;
import net.qixiaowei.message.mapper.message.MessageReceiveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @description 站内消息发送实现类
 * @Author hzk
 * @Date 2022-12-09 16:45
 **/
@Service
@Slf4j
public class PrivateMessageSendImpl implements IMessageSendStrategy {


    @Autowired
    private MessageMapper messageMapper;

    @Autowired
    private MessageContentConfigMapper messageContentConfigMapper;

    @Autowired
    private MessageReceiveMapper messageReceiveMapper;

    @Override
    public MessageType getMessageType() {
        return MessageType.PRIVATE_MESSAGE;
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
                }
            }
            Date nowDate = DateUtils.getNowDate();
            Long userId = SecurityUtils.getUserId();
            Message message = new Message();
            BeanUtils.copyProperties(messageSendDTO, message);
            message.setMessageContent(messageContent);
            message.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            message.setCreateBy(userId);
            message.setCreateTime(nowDate);
            message.setUpdateTime(nowDate);
            message.setUpdateBy(userId);
            //保存消息主体
            messageMapper.insertMessage(message);
            List<MessageReceive> messageReceiveList = new ArrayList<>();
            Long messageId = message.getMessageId();
            for (MessageReceiverDTO messageReceiverDTO : messageReceivers) {
                MessageReceive messageReceive = new MessageReceive();
                messageReceive.setMessageId(messageId);
                messageReceive.setUserId(messageReceiverDTO.getUserId());
                messageReceive.setCreateBy(userId);
                messageReceive.setCreateTime(nowDate);
                messageReceive.setUpdateTime(nowDate);
                messageReceive.setUpdateBy(userId);
                messageReceive.setStatus(BusinessConstants.DISABLE);
                messageReceive.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                messageReceiveList.add(messageReceive);
            }
            //保存消息接收集合
            messageReceiveMapper.batchMessageReceive(messageReceiveList);
        } catch (
                Exception e) {
            log.error("站内信处理失败:{}", e.getMessage());
            handleFlag = false;
        }
        return handleFlag;
    }
}
