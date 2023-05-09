package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.salary.ThirdLevelSalaryCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryItem;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDetailsDTO;
import net.qixiaowei.operate.cloud.api.vo.salary.SalaryItemVO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryItemExcel;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusPayApplicationMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryItemMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayDetailsMapper;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;


/**
 * SalaryItemService业务层处理
 *
 * @author Graves
 * @since 2022-10-05
 */
@Service
public class SalaryItemServiceImpl implements ISalaryItemService {


    private static final List<SalaryItem> INIT_SALARY_ITEM = new ArrayList<>(4);

    static {
        INIT_SALARY_ITEM.add(SalaryItem.builder().firstLevelItem(1).secondLevelItem(1).thirdLevelItem("基本工资").scope(1).sort(1).build());
        INIT_SALARY_ITEM.add(SalaryItem.builder().firstLevelItem(2).secondLevelItem(4).thirdLevelItem("战略奖").scope(2).sort(2).build());
        INIT_SALARY_ITEM.add(SalaryItem.builder().firstLevelItem(2).secondLevelItem(4).thirdLevelItem("项目奖").scope(1).sort(3).build());
        INIT_SALARY_ITEM.add(SalaryItem.builder().firstLevelItem(2).secondLevelItem(4).thirdLevelItem("绩效奖金").scope(1).sort(4).build());
    }

    @Autowired
    private SalaryItemMapper salaryItemMapper;

    @Autowired
    private SalaryPayDetailsMapper salaryPayDetailsMapper;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private BonusPayApplicationMapper bonusPayApplicationMapper;

    /**
     * 查询工资项
     *
     * @param salaryItemId 工资项主键
     * @return 工资项
     */
    @Override
    public SalaryItemDTO selectSalaryItemBySalaryItemId(Long salaryItemId) {
        return salaryItemMapper.selectSalaryItemBySalaryItemId(salaryItemId);
    }

    /**
     * 查询工资项分页列表
     *
     * @param salaryItemDTO 工资项
     * @return 工资项集合
     */
    @DataScope(businessAlias = "si")
    @Override
    public List<SalaryItemDTO> selectSalaryItemPageList(SalaryItemDTO salaryItemDTO) {
        SalaryItem salaryItem = new SalaryItem();
        Map<String, Object> params = salaryItemDTO.getParams();
        salaryItem.setParams(params);
        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemList(salaryItem);
        this.handleResult(salaryItemDTOS);
        return salaryItemDTOS;
    }

    /**
     * 查询工资项列表
     *
     * @param salaryItemDTO 工资项
     * @return 工资项
     */
    @DataScope(businessAlias = "si")
    @Override
    public List<SalaryItemDTO> selectSalaryItemList(SalaryItemDTO salaryItemDTO) {
        SalaryItem salaryItem = new SalaryItem();
        Map<String, Object> params = salaryItemDTO.getParams();
        salaryItem.setParams(params);
        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemList(salaryItem);
        if (StringUtils.isEmpty(salaryItemDTOS)) {
            return salaryItemDTOS;
        }
        List<SalaryItemDTO> salaryItemDTOList = salaryItemDTOS.stream().sorted(Comparator.comparing(SalaryItemDTO::getFirstLevelItem)
                .thenComparing(SalaryItemDTO::getSecondLevelItem)
                .thenComparing(SalaryItemDTO::getSort)).collect(Collectors.toList());
        for (SalaryItemDTO itemDTO : salaryItemDTOList) {
            if (itemDTO.getScope() == 1) {
                itemDTO.setScopeName("部门级");
            } else {
                itemDTO.setScopeName("公司级");
            }
        }
        return salaryItemDTOList;
    }

