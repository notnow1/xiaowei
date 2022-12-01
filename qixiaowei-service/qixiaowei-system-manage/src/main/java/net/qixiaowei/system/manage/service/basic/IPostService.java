package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import org.apache.ibatis.annotations.Param;


/**
* PostService接口
* @author TANGMICHI
* @since 2022-09-30
*/
public interface IPostService{
    /**
    * 查询岗位表
    *
    * @param postId 岗位表主键
    * @return 岗位表
    */
    PostDTO selectPostByPostId(Long postId);

    /**
    * 查询岗位表列表
    *
    * @param postDTO 岗位表
    * @return 岗位表集合
    */
    List<PostDTO> selectPostList(PostDTO postDTO);

    /**
    * 新增岗位表
    *
    * @param postDTO 岗位表
    * @return 结果
    */
    PostDTO insertPost(PostDTO postDTO);

    /**
    * 修改岗位表
    *
    * @param postDTO 岗位表
    * @return 结果
    */
    int updatePost(PostDTO postDTO);

    /**
    * 批量修改岗位表
    *
    * @param postDtos 岗位表
    * @return 结果
    */
    int updatePosts(List<PostDTO> postDtos);

    /**
    * 批量新增岗位表
    *
    * @param postDtos 岗位表
    * @return 结果
    */
    int insertPosts(List<PostDTO> postDtos);

    /**
    * 逻辑批量删除岗位表
    *
    * @param postIds 需要删除的岗位表集合
    * @return 结果
    */
    int logicDeletePostByPostIds(List<Long>  postIds);

    /**
    * 逻辑删除岗位表信息
    *
    * @param postDTO
    * @return 结果
    */
    int logicDeletePostByPostId(PostDTO postDTO);
    /**
    * 逻辑批量删除岗位表
    *
    * @param PostDtos 需要删除的岗位表集合
    * @return 结果
    */
    int deletePostByPostIds(List<PostDTO> PostDtos);

    /**
    * 逻辑删除岗位表信息
    *
    * @param postDTO
    * @return 结果
    */
    int deletePostByPostId(PostDTO postDTO);


    /**
    * 删除岗位表信息
    *
    * @param postId 岗位表主键
    * @return 结果
    */
    int deletePostByPostId(Long postId);

    /**
     * 根据部门查询岗位表列表
     * @param departmentId
     * @return
     */
    List<PostDTO> selectBydepartmentId(Long departmentId);

    /**
     * 根据部门查询岗位表列表
     * @param officialRankSystemId 职级体系ID
     * @return List
     */
    List<PostDTO> selectPostListByOfficialRank(@Param("officialRankSystemId") Long officialRankSystemId);
}
