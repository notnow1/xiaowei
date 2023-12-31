package net.qixiaowei.operate.cloud.service.employee;

import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDetailsDTO;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetDetailsExcel;
import net.qixiaowei.operate.cloud.excel.employee.EmployeeBudgetExcel;

import java.util.List;


/**
 * EmployeeBudgetService接口
 *
 * @author TANGMICHI
 * @since 2022-11-18
 */
public interface IEmployeeBudgetService {
    /**
     * 查询人力预算表
     *
     * @param employeeBudgetId 人力预算表主键
     * @return 人力预算表
     */
    EmployeeBudgetDTO selectEmployeeBudgetByEmployeeBudgetId(Long employeeBudgetId);

    /**
     * 查询人力预算表列表
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 人力预算表集合
     */
    List<EmployeeBudgetDTO> selectEmployeeBudgetList(EmployeeBudgetDTO employeeBudgetDTO);
    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<EmployeeBudgetDTO> result);

    /**
     * 新增人力预算表
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 结果
     */
    EmployeeBudgetDTO insertEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO);

    /**
     * 修改人力预算表
     *
     * @param employeeBudgetDTO 人力预算表
     * @return 结果
     */
    int updateEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO);

    /**
     * 批量修改人力预算表
     *
     * @param employeeBudgetDtos 人力预算表
     * @return 结果
     */
    int updateEmployeeBudgets(List<EmployeeBudgetDTO> employeeBudgetDtos);

    /**
     * 批量新增人力预算表
     *
     * @param employeeBudgetDtos 人力预算表
     * @return 结果
     */
    int insertEmployeeBudgets(List<EmployeeBudgetDTO> employeeBudgetDtos);

    /**
     * 逻辑批量删除人力预算表
     *
     * @param employeeBudgetIds 需要删除的人力预算表集合
     * @return 结果
     */
    int logicDeleteEmployeeBudgetByEmployeeBudgetIds(List<Long> employeeBudgetIds);

    /**
     * 逻辑删除人力预算表信息
     *
     * @param employeeBudgetDTO
     * @return 结果
     */
    int logicDeleteEmployeeBudgetByEmployeeBudgetId(EmployeeBudgetDTO employeeBudgetDTO);

    /**
     * 批量删除人力预算表
     *
     * @param EmployeeBudgetDtos
     * @return 结果
     */
    int deleteEmployeeBudgetByEmployeeBudgetIds(List<EmployeeBudgetDTO> EmployeeBudgetDtos);

    /**
     * 逻辑删除人力预算表信息
     *
     * @param employeeBudgetDTO
     * @return 结果
     */
    int deleteEmployeeBudgetByEmployeeBudgetId(EmployeeBudgetDTO employeeBudgetDTO);


    /**
     * 删除人力预算表信息
     *
     * @param employeeBudgetId 人力预算表主键
     * @return 结果
     */
    int deleteEmployeeBudgetByEmployeeBudgetId(Long employeeBudgetId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importEmployeeBudget(List<EmployeeBudgetExcel> list);

    /**
     * 导出Excel
     *
     * @param employeeBudgetDTO
     * @return
     */
    List<EmployeeBudgetExcel> exportEmployeeBudget(EmployeeBudgetDTO employeeBudgetDTO);

    /**
     * 查询增人/减人工资包列表
     *
     * @param employeeBudgetDTO
     * @return
     */
    List<EmployeeBudgetDetailsDTO> salaryPackageList(EmployeeBudgetDTO employeeBudgetDTO);

    /**
     * 导出增人/减人工资包
     *
     * @param employeeBudgetDTO
     * @return
     */
    List<EmployeeBudgetDetailsExcel> export(EmployeeBudgetDTO employeeBudgetDTO);

    /**
     * 根据职级体系ID集合查询预算表
     *
     * @param officialRankSystemIds 职级体系ID集合
     * @return List
     */
    List<EmployeeBudgetDTO> selectBySystemIds(List<Long> officialRankSystemIds);

    /**
     * 远程 根据部门ID集合查询预算表
     * @param departmentIds
     * @return
     */
    List<EmployeeBudgetDTO> selectByDepartmentIds(List<Long> departmentIds);
}
