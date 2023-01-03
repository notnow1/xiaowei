package net.qixiaowei.operate.cloud.service.impl.bonus;

import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusBudget;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusBudgetParameters;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcome;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetLaddertersDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetParametersDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.FutureBonusBudgetLaddertersDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.*;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetDetailsMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusBudgetParametersMapper;
import net.qixiaowei.operate.cloud.mapper.salary.EmolumentPlanMapper;
import net.qixiaowei.operate.cloud.mapper.salary.OfficialRankEmolumentMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeMapper;
import net.qixiaowei.operate.cloud.service.bonus.IBonusBudgetService;
import net.qixiaowei.operate.cloud.service.impl.salary.EmolumentPlanServiceImpl;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * BonusBudgetService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-11-26
 */
@Service
@Slf4j
public class BonusBudgetServiceImpl implements IBonusBudgetService {
    @Autowired
    private BonusBudgetMapper bonusBudgetMapper;
    @Autowired
    private BonusBudgetParametersMapper bonusBudgetParametersMapper;
    @Autowired
    private RemoteIndicatorService remoteIndicatorService;
    @Autowired
    private SalaryPayMapper salaryPayMapper;

    @Autowired
    private TargetOutcomeMapper targetOutcomeMapper;
    @Autowired
    private EmolumentPlanMapper emolumentPlanMapper;
    @Autowired
    private EmployeeBudgetDetailsMapper employeeBudgetDetailsMapper;

    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteOfficialRankSystemService remoteOfficialRankSystemService;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private RemoteUserService remoteUserService;
    @Autowired
    private OfficialRankEmolumentMapper officialRankEmolumentMapper;


    /**
     * 查询奖金预算表
     *
     * @param bonusBudgetId 奖金预算表主键
     * @return 奖金预算表
     */
    @Override
    public BonusBudgetDTO selectBonusBudgetByBonusBudgetId(Long bonusBudgetId) {
        BonusBudgetDTO bonusBudgetDTO = bonusBudgetMapper.selectBonusBudgetByBonusBudgetId(bonusBudgetId);
        if (StringUtils.isNull(bonusBudgetDTO)) {
            throw new ServiceException("数据不存在 请联系管理员！");
        }
        //根据总奖金id查询奖金预算参数表
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudgetParametersMapper.selectBonusBudgetParametersByBonusBudgetId(bonusBudgetDTO.getBonusBudgetId());
        //详情封装总奖金包预算阶梯数据
        List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS = this.queryBonusBudgetLadderters(bonusBudgetDTO, bonusBudgetParametersDTOS);
        //奖金增长率
        this.packQueryBudgetParameters(bonusBudgetParametersDTOS);
        //封装总奖金包预算生成
        this.packPaymentBonusBudget(bonusBudgetDTO.getBudgetYear(), bonusBudgetDTO);
        BigDecimal basicWageBonusBudget = bonusBudgetDTO.getBasicWageBonusBudget();
        log.info("再次打印总工资预算==============================" + basicWageBonusBudget);
        //未来三年奖金趋势集合
        List<FutureBonusBudgetLaddertersDTO> futureBonusBudgetLaddertersDTOS = new ArrayList<>();
        //查询详情未来三年奖金趋势集合
        this.packQueryFutureBonusTrend(bonusBudgetDTO, bonusBudgetDTO.getBudgetYear(), futureBonusBudgetLaddertersDTOS, bonusBudgetParametersDTOS);

        bonusBudgetDTO.setBonusBudgetParametersDTOS(bonusBudgetParametersDTOS);
        bonusBudgetDTO.setBonusBudgetLaddertersDTOS(bonusBudgetLaddertersDTOS);
        bonusBudgetDTO.setFutureBonusBudgetLaddertersDTOS(futureBonusBudgetLaddertersDTOS);
        log.info("再次打印所有数据=======================" + JSONUtil.toJsonStr(bonusBudgetDTO));
        return bonusBudgetDTO;
    }

    /**
     * 【奖金增长率（%）】：数值字段，保留两位小数，不可编辑。
     * 公式=权重×业绩增长率×奖金折让系数。
     *
     * @param bonusBudgetParametersDTOS
     */
    private void packQueryBudgetParameters(List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            List<Long> collect = bonusBudgetParametersDTOS.stream().map(BonusBudgetParametersDTO::getIndicatorId).distinct().collect(Collectors.toList());

            //远程调用指标赋值
            R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(collect, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                    for (IndicatorDTO datum : data) {
                        if (bonusBudgetParametersDTO.getIndicatorId() == datum.getIndicatorId()) {
                            bonusBudgetParametersDTO.setIndicatorName(datum.getIndicatorName());
                        }
                    }
                }
            }
            for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                //奖金增长率后一年
                BigDecimal bonusGrowthRateAfterOne = new BigDecimal("0");
                //奖金增长率后二年
                BigDecimal bonusGrowthRateAfterTwo = new BigDecimal("0");
                //权重
                BigDecimal bonusWeight = bonusBudgetParametersDTO.getBonusWeight();
                //预算年后一年业绩增长率
                BigDecimal performanceAfterOne = bonusBudgetParametersDTO.getPerformanceAfterOne();
                //预算年后二年业绩增长率
                BigDecimal performanceAfterTwo = bonusBudgetParametersDTO.getPerformanceAfterTwo();

