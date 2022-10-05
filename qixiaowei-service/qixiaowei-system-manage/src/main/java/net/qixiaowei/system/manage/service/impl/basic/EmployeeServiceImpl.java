package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* EmployeeService业务层处理
* @author TANGMICHI
* @since 2022-09-30
*/
@Service
public class EmployeeServiceImpl implements IEmployeeService{
    @Autowired
    private EmployeeMapper employeeMapper;

    /**
    * 查询员工表
    *
    * @param employeeId 员工表主键
    * @return 员工表
    */
    @Override
    public EmployeeDTO selectEmployeeByEmployeeId(Long employeeId)
    {
    return employeeMapper.selectEmployeeByEmployeeId(employeeId);
    }

    /**
    * 查询员工表列表
    *
    * @param employeeDTO 员工表
    * @return 员工表
    */
    @Override
    public List<EmployeeDTO> selectEmployeeList(EmployeeDTO employeeDTO)
    {
    Employee employee=new Employee();
    BeanUtils.copyProperties(employeeDTO,employee);
    return employeeMapper.selectEmployeeList(employee);
    }

    /**
    * 新增员工表
    *
    * @param employeeDTO 员工表
    * @return 结果
    */
    @Transactional
    @Override
    public int insertEmployee(EmployeeDTO employeeDTO){
    Employee employee=new Employee();
    BeanUtils.copyProperties(employeeDTO,employee);
    employee.setCreateBy(SecurityUtils.getUserId());
    employee.setCreateTime(DateUtils.getNowDate());
    employee.setUpdateTime(DateUtils.getNowDate());
    employee.setUpdateBy(SecurityUtils.getUserId());
    employee.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return employeeMapper.insertEmployee(employee);
    }

    /**
    * 修改员工表
    *
    * @param employeeDTO 员工表
    * @return 结果
    */
    @Transactional
    @Override
    public int updateEmployee(EmployeeDTO employeeDTO)
    {
    Employee employee=new Employee();
    BeanUtils.copyProperties(employeeDTO,employee);
    employee.setUpdateTime(DateUtils.getNowDate());
    employee.setUpdateBy(SecurityUtils.getUserId());
    return employeeMapper.updateEmployee(employee);
    }

    /**
    * 逻辑批量删除员工表
    *
    * @param employeeDtos 需要删除的员工表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int logicDeleteEmployeeByEmployeeIds(List<EmployeeDTO> employeeDtos){
            List<Long> stringList = new ArrayList();
            for (EmployeeDTO employeeDTO : employeeDtos) {
                stringList.add(employeeDTO.getEmployeeId());
            }
    return employeeMapper.logicDeleteEmployeeByEmployeeIds(stringList,employeeDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除员工表信息
    *
    * @param employeeId 员工表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteEmployeeByEmployeeId(Long employeeId)
    {
    return employeeMapper.deleteEmployeeByEmployeeId(employeeId);
    }

     /**
     * 逻辑删除员工表信息
     *
     * @param  employeeDTO 员工表
     * @return 结果
     */
     @Transactional
     @Override
     public int logicDeleteEmployeeByEmployeeId(EmployeeDTO employeeDTO)
     {
     Employee employee=new Employee();
     BeanUtils.copyProperties(employeeDTO,employee);
     return employeeMapper.logicDeleteEmployeeByEmployeeId(employee,SecurityUtils.getUserId(),DateUtils.getNowDate());
     }

     /**
     * 物理删除员工表信息
     *
     * @param  employeeDTO 员工表
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteEmployeeByEmployeeId(EmployeeDTO employeeDTO)
     {
     Employee employee=new Employee();
     BeanUtils.copyProperties(employeeDTO,employee);
     return employeeMapper.deleteEmployeeByEmployeeId(employee.getEmployeeId());
     }
     /**
     * 物理批量删除员工表
     *
     * @param employeeDtos 需要删除的员工表主键
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteEmployeeByEmployeeIds(List<EmployeeDTO> employeeDtos){
     List<Long> stringList = new ArrayList();
     for (EmployeeDTO employeeDTO : employeeDtos) {
     stringList.add(employeeDTO.getEmployeeId());
     }
     return employeeMapper.deleteEmployeeByEmployeeIds(stringList);
     }

    /**
    * 批量新增员工表信息
    *
    * @param employeeDtos 员工表对象
    */
    @Transactional
    public int insertEmployees(List<EmployeeDTO> employeeDtos){
      List<Employee> employeeList = new ArrayList();

    for (EmployeeDTO employeeDTO : employeeDtos) {
      Employee employee =new Employee();
      BeanUtils.copyProperties(employeeDTO,employee);
       employee.setCreateBy(SecurityUtils.getUserId());
       employee.setCreateTime(DateUtils.getNowDate());
       employee.setUpdateTime(DateUtils.getNowDate());
       employee.setUpdateBy(SecurityUtils.getUserId());
       employee.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      employeeList.add(employee);
    }
    return employeeMapper.batchEmployee(employeeList);
    }

    /**
    * 批量修改员工表信息
    *
    * @param employeeDtos 员工表对象
    */
    @Transactional
    public int updateEmployees(List<EmployeeDTO> employeeDtos){
     List<Employee> employeeList = new ArrayList();

     for (EmployeeDTO employeeDTO : employeeDtos) {
     Employee employee =new Employee();
     BeanUtils.copyProperties(employeeDTO,employee);
        employee.setCreateBy(SecurityUtils.getUserId());
        employee.setCreateTime(DateUtils.getNowDate());
        employee.setUpdateTime(DateUtils.getNowDate());
        employee.setUpdateBy(SecurityUtils.getUserId());
     employeeList.add(employee);
     }
     return employeeMapper.updateEmployees(employeeList);
    }
}

