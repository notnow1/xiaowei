package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcomeDetails;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeDetailsExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeDetailsMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeDetailsService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * TargetOutcomeDetailsService业务层处理
 *
 * @author Graves
 * @since 2022-11-07
 */
@Service
public class TargetOutcomeDetailsServiceImpl implements ITargetOutcomeDetailsService {
    @Autowired
    private TargetOutcomeDetailsMapper targetOutcomeDetailsMapper;

    @Autowired
    private RemoteIndicatorService indicatorService;

    /**
     * 查询目标结果详情表
     *
     * @param targetOutcomeDetailsId 目标结果详情表主键
     * @return 目标结果详情表
     */
    @Override
    public TargetOutcomeDetailsDTO selectTargetOutcomeDetailsByTargetOutcomeDetailsId(Long targetOutcomeDetailsId) {
        return targetOutcomeDetailsMapper.selectTargetOutcomeDetailsByTargetOutcomeDetailsId(targetOutcomeDetailsId);
    }

    /**
     * 查询目标结果详情表列表
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 目标结果详情表
     */
    @Override
    public List<TargetOutcomeDetailsDTO> selectTargetOutcomeDetailsList(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
        return targetOutcomeDetailsMapper.selectTargetOutcomeDetailsList(targetOutcomeDetails);
    }

