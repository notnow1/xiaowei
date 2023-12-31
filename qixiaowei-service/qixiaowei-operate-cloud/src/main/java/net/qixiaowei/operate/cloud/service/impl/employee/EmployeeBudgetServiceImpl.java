package net.qixiaowei.operate.cloud.service.impl.employee;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.employee.EmployeeBudget;
import net.qixiaowei.operate.cloud.api.domain.employee.EmployeeBudgetAdjusts;
import net.qixiaowei.operate.cloud.api.domain.employee.EmployeeBudgetDetails;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetAdjustsDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetDetailsExcel;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetExcel;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetAdjustsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetDetailsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.OfficialRankEmolumentMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.employee.IEmployeeBudgetService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
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
 * EmployeeBudgetService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-11-18
 */
@Service
public class EmployeeBudgetServiceImpl implements IEmployeeBudgetService {
    @Autowired
    private EmployeeBudgetMapper employeeBudgetMapper;
    @Autowired
    private EmployeeBudgetDetailsMapper employeeBudgetDetailsMapper;

    @Autowired
    private EmployeeBudgetAdjustsMapper employeeBudgetAdjustsMapper;

    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteOfficialRankSystemService remoteOfficialRankSystemService;

    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private SalaryPayMapper salaryPayMapper;
    @Autowired
    private OfficialRankEmolumentMapper officialRankEmolumentMapper;

    /**
     * 查询人力预算表
     *
     * @param employeeBudgetId 人力预算表主键
     * @return 人力预算表
     */
    @Override
    public EmployeeBudgetDTO selectEmployeeBudgetByEmployeeBudgetId(Long employeeBudgetId) {
        //人力预算表
        EmployeeBudgetDTO employeeBudgetDTO = employeeBudgetMapper.selectEmployeeBudgetByEmployeeBudgetId(employeeBudgetId);
        //远程调用部门
        R<DepartmentDTO> departmentDTOR = remoteDepartmentService.selectdepartmentId(employeeBudgetDTO.getDepartmentId(), SecurityConstants.INNER);
        DepartmentDTO data = departmentDTOR.getData();
        if (StringUtils.isNotNull(data)) {
            employeeBudgetDTO.setDepartmentName(data.getDepartmentName());
        }

        //职级体系远程调用
        R<OfficialRankSystemDTO> officialRankSystemDTOR = remoteOfficialRankSystemService.selectById(employeeBudgetDTO.getOfficialRankSystemId(), SecurityConstants.INNER);
        OfficialRankSystemDTO data1 = officialRankSystemDTOR.getData();
        if (StringUtils.isNotNull(data1)) {
            employeeBudgetDTO.setOfficialRankSystemName(data1.getOfficialRankSystemName());
        }

        //人力预算明细表集合
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.selectEmployeeBudgetDetailsByEmployeeBudgetId(employeeBudgetId);

        //岗位职级远程调用
        R<OfficialRankSystemDTO> officialRankSystemDTOR1 = remoteOfficialRankSystemService.selectById(employeeBudgetDTO.getOfficialRankSystemId(), SecurityConstants.INNER);
        OfficialRankSystemDTO data2 = officialRankSystemDTOR1.getData();
        if (StringUtils.isNotNull(data2)) {
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                employeeBudgetDetailsDTO.setOfficialRankName(data2.getRankPrefixCode() + employeeBudgetDetailsDTO.getOfficialRank());
            }

        }

