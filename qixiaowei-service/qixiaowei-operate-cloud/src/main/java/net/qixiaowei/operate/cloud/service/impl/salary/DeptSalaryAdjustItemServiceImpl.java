package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptSalaryAdjustItem;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;
import net.qixiaowei.operate.cloud.mapper.salary.DeptSalaryAdjustItemMapper;
import net.qixiaowei.operate.cloud.service.salary.IDeptSalaryAdjustItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * DeptSalaryAdjustItemService业务层处理
 *
 * @author Graves
 * @since 2022-12-11
 */
@Service
public class DeptSalaryAdjustItemServiceImpl implements IDeptSalaryAdjustItemService {
    @Autowired
    private DeptSalaryAdjustItemMapper deptSalaryAdjustItemMapper;

    /**
     * 查询部门调薪项表
     *
     * @param deptSalaryAdjustItemId 部门调薪项表主键
     * @return 部门调薪项表
     */
    @Override
    public DeptSalaryAdjustItemDTO selectDeptSalaryAdjustItemByDeptSalaryAdjustItemId(Long deptSalaryAdjustItemId) {
        return deptSalaryAdjustItemMapper.selectDeptSalaryAdjustItemByDeptSalaryAdjustItemId(deptSalaryAdjustItemId);
    }

    /**
     * 查询部门调薪项表列表
     *
     * @param deptSalaryAdjustItemDTO 部门调薪项表
     * @return 部门调薪项表
     */
    @Override
    public List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemList(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO) {
        DeptSalaryAdjustItem deptSalaryAdjustItem = new DeptSalaryAdjustItem();
        BeanUtils.copyProperties(deptSalaryAdjustItemDTO, deptSalaryAdjustItem);
        return deptSalaryAdjustItemMapper.selectDeptSalaryAdjustItemList(deptSalaryAdjustItem);
    }

    /**
     * 新增部门调薪项表
     *
     * @param deptSalaryAdjustItemDTO 部门调薪项表
     * @return 结果
     */
    @Override
    public DeptSalaryAdjustItemDTO insertDeptSalaryAdjustItem(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO) {
        DeptSalaryAdjustItem deptSalaryAdjustItem = new DeptSalaryAdjustItem();
        BeanUtils.copyProperties(deptSalaryAdjustItemDTO, deptSalaryAdjustItem);
        deptSalaryAdjustItem.setCreateBy(SecurityUtils.getUserId());
        deptSalaryAdjustItem.setCreateTime(DateUtils.getNowDate());
        deptSalaryAdjustItem.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustItem.setUpdateBy(SecurityUtils.getUserId());
        deptSalaryAdjustItem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        deptSalaryAdjustItemMapper.insertDeptSalaryAdjustItem(deptSalaryAdjustItem);
        deptSalaryAdjustItemDTO.setDeptSalaryAdjustItemId(deptSalaryAdjustItem.getDeptSalaryAdjustItemId());
        return deptSalaryAdjustItemDTO;
    }

    /**
     * 修改部门调薪项表
     *
     * @param deptSalaryAdjustItemDTO 部门调薪项表
     * @return 结果
     */
    @Override
    public int updateDeptSalaryAdjustItem(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO) {
        DeptSalaryAdjustItem deptSalaryAdjustItem = new DeptSalaryAdjustItem();
        BeanUtils.copyProperties(deptSalaryAdjustItemDTO, deptSalaryAdjustItem);
        deptSalaryAdjustItem.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustItem.setUpdateBy(SecurityUtils.getUserId());
        return deptSalaryAdjustItemMapper.updateDeptSalaryAdjustItem(deptSalaryAdjustItem);
    }

    /**
     * 逻辑批量删除部门调薪项表
     *
     * @param deptSalaryAdjustItemIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(List<Long> deptSalaryAdjustItemIds) {
        return deptSalaryAdjustItemMapper.logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(deptSalaryAdjustItemIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除部门调薪项表信息
     *
     * @param deptSalaryAdjustItemId 部门调薪项表主键
     * @return 结果
     */
    @Override
    public int deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(Long deptSalaryAdjustItemId) {
        return deptSalaryAdjustItemMapper.deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(deptSalaryAdjustItemId);
    }

    /**
     * 通过计划ID查找调薪项
     *
     * @param deptSalaryAdjustPlanId 计划ID
     * @return
     */
    @Override
    public List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemByPlanId(Long deptSalaryAdjustPlanId) {
        return deptSalaryAdjustItemMapper.selectDeptSalaryAdjustItemBySalaryAdjustPlanId(deptSalaryAdjustPlanId);
    }

