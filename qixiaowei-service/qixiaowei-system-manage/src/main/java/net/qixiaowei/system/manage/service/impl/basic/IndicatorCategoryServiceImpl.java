package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.IndicatorCategory;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorCategoryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
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
    public int insertIndicatorCategory(IndicatorCategoryDTO indicatorCategoryDTO) {
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        String indicatorCategoryCode = "";
        if (StringUtils.isEmpty(indicatorCategory.getIndicatorCategoryCode())) {
            // todo 指标编码生成规律
            indicatorCategoryCode = "indicatorCategoryCode";
            indicatorCategory.setIndicatorCategoryCode(indicatorCategoryCode);
        } else {
            indicatorCategoryCode = indicatorCategoryDTO.getIndicatorCategoryCode();
            if (indicatorCategoryMapper.checkUnique(indicatorCategoryCode) > 0) {
                throw new ServiceException("指标类型编码重复");
            }
        }
        indicatorCategory.setCreateBy(SecurityUtils.getUserId());
        indicatorCategory.setCreateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
        indicatorCategory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return indicatorCategoryMapper.insertIndicatorCategory(indicatorCategory);
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
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        indicatorCategory.setUpdateTime(DateUtils.getNowDate());
        indicatorCategory.setUpdateBy(SecurityUtils.getUserId());
        return indicatorCategoryMapper.updateIndicatorCategory(indicatorCategory);
    }

    /**
     * 逻辑批量删除指标分类表
     *
     * @param indicatorCategoryDtos 需要删除的指标分类表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorCategoryByIndicatorCategoryIds(List<IndicatorCategoryDTO> indicatorCategoryDtos) {
        List<Long> stringList = new ArrayList();
        for (IndicatorCategoryDTO indicatorCategoryDTO : indicatorCategoryDtos) {
            if (indicatorMapper.selectIndicatorByIndicatorId(indicatorCategoryDTO.getIndicatorCategoryId()) == null) {
                throw new ServiceException("该指标分类不存在");
            }
            Long indicatorCategoryId = indicatorCategoryDTO.getIndicatorCategoryId();
            stringList.add(indicatorCategoryId);
            if (indicatorCategoryId == null) {
                throw new ServiceException("存在指标类型Id为空的数据");
            }
            // todo 引用校验
            if (isQuote(indicatorCategoryId) > 0) {
                throw new ServiceException("该指标类型配置已被引用");
            }
        }
        return indicatorCategoryMapper.logicDeleteIndicatorCategoryByIndicatorCategoryIds(stringList, indicatorCategoryDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 引用校验
     *
     * @param indicatorCategoryId 需要删除的指标分类表主键
     * @return 结果
     */
    private int isQuote(Long indicatorCategoryId) {
        return indicatorMapper.selectIndicatorCountByIndicatorCategoryId(indicatorCategoryId);
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
     * 逻辑删除指标分类表信息
     *
     * @param indicatorCategoryDTO 指标分类表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteIndicatorCategoryByIndicatorCategoryId(IndicatorCategoryDTO indicatorCategoryDTO) {
        IndicatorCategory indicatorCategory = new IndicatorCategory();
        BeanUtils.copyProperties(indicatorCategoryDTO, indicatorCategory);
        return indicatorCategoryMapper.logicDeleteIndicatorCategoryByIndicatorCategoryId(indicatorCategory, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
        List<Long> stringList = new ArrayList();
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
        List<IndicatorCategory> indicatorCategoryList = new ArrayList();

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
        List<IndicatorCategory> indicatorCategoryList = new ArrayList();

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

