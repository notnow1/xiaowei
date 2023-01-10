package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptSalaryAdjustPlan;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.excel.salary.DeptSalaryAdjustPlanExcel;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.DeptSalaryAdjustPlanMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingMapper;
import net.qixiaowei.operate.cloud.service.bonus.IBonusBudgetService;
import net.qixiaowei.operate.cloud.service.salary.IDeptSalaryAdjustItemService;
import net.qixiaowei.operate.cloud.service.salary.IDeptSalaryAdjustPlanService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


/**
 * DeptSalaryAdjustPlanService业务层处理
 *
 * @author Graves
 * @since 2022-12-11
 */
@Service
public class DeptSalaryAdjustPlanServiceImpl implements IDeptSalaryAdjustPlanService {
    @Autowired
    private DeptSalaryAdjustPlanMapper deptSalaryAdjustPlanMapper;

    @Autowired
    private IDeptSalaryAdjustItemService deptSalaryAdjustItemService;

    @Autowired
    private IBonusBudgetService bonusBudgetService;

    @Autowired
    private BonusBudgetMapper bonusBudgetMapper;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private SalaryPayMapper salaryPayMapper;

    @Autowired
    private TargetSettingMapper targetSettingMapper;

    @Autowired
    private TargetOutcomeMapper targetOutcomeMapper;

    @Autowired
    private RemoteIndicatorService indicatorService;

