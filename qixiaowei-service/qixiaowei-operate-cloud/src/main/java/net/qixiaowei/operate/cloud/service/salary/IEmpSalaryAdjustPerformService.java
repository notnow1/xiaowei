package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPerformDTO;

import java.util.List;


/**
 * EmpSalaryAdjustPerformService接口
 *
 * @author Graves
 * @since 2022-12-14
 */
public interface IEmpSalaryAdjustPerformService {
    /**
     * 查询个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformId 个人调薪绩效记录表主键
     * @return 个人调薪绩效记录表
     */
    EmpSalaryAdjustPerformDTO selectEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(Long empSalaryAdjustPerformId);

    /**
     * 查询个人调薪绩效记录表列表
     *
     * @param empSalaryAdjustPerformDTO 个人调薪绩效记录表
     * @return 个人调薪绩效记录表集合
     */
    List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformList(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO);

    /**
     * 新增个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformDTO 个人调薪绩效记录表
     * @return 结果
     */
    EmpSalaryAdjustPerformDTO insertEmpSalaryAdjustPerform(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO);

    /**
     * 修改个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformDTO 个人调薪绩效记录表
     * @return 结果
     */
    int updateEmpSalaryAdjustPerform(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO);

    /**
     * 批量修改个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformDtos 个人调薪绩效记录表
     * @return 结果
     */
    int updateEmpSalaryAdjustPerforms(List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDtos);

    /**
     * 批量新增个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformDtos 个人调薪绩效记录表
     * @return 结果
     */
    int insertEmpSalaryAdjustPerforms(List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDtos);

    /**
     * 逻辑批量删除个人调薪绩效记录表
     *
     * @param empSalaryAdjustPerformIds 需要删除的个人调薪绩效记录表集合
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(List<Long> empSalaryAdjustPerformIds);

    /**
     * 逻辑删除个人调薪绩效记录表信息
     *
     * @param empSalaryAdjustPerformDTO
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO);

    /**
     * 批量删除个人调薪绩效记录表
     *
     * @param EmpSalaryAdjustPerformDtos
     * @return 结果
     */
    int deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(List<EmpSalaryAdjustPerformDTO> EmpSalaryAdjustPerformDtos);

    /**
     * 逻辑删除个人调薪绩效记录表信息
     *
     * @param empSalaryAdjustPerformDTO
     * @return 结果
     */
    int deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO);


    /**
     * 删除个人调薪绩效记录表信息
     *
     * @param empSalaryAdjustPerformId 个人调薪绩效记录表主键
     * @return 结果
     */
    int deleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformId(Long empSalaryAdjustPerformId);

    /**
     * 根据计划ID获取近三次绩效结果
     *
     * @return List
     */
    List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformByPlanId(Long empSalaryAdjustPlanId);

    /**
     * 根据计划ID集合查询最近三次绩效结果
     *
     * @param empSalaryAdjustPlanIds 计划ID集合
     * @return List
     */
    List<EmpSalaryAdjustPerformDTO> selectEmpSalaryAdjustPerformByPlanIds(List<Long> empSalaryAdjustPlanIds);
}
