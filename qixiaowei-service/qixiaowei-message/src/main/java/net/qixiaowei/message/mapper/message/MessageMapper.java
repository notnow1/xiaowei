package net.qixiaowei.message.mapper.message;

import java.util.List;
import net.qixiaowei.message.api.domain.message.Message;
import net.qixiaowei.message.api.dto.message.MessageDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MessageMapper接口
* @author hzk
* @since 2022-12-09
*/
public interface MessageMapper{
    /**
    * 查询消息表
    *
    * @param messageId 消息表主键
    * @return 消息表
    */
    MessageDTO selectMessageByMessageId(@Param("messageId")Long messageId);


    /**
    * 批量查询消息表
    *
    * @param messageIds 消息表主键集合
    * @return 消息表
    */
    List<MessageDTO> selectMessageByMessageIds(@Param("messageIds") List<Long> messageIds);

    /**
    * 查询消息表列表
    *
    * @param message 消息表
    * @return 消息表集合
    */
    List<MessageDTO> selectMessageList(@Param("message")Message message);

    /**
    * 新增消息表
    *
    * @param message 消息表
    * @return 结果
    */
    int insertMessage(@Param("message")Message message);

    /**
    * 修改消息表
    *
    * @param message 消息表
    * @return 结果
    */
    int updateMessage(@Param("message")Message message);

    /**
    * 批量修改消息表
    *
    * @param messageList 消息表
    * @return 结果
    */
    int updateMessages(@Param("messageList")List<Message> messageList);
    /**
    * 逻辑删除消息表
    *
    * @param message
    * @return 结果
    */
    int logicDeleteMessageByMessageId(@Param("message")Message message);

    /**
    * 逻辑批量删除消息表
    *
    * @param messageIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMessageByMessageIds(@Param("messageIds")List<Long> messageIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除消息表
    *
    * @param messageId 消息表主键
    * @return 结果
    */
    int deleteMessageByMessageId(@Param("messageId")Long messageId);

    /**
    * 物理批量删除消息表
    *
    * @param messageIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMessageByMessageIds(@Param("messageIds")List<Long> messageIds);

    /**
    * 批量新增消息表
    *
    * @param Messages 消息表列表
    * @return 结果
    */
    int batchMessage(@Param("messages")List<Message> Messages);
}
