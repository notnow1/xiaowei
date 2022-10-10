package net.qixiaowei.operate.cloud.service.impl.performance;

import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformancePercentage;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformancePercentageMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformancePercentageService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * PerformancePercentageService业务层处理
 *
 * @author Graves
 * @since 2022-10-10
 */
@Service
public class PerformancePercentageServiceImpl implements IPerformancePercentageService {
    @Autowired
    private PerformancePercentageMapper performancePercentageMapper;

    /**
     * 查询绩效比例表
     *
     * @param performancePercentageId 绩效比例表主键
     * @return 绩效比例表
     */
    @Override
    public PerformancePercentageDTO selectPerformancePercentageByPerformancePercentageId(Long performancePercentageId) {
        return performancePercentageMapper.selectPerformancePercentageByPerformancePercentageId(performancePercentageId);
    }

    /**
     * 查询绩效比例表列表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 绩效比例表
     */
    @Override
    public List<PerformancePercentageDTO> selectPerformancePercentageList(PerformancePercentageDTO performancePercentageDTO) {
        PerformancePercentage performancePercentage = new PerformancePercentage();
        BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
        return performancePercentageMapper.selectPerformancePercentageList(performancePercentage);
    }

    /**
     * 新增绩效比例表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    @Override
    public int insertPerformancePercentage(PerformancePercentageDTO performancePercentageDTO) {
        String performancePercentageName = performancePercentageDTO.getPerformancePercentageName();
        List<Map<Long, String>> informationList = performancePercentageDTO.getInformationList();
        int count = performancePercentageMapper.isUnique(performancePercentageName);
        if (count > 0) {
            throw new ServiceException("该绩效比例名称重复");
        }
        // todo 校验组织和个人绩效等级是否存在且对应
        PerformancePercentage performancePercentage = new PerformancePercentage();
        BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
        performancePercentage.setCreateBy(SecurityUtils.getUserId());
        performancePercentage.setCreateTime(DateUtils.getNowDate());
        performancePercentage.setUpdateTime(DateUtils.getNowDate());
        performancePercentage.setUpdateBy(SecurityUtils.getUserId());
        performancePercentage.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        performancePercentageMapper.insertPerformancePercentage(performancePercentage);
        if (StringUtils.isNotEmpty(informationList)) {

        }

        return 0;
    }

    /**
     * 修改绩效比例表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    @Override
    public int updatePerformancePercentage(PerformancePercentageDTO performancePercentageDTO) {
        PerformancePercentage performancePercentage = new PerformancePercentage();
        BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
        performancePercentage.setUpdateTime(DateUtils.getNowDate());
        performancePercentage.setUpdateBy(SecurityUtils.getUserId());
        return performancePercentageMapper.updatePerformancePercentage(performancePercentage);
    }

    /**
     * 逻辑批量删除绩效比例表
     *
     * @param performancePercentageDtos 需要删除的绩效比例表主键
     * @return 结果
     */
    @Override
    public int logicDeletePerformancePercentageByPerformancePercentageIds(List<PerformancePercentageDTO> performancePercentageDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformancePercentageDTO performancePercentageDTO : performancePercentageDtos) {
            stringList.add(performancePercentageDTO.getPerformancePercentageId());
        }
        return performancePercentageMapper.logicDeletePerformancePercentageByPerformancePercentageIds(stringList, performancePercentageDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效比例表信息
     *
     * @param performancePercentageId 绩效比例表主键
     * @return 结果
     */
    @Override
    public int deletePerformancePercentageByPerformancePercentageId(Long performancePercentageId) {
        return performancePercentageMapper.deletePerformancePercentageByPerformancePercentageId(performancePercentageId);
    }

    /**
     * 逻辑删除绩效比例表信息
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    @Override
    public int logicDeletePerformancePercentageByPerformancePercentageId(PerformancePercentageDTO performancePercentageDTO) {
        PerformancePercentage performancePercentage = new PerformancePercentage();
        performancePercentage.setPerformancePercentageId(performancePercentageDTO.getPerformancePercentageId());
        performancePercentage.setUpdateTime(DateUtils.getNowDate());
        performancePercentage.setUpdateBy(SecurityUtils.getUserId());
        return performancePercentageMapper.logicDeletePerformancePercentageByPerformancePercentageId(performancePercentage);
    }

    /**
     * 物理删除绩效比例表信息
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    @Override
    public int deletePerformancePercentageByPerformancePercentageId(PerformancePercentageDTO performancePercentageDTO) {
        PerformancePercentage performancePercentage = new PerformancePercentage();
        BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
        return performancePercentageMapper.deletePerformancePercentageByPerformancePercentageId(performancePercentage.getPerformancePercentageId());
    }

    /**
     * 物理批量删除绩效比例表
     *
     * @param performancePercentageDtos 需要删除的绩效比例表主键
     * @return 结果
     */
    @Override
    public int deletePerformancePercentageByPerformancePercentageIds(List<PerformancePercentageDTO> performancePercentageDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformancePercentageDTO performancePercentageDTO : performancePercentageDtos) {
            stringList.add(performancePercentageDTO.getPerformancePercentageId());
        }
        return performancePercentageMapper.deletePerformancePercentageByPerformancePercentageIds(stringList);
    }

    /**
     * 批量修改绩效比例表信息
     *
     * @param performancePercentageDtos 绩效比例表对象
     */
    public int updatePerformancePercentages(List<PerformancePercentageDTO> performancePercentageDtos) {
        List<PerformancePercentage> performancePercentageList = new ArrayList<>();

        for (PerformancePercentageDTO performancePercentageDTO : performancePercentageDtos) {
            PerformancePercentage performancePercentage = new PerformancePercentage();
            BeanUtils.copyProperties(performancePercentageDTO, performancePercentage);
            performancePercentage.setCreateBy(SecurityUtils.getUserId());
            performancePercentage.setCreateTime(DateUtils.getNowDate());
            performancePercentage.setUpdateTime(DateUtils.getNowDate());
            performancePercentage.setUpdateBy(SecurityUtils.getUserId());
            performancePercentageList.add(performancePercentage);
        }
        return performancePercentageMapper.updatePerformancePercentages(performancePercentageList);
    }
}