                //预算年后一年奖金折让系数
                BigDecimal bonusAllowanceAfterOne = bonusBudgetParametersDTO.getBonusAllowanceAfterOne();
                //预算年后二年奖金折让系数
                BigDecimal bonusAllowanceAfterTwo = bonusBudgetParametersDTO.getBonusAllowanceAfterTwo();
                //奖金增长率后一年
                if (null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0 &&
                        null != performanceAfterOne && performanceAfterOne.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusAllowanceAfterOne && bonusAllowanceAfterOne.compareTo(new BigDecimal("0")) != 0) {
                    bonusGrowthRateAfterOne = bonusWeight.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).multiply(performanceAfterOne.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).multiply(bonusAllowanceAfterOne).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    bonusBudgetParametersDTO.setBonusGrowthRateAfterOne(bonusGrowthRateAfterOne);
                }
                //奖金增长率后二年
                if (null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusAllowanceAfterTwo && bonusAllowanceAfterTwo.compareTo(new BigDecimal("0")) != 0 &&
                        null != performanceAfterTwo && performanceAfterTwo.compareTo(new BigDecimal("0")) != 0) {
                    bonusGrowthRateAfterTwo = bonusWeight.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).multiply(performanceAfterTwo.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).multiply(bonusAllowanceAfterTwo).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    bonusBudgetParametersDTO.setBonusGrowthRateAfterTwo(bonusGrowthRateAfterTwo);
                }

            }
        }

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
        List<BonusBudgetDTO> bonusBudgetDTOS = bonusBudgetMapper.selectBonusBudgetList(bonusBudget);
        //封装总奖金包预算列表涨薪包数据
        this.packPaymentBonusBudgetList(bonusBudgetDTOS);
        // 远程调用 赋值创建人名称

        Set<Long> collect = bonusBudgetDTOS.stream().map(BonusBudgetDTO::getCreateBy).distinct().collect(Collectors.toSet());
        if (StringUtils.isNotEmpty(collect)) {
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(collect, SecurityConstants.INNER);
            List<UserDTO> data = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (BonusBudgetDTO budgetDTO : bonusBudgetDTOS) {
                    for (UserDTO datum : data) {
                        if (budgetDTO.getCreateBy() == datum.getUserId()) {
                            budgetDTO.setCreateName(datum.getEmployeeName());
                        }
                    }
                }
            }
        }
        List<BonusBudgetDTO> bonusBudgetDTOList = this.packQueryBonusBudget(bonusBudgetDTO, bonusBudgetDTOS);

        return bonusBudgetDTOList;
    }

    /**
     * 封装模糊查询
     *
     * @param bonusBudgetDTO
     * @param bonusBudgetDTOS
     * @return
     */
    private List<BonusBudgetDTO> packQueryBonusBudget(BonusBudgetDTO bonusBudgetDTO, List<BonusBudgetDTO> bonusBudgetDTOS) {
        List<BonusBudgetDTO> bonusBudgetDTOList = new ArrayList<>();
        //创建人名称
        String createName = bonusBudgetDTO.getCreateName();
        //总奖金包预算
        BigDecimal amountBonusBudget = bonusBudgetDTO.getAmountBonusBudget();
        //涨薪包预算
        BigDecimal raiseSalaryBonusBudget = bonusBudgetDTO.getRaiseSalaryBonusBudget();

        if (StringUtils.isNotNull(bonusBudgetDTO)) {
            Pattern pattern = null;
            Pattern pattern1 = null;
            Pattern pattern2 = null;
            if (StringUtils.isNotBlank(createName)) {
                //创建人名称
                pattern = Pattern.compile(createName);
            }

            if (StringUtils.isNotNull(amountBonusBudget)) {
                //总奖金包预算
                pattern1 = Pattern.compile(String.valueOf(amountBonusBudget));
            }
            if (StringUtils.isNotNull(raiseSalaryBonusBudget)) {
                //涨薪包预算
                pattern2 = Pattern.compile(String.valueOf(raiseSalaryBonusBudget));
            }
            for (BonusBudgetDTO budgetDTO : bonusBudgetDTOS) {
                //创建人名称
                Matcher createByName1 = null;
                //总奖金包预算
                Matcher amountBonusBudget1 = null;
                //涨薪包预算
                Matcher raiseSalaryBonusBudget1 = null;
                if (StringUtils.isNotBlank(createName)) {
                    String createName2 = budgetDTO.getCreateName();
                    if (StringUtils.isNotBlank(createName2)) {
                        //创建人名称
                        createByName1 = pattern.matcher(createName2);
                    } else {
                        return new ArrayList<>();
                    }
                }
                if (StringUtils.isNotNull(amountBonusBudget)) {
                    //总奖金包预算
                    amountBonusBudget1 = pattern1.matcher(String.valueOf(budgetDTO.getAmountBonusBudget()));
                }
                if (StringUtils.isNotNull(raiseSalaryBonusBudget)) {
                    //涨薪包预算
                    raiseSalaryBonusBudget1 = pattern2.matcher(String.valueOf(budgetDTO.getRaiseSalaryBonusBudget()));
                }
                if ((StringUtils.isNotNull(createName) && StringUtils.isNotBlank(budgetDTO.getCreateName())) && StringUtils.isNotNull(amountBonusBudget) && StringUtils.isNotNull(raiseSalaryBonusBudget)) {
                    if (createByName1.find() || amountBonusBudget1.find() || raiseSalaryBonusBudget1.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                        bonusBudgetDTOList.add(budgetDTO);
                    }
                }
                if (StringUtils.isNotBlank(createName) && StringUtils.isNotBlank(budgetDTO.getCreateName())) {
                    if (createByName1.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                        bonusBudgetDTOList.add(budgetDTO);
                    }
                }
                if (StringUtils.isNotNull(amountBonusBudget)) {
                    if (amountBonusBudget1.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                        bonusBudgetDTOList.add(budgetDTO);
                    }
                }
                if (StringUtils.isNotNull(raiseSalaryBonusBudget)) {
                    if (raiseSalaryBonusBudget1.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                        bonusBudgetDTOList.add(budgetDTO);
                    }
                }

            }
            if (StringUtils.isNotBlank(createName) || StringUtils.isNotNull(amountBonusBudget) || StringUtils.isNotNull(raiseSalaryBonusBudget)) {
                return bonusBudgetDTOList;
            }
        }
        return StringUtils.isNotEmpty(bonusBudgetDTOList) ? bonusBudgetDTOList : bonusBudgetDTOS;
    }

    /**
     * 新增奖金预算表
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    @Transactional
    public BonusBudgetDTO insertBonusBudget(BonusBudgetDTO bonusBudgetDTO) {
        BonusBudgetDTO bonusBudgetDTO1 = bonusBudgetMapper.selectBonusBudgetByBudgetYear(bonusBudgetDTO.getBudgetYear());
        if (StringUtils.isNotNull(bonusBudgetDTO1)) {
            throw new ServiceException(bonusBudgetDTO.getBudgetYear() + "年 数据已存在无需重复添加！");
        }
        //插入总奖金包预算参数集合
        List<BonusBudgetParameters> bonusBudgetParametersList = new ArrayList<>();
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        //总奖金包预算参数集合
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudgetDTO.getBonusBudgetParametersDTOS();
        //未来三年奖金趋势
        List<FutureBonusBudgetLaddertersDTO> futureBonusBudgetLaddertersDTOS = bonusBudgetDTO.getFutureBonusBudgetLaddertersDTOS();
        if (StringUtils.isNotEmpty(futureBonusBudgetLaddertersDTOS)) {
            bonusBudget.setBonusBeforeOne(futureBonusBudgetLaddertersDTOS.get(0).getAmountBonusBudget());
        }
        bonusBudget.setCreateBy(SecurityUtils.getUserId());
        bonusBudget.setCreateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        bonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            bonusBudgetMapper.insertBonusBudget(bonusBudget);
        } catch (Exception e) {
            throw new ServiceException("新增总奖金预算失败");
        }
        //插入总奖金预算参数表
        insertParametersDTOS(bonusBudget, bonusBudgetParametersList, bonusBudgetParametersDTOS);
        bonusBudgetDTO.setBonusBudgetId(bonusBudget.getBonusBudgetId());
        return bonusBudgetDTO;
    }

    /**
     * 插入总奖金预算参数表
     *
     * @param bonusBudget
     * @param bonusBudgetParametersList
     * @param bonusBudgetParametersDTOS
     */
    private void insertParametersDTOS(BonusBudget bonusBudget, List<BonusBudgetParameters> bonusBudgetParametersList, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                BonusBudgetParameters bonusBudgetParameters = new BonusBudgetParameters();
                BeanUtils.copyProperties(bonusBudgetParametersDTO, bonusBudgetParameters);
                //奖金预算ID
                bonusBudgetParameters.setBonusBudgetId(bonusBudget.getBonusBudgetId());
                bonusBudgetParameters.setCreateBy(SecurityUtils.getUserId());
                bonusBudgetParameters.setCreateTime(DateUtils.getNowDate());
                bonusBudgetParameters.setUpdateTime(DateUtils.getNowDate());
                bonusBudgetParameters.setUpdateBy(SecurityUtils.getUserId());
                bonusBudgetParameters.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                bonusBudgetParametersList.add(bonusBudgetParameters);
            }
        }
        if (StringUtils.isNotEmpty(bonusBudgetParametersList)) {
            try {
                bonusBudgetParametersMapper.batchBonusBudgetParameters(bonusBudgetParametersList);
            } catch (Exception e) {
                throw new ServiceException("新增总奖金包预算参数失败");
            }
        }
    }

    /**
     * 修改奖金预算表
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateBonusBudget(BonusBudgetDTO bonusBudgetDTO) {
        int i = 0;
        //修改总奖金包预算参数集合
        List<BonusBudgetParameters> bonusBudgetParametersList = new ArrayList<>();
        BonusBudget bonusBudget = new BonusBudget();
        BeanUtils.copyProperties(bonusBudgetDTO, bonusBudget);
        //总奖金包预算参数集合
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudgetDTO.getBonusBudgetParametersDTOS();
        //未来三年奖金趋势
        List<FutureBonusBudgetLaddertersDTO> futureBonusBudgetLaddertersDTOS = bonusBudgetDTO.getFutureBonusBudgetLaddertersDTOS();
        if (StringUtils.isNotEmpty(futureBonusBudgetLaddertersDTOS)) {
            bonusBudget.setBonusBeforeOne(futureBonusBudgetLaddertersDTOS.get(0).getAmountBonusBudget());
        }
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = bonusBudgetMapper.updateBonusBudget(bonusBudget);
        } catch (Exception e) {
            throw new ServiceException("新增总奖金预算失败");
        }
        //修改总奖金预算参数表
        updateParametersDTOS(bonusBudgetParametersList, bonusBudgetParametersDTOS);
        return i;
    }

    /**
     * 修改总奖金预算参数表
     *
     * @param bonusBudgetParametersList
     * @param bonusBudgetParametersDTOS
     */
    private void updateParametersDTOS(List<BonusBudgetParameters> bonusBudgetParametersList, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                BonusBudgetParameters bonusBudgetParameters = new BonusBudgetParameters();
                BeanUtils.copyProperties(bonusBudgetParametersDTO, bonusBudgetParameters);
                bonusBudgetParameters.setUpdateTime(DateUtils.getNowDate());
                bonusBudgetParameters.setUpdateBy(SecurityUtils.getUserId());
                bonusBudgetParametersList.add(bonusBudgetParameters);
            }
        }
        if (StringUtils.isNotEmpty(bonusBudgetParametersList)) {
            try {
                bonusBudgetParametersMapper.updateBonusBudgetParameterss(bonusBudgetParametersList);
            } catch (Exception e) {
                throw new ServiceException("修改总奖金包预算参数失败");
            }
        }
    }

    /**
     * 逻辑批量删除奖金预算表
     *
     * @param bonusBudgetIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteBonusBudgetByBonusBudgetIds(List<Long> bonusBudgetIds) {
        int i = 0;

        List<BonusBudgetDTO> bonusBudgetDTOS = bonusBudgetMapper.selectBonusBudgetByBonusBudgetIds(bonusBudgetIds);
        if (StringUtils.isEmpty(bonusBudgetDTOS)) {
            throw new ServiceException("数据不存在 请联系管理员!");
        }
        try {
            i = bonusBudgetMapper.logicDeleteBonusBudgetByBonusBudgetIds(bonusBudgetIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除总奖金预算失败");
        }
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudgetParametersMapper.selectBonusBudgetParametersByBonusBudgetIds(bonusBudgetIds);
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            List<Long> collect = bonusBudgetParametersDTOS.stream().map(BonusBudgetParametersDTO::getBonusBudgetId).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    bonusBudgetParametersMapper.logicDeleteBonusBudgetParametersByBonusBudgetParametersIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除总奖金预算参数失败");
                }
            }

        }
        return i;
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
        BonusBudgetDTO bonusBudgetDTO = new BonusBudgetDTO();
        //1总奖金包预算参数集合
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = new ArrayList<>();
        //2总奖金包预算阶梯集合
        List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS = new ArrayList<>();
        //4未来三年奖金趋势集合
        List<FutureBonusBudgetLaddertersDTO> futureBonusBudgetLaddertersDTOS = new ArrayList<>();
        //封装总奖金包预算参数指标数据
        this.packBounParamIndicatorIds(budgetYear, bonusBudgetParametersDTOS);

        //3 封装总奖金包预算生成
        this.packPaymentBonusBudget(budgetYear, bonusBudgetDTO);
        //4未来三年奖金趋势 预制数据
        this.packAddFutureBonusTrend(bonusBudgetDTO, budgetYear, futureBonusBudgetLaddertersDTOS);
        bonusBudgetDTO.setBonusBudgetParametersDTOS(bonusBudgetParametersDTOS);
        bonusBudgetDTO.setFutureBonusBudgetLaddertersDTOS(futureBonusBudgetLaddertersDTOS);
        bonusBudgetDTO.setBonusBudgetLaddertersDTOS(bonusBudgetLaddertersDTOS);
        bonusBudgetDTO.setBudgetYear(budgetYear);
        return bonusBudgetDTO;
    }

    /**
     * @param budgetYear
     * @param futureBonusBudgetLaddertersDTOS
     */
    private void packAddFutureBonusTrend(BonusBudgetDTO bonusBudgetDTO, int budgetYear, List<FutureBonusBudgetLaddertersDTO> futureBonusBudgetLaddertersDTOS) {
        //返回上年总工资包实际数：从月度工资数据管理取值（总奖金值）
        BigDecimal amountBonusBudget = salaryPayMapper.selectSalaryBounsAmount(budgetYear);

        for (int i = 0; i < 4; i++) {
            FutureBonusBudgetLaddertersDTO futureBonusBudgetLaddertersDTO = new FutureBonusBudgetLaddertersDTO();
            if (i == 0) {
                futureBonusBudgetLaddertersDTO.setBudgetYear(budgetYear - 1);
                futureBonusBudgetLaddertersDTO.setAmountBonusBudget(amountBonusBudget.divide(new BigDecimal("10000"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                futureBonusBudgetLaddertersDTO.setBonusCompositeRate(new BigDecimal("0"));
            } else if (i == 1) {
                futureBonusBudgetLaddertersDTO.setBudgetYear(budgetYear);
                futureBonusBudgetLaddertersDTO.setAmountBonusBudget(bonusBudgetDTO.getAmountBonusBudget());
                futureBonusBudgetLaddertersDTO.setBonusCompositeRate(new BigDecimal("0"));
            } else if (i == 2) {
                futureBonusBudgetLaddertersDTO.setBudgetYear(budgetYear + 1);
                futureBonusBudgetLaddertersDTO.setAmountBonusBudget(new BigDecimal("0"));
                futureBonusBudgetLaddertersDTO.setBonusCompositeRate(new BigDecimal("0"));
            } else {
                futureBonusBudgetLaddertersDTO.setBudgetYear(budgetYear + 2);
                futureBonusBudgetLaddertersDTO.setAmountBonusBudget(new BigDecimal("0"));
                futureBonusBudgetLaddertersDTO.setBonusCompositeRate(new BigDecimal("0"));
            }
            futureBonusBudgetLaddertersDTOS.add(futureBonusBudgetLaddertersDTO);
        }
    }

    /**
     * 详情未来三年奖金趋势
     *
     * @param budgetYear
     * @param futureBonusBudgetLaddertersDTOS
     * @param bonusBudgetParametersDTOS
     */
    private void packQueryFutureBonusTrend(BonusBudgetDTO bonusBudgetDTO, int budgetYear, List<FutureBonusBudgetLaddertersDTO> futureBonusBudgetLaddertersDTOS, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        BigDecimal bonusBeforeOne = bonusBudgetDTO.getBonusBeforeOne();
        //预算年度奖金包预算
        BigDecimal amountBonusBudget1 = bonusBudgetDTO.getAmountBonusBudget();
        for (int i = 0; i < 5; i++) {
            FutureBonusBudgetLaddertersDTO futureBonusBudgetLaddertersDTO = new FutureBonusBudgetLaddertersDTO();
            if (i == 0) {
                futureBonusBudgetLaddertersDTO.setBudgetYear(budgetYear - 1);
                futureBonusBudgetLaddertersDTO.setAmountBonusBudget(bonusBeforeOne);
            } else if (i == 1) {
                BigDecimal multiply = new BigDecimal("0");
                futureBonusBudgetLaddertersDTO.setBudgetYear(budgetYear);
                // 奖金增长率  预算年度：公式=（预算年度奖金包规划值÷上年奖金包实际值-1）×100%
                futureBonusBudgetLaddertersDTO.setAmountBonusBudget(amountBonusBudget1);
                if (null != amountBonusBudget1 && amountBonusBudget1.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusBeforeOne && bonusBeforeOne.compareTo(new BigDecimal("0")) != 0) {
                    multiply = amountBonusBudget1.divide(bonusBeforeOne, 10, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                    ;
                }
                futureBonusBudgetLaddertersDTO.setBonusCompositeRate(multiply);
            } else if (i == 2) {
                BigDecimal add = new BigDecimal("0");
                futureBonusBudgetLaddertersDTO.setBudgetYear(budgetYear);
                // 奖金增长率  预算年度+1、预算年度+2：各奖金驱动因素的奖金增长率合计（若权重、业绩增长率、奖金折让系数取不到数，则视为0）

                add = bonusBudgetParametersDTOS.stream().map(BonusBudgetParametersDTO::getBonusGrowthRateAfterOne).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                futureBonusBudgetLaddertersDTO.setBonusCompositeRate(add);
            } else if (i == 3) {
                BigDecimal add = new BigDecimal("0");
                futureBonusBudgetLaddertersDTO.setBudgetYear(budgetYear);
                // 奖金增长率  预算年度+1、预算年度+2：各奖金驱动因素的奖金增长率合计（若权重、业绩增长率、奖金折让系数取不到数，则视为0）
                add = bonusBudgetParametersDTOS.stream().map(BonusBudgetParametersDTO::getBonusGrowthRateAfterTwo).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                futureBonusBudgetLaddertersDTO.setBonusCompositeRate(add);
            }
            futureBonusBudgetLaddertersDTOS.add(futureBonusBudgetLaddertersDTO);
        }
        //计算总奖金包规划值 预算年度+1、预算年度+2：公式=上年总奖金包规划值×（1+本年奖金综合增长率）。
        if (StringUtils.isNotEmpty(futureBonusBudgetLaddertersDTOS)) {
            for (int i = 0; i < futureBonusBudgetLaddertersDTOS.size(); i++) {
                if (i > 1) {
                    BigDecimal multiply = new BigDecimal("0");
                    //上年总奖金包规划值
                    BigDecimal amountBonusBudget2 = futureBonusBudgetLaddertersDTOS.get(i - 1).getAmountBonusBudget();
                    //奖金综合增长率
                    BigDecimal bonusCompositeRate = futureBonusBudgetLaddertersDTOS.get(i).getBonusCompositeRate();
                    if (null != amountBonusBudget2 && amountBonusBudget2.compareTo(new BigDecimal("0")) != 0 &&
                            null != bonusCompositeRate && bonusCompositeRate.compareTo(new BigDecimal("0")) != 0) {
                        multiply = amountBonusBudget2.multiply(new BigDecimal("1").add(bonusCompositeRate.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP))).setScale(2, BigDecimal.ROUND_HALF_UP);
                        ;
                        futureBonusBudgetLaddertersDTOS.get(i).setAmountBonusBudget(multiply);
                    }
                }
            }
        }
    }

    /**
     * 封装总奖金包预算生成
     *
     * @param budgetYear
     * @param bonusBudgetDTO
     */
    public void packPaymentBonusBudget(int budgetYear, BonusBudgetDTO bonusBudgetDTO) {
        EmolumentPlanDTO emolumentPlanDTO = emolumentPlanMapper.selectEmolumentPlanByPlanYear(budgetYear);
        if (StringUtils.isNotNull(emolumentPlanDTO)) {
            //总薪酬包预算
            BigDecimal emolumentPackage = new BigDecimal("0");
            //总工资包预算
            BigDecimal basicWageBonusBudget = new BigDecimal("0");
            //弹性薪酬包预算 公式=总薪酬包预算-总工资包预算
            BigDecimal elasticityBonusBudget = new BigDecimal("0");
            //涨薪包预算 公式=弹性薪酬包预算-总奖金包预算。
            BigDecimal raiseSalaryBonusBudget = new BigDecimal("0");
            //总奖金包预算
            BigDecimal amountBonusBudget = bonusBudgetDTO.getAmountBonusBudget();

            //查询薪酬规划详情计算方法
            EmolumentPlanServiceImpl.queryCalculate(emolumentPlanDTO);
            //薪酬规划总薪酬包
            emolumentPackage = emolumentPlanDTO.getEmolumentPackage();
            //总薪酬包预算
            bonusBudgetDTO.setPaymentBonusBudget(emolumentPackage);

            //总工资包预算 公式=上年总工资包实际数+本年增人/减人工资包合计
            //上年总工资包实际数
            BigDecimal salarySum = salaryPayMapper.selectSalaryPayAmoutNum(budgetYear);
            //从增人/减人工资包取值（增人/减人工资包列，合计行）
            EmployeeBudgetDTO employeeBudgetDTO = new EmployeeBudgetDTO();
            employeeBudgetDTO.setBudgetYear(budgetYear);
            BigDecimal increaseAndDecreasePaySum = this.salaryPackageList(employeeBudgetDTO);
            System.out.println("增人/减人工资包值====================================" + increaseAndDecreasePaySum);
            if (null != increaseAndDecreasePaySum) {
                basicWageBonusBudget = increaseAndDecreasePaySum;
            }

            if (null != salarySum) {
                //总工资包预算
                basicWageBonusBudget = salarySum.add(basicWageBonusBudget);
            }

            //总工资包预算
            bonusBudgetDTO.setBasicWageBonusBudget(basicWageBonusBudget.divide(new BigDecimal("10000"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
            System.out.println("总工资包预算====================================" + basicWageBonusBudget.divide(new BigDecimal("10000"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
            if (null != emolumentPackage && emolumentPackage.compareTo(new BigDecimal("0")) != 0) {
                elasticityBonusBudget = emolumentPackage.subtract(basicWageBonusBudget.divide(new BigDecimal("10000"), 10, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
                //弹性薪酬包  公式=总薪酬包预算-总工资包预算
                bonusBudgetDTO.setElasticityBonusBudget(elasticityBonusBudget);
            }

            if (elasticityBonusBudget.compareTo(new BigDecimal("0")) != 0 &&
                    null != amountBonusBudget && amountBonusBudget.compareTo(new BigDecimal("0")) != 0) {
                raiseSalaryBonusBudget = elasticityBonusBudget.subtract(amountBonusBudget);
                //涨薪包预算 公式=弹性薪酬包预算-总奖金包预算。
                bonusBudgetDTO.setRaiseSalaryBonusBudget(raiseSalaryBonusBudget);
            }
        }

    }

    /**
     * 封装总奖金包预算列表涨薪包数据
     *
     * @param bonusBudgetDTOS
     */
    public void packPaymentBonusBudgetList(List<BonusBudgetDTO> bonusBudgetDTOS) {

        if (StringUtils.isNotEmpty(bonusBudgetDTOS)) {
            List<Integer> collect = bonusBudgetDTOS.stream().map(BonusBudgetDTO::getBudgetYear).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                List<EmolumentPlanDTO> emolumentPlanDTOList = emolumentPlanMapper.selectEmolumentPlanByPlanYears(collect);
                for (int i = 0; i < bonusBudgetDTOS.size(); i++) {
                    //总薪酬包预算
                    BigDecimal emolumentPackage = new BigDecimal("0");
                    //总工资包预算
                    BigDecimal basicWageBonusBudget = new BigDecimal("0");
                    //弹性薪酬包预算 公式=总薪酬包预算-总工资包预算
                    BigDecimal elasticityBonusBudget = new BigDecimal("0");
                    //涨薪包预算 公式=弹性薪酬包预算-总奖金包预算。
                    BigDecimal raiseSalaryBonusBudget = new BigDecimal("0");
                    //总奖金包预算
                    BigDecimal amountBonusBudget = bonusBudgetDTOS.get(i).getAmountBonusBudget();

                    if (StringUtils.isNotEmpty(emolumentPlanDTOList)) {
                        //查询薪酬规划详情计算方法
                        EmolumentPlanServiceImpl.queryCalculate(emolumentPlanDTOList.get(i));
                        //薪酬规划总薪酬包
                        emolumentPackage = emolumentPlanDTOList.get(i).getEmolumentPackage();
                    }
                    //总薪酬包预算
                    bonusBudgetDTOS.get(i).setPaymentBonusBudget(emolumentPackage);

                    //总工资包预算 公式=上年总工资包实际数+本年增人/减人工资包合计
                    //上年总工资包实际数
                    BigDecimal salarySum = salaryPayMapper.selectSalaryPayAmoutNum(bonusBudgetDTOS.get(i).getBudgetYear());
                    //从增人/减人工资包取值（增人/减人工资包列，合计行）
                    EmployeeBudgetDTO employeeBudgetDTO = new EmployeeBudgetDTO();
                    employeeBudgetDTO.setBudgetYear(bonusBudgetDTOS.get(i).getBudgetYear());
                    BigDecimal increaseAndDecreasePaySum = this.salaryPackageList(employeeBudgetDTO);
                    if (increaseAndDecreasePaySum != null) {
                        basicWageBonusBudget = increaseAndDecreasePaySum;
                    }

                    if (null != salarySum) {
                        //总工资包预算
                        basicWageBonusBudget = salarySum.add(basicWageBonusBudget);
                    }
                    //总工资包预算
                    bonusBudgetDTOS.get(i).setBasicWageBonusBudget(basicWageBonusBudget.divide(new BigDecimal("10000"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));

                    if (null != emolumentPackage && emolumentPackage.compareTo(new BigDecimal("0")) != 0 &&
                            basicWageBonusBudget.compareTo(new BigDecimal("0")) != 0) {
                        elasticityBonusBudget = emolumentPackage.subtract(basicWageBonusBudget.divide(new BigDecimal("10000"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP));
                        //弹性薪酬包  公式=总薪酬包预算-总工资包预算
                        bonusBudgetDTOS.get(i).setElasticityBonusBudget(elasticityBonusBudget);
                    }

                    if (elasticityBonusBudget.compareTo(new BigDecimal("0")) != 0 &&
                            null != amountBonusBudget && amountBonusBudget.compareTo(new BigDecimal("0")) != 0) {
                        raiseSalaryBonusBudget = elasticityBonusBudget.subtract(amountBonusBudget.setScale(2, BigDecimal.ROUND_HALF_UP));
                        //涨薪包预算 公式=弹性薪酬包预算-总奖金包预算。
                        bonusBudgetDTOS.get(i).setRaiseSalaryBonusBudget(raiseSalaryBonusBudget);
                    }
                }
            }

        }

    }

    /**
     * 封装总奖金包预算阶梯数据
     * 1:先计算总奖金包预算总奖金包预算1 2 值
     * 2：计算表头目标值 挑战值等
     * 3：计算奖金驱动因素/比值（%） 公式=总奖金包预算参考值1÷目标值
     * 4：倒推其他行数据
     *
     * @param bonusBudgetParametersDTOS
     */
    @Override
    public BonusBudgetDTO selectBonusBudgetLadderters(List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        BonusBudgetDTO bonusBudgetDTO = new BonusBudgetDTO();
        //总奖金包预算阶梯集合
        List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS = new ArrayList<>();
        //封装总部门奖金预算 必须计算的数据方法
        this.packBounLadderNum(bonusBudgetDTO, bonusBudgetLaddertersDTOS, bonusBudgetParametersDTOS);
        return bonusBudgetDTO;
    }

    /**
     * 详情封装总奖金包预算阶梯数据
     * 1:先计算总奖金包预算总奖金包预算1 2 值
     * 2：计算表头目标值 挑战值等
     * 3：计算奖金驱动因素/比值（%） 公式=总奖金包预算参考值1÷目标值
     * 4：倒推其他行数据
     *
     * @param bonusBudgetDTO
     * @param bonusBudgetParametersDTOS
     * @return
     */
    public List<BonusBudgetLaddertersDTO> queryBonusBudgetLadderters(BonusBudgetDTO bonusBudgetDTO, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {

        //总奖金包预算阶梯集合
        List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS = new ArrayList<>();
        //封装总部门奖金预算 必须计算的数据方法
        this.packBounLadderNum(bonusBudgetDTO, bonusBudgetLaddertersDTOS, bonusBudgetParametersDTOS);
        return bonusBudgetLaddertersDTOS;
    }

    /**
     * 返回最大年份
     *
     * @return
     */
    @Override
    public int queryBonusBudgetYear() {
        return bonusBudgetMapper.queryBonusBudgetYear();
    }

    /**
     * 封装总部门奖金预算 必须计算的数据方法
     *
     * @param bonusBudgetDTO
     * @param bonusBudgetLaddertersDTOS
     * @param bonusBudgetParametersDTOS
     */
    private void packBounLadderNum(BonusBudgetDTO bonusBudgetDTO, List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        //总奖金包预算总奖金包预算1
        BigDecimal amountBonusBudgetReferenceValueOne = new BigDecimal("0");
        //总奖金包预算总奖金包预算2
        BigDecimal amountBonusBudgetReferenceValueTwo = new BigDecimal("0");
        //挑战值
        BigDecimal bonusChallengeValue = new BigDecimal("0");
        //保底值
        BigDecimal bonusGuaranteedValue = new BigDecimal("0");
        //目标值
        BigDecimal bonusTargetValue = new BigDecimal("0");
        //飞跃值
        BigDecimal bonusLeapValue = new BigDecimal("0");
        //梦想值
        BigDecimal bonusDreamValue = new BigDecimal("0");
        //最低值
        BigDecimal bonusMinValue = new BigDecimal("0");
        //奖金驱动因素/比值（%） 第四行 公式=总奖金包预算参考值1÷目标值
        BigDecimal bonusProportionRatio = new BigDecimal("0");
        //奖金驱动因素/比值（%）的行间差额 公式=各项（奖金驱动因素的奖金占比浮动差值×权重）的和
        BigDecimal bonusProportionDifference = new BigDecimal("0");
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                //目标值
                BigDecimal targetValue = bonusBudgetParametersDTO.getTargetValue();
                //挑战值
                BigDecimal challengeValue = bonusBudgetParametersDTO.getChallengeValue();
                //保底值
                BigDecimal guaranteedValue = bonusBudgetParametersDTO.getGuaranteedValue();
                //奖金占比基准值
                BigDecimal bonusProportionStandard = bonusBudgetParametersDTO.getBonusProportionStandard();
                //奖金占比浮动差值
                BigDecimal bonusProportionVariation = bonusBudgetParametersDTO.getBonusProportionVariation();
                //奖金权重(%)
                BigDecimal bonusWeight = bonusBudgetParametersDTO.getBonusWeight();
                //预算准确率
                BigDecimal targetCompletionRate = bonusBudgetParametersDTO.getTargetCompletionRate();
                //总奖金包预算总奖金包预算1
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusProportionStandard && bonusProportionStandard.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = targetValue.multiply(bonusProportionStandard.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).multiply(bonusWeight.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).setScale(10, BigDecimal.ROUND_HALF_UP);
                    //总奖金包预算总奖金包预算1 公式=各项（奖金驱动因素的目标值×奖金占比基准值×权重）的和
                    amountBonusBudgetReferenceValueOne = amountBonusBudgetReferenceValueOne.add(multiply);
                }
                //总奖金包预算总奖金包预算2
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusProportionStandard && bonusProportionStandard.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0 &&
                        null != targetCompletionRate && targetCompletionRate.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = targetValue.multiply(bonusProportionStandard.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).multiply(bonusWeight.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).multiply(targetCompletionRate.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).setScale(10, BigDecimal.ROUND_HALF_UP);
                    //总奖金包预算总奖金包预算1 公式=各项（奖金驱动因素的目标值×奖金占比基准值×权重×预算准确率）的和
                    amountBonusBudgetReferenceValueTwo = amountBonusBudgetReferenceValueTwo.add(multiply);
                }
                //目标值 公式 =各项（奖金驱动因素的目标值×权重）之和
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = targetValue.multiply(bonusWeight).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(10, BigDecimal.ROUND_HALF_UP);
                    //目标值 公式 =各项（奖金驱动因素的目标值×权重）之和
                    bonusTargetValue = bonusTargetValue.add(multiply);
                }
                //挑战值 公式 =各项（奖金驱动因素的挑战值×权重）之和
                if (null != challengeValue && challengeValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = challengeValue.multiply(bonusWeight).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(10, BigDecimal.ROUND_HALF_UP);
                    //挑战值 公式 =各项（奖金驱动因素的挑战值×权重）之和
                    bonusChallengeValue = bonusChallengeValue.add(multiply);
                }
                //保底值 公式 =各项（奖金驱动因素的保底值×权重）之和
                if (null != guaranteedValue && guaranteedValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = guaranteedValue.multiply(bonusWeight).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(10, BigDecimal.ROUND_HALF_UP);
                    //保底值 公式 =各项（奖金驱动因素的保底值×权重）之和
                    bonusGuaranteedValue = bonusGuaranteedValue.add(multiply);
                }
                //奖金驱动因素/比值（%）的行间差额 公式 =各项（奖金驱动因素的奖金占比浮动差值×权重）的和
                if (null != bonusProportionVariation && bonusProportionVariation.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = bonusProportionVariation.multiply(bonusWeight).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(10, BigDecimal.ROUND_HALF_UP);
                    //奖金驱动因素/比值（%）的行间差额 公式 =各项（奖金驱动因素的奖金占比浮动差值×权重）的和
                    bonusProportionDifference = bonusProportionDifference.add(multiply);
                }
            }
            //最低值 公式=目标值×0.5
            if (bonusTargetValue.compareTo(new BigDecimal("0")) != 0) {
                bonusMinValue = bonusTargetValue.multiply(new BigDecimal("0.5"));
            }
            //飞跃值 公式=目标值×1.5
            if (bonusTargetValue.compareTo(new BigDecimal("0")) != 0) {
                bonusLeapValue = bonusTargetValue.multiply(new BigDecimal("1.5"));
            }
            //梦想值 公式=目标值×2.0
            if (bonusTargetValue.compareTo(new BigDecimal("0")) != 0) {
                bonusDreamValue = bonusTargetValue.multiply(new BigDecimal("2.0"));
            }
            if (amountBonusBudgetReferenceValueOne.compareTo(new BigDecimal("0")) != 0 &&
                    bonusTargetValue.compareTo(new BigDecimal("0")) != 0) {
                //奖金驱动因素/比值 公式=总奖金包预算参考值1÷目标值
                bonusProportionRatio = amountBonusBudgetReferenceValueOne.divide(bonusTargetValue, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(10, BigDecimal.ROUND_HALF_UP);
            }
            //总奖金包预算总奖金包预算1
            bonusBudgetDTO.setAmountBonusBudgetReferenceValueOne(amountBonusBudgetReferenceValueOne);
            //总奖金包预算总奖金包预算2
            bonusBudgetDTO.setAmountBonusBudgetReferenceValueTwo(amountBonusBudgetReferenceValueTwo);
            //挑战值
            bonusBudgetDTO.setBonusChallengeValue(bonusChallengeValue);
            //目标值
            bonusBudgetDTO.setBonusTargetValue(bonusTargetValue);
            //保底值
            bonusBudgetDTO.setBonusGuaranteedValue(bonusGuaranteedValue);
            //飞跃值
            bonusBudgetDTO.setBonusLeapValue(bonusLeapValue);
            //梦想值
            bonusBudgetDTO.setBonusDreamValue(bonusDreamValue);
            //最低值
            bonusBudgetDTO.setBonusMinValue(bonusMinValue);
        }
        if (StringUtils.isNotNull(bonusBudgetDTO)) {
            //封装总部门奖金预算阶梯数据集合
            this.packBonusBudgetLaddertersDTO(bonusBudgetDTO, bonusBudgetLaddertersDTOS, bonusProportionRatio, bonusProportionDifference);
        }

    }


    /**
     * 封装总部门奖金预算 必须计算的数据方法
     *
     * @param bonusBudgetDTO
     * @param bonusBudgetParametersDTOS
     */
    public static void packBounLadderNum(BonusBudgetDTO bonusBudgetDTO, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        //总奖金包预算总奖金包预算1
        BigDecimal amountBonusBudgetReferenceValueOne = new BigDecimal("0");
        //总奖金包预算总奖金包预算2
        BigDecimal amountBonusBudgetReferenceValueTwo = new BigDecimal("0");
        //挑战值
        BigDecimal bonusChallengeValue = new BigDecimal("0");
        //保底值
        BigDecimal bonusGuaranteedValue = new BigDecimal("0");
        //目标值
        BigDecimal bonusTargetValue = new BigDecimal("0");
        //飞跃值
        BigDecimal bonusLeapValue = new BigDecimal("0");
        //梦想值
        BigDecimal bonusDreamValue = new BigDecimal("0");
        //最低值
        BigDecimal bonusMinValue = new BigDecimal("0");

        //奖金驱动因素/比值（%）的行间差额 公式=各项（奖金驱动因素的奖金占比浮动差值×权重）的和
        BigDecimal bonusProportionDifference = new BigDecimal("0");
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                //目标值
                BigDecimal targetValue = bonusBudgetParametersDTO.getTargetValue();
                //挑战值
                BigDecimal challengeValue = bonusBudgetParametersDTO.getChallengeValue();
                //保底值
                BigDecimal guaranteedValue = bonusBudgetParametersDTO.getGuaranteedValue();
                //奖金占比基准值
                BigDecimal bonusProportionStandard = bonusBudgetParametersDTO.getBonusProportionStandard();
                //奖金占比浮动差值
                BigDecimal bonusProportionVariation = bonusBudgetParametersDTO.getBonusProportionVariation();
                //奖金权重(%)
                BigDecimal bonusWeight = bonusBudgetParametersDTO.getBonusWeight();
                //预算准确率
                BigDecimal targetCompletionRate = bonusBudgetParametersDTO.getTargetCompletionRate();
                //总奖金包预算总奖金包预算1
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusProportionStandard && bonusProportionStandard.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = targetValue.multiply(bonusProportionStandard.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).multiply(bonusWeight.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //总奖金包预算总奖金包预算1 公式=各项（奖金驱动因素的目标值×奖金占比基准值×权重）的和
                    amountBonusBudgetReferenceValueOne = amountBonusBudgetReferenceValueOne.add(multiply);
                }
                //总奖金包预算总奖金包预算2
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusProportionStandard && bonusProportionStandard.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0 &&
                        null != targetCompletionRate && targetCompletionRate.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = targetValue.multiply(bonusProportionStandard.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).multiply(bonusWeight.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).multiply(targetCompletionRate.divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //总奖金包预算总奖金包预算1 公式=各项（奖金驱动因素的目标值×奖金占比基准值×权重×预算准确率）的和
                    amountBonusBudgetReferenceValueTwo = amountBonusBudgetReferenceValueTwo.add(multiply);
                }
                //目标值 公式 =各项（奖金驱动因素的目标值×权重）之和
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = targetValue.multiply(bonusWeight).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //目标值 公式 =各项（奖金驱动因素的目标值×权重）之和
                    bonusTargetValue = bonusTargetValue.add(multiply);
                }
                //挑战值 公式 =各项（奖金驱动因素的挑战值×权重）之和
                if (null != challengeValue && challengeValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = challengeValue.multiply(bonusWeight).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //挑战值 公式 =各项（奖金驱动因素的挑战值×权重）之和
                    bonusChallengeValue = bonusChallengeValue.add(multiply);
                }
                //保底值 公式 =各项（奖金驱动因素的保底值×权重）之和
                if (null != guaranteedValue && guaranteedValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = guaranteedValue.multiply(bonusWeight).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //保底值 公式 =各项（奖金驱动因素的保底值×权重）之和
                    bonusGuaranteedValue = bonusGuaranteedValue.add(multiply);
                }
                //奖金驱动因素/比值（%）的行间差额 公式 =各项（奖金驱动因素的奖金占比浮动差值×权重）的和
                if (null != bonusProportionVariation && bonusProportionVariation.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal multiply = bonusProportionVariation.multiply(bonusWeight).divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                    //奖金驱动因素/比值（%）的行间差额 公式 =各项（奖金驱动因素的奖金占比浮动差值×权重）的和
                    bonusProportionDifference = bonusProportionDifference.add(multiply);
                }
            }
            //最低值 公式=目标值×0.5
            if (bonusTargetValue.compareTo(new BigDecimal("0")) != 0) {
                bonusMinValue = bonusTargetValue.multiply(new BigDecimal("0.5"));
            }
            //飞跃值 公式=目标值×1.5
            if (bonusTargetValue.compareTo(new BigDecimal("0")) != 0) {
                bonusLeapValue = bonusTargetValue.multiply(new BigDecimal("1.5"));
            }
            //梦想值 公式=目标值×2.0
            if (bonusTargetValue.compareTo(new BigDecimal("0")) != 0) {
                bonusDreamValue = bonusTargetValue.multiply(new BigDecimal("2.0"));
            }
            //总奖金包预算总奖金包预算1
            bonusBudgetDTO.setAmountBonusBudgetReferenceValueOne(amountBonusBudgetReferenceValueOne);
            //总奖金包预算总奖金包预算2
            bonusBudgetDTO.setAmountBonusBudgetReferenceValueTwo(amountBonusBudgetReferenceValueTwo);
            //挑战值
            bonusBudgetDTO.setBonusChallengeValue(bonusChallengeValue);
            //目标值
            bonusBudgetDTO.setBonusTargetValue(bonusTargetValue);
            //保底值
            bonusBudgetDTO.setBonusGuaranteedValue(bonusGuaranteedValue);
            //飞跃值
            bonusBudgetDTO.setBonusLeapValue(bonusLeapValue);
            //梦想值
            bonusBudgetDTO.setBonusDreamValue(bonusDreamValue);
            //最低值
            bonusBudgetDTO.setBonusMinValue(bonusMinValue);
        }
    }

    /**
     * 封装总部门奖金预算阶梯数据集合
     *
     * @param bonusBudgetDTO
     * @param bonusBudgetLaddertersDTOS
     * @param bonusProportionRatio
     * @param bonusProportionDifference
     */
    private void packBonusBudgetLaddertersDTO(BonusBudgetDTO bonusBudgetDTO, List<BonusBudgetLaddertersDTO> bonusBudgetLaddertersDTOS, BigDecimal bonusProportionRatio, BigDecimal bonusProportionDifference) {
        //挑战值
        BigDecimal bonusChallengeValue = bonusBudgetDTO.getBonusChallengeValue();
        //目标值
        BigDecimal bonusTargetValue = bonusBudgetDTO.getBonusTargetValue();
        //保底值
        BigDecimal bonusGuaranteedValue = bonusBudgetDTO.getBonusGuaranteedValue();
        //飞跃值
        BigDecimal bonusLeapValue = bonusBudgetDTO.getBonusLeapValue();
        //梦想值
        BigDecimal bonusDreamValue = bonusBudgetDTO.getBonusDreamValue();
        //最低值
        BigDecimal bonusMinValue = bonusBudgetDTO.getBonusMinValue();
        //阶梯图的七行
        if (null != bonusProportionRatio && bonusProportionRatio.compareTo(new BigDecimal("0")) != 0 &&
                null != bonusProportionDifference && bonusProportionDifference.compareTo(new BigDecimal("0")) != 0) {
            for (int i = 0; i < 7; i++) {
                BonusBudgetLaddertersDTO bonusBudgetLaddertersDTO = new BonusBudgetLaddertersDTO();
                if (i == 0) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.subtract(bonusProportionDifference.multiply(new BigDecimal("3"))));
                } else if (i == 1) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.subtract(bonusProportionDifference.multiply(new BigDecimal("2"))));
                } else if (i == 2) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.subtract(bonusProportionDifference));
                } else if (i == 3) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio);
                } else if (i == 4) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.add(bonusProportionDifference));
                } else if (i == 5) {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.add(bonusProportionDifference.multiply(new BigDecimal("2"))));
                } else {
                    bonusBudgetLaddertersDTO.setBonusProportionRatio(bonusProportionRatio.add(bonusProportionDifference.multiply(new BigDecimal("3"))));
                }
                bonusBudgetLaddertersDTOS.add(bonusBudgetLaddertersDTO);
            }
        }
        //倒推阶梯图的七行其他数据
        if (StringUtils.isNotEmpty(bonusBudgetLaddertersDTOS)) {
            for (BonusBudgetLaddertersDTO bonusBudgetLaddertersDTO : bonusBudgetLaddertersDTOS) {
                //奖金驱动因素/比值（%）
                BigDecimal bonusProportionRatio1 = bonusBudgetLaddertersDTO.getBonusProportionRatio().divide(new BigDecimal("100"), 10, BigDecimal.ROUND_HALF_UP).setScale(10, BigDecimal.ROUND_HALF_UP);
                //挑战值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal challengeValue = new BigDecimal("0");
                //目标值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal targetValue = new BigDecimal("0");
                //保底值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal guaranteedValue = new BigDecimal("0");
                //飞跃值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal leapValue = new BigDecimal("0");
                //梦想值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal dreamValue = new BigDecimal("0");
                //最低值 公式=对应行的奖金驱动因素/比值×对应列的基准值（梦想值、飞跃值、挑战值、目标值、保底值、最低值）
                BigDecimal minValue = new BigDecimal("0");
                //挑战值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusChallengeValue && bonusChallengeValue.compareTo(new BigDecimal("0")) != 0) {
                    challengeValue = bonusProportionRatio1.multiply(bonusChallengeValue);
                    bonusBudgetLaddertersDTO.setChallengeValue(challengeValue);
                }
                //目标值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusTargetValue && bonusTargetValue.compareTo(new BigDecimal("0")) != 0) {
                    targetValue = bonusProportionRatio1.multiply(bonusTargetValue);
                    bonusBudgetLaddertersDTO.setTargetValue(targetValue);
                }
                //保底值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusGuaranteedValue && bonusGuaranteedValue.compareTo(new BigDecimal("0")) != 0) {
                    guaranteedValue = bonusProportionRatio1.multiply(bonusGuaranteedValue);
                    bonusBudgetLaddertersDTO.setGuaranteedValue(guaranteedValue);
                }
                //飞跃值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusLeapValue && bonusLeapValue.compareTo(new BigDecimal("0")) != 0) {
                    leapValue = bonusProportionRatio1.multiply(bonusLeapValue);
                    bonusBudgetLaddertersDTO.setLeapValue(leapValue);
                }
                //梦想值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusDreamValue && bonusDreamValue.compareTo(new BigDecimal("0")) != 0) {
                    dreamValue = bonusProportionRatio1.multiply(bonusDreamValue);
                    bonusBudgetLaddertersDTO.setDreamValue(dreamValue);
                }
                //最低值
                if (null != bonusProportionRatio1 && bonusProportionRatio1.compareTo(new BigDecimal("0")) != 0 &&
                        null != bonusMinValue && bonusMinValue.compareTo(new BigDecimal("0")) != 0) {
                    minValue = bonusProportionRatio1.multiply(bonusMinValue);
                    bonusBudgetLaddertersDTO.setMinValue(minValue);
                }
            }
        }
    }

    /**
     * 封装总奖金包预算参数指标数据
     *
     * @param budgetYear
     * @param bonusBudgetParametersDTOS
     */
    private void packBounParamIndicatorIds(int budgetYear, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS) {
        //获取当前年
        int year = DateUtils.getYear();
        //当前月份
        int month = DateUtils.getMonth();
        if (budgetYear < year) {
            year = budgetYear;
            month = 12;
        }
        //当前月份倒推12个月的“奖金”部分合计
        BigDecimal bonusActualSum = salaryPayMapper.selectBonusActualNum(year, month);


        //远程调用指标是否驱动因素为“是”列表
        R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIsDriverList(SecurityConstants.INNER);
        //封装奖金驱动因素实际数
        Map<Long, BigDecimal> indicatorIdBonusMap = new HashMap<>();
        List<IndicatorDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {
            for (IndicatorDTO datum : data) {
                BonusBudgetParametersDTO bonusBudgetParametersDTO = new BonusBudgetParametersDTO();
                //指标id
                bonusBudgetParametersDTO.setIndicatorId(datum.getIndicatorId());
                //奖金占比基准值(%)
                bonusBudgetParametersDTO.setBonusProportionStandard(new BigDecimal("0"));
                //预计目标达成率(%)
                bonusBudgetParametersDTO.setTargetCompletionRate(new BigDecimal("0"));
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
                targetOutcome.setLimitYear(collect.size() * 2);
            }

            //查询总奖金包预算参数的指标目标 挑战 保底 和奖金驱动因素实际数：从关键经营结果取值，当前月份倒推12个月的合计
            List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS = targetOutcomeMapper.selectDrivingFactor(targetOutcome);
            if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOS)) {
                //封装奖金驱动因素实际数
                indicatorIdBonusMap = packMonth(targetOutcomeDetailsDTOS, month);

            }
            if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
                if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOS)) {
                    //根据指标id分组
                    Map<Long, List<TargetOutcomeDetailsDTO>> indicatorIdMap = targetOutcomeDetailsDTOS.parallelStream().collect(Collectors.groupingBy(TargetOutcomeDetailsDTO::getIndicatorId));
                    for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS1 = indicatorIdMap.get(bonusBudgetParametersDTO.getIndicatorId());
                        if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOS1)) {
                            for (int i = 0; i < targetOutcomeDetailsDTOS1.size(); i++) {
                                if (i == 0) {
                                    //目标值
                                    BigDecimal targetValue = targetOutcomeDetailsDTOS1.get(i).getTargetValue();
                                    //挑战值
                                    BigDecimal challengeValue = targetOutcomeDetailsDTOS1.get(i).getChallengeValue();
                                    //保底值
                                    BigDecimal guaranteedValue = targetOutcomeDetailsDTOS1.get(i).getGuaranteedValue();
                                    bonusBudgetParametersDTO.setTargetValue(targetValue);
                                    bonusBudgetParametersDTO.setChallengeValue(challengeValue);
                                    bonusBudgetParametersDTO.setGuaranteedValue(guaranteedValue);
                                }

                            }
                        }
                    }

                }
                for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                    BigDecimal bonusProportionStandard = new BigDecimal("0");
                    //奖金驱动因素实际数
                    BigDecimal bonusProportionDrivingFactor = indicatorIdBonusMap.get(bonusBudgetParametersDTO.getIndicatorId());
                    //奖金占比基准值 公式=奖金包实际数÷奖金驱动因素实际数
                    if (null != bonusProportionDrivingFactor && bonusProportionDrivingFactor.compareTo(new BigDecimal("0")) != 0 &&
                            null != bonusActualSum && bonusActualSum.compareTo(new BigDecimal("0")) != 0) {
                        bonusProportionStandard = bonusActualSum.divide(bonusProportionDrivingFactor, 10, BigDecimal.ROUND_HALF_UP).setScale(2, BigDecimal.ROUND_HALF_UP);
                        //奖金占比基准值(%)
                        bonusBudgetParametersDTO.setBonusProportionStandard(bonusProportionStandard);
                    }
                }
            }
        }


    }

    /**
     * 封装奖金驱动因素实际数
     *
     * @param targetOutcomeDetailsDTOS
     * @param month
     */
    private Map<Long, BigDecimal> packMonth(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS, int month) {
        //根据指标id 奖金驱动因素实际数Map
        Map<Long, BigDecimal> bonusMap = new HashMap<>();

        //根据指标id分组
        Map<Long, List<TargetOutcomeDetailsDTO>> indicatorIdMap = targetOutcomeDetailsDTOS.parallelStream().collect(Collectors.groupingBy(TargetOutcomeDetailsDTO::getIndicatorId));
        if (StringUtils.isNotEmpty(indicatorIdMap)) {
            for (Long aLong : indicatorIdMap.keySet()) {
                List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOS1 = indicatorIdMap.get(aLong);
                //奖金驱动因素实际数
                BigDecimal bonusProportionDrivingFactor = new BigDecimal("0");
                if (StringUtils.isNotEmpty(targetOutcomeDetailsDTOS1)) {
                    //月份数据map集合
                    List<Map<Integer, BigDecimal>> listMap = new ArrayList<>();
                    for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDTOS1) {
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
                //添加
                bonusMap.put(aLong, bonusProportionDrivingFactor);
            }
        }


        return bonusMap;
    }

    /**
     * 逻辑删除奖金预算表信息
     *
     * @param bonusBudgetDTO 奖金预算表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteBonusBudgetByBonusBudgetId(BonusBudgetDTO bonusBudgetDTO) {
        int i = 0;
        BonusBudget bonusBudget = new BonusBudget();
        bonusBudget.setBonusBudgetId(bonusBudgetDTO.getBonusBudgetId());
        bonusBudget.setUpdateTime(DateUtils.getNowDate());
        bonusBudget.setUpdateBy(SecurityUtils.getUserId());
        BonusBudgetDTO bonusBudgetDTO1 = bonusBudgetMapper.selectBonusBudgetByBonusBudgetId(bonusBudget.getBonusBudgetId());
        if (StringUtils.isNull(bonusBudgetDTO1)) {
            throw new ServiceException("数据不存在 请联系管理员!");
        }
        try {
            i = bonusBudgetMapper.logicDeleteBonusBudgetByBonusBudgetId(bonusBudget);
        } catch (Exception e) {
            throw new ServiceException("删除总奖金预算失败");
        }
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudgetParametersMapper.selectBonusBudgetParametersByBonusBudgetId(bonusBudget.getBonusBudgetId());
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            List<Long> collect = bonusBudgetParametersDTOS.stream().map(BonusBudgetParametersDTO::getBonusBudgetId).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    bonusBudgetParametersMapper.logicDeleteBonusBudgetParametersByBonusBudgetParametersIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除总奖金预算参数失败");
                }
            }

        }
        return i;
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

    /**
     * 查询增人减人工资包
     *
     * @param employeeBudgetDTO
     * @return
     */
    public BigDecimal salaryPackageList(EmployeeBudgetDTO employeeBudgetDTO) {
        //增人减人工资包合计
        BigDecimal increaseAndDecreasePaySum = new BigDecimal("0");
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.salaryPackageList(employeeBudgetDTO);
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            //部门id去重
            List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getDepartmentId).distinct().collect(Collectors.toList());
            //远程调用组织
            if (StringUtils.isNotEmpty(collect)) {
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    //远程赋值部门名称
                    for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (employeeBudgetDetailsDTO.getDepartmentId() == datum.getDepartmentId()) {
                                employeeBudgetDetailsDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }
            //职级id去重
            List<Long> collect1 = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getOfficialRankSystemId).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect1)) {
                //职级体系远程调用
                R<List<OfficialRankSystemDTO>> listR = remoteOfficialRankSystemService.selectByIds(collect1, SecurityConstants.INNER);
                List<OfficialRankSystemDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                        //远程赋值职级名称
                        for (OfficialRankSystemDTO datum : data) {
                            if (employeeBudgetDetailsDTO.getOfficialRankSystemId() == datum.getOfficialRankSystemId()) {
                                employeeBudgetDetailsDTO.setOfficialRankSystemName(datum.getOfficialRankSystemName());
                                employeeBudgetDetailsDTO.setOfficialRankName(datum.getRankPrefixCode() + employeeBudgetDetailsDTO.getOfficialRank());
                            }
                        }
                    }
                }
            }
            List<List<Long>> list = new ArrayList<>();
            //部门id集合
            List<Long> collect2 = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getDepartmentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect2)) {
                list.add(collect2);
            }
            //职级体系ID集合
            List<Long> collect3 = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getOfficialRankSystemId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect3)) {
                list.add(collect3);
            }
            //封装增人减人工资
            this.packPayAmountNum(employeeBudgetDTO, employeeBudgetDetailsDTOS, list);

        }

        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                BigDecimal increaseAndDecreasePay = employeeBudgetDetailsDTO.getIncreaseAndDecreasePay();
                if (null != increaseAndDecreasePay && increaseAndDecreasePay.compareTo(new BigDecimal("0")) != 0)
                    increaseAndDecreasePaySum = increaseAndDecreasePaySum.add(increaseAndDecreasePay);
            }
        }
        return increaseAndDecreasePaySum;
    }

    /**
     * 封装增人减人工资
     *
     * @param employeeBudgetDTO
     * @param employeeBudgetDetailsDTOS
     * @param list
     */
    private void packPayAmountNum(EmployeeBudgetDTO employeeBudgetDTO, List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS, List<List<Long>> list) {
        if (StringUtils.isNotEmpty(list) && list.size() == 2) {
            R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByBudgeList(list, SecurityConstants.INNER);
            List<EmployeeDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data) && StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
                for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                    //人员id集合
                    List<Long> employeeIds = new ArrayList<>();
                    for (EmployeeDTO datum : data) {
                        //部门id 和个人职级相等
                        if (employeeBudgetDetailsDTO.getDepartmentId() == datum.getEmployeeDepartmentId() &&
                                employeeBudgetDetailsDTO.getOfficialRank() == datum.getEmployeeRank()) {
                            //人员id
                            employeeIds.add(datum.getEmployeeId());

                        }//取职级确定薪酬中位数
                        else {
                            OfficialRankEmolumentDTO officialRankEmolumentDTO = officialRankEmolumentMapper.selectOfficialRankEmolumentByRank(employeeBudgetDetailsDTO.getOfficialRankSystemId(), employeeBudgetDetailsDTO.getOfficialRank());
                            if (StringUtils.isNotNull(officialRankEmolumentDTO)) {
                                employeeBudgetDetailsDTO.setAgePayAmountLastYear(officialRankEmolumentDTO.getSalaryMedian().multiply(new BigDecimal("12")).setScale(10, BigDecimal.ROUND_HALF_UP));
                                employeeBudgetDetailsDTO.setAgePayAmountLastYearFlag(1);
                            }

                        }
                    }
                    if (StringUtils.isNotEmpty(employeeIds)) {
                        //发薪金额总计
                        BigDecimal payAmountSum = new BigDecimal("0");
                        //人员数量
                        int size = employeeIds.size();
                        for (Long employeeId : employeeIds) {
                            //根据人员id集合查询工资发薪表数据 计算该部门该职级体系下 根据职级等级分组的上年平均工资
                            List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectSalaryPayByBudggetEmployeeId(employeeId, employeeBudgetDTO.getBudgetYear());
                            BigDecimal reduce = salaryPayDTOS.stream().map(SalaryPayDTO::getPayAmountSum).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                            payAmountSum = payAmountSum.add(reduce);
                        }

                        if (payAmountSum.compareTo(new BigDecimal("0")) != 0 && size > 0) {
                            BigDecimal divide = payAmountSum.divide(new BigDecimal(String.valueOf(size)), 10, BigDecimal.ROUND_HALF_UP);
                            //上年平均工资 公式=相同部门、相同职级体系、相同岗位职级的员工倒推12个月的工资包合计÷员工人数
                            employeeBudgetDetailsDTO.setAgePayAmountLastYear(divide);
                            employeeBudgetDetailsDTO.setAgePayAmountLastYearFlag(0);
                        }
                    }
                }
                for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                    BigDecimal increaseAndDecreasePay = new BigDecimal("0");
                    //上年平均工资
                    BigDecimal agePayAmountLastYear = employeeBudgetDetailsDTO.getAgePayAmountLastYear();
                    //平均新增数
                    BigDecimal averageAdjust = employeeBudgetDetailsDTO.getAverageAdjust();
                    if (null != agePayAmountLastYear && null != averageAdjust && agePayAmountLastYear.compareTo(new BigDecimal("0")) != 0 && averageAdjust.compareTo(new BigDecimal("0")) != 0) {
                        //增人/减人工资包  公式=平均规划新增人数×上年平均工资。可为负数（代表部门人数减少）
                        increaseAndDecreasePay = agePayAmountLastYear.multiply(averageAdjust).setScale(2, BigDecimal.ROUND_FLOOR);
                    }
                    employeeBudgetDetailsDTO.setIncreaseAndDecreasePay(increaseAndDecreasePay);
                }
            }
        }
    }
}

