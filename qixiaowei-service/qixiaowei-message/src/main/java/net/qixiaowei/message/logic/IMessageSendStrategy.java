package net.qixiaowei.message.logic;

import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;

/**
 * @description 消息处理策略
 * @Author hzk
 * @Date 2022-12-09 16:36
 **/
public interface IMessageSendStrategy {

    /**
     * 属于哪种消息类型
     *
     * @return {@link MessageType}
     */
    MessageType getMessageType();


    /**
     * 处理各自消息类型的消费
     *
     * @param messageSendDTO 消费信息
     */
    Boolean consumer(MessageSendDTO messageSendDTO);
}
