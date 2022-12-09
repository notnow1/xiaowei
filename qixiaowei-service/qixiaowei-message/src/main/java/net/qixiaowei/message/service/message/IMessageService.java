package net.qixiaowei.message.service.message;

import java.util.List;
import net.qixiaowei.message.api.dto.message.MessageDTO;


/**
* MessageService接口
* @author hzk
* @since 2022-12-09
*/
public interface IMessageService{
    /**
    * 查询消息表
    *
    * @param messageId 消息表主键
    * @return 消息表
    */
    MessageDTO selectMessageByMessageId(Long messageId);

    /**
    * 查询消息表列表
    *
    * @param messageDTO 消息表
    * @return 消息表集合
    */
    List<MessageDTO> selectMessageList(MessageDTO messageDTO);

    /**
    * 新增消息表
    *
    * @param messageDTO 消息表
    * @return 结果
    */
    MessageDTO insertMessage(MessageDTO messageDTO);

    /**
    * 修改消息表
    *
    * @param messageDTO 消息表
    * @return 结果
    */
    int updateMessage(MessageDTO messageDTO);

    /**
    * 批量修改消息表
    *
    * @param messageDtos 消息表
    * @return 结果
    */
    int updateMessages(List<MessageDTO> messageDtos);

    /**
    * 批量新增消息表
    *
    * @param messageDtos 消息表
    * @return 结果
    */
    int insertMessages(List<MessageDTO> messageDtos);

    /**
    * 逻辑批量删除消息表
    *
    * @param messageIds 需要删除的消息表集合
    * @return 结果
    */
    int logicDeleteMessageByMessageIds(List<Long> messageIds);

    /**
    * 逻辑删除消息表信息
    *
    * @param messageDTO
    * @return 结果
    */
    int logicDeleteMessageByMessageId(MessageDTO messageDTO);
    /**
    * 批量删除消息表
    *
    * @param MessageDtos
    * @return 结果
    */
    int deleteMessageByMessageIds(List<MessageDTO> MessageDtos);

    /**
    * 逻辑删除消息表信息
    *
    * @param messageDTO
    * @return 结果
    */
    int deleteMessageByMessageId(MessageDTO messageDTO);


    /**
    * 删除消息表信息
    *
    * @param messageId 消息表主键
    * @return 结果
    */
    int deleteMessageByMessageId(Long messageId);

}
