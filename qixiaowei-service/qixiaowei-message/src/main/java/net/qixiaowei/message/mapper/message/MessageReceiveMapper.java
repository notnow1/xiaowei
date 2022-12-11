package net.qixiaowei.message.mapper.message;

import java.util.List;
import net.qixiaowei.message.api.domain.message.MessageReceive;
import net.qixiaowei.message.api.dto.message.MessageReceiveDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MessageReceiveMapper接口
* @author hzk
* @since 2022-12-08
*/
public interface MessageReceiveMapper{
    /**
    * 查询消息接收表
    *
    * @param messageReceiveId 消息接收表主键
    * @return 消息接收表
    */
    MessageReceiveDTO selectMessageReceiveByMessageReceiveId(@Param("messageReceiveId")Long messageReceiveId);

    /**
     * 统计未读消息
     *
     * @param userId 用户ID
     * @return 未读消息数量
     */
    Integer countUnreadMessage(@Param("userId")Long userId);

    /**
    * 批量查询消息接收表
    *
    * @param messageReceiveIds 消息接收表主键集合
    * @return 消息接收表
    */
    List<MessageReceiveDTO> selectMessageReceiveByMessageReceiveIds(@Param("messageReceiveIds") List<Long> messageReceiveIds);

    /**
    * 查询消息接收表列表
    *
    * @param messageReceive 消息接收表
    * @return 消息接收表集合
    */
    List<MessageReceiveDTO> selectMessageReceiveList(@Param("messageReceive")MessageReceive messageReceive);


    /**
     * 查询用户未读消息接收表列表
     *
     * @param userId 用户ID
     * @return 消息接收表集合
     */
    List<MessageReceiveDTO> selectUserUnReadMessageReceiveList(@Param("userId")Long userId);

    /**
    * 新增消息接收表
    *
    * @param messageReceive 消息接收表
    * @return 结果
    */
    int insertMessageReceive(@Param("messageReceive")MessageReceive messageReceive);

    /**
    * 修改消息接收表
    *
    * @param messageReceive 消息接收表
    * @return 结果
    */
    int updateMessageReceive(@Param("messageReceive")MessageReceive messageReceive);

    /**
    * 批量修改消息接收表
    *
    * @param messageReceiveList 消息接收表
    * @return 结果
    */
    int updateMessageReceives(@Param("messageReceiveList")List<MessageReceive> messageReceiveList);
    /**
    * 逻辑删除消息接收表
    *
    * @param messageReceive
    * @return 结果
    */
    int logicDeleteMessageReceiveByMessageReceiveId(@Param("messageReceive")MessageReceive messageReceive);

    /**
    * 逻辑批量删除消息接收表
    *
    * @param messageReceiveIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMessageReceiveByMessageReceiveIds(@Param("messageReceiveIds")List<Long> messageReceiveIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除消息接收表
    *
    * @param messageReceiveId 消息接收表主键
    * @return 结果
    */
    int deleteMessageReceiveByMessageReceiveId(@Param("messageReceiveId")Long messageReceiveId);

    /**
    * 物理批量删除消息接收表
    *
    * @param messageReceiveIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMessageReceiveByMessageReceiveIds(@Param("messageReceiveIds")List<Long> messageReceiveIds);

    /**
    * 批量新增消息接收表
    *
    * @param MessageReceives 消息接收表列表
    * @return 结果
    */
    int batchMessageReceive(@Param("messageReceives")List<MessageReceive> MessageReceives);
}
