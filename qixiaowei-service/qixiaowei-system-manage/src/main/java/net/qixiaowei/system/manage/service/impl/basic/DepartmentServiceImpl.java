package net.qixiaowei.system.manage.service.impl.basic;

import java.util.*;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.domain.basic.DepartmentPost;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.mapper.basic.DepartmentPostMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.Department;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.mapper.basic.DepartmentMapper;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import org.springframework.util.CollectionUtils;


/**
 * DepartmentService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-09-27
 */
@Service
public class DepartmentServiceImpl implements IDepartmentService {
    @Autowired
    private DepartmentMapper departmentMapper;
    @Autowired
    private DepartmentPostMapper departmentPostMapper;
    @Autowired
    private PostMapper postMapper;

    /**
     * 查询部门表
     *
     * @param departmentId 部门表主键
     * @return 部门表
     */
    @Override
    public DepartmentDTO selectDepartmentByDepartmentId(Long departmentId) {
        return departmentMapper.selectDepartmentByDepartmentId(departmentId);
    }

    /**
     * 查询部门表列表
     *
     * @param departmentDTO 部门表
     * @return 部门表
     */
    @Override
    public List<DepartmentDTO> selectDepartmentList(DepartmentDTO departmentDTO) {
        //返回数据
        List<DepartmentDTO> tree = new ArrayList<>();
        //筛选数据 获取祖级id
        List<DepartmentDTO> filtrateList = departmentMapper.getParentId(departmentDTO);
        if (CheckObjectIsNullUtils.isNull(departmentDTO)) {
            tree = createTree1(filtrateList, 0);
        } else {
            Department department = new Department();
            BeanUtils.copyProperties(departmentDTO, department);
            //封装数据 根据code或者查询数据为单条数据时
            if (!CheckObjectIsNullUtils.isNull(departmentDTO) && filtrateList.size() == 1) {
                for (DepartmentDTO dto : filtrateList) {
                    List<String> strings = new ArrayList<>();
                    if (StringUtils.isNotBlank(dto.getAncestors())) {
                        strings = Arrays.asList(dto.getAncestors().substring(2).split(","));
                        departmentDTO.setDepartmentIdList(strings);
                        departmentDTO.setDepartmentId(Long.parseLong(strings.get(0)));
                    } else {
                        departmentDTO.setDepartmentId(dto.getDepartmentId());
                        strings.add(dto.getDepartmentId().toString());
                        departmentDTO.setDepartmentIdList(strings);
                    }
                }
            }

            //查询数据
            List<DepartmentDTO> departmentDTOList = departmentMapper.selectDepartmentList(departmentDTO);
            if (filtrateList.size() == 1) {
                for (DepartmentDTO dto : filtrateList) {
                    Integer level = 0;
                    int departmentId = 0;
                    //租级为空 赋值顶级为0
                    if (StringUtils.isEmpty(dto.getAncestors())) {
                        tree = createTree(departmentDTOList, departmentId, level, dto.getDepartmentCode());
                    } else {
                        //层级 只要当前层级
                        level = dto.getLevel();
                        List<String> strings = Arrays.asList(dto.getAncestors().split(","));
                        departmentId = Integer.parseInt(strings.get(0));
                        tree = createTree(departmentDTOList, departmentId, level, dto.getDepartmentCode());
                    }

                }
            }
        }

        return tree;
    }


    /**
     * 递归建立树形结构 （算法）
     *
     * @param lists
     * @param pid
     * @return
     */
    private List<DepartmentDTO> createTree(List<DepartmentDTO> lists, int pid, Integer level, String departmentCode) {
        List<DepartmentDTO> tree = new ArrayList<>();
        for (DepartmentDTO catelog : lists) {
            if (catelog.getParentDepartmentId() == pid) {
                catelog.setChildList(createTree(lists, Integer.parseInt(catelog.getDepartmentId().toString()), level, departmentCode));
                if (level == 0) {
                    tree.add(catelog);
                } else {
                    if (StringUtils.isNotBlank(departmentCode)) {
                        //只保留上级和本级
                        if (catelog.getLevel() <= level) {
                            if (catelog.getLevel().equals(level)) {
                                if (StringUtils.equals(catelog.getDepartmentCode(), departmentCode)) {
                                    tree.add(catelog);
                                    continue;
                                }
                            } else {
                                tree.add(catelog);
                            }
                        }

                    }

                }
            }
        }
        return tree;
    }


