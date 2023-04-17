package net.qixiaowei.system.manage.mapper.basic;

import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * EmployeeMapper接口
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
public interface EmployeeMapper {
    /**
     * 查询员工表
     *
     * @param employeeId 员工表主键
     * @return 员工表
     */
    EmployeeDTO selectEmployeeByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 查询员工表List
     *
     * @param employeeIds 员工表主键
     * @return 员工表
     */
    List<EmployeeDTO> selectEmployeeByEmployeeIds(@Param("employeeIds") List<Long> employeeIds);

    /**
     * 查询人员编码集合
     *
     * @param prefixCodeRule 编码前缀
     * @return 人员编码集合
     */
    List<String> getEmployeeCodes(@Param("prefixCodeRule") String prefixCodeRule);

    /**
     * 查询员工表
     *
     * @param employeeCode 员工表主键
     * @return 员工表
     */
    EmployeeDTO selectEmployeeByEmployeeCode(@Param("employeeCode") String employeeCode);

    /**
     * 根据手机号查询员工表
     *
     * @param employeeMobile 员工手机号 注:后面新加需求因有脏数据 防止报错用list接收
     * @return 员工表
     */
    List<EmployeeDTO> selectEmployeeByEmployeeMobile(@Param("employeeMobile") String employeeMobile);

    /**
     * 根据邮箱号查询员工表
     *
     * @param employeeEmail 邮箱号 注:后面新加需求因有脏数据 防止报错用list接收
     * @return 员工表
     */
    List<EmployeeDTO> selectEmployeeByEmployeeEmail(@Param("employeeEmail") String employeeEmail);
    /**
     * 查询员工表列表
     *
     * @param employee 员工表
     * @return 员工表集合
     */
    List<EmployeeDTO> selectEmployeeList(@Param("employee") Employee employee);

    /**
     * 通过部门，岗位，职级集合查询员工表
     *
     * @param departmentIds     部门
     * @param postIds           岗位
     * @param employeeRankNames 职级名称
     * @return 员工表集合
     */
    List<EmployeeDTO> selectEmployeeByPDRIds(@Param("departmentIds") List<Long> departmentIds, @Param("postIds") List<Long> postIds, @Param("employeeRankNames") List<String> employeeRankNames);

    /**
     * 查询未分配用户的员工表列表
     *
     * @return 员工表集合
     */
    List<EmployeeDTO> unallocatedUserList(@Param("userId") Long userId);

    /**
     * 新增员工表
     *
     * @param employee 员工表
     * @return 结果
     */
    int insertEmployee(@Param("employee") Employee employee);

    /**
     * 修改员工表
     *
     * @param employee 员工表
     * @return 结果
     */
    int updateEmployee(@Param("employee") Employee employee);

    /**
     * 批量修改员工表
     *
     * @param employeeList 员工表
     * @return 结果
     */
    int updateEmployees(@Param("employeeList") List<Employee> employeeList);

