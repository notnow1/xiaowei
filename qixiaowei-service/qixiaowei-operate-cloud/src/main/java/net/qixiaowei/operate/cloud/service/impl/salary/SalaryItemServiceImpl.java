package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.SalaryItem;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
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
     *
     * @param salaryItemDTO 工资项
     * @return 工资项
     */
    @Override
    public List<SalaryItemDTO> selectSalaryItemList(SalaryItemDTO salaryItemDTO) {
        SalaryItem salaryItem = new SalaryItem();
        BeanUtils.copyProperties(salaryItemDTO, salaryItem);
        return salaryItemMapper.selectSalaryItemList(salaryItem);
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
        List<Long> exist = salaryItemMapper.isExist(salaryItemIds);
        if (StringUtils.isEmpty(exist)) {
            throw new ServiceException("该工资条配置已不存在");
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
        if (StringUtils.isNull(salaryId)){
            throw new ServiceException("工资条id不可以为空");
        }
        SalaryItemDTO salaryItemDTO = salaryItemMapper.selectSalaryItemBySalaryItemId(salaryId);
        if (StringUtils.isNull(salaryItemDTO)){
            throw new ServiceException("当前的工资条已经不存在");
        }
        return salaryItemDTO;
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
        List<Long> stringList = new ArrayList();
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
        List<SalaryItem> salaryItemList = new ArrayList();

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
        List<SalaryItem> salaryItemList = new ArrayList();
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

