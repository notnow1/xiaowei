package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


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
     * 查询员工表
     *
     * @param employeeCode 员工表主键
     * @return 员工表
     */
    EmployeeDTO selectEmployeeByEmployeeCode(@Param("employeeCode") String employeeCode);


    /**
     * 查询员工表列表
     *
     * @param employee 员工表
     * @return 员工表集合
     */
    List<EmployeeDTO> selectEmployeeList(@Param("employee") Employee employee);

    /**
     * 查询未分配用户的员工表列表
     *
     * @return 员工表集合
     */
    List<EmployeeDTO> unallocatedUserList();

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
     * @param employeeCodes
     * @return
     */
    List<EmployeeDTO> selectEmployeeByEmployeeCodes(@Param("employeeCodes")List<String> employeeCodes);

    /**
     * 根据身份证号批量查询人员信息
     * @param identityCards
     * @return
     */
    List<EmployeeDTO> selectEmployeeByIdCards(@Param("identityCards")List<String> identityCards);

    /**
     * 根据code查询员工表列表
     * @param employeeCodes
     * @return
     */
    List<EmployeeDTO> selectCodeList(@Param("employeeCodes")List<String> employeeCodes);
}
