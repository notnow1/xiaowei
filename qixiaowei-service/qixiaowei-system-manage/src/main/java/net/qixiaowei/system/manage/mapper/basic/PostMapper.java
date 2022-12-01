package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.basic.Post;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * PostMapper接口
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
public interface PostMapper {
    /**
     * 查询岗位表
     *
     * @param postId 岗位表主键
     * @return 岗位表
     */
    PostDTO selectPostByPostId(@Param("postId") Long postId);

    /**
     * 查询岗位表
     *
     * @param postIds 岗位表主键
     * @return 岗位表
     */
    List<DepartmentPostDTO> selectPostByPostIds(@Param("postIds") List<Long> postIds);

    /**
     * 查询岗位表Code
     *
     * @param postCode 岗位表Code
     * @return 岗位表
     */
    PostDTO selectPostCode(@Param("postCode") String postCode);

    /**
     * 查询岗位表名称
     *
     * @param postName 岗位表Code
     * @return 岗位表
     */
    PostDTO selectPostName(@Param("postName") String postName);

    /**
     * 查询岗位表列表
     *
     * @param post 岗位表
     * @return 岗位表集合
     */
    List<PostDTO> selectPostList(@Param("post") Post post);

    /**
     * 新增岗位表
     *
     * @param post 岗位表
     * @return 结果
     */
    int insertPost(@Param("post") Post post);

    /**
     * 修改岗位表
     *
     * @param post 岗位表
     * @return 结果
     */
    int updatePost(@Param("post") Post post);

    /**
     * 批量修改岗位表
     *
     * @param postList 岗位表
     * @return 结果
     */
    int updatePosts(@Param("postList") List<Post> postList);

    /**
     * 逻辑删除岗位表
     *
     * @param post
     * @return 结果
     */
    int logicDeletePostByPostId(@Param("post") Post post);

    /**
     * 逻辑批量删除岗位表
     *
     * @param postIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePostByPostIds(@Param("postIds") List<Long> postIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除岗位表
     *
     * @param postId 岗位表主键
     * @return 结果
     */
    int deletePostByPostId(@Param("postId") Long postId);

    /**
     * 物理批量删除岗位表
     *
     * @param postIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePostByPostIds(@Param("postIds") List<Long> postIds);

    /**
     * 批量新增岗位表
     *
     * @param Posts 岗位表列表
     * @return 结果
     */
    int batchPost(@Param("posts") List<Post> Posts);

    /**
     * 职级配置引用校验
     *
     * @param officialRankSystemIds
     * @return
     */
    List<PostDTO> selectPostByOfficialRankSystemIds(@Param("officialRankSystemIds") List<Long> officialRankSystemIds);

    /**
     * 根据部门查询岗位表列表
     * @param departmentId
     * @return
     */
    List<PostDTO> selectBydepartmentId(@Param("departmentId") Long departmentId);

    /**
     * 根据部门查询岗位表列表
     * @param officialRankSystemId 职级体系ID
     * @return
     */
    List<PostDTO> selectPostListByOfficialRank(@Param("officialRankSystemId") Long officialRankSystemId);

}
