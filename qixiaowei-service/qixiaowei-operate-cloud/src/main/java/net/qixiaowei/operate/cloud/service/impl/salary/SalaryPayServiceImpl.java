package net.qixiaowei.operate.cloud.service.impl.salary;

import cn.hutool.core.util.PageUtil;
import cn.hutool.core.util.StrUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.constant.CacheConstants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.HttpStatus;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.PageUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.common.utils.excel.ExcelUtils;
import net.qixiaowei.integration.common.utils.uuid.IdUtils;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.redis.service.RedisService;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPay;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPayDetails;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryStructureDTO;
import net.qixiaowei.operate.cloud.api.vo.salary.SalaryPayImportOfEmpDataVO;
import net.qixiaowei.operate.cloud.api.vo.salary.SalaryPayImportTempDataVO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayExcel;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayDetailsMapper;
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
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;


/**
 * SalaryPayService业务层处理
 *
 * @author Graves
 * @since 2022-11-17
 */
@Service
@Slf4j
public class SalaryPayServiceImpl implements ISalaryPayService {
    @Autowired
    private SalaryPayMapper salaryPayMapper;

    @Autowired
    private SalaryPayDetailsMapper salaryPayDetailsMapper;

    @Autowired
    private ISalaryPayDetailsService salaryPayDetailsService;

    @Autowired
    private ISalaryItemService salaryItemService;

    @Autowired
    RemoteEmployeeService employeeService;

    @Autowired
    private RedisService redisService;

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
    @DataScope(businessAlias = "sp")
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
        SalaryPay salaryPay = new SalaryPay();
        BeanUtils.copyProperties(salaryPayDTO, salaryPay);
        R<List<EmployeeDTO>> employeeDTOR = employeeService.selectRemoteList(employeeDTO, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = employeeDTOR.getData();
        if (employeeDTOR.getCode() != 200) {
            throw new ServiceException("远程获取人员信息失败");
        }
        List<Long> employeeIds;
        if (StringUtils.isNotEmpty(employeeDTOS)) {
            employeeIds = employeeDTOS.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
        } else {
            return salaryPayDTOList;
        }
        Map<String, Object> params = salaryPayDTO.getParams();
        if (StringUtils.isNotEmpty(params)) {
            int remoteIds = getRemoteIds(params, employeeIds);
            if (remoteIds == 0) {
                return new ArrayList<>();
            }
        } else {
            params = new HashMap<>();
            params.put("employeeIds", employeeIds);
        }
        String payYearMonth = salaryPayDTO.getPayYearMonth();
        if (StringUtils.isNotNull(payYearMonth)) {
            params.put("payYearMonth", payYearMonth);
        }
        salaryPay.setParams(params);
        salaryPayDTOList = salaryPayMapper.selectSalaryPayList(salaryPay);
        if (StringUtils.isNotEmpty(salaryPayDTOList)) {
            for (SalaryPayDTO payDTO : salaryPayDTOList) {
                payDTO.setTotalWages(payDTO.getSalaryAmount().add(payDTO.getAllowanceAmount()).add(payDTO.getWelfareAmount()));
                payDTO.setTotalAmount(payDTO.getTotalWages().add(payDTO.getBonusAmount()));
                payDTO.setTotalDeductions(payDTO.getOtherDeductions().add(payDTO.getWithholdRemitTax()));
                for (EmployeeDTO dto : employeeDTOS) {
                    if (payDTO.getEmployeeId().equals(dto.getEmployeeId())) {
                        payDTO.setEmployeeName(dto.getEmployeeName());
                        payDTO.setEmployeeCode(dto.getEmployeeCode());
                        payDTO.setEmployeeDepartmentName(dto.getEmployeeDepartmentName());
                        payDTO.setTopLevelDepartmentName(dto.getTopLevelDepartmentName());
                        payDTO.setEmployeePostName(dto.getEmployeePostName());
                        payDTO.setEmployeeRankName(dto.getEmployeeRankName());
                        payDTO.setEmployeeName(dto.getEmployeeName());
                        break;
                    }
                }
            }
        }
        this.handleResult(salaryPayDTOList);
        return salaryPayDTOList;
    }

    /**
     * 获取高级搜索后的ID传入params
     *
     * @param params      请求参数
     * @param employeeIds 员工ID集合
     * @return 结果
     */
    private int getRemoteIds(Map<String, Object> params, List<Long> employeeIds) {
        Map<String, Object> params2 = new HashMap<>();
        for (String key : params.keySet()) {
            switch (key) {
                case "employeeNameEqual":
                    params2.put("employeeNameEqual", params.get("employeeNameEqual"));
                    break;
                case "employeeNameNotEqual":
                    params2.put("employeeNameNotEqual", params.get("employeeNameNotEqual"));
                    break;
                case "employeeNameLike":
                    params2.put("employeeNameLike", params.get("employeeNameLike"));
                    break;
                case "employeeNameNotLike":
                    params2.put("employeeNameNotLike", params.get("employeeNameNotLike"));
                    break;
                case "employeeCodeEqual":
                    params2.put("employeeCodeEqual", params.get("employeeCodeEqual"));
                    break;
                case "employeeCodeNotEqual":
                    params2.put("employeeCodeNotEqual", params.get("employeeCodeNotEqual"));
                    break;
                case "employeeCodeLike":
                    params2.put("employeeCodeLike", params.get("employeeCodeLike"));
                    break;
                case "employeeCodeNotLike":
                    params2.put("employeeCodeNotLike", params.get("employeeCodeNotLike"));
                    break;
                case "employeeDepartmentIdEqual":
                    params2.put("employeeDepartmentIdEqual", params.get("employeeDepartmentIdEqual"));
                    break;
                case "employeeDepartmentIdNotEqual":
                    params2.put("employeeDepartmentIdNotEqual", params.get("employeeDepartmentIdNotEqual"));
                    break;
                case "employeePostIdEqual":
                    params2.put("employeePostIdEqual", params.get("employeePostIdEqual"));
                    break;
                case "employeePostIdNotEqual":
                    params2.put("employeePostIdNotEqual", params.get("employeePostIdNotEqual"));
                    break;
                case "employeePostIdNull":
                    params2.put("employeePostIdNull", params.get("employeePostIdNull"));
                    break;
                case "employeePostIdNotNull":
                    params2.put("employeePostIdNotNull", params.get("employeePostIdNotNull"));
                    break;
            }
        }
        // 人员
        if (StringUtils.isNotEmpty(params2)) {
            List<EmployeeDTO> employeeDTOS = empAdvancedSearch(params2);
            if (StringUtils.isNotEmpty(employeeDTOS)) {
                employeeIds = employeeDTOS.stream().map(EmployeeDTO::getEmployeeId).collect(Collectors.toList());
                params.put("employeeIds", employeeIds);
            } else {
                return 0;
            }
        }
        return 1;
    }

