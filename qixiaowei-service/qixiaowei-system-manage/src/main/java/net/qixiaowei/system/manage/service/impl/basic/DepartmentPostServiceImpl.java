package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.DepartmentPost;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import net.qixiaowei.system.manage.mapper.basic.DepartmentPostMapper;
import net.qixiaowei.system.manage.service.basic.IDepartmentPostService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
* DepartmentPostService业务层处理
* @author TANGMICHI
* @since 2022-09-27
*/
@Service
public class DepartmentPostServiceImpl implements IDepartmentPostService{
    @Autowired
    private DepartmentPostMapper departmentPostMapper;

    /**
    * 查询部门岗位关联表
    *
    * @param departmentPostId 部门岗位关联表主键
    * @return 部门岗位关联表
    */
    @Override
    public DepartmentPostDTO selectDepartmentPostByDepartmentPostId(Long departmentPostId)
    {
    return departmentPostMapper.selectDepartmentPostByDepartmentPostId(departmentPostId);
    }

    /**
    * 查询部门岗位关联表列表
    *
    * @param departmentPostDTO 部门岗位关联表
    * @return 部门岗位关联表
    */
    @Override
    public List<DepartmentPostDTO> selectDepartmentPostList(DepartmentPostDTO departmentPostDTO)
    {
    DepartmentPost departmentPost=new DepartmentPost();
    BeanUtils.copyProperties(departmentPostDTO,departmentPost);
    return departmentPostMapper.selectDepartmentPostList(departmentPost);
    }

    /**
    * 新增部门岗位关联表
    *
    * @param departmentPostDTO 部门岗位关联表
    * @return 结果
    */
    @Transactional
    @Override
    public int insertDepartmentPost(DepartmentPostDTO departmentPostDTO){
    DepartmentPost departmentPost=new DepartmentPost();
    BeanUtils.copyProperties(departmentPostDTO,departmentPost);
    departmentPost.setCreateBy(SecurityUtils.getUserId());
    departmentPost.setCreateTime(DateUtils.getNowDate());
    departmentPost.setUpdateTime(DateUtils.getNowDate());
    departmentPost.setUpdateBy(SecurityUtils.getUserId());
    departmentPost.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    return departmentPostMapper.insertDepartmentPost(departmentPost);
    }

    /**
    * 修改部门岗位关联表
    *
    * @param departmentPostDTO 部门岗位关联表
    * @return 结果
    */
    @Transactional
    @Override
    public int updateDepartmentPost(DepartmentPostDTO departmentPostDTO)
    {
    DepartmentPost departmentPost=new DepartmentPost();
    BeanUtils.copyProperties(departmentPostDTO,departmentPost);
    departmentPost.setUpdateTime(DateUtils.getNowDate());
    departmentPost.setUpdateBy(SecurityUtils.getUserId());
    return departmentPostMapper.updateDepartmentPost(departmentPost);
    }

    /**
    * 逻辑批量删除部门岗位关联表
    *
    * @param departmentPostDtos 需要删除的部门岗位关联表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int logicDeleteDepartmentPostByDepartmentPostIds(List<DepartmentPostDTO> departmentPostDtos){
            List<Long> stringList = new ArrayList();
            for (DepartmentPostDTO departmentPostDTO : departmentPostDtos) {
                stringList.add(departmentPostDTO.getDepartmentPostId());
            }
    return departmentPostMapper.logicDeleteDepartmentPostByDepartmentPostIds(stringList,departmentPostDtos.get(0).getUpdateBy(),DateUtils.getNowDate());
    }

    /**
    * 物理删除部门岗位关联表信息
    *
    * @param departmentPostId 部门岗位关联表主键
    * @return 结果
    */

    @Transactional
    @Override
    public int deleteDepartmentPostByDepartmentPostId(Long departmentPostId)
    {
    return departmentPostMapper.deleteDepartmentPostByDepartmentPostId(departmentPostId);
    }

     /**
     * 逻辑删除部门岗位关联表信息
     *
     * @param  departmentPostDTO 部门岗位关联表
     * @return 结果
     */
     @Transactional
     @Override
     public int logicDeleteDepartmentPostByDepartmentPostId(DepartmentPostDTO departmentPostDTO)
     {
     DepartmentPost departmentPost=new DepartmentPost();
     BeanUtils.copyProperties(departmentPostDTO,departmentPost);
     return departmentPostMapper.logicDeleteDepartmentPostByDepartmentPostId(departmentPost,SecurityUtils.getUserId(),DateUtils.getNowDate());
     }

     /**
     * 物理删除部门岗位关联表信息
     *
     * @param  departmentPostDTO 部门岗位关联表
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteDepartmentPostByDepartmentPostId(DepartmentPostDTO departmentPostDTO)
     {
     DepartmentPost departmentPost=new DepartmentPost();
     BeanUtils.copyProperties(departmentPostDTO,departmentPost);
     return departmentPostMapper.deleteDepartmentPostByDepartmentPostId(departmentPost.getDepartmentPostId());
     }
     /**
     * 物理批量删除部门岗位关联表
     *
     * @param departmentPostDtos 需要删除的部门岗位关联表主键
     * @return 结果
     */
     @Transactional
     @Override
     public int deleteDepartmentPostByDepartmentPostIds(List<DepartmentPostDTO> departmentPostDtos){
     List<Long> stringList = new ArrayList();
     for (DepartmentPostDTO departmentPostDTO : departmentPostDtos) {
     stringList.add(departmentPostDTO.getDepartmentPostId());
     }
     return departmentPostMapper.deleteDepartmentPostByDepartmentPostIds(stringList);
     }

    /**
    * 批量新增部门岗位关联表信息
    *
    * @param departmentPostDtos 部门岗位关联表对象
    */
    @Transactional
    public int insertDepartmentPosts(List<DepartmentPostDTO> departmentPostDtos){
      List<DepartmentPost> departmentPostList = new ArrayList();

    for (DepartmentPostDTO departmentPostDTO : departmentPostDtos) {
      DepartmentPost departmentPost =new DepartmentPost();
      BeanUtils.copyProperties(departmentPostDTO,departmentPost);
       departmentPost.setCreateBy(SecurityUtils.getUserId());
       departmentPost.setCreateTime(DateUtils.getNowDate());
       departmentPost.setUpdateTime(DateUtils.getNowDate());
       departmentPost.setUpdateBy(SecurityUtils.getUserId());
       departmentPost.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      departmentPostList.add(departmentPost);
    }
    return departmentPostMapper.batchDepartmentPost(departmentPostList);
    }

    /**
    * 批量修改部门岗位关联表信息
    *
    * @param departmentPostDtos 部门岗位关联表对象
    */
    @Transactional
    public int updateDepartmentPosts(List<DepartmentPostDTO> departmentPostDtos){
     List<DepartmentPost> departmentPostList = new ArrayList();

     for (DepartmentPostDTO departmentPostDTO : departmentPostDtos) {
     DepartmentPost departmentPost =new DepartmentPost();
     BeanUtils.copyProperties(departmentPostDTO,departmentPost);
        departmentPost.setCreateBy(SecurityUtils.getUserId());
        departmentPost.setCreateTime(DateUtils.getNowDate());
        departmentPost.setUpdateTime(DateUtils.getNowDate());
        departmentPost.setUpdateBy(SecurityUtils.getUserId());
     departmentPostList.add(departmentPost);
     }
     return departmentPostMapper.updateDepartmentPosts(departmentPostList);
    }
}

