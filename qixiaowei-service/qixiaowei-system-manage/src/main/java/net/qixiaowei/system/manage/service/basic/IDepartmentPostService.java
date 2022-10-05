package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;


/**
* DepartmentPostService接口
* @author TANGMICHI
* @since 2022-09-27
*/
public interface IDepartmentPostService{
    /**
    * 查询部门岗位关联表
    *
    * @param departmentPostId 部门岗位关联表主键
    * @return 部门岗位关联表
    */
    DepartmentPostDTO selectDepartmentPostByDepartmentPostId(Long departmentPostId);

    /**
    * 查询部门岗位关联表列表
    *
    * @param departmentPostDTO 部门岗位关联表
    * @return 部门岗位关联表集合
    */
    List<DepartmentPostDTO> selectDepartmentPostList(DepartmentPostDTO departmentPostDTO);

    /**
    * 新增部门岗位关联表
    *
    * @param departmentPostDTO 部门岗位关联表
    * @return 结果
    */
    int insertDepartmentPost(DepartmentPostDTO departmentPostDTO);

    /**
    * 修改部门岗位关联表
    *
    * @param departmentPostDTO 部门岗位关联表
    * @return 结果
    */
    int updateDepartmentPost(DepartmentPostDTO departmentPostDTO);

    /**
    * 批量修改部门岗位关联表
    *
    * @param departmentPostDtos 部门岗位关联表
    * @return 结果
    */
    int updateDepartmentPosts(List<DepartmentPostDTO> departmentPostDtos);

    /**
    * 批量新增部门岗位关联表
    *
    * @param departmentPostDtos 部门岗位关联表
    * @return 结果
    */
    int insertDepartmentPosts(List<DepartmentPostDTO> departmentPostDtos);

    /**
    * 逻辑批量删除部门岗位关联表
    *
    * @param DepartmentPostDtos 需要删除的部门岗位关联表集合
    * @return 结果
    */
    int logicDeleteDepartmentPostByDepartmentPostIds(List<DepartmentPostDTO> DepartmentPostDtos);

    /**
    * 逻辑删除部门岗位关联表信息
    *
    * @param departmentPostDTO
    * @return 结果
    */
    int logicDeleteDepartmentPostByDepartmentPostId(DepartmentPostDTO departmentPostDTO);
    /**
    * 逻辑批量删除部门岗位关联表
    *
    * @param DepartmentPostDtos 需要删除的部门岗位关联表集合
    * @return 结果
    */
    int deleteDepartmentPostByDepartmentPostIds(List<DepartmentPostDTO> DepartmentPostDtos);

    /**
    * 逻辑删除部门岗位关联表信息
    *
    * @param departmentPostDTO
    * @return 结果
    */
    int deleteDepartmentPostByDepartmentPostId(DepartmentPostDTO departmentPostDTO);


    /**
    * 删除部门岗位关联表信息
    *
    * @param departmentPostId 部门岗位关联表主键
    * @return 结果
    */
    int deleteDepartmentPostByDepartmentPostId(Long departmentPostId);
}