    /**
     * 懒加载
     *
     * @param lists
     * @param pid
     * @return
     */
    private List<DepartmentDTO> createTree1(List<DepartmentDTO> lists, int pid) {
        List<DepartmentDTO> tree = new ArrayList<>();
        for (DepartmentDTO catelog : lists) {
            if (catelog.getParentDepartmentId() == pid) {
                catelog.setChildList(createTree1(lists, Integer.parseInt(catelog.getDepartmentId().toString())));
                tree.add(catelog);
            }
        }
        return tree;
    }

    /**
     * 新增部门表
     *
     * @param departmentDTO 部门表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertDepartment(DepartmentDTO departmentDTO) {
        //查询code编码是否已经存在
        DepartmentDTO departmentDTO1 = departmentMapper.selectDepartmentCode(departmentDTO.getDepartmentCode());
        if (null != departmentDTO1) {
            throw new ServiceException("code编码已经存在 不允许添加相同编码" + departmentDTO.getDepartmentCode());
        }
        //实体类
        Department department = new Department();
        //dto类
        DepartmentDTO departmentDTO2 = new DepartmentDTO();
        //copy
        BeanUtils.copyProperties(departmentDTO, department);
        //祖级id
        String ancestors = null;


        //父级部门ID
        Long parentDepartmentId = departmentDTO.getParentDepartmentId();
        //如果父级部门ID为空 直接插入数据库
        if (null == parentDepartmentId) {
            department = this.packDepartment(department);
            department.setSort(1);
            department.setParentDepartmentId(0L);
            department.setAncestors("");
            return departmentMapper.insertDepartment(department);
        } else {
            department = this.packDepartment(department);
            //根据父级id查询 获取父级id的祖级列表ID
            departmentDTO2 = departmentMapper.selectParentDepartmentId(parentDepartmentId);
            Long departmentId = departmentDTO2.getDepartmentId();
            //父级id第一层级
            if (null == departmentId) {
                department.setSort(departmentDTO2.getSort() + 1);
                department.setParentDepartmentId(departmentId);
                //祖级id就是父级id
                department.setAncestors(String.valueOf(departmentDTO.getParentDepartmentId()));
                return departmentMapper.insertDepartment(department);
            } else {
                department.setSort(departmentDTO2.getSort() + 1);
                department.setParentDepartmentId(departmentId);
                //祖级id
                if (StringUtils.isNotBlank(departmentDTO2.getAncestors())) {
                    ancestors = departmentDTO2.getAncestors().trim() + "," + department.getParentDepartmentId();
                } else {
                    ancestors = department.getParentDepartmentId().toString();
                }

                //祖级id
                department.setAncestors(ancestors);
                return departmentMapper.insertDepartment(department);
            }
        }
    }

    /**
     * 封装部门表数据(组织表)
     *
     * @return
     */
    public Department packDepartment(Department department) {
        department.setCreateBy(SecurityUtils.getUserId());
        department.setCreateTime(DateUtils.getNowDate());
        department.setUpdateTime(DateUtils.getNowDate());
        department.setUpdateBy(SecurityUtils.getUserId());
        department.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        department.setStatus(0);
        return department;
    }

