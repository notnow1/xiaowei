package net.qixiaowei.message.service.message;

import java.util.List;
import net.qixiaowei.message.api.dto.message.MessageContentConfigDTO;


/**
* MessageContentConfigService接口
* @author hzk
* @since 2022-12-08
*/
public interface IMessageContentConfigService{
    /**
    * 查询消息内容配置表
    *
    * @param messageContentConfigId 消息内容配置表主键
    * @return 消息内容配置表
    */
    MessageContentConfigDTO selectMessageContentConfigByMessageContentConfigId(Long messageContentConfigId);

    /**
    * 查询消息内容配置表列表
    *
    * @param messageContentConfigDTO 消息内容配置表
    * @return 消息内容配置表集合
    */
    List<MessageContentConfigDTO> selectMessageContentConfigList(MessageContentConfigDTO messageContentConfigDTO);

    /**
    * 新增消息内容配置表
    *
    * @param messageContentConfigDTO 消息内容配置表
    * @return 结果
    */
    MessageContentConfigDTO insertMessageContentConfig(MessageContentConfigDTO messageContentConfigDTO);

    /**
    * 修改消息内容配置表
    *
    * @param messageContentConfigDTO 消息内容配置表
    * @return 结果
    */
    int updateMessageContentConfig(MessageContentConfigDTO messageContentConfigDTO);

    /**
    * 批量修改消息内容配置表
    *
    * @param messageContentConfigDtos 消息内容配置表
    * @return 结果
    */
    int updateMessageContentConfigs(List<MessageContentConfigDTO> messageContentConfigDtos);

    /**
    * 批量新增消息内容配置表
    *
    * @param messageContentConfigDtos 消息内容配置表
    * @return 结果
    */
    int insertMessageContentConfigs(List<MessageContentConfigDTO> messageContentConfigDtos);

    /**
    * 逻辑批量删除消息内容配置表
    *
    * @param messageContentConfigIds 需要删除的消息内容配置表集合
    * @return 结果
    */
    int logicDeleteMessageContentConfigByMessageContentConfigIds(List<Long> messageContentConfigIds);

    /**
    * 逻辑删除消息内容配置表信息
    *
    * @param messageContentConfigDTO
    * @return 结果
    */
    int logicDeleteMessageContentConfigByMessageContentConfigId(MessageContentConfigDTO messageContentConfigDTO);
    /**
    * 批量删除消息内容配置表
    *
    * @param MessageContentConfigDtos
    * @return 结果
    */
    int deleteMessageContentConfigByMessageContentConfigIds(List<MessageContentConfigDTO> MessageContentConfigDtos);

    /**
    * 逻辑删除消息内容配置表信息
    *
    * @param messageContentConfigDTO
    * @return 结果
    */
    int deleteMessageContentConfigByMessageContentConfigId(MessageContentConfigDTO messageContentConfigDTO);


    /**
    * 删除消息内容配置表信息
    *
    * @param messageContentConfigId 消息内容配置表主键
    * @return 结果
    */
    int deleteMessageContentConfigByMessageContentConfigId(Long messageContentConfigId);

}
