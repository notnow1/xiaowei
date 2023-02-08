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
import net.qixiaowei.operate.cloud.api.dto.targetManager.DecomposeDetailCyclesDTO;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
import net.qixiaowei.system.manage.api.domain.basic.Department;
import net.qixiaowei.system.manage.api.domain.basic.DepartmentPost;
import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.domain.basic.Post;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.excel.basic.EmployeeExcel;
import net.qixiaowei.system.manage.excel.post.PostExcel;
import net.qixiaowei.system.manage.mapper.basic.DepartmentMapper;
import net.qixiaowei.system.manage.mapper.basic.DepartmentPostMapper;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankSystemService;
import net.qixiaowei.system.manage.service.basic.IPostService;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;


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
    @Autowired
    private IDepartmentService departmentService;
    @Autowired
    private IOfficialRankSystemService officialRankSystemService;

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
            this.quotePost(postId, employeeErreo, deptPostErreo, empSalaryAdjustPlanErreo);
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
     *
     * @param postId
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
            employeeErreo.append("岗位" + postname1 + "被" + StringUtils.join(",", employeeNames) + "人员引用  无法删除！\n");
        }
        if (!StringUtils.isEmpty(employeeDepartmentNames) && employeeDepartmentNames.get(0) != null) {
            employeeErreo.append("岗位" + postname1 + "被" + StringUtils.join(",", employeeDepartmentNames) + "组织 负责人岗位引用  无法删除！\n");
        }
        //查询是否被组织引用
        List<DepartmentDTO> departmentDTOList = departmentPostMapper.selectDepartmentPostId(postId);
        List<String> departmentNames = departmentDTOList.stream().map(DepartmentDTO::getDepartmentName).filter(Objects::nonNull).collect(Collectors.toList());
        String postname2 = departmentDTOList.stream().map(DepartmentDTO::getDepartmentLeaderPostName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        if (!StringUtils.isEmpty(departmentDTOList) && departmentDTOList.get(0) != null) {
            deptPostErreo.append("岗位" + postname2 + "被" + StringUtils.join(",", departmentNames) + "组织 组织岗位信息-岗位名称引用 无法删除！\n");
        }
        //远程查询个人调薪 岗位引用
        R<List<EmpSalaryAdjustPlanDTO>> empSalaryAdjustPlanList = remoteSalaryAdjustPlanService.selectByPostId(postId, SecurityConstants.INNER);
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanData = empSalaryAdjustPlanList.getData();
        if (StringUtils.isNotEmpty(empSalaryAdjustPlanData) && empSalaryAdjustPlanData.get(0) != null) {
            empSalaryAdjustPlanErreo.append("岗位" + postname2 + "被[" + StringUtils.join(",", empSalaryAdjustPlanData.stream().map(EmpSalaryAdjustPlanDTO::getEmployeeName).filter(Objects::nonNull).collect(Collectors.toList())) + "]个人调薪引用 无法删除！\n");
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

    /**
     * 岗位导入Excel
     *
     * @param postExcelList
     */
    @Override
    @Transactional
    public void importPost(List<PostExcel> postExcelList) {
        //查询岗位已有的数据
        List<PostDTO> postDTOS = postMapper.selectPostList(new Post());
        //查询部门已有数据
        List<DepartmentDTO> departmentDTOList = departmentService.selectDepartmentListName(new Department());
        //职级体系集合
        List<OfficialRankSystemDTO> officialRankSystemDTOS = officialRankSystemService.selectOfficialRankSystemList(new OfficialRankSystemDTO());
        //岗位名称集合
        List<String> postNames = postDTOS.stream().map(PostDTO::getPostName).collect(Collectors.toList());
        //岗位编码集合
        List<String> postCodes = postDTOS.stream().map(PostDTO::getPostCode).collect(Collectors.toList());
        //新增list
        List<Post> successExcelList = new ArrayList<>();
        //失败List
        List<PostExcel> errorExcelList = new ArrayList<>();

        //返回报错信息
        StringBuffer postError = new StringBuffer();
        if (StringUtils.isNotEmpty(postExcelList)) {
            Map<String, List<PostExcel>> postExcelMap = postExcelList.parallelStream().collect(Collectors.groupingBy(PostExcel::getPostCode));
            for (String key : postExcelMap.keySet()) {
                Long postId = null;
                //新增关联表
                List<DepartmentPost> departmentPostAddList = new ArrayList<>();
                List<PostExcel> postExcels = postExcelMap.get(key);
                if (StringUtils.isNotEmpty(postExcels)) {

                    ArrayList<PostExcel> collect = postExcels.stream().collect(Collectors.collectingAndThen(
                            Collectors.toCollection(() -> new TreeSet<>(
                                    Comparator.comparing(
                                            PostExcel::getParentDepartmentExcelName))), ArrayList::new));

                    //去重
                    List<PostExcel> postExcelDistinct = postExcels.stream().distinct().collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(postExcelDistinct)) {
                        postExcels.removeAll(postExcelDistinct);
                        errorExcelList.addAll(postExcels);

                        for (int i = 0; i < postExcelDistinct.size(); i++) {
                            DepartmentPost departmentPost = new DepartmentPost();
                            Post post = new Post();
                            StringBuffer stringBuffer = new StringBuffer();
                            //职级体系名称
                            String officialRankSystemName = postExcelDistinct.get(i).getOfficialRankSystemName();
                            //岗位职级下限
                            String postRankLowerName = postExcelDistinct.get(i).getPostRankLowerName();
                            //岗位职级上限
                            String postRankUpperName = postExcelDistinct.get(i).getPostRankUpperName();
                            //状态:失效;生效
                            String postStatus = postExcelDistinct.get(i).getStatus();
                            //适用组织 部门名称(excel用)
                            String parentDepartmentExcelName = postExcelDistinct.get(i).getParentDepartmentExcelName();
                            if (postExcelDistinct.size() == 1) {
                                this.onlyOne(officialRankSystemDTOS, postNames, postCodes, postExcelDistinct, i, post, stringBuffer, officialRankSystemName, postRankLowerName, postRankUpperName, postStatus);
                                if (stringBuffer.length() > 1) {
                                    postError.append(stringBuffer);
                                    errorExcelList.addAll(postExcelDistinct);
                                    break;
                                }
                                if (StringUtils.isNotEmpty(departmentDTOList)) {
                                    List<DepartmentDTO> departmentDTO = departmentDTOList.stream().filter(f -> StringUtils.equals(f.getParentDepartmentExcelName(), parentDepartmentExcelName)).collect(Collectors.toList());
                                    if (StringUtils.isNotEmpty(departmentDTO)) {
                                        //组织id
                                        departmentPost.setDepartmentId(departmentDTO.get(0).getDepartmentId());
                                        //组织排序
                                        departmentPost.setDepartmentSort(1);
                                        departmentPost.setCreateBy(SecurityUtils.getUserId());
                                        departmentPost.setCreateTime(DateUtils.getNowDate());
                                        departmentPost.setUpdateBy(SecurityUtils.getUserId());
                                        departmentPost.setUpdateTime(DateUtils.getNowDate());
                                        departmentPost.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);

                                    }
                                } else {
                                    throw new ServiceException("适用组织不存在! 请先配置组织数据");
                                }
                                post.setCreateBy(SecurityUtils.getUserId());
                                post.setCreateTime(DateUtils.getNowDate());
                                post.setUpdateBy(SecurityUtils.getUserId());
                                post.setUpdateTime(DateUtils.getNowDate());
                                post.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                                try {
                                    postMapper.insertPost(post);
                                } catch (Exception e) {
                                    throw new ServiceException("插入岗位表失败！");
                                }
                                departmentPost.setPostId(post.getPostId());
                                try {
                                    departmentPostMapper.insertDepartmentPost(departmentPost);
                                } catch (Exception e) {
                                    throw new ServiceException("插入岗位关联表失败！");
                                }
                                successExcelList.add(post);

                            } else {
                                this.onlyOne(officialRankSystemDTOS, postNames, postCodes, postExcelDistinct, i, post, stringBuffer, officialRankSystemName, postRankLowerName, postRankUpperName, postStatus);
                                if (stringBuffer.length() > 1) {
                                    postError.append(stringBuffer);
                                    errorExcelList.addAll(postExcelDistinct);
                                    break;
                                }
                                if (i == 0) {
                                    post.setCreateBy(SecurityUtils.getUserId());
                                    post.setCreateTime(DateUtils.getNowDate());
                                    post.setUpdateBy(SecurityUtils.getUserId());
                                    post.setUpdateTime(DateUtils.getNowDate());
                                    post.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                                    try {
                                        postMapper.insertPost(post);
                                    } catch (Exception e) {
                                        throw new ServiceException("插入岗位表失败！");
                                    }
                                    postId = post.getPostId();
                                }

                                if (StringUtils.isNotEmpty(departmentDTOList)) {
                                    List<DepartmentDTO> departmentDTO = departmentDTOList.stream().filter(f -> StringUtils.equals(f.getParentDepartmentExcelName(), parentDepartmentExcelName)).collect(Collectors.toList());
                                    if (StringUtils.isNotEmpty(departmentDTO)) {
                                        //组织id
                                        departmentPost.setDepartmentId(departmentDTO.get(0).getDepartmentId());
                                        //组织排序
                                        departmentPost.setDepartmentSort(i + 1);
                                        departmentPost.setPostId(postId);
                                        departmentPost.setCreateBy(SecurityUtils.getUserId());
                                        departmentPost.setCreateTime(DateUtils.getNowDate());
                                        departmentPost.setUpdateBy(SecurityUtils.getUserId());
                                        departmentPost.setUpdateTime(DateUtils.getNowDate());
                                        departmentPost.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                                        departmentPostAddList.add(departmentPost);
                                    }
                                } else {
                                    throw new ServiceException("适用组织不存在! 请先配置组织数据");
                                }
                                if (i == (postExcelDistinct.size() - 1)) {
                                    if (StringUtils.isNotEmpty(departmentPostAddList)) {
                                        try {
                                            departmentPostMapper.batchDepartmentPost(departmentPostAddList);
                                        } catch (Exception e) {
                                            throw new ServiceException("插入岗位表失败！");
                                        }
                                    }
                                }
                            }
                        }
                    }

                }
            }
        }
        if (postError.length() > 1) {
            throw new ServiceException(postError.toString());
        }
    }

    /**
     * 先插入岗位主表 再插入关联表
     *
     * @param officialRankSystemDTOS
     * @param postNames
     * @param postCodes
     * @param postExcelDistinct
     * @param i
     * @param post
     * @param stringBuffer
     * @param officialRankSystemName
     * @param postRankLowerName
     * @param postRankUpperName
     * @param postStatus
     */
    private void onlyOne(List<OfficialRankSystemDTO> officialRankSystemDTOS, List<String> postNames, List<String> postCodes, List<PostExcel> postExcelDistinct, int i, Post post, StringBuffer stringBuffer, String officialRankSystemName, String postRankLowerName, String postRankUpperName, String postStatus) {
        this.validPost(postNames, postCodes, stringBuffer, postExcelDistinct.get(i));
        BeanUtils.copyProperties(postExcelDistinct.get(i), post);
        if (StringUtils.isNotNull(officialRankSystemName)) {
            List<OfficialRankSystemDTO> OfficialRankSystemDTO = officialRankSystemDTOS.stream().filter(f -> StringUtils.equals(f.getOfficialRankSystemName(), officialRankSystemName)).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(OfficialRankSystemDTO)) {
                post.setOfficialRankSystemId(OfficialRankSystemDTO.get(0).getOfficialRankSystemId());
                if (StringUtils.isNotNull(postRankLowerName)) {
                    if (OfficialRankSystemDTO.get(0).getRankStart() > Integer.parseInt(postRankLowerName) || OfficialRankSystemDTO.get(0).getRankEnd() < Integer.parseInt(postRankLowerName)) {
                        stringBuffer.append("岗位职级下限不在" + officialRankSystemName + "职级体系的职级范围区间！");
                    } else {
                        post.setPostRankLower(Integer.parseInt(postRankLowerName));
                    }
                }


                if (StringUtils.isNotNull(postRankUpperName)) {
                    if (OfficialRankSystemDTO.get(0).getRankStart() > Integer.parseInt(postRankUpperName) || OfficialRankSystemDTO.get(0).getRankEnd() < Integer.parseInt(postRankUpperName)) {
                        stringBuffer.append("岗位职级上限不在" + officialRankSystemName + "职级体系的职级范围区间！");
                    } else {
                        post.setPostRank(Integer.parseInt(postRankUpperName));
                        post.setPostRankUpper(Integer.parseInt(postRankUpperName));
                    }
                }
            }
        }
        //岗位状态
        if (StringUtils.equals(postStatus, "生效")) {
            post.setStatus(0);
        } else if (StringUtils.equals(postStatus, "失效")) {
            post.setStatus(1);
        } else {
            post.setStatus(0);
        }
    }

    /**
     * 检验数据
     *
     * @param postNames
     * @param postCodes
     * @param stringBuffer
     * @param postExcel
     */
    private void validPost(List<String> postNames, List<String> postCodes, StringBuffer stringBuffer, PostExcel postExcel) {
        //岗位名称
        String postName = postExcel.getPostName();
        //岗位编码
        String postCode = postExcel.getPostCode();
        //职级体系名称
        String officialRankSystemName = postExcel.getOfficialRankSystemName();
        //岗位职级下限
        String postRankLowerName = postExcel.getPostRankLowerName();
        //岗位职级上限
        String postRankUpperName = postExcel.getPostRankUpperName();
        //适用组织 部门名称(excel用)
        String parentDepartmentExcelName = postExcel.getParentDepartmentExcelName();
        try {
            Integer.parseInt(postRankLowerName);
        } catch (NumberFormatException e) {
            stringBuffer.append("岗位职级下限岗位必须为数字类型！");
        }
        try {
            Integer.parseInt(postRankUpperName);
        } catch (NumberFormatException e) {
            stringBuffer.append("岗位职级上限岗位必须为数字类型！");
        }

        if (StringUtils.isNull(postName)) {
            stringBuffer.append("岗位名称为必填项！");
        } else {
            if (StringUtils.isNotEmpty(postNames)) {
                if (postNames.contains(postExcel.getPostName())) {
                    stringBuffer.append(postExcel.getPostName() + "岗位名称已存在！");
                }
            }
        }
        if (StringUtils.isNull(postCode)) {
            stringBuffer.append("岗位编码为必填项！");
        } else {
            if (StringUtils.isNotEmpty(postCodes)) {
                if (postCodes.contains(postExcel.getPostCode())) {
                    stringBuffer.append(postExcel.getPostCode() + "岗位编码已存在！");
                }
            }
        }
        if (StringUtils.isNull(officialRankSystemName)) {
            stringBuffer.append("职级体系为必填项！");
        }
        if (StringUtils.isNull(parentDepartmentExcelName)) {
            stringBuffer.append("适用组织为必填项！");
        }
    }
}

