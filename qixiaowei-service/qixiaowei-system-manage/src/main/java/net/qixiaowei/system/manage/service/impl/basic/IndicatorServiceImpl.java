package net.qixiaowei.system.manage.service.impl.basic;

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
import net.qixiaowei.system.manage.api.domain.basic.Indicator;
import net.qixiaowei.system.manage.api.domain.basic.IndicatorCategory;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.mapper.basic.IndicatorCategoryMapper;
import net.qixiaowei.system.manage.mapper.basic.IndicatorMapper;
import net.qixiaowei.system.manage.service.basic.IIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * IndicatorService业务层处理
 *
 * @author Graves
 * @since 2022-09-28
 */
@Service
public class IndicatorServiceImpl implements IIndicatorService {
    @Autowired
    private IndicatorMapper indicatorMapper;

    @Autowired
    private IndicatorCategoryMapper indicatorCategoryMapper;

    /**
     * 查询指标表
     *
     * @param indicatorId 指标表主键
     * @return 指标表
     */
    @Override
    public IndicatorDTO selectIndicatorByIndicatorId(Long indicatorId) {
        IndicatorDTO indicatorDTO = indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
        Long indicatorCategoryId = indicatorDTO.getIndicatorCategoryId();
        IndicatorCategoryDTO indicatorCategoryDTO = indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
        indicatorDTO.setIndicatorCategoryName(indicatorCategoryDTO.getIndicatorCategoryName());
        return indicatorDTO;
    }

