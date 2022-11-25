package net.qixiaowei.operate.cloud.service.impl.employee;

import java.math.BigDecimal;
import java.util.List;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.domain.employee.EmployeeBudgetAdjusts;
import net.qixiaowei.operate.cloud.api.domain.employee.EmployeeBudgetDetails;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetAdjustsDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetAdjustsMapper;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetDetailsMapper;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.employee.EmployeeBudget;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetExcel;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.mapper.employee.EmployeeBudgetMapper;
import net.qixiaowei.operate.cloud.service.employee.IEmployeeBudgetService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;


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
        if (StringUtils.isNotNull(data1)){
            employeeBudgetDTO.setOfficialRankSystemName(data1.getOfficialRankSystemName());
        }

        //人力预算明细表集合
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.selectEmployeeBudgetDetailsByEmployeeBudgetId(employeeBudgetId);

        //岗位职级远程调用
        R<List<String>> listR = remoteOfficialRankSystemService.selectRankById(employeeBudgetDTO.getOfficialRankSystemId(), SecurityConstants.INNER);
        List<String> data2 = listR.getData();
        if (StringUtils.isNotEmpty(data2)){
            for (int i = 0; i < employeeBudgetDetailsDTOS.size(); i++) {
                employeeBudgetDetailsDTOS.get(i).setOfficialRankName(data2.get(i));
            }
        }

        List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getEmployeeBudgetDetailsId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetAdjustsMapper.selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsIds(collect);
            //根据人力预算明细表id分组
            Map<Long, List<EmployeeBudgetAdjustsDTO>> mapList = employeeBudgetAdjustsDTOS.parallelStream().collect(Collectors.groupingBy(EmployeeBudgetAdjustsDTO::getEmployeeBudgetDetailsId));


            //放入数据
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                BigDecimal annualAverageNum = new BigDecimal("0");
                //上年期末人数
                Integer numberLastYear = employeeBudgetDetailsDTO.getNumberLastYear();
                //平均新增人数
                BigDecimal averageAdjust = employeeBudgetDetailsDTO.getAverageAdjust();
                if (null != numberLastYear && null != averageAdjust){
                    annualAverageNum=new BigDecimal(numberLastYear.toString()).add(averageAdjust);
                }
                //年度平均人数 = 上年期末数+平均新增人数
                employeeBudgetDetailsDTO.setAnnualAverageNum(annualAverageNum);
                List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS1 = mapList.get(employeeBudgetDetailsDTO.getEmployeeBudgetDetailsId());
                //sterm流求和
                Integer reduce = employeeBudgetAdjustsDTOS1.stream().filter(Objects::nonNull).map(EmployeeBudgetAdjustsDTO::getNumberAdjust).reduce(0, Integer::sum);
                //年度规划人数
                employeeBudgetDetailsDTO.setAnnualPlanningNum(reduce);
                //年末总计
                employeeBudgetDetailsDTO.setEndYearSum(numberLastYear+reduce);
                employeeBudgetDetailsDTO.setEmployeeBudgetAdjustsDTOS(employeeBudgetAdjustsDTOS1);
            }
        }

        employeeBudgetDTO.setEmployeeBudgetDetailsDTOS(employeeBudgetDetailsDTOS);
        return employeeBudgetDTO;
    }

    /**
     * 查询人力预算表列表
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 人力预算表
     */
    @Override
    public List<EmployeeBudgetDTO> selectEmployeeBudgetList(EmployeeBudgetDTO employeeBudgetDTO) {
        EmployeeBudget employeeBudget = new EmployeeBudget();
        BeanUtils.copyProperties(employeeBudgetDTO, employeeBudget);
        List<EmployeeBudgetDTO> employeeBudgetDTOS = employeeBudgetMapper.selectEmployeeBudgetList(employeeBudget);
        //远程调用部门赋值
        List<Long> collect = employeeBudgetDTOS.stream().map(EmployeeBudgetDTO::getDepartmentId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
            List<DepartmentDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (EmployeeBudgetDTO budgetDTO : employeeBudgetDTOS) {
                    for (DepartmentDTO datum : data) {
                        if (budgetDTO.getDepartmentId() == datum.getDepartmentId()) {
                            budgetDTO.setDepartmentName(datum.getDepartmentName());
                        }
                    }
                }
            }
        }
        //远程调用职级赋值
        List<Long> collect1 = employeeBudgetDTOS.stream().map(EmployeeBudgetDTO::getOfficialRankSystemId).collect(Collectors.toList());
        R<List<OfficialRankSystemDTO>> listR = remoteOfficialRankSystemService.selectByIds(collect1, SecurityConstants.INNER);
        List<OfficialRankSystemDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)){
            for (EmployeeBudgetDTO budgetDTO : employeeBudgetDTOS) {
                for (OfficialRankSystemDTO datum : data) {
                    if (budgetDTO.getOfficialRankSystemId() == datum.getOfficialRankSystemId()){
                        budgetDTO.setOfficialRankSystemName(datum.getOfficialRankSystemName());
                    }
                }
            }
        }

        BigDecimal annualAverageNum = new BigDecimal("0");
        for (EmployeeBudgetDTO budgetDTO : employeeBudgetDTOS) {
            Integer amountLastYear = budgetDTO.getAmountLastYear();
            BigDecimal amountAverageAdjust = budgetDTO.getAmountAverageAdjust();
            if (null != amountLastYear && null != amountAverageAdjust){
                annualAverageNum = new BigDecimal(amountLastYear.toString()).add(amountAverageAdjust);
            }
            //年度平均人数 = 上年期末数+平均新增人数
            budgetDTO.setAnnualAverageNum(annualAverageNum);
        }
        return employeeBudgetDTOS;
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
            throw new ServiceException("已存在相同预算周期！");
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
     * @param employeeBudget
     * @param employeeBudgetDetailsDTOS
     */
    private void packNum(EmployeeBudget employeeBudget, List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS) {
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            //人力预算详情表
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetDetailsDTO.getEmployeeBudgetAdjustsDTOS();
                if (StringUtils.isNotEmpty(employeeBudgetAdjustsDTOS)){
                    //人力预算详情明细表
                    for (EmployeeBudgetAdjustsDTO employeeBudgetAdjustsDTO : employeeBudgetAdjustsDTOS) {
                        //本年新增人数
                        Integer countAdjust = 0;
                        //本年新增人数
                        countAdjust = countAdjust + employeeBudgetAdjustsDTO.getNumberAdjust();
                        employeeBudgetDetailsDTO.setCountAdjust(countAdjust);
                    }
                }
            }
            //上年期末人数
            Integer amountLastYear = 0;
            //本年新增人数
            Integer amountAdjust = 0;
            //平均新增数
            BigDecimal amountAverageAdjust = new BigDecimal("0");
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                //上年期末人数
                amountLastYear = amountLastYear + employeeBudgetDetailsDTO.getNumberLastYear();
                //本年新增人数
                amountAdjust = amountLastYear + employeeBudgetDetailsDTO.getCountAdjust();
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
    public int updateEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO) {
        //人力预算明细表集合
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDTO.getEmployeeBudgetDetailsDTOS();
        //修改人力预算调整表集合
        List<EmployeeBudgetAdjusts> employeeBudgetAdjustsList = new ArrayList<>();
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)) {
            for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
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
            }
            return employeeBudgetAdjustsMapper.updateEmployeeBudgetAdjustss(employeeBudgetAdjustsList);
        }
        return 0;
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
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)){
            List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getEmployeeBudgetDetailsId).collect(Collectors.toList());

            try {
                if (StringUtils.isNotEmpty(collect)){
                    employeeBudgetDetailsMapper.logicDeleteEmployeeBudgetDetailsByEmployeeBudgetDetailsIds(collect,SecurityUtils.getUserId(),DateUtils.getNowDate());
                }

            } catch (Exception e) {
                throw new ServiceException("删除人力预算详情表失败");
            }
            //删除明细表
            List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetAdjustsMapper.selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsIds(collect);
            List<Long> collect1 = employeeBudgetAdjustsDTOS.stream().map(EmployeeBudgetAdjustsDTO::getEmployeeBudgetAdjustsId).collect(Collectors.toList());
            try {
                if (StringUtils.isNotEmpty(collect1)){
                    employeeBudgetAdjustsMapper.logicDeleteEmployeeBudgetAdjustsByEmployeeBudgetAdjustsIds(collect1,SecurityUtils.getUserId(),DateUtils.getNowDate());
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
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)){
            List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getEmployeeBudgetDetailsId).collect(Collectors.toList());

            try {
                if (StringUtils.isNotEmpty(collect)){
                    employeeBudgetDetailsMapper.logicDeleteEmployeeBudgetDetailsByEmployeeBudgetDetailsIds(collect,SecurityUtils.getUserId(),DateUtils.getNowDate());
                }

            } catch (Exception e) {
                throw new ServiceException("删除人力预算详情表失败");
            }
            //删除明细表
            List<EmployeeBudgetAdjustsDTO> employeeBudgetAdjustsDTOS = employeeBudgetAdjustsMapper.selectEmployeeBudgetAdjustsByEmployeeBudgetDetailsIds(collect);
            List<Long> collect1 = employeeBudgetAdjustsDTOS.stream().map(EmployeeBudgetAdjustsDTO::getEmployeeBudgetAdjustsId).collect(Collectors.toList());
            try {
                if (StringUtils.isNotEmpty(collect1)){
                    employeeBudgetAdjustsMapper.logicDeleteEmployeeBudgetAdjustsByEmployeeBudgetAdjustsIds(collect1,SecurityUtils.getUserId(),DateUtils.getNowDate());
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
     * @param employeeBudgetDTO
     * @return
     */
    @Override
    public List<EmployeeBudgetDetailsDTO> salaryPackageList(EmployeeBudgetDTO employeeBudgetDTO) {
        List<EmployeeBudgetDetailsDTO> employeeBudgetDetailsDTOS = employeeBudgetDetailsMapper.salaryPackageList(employeeBudgetDTO);
        if (StringUtils.isNotEmpty(employeeBudgetDetailsDTOS)){
            //部门id去重
            List<Long> collect = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getDepartmentId).distinct().collect(Collectors.toList());
            //远程调用组织
            if (StringUtils.isNotEmpty(collect)){
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)){
                    //远程赋值部门名称
                    for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (employeeBudgetDetailsDTO.getDepartmentId() == datum.getDepartmentId()){
                                employeeBudgetDetailsDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }
            //职级id去重
            List<Long> collect1 = employeeBudgetDetailsDTOS.stream().map(EmployeeBudgetDetailsDTO::getOfficialRankSystemId).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect1)){
                //职级体系远程调用
                R<List<OfficialRankSystemDTO>> listR = remoteOfficialRankSystemService.selectByIds(collect1, SecurityConstants.INNER);
                List<OfficialRankSystemDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)){
                    for (EmployeeBudgetDetailsDTO employeeBudgetDetailsDTO : employeeBudgetDetailsDTOS) {
                        //远程赋值职级名称
                        for (OfficialRankSystemDTO datum : data) {
                            if (employeeBudgetDetailsDTO.getOfficialRankSystemId() == datum.getOfficialRankSystemId()){
                                employeeBudgetDetailsDTO.setOfficialRankSystemName(datum.getOfficialRankSystemName());
                            }
                        }
                    }
                }
            }

        }

        return employeeBudgetDetailsMapper.salaryPackageList(employeeBudgetDTO);
    }
}

