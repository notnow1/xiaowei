package net.qixiaowei.operate.cloud.service.salary;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.excel.salary.EmpSalaryAdjustPlanExcel;

import java.util.List;
import java.util.Map;


/**
 * EmpSalaryAdjustPlanService接口
 *
 * @author Graves
 * @since 2022-12-14
 */
public interface IEmpSalaryAdjustPlanService {
    /**
     * 查询个人调薪计划表
     *
     * @param empSalaryAdjustPlanId 个人调薪计划表主键
     * @return 个人调薪计划表
     */
    EmpSalaryAdjustPlanDTO selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(Long empSalaryAdjustPlanId);

    /**
     * 查询个人调薪计划表列表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 个人调薪计划表集合
     */
    List<EmpSalaryAdjustPlanDTO> selectEmpSalaryAdjustPlanList(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO);

    /**
     * 新增个人调薪计划表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 结果
     */
    int insertEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO);

    /**
     * 修改个人调薪计划表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 结果
     */
    int updateEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO);

    /**
     * 批量修改个人调薪计划表
     *
     * @param empSalaryAdjustPlanDtos 个人调薪计划表
     * @return 结果
     */
    int updateEmpSalaryAdjustPlans(List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDtos);

    /**
     * 批量新增个人调薪计划表
     *
     * @param empSalaryAdjustPlanDtos 个人调薪计划表
     * @return 结果
     */
    int insertEmpSalaryAdjustPlans(List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDtos);

    /**
     * 逻辑批量删除个人调薪计划表
     *
     * @param empSalaryAdjustPlanIds 需要删除的个人调薪计划表集合
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(List<Long> empSalaryAdjustPlanIds);

    /**
     * 逻辑删除个人调薪计划表信息
     *
     * @param empSalaryAdjustPlanDTO 个人调薪DTO
     * @return 结果
     */
    int logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO);

    /**
     * 批量删除个人调薪计划表
     *
     * @param EmpSalaryAdjustPlanDtoS 个人调薪DOS
     * @return 结果
     */
    int deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(List<EmpSalaryAdjustPlanDTO> EmpSalaryAdjustPlanDtoS);

    /**
     * 逻辑删除个人调薪计划表信息
     *
     * @param empSalaryAdjustPlanDTO 个人调薪DTO
     * @return 结果
     */
    int deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO);


    /**
     * 删除个人调薪计划表信息
     *
     * @param empSalaryAdjustPlanId 个人调薪计划表主键
     * @return 结果
     */
    int deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(Long empSalaryAdjustPlanId);

    /**
     * 导入Excel
     *
     * @param list excelList
     */
    void importEmpSalaryAdjustPlan(List<EmpSalaryAdjustPlanExcel> list);

    /**
     * 导出Excel
     *
     * @param empSalaryAdjustPlanDTO 个人调薪DTO
     * @return List
     */
    List<EmpSalaryAdjustPlanExcel> exportEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO);

    /**
     * 根据员工获取个人调薪
     *
     * @param employeeId 员工ID
     * @return List
     */
    List<EmpSalaryAdjustPlanDTO> selectByEmployeeId(Long employeeId);

    /**
     * 职级确定薪酬详情
     *
     * @param postId       岗位ID
     * @param officialRank 职级
     * @return String
     */
    String officialRankInfo(Long postId, Integer officialRank);

    /**
     * 根据ID集合查询个人调薪
     *
     * @param employeeIds 员工Id集合
     * @return
     */
    List<EmpSalaryAdjustPlanDTO> selectByEmployeeIds(List<Long> employeeIds);

    /**
     * 根据职级体系ID集合获取个人调薪
     *
     * @param officialRankSystemIds 职级体系ID集合
     * @return List
     */
    List<EmpSalaryAdjustPlanDTO> selectBySystemIds(List<Long> officialRankSystemIds);

    /**
     * 根据部门ID查询个人调薪
     *
     * @param departmentId 部门ID
     * @return R
     */
    List<EmpSalaryAdjustPlanDTO> selectByDepartmentId(Long departmentId);

    /**
     * 根据岗位ID集合获取个人调薪
     * @param postId
     * @return
     */
    List<EmpSalaryAdjustPlanDTO> selectByPostId(Long postId);
}
