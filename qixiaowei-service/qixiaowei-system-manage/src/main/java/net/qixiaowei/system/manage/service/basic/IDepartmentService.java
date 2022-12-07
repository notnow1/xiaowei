package net.qixiaowei.system.manage.service.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;


/**
 * DepartmentService接口
 *
 * @author TANGMICHI
 * @since 2022-09-27
 */
public interface IDepartmentService {
    /**
     * 查询部门表
     *
     * @param departmentId 部门表主键
     * @return 部门表
     */
    DepartmentDTO selectDepartmentByDepartmentId(Long departmentId);

    /**
     * 查询部门表列表
     *
     * @param departmentDTO 部门表
     * @return 部门表集合
     */
    List<DepartmentDTO> selectDepartmentList(DepartmentDTO departmentDTO);

    /**
     * 返回组织层级
     *
     * @return 层级集合
     */
    List<Integer> selectLevel();

    /**
     * 新增部门表
     *
     * @param departmentDTO 部门表
     * @return 结果
     */
    DepartmentDTO insertDepartment(DepartmentDTO departmentDTO);

    /**
     * 修改部门表
     *
     * @param departmentDTO 部门表
     * @return 结果
     */
    int updateDepartment(DepartmentDTO departmentDTO);

    /**
     * 批量修改部门表
     *
     * @param departmentDtos 部门表
     * @return 结果
     */
    int updateDepartments(List<DepartmentDTO> departmentDtos);

    /**
     * 批量新增部门表
     *
     * @param departmentDtos 部门表
     * @return 结果
     */
    int insertDepartments(List<DepartmentDTO> departmentDtos);

    /**
     * 逻辑批量删除部门表
     *
     * @param departmentIds 需要删除的部门表集合
     * @return 结果
     */
    int logicDeleteDepartmentByDepartmentIds(List<Long> departmentIds);

    /**
     * 逻辑删除部门表信息
     *
     * @param departmentDTO
     * @return 结果
     */
    int logicDeleteDepartmentByDepartmentId(DepartmentDTO departmentDTO);

    /**
     * 逻辑批量删除部门表信息
     *
     * @param departmentDTO
     * @return 结果
     */
    int logicDeleteDepartmentByDepartmentIds(DepartmentDTO departmentDTO);

    /**
     * 逻辑批量删除部门表
     *
     * @param DepartmentDtos 需要删除的部门表集合
     * @return 结果
     */
    int deleteDepartmentByDepartmentIds(List<DepartmentDTO> DepartmentDtos);

    /**
     * 逻辑删除部门表信息
     *
     * @param departmentDTO
     * @return 结果
     */
    int deleteDepartmentByDepartmentId(DepartmentDTO departmentDTO);


    /**
     * 删除部门表信息
     *
     * @param departmentId 部门表主键
     * @return 结果
     */
    int deleteDepartmentByDepartmentId(Long departmentId);

    /**
     * 查询上级组织
     *
     * @return
     */
    List<DepartmentDTO> queryparent();

    /**
     * 部门岗位详情
     *
     * @param departmentId
     * @return
     */
    DepartmentDTO deptParticulars(Long departmentId);

    /**
     * 分页查询部门人员表列表
     *
     * @param departmentDTO
     * @return
     */
    List<EmployeeDTO> queryDeptEmployee(DepartmentDTO departmentDTO);

    /**
     * 获取部门列表
     *
     * @param departmentDTO
     * @return
     */
    List<DepartmentDTO> dropList(DepartmentDTO departmentDTO);

    /**
     * 通过部门ID获取列表
     *
     * @param departmentIds
     * @return
     */
    List<DepartmentDTO> selectDepartmentByDepartmentIds(List<Long> departmentIds);

    /**
     * 根据code查询部门集合
     *
     * @param departmentCodes
     * @return
     */
    List<DepartmentDTO> selectCodeList(List<String> departmentCodes);

    /**
     * 查询所有
     *
     * @param departmentDTO
     * @return
     */
    List<DepartmentDTO> selectDepartmentAll(DepartmentDTO departmentDTO);

    /**
     * 查询所有部门
     * @return
     */
    List<DepartmentDTO> getAll();

    /**
     *
     * @return
     */
    List<DepartmentDTO> selectParentDepartment(Long departmentId);
}
