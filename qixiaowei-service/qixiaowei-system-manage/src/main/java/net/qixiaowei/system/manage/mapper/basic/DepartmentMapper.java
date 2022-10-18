package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.basic.Department;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DepartmentMapper接口
* @author TANGMICHI
* @since 2022-09-27
*/
public interface DepartmentMapper{
    /**
    * 查询部门表
    *
    * @param departmentId 部门表主键
    * @return 部门表
    */
    DepartmentDTO selectDepartmentByDepartmentId(@Param("departmentId")Long departmentId);

    /**
     * 批量查询部门表
     *
     * @param departmentIds 部门表主键
     * @return 部门表
     */
    List<DepartmentDTO> selectDepartmentByDepartmentIds(@Param("departmentIds")List<Long> departmentIds);

    /**
    * 查询部门表列表
    *
    * @param departmentDTO 部门表
    * @return 部门表集合
    */
    List<DepartmentDTO> selectDepartmentList(@Param("department")DepartmentDTO departmentDTO);

    /**
     * 查询人员是否被部门引用
     *
     * @param employeeId 部门表
     * @return 部门表集合
     */
    List<EmployeeDTO> deleteFlagEmployee(@Param("employeeId")Long employeeId);

    /**
     * 根据条件筛选树
     *
     * @param department 部门表
     * @return 部门表集合
     */
    List<DepartmentDTO> getParentId(@Param("department")DepartmentDTO department);

    /**
    * 新增部门表
    *
    * @param department 部门表
    * @return 结果
    */
    int insertDepartment(@Param("department")Department department);

    /**
    * 修改部门表
    *
    * @param department 部门表
    * @return 结果
    */
    int updateDepartment(@Param("department")Department department);


    /**
    * 批量修改部门表
    *
    * @param departmentList 部门表
    * @return 结果
    */
    int updateDepartments(@Param("departmentList")List<Department> departmentList);
    /**
    * 逻辑删除部门表
    *
    * @param department
    * @return 结果
    */
    int logicDeleteDepartmentByDepartmentId(@Param("department")Department department);

    /**
    * 逻辑批量删除部门表
    *
    * @param departmentIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDepartmentByDepartmentIds(@Param("departmentIds")List<Long> departmentIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门表
    *
    * @param departmentId 部门表主键
    * @return 结果
    */
    int deleteDepartmentByDepartmentId(@Param("departmentId")Long departmentId);

    /**
    * 物理批量删除部门表
    *
    * @param departmentIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDepartmentByDepartmentIds(@Param("departmentIds")List<Long> departmentIds);

    /**
    * 批量新增部门表
    *
    * @param Departments 部门表列表
    * @return 结果
    */
    int batchDepartment(@Param("departments")List<Department> Departments);

    /**
     * 根据父级id查询数据
     * @param departmentId
     * @return
     */
    DepartmentDTO selectParentDepartmentId(@Param("departmentId")Long departmentId);

    /**
     * 根据id查询所有子级数据
     * @param departmentId
     * @return
     */
    List<DepartmentDTO> selectAncestors(@Param("departmentId")Long departmentId);

    /**
     * 查询上级组织
     * @return
     */
    List<DepartmentDTO> queryparent();

    /**
     * 查询code编码是否已经存在
     * @param departmentCode
     * @return
     */
    DepartmentDTO selectDepartmentCode(@Param("departmentCode")String departmentCode);

    /**
     * 批量查询code编码是否已经存在
     * @param departmentCodes
     * @return
     */
    List<DepartmentDTO> selectDepartmentCodes(@Param("departmentCodes")List<String> departmentCodes);

    /**
     * 查询组织关联岗位信息
     * @param departmentId
     * @return
     */
    List<DepartmentPostDTO> selectDeptAndPost(@Param("departmentId")Long departmentId);

    /**
     * 分页查询部门人员表列表
     * @param departmentId
     * @return
     */
    List<EmployeeDTO> queryDeptEmployee(@Param("departmentId") Long departmentId);

    List<DepartmentDTO> deleteFlagEmployees(@Param("collect")List<Long> collect);

    /**
     * 返回部门层级
     * @return
     */
    List<Integer> selectdeptLevel();

}