    /**
     * 修改部门表
     *
     * @param departmentDTO 部门表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateDepartment(DepartmentDTO departmentDTO) {
        //部门表(组织)
        Department department = new Department();
        //排序
        int i = 1;
        DepartmentDTO departmentDTO1 = departmentMapper.selectDepartmentByDepartmentId(departmentDTO.getDepartmentId());
        if (null == departmentDTO.getDepartmentId()){
            BeanUtils.copyProperties(departmentDTO, department);
            departmentDTO.setParentDepartmentId(0L);
        }else {
            BeanUtils.copyProperties(departmentDTO, department);
            if (StringUtils.isBlank(departmentDTO1.getAncestors())){
                department.setAncestors(departmentDTO1.getParentDepartmentId()+","+departmentDTO1.getDepartmentId());
            }else {
                department.setAncestors(departmentDTO1.getAncestors()+","+departmentDTO1.getDepartmentId());
            }
        }




        //接收部门岗位关联表参数
        List<DepartmentPostDTO> departmentPostDTOList = departmentDTO.getDepartmentPostDTOList();
        //根据部门id查询出数据库的数据
        List<DepartmentPostDTO> departmentPostDTOList2 = departmentPostMapper.selectDepartmentId(departmentDTO.getDepartmentId());
        //部门ID
        List<Long> departmentPostIds = new ArrayList<>();
        //sterm流求差集
        departmentPostIds = departmentPostDTOList2.stream().filter(a ->
                !departmentPostDTOList.stream().map(DepartmentPostDTO::getDepartmentPostId).collect(Collectors.toList()).contains(a.getDepartmentPostId())
        ).collect(Collectors.toList()).stream().map(DepartmentPostDTO::getDepartmentPostId).collect(Collectors.toList());
        if (!CollectionUtils.isEmpty(departmentPostIds)) {
            try {
                departmentPostMapper.logicDeleteDepartmentPostByDepartmentPostIds(departmentPostIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除组织岗位关联失败");
            }
        }
        if (!CollectionUtils.isEmpty(departmentPostIds)){
            //去除已经删除的id
            for (int i1 = 0; i1 < departmentPostDTOList.size(); i1++) {
                if (departmentPostIds.contains(departmentPostDTOList.get(i1).getDepartmentPostId())) {
                    departmentPostDTOList.remove(i1);
                }
            }
        }

        if (!CollectionUtils.isEmpty(departmentPostDTOList)) {
            //组织中间表 新增
            List<DepartmentPost> departmentPostAddList = new ArrayList<>();
            //组织中间表 修改
            List<DepartmentPost> departmentPostUpdateList = new ArrayList<>();
            for (DepartmentPostDTO departmentPostDTO : departmentPostDTOList) {
                //部门岗位关联表
                DepartmentPost departmentPost = new DepartmentPost();
                BeanUtils.copyProperties(departmentPostDTO, departmentPost);
                //部门id(组织)
                departmentPost.setDepartmentId(departmentDTO.getDepartmentId());
                //岗位排序
                departmentPost.setPostSort(i);
                departmentPost.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                if (null == departmentPostDTO.getDepartmentPostId()) {

                    departmentPost.setCreateBy(SecurityUtils.getUserId());
                    departmentPost.setCreateTime(DateUtils.getNowDate());
                    departmentPost.setUpdateBy(SecurityUtils.getUserId());
                    departmentPost.setUpdateTime(DateUtils.getNowDate());
                    departmentPostAddList.add(departmentPost);
                } else {
                    departmentPost.setUpdateBy(SecurityUtils.getUserId());
                    departmentPost.setUpdateTime(DateUtils.getNowDate());
                    departmentPostUpdateList.add(departmentPost);
                }

                i++;
            }
            if (!CollectionUtils.isEmpty(departmentPostAddList)) {
                try {
                    departmentPostMapper.batchDepartmentPost(departmentPostAddList);
                } catch (Exception e) {
                    throw new ServiceException("新增组织岗位信息失败");
                }
            }
            if (!CollectionUtils.isEmpty(departmentPostUpdateList)) {
                try {
                    departmentPostMapper.updateDepartmentPosts(departmentPostUpdateList);
                } catch (Exception e) {
                    throw new ServiceException("修改组织岗位信息失败");
                }
            }
        }
        department.setUpdateTime(DateUtils.getNowDate());
        department.setUpdateBy(SecurityUtils.getUserId());
        return departmentMapper.updateDepartment(department);
    }

    /**
     * 逻辑批量删除部门表
     *
     * @param departmentIds 需要删除的部门表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteDepartmentByDepartmentIds(List<Long>  departmentIds) {
        StringBuffer posterreo = new StringBuffer();
        StringBuffer emplerreo = new StringBuffer();
        StringBuffer depterreo = new StringBuffer();
        int i = 0;
        //组织集合
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        //循环添加
        for (Long departmentId : departmentIds) {
            departmentDTOList.addAll(departmentMapper.selectAncestors(departmentId));
        }
        //去重
        List<DepartmentDTO> collect = departmentDTOList.stream().distinct().collect(Collectors.toList());
        for (DepartmentDTO dto : collect) {
            // 岗位是否被引用
            List<DepartmentPostDTO> departmentPostDTOS = departmentMapper.selectDeptAndPost(dto.getDepartmentId());
            if (!CollectionUtils.isEmpty(departmentPostDTOS)) {
                posterreo.append("组织编码" + dto.getDepartmentCode() + "\r\n");
            }

            // 人员是否被引用
            List<EmployeeDTO> employeeDTOS = departmentMapper.queryDeptEmployee(dto.getDepartmentId());
            if (!CollectionUtils.isEmpty(employeeDTOS)) {
                emplerreo.append("组织编码" + dto.getDepartmentCode() + "\r\n");
            }
        }
        if (posterreo.length() > 0) {
            posterreo.append("组织已被岗位引用，无法删除！" + "\r\n");
        }
        if (emplerreo.length() > 0) {
            emplerreo.append("组织已被人员引用，无法删除！");
        }
        //将错误信息放在一个字符串中
        depterreo.append(posterreo).append(emplerreo);
        if (depterreo.length() > 0) {
            throw new ServiceException(depterreo.toString());
        } else {
            //没有引用批量删除数据
            List<Long> departmentIds2 = collect.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
            try {
                i = departmentMapper.logicDeleteDepartmentByDepartmentIds(departmentIds2, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("");
            }
            departmentPostMapper.logicDeleteDepartmentPostByDepartmentIds(departmentIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        }
        return i;
    }

    /**
     * 物理删除部门表信息
     *
     * @param departmentId 部门表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteDepartmentByDepartmentId(Long departmentId) {
        return departmentMapper.deleteDepartmentByDepartmentId(departmentId);
    }

    /**
     * 查询上级组织
     *
     * @return
     */
    @Override
    public List<DepartmentDTO> queryparent() {
        return departmentMapper.queryparent();
    }