    /**
     * 人员远程高级搜索
     *
     * @param params 请求参数
     * @return List
     */
    private List<EmployeeDTO> empAdvancedSearch(Map<String, Object> params) {
        R<List<EmployeeDTO>> listR = employeeService.empAdvancedSearch(params, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("人员远程高级搜索失败 请联系管理员");
        }
        return employeeDTOS;
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
            throw new ServiceException("该员工" + salaryPayDTO.getPayYear() + "年" + salaryPayDTO.getPayMonth() + "月工资数据已存在");
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

    @Override
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

    @Override
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
     * @param list      excel内容
     * @param sheetName sheet名称
     */
    @Override
    @Transactional
    public Map<Object, Object> importSalaryPay(SalaryPayImportTempDataVO salaryPayImportTempDataVO, List<Map<Integer, String>> list, String sheetName) {
        if (StringUtils.isEmpty(list)) {
            throw new ServiceException("模板数据不能为空，至少有1条数据!");
        }
        List<Map<Integer, String>> listMap = this.getMaps(sheetName, list);
        //以下三个值取初始化值即可
        Map<Integer, String> headMap = salaryPayImportTempDataVO.getHeadMap();
        Map<String, SalaryItemDTO> salaryItemOfThirdLevelItemMap = salaryPayImportTempDataVO.getSalaryItemOfThirdLevelItemMap();
        Map<Long, SalaryItemDTO> salaryItemMap = salaryPayImportTempDataVO.getSalaryItemMap();
        //以下值需要更新
        Map<String, SalaryPayImportOfEmpDataVO> employeeTempDataOfCode = salaryPayImportTempDataVO.getEmployeeTempDataOfCode();
        Set<String> employeeCodes = salaryPayImportTempDataVO.getEmployeeCodes();
        Set<String> salarySet = salaryPayImportTempDataVO.getSalarySet();
        Set<String> employeeCodeAndPayYearSet = salaryPayImportTempDataVO.getEmployeeCodeAndPayYearSet();
        Map<String, SalaryPayDTO> employeeIdAndYearAndMonthOfSalaryPayMap = salaryPayImportTempDataVO.getEmployeeIdAndYearAndMonthOfSalaryPayMap();
        //本次新增导入的员工code
        List<String> employeeCodeList = new ArrayList<>();
        Set<Integer> salaryYearsOfThisCycle = new HashSet<>();
        Set<Long> employeeIdsOfThisCycle = new HashSet<>();
        List<Object> errorExcelList = new ArrayList<>();
        List<Map<Integer, String>> successExcels = new ArrayList<>();
        checkSalaryPayList(listMap, employeeTempDataOfCode, employeeCodes, salarySet, employeeCodeAndPayYearSet, employeeCodeList, salaryYearsOfThisCycle, employeeIdsOfThisCycle, errorExcelList, successExcels, headMap);
        //添加本次循环的值
        salaryPayImportTempDataVO.setSalarySet(salarySet);
        salaryPayImportTempDataVO.setEmployeeCodes(employeeCodes);
        //对本次新导入员工处理
        this.handleEmployeeData(employeeTempDataOfCode, employeeCodeList, employeeIdsOfThisCycle);
        //处理发薪历史数据
        this.handleSalaryPayOfEmployeeIdsAndPayYears(salaryPayImportTempDataVO, employeeIdAndYearAndMonthOfSalaryPayMap, salaryYearsOfThisCycle, employeeIdsOfThisCycle);
        List<SalaryPay> insertSalaryPays = new ArrayList<>();
        List<SalaryPay> updateSalaryPays = new ArrayList<>();
        List<SalaryPayDetails> insertSalaryPayDetails = new ArrayList<>();
        List<SalaryPayDetails> updateSalaryPayDetails = new ArrayList<>();
        Long userId = SecurityUtils.getUserId();
        Date nowDate = DateUtils.getNowDate();
        Map<String, List<SalaryPayDetails>> yearAndMonthOfSalaryPayDetailsMap = new HashMap<>();
        List<Object> successExcelList = new ArrayList<>();
        operateSalaryList(salaryPayImportTempDataVO, headMap, salaryItemOfThirdLevelItemMap, salaryItemMap, employeeTempDataOfCode, employeeIdAndYearAndMonthOfSalaryPayMap, successExcels, insertSalaryPays, updateSalaryPays, insertSalaryPayDetails, updateSalaryPayDetails, userId, nowDate, yearAndMonthOfSalaryPayDetailsMap, successExcelList);
        String uuId;
        String simpleUUID = IdUtils.simpleUUID();
        if (StringUtils.isNotEmpty(errorExcelList)) {
            uuId = CacheConstants.ERROR_EXCEL_KEY + simpleUUID;
            redisService.setCacheObject(uuId, errorExcelList, CacheConstants.ERROR_EXCEL_LOCK_TIME, TimeUnit.HOURS);
        }
        return ExcelUtils.parseExcelResult(successExcelList, errorExcelList, false, simpleUUID);
    }

    /**
     * 更新工资表
     *
     * @param successExcelList excel列表
     */
    private void operateSalaryList(SalaryPayImportTempDataVO salaryPayImportTempDataVO, Map<Integer, String> headMap, Map<String, SalaryItemDTO> salaryItemOfThirdLevelItemMap,
                                   Map<Long, SalaryItemDTO> salaryItemMap, Map<String, SalaryPayImportOfEmpDataVO> employeeTempDataOfCode,
                                   Map<String, SalaryPayDTO> employeeIdAndYearAndMonthOfSalaryPayMap, List<Map<Integer, String>> successExcels, List<SalaryPay> insertSalaryPays,
                                   List<SalaryPay> updateSalaryPays, List<SalaryPayDetails> insertSalaryPayDetails, List<SalaryPayDetails> updateSalaryPayDetails, Long userId,
                                   Date nowDate, Map<String, List<SalaryPayDetails>> yearAndMonthOfSalaryPayDetailsMap, List<Object> successExcelList) {
        if (StringUtils.isNotEmpty(successExcels)) {
            for (Map<Integer, String> map : successExcels) {
                String employeeCode = map.get(0);
                SalaryPayImportOfEmpDataVO salaryPayImportOfEmpDataVO = employeeTempDataOfCode.get(employeeCode);
                Long employeeId = salaryPayImportOfEmpDataVO.getEmployeeId();
                String salaryYearAndMonth = map.get(2);
                Date date = DateUtils.parseAnalysisExcelDate(salaryYearAndMonth);
                int year = Integer.parseInt(String.valueOf(DateUtils.getYear(date)));
                int month = Integer.parseInt(String.valueOf(DateUtils.getMonth(date)));
                BigDecimal salaryAmount = BigDecimal.ZERO;//工资金额
                BigDecimal allowanceAmount = BigDecimal.ZERO;//津贴金额
                BigDecimal welfareAmount = BigDecimal.ZERO;//福利金额
                BigDecimal bonusAmount = BigDecimal.ZERO;//奖金金额
                BigDecimal withholdRemitTax = BigDecimal.ZERO;//代扣代缴金额
                BigDecimal otherDeductions = BigDecimal.ZERO;//其他扣款金额
                List<SalaryPayDetails> salaryPayDetailsOfImport = new ArrayList<>();
                //处理发薪明细
                for (int i = 3; i < headMap.size(); i++) {
                    SalaryPayDetails salaryPayDetails = new SalaryPayDetails();
                    String thirdLevelItemOfImport = headMap.get(i);
                    if (salaryItemOfThirdLevelItemMap.containsKey(thirdLevelItemOfImport)) {
                        SalaryItemDTO salaryItemDTO = salaryItemOfThirdLevelItemMap.get(thirdLevelItemOfImport);
                        BigDecimal amount;
                        String amountOfImport = map.get(i);
                        if (StringUtils.isEmpty(amountOfImport)) {
                            amount = BigDecimal.ZERO;
                        } else {
                            amount = new BigDecimal(amountOfImport);
                        }
                        salaryPayDetails.setSalaryItemId(salaryItemDTO.getSalaryItemId());
                        salaryPayDetails.setAmount(amount);
                        salaryPayDetails.setSort(i - 2);
                        switch (salaryItemDTO.getSecondLevelItem()) {
                            case 1:
                                salaryAmount = salaryAmount.add(amount);
                                break;
                            case 2:
                                allowanceAmount = allowanceAmount.add(amount);
                                break;
                            case 3:
                                welfareAmount = welfareAmount.add(amount);
                                break;
                            case 4:
                                bonusAmount = bonusAmount.add(amount);
                                break;
                            case 5:
                                withholdRemitTax = withholdRemitTax.add(amount);
                                break;
                            case 6:
                                otherDeductions = otherDeductions.add(amount);
                                break;
                        }
                        salaryPayDetailsOfImport.add(salaryPayDetails);
                    }
                }
                String key = employeeId + StrUtil.COLON + year + StrUtil.COLON + month;
                SalaryPay salaryPay = new SalaryPay();
                //判断系统是否存在发薪记录
                salaryPay.setUpdateBy(userId);
                salaryPay.setUpdateTime(nowDate);
                //更新
                if (employeeIdAndYearAndMonthOfSalaryPayMap.containsKey(key)) {
                    SalaryPayDTO salaryPayDTO = employeeIdAndYearAndMonthOfSalaryPayMap.get(key);
                    Long salaryPayId = salaryPayDTO.getSalaryPayId();
                    List<SalaryPayDetailsDTO> salaryPayDetailsDTOList = salaryPayDTO.getSalaryPayDetailsDTOList();
                    Map<Long, SalaryPayDetailsDTO> salaryPayDetailsMap = new HashMap<>();
                    for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOList) {
                        salaryPayDetailsMap.put(salaryPayDetailsDTO.getSalaryItemId(), salaryPayDetailsDTO);
                    }
                    Set<Long> salaryPayDetailsSet = new HashSet<>();
                    handleSalaryPayDetails(insertSalaryPayDetails, updateSalaryPayDetails, userId, nowDate, salaryPayDetailsOfImport, salaryPayId, salaryPayDetailsMap, salaryPayDetailsSet);
                    for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOList) {
                        Long salaryItemId = salaryPayDetailsDTO.getSalaryItemId();
                        BigDecimal amount = Optional.ofNullable(salaryPayDetailsDTO.getAmount()).orElse(BigDecimal.ZERO);
                        //如果导入不包含的话，则把系统里面的重新一起计算一遍
                        if (!salaryPayDetailsSet.contains(salaryItemId)) {
                            SalaryItemDTO salaryItemDTO = salaryItemMap.get(salaryItemId);
                            switch (salaryItemDTO.getSecondLevelItem()) {
                                case 1:
                                    salaryAmount = salaryAmount.add(amount);
                                    break;
                                case 2:
                                    allowanceAmount = allowanceAmount.add(amount);
                                    break;
                                case 3:
                                    welfareAmount = welfareAmount.add(amount);
                                    break;
                                case 4:
                                    bonusAmount = bonusAmount.add(amount);
                                    break;
                                case 5:
                                    withholdRemitTax = withholdRemitTax.add(amount);
                                    break;
                                case 6:
                                    otherDeductions = otherDeductions.add(amount);
                                    break;
                            }
                        }
                    }
                    //获取工资发薪
                    this.buildSalaryPay
                            (salaryPay, employeeId, salaryAmount, allowanceAmount,
                                    welfareAmount, bonusAmount, withholdRemitTax, otherDeductions);
                    salaryPay.setSalaryPayId(salaryPayId);
                    updateSalaryPays.add(salaryPay);
                } else {//插入
                    //获取工资发薪
                    this.buildSalaryPay
                            (salaryPay, employeeId, salaryAmount, allowanceAmount,
                                    welfareAmount, bonusAmount, withholdRemitTax, otherDeductions);
                    salaryPay.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    salaryPay.setCreateBy(userId);
                    salaryPay.setCreateTime(nowDate);
                    insertSalaryPays.add(salaryPay);
                    //处理发薪明细
                    yearAndMonthOfSalaryPayDetailsMap.put(employeeId + StrUtil.COLON + year + StrUtil.COLON + month, salaryPayDetailsOfImport);
                }
                salaryPay.setPayYear(year);
                salaryPay.setPayMonth(month);
                successExcelList.add(map);
            }
            salaryPayImportTempDataVO.setEmployeeTempDataOfCode(employeeTempDataOfCode);
            //批量新增工资发薪
            this.insertSalaryPaysOfImport(insertSalaryPays, insertSalaryPayDetails, userId, nowDate, yearAndMonthOfSalaryPayDetailsMap);
            //批量修改工资发薪
            this.updateSalaryPaysOfImport(updateSalaryPays);
            //批量新增工资发薪明细
            this.insertSalaryPayDetailsOfImport(insertSalaryPayDetails);
            //批量修改工资发薪明细
            this.updateSalaryPayDetailsOfImport(updateSalaryPayDetails);
        }
    }

    /**
     * 检查
     *
     * @param listMap excel数据
     * @param headMap 表头
     */
    private static void checkSalaryPayList(List<Map<Integer, String>> listMap, Map<String, SalaryPayImportOfEmpDataVO> employeeTempDataOfCode, Set<String> employeeCodes, Set<String> salarySet,
                                           Set<String> employeeCodeAndPayYearSet, List<String> employeeCodeList, Set<Integer> salaryYearsOfThisCycle, Set<Long> employeeIdsOfThisCycle,
                                           List<Object> errorExcelList, List<Map<Integer, String>> successExcels, Map<Integer, String> headMap) {
        for (Map<Integer, String> map : listMap) {
            StringBuilder errorNote = new StringBuilder();
            String employeeCode = map.get(0);
            String employeeName = map.get(1);
            String salaryYearAndMonth = map.get(2);
            if (StringUtils.isEmpty(employeeCode)) {
                errorNote.append("员工编码为空；");
            }
            if (StringUtils.isEmpty(employeeName)) {
                errorNote.append("员工姓名为空；");
            }
            //发薪年月校验
            if (StringUtils.isEmpty(salaryYearAndMonth)) {
                errorNote.append("发薪年月未输入；");
            }
            int year = 0;
            try {
                Date date = DateUtils.parseAnalysisExcelDate(salaryYearAndMonth);
                if (StringUtils.isNull(date)) {
                    errorNote.append("发薪年月格式错误；");
                }
                year = Integer.parseInt(String.valueOf(DateUtils.getYear(date)));
                int month = Integer.parseInt(String.valueOf(DateUtils.getMonth(date)));
            } catch (Exception e) {
                log.error("导入员工薪酬报错，发薪年月格式错误:{}", e.getMessage());
                errorNote.append("发薪年月格式错误；");
            }
            String salary = employeeCode + StrUtil.COLON + salaryYearAndMonth;
            if (salarySet.contains(salary)) {
                errorNote.append("存在重复工资项；");
            }
            if (map.size() > 3) {
                for (int i = 3; i < map.keySet().size(); i++) {
                    String value = map.get(i);
                    if (StringUtils.isNotBlank(value) && ExcelUtils.isNotNumber(value)) {
                        errorNote.append(headMap.get(i)).append("格式错误；");
                    }
                }
            }
            String employeeCodeAndPayYear = employeeCode + StrUtil.COLON + year;
            salarySet.add(salary);
            // 员工编码
            handleEmployeeImportData(employeeTempDataOfCode, employeeCodes, employeeCodeAndPayYearSet, employeeCodeList, employeeIdsOfThisCycle, employeeCode, employeeName, employeeCodeAndPayYear, errorNote);
            if (!employeeCodeAndPayYearSet.contains(employeeCodeAndPayYear)) {
                employeeCodeAndPayYearSet.add(employeeCodeAndPayYear);
                salaryYearsOfThisCycle.add(year);
            }
            if (errorNote.length() != 0) {
                List<String> errorList = new ArrayList<>();
                errorList.add(errorNote.substring(0, errorNote.length() - 1));
                errorList.addAll(map.values());
                errorExcelList.add(errorList);
            } else {
                successExcels.add(map);
            }
        }
    }

    /**
     * 获取list
     *
     * @param sheetName sheet名称
     * @param listMap   列表内容
     * @return 结果
     */
    private List<Map<Integer, String>> getMaps(String sheetName, List<Map<Integer, String>> listMap) {
        if (StringUtils.equals(sheetName, "月度工资条导入错误报告")) {
            List<Map<Integer, String>> objects = new ArrayList<>();
            for (Map<Integer, String> map1 : listMap) {
                Map<Integer, String> map2 = new TreeMap<>();
                for (int i = 1; i < map1.size(); i++) {
                    map2.put(i - 1, map1.get(i));
                }
                objects.add(map2);
            }
            listMap = objects;
        } else if (!StringUtils.equals(sheetName, "月度工资条导入")) {
            throw new ServiceException("职级确定薪酬模板不正确 请检查");
        }
        return listMap;
    }

    /**
     * @description: 处理发薪明细插入和更新逻辑
     * @Author: hzk
     * @date: 2022/12/24 18:13
     * @param: [insertSalaryPayDetails, updateSalaryPayDetails, userId, nowDate, salaryPayDetailsOfImport, salaryPayId, salaryPayDetailsMap, salaryPayDetailsSet]
     * @return: void
     **/
    private static void handleSalaryPayDetails(List<SalaryPayDetails> insertSalaryPayDetails, List<SalaryPayDetails> updateSalaryPayDetails, Long userId, Date nowDate, List<SalaryPayDetails> salaryPayDetailsOfImport, Long salaryPayId, Map<Long, SalaryPayDetailsDTO> salaryPayDetailsMap, Set<Long> salaryPayDetailsSet) {
        for (SalaryPayDetails salaryPayDetails : salaryPayDetailsOfImport) {
            Long salaryItemId = salaryPayDetails.getSalaryItemId();
            salaryPayDetailsSet.add(salaryItemId);
            salaryPayDetails.setUpdateBy(userId);
            salaryPayDetails.setUpdateTime(nowDate);
            //与系统存储的作比较，如果存在则更新---注：如果系统的存在的工资项目，导入时不存在，跳过不处理，即无需找到导入中系统不存在的部分（产品意思）
            if (salaryPayDetailsMap.containsKey(salaryItemId)) {
                SalaryPayDetailsDTO salaryPayDetailsDTO = salaryPayDetailsMap.get(salaryItemId);
                salaryPayDetails.setSalaryPayDetailsId(salaryPayDetailsDTO.getSalaryPayDetailsId());
                updateSalaryPayDetails.add(salaryPayDetails);
            } else {//不存在则插入
                salaryPayDetails.setSalaryPayId(salaryPayId);
                salaryPayDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                salaryPayDetails.setCreateBy(userId);
                salaryPayDetails.setCreateTime(nowDate);
                insertSalaryPayDetails.add(salaryPayDetails);
            }
        }
    }

    /**
     * @description: 处理员工导入数据
     * @Author: hzk
     * @date: 2022/12/24 18:10
     * @param: [employeeTempDataOfCode, employeeCodes, employeeCodeAndPayYearSet, employeeCodeList, employeeIdsOfThisCycle, employeeCode, employeeName, employeeCodeAndPayYear]
     * @return: void
     **/
    private static void handleEmployeeImportData(Map<String, SalaryPayImportOfEmpDataVO> employeeTempDataOfCode, Set<String> employeeCodes, Set<String> employeeCodeAndPayYearSet,
                                                 List<String> employeeCodeList, Set<Long> employeeIdsOfThisCycle, String employeeCode, String employeeName, String employeeCodeAndPayYear, StringBuilder errorNote) {
        if (!employeeCodes.contains(employeeCode)) {
            //如果不存在则添加进员工code的set集合
            employeeCodes.add(employeeCode);
            employeeCodeList.add(employeeCode);
            SalaryPayImportOfEmpDataVO salaryPayImportOfEmpDataVO = new SalaryPayImportOfEmpDataVO();
            salaryPayImportOfEmpDataVO.setEmployeeName(employeeName);
            employeeTempDataOfCode.put(employeeCode, salaryPayImportOfEmpDataVO);
        } else {
            SalaryPayImportOfEmpDataVO salaryPayImportOfEmpDataVO = employeeTempDataOfCode.get(employeeCode);
            String employeeNameOfTemp = salaryPayImportOfEmpDataVO.getEmployeeName();
            Long employeeId = salaryPayImportOfEmpDataVO.getEmployeeId();
            if (!employeeName.equals(employeeNameOfTemp)) {
                errorNote.append("员工编码(").append(employeeCode).append(")存在姓名前[").append(employeeNameOfTemp).append("],").append("后[").append(employeeName).append("]不一致，请检查；");
            }
            if (!employeeCodeAndPayYearSet.contains(employeeCodeAndPayYear) && StringUtils.isNotNull(employeeId)) {
                employeeIdsOfThisCycle.add(employeeId);
            }
            salaryPayImportOfEmpDataVO.setEmployeeName(employeeName);
            employeeTempDataOfCode.put(employeeCode, salaryPayImportOfEmpDataVO);
        }
    }

    /**
     * @description: 处理发薪历史数据
     * @Author: hzk
     * @date: 2022/12/24 18:08
     * @param: [salaryPayImportTempDataVO, employeeIdAndYearAndMonthOfSalaryPayMap, salaryYearsOfThisCycle, employeeIdsOfThisCycle]
     * @return: void
     **/
    private void handleSalaryPayOfEmployeeIdsAndPayYears(SalaryPayImportTempDataVO salaryPayImportTempDataVO, Map<String, SalaryPayDTO> employeeIdAndYearAndMonthOfSalaryPayMap, Set<Integer> salaryYearsOfThisCycle, Set<Long> employeeIdsOfThisCycle) {
        if (StringUtils.isNotEmpty(employeeIdsOfThisCycle) && StringUtils.isNotEmpty(salaryYearsOfThisCycle)) {
            List<SalaryPayDTO> salaryPayOfPayYears = salaryPayMapper.selectSalaryPayOfEmployeeIdsAndPayYears(employeeIdsOfThisCycle, salaryYearsOfThisCycle);
            if (StringUtils.isNotEmpty(salaryPayOfPayYears)) {
                for (SalaryPayDTO salaryPayOfPayYear : salaryPayOfPayYears) {
                    Long employeeId = salaryPayOfPayYear.getEmployeeId();
                    Integer payYear = salaryPayOfPayYear.getPayYear();
                    Integer payMonth = salaryPayOfPayYear.getPayMonth();
                    String key = employeeId + StrUtil.COLON + payYear + StrUtil.COLON + payMonth;
                    employeeIdAndYearAndMonthOfSalaryPayMap.put(key, salaryPayOfPayYear);
                }
            }
        }
        salaryPayImportTempDataVO.setEmployeeIdAndYearAndMonthOfSalaryPayMap(employeeIdAndYearAndMonthOfSalaryPayMap);
    }

    /**
     * @description: 处理员工数据
     * @Author: hzk
     * @date: 2022/12/24 18:07
     * @param: [employeeTempDataOfCode, employeeCodeList, employeeIdsOfThisCycle]
     * @return: void
     **/
    private void handleEmployeeData(Map<String, SalaryPayImportOfEmpDataVO> employeeTempDataOfCode, List<String> employeeCodeList, Set<Long> employeeIdsOfThisCycle) {
        if (StringUtils.isNotEmpty(employeeCodeList)) {
            R<List<EmployeeDTO>> employeeR = employeeService.selectByCodes(employeeCodeList, SecurityConstants.INNER);
            List<EmployeeDTO> employeeDTOS = employeeR.getData();
            if (R.SUCCESS != employeeR.getCode()) {
                throw new ServiceException("未知错误，请联系管理员");
            }
            if (StringUtils.isEmpty(employeeDTOS)) {
                throw new ServiceException("当前员工编码未存在 请检查员工配置");
            }
            if (employeeCodeList.size() != employeeDTOS.size()) {
                throw new ServiceException("员工编码有误 请检查");
            }
            //绑定员工信息
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                String employeeCode = employeeDTO.getEmployeeCode();
                String employeeName = employeeDTO.getEmployeeName();
                Long employeeId = employeeDTO.getEmployeeId();
                SalaryPayImportOfEmpDataVO salaryPayImportOfEmpDataVO = employeeTempDataOfCode.get(employeeCode);
                String employeeNameOfImport = salaryPayImportOfEmpDataVO.getEmployeeName();
                salaryPayImportOfEmpDataVO.setEmployeeId(employeeId);
                employeeTempDataOfCode.put(employeeCode, salaryPayImportOfEmpDataVO);
                if (!employeeName.equals(employeeNameOfImport)) {
                    throw new ServiceException("员工编码与员工姓名不匹配:" + employeeCode + ":表格姓名为:[" + employeeNameOfImport + "],系统姓名为:[" + employeeName + "]");
                }
                employeeIdsOfThisCycle.add(employeeId);
            }
        }
    }

    /**
     * @description: 批量新增工资发薪
     * @Author: hzk
     * @date: 2022/12/24 18:01
     * @param: [insertSalaryPays, insertSalaryPayDetails, userId, nowDate, yearAndMonthOfSalaryPayDetailsMap]
     * @return: void
     **/
    private void insertSalaryPaysOfImport(List<SalaryPay> insertSalaryPays, List<SalaryPayDetails> insertSalaryPayDetails, Long userId, Date nowDate, Map<String, List<SalaryPayDetails>> yearAndMonthOfSalaryPayDetailsMap) {
        if (StringUtils.isNotEmpty(insertSalaryPays)) {
            int dealRows = 500;//一次处理条数
            int listSize = insertSalaryPays.size();
            if (listSize > 0) {
                for (int i = 0; i < listSize; i += dealRows) {
                    if (i + dealRows > listSize) {
                        dealRows = listSize - i;
                    }
                    List<SalaryPay> newList = insertSalaryPays.subList(i, i + dealRows);
                    salaryPayMapper.batchSalaryPay(newList);
                    //处理发薪明细，绑定发薪ID
                    for (SalaryPay insertSalaryPay : newList) {
                        String key = insertSalaryPay.getEmployeeId() + StrUtil.COLON + insertSalaryPay.getPayYear() + StrUtil.COLON + insertSalaryPay.getPayMonth();
                        Long salaryPayId = insertSalaryPay.getSalaryPayId();
                        if (yearAndMonthOfSalaryPayDetailsMap.containsKey(key)) {
                            List<SalaryPayDetails> salaryPayDetails = yearAndMonthOfSalaryPayDetailsMap.get(key);
                            for (SalaryPayDetails salaryPayDetail : salaryPayDetails) {
                                salaryPayDetail.setSalaryPayId(salaryPayId);
                                salaryPayDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                                salaryPayDetail.setCreateBy(userId);
                                salaryPayDetail.setCreateTime(nowDate);
                                salaryPayDetail.setUpdateBy(userId);
                                salaryPayDetail.setUpdateTime(nowDate);
                                insertSalaryPayDetails.add(salaryPayDetail);
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * @description: 批量修改工资发薪
     * @Author: hzk
     * @date: 2022/12/24 18:01
     * @param: [updateSalaryPays]
     * @return: void
     **/
    private void updateSalaryPaysOfImport(List<SalaryPay> updateSalaryPays) {
        if (StringUtils.isNotEmpty(updateSalaryPays)) {
            int dealRows = 500;//一次处理条数
            int listSize = updateSalaryPays.size();
            if (listSize > 0) {
                for (int i = 0; i < listSize; i += dealRows) {
                    if (i + dealRows > listSize) {
                        dealRows = listSize - i;
                    }
                    List<SalaryPay> newList = updateSalaryPays.subList(i, i + dealRows);
                    salaryPayMapper.updateSalaryPays(newList);
                }
            }
        }
    }

    /**
     * @description: 批量新增工资发薪明细
     * @Author: hzk
     * @date: 2022/12/24 18:01
     * @param: [insertSalaryPayDetails]
     * @return: void
     **/
    private void insertSalaryPayDetailsOfImport(List<SalaryPayDetails> insertSalaryPayDetails) {
        if (StringUtils.isNotEmpty(insertSalaryPayDetails)) {
            int dealRows = 500;//一次处理条数
            int listSize = insertSalaryPayDetails.size();
            if (listSize > 0) {
                for (int i = 0; i < listSize; i += dealRows) {
                    if (i + dealRows > listSize) {
                        dealRows = listSize - i;
                    }
                    List<SalaryPayDetails> newList = insertSalaryPayDetails.subList(i, i + dealRows);
                    salaryPayDetailsMapper.batchSalaryPayDetails(newList);
                }
            }
        }
    }

    /**
     * @description: 批量修改工资发薪明细
     * @Author: hzk
     * @date: 2022/12/24 18:01
     * @param: [updateSalaryPayDetails]
     * @return: void
     **/
    private void updateSalaryPayDetailsOfImport(List<SalaryPayDetails> updateSalaryPayDetails) {
        if (StringUtils.isNotEmpty(updateSalaryPayDetails)) {
            int dealRows = 500;//一次处理条数
            int listSize = updateSalaryPayDetails.size();
            if (listSize > 0) {
                for (int i = 0; i < listSize; i += dealRows) {
                    if (i + dealRows > listSize) {
                        dealRows = listSize - i;
                    }
                    List<SalaryPayDetails> newList = updateSalaryPayDetails.subList(i, i + dealRows);
                    salaryPayDetailsMapper.updateSalaryPayDetailss(newList);
                }
            }
        }
    }

    /**
     * @description: 构建SalaryPay对象
     * @Author: hzk
     * @date: 2022/12/23 11:26
     * @param: [salaryPay, employeeId, salaryAmount, allowanceAmount, welfareAmount, bonusAmount, withholdRemitTax, otherDeductions]
     * @return: void
     **/
    private void buildSalaryPay(SalaryPay salaryPay, Long employeeId, BigDecimal salaryAmount, BigDecimal allowanceAmount, BigDecimal welfareAmount, BigDecimal bonusAmount, BigDecimal withholdRemitTax, BigDecimal otherDeductions) {
        BigDecimal payAmount;
        payAmount = salaryAmount.add(allowanceAmount).add(welfareAmount).add(bonusAmount).subtract(withholdRemitTax).subtract(otherDeductions);
        salaryPay.setEmployeeId(employeeId);
        salaryPay.setSalaryAmount(salaryAmount);
        salaryPay.setAllowanceAmount(allowanceAmount);
        salaryPay.setWelfareAmount(welfareAmount);
        salaryPay.setBonusAmount(bonusAmount);
        salaryPay.setWithholdRemitTax(withholdRemitTax);
        salaryPay.setOtherDeductions(otherDeductions);
        salaryPay.setPayAmount(payAmount);
    }

    /**
     * 解析中文月份
     *
     * @param dateList 中文年月List
     * @return monthList
     */
    public static List<Integer> parseMonthCh(List<String> dateList) {
        List<Integer> monthList = new ArrayList<>();
        for (String dateString : dateList) {
            String chMonth = dateString.split("-")[0];
            switch (chMonth) {
                case "一月":
                    monthList.add(1);
                    break;
                case "二月":
                    monthList.add(2);
                    break;
                case "三月":
                    monthList.add(3);
                    break;
                case "四月":
                    monthList.add(4);
                    break;
                case "五月":
                    monthList.add(5);
                    break;
                case "六月":
                    monthList.add(6);
                    break;
                case "七月":
                    monthList.add(7);
                    break;
                case "八月":
                    monthList.add(8);
                    break;
                case "九月":
                    monthList.add(9);
                    break;
                case "十月":
                    monthList.add(10);
                    break;
                case "十一月":
                    monthList.add(11);
                    break;
                case "十二月":
                    monthList.add(12);
                    break;
            }
        }
        return monthList;
    }

    /**
     * 解析中文年份
     *
     * @param dateList 中文年月List
     * @return yearList
     */
    public static List<Integer> parseYearCh(List<String> dateList) {
        List<Integer> yearList = new ArrayList<>();
        for (String dateString : dateList) {
            String chYear = "20" + dateString.split("-")[1];
            yearList.add(Integer.valueOf(chYear));
        }
        return yearList;
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
        return new ArrayList<>();
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
            return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        employeeDTOR = getEmployee(salaryPayDTOList, departmentName);
        List<EmployeeDTO> employeeDTOS = getEmployeeDTOList(employeeDTOR);
        if (StringUtils.isEmpty(employeeDTOS)) {
            return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        setSalaryPayValue(salaryPayDTOList, employeeDTOS);
        if (StringUtils.isEmpty(salaryPayDTOList)) {
            return PageUtils.tableDataInfo(HttpStatus.SUCCESS, new ArrayList<>(), 0);
        }
        List<SalaryPayDTO> salaryPayDTOS = calculateAmount(salaryPayDTOList);
        List<SalaryPayDTO> partition;
        if (pageNum > PageUtil.totalPage(salaryPayDTOS.size(), pageSize)) {
            partition = new ArrayList<>();
        } else {
            partition = ListUtils.partition(salaryPayDTOS, pageSize).get(pageNum - 1);
        }
        return PageUtils.tableDataInfo(HttpStatus.SUCCESS, partition, salaryPayDTOS.size());
    }

    /**
     * 通过Id集合查找发行表
     *
     * @param salaryPayIds 薪酬发行ID集合
     * @return 结果
     */
    @Override
    public List<SalaryPayDTO> selectSalaryPayBySalaryPay(Integer isSelect, List<Long> salaryPayIds) {
        if (StringUtils.isNull(isSelect)) {
            throw new ServiceException("请选择请选择导出数据范围");
        }
        List<SalaryPayDTO> salaryPayDTOS;
        if (isSelect == 1) {
            salaryPayDTOS = salaryPayMapper.selectSalaryPayList(new SalaryPay());
        } else {
            if (StringUtils.isEmpty(salaryPayIds)) {
                throw new ServiceException("请选择要导出的月度工资");
            }
            for (Long salaryPayId : salaryPayIds) {
                if (StringUtils.isNull(salaryPayId)) {
                    throw new ServiceException("工资发薪id存在空值");
                }
            }
            salaryPayDTOS = salaryPayMapper.selectSalaryPayBySalaryPayIds(salaryPayIds);
        }
        if (StringUtils.isEmpty(salaryPayDTOS)) {
            throw new ServiceException("当前需要导出的工资发薪列表已不存在");
        }
        List<Long> employeeIds = salaryPayDTOS.stream().map(SalaryPayDTO::getEmployeeId).collect(Collectors.toList());
        // 为员工赋值
        R<List<EmployeeDTO>> employeeDTOR = employeeService.selectByEmployeeIds(employeeIds, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = employeeDTOR.getData();
        if (employeeDTOR.getCode() != 200) {
            throw new ServiceException("远程获取人员信息失败");
        }
        if (StringUtils.isEmpty(employeeDTOS)) {
            throw new ServiceException("当前人员信息已不存在，请检查员工配置");
        }
        List<Long> salaryPayIdList = new ArrayList<>();
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOS) {
            salaryPayIdList.add(salaryPayDTO.getSalaryPayId());
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (salaryPayDTO.getEmployeeId().equals(employeeDTO.getEmployeeId())) {
                    salaryPayDTO.setEmployeeName(employeeDTO.getEmployeeName());
                    salaryPayDTO.setEmployeeCode(employeeDTO.getEmployeeCode());
                    salaryPayDTO.setEmployeeDepartmentName(employeeDTO.getEmployeeDepartmentName());
                    salaryPayDTO.setEmployeePostName(employeeDTO.getEmployeePostName());
                    salaryPayDTO.setTopLevelDepartmentName(employeeDTO.getTopLevelDepartmentName());
                    break;
                }
            }
        }
        List<SalaryPayDetailsDTO> salaryPayDetailsDTOList = salaryPayDetailsService.selectSalaryPayDetailsBySalaryPayIds(salaryPayIdList);
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOS) {
            List<SalaryPayDetailsDTO> salaryPayDetailsDTOS = new ArrayList<>();
            for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOList) {
                if (salaryPayDTO.getSalaryPayId().equals(salaryPayDetailsDTO.getSalaryPayId())) {
                    salarySetName(salaryPayDetailsDTO);
                    salaryPayDetailsDTOS.add(salaryPayDetailsDTO);
                }
            }
            salaryPayDTO.setSalaryPayDetailsDTOList(salaryPayDetailsDTOS);
        }
        return salaryPayDTOS;
    }

    /**
     * 为一级工资二级工资附名称
     *
     * @param salaryPayDetailsDTO 发薪详情表
     */
    @Override
    public void salarySetName(SalaryPayDetailsDTO salaryPayDetailsDTO) {
        Integer secondLevelItem = salaryPayDetailsDTO.getSecondLevelItem();
        if (StringUtils.isNotNull(secondLevelItem)) {
            switch (secondLevelItem) {
                case 1:
                    salaryPayDetailsDTO.setSecondLevelItemValue("工资");
                    break;
                case 2:
                    salaryPayDetailsDTO.setSecondLevelItemValue("津贴");
                    break;
                case 3:
                    salaryPayDetailsDTO.setSecondLevelItemValue("福利");
                    break;
                case 4:
                    salaryPayDetailsDTO.setSecondLevelItemValue("奖金");
                    break;
                case 5:
                    salaryPayDetailsDTO.setSecondLevelItemValue("代扣代缴");
                    break;
                case 6:
                    salaryPayDetailsDTO.setSecondLevelItemValue("其他扣款");
                    break;
            }
        }
    }


    /**
     * 根据员工ID查询工资条
     *
     * @param employeeId 员工ID
     * @return List
     */
    @Override
    public List<SalaryPayDTO> selectByEmployeeId(Long employeeId) {
        return salaryPayMapper.selectByEmployeeId(employeeId);
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
        BigDecimal totalCompensationSum;
        // 获取工资，津贴，福利，奖金合计
        for (SalaryPayDTO salaryPayDTO : salaryPayDTOList) {
            salaryAmountSum = salaryAmountSum.add(Optional.ofNullable(salaryPayDTO.getSalaryAmount()).orElse(BigDecimal.ZERO));
            allowanceAmountSum = allowanceAmountSum.add(Optional.ofNullable(salaryPayDTO.getAllowanceAmount()).orElse(BigDecimal.ZERO));
            welfareAmountSum = welfareAmountSum.add(Optional.ofNullable(salaryPayDTO.getWelfareAmount()).orElse(BigDecimal.ZERO));
            bonusAmountSum = bonusAmountSum.add(Optional.ofNullable(salaryPayDTO.getBonusAmount()).orElse(BigDecimal.ZERO));
        }
        totalCompensationSum = salaryAmountSum.add(allowanceAmountSum).add(welfareAmountSum).add(bonusAmountSum);
        salaryStructure.setSalaryAmountSum(salaryAmountSum);
        salaryStructure.setAllowanceAmountSum(allowanceAmountSum);
        salaryStructure.setWelfareAmountSum(welfareAmountSum);
        salaryStructure.setBonusAmountSum(bonusAmountSum);
        salaryStructure.setTotalCompensationSum(totalCompensationSum);
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
                if (!employeeIds.contains(salaryPayDTO.getEmployeeId())) {
                    employeeIds.add(salaryPayDTO.getEmployeeId());
                }
            }
            employeeDTOR = employeeService.selectByEmployeeIds(employeeIds, SecurityConstants.INNER);
        }
        return employeeDTOR;
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
            salaryPayDTOList = salaryPayMapper.selectSalaryPayBySomeMonth(null, null, null);
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
     * @param employeeDTOR 人员DTO
     * @return List
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
        List<SalaryPayDTO> collect = salaryPayDTOList.stream().filter(salaryPayDTO -> StringUtils.isNotEmpty(salaryPayDTO.getEmployeeDepartmentName())).collect(Collectors.toList());
        Map<String, List<SalaryPayDTO>> salaryPayMap = collect.stream().collect(Collectors.groupingBy(SalaryPayDTO::getEmployeeDepartmentName));

        List<SalaryPayDTO> salaryPayDTOS = new ArrayList<>(10);
        for (String departmentName : salaryPayMap.keySet()) {
            List<SalaryPayDTO> salaryPayDTOList1 = salaryPayMap.get(departmentName);
            BigDecimal salaryAmountValue = BigDecimal.ZERO;// 工资金额
            BigDecimal allowanceAmountValue = BigDecimal.ZERO;// 津贴金额
            BigDecimal welfareAmountValue = BigDecimal.ZERO;// 福利金额
            BigDecimal bonusAmountValue = BigDecimal.ZERO;// 奖金金额
//                SalaryPayDTO salaryPay;
//                if (StringUtils.isEmpty(salaryPayDTOList1)) {
//                    salaryPay = new SalaryPayDTO();
//                } else {
//                    salaryPay = salaryPayDTOList1.get(0);
//                }
            for (SalaryPayDTO salaryPayDTO : salaryPayDTOList1) {
                salaryAmountValue = salaryAmountValue.add(Optional.ofNullable(salaryPayDTO.getSalaryAmount()).orElse(BigDecimal.ZERO));
                allowanceAmountValue = allowanceAmountValue.add(Optional.ofNullable(salaryPayDTO.getAllowanceAmount()).orElse(BigDecimal.ZERO));
                welfareAmountValue = welfareAmountValue.add(Optional.ofNullable(salaryPayDTO.getWelfareAmount()).orElse(BigDecimal.ZERO));
                bonusAmountValue = bonusAmountValue.add(Optional.ofNullable(salaryPayDTO.getBonusAmount()).orElse(BigDecimal.ZERO));
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
                fixedProportion = fixedValue.multiply(new BigDecimal(100)).divide(paymentBonus, 2, RoundingMode.HALF_UP);
                floatProportion = bonusAmountValue.multiply(new BigDecimal(100)).divide(paymentBonus, 2, RoundingMode.HALF_UP);
            } else {
                fixedProportion = BigDecimal.ZERO;
                floatProportion = BigDecimal.ZERO;
            }
            SalaryPayDTO salaryPayDTO = new SalaryPayDTO();
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
        return salaryPayDTOS;
    }

    @Override
    public void handleResult(List<SalaryPayDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(SalaryPayDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

}

