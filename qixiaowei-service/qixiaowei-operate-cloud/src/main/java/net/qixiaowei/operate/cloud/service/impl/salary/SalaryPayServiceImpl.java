package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPay;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryStructureDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayExcel;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayDetailsService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayService;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * SalaryPayService业务层处理
 *
 * @author Graves
 * @since 2022-11-17
 */
@Service
public class SalaryPayServiceImpl implements ISalaryPayService {
    @Autowired
    private SalaryPayMapper salaryPayMapper;

    @Autowired
    private ISalaryPayDetailsService salaryPayDetailsService;

    @Autowired
    private ISalaryItemService salaryItemService;

    @Autowired
    RemoteEmployeeService employeeService;

    /**
     * 查询工资发薪表
     *
     * @param salaryPayId 工资发薪表主键
     * @return 工资发薪表
     */
    @Override
    public SalaryPayDTO selectSalaryPayBySalaryPayId(Long salaryPayId) {
        if (StringUtils.isNull(salaryPayId)) {
            throw new ServiceException("请输入ID");
        }
        SalaryPayDTO salaryPayDTO = salaryPayMapper.selectSalaryPayBySalaryPayId(salaryPayId);
        if (StringUtils.isNull(salaryPayDTO)) {
            throw new ServiceException("当前数据已不存在");
        }
        Long employeeId = salaryPayDTO.getEmployeeId();
        // 为员工赋值
        R<EmployeeDTO> employeeDTOR = employeeService.selectByEmployeeId(employeeId, SecurityConstants.INNER);
        EmployeeDTO employeeDTO = employeeDTOR.getData();
        if (employeeDTOR.getCode() != 200) {
            throw new ServiceException("远程获取人员信息失败");
        }
        if (StringUtils.isNull(employeeDTO)) {
            throw new ServiceException("当前人员信息已不存在，请检查员工配置");
        }
        salaryPayDTO.setEmployeeName(employeeDTO.getEmployeeName());
        salaryPayDTO.setEmployeeCode(employeeDTO.getEmployeeCode());
        salaryPayDTO.setDepartmentName(employeeDTO.getEmployeeDepartmentName());
        salaryPayDTO.setPostName(employeeDTO.getEmployeePostName());
        salaryPayDTO.setEmployeeName(employeeDTO.getEmployeeName());
        List<SalaryPayDetailsDTO> salaryPayDetailsDTOList = salaryPayDetailsService.selectSalaryPayDetailsBySalaryPayId(salaryPayId);
        List<SalaryItemDTO> salaryItemDTOS = salaryItemService.selectSalaryItemList(new SalaryItemDTO());
        if (StringUtils.isEmpty(salaryItemDTOS)) {
            throw new ServiceException("当前工资项未进行任何配置，请联系管理员");
        }
        if (StringUtils.isEmpty(salaryPayDetailsDTOList)) {
            // 造假数据
            // 月度工资发薪统计
            List<SalaryPayDTO> salaryPayDTOList = new ArrayList<>();
            SalaryPayDTO innerSalaryDTO = new SalaryPayDTO();
            innerSalaryDTO.setSalaryAmount(BigDecimal.ZERO);
            innerSalaryDTO.setAllowanceAmount(BigDecimal.ZERO);
            innerSalaryDTO.setWelfareAmount(BigDecimal.ZERO);
            innerSalaryDTO.setOtherDeductions(BigDecimal.ZERO);
            innerSalaryDTO.setBonusAmount(BigDecimal.ZERO);
            innerSalaryDTO.setWithholdRemitTax(BigDecimal.ZERO);
            innerSalaryDTO.setPayAmount(BigDecimal.ZERO);
            salaryPayDTOList.add(innerSalaryDTO);
            salaryPayDTO.setSalaryPayDTOList(salaryPayDTOList);
            // 月度工资发薪明细表
            for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                salaryItemService.salarySetName(salaryItemDTO);
                salaryItemDTO.setAmount(BigDecimal.ZERO);
            }
            salaryPayDTO.setSalaryItemDTOList(salaryItemDTOS);
            return salaryPayDTO;
        }
        // 月度工资发薪统计
        List<SalaryPayDTO> salaryPayDTOList = new ArrayList<>();
        SalaryPayDTO innerSalaryDTO = new SalaryPayDTO();
        innerSalaryDTO.setSalaryAmount(salaryPayDTO.getSalaryAmount());
        innerSalaryDTO.setAllowanceAmount(salaryPayDTO.getAllowanceAmount());
        innerSalaryDTO.setWelfareAmount(salaryPayDTO.getWelfareAmount());
        innerSalaryDTO.setOtherDeductions(salaryPayDTO.getOtherDeductions());
        innerSalaryDTO.setBonusAmount(salaryPayDTO.getBonusAmount());
        innerSalaryDTO.setWithholdRemitTax(salaryPayDTO.getWithholdRemitTax());
        innerSalaryDTO.setPayAmount(salaryPayDTO.getPayAmount());
        salaryPayDTOList.add(innerSalaryDTO);
        salaryPayDTO.setSalaryPayDTOList(salaryPayDTOList);
        // 月度工资发薪明细表
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
            salaryItemService.salarySetName(salaryItemDTO);
            for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOList) {
                if (salaryItemDTO.getSalaryItemId().equals(salaryPayDetailsDTO.getSalaryItemId())) {
                    salaryItemDTO.setAmount(salaryPayDetailsDTO.getAmount());
                    break;
                }
            }
        }
        salaryPayDTO.setSalaryItemDTOList(salaryItemDTOS);
        return salaryPayDTO;
    }

    /**
     * 查询工资发薪表列表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 工资发薪表
     */
    @Override
    public List<SalaryPayDTO> selectSalaryPayList(SalaryPayDTO salaryPayDTO) {
        List<SalaryPayDTO> salaryPayDTOList = new ArrayList<>();
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String postName = salaryPayDTO.getPostName();
        String employeeName = salaryPayDTO.getEmployeeName();
        String departmentName = salaryPayDTO.getDepartmentName();
        String employeeCode = salaryPayDTO.getEmployeeCode();
        employeeDTO.setEmployeeName(employeeName);
        employeeDTO.setEmployeeCode(employeeCode);
        employeeDTO.setEmployeePostName(postName);
        employeeDTO.setEmployeeDepartmentName(departmentName);
        if (CheckObjectIsNullUtils.isNull(employeeDTO)) {
            return salaryPayMapper.selectSalaryPayList(new SalaryPay());
        }
        R<List<EmployeeDTO>> employeeDTOR = employeeService.selectRemoteList(employeeDTO, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = employeeDTOR.getData();
        if (employeeDTOR.getCode() != 200) {
            throw new ServiceException("远程获取人员信息失败");
        }
        List<Long> employeeIds = new ArrayList<>();
        if (StringUtils.isNotEmpty(employeeDTOS)) {
            for (EmployeeDTO dto : employeeDTOS) {
                employeeIds.add(dto.getEmployeeId());
            }
        } else {
            return salaryPayDTOList;
        }
        salaryPayDTOList = salaryPayMapper.selectSalaryPayByEmployeeIds(employeeIds);
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            return salaryPayDTOList;
        } else {
            for (SalaryPayDTO payDTO : salaryPayDTOList) {
                for (EmployeeDTO dto : employeeDTOS) {
                    if (payDTO.getEmployeeId().equals(dto.getEmployeeId())) {
                        payDTO.setEmployeeName(dto.getEmployeeName());
                        payDTO.setEmployeeCode(dto.getEmployeeCode());
                        payDTO.setDepartmentName(dto.getEmployeeDepartmentName());
                        payDTO.setPostName(dto.getEmployeePostName());
                        payDTO.setEmployeeName(dto.getEmployeeName());
                        break;
                    }
                }
            }
            return salaryPayDTOList;
        }
    }

    /**
     * 新增工资发薪表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 结果
     */
    @Override
    @Transactional
    public SalaryPayDTO insertSalaryPay(SalaryPayDTO salaryPayDTO) {
        insertCheck(salaryPayDTO);
        int year = salaryPayDTO.getPayYear();
        int month = salaryPayDTO.getPayMonth();
        List<SalaryPayDTO> salaryPayDTOList = salaryPayDTO.getSalaryPayDTOList();
        List<SalaryItemDTO> salaryItemDTOList = salaryPayDTO.getSalaryItemDTOList();
        SalaryPay salaryPay = new SalaryPay();
        BeanUtils.copyProperties(salaryPayDTO, salaryPay);
        for (SalaryPayDTO payDTO : salaryPayDTOList) {
            salaryPay.setSalaryAmount(payDTO.getSalaryAmount());
            salaryPay.setAllowanceAmount(payDTO.getAllowanceAmount());
            salaryPay.setWelfareAmount(payDTO.getWelfareAmount());
            salaryPay.setOtherDeductions(payDTO.getOtherDeductions());
            salaryPay.setBonusAmount(payDTO.getBonusAmount());
            salaryPay.setWithholdRemitTax(payDTO.getWithholdRemitTax());
            salaryPay.setPayAmount(payDTO.getPayAmount());
        }
        salaryPay.setPayYear(year);
        salaryPay.setPayMonth(month);
        salaryPay.setCreateBy(SecurityUtils.getUserId());
        salaryPay.setCreateTime(DateUtils.getNowDate());
        salaryPay.setUpdateTime(DateUtils.getNowDate());
        salaryPay.setUpdateBy(SecurityUtils.getUserId());
        salaryPay.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        salaryPayMapper.insertSalaryPay(salaryPay);
        Long salaryPayId = salaryPay.getSalaryPayId();
        List<SalaryPayDetailsDTO> salaryPayDetailsS = new ArrayList<>();
//       salaryItemDTOList遍历，拿出salaryItemId集合，去SalaryItem表查询list(有必要的话需要进行一次校验，比较size大小)
        salaryPaySort(salaryItemDTOList, salaryPayId, salaryPayDetailsS);
        salaryPayDetailsService.insertSalaryPayDetailss(salaryPayDetailsS);
        salaryPayDTO.setSalaryPayId(salaryPayId);
        return salaryPayDTO;
    }

    /**
     * 给详情表进行排序
     *
     * @param salaryItemDTOList 工资项列表
     * @param salaryPayId       工资ID
     * @param salaryPayDetailsS 工资详情
     */
    private static void salaryPaySort(List<SalaryItemDTO> salaryItemDTOList, Long salaryPayId, List<SalaryPayDetailsDTO> salaryPayDetailsS) {
        int sort = 0;//排序用
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOList) {
            SalaryPayDetailsDTO salaryPayDetailsDTO = new SalaryPayDetailsDTO();
            salaryPayDetailsDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
            salaryPayDetailsDTO.setAmount(salaryItemDTO.getAmount());
            salaryPayDetailsDTO.setSalaryPayId(salaryPayId);
            salaryPayDetailsDTO.setSort(sort);
            sort++;
            salaryPayDetailsS.add(salaryPayDetailsDTO);
        }
    }

    /**
     * 新增校验
     *
     * @param salaryPayDTO dto
     */
    private void insertCheck(SalaryPayDTO salaryPayDTO) {
        Long employeeId = salaryPayDTO.getEmployeeId();
        int year = salaryPayDTO.getPayYear();
        int month = salaryPayDTO.getPayMonth();
        List<SalaryPayDTO> salaryPayDTOList = salaryPayDTO.getSalaryPayDTOList();
        List<SalaryItemDTO> salaryItemDTOList = salaryPayDTO.getSalaryItemDTOList();
        SalaryPay salaryPayByTime = new SalaryPay();
        salaryPayByTime.setEmployeeId(employeeId);
        salaryPayByTime.setPayYear(year);
        salaryPayByTime.setPayMonth(month);
        if (StringUtils.isNull(employeeId)) {
            throw new ServiceException("员工不能为空");
        }
        R<EmployeeDTO> employeeDTOR = employeeService.selectByEmployeeId(employeeId, SecurityConstants.INNER);
        EmployeeDTO employeeDTO = employeeDTOR.getData();
        if (employeeDTOR.getCode() != 200) {
            throw new ServiceException("员工不能为空");
        }
        if (StringUtils.isNull(employeeDTO)) {
            throw new ServiceException("该员工信息不存在");
        }
        List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectSalaryPayList(salaryPayByTime);
        if (StringUtils.isNotEmpty(salaryPayDTOS)) {
            throw new ServiceException("已存在当前月份的员工");
        }
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            throw new ServiceException("月度工资数据不可以为空");
        }
        if (salaryPayDTOList.size() > 1) {
            throw new ServiceException("月度工资数据只能为一行");
        }
        if (StringUtils.isEmpty(salaryItemDTOList)) {
            throw new ServiceException("月度工资数据详情不可以为空");
        }
        List<Long> salaryItemIds = new ArrayList<>();
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOList) {
            salaryItemIds.add(salaryItemDTO.getSalaryItemId());
        }
        List<SalaryItemDTO> salaryItemDTOS = salaryItemService.selectSalaryItemBySalaryItemIds(salaryItemIds);
        if (salaryItemDTOS.size() != salaryItemIds.size()) {
            throw new ServiceException("工资数据详情数据异常");
        }
    }

    /**
     * 修改工资发薪表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateSalaryPay(SalaryPayDTO salaryPayDTO) {
        updateCheck(salaryPayDTO);
        Long salaryPayId = salaryPayDTO.getSalaryPayId();
        List<SalaryPayDTO> salaryPayDTOList = salaryPayDTO.getSalaryPayDTOList();
        List<SalaryItemDTO> salaryItemDTOList = salaryPayDTO.getSalaryItemDTOList();
        int year = salaryPayDTO.getPayYear();
        int month = salaryPayDTO.getPayMonth();
        SalaryPay salaryPay = new SalaryPay();
        BeanUtils.copyProperties(salaryPayDTO, salaryPay);
        for (SalaryPayDTO payDTO : salaryPayDTOList) {
            salaryPay.setSalaryAmount(payDTO.getSalaryAmount());
            salaryPay.setAllowanceAmount(payDTO.getAllowanceAmount());
            salaryPay.setWelfareAmount(payDTO.getWelfareAmount());
            salaryPay.setOtherDeductions(payDTO.getOtherDeductions());
            salaryPay.setBonusAmount(payDTO.getBonusAmount());
            salaryPay.setWithholdRemitTax(payDTO.getWithholdRemitTax());
            salaryPay.setPayAmount(payDTO.getPayAmount());
        }
        salaryPay.setPayYear(year);
        salaryPay.setPayMonth(month);
        salaryPay.setUpdateTime(DateUtils.getNowDate());
        salaryPay.setUpdateBy(SecurityUtils.getUserId());
        salaryPay.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        // 更新详情表
        //如果以后前端不穿ID的话，可以用salaryPayDetailDTOBefore的SalaryItemId来重新获取beforeList的ID
        List<SalaryPayDetailsDTO> salaryPayDetailsDTOBefore = salaryPayDetailsService.selectSalaryPayDetailsBySalaryPayId(salaryPayId);
        List<SalaryPayDetailsDTO> salaryPayDetailsDTOAfter = new ArrayList<>();
        int sort = 0;
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOList) {
            SalaryPayDetailsDTO salaryPayDetailsDTO = new SalaryPayDetailsDTO();
            salaryPayDetailsDTO.setSort(sort);
            salaryPayDetailsDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
            salaryPayDetailsDTO.setAmount(salaryItemDTO.getAmount());
            salaryPayDetailsDTO.setSalaryPayId(salaryPayId);
            salaryPayDetailsDTOAfter.add(salaryPayDetailsDTO);
            sort++;
        }
        //给After添加上SalaryPayDetailsId
        for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOBefore) {
            for (SalaryPayDetailsDTO payDetailsDTO : salaryPayDetailsDTOAfter) {
                Long salaryItemId = salaryPayDetailsDTO.getSalaryItemId();
                if (salaryItemId.equals(payDetailsDTO.getSalaryItemId())) {
                    payDetailsDTO.setSalaryPayDetailsId(salaryPayDetailsDTO.getSalaryPayDetailsId());
                }
            }
        }
        setSalaryValue(salaryPayDetailsDTOBefore, salaryPayDetailsDTOAfter);
        return salaryPayMapper.updateSalaryPay(salaryPay);
    }

    /**
     * 给详情表赋值
     *
     * @param salaryPayDetailsDTOBefore 库里的值
     * @param salaryPayDetailsDTOAfter  要更新的值
     */
    private void setSalaryValue(List<SalaryPayDetailsDTO> salaryPayDetailsDTOBefore, List<SalaryPayDetailsDTO> salaryPayDetailsDTOAfter) {
        // 交集
        List<SalaryPayDetailsDTO> updateSalaryPayDetails =
                salaryPayDetailsDTOAfter.stream().filter(salaryPayDetailsDTO ->
                        salaryPayDetailsDTOBefore.stream().map(SalaryPayDetailsDTO::getSalaryItemId)
                                .collect(Collectors.toList()).contains(salaryPayDetailsDTO.getSalaryItemId())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(updateSalaryPayDetails)) {
            salaryPayDetailsService.updateSalaryPayDetailss(updateSalaryPayDetails);
        }
        // 差集 Before中After的补集
        List<SalaryPayDetailsDTO> delSalaryPayDetails =
                salaryPayDetailsDTOBefore.stream().filter(salaryPayDetailsDTO ->
                        !salaryPayDetailsDTOAfter.stream().map(SalaryPayDetailsDTO::getSalaryItemId)
                                .collect(Collectors.toList()).contains(salaryPayDetailsDTO.getSalaryItemId())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(delSalaryPayDetails)) {
            List<Long> salaryPayDetailsIds = new ArrayList<>();
            for (SalaryPayDetailsDTO delSalaryPayDetail : delSalaryPayDetails) {
                salaryPayDetailsIds.add(delSalaryPayDetail.getSalaryPayDetailsId());
            }
            salaryPayDetailsService.logicDeleteSalaryPayDetailsBySalaryPayDetailsIds(salaryPayDetailsIds);
        }
        // 差集 After中Before的补集
        List<SalaryPayDetailsDTO> addSalaryPayDetails =
                salaryPayDetailsDTOAfter.stream().filter(salaryPayDetailsDTO ->
                        !salaryPayDetailsDTOBefore.stream().map(SalaryPayDetailsDTO::getSalaryItemId)
                                .collect(Collectors.toList()).contains(salaryPayDetailsDTO.getSalaryItemId())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(addSalaryPayDetails)) {
            salaryPayDetailsService.insertSalaryPayDetailss(addSalaryPayDetails);
        }
    }

    /**
     * 更新校验
     *
     * @param salaryPayDTO
     */
    private void updateCheck(SalaryPayDTO salaryPayDTO) {
        Long salaryPayId = salaryPayDTO.getSalaryPayId();
        Long employeeId = salaryPayDTO.getEmployeeId();
        int year = salaryPayDTO.getPayYear();
        int month = salaryPayDTO.getPayMonth();
        List<SalaryPayDTO> salaryPayDTOList = salaryPayDTO.getSalaryPayDTOList();
        List<SalaryItemDTO> salaryItemDTOList = salaryPayDTO.getSalaryItemDTOList();
        SalaryPay salaryPayByTime = new SalaryPay();
        salaryPayByTime.setEmployeeId(employeeId);
        salaryPayByTime.setPayYear(year);
        salaryPayByTime.setPayMonth(month);
        if (StringUtils.isNull(salaryPayId)) {
            throw new ServiceException("工资表ID不能为空");
        }
        if (StringUtils.isNull(employeeId)) {
            throw new ServiceException("员工不能为空");
        }
        R<EmployeeDTO> employeeDTOR = employeeService.selectByEmployeeId(employeeId, SecurityConstants.INNER);
        EmployeeDTO employeeDTO = employeeDTOR.getData();
        if (employeeDTOR.getCode() != 200) {
            throw new ServiceException("员工不能为空");
        }
        if (StringUtils.isNull(employeeDTO)) {
            throw new ServiceException("该员工信息不存在");
        }
        List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectSalaryPayList(salaryPayByTime);
        if (StringUtils.isNotEmpty(salaryPayDTOS)) {
            if (!salaryPayDTOS.get(0).getSalaryPayId().equals(salaryPayId)) {
                throw new ServiceException("已存在当前月份的员工");
            }
        }
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            throw new ServiceException("月度工资数据不可以为空");
        }
        if (salaryPayDTOList.size() > 1) {
            throw new ServiceException("月度工资数据只能为一行");
        }
        if (StringUtils.isEmpty(salaryItemDTOList)) {
            throw new ServiceException("月度工资数据详情不可以为空");
        }
        List<Long> salaryItemIds = new ArrayList<>();
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOList) {
            salaryItemIds.add(salaryItemDTO.getSalaryItemId());
        }
        List<SalaryItemDTO> salaryItemDTOS = salaryItemService.selectSalaryItemBySalaryItemIds(salaryItemIds);
        if (salaryItemDTOS.size() != salaryItemIds.size()) {
            throw new ServiceException("工资数据详情数据异常");
        }
    }

    /**
     * 更新校验
     *
     * @param payTime           时间
     * @param employeeId        员工ID
     * @param salaryPayDTOList  工资发薪列表
     * @param salaryItemDTOList 工资项列表
     * @param salaryPayByTime   工资时间
     */
    private void updateCheck(Date payTime, Long employeeId, List<SalaryPayDTO> salaryPayDTOList, List<SalaryItemDTO> salaryItemDTOList, SalaryPay salaryPayByTime) {
        if (StringUtils.isNull(payTime)) {
            throw new ServiceException("时间不能为空");
        }
        if (StringUtils.isNull(employeeId)) {
            throw new ServiceException("员工不能为空");
        }
        List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectSalaryPayList(salaryPayByTime);
        if (StringUtils.isNotEmpty(salaryPayDTOS)) {
            throw new ServiceException("已存在当前月份的员工");
        }
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            throw new ServiceException("月度工资数据不可以为空");
        }
        if (salaryPayDTOList.size() > 1) {
            throw new ServiceException("月度工资数据只能为一行");
        }
        if (StringUtils.isEmpty(salaryItemDTOList)) {
            throw new ServiceException("月度工资数据详情不可以为空");
        }
        List<Long> salaryItemIds = new ArrayList<>();
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOList) {
            salaryItemIds.add(salaryItemDTO.getSalaryItemId());
        }
        List<SalaryItemDTO> salaryItemDTOS = salaryItemService.selectSalaryItemBySalaryItemIds(salaryItemIds);
        if (salaryItemDTOS.size() != salaryItemIds.size()) {
            throw new ServiceException("工资数据详情数据异常");
        }
    }

    /**
     * 逻辑批量删除工资发薪表
     *
     * @param salaryPayIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteSalaryPayBySalaryPayIds(List<Long> salaryPayIds) {
        salaryPayMapper.logicDeleteSalaryPayBySalaryPayIds(salaryPayIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        return salaryPayDetailsService.logicDeleteSalaryPayDetailsBySalaryPayIds(salaryPayIds);
    }

    /**
     * 物理删除工资发薪表信息
     *
     * @param salaryPayId 工资发薪表主键
     * @return 结果
     */
    @Override
    @Transactional
    public int deleteSalaryPayBySalaryPayId(Long salaryPayId) {
        return salaryPayMapper.deleteSalaryPayBySalaryPayId(salaryPayId);
    }

    /**
     * 逻辑删除工资发薪表信息
     *
     * @param salaryPayDTO 工资发薪表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteSalaryPayBySalaryPayId(SalaryPayDTO salaryPayDTO) {
        Long salaryPayId = salaryPayDTO.getSalaryPayId();
        SalaryPayDTO salaryPayById = salaryPayMapper.selectSalaryPayBySalaryPayId(salaryPayId);
        if (StringUtils.isNull(salaryPayById)) {
            throw new ServiceException("该数据已不存在");
        }
        salaryPayDetailsService.logicDeleteSalaryPayDetailsBySalaryPayId(salaryPayId);
        SalaryPay salaryPay = new SalaryPay();
        salaryPay.setSalaryPayId(salaryPayId);
        salaryPay.setUpdateTime(DateUtils.getNowDate());
        salaryPay.setUpdateBy(SecurityUtils.getUserId());
        return salaryPayMapper.logicDeleteSalaryPayBySalaryPayId(salaryPay);
    }

    /**
     * 物理删除工资发薪表信息
     *
     * @param salaryPayDTO 工资发薪表
     * @return 结果
     */
    @Override
    public int deleteSalaryPayBySalaryPayId(SalaryPayDTO salaryPayDTO) {
        SalaryPay salaryPay = new SalaryPay();
        BeanUtils.copyProperties(salaryPayDTO, salaryPay);
        return salaryPayMapper.deleteSalaryPayBySalaryPayId(salaryPay.getSalaryPayId());
    }

    /**
     * 物理批量删除工资发薪表
     *
     * @param salaryPayDtos 需要删除的工资发薪表主键
     * @return 结果
     */

    @Override
    public int deleteSalaryPayBySalaryPayIds(List<SalaryPayDTO> salaryPayDtos) {
        List<Long> stringList = new ArrayList<>();
        for (SalaryPayDTO salaryPayDTO : salaryPayDtos) {
            stringList.add(salaryPayDTO.getSalaryPayId());
        }
        return salaryPayMapper.deleteSalaryPayBySalaryPayIds(stringList);
    }

    /**
     * 批量新增工资发薪表信息
     *
     * @param salaryPayDtos 工资发薪表对象
     */

    public int insertSalaryPays(List<SalaryPayDTO> salaryPayDtos) {
        List<SalaryPay> salaryPayList = new ArrayList<>();
        for (SalaryPayDTO salaryPayDTO : salaryPayDtos) {
            SalaryPay salaryPay = new SalaryPay();
            BeanUtils.copyProperties(salaryPayDTO, salaryPay);
            salaryPay.setCreateBy(SecurityUtils.getUserId());
            salaryPay.setCreateTime(DateUtils.getNowDate());
            salaryPay.setUpdateTime(DateUtils.getNowDate());
            salaryPay.setUpdateBy(SecurityUtils.getUserId());
            salaryPay.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            salaryPayList.add(salaryPay);
        }
        return salaryPayMapper.batchSalaryPay(salaryPayList);
    }

    /**
     * 批量修改工资发薪表信息
     *
     * @param salaryPayDtos 工资发薪表对象
     */

    public int updateSalaryPays(List<SalaryPayDTO> salaryPayDtos) {
        List<SalaryPay> salaryPayList = new ArrayList<>();

        for (SalaryPayDTO salaryPayDTO : salaryPayDtos) {
            SalaryPay salaryPay = new SalaryPay();
            BeanUtils.copyProperties(salaryPayDTO, salaryPay);
            salaryPay.setCreateBy(SecurityUtils.getUserId());
            salaryPay.setCreateTime(DateUtils.getNowDate());
            salaryPay.setUpdateTime(DateUtils.getNowDate());
            salaryPay.setUpdateBy(SecurityUtils.getUserId());
            salaryPayList.add(salaryPay);
        }
        return salaryPayMapper.updateSalaryPays(salaryPayList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    @Transactional
    public void importSalaryPay(List<Map<Integer, String>> list) {
        List<SalaryPay> salaryPayList = new ArrayList<>();
        Map<Integer, String> head = list.get(0);
        list.remove(0);
        if (StringUtils.isEmpty(list)) {
            throw new ServiceException("数据为空");
        }
        list.forEach(map -> {
            for (Integer index : map.keySet()) {
                if (StringUtils.isNull(map.get(index))) {
                    map.put(index, "0");
                }
            }
        });
//        SalaryPay salaryPay = new SalaryPay();
//        BeanUtils.copyProperties(map, salaryPay);
//        salaryPay.setCreateBy(SecurityUtils.getUserId());
//        salaryPay.setCreateTime(DateUtils.getNowDate());
//        salaryPay.setUpdateTime(DateUtils.getNowDate());
//        salaryPay.setUpdateBy(SecurityUtils.getUserId());
//        salaryPay.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
//        salaryPayList.add(salaryPay);

        try {
//            salaryPayMapper.batchSalaryPay(salaryPayList);
        } catch (Exception e) {
            throw new ServiceException("导入工资发薪表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param salaryPayDTO
     * @return
     */
    @Override
    public List<SalaryPayExcel> exportSalaryPay(SalaryPayDTO salaryPayDTO) {
        SalaryPay salaryPay = new SalaryPay();
        BeanUtils.copyProperties(salaryPayDTO, salaryPay);
        List<SalaryPayDTO> salaryPayDTOList = salaryPayMapper.selectSalaryPayList(salaryPay);
        List<SalaryPayExcel> salaryPayExcelList = new ArrayList<>();
        return salaryPayExcelList;
    }

    /**
     * 查询薪酬架构报表列表
     *
     * @param salaryStructureDTO 薪酬架构DTO
     * @return
     */
    @Override
    public SalaryStructureDTO selectSalaryPayStructureList(SalaryStructureDTO salaryStructureDTO) {
        // 默认当前年份的1月到现在的月份
        // 默认当前所有部门
        SalaryStructureDTO salaryStructure = new SalaryStructureDTO();
        if (CheckObjectIsNullUtils.isNull(salaryStructureDTO)) {
            int month = DateUtils.getMonth();
            int year = DateUtils.getYear();
            List<SalaryPayDTO> salaryPayDTOList = salaryPayMapper.selectSalaryPayBySomeMonth(year, 0, month);
            BigDecimal salaryAmountSum = BigDecimal.ZERO;
            BigDecimal allowanceAmountSum = BigDecimal.ZERO;
            BigDecimal welfareAmountSum = BigDecimal.ZERO;
            BigDecimal bonusAmountSum = BigDecimal.ZERO;
            List<Long> employeeIds = new ArrayList<>();
            // 获取工资，津贴，福利，奖金合计
            for (SalaryPayDTO salaryPayDTO : salaryPayDTOList) {
                salaryAmountSum = salaryAmountSum.add(salaryPayDTO.getSalaryAmount());
                allowanceAmountSum = allowanceAmountSum.add(salaryPayDTO.getAllowanceAmount());
                welfareAmountSum = welfareAmountSum.add(salaryPayDTO.getWelfareAmount());
                bonusAmountSum = bonusAmountSum.add(salaryPayDTO.getBonusAmount());
                Long employeeId = salaryPayDTO.getEmployeeId();
                employeeIds.add(employeeId);
            }
            salaryStructure.setSalaryAmountSum(salaryAmountSum);
            salaryStructure.setAllowanceAmountSum(allowanceAmountSum);
            salaryStructure.setWelfareAmountSum(welfareAmountSum);
            salaryStructure.setBonusAmountSum(bonusAmountSum);
            salaryStructure.setSalaryPayDTOList(salaryPayDTOList);
            // 为员工赋值
            R<List<EmployeeDTO>> employeeDTOR = employeeService.selectByEmployeeIds(employeeIds, SecurityConstants.INNER);
            List<EmployeeDTO> employeeDTOS = employeeDTOR.getData();
            if (employeeDTOR.getCode() != 200) {
                throw new ServiceException(employeeDTOR.getMsg());
            }
            if (StringUtils.isEmpty(employeeDTOS)) {
                throw new ServiceException("当前人员信息已不存在，请检查员工配置");
            }
            for (SalaryPayDTO salaryPayDTO : salaryPayDTOList) {
                for (EmployeeDTO employeeDTO : employeeDTOS) {
                    if (salaryPayDTO.getEmployeeId().equals(employeeDTO.getEmployeeId())) {
                        salaryPayDTO.setPostRankName(employeeDTO.getEmployeeRankName());
                        salaryPayDTO.setDepartmentName(employeeDTO.getEmployeeDepartmentName());
                        salaryPayDTO.setDepartmentId(employeeDTO.getEmployeeDepartmentId());
                        break;
                    }
                }
            }
            Map<String, List<SalaryPayDTO>> map = new HashMap<>();
            Map<String, List<SalaryPayDTO>> map2 = salaryPayDTOList.stream().collect(Collectors.groupingBy(SalaryPayDTO::getDepartmentName));

            Map<String, Map<String, List<SalaryPayDTO>>> salaryPayMap = salaryPayDTOList.stream()
                    .collect(Collectors.groupingBy(SalaryPayDTO::getDepartmentName, Collectors.groupingBy(SalaryPayDTO::getPostRankName)));
            for (String s : salaryPayMap.keySet()) {
                Map<String, List<SalaryPayDTO>> stringListMap = salaryPayMap.get(s);
                for (String s1 : stringListMap.keySet()) {
                    List<SalaryPayDTO> salaryPayDTOList1 = stringListMap.get(s1);
                    List<SalaryPayDTO> salaryPayDTOS = new ArrayList<>();
                    for (SalaryPayDTO salaryPayDTO : salaryPayDTOList1) {

                    }
                }
            }

            return null;
        }
        EmployeeDTO employeeDTO = new EmployeeDTO();
        employeeDTO.setEmployeePostId(salaryStructureDTO.getPostId());
        R<List<EmployeeDTO>> listR = employeeService.selectRemoteList(employeeDTO, SecurityConstants.INNER);
        return null;
    }

}

