package net.qixiaowei.operate.cloud.service.impl.performance;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceRankFactor;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceRankFactorMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceRankFactorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * PerformanceRankFactorService业务层处理
 *
 * @author Graves
 * @since 2022-10-06
 */
@Service
public class PerformanceRankFactorServiceImpl implements IPerformanceRankFactorService {
    @Autowired
    private PerformanceRankFactorMapper performanceRankFactorMapper;

    /**
     * 查询绩效等级系数
     *
     * @param performanceRankFactorId 绩效等级系数主键
     * @return 绩效等级系数
     */
    @Override
    public PerformanceRankFactorDTO selectPerformanceRankFactorByPerformanceRankFactorId(Long performanceRankFactorId) {
        return performanceRankFactorMapper.selectPerformanceRankFactorByPerformanceRankFactorId(performanceRankFactorId);
    }

    /**
     * 查询绩效等级系数列表
     *
     * @param performanceRankFactorDTO 绩效等级系数
     * @return 绩效等级系数
     */
    @Override
    public List<PerformanceRankFactorDTO> selectPerformanceRankFactorList(PerformanceRankFactorDTO performanceRankFactorDTO) {
        PerformanceRankFactor performanceRankFactor = new PerformanceRankFactor();
        BeanUtils.copyProperties(performanceRankFactorDTO, performanceRankFactor);
        return performanceRankFactorMapper.selectPerformanceRankFactorList(performanceRankFactor);
    }

    /**
     * 新增绩效等级系数
     *
     * @param performanceRankFactorDTO 绩效等级系数
     * @return 结果
     */
    @Transactional
    @Override
    public int insertPerformanceRankFactor(PerformanceRankFactorDTO performanceRankFactorDTO) {
        PerformanceRankFactor performanceRankFactor = new PerformanceRankFactor();
        BeanUtils.copyProperties(performanceRankFactorDTO, performanceRankFactor);
        performanceRankFactor.setCreateBy(SecurityUtils.getUserId());
        performanceRankFactor.setCreateTime(DateUtils.getNowDate());
        performanceRankFactor.setUpdateTime(DateUtils.getNowDate());
        performanceRankFactor.setUpdateBy(SecurityUtils.getUserId());
        performanceRankFactor.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return performanceRankFactorMapper.insertPerformanceRankFactor(performanceRankFactor);
    }

    /**
     * 修改绩效等级系数
     *
     * @param performanceRankFactorDTO 绩效等级系数
     * @return 结果
     */
    @Transactional
    @Override
    public int updatePerformanceRankFactor(PerformanceRankFactorDTO performanceRankFactorDTO) {
        PerformanceRankFactor performanceRankFactor = new PerformanceRankFactor();
        BeanUtils.copyProperties(performanceRankFactorDTO, performanceRankFactor);
        performanceRankFactor.setUpdateTime(DateUtils.getNowDate());
        performanceRankFactor.setUpdateBy(SecurityUtils.getUserId());
        return performanceRankFactorMapper.updatePerformanceRankFactor(performanceRankFactor);
    }


