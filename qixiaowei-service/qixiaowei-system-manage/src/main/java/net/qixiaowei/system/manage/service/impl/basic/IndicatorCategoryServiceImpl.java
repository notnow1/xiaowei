package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.IndicatorCategory;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import net.qixiaowei.system.manage.mapper.basic.IndicatorCategoryMapper;
import net.qixiaowei.system.manage.mapper.basic.IndicatorMapper;
import net.qixiaowei.system.manage.service.basic.IIndicatorCategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * IndicatorCategoryService业务层处理
 *
 * @author Graves
 * @since 2022-09-28
 */
@Service
public class IndicatorCategoryServiceImpl implements IIndicatorCategoryService {
    @Autowired
    private IndicatorCategoryMapper indicatorCategoryMapper;

    @Autowired
    private IndicatorMapper indicatorMapper;

    /**
     * 查询指标分类表
     *
     * @param indicatorCategoryId 指标分类表主键
     * @return 指标分类表
     */
    @Override
    public IndicatorCategoryDTO selectIndicatorCategoryByIndicatorCategoryId(Long indicatorCategoryId) {
        return indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
    }

    /**
     * 查询指标分类表列表
     *
     * @param indicatorCategoryDTO 指标分类表
     * @return 指标分类表
     */
    @Override
    public List<IndicatorCategoryDTO> selectIndicatorCategoryList(IndicatorCategoryDTO indicatorCategoryDTO) {
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        return indicatorCategoryMapper.selectIndicatorCategoryList(indicatorCategory);
    }

    /**
     * 新增指标分类表
     *
     * @param indicatorCategoryDTO 指标分类表
     * @return 结果
     */
    @Transactional
    @Override
    public IndicatorCategoryDTO insertIndicatorCategory(IndicatorCategoryDTO indicatorCategoryDTO) {
        String indicatorCategoryCode = indicatorCategoryDTO.getIndicatorCategoryCode();
        String indicatorCategoryName = indicatorCategoryDTO.getIndicatorCategoryName();
        if (StringUtils.isEmpty(indicatorCategoryName)) {
            throw new ServiceException("指标分类名称不能为空");
        }
        if (StringUtils.isEmpty(indicatorCategoryCode)) {
            throw new ServiceException("指标分类编码不能为空");
        }
        if (indicatorCategoryMapper.checkUnique(indicatorCategoryCode) > 0) {
            throw new ServiceException("指标分类编码重复");
        }
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        indicatorCategory.setCreateBy(SecurityUtils.getUserId());
        indicatorCategory.setCreateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
        indicatorCategory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        int i = indicatorCategoryMapper.insertIndicatorCategory(indicatorCategory);
        if (i == 1) {
            Long indicatorCategoryId = indicatorCategory.getIndicatorCategoryId();
            if (StringUtils.isNotNull(indicatorCategoryId)) {
                indicatorCategoryDTO.setIndicatorCategoryId(indicatorCategoryId);
            }
            return indicatorCategoryDTO;
        }
        throw new ServiceException("指标分类新增失败");
    }

    /**
     * 修改指标分类表
     *
     * @param indicatorCategoryDTO 指标分类表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateIndicatorCategory(IndicatorCategoryDTO indicatorCategoryDTO) {
        String indicatorCategoryCode = indicatorCategoryDTO.getIndicatorCategoryCode();
        Long indicatorCategoryId = indicatorCategoryDTO.getIndicatorCategoryId();
        if (StringUtils.isNull(indicatorCategoryId)) {
            throw new ServiceException("指标分类id不能为空");
        }
        if (StringUtils.isEmpty(indicatorCategoryCode)) {
            throw new ServiceException("指标分类编码不能为空");
        }
        IndicatorCategoryDTO isExist = indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
        if (StringUtils.isNull(isExist)) {
            throw new ServiceException("当前指标分类不存在");
        }
        if (indicatorCategoryMapper.checkUnique(indicatorCategoryCode) > 0) {
            throw new ServiceException("指标分类编码重复");
        }
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        indicatorCategory.setUpdateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
        return indicatorCategoryMapper.updateIndicatorCategory(indicatorCategory);
    }

    /**
     * 逻辑批量删除指标分类表
     *
     * @param indicatorCategoryIds 需要删除的指标分类表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorCategoryByIndicatorCategoryIds(List<Long> indicatorCategoryIds) {
        List<Long> exist = indicatorCategoryMapper.isExist(indicatorCategoryIds);
        if (StringUtils.isEmpty(exist)) {
            throw new ServiceException("指标分类不存在");
        }
        if (isQuote(indicatorCategoryIds) > 0) {
            throw new ServiceException("存在被正在引用的指标");
        }
        return indicatorCategoryMapper.logicDeleteIndicatorCategoryByIndicatorCategoryIds(indicatorCategoryIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 引用校验
     *
     * @param indicatorCategoryIds 需要删除的指标分类表主键
     * @return 结果
     */
    private int isQuote(List<Long> indicatorCategoryIds) {
        return indicatorMapper.selectIndicatorCountByIndicatorCategoryId(indicatorCategoryIds);
    }

