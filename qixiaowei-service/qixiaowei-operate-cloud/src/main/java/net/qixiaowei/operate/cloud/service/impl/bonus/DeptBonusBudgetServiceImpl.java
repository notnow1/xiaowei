package net.qixiaowei.operate.cloud.service.impl.bonus;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.bonus.DeptBonusBudget;
import net.qixiaowei.operate.cloud.api.domain.bonus.DeptBonusBudgetDetails;
import net.qixiaowei.operate.cloud.api.domain.bonus.DeptBonusBudgetItems;
import net.qixiaowei.operate.cloud.api.dto.bonus.*;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.*;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.DeptBonusBudgetDetailsMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.DeptBonusBudgetItemsMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.DeptBonusBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetAdjustsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetDetailsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.*;
import net.qixiaowei.operate.cloud.service.bonus.IDeptBonusBudgetService;
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
import org.springframework.transaction.annotation.Transactional;

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
        List<DeptBonusCompanyDTO> deptBonusCompanyDTOList = deptBonusBudgetItemsMapper.selectCompanyBonusBudgetDetailsByCompanyBonusBudgetId(deptBonusBudgetId);

        //封装部门奖金主表实体类数据
        this.packDeptBonusBudget(deptBonusBudgetDTO.getBudgetYear(), deptBonusBudgetDTO);
        //公司级奖金包
        BigDecimal strategyAwardAmount = deptBonusBudgetDTO.getStrategyAwardAmount();
        if (StringUtils.isNotEmpty(deptBonusCompanyDTOList)){
            for (DeptBonusCompanyDTO deptBonusCompanyDTO : deptBonusCompanyDTOList) {
                //公司奖金占比
                BigDecimal bonusCompanyPercentage = deptBonusCompanyDTO.getBonusCompanyPercentage();
                //公司级奖金包
                BigDecimal bonusCompanyAmount = new BigDecimal("0");
                if (null != strategyAwardAmount && strategyAwardAmount.compareTo(new BigDecimal("0")) != 0
                    &&null != bonusCompanyPercentage && bonusCompanyPercentage.compareTo(new BigDecimal("0")) != 0){
                    bonusCompanyAmount=strategyAwardAmount.multiply(bonusCompanyPercentage.divide(new BigDecimal("100"),10, BigDecimal.ROUND_HALF_UP));
                }
                deptBonusCompanyDTO.setBonusCompanyAmount(bonusCompanyAmount);
            }
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
                        if (StringUtils.isNotEmpty(deptBonusBudgetItemsDTOS1)) {
                            for (DeptBonusBudgetItemsDTO deptBonusBudgetItemsDTO : deptBonusBudgetItemsDTOS1) {
                                //奖金金额
                                BigDecimal bonusAmount = new BigDecimal("0");
                                //奖金占比
                                BigDecimal bonusPercentage = deptBonusBudgetItemsDTO.getBonusPercentage();
                                if (null != bonusPercentage){
                                    bonusAmount = deptBonusSum.multiply(bonusPercentage.divide(new BigDecimal("100"),10, BigDecimal.ROUND_HALF_UP));
                                }
                                deptBonusBudgetItemsDTO.setBonusAmount(bonusAmount);
                            }
                        }

                        deptBonusBudgetDetailsDTO.setDeptBonusBudgetItemsDTOS(deptBonusBudgetItemsDTOS1);
                    }
                }
            }
        }
        //赋值组织名称
        this.packDeptName(deptBonusBudgetDetailsDTOS);
        deptBonusBudgetDTO.setDeptBonusBudgetDetailsDTOS(deptBonusBudgetDetailsDTOS);
        deptBonusBudgetDTO.setDeptBonusCompanyDTOS(deptBonusCompanyDTOList);
        return deptBonusBudgetDTO;
    }

    /**
     * 封装赋值组织名称
     *
     * @param deptBonusBudgetDetailsDTOS
     */
    private void packDeptName(List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS) {
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            List<Long> collect = deptBonusBudgetDetailsDTOS.stream().map(DeptBonusBudgetDetailsDTO::getDepartmentId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //远程调用组织信息
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (deptBonusBudgetDetailsDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                deptBonusBudgetDetailsDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }
        }

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
                deptBonusSum = deptAmountBonus.multiply(deptBonusPercentage.divide(new BigDecimal("100"),10, BigDecimal.ROUND_HALF_UP)).setScale(2, RoundingMode.CEILING);
            }
            deptBonusBudgetDetailsDTO.setDeptBonusSum(deptBonusSum);
        }

        //根据 部门分组 一级部门带包数
        Map<Long, BigDecimal> topDeptPaymentMap = new HashMap<>();

        //封装一级带包数
        this.packTopDeptPayMent(topDeptPaymentMap, deptBonusBudgetDTO.getBudgetYear());
        //封装各一级部门组织重要性系数
        Map<Long, BigDecimal> deptImportFactorMap = new HashMap<>();
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)){
            for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                deptImportFactorMap.put(deptBonusBudgetDetailsDTO.getDepartmentId(),deptBonusBudgetDetailsDTO.getDepartmentImportanceFactor());
            }
        }
        //封装∑（各一级部门带包数×各一级部门组织重要性系数）
        BigDecimal topDeptPaySum = this.packTopDeptPayMentSum(topDeptPaymentMap, deptImportFactorMap);

        //封装部门奖金参考值
        packDepartmentImportanceFactor(departmentAll, deptBonusBudgetDetailsDTOS, topDeptPaymentMap, topDeptPaySum);
    }

    /**
     * 查询部门奖金包预算表列表
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 部门奖金包预算表
     */
    @DataScope(businessAlias = "dbb")
    @Override
    public List<DeptBonusBudgetDTO> selectDeptBonusBudgetList(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        List<String> createBys = new ArrayList<>();
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        if (StringUtils.isNotNull(deptBonusBudgetDTO.getCreateByName())) {
            UserDTO userDTO = new UserDTO();
            userDTO.setEmployeeName(deptBonusBudgetDTO.getCreateByName());
            R<List<UserDTO>> userList = remoteUserService.remoteSelectUserList(userDTO, SecurityConstants.INNER);
            List<UserDTO> userListData = userList.getData();
            List<Long> userIds = userListData.stream().filter(f -> f.getUserId() != null).map(UserDTO::getUserId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(userIds)) {
                userIds.forEach(e -> {
                    createBys.add(String.valueOf(e));
                });
            } else {
                createBys.add("");
            }
        }
        deptBonusBudget.setCreateBys(createBys);
        List<DeptBonusBudgetDTO> deptBonusBudgetDTOS = deptBonusBudgetMapper.selectDeptBonusBudgetList(deptBonusBudget);
        //模糊查询名称
        String createByName = deptBonusBudgetDTO.getCreateByName();
        if (StringUtils.isNotNull(createByName)) {
            List<DeptBonusBudgetDTO> deptBonusBudgetDTOList = new ArrayList<>();
            //模糊查询
            Pattern pattern = Pattern.compile(deptBonusBudgetDTO.getCreateByName());
            for (DeptBonusBudgetDTO bonusBudgetDTO : deptBonusBudgetDTOS) {
                String createByName1 = bonusBudgetDTO.getCreateByName();
                if (StringUtils.isNotBlank(createByName1)){
                    Matcher matcher = pattern.matcher(createByName1);
                    if (matcher.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                        deptBonusBudgetDTOList.add(bonusBudgetDTO);
                    }
                }else {
                    return new ArrayList<>();
                }
            }
            return deptBonusBudgetDTOList;
        }
        this.handleResult(deptBonusBudgetDTOS);
        return deptBonusBudgetDTOS;
    }

    /**
     * 新增部门奖金包预算表
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
    @Override
    @Transactional
    public DeptBonusBudgetDTO insertDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO) {

        //部门奖金包预算表
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        //部门奖金预算明细表集合
        List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList = new ArrayList<>();
        //部门奖金预算项目表集合
        List<DeptBonusBudgetItems> deptBonusBudgetItemsList = new ArrayList<>();
        //前台传入部门奖金预算明细表集合数据
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS = deptBonusBudgetDTO.getDeptBonusBudgetDetailsDTOS();
        //前台传入公司奖金预算明细表集合数据
        List<DeptBonusCompanyDTO> deptBonusCompanyDTOS = deptBonusBudgetDTO.getDeptBonusCompanyDTOS();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        DeptBonusBudgetDTO deptBonusBudgetDTO1 = deptBonusBudgetMapper.selectDeptBonusBudgetBybudgetYear(deptBonusBudget.getBudgetYear());
        if (StringUtils.isNotNull(deptBonusBudgetDTO1)){
            throw new ServiceException("已存在"+deptBonusBudget.getBudgetYear()+"年数据 无需重复添加！");
        }
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
        packBatchDeptBonusBudgetItems(deptBonusBudget, deptBonusBudgetDetailsList, deptBonusBudgetItemsList, deptBonusBudgetDetailsDTOS,deptBonusCompanyDTOS);
        deptBonusBudgetDTO.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
        return deptBonusBudgetDTO;
    }

    /**
     * 封装批量新增部门奖金预算项目表
     *  @param deptBonusBudget
     * @param deptBonusBudgetDetailsList
     * @param deptBonusBudgetItemsList
     * @param deptBonusBudgetDetailsDTOS
     * @param deptBonusCompanyDTOS
     */
    private void packBatchDeptBonusBudgetItems(DeptBonusBudget deptBonusBudget, List<DeptBonusBudgetDetails> deptBonusBudgetDetailsList, List<DeptBonusBudgetItems> deptBonusBudgetItemsList, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS, List<DeptBonusCompanyDTO> deptBonusCompanyDTOS) {
        if (StringUtils.isNotEmpty(deptBonusCompanyDTOS)){
            for (int i = 0; i < deptBonusCompanyDTOS.size(); i++) {
                DeptBonusBudgetItems deptBonusBudgetItems = new DeptBonusBudgetItems();
                //公司奖金占比
                deptBonusBudgetItems.setBonusPercentage(deptBonusCompanyDTOS.get(i).getBonusCompanyPercentage());
                deptBonusBudgetItems.setSort(i+1);
                //部门奖金包预算主表id
                deptBonusBudgetItems.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
                //部门奖金预算明细表id
                deptBonusBudgetItems.setDeptBonusBudgetDetailsId(0L);
                deptBonusBudgetItems.setCreateBy(SecurityUtils.getUserId());
                deptBonusBudgetItems.setCreateTime(DateUtils.getNowDate());
                deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
                deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
                deptBonusBudgetItems.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                deptBonusBudgetItemsList.add(deptBonusBudgetItems);
            }
        }
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
    @Transactional
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
        //前台传入公司级奖金预算明细表集合数据
        List<DeptBonusCompanyDTO> deptBonusCompanyDTOS = deptBonusBudgetDTO.getDeptBonusCompanyDTOS();

        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = deptBonusBudgetMapper.updateDeptBonusBudget(deptBonusBudget);
        } catch (Exception e) {
            throw new ServiceException("插入部门奖金预算失败");
        }

        //批量修改部门奖金预算明细表
        this.packUpdateDeptBonusBudgetDetailss(deptBonusBudgetDetailsList, deptBonusBudgetDetailsDTOS);
        //批量新增修改公司奖金项目预算
        this.packDeptBonusCompanyDTOS(deptBonusBudget, deptBonusCompanyDTOS);
        //批量修改部门奖金项目预算
        this.packUpdateDeptBonusBudgetItems(deptBonusBudgetItemsList, deptBonusBudgetDetailsDTOS);

        deptBonusBudgetDTO.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
        return i;
    }

    /**
     *批量新增修改公司奖金项目预算
     * @param deptBonusBudget
     * @param deptBonusCompanyDTOS
     */
    private void packDeptBonusCompanyDTOS(DeptBonusBudget deptBonusBudget, List<DeptBonusCompanyDTO> deptBonusCompanyDTOS) {
        //公司奖金预算项目表新增集合
        List<DeptBonusBudgetItems> deptBonusBudgetItemsAddList = new ArrayList<>();
        //公司奖金预算项目表修改集合
        List<DeptBonusBudgetItems> deptBonusBudgetItemsUpdateList = new ArrayList<>();
        //数据库存在数据
        List<DeptBonusCompanyDTO> deptBonusCompanyDTOList = deptBonusBudgetItemsMapper.selectCompanyBonusBudgetDetailsByCompanyBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
        //sterm流求差集
        List<Long> deptBonusBudgetItemsIds = deptBonusCompanyDTOList.stream().filter(a ->
                !deptBonusCompanyDTOS.stream().map(DeptBonusCompanyDTO::getDeptBonusBudgetItemsId).collect(Collectors.toList()).contains(a.getDeptBonusBudgetItemsId())
        ).collect(Collectors.toList()).stream().map(DeptBonusCompanyDTO::getDeptBonusBudgetItemsId).collect(Collectors.toList());
        //删除数据
        if (StringUtils.isNotEmpty(deptBonusBudgetItemsIds)){
            try {
                deptBonusBudgetItemsMapper.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(deptBonusBudgetItemsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("逻辑批量删除部门奖金预算项目表失败");
            }
        }
        for (int i1 = 0; i1 < deptBonusCompanyDTOS.size(); i1++) {
            DeptBonusBudgetItems deptBonusBudgetItems = new DeptBonusBudgetItems();
            Long deptBonusBudgetItemsId = deptBonusCompanyDTOS.get(i1).getDeptBonusBudgetItemsId();
            if (null == deptBonusBudgetItemsId){
                //公司奖金占比
                deptBonusBudgetItems.setBonusPercentage(deptBonusCompanyDTOS.get(i1).getBonusCompanyPercentage());
                deptBonusBudgetItems.setSort(i1 +1);
                //部门奖金包预算主表id
                deptBonusBudgetItems.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
                //部门奖金预算明细表id
                deptBonusBudgetItems.setDeptBonusBudgetDetailsId(0L);
                deptBonusBudgetItems.setCreateBy(SecurityUtils.getUserId());
                deptBonusBudgetItems.setCreateTime(DateUtils.getNowDate());
                deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
                deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
                deptBonusBudgetItems.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                deptBonusBudgetItemsAddList.add(deptBonusBudgetItems);
            }else {
                //公司奖金占比
                deptBonusBudgetItems.setBonusPercentage(deptBonusCompanyDTOS.get(i1).getBonusCompanyPercentage());
                deptBonusBudgetItems.setUpdateTime(DateUtils.getNowDate());
                deptBonusBudgetItems.setUpdateBy(SecurityUtils.getUserId());
                deptBonusBudgetItemsUpdateList.add(deptBonusBudgetItems);
            }
        }
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
    @Transactional
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
        List<DeptBonusCompanyDTO> deptBonusCompanyDTOList = deptBonusBudgetItemsMapper.selectCompanyBonusBudgetDetailsByCompanyBonusBudgetIds(deptBonusBudgetIds);
        if (StringUtils.isNotEmpty(deptBonusCompanyDTOList)){
            List<Long> collect1 = deptBonusCompanyDTOList.stream().map(DeptBonusCompanyDTO::getDeptBonusBudgetItemsId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect1)) {
                try {
                    deptBonusBudgetItemsMapper.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除公司奖金预算项目表失败");
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
        //公司级奖金预算明细表集合
        List<DeptBonusCompanyDTO> deptBonusCompanyDTOList = new ArrayList<>();
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemByAward();
        if (StringUtils.isNotEmpty(salaryItemDTOS)){
            for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                DeptBonusCompanyDTO deptBonusCompanyDTO = new DeptBonusCompanyDTO();
                BeanUtils.copyProperties(salaryItemDTO,deptBonusCompanyDTO);
                deptBonusCompanyDTO.setBonusCompanyAmount(new BigDecimal("0"));
                deptBonusCompanyDTO.setBonusCompanyPercentage(new BigDecimal("0"));
                deptBonusCompanyDTOList.add(deptBonusCompanyDTO);
            }
        }

        //查询部门
        List<DepartmentDTO> departmentAll = this.getDepartmentAll();
        //部门奖金预算明细表集合
        List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS = new ArrayList<>();

        //部门奖金预算项目表集合
        List<DeptBonusBudgetItemsDTO> deptBonusBudgetItemsDTOS = new ArrayList<>();
        //根据 部门分组 一级部门带包数
        Map<Long, BigDecimal> topDeptPaymentMap = new HashMap<>();
        //封装一级带包数
        this.packTopDeptPayMent(topDeptPaymentMap,budgetYear);
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
        deptBonusBudgetDTO.setDeptBonusCompanyDTOS(deptBonusCompanyDTOList);
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
        //赋值组织名称
        this.packDeptName(deptBonusBudgetDetailsDTOList);
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
                    BigDecimal bigDecimal = deptAmountBonus.multiply(deptBonusPercentageReference.divide(new BigDecimal("100"),10, BigDecimal.ROUND_HALF_UP)).setScale(10, RoundingMode.HALF_DOWN);
                    //部门奖金总计
                    deptBonusBudgetDetailsDTO.setDeptBonusSum(bigDecimal);
                }
                deptBonusBudgetDetailsDTO.setDeptBonusPercentage(deptBonusPercentageReference);
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
    private void    packDepartmentImportanceFactor(List<DepartmentDTO> departmentAll, List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOS, Map<Long, BigDecimal> topDeptPaymentMap, BigDecimal topDeptPaySum) {
        //赋值所有部门
        if (StringUtils.isEmpty(deptBonusBudgetDetailsDTOS)) {
            for (DepartmentDTO departmentDTO : departmentAll) {
                DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO = new DeptBonusBudgetDetailsDTO();
                BeanUtils.copyProperties(departmentDTO, deptBonusBudgetDetailsDTO);
                deptBonusBudgetDetailsDTOS.add(deptBonusBudgetDetailsDTO);
            }
        }
        //部门奖金参考值 公式=【当前一级部门带包数×当前一级部门组织重要性系数÷∑（各一级部门带包数×各一级部门组织重要性系数）】×100%
        if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOS)) {
            for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOS) {
                //当前一级部门带包数×当前一级部门组织重要性系数
                BigDecimal nowTopDeptPay = new BigDecimal("0");
                for (Long aLong : topDeptPaymentMap.keySet()) {
                    if (deptBonusBudgetDetailsDTO.getDepartmentId().equals(aLong)) {
                        //一级部门带包数
                        BigDecimal topDeptPay = topDeptPaymentMap.get(aLong);
                        //部门组织重要性系数
                        BigDecimal departmentImportanceFactor = deptBonusBudgetDetailsDTO.getDepartmentImportanceFactor();
                        if (null != topDeptPay && topDeptPay.compareTo(new BigDecimal("0")) != 0 &&
                                null != departmentImportanceFactor && departmentImportanceFactor.compareTo(new BigDecimal("0")) != 0) {
                            nowTopDeptPay = topDeptPay.multiply(departmentImportanceFactor).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }
                        if (null != nowTopDeptPay && nowTopDeptPay.compareTo(new BigDecimal("0")) != 0 &&
                                null != topDeptPaySum && topDeptPaySum.compareTo(new BigDecimal("0")) != 0) {
                            BigDecimal multiply = nowTopDeptPay.divide(topDeptPaySum, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2,BigDecimal.ROUND_HALF_UP);
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
                    if (aLong.equals(aLong1)) {
                        //一级部门带包数
                        BigDecimal topDeptPay = topDeptPaymentMap.get(aLong);
                        //部门组织重要性系数
                        BigDecimal departmentImportanceFactor = deptImportFactorMap.get(aLong1);
                        if (null != topDeptPay && topDeptPay.compareTo(new BigDecimal("0")) != 0 &&
                                null != departmentImportanceFactor && departmentImportanceFactor.compareTo(new BigDecimal("0")) != 0) {
                            topDeptPaySum =topDeptPaySum.add(topDeptPay.multiply(departmentImportanceFactor).setScale(10, BigDecimal.ROUND_HALF_UP));
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
                    strategyAwardAmount = amountBonusBudget.multiply(new BigDecimal("0.05")).setScale(2, BigDecimal.ROUND_HALF_UP);
                } else {
                    //战略奖金额
                    strategyAwardAmount = amountBonusBudget.multiply(deptBonusBudgetDTO.getStrategyAwardPercentage().divide(new BigDecimal("100"),10, BigDecimal.ROUND_HALF_UP)).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                ////战略奖金额
                deptBonusBudgetDTO.setStrategyAwardAmount(strategyAwardAmount);
                // 部门总奖金包 公式=总奖金包预算-战略奖金额
                BigDecimal deptAmountBonus = amountBonusBudget.subtract(strategyAwardAmount).setScale(2, BigDecimal.ROUND_HALF_UP);
                deptBonusBudgetDTO.setDeptAmountBonus(deptAmountBonus);

            }

            deptBonusBudgetDTO.setDeptBonusAddFlag(true);
        }
    }

    /**
     * 封装一级带包数
     * 1.查询所有一级部门
     * 2.查询一级部门及子级部门
     * 3.查询一级部门及子级部门下所有人员
     * 4.根据人员查询工资
     * 5.查询一级部门及子级部门下所有人力预算
     * 6.查询职级确认薪酬
     * @param topDeptPaymentMap
     * @param budgetYear
     */
    private void packTopDeptPayMent(Map<Long, BigDecimal> topDeptPaymentMap, int budgetYear) {
        //所有一级部门
        List<DepartmentDTO> departmentAll = this.getDepartmentAll();
        if (StringUtils.isNotEmpty(departmentAll)){
            for (DepartmentDTO departmentDTO : departmentAll) {
                //上年总薪酬包
                BigDecimal paymentBonus = new BigDecimal("0");
                //人力预算确认职级中位数之和
                BigDecimal employeeBudgetSum = new BigDecimal("0");
                Long departmentId = departmentDTO.getDepartmentId();
                //远程查询一级部门及子级部门
                R<List<DepartmentDTO>> sublevelDepartment = remoteDepartmentService.selectSublevelDepartment(departmentId, SecurityConstants.INNER);
                List<DepartmentDTO> data = sublevelDepartment.getData();
                if (StringUtils.isNotEmpty(data)){
                    List<Long> departmentIds = data.stream().filter(f -> f.getDepartmentId() != null).map(DepartmentDTO::getDepartmentId).distinct().collect(Collectors.toList());
                    //查询一级部门及子级部门下所有人员
                    R<List<EmployeeDTO>> employeeListData = remoteEmployeeService.selectEmployeeByDepartmentIds(departmentIds, SecurityConstants.INNER);
                    List<EmployeeDTO> employeeList= employeeListData.getData();
                    if (StringUtils.isNotEmpty(employeeList)){
                        for (EmployeeDTO employeeDTO : employeeList) {
                            //根据人员查询工资表
                            List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectDeptBonusBudgetPay(employeeDTO.getEmployeeId(), budgetYear, DateUtils.getYear(), SecurityUtils.getTenantId());
                            if (StringUtils.isNotEmpty(salaryPayDTOS)){
                                //sterm流求和 总薪酬包 公式= 工资+津贴+福利+奖金
                                paymentBonus = paymentBonus.add(salaryPayDTOS.stream().map(SalaryPayDTO::getPaymentBonus).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                            }
                        }

                    }
                    //查询一级部门及子级部门下所有人力预算
                    List<EmployeeBudgetDTO> employeeBudgetDTOList = employeeBudgetMapper.selectEmployeeBudgetByOfficialRankSystemIds(departmentIds, budgetYear);
                    if (StringUtils.isNotEmpty(employeeBudgetDTOList)){
                        for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDTOList) {
                            //根据人力预算主表主键查询人力预算明细表
                            List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.selectEmployeeBudgetDetailsByEmployeeBudgetId(employeeBudgetDTO.getEmployeeBudgetId());
                            if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)){
                                for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                                    //根据职级体系ID和职级获取职级确认薪酬
                                    OfficialRankEmolumentDTO officialRankEmolumentDTO = officialRankEmolumentMapper.selectOfficialRankEmolumentByRank(employeeBudgetDTO.getOfficialRankSystemId(), employeeBudgetDetailsDTO.getOfficialRank());
                                    if (StringUtils.isNotNull(officialRankEmolumentDTO)){
                                        //中位数*12
                                        BigDecimal bigDecimal = officialRankEmolumentDTO.getSalaryMedian().multiply(new BigDecimal("12")).setScale(10, BigDecimal.ROUND_HALF_UP);
                                        if (null!= employeeBudgetDetailsDTO.getAverageAdjust() && employeeBudgetDetailsDTO.getAverageAdjust().compareTo(new BigDecimal("0")) != 0){
                                            //平均新增数
                                            BigDecimal amountAverageAdjust = employeeBudgetDetailsDTO.getAverageAdjust();
                                            if (bigDecimal != null && bigDecimal.compareTo(new BigDecimal("0"))!=0 && amountAverageAdjust != null && amountAverageAdjust.compareTo(new BigDecimal("0")) != 0){
                                                employeeBudgetSum=employeeBudgetSum.add(bigDecimal.multiply(amountAverageAdjust).setScale(10,BigDecimal.ROUND_HALF_UP));
                                            }

                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                if (employeeBudgetSum !=null){
                    paymentBonus= paymentBonus.add(employeeBudgetSum);
                }
                topDeptPaymentMap.put(departmentDTO.getDepartmentId(),paymentBonus);
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
                        if (employeeBudgetDetailsDTO.getOfficialRankSystemId().equals(datum.getOfficialRankSystemId())) {
                            employeeBudgetDetailsDTO.setOfficialRankSystemName(datum.getOfficialRankSystemName());
                            if (StringUtils.isNotBlank(datum.getRankPrefixCode())){
                                employeeBudgetDetailsDTO.setOfficialRankName( datum.getRankPrefixCode()+ employeeBudgetDetailsDTO.getOfficialRank());
                            }else {
                                employeeBudgetDetailsDTO.setOfficialRankName(employeeBudgetDetailsDTO.getOfficialRank().toString());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装远程调用一级部门id集合
     *
     * @return
     */
    private List<Long> getDepartmentIdAll() {
        //远程查询所有部门
        R<List<DepartmentDTO>> allDepartment = remoteDepartmentService.getParentAll(SecurityConstants.INNER);
        List<DepartmentDTO> data = allDepartment.getData();
        if (StringUtils.isEmpty(data)) {
            throw new ServiceException("无部门数据 请联系管理员！");
        }
        //部门id集合
        List<Long> departmentIdParentAll = data.stream().map(DepartmentDTO::getDepartmentId).distinct().collect(Collectors.toList());
        return departmentIdParentAll;
    }

    /**
     * 返回部门集合
     *
     * @return
     */
    private List<DepartmentDTO> getDepartmentAll() {
        //远程查询所有部门
        R<List<DepartmentDTO>> allDepartment = remoteDepartmentService.getParentAll(SecurityConstants.INNER);
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
    @Transactional
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
        List<DeptBonusCompanyDTO> deptBonusCompanyDTOList = deptBonusBudgetItemsMapper.selectCompanyBonusBudgetDetailsByCompanyBonusBudgetId(deptBonusBudgetId);
        if (StringUtils.isNotEmpty(deptBonusCompanyDTOList)){
            List<Long> collect1 = deptBonusCompanyDTOList.stream().map(DeptBonusCompanyDTO::getDeptBonusBudgetItemsId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect1)) {
                try {
                    deptBonusBudgetItemsMapper.logicDeleteDeptBonusBudgetItemsByDeptBonusBudgetItemsIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除公司奖金预算项目表失败");
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

    @Override
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

    @Override
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

    @Override
    public void handleResult(List<DeptBonusBudgetDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(DeptBonusBudgetDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }
}

