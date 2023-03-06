package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyIndexDimension;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyIndexDimensionDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyIndexDimensionMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IStrategyIndexDimensionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * StrategyIndexDimensionService业务层处理
 *
 * @author Graves
 * @since 2023-03-03
 */
@Service
public class StrategyIndexDimensionServiceImpl implements IStrategyIndexDimensionService {
    @Autowired
    private StrategyIndexDimensionMapper strategyIndexDimensionMapper;

    /**
     * 查询战略指标维度表
     *
     * @param strategyIndexDimensionId 战略指标维度表主键
     * @return 战略指标维度表
     */
    @Override
    public StrategyIndexDimensionDTO selectStrategyIndexDimensionByStrategyIndexDimensionId(Long strategyIndexDimensionId) {
        return strategyIndexDimensionMapper.selectStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimensionId);
    }

    /**
     * 查询战略指标维度表列表
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 战略指标维度表
     */
    @Override
    public List<StrategyIndexDimensionDTO> selectStrategyIndexDimensionList(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {

        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        return strategyIndexDimensionMapper.selectStrategyIndexDimensionList(strategyIndexDimension);
    }


    /**
     * 获取战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO 战略指标维度表DTO
     * @return 结果
     */
    @Override
    public List<Tree<Long>> selectStrategyIndexDimensionTreeList(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS = strategyIndexDimensionMapper.selectStrategyIndexDimensionList(new StrategyIndexDimension());
        //自定义属性名
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("strategyIndexDimensionId");
        treeNodeConfig.setNameKey("indexDimensionName");
        treeNodeConfig.setParentIdKey("parentIndexDimensionId");
        return TreeUtil.build(strategyIndexDimensionDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
                    tree.setId(treeNode.getStrategyIndexDimensionId());
                    tree.setParentId(treeNode.getParentIndexDimensionId());
                    tree.setName(treeNode.getIndexDimensionName());
                    tree.putExtra("indexDimensionCode", treeNode.getIndexDimensionCode());
                    tree.putExtra("level", treeNode.getLevel());
                    tree.putExtra("status", treeNode.getStatus());
                    tree.putExtra("createBy", treeNode.getCreateBy());
                    tree.putExtra("createTime", DateUtils.parseDateToStr("yyyy/MM/dd HH:mm:ss", treeNode.getCreateTime()));
                }
        );
    }

    /**
     * 新增战略指标维度表
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */
    @Override
    public StrategyIndexDimensionDTO insertStrategyIndexDimension(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {


        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        strategyIndexDimension.setCreateBy(SecurityUtils.getUserId());
        strategyIndexDimension.setCreateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
        strategyIndexDimension.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        strategyIndexDimensionMapper.insertStrategyIndexDimension(strategyIndexDimension);
        strategyIndexDimensionDTO.setStrategyIndexDimensionId(strategyIndexDimension.getStrategyIndexDimensionId());
        return strategyIndexDimensionDTO;
    }

    /**
     * 修改战略指标维度表
     *
     * @param strategyIndexDimensionDTOS 战略指标维度表
     * @return 结果
     */
    @Override
    public int updateStrategyIndexDimension(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS) {
        if (StringUtils.isEmpty(strategyIndexDimensionDTOS)) {
            return 1;
        }
        setValue(strategyIndexDimensionDTOS, 0);
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOSBefore = selectStrategyIndexDimensionList(new StrategyIndexDimensionDTO());
        List<Long> strategyIndexDimensionIds = strategyIndexDimensionDTOSBefore.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId).collect(Collectors.toList());

        for (int i = 0; i < strategyIndexDimensionDTOS.size(); i++) {
            StrategyIndexDimensionDTO strategyIndexDimensionDTO = strategyIndexDimensionDTOS.get(0);
            Long strategyIndexDimensionId = strategyIndexDimensionDTO.getStrategyIndexDimensionId();
            if (StringUtils.isNull(strategyIndexDimensionId)) {
                StrategyIndexDimensionDTO insertStrategyIndexDimension = insertStrategyIndexDimension(strategyIndexDimensionDTO);
                strategyIndexDimensionId = insertStrategyIndexDimension.getStrategyIndexDimensionId();
            } else if (strategyIndexDimensionIds.contains(strategyIndexDimensionId)) {
                editStrategyIndexDimension(strategyIndexDimensionDTO);
            } else {
                throw new ServiceException("战略指标配置维度部分数据不存在");
            }
            strategyIndexDimensionDTO.setStrategyIndexDimensionId(strategyIndexDimensionId);
            if (strategyIndexDimensionDTO.getLevel() == 1) {
                strategyIndexDimensionDTO.setParentIndexDimensionId(0L);
            } else {
                if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS()))
                    operateStrategyList(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), strategyIndexDimensionId, strategyIndexDimensionIds);
            }
        }
        setStrategyAncestors(strategyIndexDimensionDTOS, "", 0L);

        return 1;
    }

    /**
     * 赋值
     *
     * @param strategyIndexDimensionDTOS 战略指标维度列表
     * @param ancestors                  祖籍
     * @param parentStrategyId           父级ID
     */
    private static void setStrategyAncestors(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, String ancestors, Long parentStrategyId) {
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            Integer level = strategyIndexDimensionDTO.getLevel();
            if (level == 0) {
                strategyIndexDimensionDTO.setAncestors(null);
            } else {
                ancestors = ancestors + parentStrategyId;
                strategyIndexDimensionDTO.setAncestors(ancestors);
            }
            strategyIndexDimensionDTO.setParentIndexDimensionId(parentStrategyId);
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS()))
                setStrategyAncestors(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), ancestors, strategyIndexDimensionDTO.getStrategyIndexDimensionId());
        }
    }

    /**
     * 处理战略指标维度表
     *
     * @param strategyIndexDimensionDTOS 战略指标维度表
     * @param strategyIndexDimensionIds  战略指标维度ID集合
     * @param parentIndexDimensionId     父级战略指标维度ID
     */
    private void operateStrategyList(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, Long parentIndexDimensionId, List<Long> strategyIndexDimensionIds) {
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            Long strategyIndexDimensionId = strategyIndexDimensionDTO.getStrategyIndexDimensionId();
            if (StringUtils.isNull(strategyIndexDimensionId)) {
                StrategyIndexDimensionDTO insertStrategyIndexDimension = insertStrategyIndexDimension(strategyIndexDimensionDTO);
                strategyIndexDimensionId = insertStrategyIndexDimension.getStrategyIndexDimensionId();
            } else if (strategyIndexDimensionIds.contains(strategyIndexDimensionId)) {
                editStrategyIndexDimension(strategyIndexDimensionDTO);
            } else {
                throw new ServiceException("战略指标配置维度部分数据不存在");
            }
            strategyIndexDimensionDTO.setStrategyIndexDimensionId(strategyIndexDimensionId);
            strategyIndexDimensionDTO.setParentIndexDimensionId(parentIndexDimensionId);
            List<StrategyIndexDimensionDTO> sonStrategyIndexDimensionDTOS = strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS();
            if (StringUtils.isNotEmpty(sonStrategyIndexDimensionDTOS)) {
                operateStrategyList(sonStrategyIndexDimensionDTOS, strategyIndexDimensionId, strategyIndexDimensionIds);
            }
        }
    }

    /**
     * 更新战略指标维度表
     *
     * @param strategyIndexDimensionDTO 战略指标维度DTO
     */
    private void editStrategyIndexDimension(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
        strategyIndexDimensionMapper.updateStrategyIndexDimension(strategyIndexDimension);
    }

    /**
     * 递归赋值排序与等级
     *
     * @param strategyIndexDimensionDTOS 战略指标维度
     */
    private void setValue(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, int level) {
        int sort = 0;
        level += 1;
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS())) {
                setValue(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), level);
            }
            strategyIndexDimensionDTO.setSort(sort + 1);
            strategyIndexDimensionDTO.setLevel(level);
            sort++;
        }
    }

    /**
     * 逻辑批量删除战略指标维度表
     *
     * @param strategyIndexDimensionIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyIndexDimensionByStrategyIndexDimensionIds(List<Long> strategyIndexDimensionIds) {
        return strategyIndexDimensionMapper.logicDeleteStrategyIndexDimensionByStrategyIndexDimensionIds(strategyIndexDimensionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除战略指标维度表信息
     *
     * @param strategyIndexDimensionId 战略指标维度表主键
     * @return 结果
     */
    @Override
    public int deleteStrategyIndexDimensionByStrategyIndexDimensionId(Long strategyIndexDimensionId) {
        return strategyIndexDimensionMapper.deleteStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimensionId);
    }

    /**
     * 逻辑删除战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */
    @Override
    public int logicDeleteStrategyIndexDimensionByStrategyIndexDimensionId(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        strategyIndexDimension.setStrategyIndexDimensionId(strategyIndexDimensionDTO.getStrategyIndexDimensionId());
        strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
        return strategyIndexDimensionMapper.logicDeleteStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimension);
    }

    /**
     * 物理删除战略指标维度表信息
     *
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */

    @Override
    public int deleteStrategyIndexDimensionByStrategyIndexDimensionId(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        return strategyIndexDimensionMapper.deleteStrategyIndexDimensionByStrategyIndexDimensionId(strategyIndexDimension.getStrategyIndexDimensionId());
    }

    /**
     * 物理批量删除战略指标维度表
     *
     * @param strategyIndexDimensionDtos 需要删除的战略指标维度表主键
     * @return 结果
     */

    @Override
    public int deleteStrategyIndexDimensionByStrategyIndexDimensionIds(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos) {
        List<Long> stringList = new ArrayList<>();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDtos) {
            stringList.add(strategyIndexDimensionDTO.getStrategyIndexDimensionId());
        }
        return strategyIndexDimensionMapper.deleteStrategyIndexDimensionByStrategyIndexDimensionIds(stringList);
    }

    /**
     * 批量新增战略指标维度表信息
     *
     * @param strategyIndexDimensionDtos 战略指标维度表对象
     */

    public List<StrategyIndexDimension> insertStrategyIndexDimensions(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos) {
        List<StrategyIndexDimension> strategyIndexDimensionList = new ArrayList<>();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDtos) {
            StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
            BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
            strategyIndexDimension.setCreateBy(SecurityUtils.getUserId());
            strategyIndexDimension.setCreateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
            strategyIndexDimension.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            strategyIndexDimensionList.add(strategyIndexDimension);
        }
        strategyIndexDimensionMapper.batchStrategyIndexDimension(strategyIndexDimensionList);
        return strategyIndexDimensionList;
    }

    /**
     * 批量修改战略指标维度表信息
     *
     * @param strategyIndexDimensionDtos 战略指标维度表对象
     */

    public int updateStrategyIndexDimensions(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos) {
        List<StrategyIndexDimension> strategyIndexDimensionList = new ArrayList<>();

        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDtos) {
            StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
            BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
            strategyIndexDimension.setCreateBy(SecurityUtils.getUserId());
            strategyIndexDimension.setCreateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
            strategyIndexDimensionList.add(strategyIndexDimension);
        }
        return strategyIndexDimensionMapper.updateStrategyIndexDimensions(strategyIndexDimensionList);
    }

}

