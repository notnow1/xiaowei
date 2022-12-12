package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.salary.DeptSalaryAdjustItem;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * DeptSalaryAdjustItemMapper接口
 *
 * @author Graves
 * @since 2022-12-11
 */
public interface DeptSalaryAdjustItemMapper {
    /**
     * 查询部门调薪项表
     *
     * @param deptSalaryAdjustItemId 部门调薪项表主键
     * @return 部门调薪项表
     */
    DeptSalaryAdjustItemDTO selectDeptSalaryAdjustItemByDeptSalaryAdjustItemId(@Param("deptSalaryAdjustItemId") Long deptSalaryAdjustItemId);


    /**
     * 批量查询部门调薪项表
     *
     * @param deptSalaryAdjustItemIds 部门调薪项表主键集合
     * @return 部门调薪项表
     */
    List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(@Param("deptSalaryAdjustItemIds") List<Long> deptSalaryAdjustItemIds);

    /**
     * 查询部门调薪项表列表
     *
     * @param deptSalaryAdjustItem 部门调薪项表
     * @return 部门调薪项表集合
     */
    List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemList(@Param("deptSalaryAdjustItem") DeptSalaryAdjustItem deptSalaryAdjustItem);

    /**
     * 新增部门调薪项表
     *
     * @param deptSalaryAdjustItem 部门调薪项表
     * @return 结果
     */
    int insertDeptSalaryAdjustItem(@Param("deptSalaryAdjustItem") DeptSalaryAdjustItem deptSalaryAdjustItem);

    /**
     * 修改部门调薪项表
     *
     * @param deptSalaryAdjustItem 部门调薪项表
     * @return 结果
     */
    int updateDeptSalaryAdjustItem(@Param("deptSalaryAdjustItem") DeptSalaryAdjustItem deptSalaryAdjustItem);

    /**
     * 批量修改部门调薪项表
     *
     * @param deptSalaryAdjustItemList 部门调薪项表
     * @return 结果
     */
    int updateDeptSalaryAdjustItems(@Param("deptSalaryAdjustItemList") List<DeptSalaryAdjustItem> deptSalaryAdjustItemList);

    /**
     * 逻辑删除部门调薪项表
     *
     * @param deptSalaryAdjustItem
     * @return 结果
     */
    int logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(@Param("deptSalaryAdjustItem") DeptSalaryAdjustItem deptSalaryAdjustItem);

    /**
     * 逻辑批量删除部门调薪项表
     *
     * @param deptSalaryAdjustItemIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(@Param("deptSalaryAdjustItemIds") List<Long> deptSalaryAdjustItemIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除部门调薪项表
     *
     * @param deptSalaryAdjustItemId 部门调薪项表主键
     * @return 结果
     */
    int deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(@Param("deptSalaryAdjustItemId") Long deptSalaryAdjustItemId);

    /**
     * 物理批量删除部门调薪项表
     *
     * @param deptSalaryAdjustItemIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(@Param("deptSalaryAdjustItemIds") List<Long> deptSalaryAdjustItemIds);

    /**
     * 批量新增部门调薪项表
     *
     * @param DeptSalaryAdjustItems 部门调薪项表列表
     * @return 结果
     */
    int batchDeptSalaryAdjustItem(@Param("deptSalaryAdjustItems") List<DeptSalaryAdjustItem> DeptSalaryAdjustItems);

    /**
     * 通过计划ID查找调薪项
     *
     * @param deptSalaryAdjustPlanId 计划ID
     * @return
     */
    List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemBySalaryAdjustPlanId(Long deptSalaryAdjustPlanId);

    /**
     * 通过计划ID集合查找调薪项
     *
     * @param deptSalaryAdjustPlanIds 计划ID集合
     * @return
     */
    List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemBySalaryAdjustPlanIds(Long deptSalaryAdjustPlanIds);
}
