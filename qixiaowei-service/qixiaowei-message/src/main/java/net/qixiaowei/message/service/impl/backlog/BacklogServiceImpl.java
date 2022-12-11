package net.qixiaowei.message.service.impl.backlog;

import java.util.Date;
import java.util.List;

import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.message.api.vo.backlog.BacklogCountVO;
import net.qixiaowei.message.mapper.message.MessageReceiveMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.message.api.domain.backlog.Backlog;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.mapper.backlog.BacklogMapper;
import net.qixiaowei.message.service.backlog.IBacklogService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
 * BacklogService业务层处理
 *
 * @author hzk
 * @since 2022-12-11
 */
@Service
public class BacklogServiceImpl implements IBacklogService {
    @Autowired
    private BacklogMapper backlogMapper;

    @Autowired
    private MessageReceiveMapper messageReceiveMapper;

    /**
     * 查询待办事项表
     *
     * @param backlogId 待办事项表主键
     * @return 待办事项表
     */
    @Override
    public BacklogDTO selectBacklogByBacklogId(Long backlogId) {
        return backlogMapper.selectBacklogByBacklogId(backlogId);
    }

    /**
     * 查询待办统计数据
     *
     * @return 待办统计数据
     */
    @Override
    public BacklogCountVO count() {
        boolean havingBacklog = false;
        Long userId = SecurityUtils.getUserId();
        Integer countBacklog = backlogMapper.countBacklog(userId);
        Integer countUnreadMessage = messageReceiveMapper.countUnreadMessage(userId);
        int totalBacklog = countBacklog + countUnreadMessage;
        if (totalBacklog > 0) {
            havingBacklog = true;
        }
        BacklogCountVO backlogCountVO = new BacklogCountVO();
        backlogCountVO.setHavingBacklog(havingBacklog);
        backlogCountVO.setTotalBacklog(totalBacklog);
        backlogCountVO.setCountBacklog(countBacklog);
        backlogCountVO.setCountUnreadMessage(countUnreadMessage);
        return backlogCountVO;
    }

    /**
     * 查询待办事项表列表
     *
     * @param backlogDTO 待办事项表
     * @return 待办事项表
     */
    @Override
    public List<BacklogDTO> selectBacklogList(BacklogDTO backlogDTO) {
        Backlog backlog = new Backlog();
        BeanUtils.copyProperties(backlogDTO, backlog);
        Long userId = SecurityUtils.getUserId();
        backlog.setUserId(userId);
        return backlogMapper.selectBacklogList(backlog);
    }

    /**
     * 新增待办事项表
     *
     * @param backlogDTO 待办事项表
     * @return 结果
     */
    @Override
    public BacklogDTO insertBacklog(BacklogDTO backlogDTO) {
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        Backlog backlog = new Backlog();
        BeanUtils.copyProperties(backlogDTO, backlog);
        backlog.setCreateBy(userId);
        backlog.setCreateTime(nowDate);
        backlog.setUpdateTime(nowDate);
        backlog.setUpdateBy(userId);
        backlog.setBacklogInitiationTime(nowDate);
        backlog.setStatus(BusinessConstants.DISABLE);
        backlog.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        backlogMapper.insertBacklog(backlog);
        backlogDTO.setBacklogId(backlog.getBacklogId());
        return backlogDTO;
    }

    /**
     * 待办事项处理为已办
     *
     * @param backlogDTO 待办事项表
     * @return 结果
     */
    @Override
    public int handled(BacklogDTO backlogDTO) {
        Integer businessType = backlogDTO.getBusinessType();
        Integer businessSubtype = backlogDTO.getBusinessSubtype();
        Long backlogUserId = backlogDTO.getUserId();
        Long businessId = backlogDTO.getBusinessId();
        //判断该事项是否已处理
        BacklogDTO selectUserBacklog = backlogMapper.selectUserBacklog(businessType, businessSubtype, backlogUserId, businessId);
        if (StringUtils.isNull(selectUserBacklog)) {
            throw new ServiceException("不存在该待办事项");
        }
        if (BusinessConstants.NORMAL.equals(selectUserBacklog.getStatus())) {
            throw new ServiceException("待办事项已处理，无需重复处理");
        }
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        Backlog backlog = new Backlog();
        backlog.setBacklogId(selectUserBacklog.getBacklogId());
        backlog.setBacklogProcessTime(nowDate);
        backlog.setStatus(BusinessConstants.NORMAL);
        backlog.setUpdateTime(nowDate);
        backlog.setUpdateBy(userId);
        return backlogMapper.updateBacklog(backlog);
    }