    /**
     * 查询部门调薪计划表-详情
     *
     * @param deptSalaryAdjustPlanId 部门调薪计划表主键
     * @return 部门调薪计划表
     */
    @Override
    public DeptSalaryAdjustPlanDTO selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(Long deptSalaryAdjustPlanId) {
        if (StringUtils.isNull(deptSalaryAdjustPlanId)) {
            throw new ServiceException("请返回部门调薪计划ID");
        }
        DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
        if (StringUtils.isNull(deptSalaryAdjustPlanDTO)) {
            throw new ServiceException("当前部门调薪计划已不存在");
        }
        Integer planYear = deptSalaryAdjustPlanDTO.getPlanYear();
        BonusBudgetDTO bonusBudgetDTO = bonusBudgetMapper.selectBonusBudgetListByBudgetYear(planYear);
        if (StringUtils.isNull(bonusBudgetDTO)) {
            deptSalaryAdjustPlanDTO.setRaiseSalaryBonusBudget(BigDecimal.ZERO);
        } else {
            bonusBudgetService.packPaymentBonusBudget(planYear, bonusBudgetDTO);
            deptSalaryAdjustPlanDTO.setRaiseSalaryBonusBudget(bonusBudgetDTO.getRaiseSalaryBonusBudget());
        }
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOS = deptSalaryAdjustItemService.selectDeptSalaryAdjustItemByPlanId(deptSalaryAdjustPlanId);
        if (StringUtils.isEmpty(deptSalaryAdjustItemDTOS)) {
            return deptSalaryAdjustPlanDTO;
        }
        List<Long> departmentIds = deptSalaryAdjustItemDTOS.stream().map(DeptSalaryAdjustItemDTO::getDepartmentId).distinct().collect(Collectors.toList());// 去重
        List<DepartmentDTO> departmentDTO = getDepartmentDTO(departmentIds);
        List<EmployeeDTO> employeeByDepartmentIds = getEmployeeByDepartmentIds(departmentIds);
        for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : deptSalaryAdjustItemDTOS) {
            for (DepartmentDTO department : departmentDTO) {
                if (deptSalaryAdjustItemDTO.getDepartmentId().equals(department.getDepartmentId())) {
                    deptSalaryAdjustItemDTO.setDepartmentName(department.getDepartmentName());
                    break;
                }
            }
            List<Long> employeeIds = new ArrayList<>();
            for (EmployeeDTO employeeDTO : employeeByDepartmentIds) {
                if (employeeDTO.getEmployeeDepartmentId().equals(deptSalaryAdjustItemDTO.getDepartmentId())) {
                    employeeIds.add(employeeDTO.getEmployeeId());
                }
            }
            // 上年工资包
            BigDecimal lastSalary = new BigDecimal(0);
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOList = null;
            if (StringUtils.isNotEmpty(employeeIds)) {
                if (planYear < DateUtils.getYear()) {
                    salaryPayDetailsDTOList = salaryPayMapper.selectSalaryAmountNum(planYear, employeeIds);
                } else {
                    salaryPayDetailsDTOList = salaryPayMapper.selectSalaryAmountNum(DateUtils.getYear(), employeeIds);
                }
                if (StringUtils.isNotEmpty(salaryPayDetailsDTOList)) {
                    lastSalary = getLastSalaryAmount(lastSalary, salaryPayDetailsDTOList);
                }
            }
            BigDecimal adjustmentPercentage = deptSalaryAdjustItemDTO.getAdjustmentPercentage();//调幅
            BigDecimal coveragePercentage = deptSalaryAdjustItemDTO.getCoveragePercentage();// 覆盖比例
            LocalDate adjustmentTime = DateUtils.toLocalDate(deptSalaryAdjustItemDTO.getAdjustmentTime());//调整时间
            int month;
            if (StringUtils.isNull(adjustmentTime)) {
                month = DateUtils.getMonth();
            } else {
                month = DateUtils.getMonth(adjustmentTime);
            }
            // addSalary 新增工资包公式    =   上年工资包×覆盖比例×调幅×（12-调薪月份）÷12。
            BigDecimal addSalary;
            if (lastSalary.compareTo(BigDecimal.ZERO) != 0 && adjustmentPercentage.compareTo(BigDecimal.ZERO) != 0 && coveragePercentage.compareTo(BigDecimal.ZERO) != 0) {
                addSalary = lastSalary.multiply(adjustmentPercentage).multiply(coveragePercentage)
                        .multiply(new BigDecimal(12).subtract(new BigDecimal(month))).divide(new BigDecimal(120000), 2, RoundingMode.HALF_UP);
            } else {
                addSalary = BigDecimal.ZERO;
            }
            deptSalaryAdjustItemDTO.setLastSalary(lastSalary);
            deptSalaryAdjustItemDTO.setAddSalary(addSalary);
        }
        deptSalaryAdjustPlanDTO.setDeptSalaryAdjustItemDTOS(deptSalaryAdjustItemDTOS);
        return deptSalaryAdjustPlanDTO;
    }

    /**
     * 根据部门ID集合查找人员信息
     *
     * @param departmentIds 部门ID集合
     * @return List
     */
    private List<EmployeeDTO> getEmployeeByDepartmentIds(List<Long> departmentIds) {
        R<List<EmployeeDTO>> listR = employeeService.selectEmployeeByDepartmentIds(departmentIds, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("根据部门ID 集合查询人员失败");
        }
        return employeeDTOS;
    }

    /**
     * 根据部门ID集合查找人员信息
     *
     * @param departmentId 部门ID集合
     * @return List
     */
    private List<EmployeeDTO> getEmployeeByDepartmentId(Long departmentId) {
        R<List<EmployeeDTO>> listR = employeeService.selectEmployeeByDepts(departmentId, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("根据部门ID查询人员失败");
        }
        return employeeDTOS;
    }

    /**
     * 根据部门ID集合查找人员信息
     *
     * @param departmentIds 部门ID集合
     * @return List
     */
    private List<DepartmentDTO> getDepartmentDTO(List<Long> departmentIds) {
        R<List<DepartmentDTO>> listR = departmentService.selectdepartmentIds(departmentIds, SecurityConstants.INNER);
        List<DepartmentDTO> departmentDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("根据部门ID 集合查询部门失败");
        }
        return departmentDTOS;
    }

    /**
     * 查询部门调薪计划表列表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 部门调薪计划表
     */
    @Override
    public List<DeptSalaryAdjustPlanDTO> selectDeptSalaryAdjustPlanList(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        BeanUtils.copyProperties(deptSalaryAdjustPlanDTO, deptSalaryAdjustPlan);
        List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDTOS = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanList(deptSalaryAdjustPlan);
        if (StringUtils.isEmpty(deptSalaryAdjustPlanDTOS)) {
            return new ArrayList<>();
        }
        List<Integer> planYears = deptSalaryAdjustPlanDTOS.stream().map(DeptSalaryAdjustPlanDTO::getPlanYear).collect(Collectors.toList());
        List<BonusBudgetDTO> bonusBudgetDTOS = bonusBudgetMapper.selectBonusBudgetListByBudgetYears(planYears);
        //封装总奖金包预算列表涨薪包数据
        if (StringUtils.isEmpty(bonusBudgetDTOS)) {
            for (DeptSalaryAdjustPlanDTO salaryAdjustPlanDTO : deptSalaryAdjustPlanDTOS) {
                salaryAdjustPlanDTO.setRaiseSalaryBonusBudget(BigDecimal.ZERO);
            }
        } else {
            bonusBudgetService.packPaymentBonusBudgetList(bonusBudgetDTOS);
            for (DeptSalaryAdjustPlanDTO salaryAdjustPlanDTO : deptSalaryAdjustPlanDTOS) {
                for (BonusBudgetDTO bonusBudgetDTO : bonusBudgetDTOS) {
                    if (salaryAdjustPlanDTO.getPlanYear().equals(bonusBudgetDTO.getBudgetYear())) {
                        if (StringUtils.isNull(bonusBudgetDTO.getRaiseSalaryBonusBudget()) || bonusBudgetDTO.getRaiseSalaryBonusBudget().compareTo(BigDecimal.ZERO) == 0) {
                            bonusBudgetDTO.setRaiseSalaryBonusBudget(BigDecimal.ZERO);
                            break;
                        }
                        salaryAdjustPlanDTO.setRaiseSalaryBonusBudget(bonusBudgetDTO.getRaiseSalaryBonusBudget());
                        break;
                    }
                }
            }
        }
        return deptSalaryAdjustPlanDTOS;
    }

    /**
     * 新增部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    @Override
    public int insertDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        if (StringUtils.isNull(deptSalaryAdjustPlanDTO)) {
            throw new ServiceException("部门调薪计划表不能为空");
        }
        Integer planYear = deptSalaryAdjustPlanDTO.getPlanYear();
        DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanByYear = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanByYear(planYear);
        if (StringUtils.isNotNull(deptSalaryAdjustPlanByYear)) {
            throw new ServiceException("当前年份数据已存在 请前往编辑页面修改");
        }
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = addDeptSalaryAdjustPlan(deptSalaryAdjustPlanDTO);
        Long deptSalaryAdjustPlanId = deptSalaryAdjustPlan.getDeptSalaryAdjustPlanId();
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSAfter = deptSalaryAdjustPlanDTO.getDeptSalaryAdjustItemDTOS();
        if (StringUtils.isEmpty(deptSalaryAdjustItemDTOSAfter)) {
            return 1;
        }
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSBefore = deptSalaryAdjustItemService.selectDeptSalaryAdjustItemByPlanId(deptSalaryAdjustPlanId);
        for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : deptSalaryAdjustItemDTOSAfter) {
            if (StringUtils.isNull(deptSalaryAdjustItemDTO.getDepartmentId())) {
                throw new ServiceException("部门不可以为空");
            }
            deptSalaryAdjustItemDTO.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
            for (DeptSalaryAdjustItemDTO salaryAdjustItemDTO : deptSalaryAdjustItemDTOSBefore) {
                if (salaryAdjustItemDTO.getDepartmentId().equals(deptSalaryAdjustItemDTO.getDepartmentId())) {
                    deptSalaryAdjustItemDTO.setDeptSalaryAdjustItemId(salaryAdjustItemDTO.getDeptSalaryAdjustItemId());
                    break;
                }
            }
        }
        return operateDeptSalaryAdjustItem(deptSalaryAdjustItemDTOSBefore, deptSalaryAdjustItemDTOSAfter);
    }

    /**
     * 修改部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    @Override
    @Transactional
    public int editDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        if (StringUtils.isNull(deptSalaryAdjustPlanDTO)) {
            throw new ServiceException("部门调薪计划表不能为空");
        }
        Long deptSalaryAdjustPlanId = deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId();
        if (StringUtils.isNull(deptSalaryAdjustPlanId)) {
            throw new ServiceException("部门调薪ID不能为空");
        }
        DeptSalaryAdjustPlanDTO deptSalaryAdjustPlan = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
        if (StringUtils.isNull(deptSalaryAdjustPlan)) {
            throw new ServiceException("当前部门调薪计划已不存在");
        }
        if (StringUtils.isNull(deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId())) {
            deptSalaryAdjustPlanDTO.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlan.getDeptSalaryAdjustPlanId());
        }
        this.updateDeptSalaryAdjustPlan(deptSalaryAdjustPlanDTO);
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSAfter = deptSalaryAdjustPlanDTO.getDeptSalaryAdjustItemDTOS();
        if (StringUtils.isEmpty(deptSalaryAdjustItemDTOSAfter)) {
            return 1;
        }
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSBefore = deptSalaryAdjustItemService.selectDeptSalaryAdjustItemByPlanId(deptSalaryAdjustPlanId);
        for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : deptSalaryAdjustItemDTOSAfter) {
            deptSalaryAdjustItemDTO.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
            for (DeptSalaryAdjustItemDTO salaryAdjustItemDTO : deptSalaryAdjustItemDTOSBefore) {
                if (salaryAdjustItemDTO.getDepartmentId().equals(deptSalaryAdjustItemDTO.getDepartmentId())) {
                    deptSalaryAdjustItemDTO.setDeptSalaryAdjustItemId(salaryAdjustItemDTO.getDeptSalaryAdjustItemId());
                    break;
                }
            }
        }
        return operateDeptSalaryAdjustItem(deptSalaryAdjustItemDTOSBefore, deptSalaryAdjustItemDTOSAfter);
    }

    /**
     * 新增部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    private DeptSalaryAdjustPlan addDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        deptSalaryAdjustPlan.setPlanYear(deptSalaryAdjustPlanDTO.getPlanYear());
        deptSalaryAdjustPlan.setSalaryAdjustTotal(deptSalaryAdjustPlanDTO.getSalaryAdjustTotal());
        deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
        deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        deptSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        deptSalaryAdjustPlanMapper.insertDeptSalaryAdjustPlan(deptSalaryAdjustPlan);
        return deptSalaryAdjustPlan;
    }

    /**
     * 获取上年发薪包
     *
     * @param departmentId 部门ID
     * @param planYear     预算年份
     * @return adjustmentPercentage 调幅
     * 公式=（预算年度的销售收入目标值/上年实际销售收入-1）×0.5。
     * 预算年度的销售收入目标值：销售收入目标制定中的目标值。
     * 上年实际销售收入：关键经营结果中的销售收入的年度实际值。
     * 若取不到数，则调幅为0。
     */
    @Override
    public Map<String, BigDecimal> getLastSalary(Long departmentId, Integer planYear) {
        Map<String, BigDecimal> map = new HashMap<>();
        List<EmployeeDTO> employeeByDepartmentId = getEmployeeByDepartmentId(departmentId);
        BigDecimal lastSalary = new BigDecimal(0);
        if (StringUtils.isNotEmpty(employeeByDepartmentId)) {
            List<Long> employeeIds = employeeByDepartmentId.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
            // 上年工资包
            if (StringUtils.isNull(planYear)) {
                planYear = DateUtils.getYear();
            }
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOList;
            if (planYear < DateUtils.getYear()) {
                salaryPayDetailsDTOList = salaryPayMapper.selectSalaryAmountNum(planYear, employeeIds);
            } else {
                salaryPayDetailsDTOList = salaryPayMapper.selectSalaryAmountNum(DateUtils.getYear(), employeeIds);
            }
            lastSalary = getLastSalaryAmount(lastSalary, salaryPayDetailsDTOList);
        }
        map.put("lastSalary", lastSalary);
        TargetSettingDTO targetSettingDTO = new TargetSettingDTO();
        targetSettingDTO.setTargetSettingType(2);
        List<Integer> planYears = new ArrayList<>();
        planYears.add(planYear);
        IndicatorDTO indicatorDTO = getIndicatorDTO();
        if (StringUtils.isNull(indicatorDTO)) {
            map.put("adjustmentPercentage", BigDecimal.ZERO);
            return map;
        }
        TargetOutcomeDetailsDTO targetOutcomeDetailsDTO = targetOutcomeMapper.selectTargetOutcomeValue(planYear - 1, indicatorDTO.getIndicatorId());
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingByYears(targetSettingDTO, planYears);
        if (StringUtils.isEmpty(targetSettingDTOList) || StringUtils.isNull(targetOutcomeDetailsDTO)) {
            map.put("adjustmentPercentage", BigDecimal.ZERO);
            return map;
        }

        BigDecimal targetValue = Optional.ofNullable(targetSettingDTOList.get(0).getTargetValue()).orElse(BigDecimal.ZERO);
        BigDecimal actualTotal = Optional.ofNullable(targetOutcomeDetailsDTO.getActualTotal()).orElse(BigDecimal.ZERO);
        if (targetValue.compareTo(BigDecimal.ZERO) == 0 || actualTotal.compareTo(BigDecimal.ZERO) == 0) {
            map.put("adjustmentPercentage", BigDecimal.ZERO);
            return map;
        }

        BigDecimal adjustmentPercentage = (targetValue.divide(actualTotal, 2, RoundingMode.HALF_UP).subtract(BigDecimal.ONE)).multiply(new BigDecimal("0.5"));
        map.put("adjustmentPercentage", adjustmentPercentage);
        return map;
    }

    /**
     * 获取上年工资包金额
     *
     * @param lastSalary              工资包金额
     * @param salaryPayDetailsDTOList 工资详情LIST
     * @return BigDecimal
     */
    private BigDecimal getLastSalaryAmount(BigDecimal lastSalary, List<SalaryPayDetailsDTO> salaryPayDetailsDTOList) {
        Map<Long, List<SalaryPayDetailsDTO>> employeeMap = salaryPayDetailsDTOList.stream().collect(Collectors.groupingBy(SalaryPayDetailsDTO::getEmployeeId));
        for (Long employeeId : employeeMap.keySet()) {
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOS = employeeMap.get(employeeId);
            for (int i = salaryPayDetailsDTOS.size() - 1; i >= 0; i--) {
                SalaryPayDetailsDTO salaryPayDetailsDTO = salaryPayDetailsDTOS.get(i);
                if (salaryPayDetailsDTO.getPayYear() == DateUtils.getYear() && salaryPayDetailsDTO.getPayMonth() >= DateUtils.getMonth()) {
                    salaryPayDetailsDTOS.remove(i);
                }
            }
            if (salaryPayDetailsDTOS.size() > 12) {
                salaryPayDetailsDTOS = salaryPayDetailsDTOS.subList(0, 12);//取0-12月
            }
            for (SalaryPayDetailsDTO payDetailsDTO : salaryPayDetailsDTOS) {
                lastSalary = lastSalary.add(payDetailsDTO.getAmount());
            }
        }
        // 单位时万元 此处除以10000
        lastSalary = lastSalary.divide(new BigDecimal(10000), 2, RoundingMode.HALF_UP);
        return lastSalary;
    }

    /**
     * 获取涨薪包预算
     *
     * @param planYear 预算年份
     * @return BigDecimal
     */
    @Override
    public BigDecimal getRaiseSalary(Integer planYear) {
        BonusBudgetDTO bonusBudgetDTO = bonusBudgetMapper.selectBonusBudgetListByBudgetYear(planYear);
        if (StringUtils.isNull(bonusBudgetDTO)) {
            return BigDecimal.ZERO;
        }
        bonusBudgetService.packPaymentBonusBudget(planYear, bonusBudgetDTO);
        return Optional.ofNullable(bonusBudgetDTO.getRaiseSalaryBonusBudget()).orElse(BigDecimal.ZERO);
    }

    /**
     * 获取已有数据的最大年份
     *
     * @return Integer
     */
    @Override
    public Integer getMaxYear() {
        Integer planYear = deptSalaryAdjustPlanMapper.selectMaxYear();
        if (StringUtils.isNull(planYear)) {
            return DateUtils.getYear();
        }
        return planYear + 1;
    }

    /**
     * 获取指标数据
     *
     * @return IndicatorDTO
     */
    private IndicatorDTO getIndicatorDTO() {
        R<IndicatorDTO> indicatorDTOR = indicatorService.selectIndicatorByCode(IndicatorCode.INCOME.getCode(), SecurityConstants.INNER);
        IndicatorDTO indicatorDTO = indicatorDTOR.getData();
        if (indicatorDTOR.getCode() != 200) {
            throw new ServiceException("获取指标信息失败");
        }
        return indicatorDTO;
    }

    /**
     * 处理部门调薪项
     *
     * @param deptSalaryAdjustItemDTOSBefore 库的内容
     * @param deptSalaryAdjustItemDTOSAfter  传来的内容
     */
    private int operateDeptSalaryAdjustItem(List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSBefore, List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSAfter) {
        // 交集
        List<DeptSalaryAdjustItemDTO> updateDeptSalaryAdjustItem =
                deptSalaryAdjustItemDTOSAfter.stream().filter(deptSalaryAdjustItemDTO ->
                        deptSalaryAdjustItemDTOSBefore.stream().map(DeptSalaryAdjustItemDTO::getDepartmentId)
                                .collect(Collectors.toList()).contains(deptSalaryAdjustItemDTO.getDepartmentId())
                ).collect(Collectors.toList());
        // 差集 Before中After的补集
        List<DeptSalaryAdjustItemDTO> delDeptSalaryAdjustItem =
                deptSalaryAdjustItemDTOSBefore.stream().filter(deptSalaryAdjustItemDTO ->
                        !deptSalaryAdjustItemDTOSAfter.stream().map(DeptSalaryAdjustItemDTO::getDepartmentId)
                                .collect(Collectors.toList()).contains(deptSalaryAdjustItemDTO.getDepartmentId())
                ).collect(Collectors.toList());
        // 差集 After中Before的补集
        List<DeptSalaryAdjustItemDTO> addDeptSalaryAdjustItem =
                deptSalaryAdjustItemDTOSAfter.stream().filter(deptSalaryAdjustItemDTO ->
                        !deptSalaryAdjustItemDTOSBefore.stream().map(DeptSalaryAdjustItemDTO::getDepartmentId)
                                .collect(Collectors.toList()).contains(deptSalaryAdjustItemDTO.getDepartmentId())
                ).collect(Collectors.toList());
        try {
            if (StringUtils.isNotEmpty(addDeptSalaryAdjustItem)) {
                deptSalaryAdjustItemService.insertDeptSalaryAdjustItems(addDeptSalaryAdjustItem);
            }
            if (StringUtils.isNotEmpty(updateDeptSalaryAdjustItem)) {
                deptSalaryAdjustItemService.updateDeptSalaryAdjustItems(updateDeptSalaryAdjustItem);
            }
            if (StringUtils.isNotEmpty(delDeptSalaryAdjustItem)) {
                List<Long> deptSalaryAdjustItemIds = new ArrayList<>();
                for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : delDeptSalaryAdjustItem) {
                    deptSalaryAdjustItemIds.add(deptSalaryAdjustItemDTO.getDeptSalaryAdjustItemId());
                }
                deptSalaryAdjustItemService.logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(deptSalaryAdjustItemIds);
            }
        } catch (ServiceException e) {
            throw new ServiceException("调薪项保存失败 请联系管理员");
        }
        return 1;
    }

    /**
     * 修改部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    @Override
    public int updateDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        deptSalaryAdjustPlan.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId());
        deptSalaryAdjustPlan.setSalaryAdjustTotal(deptSalaryAdjustPlanDTO.getSalaryAdjustTotal());
        deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        return deptSalaryAdjustPlanMapper.updateDeptSalaryAdjustPlan(deptSalaryAdjustPlan);
    }

    /**
     * 逻辑批量删除部门调薪计划表
     *
     * @param deptSalaryAdjustPlanIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(List<Long> deptSalaryAdjustPlanIds) {
        if (StringUtils.isEmpty(deptSalaryAdjustPlanIds)) {
            throw new ServiceException("请传输需要删除的ID");
        }
        List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDTOS = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(deptSalaryAdjustPlanIds);
        if (deptSalaryAdjustPlanDTOS.size() != deptSalaryAdjustPlanIds.size()) {
            throw new ServiceException("当前的调薪计划丢失");
        }
        deptSalaryAdjustPlanMapper.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(deptSalaryAdjustPlanIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOS = deptSalaryAdjustItemService.selectDeptSalaryAdjustItemByPlanIds(deptSalaryAdjustPlanIds);
        if (StringUtils.isEmpty(deptSalaryAdjustItemDTOS)) {
            return 1;
        }
        List<Long> deptSalaryAdjustItemIds = deptSalaryAdjustItemDTOS.stream().map(DeptSalaryAdjustItemDTO::getDeptSalaryAdjustItemId).collect(Collectors.toList());
        return deptSalaryAdjustItemService.logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(deptSalaryAdjustItemIds);
    }

    /**
     * 物理删除部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanId 部门调薪计划表主键
     * @return 结果
     */
    @Override
    public int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(Long deptSalaryAdjustPlanId) {
        return deptSalaryAdjustPlanMapper.deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
    }

    /**
     * 逻辑删除部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    @Override
    public int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        Long deptSalaryAdjustPlanId = deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId();
        if (StringUtils.isNull(deptSalaryAdjustPlanId)) {
            throw new ServiceException("请传输需要删除的ID");
        }
        DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanById = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
        if (StringUtils.isNull(deptSalaryAdjustPlanById)) {
            throw new ServiceException("当前的调薪计划信息已不存在");
        }
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        deptSalaryAdjustPlan.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlanId);
        deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        deptSalaryAdjustPlanMapper.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlan);
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOS = deptSalaryAdjustItemService.selectDeptSalaryAdjustItemByPlanId(deptSalaryAdjustPlanId);
        if (StringUtils.isEmpty(deptSalaryAdjustItemDTOS)) {
            return 1;
        }
        List<Long> deptSalaryAdjustItemIds = deptSalaryAdjustItemDTOS.stream().map(DeptSalaryAdjustItemDTO::getDeptSalaryAdjustItemId).collect(Collectors.toList());
        return deptSalaryAdjustItemService.logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(deptSalaryAdjustItemIds);
    }

    /**
     * 物理删除部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */

    @Override
    public int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        BeanUtils.copyProperties(deptSalaryAdjustPlanDTO, deptSalaryAdjustPlan);
        return deptSalaryAdjustPlanMapper.deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlan.getDeptSalaryAdjustPlanId());
    }

    /**
     * 物理批量删除部门调薪计划表
     *
     * @param DeptSalaryAdjustPlanDtoS 需要删除的部门调薪计划表主键
     * @return 结果
     */

    @Override
    public int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(List<DeptSalaryAdjustPlanDTO> DeptSalaryAdjustPlanDtoS) {
        List<Long> stringList = new ArrayList<>();
        for (DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO : DeptSalaryAdjustPlanDtoS) {
            stringList.add(deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId());
        }
        return deptSalaryAdjustPlanMapper.deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(stringList);
    }

    /**
     * 批量新增部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanDtos 部门调薪计划表对象
     */

    @Override
    public int insertDeptSalaryAdjustPlans(List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDtos) {
        List<DeptSalaryAdjustPlan> deptSalaryAdjustPlanList = new ArrayList<>();

        for (DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO : deptSalaryAdjustPlanDtos) {
            DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
            BeanUtils.copyProperties(deptSalaryAdjustPlanDTO, deptSalaryAdjustPlan);
            deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
            deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
            deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
            deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
            deptSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            deptSalaryAdjustPlanList.add(deptSalaryAdjustPlan);
        }
        return deptSalaryAdjustPlanMapper.batchDeptSalaryAdjustPlan(deptSalaryAdjustPlanList);
    }

    /**
     * 批量修改部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanDtos 部门调薪计划表对象
     */

    @Override
    public int updateDeptSalaryAdjustPlans(List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDtos) {
        List<DeptSalaryAdjustPlan> deptSalaryAdjustPlanList = new ArrayList<>();

        for (DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO : deptSalaryAdjustPlanDtos) {
            DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
            BeanUtils.copyProperties(deptSalaryAdjustPlanDTO, deptSalaryAdjustPlan);
            deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
            deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
            deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
            deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
            deptSalaryAdjustPlanList.add(deptSalaryAdjustPlan);
        }
        return deptSalaryAdjustPlanMapper.updateDeptSalaryAdjustPlans(deptSalaryAdjustPlanList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importDeptSalaryAdjustPlan(List<DeptSalaryAdjustPlanExcel> list) {
        List<DeptSalaryAdjustPlan> deptSalaryAdjustPlanList = new ArrayList<>();
        list.forEach(l -> {
            DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
            BeanUtils.copyProperties(l, deptSalaryAdjustPlan);
            deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
            deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
            deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
            deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
            deptSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            deptSalaryAdjustPlanList.add(deptSalaryAdjustPlan);
        });
        try {
            deptSalaryAdjustPlanMapper.batchDeptSalaryAdjustPlan(deptSalaryAdjustPlanList);
        } catch (Exception e) {
            throw new ServiceException("导入部门调薪计划表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param deptSalaryAdjustPlanDTO
     * @return
     */
    @Override
    public List<DeptSalaryAdjustPlanExcel> exportDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        BeanUtils.copyProperties(deptSalaryAdjustPlanDTO, deptSalaryAdjustPlan);
        List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDTOList = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanList(deptSalaryAdjustPlan);
        List<DeptSalaryAdjustPlanExcel> deptSalaryAdjustPlanExcelList = new ArrayList<>();
        return deptSalaryAdjustPlanExcelList;
    }
}

