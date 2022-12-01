package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudget;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudgetDetails;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptBonusBudgetItems;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetAdjustsDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.*;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetAdjustsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetDetailsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.*;
import net.qixiaowei.operate.cloud.service.impl.employee.EmployeeBudgetServiceImpl;
import net.qixiaowei.operate.cloud.service.salary.IDeptBonusBudgetService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;


/**
 * DeptBonusBudgetService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-11-29
 */
@Service
public class DeptBonusBudgetServiceImpl implements IDeptBonusBudgetService {
    @Autowired
    private DeptBonusBudgetMapper deptBonusBudgetMapper;
    @Autowired
    private RemoteOfficialRankSystemService remoteOfficialRankSystemService;
    @Autowired
    private EmployeeBudgetMapper employeeBudgetMapper;
    @Autowired
    private EmployeeBudgetDetailsMapper employeeBudgetDetailsMapper;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private EmployeeBudgetAdjustsMapper employeeBudgetAdjustsMapper;
    @Autowired
    private SalaryPayMapper salaryPayMapper;
    @Autowired
    private OfficialRankEmolumentMapper officialRankEmolumentMapper;
    @Autowired
    private BonusBudgetMapper bonusBudgetMapper;
    @Autowired
    private SalaryItemMapper salaryItemMapper;
    @Autowired
    private DeptBonusBudgetDetailsMapper deptBonusBudgetDetailsMapper;
    @Autowired
    private DeptBonusBudgetItemsMapper deptBonusBudgetItemsMapper;
    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 查询部门奖金包预算表
     *
     * @param deptBonusBudgetId 部门奖金包预算表主键
     * @return 部门奖金包预算表
     */
    @Override
    public DeptBonusBudgetDTO selectDeptBonusBudgetByDeptBonusBudgetId(Long deptBonusBudgetId) {
        //查询部门
        List<DepartmentDTO> departmentAll = this.getDepartmentAll();
        DeptBonusBudgetDTO deptBonusBudgetDTO = deptBonusBudgetMapper.selectDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetId);
        if (StringUtils.isNull(deptBonusBudgetDTO)) {
            throw new ServiceException("数据不存在 请联系管理员！");
        }
        //部门总奖金包
        BigDecimal deptAmountBonus = deptBonusBudgetDTO.getDeptAmountBonus();

