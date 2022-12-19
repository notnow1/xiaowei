package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustSnapDTO;

import java.util.List;


/**
 * EmpSalaryAdjustSnapService接口
 *
 * @author Graves
 * @since 2022-12-14
 */
public interface IEmpSalaryAdjustSnapService {
    /**
     * 查询个人调薪快照表
     *
     * @param empSalaryAdjustSnapId 个人调薪快照表主键
     * @return 个人调薪快照表
     */
    EmpSalaryAdjustSnapDTO selectEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(Long empSalaryAdjustSnapId);

    /**
     * 查询个人调薪快照表列表
     *
     * @param empSalaryAdjustSnapDTO 个人调薪快照表
     * @return 个人调薪快照表集合
     */
    List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapList(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO);

    /**
     * 新增个人调薪快照表
     *
     * @param empSalaryAdjustSnapDTO 个人调薪快照表
     * @return 结果
     */
    EmpSalaryAdjustSnapDTO insertEmpSalaryAdjustSnap(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO);

    /**
     * 修改个人调薪快照表
     *
     * @param empSalaryAdjustSnapDTO 个人调薪快照表
     * @return 结果
     */
    int updateEmpSalaryAdjustSnap(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO);

    /**
     * 批量修改个人调薪快照表
     *
     * @param EmpSalaryAdjustSnapDtoS 个人调薪快照表
     * @return 结果
     */
    int updateEmpSalaryAdjustSnaps(List<EmpSalaryAdjustSnapDTO> EmpSalaryAdjustSnapDtoS);

    /**
     * 批量新增个人调薪快照表
     *
     * @param EmpSalaryAdjustSnapDtoS 个人调薪快照表
     * @return 结果
     */
    int insertEmpSalaryAdjustSnaps(List<EmpSalaryAdjustSnapDTO> EmpSalaryAdjustSnapDtoS);

    /**
     * 逻辑批量删除个人调薪快照表
     *
     * @param empSalaryAdjustSnapIds 需要删除的个人调薪快照表集合
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(List<Long> empSalaryAdjustSnapIds);

    /**
     * 逻辑删除个人调薪快照表信息
     *
     * @param empSalaryAdjustSnapDTO
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO);

    /**
     * 批量删除个人调薪快照表
     *
     * @param EmpSalaryAdjustSnapDtoS
     * @return 结果
     */
    int deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(List<EmpSalaryAdjustSnapDTO> EmpSalaryAdjustSnapDtoS);

    /**
     * 逻辑删除个人调薪快照表信息
     *
     * @param empSalaryAdjustSnapDTO
     * @return 结果
     */
    int deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO);


    /**
     * 删除个人调薪快照表信息
     *
     * @param empSalaryAdjustSnapId 个人调薪快照表主键
     * @return 结果
     */
    int deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(Long empSalaryAdjustSnapId);

    /**
     * 根据计划ID获取调薪快照表
     *
     * @param empSalaryAdjustPlanId 计划ID
     * @return List
     */
    List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanId(Long empSalaryAdjustPlanId);

    /**
     * 根据计划ID集合查询个人调薪快照表
     *
     * @param empSalaryAdjustPlanIds 计划ID集合查询
     * @return List
     */
    List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanIds(List<Long> empSalaryAdjustPlanIds);
}
