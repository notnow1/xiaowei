package net.qixiaowei.system.manage.service.impl.basic;

import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.CheckObjectIsNullUtils;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.DeptSalaryAdjustItemDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteBonusPayApplicationService;
import net.qixiaowei.operate.cloud.api.remote.bonus.RemoteEmployeeAnnualBonusService;
import net.qixiaowei.operate.cloud.api.remote.employee.RemoteEmployeeBudgetService;
import net.qixiaowei.operate.cloud.api.remote.performance.RemotePerformanceAppraisalService;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteDeptSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.api.remote.salary.RemoteSalaryAdjustPlanService;
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

import java.util.*;
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
    @Autowired
    private RemoteEmployeeBudgetService remoteEmployeeBudgetService;
    @Autowired
    private RemoteBonusPayApplicationService remoteBonusPayApplicationService;
    @Autowired
    private RemoteEmployeeAnnualBonusService remoteEmployeeAnnualBonusService;
    @Autowired
    private RemotePerformanceAppraisalService remotePerformanceAppraisalService;
    @Autowired
    private RemoteSalaryAdjustPlanService remoteSalaryAdjustPlanService;
    @Autowired
    private RemoteDeptSalaryAdjustPlanService remoteDeptSalaryAdjustPlanService;

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
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO,department);
        //返回数据
        List<DepartmentDTO> tree = new ArrayList<>();
        //查询数据
        List<DepartmentDTO> departmentDTOList = departmentMapper.selectDepartmentList(department);
        this.handleResult(departmentDTOList);
        if (!CheckObjectIsNullUtils.isNull(departmentDTO)) {
            return departmentDTOList;
        } else {
            if (StringUtils.isEmpty(departmentDTOList)) {
                return departmentDTOList;
            } else {
                List<DepartmentDTO> departmentDTOS = this.createTree(departmentDTOList, 0);
                return this.getEmployeeByDepartmentId(departmentDTOS);
            }
        }

    }

    @Override
    public void handleResult(List<DepartmentDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(DepartmentDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }

    /**
     * 查询部门名称附加父级名称
     *
     * @return
     */
    @Override
    public List<String> selectDepartmentListName() {
        List<String> parentDepartmentExcelNames = new ArrayList<>();
        Department department= new Department();
        //查询数据
        List<DepartmentDTO> departmentDTOList = departmentMapper.selectDepartmentList(department);
        List<DepartmentDTO> tree = this.createTree(departmentDTOList, 0);
        List<DepartmentDTO> natureGroups = treeToList(tree);
        if (StringUtils.isNotEmpty(natureGroups)) {
            parentDepartmentExcelNames = natureGroups.stream().map(DepartmentDTO::getParentDepartmentExcelName).collect(Collectors.toList());
        }
        return parentDepartmentExcelNames;
    }

    /**
     * 查询部门名称附加父级名称
     * @param department
     * @return
     */
    @Override
    public List<DepartmentDTO> selectDepartmentListName(Department department) {
        //查询数据
        List<DepartmentDTO> departmentDTOList = departmentMapper.selectDepartmentList(department);
        List<DepartmentDTO> tree = this.createTree(departmentDTOList, 0);
        List<DepartmentDTO> natureGroups = treeToList(tree);
        return natureGroups;
    }


    /**
     * 树形数据转list
     *
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
     * 树形数据转list
     *
     * @param departmentDTOList
     * @return
     */
    private List<DepartmentDTO> getEmployeeByDepartmentId(List<DepartmentDTO> departmentDTOList) {
        if (StringUtils.isNotEmpty(departmentDTOList)){
            for (DepartmentDTO departmentDTO : departmentDTOList) {
                List<DepartmentDTO> children = departmentDTO.getChildren();
                departmentDTO.setEmployeeDTOList(employeeMapper.selectEmployeeByDepartmentId(departmentDTO.getDepartmentId()));
                if (children != null && children.size() > 0) {
                    getEmployeeByDepartmentId(children);

                }
            }
        }
        return departmentDTOList;
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
                if (pid == 0) {
                    catelog.setParentDepartmentExcelName(catelog.getDepartmentName());
                } else {
                    List<DepartmentDTO> departmentDTOList = lists.stream().filter(f -> f.getDepartmentId() == pid).collect(Collectors.toList());
                    if (StringUtils.isNotEmpty(departmentDTOList)) {
                        catelog.setParentDepartmentExcelName(departmentDTOList.get(0).getParentDepartmentExcelName() + "/" + catelog.getDepartmentName());
                    }
                }
                catelog.setChildren(createTree(lists, Integer.parseInt(catelog.getDepartmentId().toString())));
                tree.add(catelog);
            }
        }
        return tree;
    }

    /**
     * 生成部门编码
     *
     * @return 部门编码
     */
    @Override
    public String generateDepartmentCode() {
        String departmentCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.DEPARTMENT.getCode();
        List<String> departmentCodes = departmentMapper.getDepartmentCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(departmentCodes)) {
            for (String code : departmentCodes) {
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
        departmentCode = "000" + number;
        departmentCode = prefixCodeRule + departmentCode.substring(departmentCode.length() - 3);
        return departmentCode;
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
        List<DepartmentPost> departmentPostList = new ArrayList<>();
        List<DepartmentPostDTO> departmentPostDTOList = departmentDTO.getDepartmentPostDTOList();
        //查询code编码是否已经存在
        DepartmentDTO departmentDTO1 = departmentMapper.selectDepartmentCode(departmentDTO.getDepartmentCode());
        if (null != departmentDTO1) {
            throw new ServiceException("编码已经存在 不允许添加相同编码" + departmentDTO.getDepartmentCode());
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
            this.packageInsertDepartmentPost(departmentDTO, departmentPostList, departmentPostDTOList);
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
            this.packageInsertDepartmentPost(departmentDTO, departmentPostList, departmentPostDTOList);

            return departmentDTO;

        }
    }

    private void packageInsertDepartmentPost(DepartmentDTO departmentDTO, List<DepartmentPost> departmentPostList, List<DepartmentPostDTO> departmentPostDTOList) {
        if (StringUtils.isNotEmpty(departmentPostDTOList)){
            for (int i = 0; i < departmentPostDTOList.size(); i++) {
                //部门岗位关联表
                DepartmentPost departmentPost = new DepartmentPost();
                BeanUtils.copyProperties(departmentPostDTOList.get(i), departmentPost);
                //部门id(组织)
                departmentPost.setDepartmentId(departmentDTO.getDepartmentId());
                //岗位排序
                departmentPost.setPostSort(i);
                departmentPost.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                departmentPost.setCreateBy(SecurityUtils.getUserId());
                departmentPost.setCreateTime(DateUtils.getNowDate());
                departmentPost.setUpdateBy(SecurityUtils.getUserId());
                departmentPost.setUpdateTime(DateUtils.getNowDate());
                departmentPostList.add(departmentPost);
            }
        }
        if (StringUtils.isNotEmpty(departmentPostList)){
            departmentPostMapper.batchDepartmentPost(departmentPostList);
        }
    }

    /**
     * 封装部门表数据(组织表)
     *
     * @return
     */
    public Department packDepartment(Department department) {
        if (StringUtils.isNotNull(department)){
            if (null == department.getStatus()){
                department.setStatus(1);
            }
        }
        department.setCreateBy(SecurityUtils.getUserId());
        department.setCreateTime(DateUtils.getNowDate());
        department.setUpdateTime(DateUtils.getNowDate());
        department.setUpdateBy(SecurityUtils.getUserId());
        department.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
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
        int num = 0;
        //部门表(组织)
        Department department = new Department();


        List<Department> departmentUpdateList = new ArrayList<>();
        //排序
        int i = 1;
        if (departmentDTO.getParentDepartmentId() == 0) {
            BeanUtils.copyProperties(departmentDTO, department);
            department.setUpdateTime(DateUtils.getNowDate());
            department.setAncestors("");
            department.setUpdateBy(SecurityUtils.getUserId());
            department.setParentDepartmentId(Constants.TOP_PARENT_ID);
        } else {
            DepartmentDTO departmentDTO1 = departmentMapper.selectDepartmentByDepartmentId(departmentDTO.getParentDepartmentId());
            BeanUtils.copyProperties(departmentDTO, department);
            department.setUpdateTime(DateUtils.getNowDate());
            department.setUpdateBy(SecurityUtils.getUserId());
            if (StringUtils.isBlank(departmentDTO1.getAncestors())) {
                department.setAncestors(departmentDTO1.getParentDepartmentId() + "," + departmentDTO1.getDepartmentId());
            } else {
                department.setAncestors(departmentDTO1.getAncestors() + "," + departmentDTO1.getParentDepartmentId());
            }
        }
        Map<Long,Integer> map = new HashMap<>();
        List<DepartmentDTO> departmentDTOList = departmentMapper.selectAncestors(department.getDepartmentId());
        for (int i1 = 0; i1 < departmentDTOList.size(); i1++) {
            map.put(departmentDTOList.get(i1).getDepartmentId(),i1);
        }
        if (StringUtils.isNotEmpty(departmentDTOList) && departmentDTOList.size()>1) {
            for (int i1 = 1; i1 < departmentDTOList.size(); i1++) {
                if (i1 == 1){
                    Department department2 = new Department();
                    if (StringUtils.isBlank(department.getAncestors())) {
                        departmentDTOList.get(i1).setAncestors(department.getParentDepartmentId() + "," + department.getDepartmentId());
                    } else {
                        departmentDTOList.get(i1).setAncestors(department.getAncestors() + "," + department.getDepartmentId());
                    }
                    departmentDTOList.get(i1).setLevel(department.getLevel()+1);
                    if(null != department.getStatus() && department.getStatus() == 0){
                        departmentDTOList.get(i1).setStatus(department.getStatus());
                    }
                    departmentDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                    departmentDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                    departmentDTOList.get(i1).setParentDepartmentId(department.getDepartmentId());
                    BeanUtils.copyProperties(departmentDTOList.get(i1),department2);
                    departmentUpdateList.add(department2);
                }else {
                    if (departmentDTOList.get(i1 - 1).getDepartmentId().equals(departmentDTOList.get(i1).getParentDepartmentId())){
                        Department department2 = new Department();
                        //父级
                        DepartmentDTO departmentDTO2 = departmentDTOList.get(i1 - 1);
                        if (StringUtils.isBlank(departmentDTO2.getAncestors())) {
                            departmentDTOList.get(i1).setAncestors(departmentDTO2.getParentDepartmentId() + "," + departmentDTO2.getDepartmentId());
                        } else {
                            departmentDTOList.get(i1).setAncestors(departmentDTO2.getAncestors() + "," + departmentDTO2.getDepartmentId());
                        }
                        departmentDTOList.get(i1).setLevel(departmentDTO2.getLevel()+1);
                        if(null != department.getStatus() && department.getStatus() == 0){
                            departmentDTOList.get(i1).setParentDepartmentId(department.getDepartmentId());
                        }
                        departmentDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                        departmentDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                        departmentDTOList.get(i1).setParentDepartmentId(departmentDTO2.getDepartmentId());
                        BeanUtils.copyProperties(departmentDTOList.get(i1),department2);
                        departmentUpdateList.add(department2);
                    }else {
                        Department department2 = new Department();
                        //父级
                        DepartmentDTO departmentDTO2 = departmentDTOList.get(map.get(departmentDTOList.get(i1).getParentDepartmentId()));
                        if (StringUtils.isBlank(departmentDTO2.getAncestors())) {
                            departmentDTOList.get(i1).setAncestors(departmentDTO2.getParentDepartmentId() + "," + departmentDTO2.getDepartmentId());
                        } else {
                            departmentDTOList.get(i1).setAncestors(departmentDTO2.getAncestors() + "," + departmentDTO2.getDepartmentId());
                        }
                        departmentDTOList.get(i1).setLevel(departmentDTO2.getLevel()+1);
                        if(null != department.getStatus() && department.getStatus() == 0){
                            departmentDTOList.get(i1).setParentDepartmentId(department.getDepartmentId());
                        }
                        departmentDTOList.get(i1).setUpdateTime(DateUtils.getNowDate());
                        departmentDTOList.get(i1).setUpdateBy(SecurityUtils.getUserId());
                        departmentDTOList.get(i1).setParentDepartmentId(departmentDTO2.getDepartmentId());
                        BeanUtils.copyProperties(departmentDTOList.get(i1),department2);
                        departmentUpdateList.add(department2);
                    }
                }
            }
        }

        //接收部门岗位关联表参数
        List<DepartmentPostDTO> departmentPostDTOList = departmentDTO.getDepartmentPostDTOList();
        //根据部门id查询出数据库的数据
        List<DepartmentPostDTO> departmentPostDTOList2 = departmentPostMapper.selectDepartmentId(departmentDTO.getDepartmentId());
        //部门ID
        List<Long> departmentPostIds = new ArrayList<>();
        //sterm流求差集
        departmentPostIds = departmentPostDTOList2.stream().filter(a -> !departmentPostDTOList.stream().map(DepartmentPostDTO::getDepartmentPostId).collect(Collectors.toList()).contains(a.getDepartmentPostId())).collect(Collectors.toList()).stream().map(DepartmentPostDTO::getDepartmentPostId).collect(Collectors.toList());
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
        try {
            num = departmentMapper.updateDepartment(department);
        } catch (Exception e) {
            throw new ServiceException("修改组织信息失败");
        }
        if (StringUtils.isNotEmpty(departmentUpdateList)) {
            try {
                departmentMapper.updateDepartments(departmentUpdateList);
            } catch (Exception e) {
                throw new ServiceException("批量修改组织子级信息失败");
            }
        }
        return num;
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
        StringBuffer officialRankDecomposeerreo = new StringBuffer();
        StringBuffer emplerreo = new StringBuffer();
        StringBuffer decomposesErreo = new StringBuffer();
        StringBuffer employeeBudgetErreo = new StringBuffer();
        StringBuffer bonusPayApplicationErreo = new StringBuffer();
        StringBuffer employeeAnnualBonusErreo = new StringBuffer();
        // 个人调薪引用
        StringBuffer empSalaryAdjustPlanErreo = new StringBuffer();
        StringBuffer depterreo = new StringBuffer();
        int i = 0;
        //包含其子级数据
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        List<DepartmentDTO> departmentDTOList1 = departmentMapper.selectDepartmentByDepartmentIds(departmentIds);
        for (DepartmentDTO departmentDTO : departmentDTOList1) {
            //根据id查询所有子级数据
            departmentDTOList.addAll(departmentMapper.selectAncestors(departmentDTO.getDepartmentId()));
        }
        if (StringUtils.isNotEmpty(departmentDTOList) && departmentDTOList.get(0) != null) {
            List<DepartmentDTO> departmentDTOList12 = departmentDTOList.stream().distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(departmentDTOList12)) {
                for (DepartmentDTO dto : departmentDTOList12) {
                    this.quoteDepartment(dto, officialRankDecomposeerreo, employeeBudgetErreo, bonusPayApplicationErreo, employeeAnnualBonusErreo, posterreo, emplerreo, decomposesErreo, empSalaryAdjustPlanErreo);
                }
            }
            //将错误信息放在一个字符串中
            depterreo.append(posterreo).append(emplerreo).append(decomposesErreo).append(employeeBudgetErreo).append(bonusPayApplicationErreo).append(employeeAnnualBonusErreo).append(officialRankDecomposeerreo).append(empSalaryAdjustPlanErreo);
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
     * @param departmentId
     * @return
     */
    @Override
    public List<DepartmentDTO> queryparent(Long departmentId) {
        List<DepartmentDTO> queryparent = departmentMapper.queryparent();
        List<DepartmentDTO> tree = this.createTree(queryparent, 0);
        if (StringUtils.isNotNull(departmentId)) {
            if (StringUtils.isNotEmpty(tree)) {
                this.remoteTree(tree, departmentId);
            }

        }
        return tree;
    }

    /**
     * 删除自己及下级树形结构
     *
     * @param edpartmentList
     * @param departmentId
     * @return
     */
    private void remoteTree(List<DepartmentDTO> edpartmentList, Long departmentId) {
        for (int i = edpartmentList.size() - 1; i >= 0; i--) {
            List<DepartmentDTO> children = edpartmentList.get(i).getChildren();
            if (children != null && children.size() > 0) {
                if (edpartmentList.get(i).getDepartmentId().equals(departmentId)) {
                    edpartmentList.remove(edpartmentList.get(i));
                } else {
                    remoteTree(children, departmentId);
                }
            } else {
                if (edpartmentList.get(i).getDepartmentId().equals(departmentId)) {
                    edpartmentList.remove(edpartmentList.get(i));
                }
            }
        }
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
            departmentDTO.setParentDepartmentStatus(departmentDTO1.getStatus());
        }else {
            departmentDTO.setParentDepartmentStatus(1);
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
     * @param department
     * @return
     */
    @Override
    public List<DepartmentDTO> dropList(Department department) {
        return departmentMapper.selectDepartmentList(department);
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
     *
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
        Department department = new Department();
        BeanUtils.copyProperties(departmentDTO,department);
        return departmentMapper.selectDepartmentList(department);
    }

    /**
     * 查询所有部门
     *
     * @return
     */
    @Override
    public List<DepartmentDTO> getParentAll() {
        return departmentMapper.getParentAll();
    }

    /**
     * 远程查询一级部门及子级部门
     *
     * @param departmentId
     * @return
     */
    @Override
    public List<DepartmentDTO> selectParentDepartment(Long departmentId) {
        return departmentMapper.selectParentDepartment(departmentId);
    }

    /**
     * 远程查询所有部门
     *
     * @return
     */
    @Override
    public List<DepartmentDTO> getAll() {
        return departmentMapper.getAll();
    }

    /**
     * 根据等级查找部门
     *
     * @param level 等级
     * @return List
     */
    @Override
    public List<DepartmentDTO> selectDepartmentByLevel(Integer level) {
        Department department = new Department();
        department.setLevel(level);
        return departmentMapper.selectDepartmentList(department);
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
            posterreo.append("组织名称" + departmentDTO.getDepartmentName() + "已被岗位" + collect1 + "引用\n");
        }
        //人员是否被引用
        List<EmployeeDTO> employeeDTOS = departmentMapper.queryDeptEmployee(departmentDTO.getDepartmentId());
        List<String> collect2 = employeeDTOS.stream().map(EmployeeDTO::getEmployeeName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
        if (!StringUtils.isEmpty(employeeDTOS)) {
            emplerreo.append("组织名称" + departmentDTO.getDepartmentName() + "已被人员" + collect2 + "引用\n");
        }
        //远程调用 目标分解是否被引用
        R<List<TargetDecompose>> listR = remoteDecomposeService.queryDeptDecompose(departmentDTO.getDepartmentId());
        List<TargetDecompose> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {
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
        StringBuffer officialRankDecomposeerreo = new StringBuffer();
        StringBuffer emplerreo = new StringBuffer();
        StringBuffer decomposesErreo = new StringBuffer();
        StringBuffer employeeBudgetErreo = new StringBuffer();
        StringBuffer bonusPayApplicationErreo = new StringBuffer();
        StringBuffer employeeAnnualBonusErreo = new StringBuffer();
        // 个人调薪引用
        StringBuffer empSalaryAdjustPlanErreo = new StringBuffer();
        StringBuffer depterreo = new StringBuffer();
        int i = 0;
        Department department = new Department();
        department.setDepartmentId(departmentDTO.getDepartmentId());
        //组织集合
        List<DepartmentDTO> departmentDTOList = new ArrayList<>();
        //根据id查询所有子级数据
        departmentDTOList = departmentMapper.selectAncestors(department.getDepartmentId());

        for (DepartmentDTO dto : departmentDTOList) {
            this.quoteDepartment(dto, officialRankDecomposeerreo, employeeBudgetErreo, bonusPayApplicationErreo, employeeAnnualBonusErreo, posterreo, emplerreo, decomposesErreo, empSalaryAdjustPlanErreo);
        }
        //将错误信息放在一个字符串中
        depterreo.append(posterreo).append(emplerreo).append(decomposesErreo).append(employeeBudgetErreo).append(bonusPayApplicationErreo).append(employeeAnnualBonusErreo).append(officialRankDecomposeerreo).append(empSalaryAdjustPlanErreo);
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
     *
     * @param dto
     * @param officialRankDecomposeerreo
     * @param employeeBudgetErreo
     * @param bonusPayApplicationErreo
     * @param employeeAnnualBonusErreo
     * @param posterreo
     * @param emplerreo
     * @param decomposesErreo
     * @param empSalaryAdjustPlanErreo
     */
    private void quoteDepartment(DepartmentDTO dto, StringBuffer officialRankDecomposeerreo, StringBuffer employeeBudgetErreo, StringBuffer bonusPayApplicationErreo, StringBuffer employeeAnnualBonusErreo, StringBuffer posterreo, StringBuffer emplerreo, StringBuffer decomposesErreo, StringBuffer empSalaryAdjustPlanErreo) {
        // 岗位是否被引用
        List<DepartmentPostDTO> departmentPostDTOS = departmentMapper.selectDeptAndPost(dto.getDepartmentId());
        String postName = departmentPostDTOS.stream().map(DepartmentPostDTO::getPostName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        //职级分解id
        String officialRankDecomposeId = departmentPostDTOS.stream().map(DepartmentPostDTO::getOfficialRankDecomposeId).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        //职级分解名称
        String officialRankSystemName = departmentPostDTOS.stream().map(DepartmentPostDTO::getOfficialRankDecomposeName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        if (!StringUtils.isEmpty(officialRankDecomposeId) && !StringUtils.equals(officialRankDecomposeId, "[]")) {
            officialRankDecomposeerreo.append("组织" + dto.getDepartmentName() + "已被职级分解[" + StringUtils.join(officialRankSystemName, ",") + "]职级引用\n");
        }
        if (!StringUtils.isEmpty(departmentPostDTOS)) {
            posterreo.append("组织" + dto.getDepartmentName() + "已被岗位" + StringUtils.join(postName, ",") + "引用\n");
        }

        // 人员是否被引用
        List<EmployeeDTO> employeeDTOS = departmentMapper.queryDeptEmployee(dto.getDepartmentId());
        String employeeNames = employeeDTOS.stream().map(EmployeeDTO::getEmployeeName).filter(Objects::nonNull).distinct().collect(Collectors.toList()).toString();
        if (!StringUtils.isEmpty(employeeDTOS)) {
            emplerreo.append("组织名称" + dto.getDepartmentName() + "已被人员" + StringUtils.join(employeeNames, ",") + "引用\n");
        }
        //远程调用 目标分解是否被引用
        R<List<TargetDecompose>> listR = remoteDecomposeService.queryDeptDecompose(dto.getDepartmentId());
        List<TargetDecompose> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {
            List<String> collect = data.stream().map(TargetDecompose::getIndicatorName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                decomposesErreo.append("组织名称" + dto.getDepartmentName() + "已被目标分解[" + StringUtils.join(collect, ",") + "]引用\n");
            }
        }
        //远程人力预算
        R<List<EmployeeBudgetDTO>> employeeBudgetData = remoteEmployeeBudgetService.selectByDepartmentId(dto.getDepartmentId(), SecurityConstants.INNER);
        List<EmployeeBudgetDTO> employeeBudgetDTOList = employeeBudgetData.getData();
        if (StringUtils.isNotEmpty(employeeBudgetDTOList)) {
            List<Long> departmentIds = employeeBudgetDTOList.stream().map(EmployeeBudgetDTO::getDepartmentId).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            //预算年度
            List<Integer> budgetYears = employeeBudgetDTOList.stream().map(EmployeeBudgetDTO::getBudgetYear).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            //职级体系名称
            List<String> officialRankSystemNames = employeeBudgetDTOList.stream().map(EmployeeBudgetDTO::getOfficialRankSystemName).filter(Objects::nonNull).distinct().collect(Collectors.toList());

            if (StringUtils.isNotEmpty(departmentIds)) {
                employeeBudgetErreo.append("组织名称" + dto.getDepartmentName() + "已被[" + StringUtils.join(budgetYears, ",") + "][" + StringUtils.join(officialRankSystemNames, ",") + "]年人力预算引用\n");
            }
        }
        //远程奖金发放申请
        R<List<BonusPayApplicationDTO>> bonusPayApplicationList = remoteBonusPayApplicationService.selectBonusPayApplicationByDepartmentId(dto.getDepartmentId(), SecurityConstants.INNER);
        List<BonusPayApplicationDTO> bonusPayApplicationData = bonusPayApplicationList.getData();
        if (StringUtils.isNotEmpty(bonusPayApplicationData)) {
            //获奖时间-年月
            List<String> awardYearMonths = bonusPayApplicationData.stream().map(BonusPayApplicationDTO::getAwardYearMonth).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            //奖项名称
            List<String> awardNames = bonusPayApplicationData.stream().map(BonusPayApplicationDTO::getAwardName).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(awardYearMonths)) {
                bonusPayApplicationErreo.append("组织名称" + dto.getDepartmentName() + "已被[" + StringUtils.join(awardYearMonths, ",") + "]奖金发放申请的奖金名称[" + StringUtils.join(awardNames, ",") + "]引用\n");
            }
        }
        //远程个人年终奖
        R<List<EmployeeAnnualBonus>> employeeAnnualBonusList = remoteEmployeeAnnualBonusService.selectEmployeeAnnualBonusByDepartmentId(dto.getDepartmentId(), SecurityConstants.INNER);
        List<EmployeeAnnualBonus> employeeAnnualBonusData = employeeAnnualBonusList.getData();
        if (StringUtils.isNotEmpty(employeeAnnualBonusData)) {
            List<Integer> annualBonusYears = employeeAnnualBonusData.stream().map(EmployeeAnnualBonus::getAnnualBonusYear).filter(Objects::nonNull).distinct().collect(Collectors.toList());
            bonusPayApplicationErreo.append("组织名称" + dto.getDepartmentName() + "已被[" + StringUtils.join(annualBonusYears, ",") + "]个人年终奖引用\n");
        }
        //远程绩效考核服务
        List<Long> departmentIds = new ArrayList<>();
        departmentIds.add(dto.getDepartmentId());
        R<List<PerformanceAppraisalObjectsDTO>> performanceAppraisalObjectsList = remotePerformanceAppraisalService.selectByDepartmentIds(departmentIds, SecurityConstants.INNER);
        List<PerformanceAppraisalObjectsDTO> data1 = performanceAppraisalObjectsList.getData();
        if (StringUtils.isNotEmpty(data1)) {
            List<String> appraisalNames = data1.stream().map(PerformanceAppraisalObjectsDTO::getAppraisalName).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(appraisalNames)) {
                bonusPayApplicationErreo.append("组织名称" + dto.getDepartmentName() + "已被[" + StringUtils.join(appraisalNames, ",") + "]绩效考核引用\n");
            }
        }
        //远程查询个人调薪 组织引用
        R<List<EmpSalaryAdjustPlanDTO>> empSalaryAdjustPlanList = remoteSalaryAdjustPlanService.selectByDepartmentId(dto.getDepartmentId(), SecurityConstants.INNER);
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanData = empSalaryAdjustPlanList.getData();
        if (StringUtils.isNotEmpty(empSalaryAdjustPlanData)) {
            empSalaryAdjustPlanErreo.append("组织名称" + dto.getDepartmentName() + "被[" + StringUtils.join(",", empSalaryAdjustPlanData.stream().map(EmpSalaryAdjustPlanDTO::getEmployeeName).filter(Objects::nonNull).distinct().collect(Collectors.toList())) + "]个人调薪引用 无法删除！\n");
        }
        //远程查询组织调薪 组织引用
        R<List<DeptSalaryAdjustItemDTO>> deptSalaryAdjustItemList = remoteDeptSalaryAdjustPlanService.selectByDepartmentId(dto.getDepartmentId(), SecurityConstants.INNER);
        List<DeptSalaryAdjustItemDTO> deptSalaryAdjustItemData = deptSalaryAdjustItemList.getData();
        if (StringUtils.isNotEmpty(deptSalaryAdjustItemData)) {
            empSalaryAdjustPlanErreo.append("组织名称" + dto.getDepartmentName() + "被[" + StringUtils.join(",", deptSalaryAdjustItemData.stream().map(DeptSalaryAdjustItemDTO::getPlanYear).filter(Objects::nonNull).distinct().collect(Collectors.toList())) + "]年部门调薪引用 无法删除！\n");
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

