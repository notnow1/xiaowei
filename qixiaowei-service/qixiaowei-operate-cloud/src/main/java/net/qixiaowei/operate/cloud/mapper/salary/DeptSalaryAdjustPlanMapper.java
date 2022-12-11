package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptSalaryAdjustPlan;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustPlanDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DeptSalaryAdjustPlanMapper接口
* @author Graves
* @since 2022-12-11
*/
public interface DeptSalaryAdjustPlanMapper{
    /**
    * 查询部门调薪计划表
    *
    * @param deptSalaryAdjustPlanId 部门调薪计划表主键
    * @return 部门调薪计划表
    */
    DeptSalaryAdjustPlanDTO selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(@Param("deptSalaryAdjustPlanId")Long deptSalaryAdjustPlanId);


    /**
    * 批量查询部门调薪计划表
    *
    * @param deptSalaryAdjustPlanIds 部门调薪计划表主键集合
    * @return 部门调薪计划表
    */
    List<DeptSalaryAdjustPlanDTO> selectDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(@Param("deptSalaryAdjustPlanIds") List<Long> deptSalaryAdjustPlanIds);

    /**
    * 查询部门调薪计划表列表
    *
    * @param deptSalaryAdjustPlan 部门调薪计划表
    * @return 部门调薪计划表集合
    */
    List<DeptSalaryAdjustPlanDTO> selectDeptSalaryAdjustPlanList(@Param("deptSalaryAdjustPlan")DeptSalaryAdjustPlan deptSalaryAdjustPlan);

    /**
    * 新增部门调薪计划表
    *
    * @param deptSalaryAdjustPlan 部门调薪计划表
    * @return 结果
    */
    int insertDeptSalaryAdjustPlan(@Param("deptSalaryAdjustPlan")DeptSalaryAdjustPlan deptSalaryAdjustPlan);

    /**
    * 修改部门调薪计划表
    *
    * @param deptSalaryAdjustPlan 部门调薪计划表
    * @return 结果
    */
    int updateDeptSalaryAdjustPlan(@Param("deptSalaryAdjustPlan")DeptSalaryAdjustPlan deptSalaryAdjustPlan);

    /**
    * 批量修改部门调薪计划表
    *
    * @param deptSalaryAdjustPlanList 部门调薪计划表
    * @return 结果
    */
    int updateDeptSalaryAdjustPlans(@Param("deptSalaryAdjustPlanList")List<DeptSalaryAdjustPlan> deptSalaryAdjustPlanList);
    /**
    * 逻辑删除部门调薪计划表
    *
    * @param deptSalaryAdjustPlan
    * @return 结果
    */
    int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(@Param("deptSalaryAdjustPlan")DeptSalaryAdjustPlan deptSalaryAdjustPlan);

    /**
    * 逻辑批量删除部门调薪计划表
    *
    * @param deptSalaryAdjustPlanIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(@Param("deptSalaryAdjustPlanIds")List<Long> deptSalaryAdjustPlanIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门调薪计划表
    *
    * @param deptSalaryAdjustPlanId 部门调薪计划表主键
    * @return 结果
    */
    int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(@Param("deptSalaryAdjustPlanId")Long deptSalaryAdjustPlanId);

    /**
    * 物理批量删除部门调薪计划表
    *
    * @param deptSalaryAdjustPlanIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(@Param("deptSalaryAdjustPlanIds")List<Long> deptSalaryAdjustPlanIds);

    /**
    * 批量新增部门调薪计划表
    *
    * @param DeptSalaryAdjustPlans 部门调薪计划表列表
    * @return 结果
    */
    int batchDeptSalaryAdjustPlan(@Param("deptSalaryAdjustPlans")List<DeptSalaryAdjustPlan> DeptSalaryAdjustPlans);
}