    /**
     * 逻辑批量删除绩效等级系数
     *
     * @param performanceRankFactorDtos 需要删除的绩效等级系数主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeletePerformanceRankFactorByPerformanceRankFactorDTO(List<PerformanceRankFactorDTO> performanceRankFactorDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDtos) {
            stringList.add(performanceRankFactorDTO.getPerformanceRankFactorId());
        }
        return performanceRankFactorMapper.logicDeletePerformanceRankFactorByPerformanceRankFactorIds(stringList, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 逻辑批量删除绩效等级系数
     *
     * @param performanceRankFactorIds 需要删除的绩效等级系数主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeletePerformanceRankFactorByPerformanceRankFactorIds(List<Long> performanceRankFactorIds) {
        return performanceRankFactorMapper.logicDeletePerformanceRankFactorByPerformanceRankFactorIds(performanceRankFactorIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效等级系数信息
     *
     * @param performanceRankFactorId 绩效等级系数主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePerformanceRankFactorByPerformanceRankFactorId(Long performanceRankFactorId) {
        return performanceRankFactorMapper.deletePerformanceRankFactorByPerformanceRankFactorId(performanceRankFactorId);
    }

    /**
     * 逻辑删除绩效等级系数信息
     *
     * @param performanceRankFactorDTO 绩效等级系数
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeletePerformanceRankFactorByPerformanceRankFactorId(PerformanceRankFactorDTO performanceRankFactorDTO) {
        PerformanceRankFactor performanceRankFactor = new PerformanceRankFactor();
        BeanUtils.copyProperties(performanceRankFactorDTO, performanceRankFactor);
        return performanceRankFactorMapper.logicDeletePerformanceRankFactorByPerformanceRankFactorId(performanceRankFactor, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效等级系数信息
     *
     * @param performanceRankFactorDTO 绩效等级系数
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePerformanceRankFactorByPerformanceRankFactorId(PerformanceRankFactorDTO performanceRankFactorDTO) {
        PerformanceRankFactor performanceRankFactor = new PerformanceRankFactor();
        BeanUtils.copyProperties(performanceRankFactorDTO, performanceRankFactor);
        return performanceRankFactorMapper.deletePerformanceRankFactorByPerformanceRankFactorId(performanceRankFactor.getPerformanceRankFactorId());
    }

    /**
     * 物理批量删除绩效等级系数
     *
     * @param performanceRankFactorDtos 需要删除的绩效等级系数主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePerformanceRankFactorByPerformanceRankFactorIds(List<PerformanceRankFactorDTO> performanceRankFactorDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDtos) {
            stringList.add(performanceRankFactorDTO.getPerformanceRankFactorId());
        }
        return performanceRankFactorMapper.deletePerformanceRankFactorByPerformanceRankFactorIds(stringList);
    }

    /**
     * 批量新增绩效等级系数信息
     *
     * @param performanceRankFactorDtos 绩效等级系数对象
     */
    @Override
    @Transactional
    public int insertPerformanceRankFactors(List<PerformanceRankFactorDTO> performanceRankFactorDtos) {
        List<PerformanceRankFactor> performanceRankFactorList = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDtos) {
            PerformanceRankFactor performanceRankFactor = new PerformanceRankFactor();
            BeanUtils.copyProperties(performanceRankFactorDTO, performanceRankFactor);
            performanceRankFactor.setCreateBy(SecurityUtils.getUserId());
            performanceRankFactor.setCreateTime(DateUtils.getNowDate());
            performanceRankFactor.setUpdateTime(DateUtils.getNowDate());
            performanceRankFactor.setUpdateBy(SecurityUtils.getUserId());
            performanceRankFactor.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceRankFactorList.add(performanceRankFactor);
        }
        return performanceRankFactorMapper.batchPerformanceRankFactor(performanceRankFactorList);
    }

    /**
     * 批量修改绩效等级系数信息
     *
     * @param performanceRankFactorDtos 绩效等级系数
     * @return
     */
    @Override
    public int updatePerformanceRankFactors(List<PerformanceRankFactorDTO> performanceRankFactorDtos) {
        List<PerformanceRankFactor> performanceRankFactorList = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDtos) {
            PerformanceRankFactor performanceRankFactor = new PerformanceRankFactor();
            BeanUtils.copyProperties(performanceRankFactorDTO, performanceRankFactor);
            performanceRankFactor.setCreateBy(SecurityUtils.getUserId());
            performanceRankFactor.setCreateTime(DateUtils.getNowDate());
            performanceRankFactor.setUpdateTime(DateUtils.getNowDate());
            performanceRankFactor.setUpdateBy(SecurityUtils.getUserId());
            performanceRankFactor.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceRankFactorList.add(performanceRankFactor);
        }
        return performanceRankFactorMapper.updatePerformanceRankFactors(performanceRankFactorList);
    }

    /**
     * 更新绩效等级系数
     *
     * @param performanceRankFactorAfter 绩效等级系数
     * @param performanceRankId          绩效等级Id
     * @return
     */
    @Override
    @Transactional
    public int operatePerformanceRankFactor(List<PerformanceRankFactorDTO> performanceRankFactorAfter, Long performanceRankId) {
        ArrayList<String> performanceRankFactorNames = new ArrayList<>();
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorAfter) {
            String performanceRankName = performanceRankFactorDTO.getPerformanceRankName();
            if (StringUtils.isEmpty(performanceRankName)) {
                throw new ServiceException("绩效等级系数名称不能为空");
            }
            if (performanceRankFactorNames.contains(performanceRankName)) {
                throw new ServiceException("绩效等级系数名称不能重复");
            }
            BigDecimal bonusFactor = performanceRankFactorDTO.getBonusFactor();
            if (StringUtils.isNull(bonusFactor)) {
                performanceRankFactorDTO.setBonusFactor(BigDecimal.ZERO);
            }
            performanceRankFactorDTO.setPerformanceRankId(performanceRankId);
            performanceRankFactorNames.add(performanceRankName);
        }
        int sort = 0;
        for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorAfter) {
            performanceRankFactorDTO.setSort(sort);
            sort++;
        }
        List<PerformanceRankFactorDTO> performanceRankFactorBefore
                = performanceRankFactorMapper.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
        //
        List<PerformanceRankFactorDTO> updatePerformanceRankFactor =
                performanceRankFactorAfter.stream().filter(performanceRankFactorDTO ->
                        performanceRankFactorBefore.stream().map(PerformanceRankFactorDTO::getPerformanceRankFactorId)
                                .collect(Collectors.toList()).contains(performanceRankFactorDTO.getPerformanceRankFactorId())
                ).collect(Collectors.toList());
        // 差集 Before中After的补集
        List<PerformanceRankFactorDTO> delPerformanceRankFactor =
                performanceRankFactorBefore.stream().filter(performanceRankFactorDTO ->
                        !performanceRankFactorAfter.stream().map(PerformanceRankFactorDTO::getPerformanceRankFactorId)
                                .collect(Collectors.toList()).contains(performanceRankFactorDTO.getPerformanceRankFactorId())
                ).collect(Collectors.toList());
        // 差集 After中Before的补集
        List<PerformanceRankFactorDTO> addPerformanceRankFactor =
                performanceRankFactorAfter.stream().filter(performanceRankFactorDTO ->
                        !performanceRankFactorBefore.stream().map(PerformanceRankFactorDTO::getPerformanceRankFactorId)
                                .collect(Collectors.toList()).contains(performanceRankFactorDTO.getPerformanceRankFactorId())
                ).collect(Collectors.toList());
        try {
            if (StringUtils.isNotEmpty(addPerformanceRankFactor)) {
                insertPerformanceRankFactors(addPerformanceRankFactor);
            }
            if (StringUtils.isNotEmpty(updatePerformanceRankFactor)) {
                updatePerformanceRankFactors(updatePerformanceRankFactor);
            }
            if (StringUtils.isNotEmpty(delPerformanceRankFactor)) {
                logicDeletePerformanceRankFactorByPerformanceRankFactorDTO(delPerformanceRankFactor);
            }
        } catch (ServiceException e) {
            throw new ServiceException(e.toString());
        }
        return 1;
    }

    /**
     * 根据绩效等级id查询等级系数
     *
     * @param performanceRankId
     * @return
     */
    @Override
    public List<PerformanceRankFactorDTO> selectPerformanceRankFactorByPerformanceRankId(Long performanceRankId) {
        return performanceRankFactorMapper.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
    }
}

