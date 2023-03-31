package net.qixiaowei.system.manage.service.basic;

import java.text.ParseException;
import java.util.List;
import java.util.Map;

import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalaryPlanVO;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalarySnapVO;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import org.apache.ibatis.annotations.Param;


/**
 * EmployeeService接口
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
public interface IEmployeeService {
    /**
     * 查询员工表
     *
     * @param employeeId 员工表主键
     * @return 员工表
     */
    EmployeeDTO selectEmployeeByEmployeeId(Long employeeId);

    /**
     * 根据id集合查询员工表
     *
     * @param employeeIds 员工表主键集合
     * @return 员工表
     */
    List<EmployeeDTO> selectEmployeeByEmployeeIds(List<Long> employeeIds);

    /**
     * 查询员工表列表
     *
     * @param employeeDTO 员工表
     * @return 员工表集合
     */
    List<EmployeeDTO> selectEmployeeList(EmployeeDTO employeeDTO);

    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<EmployeeDTO> result);

    /**
     * 查询员工表列表(下拉框)
     *
     * @param employeeDTO
     * @return
     */
    List<EmployeeDTO> selectDropEmployeeList(@Param("employee") EmployeeDTO employeeDTO);

    /**
     * 根据code查询员工表列表
     *
     * @param employeeCodes code集合
     * @return 员工表集合
     */
    List<EmployeeDTO> selectCodeList(List<String> employeeCodes);

    /**
     * 通过部门，岗位，职级集合查询员工表
     *
     * @param idMaps id集合表
     * @return 员工表集合
     */
    List<EmployeeDTO> selectEmployeeByPDRIds(Map<String, List<String>> idMaps);

    /**
     * 查询员工单条信息
     *
     * @param employeeDTO 员工表
     * @return 员工表集合
     */
    EmployeeDTO selectEmployee(EmployeeDTO employeeDTO);

    /**
     * 生成员工编码
     *
     * @return 员工编码
     */
    String generateEmployeeCode();

    /**
     * 新增员工表
     *
     * @param employeeDTO 员工表
     * @return 结果
     */
    EmployeeDTO insertEmployee(EmployeeDTO employeeDTO);

    /**
     * 修改员工表
     *
     * @param employeeDTO 员工表
     * @return 结果
     */
    int updateEmployee(EmployeeDTO employeeDTO);

    /**
     * 批量修改员工表
     *
     * @param employeeDtos 员工表
     * @return 结果
     */
    int updateEmployees(List<EmployeeDTO> employeeDtos);

    /**
     * 批量新增员工表
     *
     * @param employeeDtos 员工表
     * @return 结果
     */
    int insertEmployees(List<EmployeeDTO> employeeDtos);

    /**
     * 逻辑批量删除员工表
     *
     * @param employeeIds 需要删除的员工表集合
     * @return 结果
     */
    int logicDeleteEmployeeByEmployeeIds(List<Long> employeeIds);

    /**
     * 逻辑删除员工表信息
     *
     * @param employeeDTO
     * @return 结果
     */
    int logicDeleteEmployeeByEmployeeId(EmployeeDTO employeeDTO);

    /**
     * 逻辑批量删除员工表
     *
     * @param EmployeeDtos 需要删除的员工表集合
     * @return 结果
     */
    int deleteEmployeeByEmployeeIds(List<EmployeeDTO> EmployeeDtos);

    /**
     * 逻辑删除员工表信息
     *
     * @param employeeDTO
     * @return 结果
     */
    int deleteEmployeeByEmployeeId(EmployeeDTO employeeDTO);


    /**
     * 删除员工表信息
     *
     * @param employeeId 员工表主键
     * @return 结果
     */
    int deleteEmployeeByEmployeeId(Long employeeId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importEmployee(List<EmployeeExcel> list) throws ParseException;

    /**
     * 导出Excel
     *
     * @param employeeDTO
     * @return
     */
    List<EmployeeExcel> exportEmployee(EmployeeDTO employeeDTO);

    /**
     * 查询未分配用户员工列表
     */
    List<EmployeeDTO> unallocatedUserList(Long userId);

    /**
     * 分页查询岗位薪酬报表
     *
     * @param employeeDTO
     * @return
     */
    List<EmployeeDTO> pagePostSalaryReportList(EmployeeDTO employeeDTO);

    /**
     * 新增人力预算上年期末数集合
     *
     * @param employeeDTO
     * @return
     */
    List<OfficialRankSystemDTO> selecTamountLastYearList(EmployeeDTO employeeDTO);

    /**
     * 根据部门 职级 获取人员信息集合
     *
     * @param list
     * @return
     */
    List<EmployeeDTO> selectByBudgeList(List<List<Long>> list);

    /**
     * 根据Code集合根据Code集合
     *
     * @param assessmentList
     * @return
     */
    List<EmployeeDTO> selectByCodes(List<String> assessmentList);

    /**
     * 相同部门下 相同职级的 在职人数
     *
     * @param departmentIds
     * @return
     */
    List<EmployeeDTO> selectDepartmentAndOfficialRankSystem(List<Long> departmentIds);

    /**
     * 查询部门下所有人员
     *
     * @param departmentId
     * @return
     */
    List<EmployeeDTO> selectEmployeeByDepts(Long departmentId);

    /**
     * 根据部门id查询员工表列表
     *
     * @param employeeDepartmentId
     * @return
     */
    List<EmployeeDTO> queryEmployeeByDept(Long employeeDepartmentId);

    /**
     * 查询一级部门下所有的人员 返回部门id和职级体系id
     *
     * @param departmentIdAll
     * @return
     */
    List<EmployeeDTO> selectParentDepartmentIdAndOfficialRankSystem(List<Long> departmentIdAll);

    /**
     * 远程查询所有人员
     *
     * @return
     */
    List<EmployeeDTO> getAll();

    /**
     * 根据部门ID 集合查询人员
     *
     * @param departmentIds 人员ID集合
     * @return
     */
    List<EmployeeDTO> selectEmployeeByDepartmentIds(List<Long> departmentIds);

    /**
     * 远程个人调薪计划员工信息
     *
     * @param employeeDTO 员工DTO
     * @return R
     */
    EmployeeSalaryPlanVO empSalaryAdjustPlan(EmployeeDTO employeeDTO);

    /**
     * 远程查询用户数据
     *
     * @param employeeDTO
     * @return
     */
    List<EmployeeDTO> selectUserList(EmployeeDTO employeeDTO);

    /**
     * 根据调整策略进行更新人员薪资，岗位，职级
     *
     * @param employeeSalarySnapVO 计划VO
     * @return int
     */
    int empAdjustUpdate(EmployeeSalarySnapVO employeeSalarySnapVO);

    /**
     * 根据调整策略进行更新人员薪资，岗位，职级
     *
     * @param employeeSalarySnapVOS 计划VO集合
     * @return int
     */
    int empAdjustUpdates(List<EmployeeSalarySnapVO> employeeSalarySnapVOS);

    /**
     * 查询有账号的员工
     * @return
     */
    List<EmployeeDTO> getUseEmployeeUser();
}
