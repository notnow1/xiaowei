package net.qixiaowei.operate.cloud.service.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.excel.salary.DeptSalaryAdjustPlanExcel;


/**
 * DeptSalaryAdjustPlanService接口
 *
 * @author Graves
 * @since 2022-12-11
 */
public interface IDeptSalaryAdjustPlanService {
    /**
     * 查询部门调薪计划表
     *
     * @param deptSalaryAdjustPlanId 部门调薪计划表主键
     * @return 部门调薪计划表
     */
    DeptSalaryAdjustPlanDTO selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(Long deptSalaryAdjustPlanId);

    /**
     * 查询部门调薪计划表列表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 部门调薪计划表集合
     */
    List<DeptSalaryAdjustPlanDTO> selectDeptSalaryAdjustPlanList(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 新增部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    DeptSalaryAdjustPlanDTO insertDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 修改部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    int updateDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 批量修改部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDtos 部门调薪计划表
     * @return 结果
     */
    int updateDeptSalaryAdjustPlans(List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDtos);

    /**
     * 批量新增部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDtos 部门调薪计划表
     * @return 结果
     */
    int insertDeptSalaryAdjustPlans(List<DeptSalaryAdjustPlanDTO> deptSalaryAdjustPlanDtos);

    /**
     * 逻辑批量删除部门调薪计划表
     *
     * @param deptSalaryAdjustPlanIds 需要删除的部门调薪计划表集合
     * @return 结果
     */
    int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(List<Long> deptSalaryAdjustPlanIds);

    /**
     * 逻辑删除部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanDTO
     * @return 结果
     */
    int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 批量删除部门调薪计划表
     *
     * @param DeptSalaryAdjustPlanDtos
     * @return 结果
     */
    int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(List<DeptSalaryAdjustPlanDTO> DeptSalaryAdjustPlanDtos);

    /**
     * 逻辑删除部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanDTO
     * @return 结果
     */
    int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);


    /**
     * 删除部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanId 部门调薪计划表主键
     * @return 结果
     */
    int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(Long deptSalaryAdjustPlanId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importDeptSalaryAdjustPlan(List<DeptSalaryAdjustPlanExcel> list);

    /**
     * 导出Excel
     *
     * @param deptSalaryAdjustPlanDTO
     * @return
     */
    List<DeptSalaryAdjustPlanExcel> exportDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 编辑部门调薪
     *
     * @param deptSalaryAdjustPlanDTO 调薪计划
     * @return
     */
    DeptSalaryAdjustPlanDTO editDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 获取上年发薪包
     *
     * @param departmentId 部门ID
     * @return String
     */
    String getLastSalary(Long departmentId);
}
