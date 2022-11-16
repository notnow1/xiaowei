package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.salary.ThirdLevelSalaryCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryItem;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.excel.salary.SalaryItemExcel;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryItemMapper;
import net.qixiaowei.operate.cloud.service.salary.ISalaryItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;


/**
 * SalaryItemService业务层处理
 *
 * @author Graves
 * @since 2022-10-05
 */
@Service
public class SalaryItemServiceImpl implements ISalaryItemService {
    @Autowired
    private SalaryItemMapper salaryItemMapper;

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
     * 查询工资项列表
     * todo 正在做
     *
     * @param salaryItemDTO 工资项
     * @return 工资项
     */
    @Override
    public List<SalaryItemDTO> selectSalaryItemList(SalaryItemDTO salaryItemDTO) {
        SalaryItem salaryItem = new SalaryItem();
        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemList(salaryItem);
        for (SalaryItemDTO itemDTO : salaryItemDTOS) {
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
        return salaryItemDTOS;
    }

    /**
     * 新增工资项
     *
     * @param salaryItemDTO 工资项
     * @return 结果
     */
    @Transactional
    @Override
    public int insertSalaryItem(SalaryItemDTO salaryItemDTO) {
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
        SalaryItem salaryItem = new SalaryItem();
        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
        salaryItem.setCreateBy(SecurityUtils.getUserId());
        salaryItem.setCreateTime(DateUtils.getNowDate());
        salaryItem.setUpdateTime(DateUtils.getNowDate());
        salaryItem.setUpdateBy(SecurityUtils.getUserId());
        salaryItem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return salaryItemMapper.insertSalaryItem(salaryItem);
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
        //todo 引用校验
        if (isQuote(salaryItemIds)) {
            throw new ServiceException("存在被引用的工资条配置");
        }
        return salaryItemMapper.logicDeleteSalaryItemBySalaryItemIds(salaryItemIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    private boolean isQuote(List<Long> salaryItemIds) {
        return false;
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
     * @param salaryId
     * @return
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
     * @param salaryItemDTO
     * @return
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
            BeanUtils.copyProperties(dto, salaryItemExcel);
            switch (dto.getFirstLevelItem()) {
                case 1:
                    salaryItemExcel.setFirstLevelItem("总工资包");
                    break;
                case 2:
                    salaryItemExcel.setFirstLevelItem("总奖金包");
                    break;
                case 3:
                    salaryItemExcel.setFirstLevelItem("总扣减项");
                    break;
            }
            switch (dto.getSecondLevelItem()) {
                case 1:
                    salaryItemExcel.setSecondLevelItem("工资");
                    break;
                case 2:
                    salaryItemExcel.setSecondLevelItem("津贴");
                    break;
                case 3:
                    salaryItemExcel.setSecondLevelItem("福利");
                    break;
                case 4:
                    salaryItemExcel.setSecondLevelItem("奖金");
                    break;
                case 5:
                    salaryItemExcel.setSecondLevelItem("代扣代缴");
                    break;
                case 6:
                    salaryItemExcel.setSecondLevelItem("其他扣款");
                    break;
            }
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
     * 导入工资条
     *
     * @param list
     * @return
     */
    @Override
    public void importSalaryItem(List<SalaryItemExcel> list) {

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
        SalaryItemDTO salaryItemBySalaryItemId = salaryItemMapper.selectSalaryItemBySalaryItemId(salaryItemId);

        if (StringUtils.isNull(salaryItemId)) {
            throw new ServiceException("工资条ID不能为空");
        }
        if (ThirdLevelSalaryCode.containThirdItems(salaryItemBySalaryItemId.getThirdLevelItem())) {
            throw new ServiceException(salaryItemBySalaryItemId.getThirdLevelItem() + "为预置配置，不可以删除");
        }
        SalaryItem salaryItem = new SalaryItem();
        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
        return salaryItemMapper.logicDeleteSalaryItemBySalaryItemId(salaryItem, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
    @Transactional
    public int updateSalaryItems(List<SalaryItemDTO> salaryItemDtos) {
        List<SalaryItem> salaryItemList = new ArrayList<>();
        for (SalaryItemDTO salaryItemDTO : salaryItemDtos) {
            SalaryItem salaryItem = new SalaryItem();
            BeanUtils.copyProperties(salaryItemDTO, salaryItem);
            salaryItem.setCreateBy(SecurityUtils.getUserId());
            salaryItem.setCreateTime(DateUtils.getNowDate());
            salaryItem.setUpdateTime(DateUtils.getNowDate());
            salaryItem.setUpdateBy(SecurityUtils.getUserId());
            salaryItemList.add(salaryItem);
        }
        return salaryItemMapper.updateSalaryItems(salaryItemList);
    }
}

