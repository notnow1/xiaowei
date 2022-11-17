package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.tenant.TenantDTO;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;


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
     * 根据code查询员工表列表
     *
     * @param employeeCodes code集合
     * @return 员工表集合
     */
    List<EmployeeDTO> selectCodeList(List<String> employeeCodes);
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

    /**
     * 导入Excel
     * @param list
     */
    void importEmployee(List<EmployeeExcel> list);

    /**
     * 导出Excel
     * @param employeeDTO
     * @return
     */
    List<EmployeeExcel> exportEmployee(EmployeeDTO employeeDTO);

    /**
     * 查询未分配用户员工列表
     */
    List<EmployeeDTO> unallocatedUserList();

}