        //根据部门奖金预算主表id查询部门奖金预算明细表集合
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS = deptBonusBudgetDetailsMapper.selectDeptBonusBudgetDetailsByDeptBonusBudgetId(deptBonusBudgetId);
        //详情时实时计算部门奖金参考值
        packdetails(departmentAll, deptBonusBudgetDTO, deptAmountBonus, deptBonusBudgetDetailsDTOS);
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            //部门奖金预算明细表id集合
            List<Long> collect = deptBonusBudgetDetailsDTOS.stream().map(DeptBonusBudgetDetailsDTO::getDeptBonusBudgetDetailsId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //根据部门奖金预算明细表id集合批量查询部门奖金预算项目表集合
                List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS = deptBonusBudgetItemsMapper.selectDeptBonusBudgetItemsByDeptBonusBudgetDetailsIds(collect);

                if (StringUtils.isNotEmpty(deptBonusBudgetItemsDTOS)) {

                    //根据部门奖金预算明细表id分组
                    Map<Long, List<DeptBonusBudgetItemsDTO>> listMap = deptBonusBudgetItemsDTOS.parallelStream().collect(Collectors.groupingBy(DeptBonusBudgetItemsDTO::getDeptBonusBudgetDetailsId));
                    for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                        //部门奖总计
                        BigDecimal deptBonusSum = deptBonusBudgetDetailsDTO.getDeptBonusSum();
                        List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS1 = listMap.get(deptBonusBudgetDetailsDTO.getDeptBonusBudgetDetailsId());
                        if (StringUtils.isNotEmpty(deptBonusBudgetItemsDTOS1)){
                            for (DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO : deptBonusBudgetItemsDTOS1) {
                                //奖金金额
                                BigDecimal bonusAmount = new BigDecimal("0");
                                //奖金占比
                                BigDecimal bonusPercentage = deptBonusBudgetItemsDTO.getBonusPercentage();
                                bonusAmount = deptBonusSum.multiply(bonusPercentage);
                                deptBonusBudgetItemsDTO.setBonusAmount(bonusAmount);
                            }
                        }

                        deptBonusBudgetDetailsDTO.setDeptBonusBudgetItemsDTOS(deptBonusBudgetItemsDTOS1);
                    }
                }
            }
        }
        return deptBonusBudgetDTO;
    }

    /**
     * 封装详情时实时计算部门奖金参考值
     *
     * @param departmentAll
     * @param deptBonusBudgetDTO
     * @param deptAmountBonus
     * @param deptBonusBudgetDetailsDTOS
     */
    private void packdetails(List<DepartmentDTO> departmentAll, DeptBonusBudgetDTO deptBonusBudgetDTO, BigDecimal deptAmountBonus, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS) {
        for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
            //部门总计
            BigDecimal deptBonusSum = new BigDecimal("0");
            //部门奖金占比
            BigDecimal deptBonusPercentage = deptBonusBudgetDetailsDTO.getDeptBonusPercentage();
            if (null != deptAmountBonus && deptAmountBonus.compareTo(new BigDecimal("0")) != 0 &&
                    null != deptBonusPercentage && deptBonusPercentage.compareTo(new BigDecimal("0")) != 0) {
                deptBonusSum = deptAmountBonus.multiply(deptBonusPercentage).setScale(2, RoundingMode.CEILING);
            }
            deptBonusBudgetDetailsDTO.setDeptBonusSum(deptBonusSum);
        }
        //部门id 职级平均人数
        Map<Long, Map<String, BigDecimal>> mapEmployeeAve = new HashMap<>();
        //部门id 职级平均薪酬
        Map<Long, Map<String, BigDecimal>> mapPaymentAve = new HashMap<>();
        //根据 部门分组 一级部门带包数
        Map<Long, BigDecimal> topDeptPaymentMap = new HashMap<>();
        //封装每个部门 相同职级的 平均人数
        this.getEmployeeBudgetDTOS(deptBonusBudgetDTO.getBudgetYear(), mapEmployeeAve);
        //封装每个部门 相同职级的 平均薪酬
        this.packDeptOffPaymentAve(deptBonusBudgetDTO.getBudgetYear(), mapPaymentAve);
        //封装一级带包数
        this.packTopDeptPayMent(mapEmployeeAve, mapPaymentAve, topDeptPaymentMap);
        //封装各一级部门组织重要性系数
        Map<Long, BigDecimal> deptImportFactorMap = this.packDeptImportFactor(departmentAll);
        //封装∑（各一级部门带包数×各一级部门组织重要性系数）
        BigDecimal topDeptPaySum = this.packTopDeptPayMentSum(topDeptPaymentMap, deptImportFactorMap);
        //封装部门奖金主表实体类数据
        this.packDeptBonusBudget(deptBonusBudgetDTO.getBudgetYear(), deptBonusBudgetDTO);
        //封装部门奖金参考值
        packDepartmentImportanceFactor(departmentAll, deptBonusBudgetDetailsDTOS, topDeptPaymentMap, topDeptPaySum);
    }

    /**
     * 查询部门奖金包预算表列表
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 部门奖金包预算表
     */
    @Override
    public List<DeptBonusBudgetDTO> selectDeptBonusBudgetList(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        List<DeptBonusBudgetDTO> deptBonusBudgetDTOS = deptBonusBudgetMapper.selectDeptBonusBudgetList(deptBonusBudget);
        if (StringUtils.isNotEmpty(deptBonusBudgetDTOS)) {
            Set<Long> collect = deptBonusBudgetDTOS.stream().map(DeptBonusBudgetDTO::getCreateBy).collect(Collectors.toSet());
            //远程调用人员查询姓名
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(collect, SecurityConstants.INNER);
            List<UserDTO> data = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (DeptBonusBudgetDTO bonusBudgetDTO : deptBonusBudgetDTOS) {
                    for (UserDTO datum : data) {
                        if (bonusBudgetDTO.getCreateBy() == datum.getUserId()) {
                            bonusBudgetDTO.setCreateByName(datum.getEmployeeName());
                        }
                    }
                }
            }
        }
        //模糊查询名称
        String createByName = deptBonusBudgetDTO.getCreateByName();
        if (StringUtils.isNotNull(createByName)) {
            List<DeptBonusBudgetDTO> deptBonusBudgetDTOList = new ArrayList<>();
            //模糊查询
            Pattern pattern = Pattern.compile(deptBonusBudgetDTO.getCreateByName());
            for (DeptBonusBudgetDTO bonusBudgetDTO : deptBonusBudgetDTOS) {
                Matcher matcher = pattern.matcher(bonusBudgetDTO.getCreateByName());
                if (matcher.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                    deptBonusBudgetDTOList.add(bonusBudgetDTO);
                }
            }
            return deptBonusBudgetDTOList;
        }

        return deptBonusBudgetDTOS;
    }

    /**
     * 新增部门奖金包预算表
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
    @Override
    public DeptBonusBudgetDTO insertDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        //部门奖金包预算表
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        //部门奖金预算明细表集合
        List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList = new ArrayList<>();
        //部门奖金预算项目表集合
        List<DeptBonusBudgetItems> deptBonusBudgetItemsList = new ArrayList<>();
        //前台传入部门奖金预算明细表集合数据
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS = deptBonusBudgetDTO.getDeptBonusBudgetDetailsDTOS();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);

        deptBonusBudget.setCreateBy(SecurityUtils.getUserId());
        deptBonusBudget.setCreateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
        deptBonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            deptBonusBudgetMapper.insertDeptBonusBudget(deptBonusBudget);
        } catch (Exception e) {
            throw new ServiceException("插入部门奖金预算失败");
        }

        //批量新增部门奖金预算明细表
        packBatchDeptBonusBudgetDetails(deptBonusBudget, deptBonusBudgetDetailsList, deptBonusBudgetDetailsDTOS);
        //批量新增部门奖金预算项目表
        packBatchDeptBonusBudgetItems(deptBonusBudget, deptBonusBudgetDetailsList, deptBonusBudgetItemsList, deptBonusBudgetDetailsDTOS);
        deptBonusBudgetDTO.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
        return deptBonusBudgetDTO;
    }

    /**
     * 封装批量新增部门奖金预算项目表
     *
     * @param deptBonusBudget
     * @param deptBonusBudgetDetailsList
     * @param deptBonusBudgetItemsList
     * @param deptBonusBudgetDetailsDTOS
     */
    private void packBatchDeptBonusBudgetItems(DeptBonusBudget deptBonusBudget, List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList, List<DeptBonusBudgetItems> deptBonusBudgetItemsList, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS) {
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            for (int i = 0; i < deptBonusBudgetDetailsDTOS.size(); i++) {
                List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS = deptBonusBudgetDetailsDTOS.get(i).getDeptBonusBudgetItemsDTOS();
                if (StringUtils.isNotEmpty(deptBonusBudgetItemsDTOS)) {
                    int sort = 1;
                    for (DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO : deptBonusBudgetItemsDTOS) {
                        DeptBonusBudgetItems deptBonusBudgetItems = new DeptBonusBudgetItems();
                        BeanUtils.copyProperties(deptBonusBudgetItemsDTO, deptBonusBudgetItems);
                        deptBonusBudgetItems.setSort(sort);
                        //部门奖金包预算主表id
                        deptBonusBudgetItems.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
                        //部门奖金预算明细表id
                        deptBonusBudgetItems.setDeptBonusBudgetDetailsId(deptBonusBudgetDetailsList.get(i).getDeptBonusBudgetDetailsId());
                        deptBonusBudgetItems.setCreateBy(SecurityUtils.getUserId());
                        deptBonusBudgetItems.setCreateTime(DateUtils.getNowDate());
                        deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
                        deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
                        deptBonusBudgetItems.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        deptBonusBudgetItemsList.add(deptBonusBudgetItems);
                        sort++;
                    }
                }
            }

        }
        if (StringUtils.isNotEmpty(deptBonusBudgetItemsList)) {
            try {
                deptBonusBudgetItemsMapper.batchDeptBonusBudgetItems(deptBonusBudgetItemsList);
            } catch (Exception e) {
                throw new ServiceException("批量新增部门奖金预算项目表失败");
            }
        }
    }

    /**
     * 批量新增部门奖金预算明细表
     *
     * @param deptBonusBudget
     * @param deptBonusBudgetDetailsList
     * @param deptBonusBudgetDetailsDTOS
     */
    private void packBatchDeptBonusBudgetDetails(DeptBonusBudget deptBonusBudget, List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS) {
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                DeptBonusBudgetDetails deptBonusBudgetDetails = new DeptBonusBudgetDetails();
                BeanUtils.copyProperties(deptBonusBudgetDetailsDTO, deptBonusBudgetDetails);
                //部门奖金包预算主表id
                deptBonusBudgetDetails.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
                deptBonusBudgetDetails.setCreateBy(SecurityUtils.getUserId());
                deptBonusBudgetDetails.setCreateTime(DateUtils.getNowDate());
                deptBonusBudgetDetails.setUpdateTime(DateUtils.getNowDate());
                deptBonusBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
                deptBonusBudgetDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                deptBonusBudgetDetailsList.add(deptBonusBudgetDetails);
            }
        }
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsList)) {
            try {
                deptBonusBudgetDetailsMapper.batchDeptBonusBudgetDetails(deptBonusBudgetDetailsList);
            } catch (Exception e) {
                throw new ServiceException("批量新增部门奖金预算明细表失败");
            }
        }
    }

    /**
     * 批量新增部门奖金预算明细表
     *
     * @param deptBonusBudget
     * @param deptBonusBudgetDetailsList
     * @param deptBonusBudgetDetailsDTOS
     */
    private void packBatchDeptBonusBudgetDetails1(DeptBonusBudget deptBonusBudget, List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS) {
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                DeptBonusBudgetDetails deptBonusBudgetDetails = new DeptBonusBudgetDetails();
                BeanUtils.copyProperties(deptBonusBudgetDetailsDTO, deptBonusBudgetDetails);
                //部门奖金包预算主表id
                deptBonusBudgetDetails.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
                deptBonusBudgetDetails.setCreateBy(SecurityUtils.getUserId());
                deptBonusBudgetDetails.setCreateTime(DateUtils.getNowDate());
                deptBonusBudgetDetails.setUpdateTime(DateUtils.getNowDate());
                deptBonusBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
                deptBonusBudgetDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                deptBonusBudgetDetailsList.add(deptBonusBudgetDetails);
            }
        }
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsList)) {
            try {
                deptBonusBudgetDetailsMapper.batchDeptBonusBudgetDetails(deptBonusBudgetDetailsList);
            } catch (Exception e) {
                throw new ServiceException("批量新增部门奖金预算明细表失败");
            }
        }
    }

    /**
     * 修改部门奖金包预算表
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
    @Override
    public int updateDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        int i = 0;
        //部门奖金包预算表
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        //部门奖金预算明细表集合
        List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList = new ArrayList<>();
        //部门奖金预算项目表集合
        List<DeptBonusBudgetItems> deptBonusBudgetItemsList = new ArrayList<>();
        //前台传入部门奖金预算明细表集合数据
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS = deptBonusBudgetDTO.getDeptBonusBudgetDetailsDTOS();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = deptBonusBudgetMapper.updateDeptBonusBudget(deptBonusBudget);
        } catch (Exception e) {
            throw new ServiceException("插入部门奖金预算失败");
        }

        //批量修改部门奖金预算明细表
        packUpdateDeptBonusBudgetDetailss(deptBonusBudgetDetailsList, deptBonusBudgetDetailsDTOS);
        //批量修改部门奖金项目预算
        packUpdateDeptBonusBudgetItems(deptBonusBudgetItemsList, deptBonusBudgetDetailsDTOS);
        deptBonusBudgetDTO.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
        return i;
    }

    /**
     * 封装批量修改部门奖金预算明细表
     *
     * @param deptBonusBudgetItemsList
     * @param deptBonusBudgetDetailsDTOS
     */
    private void packUpdateDeptBonusBudgetItems(List<DeptBonusBudgetItems> deptBonusBudgetItemsList, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS) {
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS = deptBonusBudgetDetailsDTO.getDeptBonusBudgetItemsDTOS();
                if (StringUtils.isNotEmpty(deptBonusBudgetItemsDTOS)) {
                    for (DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO : deptBonusBudgetItemsDTOS) {
                        DeptBonusBudgetItems deptBonusBudgetItems = new DeptBonusBudgetItems();
                        BeanUtils.copyProperties(deptBonusBudgetItemsDTO, deptBonusBudgetItems);
                        deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
                        deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
                        deptBonusBudgetItemsList.add(deptBonusBudgetItems);
                    }
                }
            }
        }
        if (StringUtils.isNotEmpty(deptBonusBudgetItemsList)) {
            try {
                deptBonusBudgetItemsMapper.updateDeptBonusBudgetItemss(deptBonusBudgetItemsList);
            } catch (Exception e) {
                throw new ServiceException("批量修改部门奖金预算项目表失败");
            }
        }
    }

    /**
     * 封装批量修改部门奖金项目预算
     *
     * @param deptBonusBudgetDetailsList
     * @param deptBonusBudgetDetailsDTOS
     */
    private void packUpdateDeptBonusBudgetDetailss(List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS) {
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                DeptBonusBudgetDetails deptBonusBudgetDetails = new DeptBonusBudgetDetails();
                BeanUtils.copyProperties(deptBonusBudgetDetailsDTO, deptBonusBudgetDetails);
                deptBonusBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
                deptBonusBudgetDetails.setUpdateTime(DateUtils.getNowDate());
                deptBonusBudgetDetailsList.add(deptBonusBudgetDetails);
            }
        }

        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsList)) {
            try {
                deptBonusBudgetDetailsMapper.updateDeptBonusBudgetDetailss(deptBonusBudgetDetailsList);
            } catch (Exception e) {
                throw new ServiceException("批量修改部门奖金预算明细表");
            }
        }
    }

    /**
     * 逻辑批量删除部门奖金包预算表
     *
     * @param deptBonusBudgetIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(List<Long> deptBonusBudgetIds) {
        int i = 0;
        List<DeptBonusBudgetDTO> deptBonusBudgetDTOS = deptBonusBudgetMapper.selectDeptBonusBudgetByDeptBonusBudgetIds(deptBonusBudgetIds);
        if (StringUtils.isEmpty(deptBonusBudgetDTOS)) {
            throw new ServiceException("数据不存在 请联系管理员！");
        }

        try {
            i = deptBonusBudgetMapper.logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(deptBonusBudgetIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("逻辑批量删除部门奖金包预算失败");
        }
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS = deptBonusBudgetDetailsMapper.selectDeptBonusBudgetDetailsByDeptBonusBudgetIds(deptBonusBudgetIds);
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            List<Long> collect = deptBonusBudgetDetailsDTOS.stream().map(DeptBonusBudgetDetailsDTO::getDeptBonusBudgetDetailsId).collect(Collectors.toList());
            try {
                deptBonusBudgetDetailsMapper.logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("逻辑批量删除部门奖金预算明细表失败");
            }
            List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS = deptBonusBudgetItemsMapper.selectDeptBonusBudgetItemsByDeptBonusBudgetDetailsIds(collect);
            if (StringUtils.isNotEmpty(deptBonusBudgetItemsDTOS)) {
                List<Long> collect1 = deptBonusBudgetItemsDTOS.stream().map(DeptBonusBudgetItemsDTO::getDeptBonusBudgetItemsId).collect(Collectors.toList());
                try {
                    deptBonusBudgetItemsMapper.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除部门奖金预算项目表失败");
                }
            }
        }

        return i;
    }

    /**
     * 物理删除部门奖金包预算表信息
     *
     * @param deptBonusBudgetId 部门奖金包预算表主键
     * @return 结果
     */
    @Override
    public int deleteDeptBonusBudgetByDeptBonusBudgetId(Long deptBonusBudgetId) {
        return deptBonusBudgetMapper.deleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetId);
    }

    /**
     * 新增部门奖金包预算预制数据
     * 1查询所有部门
     * 2根据部门id查询人力预算数据
     * 3查询人员表计算未做人力预算的数据
     *
     * @param budgetYear
     * @return
     */
    @Override
    public DeptBonusBudgetDTO addDeptBonusBudgetTamount(int budgetYear) {
        DeptBonusBudgetDTO deptBonusBudgetDTO = new DeptBonusBudgetDTO();
        //查询部门
        List<DepartmentDTO> departmentAll = this.getDepartmentAll();
        //部门奖金预算明细表集合
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS = new ArrayList<>();
        //部门奖金预算项目表集合
        List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS = new ArrayList<>();
        //部门id 职级平均人数
        Map<Long, Map<String, BigDecimal>> mapEmployeeAve = new HashMap<>();
        //部门id 职级平均薪酬
        Map<Long, Map<String, BigDecimal>> mapPaymentAve = new HashMap<>();
        //根据 部门分组 一级部门带包数
        Map<Long, BigDecimal> topDeptPaymentMap = new HashMap<>();
        //封装每个部门 相同职级的 平均人数
        this.getEmployeeBudgetDTOS(budgetYear, mapEmployeeAve);
        //封装每个部门 相同职级的 平均薪酬
        this.packDeptOffPaymentAve(budgetYear, mapPaymentAve);
        //封装一级带包数
        this.packTopDeptPayMent(mapEmployeeAve, mapPaymentAve, topDeptPaymentMap);
        //封装各一级部门组织重要性系数
        Map<Long, BigDecimal> deptImportFactorMap = this.packDeptImportFactor(departmentAll);
        //封装∑（各一级部门带包数×各一级部门组织重要性系数）
        BigDecimal topDeptPaySum = this.packTopDeptPayMentSum(topDeptPaymentMap, deptImportFactorMap);
        //封装部门奖金主表实体类数据
        this.packDeptBonusBudget(budgetYear, deptBonusBudgetDTO);
        //部门总奖金包
        BigDecimal deptAmountBonus = deptBonusBudgetDTO.getDeptAmountBonus();
        //封装部门奖金参考值
        packDepartmentImportanceFactor(departmentAll, deptBonusBudgetDetailsDTOS, topDeptPaymentMap, topDeptPaySum);
        //封装部门奖金总计
        packDeptBonusSum(deptBonusBudgetDetailsDTOS, deptAmountBonus);
        //封装动态奖金类别：从工资条配置中取值，取所有部门级且二级工资项目属于奖金的工资项目 和封装各部门的奖金 占比
        packDeptBonusBudgetItemsDTOS(deptBonusBudgetDetailsDTOS, deptBonusBudgetItemsDTOS);
        deptBonusBudgetDTO.setDeptBonusBudgetDetailsDTOS(deptBonusBudgetDetailsDTOS);
        deptBonusBudgetDTO.setBudgetYear(budgetYear);
        return deptBonusBudgetDTO;
    }

    /**
     * 返回部门奖金预算最大年份
     *
     * @param deptBonusBudgetDTO
     * @return
     */
    @Override
    public DeptBonusBudgetDTO queryDeptBonusBudgetYear(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        DeptBonusBudgetDTO deptBonusBudgetDTO2 = new DeptBonusBudgetDTO();
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        DeptBonusBudgetDTO deptBonusBudgetDTO1 = deptBonusBudgetMapper.queryDeptBonusBudgetYear(deptBonusBudget);

        if (StringUtils.isNull(deptBonusBudgetDTO1)) {
            deptBonusBudgetDTO.setDeptBonusAddFlag(false);
            return deptBonusBudgetDTO2;
        } else {
            deptBonusBudgetDTO2.setDeptBonusAddFlag(true);
            deptBonusBudgetDTO2.setBudgetYear(deptBonusBudgetDTO1.getBudgetYear());
            return deptBonusBudgetDTO2;
        }
    }

    /**
     * 实时查询部门奖金包预算明细参考值数据
     *
     * @param deptBonusBudgetDTO
     * @return
     */
    @Override
    public List<DeptBonusBudgetDetailsDTO> realTimeQueryDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        //查询部门
        List<DepartmentDTO> departmentAll = this.getDepartmentAll();
        //部门总奖金包
        BigDecimal deptAmountBonus = deptBonusBudgetDTO.getDeptAmountBonus();

        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOList = deptBonusBudgetDTO.getDeptBonusBudgetDetailsDTOS();
        //详情时实时计算部门奖金参考值
        packdetails(departmentAll, deptBonusBudgetDTO, deptAmountBonus, deptBonusBudgetDetailsDTOList);
        return deptBonusBudgetDetailsDTOList;
    }

    /**
     * 封装各部门的奖金 占比 和封装动态奖金类别：从工资条配置中取值，取所有部门级且二级工资项目属于奖金的工资项目
     *
     * @param deptBonusBudgetDetailsDTOS
     * @param deptBonusBudgetItemsDTOS
     */
    private void packDeptBonusBudgetItemsDTOS(List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS, List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS) {
        //部门奖金预算预制数据 从工资条配置中取值，取所有部门级且二级工资项目属于奖金的工资项目
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryAddPrefabricate();
        if (StringUtils.isEmpty(salaryItemDTOS)) {
            throw new ServiceException("工资项未配置 请联系管理员！");
        }
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
            DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO = new DeptBonusBudgetItemsDTO();
            deptBonusBudgetItemsDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
            //三级项目是名称
            deptBonusBudgetItemsDTO.setSalaryItemName(salaryItemDTO.getThirdLevelItem());
            deptBonusBudgetItemsDTOS.add(deptBonusBudgetItemsDTO);
        }
        for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
            deptBonusBudgetDetailsDTO.setDeptBonusBudgetItemsDTOS(deptBonusBudgetItemsDTOS);
        }
    }

    /**
     * 封装部门奖金总计
     *
     * @param deptBonusBudgetDetailsDTOS
     * @param deptAmountBonus
     */
    private void packDeptBonusSum(List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS, BigDecimal deptAmountBonus) {
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {

            for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                //部门奖金占比参考值
                BigDecimal deptBonusPercentageReference = deptBonusBudgetDetailsDTO.getDeptBonusPercentageReference();
                if (null != deptAmountBonus && deptAmountBonus.compareTo(new BigDecimal("0")) != 0 &&
                        null != deptBonusPercentageReference && deptBonusPercentageReference.compareTo(new BigDecimal("0")) != 0) {
                    BigDecimal bigDecimal = deptAmountBonus.multiply(deptBonusPercentageReference).setScale(2, RoundingMode.HALF_DOWN);
                    //部门奖金总计
                    deptBonusBudgetDetailsDTO.setDeptBonusSum(bigDecimal);
                }

            }
        }
    }

    /**
     * 封装部门奖金参考值
     *
     * @param departmentAll
     * @param deptBonusBudgetDetailsDTOS
     * @param topDeptPaymentMap
     * @param topDeptPaySum
     */
    private void packDepartmentImportanceFactor(List<DepartmentDTO> departmentAll, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS, Map<Long, BigDecimal> topDeptPaymentMap, BigDecimal topDeptPaySum) {
        //赋值所有部门
        for (DepartmentDTO departmentDTO : departmentAll) {
            DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO = new DeptBonusBudgetDetailsDTO();
            BeanUtils.copyProperties(departmentDTO, deptBonusBudgetDetailsDTO);
            deptBonusBudgetDetailsDTOS.add(deptBonusBudgetDetailsDTO);
        }
        //部门奖金参考值 公式=【当前一级部门带包数×当前一级部门组织重要性系数÷∑（各一级部门带包数×各一级部门组织重要性系数）】×100%
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                //当前一级部门带包数×当前一级部门组织重要性系数
                BigDecimal nowTopDeptPay = new BigDecimal("0");
                for (Long aLong : topDeptPaymentMap.keySet()) {
                    if (deptBonusBudgetDetailsDTO.getDepartmentId() == aLong) {
                        //一级部门带包数
                        BigDecimal topDeptPay = topDeptPaymentMap.get(aLong);
                        //部门组织重要性系数
                        BigDecimal departmentImportanceFactor = deptBonusBudgetDetailsDTO.getDepartmentImportanceFactor();
                        if (null != topDeptPay && topDeptPay.compareTo(new BigDecimal("0")) != 0 &&
                                null != departmentImportanceFactor && departmentImportanceFactor.compareTo(new BigDecimal("0")) != 0) {
                            nowTopDeptPay = topDeptPay.multiply(departmentImportanceFactor).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                        }
                        if (null != nowTopDeptPay && nowTopDeptPay.compareTo(new BigDecimal("0")) != 0 &&
                                null != topDeptPaySum && topDeptPaySum.compareTo(new BigDecimal("0")) != 0) {
                            BigDecimal multiply = nowTopDeptPay.divide(topDeptPaySum, 4, BigDecimal.ROUND_HALF_DOWN).multiply(new BigDecimal("0"));
                            deptBonusBudgetDetailsDTO.setDeptBonusPercentageReference(multiply);
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装各一级部门组织重要性系数
     *
     * @param departmentAll
     */
    private Map<Long, BigDecimal> packDeptImportFactor(List<DepartmentDTO> departmentAll) {
        Map<Long, BigDecimal> deptImportFactorMap = new HashMap<>();
        if (StringUtils.isNotEmpty(departmentAll)) {
            for (DepartmentDTO departmentDTO : departmentAll) {
                deptImportFactorMap.put(departmentDTO.getDepartmentId(), departmentDTO.getDepartmentImportanceFactor());

            }
        }
        return deptImportFactorMap;
    }

    /**
     * 封装∑（各一级部门带包数×各一级部门组织重要性系数）
     *
     * @param topDeptPaymentMap
     * @param deptImportFactorMap
     * @return
     */
    private BigDecimal packTopDeptPayMentSum(Map<Long, BigDecimal> topDeptPaymentMap, Map<Long, BigDecimal> deptImportFactorMap) {
        BigDecimal topDeptPaySum = new BigDecimal("0");
        if (StringUtils.isNotEmpty(topDeptPaymentMap) && StringUtils.isNotEmpty(deptImportFactorMap)) {
            for (Long aLong : topDeptPaymentMap.keySet()) {
                for (Long aLong1 : deptImportFactorMap.keySet()) {
                    if (aLong == aLong1) {
                        //一级部门带包数
                        BigDecimal topDeptPay = topDeptPaymentMap.get(aLong);
                        //部门组织重要性系数
                        BigDecimal departmentImportanceFactor = deptImportFactorMap.get(aLong1);
                        if (null != topDeptPay && topDeptPay.compareTo(new BigDecimal("0")) != 0 &&
                                null != departmentImportanceFactor && departmentImportanceFactor.compareTo(new BigDecimal("0")) != 0) {
                            topDeptPaySum = topDeptPay.multiply(departmentImportanceFactor).setScale(2, RoundingMode.HALF_DOWN);
                        }

                    }
                }

            }
        }
        return topDeptPaySum;
    }

    /**
     * 封装部门奖金主表实体类数据
     *
     * @param budgetYear
     * @param deptBonusBudgetDTO
     */
    private void packDeptBonusBudget(int budgetYear, DeptBonusBudgetDTO deptBonusBudgetDTO) {
        BonusBudgetDTO bonusBudgetDTO = bonusBudgetMapper.selectBonusBudgetByBudgetYear(budgetYear);
        if (StringUtils.isNull(bonusBudgetDTO)) {
            deptBonusBudgetDTO.setDeptBonusAddFlag(false);
        } else {
            //总奖金包预算
            BigDecimal amountBonusBudget = bonusBudgetDTO.getAmountBonusBudget();
            if (null != amountBonusBudget) {
                //总奖金包预算
                deptBonusBudgetDTO.setAmountBonusBudget(amountBonusBudget);
            }
            if (null == deptBonusBudgetDTO.getStrategyAwardPercentage()) {
                deptBonusBudgetDTO.setStrategyAwardPercentage(new BigDecimal("5"));
            }

            //年份
            deptBonusBudgetDTO.setBudgetYear(budgetYear);
            //公式=总奖金包预算×战略奖比例
            if (null != amountBonusBudget) {
                BigDecimal strategyAwardAmount = new BigDecimal("0");
                if (null == deptBonusBudgetDTO.getStrategyAwardPercentage()) {
                    //战略奖金额
                    strategyAwardAmount = amountBonusBudget.multiply(new BigDecimal("0.05")).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                } else {
                    //战略奖金额
                    strategyAwardAmount = amountBonusBudget.multiply(deptBonusBudgetDTO.getStrategyAwardPercentage().divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                }
                ////战略奖金额
                deptBonusBudgetDTO.setStrategyAwardAmount(strategyAwardAmount);
                // 部门总奖金包 公式=总奖金包预算-战略奖金额
                BigDecimal deptAmountBonus = amountBonusBudget.subtract(strategyAwardAmount).setScale(2, BigDecimal.ROUND_HALF_DOWN);
                deptBonusBudgetDTO.setDeptAmountBonus(deptAmountBonus);

            }

            deptBonusBudgetDTO.setDeptBonusAddFlag(true);
        }
    }

    /**
     * 封装一级带包数
     *
     * @param mapEmployeeAve
     * @param mapPaymentAve
     * @param topDeptPaymentMap
     */
    private void packTopDeptPayMent(Map<Long, Map<String, BigDecimal>> mapEmployeeAve, Map<Long, Map<String, BigDecimal>> mapPaymentAve, Map<Long, BigDecimal> topDeptPaymentMap) {
        if (StringUtils.isNotEmpty(mapEmployeeAve) && StringUtils.isNotEmpty(mapPaymentAve)) {
            for (Long aLong : mapEmployeeAve.keySet()) {
                for (Long aLong1 : mapPaymentAve.keySet()) {
                    //相同部门
                    if (aLong == aLong1) {
                        Map<String, BigDecimal> employeeAveBigDecimalMap = mapEmployeeAve.get(aLong);
                        Map<String, BigDecimal> paymentAveBigDecimalMap = mapPaymentAve.get(aLong1);
                        for (String s : employeeAveBigDecimalMap.keySet()) {
                            for (String s1 : paymentAveBigDecimalMap.keySet()) {
                                //相同职级
                                if (StringUtils.equals(s, s1)) {
                                    //职级的平均人数
                                    BigDecimal bigDecimal = employeeAveBigDecimalMap.get(s);
                                    // //职级的平均薪资
                                    BigDecimal bigDecimal1 = paymentAveBigDecimalMap.get(s1);
                                    if (null != bigDecimal && bigDecimal.compareTo(new BigDecimal("0")) > 0 &&
                                            null != bigDecimal1 && bigDecimal1.compareTo(new BigDecimal("0")) > 0) {
                                        topDeptPaymentMap.put(aLong, bigDecimal.multiply(bigDecimal1));
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装某职级的平均薪酬
     *
     * @param budgetYear
     * @param mapPaymentAve
     */
    private void packDeptOffPaymentAve(int budgetYear, Map<Long, Map<String, BigDecimal>> mapPaymentAve) {
        //封装远程调用部门id集合
        List<Long> departmentIdAll = getDepartmentIdAll();
        //远程调用部门查询 相同部门 相同职级的人数
        R<List<EmployeeDTO>> listR = remoteEmployeeService.selectDepartmentAndOfficialRankSystem(departmentIdAll, SecurityConstants.INNER);
        List<EmployeeDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {

            //员工部门id集合
            List<Long> employeeDepartmentIdS = data.stream().map(EmployeeDTO::getEmployeeDepartmentId).collect(Collectors.toList());

            //查询工资表数据 某职级的平均薪酬：从月度工资管理取数，取数范围为倒推12个月的数据（年工资）。
            if (StringUtils.isNotEmpty(employeeDepartmentIdS)) {

                //根据部门id查询人力预算信息
                List<EmployeeBudgetDTO> employeeBudgetDTOList = employeeBudgetMapper.selectEmployeeBudgetByOfficialRankSystemIds(employeeDepartmentIdS, budgetYear);
                if (StringUtils.isNotEmpty(employeeBudgetDTOList)) {

                    //根据部门id分组
                    Map<Long, List<EmployeeBudgetDTO>> departmentMap = employeeBudgetDTOList.parallelStream().collect(Collectors.groupingBy(EmployeeBudgetDTO::getDepartmentId));
                    if (StringUtils.isNotEmpty(departmentMap)) {
                        //每个部门的职级体系平均人数
                        for (Long aLong : departmentMap.keySet()) {
                            List<EmployeeBudgetDTO> employeeBudgetDTOList1 = departmentMap.get(aLong);
                            if (StringUtils.isNotEmpty(employeeBudgetDTOList1)) {
                                //人力预算表id集合
                                List<Long> employeeBudgetIds = employeeBudgetDTOList1.stream().map(EmployeeBudgetDTO::getEmployeeBudgetId).collect(Collectors.toList());
                                //人力预算详情表集合
                                List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.selectEmployeeBudgetDetailsByEmployeeBudgetIds(employeeBudgetIds);
                                //赋值职级名称
                                packRankName(employeeBudgetDetailsDTOS);

                                for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                                    for (EmployeeDTO datum : data) {
                                        Map<String, BigDecimal> map = new HashMap<>();
                                        // 已有的人员职级   做了人力预算调控 从工资发薪表取数
                                        if (employeeBudgetDetailsDTO.getOfficialRankName() == datum.getEmployeeRankName() && employeeBudgetDetailsDTO.getDepartmentId() == datum.getEmployeeDepartmentId()) {
                                            //总薪酬包
                                            BigDecimal payAmountSum = new BigDecimal("0");
                                            List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectDeptBonusBudgetPay(datum.getEmployeeId(), budgetYear);
                                            if (StringUtils.isNotEmpty(salaryPayDTOS)) {
                                                //sterm流求和 总薪酬包 公式= 工资+津贴+福利+奖金
                                                BigDecimal reduce = salaryPayDTOS.stream().map(SalaryPayDTO::getPaymentBonus).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                                                //公式=某职级的总薪酬包（包含总工资包和总奖金包）÷某职级的人数。
                                                payAmountSum = reduce.divide(new BigDecimal(String.valueOf(salaryPayDTOS.size())), 4, BigDecimal.ROUND_HALF_DOWN);
                                            }
                                            map.put(datum.getEmployeeRankName(), payAmountSum);
                                            mapPaymentAve.put(aLong, map);
                                        } // 职级确认薪酬取中位值
                                        else {
                                            //根据职级体系id查询职级确认薪酬
                                            List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = officialRankEmolumentMapper.selectOfficialRankEmolumentBySystemId(employeeBudgetDetailsDTO.getOfficialRankSystemId());
                                            //远程调用查询职级
                                            R<OfficialRankSystemDTO> officialRankSystemDTOR = remoteOfficialRankSystemService.selectById(employeeBudgetDetailsDTO.getOfficialRankSystemId(), SecurityConstants.INNER);
                                            OfficialRankSystemDTO data1 = officialRankSystemDTOR.getData();
                                            if (StringUtils.isNull(data1)) {
                                                throw new ServiceException("职级不存在 请联系管理员！");
                                            }
                                            if (StringUtils.isNotEmpty(officialRankEmolumentDTOList)) {
                                                for (OfficialRankEmolumentDTO officialRankEmolumentDTO : officialRankEmolumentDTOList) {
                                                    map.put(data1.getRankPrefixCode() + data1.getOfficialRank(), officialRankEmolumentDTO.getSalaryMedian());
                                                    mapPaymentAve.put(aLong, map);
                                                }
                                            }

                                        }
                                    }
                                }


                            }
                        }
                    }
                    //已做人力预算的部门
                    List<Long> collect = employeeBudgetDTOList.stream().map(EmployeeBudgetDTO::getDepartmentId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(collect)) {
                        //求除未做人力预算的集合
                        departmentIdAll.removeAll(collect);

                        R<List<EmployeeDTO>> listR1 = remoteEmployeeService.selectDepartmentAndOfficialRankSystem(departmentIdAll, SecurityConstants.INNER);
                        List<EmployeeDTO> data1 = listR1.getData();
                        if (StringUtils.isNotEmpty(data1)) {
                            //根据部门id分组
                            Map<Long, List<EmployeeDTO>> employeeDepartmentMap = data1.parallelStream().collect(Collectors.groupingBy(EmployeeDTO::getEmployeeDepartmentId));
                            for (Long aLong : employeeDepartmentMap.keySet()) {
                                List<EmployeeDTO> employeeDTOList = employeeDepartmentMap.get(aLong);
                                //职级体系id集合
                                List<Long> collect1 = employeeDTOList.stream().map(EmployeeDTO::getOfficialRankSystemId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                                //远程调用查询职级
                                R<List<OfficialRankSystemDTO>> listR2 = remoteOfficialRankSystemService.selectByIds(collect1, SecurityConstants.INNER);
                                //根据职级体系id查询职级确认薪酬
                                List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList = officialRankEmolumentMapper.selectOfficialRankEmolumentBySystemIds(collect1);
                                List<OfficialRankSystemDTO> data2 = listR2.getData();
                                if (StringUtils.isNotEmpty(data2) && StringUtils.isNotEmpty(officialRankEmolumentDTOList)) {
                                    for (OfficialRankSystemDTO officialRankSystemDTO : data2) {
                                        Map<String, BigDecimal> map = new HashMap<>();
                                        for (OfficialRankEmolumentDTO officialRankEmolumentDTO : officialRankEmolumentDTOList) {
                                            if (officialRankSystemDTO.getOfficialRankSystemId() == officialRankEmolumentDTO.getOfficialRankSystemId()) {
                                                //起始级别
                                                Integer rankStart = officialRankSystemDTO.getRankStart();
                                                //终止级别
                                                Integer rankEnd = officialRankSystemDTO.getRankEnd();
                                                for (Integer i = rankStart; i <= rankEnd; i++) {
                                                    map.put(officialRankSystemDTO.getRankPrefixCode() + officialRankSystemDTO.getOfficialRank(), officialRankEmolumentDTO.getSalaryMedian());
                                                    mapPaymentAve.put(aLong, map);
                                                }

                                            }
                                        }
                                    }
                                }
                            }

                        }

                    }
                }
            }
        }
    }

    /**
     * 封装每个部门 相同职级的 平均人数
     *
     * @param budgetYear
     * @param mapEmployeeAve
     */
    private void getEmployeeBudgetDTOS(int budgetYear, Map<Long, Map<String, BigDecimal>> mapEmployeeAve) {
        //封装远程调用部门id集合
        List<Long> departmentIdAll = getDepartmentIdAll();
        //根据部门id查询人力预算信息
        List<EmployeeBudgetDTO> employeeBudgetDTOList = employeeBudgetMapper.selectEmployeeBudgetByOfficialRankSystemIds(departmentIdAll, budgetYear);
        if (StringUtils.isNotEmpty(employeeBudgetDTOList)) {
            //根据部门id分组
            Map<Long, List<EmployeeBudgetDTO>> departmentMap = employeeBudgetDTOList.parallelStream().collect(Collectors.groupingBy(EmployeeBudgetDTO::getDepartmentId));
            if (StringUtils.isNotEmpty(departmentMap)) {
                //每个部门的职级体系平均人数
                for (Long aLong : departmentMap.keySet()) {
                    List<EmployeeBudgetDTO> employeeBudgetDTOList1 = departmentMap.get(aLong);
                    if (StringUtils.isNotEmpty(employeeBudgetDTOList1)) {
                        //人力预算表id集合
                        List<Long> employeeBudgetIds = employeeBudgetDTOList1.stream().map(EmployeeBudgetDTO::getEmployeeBudgetId).collect(Collectors.toList());
                        //人力预算详情表集合
                        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.selectEmployeeBudgetDetailsByEmployeeBudgetIds(employeeBudgetIds);
                        //人力预算详情id
                        List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getEmployeeBudgetDetailsId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(collect)) {
                            //人力预算调整表
                            List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetAdjustsMapper.selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsIds(collect);
                            //公共方法
                            EmployeeBudgetServiceImpl.packEmployeeBudgetDetailsNum(employeeBudgetDetailsDTOS, employeeBudgetAdjustsDTOS);
                        }
                        //赋值职级名称
                        packRankName(employeeBudgetDetailsDTOS);
                        for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                            Map<String, BigDecimal> map = new HashMap<>();
                            map.put(employeeBudgetDetailsDTO.getOfficialRankName(), employeeBudgetDetailsDTO.getAnnualAverageNum());
                            //相同部门的 职级体系平均人数
                            mapEmployeeAve.put(aLong, map);
                        }
                    }
                }
            }
            //已做人力预算的部门
            List<Long> collect = employeeBudgetDTOList.stream().map(EmployeeBudgetDTO::getDepartmentId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //求除未做人力预算的集合
                departmentIdAll.removeAll(collect);
                //远程调用部门查询 相同部门 相同职级的人数
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectDepartmentAndOfficialRankSystem(departmentIdAll, SecurityConstants.INNER);
                List<EmployeeDTO> data1 = listR.getData();
                if (StringUtils.isNotEmpty(data1)) {
                    //根据部门id分组
                    Map<Long, List<EmployeeDTO>> collect1 = data1.parallelStream().collect(Collectors.groupingBy(EmployeeDTO::getEmployeeDepartmentId));
                    if (StringUtils.isNotEmpty(collect1)) {
                        for (Long aLong : collect1.keySet()) {
                            List<EmployeeDTO> employeeDTOList = collect1.get(aLong);
                            if (StringUtils.isNotEmpty(employeeDTOList)) {
                                for (EmployeeDTO employeeDTO : employeeDTOList) {
                                    Map<String, BigDecimal> map = new HashMap<>();
                                    //职级 人数
                                    map.put(employeeDTO.getEmployeeRankName(), employeeDTO.getAnnualAverageNum());
                                    mapEmployeeAve.put(aLong, map);
                                }
                            }

                        }

                    }

                }
            }

        }
    }

    /**
     * 赋值职级名称
     *
     * @param employeeBudgetDetailsDTOS
     */
    private void packRankName(List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS) {
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
    }

    /**
     * 封装远程调用部门id集合
     *
     * @return
     */
    private List<Long> getDepartmentIdAll() {
        //远程查询所有部门
        R<List<DepartmentDTO>> allDepartment = remoteDepartmentService.getAll(SecurityConstants.INNER);
        List<DepartmentDTO> data = allDepartment.getData();
        if (StringUtils.isEmpty(data)) {
            throw new ServiceException("无部门数据 请联系管理员！");
        }
        //部门id集合
        List<Long> departmentIdAll = data.stream().map(DepartmentDTO::getDepartmentId).distinct().collect(Collectors.toList());
        return departmentIdAll;
    }

    /**
     * 返回部门集合
     *
     * @return
     */
    private List<DepartmentDTO> getDepartmentAll() {
        //远程查询所有部门
        R<List<DepartmentDTO>> allDepartment = remoteDepartmentService.getAll(SecurityConstants.INNER);
        List<DepartmentDTO> data = allDepartment.getData();
        if (StringUtils.isEmpty(data)) {
            throw new ServiceException("无部门数据 请联系管理员！");
        }
        return data;
    }

    /**
     * 逻辑删除部门奖金包预算表信息
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
    @Override
    public int logicDeleteDeptBonusBudgetByDeptBonusBudgetId(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        int i = 0;
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        deptBonusBudget.setDeptBonusBudgetId(deptBonusBudgetDTO.getDeptBonusBudgetId());
        deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
        //部门奖金预算主表主键
        Long deptBonusBudgetId = deptBonusBudget.getDeptBonusBudgetId();

        DeptBonusBudgetDTO deptBonusBudgetDTO1 = deptBonusBudgetMapper.selectDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetId);
        if (StringUtils.isNull(deptBonusBudgetDTO1)) {
            throw new ServiceException("部门奖金预算数据不存在 请联系管理员！");
        }

        try {
            i = deptBonusBudgetMapper.logicDeleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudget);
        } catch (Exception e) {
            throw new ServiceException("删除部门奖金预算失败");
        }
        //部门奖金预算明细表集合
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS = deptBonusBudgetDetailsMapper.selectDeptBonusBudgetDetailsByDeptBonusBudgetId(deptBonusBudgetId);
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            List<Long> collect = deptBonusBudgetDetailsDTOS.stream().map(DeptBonusBudgetDetailsDTO::getDeptBonusBudgetDetailsId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    deptBonusBudgetDetailsMapper.logicDeleteDeptBonusBudgetDetailsByDeptBonusBudgetDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除部门奖金预算明细表失败");
                }

                List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS = deptBonusBudgetItemsMapper.selectDeptBonusBudgetItemsByDeptBonusBudgetDetailsIds(collect);
                if (StringUtils.isNotEmpty(deptBonusBudgetItemsDTOS)) {
                    List<Long> collect1 = deptBonusBudgetItemsDTOS.stream().map(DeptBonusBudgetItemsDTO::getDeptBonusBudgetItemsId).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(collect1)) {
                        try {
                            deptBonusBudgetItemsMapper.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
                        } catch (Exception e) {
                            throw new ServiceException("逻辑批量删除部门奖金预算项目表失败");
                        }
                    }

                }
            }
        }


        return i;
    }

    /**
     * 物理删除部门奖金包预算表信息
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */

    @Override
    public int deleteDeptBonusBudgetByDeptBonusBudgetId(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        return deptBonusBudgetMapper.deleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
    }

    /**
     * 物理批量删除部门奖金包预算表
     *
     * @param deptBonusBudgetDtos 需要删除的部门奖金包预算表主键
     * @return 结果
     */

    @Override
    public int deleteDeptBonusBudgetByDeptBonusBudgetIds(List<DeptBonusBudgetDTO> deptBonusBudgetDtos) {
        List<Long> stringList = new ArrayList();
        for (DeptBonusBudgetDTO deptBonusBudgetDTO : deptBonusBudgetDtos) {
            stringList.add(deptBonusBudgetDTO.getDeptBonusBudgetId());
        }
        return deptBonusBudgetMapper.deleteDeptBonusBudgetByDeptBonusBudgetIds(stringList);
    }

    /**
     * 批量新增部门奖金包预算表信息
     *
     * @param deptBonusBudgetDtos 部门奖金包预算表对象
     */

    public int insertDeptBonusBudgets(List<DeptBonusBudgetDTO> deptBonusBudgetDtos) {
        List<DeptBonusBudget> deptBonusBudgetList = new ArrayList();

        for (DeptBonusBudgetDTO deptBonusBudgetDTO : deptBonusBudgetDtos) {
            DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
            BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
            deptBonusBudget.setCreateBy(SecurityUtils.getUserId());
            deptBonusBudget.setCreateTime(DateUtils.getNowDate());
            deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
            deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
            deptBonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            deptBonusBudgetList.add(deptBonusBudget);
        }
        return deptBonusBudgetMapper.batchDeptBonusBudget(deptBonusBudgetList);
    }

    /**
     * 批量修改部门奖金包预算表信息
     *
     * @param deptBonusBudgetDtos 部门奖金包预算表对象
     */

    public int updateDeptBonusBudgets(List<DeptBonusBudgetDTO> deptBonusBudgetDtos) {
        List<DeptBonusBudget> deptBonusBudgetList = new ArrayList();

        for (DeptBonusBudgetDTO deptBonusBudgetDTO : deptBonusBudgetDtos) {
            DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
            BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
            deptBonusBudget.setCreateBy(SecurityUtils.getUserId());
            deptBonusBudget.setCreateTime(DateUtils.getNowDate());
            deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
            deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
            deptBonusBudgetList.add(deptBonusBudget);
        }
        return deptBonusBudgetMapper.updateDeptBonusBudgets(deptBonusBudgetList);
    }
}