    /**
     * 物理删除指标分类表信息
     *
     * @param indicatorCategoryId 指标分类表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorCategoryByIndicatorCategoryId(Long indicatorCategoryId) {
        return indicatorCategoryMapper.deleteIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
    }

    /**
     * 指标类型详情
     *
     * @param indicatorCategoryId
     * @return
     */
    @Override
    public IndicatorCategoryDTO detailIndicatorCategory(Long indicatorCategoryId) {
        if (StringUtils.isNull(indicatorCategoryId)) {
            throw new ServiceException("指标类型id不能为空");
        }
        IndicatorCategoryDTO indicatorCategoryDTO = indicatorCategoryMapper.selectIndicatorCategoryByIndicatorCategoryId(indicatorCategoryId);
        if (StringUtils.isNull(indicatorCategoryDTO)) {
            throw new ServiceException("指标类型已不存在");
        }
        return indicatorCategoryDTO;
    }

    /**
     * 逻辑删除指标分类表信息
     *
     * @param indicatorCategoryId 指标分类表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorCategoryByIndicatorCategoryId(Long indicatorCategoryId) {
        ArrayList<Long> indicatorCategoryIds = new ArrayList<>();
        indicatorCategoryIds.add(indicatorCategoryId);
        return logicDeleteIndicatorCategoryByIndicatorCategoryIds(indicatorCategoryIds);
    }

    /**
     * 物理删除指标分类表信息
     *
     * @param indicatorCategoryDTO 指标分类表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorCategoryByIndicatorCategoryId(IndicatorCategoryDTO indicatorCategoryDTO) {
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        return indicatorCategoryMapper.deleteIndicatorCategoryByIndicatorCategoryId(indicatorCategory.getIndicatorCategoryId());
    }

    /**
     * 物理批量删除指标分类表
     *
     * @param indicatorCategoryDtos 需要删除的指标分类表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteIndicatorCategoryByIndicatorCategoryIds(List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        List<Long> stringList = new ArrayList<>();
        for (IndicatorCategoryDTO indicatorCategoryDTO : indicatorCategoryDtos) {
            stringList.add(indicatorCategoryDTO.getIndicatorCategoryId());
        }
        return indicatorCategoryMapper.deleteIndicatorCategoryByIndicatorCategoryIds(stringList);
    }

    /**
     * 批量新增指标分类表信息
     *
     * @param indicatorCategoryDtos 指标分类表对象
     */
    @Transactional
    public int insertIndicatorCategorys(List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        List<IndicatorCategory> indicatorCategoryList = new ArrayList<>();
        for (IndicatorCategoryDTO indicatorCategoryDTO : indicatorCategoryDtos) {
            IndicatorCategory indicatorCategory = new IndicatorCategory();
            BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
            indicatorCategory.setCreateBy(SecurityUtils.getUserId());
            indicatorCategory.setCreateTime(DateUtils.getNowDate());
            indicatorCategory.setUpdateTime(DateUtils.getNowDate());
            indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
            indicatorCategory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            indicatorCategoryList.add(indicatorCategory);
        }
        return indicatorCategoryMapper.batchIndicatorCategory(indicatorCategoryList);
    }

    /**
     * 批量修改指标分类表信息
     *
     * @param indicatorCategoryDtos 指标分类表对象
     */
    @Transactional
    public int updateIndicatorCategorys(List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        List<IndicatorCategory> indicatorCategoryList = new ArrayList<>();

        for (IndicatorCategoryDTO indicatorCategoryDTO : indicatorCategoryDtos) {
            IndicatorCategory indicatorCategory = new IndicatorCategory();
            BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
            indicatorCategory.setCreateBy(SecurityUtils.getUserId());
            indicatorCategory.setCreateTime(DateUtils.getNowDate());
            indicatorCategory.setUpdateTime(DateUtils.getNowDate());
            indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
            indicatorCategoryList.add(indicatorCategory);
        }
        return indicatorCategoryMapper.updateIndicatorCategorys(indicatorCategoryList);
    }
}

