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
import org.springframework.transaction.annotation.Transactional;

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
                    tree.setWeight(treeNode.getSort());
                    tree.putExtra("indexDimensionCode", treeNode.getIndexDimensionCode());
                    tree.putExtra("level", treeNode.getLevel());
                    tree.putExtra("levelName", treeNode.getLevel() + "级");
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
    @Transactional
    public int updateStrategyIndexDimension(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS) {
        if (StringUtils.isEmpty(strategyIndexDimensionDTOS)) {
            return 1;
        }
        this.setValue(strategyIndexDimensionDTOS, 0);
        List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOSBefore = selectStrategyIndexDimensionList(new StrategyIndexDimensionDTO());
        List<Long> strategyIndexDimensionIds = strategyIndexDimensionDTOSBefore.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId).collect(Collectors.toList());
        // 赋值ID
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
            if (strategyIndexDimensionDTO.getLevel() == 1)
                strategyIndexDimensionDTO.setParentIndexDimensionId(0L);
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS()))
                this.operateStrategyList(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), strategyIndexDimensionDTO, strategyIndexDimensionIds);
        }
        // 赋值祖籍
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            strategyIndexDimensionDTO.setAncestors(null);
            strategyIndexDimensionDTO.setParentIndexDimensionId(0L);
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS()))
                this.setStrategyAncestors(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), strategyIndexDimensionDTO);
        }
        // 删除
        List<Long> delStrategyIndexDimensionIds = strategyIndexDimensionDTOSBefore.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId)
                .filter(id -> !strategyIndexDimensionDTOS.stream().map(StrategyIndexDimensionDTO::getStrategyIndexDimensionId)
                        .collect(Collectors.toList()).contains(id)).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delStrategyIndexDimensionIds))
            this.logicDeleteStrategyIndexDimensionByStrategyIndexDimensionIds(delStrategyIndexDimensionIds);
        return this.updateStrategyIndexDimensions(strategyIndexDimensionDTOS);
    }

    /**
     * 赋值
     *
     * @param strategyIndexDimensionDTOS      战略指标维度列表列表
     * @param parentStrategyIndexDimensionDTO 父级战略指标维度列表DTO
     */
    private void setStrategyAncestors(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, StrategyIndexDimensionDTO parentStrategyIndexDimensionDTO) {
        Long parentStrategyId = parentStrategyIndexDimensionDTO.getStrategyIndexDimensionId();
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            strategyIndexDimensionDTO.setAncestors((StringUtils.isNull(strategyIndexDimensionDTO.getAncestors()) ? "" : parentStrategyIndexDimensionDTO.getAncestors() + ",") + parentStrategyId);
            strategyIndexDimensionDTO.setParentIndexDimensionId(parentStrategyId);
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS()))
                setStrategyAncestors(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), strategyIndexDimensionDTO);
        }
    }

    /**
     * 处理战略指标维度表
     *
     * @param strategyIndexDimensionDTOS      战略指标维度表
     * @param parentStrategyIndexDimensionDTO 父级战略指标维度DTO
     * @param strategyIndexDimensionIds       战略指标维度ID集合
     */
    private void operateStrategyList(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, StrategyIndexDimensionDTO parentStrategyIndexDimensionDTO, List<Long> strategyIndexDimensionIds) {
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            Long strategyIndexDimensionId = strategyIndexDimensionDTO.getStrategyIndexDimensionId();
            if (StringUtils.isNull(strategyIndexDimensionId)) {
                StrategyIndexDimensionDTO insertStrategyIndexDimension = insertStrategyIndexDimension(strategyIndexDimensionDTO);
                strategyIndexDimensionId = insertStrategyIndexDimension.getStrategyIndexDimensionId();
            } else if (strategyIndexDimensionIds.contains(strategyIndexDimensionId)) {
                editStrategyIndexDimension(strategyIndexDimensionDTO);
            } else {
                throw new ServiceException("战略指标配置维度部分数据已不存在");
            }
            strategyIndexDimensionDTO.setStrategyIndexDimensionId(strategyIndexDimensionId);
            strategyIndexDimensionDTO.setParentIndexDimensionId(parentStrategyIndexDimensionDTO.getStrategyIndexDimensionId());
            List<StrategyIndexDimensionDTO> sonStrategyIndexDimensionDTOS = strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS();
            if (StringUtils.isNotEmpty(sonStrategyIndexDimensionDTOS)) {
                operateStrategyList(sonStrategyIndexDimensionDTOS, strategyIndexDimensionDTO, strategyIndexDimensionIds);
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
            strategyIndexDimensionDTO.setSort(sort + 1);
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS())) {
                setValue(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), level);
            }
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
        setListWithDTO(strategyIndexDimensionDtos, strategyIndexDimensionList);
        return strategyIndexDimensionMapper.updateStrategyIndexDimensions(strategyIndexDimensionList);
    }


    /**
     * 为需要更新的list赋值
     *
     * @param strategyIndexDimensionDTOS dtoList
     * @param strategyIndexDimensionList 更新list
     */
    private void setListWithDTO(List<StrategyIndexDimensionDTO> strategyIndexDimensionDTOS, List<StrategyIndexDimension> strategyIndexDimensionList) {
        for (StrategyIndexDimensionDTO strategyIndexDimensionDTO : strategyIndexDimensionDTOS) {
            StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
            BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
            strategyIndexDimension.setCreateBy(SecurityUtils.getUserId());
            strategyIndexDimension.setCreateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
            strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
            strategyIndexDimensionList.add(strategyIndexDimension);
            if (StringUtils.isNotEmpty(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS())) {
                this.setListWithDTO(strategyIndexDimensionDTO.getStrategyIndexDimensionDTOS(), strategyIndexDimensionList);
            }
        }
    }
}

