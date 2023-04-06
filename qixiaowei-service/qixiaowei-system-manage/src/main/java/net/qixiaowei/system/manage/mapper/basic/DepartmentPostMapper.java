package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.basic.DepartmentPost;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DepartmentPostMapper接口
* @author TANGMICHI
* @since 2022-09-27
*/
public interface DepartmentPostMapper{
    /**
    * 查询部门岗位关联表
    *
    * @param departmentPostId 部门岗位关联表主键
    * @return 部门岗位关联表
    */
    DepartmentPostDTO selectDepartmentPostByDepartmentPostId(@Param("departmentPostId")Long departmentPostId);

    /**
    * 查询部门岗位关联表列表
    *
    * @param departmentPost 部门岗位关联表
    * @return 部门岗位关联表集合
    */
    List<DepartmentPostDTO> selectDepartmentPostList(@Param("departmentPost")DepartmentPost departmentPost);

    /**
    * 新增部门岗位关联表
    *
    * @param departmentPost 部门岗位关联表
    * @return 结果
    */
    int insertDepartmentPost(@Param("departmentPost")DepartmentPost departmentPost);

    /**
    * 修改部门岗位关联表
    *
    * @param departmentPost 部门岗位关联表
    * @return 结果
    */
    int updateDepartmentPost(@Param("departmentPost")DepartmentPost departmentPost);

    /**
    * 批量修改部门岗位关联表
    *
    * @param departmentPostList 部门岗位关联表
    * @return 结果
    */
    int updateDepartmentPosts(@Param("departmentPostList")List<DepartmentPost> departmentPostList);
    /**
    * 逻辑删除部门岗位关联表
    *
    * @param departmentPost
    * @return 结果
    */
    int logicDeleteDepartmentPostByDepartmentPostId(@Param("departmentPost")DepartmentPost departmentPost);

    /**
    * 逻辑批量删除部门岗位关联表
    *
    * @param departmentPostIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDepartmentPostByDepartmentPostIds(@Param("departmentPostIds")List<Long> departmentPostIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
     * 根据组织id逻辑批量删除部门岗位关联表
     *
     * @param departmentIds 需要删除的数据组织id集合
     * @return 结果
     */
    int logicDeleteDepartmentPostByDepartmentIds(@Param("departmentIds")List<Long> departmentIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除部门岗位关联表
    *
    * @param departmentPostId 部门岗位关联表主键
    * @return 结果
    */
    int deleteDepartmentPostByDepartmentPostId(@Param("departmentPostId")Long departmentPostId);

    /**
    * 物理批量删除部门岗位关联表
    *
    * @param departmentPostIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDepartmentPostByDepartmentPostIds(@Param("departmentPostIds")List<Long> departmentPostIds);

    /**
    * 批量新增部门岗位关联表
    *
    * @param DepartmentPosts 部门岗位关联表列表
    * @return 结果
    */
    int batchDepartmentPost(@Param("departmentPosts")List<DepartmentPost> DepartmentPosts);

    /**
     * 根据部门id查询出数据库的数据
     * @return
     */
    List<DepartmentPostDTO> selectDepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据部门中间表id查询出数据库的数据
     * @return
     */
    List<DepartmentDTO> selectDepartmentPostId(@Param("postId")Long postId);

    /**
     * Excel根据部门中间表id查询出数据库的数据
     * @return
     */
    List<DepartmentPostDTO> selectExcelDepartmentPostId(@Param("postId")Long postId);
    /**
     * 根据部门中间表id集合查询出数据库的数据
     * @param postIds
     * @return
     */
    List<DepartmentPostDTO> selectDepartmentPostIds(@Param("postIds")List<Long> postIds);

    /**
     * 根据部门id批量查询出数据库的数据
     * @return
     */
    List<DepartmentPostDTO> selectDepartmentIds(@Param("departmentIds") List<Long> departmentIds);

    /**
     * 根据部门id批量查询出数据库的数据(岗位信息)
     * @return
     */
    List<DepartmentPostDTO> selectPostDepartmentIds(@Param("departmentIds") List<Long> departmentIds);

    /**
     * 根据部门id批量查询出数据库的数据(岗位信息)
     *
     * @return List
     */
    List<DepartmentPostDTO> selectPostByRankSystemId(@Param("departmentIds") List<Long> departmentIds, @Param("officialRankSystemId") Long officialRankSystemId);

}
