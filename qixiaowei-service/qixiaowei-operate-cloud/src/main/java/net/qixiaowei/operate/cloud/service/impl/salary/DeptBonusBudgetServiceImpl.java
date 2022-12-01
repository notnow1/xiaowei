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
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetAdjustsDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptBonusBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetAdjustsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetDetailsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.DeptBonusBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.OfficialRankEmolumentMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.impl.employee.EmployeeBudgetServiceImpl;
import net.qixiaowei.operate.cloud.service.salary.IDeptBonusBudgetService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;
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

    /**
     * 查询部门奖金包预算表
     *
     * @param deptBonusBudgetId 部门奖金包预算表主键
     * @return 部门奖金包预算表
     */
    @Override
    public DeptBonusBudgetDTO selectDeptBonusBudgetByDeptBonusBudgetId(Long deptBonusBudgetId) {
        return deptBonusBudgetMapper.selectDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudgetId);
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
        return deptBonusBudgetMapper.selectDeptBonusBudgetList(deptBonusBudget);
    }

    /**
     * 新增部门奖金包预算表
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
    @Override
    public DeptBonusBudgetDTO insertDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        deptBonusBudget.setCreateBy(SecurityUtils.getUserId());
        deptBonusBudget.setCreateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
        deptBonusBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        deptBonusBudgetMapper.insertDeptBonusBudget(deptBonusBudget);
        deptBonusBudgetDTO.setDeptBonusBudgetId(deptBonusBudget.getDeptBonusBudgetId());
        return deptBonusBudgetDTO;
    }

    /**
     * 修改部门奖金包预算表
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
    @Override
    public int updateDeptBonusBudget(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        BeanUtils.copyProperties(deptBonusBudgetDTO, deptBonusBudget);
        deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
        return deptBonusBudgetMapper.updateDeptBonusBudget(deptBonusBudget);
    }

    /**
     * 逻辑批量删除部门奖金包预算表
     *
     * @param deptBonusBudgetIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(List<Long> deptBonusBudgetIds) {
        return deptBonusBudgetMapper.logicDeleteDeptBonusBudgetByDeptBonusBudgetIds(deptBonusBudgetIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
        //部门id 职级平均人数
        Map<Long, Map<String, BigDecimal>> mapEmployeeAve = new HashMap<>();
        //部门id 职级平均薪酬
        Map<Long, Map<String, BigDecimal>> mapPaymentAve = new HashMap<>();
        //根据 部门分组 一级部门带包数
        Map<Long,BigDecimal> topDeptPaymentMap = new HashMap<>();
        //封装每个部门 相同职级的 平均人数
        getEmployeeBudgetDTOS(budgetYear, mapEmployeeAve);
        //封装每个部门 相同职级的 平均薪酬
        packDeptOffPaymentAve(budgetYear, mapPaymentAve);
        //封装一级带包数
        packTopDeptPayMent(mapEmployeeAve, mapPaymentAve, topDeptPaymentMap);

        return null;
    }

    /**
     * 封装一级带包数
     * @param mapEmployeeAve
     * @param mapPaymentAve
     * @param topDeptPaymentMap
     */
    private void packTopDeptPayMent(Map<Long, Map<String, BigDecimal>> mapEmployeeAve, Map<Long, Map<String, BigDecimal>> mapPaymentAve, Map<Long, BigDecimal> topDeptPaymentMap) {
        if (StringUtils.isNotEmpty(mapEmployeeAve) && StringUtils.isNotEmpty(mapPaymentAve)){
            for (Long aLong : mapEmployeeAve.keySet()) {
                for (Long aLong1 : mapPaymentAve.keySet()) {
                    //相同部门
                    if (aLong == aLong1){
                        Map<String, BigDecimal> employeeAveBigDecimalMap = mapEmployeeAve.get(aLong);
                        Map<String, BigDecimal> paymentAveBigDecimalMap = mapPaymentAve.get(aLong1);
                        for (String s : employeeAveBigDecimalMap.keySet()) {
                            for (String s1 : paymentAveBigDecimalMap.keySet()) {
                                //相同职级
                                if (StringUtils.equals(s,s1)){
                                    //职级的平均人数
                                    BigDecimal bigDecimal = employeeAveBigDecimalMap.get(s);
                                    // //职级的平均薪资
                                    BigDecimal bigDecimal1 = paymentAveBigDecimalMap.get(s1);
                                    if (null != bigDecimal && bigDecimal.compareTo(new BigDecimal("0"))>0 &&
                                            null != bigDecimal1 && bigDecimal1.compareTo(new BigDecimal("0"))>0){
                                        topDeptPaymentMap.put(aLong,bigDecimal.multiply(bigDecimal1));
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
                                            if (StringUtils.isNotEmpty(salaryPayDTOS)){
                                                //sterm流求和 总薪酬包 公式= 工资+津贴+福利+奖金
                                                BigDecimal reduce = salaryPayDTOS.stream().map(SalaryPayDTO::getPaymentBonus).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                                                //公式=某职级的总薪酬包（包含总工资包和总奖金包）÷某职级的人数。
                                                 payAmountSum = reduce.divide(new BigDecimal(String.valueOf(salaryPayDTOS.size())), 2, BigDecimal.ROUND_HALF_DOWN);
                                            }
                                            map.put(datum.getEmployeeRankName(),payAmountSum);
                                            mapPaymentAve.put(aLong,map);
                                        } // 职级确认薪酬取中位值
                                        else {
                                            //根据职级体系id查询职级确认薪酬
                                            List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList= officialRankEmolumentMapper.selectOfficialRankEmolumentBySystemId(employeeBudgetDetailsDTO.getOfficialRankSystemId());
                                            //远程调用查询职级
                                            R<OfficialRankSystemDTO> officialRankSystemDTOR = remoteOfficialRankSystemService.selectById(employeeBudgetDetailsDTO.getOfficialRankSystemId(), SecurityConstants.INNER);
                                            OfficialRankSystemDTO data1 = officialRankSystemDTOR.getData();
                                            if (StringUtils.isNull(data1)){
                                                throw new ServiceException("职级不存在 请联系管理员！");
                                            }
                                            if (StringUtils.isNotEmpty(officialRankEmolumentDTOList)){
                                                for (OfficialRankEmolumentDTO officialRankEmolumentDTO : officialRankEmolumentDTOList) {
                                                    map.put(data1.getRankPrefixCode()+data1.getOfficialRank(),officialRankEmolumentDTO.getSalaryMedian());
                                                    mapPaymentAve.put(aLong,map);
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
                        if (StringUtils.isNotEmpty(data1)){
                            //根据部门id分组
                            Map<Long, List<EmployeeDTO>> employeeDepartmentMap = data1.parallelStream().collect(Collectors.groupingBy(EmployeeDTO::getEmployeeDepartmentId));
                            for (Long aLong : employeeDepartmentMap.keySet()) {
                                List<EmployeeDTO> employeeDTOList = employeeDepartmentMap.get(aLong);
                                //职级体系id集合
                                List<Long> collect1 = employeeDTOList.stream().map(EmployeeDTO::getOfficialRankSystemId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                                //远程调用查询职级
                                R<List<OfficialRankSystemDTO>> listR2 = remoteOfficialRankSystemService.selectByIds(collect1, SecurityConstants.INNER);
                                //根据职级体系id查询职级确认薪酬
                                List<OfficialRankEmolumentDTO> officialRankEmolumentDTOList= officialRankEmolumentMapper.selectOfficialRankEmolumentBySystemIds(collect1);
                                List<OfficialRankSystemDTO> data2 = listR2.getData();
                                if (StringUtils.isNotEmpty(data2) && StringUtils.isNotEmpty(officialRankEmolumentDTOList)){
                                    for (OfficialRankSystemDTO officialRankSystemDTO : data2) {
                                        Map<String, BigDecimal> map = new HashMap<>();
                                        for (OfficialRankEmolumentDTO officialRankEmolumentDTO : officialRankEmolumentDTOList) {
                                            if (officialRankSystemDTO.getOfficialRankSystemId() == officialRankEmolumentDTO.getOfficialRankSystemId()){
                                                //起始级别
                                                Integer rankStart = officialRankSystemDTO.getRankStart();
                                                //终止级别
                                                Integer rankEnd = officialRankSystemDTO.getRankEnd();
                                                for (Integer i = rankStart; i <= rankEnd; i++) {
                                                    map.put(officialRankSystemDTO.getRankPrefixCode()+officialRankSystemDTO.getOfficialRank(),officialRankEmolumentDTO.getSalaryMedian());
                                                    mapPaymentAve.put(aLong,map);
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
     * 逻辑删除部门奖金包预算表信息
     *
     * @param deptBonusBudgetDTO 部门奖金包预算表
     * @return 结果
     */
    @Override
    public int logicDeleteDeptBonusBudgetByDeptBonusBudgetId(DeptBonusBudgetDTO deptBonusBudgetDTO) {
        DeptBonusBudget deptBonusBudget = new DeptBonusBudget();
        deptBonusBudget.setDeptBonusBudgetId(deptBonusBudgetDTO.getDeptBonusBudgetId());
        deptBonusBudget.setUpdateTime(DateUtils.getNowDate());
        deptBonusBudget.setUpdateBy(SecurityUtils.getUserId());
        return deptBonusBudgetMapper.logicDeleteDeptBonusBudgetByDeptBonusBudgetId(deptBonusBudget);
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

