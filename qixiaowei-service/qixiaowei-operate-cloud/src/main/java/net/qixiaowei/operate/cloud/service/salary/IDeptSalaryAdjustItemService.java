package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;

import java.util.List;


/**
 * DeptSalaryAdjustItemService接口
 *
 * @author Graves
 * @since 2022-12-11
 */
public interface IDeptSalaryAdjustItemService {
    /**
     * 查询部门调薪项表
     *
     * @param deptSalaryAdjustItemId 部门调薪项表主键
     * @return 部门调薪项表
     */
    DeptSalaryAdjustItemDTO selectDeptSalaryAdjustItemByDeptSalaryAdjustItemId(Long deptSalaryAdjustItemId);

    /**
     * 查询部门调薪项表列表
     *
     * @param deptSalaryAdjustItemDTO 部门调薪项表
     * @return 部门调薪项表集合
     */
    List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemList(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO);

    /**
     * 新增部门调薪项表
     *
     * @param deptSalaryAdjustItemDTO 部门调薪项表
     * @return 结果
     */
    DeptSalaryAdjustItemDTO insertDeptSalaryAdjustItem(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO);

    /**
     * 修改部门调薪项表
     *
     * @param deptSalaryAdjustItemDTO 部门调薪项表
     * @return 结果
     */
    int updateDeptSalaryAdjustItem(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO);

    /**
     * 批量修改部门调薪项表
     *
     * @param deptSalaryAdjustItemDtos 部门调薪项表
     * @return 结果
     */
    int updateDeptSalaryAdjustItems(List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDtos);

    /**
     * 批量新增部门调薪项表
     *
     * @param deptSalaryAdjustItemDtos 部门调薪项表
     * @return 结果
     */
    int insertDeptSalaryAdjustItems(List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemDtos);

    /**
     * 逻辑批量删除部门调薪项表
     *
     * @param deptSalaryAdjustItemIds 需要删除的部门调薪项表集合
     * @return 结果
     */
    int logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(List<Long> deptSalaryAdjustItemIds);

    /**
     * 逻辑删除部门调薪项表信息
     *
     * @param deptSalaryAdjustItemDTO
     * @return 结果
     */
    int logicDeleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO);

    /**
     * 批量删除部门调薪项表
     *
     * @param DeptSalaryAdjustItemDtos
     * @return 结果
     */
    int deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemIds(List<DeptSalaryAdjustItemDTO> DeptSalaryAdjustItemDtos);

    /**
     * 逻辑删除部门调薪项表信息
     *
     * @param deptSalaryAdjustItemDTO
     * @return 结果
     */
    int deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(DeptSalaryAdjustItemDTO deptSalaryAdjustItemDTO);


    /**
     * 删除部门调薪项表信息
     *
     * @param deptSalaryAdjustItemId 部门调薪项表主键
     * @return 结果
     */
    int deleteDeptSalaryAdjustItemByDeptSalaryAdjustItemId(Long deptSalaryAdjustItemId);

    /**
     * 通过计划ID查找调薪项
     *
     * @param deptSalaryAdjustPlanId 计划ID
     * @return List
     */
    List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemByPlanId(Long deptSalaryAdjustPlanId);

    /**
     * 通过计划ID集合查找调薪项
     *
     * @param deptSalaryAdjustPlanIds 计划ID集合
     * @return List
     */
    List<DeptSalaryAdjustItemDTO> selectDeptSalaryAdjustItemByPlanIds(Long deptSalaryAdjustPlanIds);

}
