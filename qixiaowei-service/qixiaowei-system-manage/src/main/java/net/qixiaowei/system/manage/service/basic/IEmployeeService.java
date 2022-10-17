package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;


/**
* EmployeeService接口
* @author TANGMICHI
* @since 2022-09-30
*/
public interface IEmployeeService{
    /**
    * 查询员工表
    *
    * @param employeeId 员工表主键
    * @return 员工表
    */
    EmployeeDTO selectEmployeeByEmployeeId(Long employeeId);

    /**
    * 查询员工表列表
    *
    * @param employeeDTO 员工表
    * @return 员工表集合
    */
    List<EmployeeDTO> selectEmployeeList(EmployeeDTO employeeDTO);
    /**
     * 查询员工单条信息
     *
     * @param employeeDTO 员工表
     * @return 员工表集合
     */
    EmployeeDTO selectEmployee(EmployeeDTO employeeDTO);

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
    int logicDeleteEmployeeByEmployeeIds(List<Long>  employeeIds);

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
}
