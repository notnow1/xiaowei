package net.qixiaowei.message.service.impl.message;

import java.util.Date;
import java.util.List;

import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.message.api.domain.message.MessageReceive;
import net.qixiaowei.message.api.dto.message.MessageReceiveDTO;
import net.qixiaowei.message.mapper.message.MessageReceiveMapper;
import net.qixiaowei.message.service.message.IMessageReceiveService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
 * MessageReceiveService业务层处理
 *
 * @author hzk
 * @since 2022-12-08
 */
@Service
public class MessageReceiveServiceImpl implements IMessageReceiveService {
    @Autowired
    private MessageReceiveMapper messageReceiveMapper;

    /**
     * 查询消息接收表
     *
     * @param messageReceiveId 消息接收表主键
     * @return 消息接收表
     */
    @Override
    public MessageReceiveDTO selectMessageReceiveByMessageReceiveId(Long messageReceiveId) {
        return messageReceiveMapper.selectMessageReceiveByMessageReceiveId(messageReceiveId);
    }

    /**
     * 查询消息接收表列表
     *
     * @param messageReceiveDTO 消息接收表
     * @return 消息接收表
     */
    @Override
    public List<MessageReceiveDTO> selectMessageReceiveList(MessageReceiveDTO messageReceiveDTO) {
        MessageReceive messageReceive = new MessageReceive();
        BeanUtils.copyProperties(messageReceiveDTO, messageReceive);
        Long userId = SecurityUtils.getUserId();
        messageReceive.setUserId(userId);
        String messageTitle = messageReceiveDTO.getMessageTitle();
        Map<String, Object> params = messageReceive.getParams();
        if (StringUtils.isNotEmpty(messageTitle)) {
            params.put("messageTitle", messageTitle);
        }
        messageReceive.setParams(params);
        return messageReceiveMapper.selectMessageReceiveList(messageReceive);
    }

    /**
     * 新增消息接收表
     *
     * @param messageReceiveDTO 消息接收表
     * @return 结果
     */
    @Override
    public MessageReceiveDTO insertMessageReceive(MessageReceiveDTO messageReceiveDTO) {
        MessageReceive messageReceive = new MessageReceive();
        BeanUtils.copyProperties(messageReceiveDTO, messageReceive);
        messageReceive.setCreateBy(SecurityUtils.getUserId());
        messageReceive.setCreateTime(DateUtils.getNowDate());
        messageReceive.setUpdateTime(DateUtils.getNowDate());
        messageReceive.setUpdateBy(SecurityUtils.getUserId());
        messageReceive.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        messageReceiveMapper.insertMessageReceive(messageReceive);
        messageReceiveDTO.setMessageReceiveId(messageReceive.getMessageReceiveId());
        return messageReceiveDTO;
    }

    /**
     * 修改消息接收表
     *
     * @param messageReceiveDTO 消息接收表
     * @return 结果
     */
    @Override
    public int updateMessageReceive(MessageReceiveDTO messageReceiveDTO) {
        MessageReceive messageReceive = new MessageReceive();
        BeanUtils.copyProperties(messageReceiveDTO, messageReceive);
        messageReceive.setUpdateTime(DateUtils.getNowDate());
        messageReceive.setUpdateBy(SecurityUtils.getUserId());
        return messageReceiveMapper.updateMessageReceive(messageReceive);
    }

    /**
     * 修改已读消息
     *
     * @param messageReceiveDTO 消息接收表
     * @return 结果
     */
    @Override
    public int read(MessageReceiveDTO messageReceiveDTO) {
        Long messageReceiveId = messageReceiveDTO.getMessageReceiveId();
        MessageReceiveDTO messageReceiveOfDB = messageReceiveMapper.selectMessageReceiveByMessageReceiveId(messageReceiveId);
        if (StringUtils.isNull(messageReceiveOfDB)) {
            throw new ServiceException("消息不存在");
        }
        if (BusinessConstants.NORMAL.equals(messageReceiveOfDB.getStatus())) {
            throw new ServiceException("消息已查阅，请勿重复修改");
        }
        Date nowDate = DateUtils.getNowDate();
        MessageReceive messageReceive = new MessageReceive();
        messageReceive.setMessageReceiveId(messageReceiveId);
        messageReceive.setReadTime(nowDate);
        messageReceive.setStatus(BusinessConstants.NORMAL);
        messageReceive.setUpdateTime(DateUtils.getNowDate());
        messageReceive.setUpdateBy(SecurityUtils.getUserId());
        return messageReceiveMapper.updateMessageReceive(messageReceive);
    }

    /**
     * 修改所有已读消息
     *
     * @return 结果
     */
    @Override
    public int readAll() {
        Long userId = SecurityUtils.getUserId();
        List<MessageReceiveDTO> messageReceiveDTOS = messageReceiveMapper.selectUserUnReadMessageReceiveList(userId);
        if (StringUtils.isEmpty(messageReceiveDTOS)) {
            throw new ServiceException("不存在未读消息");
        }
        Date nowDate = DateUtils.getNowDate();
        List<MessageReceive> messageReceiveList = new ArrayList<>();
        for (MessageReceiveDTO messageReceiveDTO : messageReceiveDTOS) {
            Long messageReceiveId = messageReceiveDTO.getMessageReceiveId();
            MessageReceive messageReceive = new MessageReceive();
            messageReceive.setMessageReceiveId(messageReceiveId);
            messageReceive.setReadTime(nowDate);
            messageReceive.setStatus(BusinessConstants.NORMAL);
            messageReceive.setUpdateTime(nowDate);
            messageReceive.setUpdateBy(userId);
            messageReceiveList.add(messageReceive);
        }
        return messageReceiveMapper.updateMessageReceives(messageReceiveList);
    }