    /**
     * 逻辑删除员工表
     *
     * @param employee
     * @return 结果
     */
    int logicDeleteEmployeeByEmployeeId(@Param("employee") Employee employee, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除员工表
     *
     * @param employeeIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteEmployeeByEmployeeIds(@Param("employeeIds") List<Long> employeeIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除员工表
     *
     * @param employeeId 员工表主键
     * @return 结果
     */
    int deleteEmployeeByEmployeeId(@Param("employeeId") Long employeeId);

    /**
     * 物理批量删除员工表
     *
     * @param employeeIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteEmployeeByEmployeeIds(@Param("employeeIds") List<Long> employeeIds);

    /**
     * 批量新增员工表
     *
     * @param Employees 员工表列表
     * @return 结果
     */
    int batchEmployee(@Param("employees") List<Employee> Employees);

    /**
     * 查询员工单条信息
     *
     * @param employee
     * @return
     */
    EmployeeDTO selectEmployee(@Param("employee") Employee employee);

    /**
     * 批量查询是否被引用
     *
     * @param employeePostIds
     * @return
     */
    List<EmployeeDTO> selectEmployeePostIds(@Param("employeePostIds") List<Long> employeePostIds);

    /**
     * 查询是否被引用
     *
     * @param employeePostId
     * @return
     */
    List<EmployeeDTO> selectEmployeePostId(@Param("employeePostId") Long employeePostId);

    /**
     * 根据code批量查询人员信息
     *
     * @param employeeCodes
     * @return
     */
    List<EmployeeDTO> selectEmployeeByEmployeeCodes(@Param("employeeCodes") List<String> employeeCodes);

    /**
     * 根据身份证号批量查询人员信息
     *
     * @param identityCards
     * @return
     */
    List<EmployeeDTO> selectEmployeeByIdCards(@Param("identityCards") List<String> identityCards);

    /**
     * 根据code查询员工表列表
     *
     * @param employeeCodes
     * @return
     */
    List<EmployeeDTO> selectCodeList(@Param("employeeCodes") List<String> employeeCodes);

    /**
     * 分页查询岗位薪酬报表
     *
     * @param employee
     * @return
     */
    List<EmployeeDTO> selectPostSalaryReportList(@Param("employee") Employee employee);

    /**
     * 新增人力预算上年期末数集合
     *
     * @param employee
     * @return
     */
    List<EmployeeDTO> selecTamountLastYearList(@Param("employee") Employee employee);

    /**
     * 根据部门 职级 获取人员信息集合
     *
     * @param list
     * @return
     */
    List<EmployeeDTO> selectByBudgeList(@Param("list") List<List<Long>> list);

    /**
     * 查询在职的所有员工
     *
     * @param employee
     * @return
     */
    List<EmployeeDTO> selectDropDownEmployeeList(@Param("employee") Employee employee);

    /**
     * 根据Code集合
     *
     * @param assessmentList
     * @return
     */
    List<EmployeeDTO> selectByCodes(@Param("assessmentList") List<String> assessmentList);

    /**
     * 相同部门下 相同职级的 在职人数
     *
     * @param departmentIds
     * @return
     */
    List<EmployeeDTO> selectDepartmentAndOfficialRankSystem(@Param("departmentIds") List<Long> departmentIds);

    /**
     * 远程 查询部门下所有人员
     *
     * @param departmentId
     * @return
     */
    List<EmployeeDTO> selectEmployeeByDepts(@Param("departmentId") Long departmentId);

    /**
     * 根据部门id查询员工表列表
     *
     * @param employee
     * @return
     */
    List<EmployeeDTO> queryEmployeeByDept(@Param("employee") Employee employee);

    /**
     * 查询员工表列表(下拉框)
     *
     * @param employee
     * @return
     */
    List<EmployeeDTO> selectDropEmployeeList(@Param("employee") Employee employee);

    /**
     * 远程 查询一级部门下所有的人员 返回部门id和职级体系id
     *
     * @param departmentIdAll
     * @return
     */
    List<EmployeeDTO> selectParentDepartmentIdAndOfficialRankSystem(@Param("departmentIdAll") List<Long> departmentIdAll);

    /**
     * 远程查询所有人员
     *
     * @return
     */
    List<EmployeeDTO> getAll();

    /**
     * 根据部门ID集合 查询人员
     *
     * @param departmentIds 部门ID集合
     * @return
     */
    List<EmployeeDTO> selectEmployeeByDepartmentIds(@Param("departmentIds") List<Long> departmentIds);


    /**
     * 根据部门ID 查询人员
     *
     * @param departmentId 部门ID
     * @return
     */
    List<EmployeeDTO> selectEmployeeByDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 远程查询用户数据
     *
     * @param employee 员工DTO
     * @return
     */
    List<EmployeeDTO> selectUserList(@Param("employee") Employee employee);

    /**
     * 查询在职有账号的员工
     *
     * @return
     */
    List<EmployeeDTO> getUseEmployeeUser();

    /**
     * 生效包含在职离职有账号的员工
     *
     * @return
     */
    List<EmployeeDTO> getUseEmployeeStatus();

    /**
     * 生效包含在职离职的员工
     *
     * @return
     */
    List<EmployeeDTO> getAllUseEmployee();

    /**
     * 根据name 查询
     *
     * @param employeeNames 人员名称
     * @return 结果
     */
    List<EmployeeDTO> selectByNames(@Param("employeeNames") List<String> employeeNames);
}