    /**
     * 部门岗位详情
     *
     * @param departmentId
     * @return
     */
    @Override
    public DepartmentDTO deptParticulars(Long departmentId) {
        //查询组织信息
        DepartmentDTO departmentDTO1 = departmentMapper.selectParentDepartmentId(departmentId);
        //查询组织关联岗位信息
        List<DepartmentPostDTO> departmentPostDTOList = departmentMapper.selectDeptAndPost(departmentId);
        List<Long> collect = departmentPostDTOList.stream().map(DepartmentPostDTO::getPostId).collect(Collectors.toList());
        List<DepartmentPostDTO>  departmentPostDTOList2 = postMapper.selectPostByPostIds(collect);

        departmentDTO1.setDepartmentPostDTOList(departmentPostDTOList2);
        return departmentDTO1;
    }

    /**
     * 分页查询部门人员表列表
     *
     * @param departmentDTO
     * @return
     */
    @Override
    public List<EmployeeDTO> queryDeptEmployee(DepartmentDTO departmentDTO) {
        return departmentMapper.queryDeptEmployee(departmentDTO.getDepartmentId());
    }


    @Transactional
    @Override
    public int logicDeleteDepartmentByDepartmentId(DepartmentDTO departmentDTO) {
        StringBuffer posterreo = new StringBuffer();
        StringBuffer emplerreo = new StringBuffer();
        StringBuffer depterreo = new StringBuffer();
        int i = 0;
        Department department = new Department();
        department.setDepartmentId(departmentDTO.getDepartmentId());
        department.setUpdateTime(DateUtils.getNowDate());
        department.setUpdateBy(SecurityUtils.getUserId());
        //岗位是否被引用
        List<DepartmentPostDTO> departmentPostDTOS = departmentMapper.selectDeptAndPost(departmentDTO.getDepartmentId());
        if (null != departmentPostDTOS) {
            posterreo.append("组织编码" + departmentDTO.getDepartmentCode() + "\r\n");
        }
        //人员是否被引用
        List<EmployeeDTO> employeeDTOS = departmentMapper.queryDeptEmployee(departmentDTO.getDepartmentId());
        if (!CollectionUtils.isEmpty(employeeDTOS)) {
            emplerreo.append("组织编码" + departmentDTO.getDepartmentCode() + "\r\n");
        }
        if (posterreo.length() > 0) {
            posterreo.append("组织已被岗位引用，无法删除！" + "\r\n");
        }
        if (emplerreo.length() > 0) {
            emplerreo.append("组织已被人员引用，无法删除！");
        }
        //将错误信息放在一个字符串中
        depterreo.append(posterreo).append(emplerreo);
        if (depterreo.length() > 0) {
            throw new ServiceException(depterreo.toString());
        } else {
            //删除数据
            i = departmentMapper.logicDeleteDepartmentByDepartmentId(department);
        }
        return i;
    }