    /**
     * 逻辑批量删除消息接收表
     *
     * @param messageReceiveIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteMessageReceiveByMessageReceiveIds(List<Long> messageReceiveIds) {
        return messageReceiveMapper.logicDeleteMessageReceiveByMessageReceiveIds(messageReceiveIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除消息接收表信息
     *
     * @param messageReceiveId 消息接收表主键
     * @return 结果
     */
    @Override
    public int deleteMessageReceiveByMessageReceiveId(Long messageReceiveId) {
        return messageReceiveMapper.deleteMessageReceiveByMessageReceiveId(messageReceiveId);
    }

    /**
     * 逻辑删除消息接收表信息
     *
     * @param messageReceiveDTO 消息接收表
     * @return 结果
     */
    @Override
    public int logicDeleteMessageReceiveByMessageReceiveId(MessageReceiveDTO messageReceiveDTO) {
        Long messageReceiveId = messageReceiveDTO.getMessageReceiveId();
        MessageReceiveDTO messageReceiveOfDB = messageReceiveMapper.selectMessageReceiveByMessageReceiveId(messageReceiveId);
        if (StringUtils.isNull(messageReceiveOfDB)) {
            throw new ServiceException("消息不存在");
        }
        MessageReceive messageReceive = new MessageReceive();
        messageReceive.setMessageReceiveId(messageReceiveId);
        messageReceive.setUpdateTime(DateUtils.getNowDate());
        messageReceive.setUpdateBy(SecurityUtils.getUserId());
        return messageReceiveMapper.logicDeleteMessageReceiveByMessageReceiveId(messageReceive);
    }

    /**
     * 物理删除消息接收表信息
     *
     * @param messageReceiveDTO 消息接收表
     * @return 结果
     */

    @Override
    public int deleteMessageReceiveByMessageReceiveId(MessageReceiveDTO messageReceiveDTO) {
        MessageReceive messageReceive = new MessageReceive();
        BeanUtils.copyProperties(messageReceiveDTO, messageReceive);
        return messageReceiveMapper.deleteMessageReceiveByMessageReceiveId(messageReceive.getMessageReceiveId());
    }

    /**
     * 物理批量删除消息接收表
     *
     * @param messageReceiveDtos 需要删除的消息接收表主键
     * @return 结果
     */

    @Override
    public int deleteMessageReceiveByMessageReceiveIds(List<MessageReceiveDTO> messageReceiveDtos) {
        List<Long> stringList = new ArrayList();
        for (MessageReceiveDTO messageReceiveDTO : messageReceiveDtos) {
            stringList.add(messageReceiveDTO.getMessageReceiveId());
        }
        return messageReceiveMapper.deleteMessageReceiveByMessageReceiveIds(stringList);
    }

    /**
     * 批量新增消息接收表信息
     *
     * @param messageReceiveDtos 消息接收表对象
     */

    public int insertMessageReceives(List<MessageReceiveDTO> messageReceiveDtos) {
        List<MessageReceive> messageReceiveList = new ArrayList();

        for (MessageReceiveDTO messageReceiveDTO : messageReceiveDtos) {
            MessageReceive messageReceive = new MessageReceive();
            BeanUtils.copyProperties(messageReceiveDTO, messageReceive);
            messageReceive.setCreateBy(SecurityUtils.getUserId());
            messageReceive.setCreateTime(DateUtils.getNowDate());
            messageReceive.setUpdateTime(DateUtils.getNowDate());
            messageReceive.setUpdateBy(SecurityUtils.getUserId());
            messageReceive.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            messageReceiveList.add(messageReceive);
        }
        return messageReceiveMapper.batchMessageReceive(messageReceiveList);
    }

    /**
     * 批量修改消息接收表信息
     *
     * @param messageReceiveDtos 消息接收表对象
     */

    public int updateMessageReceives(List<MessageReceiveDTO> messageReceiveDtos) {
        List<MessageReceive> messageReceiveList = new ArrayList();

        for (MessageReceiveDTO messageReceiveDTO : messageReceiveDtos) {
            MessageReceive messageReceive = new MessageReceive();
            BeanUtils.copyProperties(messageReceiveDTO, messageReceive);
            messageReceive.setCreateBy(SecurityUtils.getUserId());
            messageReceive.setCreateTime(DateUtils.getNowDate());
            messageReceive.setUpdateTime(DateUtils.getNowDate());
            messageReceive.setUpdateBy(SecurityUtils.getUserId());
            messageReceiveList.add(messageReceive);
        }
        return messageReceiveMapper.updateMessageReceives(messageReceiveList);
    }

}

