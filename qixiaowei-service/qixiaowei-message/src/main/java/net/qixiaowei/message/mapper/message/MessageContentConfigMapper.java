package net.qixiaowei.message.mapper.message;

import java.util.List;
import net.qixiaowei.message.api.domain.message.MessageContentConfig;
import net.qixiaowei.message.api.dto.message.MessageContentConfigDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MessageContentConfigMapper接口
* @author hzk
* @since 2022-12-08
*/
public interface MessageContentConfigMapper{
    /**
    * 查询消息内容配置表
    *
    * @param messageContentConfigId 消息内容配置表主键
    * @return 消息内容配置表
    */
    MessageContentConfigDTO selectMessageContentConfigByMessageContentConfigId(@Param("messageContentConfigId")Long messageContentConfigId);


    /**
    * 批量查询消息内容配置表
    *
    * @param messageContentConfigIds 消息内容配置表主键集合
    * @return 消息内容配置表
    */
    List<MessageContentConfigDTO> selectMessageContentConfigByMessageContentConfigIds(@Param("messageContentConfigIds") List<Long> messageContentConfigIds);

    /**
    * 查询消息内容配置表列表
    *
    * @param messageContentConfig 消息内容配置表
    * @return 消息内容配置表集合
    */
    List<MessageContentConfigDTO> selectMessageContentConfigList(@Param("messageContentConfig")MessageContentConfig messageContentConfig);

    /**
    * 新增消息内容配置表
    *
    * @param messageContentConfig 消息内容配置表
    * @return 结果
    */
    int insertMessageContentConfig(@Param("messageContentConfig")MessageContentConfig messageContentConfig);

    /**
    * 修改消息内容配置表
    *
    * @param messageContentConfig 消息内容配置表
    * @return 结果
    */
    int updateMessageContentConfig(@Param("messageContentConfig")MessageContentConfig messageContentConfig);

    /**
    * 批量修改消息内容配置表
    *
    * @param messageContentConfigList 消息内容配置表
    * @return 结果
    */
    int updateMessageContentConfigs(@Param("messageContentConfigList")List<MessageContentConfig> messageContentConfigList);
    /**
    * 逻辑删除消息内容配置表
    *
    * @param messageContentConfig
    * @return 结果
    */
    int logicDeleteMessageContentConfigByMessageContentConfigId(@Param("messageContentConfig")MessageContentConfig messageContentConfig);

    /**
    * 逻辑批量删除消息内容配置表
    *
    * @param messageContentConfigIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMessageContentConfigByMessageContentConfigIds(@Param("messageContentConfigIds")List<Long> messageContentConfigIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除消息内容配置表
    *
    * @param messageContentConfigId 消息内容配置表主键
    * @return 结果
    */
    int deleteMessageContentConfigByMessageContentConfigId(@Param("messageContentConfigId")Long messageContentConfigId);

    /**
    * 物理批量删除消息内容配置表
    *
    * @param messageContentConfigIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMessageContentConfigByMessageContentConfigIds(@Param("messageContentConfigIds")List<Long> messageContentConfigIds);

    /**
    * 批量新增消息内容配置表
    *
    * @param MessageContentConfigs 消息内容配置表列表
    * @return 结果
    */
    int batchMessageContentConfig(@Param("messageContentConfigs")List<MessageContentConfig> MessageContentConfigs);
}
