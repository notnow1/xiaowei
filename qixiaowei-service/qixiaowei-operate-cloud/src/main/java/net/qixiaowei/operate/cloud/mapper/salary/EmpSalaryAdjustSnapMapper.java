package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.salary.EmpSalaryAdjustSnap;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustSnapDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * EmpSalaryAdjustSnapMapper接口
 *
 * @author Graves
 * @since 2022-12-15
 */
public interface EmpSalaryAdjustSnapMapper {
    /**
     * 查询个人调薪快照表
     *
     * @param empSalaryAdjustSnapId 个人调薪快照表主键
     * @return 个人调薪快照表
     */
    EmpSalaryAdjustSnapDTO selectEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(@Param("empSalaryAdjustSnapId") Long empSalaryAdjustSnapId);


    /**
     * 批量查询个人调薪快照表
     *
     * @param empSalaryAdjustSnapIds 个人调薪快照表主键集合
     * @return 个人调薪快照表
     */
    List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(@Param("empSalaryAdjustSnapIds") List<Long> empSalaryAdjustSnapIds);

    /**
     * 查询个人调薪快照表列表
     *
     * @param empSalaryAdjustSnap 个人调薪快照表
     * @return 个人调薪快照表集合
     */
    List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapList(@Param("empSalaryAdjustSnap") EmpSalaryAdjustSnap empSalaryAdjustSnap);

    /**
     * 新增个人调薪快照表
     *
     * @param empSalaryAdjustSnap 个人调薪快照表
     * @return 结果
     */
    int insertEmpSalaryAdjustSnap(@Param("empSalaryAdjustSnap") EmpSalaryAdjustSnap empSalaryAdjustSnap);

    /**
     * 修改个人调薪快照表
     *
     * @param empSalaryAdjustSnap 个人调薪快照表
     * @return 结果
     */
    int updateEmpSalaryAdjustSnap(@Param("empSalaryAdjustSnap") EmpSalaryAdjustSnap empSalaryAdjustSnap);

    /**
     * 批量修改个人调薪快照表
     *
     * @param empSalaryAdjustSnapList 个人调薪快照表
     * @return 结果
     */
    int updateEmpSalaryAdjustSnaps(@Param("empSalaryAdjustSnapList") List<EmpSalaryAdjustSnap> empSalaryAdjustSnapList);

    /**
     * 逻辑删除个人调薪快照表
     *
     * @param empSalaryAdjustSnap
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(@Param("empSalaryAdjustSnap") EmpSalaryAdjustSnap empSalaryAdjustSnap);

    /**
     * 逻辑批量删除个人调薪快照表
     *
     * @param empSalaryAdjustSnapIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(@Param("empSalaryAdjustSnapIds") List<Long> empSalaryAdjustSnapIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除个人调薪快照表
     *
     * @param empSalaryAdjustSnapId 个人调薪快照表主键
     * @return 结果
     */
    int deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapId(@Param("empSalaryAdjustSnapId") Long empSalaryAdjustSnapId);

    /**
     * 物理批量删除个人调薪快照表
     *
     * @param empSalaryAdjustSnapIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(@Param("empSalaryAdjustSnapIds") List<Long> empSalaryAdjustSnapIds);

    /**
     * 批量新增个人调薪快照表
     *
     * @param EmpSalaryAdjustSnaps 个人调薪快照表列表
     * @return 结果
     */
    int batchEmpSalaryAdjustSnap(@Param("empSalaryAdjustSnaps") List<EmpSalaryAdjustSnap> EmpSalaryAdjustSnaps);

    /**
     * 根据计划ID获取调薪快照表
     *
     * @param empSalaryAdjustPlanId 计划ID
     * @return List
     */
    List<EmpSalaryAdjustSnapDTO> selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanId(@Param("empSalaryAdjustPlanId") Long empSalaryAdjustPlanId);
}
