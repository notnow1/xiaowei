package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.salary.EmpSalaryAdjustPerform;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPerformDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * EmpSalaryAdjustPerformMapper接口
 *
 * @author Graves
 * @since 2022-12-14
 */
public interface EmpSalaryAdjustPerformMapper {
    /**
     * 查询个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformId 个人调薪绩效记录表主键
     * @return 个人调薪绩效记录表
     */
    EmpSalaryAdjustPerformDTO selectEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(@Param("empSalaryAdjustPerformId") Long empSalaryAdjustPerformId);


    /**
     * 批量查询个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformIds 个人调薪绩效记录表主键集合
     * @return 个人调薪绩效记录表
     */
    List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(@Param("empSalaryAdjustPerformIds") List<Long> empSalaryAdjustPerformIds);

    /**
     * 查询个人调薪绩效记录表列表
     *
     * @param empSalaryAdjustPerform 个人调薪绩效记录表
     * @return 个人调薪绩效记录表集合
     */
    List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformList(@Param("empSalaryAdjustPerform") EmpSalaryAdjustPerform empSalaryAdjustPerform);

    /**
     * 新增个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerform 个人调薪绩效记录表
     * @return 结果
     */
    int insertEmpSalaryAdjustPerform(@Param("empSalaryAdjustPerform") EmpSalaryAdjustPerform empSalaryAdjustPerform);

    /**
     * 修改个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerform 个人调薪绩效记录表
     * @return 结果
     */
    int updateEmpSalaryAdjustPerform(@Param("empSalaryAdjustPerform") EmpSalaryAdjustPerform empSalaryAdjustPerform);

    /**
     * 批量修改个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformList 个人调薪绩效记录表
     * @return 结果
     */
    int updateEmpSalaryAdjustPerforms(@Param("empSalaryAdjustPerformList") List<EmpSalaryAdjustPerform> empSalaryAdjustPerformList);

    /**
     * 逻辑删除个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerform
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(@Param("empSalaryAdjustPerform") EmpSalaryAdjustPerform empSalaryAdjustPerform);

    /**
     * 逻辑批量删除个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(@Param("empSalaryAdjustPerformIds") List<Long> empSalaryAdjustPerformIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformId 个人调薪绩效记录表主键
     * @return 结果
     */
    int deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(@Param("empSalaryAdjustPerformId") Long empSalaryAdjustPerformId);

    /**
     * 物理批量删除个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(@Param("empSalaryAdjustPerformIds") List<Long> empSalaryAdjustPerformIds);

    /**
     * 批量新增个人调薪绩效记录表
     *
     * @param EmpSalaryAdjustPerforms 个人调薪绩效记录表列表
     * @return 结果
     */
    int batchEmpSalaryAdjustPerform(@Param("empSalaryAdjustPerforms") List<EmpSalaryAdjustPerform> EmpSalaryAdjustPerforms);

    /**
     * 根据计划ID获取近三次绩效结果
     *
     * @param empSalaryAdjustPlanId 个人调薪计划ID
     * @return List
     */
    List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformByPlanId(@Param("empSalaryAdjustPlanId") Long empSalaryAdjustPlanId);

    /**
     * 根据计划ID集合查询最近三次绩效结果
     *
     * @param empSalaryAdjustPlanIds 计划ID集合
     * @return List
     */
    List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformByPlanIds(@Param("empSalaryAdjustPlanIds") List<Long> empSalaryAdjustPlanIds);
}