    /**
     * 新增目标结果详情表
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 结果
     */
    @Override
    public TargetOutcomeDetailsDTO insertTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        // 当前的targetOutcomeDetailsDTO只有indicator的值
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = selectTargetOutcomeDetailsList(targetOutcomeDetailsDTO);
        if (StringUtils.isEmpty(targetOutcomeDetailsDTOList)) {
            setMonthValue(BigDecimal.ZERO, targetOutcomeDetailsDTO);
            TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
            BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
            targetOutcomeDetails.setCreateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setCreateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetOutcomeDetailsMapper.insertTargetOutcomeDetails(targetOutcomeDetails);
            targetOutcomeDetailsDTO.setTargetOutcomeDetailsId(targetOutcomeDetails.getTargetOutcomeDetailsId());
        }
        return targetOutcomeDetailsDTO;
    }

    /**
     * 修改目标结果详情表
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 结果
     */
    @Override
    public int updateTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
        targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
        targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
        return targetOutcomeDetailsMapper.updateTargetOutcomeDetails(targetOutcomeDetails);
    }

    /**
     * 逻辑批量删除目标结果详情表
     *
     * @param targetOutcomeDetailsIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(List<Long> targetOutcomeDetailsIds) {
        return targetOutcomeDetailsMapper.logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(targetOutcomeDetailsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 逻辑批量删除目标结果详情表根据指标ID和OutComeID
     *
     * @param indicators      指标IDs
     * @param targetOutcomeId
     * @return
     */
    @Override
    public int logicDeleteTargetOutcomeDetailsByOutcomeIdAndIndicator(List<Long> indicators, Long targetOutcomeId) {
        Long updateBy = SecurityUtils.getUserId();
        Date updateTime = DateUtils.getNowDate();
        return targetOutcomeDetailsMapper.logicDeleteTargetOutcomeDetailsByOutcomeIdAndIndicator(updateBy, updateTime, indicators, targetOutcomeId);
    }

    /**
     * 物理删除目标结果详情表信息
     *
     * @param targetOutcomeDetailsId 目标结果详情表主键
     * @return 结果
     */
    @Override
    public int deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(Long targetOutcomeDetailsId) {
        return targetOutcomeDetailsMapper.deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(targetOutcomeDetailsId);
    }

    /**
     * 根据outId查找目标结果详情表
     *
     * @param targetOutcomeId
     * @return
     */
    @Override
    public List<TargetOutcomeDetailsDTO> selectTargetOutcomeDetailsByOutcomeId(Long targetOutcomeId) {
        return targetOutcomeDetailsMapper.selectTargetOutcomeDetailsByOutcomeId(targetOutcomeId);
    }

    /**
     * 新增outCome
     *
     * @param indicatorIds
     * @param targetOutcomeId
     * @return
     */
    @Override
    public int addTargetOutcomeDetailsS(List<Long> indicatorIds, Long targetOutcomeId) {
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = new ArrayList<>();
        //todo BUG新增时会产生多余的预置指标
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOByIndicators = targetOutcomeDetailsMapper.selectTargetOutcomeDetailsByIndicatorIds(indicatorIds, targetOutcomeId);
        // 筛选掉已经存在的目标结果详情列表
        if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOByIndicators)) {
            for (int i = indicatorIds.size() - 1; i >= 0; i--) {
                for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTOByIndicator : targetOutcomeDetailsDTOByIndicators) {
                    if (targetOutcomeDetailsDTOByIndicator.getIndicatorId().equals(indicatorIds.get(i))) {
                        indicatorIds.remove(i);
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(indicatorIds)) {
            for (Long indicatorId : indicatorIds) {
                TargetOutcomeDetailsDTO targetOutcomeDetailsDTO = new TargetOutcomeDetailsDTO();
                targetOutcomeDetailsDTO.setIndicatorId(indicatorId);
                targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcomeId);
                targetOutcomeDetailsDTO.setActualTotal(BigDecimal.ZERO);//总值
                setMonthValue(BigDecimal.ZERO, targetOutcomeDetailsDTO);
                targetOutcomeDetailsDTOList.add(targetOutcomeDetailsDTO);
            }
            return insertTargetOutcomeDetailss(targetOutcomeDetailsDTOList);
        }
        return 1;
    }

    /**
     * 给月份赋值
     *
     * @param value
     * @param targetOutcomeDetailsDTO
     */
    private static void setMonthValue(BigDecimal value, TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        targetOutcomeDetailsDTO.setActualJanuary(value);
        targetOutcomeDetailsDTO.setActualFebruary(value);
        targetOutcomeDetailsDTO.setActualMarch(value);
        targetOutcomeDetailsDTO.setActualApril(value);
        targetOutcomeDetailsDTO.setActualMay(value);
        targetOutcomeDetailsDTO.setActualJune(value);
        targetOutcomeDetailsDTO.setActualJuly(value);
        targetOutcomeDetailsDTO.setActualAugust(value);
        targetOutcomeDetailsDTO.setActualSeptember(value);
        targetOutcomeDetailsDTO.setActualOctober(value);
        targetOutcomeDetailsDTO.setActualNovember(value);
        targetOutcomeDetailsDTO.setActualDecember(value);
        targetOutcomeDetailsDTO.setActualTotal(value);
    }

    /**
     * 逻辑删除目标结果详情表信息
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 结果
     */
    @Override
    public int logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsId(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        targetOutcomeDetails.setTargetOutcomeDetailsId(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
        targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
        targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
        return targetOutcomeDetailsMapper.logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsId(targetOutcomeDetails);
    }

    /**
     * 物理删除目标结果详情表信息
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 结果
     */

    @Override
    public int deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
        return targetOutcomeDetailsMapper.deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(targetOutcomeDetails.getTargetOutcomeDetailsId());
    }

    /**
     * 物理批量删除目标结果详情表
     *
     * @param targetOutcomeDetailsDtos 需要删除的目标结果详情表主键
     * @return 结果
     */

    @Override
    public int deleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDtos) {
        List<Long> stringList = new ArrayList<>();
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDtos) {
            stringList.add(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
        }
        return targetOutcomeDetailsMapper.deleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(stringList);
    }

    /**
     * 批量新增目标结果详情表信息
     *
     * @param targetOutcomeDetailsDtos 目标结果详情表对象
     */

    public int insertTargetOutcomeDetailss(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDtos) {
        List<TargetOutcomeDetails> targetOutcomeDetailsList = new ArrayList<>();
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDtos) {
            TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
            BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
            targetOutcomeDetails.setCreateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setCreateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetOutcomeDetailsList.add(targetOutcomeDetails);
        }
        return targetOutcomeDetailsMapper.batchTargetOutcomeDetails(targetOutcomeDetailsList);
    }

    /**
     * 批量修改目标结果详情表信息
     *
     * @param targetOutcomeDetailsDtos 目标结果详情表对象
     */

    public int updateTargetOutcomeDetailss(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDtos) {
        List<TargetOutcomeDetails> targetOutcomeDetailsList = new ArrayList<>();

        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDtos) {
            TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
            BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
            targetOutcomeDetails.setCreateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setCreateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
            targetOutcomeDetailsList.add(targetOutcomeDetails);
        }
        return targetOutcomeDetailsMapper.updateTargetOutcomeDetailss(targetOutcomeDetailsList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetOutcomeDetails(List<TargetOutcomeDetailsExcel> list) {
        List<TargetOutcomeDetails> targetOutcomeDetailsList = new ArrayList<>();
        list.forEach(l -> {
            TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
            BeanUtils.copyProperties(l, targetOutcomeDetails);
            targetOutcomeDetails.setCreateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setCreateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetOutcomeDetailsList.add(targetOutcomeDetails);
        });
        try {
            targetOutcomeDetailsMapper.batchTargetOutcomeDetails(targetOutcomeDetailsList);
        } catch (Exception e) {
            throw new ServiceException("导入目标结果详情表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param targetOutcomeDetailsDTO
     * @return
     */
    @Override
    public List<TargetOutcomeDetailsExcel> exportTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDetailsMapper.selectTargetOutcomeDetailsList(targetOutcomeDetails);
        List<TargetOutcomeDetailsExcel> targetOutcomeDetailsExcelList = new ArrayList<>();
        return targetOutcomeDetailsExcelList;
    }
}

