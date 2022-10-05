package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Post;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.service.basic.IPostService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* PostService业务层处理
* @author TANGMICHI
* @since 2022-09-30
*/
@Service
public class PostServiceImpl implements IPostService{
    @Autowired
    private PostMapper postMapper;

    /**
    * 查询岗位表
    *
    * @param postId 岗位表主键
    * @return 岗位表
    */
    @Override
    public PostDTO selectPostByPostId(Long postId)
    {
    return postMapper.selectPostByPostId(postId);
    }

    /**
    * 查询岗位表列表
    *
    * @param postDTO 岗位表
    * @return 岗位表
    */
    @Override
    public List<PostDTO> selectPostList(PostDTO postDTO)
    {
    Post post=new Post();
    BeanUtils.copyProperties(postDTO,post);
    return postMapper.selectPostList(post);
    }

    /**
    * 新增岗位表
    *
    * @param postDTO 岗位表
    * @return 结果
    */
    @Transactional
    @Override
    public int insertPost(PostDTO postDTO){
    Post post=new Post();
    BeanUtils.copyProperties(postDTO,post);
    post.setCreateBy(SecurityUtils.getUserId());
    post.setCreateTime(DateUtils.getNowDate());
    post.setUpdateTime(DateUtils.getNowDate());
    post.setUpdateBy(SecurityUtils.getUserId());
    post.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return postMapper.insertPost(post);
    }

    /**
    * 修改岗位表
    *
    * @param postDTO 岗位表
    * @return 结果
    */
    @Transactional
    @Override
    public int updatePost(PostDTO postDTO)
    {
    Post post=new Post();
    BeanUtils.copyProperties(postDTO,post);
    post.setUpdateTime(DateUtils.getNowDate());
    post.setUpdateBy(SecurityUtils.getUserId());
    return postMapper.updatePost(post);
    }

    /**
    * 逻辑批量删除岗位表
    *
    * @param postDtos 需要删除的岗位表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int logicDeletePostByPostIds(List<PostDTO> postDtos){
            List<Long> stringList = new ArrayList();
            for (PostDTO postDTO : postDtos) {
                stringList.add(postDTO.getPostId());
            }
    return postMapper.logicDeletePostByPostIds(stringList,postDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除岗位表信息
    *
    * @param postId 岗位表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deletePostByPostId(Long postId)
    {
    return postMapper.deletePostByPostId(postId);
    }

     /**
     * 逻辑删除岗位表信息
     *
     * @param  postDTO 岗位表
     * @return 结果
     */
     @Transactional
     @Override
     public int logicDeletePostByPostId(PostDTO postDTO)
     {
     Post post=new Post();
     BeanUtils.copyProperties(postDTO,post);
     return postMapper.logicDeletePostByPostId(post,SecurityUtils.getUserId(),DateUtils.getNowDate());
     }

     /**
     * 物理删除岗位表信息
     *
     * @param  postDTO 岗位表
     * @return 结果
     */
     @Transactional
     @Override
     public int deletePostByPostId(PostDTO postDTO)
     {
     Post post=new Post();
     BeanUtils.copyProperties(postDTO,post);
     return postMapper.deletePostByPostId(post.getPostId());
     }
     /**
     * 物理批量删除岗位表
     *
     * @param postDtos 需要删除的岗位表主键
     * @return 结果
     */
     @Transactional
     @Override
     public int deletePostByPostIds(List<PostDTO> postDtos){
     List<Long> stringList = new ArrayList();
     for (PostDTO postDTO : postDtos) {
     stringList.add(postDTO.getPostId());
     }
     return postMapper.deletePostByPostIds(stringList);
     }

    /**
    * 批量新增岗位表信息
    *
    * @param postDtos 岗位表对象
    */
    @Transactional
    public int insertPosts(List<PostDTO> postDtos){
      List<Post> postList = new ArrayList();

    for (PostDTO postDTO : postDtos) {
      Post post =new Post();
      BeanUtils.copyProperties(postDTO,post);
       post.setCreateBy(SecurityUtils.getUserId());
       post.setCreateTime(DateUtils.getNowDate());
       post.setUpdateTime(DateUtils.getNowDate());
       post.setUpdateBy(SecurityUtils.getUserId());
       post.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      postList.add(post);
    }
    return postMapper.batchPost(postList);
    }

    /**
    * 批量修改岗位表信息
    *
    * @param postDtos 岗位表对象
    */
    @Transactional
    public int updatePosts(List<PostDTO> postDtos){
     List<Post> postList = new ArrayList();

     for (PostDTO postDTO : postDtos) {
     Post post =new Post();
     BeanUtils.copyProperties(postDTO,post);
        post.setCreateBy(SecurityUtils.getUserId());
        post.setCreateTime(DateUtils.getNowDate());
        post.setUpdateTime(DateUtils.getNowDate());
        post.setUpdateBy(SecurityUtils.getUserId());
     postList.add(post);
     }
     return postMapper.updatePosts(postList);
    }
}

