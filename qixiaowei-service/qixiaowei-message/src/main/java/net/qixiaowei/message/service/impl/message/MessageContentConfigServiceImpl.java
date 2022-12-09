package net.qixiaowei.message.service.impl.message;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.message.api.domain.message.MessageContentConfig;
import net.qixiaowei.message.api.dto.message.MessageContentConfigDTO;
import net.qixiaowei.message.mapper.message.MessageContentConfigMapper;
import net.qixiaowei.message.service.message.IMessageContentConfigService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
* MessageContentConfigService业务层处理
* @author hzk
* @since 2022-12-08
*/
@Service
public class MessageContentConfigServiceImpl implements IMessageContentConfigService{
    @Autowired
    private MessageContentConfigMapper messageContentConfigMapper;

    /**
    * 查询消息内容配置表
    *
    * @param messageContentConfigId 消息内容配置表主键
    * @return 消息内容配置表
    */
    @Override
    public MessageContentConfigDTO selectMessageContentConfigByMessageContentConfigId(Long messageContentConfigId)
    {
    return messageContentConfigMapper.selectMessageContentConfigByMessageContentConfigId(messageContentConfigId);
    }

    /**
    * 查询消息内容配置表列表
    *
    * @param messageContentConfigDTO 消息内容配置表
    * @return 消息内容配置表
    */
    @Override
    public List<MessageContentConfigDTO> selectMessageContentConfigList(MessageContentConfigDTO messageContentConfigDTO)
    {
    MessageContentConfig messageContentConfig=new MessageContentConfig();
    BeanUtils.copyProperties(messageContentConfigDTO,messageContentConfig);
    return messageContentConfigMapper.selectMessageContentConfigList(messageContentConfig);
    }

    /**
    * 新增消息内容配置表
    *
    * @param messageContentConfigDTO 消息内容配置表
    * @return 结果
    */
    @Override
    public MessageContentConfigDTO insertMessageContentConfig(MessageContentConfigDTO messageContentConfigDTO){
    MessageContentConfig messageContentConfig=new MessageContentConfig();
    BeanUtils.copyProperties(messageContentConfigDTO,messageContentConfig);
    messageContentConfig.setCreateBy(SecurityUtils.getUserId());
    messageContentConfig.setCreateTime(DateUtils.getNowDate());
    messageContentConfig.setUpdateTime(DateUtils.getNowDate());
    messageContentConfig.setUpdateBy(SecurityUtils.getUserId());
    messageContentConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    messageContentConfigMapper.insertMessageContentConfig(messageContentConfig);
    messageContentConfigDTO.setMessageContentConfigId(messageContentConfig.getMessageContentConfigId());
    return messageContentConfigDTO;
    }

    /**
    * 修改消息内容配置表
    *
    * @param messageContentConfigDTO 消息内容配置表
    * @return 结果
    */
    @Override
    public int updateMessageContentConfig(MessageContentConfigDTO messageContentConfigDTO)
    {
    MessageContentConfig messageContentConfig=new MessageContentConfig();
    BeanUtils.copyProperties(messageContentConfigDTO,messageContentConfig);
    messageContentConfig.setUpdateTime(DateUtils.getNowDate());
    messageContentConfig.setUpdateBy(SecurityUtils.getUserId());
    return messageContentConfigMapper.updateMessageContentConfig(messageContentConfig);
    }

    /**
    * 逻辑批量删除消息内容配置表
    *
    * @param messageContentConfigIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteMessageContentConfigByMessageContentConfigIds(List<Long> messageContentConfigIds){
    return messageContentConfigMapper.logicDeleteMessageContentConfigByMessageContentConfigIds(messageContentConfigIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除消息内容配置表信息
    *
    * @param messageContentConfigId 消息内容配置表主键
    * @return 结果
    */
    @Override
    public int deleteMessageContentConfigByMessageContentConfigId(Long messageContentConfigId)
    {
    return messageContentConfigMapper.deleteMessageContentConfigByMessageContentConfigId(messageContentConfigId);
    }