    /**
     * 通过计划ID集合查找调薪项
     *
     * @param deptSalaryAdjustPlanIds 计划ID集合
     * @return
     */
    @Override
    public List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemByPlanIds(List<Long> deptSalaryAdjustPlanIds) {
        return deptSalaryAdjustItemMapper.selectDeptSalaryAdjustItemBySalaryAdjustPlanIds(deptSalaryAdjustPlanIds);
    }

    /**
     * 逻辑删除部门调薪项表信息
     *
     * @param deptSalaryAdjustItemDTO 部门调薪项表
     * @return 结果
     */
    @Override
    public int logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO) {
        DeptSalaryAdjustItem deptSalaryAdjustItem = new DeptSalaryAdjustItem();
        deptSalaryAdjustItem.setDeptSalaryAdjustItemId(deptSalaryAdjustItemDTO.getDeptSalaryAdjustItemId());
        deptSalaryAdjustItem.setUpdateTime(DateUtils.getNowDate());
        deptSalaryAdjustItem.setUpdateBy(SecurityUtils.getUserId());
        return deptSalaryAdjustItemMapper.logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(deptSalaryAdjustItem);
    }

    /**
     * 物理删除部门调薪项表信息
     *
     * @param deptSalaryAdjustItemDTO 部门调薪项表
     * @return 结果
     */

    @Override
    public int deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO) {
        DeptSalaryAdjustItem deptSalaryAdjustItem = new DeptSalaryAdjustItem();
        BeanUtils.copyProperties(deptSalaryAdjustItemDTO, deptSalaryAdjustItem);
        return deptSalaryAdjustItemMapper.deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(deptSalaryAdjustItem.getDeptSalaryAdjustItemId());
    }

    /**
     * 物理批量删除部门调薪项表
     *
     * @param deptSalaryAdjustItemDtos 需要删除的部门调薪项表主键
     * @return 结果
     */

    @Override
    public int deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDtos) {
        List<Long> stringList = new ArrayList();
        for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : deptSalaryAdjustItemDtos) {
            stringList.add(deptSalaryAdjustItemDTO.getDeptSalaryAdjustItemId());
        }
        return deptSalaryAdjustItemMapper.deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(stringList);
    }

    /**
     * 批量新增部门调薪项表信息
     *
     * @param deptSalaryAdjustItemDtos 部门调薪项表对象
     */

    @Override
    public int insertDeptSalaryAdjustItems(List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDtos) {
        List<DeptSalaryAdjustItem> deptSalaryAdjustItemList = new ArrayList();

        for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : deptSalaryAdjustItemDtos) {
            DeptSalaryAdjustItem deptSalaryAdjustItem = new DeptSalaryAdjustItem();
            BeanUtils.copyProperties(deptSalaryAdjustItemDTO, deptSalaryAdjustItem);
            deptSalaryAdjustItem.setCreateBy(SecurityUtils.getUserId());
            deptSalaryAdjustItem.setCreateTime(DateUtils.getNowDate());
            deptSalaryAdjustItem.setUpdateTime(DateUtils.getNowDate());
            deptSalaryAdjustItem.setUpdateBy(SecurityUtils.getUserId());
            deptSalaryAdjustItem.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            deptSalaryAdjustItemList.add(deptSalaryAdjustItem);
        }
        return deptSalaryAdjustItemMapper.batchDeptSalaryAdjustItem(deptSalaryAdjustItemList);
    }

    /**
     * 批量修改部门调薪项表信息
     *
     * @param deptSalaryAdjustItemDtos 部门调薪项表对象
     */

    @Override
    public int updateDeptSalaryAdjustItems(List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDtos) {
        List<DeptSalaryAdjustItem> deptSalaryAdjustItemList = new ArrayList();

        for (DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO : deptSalaryAdjustItemDtos) {
            DeptSalaryAdjustItem deptSalaryAdjustItem = new DeptSalaryAdjustItem();
            BeanUtils.copyProperties(deptSalaryAdjustItemDTO, deptSalaryAdjustItem);
            deptSalaryAdjustItem.setCreateBy(SecurityUtils.getUserId());
            deptSalaryAdjustItem.setCreateTime(DateUtils.getNowDate());
            deptSalaryAdjustItem.setUpdateTime(DateUtils.getNowDate());
            deptSalaryAdjustItem.setUpdateBy(SecurityUtils.getUserId());
            deptSalaryAdjustItemList.add(deptSalaryAdjustItem);
        }
        return deptSalaryAdjustItemMapper.updateDeptSalaryAdjustItems(deptSalaryAdjustItemList);
    }
}

