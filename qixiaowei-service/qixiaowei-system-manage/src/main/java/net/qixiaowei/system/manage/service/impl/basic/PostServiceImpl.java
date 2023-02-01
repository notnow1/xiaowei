package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import net.qixiaowei.system.manage.api.domain.basic.DepartmentPost;
import net.qixiaowei.system.manage.api.domain.basic.Post;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.mapper.basic.DepartmentMapper;
import net.qixiaowei.system.manage.mapper.basic.DepartmentPostMapper;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.service.basic.IPostService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
    @Autowired
    private RemoteSalaryAdjustPlanService remoteSalaryAdjustPlanService;

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
        List<DepartmentDTO> departmentDTOList = departmentPostMapper.selectDepartmentPostId(postId);
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
     * 生成岗位编码
     *
     * @return 岗位编码
     */
    @Override
    public String generatePostCode() {
        String postCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.POST.getCode();
        List<String> postCodes = postMapper.getPostCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(postCodes)) {
            for (String code : postCodes) {
                if (StringUtils.isEmpty(code) || code.length() != 5) {
                    continue;
                }
                code = code.replaceFirst(prefixCodeRule, "");
                try {
                    int codeOfNumber = Integer.parseInt(code);
                    if (number != codeOfNumber) {
                        break;
                    }
                    number++;
                } catch (Exception ignored) {
                }
            }
        }
        if (number > 1000) {
            throw new ServiceException("流水号溢出，请联系管理员");
        }
        postCode = "000" + number;
        postCode = prefixCodeRule + postCode.substring(postCode.length() - 3);
        return postCode;
    }

    /**
     * 新增岗位表
     *
     * @param postDTO 岗位表
     * @return 结果
     */
    @Transactional
    @Override
    public PostDTO insertPost(PostDTO postDTO) {
        //先查询 重复无法添加
        PostDTO postDTO1 = postMapper.selectPostCode(postDTO.getPostCode());
        if (StringUtils.isNotNull(postDTO1)) {
            throw new ServiceException("岗位编码已存在！");
        }
        //先查询 重复无法添加
        PostDTO postDTO2 = postMapper.selectPostName(postDTO.getPostName());
        if (StringUtils.isNotNull(postDTO2)) {
            throw new ServiceException("岗位名称已存在！");
        }
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
            postMapper.insertPost(post);
        } catch (Exception e) {
            throw new ServiceException("新增岗位失败！");
        }
        if (!StringUtils.isEmpty(collect1)) {
            //拿到code编码批量查询
            List<String> collect = collect1.stream().map(DepartmentDTO::getDepartmentCode).collect(Collectors.toList());

            departmentDTOList1 = departmentMapper.selectDepartmentCodes(collect);
            if (StringUtils.isEmpty(departmentDTOList1)) {
                throw new ServiceException("组织信息不存在 无法新增！");
            }
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

        if (!StringUtils.isEmpty(departmentPostAddList)) {
            try {
                departmentPostMapper.batchDepartmentPost(departmentPostAddList);
            } catch (Exception e) {
                throw new ServiceException("新增岗位组织信息失败！");
            }
        }
        postDTO.setPostId(post.getPostId());
        return postDTO;
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
        if (!StringUtils.isEmpty(collect3)) {
            //拿到code编码批量查询
            List<String> collect = collect3.stream().map(DepartmentDTO::getDepartmentCode).collect(Collectors.toList());

            departmentDTOList1 = departmentMapper.selectDepartmentCodes(collect);
            if (StringUtils.isEmpty(departmentDTOList1)) {
                throw new ServiceException("组织信息不存在 无法新增！");
            }
        }
        //查询中间表
        List<DepartmentDTO> departmentPostDTOS1 = departmentPostMapper.selectDepartmentPostId(post.getPostId());

        //sterm流求差集
        List<Long> collect1 = departmentPostDTOS1.stream().filter(a ->
                !departmentDTOList.stream().map(DepartmentDTO::getDepartmentPostId).collect(Collectors.toList()).contains(a.getDepartmentPostId())
        ).collect(Collectors.toList()).stream().map(DepartmentDTO::getDepartmentPostId).collect(Collectors.toList());

        if (!StringUtils.isEmpty(collect1)) {
            try {
                departmentPostMapper.logicDeleteDepartmentPostByDepartmentPostIds(collect1, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除岗位组织信息失败");
            }
        }
        //是否包含
        List<Long> collect = departmentPostDTOS1.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
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
                for (DepartmentDTO departmentDTO1 : departmentPostDTOS1) {
                    if (departmentDTO1.getDepartmentId().equals(departmentDTO.getDepartmentId())) {
                        departmentPost.setDepartmentPostId(departmentDTO1.getDepartmentPostId());
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

        if (!StringUtils.isEmpty(departmentPostAddList)) {
            try {
                departmentPostMapper.batchDepartmentPost(departmentPostAddList);
            } catch (Exception e) {
                throw new ServiceException("新增岗位组织信息失败！");
            }
        }
        if (!StringUtils.isEmpty(departmentUpdatePostList)) {
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
        // 个人调薪引用
        StringBuffer empSalaryAdjustPlanErreo = new StringBuffer();
        //错误信息
        StringBuffer postErreo = new StringBuffer();
        for (Long postId : postIds) {
            //引用关系
            this.quotePost(postId, employeeErreo, deptPostErreo,empSalaryAdjustPlanErreo);
        }
        postErreo.append(employeeErreo).append(deptPostErreo).append(empSalaryAdjustPlanErreo);
        if (postErreo.length() > 1) {
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
     * 根据部门查询岗位表列表
     *
     * @param departmentId
     * @return
     */
    @Override
    public List<PostDTO> selectBydepartmentId(Long departmentId) {
        return postMapper.selectBydepartmentId(departmentId);
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
        // 个人调薪应用
        StringBuffer empSalaryAdjustPlanErreo = new StringBuffer();
        //错误信息
        StringBuffer postErreo = new StringBuffer();
        //引用关系
        this.quotePost(post.getPostId(), employeeErreo, deptPostErreo, empSalaryAdjustPlanErreo);
        postErreo.append(employeeErreo).append(deptPostErreo).append(empSalaryAdjustPlanErreo);

        if (postErreo.length() > 1) {
            throw new ServiceException(postErreo.toString());
        }
        return postMapper.logicDeletePostByPostId(post);
    }

    /**
     * 删除引用关系
     *  @param postId
     * @param employeeErreo
     * @param deptPostErreo
     * @param empSalaryAdjustPlanErreo
     */
    private void quotePost(Long postId, StringBuffer employeeErreo, StringBuffer deptPostErreo, StringBuffer empSalaryAdjustPlanErreo) {
        //查询是否被人员引用
        List<EmployeeDTO> employeeDTOS = employeeMapper.selectEmployeePostId(postId);
        List<String> employeeNames = employeeDTOS.stream().map(EmployeeDTO::getEmployeeName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        List<String> employeeDepartmentNames = employeeDTOS.stream().map(EmployeeDTO::getEmployeeDepartmentName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        String postname1 = employeeDTOS.stream().map(EmployeeDTO::getEmployeePostName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        if (!StringUtils.isEmpty(employeeNames) && employeeNames.get(0) != null) {
            employeeErreo.append("岗位" + postname1 + "被" + StringUtils.join(",",employeeNames) + "人员引用  无法删除！\n");
        }
        if (!StringUtils.isEmpty(employeeDepartmentNames) && employeeDepartmentNames.get(0) != null) {
            employeeErreo.append("岗位" + postname1 + "被" + StringUtils.join(",",employeeDepartmentNames) + "组织 负责人岗位引用  无法删除！\n");
        }
        //查询是否被组织引用
        List<DepartmentDTO> departmentDTOList = departmentPostMapper.selectDepartmentPostId(postId);
        List<String> departmentNames = departmentDTOList.stream().map(DepartmentDTO::getDepartmentName).filter(Objects::nonNull).collect(Collectors.toList());
        String postname2 = departmentDTOList.stream().map(DepartmentDTO::getDepartmentLeaderPostName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        if (!StringUtils.isEmpty(departmentDTOList) && departmentDTOList.get(0) != null) {
            deptPostErreo.append("岗位" + postname2 + "被" + StringUtils.join(",",departmentNames) + "组织 组织岗位信息-岗位名称引用 无法删除！\n");
        }
        //远程查询个人调薪 岗位引用
        R<List<EmpSalaryAdjustPlanDTO>> empSalaryAdjustPlanList = remoteSalaryAdjustPlanService.selectByPostId(postId, SecurityConstants.INNER);
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanData = empSalaryAdjustPlanList.getData();
        if (StringUtils.isNotEmpty(empSalaryAdjustPlanData) && empSalaryAdjustPlanData.get(0) != null ){
            empSalaryAdjustPlanErreo.append("岗位" + postname2 + "被[" + StringUtils.join(",",empSalaryAdjustPlanData.stream().map(EmpSalaryAdjustPlanDTO::getEmployeeName).filter(Objects::nonNull).collect(Collectors.toList())) + "]个人调薪引用 无法删除！\n");
        }
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
    @Override
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
    @Override
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

    /**
     * 根据部门查询岗位表列表
     *
     * @param officialRankSystemId 职级体系ID
     * @return List
     */
    @Override
    public List<PostDTO> selectPostListByOfficialRank(@Param("officialRankSystemId") Long officialRankSystemId) {
        return postMapper.selectPostListByOfficialRank(officialRankSystemId);
    }
}

