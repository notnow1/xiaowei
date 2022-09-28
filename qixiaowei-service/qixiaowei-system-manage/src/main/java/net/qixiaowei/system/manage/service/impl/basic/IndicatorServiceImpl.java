package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Indicator;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
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
 * @author TANGMICHI
 * @since 2022-09-28
 */
@Service
public class IndicatorServiceImpl implements IIndicatorService {
    @Autowired
    private IndicatorMapper indicatorMapper;

    /**
     * 查询指标表
     *
     * @param indicatorId 指标表主键
     * @return 指标表
     */
    @Override
    public IndicatorDTO selectIndicatorByIndicatorId(Long indicatorId) {
        return indicatorMapper.selectIndicatorByIndicatorId(indicatorId);
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
        return indicatorMapper.selectIndicatorList(indicator);
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
        Indicator indicator = new Indicator();
        BeanUtils.copyProperties(indicatorDTO, indicator);
        String indicatorCode = "";
        if (StringUtils.isEmpty(indicator.getIndicatorCode())) {
            // todo 指标编码生成规律
            indicatorCode = "industryCode";
            indicatorDTO.setIndicatorCode(indicatorCode);
        } else {
            indicatorCode = indicatorDTO.getIndicatorCode();
            if (indicatorMapper.checkUnique(indicatorCode) > 0) {
                throw new ServiceException("指标编码重复");
            }
        }

        String parentAncestors = "";//仅在非一级行业时有用
        int parentLevel = 0;
        if (indicatorDTO.getParentIndicatorId() != 0) {// 一级行业
            IndicatorDTO parentIndicator = indicatorMapper.selectIndicatorByIndicatorId(indicatorDTO.getParentIndicatorId());
            if (parentIndicator == null) {
                throw new ServiceException("该上级行业不存在");
            }
            parentAncestors = parentIndicator.getAncestors();
            parentLevel = parentIndicator.getLevel();
        }
        //todo 指标排序
        indicator.setSort(0);
        indicator.setIndicatorCode(indicatorCode);
        indicator.setCreateBy(SecurityUtils.getUserId());
        indicator.setCreateTime(DateUtils.getNowDate());
        indicator.setUpdateTime(DateUtils.getNowDate());
        indicator.setUpdateBy(SecurityUtils.getUserId());
        indicator.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            indicatorMapper.insertIndicator(indicator);
        } catch (Exception e) {
            return 0;
        }
        Long indicatorId = indicator.getIndicatorId();
        if (indicatorDTO.getParentIndicatorId() == 0) {// 一级行业
            indicator.setAncestors(indicatorId.toString());
            indicator.setLevel(1);
        } else {
            indicator.setAncestors(parentAncestors + "," + indicator.getIndicatorId());
            indicator.setLevel(parentLevel + 1);
        }
        try {
            return indicatorMapper.updateAncestors(indicator);
        } catch (Exception e) {
            return 0;
        }

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
        List<Long> stringList = new ArrayList();
        for (IndicatorDTO indicatorDTO : indicatorDTOs) {
            stringList.add(indicatorDTO.getIndicatorId());
        }
        List<Long> indicatorIds = indicatorMapper.selectSons(stringList);//获取子级
        for (Long indcator : indicatorIds) {
            // todo 引用校验
            if (isQuote(indcator)) {
                throw new ServiceException("存在被引用的行业");
            }
        }
        return indicatorMapper.logicDeleteIndicatorByIndicatorIds(stringList, indicatorDTOs.get(0).getUpdateBy(), DateUtils.getNowDate());
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