     /**
     * 逻辑删除消息内容配置表信息
     *
     * @param  messageContentConfigDTO 消息内容配置表
     * @return 结果
     */
     @Override
     public int logicDeleteMessageContentConfigByMessageContentConfigId(MessageContentConfigDTO messageContentConfigDTO)
     {
     MessageContentConfig messageContentConfig=new MessageContentConfig();
     messageContentConfig.setMessageContentConfigId(messageContentConfigDTO.getMessageContentConfigId());
     messageContentConfig.setUpdateTime(DateUtils.getNowDate());
     messageContentConfig.setUpdateBy(SecurityUtils.getUserId());
     return messageContentConfigMapper.logicDeleteMessageContentConfigByMessageContentConfigId(messageContentConfig);
     }

     /**
     * 物理删除消息内容配置表信息
     *
     * @param  messageContentConfigDTO 消息内容配置表
     * @return 结果
     */
     
     @Override
     public int deleteMessageContentConfigByMessageContentConfigId(MessageContentConfigDTO messageContentConfigDTO)
     {
     MessageContentConfig messageContentConfig=new MessageContentConfig();
     BeanUtils.copyProperties(messageContentConfigDTO,messageContentConfig);
     return messageContentConfigMapper.deleteMessageContentConfigByMessageContentConfigId(messageContentConfig.getMessageContentConfigId());
     }
     /**
     * 物理批量删除消息内容配置表
     *
     * @param messageContentConfigDtos 需要删除的消息内容配置表主键
     * @return 结果
     */
     
     @Override
     public int deleteMessageContentConfigByMessageContentConfigIds(List<MessageContentConfigDTO> messageContentConfigDtos){
     List<Long> stringList = new ArrayList();
     for (MessageContentConfigDTO messageContentConfigDTO : messageContentConfigDtos) {
     stringList.add(messageContentConfigDTO.getMessageContentConfigId());
     }
     return messageContentConfigMapper.deleteMessageContentConfigByMessageContentConfigIds(stringList);
     }

    /**
    * 批量新增消息内容配置表信息
    *
    * @param messageContentConfigDtos 消息内容配置表对象
    */
    
    public int insertMessageContentConfigs(List<MessageContentConfigDTO> messageContentConfigDtos){
      List<MessageContentConfig> messageContentConfigList = new ArrayList();

    for (MessageContentConfigDTO messageContentConfigDTO : messageContentConfigDtos) {
      MessageContentConfig messageContentConfig =new MessageContentConfig();
      BeanUtils.copyProperties(messageContentConfigDTO,messageContentConfig);
       messageContentConfig.setCreateBy(SecurityUtils.getUserId());
       messageContentConfig.setCreateTime(DateUtils.getNowDate());
       messageContentConfig.setUpdateTime(DateUtils.getNowDate());
       messageContentConfig.setUpdateBy(SecurityUtils.getUserId());
       messageContentConfig.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      messageContentConfigList.add(messageContentConfig);
    }
    return messageContentConfigMapper.batchMessageContentConfig(messageContentConfigList);
    }

    /**
    * 批量修改消息内容配置表信息
    *
    * @param messageContentConfigDtos 消息内容配置表对象
    */
    
    public int updateMessageContentConfigs(List<MessageContentConfigDTO> messageContentConfigDtos){
     List<MessageContentConfig> messageContentConfigList = new ArrayList();

     for (MessageContentConfigDTO messageContentConfigDTO : messageContentConfigDtos) {
     MessageContentConfig messageContentConfig =new MessageContentConfig();
     BeanUtils.copyProperties(messageContentConfigDTO,messageContentConfig);
        messageContentConfig.setCreateBy(SecurityUtils.getUserId());
        messageContentConfig.setCreateTime(DateUtils.getNowDate());
        messageContentConfig.setUpdateTime(DateUtils.getNowDate());
        messageContentConfig.setUpdateBy(SecurityUtils.getUserId());
     messageContentConfigList.add(messageContentConfig);
     }
     return messageContentConfigMapper.updateMessageContentConfigs(messageContentConfigList);
    }

}

