package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcome;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeDetailsService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


/**
 * TargetOutcomeService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
@Service
public class TargetOutcomeServiceImpl implements ITargetOutcomeService {
    @Autowired
    private TargetOutcomeMapper targetOutcomeMapper;

    @Autowired
    private RemoteIndicatorService indicatorService;

    @Autowired
    private ITargetOutcomeDetailsService targetOutcomeDetailsService;

    /**
     * 查询目标结果表
     *
     * @param targetOutcomeId 目标结果表主键
     * @return 目标结果表
     */
    @Override
    public TargetOutcomeDTO selectTargetOutcomeByTargetOutcomeId(Long targetOutcomeId) {
        TargetOutcomeDTO targetOutcomeDTO = targetOutcomeMapper.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDetailsService.selectTargetOutcomeDetailsByOutcomeId(targetOutcomeId);
        List<Long> indicatorIds = new ArrayList<>();
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            Long indicatorId = targetOutcomeDetailsDTO.getIndicatorId();
            Long createBy = targetOutcomeDetailsDTO.getCreateBy();
            indicatorIds.add(indicatorId);
        }
        R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorDTOS = indicatorR.getData();
        if (indicatorR.getCode() != 200 || StringUtils.isEmpty(indicatorDTOS)) {
            throw new ServiceException("远程调用指标失败 请咨询管理员");
        }
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                if (targetOutcomeDetailsDTO.getIndicatorId().equals(indicatorDTO.getIndicatorId())) {
                    targetOutcomeDetailsDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                    break;
                }
            }
        }
        // 判断当前年月,然后从targetOutcomeDetailsDTOList转向targetOutcomeDetailsDTOS
        int year = DateUtils.getYear();
        int month = DateUtils.getMonth();
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS = new ArrayList<>();
        List<BigDecimal> monthValue = new ArrayList<>();
        if (targetOutcomeDTO.getTargetYear() < year) {
            setAllMonth(targetOutcomeDetailsDTOList, monthValue, targetOutcomeDetailsDTOS);// 存放所有月份
        } else if (targetOutcomeDTO.getTargetYear() == year) {
            setSomeMonth(targetOutcomeDetailsDTOList, month, monthValue, targetOutcomeDetailsDTOS);//存放部分月份
        } else {
            setOtherValue(targetOutcomeDetailsDTOList, targetOutcomeDetailsDTOS);//不存放月份
        }
        targetOutcomeDTO.setTargetOutcomeDetailsDTOList(targetOutcomeDetailsDTOS);
        return targetOutcomeDTO;
    }

    private void setOtherValue(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList, List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS) {
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            TargetOutcomeDetailsDTO targetOutcomeDetails = new TargetOutcomeDetailsDTO();
            targetOutcomeDetails.setActualTotal(targetOutcomeDetailsDTO.getActualTotal());
            targetOutcomeDetails.setIndicatorName(targetOutcomeDetailsDTO.getIndicatorName());
            targetOutcomeDetails.setIndicatorId(targetOutcomeDetailsDTO.getIndicatorId());
            targetOutcomeDetails.setTargetOutcomeId(targetOutcomeDetailsDTO.getTargetOutcomeId());
            targetOutcomeDetails.setTargetOutcomeDetailsId(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
            targetOutcomeDetails.setCreateBy(targetOutcomeDetailsDTO.getCreateBy());
            targetOutcomeDetails.setCreateTime(targetOutcomeDetailsDTO.getCreateTime());
            targetOutcomeDetailsDTOS.add(targetOutcomeDetails);
        }
    }

    /**
     * 存放部分月份
     *
     * @param targetOutcomeDetailsDTOList
     * @param month
     * @param monthValue
     * @param targetOutcomeDetailsDTOS
     */
    private void setSomeMonth(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList, int month, List<BigDecimal> monthValue, List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS) {
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            monthValue = new ArrayList<>();
            TargetOutcomeDetailsDTO targetOutcomeDetails = new TargetOutcomeDetailsDTO();
            targetOutcomeDetails.setActualTotal(targetOutcomeDetailsDTO.getActualTotal());
            targetOutcomeDetails.setIndicatorName(targetOutcomeDetailsDTO.getIndicatorName());
            targetOutcomeDetails.setIndicatorId(targetOutcomeDetailsDTO.getIndicatorId());
            targetOutcomeDetails.setTargetOutcomeId(targetOutcomeDetailsDTO.getTargetOutcomeId());
            targetOutcomeDetails.setTargetOutcomeDetailsId(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
            targetOutcomeDetails.setCreateBy(targetOutcomeDetailsDTO.getCreateBy());
            targetOutcomeDetails.setCreateTime(targetOutcomeDetailsDTO.getCreateTime());
            int i = 1;
            while (i < month) {
                switch (i) {
                    case 1:
                        monthValue.add(targetOutcomeDetailsDTO.getActualJanuary());
                        break;
                    case 2:
                        monthValue.add(targetOutcomeDetailsDTO.getActualFebruary());
                        break;
                    case 3:
                        monthValue.add(targetOutcomeDetailsDTO.getActualMarch());
                        break;
                    case 4:
                        monthValue.add(targetOutcomeDetailsDTO.getActualApril());
                        break;
                    case 5:
                        monthValue.add(targetOutcomeDetailsDTO.getActualMay());
                        break;
                    case 6:
                        monthValue.add(targetOutcomeDetailsDTO.getActualJune());
                        break;
                    case 7:
                        monthValue.add(targetOutcomeDetailsDTO.getActualJuly());
                        break;
                    case 8:
                        monthValue.add(targetOutcomeDetailsDTO.getActualAugust());
                        break;
                    case 9:
                        monthValue.add(targetOutcomeDetailsDTO.getActualSeptember());
                        break;
                    case 10:
                        monthValue.add(targetOutcomeDetailsDTO.getActualOctober());
                        break;
                    case 11:
                        monthValue.add(targetOutcomeDetailsDTO.getActualNovember());
                        break;
                }
                i++;
            }
            targetOutcomeDetails.setMonthValue(monthValue);
            targetOutcomeDetailsDTOS.add(targetOutcomeDetails);
        }
    }

    /**
     * 存放所有月份
     *
     * @param targetOutcomeDetailsDTOList
     * @param monthValue
     * @param targetOutcomeDetailsDTOS
     */
    private void setAllMonth(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList, List<BigDecimal> monthValue, List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS) {
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            monthValue = new ArrayList<>();
            TargetOutcomeDetailsDTO targetOutcomeDetails = new TargetOutcomeDetailsDTO();
            targetOutcomeDetails.setActualTotal(targetOutcomeDetailsDTO.getActualTotal());
            targetOutcomeDetails.setIndicatorName(targetOutcomeDetailsDTO.getIndicatorName());
            targetOutcomeDetails.setIndicatorId(targetOutcomeDetailsDTO.getIndicatorId());
            targetOutcomeDetails.setTargetOutcomeId(targetOutcomeDetailsDTO.getTargetOutcomeId());
            targetOutcomeDetails.setTargetOutcomeDetailsId(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
            targetOutcomeDetails.setCreateBy(targetOutcomeDetailsDTO.getCreateBy());
            targetOutcomeDetails.setCreateTime(targetOutcomeDetailsDTO.getCreateTime());
            monthValue.add(targetOutcomeDetailsDTO.getActualJanuary());
            monthValue.add(targetOutcomeDetailsDTO.getActualFebruary());
            monthValue.add(targetOutcomeDetailsDTO.getActualMarch());
            monthValue.add(targetOutcomeDetailsDTO.getActualApril());
            monthValue.add(targetOutcomeDetailsDTO.getActualMay());
            monthValue.add(targetOutcomeDetailsDTO.getActualJune());
            monthValue.add(targetOutcomeDetailsDTO.getActualJuly());
            monthValue.add(targetOutcomeDetailsDTO.getActualAugust());
            monthValue.add(targetOutcomeDetailsDTO.getActualSeptember());
            monthValue.add(targetOutcomeDetailsDTO.getActualOctober());
            monthValue.add(targetOutcomeDetailsDTO.getActualNovember());
            monthValue.add(targetOutcomeDetailsDTO.getActualDecember());
            targetOutcomeDetails.setMonthValue(monthValue);
            targetOutcomeDetailsDTOS.add(targetOutcomeDetails);
        }
    }

    /**
     * 查询目标结果表列表
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 目标结果表
     */
    @Override
    public List<TargetOutcomeDTO> selectTargetOutcomeList(TargetOutcomeDTO targetOutcomeDTO) {
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
        List<Long> createBys = new ArrayList<>();
        List<TargetOutcomeDTO> targetOutcomeDTOS = targetOutcomeMapper.selectTargetOutcomeList(targetOutcome);
        for (TargetOutcomeDTO outcomeDTO : targetOutcomeDTOS) {
            createBys.add(outcomeDTO.getCreateBy());
        }
        // todo 根据createBys获取创建人名称  setCreateByName
        return targetOutcomeDTOS;
    }

    /**
     * 新增目标结果表
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetOutcomeDTO insertTargetOutcome(TargetOutcomeDTO targetOutcomeDTO) {
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
        targetOutcome.setCreateBy(SecurityUtils.getUserId());
        targetOutcome.setCreateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateBy(SecurityUtils.getUserId());
        targetOutcome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetOutcomeMapper.insertTargetOutcome(targetOutcome);
        targetOutcomeDTO.setTargetOutcomeId(targetOutcome.getTargetOutcomeId());
        return targetOutcomeDTO;
    }

    /**
     * 修改目标结果表
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateTargetOutcome(TargetOutcomeDTO targetOutcomeDTO) {
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDTO.getTargetOutcomeDetailsDTOList();
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
        targetOutcome.setUpdateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateBy(SecurityUtils.getUserId());
        // todo 更新 targetOutcomeDetail
        if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOList)) {
            for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
                targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcome.getTargetOutcomeId());
            }
            targetOutcomeDetailsService.updateTargetOutcomeDetailss(targetOutcomeDetailsDTOList);
        }
        return targetOutcomeMapper.updateTargetOutcome(targetOutcome);
    }

    /**
     * 逻辑批量删除目标结果表
     *
     * @param targetOutcomeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteTargetOutcomeByTargetOutcomeIds(List<Long> targetOutcomeIds) {
        return targetOutcomeMapper.logicDeleteTargetOutcomeByTargetOutcomeIds(targetOutcomeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标结果表信息
     *
     * @param targetOutcomeId 目标结果表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteTargetOutcomeByTargetOutcomeId(Long targetOutcomeId) {
        return targetOutcomeMapper.deleteTargetOutcomeByTargetOutcomeId(targetOutcomeId);
    }

    /**
     * 逻辑删除目标结果表信息
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteTargetOutcomeByTargetOutcomeId(TargetOutcomeDTO targetOutcomeDTO) {
        TargetOutcome targetOutcome = new TargetOutcome();
        targetOutcome.setTargetOutcomeId(targetOutcomeDTO.getTargetOutcomeId());
        targetOutcome.setUpdateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateBy(SecurityUtils.getUserId());
        return targetOutcomeMapper.logicDeleteTargetOutcomeByTargetOutcomeId(targetOutcome);
    }

    /**
     * 物理删除目标结果表信息
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 结果
     */

    @Override
    public int deleteTargetOutcomeByTargetOutcomeId(TargetOutcomeDTO targetOutcomeDTO) {
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
        return targetOutcomeMapper.deleteTargetOutcomeByTargetOutcomeId(targetOutcome.getTargetOutcomeId());
    }

    /**
     * 物理批量删除目标结果表
     *
     * @param targetOutcomeDtoS 需要删除的目标结果表主键
     * @return 结果
     */

    @Override
    public int deleteTargetOutcomeByTargetOutcomeIds(List<TargetOutcomeDTO> targetOutcomeDtoS) {
        List<Long> stringList = new ArrayList<>();
        for (TargetOutcomeDTO targetOutcomeDTO : targetOutcomeDtoS) {
            stringList.add(targetOutcomeDTO.getTargetOutcomeId());
        }
        return targetOutcomeMapper.deleteTargetOutcomeByTargetOutcomeIds(stringList);
    }

    /**
     * 批量新增目标结果表信息
     *
     * @param targetOutcomeDtoS 目标结果表对象
     */

    public int insertTargetOutcomes(List<TargetOutcomeDTO> targetOutcomeDtoS) {
        List<TargetOutcome> targetOutcomeList = new ArrayList<>();

        for (TargetOutcomeDTO targetOutcomeDTO : targetOutcomeDtoS) {
            TargetOutcome targetOutcome = new TargetOutcome();
            BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
            targetOutcome.setCreateBy(SecurityUtils.getUserId());
            targetOutcome.setCreateTime(DateUtils.getNowDate());
            targetOutcome.setUpdateTime(DateUtils.getNowDate());
            targetOutcome.setUpdateBy(SecurityUtils.getUserId());
            targetOutcome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetOutcomeList.add(targetOutcome);
        }
        return targetOutcomeMapper.batchTargetOutcome(targetOutcomeList);
    }

    /**
     * 批量修改目标结果表信息
     *
     * @param targetOutcomeDtoS 目标结果表对象
     */

    public int updateTargetOutcomes(List<TargetOutcomeDTO> targetOutcomeDtoS) {
        List<TargetOutcome> targetOutcomeList = new ArrayList<>();

        for (TargetOutcomeDTO targetOutcomeDTO : targetOutcomeDtoS) {
            TargetOutcome targetOutcome = new TargetOutcome();
            BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
            targetOutcome.setCreateBy(SecurityUtils.getUserId());
            targetOutcome.setCreateTime(DateUtils.getNowDate());
            targetOutcome.setUpdateTime(DateUtils.getNowDate());
            targetOutcome.setUpdateBy(SecurityUtils.getUserId());
            targetOutcomeList.add(targetOutcome);
        }
        return targetOutcomeMapper.updateTargetOutcomes(targetOutcomeList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetOutcome(List<TargetOutcomeExcel> list) {
        List<TargetOutcome> targetOutcomeList = new ArrayList<>();
        list.forEach(l -> {
            TargetOutcome targetOutcome = new TargetOutcome();
            BeanUtils.copyProperties(l, targetOutcome);
            targetOutcome.setCreateBy(SecurityUtils.getUserId());
            targetOutcome.setCreateTime(DateUtils.getNowDate());
            targetOutcome.setUpdateTime(DateUtils.getNowDate());
            targetOutcome.setUpdateBy(SecurityUtils.getUserId());
            targetOutcome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetOutcomeList.add(targetOutcome);
        });
        try {
            targetOutcomeMapper.batchTargetOutcome(targetOutcomeList);
        } catch (Exception e) {
            throw new ServiceException("导入目标结果表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param targetOutcomeDTO
     * @return
     */
    @Override
    public List<TargetOutcomeExcel> exportTargetOutcome(TargetOutcomeDTO targetOutcomeDTO) {
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
        List<TargetOutcomeDTO> targetOutcomeDTOList = targetOutcomeMapper.selectTargetOutcomeList(targetOutcome);
        List<TargetOutcomeExcel> targetOutcomeExcelList = new ArrayList<>();
        return targetOutcomeExcelList;
    }

    /**
     * 通过targetYear查找Target Outcome DTO
     *
     * @param targetYear
     * @return
     */
    @Override
    public TargetOutcomeDTO selectTargetOutcomeByTargetYear(Integer targetYear) {
        return targetOutcomeMapper.selectTargetOutcomeByTargetYear(targetYear);
    }

    /**
     * 通过targetSetting列表更新目标结果表
     *
     * @param updateTargetSetting
     * @param targetYear
     * @return
     */
    @Override
    public int changeTargetOutcome(List<TargetSettingDTO> updateTargetSetting, Integer targetYear) {
        TargetOutcomeDTO targetOutcomeDTO = selectTargetOutcomeByTargetYear(targetYear);
        updateTargetOutcome(targetOutcomeDTO);
        Long targetOutcomeId = targetOutcomeDTO.getTargetOutcomeId();
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDetailsService.selectTargetOutcomeDetailsByOutcomeId(targetOutcomeId);
        return 0;
    }

    /**
     * 通过targetYear列表查找Target Outcome DTO
     *
     * @param targetYears 目标年度列表
     * @param indicatorId
     * @return
     */
    @Override
    public List<TargetOutcomeDetailsDTO> selectTargetOutcomeByTargetYears(List<Integer> targetYears, Long indicatorId) {
        return targetOutcomeMapper.selectTargetOutcomeByTargetYears(targetYears, indicatorId);
    }
}

