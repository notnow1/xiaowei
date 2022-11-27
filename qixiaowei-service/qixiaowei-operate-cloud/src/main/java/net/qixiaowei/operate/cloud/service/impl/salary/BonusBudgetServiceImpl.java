package net.qixiaowei.operate.cloud.service.impl.salary;

import java.math.BigDecimal;
import java.util.*;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcome;
import net.qixiaowei.operate.cloud.api.dto.salary.BonusBudgetParametersDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeMapper;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.BonusBudget;
import net.qixiaowei.operate.cloud.api.dto.salary.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.mapper.salary.BonusBudgetMapper;
import net.qixiaowei.operate.cloud.service.salary.IBonusBudgetService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * BonusBudgetService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-11-26
 */
@Service
public class BonusBudgetServiceImpl implements IBonusBudgetService {
    @Autowired
    private BonusBudgetMapper bonusBudgetMapper;
    @Autowired
    private RemoteIndicatorService remoteIndicatorService;
    @Autowired
    private SalaryPayMapper salaryPayMapper;

    @Autowired
    private TargetOutcomeMapper targetOutcomeMapper;

    /**
     * 查询奖金预算表
     *
     * @param bonusBudgetId 奖金预算表主键
     * @return 奖金预算表
     */
    @Override
    public BonusBudgetDTO selectBonusBudgetByBonusBudgetId(Long bonusBudgetId) {
        return bonusBudgetMapper.selectBonusBudgetByBonusBudgetId(bonusBudgetId);
    }

    /**
     * 查询奖金预算表列表
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 奖金预算表
     */
    @Override
    public List<BonusBudgetDTO> selectBonusBudgetList(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        return bonusBudgetMapper.selectBonusBudgetList(bonusBudget);
    }

    /**
     * 新增奖金预算表
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    public BonusBudgetDTO insertBonusBudget(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        bonusBudget.setCreateBy(SecurityUtils.getUserId());
        bonusBudget.setCreateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        bonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        bonusBudgetMapper.insertBonusBudget(bonusBudget);
        bonusBudgetDTO.setBonusBudgetId(bonusBudget.getBonusBudgetId());
        return bonusBudgetDTO;
    }

    /**
     * 修改奖金预算表
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    public int updateBonusBudget(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        return bonusBudgetMapper.updateBonusBudget(bonusBudget);
    }

    /**
     * 逻辑批量删除奖金预算表
     *
     * @param bonusBudgetIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteBonusBudgetByBonusBudgetIds(List<Long> bonusBudgetIds) {
        return bonusBudgetMapper.logicDeleteBonusBudgetByBonusBudgetIds(bonusBudgetIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除奖金预算表信息
     *
     * @param bonusBudgetId 奖金预算表主键
     * @return 结果
     */
    @Override
    public int deleteBonusBudgetByBonusBudgetId(Long bonusBudgetId) {
        return bonusBudgetMapper.deleteBonusBudgetByBonusBudgetId(bonusBudgetId);
    }

    /**
     * 新增奖金预算预制数据
     *
     * @param budgetYear
     * @return
     */
    @Override
    public BonusBudgetDTO addBonusBudgetTamount(int budgetYear) {
        //总奖金包预算参数集合
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = new ArrayList<>();
        //封装总奖金包预算参数指标数据
        packBounParamIndicatorIds(budgetYear, bonusBudgetParametersDTOS);
        return null;
    }

