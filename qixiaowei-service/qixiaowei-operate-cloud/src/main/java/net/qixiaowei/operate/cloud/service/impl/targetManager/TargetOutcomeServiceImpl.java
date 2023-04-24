package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcome;
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.api.vo.strategyIntent.StrategyIntentOperateMapVO;
import net.qixiaowei.operate.cloud.api.vo.strategyIntent.StrategyIntentOperateVO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.*;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeDimensionService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeDetailsService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeService;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


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

    @Autowired
    private RemoteUserService userService;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private TargetDecomposeMapper targetDecomposeMapper;

    @Autowired
    private TargetDecomposeDetailsMapper targetDecomposeDetailsMapper;

    @Autowired
    private ITargetDecomposeDimensionService targetDecomposeDimensionService;

    @Autowired
    private DecomposeDetailCyclesMapper decomposeDetailCyclesMapper;

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
        if (targetYear < year) {
            setAllMonth(targetOutcomeDetailsDTOList, targetOutcomeDetailsDTOS);// 存放所有月份
        } else if (targetYear == year) {
            setSomeMonth(targetOutcomeDetailsDTOList, month, targetOutcomeDetailsDTOS);//存放部分月份
        } else {
            setOtherValue(targetOutcomeDetailsDTOList, targetOutcomeDetailsDTOS);//不存放月份
        }
        targetOutcomeDTO.setTargetOutcomeDetailsDTOList(targetOutcomeDetailsDTOS);
        return targetOutcomeDTO;
    }

    /**
     * 赋值--目标制定那边的目标值和比例
     *
     * @param targetOutcomeDetailsDTOList 详情列表
     * @param targetYear                  目标年度
     * @param indicatorIds                指标ID集合
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
                        targetCompletionRate = actualTotal.multiply(new BigDecimal(100)).divide(targetValue, 2, RoundingMode.HALF_UP);
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
     * @param targetOutcomeId 结果ID
     * @return List
     */
    private List<TargetOutcomeDetailsDTO> getTargetOutcomeDetailsDTOList(Long targetOutcomeId) {
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDetailsService.selectTargetOutcomeDetailsByOutcomeId(targetOutcomeId);
        List<Long> indicatorIds = new ArrayList<>();
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            Long indicatorId = targetOutcomeDetailsDTO.getIndicatorId();
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
                    targetOutcomeDetailsDTO.setIndicatorCode(indicatorDTO.getIndicatorCode());
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
     * @param targetOutcomeDetailsDTOList 列表
     * @param month                       月
     * @param targetOutcomeDetailsDTOS    详情列表
     */
    private void setSomeMonth(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList, int month, List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS) {
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            List<BigDecimal> monthValue = new ArrayList<>();
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
     * @param targetOutcomeDetailsDTOList 列表
     * @param targetOutcomeDetailsDTOS    结果详情列表
     */
    private void setAllMonth(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList, List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS) {
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
            List<BigDecimal> monthValue = new ArrayList<>();
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
     * @param targetOutcomeDetailsDTO 结果dto
     * @return 结果
     */
    private static TargetOutcomeDetailsDTO setTargetOutcomeValue(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetailsDTO targetOutcomeDetails = new TargetOutcomeDetailsDTO();
        targetOutcomeDetails.setTargetValue(targetOutcomeDetailsDTO.getTargetValue());
        targetOutcomeDetails.setTargetCompletionRate(targetOutcomeDetailsDTO.getTargetCompletionRate());
        targetOutcomeDetails.setActualTotal(targetOutcomeDetailsDTO.getActualTotal());
        targetOutcomeDetails.setIndicatorName(targetOutcomeDetailsDTO.getIndicatorName());
        targetOutcomeDetails.setIndicatorCode(targetOutcomeDetailsDTO.getIndicatorCode());
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
    @DataScope(businessAlias = "tao")
    @Override
    public List<TargetOutcomeDTO> selectTargetOutcomeList(TargetOutcomeDTO targetOutcomeDTO) {
        String createByName = targetOutcomeDTO.getCreateByName();
        List<TargetOutcomeDTO> targetOutcomeDTOS;
        if (StringUtils.isNull(createByName)) {
            TargetOutcome targetOutcome = new TargetOutcome();
            BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
            Map<String, Object> params = targetOutcome.getParams();
            targetOutcome.setParams(params);
            targetOutcomeDTOS = targetOutcomeMapper.selectTargetOutcomeList(targetOutcome);
        } else {
            List<EmployeeDTO> employeeDTOS = getEmployee(createByName);
            Set<Long> createBySet = new HashSet<>();
            if (StringUtils.isEmpty(employeeDTOS)) {
                targetOutcomeDTOS = new ArrayList<>();
            } else {
                for (EmployeeDTO employeeDTO : employeeDTOS) {
                    Long userId = employeeDTO.getUserId();
                    if (StringUtils.isNotNull(userId)) {
                        createBySet.add(userId);
                    }
                }
                targetOutcomeDTO.setCreateBys(createBySet);
                TargetOutcome targetOutcome = new TargetOutcome();
                BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
                Map<String, Object> params = targetOutcome.getParams();
                if (StringUtils.isNotEmpty(targetOutcomeDTO.getCreateBys())) {
                    params.put("createBys", targetOutcomeDTO.getCreateBys());
                }
                targetOutcome.setParams(params);
                targetOutcomeDTOS = targetOutcomeMapper.selectTargetOutcomeByCreateBys(targetOutcome);
            }
        }
        this.handleResult(targetOutcomeDTOS);
        return targetOutcomeDTOS;
    }

    @Override
    public void handleResult(List<TargetOutcomeDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(TargetOutcomeDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 获取用户信息
     */
    private List<EmployeeDTO> getEmployee(String createByName) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeeName(createByName);
        R<List<EmployeeDTO>> listR = employeeService.selectUserByEmployeeName(employeeDTO, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程获取用户信息失败");
        }
        return employeeDTOS;
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
        Integer targetYear = targetOutcomeDTO.getTargetYear();
        if (targetYear > DateUtils.getYear() || (targetYear == DateUtils.getYear() && DateUtils.getMonth() == 1)) {
            return 1;
        }
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDTO.getTargetOutcomeDetailsDTOList();
        setMonthValue(targetOutcomeDetailsDTOList);
        TargetOutcome targetOutcome = new TargetOutcome();
        BeanUtils.copyProperties(targetOutcomeDTO, targetOutcome);
        targetOutcome.setUpdateTime(DateUtils.getNowDate());
        targetOutcome.setUpdateBy(SecurityUtils.getUserId());
        //  更新 targetOutcomeDetail
        if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOList)) {
            for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
                targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcome.getTargetOutcomeId());
            }
            targetOutcomeDetailsService.updateTargetOutcomeDetailss(targetOutcomeDetailsDTOList);
        }
        return targetOutcomeMapper.updateTargetOutcome(targetOutcome);
    }

    /**
     * 未目标结果详情表月份赋值
     *
     * @param targetOutcomeDetailsDTOList 目标结果详情表
     */
    private static void setMonthValue(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList) {
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
    @Override
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

    @Override
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
     * @param dataList        数据列表
     * @param targetOutcomeId 结果ID
     */
    @Override
    @Transactional
    public Map<Object, Object> importTargetOutcome(List<Map<Integer, String>> dataList, Long targetOutcomeId) {
        if (StringUtils.isNull(targetOutcomeId)) {
            throw new ServiceException("关键经营结果ID不能为空!");
        }
        TargetOutcomeDTO targetOutcomeDTO = targetOutcomeMapper.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
        Integer targetYear = targetOutcomeDTO.getTargetYear();
        if (DateUtils.getYear() < targetYear) {
            return ExcelUtils.parseExcelResult(null, null, true, null);
        }
        int month = DateUtils.getMonth();
        int size = dataList.get(0).size() + 1;
        List<String> indicatorNames = new ArrayList<>();
        dataList.remove(2);
        dataList.remove(1);
        dataList.remove(0);
        for (Map<Integer, String> data : dataList) {
            indicatorNames.add(data.get(0));
        }
        List<IndicatorDTO> indicatorDTOS = getIndicatorDTOS(dataList, indicatorNames);
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsAfter = new ArrayList<>();
        for (Map<Integer, String> data : dataList) {
            TargetOutcomeDetailsDTO targetOutcomeDetailsDTO = new TargetOutcomeDetailsDTO();
            String indicatorName = data.get(0);
            targetOutcomeDetailsDTO.setTargetOutcomeId(targetOutcomeId);
            for (IndicatorDTO indicatorDTO : indicatorDTOS) {
                if (indicatorDTO.getIndicatorName().equals(indicatorName)) {
                    targetOutcomeDetailsDTO.setIndicatorId(indicatorDTO.getIndicatorId());
                    targetOutcomeDetailsDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                }
            }
            excelToDTO(size, data, targetOutcomeDetailsDTO);
            targetOutcomeDetailsAfter.add(targetOutcomeDetailsDTO);
        }
        List<Long> indicatorIds = indicatorDTOS.stream().map(IndicatorDTO::getIndicatorId).collect(Collectors.toList());
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingByIndicators(indicatorIds, targetYear);
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsBefore = targetOutcomeDetailsService.selectTargetOutcomeDetailsByOutcomeId(targetOutcomeId);
        if (targetOutcomeDetailsBefore.size() != targetOutcomeDetailsAfter.size()) {
            throw new ServiceException("导入失败 模板错误");
        }
        Set<Long> indicatorBefore = targetOutcomeDetailsBefore.stream().map(TargetOutcomeDetailsDTO::getIndicatorId).collect(Collectors.toSet());
        Set<Long> indicatorAfter = targetOutcomeDetailsAfter.stream().map(TargetOutcomeDetailsDTO::getIndicatorId).collect(Collectors.toSet());
        if (!indicatorBefore.containsAll(indicatorAfter)) {
            throw new ServiceException("导入失败 模板错误");
        }
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsBefore) {
            for (TargetOutcomeDetailsDTO outcomeDetailsDTO : targetOutcomeDetailsAfter) {
                Long indicatorId = outcomeDetailsDTO.getIndicatorId();
                if (targetOutcomeDetailsDTO.getIndicatorId().equals(indicatorId)) {
                    outcomeDetailsDTO.setTargetOutcomeDetailsId(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
                    List<TargetSettingDTO> targetSettingDTOList = targetSettingDTOS.stream().filter(t -> t.getIndicatorId().equals(indicatorId)).collect(Collectors.toList());
                    TargetSettingDTO targetSettingDTO = new TargetSettingDTO();
                    if (StringUtils.isNotEmpty(targetSettingDTOList)) {
                        targetSettingDTO = targetSettingDTOList.get(0);
                    }
                    BigDecimal targetValue = Optional.ofNullable(targetSettingDTO.getTargetValue()).orElse(BigDecimal.ZERO);
                    BigDecimal actualTotal = outcomeDetailsDTO.getActualTotal();
                    outcomeDetailsDTO.setTargetValue(targetValue);
                    //实际值合计/目标值
                    outcomeDetailsDTO.setTargetCompletionRate(targetValue.compareTo(BigDecimal.ZERO) == 0 ? BigDecimal.ZERO : actualTotal.multiply(new BigDecimal(100)).divide(targetValue, 2, RoundingMode.HALF_UP));
                    break;
                }
            }
        }
        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS = new ArrayList<>();
        int year = DateUtils.getYear();
        if (targetYear < year) {
            setAllMonth(targetOutcomeDetailsAfter, targetOutcomeDetailsDTOS);// 存放所有月份
        } else if (targetYear == year) {
            setSomeMonth(targetOutcomeDetailsAfter, month, targetOutcomeDetailsDTOS);// 存放部分月份
        } else {
            setOtherValue(targetOutcomeDetailsAfter, targetOutcomeDetailsDTOS);// 不存放月份
        }
        return ExcelUtils.parseExcelResult(targetOutcomeDetailsDTOS, null, true, null);
    }

    /**
     * 获取指标
     *
     * @param dataList       数据集合
     * @param indicatorNames 指标名称
     * @return 结果
     */
    private List<IndicatorDTO> getIndicatorDTOS(List<Map<Integer, String>> dataList, List<String> indicatorNames) {
        R<List<IndicatorDTO>> indicatorR = indicatorService.selectIndicatorByNames(indicatorNames, SecurityConstants.INNER);
        List<IndicatorDTO> indicatorDTOS = indicatorR.getData();
        if (indicatorR.getCode() != 200 || StringUtils.isEmpty(indicatorDTOS)) {
            throw new ServiceException("获取指标名称失败!");
        }
        if (indicatorDTOS.size() != dataList.size()) {
            throw new ServiceException("指标数据异常 请检查指标配置!");
        }
        return indicatorDTOS;
    }

    /**
     * excel → DTO
     *
     * @param size                    大小
     * @param data                    数据
     * @param targetOutcomeDetailsDTO 详情dto
     */
    private static void excelToDTO(int size, Map<Integer, String> data, TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        BigDecimal sum = BigDecimal.ZERO;
        if (size > 2) {
            targetOutcomeDetailsDTO.setActualJanuary(new BigDecimal(Optional.ofNullable(data.get(1)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(1)).orElse("0")));
        }
        if (size > 3) {
            targetOutcomeDetailsDTO.setActualFebruary(new BigDecimal(Optional.ofNullable(data.get(2)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(2)).orElse("0")));
        }
        if (size > 4) {
            targetOutcomeDetailsDTO.setActualMarch(new BigDecimal(Optional.ofNullable(data.get(3)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(3)).orElse("0")));
        }
        if (size > 5) {
            targetOutcomeDetailsDTO.setActualApril(new BigDecimal(Optional.ofNullable(data.get(4)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(4)).orElse("0")));
        }
        if (size > 6) {
            targetOutcomeDetailsDTO.setActualMay(new BigDecimal(Optional.ofNullable(data.get(5)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(5)).orElse("0")));
        }
        if (size > 7) {
            targetOutcomeDetailsDTO.setActualJune(new BigDecimal(Optional.ofNullable(data.get(6)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(6)).orElse("0")));
        }
        if (size > 8) {
            targetOutcomeDetailsDTO.setActualJuly(new BigDecimal(Optional.ofNullable(data.get(7)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(7)).orElse("0")));
        }
        if (size > 9) {
            targetOutcomeDetailsDTO.setActualAugust(new BigDecimal(Optional.ofNullable(data.get(8)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(8)).orElse("0")));
        }
        if (size > 10) {
            targetOutcomeDetailsDTO.setActualSeptember(new BigDecimal(Optional.ofNullable(data.get(9)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(9)).orElse("0")));
        }
        if (size > 11) {
            targetOutcomeDetailsDTO.setActualOctober(new BigDecimal(Optional.ofNullable(data.get(10)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(10)).orElse("0")));
        }
        if (size > 12) {
            targetOutcomeDetailsDTO.setActualNovember(new BigDecimal(Optional.ofNullable(data.get(11)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(11)).orElse("0")));
        }
        if (size > 13) {
            targetOutcomeDetailsDTO.setActualDecember(new BigDecimal(Optional.ofNullable(data.get(12)).orElse("0")));
            sum = sum.add(new BigDecimal(Optional.ofNullable(data.get(12)).orElse("0")));
        }
        targetOutcomeDetailsDTO.setActualTotal(sum);
    }

    /**
     * 导出Excel
     *
     * @param targetOutcomeDTO 结果DTO
     * @return 结果
     */
    @Override
    public List<TargetOutcomeExcel> exportTargetOutcome(TargetOutcomeDTO targetOutcomeDTO) {
        Long targetOutcomeId = targetOutcomeDTO.getTargetOutcomeId();
        if (StringUtils.isNull(targetOutcomeId)) {
            throw new ServiceException("关键经营结果ID不能为空");
        }
        TargetOutcomeDTO targetOutcome = targetOutcomeMapper.selectTargetOutcomeByTargetOutcomeId(targetOutcomeId);
        targetOutcomeDTO.setTargetYear(targetOutcome.getTargetYear());
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
     * @param targetYear 目标年度
     * @return 结果
     */
    @Override
    public TargetOutcomeDTO selectTargetOutcomeByTargetYear(Integer targetYear) {
        return targetOutcomeMapper.selectTargetOutcomeByTargetYear(targetYear);
    }

    /**
     * 通过targetYear列表查找Target Outcome DTO
     *
     * @param targetYears 目标年度列表
     * @param indicatorId 指标id
     * @return 结果
     */
    @Override
    public List<TargetOutcomeDetailsDTO> selectTargetOutcomeByTargetYears(List<Integer> targetYears, Long indicatorId) {
        return targetOutcomeMapper.selectTargetOutcomeByTargetYears(targetYears, indicatorId);
    }

    /**
     * 战略云获取指标实际值
     *
     * @param strategyIntentOperateVOS vos
     * @return 结果
     */
    @Override
    public List<StrategyIntentOperateVO> getResultIndicator(List<StrategyIntentOperateVO> strategyIntentOperateVOS) {
        StrategyIntentOperateVO strategyIntentOperateVO = new StrategyIntentOperateVO();
        if (StringUtils.isNotEmpty(strategyIntentOperateVOS)) {
            List<Long> indicatorIds = strategyIntentOperateVOS.get(0).getIndicatorIds();
            List<String> targetYears = strategyIntentOperateVOS.get(0).getTargetYears();
            if (StringUtils.isEmpty(indicatorIds) || StringUtils.isEmpty(targetYears)) {
                throw new ServiceException("年度或指标为空！");
            }
            strategyIntentOperateVO.setIndicatorIds(indicatorIds);
            strategyIntentOperateVO.setTargetYears(targetYears);
            List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeMapper.getResultIndicator(strategyIntentOperateVO);
            if (StringUtils.isEmpty(targetYears)) {
                return strategyIntentOperateVOS;
            } else {
                List<Long> indicatorIdList = targetOutcomeDetailsDTOList.stream().map(TargetOutcomeDetailsDTO::getIndicatorId).collect(Collectors.toList());
                R<List<IndicatorDTO>> listR = indicatorService.selectIndicatorByIds(indicatorIdList, SecurityConstants.INNER);
                List<IndicatorDTO> indicatorDTOList = listR.getData();
                if (StringUtils.isNotEmpty(indicatorDTOList)) {
                    for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList) {
                        for (IndicatorDTO indicatorDTO : indicatorDTOList) {
                            if (targetOutcomeDetailsDTO.getIndicatorId().equals(indicatorDTO.getIndicatorId())) {
                                targetOutcomeDetailsDTO.setIndicatorName(indicatorDTO.getIndicatorName());
                            }
                        }
                    }
                }
                Map<Long, List<TargetOutcomeDetailsDTO>> indicatorIdDataMap = targetOutcomeDetailsDTOList.parallelStream().collect(Collectors.groupingBy(TargetOutcomeDetailsDTO::getIndicatorId));
                if (StringUtils.isNotEmpty(indicatorIdDataMap)) {
                    for (StrategyIntentOperateVO intentOperateVO : strategyIntentOperateVOS) {
                        List<StrategyIntentOperateMapVO> strategyIntentOperateMapDTOS = intentOperateVO.getStrategyIntentOperateMapDTOS();
                        //数据库
                        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList1 = indicatorIdDataMap.get(intentOperateVO.getIndicatorId());
                        if (StringUtils.isNotEmpty(strategyIntentOperateMapDTOS)) {
                            for (StrategyIntentOperateMapVO strategyIntentOperateMapDTO : strategyIntentOperateMapDTOS) {
                                Map<Integer, BigDecimal> yearValues1 = strategyIntentOperateMapDTO.getYearValues();
                                //经营年度
                                Integer operateYear = null;
                                for (Integer key : yearValues1.keySet()) {
                                    operateYear = key;

                                }
                                if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOList1)) {
                                    for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOList1) {
                                        Integer targetYear = targetOutcomeDetailsDTO.getTargetYear();
                                        if ((targetYear.equals(operateYear))) {
                                            yearValues1.put(targetYear, targetOutcomeDetailsDTO.getActualTotal());
                                        }
                                    }
                                }
                            }
                        }
                    }

                }

            }
        }
        return strategyIntentOperateVOS;
    }

    /**
     * 同步数据
     *
     * @param targetYear 目标年度
     * @return 结果
     */
    @Override
    public List<TargetDecomposeDTO> migrationData(Integer targetYear) {
        int year = DateUtils.getYear();
        int month = DateUtils.getMonth();
        TargetDecompose targetDecompose = new TargetDecompose();
        targetDecompose.setTimeDimension(4);
        targetDecompose.setTargetYear(targetYear);
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectRollPageList(targetDecompose);
        if (StringUtils.isEmpty(targetDecomposeDTOS) || targetYear > year) {
            return targetDecomposeDTOS;
        }
        List<Long> targetDecomposeIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getTargetDecomposeId).collect(Collectors.toList());
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeIds(targetDecomposeIds);
        if (StringUtils.isEmpty(targetDecomposeDetailsDTOList)) {
            throw new ServiceException("数据异常 请联系管理员");
        }
        List<Long> targetDecomposeDetailsIds = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
        List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsIds(targetDecomposeDetailsIds);
        for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDTOS) {
            Map<Integer, BigDecimal> valueMap = new HashMap<>();
            Long targetDecomposeId = targetDecomposeDTO.getTargetDecomposeId();
            List<Long> targetDecomposeDetailsIdList = targetDecomposeDetailsDTOList.stream().filter(t -> targetDecomposeId.equals(t.getTargetDecomposeId())).map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = decomposeDetailCyclesDTOList.stream().filter(d -> targetDecomposeDetailsIdList.contains(d.getTargetDecomposeDetailsId())).collect(Collectors.toList());
            for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                Integer cycleNumber = decomposeDetailCyclesDTO.getCycleNumber();
                BigDecimal cycleTarget = Optional.ofNullable(decomposeDetailCyclesDTO.getCycleActual()).orElse(BigDecimal.ZERO);
                if (valueMap.containsKey(cycleNumber)) {
                    valueMap.put(cycleNumber, valueMap.get(cycleNumber).add(cycleTarget));
                } else {
                    valueMap.put(cycleNumber, cycleTarget);
                }
            }
            List<BigDecimal> monthValue = new ArrayList<>();
            Map<Integer, BigDecimal> treeMap;
            if (targetYear < year) {
                treeMap = new TreeMap<>(valueMap);
                monthValue = new ArrayList<>(treeMap.values());
            } else {
                int i = 1;
                while (i < month) {
                    monthValue.add(valueMap.get(i));
                    i++;
                }
            }
            targetDecomposeDTO.setMonthValue(monthValue);
        }
        List<Long> indicatorIds = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getIndicatorId).collect(Collectors.toList());
        //远程获取指标名称
        R<List<IndicatorDTO>> listR1 = indicatorService.selectIndicatorByIds(indicatorIds, SecurityConstants.INNER);
        List<IndicatorDTO> data = listR1.getData();
        if (StringUtils.isNotEmpty(data)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOS) {
                for (IndicatorDTO datum : data) {
                    if (decomposeDTO.getIndicatorId().equals(datum.getIndicatorId())) {
                        decomposeDTO.setIndicatorName(datum.getIndicatorName());
                    }
                }
            }
        }
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDTOS = targetDecomposeDimensionService.selectTargetDecomposeDimensionList(new TargetDecomposeDimensionDTO());
        targetDecomposeDTOS.sort(Comparator.comparing(TargetDecomposeDTO::getIndicatorId).thenComparing(TargetDecomposeDTO::getTargetDecomposeDimensionId));
        List<TargetDecomposeDTO> targetDecomposeDTOList = new ArrayList<>();
        Map<Long, List<TargetDecomposeDTO>> groupTargetDecomposeDTOS = targetDecomposeDTOS.stream().collect(Collectors.groupingBy(TargetDecomposeDTO::getIndicatorId));
        for (Long indicatorId : groupTargetDecomposeDTOS.keySet()) {
            List<TargetDecomposeDTO> decomposeDTOS = groupTargetDecomposeDTOS.get(indicatorId);
            for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDTOS) {
                for (TargetDecomposeDTO decomposeDTO : decomposeDTOS) {
                    if (targetDecomposeDimensionDTO.getDecompositionDimensionName().equals(decomposeDTO.getDecompositionDimension())) {
                        targetDecomposeDTOList.add(decomposeDTO);
                        break;
                    }
                }
            }
        }
        return targetDecomposeDTOList;
    }

}