    /**
     * 查询指标表列表
     *
     * @param indicatorDTO 指标表
     * @return 指标表
     */
    @Override
    public List<IndicatorDTO> selectIndicatorList(IndicatorDTO indicatorDTO) {
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicatorDTO);
        Long parentIndicatorId = indicatorDTO.getParentIndicatorId();
        if (StringUtils.isNull(parentIndicatorId)) {
            indicatorDTO.setParentIndicatorId(0L);
        }
        ArrayList<Long> indicatorCategoryList = new ArrayList<>();
        for (IndicatorDTO dto : indicatorDTOS) {
            indicatorCategoryList.add(dto.getIndicatorCategoryId());
        }
        // 通过categoryId集合查找对应的categoryName
        List<IndicatorCategory> indicatorCategory = indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryIds(indicatorCategoryList);
        // 赋值
        for (int i = 0; i < indicatorCategoryList.size(); i++) {
            for (IndicatorCategory category : indicatorCategory) {
                if (category.getIndicatorCategoryId().equals(indicatorCategoryList.get(i))) {
                    indicatorDTOS.get(i).setIndicatorCategoryName(category.getIndicatorCategoryName());
                    break;
                }
            }
        }
        return indicatorDTOS;
    }

    /**
     * 查询指标表树状图
     *
     * @param indicatorDTO
     * @return
     */
    @Override
    public List<Tree<Long>> selectTreeList(IndicatorDTO indicatorDTO) {
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicatorDTO);
        TreeNodeConfig treeNodeConfig = new TreeNodeConfig();
        treeNodeConfig.setIdKey("indicator");
        treeNodeConfig.setNameKey("indicatorName");
        treeNodeConfig.setParentIdKey("parentIndicatorId");
        return TreeUtil.build(indicatorDTOS, Constants.TOP_PARENT_ID, treeNodeConfig, (treeNode, tree) -> {
            tree.setId(treeNode.getIndicatorId());
            tree.setParentId(treeNode.getParentIndicatorId());
            tree.setName(treeNode.getIndicatorName());
            tree.putExtra("level", treeNode.getLevel());
            tree.putExtra("indicatorCode", treeNode.getIndicatorCode());
            tree.putExtra("sort", treeNode.getSort());
            tree.putExtra("indicatorType", treeNode.getIndicatorType());
            tree.putExtra("indicatorValueType", treeNode.getIndicatorValueType());
            tree.putExtra("choiceFlag", treeNode.getChoiceFlag());
            tree.putExtra("examineDirection", treeNode.getExamineDirection());
            tree.putExtra("drivingFactorFlag", treeNode.getDrivingFactorFlag());
            tree.putExtra("indicatorCategoryId", treeNode.getIndicatorCategoryId());
            tree.putExtra("indicatorCategoryName", treeNode.getIndicatorCategoryName());
        });
    }

    /**
     * 新增指标表
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public IndicatorDTO insertIndicator(IndicatorDTO indicatorDTO) {
        String indicatorCode = indicatorDTO.getIndicatorCode();
        Integer indicatorValueType = indicatorDTO.getIndicatorValueType();
        Integer examineDirection = indicatorDTO.getExamineDirection();
        Integer choiceFlag = indicatorDTO.getChoiceFlag();
        if (StringUtils.isEmpty(indicatorCode)) {
            throw new ServiceException("指标编码不能为空");
        }
        IndicatorDTO indicatorByCode = indicatorMapper.checkUnique(indicatorCode);
        if (StringUtils.isNotNull(indicatorByCode)) {
            throw new ServiceException("指标编码重复");
        }
        if (indicatorValueType != 1 && indicatorValueType != 2) {
            throw new ServiceException("指标值类型输入不正确");
        }
        if (choiceFlag != 0 && choiceFlag != 1) {
            throw new ServiceException("必选标记输入不正确");
        }
        if (examineDirection != 0 && examineDirection != 1) {
            throw new ServiceException("考核方向输入不正确");
        }
        Long parentIndicatorId = indicatorDTO.getParentIndicatorId();
        String parentAncestors = "";//仅在非一级行业时有用
        Integer parentLevel = 1;
        if (StringUtils.isNotNull(parentIndicatorId)) {// 一级行业
            IndicatorDTO parentIndicator = indicatorMapper.selectIndicatorByIndicatorId(parentIndicatorId);
            if (parentIndicator == null) {
                throw new ServiceException("上级指标不存在");
            }
            parentAncestors = parentIndicator.getAncestors();
            parentLevel = parentIndicator.getLevel();
        }
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        if (StringUtils.isNotNull(parentIndicatorId)) {
            String ancestors = parentAncestors;
            if (StringUtils.isNotEmpty(ancestors)) {
                ancestors = ancestors + ",";
            }
            ancestors = ancestors + parentIndicatorId;
            indicator.setAncestors(ancestors);
            indicator.setLevel(parentLevel + 1);
        } else {
            indicator.setParentIndicatorId(0L);
            indicator.setAncestors(parentAncestors);
            indicator.setLevel(parentLevel);
        }
        indicator.setSort(0);
        indicator.setCreateBy(SecurityUtils.getUserId());
        indicator.setCreateTime(DateUtils.getNowDate());
        indicator.setUpdateTime(DateUtils.getNowDate());
        indicator.setUpdateBy(SecurityUtils.getUserId());
        indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        indicatorMapper.insertIndicator(indicator);
        indicatorDTO.setIndicatorId(indicator.getIndicatorId());
        return indicatorDTO;
    }

    /**
     * 修改指标表
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateIndicator(IndicatorDTO indicatorDTO) {
        String indicatorCode = indicatorDTO.getIndicatorCode();
        Long indicatorId = indicatorDTO.getIndicatorId();
        if (StringUtils.isEmpty(indicatorCode)) {
            throw new ServiceException("指标编码不能为空");
        }
        IndicatorDTO indicatorById = indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
        if (StringUtils.isNull(indicatorById)) {
            throw new ServiceException("该行业不存在");
        }
        IndicatorDTO indicatorByCode = indicatorMapper.checkUnique(indicatorCode);
        if (StringUtils.isNotNull(indicatorByCode)) {
            if (!indicatorByCode.getIndicatorId().equals(indicatorId)) {
                throw new ServiceException("更新指标" + indicatorDTO.getIndicatorName() + "失败,指标编码重复");
            }
        }
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        indicator.setUpdateTime(DateUtils.getNowDate());
        indicator.setUpdateBy(SecurityUtils.getUserId());
        return indicatorMapper.updateIndicator(indicator);
    }

    /**
     * 逻辑批量删除指标表
     *
     * @param indicatorIds 需要删除的指标表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorByIndicatorIds(List<Long> indicatorIds) {
        List<Long> exist = indicatorMapper.isExist(indicatorIds);
        if (StringUtils.isEmpty(exist)) {
            throw new ServiceException("指标不存在");
        }
//        addSons(indicatorIds);
        // todo 引用校验
        if (isQuote(indicatorIds)) {
            throw new ServiceException("存在被引用的指标");
        }
        return indicatorMapper.logicDeleteIndicatorByIndicatorIds(indicatorIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 添加子级
     *
     * @param indicatorIds
     */
    private void addSons(List<Long> indicatorIds) {
        List<Long> sons = indicatorMapper.selectSons(indicatorIds);//获取子级
        if (StringUtils.isNotEmpty(sons)) {
            indicatorIds.addAll(sons);
        }
    }

    /**
     * todo 引用校验
     *
     * @param indicatorIds
     * @return
     */
    private boolean isQuote(List<Long> indicatorIds) {
        return false;
    }

    /**
     * 物理删除指标表信息
     *
     * @param indicatorId 指标表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteIndicatorByIndicatorId(Long indicatorId) {
        return indicatorMapper.deleteIndicatorByIndicatorId(indicatorId);
    }

    /**
     * 指标详情
     *
     * @param indicatorId
     * @return
     */
    @Override
    public IndicatorDTO detailIndicator(Long indicatorId) {
        if (StringUtils.isNull(indicatorId)) {
            throw new ServiceException("指标详情id不能为空");
        }
        IndicatorDTO dto = indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
        if (StringUtils.isNull(dto)) {
            throw new ServiceException("该指标已经不存在");
        }
        return dto;
    }

    /**
     * 获取指标最大层级
     *
     * @return
     */
    @Override
    public List<Integer> getLevel() {
        return indicatorMapper.selectLevel();
    }

    /**
     * 逻辑删除指标表信息
     *
     * @param indicatorId 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorByIndicatorId(Long indicatorId) {
        ArrayList<Long> indicatorIds = new ArrayList<>();
        indicatorIds.add(indicatorId);
        return logicDeleteIndicatorByIndicatorIds(indicatorIds);
    }

    /**
     * 物理删除指标表信息
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorByIndicatorId(IndicatorDTO indicatorDTO) {
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        return indicatorMapper.deleteIndicatorByIndicatorId(indicator.getIndicatorId());
    }

    /**
     * 物理批量删除指标表
     *
     * @param indicatorDtos 需要删除的指标表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorByIndicatorIds(List<IndicatorDTO> indicatorDtos) {
        List<Long> stringList = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDtos) {
            stringList.add(indicatorDTO.getIndicatorId());
        }
        return indicatorMapper.deleteIndicatorByIndicatorIds(stringList);
    }

    /**
     * 批量新增指标表信息
     *
     * @param indicatorDtos 指标表对象
     */
    @Transactional
    public int insertIndicators(List<IndicatorDTO> indicatorDtos) {
        List<Indicator> indicatorList = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDtos) {
            Indicator indicator = new Indicator();
            BeanUtils.copyProperties(indicatorDTO, indicator);
            indicator.setCreateBy(SecurityUtils.getUserId());
            indicator.setCreateTime(DateUtils.getNowDate());
            indicator.setUpdateTime(DateUtils.getNowDate());
            indicator.setUpdateBy(SecurityUtils.getUserId());
            indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            indicatorList.add(indicator);
        }
        return indicatorMapper.batchIndicator(indicatorList);
    }

    /**
     * 批量修改指标表信息
     *
     * @param indicatorDtos 指标表对象
     */
    @Transactional
    public int updateIndicators(List<IndicatorDTO> indicatorDtos) {
        List<Indicator> indicatorList = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDtos) {
            Indicator indicator = new Indicator();
            BeanUtils.copyProperties(indicatorDTO, indicator);
            indicator.setCreateBy(SecurityUtils.getUserId());
            indicator.setCreateTime(DateUtils.getNowDate());
            indicator.setUpdateTime(DateUtils.getNowDate());
            indicator.setUpdateBy(SecurityUtils.getUserId());
            indicatorList.add(indicator);
        }
        return indicatorMapper.updateIndicators(indicatorList);
    }
}
