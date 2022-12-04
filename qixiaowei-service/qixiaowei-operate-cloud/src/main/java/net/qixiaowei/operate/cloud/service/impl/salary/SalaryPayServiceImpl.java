package net.qixiaowei.operate.cloud.service.impl.salary;

import cn.hutool.core.util.PageUtil;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.HttpStatus;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
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
import org.apache.commons.collections4.ListUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
        salaryPayDTO.setEmployeeDepartmentName(employeeDTO.getEmployeeDepartmentName());
        salaryPayDTO.setEmployeePostName(employeeDTO.getEmployeePostName());
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
                    salaryItemDTO.setSort(salaryPayDetailsDTO.getSort());
                    break;
                }
            }
        }
        salaryItemDTOS.sort(Comparator.comparingInt(SalaryItemDTO::getSort));
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
        String postName = salaryPayDTO.getEmployeePostName();
        String employeeName = salaryPayDTO.getEmployeeName();
        String departmentName = salaryPayDTO.getEmployeeDepartmentName();
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
        if (StringUtils.isNotEmpty(salaryPayDTOList)) {
            for (SalaryPayDTO payDTO : salaryPayDTOList) {
                for (EmployeeDTO dto : employeeDTOS) {
                    if (payDTO.getEmployeeId().equals(dto.getEmployeeId())) {
                        payDTO.setEmployeeName(dto.getEmployeeName());
                        payDTO.setEmployeeCode(dto.getEmployeeCode());
                        payDTO.setEmployeeDepartmentName(dto.getEmployeeDepartmentName());
                        payDTO.setEmployeePostName(dto.getEmployeePostName());
                        payDTO.setEmployeeRankName(dto.getEmployeeRankName());
                        payDTO.setEmployeeName(dto.getEmployeeName());
                        break;
                    }
                }
            }
        }
        return salaryPayDTOList;
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
     * @param salaryPayDTO dto
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
        if (StringUtils.isEmpty(list)) {
            throw new ServiceException("请使用系统的excel模板导入");
        }
        Map<Integer, String> head = list.get(0);
        list.remove(0);
        list.remove(1);
        if (StringUtils.isEmpty(list)) {
            throw new ServiceException("当前excel数据为空 请填充数据");
        }
        List<String> employeeCodes = new ArrayList<>();
        List<String> dateList = new ArrayList<>();
        for (Map<Integer, String> map : list) {
            if (StringUtils.isNull(map.get(0))) {
                continue;
            }
            employeeCodes.add(map.get(0));// 员工编码
            if (StringUtils.isNull(map.get(1))) {
                throw new ServiceException("请输入 " + map.get(0) + " 员工工号发薪年月");
            }
            dateList.add(map.get(1));// 发薪年月
            map.remove(0);
            map.remove(1);
        }
        if (StringUtils.isEmpty(employeeCodes)) {
            throw new ServiceException("当前员工编码未存在 请检查员工配置");
        }
        R<List<EmployeeDTO>> employeeR = employeeService.selectByCodes(employeeCodes, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = employeeR.getData();
        if (employeeR.getCode() != 200) {
            throw new ServiceException("远程调用失败 请联系管理员");
        }
        if (employeeDTOS.size() == employeeCodes.size()) {
            throw new ServiceException("员工编码有误 请核对");
        }
        List<SalaryItemDTO> salaryItemDTOS = salaryItemService.selectSalaryItemList(new SalaryItemDTO());
        if (StringUtils.isEmpty(salaryItemDTOS)) {
            throw new ServiceException("当前工资项未进行任何配置，请联系管理员");
        }
        for (int j = 0; j < list.size(); j++) {
            Map<Integer, String> map = list.get(j);
            SalaryPay salaryPay = new SalaryPay();
            BigDecimal salaryAmount = BigDecimal.ZERO;//工资金额
            BigDecimal allowanceAmount = BigDecimal.ZERO;//津贴金额
            BigDecimal welfareAmount = BigDecimal.ZERO;//福利金额
            BigDecimal bonusAmount = BigDecimal.ZERO;//奖金金额
            BigDecimal withholdRemitTax = BigDecimal.ZERO;//代扣代缴金额
            BigDecimal otherDeductions = BigDecimal.ZERO;//其他扣款金额
            BigDecimal payAmount;//发薪金额
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOAfter = new ArrayList<>();
            for (int i = 2; i < map.size(); i++) {
                SalaryPayDetailsDTO salaryPayDetailsDTO = new SalaryPayDetailsDTO();
                for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                    if (head.get(i).equals(salaryItemDTO.getThirdLevelItem())) {
                        BigDecimal amount;
                        if (StringUtils.isNull(map.get(i))) {
                            amount = BigDecimal.ZERO;
                        } else {
                            amount = new BigDecimal(map.get(i));
                        }
                        salaryPayDetailsDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
                        salaryPayDetailsDTO.setAmount(amount);
                        salaryPayDetailsDTO.setSort(i - 2);
                        switch (salaryItemDTO.getSecondLevelItem()) {
                            case 1:
                        }
                        switch (salaryItemDTO.getSecondLevelItem()) {
                            case 1:
                                salaryAmount.add(amount);
                                break;
                            case 2:
                                allowanceAmount.add(amount);
                                break;
                            case 3:
                                welfareAmount.add(amount);
                                break;
                            case 4:
                                bonusAmount.add(amount);
                                break;
                            case 5:
                                withholdRemitTax.add(amount);
                                break;
                            case 6:
                                otherDeductions.add(amount);
                                break;
                        }
                    }
                    salaryPayDetailsDTOAfter.add(salaryPayDetailsDTO);
                    break;
                }
            }
            int year = DateUtils.getYear(dateList.get(j));
            int month = DateUtils.getMonth(dateList.get(j));
            String employeeCode = employeeCodes.get(j);
            Long employeeId = null;
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (employeeCode.equals(employeeDTO.getEmployeeCode())) {
                    employeeId = employeeDTO.getEmployeeId();
                    break;
                }
            }
            payAmount = salaryAmount.add(allowanceAmount).add(welfareAmount).add(bonusAmount).subtract(withholdRemitTax).subtract(otherDeductions);
            salaryPay.setSalaryAmount(salaryAmount);
            salaryPay.setAllowanceAmount(allowanceAmount);
            salaryPay.setWelfareAmount(welfareAmount);
            salaryPay.setBonusAmount(bonusAmount);
            salaryPay.setWithholdRemitTax(withholdRemitTax);
            salaryPay.setOtherDeductions(otherDeductions);
            salaryPay.setPayAmount(payAmount);
            salaryPay.setUpdateBy(SecurityUtils.getUserId());
            salaryPay.setUpdateTime(DateUtils.getNowDate());
            SalaryPayDTO salaryPayDTOByYearAndMonth = salaryPayMapper.selectSalaryPayByYearAndMonth(employeeId, year, month);
            if (StringUtils.isNotNull(salaryPayDTOByYearAndMonth)) {
                salaryPay.setSalaryPayId(salaryPayDTOByYearAndMonth.getSalaryPayId());
//                salaryPayMapper.updateSalaryPay(salaryPay);
            } else {
                salaryPay.setPayYear(year);
                salaryPay.setPayMonth(month);
                salaryPay.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                salaryPay.setCreateBy(SecurityUtils.getUserId());
                salaryPay.setCreateTime(DateUtils.getNowDate());
                salaryPay.setEmployeeId(employeeId);
//                salaryPayMapper.insertSalaryPay(salaryPay);
            }
            for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOAfter) {
                salaryPayDetailsDTO.setSalaryPayId(salaryPay.getSalaryPayId());
            }
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOBefore = salaryPayDetailsService.selectSalaryPayDetailsBySalaryPayId(salaryPay.getSalaryPayId());
            // 交集
            List<SalaryPayDetailsDTO> updateSalaryPayDetail =
                    salaryPayDetailsDTOAfter.stream().filter(salaryPayDetailsDTO ->
                            salaryPayDetailsDTOBefore.stream().map(SalaryPayDetailsDTO::getSalaryItemId)
                                    .collect(Collectors.toList()).contains(salaryPayDetailsDTO.getSalaryItemId())
                    ).collect(Collectors.toList());
            // 差集 Before中After的补集
            List<SalaryPayDetailsDTO> delSalaryPayDetail =
                    salaryPayDetailsDTOBefore.stream().filter(salaryPayDetailsDTO ->
                            !salaryPayDetailsDTOAfter.stream().map(SalaryPayDetailsDTO::getSalaryItemId)
                                    .collect(Collectors.toList()).contains(salaryPayDetailsDTO.getSalaryItemId())
                    ).collect(Collectors.toList());
            // 差集 After中Before的补集
            List<SalaryPayDetailsDTO> addSalaryPayDetail =
                    salaryPayDetailsDTOAfter.stream().filter(salaryPayDetailsDTO ->
                            !salaryPayDetailsDTOBefore.stream().map(SalaryPayDetailsDTO::getSalaryItemId)
                                    .collect(Collectors.toList()).contains(salaryPayDetailsDTO.getSalaryItemId())
                    ).collect(Collectors.toList());
            try {
                if (StringUtils.isNotEmpty(delSalaryPayDetail)) {
//                    salaryPayDetailsService.deleteSalaryPayDetailsBySalaryPayDetailsIds(delSalaryPayDetail);
                }
                if (StringUtils.isNotEmpty(addSalaryPayDetail)) {
//                    salaryPayDetailsService.insertSalaryPayDetailss(addSalaryPayDetail);
                }
                if (StringUtils.isNotEmpty(updateSalaryPayDetail)) {
//                    salaryPayDetailsService.updateSalaryPayDetailss(updateSalaryPayDetail);
                }
            } catch (ServiceException e) {
                throw new ServiceException("更新数据失败");
            }

        }
    }


    /**
     * 导出Excel
     *
     * @param salaryPayDTO 薪酬架构报表
     * @return List
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
     * 薪酬架构报表
     *
     * @param salaryStructureDTO 薪酬架构报表
     * @return SalaryStructureDTO
     */
    @Override
    public SalaryStructureDTO selectSalaryPayStructure(SalaryStructureDTO salaryStructureDTO) {
        List<SalaryPayDTO> salaryPayDTOList = getSalaryPayList(salaryStructureDTO);
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            return new SalaryStructureDTO();
        }
        String departmentName = salaryStructureDTO.getDepartmentName();
        R<List<EmployeeDTO>> employeeDTOR;
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            return new SalaryStructureDTO();
        }
        employeeDTOR = getEmployee(salaryPayDTOList, departmentName);
        List<EmployeeDTO> employeeDTOS = getEmployeeDTOList(employeeDTOR);
        if (StringUtils.isEmpty(employeeDTOS)) {
            return new SalaryStructureDTO();
        }
        setSalaryPayValue(salaryPayDTOList, employeeDTOS);
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            return new SalaryStructureDTO();
        }
        return getSalaryStructureDTO(salaryPayDTOList);
    }

    /**
     * 查询薪酬架构报表列表
     * todo 1.有查询条件的情况查询2.分页
     * todo 新增一个接口
     *
     * @param salaryStructureDTO 薪酬架构DTO
     * @return SalaryStructureDTO
     */
    @Override
    public TableDataInfo selectSalaryPayStructureList(SalaryStructureDTO salaryStructureDTO) {
        Integer pageNum = salaryStructureDTO.getPageNum();
        Integer pageSize = salaryStructureDTO.getPageSize();
        List<SalaryPayDTO> salaryPayDTOList = getSalaryPayList(salaryStructureDTO);
        String departmentName = salaryStructureDTO.getDepartmentName();
        R<List<EmployeeDTO>> employeeDTOR;
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            return tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        employeeDTOR = getEmployee(salaryPayDTOList, departmentName);
        List<EmployeeDTO> employeeDTOS = getEmployeeDTOList(employeeDTOR);
        if (StringUtils.isEmpty(employeeDTOS)) {
            return tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        setSalaryPayValue(salaryPayDTOList, employeeDTOS);
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            return tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        List<SalaryPayDTO> salaryPayDTOS = calculateAmount(salaryPayDTOList);
        List<SalaryPayDTO> partition;
        if (pageNum > PageUtil.totalPage(salaryPayDTOS.size(), pageSize)) {
            partition = new ArrayList<>();
        } else {
            partition = ListUtils.partition(salaryPayDTOS, pageSize).get(pageNum - 1);
        }
        return tableDataInfo(HttpStatus.SUCCESS, partition, salaryPayDTOS.size());
    }

    @Override
    public List<SalaryPayDTO> selectSalaryPayBySalaryPayIds(List<Long> salaryPayIds) {

        return null;
    }

    /**
     * 给发薪表赋值
     *
     * @param salaryPayDTOList 发薪DTOList
     * @param employeeDTOS     人员List
     */
    private static void setSalaryPayValue(List<SalaryPayDTO> salaryPayDTOList, List<EmployeeDTO> employeeDTOS) {
        for (int i = employeeDTOS.size() - 1; i >= 0; i--) {
            if (StringUtils.isNull(employeeDTOS.get(i).getPostRankName())) {
                employeeDTOS.remove(i);
            }
        }
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOList) {
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (salaryPayDTO.getEmployeeId().equals(employeeDTO.getEmployeeId())) {
                    salaryPayDTO.setPostRankName(employeeDTO.getPostRankName());
                    salaryPayDTO.setEmployeeDepartmentName(employeeDTO.getEmployeeDepartmentName());
                    salaryPayDTO.setEmployeeDepartmentId(employeeDTO.getEmployeeDepartmentId());
                    break;
                }
            }
        }
        for (int i = salaryPayDTOList.size() - 1; i >= 0; i--) {
            if (StringUtils.isNull(salaryPayDTOList.get(i).getPostRankName())) {
                salaryPayDTOList.remove(i);
            }
        }
    }

    /**
     * 计算总和
     *
     * @param salaryPayDTOList 月度表
     * @return
     */
    private static SalaryStructureDTO getSalaryStructureDTO(List<SalaryPayDTO> salaryPayDTOList) {
        SalaryStructureDTO salaryStructure = new SalaryStructureDTO();
        BigDecimal salaryAmountSum = BigDecimal.ZERO;
        BigDecimal allowanceAmountSum = BigDecimal.ZERO;
        BigDecimal welfareAmountSum = BigDecimal.ZERO;
        BigDecimal bonusAmountSum = BigDecimal.ZERO;
        // 获取工资，津贴，福利，奖金合计
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOList) {
            salaryAmountSum = salaryAmountSum.add(salaryPayDTO.getSalaryAmount());
            allowanceAmountSum = allowanceAmountSum.add(salaryPayDTO.getAllowanceAmount());
            welfareAmountSum = welfareAmountSum.add(salaryPayDTO.getWelfareAmount());
            bonusAmountSum = bonusAmountSum.add(salaryPayDTO.getBonusAmount());
        }
        salaryStructure.setSalaryAmountSum(salaryAmountSum);
        salaryStructure.setAllowanceAmountSum(allowanceAmountSum);
        salaryStructure.setWelfareAmountSum(welfareAmountSum);
        salaryStructure.setBonusAmountSum(bonusAmountSum);
        return salaryStructure;
    }

    /**
     * 获取人员信息
     *
     * @param salaryPayDTOList 工资发薪列表
     * @param departmentName   部门名称
     * @return R
     */
    private R<List<EmployeeDTO>> getEmployee(List<SalaryPayDTO> salaryPayDTOList, String departmentName) {
        R<List<EmployeeDTO>> employeeDTOR;
        if (StringUtils.isNotNull(departmentName)) {
            EmployeeDTO employeeDTO = new EmployeeDTO();
            employeeDTO.setEmployeeDepartmentName(departmentName);
            employeeDTOR = employeeService.selectRemoteList(employeeDTO, SecurityConstants.INNER);
        } else {
            List<Long> employeeIds = new ArrayList<>();
            for (SalaryPayDTO salaryPayDTO : salaryPayDTOList) {
                employeeIds.add(salaryPayDTO.getEmployeeId());
            }
            employeeDTOR = employeeService.selectByEmployeeIds(employeeIds, SecurityConstants.INNER);
        }
        return employeeDTOR;
    }

    /**
     * 返回分页
     *
     * @param code 状态码
     * @param rows 数据
     * @param size 大小
     * @return TableDataInfo
     */
    private static TableDataInfo tableDataInfo(int code, List<SalaryPayDTO> rows, int size) {
        TableDataInfo rspData = new TableDataInfo();
        rspData.setCode(code);
        rspData.setRows(rows);
        rspData.setMsg("查询成功");
        rspData.setTotal(size);
        return rspData;
    }

    /**
     * 获取月度工资列表
     *
     * @param salaryStructureDTO 薪酬架构表
     * @return List
     */
    private List<SalaryPayDTO> getSalaryPayList(SalaryStructureDTO salaryStructureDTO) {
        List<SalaryPayDTO> salaryPayDTOList;
        Date timeStart = salaryStructureDTO.getTimeStart();
        Date timeEnd = salaryStructureDTO.getTimeEnd();
        if (StringUtils.isNull(timeStart) || StringUtils.isNull(timeEnd)) {
            int month = DateUtils.getMonth();
            int year = DateUtils.getYear();
            salaryPayDTOList = salaryPayMapper.selectSalaryPayBySomeMonth(year, 0, month);
        } else {
            int startYear = DateUtils.getYear(timeStart);
            int endYear = DateUtils.getYear(timeEnd);
            int startMonth = DateUtils.getMonth(timeStart);
            int endMonth = DateUtils.getMonth(timeEnd);
            if (endYear - startYear == 0) {// 当前年份
                salaryPayDTOList = salaryPayMapper.selectSalaryPayBySomeMonth(endYear, startMonth, endMonth);
            } else if (endYear - startYear == 1) {
                salaryPayDTOList = salaryPayMapper.selectSalaryPayBySomeMonth(startYear, startMonth, 12);
                salaryPayDTOList.addAll(salaryPayMapper.selectSalaryPayBySomeMonth(endYear, 1, endMonth));
            } else if (endYear - startYear > 1) {
                salaryPayDTOList = salaryPayMapper.selectSalaryPayBySomeMonth(startYear, startMonth, 12);
                salaryPayDTOList.addAll(salaryPayMapper.selectSalaryPayBySomeYear(startYear + 1, endYear - 1));
                salaryPayDTOList.addAll(salaryPayMapper.selectSalaryPayBySomeMonth(endYear, 1, endMonth));
            } else {
                throw new ServiceException("结束年份不可以小于开始年份");
            }
        }
        return salaryPayDTOList;
    }

    /**
     * 获取人员信息
     *
     * @param employeeDTOR
     * @return
     */
    private static List<EmployeeDTO> getEmployeeDTOList(R<List<EmployeeDTO>> employeeDTOR) {
        List<EmployeeDTO> employeeDTOS = employeeDTOR.getData();
        if (employeeDTOR.getCode() != 200) {
            throw new ServiceException(employeeDTOR.getMsg());
        }
        return employeeDTOS;
    }

    /**
     * 薪酬架构报表金额计算
     *
     * @param salaryPayDTOList 子表DTO
     */
    private static List<SalaryPayDTO> calculateAmount(List<SalaryPayDTO> salaryPayDTOList) {
        Map<String, Map<String, List<SalaryPayDTO>>> salaryPayMap = salaryPayDTOList.stream()
                .collect(Collectors.groupingBy(SalaryPayDTO::getEmployeeDepartmentName, Collectors.groupingBy(SalaryPayDTO::getPostRankName)));
        List<SalaryPayDTO> salaryPayDTOS = new ArrayList<>(10);
        for (String departmentName : salaryPayMap.keySet()) {
            Map<String, List<SalaryPayDTO>> stringListMap = salaryPayMap.get(departmentName);
            for (String rankName : stringListMap.keySet()) {
                List<SalaryPayDTO> salaryPayDTOList1 = stringListMap.get(rankName);
                BigDecimal salaryAmountValue = BigDecimal.ZERO;// 工资金额
                BigDecimal allowanceAmountValue = BigDecimal.ZERO;// 津贴金额
                BigDecimal welfareAmountValue = BigDecimal.ZERO;// 福利金额
                BigDecimal bonusAmountValue = BigDecimal.ZERO;// 奖金金额
                SalaryPayDTO salaryPay;
                if (StringUtils.isEmpty(salaryPayDTOList1)) {
                    salaryPay = new SalaryPayDTO();
                } else {
                    salaryPay = salaryPayDTOList1.get(0);
                }
                for (SalaryPayDTO salaryPayDTO : salaryPayDTOList1) {
                    salaryAmountValue = salaryAmountValue.add(salaryPayDTO.getSalaryAmount());
                    allowanceAmountValue = allowanceAmountValue.add(salaryPayDTO.getAllowanceAmount());
                    welfareAmountValue = welfareAmountValue.add(salaryPayDTO.getWelfareAmount());
                    bonusAmountValue = bonusAmountValue.add(salaryPayDTO.getBonusAmount());
                }
                //固定值
                BigDecimal fixedValue = salaryAmountValue.add(allowanceAmountValue).add(welfareAmountValue);
                //总计
                BigDecimal paymentBonus = fixedValue.add(bonusAmountValue);
                //固定占比（%）
                BigDecimal fixedProportion;
                //浮动占比（%）
                BigDecimal floatProportion;
                if (paymentBonus.compareTo(BigDecimal.ZERO) != 0) {
                    fixedProportion = fixedValue.divide(paymentBonus, 2, RoundingMode.HALF_UP);
                    floatProportion = bonusAmountValue.divide(paymentBonus, 2, RoundingMode.HALF_UP);
                } else {
                    fixedProportion = BigDecimal.ZERO;
                    floatProportion = BigDecimal.ZERO;
                }
                SalaryPayDTO salaryPayDTO = new SalaryPayDTO();
                salaryPayDTO.setPostRankName(rankName);
                salaryPayDTO.setEmployeeDepartmentName(departmentName);
                salaryPayDTO.setSalaryAmount(salaryAmountValue);
                salaryPayDTO.setAllowanceAmount(allowanceAmountValue);
                salaryPayDTO.setWelfareAmount(welfareAmountValue);
                salaryPayDTO.setBonusAmount(bonusAmountValue);
                salaryPayDTO.setFixedProportion(fixedProportion);
                salaryPayDTO.setFloatProportion(floatProportion);
                salaryPayDTO.setPaymentBonus(paymentBonus);// 总计
                salaryPayDTOS.add(salaryPayDTO);
            }
        }
        return salaryPayDTOS;
    }

}

