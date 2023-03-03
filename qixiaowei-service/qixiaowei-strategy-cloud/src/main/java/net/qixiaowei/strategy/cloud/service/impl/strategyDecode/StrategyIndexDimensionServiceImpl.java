package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import cn.hutool.core.lang.tree.Tree;
import cn.hutool.core.lang.tree.TreeNodeConfig;
import cn.hutool.core.lang.tree.TreeUtil;
import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
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
     * @param strategyIndexDimensionDTO 战略指标维度表
     * @return 结果
     */
    @Override
    public int updateStrategyIndexDimension(StrategyIndexDimensionDTO strategyIndexDimensionDTO) {
        StrategyIndexDimension strategyIndexDimension = new StrategyIndexDimension();
        BeanUtils.copyProperties(strategyIndexDimensionDTO, strategyIndexDimension);
        strategyIndexDimension.setUpdateTime(DateUtils.getNowDate());
        strategyIndexDimension.setUpdateBy(SecurityUtils.getUserId());
        return strategyIndexDimensionMapper.updateStrategyIndexDimension(strategyIndexDimension);
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

    public int insertStrategyIndexDimensions(List<StrategyIndexDimensionDTO> strategyIndexDimensionDtos) {
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
        return strategyIndexDimensionMapper.batchStrategyIndexDimension(strategyIndexDimensionList);
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