    /**
     * 工资项编辑列表
     *
     * @param salaryItemDTO 工资项dto
     * @return 结果
     */
    @Override
    public List<SalaryItemVO> selectSalaryItemEditList(SalaryItemDTO salaryItemDTO) {
        SalaryItem salaryItem = new SalaryItem();
        salaryItem.setStatus(0);
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemEditList(salaryItem);
        for (SalaryItemDTO itemDTO : salaryItemDTOS) {
            if (ThirdLevelSalaryCode.containThirdItems(itemDTO.getThirdLevelItem())) {
                itemDTO.setIsPreset(1);
            }
        }
        List<SalaryItemDTO> sortSalaryItemDTOS = salaryItemDTOS.stream().sorted(Comparator
                        .comparing(SalaryItemDTO::getFirstLevelItem)
                        .thenComparing(SalaryItemDTO::getSecondLevelItem)
                        .thenComparing(SalaryItemDTO::getSort))
                .collect(Collectors.toList());
        return getSalaryItemVOS(sortSalaryItemDTOS);
    }

    /**
     * 获取六个列表
     *
     * @param sortSalaryItemDTOS 分组后的工资项列表
     * @return 结果
     */
    private static List<SalaryItemVO> getSalaryItemVOS(List<SalaryItemDTO> sortSalaryItemDTOS) {
        List<SalaryItemDTO> wageWages = new ArrayList<>();
        List<SalaryItemDTO> wageAllowance = new ArrayList<>();
        List<SalaryItemDTO> wageWelfare = new ArrayList<>();
        List<SalaryItemDTO> bonusBonus = new ArrayList<>();
        List<SalaryItemDTO> deductionWithhold = new ArrayList<>();
        List<SalaryItemDTO> deductionDeduction = new ArrayList<>();
        for (SalaryItemDTO sortSalaryItemDTO : sortSalaryItemDTOS) {
            Integer firstLevelItem = sortSalaryItemDTO.getFirstLevelItem();
            Integer secondLevelItem = sortSalaryItemDTO.getSecondLevelItem();
            if (firstLevelItem == 1) {
                if (secondLevelItem == 1) {
                    wageWages.add(sortSalaryItemDTO);
                } else if (secondLevelItem == 2) {
                    wageAllowance.add(sortSalaryItemDTO);
                } else if (secondLevelItem == 3) {
                    wageWelfare.add(sortSalaryItemDTO);
                }
            } else if (firstLevelItem == 2 && secondLevelItem == 4) {
                bonusBonus.add(sortSalaryItemDTO);
            } else if (firstLevelItem == 3) {
                if (secondLevelItem == 5) {
                    deductionWithhold.add(sortSalaryItemDTO);
                } else if (secondLevelItem == 6) {
                    deductionDeduction.add(sortSalaryItemDTO);
                }
            }
        }
        List<SalaryItemVO> salaryItemVOS = new ArrayList<>();
        SalaryItemVO salaryItemVO = new SalaryItemVO();
        salaryItemVO.setFirstLevelItem(1);
        salaryItemVO.setSecondLevelItem(1);
        salaryItemVO.setSalaryItemDTOS(wageWages);
        salaryItemVO.setSalaryItemName("wageWages");
        salaryItemVOS.add(salaryItemVO);
        salaryItemVO = new SalaryItemVO();
        salaryItemVO.setFirstLevelItem(1);
        salaryItemVO.setSecondLevelItem(2);
        salaryItemVO.setSalaryItemDTOS(wageAllowance);
        salaryItemVO.setSalaryItemName("wageAllowance");
        salaryItemVOS.add(salaryItemVO);
        salaryItemVO = new SalaryItemVO();
        salaryItemVO.setFirstLevelItem(1);
        salaryItemVO.setSecondLevelItem(3);
        salaryItemVO.setSalaryItemDTOS(wageWelfare);
        salaryItemVO.setSalaryItemName("wageWelfare");
        salaryItemVOS.add(salaryItemVO);
        salaryItemVO = new SalaryItemVO();
        salaryItemVO.setFirstLevelItem(2);
        salaryItemVO.setSecondLevelItem(4);
        salaryItemVO.setSalaryItemDTOS(bonusBonus);
        salaryItemVO.setSalaryItemName("bonusBonus");
        salaryItemVOS.add(salaryItemVO);
        salaryItemVO = new SalaryItemVO();
        salaryItemVO.setFirstLevelItem(3);
        salaryItemVO.setSecondLevelItem(5);
        salaryItemVO.setSalaryItemDTOS(deductionWithhold);
        salaryItemVO.setSalaryItemName("deductionWithhold");
        salaryItemVOS.add(salaryItemVO);
        salaryItemVO = new SalaryItemVO();
        salaryItemVO.setFirstLevelItem(3);
        salaryItemVO.setSecondLevelItem(6);
        salaryItemVO.setSalaryItemDTOS(deductionDeduction);
        salaryItemVO.setSalaryItemName("deductionDeduction");
        salaryItemVOS.add(salaryItemVO);
        salaryItemVOS.forEach(SalaryItemServiceImpl::salaryVOSetName);
        return salaryItemVOS;
    }