    /**
     * 逻辑删除部门表信息
     *
     * @param departmentDTO 部门表
     * @return 结果
     */
    @Override
    public int logicDeleteDepartmentByDepartmentIds(DepartmentDTO departmentDTO) {
        StringBuffer posterreo = new StringBuffer();
        StringBuffer emplerreo = new StringBuffer();
        StringBuffer depterreo = new StringBuffer();
        int i = 0;
        Department department = new Department();
        department.setDepartmentId(departmentDTO.getDepartmentId());
        //组织集合
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        //根据id查询所有子级数据
        departmentDTOList = departmentMapper.selectAncestors(department.getDepartmentId());

        for (DepartmentDTO dto : departmentDTOList) {
            // 岗位是否被引用
            List<DepartmentPostDTO> departmentPostDTOS = departmentMapper.selectDeptAndPost(dto.getDepartmentId());
            if (!CollectionUtils.isEmpty(departmentPostDTOS)) {
                posterreo.append("组织编码" + dto.getDepartmentCode() + "\r\n");
            }

            // 人员是否被引用
            List<EmployeeDTO> employeeDTOS = departmentMapper.queryDeptEmployee(dto.getDepartmentId());
            if (!CollectionUtils.isEmpty(employeeDTOS)) {
                emplerreo.append("组织编码" + dto.getDepartmentCode() + "\r\n");
            }
        }
        if (posterreo.length() > 0) {
            posterreo.append("组织已被岗位引用，无法删除！" + "\r\n");
        }
        if (emplerreo.length() > 0) {
            emplerreo.append("组织已被人员引用，无法删除！");
        }
        //将错误信息放在一个字符串中
        depterreo.append(posterreo).append(emplerreo);
        if (depterreo.length() > 0) {
            throw new ServiceException(depterreo.toString());
        } else {
            //没有引用批量删除数据
            List<Long> departmentIds = departmentDTOList.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
            try {
                i = departmentMapper.logicDeleteDepartmentByDepartmentIds(departmentIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
               throw new ServiceException("删除组织失败");
            }
            try {
                departmentPostMapper.logicDeleteDepartmentPostByDepartmentIds(departmentIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除组织关联表失败");
            }
        }
        return i;
    }

    /**
     * 物理删除部门表信息
     *
     * @param departmentDTO 部门表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDepartmentByDepartmentId(DepartmentDTO departmentDTO) {
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO, department);
        return departmentMapper.deleteDepartmentByDepartmentId(department.getDepartmentId());
    }

    /**
     * 物理批量删除部门表
     *
     * @param departmentDtos 需要删除的部门表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteDepartmentByDepartmentIds(List<DepartmentDTO> departmentDtos) {
        List<Long> stringList = new ArrayList();
        for (DepartmentDTO departmentDTO : departmentDtos) {
            stringList.add(departmentDTO.getDepartmentId());
        }
        return departmentMapper.deleteDepartmentByDepartmentIds(stringList);
    }

    /**
     * 批量新增部门表信息
     *
     * @param departmentDtos 部门表对象
     */
    @Transactional
    public int insertDepartments(List<DepartmentDTO> departmentDtos) {
        List<Department> departmentList = new ArrayList();

        for (DepartmentDTO departmentDTO : departmentDtos) {
            Department department = new Department();
            BeanUtils.copyProperties(departmentDTO, department);
            department.setCreateBy(SecurityUtils.getUserId());
            department.setCreateTime(DateUtils.getNowDate());
            department.setUpdateTime(DateUtils.getNowDate());
            department.setUpdateBy(SecurityUtils.getUserId());
            department.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            departmentList.add(department);
        }
        return departmentMapper.batchDepartment(departmentList);
    }

    /**
     * 批量修改部门表信息
     *
     * @param departmentDtos 部门表对象
     */
    @Transactional
    public int updateDepartments(List<DepartmentDTO> departmentDtos) {
        List<Department> departmentList = new ArrayList();

        for (DepartmentDTO departmentDTO : departmentDtos) {
            Department department = new Department();
            BeanUtils.copyProperties(departmentDTO, department);
            department.setCreateBy(SecurityUtils.getUserId());
            department.setCreateTime(DateUtils.getNowDate());
            department.setUpdateTime(DateUtils.getNowDate());
            department.setUpdateBy(SecurityUtils.getUserId());
            departmentList.add(department);
        }
        return departmentMapper.updateDepartments(departmentList);
    }
}