    /**
     * 封装总奖金包预算参数指标数据
     *
     * @param budgetYear
     * @param bonusBudgetParametersDTOS
     */
    private void packBounParamIndicatorIds(int budgetYear, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIsDriverList(SecurityConstants.INNER);
        List<IndicatorDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {
            for (IndicatorDTO datum : data) {
                BonusBudgetParametersDTO bonusBudgetParametersDTO = new BonusBudgetParametersDTO();
                //指标id
                bonusBudgetParametersDTO.setIndicatorId(datum.getIndicatorId());
                bonusBudgetParametersDTOS.add(bonusBudgetParametersDTO);
            }
            //指标id集合
            List<Long> collect = bonusBudgetParametersDTOS.stream().map(BonusBudgetParametersDTO::getIndicatorId).filter(Objects::nonNull).collect(Collectors.toList());
            TargetOutcome targetOutcome = new TargetOutcome();
            //目标年度
            targetOutcome.setTargetYear(budgetYear);
            //指标id集合
            if (StringUtils.isNotEmpty(collect)) {
                targetOutcome.setIndicatorIds(collect);
            }

            //查询总奖金包预算参数的指标目标 挑战 保底 和奖金驱动因素实际数：从关键经营结果取值，当前月份倒推12个月的合计
            List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS = targetOutcomeMapper.selectDrivingFactor(targetOutcome);
            if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOS)) {

                //封装奖金驱动因素实际数
                packMonth(targetOutcomeDetailsDTOS, DateUtils.getMonth());

            }

        }
    }

    /**
     * 封装奖金驱动因素实际数
     *
     * @param targetOutcomeDetailsDTOS
     * @param month
     */
    private BigDecimal packMonth(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS, int month) {
        BigDecimal bonusProportionDrivingFactor = new BigDecimal("0");
        if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOS)) {
            //月份数据map集合
            List<Map<Integer, BigDecimal>> listMap = new ArrayList<>();
            for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOS) {
                //所有月份对应的实际值
                Map<Integer, BigDecimal> map = new HashMap<>();
                //一月实际值
                map.put(1, targetOutcomeDetailsDTO.getActualJanuary());
                //二月实际值
                map.put(2, targetOutcomeDetailsDTO.getActualFebruary());
                //三月实际值
                map.put(3, targetOutcomeDetailsDTO.getActualMarch());
                //四月实际值
                map.put(4, targetOutcomeDetailsDTO.getActualApril());
                //五月实际值
                map.put(5, targetOutcomeDetailsDTO.getActualMay());
                //六月实际值
                map.put(6, targetOutcomeDetailsDTO.getActualJune());
                //七月实际值
                map.put(7, targetOutcomeDetailsDTO.getActualJuly());
                //八月实际值
                map.put(8, targetOutcomeDetailsDTO.getActualAugust());
                //九月实际值
                map.put(9, targetOutcomeDetailsDTO.getActualSeptember());
                //十月实际值
                map.put(10, targetOutcomeDetailsDTO.getActualOctober());
                //十一月实际值
                map.put(11, targetOutcomeDetailsDTO.getActualNovember());
                //十二月实际值
                map.put(12, targetOutcomeDetailsDTO.getActualDecember());
                listMap.add(map);
            }
            if (listMap.size() == 1) {
                for (Map<Integer, BigDecimal> integerBigDecimalMap : listMap) {
                    for (Integer key : integerBigDecimalMap.keySet()) {
                        bonusProportionDrivingFactor = bonusProportionDrivingFactor.add(integerBigDecimalMap.get(key));
                    }

                }
            } else {
                for (int i = 0; i < listMap.size(); i++) {
                    for (Integer key : listMap.get(i).keySet()) {
                        if (i == 0) {
                            if (key < month) {
                                bonusProportionDrivingFactor = bonusProportionDrivingFactor.add(listMap.get(i).get(key));
                            }
                        } else if (i == 1) {
                            if (key > month) {
                                bonusProportionDrivingFactor = bonusProportionDrivingFactor.add(listMap.get(i).get(key));
                            }
                        }
                    }
                }
            }

        }

        return bonusProportionDrivingFactor;
    }

    /**
     * 逻辑删除奖金预算表信息
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    public int logicDeleteBonusBudgetByBonusBudgetId(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        bonusBudget.setBonusBudgetId(bonusBudgetDTO.getBonusBudgetId());
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        return bonusBudgetMapper.logicDeleteBonusBudgetByBonusBudgetId(bonusBudget);
    }

    /**
     * 物理删除奖金预算表信息
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */

    @Override
    public int deleteBonusBudgetByBonusBudgetId(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        return bonusBudgetMapper.deleteBonusBudgetByBonusBudgetId(bonusBudget.getBonusBudgetId());
    }

    /**
     * 物理批量删除奖金预算表
     *
     * @param bonusBudgetDtos 需要删除的奖金预算表主键
     * @return 结果
     */

    @Override
    public int deleteBonusBudgetByBonusBudgetIds(List<BonusBudgetDTO> bonusBudgetDtos) {
        List<Long> stringList = new ArrayList();
        for (BonusBudgetDTO bonusBudgetDTO : bonusBudgetDtos) {
            stringList.add(bonusBudgetDTO.getBonusBudgetId());
        }
        return bonusBudgetMapper.deleteBonusBudgetByBonusBudgetIds(stringList);
    }

    /**
     * 批量新增奖金预算表信息
     *
     * @param bonusBudgetDtos 奖金预算表对象
     */

    public int insertBonusBudgets(List<BonusBudgetDTO> bonusBudgetDtos) {
        List<BonusBudget> bonusBudgetList = new ArrayList();

        for (BonusBudgetDTO bonusBudgetDTO : bonusBudgetDtos) {
            BonusBudget bonusBudget = new BonusBudget();
            BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
            bonusBudget.setCreateBy(SecurityUtils.getUserId());
            bonusBudget.setCreateTime(DateUtils.getNowDate());
            bonusBudget.setUpdateTime(DateUtils.getNowDate());
            bonusBudget.setUpdateBy(SecurityUtils.getUserId());
            bonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            bonusBudgetList.add(bonusBudget);
        }
        return bonusBudgetMapper.batchBonusBudget(bonusBudgetList);
    }

    /**
     * 批量修改奖金预算表信息
     *
     * @param bonusBudgetDtos 奖金预算表对象
     */

    public int updateBonusBudgets(List<BonusBudgetDTO> bonusBudgetDtos) {
        List<BonusBudget> bonusBudgetList = new ArrayList();

        for (BonusBudgetDTO bonusBudgetDTO : bonusBudgetDtos) {
            BonusBudget bonusBudget = new BonusBudget();
            BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
            bonusBudget.setCreateBy(SecurityUtils.getUserId());
            bonusBudget.setCreateTime(DateUtils.getNowDate());
            bonusBudget.setUpdateTime(DateUtils.getNowDate());
            bonusBudget.setUpdateBy(SecurityUtils.getUserId());
            bonusBudgetList.add(bonusBudget);
        }
        return bonusBudgetMapper.updateBonusBudgets(bonusBudgetList);
    }
}