    /**
     * 批量修改工资项
     *
     * @param salaryItemDTOSAfter 项目dto列表
     * @return 结果
     */
    @Override
    @Transactional
    public int editSalaryItems(List<SalaryItemDTO> salaryItemDTOSAfter) {
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOSAfter) {
            if (StringUtils.isNull(salaryItemDTO.getThirdLevelItem())) {
                throw new ServiceException("请录入三级薪酬项目");
            }
            if (salaryItemDTO.getSecondLevelItem() != 2 && StringUtils.isNotNull(salaryItemDTO.getScope())) {
                throw new ServiceException("只有奖金包才可以选择级别");
            }
        }
        List<SalaryItemDTO> salaryItemDTOSBefore = salaryItemMapper.selectSalaryItemList(new SalaryItem());
        List<SalaryItemDTO> editSalaryItemDTOS = salaryItemDTOSAfter.stream().filter(s -> salaryItemDTOSBefore.stream().map(SalaryItemDTO::getSalaryItemId)
                .collect(Collectors.toList()).contains(s.getSalaryItemId())).collect(Collectors.toList());
        List<SalaryItemDTO> addSalaryItemDTOS = salaryItemDTOSAfter.stream().filter(s -> !salaryItemDTOSBefore.stream().map(SalaryItemDTO::getSalaryItemId)
                .collect(Collectors.toList()).contains(s.getSalaryItemId())).collect(Collectors.toList());
        List<SalaryItemDTO> delSalaryItemDTOS = salaryItemDTOSAfter.stream().filter(s -> !salaryItemDTOSBefore.stream().map(SalaryItemDTO::getSalaryItemId)
                .collect(Collectors.toList()).contains(s.getSalaryItemId())).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(editSalaryItemDTOS)) {
            this.updateSalaryItems(editSalaryItemDTOS);
        }
        if (StringUtils.isNotEmpty(addSalaryItemDTOS)) {
            this.insertSalaryItems(addSalaryItemDTOS);
        }
        if (StringUtils.isNotEmpty(delSalaryItemDTOS)) {
            List<Long> salaryItemIds = delSalaryItemDTOS.stream().map(SalaryItemDTO::getSalaryItemId).collect(Collectors.toList());
            this.logicDeleteSalaryItemBySalaryItemIds(salaryItemIds);
        }
        return 1;
    }

    /**
     * 为一级工资二级工资附名称
     *
     * @param itemDTO 项目
     */
    @Override
    public void salarySetName(SalaryItemDTO itemDTO) {
        Integer firstLevelItem = itemDTO.getFirstLevelItem();
        Integer secondLevelItem = itemDTO.getSecondLevelItem();
        switch (firstLevelItem) {
            case 1:
                itemDTO.setFirstLevelItemValue("总工资包");
                break;
            case 2:
                itemDTO.setFirstLevelItemValue("总奖金包");
                break;
            case 3:
                itemDTO.setFirstLevelItemValue("总扣减项");
                break;
        }
        switch (secondLevelItem) {
            case 1:
                itemDTO.setSecondLevelItemValue("工资");
                break;
            case 2:
                itemDTO.setSecondLevelItemValue("津贴");
                break;
            case 3:
                itemDTO.setSecondLevelItemValue("福利");
                break;
            case 4:
                itemDTO.setSecondLevelItemValue("奖金");
                break;
            case 5:
                itemDTO.setSecondLevelItemValue("代扣代缴");
                break;
            case 6:
                itemDTO.setSecondLevelItemValue("其他扣款");
                break;
        }
    }

    /**
     * 为一级工资二级工资附名称
     *
     * @param itemDTO 项目
     */
    public static void salaryVOSetName(SalaryItemVO itemDTO) {
        Integer firstLevelItem = itemDTO.getFirstLevelItem();
        Integer secondLevelItem = itemDTO.getSecondLevelItem();
        switch (firstLevelItem) {
            case 1:
                itemDTO.setFirstLevelItemValue("总工资包");
                break;
            case 2:
                itemDTO.setFirstLevelItemValue("总奖金包");
                break;
            case 3:
                itemDTO.setFirstLevelItemValue("总扣减项");
                break;
        }
        switch (secondLevelItem) {
            case 1:
                itemDTO.setSecondLevelItemValue("工资");
                break;
            case 2:
                itemDTO.setSecondLevelItemValue("津贴");
                break;
            case 3:
                itemDTO.setSecondLevelItemValue("福利");
                break;
            case 4:
                itemDTO.setSecondLevelItemValue("奖金");
                break;
            case 5:
                itemDTO.setSecondLevelItemValue("代扣代缴");
                break;
            case 6:
                itemDTO.setSecondLevelItemValue("其他扣款");
                break;
        }
    }

    /**
     * 新增工资项
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    @Transactional
    @Override
    public SalaryItemDTO insertSalaryItem(SalaryItemDTO salaryItemDTO) {
        String thirdLevelItem = salaryItemDTO.getThirdLevelItem();
        if (StringUtils.isEmpty(thirdLevelItem)) {
            throw new ServiceException("三级工资项目不可为空");
        }
        if (ThirdLevelSalaryCode.containThirdItems(thirdLevelItem)) {
            throw new ServiceException("新增三级项目不能为预置三级项目");
        }
        SalaryItemDTO salaryItemByThirdLevelItem = salaryItemMapper.getSalaryItemByThirdLevelItem(thirdLevelItem);
        if (StringUtils.isNotNull(salaryItemByThirdLevelItem)) {
            throw new ServiceException("三级项目名称不能重复");
        }
        Integer maxSort = salaryItemMapper.selectMaxSort();
        SalaryItem salaryItem = new SalaryItem();
        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
        if (StringUtils.isNull(salaryItemDTO.getScope())) {
            salaryItem.setScope(1);
        }
        salaryItem.setStatus(0);
        salaryItem.setSort(maxSort + 1);
        salaryItem.setCreateBy(SecurityUtils.getUserId());
        salaryItem.setCreateTime(DateUtils.getNowDate());
        salaryItem.setUpdateTime(DateUtils.getNowDate());
        salaryItem.setUpdateBy(SecurityUtils.getUserId());
        salaryItem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        salaryItemMapper.insertSalaryItem(salaryItem);
        return salaryItemDTO.setSalaryItemId(salaryItem.getSalaryItemId());
    }

    /**
     * 初始化工资项
     *
     * @return 结果
     */
    @Override
    public Boolean initSalaryItem(Long userId) {
        boolean initSuccess = false;
        Date nowDate = DateUtils.getNowDate();
        List<SalaryItem> salaryItems = new ArrayList<>();
        for (SalaryItem salaryItem : INIT_SALARY_ITEM) {
            salaryItem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            salaryItem.setCreateBy(userId);
            salaryItem.setUpdateBy(userId);
            salaryItem.setCreateTime(nowDate);
            salaryItem.setUpdateTime(nowDate);
            salaryItems.add(salaryItem);
        }
        int i = salaryItemMapper.batchSalaryItem(salaryItems);
        if (i == 4) {
            initSuccess = true;
        }
        return initSuccess;
    }

    /**
     * 修改工资项
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    @Transactional
    @Override
    public int updateSalaryItem(SalaryItemDTO salaryItemDTO) {
        String thirdLevelItem = salaryItemDTO.getThirdLevelItem();
        Long salaryItemId = salaryItemDTO.getSalaryItemId();
        if (StringUtils.isNull(salaryItemId)) {
            throw new ServiceException("工资条配置id不能为空！");
        }
        if (StringUtils.isEmpty(thirdLevelItem)) {
            throw new ServiceException("三级工资项目不可为空！");
        }
        if (ThirdLevelSalaryCode.containThirdItems(thirdLevelItem)) {
            throw new ServiceException("新增三级项目不能为预置三级项目");
        }
        SalaryItemDTO salaryItemByThirdLevelItem = salaryItemMapper.getSalaryItemByThirdLevelItem(thirdLevelItem);
        if (StringUtils.isNotNull(salaryItemByThirdLevelItem)) {
            if (!salaryItemByThirdLevelItem.getSalaryItemId().equals(salaryItemId)) {
                throw new ServiceException("三级项目名称不能重复");
            }
        }
        SalaryItem salaryItem = new SalaryItem();
        salaryItem.setSalaryItemId(salaryItemId);
        salaryItem.setThirdLevelItem(thirdLevelItem);
        salaryItem.setUpdateTime(DateUtils.getNowDate());
        salaryItem.setUpdateBy(SecurityUtils.getUserId());
        return salaryItemMapper.updateSalaryItem(salaryItem);
    }

    /**
     * 逻辑批量删除工资项
     *
     * @param salaryItemIds 需要删除的工资项主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteSalaryItemBySalaryItemIds(List<Long> salaryItemIds) {
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.getSalaryItemByIds(salaryItemIds);
        if (StringUtils.isEmpty(salaryItemDTOS) && salaryItemDTOS.size() < salaryItemIds.size()) {
            throw new ServiceException("该工资条配置已不存在");
        }
        for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
            if (ThirdLevelSalaryCode.containThirdItems(salaryItemDTO.getThirdLevelItem())) {
                throw new ServiceException(salaryItemDTO.getThirdLevelItem() + "为预置配置，不可以删除");
            }
        }
        isQuote(salaryItemIds, salaryItemDTOS);
        salaryItemMapper.logicDeleteSalaryItemBySalaryItemIds(salaryItemIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return 1;
    }

    /**
     * 引用校验
     *
     * @param salaryItemIds  工资项Id集合
     * @param salaryItemDTOS 工资项DTO集合
     */
    private void isQuote(List<Long> salaryItemIds, List<SalaryItemDTO> salaryItemDTOS) {
        StringBuilder quoteReminder = new StringBuilder("");
        // 工资条
        List<SalaryPayDetailsDTO> salaryPayDetailsDTOS = salaryPayDetailsMapper.selectSalaryPayDetailsByItemIds(salaryItemIds);
        if (StringUtils.isNotEmpty(salaryPayDetailsDTOS)) {
            Set<Long> employeeIds = new HashSet<>();
            StringBuilder salaryItemName = new StringBuilder("");
            for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOS) {
                    if (salaryPayDetailsDTO.getSalaryItemId().equals(salaryItemDTO.getSalaryItemId())) {
                        employeeIds.add(salaryPayDetailsDTO.getEmployeeId());
                    }
                }
            }
            for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                for (SalaryPayDetailsDTO salaryPayDetailsDTO : salaryPayDetailsDTOS) {
                    if (salaryPayDetailsDTO.getSalaryItemId().equals(salaryItemDTO.getSalaryItemId())) {
                        salaryItemName.append(salaryItemDTO.getThirdLevelItem()).append(",");
                        break;
                    }
                }
            }
            if (StringUtils.isNotEmpty(employeeIds)) {
                R<List<EmployeeDTO>> listR = employeeService.selectByEmployeeIds(new ArrayList<>(employeeIds), SecurityConstants.INNER);
                if (listR.getCode() != 200) {
                    throw new ServiceException("远程调用人员信息失败 请联系管理员");
                }
                List<EmployeeDTO> employeeDTOS = listR.getData();
                List<String> employeeNames = employeeDTOS.stream().map(EmployeeDTO::getEmployeeName).collect(Collectors.toList());
                quoteReminder.append("薪酬类别【三级工资项目】的【")
                        .append(salaryItemName.deleteCharAt(salaryItemName.length() - 1))
                        .append("】已被月度工资数据管理中的【员工姓名】为【")
                        .append(employeeNames)
                        .append("】引用 无法删除\n");
            }
        }
        // 奖金发放申请
        List<BonusPayApplicationDTO> bonusPayApplicationDTOS = bonusPayApplicationMapper.selectBonusPayApplicationBySalaryItemId(salaryItemIds);
        if (StringUtils.isNotEmpty(bonusPayApplicationDTOS)) {
            StringBuilder salaryItemName = new StringBuilder("");
            for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                if (StringUtils.isNull(salaryItemDTO.getSalaryItemId())) {
                    continue;
                }
                for (BonusPayApplicationDTO bonusPayApplicationDTO : bonusPayApplicationDTOS) {
                    if (StringUtils.isNull(bonusPayApplicationDTO.getSalaryItemId())) {
                        continue;
                    }
                    if (bonusPayApplicationDTO.getSalaryItemId().equals(salaryItemDTO.getSalaryItemId())) {
                        salaryItemName.append(salaryItemDTO.getThirdLevelItem()).append(",");
                        break;
                    }
                }
            }
            quoteReminder.append("薪酬类别【三级工资项目】的【")
                    .append(salaryItemName.deleteCharAt(salaryItemName.length() - 1))
                    .append("】已被奖金发放申请中的【奖项类别】引用 无法删除\n");
        }
        if (quoteReminder.length() != 0) {
            throw new ServiceException(quoteReminder.toString());
        }
    }

    /**
     * 物理删除工资项信息
     *
     * @param salaryItemId 工资项主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSalaryItemBySalaryItemId(Long salaryItemId) {
        List<Long> salaryItemIds = new ArrayList<>();
        salaryItemIds.add(salaryItemId);
        return logicDeleteSalaryItemBySalaryItemIds(salaryItemIds);
    }

    /**
     * 根据salaryId查询工资详情
     *
     * @param salaryId 工资项ID
     * @return 结果
     */
    @Override
    public SalaryItemDTO detailSalaryItemBySalaryId(Long salaryId) {
        if (StringUtils.isNull(salaryId)) {
            throw new ServiceException("工资条id不可以为空");
        }
        SalaryItemDTO salaryItemDTO = salaryItemMapper.selectSalaryItemBySalaryItemId(salaryId);
        if (StringUtils.isNull(salaryItemDTO)) {
            throw new ServiceException("当前的工资条已经不存在");
        }
        return salaryItemDTO;
    }

    /**
     * 导出工资条
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    @Override
    public List<SalaryItemExcel> exportSalaryExcel(SalaryItemDTO salaryItemDTO) {
        List<SalaryItemDTO> salaryItemDTOList;
        List<Long> salaryItemIds = salaryItemDTO.getSelectSalaryItem();
        if (StringUtils.isNotEmpty(salaryItemIds)) {
            salaryItemDTOList = salaryItemMapper.getSalaryItemByIds(salaryItemIds);
        } else {
            SalaryItem salaryItem = new SalaryItem();
            BeanUtils.copyProperties(salaryItemDTO, salaryItem);
            salaryItemDTOList = salaryItemMapper.selectSalaryItemList(salaryItem);
        }
        List<SalaryItemExcel> salaryItemExcelList = new ArrayList<>();
        for (SalaryItemDTO dto : salaryItemDTOList) {
            SalaryItemExcel salaryItemExcel = new SalaryItemExcel();
            salarySetName(dto);
            BeanUtils.copyProperties(dto, salaryItemExcel);
            if (dto.getScope() == 1) {
                salaryItemExcel.setScope("部门");
            } else {
                salaryItemExcel.setScope("公司");
            }
            salaryItemExcelList.add(salaryItemExcel);
        }
        return salaryItemExcelList;
    }

    /**
     * 通过ID集合查找工资项
     *
     * @param salaryItemIds
     * @return
     */
    @Override
    public List<SalaryItemDTO> selectSalaryItemBySalaryItemIds(List<Long> salaryItemIds) {
        return salaryItemMapper.selectSalaryItemBySalaryItemIds(salaryItemIds);
    }

    /**
     * 查找奖金的三级工资项目
     *
     * @param salaryItemDTO dto
     * @return List
     */
    @Override
    public List<Map<String, String>> selectBonusItemList(SalaryItemDTO salaryItemDTO) {
        SalaryItem salaryItem = new SalaryItem();
        salaryItem.setSecondLevelItem(4);
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemList(salaryItem);
        List<Map<String, String>> list = new ArrayList<>();
        for (SalaryItemDTO itemDTO : salaryItemDTOS) {
            Map<String, String> map = new HashMap<>();
            map.put("salaryItemId", itemDTO.getSalaryItemId().toString());
            map.put("thirdLevelItem", itemDTO.getThirdLevelItem());
            list.add(map);
        }
        return list;
    }

    /**
     * 查找二级为奖金的三级工资条
     *
     * @return
     */
    @Override
    public List<SalaryItemDTO> applyBonusList() {
        return salaryItemMapper.applyBonusList();
    }

    /**
     * 逻辑删除工资项信息
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteSalaryItemBySalaryItemId(SalaryItemDTO salaryItemDTO) {
        Long salaryItemId = salaryItemDTO.getSalaryItemId();
//        SalaryItemDTO salaryItemBySalaryItemId = salaryItemMapper.selectSalaryItemBySalaryItemId(salaryItemId);
        if (StringUtils.isNull(salaryItemId)) {
            throw new ServiceException("工资条ID不能为空");
        }
//        if (ThirdLevelSalaryCode.containThirdItems(salaryItemBySalaryItemId.getThirdLevelItem())) {
//            throw new ServiceException(salaryItemBySalaryItemId.getThirdLevelItem() + "为预置配置，不可以删除");
//        }
        ArrayList<Long> salaryItems = new ArrayList<>();
        salaryItems.add(salaryItemId);
        return logicDeleteSalaryItemBySalaryItemIds(salaryItems);
//        SalaryItem salaryItem = new SalaryItem();
//        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
//        return salaryItemMapper.logicDeleteSalaryItemBySalaryItemId(salaryItem, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除工资项信息
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSalaryItemBySalaryItemId(SalaryItemDTO salaryItemDTO) {
        SalaryItem salaryItem = new SalaryItem();
        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
        return salaryItemMapper.deleteSalaryItemBySalaryItemId(salaryItem.getSalaryItemId());
    }

    /**
     * 物理批量删除工资项
     *
     * @param salaryItemDtos 需要删除的工资项主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteSalaryItemBySalaryItemIds(List<SalaryItemDTO> salaryItemDtos) {
        List<Long> stringList = new ArrayList<>();
        for (SalaryItemDTO salaryItemDTO : salaryItemDtos) {
            stringList.add(salaryItemDTO.getSalaryItemId());
        }
        return salaryItemMapper.deleteSalaryItemBySalaryItemIds(stringList);
    }

    /**
     * 批量新增工资项信息
     *
     * @param salaryItemDtos 工资项对象
     */
    @Override
    @Transactional
    public int insertSalaryItems(List<SalaryItemDTO> salaryItemDtos) {
        List<SalaryItem> salaryItemList = new ArrayList<>();

        for (SalaryItemDTO salaryItemDTO : salaryItemDtos) {
            SalaryItem salaryItem = new SalaryItem();
            BeanUtils.copyProperties(salaryItemDTO, salaryItem);
            salaryItem.setCreateBy(SecurityUtils.getUserId());
            salaryItem.setCreateTime(DateUtils.getNowDate());
            salaryItem.setUpdateTime(DateUtils.getNowDate());
            salaryItem.setUpdateBy(SecurityUtils.getUserId());
            salaryItem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            salaryItemList.add(salaryItem);
        }
        return salaryItemMapper.batchSalaryItem(salaryItemList);
    }

    /**
     * 批量修改工资项信息
     *
     * @param salaryItemDtos 工资项对象
     */
    @Override
    @Transactional
    public int updateSalaryItems(List<SalaryItemDTO> salaryItemDtos) {
        List<SalaryItem> salaryItemList = new ArrayList<>();
        for (SalaryItemDTO salaryItemDTO : salaryItemDtos) {
            SalaryItem salaryItem = new SalaryItem();
            BeanUtils.copyProperties(salaryItemDTO, salaryItem);
            salaryItem.setUpdateTime(DateUtils.getNowDate());
            salaryItem.setUpdateBy(SecurityUtils.getUserId());
            salaryItemList.add(salaryItem);
        }
        return salaryItemMapper.updateSalaryItems(salaryItemList);
    }

    @Override
    public void handleResult(List<SalaryItemDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(SalaryItemDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
                salarySetName(entity);
            });
        }
    }
}