    /**
     * 修改待办事项表
     *
     * @param backlogDTO 待办事项表
     * @return 结果
     */
    @Override
    public int updateBacklog(BacklogDTO backlogDTO) {
        Backlog backlog = new Backlog();
        BeanUtils.copyProperties(backlogDTO, backlog);
        backlog.setUpdateTime(DateUtils.getNowDate());
        backlog.setUpdateBy(SecurityUtils.getUserId());
        return backlogMapper.updateBacklog(backlog);
    }

    /**
     * 逻辑批量删除待办事项表
     *
     * @param backlogIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteBacklogByBacklogIds(List<Long> backlogIds) {
        return backlogMapper.logicDeleteBacklogByBacklogIds(backlogIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除待办事项表信息
     *
     * @param backlogId 待办事项表主键
     * @return 结果
     */
    @Override
    public int deleteBacklogByBacklogId(Long backlogId) {
        return backlogMapper.deleteBacklogByBacklogId(backlogId);
    }

    /**
     * 逻辑删除待办事项表信息
     *
     * @param backlogDTO 待办事项表
     * @return 结果
     */
    @Override
    public int logicDeleteBacklogByBacklogId(BacklogDTO backlogDTO) {
        Backlog backlog = new Backlog();
        backlog.setBacklogId(backlogDTO.getBacklogId());
        backlog.setUpdateTime(DateUtils.getNowDate());
        backlog.setUpdateBy(SecurityUtils.getUserId());
        return backlogMapper.logicDeleteBacklogByBacklogId(backlog);
    }

    /**
     * 物理删除待办事项表信息
     *
     * @param backlogDTO 待办事项表
     * @return 结果
     */

    @Override
    public int deleteBacklogByBacklogId(BacklogDTO backlogDTO) {
        Backlog backlog = new Backlog();
        BeanUtils.copyProperties(backlogDTO, backlog);
        return backlogMapper.deleteBacklogByBacklogId(backlog.getBacklogId());
    }

    /**
     * 物理批量删除待办事项表
     *
     * @param backlogDtos 需要删除的待办事项表主键
     * @return 结果
     */

    @Override
    public int deleteBacklogByBacklogIds(List<BacklogDTO> backlogDtos) {
        List<Long> stringList = new ArrayList();
        for (BacklogDTO backlogDTO : backlogDtos) {
            stringList.add(backlogDTO.getBacklogId());
        }
        return backlogMapper.deleteBacklogByBacklogIds(stringList);
    }

    /**
     * 批量新增待办事项表信息
     *
     * @param backlogDtos 待办事项表对象
     */

    public int insertBacklogs(List<BacklogDTO> backlogDtos) {
        List<Backlog> backlogList = new ArrayList();

        for (BacklogDTO backlogDTO : backlogDtos) {
            Backlog backlog = new Backlog();
            BeanUtils.copyProperties(backlogDTO, backlog);
            backlog.setCreateBy(SecurityUtils.getUserId());
            backlog.setCreateTime(DateUtils.getNowDate());
            backlog.setUpdateTime(DateUtils.getNowDate());
            backlog.setUpdateBy(SecurityUtils.getUserId());
            backlog.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            backlogList.add(backlog);
        }
        return backlogMapper.batchBacklog(backlogList);
    }

    /**
     * 批量修改待办事项表信息
     *
     * @param backlogDtos 待办事项表对象
     */

    public int updateBacklogs(List<BacklogDTO> backlogDtos) {
        List<Backlog> backlogList = new ArrayList();

        for (BacklogDTO backlogDTO : backlogDtos) {
            Backlog backlog = new Backlog();
            BeanUtils.copyProperties(backlogDTO, backlog);
            backlog.setCreateBy(SecurityUtils.getUserId());
            backlog.setCreateTime(DateUtils.getNowDate());
            backlog.setUpdateTime(DateUtils.getNowDate());
            backlog.setUpdateBy(SecurityUtils.getUserId());
            backlogList.add(backlog);
        }
        return backlogMapper.updateBacklogs(backlogList);
    }

}

