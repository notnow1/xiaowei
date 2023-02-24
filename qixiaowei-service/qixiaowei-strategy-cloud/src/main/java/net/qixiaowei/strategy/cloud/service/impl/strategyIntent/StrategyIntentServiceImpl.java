package net.qixiaowei.strategy.cloud.service.impl.strategyIntent;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyIntent.StrategyIntent;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyIntent.StrategyIntentMapper;
import net.qixiaowei.strategy.cloud.service.strategyIntent.IStrategyIntentService;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * StrategyIntentService业务层处理
 *
 * @author TANGMICHI
 * @since 2023-02-23
 */
@Service
public class StrategyIntentServiceImpl implements IStrategyIntentService {
    @Autowired
    private StrategyIntentMapper strategyIntentMapper;
    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 查询战略意图表
     *
     * @param strategyIntentId 战略意图表主键
     * @return 战略意图表
     */
    @Override
    public StrategyIntentDTO selectStrategyIntentByStrategyIntentId(Long strategyIntentId) {
        return strategyIntentMapper.selectStrategyIntentByStrategyIntentId(strategyIntentId);
    }

    /**
     * 查询战略意图表列表
     *
     * @param strategyIntentDTO 战略意图表
     * @return 战略意图表
     */
    @Override
    public List<StrategyIntentDTO> selectStrategyIntentList(StrategyIntentDTO strategyIntentDTO) {

        StrategyIntent strategyIntent = new StrategyIntent();
        BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
        List<StrategyIntentDTO> strategyIntentDTOS = strategyIntentMapper.selectStrategyIntentList(strategyIntent);
        if (StringUtils.isNotEmpty(strategyIntentDTOS)) {
            Set<Long> createBys = strategyIntentDTOS.stream().map(StrategyIntentDTO::getCreateBy).collect(Collectors.toSet());
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(createBys, SecurityConstants.INNER);
            List<UserDTO> userDTOList = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(userDTOList)) {
                for (StrategyIntentDTO intentDTO : strategyIntentDTOS) {
                    for (UserDTO userDTO : userDTOList) {
                        if (intentDTO.getCreateBy().equals(userDTO.getUserId())) {
                            intentDTO.setCreateByName(userDTO.getEmployeeName());
                        }
                    }
                }
            }
        }
        return strategyIntentDTOS;
    }

    /**
     * 新增战略意图表
     *
     * @param strategyIntentDTO 战略意图表
     * @return 结果
     */
    @Override
    public StrategyIntentDTO insertStrategyIntent(StrategyIntentDTO strategyIntentDTO) {
        StrategyIntent strategyIntent = new StrategyIntent();
        BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
        strategyIntent.setCreateBy(SecurityUtils.getUserId());
        strategyIntent.setCreateTime(DateUtils.getNowDate());
        strategyIntent.setUpdateTime(DateUtils.getNowDate());
        strategyIntent.setUpdateBy(SecurityUtils.getUserId());
        strategyIntent.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyIntentMapper.insertStrategyIntent(strategyIntent);
        strategyIntentDTO.setStrategyIntentId(strategyIntent.getStrategyIntentId());
        return strategyIntentDTO;
    }

    /**
     * 修改战略意图表
     *
     * @param strategyIntentDTO 战略意图表
     * @return 结果
     */
    @Override
    public int updateStrategyIntent(StrategyIntentDTO strategyIntentDTO) {
        StrategyIntent strategyIntent = new StrategyIntent();
        BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
        strategyIntent.setUpdateTime(DateUtils.getNowDate());
        strategyIntent.setUpdateBy(SecurityUtils.getUserId());
        return strategyIntentMapper.updateStrategyIntent(strategyIntent);
    }

    /**
     * 逻辑批量删除战略意图表
     *
     * @param strategyIntentIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyIntentByStrategyIntentIds(List<Long> strategyIntentIds) {
        return strategyIntentMapper.logicDeleteStrategyIntentByStrategyIntentIds(strategyIntentIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略意图表信息
     *
     * @param strategyIntentId 战略意图表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyIntentByStrategyIntentId(Long strategyIntentId) {
        return strategyIntentMapper.deleteStrategyIntentByStrategyIntentId(strategyIntentId);
    }

    /**
     * 逻辑删除战略意图表信息
     *
     * @param strategyIntentDTO 战略意图表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyIntentByStrategyIntentId(StrategyIntentDTO strategyIntentDTO) {
        StrategyIntent strategyIntent = new StrategyIntent();
        strategyIntent.setStrategyIntentId(strategyIntentDTO.getStrategyIntentId());
        strategyIntent.setUpdateTime(DateUtils.getNowDate());
        strategyIntent.setUpdateBy(SecurityUtils.getUserId());
        return strategyIntentMapper.logicDeleteStrategyIntentByStrategyIntentId(strategyIntent);
    }

    /**
     * 物理删除战略意图表信息
     *
     * @param strategyIntentDTO 战略意图表
     * @return 结果
     */

    @Override
    public int deleteStrategyIntentByStrategyIntentId(StrategyIntentDTO strategyIntentDTO) {
        StrategyIntent strategyIntent = new StrategyIntent();
        BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
        return strategyIntentMapper.deleteStrategyIntentByStrategyIntentId(strategyIntent.getStrategyIntentId());
    }

    /**
     * 物理批量删除战略意图表
     *
     * @param strategyIntentDtos 需要删除的战略意图表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyIntentByStrategyIntentIds(List<StrategyIntentDTO> strategyIntentDtos) {
        List<Long> stringList = new ArrayList();
        for (StrategyIntentDTO strategyIntentDTO : strategyIntentDtos) {
            stringList.add(strategyIntentDTO.getStrategyIntentId());
        }
        return strategyIntentMapper.deleteStrategyIntentByStrategyIntentIds(stringList);
    }

    /**
     * 批量新增战略意图表信息
     *
     * @param strategyIntentDtos 战略意图表对象
     */

    public int insertStrategyIntents(List<StrategyIntentDTO> strategyIntentDtos) {
        List<StrategyIntent> strategyIntentList = new ArrayList();

        for (StrategyIntentDTO strategyIntentDTO : strategyIntentDtos) {
            StrategyIntent strategyIntent = new StrategyIntent();
            BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
            strategyIntent.setCreateBy(SecurityUtils.getUserId());
            strategyIntent.setCreateTime(DateUtils.getNowDate());
            strategyIntent.setUpdateTime(DateUtils.getNowDate());
            strategyIntent.setUpdateBy(SecurityUtils.getUserId());
            strategyIntent.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyIntentList.add(strategyIntent);
        }
        return strategyIntentMapper.batchStrategyIntent(strategyIntentList);
    }

    /**
     * 批量修改战略意图表信息
     *
     * @param strategyIntentDtos 战略意图表对象
     */

    public int updateStrategyIntents(List<StrategyIntentDTO> strategyIntentDtos) {
        List<StrategyIntent> strategyIntentList = new ArrayList();

        for (StrategyIntentDTO strategyIntentDTO : strategyIntentDtos) {
            StrategyIntent strategyIntent = new StrategyIntent();
            BeanUtils.copyProperties(strategyIntentDTO, strategyIntent);
            strategyIntent.setCreateBy(SecurityUtils.getUserId());
            strategyIntent.setCreateTime(DateUtils.getNowDate());
            strategyIntent.setUpdateTime(DateUtils.getNowDate());
            strategyIntent.setUpdateBy(SecurityUtils.getUserId());
            strategyIntentList.add(strategyIntent);
        }
        return strategyIntentMapper.updateStrategyIntents(strategyIntentList);
    }
}

