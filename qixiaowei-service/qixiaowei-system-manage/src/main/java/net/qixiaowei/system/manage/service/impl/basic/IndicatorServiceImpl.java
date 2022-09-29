package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.BusinessConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Indicator;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
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
        indicatorDTO.setIndicatorCategory(indicatorCategoryDTO.getIndicatorCategoryName());
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
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        List<IndicatorDTO> indicatorDTOS = indicatorMapper.selectIndicatorList(indicator);
        for (IndicatorDTO dto : indicatorDTOS) {
            Long indicatorCategoryId = dto.getIndicatorCategoryId();
            IndicatorCategoryDTO indicatorCategoryDTO = indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
            dto.setIndicatorCategory(indicatorCategoryDTO.getIndicatorCategoryName());
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
    public List<IndicatorDTO> selectTreeList(IndicatorDTO indicatorDTO) {
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        return indicatorMapper.selectIndicatorTree(indicator);
    }

    /**
     * 新增指标表
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertIndicator(IndicatorDTO indicatorDTO) {
        String indicatorCode = indicatorDTO.getIndicatorCode();
        if (StringUtils.isEmpty(indicatorCode)) {
            throw new ServiceException("指标编码不能为空");
        }
        if (indicatorMapper.checkUnique(indicatorCode) > 0) {
            throw new ServiceException("指标编码重复");
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
        //todo 指标排序
        indicator.setSort(0);
        indicator.setCreateBy(SecurityUtils.getUserId());
        indicator.setCreateTime(DateUtils.getNowDate());
        indicator.setUpdateTime(DateUtils.getNowDate());
        indicator.setUpdateBy(SecurityUtils.getUserId());
        indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return indicatorMapper.insertIndicator(indicator);
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
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        indicator.setUpdateTime(DateUtils.getNowDate());
        indicator.setUpdateBy(SecurityUtils.getUserId());
        return indicatorMapper.updateIndicator(indicator);
    }

    /**
     * 逻辑批量删除指标表
     *
     * @param indicatorDTOs 需要删除的指标表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorByIndicatorIds(List<IndicatorDTO> indicatorDTOs) {
        List<Long> stringList = new ArrayList<>();
        for (IndicatorDTO indicatorDTO : indicatorDTOs) {
            if (indicatorMapper.selectIndicatorByIndicatorId(indicatorDTO.getIndicatorId()) == null) {
                throw new ServiceException("指标分类不存在");
            }
            stringList.add(indicatorDTO.getIndicatorId());
        }
        List<Long> indicatorIds = indicatorMapper.selectSons(stringList);//获取子级
        for (Long indicator : indicatorIds) {
            // todo 引用校验
            if (isQuote(indicator)) {
                throw new ServiceException("存在被引用的行业");
            }
        }
        return indicatorMapper.logicDeleteIndicatorByIndicatorIds(indicatorIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * todo 引用校验
     *
     * @param indicatorId
     * @return
     */
    private boolean isQuote(Long indicatorId) {
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
     * 逻辑删除指标表信息
     *
     * @param indicatorDTO 指标表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorByIndicatorId(IndicatorDTO indicatorDTO) {
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        return indicatorMapper.logicDeleteIndicatorByIndicatorId(indicator, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
        List<Long> stringList = new ArrayList();
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
        List<Indicator> indicatorList = new ArrayList();

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
        List<Indicator> indicatorList = new ArrayList();

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

