package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.system.manage.api.domain.basic.Department;
import net.qixiaowei.system.manage.api.domain.basic.DepartmentPost;
import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.mapper.basic.DepartmentMapper;
import net.qixiaowei.system.manage.mapper.basic.DepartmentPostMapper;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Post;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.service.basic.IPostService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import org.springframework.util.CollectionUtils;


/**
 * PostService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
@Service
public class PostServiceImpl implements IPostService {
    @Autowired
    private PostMapper postMapper;

    @Autowired
    private DepartmentMapper departmentMapper;

    @Autowired
    private DepartmentPostMapper departmentPostMapper;

    @Autowired
    private EmployeeMapper employeeMapper;


    /**
     * 查询岗位表
     *
     * @param postId 岗位表主键
     * @return 岗位表
     */
    @Override
    public PostDTO selectPostByPostId(Long postId) {
        PostDTO postDTO = postMapper.selectPostByPostId(postId);
        //先查询中间表
        List<DepartmentPostDTO> departmentPostDTOS = departmentPostMapper.selectDepartmentPostId(postId);
        //组织id集合查询
        List<Long> collect = departmentPostDTOS.stream().map(DepartmentPostDTO::getDepartmentId).collect(Collectors.toList());
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(collect)){
            departmentDTOList = departmentMapper.selectDepartmentByDepartmentIds(collect);
        }

        postDTO.setDepartmentDTOList(departmentDTOList);
        return postDTO;
    }

    /**
     * 查询岗位表列表
     *
     * @param postDTO 岗位表
     * @return 岗位表
     */
    @Override
    public List<PostDTO> selectPostList(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
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
    public int insertPost(PostDTO postDTO) {
        int i = 0;
        //岗位表
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        //接收list
        List<DepartmentDTO> departmentDTOList = postDTO.getDepartmentDTOList();
        //去重
        List<DepartmentDTO> collect1 = departmentDTOList.stream().distinct().collect(Collectors.toList());
        //查询组织list
        List<DepartmentDTO> departmentDTOList1 = new ArrayList<>();
        //新增
        List<DepartmentPost> departmentPostAddList = new ArrayList<>();
        post.setCreateBy(SecurityUtils.getUserId());
        post.setCreateTime(DateUtils.getNowDate());
        post.setUpdateTime(DateUtils.getNowDate());
        post.setUpdateBy(SecurityUtils.getUserId());
        post.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            i = postMapper.insertPost(post);
        } catch (Exception e) {
            throw new ServiceException("新增岗位失败！");
        }
        if (!CollectionUtils.isEmpty(collect1)) {
            //拿到code编码批量查询
            List<String> collect = collect1.stream().map(DepartmentDTO::getDepartmentCode).collect(Collectors.toList());

            departmentDTOList1 = departmentMapper.selectDepartmentCodes(collect);
            if (CollectionUtils.isEmpty(departmentDTOList1)) {
                throw new ServiceException("组织信息不存在 无法新增！");
            }
        }
        //先查询 重复无法添加
        PostDTO postDTO1 = postMapper.selectPostCode(post.getPostCode());
        if (null == postDTO1) {
            throw new ServiceException("岗位编码已存在！");
        }
        for (DepartmentDTO departmentDTO : collect1) {
            //组织中间表
            DepartmentPost departmentPost = new DepartmentPost();
            //组织id
            departmentPost.setDepartmentId(departmentDTO.getDepartmentId());
            //组织排序 岗位放组织排序
            departmentPost.setDepartmentSort(departmentDTO.getSort());
            //岗位id
            departmentPost.setPostId(post.getPostId());
            departmentPost.setCreateBy(SecurityUtils.getUserId());
            departmentPost.setCreateTime(DateUtils.getNowDate());
            departmentPost.setUpdateBy(SecurityUtils.getUserId());
            departmentPost.setUpdateTime(DateUtils.getNowDate());
            departmentPost.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            departmentPostAddList.add(departmentPost);

        }

        if (!CollectionUtils.isEmpty(departmentPostAddList)) {
            try {
                departmentPostMapper.batchDepartmentPost(departmentPostAddList);
            } catch (Exception e) {
                throw new ServiceException("新增岗位组织信息失败！");
            }
        }
        return i;
    }

    /**
     * 修改岗位表
     *
     * @param postDTO 岗位表
     * @return 结果
     */
    @Transactional
    @Override
    public int updatePost(PostDTO postDTO) {
        int i = 0;
        //岗位表
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
        //接收list
        List<DepartmentDTO> departmentDTOList = postDTO.getDepartmentDTOList();
        //去重
        List<DepartmentDTO> collect3 = departmentDTOList.stream().distinct().collect(Collectors.toList());
        //查询组织list
        List<DepartmentDTO> departmentDTOList1 = new ArrayList<>();
        //新增
        List<DepartmentPost> departmentPostAddList = new ArrayList<>();
        //修改
        List<DepartmentPost> departmentUpdatePostList = new ArrayList<>();
        post.setUpdateTime(DateUtils.getNowDate());
        post.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = postMapper.updatePost(post);
        } catch (Exception e) {
            throw new ServiceException("新增岗位失败！");
        }
        if (!CollectionUtils.isEmpty(collect3)) {
            //拿到code编码批量查询
            List<String> collect = collect3.stream().map(DepartmentDTO::getDepartmentCode).collect(Collectors.toList());

            departmentDTOList1 = departmentMapper.selectDepartmentCodes(collect);
            if (CollectionUtils.isEmpty(departmentDTOList1)) {
                throw new ServiceException("组织信息不存在 无法新增！");
            }
        }
        //查询中间表
        List<DepartmentPostDTO> departmentPostDTOS1 = departmentPostMapper.selectDepartmentPostId(post.getPostId());

        //sterm流求差集
        List<Long> collect1 = departmentPostDTOS1.stream().filter(a ->
                !departmentDTOList.stream().map(DepartmentDTO::getDepartmentPostId).collect(Collectors.toList()).contains(a.getDepartmentPostId())
        ).collect(Collectors.toList()).stream().map(DepartmentPostDTO::getDepartmentPostId).collect(Collectors.toList());

        if (!CollectionUtils.isEmpty(collect1)) {
            try {
                departmentPostMapper.logicDeleteDepartmentPostByDepartmentPostIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除岗位组织信息失败");
            }
        }
        //是否包含
        List<Long> collect = departmentPostDTOS1.stream().map(DepartmentPostDTO::getDepartmentId).collect(Collectors.toList());
        for (DepartmentDTO departmentDTO : collect3) {
            //组织中间表
            DepartmentPost departmentPost = new DepartmentPost();
            //组织id
            departmentPost.setDepartmentId(departmentDTO.getDepartmentId());
            //组织排序 岗位放组织排序
            departmentPost.setDepartmentSort(departmentDTO.getSort());
            //岗位id
            departmentPost.setPostId(post.getPostId());
            if (collect.contains(departmentDTO.getDepartmentId())) {
                //修改id
                departmentPost.setDepartmentPostId(departmentDTO.getDepartmentPostId());
                departmentPost.setUpdateBy(SecurityUtils.getUserId());
                departmentPost.setUpdateTime(DateUtils.getNowDate());
                for (DepartmentPostDTO departmentPostDTO : departmentPostDTOS1) {
                    if (departmentPostDTO.getDepartmentId() == departmentDTO.getDepartmentId()) {
                        departmentPost.setDepartmentPostId(departmentPostDTO.getDepartmentPostId());
                    }
                }
                departmentUpdatePostList.add(departmentPost);
            } else {
                departmentPost.setCreateBy(SecurityUtils.getUserId());
                departmentPost.setCreateTime(DateUtils.getNowDate());
                departmentPost.setUpdateBy(SecurityUtils.getUserId());
                departmentPost.setUpdateTime(DateUtils.getNowDate());
                departmentPost.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                departmentPostAddList.add(departmentPost);
            }
        }

        if (!CollectionUtils.isEmpty(departmentPostAddList)) {
            try {
                departmentPostMapper.batchDepartmentPost(departmentPostAddList);
            } catch (Exception e) {
                throw new ServiceException("新增岗位组织信息失败！");
            }
        }
        if (!CollectionUtils.isEmpty(departmentUpdatePostList)) {
            try {
                departmentPostMapper.updateDepartmentPosts(departmentUpdatePostList);
            } catch (Exception e) {
                throw new ServiceException("修改岗位组织信息失败！");
            }
        }
        return i;
    }

    /**
     * 逻辑批量删除岗位表
     *
     * @param postIds 需要删除的岗位表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeletePostByPostIds(List<Long> postIds) {

        //人员引用
        StringBuffer employeeErreo = new StringBuffer();
        //组织引用
        StringBuffer deptPostErreo = new StringBuffer();
        //错误信息
        StringBuffer postErreo = new StringBuffer();
        for (Long postId : postIds) {
            //查询是否被人员引用
            List<EmployeeDTO> employeeDTOS = employeeMapper.selectEmployeePostId(postId);
            if (!CollectionUtils.isEmpty(employeeDTOS)) {
                for (EmployeeDTO employeeDTO : employeeDTOS) {
                    employeeErreo.append("该岗位" + "已被" + employeeDTO.getEmployeeName() + "人员引用" + "\n");
                }
            }
            //查询是否被组织引用
            List<DepartmentPostDTO> departmentPostDTOS = departmentPostMapper.selectDepartmentPostId(postId);
            if (!CollectionUtils.isEmpty(departmentPostDTOS)) {
                for (DepartmentPostDTO departmentPostDTO : departmentPostDTOS) {
                    deptPostErreo.append("该岗位" + "已被组织引用" + "\n");
                }
            }
        }
        // todo 还有暂未确定
        if (employeeErreo.length() > 1) {
            postErreo.append(employeeErreo).append("\n");
        }
        if (deptPostErreo.length() > 1) {
            postErreo.append(deptPostErreo).append("\n");
        }
        if (postErreo.length() > 1) {
            postErreo.append("已上数据已被引用 无法删除！");
            throw new ServiceException(postErreo.toString());
        }

        return postMapper.logicDeletePostByPostIds(postIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除岗位表信息
     *
     * @param postId 岗位表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deletePostByPostId(Long postId) {
        return postMapper.deletePostByPostId(postId);
    }

    /**
     * 逻辑删除岗位表信息
     *
     * @param postDTO 岗位表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeletePostByPostId(PostDTO postDTO) {
        Post post = new Post();
        post.setPostId(postDTO.getPostId());
        post.setUpdateTime(DateUtils.getNowDate());
        post.setUpdateBy(SecurityUtils.getUserId());
        //人员引用
        StringBuffer employeeErreo = new StringBuffer();
        //组织引用
        StringBuffer deptPostErreo = new StringBuffer();
        //错误信息
        StringBuffer postErreo = new StringBuffer();
        //查询是否被人员引用
        List<EmployeeDTO> employeeDTOS = employeeMapper.selectEmployeePostId(post.getPostId());
        String s = employeeDTOS.stream().map(EmployeeDTO::getEmployeeName).collect(Collectors.toList()).toString();
        if (!CollectionUtils.isEmpty(employeeDTOS)) {
                employeeErreo.append("岗位被"+s+"人员引用  无法删除！\n");
        }
        //查询是否被组织引用
        List<DepartmentPostDTO> departmentPostDTOS = departmentPostMapper.selectDepartmentPostId(post.getPostId());
        List<Long> collect = departmentPostDTOS.stream().map(DepartmentPostDTO::getDepartmentId).collect(Collectors.toList());
        List<DepartmentDTO> departmentDTOList = departmentMapper.selectDepartmentByDepartmentIds(collect);
        String s1 = departmentDTOList.stream().map(DepartmentDTO::getDepartmentName).collect(Collectors.toList()).toString();
        if (!CollectionUtils.isEmpty(departmentPostDTOS)) {
                deptPostErreo.append("岗位被"+s1+"组织引用 无法删除！\n");

        }
        // todo 还有暂未确定
        if (employeeErreo.length() > 1) {
            postErreo.append(employeeErreo).append("\n");
        }
        if (deptPostErreo.length() > 1) {
            postErreo.append(deptPostErreo).append("\n");
        }
        if (postErreo.length() > 1) {
            throw new ServiceException(postErreo.toString());
        }
        return postMapper.logicDeletePostByPostId(post);
    }

    /**
     * 物理删除岗位表信息
     *
     * @param postDTO 岗位表
     * @return 结果
     */
    @Transactional
    @Override
    public int deletePostByPostId(PostDTO postDTO) {
        Post post = new Post();
        BeanUtils.copyProperties(postDTO, post);
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
    public int deletePostByPostIds(List<PostDTO> postDtos) {
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
    public int insertPosts(List<PostDTO> postDtos) {
        List<Post> postList = new ArrayList();

        for (PostDTO postDTO : postDtos) {
            Post post = new Post();
            BeanUtils.copyProperties(postDTO, post);
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
    public int updatePosts(List<PostDTO> postDtos) {
        List<Post> postList = new ArrayList();

        for (PostDTO postDTO : postDtos) {
            Post post = new Post();
            BeanUtils.copyProperties(postDTO, post);
            post.setCreateBy(SecurityUtils.getUserId());
            post.setCreateTime(DateUtils.getNowDate());
            post.setUpdateTime(DateUtils.getNowDate());
            post.setUpdateBy(SecurityUtils.getUserId());
            postList.add(post);
        }
        return postMapper.updatePosts(postList);
    }
}

