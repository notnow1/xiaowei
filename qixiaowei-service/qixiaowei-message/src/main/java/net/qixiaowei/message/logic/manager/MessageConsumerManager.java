package net.qixiaowei.message.logic.manager;

import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.enums.message.MessageType;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.message.api.dto.message.MessageSendDTO;
import net.qixiaowei.message.logic.IMessageSendStrategy;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @description 消息处理管理
 * @Author hzk
 * @Date 2022-12-09 16:41
 **/
@Component
@Slf4j
public class MessageConsumerManager implements ApplicationContextAware {

    private final Map<MessageType, IMessageSendStrategy> messageStrategyMap = new ConcurrentHashMap<>();

    /**
     * 处理消费
     *
     * @param messageType    消息类型
     * @param messageSendDTO 消息体
     */
    public Boolean handleConsumer(Integer messageType, MessageSendDTO messageSendDTO) {
        if (StringUtils.isNull(messageType)) {
            throw new ServiceException("消息类型不能为空");
        }
        MessageType messageTypeEnum = MessageType.getMessageType(messageType);
        if (StringUtils.isNull(messageTypeEnum)) {
            throw new ServiceException("消息类型错误");
        }
        IMessageSendStrategy messageSendStrategy = messageStrategyMap.get(messageTypeEnum);
        return messageSendStrategy.consumer(messageSendDTO);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        Map<String, IMessageSendStrategy> strategyMap = applicationContext.getBeansOfType(IMessageSendStrategy.class);
        strategyMap.values().forEach(strategyService -> {
            messageStrategyMap.put(strategyService.getMessageType(), strategyService);
        });
    }
}
