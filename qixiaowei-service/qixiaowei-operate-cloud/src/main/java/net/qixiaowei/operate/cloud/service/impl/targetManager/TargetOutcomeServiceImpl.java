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
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeDetailsService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;


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
    TargetSettingMapper targetSettingMapper;

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
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = getTargetOutcomeDetailsDTOList(targetOutcomeId);
        Integer targetYear = targetOutcomeDTO.getTargetYear();
        List<Long> indicatorIds = new ArrayList<>();
        setTargetSettingValue(targetOutcomeDetailsDTOList, targetYear, indicatorIds);
        // 判断当前年月,然后从targetOutcomeDetailsDTOList转向targetOutcomeDetailsDTOS
        int year = DateUtils.getYear();
        int month = DateUtils.getMonth();
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS = new ArrayList<>();
        List<BigDecimal> monthValue = new ArrayList<>();
        if (targetYear < year) {
            setAllMonth(targetOutcomeDetailsDTOList, monthValue, targetOutcomeDetailsDTOS);// 存放所有月份
        } else if (targetYear == year) {
            setSomeMonth(targetOutcomeDetailsDTOList, month, monthValue, targetOutcomeDetailsDTOS);//存放部分月份
        } else {
            setOtherValue(targetOutcomeDetailsDTOList, targetOutcomeDetailsDTOS);//不存放月份
        }
        targetOutcomeDTO.setTargetOutcomeDetailsDTOList(targetOutcomeDetailsDTOS);
        return targetOutcomeDTO;
    }

    /**
     * 赋值--目标制定那边的目标值和比例
     *
     * @param targetOutcomeDetailsDTOList
     * @param targetYear
     * @param indicatorIds
     */
    private void setTargetSettingValue(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList, Integer targetYear, List<Long> indicatorIds) {
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            indicatorIds.add(targetOutcomeDetailsDTO.getIndicatorId());
        }
        //根据指标和年份获取目标制定值
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingByIndicators(indicatorIds, targetYear);
        Map<Long, TargetSettingDTO> targetSettingMaps = new HashMap<>();
        for (TargetSettingDTO targetSettingDTO : targetSettingDTOList) {
            targetSettingMaps.put(targetSettingDTO.getIndicatorId(), targetSettingDTO);
        }
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            Long indicatorId = targetOutcomeDetailsDTO.getIndicatorId();
            //赋予目标值
            if (targetSettingMaps.containsKey(indicatorId)) {
                TargetSettingDTO targetSettingDTO = targetSettingMaps.get(indicatorId);
                if (targetOutcomeDetailsDTO.getIndicatorId().equals(targetSettingDTO.getIndicatorId())) {
                    BigDecimal targetValue = Optional.ofNullable(targetSettingDTO.getTargetValue()).orElse(BigDecimal.ZERO);
                    BigDecimal actualTotal = targetOutcomeDetailsDTO.getActualTotal();
                    targetOutcomeDetailsDTO.setTargetValue(targetValue);
                    BigDecimal targetCompletionRate;
                    if (targetValue.compareTo(BigDecimal.ZERO) != 0) {
                        targetCompletionRate = actualTotal.divide(targetValue, 2, RoundingMode.HALF_UP);
                    } else {
                        targetCompletionRate = BigDecimal.ZERO;
                    }
                    targetOutcomeDetailsDTO.setTargetCompletionRate(targetCompletionRate);
                }
            } else {//初始化
                targetOutcomeDetailsDTO.setTargetYear(targetYear);
                targetOutcomeDetailsDTO.setTargetValue(BigDecimal.ZERO);
                targetOutcomeDetailsDTO.setTargetCompletionRate(BigDecimal.ZERO);
            }
        }
    }

    /**
     * 给详情赋指标名称
     *
     * @param targetOutcomeId
     * @return
     */
    private List<TargetOutcomeDetailsDTO> getTargetOutcomeDetailsDTOList(Long targetOutcomeId) {
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
            throw new ServiceException("查询失败 请咨询管理员");
        }
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                if (targetOutcomeDetailsDTO.getIndicatorId().equals(indicatorDTO.getIndicatorId())) {
                    targetOutcomeDetailsDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                    break;
                }
            }
        }
        return targetOutcomeDetailsDTOList;
    }

    private void setOtherValue(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList, List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS) {
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            TargetOutcomeDetailsDTO targetOutcomeDetails = setTargetOutcomeValue(targetOutcomeDetailsDTO);
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
            TargetOutcomeDetailsDTO targetOutcomeDetails = setTargetOutcomeValue(targetOutcomeDetailsDTO);
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
            TargetOutcomeDetailsDTO targetOutcomeDetails = setTargetOutcomeValue(targetOutcomeDetailsDTO);
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
     * 目标结果赋值
     *
     * @param targetOutcomeDetailsDTO
     * @return
     */
    private static TargetOutcomeDetailsDTO setTargetOutcomeValue(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetailsDTO targetOutcomeDetails = new TargetOutcomeDetailsDTO();
        targetOutcomeDetails.setTargetValue(targetOutcomeDetailsDTO.getTargetValue());
        targetOutcomeDetails.setTargetCompletionRate(targetOutcomeDetailsDTO.getTargetCompletionRate());
        targetOutcomeDetails.setActualTotal(targetOutcomeDetailsDTO.getActualTotal());
        targetOutcomeDetails.setIndicatorName(targetOutcomeDetailsDTO.getIndicatorName());
        targetOutcomeDetails.setIndicatorId(targetOutcomeDetailsDTO.getIndicatorId());
        targetOutcomeDetails.setTargetOutcomeId(targetOutcomeDetailsDTO.getTargetOutcomeId());
        targetOutcomeDetails.setTargetOutcomeDetailsId(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
        targetOutcomeDetails.setCreateBy(targetOutcomeDetailsDTO.getCreateBy());
        targetOutcomeDetails.setCreateTime(targetOutcomeDetailsDTO.getCreateTime());
        return targetOutcomeDetails;
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
     * 新增目标结果表--订单，收入，回款
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetOutcomeDTO insertTargetOutcome(TargetOutcomeDTO targetOutcomeDTO, IndicatorDTO indicatorDTO) {
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
        targetOutcome.setCreateBy(SecurityUtils.getUserId());
        targetOutcome.setCreateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateBy(SecurityUtils.getUserId());
        targetOutcome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetOutcomeMapper.insertTargetOutcome(targetOutcome);
        TargetOutcomeDetailsDTO targetOutcomeDetailsDTO = new TargetOutcomeDetailsDTO();
        targetOutcomeDetailsDTO.setIndicatorId(indicatorDTO.getIndicatorId());
        targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcome.getTargetOutcomeId());
        targetOutcomeDetailsService.insertTargetOutcomeDetails(targetOutcomeDetailsDTO);
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
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            List<BigDecimal> monthValue = targetOutcomeDetailsDTO.getMonthValue();
            for (int i = 0; i < monthValue.size(); i++) {
                switch (i) {
                    case 0:
                        targetOutcomeDetailsDTO.setActualJanuary(monthValue.get(i));
                        break;
                    case 1:
                        targetOutcomeDetailsDTO.setActualFebruary(monthValue.get(i));
                        break;
                    case 2:
                        targetOutcomeDetailsDTO.setActualMarch(monthValue.get(i));
                        break;
                    case 3:
                        targetOutcomeDetailsDTO.setActualApril(monthValue.get(i));
                        break;
                    case 4:
                        targetOutcomeDetailsDTO.setActualMay(monthValue.get(i));
                        break;
                    case 5:
                        targetOutcomeDetailsDTO.setActualJune(monthValue.get(i));
                        break;
                    case 6:
                        targetOutcomeDetailsDTO.setActualJuly(monthValue.get(i));
                        break;
                    case 7:
                        targetOutcomeDetailsDTO.setActualAugust(monthValue.get(i));
                        break;
                    case 8:
                        targetOutcomeDetailsDTO.setActualSeptember(monthValue.get(i));
                        break;
                    case 9:
                        targetOutcomeDetailsDTO.setActualOctober(monthValue.get(i));
                        break;
                    case 10:
                        targetOutcomeDetailsDTO.setActualNovember(monthValue.get(i));
                        break;
                    case 11:
                        targetOutcomeDetailsDTO.setActualDecember(monthValue.get(i));
                        break;
                }
            }
        }
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
     * @param dataList
     * @param targetOutcomeId
     */
    @Override
    @Transactional
    public List<TargetOutcomeDetailsDTO> importTargetOutcome(List<Map<Integer, String>> dataList, Long targetOutcomeId) {
        if (StringUtils.isNull(targetOutcomeId)) {
            throw new ServiceException("关键经营结果ID不能为空!");
        }
        int size = dataList.get(1).size() + 1;
        dataList.remove(0);
        List<String> indicatorCodes = new ArrayList<>();
        for (Map<Integer, String> data : dataList) {
            indicatorCodes.add(data.get(1));
        }
        R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByCodeList(indicatorCodes, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorDTOS = indicatorR.getData();
        if (indicatorR.getCode() != 200 || StringUtils.isEmpty(indicatorDTOS)) {
            throw new ServiceException("获取指标名称失败!");
        }
        if (indicatorDTOS.size() != dataList.size()) {
            throw new ServiceException("指标有问题,请检查指标配置!");
        }
        List<BigDecimal> monthValue = new ArrayList<>();
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsAfter = new ArrayList<>();
        for (Map<Integer, String> data : dataList) {
            TargetOutcomeDetailsDTO targetOutcomeDetailsDTO = new TargetOutcomeDetailsDTO();
            String indicatorName = data.get(0);
            String indicatorCode = data.get(1);
            targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcomeId);
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                if (indicatorDTO.getIndicatorCode().equals(indicatorCode)) {
                    targetOutcomeDetailsDTO.setIndicatorId(indicatorDTO.getIndicatorId());
                    targetOutcomeDetailsDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                }
            }
            excelToDTO(size, data, targetOutcomeDetailsDTO);
            targetOutcomeDetailsAfter.add(targetOutcomeDetailsDTO);
        }
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsBefore = targetOutcomeDetailsService.selectTargetOutcomeDetailsByOutcomeId(targetOutcomeId);
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsBefore) {
            for (TargetOutcomeDetailsDTO outcomeDetailsDTO : targetOutcomeDetailsAfter) {
                if (targetOutcomeDetailsDTO.getIndicatorId().equals(outcomeDetailsDTO.getIndicatorId())) {
                    outcomeDetailsDTO.setTargetOutcomeDetailsId(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
                    break;
                }
            }
        }
        TargetOutcomeDTO targetOutcomeDTO = targetOutcomeMapper.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
        Integer targetYear = targetOutcomeDTO.getTargetYear();
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS = new ArrayList<>();
        int year = DateUtils.getYear();
        int month = DateUtils.getMonth();
        if (targetYear < year) {
            setAllMonth(targetOutcomeDetailsAfter, monthValue, targetOutcomeDetailsDTOS);// 存放所有月份
        } else if (targetYear == year) {
            setSomeMonth(targetOutcomeDetailsAfter, month, monthValue, targetOutcomeDetailsDTOS);//存放部分月份
        } else {
            setOtherValue(targetOutcomeDetailsAfter, targetOutcomeDetailsDTOS);//不存放月份
        }
        return targetOutcomeDetailsDTOS;
    }

    /**
     * excel → DTO
     *
     * @param size
     * @param data
     * @param targetOutcomeDetailsDTO
     */
    private static void excelToDTO(int size, Map<Integer, String> data, TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        BigDecimal sum = BigDecimal.ZERO;
        if (size > 3) {
            targetOutcomeDetailsDTO.setActualJanuary(new BigDecimal(data.get(2)));
            sum = sum.add(new BigDecimal(data.get(2)));
        }
        if (size > 4) {
            targetOutcomeDetailsDTO.setActualFebruary(new BigDecimal(data.get(3)));
            sum = sum.add(new BigDecimal(data.get(3)));
        }
        if (size > 5) {
            targetOutcomeDetailsDTO.setActualMarch(new BigDecimal(data.get(4)));
            sum = sum.add(new BigDecimal(data.get(4)));
        }
        if (size > 6) {
            targetOutcomeDetailsDTO.setActualApril(new BigDecimal(data.get(5)));
            sum = sum.add(new BigDecimal(data.get(5)));
        }
        if (size > 7) {
            targetOutcomeDetailsDTO.setActualMay(new BigDecimal(data.get(6)));
            sum = sum.add(new BigDecimal(data.get(6)));
        }
        if (size > 8) {
            targetOutcomeDetailsDTO.setActualJune(new BigDecimal(data.get(7)));
            sum = sum.add(new BigDecimal(data.get(7)));
        }
        if (size > 9) {
            targetOutcomeDetailsDTO.setActualJuly(new BigDecimal(data.get(8)));
            sum = sum.add(new BigDecimal(data.get(8)));
        }
        if (size > 10) {
            targetOutcomeDetailsDTO.setActualAugust(new BigDecimal(data.get(9)));
            sum = sum.add(new BigDecimal(data.get(9)));
        }
        if (size > 11) {
            targetOutcomeDetailsDTO.setActualSeptember(new BigDecimal(data.get(10)));
            sum = sum.add(new BigDecimal(data.get(10)));
        }
        if (size > 12) {
            targetOutcomeDetailsDTO.setActualOctober(new BigDecimal(data.get(11)));
            sum = sum.add(new BigDecimal(data.get(11)));
        }
        if (size > 13) {
            targetOutcomeDetailsDTO.setActualNovember(new BigDecimal(data.get(12)));
            sum = sum.add(new BigDecimal(data.get(12)));
        }
        if (size > 14) {
            targetOutcomeDetailsDTO.setActualDecember(new BigDecimal(data.get(13)));
            sum = sum.add(new BigDecimal(data.get(13)));
        }
        targetOutcomeDetailsDTO.setActualTotal(sum);
    }

    /**
     * 导出Excel
     *
     * @param targetOutcomeDTO
     * @return
     */
    @Override
    public List<TargetOutcomeExcel> exportTargetOutcome(TargetOutcomeDTO targetOutcomeDTO) {
        Long targetOutcomeId = targetOutcomeDTO.getTargetOutcomeId();
        if (StringUtils.isNull(targetOutcomeId)) {
            throw new ServiceException("关键经营结果ID不能为空");
        }
        TargetOutcomeDTO targetOutcome = targetOutcomeMapper.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = getTargetOutcomeDetailsDTOList(targetOutcomeId);
        Integer targetYear = targetOutcome.getTargetYear();
        List<Long> indicatorIds = new ArrayList<>();
        setTargetSettingValue(targetOutcomeDetailsDTOList, targetYear, indicatorIds);
        List<TargetOutcomeExcel> targetOutcomeExcels = new ArrayList<>();
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            TargetOutcomeExcel targetOutcomeExcel = new TargetOutcomeExcel();
            BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeExcel);
            targetOutcomeExcels.add(targetOutcomeExcel);
        }
        return targetOutcomeExcels;
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