        List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getEmployeeBudgetDetailsId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetAdjustsMapper.selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsIds(collect);
            EmployeeBudgetServiceImpl.packEmployeeBudgetDetailsNum(employeeBudgetDetailsDTOS, employeeBudgetAdjustsDTOS);
        }

        employeeBudgetDTO.setEmployeeBudgetDetailsDTOS(employeeBudgetDetailsDTOS);
        return employeeBudgetDTO;
    }

    /**
     * 封装人力预算计算年末总计等值
     *
     * @param employeeBudgetDetailsDTOS
     * @param employeeBudgetAdjustsDTOS
     */
    public static void packEmployeeBudgetDetailsNum(List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS, List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS) {
        //根据人力预算明细表id分组
        Map<Long, List<EmployeeBudgetAdjustsDTO>> mapList = employeeBudgetAdjustsDTOS.parallelStream().collect(Collectors.groupingBy(EmployeeBudgetAdjustsDTO::getEmployeeBudgetDetailsId));


        //放入数据
        for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
            BigDecimal annualAverageNum = new BigDecimal("0");
            //上年期末人数
            Integer numberLastYear = employeeBudgetDetailsDTO.getNumberLastYear();
            //平均新增人数
            BigDecimal averageAdjust = employeeBudgetDetailsDTO.getAverageAdjust();
            if (null != numberLastYear && null != averageAdjust) {
                annualAverageNum = new BigDecimal(numberLastYear.toString()).add(averageAdjust);
            }
            //年度平均人数 = 上年期末数+平均新增人数
            employeeBudgetDetailsDTO.setAnnualAverageNum(annualAverageNum);
            List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS1 = mapList.get(employeeBudgetDetailsDTO.getEmployeeBudgetDetailsId());
            //sterm流求和
            Integer reduce = employeeBudgetAdjustsDTOS1.stream().filter(Objects::nonNull).map(EmployeeBudgetAdjustsDTO::getNumberAdjust).reduce(0, Integer::sum);
            //年度规划人数
            employeeBudgetDetailsDTO.setAnnualPlanningNum(reduce);
            //年末总计
            employeeBudgetDetailsDTO.setEndYearSum(numberLastYear + reduce);
            employeeBudgetDetailsDTO.setEmployeeBudgetAdjustsDTOS(employeeBudgetAdjustsDTOS1);
        }
    }

    /**
     * 查询人力预算表列表
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 人力预算表
     */
    @DataScope(businessAlias = "eb")
    @Override
    public List<EmployeeBudgetDTO> selectEmployeeBudgetList(EmployeeBudgetDTO employeeBudgetDTO) {
        List<String> departmentIds = new ArrayList<>();
        EmployeeBudget employeeBudget = new EmployeeBudget();
        this.getDepartmentIdAndofficialRankSystemId(employeeBudgetDTO.getParams());
        BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
        if (StringUtils.isNotNull(employeeBudgetDTO.getDepartmentName())) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            departmentDTO.setDepartmentName(employeeBudgetDTO.getDepartmentName());
            R<List<DepartmentDTO>> departmentDtoList = remoteDepartmentService.selectDepartment(departmentDTO, SecurityConstants.INNER);
            List<DepartmentDTO> departmentListData = departmentDtoList.getData();
            List<Long> remoteDepartmentIds = departmentListData.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(remoteDepartmentIds)) {
                remoteDepartmentIds.forEach(d->{
                    departmentIds.add(String.valueOf(d));
                });
            }else {
                departmentIds.add("");
            }
        }
        employeeBudget.setDepartmentIds(departmentIds);
        List<EmployeeBudgetDTO> employeeBudgetDTOS = employeeBudgetMapper.selectEmployeeBudgetList(employeeBudget);
        //远程调用部门赋值
        List<Long> collect = employeeBudgetDTOS.stream().map(EmployeeBudgetDTO::getDepartmentId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
            List<DepartmentDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (EmployeeBudgetDTO budgetDTO : employeeBudgetDTOS) {
                    for (DepartmentDTO datum : data) {
                        if (budgetDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                            budgetDTO.setDepartmentName(datum.getDepartmentName());
                        }
                    }
                }
            }
        }
        //远程调用职级赋值
        List<Long> collect1 = employeeBudgetDTOS.stream().map(EmployeeBudgetDTO::getOfficialRankSystemId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect1)) {
            R<List<OfficialRankSystemDTO>> listR = remoteOfficialRankSystemService.selectByIds(collect1, SecurityConstants.INNER);
            List<OfficialRankSystemDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (EmployeeBudgetDTO budgetDTO : employeeBudgetDTOS) {
                    for (OfficialRankSystemDTO datum : data) {
                        if (budgetDTO.getOfficialRankSystemId().equals(datum.getOfficialRankSystemId())) {
                            budgetDTO.setOfficialRankSystemName(datum.getOfficialRankSystemName());
                        }
                    }
                }
            }
        }
        BigDecimal annualAverageNum = new BigDecimal("0");
        for (EmployeeBudgetDTO budgetDTO : employeeBudgetDTOS) {
            Integer amountLastYear = budgetDTO.getAmountLastYear();
            BigDecimal amountAverageAdjust = budgetDTO.getAmountAverageAdjust();
            if (null != amountLastYear && null != amountAverageAdjust) {
                annualAverageNum = new BigDecimal(amountLastYear.toString()).add(amountAverageAdjust);
            }
            //年度平均人数 = 上年期末数+平均新增人数
            budgetDTO.setAnnualAverageNum(annualAverageNum);
        }
        this.handleResult(employeeBudgetDTOS);
        return employeeBudgetDTOS;
    }
    @Override
    public void handleResult(List<EmployeeBudgetDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(EmployeeBudgetDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 获取高级搜索后的组织ID传入params
     *
     * @param params 请求参数
     */
    private void getDepartmentIdAndofficialRankSystemId(Map<String, Object> params) {
        if (StringUtils.isNotEmpty(params)) {
            DepartmentDTO departmentDTO = new DepartmentDTO();
            OfficialRankSystemDTO officialRankSystemDTO = new OfficialRankSystemDTO();
            Map<String, Object> params2 = new HashMap<>();

            Map<String, Object> params3 = new HashMap<>();
            for (String key : params.keySet()) {
                switch (key) {
                    case "departmentNameEqual":
                        params2.put("departmentNameEqual", params.get("departmentNameEqual"));
                        break;
                    case "departmentNameNotEqual":
                        params2.put("departmentNameNotEqual", params.get("departmentNameNotEqual"));
                        break;
                    case "officialRankSystemNameEqual":
                        params3.put("officialRankSystemNameEqual", params.get("officialRankSystemNameEqual"));
                        break;
                    case "officialRankSystemNameNotEqual":
                        params3.put("officialRankSystemNameNotEqual", params.get("officialRankSystemNameNotEqual"));
                        break;
                    default:break;
                }
            }
            if (StringUtils.isNotEmpty(params2)) {
                departmentDTO.setParams(params2);
                //远程查找部门列表
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectDepartment(departmentDTO, SecurityConstants.INNER);
                if (listR.getCode() != 200) {
                    throw new ServiceException("远程调用组织失败 请联系管理员");
                }
                List<DepartmentDTO> departmentDataList = listR.getData();
                List<Long> departmentIds = new ArrayList<>();
                if (StringUtils.isNotEmpty(departmentDataList)) {
                    departmentIds = departmentDataList.stream().map(DepartmentDTO::getDepartmentId).distinct().collect(Collectors.toList());
                    params.put("departmentIds", departmentIds);
                }


            }
            if (StringUtils.isNotEmpty(params3)) {
                officialRankSystemDTO.setParams(params3);
                //远程查询职级体系表列表
                R<List<OfficialRankSystemDTO>> listR1 = remoteOfficialRankSystemService.selectOfficialRankSystemDTOTab(officialRankSystemDTO, SecurityConstants.INNER);
                if (listR1.getCode() != 200) {
                    throw new ServiceException("远程调用职级体系失败 请联系管理员");
                }
                List<OfficialRankSystemDTO> officialRankSystemDataList = listR1.getData();

                List<Long> officialRankSystemIds = new ArrayList<>();
                if (StringUtils.isNotEmpty(officialRankSystemDataList)) {
                    officialRankSystemIds = officialRankSystemDataList.stream().map(OfficialRankSystemDTO::getOfficialRankSystemId).distinct().collect(Collectors.toList());
                    params.put("officialRankSystemIds", officialRankSystemIds);
                }

            }
        }
    }


    /**
     * 新增人力预算表
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 结果
     */
    @Override
    @Transactional
    public EmployeeBudgetDTO insertEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO) {
        EmployeeBudget employeeBudget = new EmployeeBudget();
        BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
        EmployeeBudgetDTO employeeBudgetDTO1 = employeeBudgetMapper.selectEmployeeBudget(employeeBudget);
        if (StringUtils.isNotNull(employeeBudgetDTO1)) {
            throw new ServiceException("人力预算已存在");
        }
        employeeBudget.setCreateBy(SecurityUtils.getUserId());
        employeeBudget.setCreateTime(DateUtils.getNowDate());
        employeeBudget.setUpdateTime(DateUtils.getNowDate());
        employeeBudget.setUpdateBy(SecurityUtils.getUserId());
        employeeBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //人力预算详情表
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDTO.getEmployeeBudgetDetailsDTOS();
        //封装主表上年期末人数 本年新增人数 平均新增数 封装详情表本年新增人数
        packNum(employeeBudget, employeeBudgetDetailsDTOS);
        try {
            employeeBudgetMapper.insertEmployeeBudget(employeeBudget);
        } catch (Exception e) {
            throw new ServiceException("新增人力预算失败");
        }

        //插入人力详情表
        List<EmployeeBudgetDetails> employeeBudgetDetails = packEmployeeBudgetDetailsList(employeeBudgetDetailsDTOS, employeeBudget);
        //插入人力预算详情明细调整表
        if (StringUtils.isNotEmpty(employeeBudgetDetails)) {
            packEmployeeBudgetAdjustsList(employeeBudgetDetailsDTOS, employeeBudgetDetails);
        }
        employeeBudgetDTO.setEmployeeBudgetId(employeeBudget.getEmployeeBudgetId());
        return employeeBudgetDTO;
    }

    /**
     * 封装主表上年期末人数 本年新增人数 平均新增数 封装详情表本年新增人数
     *
     * @param employeeBudget
     * @param employeeBudgetDetailsDTOS
     */
    private void packNum(EmployeeBudget employeeBudget, List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS) {
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            //人力预算详情表
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetDetailsDTO.getEmployeeBudgetAdjustsDTOS();
                if (StringUtils.isNotEmpty(employeeBudgetAdjustsDTOS)) {
                    //本年新增人数
                    Integer countAdjust = 0;
                    //人力预算详情明细表
                    for (EmployeeBudgetAdjustsDTO employeeBudgetAdjustsDTO : employeeBudgetAdjustsDTOS) {
                        //本年新增人数
                        countAdjust = countAdjust + employeeBudgetAdjustsDTO.getNumberAdjust();

                    }
                    employeeBudgetDetailsDTO.setCountAdjust(countAdjust);
                }
            }
            //上年期末人数
            Integer amountLastYear = 0;
            //列表本年新增人数
            Integer amountAdjust = 0;
            //平均新增数
            BigDecimal amountAverageAdjust = new BigDecimal("0");
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                //上年期末人数
                amountLastYear = amountLastYear + employeeBudgetDetailsDTO.getNumberLastYear();
                //列表本年新增人数
                amountAdjust = amountAdjust + employeeBudgetDetailsDTO.getCountAdjust();
                //平均新增数
                amountAverageAdjust = amountAverageAdjust.add(employeeBudgetDetailsDTO.getAverageAdjust());
            }
            employeeBudget.setAmountLastYear(amountLastYear);
            employeeBudget.setAmountAdjust(amountAdjust);
            employeeBudget.setAmountAverageAdjust(amountAverageAdjust);
        }
    }

    /**
     * 插入人力预算详情明细调整表
     *
     * @param employeeBudgetDetailsDTOS
     * @param employeeBudgetDetails
     */
    private void packEmployeeBudgetAdjustsList(List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS, List<EmployeeBudgetDetails> employeeBudgetDetails) {
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            List<EmployeeBudgetAdjusts> employeeBudgetAdjustsList = new ArrayList<>();
            for (int i = 0; i < employeeBudgetDetailsDTOS.size(); i++) {
                List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetDetailsDTOS.get(i).getEmployeeBudgetAdjustsDTOS();
                if (StringUtils.isNotEmpty(employeeBudgetAdjustsDTOS)) {
                    for (int i1 = 0; i1 < employeeBudgetAdjustsDTOS.size(); i1++) {
                        EmployeeBudgetAdjusts employeeBudgetAdjusts = new EmployeeBudgetAdjusts();
                        //调整人数
                        employeeBudgetAdjusts.setEmployeeBudgetDetailsId(employeeBudgetDetails.get(i).getEmployeeBudgetDetailsId());
                        employeeBudgetAdjusts.setNumberAdjust(employeeBudgetAdjustsDTOS.get(i1).getNumberAdjust());
                        employeeBudgetAdjusts.setCycleNumber(i1 + 1);
                        employeeBudgetAdjusts.setCreateBy(SecurityUtils.getUserId());
                        employeeBudgetAdjusts.setCreateTime(DateUtils.getNowDate());
                        employeeBudgetAdjusts.setUpdateTime(DateUtils.getNowDate());
                        employeeBudgetAdjusts.setUpdateBy(SecurityUtils.getUserId());
                        employeeBudgetAdjusts.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        employeeBudgetAdjustsList.add(employeeBudgetAdjusts);
                    }
                }
            }
            try {
                employeeBudgetAdjustsMapper.batchEmployeeBudgetAdjusts(employeeBudgetAdjustsList);
            } catch (Exception e) {
                throw new ServiceException("新增人力预算详情明细失败");
            }
        }
    }

    /**
     * 封装插入人力预算详情表
     *
     * @param employeeBudgetDetailsDTOS
     * @param employeeBudget
     * @return
     */
    private List<EmployeeBudgetDetails> packEmployeeBudgetDetailsList(List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS, EmployeeBudget employeeBudget) {
        List<EmployeeBudgetDetails> employeeBudgetDetailsList = new ArrayList<>();
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                EmployeeBudgetDetails employeeBudgetDetails = new EmployeeBudgetDetails();
                BeanUtils.copyProperties(employeeBudgetDetailsDTO, employeeBudgetDetails);
                //人力预算ID
                employeeBudgetDetails.setEmployeeBudgetId(employeeBudget.getEmployeeBudgetId());
                employeeBudgetDetails.setCreateBy(SecurityUtils.getUserId());
                employeeBudgetDetails.setCreateTime(DateUtils.getNowDate());
                employeeBudgetDetails.setUpdateTime(DateUtils.getNowDate());
                employeeBudgetDetails.setUpdateBy(SecurityUtils.getUserId());
                employeeBudgetDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                employeeBudgetDetailsList.add(employeeBudgetDetails);

            }
            try {
                employeeBudgetDetailsMapper.batchEmployeeBudgetDetails(employeeBudgetDetailsList);
            } catch (Exception e) {
                throw new ServiceException("新增人力预算详情失败");
            }
        }
        return employeeBudgetDetailsList;
    }

    /**
     * 修改人力预算表
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO) {
        int i = 0;
        //人力预算主表
        EmployeeBudget employeeBudget = new EmployeeBudget();
        //人力预算明细表集合
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDTO.getEmployeeBudgetDetailsDTOS();
        //修改人力预算详情表集合
        List<EmployeeBudgetDetails> employeeBudgetDetailsList = new ArrayList<>();
        //修改人力预算调整表集合
        List<EmployeeBudgetAdjusts> employeeBudgetAdjustsList = new ArrayList<>();
        //列表本年新增人数
        Integer amountAdjust = 0;
        //列表上年期末人数
        Integer amountLastYear = 0;
        //列表平均新增数
        BigDecimal amountAverageAdjust = new BigDecimal("0");
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                //本年新增人数
                Integer countAdjust = 0;
                //人力预算调整表集合
                List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetDetailsDTO.getEmployeeBudgetAdjustsDTOS();
                if (StringUtils.isNotEmpty(employeeBudgetAdjustsDTOS)) {
                    for (EmployeeBudgetAdjustsDTO employeeBudgetAdjustsDTO : employeeBudgetAdjustsDTOS) {
                        countAdjust = countAdjust + employeeBudgetAdjustsDTO.getNumberAdjust();
                    }
                }
                //本年新增人数
                employeeBudgetDetailsDTO.setCountAdjust(countAdjust);
            }
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                //上年期末人数
                amountLastYear = amountLastYear + employeeBudgetDetailsDTO.getNumberLastYear();
                //本年新增人数
                amountAdjust = amountAdjust + employeeBudgetDetailsDTO.getNumberLastYear() + employeeBudgetDetailsDTO.getCountAdjust();
                //平均新增数
                amountAverageAdjust = amountAverageAdjust.add(employeeBudgetDetailsDTO.getAverageAdjust());
                //人力预算详情表
                EmployeeBudgetDetails employeeBudgetDetails = new EmployeeBudgetDetails();
                BeanUtils.copyProperties(employeeBudgetDetailsDTO, employeeBudgetDetails);

                //人力预算调整表集合
                List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetDetailsDTO.getEmployeeBudgetAdjustsDTOS();
                if (StringUtils.isNotEmpty(employeeBudgetAdjustsDTOS)) {
                    for (EmployeeBudgetAdjustsDTO employeeBudgetAdjustsDTO : employeeBudgetAdjustsDTOS) {
                        EmployeeBudgetAdjusts employeeBudgetAdjusts = new EmployeeBudgetAdjusts();
                        employeeBudgetAdjusts.setEmployeeBudgetAdjustsId(employeeBudgetAdjustsDTO.getEmployeeBudgetAdjustsId());
                        employeeBudgetAdjusts.setNumberAdjust(employeeBudgetAdjustsDTO.getNumberAdjust());
                        employeeBudgetAdjusts.setUpdateTime(DateUtils.getNowDate());
                        employeeBudgetAdjusts.setUpdateBy(SecurityUtils.getUserId());
                        employeeBudgetAdjustsList.add(employeeBudgetAdjusts);
                    }
                }
                employeeBudgetDetailsList.add(employeeBudgetDetails);
            }

            BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
            //列表上年期末人数
            employeeBudget.setAmountLastYear(amountLastYear);
            //列表本年新增人数
            employeeBudget.setAmountAdjust(amountAdjust);
            //列表平均新增数
            employeeBudget.setAmountAverageAdjust(amountAverageAdjust);

            if (StringUtils.isNotNull(employeeBudget)) {
                employeeBudget.setUpdateBy(SecurityUtils.getUserId());
                employeeBudget.setUpdateTime(DateUtils.getNowDate());
                try {
                    i = employeeBudgetMapper.updateEmployeeBudget(employeeBudget);
                } catch (Exception e) {
                    throw new ServiceException("修改人力预算失败");
                }
            }
            if (StringUtils.isNotEmpty(employeeBudgetDetailsList)) {
                try {
                    employeeBudgetDetailsMapper.updateEmployeeBudgetDetailss(employeeBudgetDetailsList);
                } catch (Exception e) {
                    throw new ServiceException("修改人力预算详情失败");
                }
            }
            if (StringUtils.isNotEmpty(employeeBudgetAdjustsList)) {
                try {
                    employeeBudgetAdjustsMapper.updateEmployeeBudgetAdjustss(employeeBudgetAdjustsList);
                } catch (Exception e) {
                    throw new ServiceException("修改人力预算详情明细失败");
                }
            }
        }
        return i;
    }

    /**
     * 逻辑批量删除人力预算表
     *
     * @param employeeBudgetIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteEmployeeBudgetByEmployeeBudgetIds(List<Long> employeeBudgetIds) {
        List<EmployeeBudgetDTO> employeeBudgetDTOS = employeeBudgetMapper.selectEmployeeBudgetByEmployeeBudgetIds(employeeBudgetIds);
        if (StringUtils.isEmpty(employeeBudgetDTOS)) {
            throw new ServiceException("数据不存在！");
        }
        //删除详情表
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.selectEmployeeBudgetDetailsByEmployeeBudgetIds(employeeBudgetIds);
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getEmployeeBudgetDetailsId).collect(Collectors.toList());

            try {
                if (StringUtils.isNotEmpty(collect)) {
                    employeeBudgetDetailsMapper.logicDeleteEmployeeBudgetDetailsByEmployeeBudgetDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                }

            } catch (Exception e) {
                throw new ServiceException("删除人力预算详情表失败");
            }
            //删除明细表
            List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetAdjustsMapper.selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsIds(collect);
            List<Long> collect1 = employeeBudgetAdjustsDTOS.stream().map(EmployeeBudgetAdjustsDTO::getEmployeeBudgetAdjustsId).collect(Collectors.toList());
            try {
                if (StringUtils.isNotEmpty(collect1)) {
                    employeeBudgetAdjustsMapper.logicDeleteEmployeeBudgetAdjustsByEmployeeBudgetAdjustsIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
                }
            } catch (Exception e) {
                throw new ServiceException("删除人力预算明细表失败");
            }

        }
        return employeeBudgetMapper.logicDeleteEmployeeBudgetByEmployeeBudgetIds(employeeBudgetIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除人力预算表信息
     *
     * @param employeeBudgetId 人力预算表主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeBudgetByEmployeeBudgetId(Long employeeBudgetId) {
        return employeeBudgetMapper.deleteEmployeeBudgetByEmployeeBudgetId(employeeBudgetId);
    }

    /**
     * 逻辑删除人力预算表信息
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteEmployeeBudgetByEmployeeBudgetId(EmployeeBudgetDTO employeeBudgetDTO) {
        EmployeeBudget employeeBudget = new EmployeeBudget();
        employeeBudget.setEmployeeBudgetId(employeeBudgetDTO.getEmployeeBudgetId());
        EmployeeBudgetDTO employeeBudgetDTO1 = employeeBudgetMapper.selectEmployeeBudgetByEmployeeBudgetId(employeeBudgetDTO.getEmployeeBudgetId());
        if (StringUtils.isNull(employeeBudgetDTO1)) {
            throw new ServiceException("数据不存在！");
        }
        employeeBudget.setUpdateTime(DateUtils.getNowDate());
        employeeBudget.setUpdateBy(SecurityUtils.getUserId());
        //删除详情表
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.selectEmployeeBudgetDetailsByEmployeeBudgetId(employeeBudgetDTO.getEmployeeBudgetId());
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getEmployeeBudgetDetailsId).collect(Collectors.toList());

            try {
                if (StringUtils.isNotEmpty(collect)) {
                    employeeBudgetDetailsMapper.logicDeleteEmployeeBudgetDetailsByEmployeeBudgetDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                }

            } catch (Exception e) {
                throw new ServiceException("删除人力预算详情表失败");
            }
            //删除明细表
            List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetAdjustsMapper.selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsIds(collect);
            List<Long> collect1 = employeeBudgetAdjustsDTOS.stream().map(EmployeeBudgetAdjustsDTO::getEmployeeBudgetAdjustsId).collect(Collectors.toList());
            try {
                if (StringUtils.isNotEmpty(collect1)) {
                    employeeBudgetAdjustsMapper.logicDeleteEmployeeBudgetAdjustsByEmployeeBudgetAdjustsIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
                }
            } catch (Exception e) {
                throw new ServiceException("删除人力预算明细表失败");
            }

        }
        return employeeBudgetMapper.logicDeleteEmployeeBudgetByEmployeeBudgetId(employeeBudget);
    }

    /**
     * 物理删除人力预算表信息
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 结果
     */

    @Override
    public int deleteEmployeeBudgetByEmployeeBudgetId(EmployeeBudgetDTO employeeBudgetDTO) {
        EmployeeBudget employeeBudget = new EmployeeBudget();
        BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
        return employeeBudgetMapper.deleteEmployeeBudgetByEmployeeBudgetId(employeeBudget.getEmployeeBudgetId());
    }

    /**
     * 物理批量删除人力预算表
     *
     * @param employeeBudgetDtos 需要删除的人力预算表主键
     * @return 结果
     */

    @Override
    public int deleteEmployeeBudgetByEmployeeBudgetIds(List<EmployeeBudgetDTO> employeeBudgetDtos) {
        List<Long> stringList = new ArrayList();
        for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDtos) {
            stringList.add(employeeBudgetDTO.getEmployeeBudgetId());
        }
        return employeeBudgetMapper.deleteEmployeeBudgetByEmployeeBudgetIds(stringList);
    }

    /**
     * 批量新增人力预算表信息
     *
     * @param employeeBudgetDtos 人力预算表对象
     */

    @Override
    public int insertEmployeeBudgets(List<EmployeeBudgetDTO> employeeBudgetDtos) {
        List<EmployeeBudget> employeeBudgetList = new ArrayList();

        for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDtos) {
            EmployeeBudget employeeBudget = new EmployeeBudget();
            BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
            employeeBudget.setCreateBy(SecurityUtils.getUserId());
            employeeBudget.setCreateTime(DateUtils.getNowDate());
            employeeBudget.setUpdateTime(DateUtils.getNowDate());
            employeeBudget.setUpdateBy(SecurityUtils.getUserId());
            employeeBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            employeeBudgetList.add(employeeBudget);
        }
        return employeeBudgetMapper.batchEmployeeBudget(employeeBudgetList);
    }

    /**
     * 批量修改人力预算表信息
     *
     * @param employeeBudgetDtos 人力预算表对象
     */

    @Override
    public int updateEmployeeBudgets(List<EmployeeBudgetDTO> employeeBudgetDtos) {
        List<EmployeeBudget> employeeBudgetList = new ArrayList();

        for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDtos) {
            EmployeeBudget employeeBudget = new EmployeeBudget();
            BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
            employeeBudget.setCreateBy(SecurityUtils.getUserId());
            employeeBudget.setCreateTime(DateUtils.getNowDate());
            employeeBudget.setUpdateTime(DateUtils.getNowDate());
            employeeBudget.setUpdateBy(SecurityUtils.getUserId());
            employeeBudgetList.add(employeeBudget);
        }
        return employeeBudgetMapper.updateEmployeeBudgets(employeeBudgetList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importEmployeeBudget(List<EmployeeBudgetExcel> list) {
        List<EmployeeBudget> employeeBudgetList = new ArrayList<>();
        list.forEach(l -> {
            EmployeeBudget employeeBudget = new EmployeeBudget();
            BeanUtils.copyProperties(l, employeeBudget);
            employeeBudget.setCreateBy(SecurityUtils.getUserId());
            employeeBudget.setCreateTime(DateUtils.getNowDate());
            employeeBudget.setUpdateTime(DateUtils.getNowDate());
            employeeBudget.setUpdateBy(SecurityUtils.getUserId());
            employeeBudget.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            employeeBudgetList.add(employeeBudget);
        });
        try {
            employeeBudgetMapper.batchEmployeeBudget(employeeBudgetList);
        } catch (Exception e) {
            throw new ServiceException("导入人力预算表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param employeeBudgetDTO
     * @return
     */
    @Override
    public List<EmployeeBudgetExcel> exportEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO) {
        EmployeeBudget employeeBudget = new EmployeeBudget();
        BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
        List<EmployeeBudgetDTO> employeeBudgetDTOList = employeeBudgetMapper.selectEmployeeBudgetList(employeeBudget);
        List<EmployeeBudgetExcel> employeeBudgetExcelList = new ArrayList<>();
        return employeeBudgetExcelList;
    }

    /**
     * 查询增人/减人工资包列表
     *
     * @param employeeBudgetDTO
     * @return
     */
    @Override
    @DataScope(businessAlias = "ebd")
    public List<EmployeeBudgetDetailsDTO> salaryPackageList(EmployeeBudgetDTO employeeBudgetDTO) {
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
                            if (employeeBudgetDetailsDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                employeeBudgetDetailsDTO.setDepartmentName(datum.getDepartmentName());
                                break;
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
                            if (employeeBudgetDetailsDTO.getOfficialRankSystemId().equals(datum.getOfficialRankSystemId())) {
                                employeeBudgetDetailsDTO.setOfficialRankSystemName(datum.getOfficialRankSystemName());
                                employeeBudgetDetailsDTO.setOfficialRankName(datum.getRankPrefixCode() + employeeBudgetDetailsDTO.getOfficialRank());
                                break;
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
        List<EmployeeBudgetDetailsDTO> emolumentPlanDTOList = getEmployeeBudgetDetailsDTOS(employeeBudgetDTO, employeeBudgetDetailsDTOS);

        return emolumentPlanDTOList;
    }

    /**
     * 封装增人/减人工资模糊查询
     *
     * @param employeeBudgetDTO
     * @param employeeBudgetDetailsDTOS
     * @return
     */
    private List<EmployeeBudgetDetailsDTO> getEmployeeBudgetDetailsDTOS(EmployeeBudgetDTO employeeBudgetDTO, List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS) {
        List<EmployeeBudgetDetailsDTO> emolumentPlanDTOList = new ArrayList<>();
        //部门名称
        String departmentName1 = employeeBudgetDTO.getDepartmentName();
        //职级体系名称
        String officialRankSystemName1 = employeeBudgetDTO.getOfficialRankSystemName();
        //岗位职级名称
        String officialRankName1 = employeeBudgetDTO.getOfficialRankName();
        if (!CheckObjectIsNullUtils.isNull(employeeBudgetDTO)) {
            Pattern pattern = null;
            Pattern pattern1 = null;
            Pattern pattern2 = null;
            if (StringUtils.isNotBlank(departmentName1)) {
                //部门模糊查询
                pattern = Pattern.compile(departmentName1);
            }

            if (StringUtils.isNotBlank(officialRankSystemName1)) {
                //职级体系模糊查询
                pattern1 = Pattern.compile(officialRankSystemName1);
            }
            if (StringUtils.isNotBlank(officialRankName1)) {
                //部门模糊查询
                pattern2 = Pattern.compile(officialRankName1);
            }
            for (EmployeeBudgetDetailsDTO budgetDetailsDTO : employeeBudgetDetailsDTOS) {
                //部门名称
                Matcher departmentName = null;
                //职级体系名称
                Matcher officialRankSystemName = null;
                //职级体系名称
                Matcher officialRankName = null;
                if (StringUtils.isNotBlank(departmentName1)) {
                    //部门名称
                    departmentName = pattern.matcher(budgetDetailsDTO.getDepartmentName());
                }
                if (StringUtils.isNotBlank(officialRankSystemName1)) {
                    //职级体系名称
                    officialRankSystemName = pattern1.matcher(budgetDetailsDTO.getOfficialRankSystemName());
                }
                if (StringUtils.isNotBlank(officialRankName1)) {
                    //岗位职级名称
                    officialRankName = pattern2.matcher(budgetDetailsDTO.getOfficialRankName());
                }

                if (StringUtils.isNotBlank(departmentName1) && StringUtils.isNotBlank(officialRankSystemName1) && StringUtils.isNotBlank(officialRankName1)) {
                    if (departmentName.find() && officialRankSystemName.find() && officialRankName.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                        emolumentPlanDTOList.add(budgetDetailsDTO);
                    }
                }else {
                    if (StringUtils.isNotBlank(departmentName1) && StringUtils.isNotBlank(officialRankSystemName1) ){
                        if (departmentName.find() && officialRankSystemName.find() ) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                            emolumentPlanDTOList.add(budgetDetailsDTO);
                        }
                    }else   if (StringUtils.isNotBlank(officialRankName1) && StringUtils.isNotBlank(departmentName1) ){
                        if (departmentName.find() && officialRankName.find() ) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                            emolumentPlanDTOList.add(budgetDetailsDTO);
                        }
                    }else   if (StringUtils.isNotBlank(officialRankName1) && StringUtils.isNotBlank(officialRankSystemName1) ){
                        if (officialRankSystemName.find() && officialRankName.find() ) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                            emolumentPlanDTOList.add(budgetDetailsDTO);
                        }
                    }else {
                        if (StringUtils.isNotBlank(departmentName1)) {
                            if (departmentName.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                                emolumentPlanDTOList.add(budgetDetailsDTO);
                            }
                        }
                        if (StringUtils.isNotBlank(officialRankSystemName1)) {
                            if (officialRankSystemName.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                                emolumentPlanDTOList.add(budgetDetailsDTO);
                            }
                        }
                        if (StringUtils.isNotBlank(officialRankName1)) {
                            if (officialRankName.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                                emolumentPlanDTOList.add(budgetDetailsDTO);
                            }
                        }
                    }

                }
            }
            if (StringUtils.isNotBlank(departmentName1) || StringUtils.isNotBlank(officialRankSystemName1) || StringUtils.isNotBlank(officialRankName1)) {
                return emolumentPlanDTOList;
            }
        }
        return StringUtils.isNotEmpty(emolumentPlanDTOList) ? emolumentPlanDTOList : employeeBudgetDetailsDTOS;
    }

    /**
     * 导出增人/减人工资包
     *
     * @param employeeBudgetDTO
     * @return
     */
    @Override
    public List<EmployeeBudgetDetailsExcel> export(EmployeeBudgetDTO employeeBudgetDTO) {
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = this.salaryPackageList(employeeBudgetDTO);
        //导出增人/减人工资包
        List<EmployeeBudgetDetailsExcel> employeeBudgetDetailsExcelList = new ArrayList<>();
        for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
            if (null != employeeBudgetDetailsDTO.getAverageAdjust()){
                employeeBudgetDetailsDTO.setAverageAdjust(employeeBudgetDetailsDTO.getAverageAdjust().setScale(2, RoundingMode.HALF_UP));
            }
            if (null != employeeBudgetDetailsDTO.getAgePayAmountLastYear()){
                employeeBudgetDetailsDTO.setAgePayAmountLastYear(employeeBudgetDetailsDTO.getAgePayAmountLastYear().setScale(2, RoundingMode.HALF_UP));
            }
            if (null != employeeBudgetDetailsDTO.getIncreaseAndDecreasePay()){
                employeeBudgetDetailsDTO.setIncreaseAndDecreasePay(employeeBudgetDetailsDTO.getIncreaseAndDecreasePay().setScale(2, RoundingMode.HALF_UP));
            }
            EmployeeBudgetDetailsExcel employeeBudgetDetailsExcel = new EmployeeBudgetDetailsExcel();
            BeanUtils.copyProperties(employeeBudgetDetailsDTO, employeeBudgetDetailsExcel);

            employeeBudgetDetailsExcelList.add(employeeBudgetDetailsExcel);
        }
        return employeeBudgetDetailsExcelList;
    }

    /**
     * 根据职级体系ID集合查询预算表
     *
     * @param officialRankSystemIds 职级体系ID集合
     * @return List
     */
    @Override
    public List<EmployeeBudgetDTO> selectBySystemIds(List<Long> officialRankSystemIds) {
        return employeeBudgetMapper.selectEmployeeBudgetBySystemIds(officialRankSystemIds);
    }

    /**
     * 远程 根据部门ID集合查询预算表
     *
     * @param departmentIds
     * @return
     */
    @Override
    public List<EmployeeBudgetDTO> selectByDepartmentIds(List<Long> departmentIds) {
        List<EmployeeBudgetDTO> employeeBudgetDTOList = employeeBudgetMapper.selectByDepartmentIds(departmentIds);
        if (StringUtils.isNotEmpty(employeeBudgetDTOList)) {
            List<Long> officialRankSystemIds = employeeBudgetDTOList.stream().map(EmployeeBudgetDTO::getOfficialRankSystemId).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            R<List<OfficialRankSystemDTO>> officialRankSystemList = remoteOfficialRankSystemService.selectByIds(officialRankSystemIds, SecurityConstants.INNER);
            List<OfficialRankSystemDTO> data = officialRankSystemList.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (EmployeeBudgetDTO employeeBudgetDTO : employeeBudgetDTOList) {
                    for (OfficialRankSystemDTO datum : data) {
                        if (employeeBudgetDTO.getOfficialRankSystemId().equals(datum.getOfficialRankSystemId())) {
                            employeeBudgetDTO.setOfficialRankSystemName(datum.getOfficialRankSystemName());
                        }
                    }
                }
            }
        }

        return employeeBudgetDTOList;
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
                    OfficialRankEmolumentDTO officialRankEmolumentDTO = officialRankEmolumentMapper.selectOfficialRankEmolumentByRank(employeeBudgetDetailsDTO.getOfficialRankSystemId(), employeeBudgetDetailsDTO.getOfficialRank());
                    //人员id集合
                    List<Long> employeeIds = new ArrayList<>();
                    for (EmployeeDTO datum : data) {
                        //部门id 和个人职级相等
                        if (employeeBudgetDetailsDTO.getDepartmentId().equals(datum.getEmployeeDepartmentId()) &&
                                employeeBudgetDetailsDTO.getOfficialRank().equals(datum.getEmployeeRank())) {
                            //人员id
                            employeeIds.add(datum.getEmployeeId());
                        }//取职级确定薪酬中位数
                        else {

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

