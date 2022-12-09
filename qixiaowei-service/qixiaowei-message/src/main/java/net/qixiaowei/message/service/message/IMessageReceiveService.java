package net.qixiaowei.message.service.message;

import java.util.List;
import net.qixiaowei.message.api.dto.message.MessageReceiveDTO;


/**
* MessageReceiveService接口
* @author hzk
* @since 2022-12-08
*/
public interface IMessageReceiveService{
    /**
    * 查询消息接收表
    *
    * @param messageReceiveId 消息接收表主键
    * @return 消息接收表
    */
    MessageReceiveDTO selectMessageReceiveByMessageReceiveId(Long messageReceiveId);

    /**
    * 查询消息接收表列表
    *
    * @param messageReceiveDTO 消息接收表
    * @return 消息接收表集合
    */
    List<MessageReceiveDTO> selectMessageReceiveList(MessageReceiveDTO messageReceiveDTO);

    /**
    * 新增消息接收表
    *
    * @param messageReceiveDTO 消息接收表
    * @return 结果
    */
    MessageReceiveDTO insertMessageReceive(MessageReceiveDTO messageReceiveDTO);

    /**
    * 修改消息接收表
    *
    * @param messageReceiveDTO 消息接收表
    * @return 结果
    */
    int updateMessageReceive(MessageReceiveDTO messageReceiveDTO);

    /**
     * 修改已读消息
     *
     * @param messageReceiveDTO 消息接收表
     * @return 结果
     */
    int read(MessageReceiveDTO messageReceiveDTO);

    /**
     * 修改所有已读消息
     *
     * @return 结果
     */
    int readAll();

    /**
    * 批量修改消息接收表
    *
    * @param messageReceiveDtos 消息接收表
    * @return 结果
    */
    int updateMessageReceives(List<MessageReceiveDTO> messageReceiveDtos);

    /**
    * 批量新增消息接收表
    *
    * @param messageReceiveDtos 消息接收表
    * @return 结果
    */
    int insertMessageReceives(List<MessageReceiveDTO> messageReceiveDtos);

    /**
    * 逻辑批量删除消息接收表
    *
    * @param messageReceiveIds 需要删除的消息接收表集合
    * @return 结果
    */
    int logicDeleteMessageReceiveByMessageReceiveIds(List<Long> messageReceiveIds);

    /**
    * 逻辑删除消息接收表信息
    *
    * @param messageReceiveDTO
    * @return 结果
    */
    int logicDeleteMessageReceiveByMessageReceiveId(MessageReceiveDTO messageReceiveDTO);
    /**
    * 批量删除消息接收表
    *
    * @param MessageReceiveDtos
    * @return 结果
    */
    int deleteMessageReceiveByMessageReceiveIds(List<MessageReceiveDTO> MessageReceiveDtos);

    /**
    * 逻辑删除消息接收表信息
    *
    * @param messageReceiveDTO
    * @return 结果
    */
    int deleteMessageReceiveByMessageReceiveId(MessageReceiveDTO messageReceiveDTO);


    /**
    * 删除消息接收表信息
    *
    * @param messageReceiveId 消息接收表主键
    * @return 结果
    */
    int deleteMessageReceiveByMessageReceiveId(Long messageReceiveId);

}
