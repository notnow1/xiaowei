package net.qixiaowei.operate.cloud.service.salary;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

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
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<DeptSalaryAdjustPlanDTO> result);

    /**
     * 新增部门调薪计划表
     *
     * @param deptSalaryAdjustPlanDTO 部门调薪计划表
     * @return 结果
     */
    int insertDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

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
     * @param deptSalaryAdjustPlanDTO 调薪计划dto
     * @return 结果
     */
    int logicDeleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanId(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 批量删除部门调薪计划表
     *
     * @param DeptSalaryAdjustPlanDtoS 调薪计划dto列表
     * @return 结果
     */
    int deleteDeptSalaryAdjustPlanByDeptSalaryAdjustPlanIds(List<DeptSalaryAdjustPlanDTO> DeptSalaryAdjustPlanDtoS);

    /**
     * 逻辑删除部门调薪计划表信息
     *
     * @param deptSalaryAdjustPlanDTO 调薪计划dto
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
     * @param list list
     */
    void importDeptSalaryAdjustPlan(List<DeptSalaryAdjustPlanExcel> list);

    /**
     * 导出Excel
     *
     * @param deptSalaryAdjustPlanDTO 调薪
     * @return List
     */
    List<DeptSalaryAdjustPlanExcel> exportDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 编辑部门调薪
     *
     * @param deptSalaryAdjustPlanDTO 调薪计划
     * @return DeptSalaryAdjustPlanDTO
     */
    int editDeptSalaryAdjustPlan(DeptSalaryAdjustPlanDTO deptSalaryAdjustPlanDTO);

    /**
     * 获取上年发薪包
     *
     * @param departmentId 部门ID
     * @param planYear     预算年份
     * @return String
     */
    Map<String, BigDecimal> getLastSalary(Long departmentId, Integer planYear);

    /**
     * 获取涨薪包预算
     *
     * @param planYear 预算年份
     * @return BigDecimal
     */
    BigDecimal getRaiseSalary(Integer planYear);

    /**
     * 获取已有数据的最大年份
     *
     * @return Integer
     */
    Integer getMaxYear();

    /**
     * 获取已存在的年份
     *
     * @return list
     */
    List<String> getExistYear();
}
