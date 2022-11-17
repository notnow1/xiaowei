package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryPay;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryPayExcel;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayDetailsService;
import net.qixiaowei.operate.cloud.service.salary.ISalaryPayService;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


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
        return salaryPayMapper.selectSalaryPayBySalaryPayId(salaryPayId);
    }

    /**
     * 查询工资发薪表列表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 工资发薪表
     */
    @Override
    public List<SalaryPayDTO> selectSalaryPayList(SalaryPayDTO salaryPayDTO) {
        EmployeeDTO employeeDTO = new EmployeeDTO();
        String postName = salaryPayDTO.getPostName();
        String employeeName = salaryPayDTO.getEmployeeName();
        String departmentName = salaryPayDTO.getDepartmentName();
//        employeeDTO.setName
//        employeeService.selectRemoteList()
        SalaryPay salaryPay = new SalaryPay();
        BeanUtils.copyProperties(salaryPayDTO, salaryPay);
        return salaryPayMapper.selectSalaryPayList(salaryPay);
    }

    /**
     * 新增工资发薪表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 结果
     */
    @Override
    public SalaryPayDTO insertSalaryPay(SalaryPayDTO salaryPayDTO) {
        Date payTime = salaryPayDTO.getPayTime();
        Long employeeId = salaryPayDTO.getEmployeeId();
        int year = DateUtils.getYear(payTime);
        int month = DateUtils.getMonth(payTime);
        List<SalaryPayDTO> salaryPayDTOList = salaryPayDTO.getSalaryPayDTOList();
        List<SalaryItemDTO> salaryItemDTOList = salaryPayDTO.getSalaryItemDTOList();
        SalaryPay salaryPayByTime = new SalaryPay();
        salaryPayByTime.setEmployeeId(employeeId);
        salaryPayByTime.setPayYear(year);
        salaryPayByTime.setPayMonth(month);
        insertCheck(payTime, employeeId, salaryPayDTOList, salaryItemDTOList, salaryPayByTime);
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
     * @param payTime           时间
     * @param employeeId        员工ID
     * @param salaryPayDTOList  工资发薪列表
     * @param salaryItemDTOList 工资项列表
     * @param salaryPayByTime   工资时间
     */
    private void insertCheck(Date payTime, Long employeeId, List<SalaryPayDTO> salaryPayDTOList, List<SalaryItemDTO> salaryItemDTOList, SalaryPay salaryPayByTime) {
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
     * 修改工资发薪表
     *
     * @param salaryPayDTO 工资发薪表
     * @return 结果
     */
    @Override
    public int updateSalaryPay(SalaryPayDTO salaryPayDTO) {
        SalaryPay salaryPay = new SalaryPay();
        BeanUtils.copyProperties(salaryPayDTO, salaryPay);
        salaryPay.setUpdateTime(DateUtils.getNowDate());
        salaryPay.setUpdateBy(SecurityUtils.getUserId());
        return salaryPayMapper.updateSalaryPay(salaryPay);
    }

    /**
     * 逻辑批量删除工资发薪表
     *
     * @param salaryPayIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteSalaryPayBySalaryPayIds(List<Long> salaryPayIds) {
        return salaryPayMapper.logicDeleteSalaryPayBySalaryPayIds(salaryPayIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除工资发薪表信息
     *
     * @param salaryPayId 工资发薪表主键
     * @return 结果
     */
    @Override
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
    public int logicDeleteSalaryPayBySalaryPayId(SalaryPayDTO salaryPayDTO) {
        SalaryPay salaryPay = new SalaryPay();
        salaryPay.setSalaryPayId(salaryPayDTO.getSalaryPayId());
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
        List<Long> stringList = new ArrayList();
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
        List<SalaryPay> salaryPayList = new ArrayList();

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
        List<SalaryPay> salaryPayList = new ArrayList();

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
    public void importSalaryPay(List<SalaryPayExcel> list) {
        List<SalaryPay> salaryPayList = new ArrayList<>();
        list.forEach(l -> {
            SalaryPay salaryPay = new SalaryPay();
            BeanUtils.copyProperties(l, salaryPay);
            salaryPay.setCreateBy(SecurityUtils.getUserId());
            salaryPay.setCreateTime(DateUtils.getNowDate());
            salaryPay.setUpdateTime(DateUtils.getNowDate());
            salaryPay.setUpdateBy(SecurityUtils.getUserId());
            salaryPay.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            salaryPayList.add(salaryPay);
        });
        try {
            salaryPayMapper.batchSalaryPay(salaryPayList);
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
}

