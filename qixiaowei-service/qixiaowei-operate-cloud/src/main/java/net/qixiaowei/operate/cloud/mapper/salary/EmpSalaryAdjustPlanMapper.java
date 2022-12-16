package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.salary.EmpSalaryAdjustPlan;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * EmpSalaryAdjustPlanMapper接口
 *
 * @author Graves
 * @since 2022-12-15
 */
public interface EmpSalaryAdjustPlanMapper {
    /**
     * 查询个人调薪计划表
     *
     * @param empSalaryAdjustPlanId 个人调薪计划表主键
     * @return 个人调薪计划表
     */
    EmpSalaryAdjustPlanDTO selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(@Param("empSalaryAdjustPlanId") Long empSalaryAdjustPlanId);


    /**
     * 批量查询个人调薪计划表
     *
     * @param empSalaryAdjustPlanIds 个人调薪计划表主键集合
     * @return 个人调薪计划表
     */
    List<EmpSalaryAdjustPlanDTO> selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(@Param("empSalaryAdjustPlanIds") List<Long> empSalaryAdjustPlanIds);

    /**
     * 查询个人调薪计划表列表
     *
     * @param empSalaryAdjustPlan 个人调薪计划表
     * @return 个人调薪计划表集合
     */
    List<EmpSalaryAdjustPlanDTO> selectEmpSalaryAdjustPlanList(@Param("empSalaryAdjustPlan") EmpSalaryAdjustPlan empSalaryAdjustPlan);

    /**
     * 新增个人调薪计划表
     *
     * @param empSalaryAdjustPlan 个人调薪计划表
     * @return 结果
     */
    int insertEmpSalaryAdjustPlan(@Param("empSalaryAdjustPlan") EmpSalaryAdjustPlan empSalaryAdjustPlan);

    /**
     * 修改个人调薪计划表
     *
     * @param empSalaryAdjustPlan 个人调薪计划表
     * @return 结果
     */
    int updateEmpSalaryAdjustPlan(@Param("empSalaryAdjustPlan") EmpSalaryAdjustPlan empSalaryAdjustPlan);

    /**
     * 批量修改个人调薪计划表
     *
     * @param empSalaryAdjustPlanList 个人调薪计划表
     * @return 结果
     */
    int updateEmpSalaryAdjustPlans(@Param("empSalaryAdjustPlanList") List<EmpSalaryAdjustPlan> empSalaryAdjustPlanList);

    /**
     * 逻辑删除个人调薪计划表
     *
     * @param empSalaryAdjustPlan
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(@Param("empSalaryAdjustPlan") EmpSalaryAdjustPlan empSalaryAdjustPlan);

    /**
     * 逻辑批量删除个人调薪计划表
     *
     * @param empSalaryAdjustPlanIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(@Param("empSalaryAdjustPlanIds") List<Long> empSalaryAdjustPlanIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除个人调薪计划表
     *
     * @param empSalaryAdjustPlanId 个人调薪计划表主键
     * @return 结果
     */
    int deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(@Param("empSalaryAdjustPlanId") Long empSalaryAdjustPlanId);

    /**
     * 物理批量删除个人调薪计划表
     *
     * @param empSalaryAdjustPlanIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(@Param("empSalaryAdjustPlanIds") List<Long> empSalaryAdjustPlanIds);

    /**
     * 批量新增个人调薪计划表
     *
     * @param EmpSalaryAdjustPlans 个人调薪计划表列表
     * @return 结果
     */
    int batchEmpSalaryAdjustPlan(@Param("empSalaryAdjustPlans") List<EmpSalaryAdjustPlan> EmpSalaryAdjustPlans);

    /**
     * 根据员工ID获取个人调薪计划表
     *
     * @param employeeId 员工ID
     * @return
     */
    List<EmpSalaryAdjustPlanDTO> selectByEmployeeId(@Param("employeeId") Long employeeId);
}
