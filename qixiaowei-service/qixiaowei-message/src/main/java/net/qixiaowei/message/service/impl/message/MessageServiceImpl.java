package net.qixiaowei.message.service.impl.message;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.message.api.domain.message.Message;
import net.qixiaowei.message.api.dto.message.MessageDTO;
import net.qixiaowei.message.mapper.message.MessageMapper;
import net.qixiaowei.message.service.message.IMessageService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* MessageService业务层处理
* @author hzk
* @since 2022-12-09
*/
@Service
public class MessageServiceImpl implements IMessageService{
    @Autowired
    private MessageMapper messageMapper;

    /**
    * 查询消息表
    *
    * @param messageId 消息表主键
    * @return 消息表
    */
    @Override
    public MessageDTO selectMessageByMessageId(Long messageId)
    {
    return messageMapper.selectMessageByMessageId(messageId);
    }

    /**
    * 查询消息表列表
    *
    * @param messageDTO 消息表
    * @return 消息表
    */
    @Override
    public List<MessageDTO> selectMessageList(MessageDTO messageDTO)
    {
    Message message=new Message();
    BeanUtils.copyProperties(messageDTO,message);
    return messageMapper.selectMessageList(message);
    }

    /**
    * 新增消息表
    *
    * @param messageDTO 消息表
    * @return 结果
    */
    @Override
    public MessageDTO insertMessage(MessageDTO messageDTO){
    Message message=new Message();
    BeanUtils.copyProperties(messageDTO,message);
    message.setCreateBy(SecurityUtils.getUserId());
    message.setCreateTime(DateUtils.getNowDate());
    message.setUpdateTime(DateUtils.getNowDate());
    message.setUpdateBy(SecurityUtils.getUserId());
    message.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    messageMapper.insertMessage(message);
    messageDTO.setMessageId(message.getMessageId());
    return messageDTO;
    }

    /**
    * 修改消息表
    *
    * @param messageDTO 消息表
    * @return 结果
    */
    @Override
    public int updateMessage(MessageDTO messageDTO)
    {
    Message message=new Message();
    BeanUtils.copyProperties(messageDTO,message);
    message.setUpdateTime(DateUtils.getNowDate());
    message.setUpdateBy(SecurityUtils.getUserId());
    return messageMapper.updateMessage(message);
    }

    /**
    * 逻辑批量删除消息表
    *
    * @param messageIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteMessageByMessageIds(List<Long> messageIds){
    return messageMapper.logicDeleteMessageByMessageIds(messageIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除消息表信息
    *
    * @param messageId 消息表主键
    * @return 结果
    */
    @Override
    public int deleteMessageByMessageId(Long messageId)
    {
    return messageMapper.deleteMessageByMessageId(messageId);
    }

     /**
     * 逻辑删除消息表信息
     *
     * @param  messageDTO 消息表
     * @return 结果
     */
     @Override
     public int logicDeleteMessageByMessageId(MessageDTO messageDTO)
     {
     Message message=new Message();
     message.setMessageId(messageDTO.getMessageId());
     message.setUpdateTime(DateUtils.getNowDate());
     message.setUpdateBy(SecurityUtils.getUserId());
     return messageMapper.logicDeleteMessageByMessageId(message);
     }

     /**
     * 物理删除消息表信息
     *
     * @param  messageDTO 消息表
     * @return 结果
     */
     
     @Override
     public int deleteMessageByMessageId(MessageDTO messageDTO)
     {
     Message message=new Message();
     BeanUtils.copyProperties(messageDTO,message);
     return messageMapper.deleteMessageByMessageId(message.getMessageId());
     }
     /**
     * 物理批量删除消息表
     *
     * @param messageDtos 需要删除的消息表主键
     * @return 结果
     */
     
     @Override
     public int deleteMessageByMessageIds(List<MessageDTO> messageDtos){
     List<Long> stringList = new ArrayList();
     for (MessageDTO messageDTO : messageDtos) {
     stringList.add(messageDTO.getMessageId());
     }
     return messageMapper.deleteMessageByMessageIds(stringList);
     }

    /**
    * 批量新增消息表信息
    *
    * @param messageDtos 消息表对象
    */
    
    public int insertMessages(List<MessageDTO> messageDtos){
      List<Message> messageList = new ArrayList();

    for (MessageDTO messageDTO : messageDtos) {
      Message message =new Message();
      BeanUtils.copyProperties(messageDTO,message);
       message.setCreateBy(SecurityUtils.getUserId());
       message.setCreateTime(DateUtils.getNowDate());
       message.setUpdateTime(DateUtils.getNowDate());
       message.setUpdateBy(SecurityUtils.getUserId());
       message.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      messageList.add(message);
    }
    return messageMapper.batchMessage(messageList);
    }

    /**
    * 批量修改消息表信息
    *
    * @param messageDtos 消息表对象
    */
    
    public int updateMessages(List<MessageDTO> messageDtos){
     List<Message> messageList = new ArrayList();

     for (MessageDTO messageDTO : messageDtos) {
     Message message =new Message();
     BeanUtils.copyProperties(messageDTO,message);
        message.setCreateBy(SecurityUtils.getUserId());
        message.setCreateTime(DateUtils.getNowDate());
        message.setUpdateTime(DateUtils.getNowDate());
        message.setUpdateBy(SecurityUtils.getUserId());
     messageList.add(message);
     }
     return messageMapper.updateMessages(messageList);
    }

}

