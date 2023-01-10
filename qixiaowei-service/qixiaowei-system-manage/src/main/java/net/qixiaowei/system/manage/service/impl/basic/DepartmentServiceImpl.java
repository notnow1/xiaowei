package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import net.qixiaowei.system.manage.api.domain.basic.Department;
import net.qixiaowei.system.manage.api.domain.basic.DepartmentPost;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.mapper.basic.DepartmentMapper;
import net.qixiaowei.system.manage.mapper.basic.DepartmentPostMapper;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


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
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RemoteDecomposeService remoteDecomposeService;

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
        //查询数据
        List<DepartmentDTO> departmentDTOList = departmentMapper.selectDepartmentList(departmentDTO);
        if (!CheckObjectIsNullUtils.isNull(departmentDTO)) {
            return departmentDTOList;
        } else {
            if (StringUtils.isEmpty(departmentDTOList)) {
                return departmentDTOList;
            } else {
                return this.createTree(departmentDTOList, 0);
            }
        }

    }

    /**
     * 查询部门名称附加父级名称
     * @return
     */
    @Override
    public List<String> selectDepartmentListName() {
        List<String> parentDepartmentExcelNames =  new ArrayList<>();
        DepartmentDTO departmentDTO = new DepartmentDTO();
        //查询数据
        List<DepartmentDTO> departmentDTOList = departmentMapper.selectDepartmentList(departmentDTO);
        List<DepartmentDTO> tree = this.createTree(departmentDTOList, 0);
        List<DepartmentDTO> natureGroups = treeToList(tree);
        if (StringUtils.isNotEmpty(natureGroups)){
            parentDepartmentExcelNames=natureGroups.stream().map(DepartmentDTO::getParentDepartmentExcelName).collect(Collectors.toList());
        }
        return parentDepartmentExcelNames;
    }

    @Override
    public List<DepartmentDTO> selectDepartmentListName(DepartmentDTO departmentDTO) {
        //查询数据
        List<DepartmentDTO> departmentDTOList = departmentMapper.selectDepartmentList(departmentDTO);
        List<DepartmentDTO> tree = this.createTree(departmentDTOList, 0);
        List<DepartmentDTO> natureGroups = treeToList(tree);
        return natureGroups;
    }


    /**
     * 树形数据转list
     * @param departmentDTOList
     * @return
     */
    private List<DepartmentDTO> treeToList(List<DepartmentDTO> departmentDTOList) {
        List<DepartmentDTO> allSysMenuDto = new ArrayList<>();
        for (DepartmentDTO departmentDTO : departmentDTOList) {
            List<DepartmentDTO> children = departmentDTO.getChildren();
            allSysMenuDto.add(departmentDTO);
            if (children != null && children.size() > 0) {
                allSysMenuDto.addAll(treeToList(children));
                departmentDTO.setChildren(null);
            }
        }
        return allSysMenuDto;
    }

    /**
     * 返回组织层级
     *
     * @return
     */
    @Override
    public List<Integer> selectLevel() {
        return departmentMapper.selectLevel();
    }


    /**
     * 树形结构
     *
     * @param lists
     * @param pid
     * @return
     */
    private List<DepartmentDTO> createTree(List<DepartmentDTO> lists, int pid) {
        List<DepartmentDTO> tree = new ArrayList<>();

        for (DepartmentDTO catelog : lists) {
            if (catelog.getParentDepartmentId() == pid) {
                if (pid==0){
                    catelog.setParentDepartmentExcelName(catelog.getDepartmentName());
                } else {
                    List<DepartmentDTO> departmentDTOList = lists.stream().filter(f -> f.getDepartmentId() == pid).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(departmentDTOList)){
                        catelog.setParentDepartmentExcelName(departmentDTOList.get(0).getParentDepartmentExcelName()+"/"+catelog.getDepartmentName());
                    }
                }
                catelog.setChildren(createTree(lists, Integer.parseInt(catelog.getDepartmentId().toString())));
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
    public DepartmentDTO insertDepartment(DepartmentDTO departmentDTO) {
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
        //层级
        Integer level = departmentDTO.getLevel();
        //如果父级部门ID为空 直接插入数据库
        if (level == 1) {
            department = this.packDepartment(department);
            department.setSort(1);
            department.setParentDepartmentId(Constants.TOP_PARENT_ID);
            department.setAncestors("");
            departmentMapper.insertDepartment(department);
            departmentDTO.setDepartmentId(department.getDepartmentId());
            return departmentDTO;
        } else {
            department = this.packDepartment(department);
            //根据父级id查询 获取父级id的祖级列表ID
            departmentDTO2 = departmentMapper.selectParentDepartmentId(parentDepartmentId);
            Long departmentId = departmentDTO2.getDepartmentId();
            department.setSort(departmentDTO2.getSort() + 1);
            department.setParentDepartmentId(departmentId);
            //祖级id
            if (StringUtils.isNotBlank(departmentDTO2.getAncestors())) {
                ancestors = departmentDTO2.getAncestors().trim() + "," + department.getParentDepartmentId();
            } else {
                ancestors = departmentDTO2.getParentDepartmentId() + "," + departmentDTO2.getDepartmentId();
            }

            //祖级id
            department.setAncestors(ancestors);
            departmentMapper.insertDepartment(department);
            departmentDTO.setDepartmentId(department.getDepartmentId());
            return departmentDTO;

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
        department.setStatus(1);
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
        if (departmentDTO.getParentDepartmentId()==0) {
            BeanUtils.copyProperties(departmentDTO, department);
            departmentDTO.setParentDepartmentId(Constants.TOP_PARENT_ID);
        } else {
            DepartmentDTO departmentDTO1 = departmentMapper.selectDepartmentByDepartmentId(departmentDTO.getParentDepartmentId());
            BeanUtils.copyProperties(departmentDTO, department);
            if (StringUtils.isBlank(departmentDTO1.getAncestors())) {
                department.setAncestors(departmentDTO1.getParentDepartmentId() + "," + departmentDTO1.getDepartmentId());
            } else {
                department.setAncestors(departmentDTO1.getAncestors() + "," + departmentDTO1.getParentDepartmentId());
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
        if (!StringUtils.isEmpty(departmentPostIds)) {
            try {
                departmentPostMapper.logicDeleteDepartmentPostByDepartmentPostIds(departmentPostIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除组织岗位关联失败");
            }
        }
        if (!StringUtils.isEmpty(departmentPostIds)) {
            //去除已经删除的id
            for (int i1 = 0; i1 < departmentPostDTOList.size(); i1++) {
                if (departmentPostIds.contains(departmentPostDTOList.get(i1).getDepartmentPostId())) {
                    departmentPostDTOList.remove(i1);
                }
            }
        }

        if (!StringUtils.isEmpty(departmentPostDTOList)) {
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
            if (!StringUtils.isEmpty(departmentPostAddList)) {
                try {
                    departmentPostMapper.batchDepartmentPost(departmentPostAddList);
                } catch (Exception e) {
                    throw new ServiceException("新增组织岗位信息失败");
                }
            }
            if (!StringUtils.isEmpty(departmentPostUpdateList)) {
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
    public int logicDeleteDepartmentByDepartmentIds(List<Long> departmentIds) {
        StringBuffer posterreo = new StringBuffer();
        StringBuffer emplerreo = new StringBuffer();
        StringBuffer depterreo = new StringBuffer();
        StringBuffer decomposesErreo = new StringBuffer();
        int i = 0;
        //包含其子级数据
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        List<DepartmentDTO> departmentDTOList1 = departmentMapper.selectDepartmentByDepartmentIds(departmentIds);
        for (DepartmentDTO departmentDTO : departmentDTOList1) {
            //根据id查询所有子级数据
            departmentDTOList.addAll(departmentMapper.selectAncestors(departmentDTO.getDepartmentId()));
        }
        if (StringUtils.isNotEmpty(departmentDTOList)&& departmentDTOList.get(0) != null){
            List<DepartmentDTO> departmentDTOList12 = departmentDTOList.stream().distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(departmentDTOList12)){
                for (DepartmentDTO dto : departmentDTOList12) {
                    // 岗位是否被引用
                    List<DepartmentPostDTO> departmentPostDTOS = departmentMapper.selectDeptAndPost(dto.getDepartmentId());
                    String s = departmentPostDTOS.stream().map(DepartmentPostDTO::getPostName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
                    if (!StringUtils.isEmpty(departmentPostDTOS)) {
                        posterreo.append("组织" + dto.getDepartmentName() + "已被岗位" + s + "引用\n");
                    }

                    // 人员是否被引用
                    List<EmployeeDTO> employeeDTOS = departmentMapper.queryDeptEmployee(dto.getDepartmentId());
                    String s1 = employeeDTOS.stream().map(EmployeeDTO::getEmployeeName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
                    if (!StringUtils.isEmpty(employeeDTOS)) {
                        emplerreo.append("组织" + dto.getDepartmentName() + "已被人员" + s1 + "引用\n");
                    }
                    //远程调用 目标分解是否被引用
                    R<List<TargetDecompose>> listR = remoteDecomposeService.queryDeptDecompose(dto.getDepartmentId());
                    List<TargetDecompose> data = listR.getData();
                    if (StringUtils.isNotEmpty(data)){
                        List<String> collect = data.stream().map(TargetDecompose::getIndicatorName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(data)) {
                            decomposesErreo.append("组织名称" + dto.getDepartmentName() + "已被目标分解" + collect + "引用\n");
                        }
                    }
                }
            }
            //将错误信息放在一个字符串中
            depterreo.append(posterreo).append(emplerreo);
            if (depterreo.length() > 0) {
                throw new ServiceException(depterreo.toString());
            } else {
                //没有引用批量删除数据
                try {
                    i = departmentMapper.logicDeleteDepartmentByDepartmentIds(departmentDTOList12.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList()), SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除组织信息失败");
                }
                try {
                    departmentPostMapper.logicDeleteDepartmentPostByDepartmentIds(departmentDTOList12.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList()), SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除组织岗位信息失败");
                }
            }
        }


        return i;
    }

    /**
     * 批量删 只传父级
     *
     * @param departmentIds
     * @return
     */
    public List<DepartmentDTO> packDepartmentDTOList(List<Long> departmentIds) {
        //组织集合
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        //循环添加
        for (Long departmentId : departmentIds) {
            departmentDTOList.addAll(departmentMapper.selectAncestors(departmentId));
        }
        //去重
        List<DepartmentDTO> collect = departmentDTOList.stream().distinct().collect(Collectors.toList());
        return collect;
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
        List<DepartmentDTO> queryparent = departmentMapper.queryparent();
        return this.createTree(queryparent, 0);
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
        DepartmentDTO departmentDTO = departmentMapper.selectParentDepartmentId(departmentId);

        DepartmentDTO departmentDTO1 = departmentMapper.selectParentDepartmentId(departmentDTO.getParentDepartmentId());
        if (null != departmentDTO1) {

            departmentDTO.setParentDepartmentName(departmentDTO1.getDepartmentName());
        }
        //查询组织关联岗位信息
        List<DepartmentPostDTO> departmentPostDTOList = departmentMapper.selectDeptAndPost(departmentId);
        departmentDTO.setDepartmentPostDTOList(departmentPostDTOList);
        return departmentDTO;
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

    /**
     * 获取部门列表
     *
     * @param departmentDTO
     * @return
     */
    @Override
    public List<DepartmentDTO> dropList(DepartmentDTO departmentDTO) {
        return departmentMapper.selectDepartmentList(departmentDTO);
    }

    /**
     * 通过部门ID获取列表
     *
     * @param departmentIds
     * @return
     */
    @Override
    public List<DepartmentDTO> selectDepartmentByDepartmentIds(List<Long> departmentIds) {
        return departmentMapper.selectDepartmentByDepartmentIds(departmentIds);
    }

    /**
     * 根据code查询部门集合
     * @param departmentCodes
     * @return
     */
    @Override
    public List<DepartmentDTO> selectCodeList(List<String> departmentCodes) {
        return departmentMapper.selectDepartmentCodes(departmentCodes);
    }

    /**
     * 查询所有
     *
     * @param departmentDTO
     * @return
     */
    @Override
    public List<DepartmentDTO> selectDepartmentAll(DepartmentDTO departmentDTO) {
        return departmentMapper.selectDepartmentList(departmentDTO);
    }

    /**
     * 查询所有部门
     * @return
     */
    @Override
    public List<DepartmentDTO> getParentAll() {
        return departmentMapper.getParentAll();
    }

    /**
     * 远程查询一级部门及子级部门
     * @param departmentId
     * @return
     */
    @Override
    public List<DepartmentDTO> selectParentDepartment(Long departmentId) {
        return departmentMapper.selectParentDepartment(departmentId);
    }

    /**
     * 远程查询所有部门
     * @return
     */
    @Override
    public List<DepartmentDTO> getAll() {
        return departmentMapper.getAll();
    }

    /**
     * 根据等级查找部门
     * @param level 等级
     * @return List
     */
    @Override
    public List<DepartmentDTO> selectDepartmentByLevel(Integer level) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        departmentDTO.setLevel(level);
        return departmentMapper.selectDepartmentList(departmentDTO);
    }


    @Transactional
    @Override
    public int logicDeleteDepartmentByDepartmentId(DepartmentDTO departmentDTO) {
        StringBuffer posterreo = new StringBuffer();
        StringBuffer emplerreo = new StringBuffer();
        StringBuffer decomposesErreo = new StringBuffer();
        StringBuffer depterreo = new StringBuffer();
        int i = 0;
        Department department = new Department();
        department.setDepartmentId(departmentDTO.getDepartmentId());
        department.setUpdateTime(DateUtils.getNowDate());
        department.setUpdateBy(SecurityUtils.getUserId());
        //岗位是否被引用
        List<DepartmentPostDTO> departmentPostDTOS = departmentMapper.selectDeptAndPost(departmentDTO.getDepartmentId());
        List<String> collect1 = departmentPostDTOS.stream().map(DepartmentPostDTO::getPostName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (StringUtils.isNotEmpty(departmentPostDTOS)) {
            posterreo.append("组织名称" + departmentDTO.getDepartmentName() + "已被岗位"+collect1+"引用\n");
        }
        //人员是否被引用
        List<EmployeeDTO> employeeDTOS = departmentMapper.queryDeptEmployee(departmentDTO.getDepartmentId());
        List<String> collect2 = employeeDTOS.stream().map(EmployeeDTO::getEmployeeName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (!StringUtils.isEmpty(employeeDTOS)) {
            emplerreo.append("组织名称" + departmentDTO.getDepartmentName() + "已被人员"+collect2+"引用\n");
        }
        //远程调用 目标分解是否被引用
        R<List<TargetDecompose>> listR = remoteDecomposeService.queryDeptDecompose(departmentDTO.getDepartmentId());
        List<TargetDecompose> data = listR.getData();
        if (StringUtils.isNotEmpty(data)){
            List<String> collect = data.stream().map(TargetDecompose::getIndicatorName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(data)) {
                decomposesErreo.append("组织名称" + departmentDTO.getDepartmentName() + "已被目标分解" + collect + "引用\n");
            }
        }

        //将错误信息放在一个字符串中
        depterreo.append(posterreo).append(emplerreo).append(decomposesErreo);
        if (depterreo.length() > 0) {
            throw new ServiceException(depterreo.toString());
        } else {
            //删除数据
            try {
                i = departmentMapper.logicDeleteDepartmentByDepartmentId(department);
            } catch (Exception e) {
                throw new ServiceException("删除组织失败");
            }
            List<Long> departments = new ArrayList<>();
            departments.add(department.getDepartmentId());
            departmentPostMapper.logicDeleteDepartmentPostByDepartmentIds(departments, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
    @Transactional
    public int logicDeleteDepartmentByDepartmentIds(DepartmentDTO departmentDTO) {
        StringBuffer posterreo = new StringBuffer();
        StringBuffer emplerreo = new StringBuffer();
        StringBuffer decomposesErreo = new StringBuffer();
        StringBuffer depterreo = new StringBuffer();
        int i = 0;
        Department department = new Department();
        department.setDepartmentId(departmentDTO.getDepartmentId());
        //组织集合
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        //根据id查询所有子级数据
        departmentDTOList = departmentMapper.selectAncestors(department.getDepartmentId());

        for (DepartmentDTO dto : departmentDTOList) {
            this.quoteEmployee(dto,posterreo,emplerreo,decomposesErreo,depterreo);
        }
        //将错误信息放在一个字符串中
        depterreo.append(posterreo).append(emplerreo).append(decomposesErreo);
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
     * 组织引用删除
     * @param dto
     * @param posterreo
     * @param emplerreo
     * @param decomposesErreo
     * @param depterreo
     */
    private void quoteEmployee(DepartmentDTO dto, StringBuffer posterreo, StringBuffer emplerreo, StringBuffer decomposesErreo, StringBuffer depterreo) {
        // 岗位是否被引用
        List<DepartmentPostDTO> departmentPostDTOS = departmentMapper.selectDeptAndPost(dto.getDepartmentId());
        String postName = departmentPostDTOS.stream().map(DepartmentPostDTO::getPostName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        String officialRankSystemName = departmentPostDTOS.stream().map(DepartmentPostDTO::getOfficialRankSystemName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        if (!StringUtils.isEmpty(departmentPostDTOS)) {
            posterreo.append("组织" + dto.getDepartmentName() + "已被岗位" + postName + "引用\n");
        }

        // 人员是否被引用
        List<EmployeeDTO> employeeDTOS = departmentMapper.queryDeptEmployee(dto.getDepartmentId());
        String s1 = employeeDTOS.stream().map(EmployeeDTO::getEmployeeName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        if (!StringUtils.isEmpty(employeeDTOS)) {
            emplerreo.append("组织名称" + dto.getDepartmentName() + "已被人员" + s1 + "引用\n");
        }
        //远程调用 目标分解是否被引用
        R<List<TargetDecompose>> listR = remoteDecomposeService.queryDeptDecompose(dto.getDepartmentId());
        List<TargetDecompose> data = listR.getData();
        if (StringUtils.isNotEmpty(data)){
            List<String> collect = data.stream().map(TargetDecompose::getIndicatorName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                decomposesErreo.append("组织名称" + dto.getDepartmentName() + "已被目标分解" + collect + "引用\n");
            }
        }
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
    @Override
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
    @Override
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

