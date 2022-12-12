package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptSalaryAdjustPlan;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.excel.salary.DeptSalaryAdjustPlanExcel;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.DeptSalaryAdjustPlanMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.bonus.IBonusBudgetService;
import net.qixiaowei.operate.cloud.service.salary.IDeptSalaryAdjustItemService;
import net.qixiaowei.operate.cloud.service.salary.IDeptSalaryAdjustPlanService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
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
        bonusBudgetService.packPaymentBonusBudget(planYear, bonusBudgetDTO);
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOS = deptSalaryAdjustItemService.selectDeptSalaryAdjustItemByPlanId(deptSalaryAdjustPlanId);
        if (StringUtils.isEmpty(deptSalaryAdjustItemDTOS)) {
            return deptSalaryAdjustPlanDTO;
        }
        List<Long> departmentIds = deptSalaryAdjustItemDTOS.stream().map(DeptSalaryAdjustItemDTO::getDepartmentId).distinct().collect(Collectors.toList());// 去重
        List<EmployeeDTO> employeeByDepartmentIds = getEmployeeByDepartmentIds(departmentIds);
        List<DepartmentDTO> departmentDTO = getDepartmentDTO(departmentIds);
        for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : deptSalaryAdjustItemDTOS) {
            for (DepartmentDTO department : departmentDTO) {
                if (deptSalaryAdjustItemDTO.getDepartmentId().equals(department.getParentDepartmentId())) {
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
            BigDecimal lastSalary = salaryPayMapper.selectSalaryAmountNum(DateUtils.getYear(), DateUtils.getMonth(), employeeIds);
            deptSalaryAdjustItemDTO.setLastSalary(lastSalary);
            // addSalary 新增工资包公式    =   上年工资包×覆盖比例×调幅×（12-调薪月份）÷12。
            BigDecimal adjustmentPercentage = deptSalaryAdjustItemDTO.getAdjustmentPercentage();//调幅
            BigDecimal coveragePercentage = deptSalaryAdjustItemDTO.getCoveragePercentage();// 覆盖比例
            LocalDate adjustmentTime = deptSalaryAdjustItemDTO.getAdjustmentTime();//调整时间
            int month;
            if (StringUtils.isNull(adjustmentTime)) {
                month = DateUtils.getMonth();
            } else {
                month = DateUtils.getMonth(adjustmentTime);
            }
            BigDecimal addSalary;
            if (lastSalary.compareTo(BigDecimal.ZERO) != 0 && adjustmentPercentage.compareTo(BigDecimal.ZERO) != 0 && coveragePercentage.compareTo(BigDecimal.ZERO) != 0) {
                addSalary = lastSalary.multiply(adjustmentPercentage).multiply(coveragePercentage)
                        .multiply(new BigDecimal(12).subtract(new BigDecimal(month))).divide(new BigDecimal(12), 2, RoundingMode.HALF_UP);
            } else {
                addSalary = BigDecimal.ZERO;
            }
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
        if (StringUtils.isNotEmpty(employeeDTOS)) {
            throw new ServiceException("部门调薪项中的部门已没有没有人员信息 请检查");
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
        if (StringUtils.isNotEmpty(departmentDTOS)) {
            throw new ServiceException("调薪项中部门的信息丢失");
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
        bonusBudgetService.packPaymentBonusBudgetList(bonusBudgetDTOS);
        for (DeptSalaryAdjustPlanDTO salaryAdjustPlanDTO : deptSalaryAdjustPlanDTOS) {
            for (BonusBudgetDTO bonusBudgetDTO : bonusBudgetDTOS) {
                if (StringUtils.isNull(bonusBudgetDTO.getRaiseSalaryBonusBudget()) || bonusBudgetDTO.getRaiseSalaryBonusBudget().compareTo(BigDecimal.ZERO) == 0) {
                    bonusBudgetDTO.setRaiseSalaryBonusBudget(BigDecimal.ZERO);
                }
                if (salaryAdjustPlanDTO.getPlanYear().equals(bonusBudgetDTO.getBudgetYear())) {
                    salaryAdjustPlanDTO.setRaiseSalaryBonusBudget(bonusBudgetDTO.getRaiseSalaryBonusBudget());
                    break;
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
    public DeptSalaryAdjustPlanDTO insertDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        deptSalaryAdjustPlan.setPlanYear(deptSalaryAdjustPlanDTO.getPlanYear());
        deptSalaryAdjustPlan.setSalaryAdjustTotal(deptSalaryAdjustPlanDTO.getSalaryAdjustTotal());
        deptSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
        deptSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        deptSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        deptSalaryAdjustPlanMapper.insertDeptSalaryAdjustPlan(deptSalaryAdjustPlan);
        deptSalaryAdjustPlanDTO.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlan.getDeptSalaryAdjustPlanId());
        return deptSalaryAdjustPlanDTO;
    }

    /**
     * 修改部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    @Override
    public DeptSalaryAdjustPlanDTO editDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO) {
        if (StringUtils.isNull(deptSalaryAdjustPlanDTO)) {
            throw new ServiceException("部门调薪计划表不能为空");
        }
        Integer planYear = deptSalaryAdjustPlanDTO.getPlanYear();
        if (StringUtils.isNull(planYear)) {
            throw new ServiceException("预算年度不能为空");
        }
        DeptSalaryAdjustPlanDTO deptSalaryAdjustPlan = deptSalaryAdjustPlanMapper.selectDeptSalaryAdjustPlanByYear(planYear);
        if (StringUtils.isNull(deptSalaryAdjustPlan)) {
            deptSalaryAdjustPlanDTO = this.insertDeptSalaryAdjustPlan(deptSalaryAdjustPlanDTO);
        } else {
            if (StringUtils.isNull(deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId())) {
                deptSalaryAdjustPlanDTO.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlan.getDeptSalaryAdjustPlanId());
            }
            this.updateDeptSalaryAdjustPlan(deptSalaryAdjustPlanDTO);
        }
        Long deptSalaryAdjustPlanId = deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId();
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSAfter = deptSalaryAdjustPlanDTO.getDeptSalaryAdjustItemDTOS();
        if (StringUtils.isEmpty(deptSalaryAdjustItemDTOSAfter)) {
            return deptSalaryAdjustPlanDTO;
        }
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSBefore = deptSalaryAdjustItemService.selectDeptSalaryAdjustItemByPlanId(deptSalaryAdjustPlanId);
        for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : deptSalaryAdjustItemDTOSAfter) {
            for (DeptSalaryAdjustItemDTO salaryAdjustItemDTO : deptSalaryAdjustItemDTOSBefore) {
                if (salaryAdjustItemDTO.getDepartmentId().equals(deptSalaryAdjustItemDTO.getDepartmentId())) {
                    deptSalaryAdjustItemDTO.setDeptSalaryAdjustPlanId(salaryAdjustItemDTO.getDeptSalaryAdjustPlanId());
                    break;
                }
            }
        }
        operateDeptSalaryAdjustItem(deptSalaryAdjustItemDTOSBefore, deptSalaryAdjustItemDTOSAfter);
        DeptSalaryAdjustPlanDTO salaryAdjustPlanDTO = new DeptSalaryAdjustPlanDTO();
        salaryAdjustPlanDTO.setPlanYear(planYear);
        return salaryAdjustPlanDTO;
    }

    /**
     * 获取上年发薪包
     *
     * @param departmentId 部门ID
     * @return String
     */
    @Override
    public String getLastSalary(Long departmentId) {

        return null;
    }

    /**
     * 处理部门调薪项
     *
     * @param deptSalaryAdjustItemDTOSBefore 库的内容
     * @param deptSalaryAdjustItemDTOSAfter  传来的内容
     */
    private void operateDeptSalaryAdjustItem(List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSBefore, List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDTOSAfter) {
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
            throw new ServiceException(e.toString());
        }
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
        return deptSalaryAdjustPlanMapper.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(deptSalaryAdjustPlanIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
        DeptSalaryAdjustPlan deptSalaryAdjustPlan = new DeptSalaryAdjustPlan();
        deptSalaryAdjustPlan.setDeptSalaryAdjustPlanId(deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId());
        deptSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        return deptSalaryAdjustPlanMapper.logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(deptSalaryAdjustPlan);
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
     * @param deptSalaryAdjustPlanDtos 需要删除的部门调薪计划表主键
     * @return 结果
     */

    @Override
    public int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDtos) {
        List<Long> stringList = new ArrayList<>();
        for (DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO : deptSalaryAdjustPlanDtos) {
            stringList.add(deptSalaryAdjustPlanDTO.getDeptSalaryAdjustPlanId());
        }
        return deptSalaryAdjustPlanMapper.deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(stringList);
    }

    /**
     * 批量新增部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanDtos 部门调薪计划表对象
     */

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

