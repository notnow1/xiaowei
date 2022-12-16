package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpSalaryAdjustPlan;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPerformDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustPlanDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpSalaryAdjustSnapDTO;
import net.qixiaowei.operate.cloud.excel.salary.EmpSalaryAdjustPlanExcel;
import net.qixiaowei.operate.cloud.mapper.salary.EmpSalaryAdjustPlanMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalObjectsService;
import net.qixiaowei.operate.cloud.service.salary.IEmpSalaryAdjustPerformService;
import net.qixiaowei.operate.cloud.service.salary.IEmpSalaryAdjustPlanService;
import net.qixiaowei.operate.cloud.service.salary.IEmpSalaryAdjustSnapService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import net.qixiaowei.system.manage.api.remote.basic.RemotePostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * EmpSalaryAdjustPlanService业务层处理
 *
 * @author Graves
 * @since 2022-12-14
 */
@Service
public class EmpSalaryAdjustPlanServiceImpl implements IEmpSalaryAdjustPlanService {
    @Autowired
    private EmpSalaryAdjustPlanMapper empSalaryAdjustPlanMapper;

    @Autowired
    private IEmpSalaryAdjustSnapService empSalaryAdjustSnapService;

    @Autowired
    private IEmpSalaryAdjustPerformService empSalaryAdjustPerformService;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private SalaryPayMapper salaryPayMapper;

    @Autowired
    private RemoteOfficialRankSystemService officialRankSystemService;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemotePostService postService;

    @Autowired
    private IPerformanceAppraisalObjectsService performanceAppraisalObjectsService;

    /**
     * 查询个人调薪计划表
     *
     * @param empSalaryAdjustPlanId 个人调薪计划表主键
     * @return 个人调薪计划表
     */
    @Override
    public EmpSalaryAdjustPlanDTO selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(Long empSalaryAdjustPlanId) {
        EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        if (StringUtils.isNull(empSalaryAdjustPlanDTO)) {
            throw new ServiceException("当前个人调薪计划已不存在");
        }
        return null;
    }

    /**
     * 查询个人调薪计划表列表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 个人调薪计划表
     */
    @Override
    public List<EmpSalaryAdjustPlanDTO> selectEmpSalaryAdjustPlanList(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        BeanUtils.copyProperties(empSalaryAdjustPlanDTO, empSalaryAdjustPlan);
        return empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlan);
    }

    /**
     * 新增个人调薪计划表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 结果
     */
    @Override
    public int insertEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        if (StringUtils.isNull(empSalaryAdjustPlanDTO)) {
            throw new ServiceException("请传入个人调薪计划表");
        }
        Long employeeId = empSalaryAdjustPlanDTO.getEmployeeId();
        Integer isSubmit = empSalaryAdjustPlanDTO.getIsSubmit();
        Date effectiveDate = empSalaryAdjustPlanDTO.getEffectiveDate();
        List<Integer> adjustmentTypeList = empSalaryAdjustPlanDTO.getAdjustmentTypeList();
        if (StringUtils.isNull(employeeId)) {
            throw new ServiceException("请传入员工ID");
        }
        if (StringUtils.isNull(isSubmit)) {
            throw new ServiceException("请提供是否提交标识");
        }
        if (isSubmit == 1) {
            if (StringUtils.isEmpty(adjustmentTypeList)) {
                throw new ServiceException("至少选择一个调岗/调薪/调级才可以提交");
            }
            if (StringUtils.isNull(effectiveDate)) {
                throw new ServiceException("请提供生效日期");
            }
        }
        EmployeeDTO employeeDTO = getEmployee(employeeId);
        // 调薪计划表
        // todo 获取部门名称  根据岗位ID获取就获取岗位名称
        Long adjustOfficialRankSystemId = empSalaryAdjustPlanDTO.getAdjustOfficialRankSystemId();
        OfficialRankSystemDTO officialRankSystem = getOfficialRankSystem(adjustOfficialRankSystemId);
        String adjustOfficialRankSystemName = officialRankSystem.getOfficialRankSystemName();
        Long adjustDepartmentId = empSalaryAdjustPlanDTO.getAdjustDepartmentId();
        DepartmentDTO departmentDTO = getDepartment(adjustDepartmentId);
        String adjustDepartmentName = departmentDTO.getDepartmentName();
        Long adjustPostId = empSalaryAdjustPlanDTO.getAdjustPostId();
        PostDTO post = getPost(adjustPostId);
        String adjustPostName = post.getPostName();
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        if (StringUtils.isNotEmpty(adjustmentTypeList)) {
            String adjustmentType = StringUtils.join(",", adjustmentTypeList);
            empSalaryAdjustPlan.setAdjustmentType(adjustmentType);
        }
        empSalaryAdjustPlan.setEmployeeId(employeeId);
        empSalaryAdjustPlan.setEffectiveDate(effectiveDate);
        empSalaryAdjustPlan.setAdjustOfficialRank(empSalaryAdjustPlanDTO.getAdjustOfficialRank());// 职级
        empSalaryAdjustPlan.setAdjustOfficialRankName(empSalaryAdjustPlanDTO.getAdjustOfficialRankName());// 职级名称
        empSalaryAdjustPlan.setAdjustOfficialRankSystemId(adjustOfficialRankSystemId);
        empSalaryAdjustPlan.setAdjustOfficialRankSystemName(adjustOfficialRankSystemName);
        empSalaryAdjustPlan.setAdjustDepartmentId(adjustDepartmentId);
        empSalaryAdjustPlan.setAdjustDepartmentName(adjustDepartmentName);
        empSalaryAdjustPlan.setAdjustPostId(adjustPostId);
        empSalaryAdjustPlan.setAdjustPostName(adjustPostName);
        empSalaryAdjustPlan.setAdjustEmolument(empSalaryAdjustPlanDTO.getAdjustEmolument());
        empSalaryAdjustPlan.setAdjustExplain(empSalaryAdjustPlanDTO.getAdjustExplain());
        if (isSubmit == 1) {
            empSalaryAdjustPlan.setStatus(1);
        } else {
            empSalaryAdjustPlan.setStatus(0);
        }
        addEmpSalaryAdjustPlan(empSalaryAdjustPlan);
        Long empSalaryAdjustPlanId = empSalaryAdjustPlan.getEmpSalaryAdjustPlanId();
        List<EmpSalaryAdjustSnapDTO> empSalaryAdjustSnapDTOS = empSalaryAdjustSnapService.selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        if (StringUtils.isNotEmpty(empSalaryAdjustSnapDTOS)) {
            throw new ServiceException("调薪快照数据异常 请联系管理员");
        }
        // 调薪快照表
        EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO = new EmpSalaryAdjustSnapDTO();
        empSalaryAdjustSnapDTO.setEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        empSalaryAdjustSnapDTO.setEmployeeName(employeeDTO.getEmployeeName());
        empSalaryAdjustSnapDTO.setEmploymentDate(DateUtils.toLocalDate(employeeDTO.getEmploymentDate()));
        empSalaryAdjustSnapDTO.setSeniority(employeeDTO.getWorkingAge());
        empSalaryAdjustSnapDTO.setDepartmentId(employeeDTO.getEmployeeDepartmentId());
        empSalaryAdjustSnapDTO.setDepartmentName(employeeDTO.getEmployeeDepartmentName());
        empSalaryAdjustSnapDTO.setDepartmentLeaderId(employeeDTO.getDepartmentLeaderId());
        empSalaryAdjustSnapDTO.setDepartmentLeaderName(employeeDTO.getInCharge());
        empSalaryAdjustSnapDTO.setPostId(employeeDTO.getEmployeePostId());
        empSalaryAdjustSnapDTO.setPostName(employeeDTO.getEmployeePostName());
        empSalaryAdjustSnapDTO.setOfficialRankSystemId(employeeDTO.getOfficialRankSystemId());
        empSalaryAdjustSnapDTO.setOfficialRankSystemName(employeeDTO.getOfficialRankSystemName());
        empSalaryAdjustSnapDTO.setOfficialRank(employeeDTO.getEmployeeRank());
        empSalaryAdjustSnapDTO.setOfficialRankName(employeeDTO.getEmployeeRankName());
        empSalaryAdjustSnapDTO.setEmployeeName(employeeDTO.getEmployeeName());
        empSalaryAdjustSnapDTO.setBasicWage(employeeDTO.getEmployeeBasicWage());
        empSalaryAdjustSnapService.insertEmpSalaryAdjustSnap(empSalaryAdjustSnapDTO);
        // 近三次考核
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.performanceResult(employeeId);
        if (StringUtils.isNotEmpty(performanceAppraisalObjectsDTOList)) {
            List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDTOS = new ArrayList<>();
            for (PerformanceAppraisalObjectsDTO objectsDTO : performanceAppraisalObjectsDTOList) {
                setObjectFieldName(objectsDTO);
                EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO = new EmpSalaryAdjustPerformDTO();
                empSalaryAdjustPerformDTO.setEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
                empSalaryAdjustPerformDTO.setPerformanceAppraisalId(objectsDTO.getPerformanceAppraisalId());
                empSalaryAdjustPerformDTO.setPerformAppraisalObjectsId(objectsDTO.getPerformAppraisalObjectsId());
                empSalaryAdjustPerformDTO.setCycleType(objectsDTO.getCycleNumber());
                empSalaryAdjustPerformDTO.setCycleNumber(objectsDTO.getCycleNumber());
                empSalaryAdjustPerformDTO.setFilingDate(objectsDTO.getFilingDate());
                empSalaryAdjustPerformDTO.setAppraisalResult(objectsDTO.getAppraisalResult());
                empSalaryAdjustPerformDTOS.add(empSalaryAdjustPerformDTO);
            }
            empSalaryAdjustPerformService.insertEmpSalaryAdjustPerforms(empSalaryAdjustPerformDTOS);
        }
        return 1;
    }

    /**
     * 为字段命名
     *
     * @param appraisalObjectsDTO 考核对象任务
     */
    private void setObjectFieldName(PerformanceAppraisalObjectsDTO appraisalObjectsDTO) {
        // 考核周期类型/考核周期
        if (StringUtils.isNotNull(appraisalObjectsDTO)) {
            if (StringUtils.isNotNull(appraisalObjectsDTO.getCycleType())) {
                switch (appraisalObjectsDTO.getCycleType()) {
                    case 1:
                        appraisalObjectsDTO.setCycleTypeName("月度");
                        appraisalObjectsDTO.setCycleNumberName(appraisalObjectsDTO.getCycleNumber().toString() + "月");
                        break;
                    case 2:
                        appraisalObjectsDTO.setCycleTypeName("季度");
                        appraisalObjectsDTO.setCycleNumberName(appraisalObjectsDTO.getCycleNumber().toString() + "季度");
                        break;
                    case 3:
                        appraisalObjectsDTO.setCycleTypeName("半年度");
                        if (appraisalObjectsDTO.getCycleNumber() == 1) {
                            appraisalObjectsDTO.setCycleNumberName("上半年");
                        } else {
                            appraisalObjectsDTO.setCycleNumberName("下半年");
                        }
                        break;
                    case 4:
                        appraisalObjectsDTO.setCycleTypeName("年度");
                        appraisalObjectsDTO.setCycleNumberName("整年度");
                        break;
                }
            }
        }
    }

    /**
     * 获取员工信息
     *
     * @param adjustPostId 职级体系ID
     */
    private PostDTO getPost(Long adjustPostId) {
        R<PostDTO> postDTOR = postService.selectPostByPostId(adjustPostId, SecurityConstants.INNER);
        PostDTO postDTO = postDTOR.getData();
        if (postDTOR.getCode() != 200) {
            throw new ServiceException("远程调用岗位信息失败 请联系管理员");
        }
        if (StringUtils.isNull(postDTO)) {
            throw new ServiceException("数据异常 当前部门信息已不存在");
        }
        return postDTO;
    }

    /**
     * 获取部门信息
     *
     * @param adjustDepartmentId 职级体系ID
     */
    private DepartmentDTO getDepartment(Long adjustDepartmentId) {
        R<DepartmentDTO> departmentDTOR = departmentService.selectdepartmentId(adjustDepartmentId, SecurityConstants.INNER);
        DepartmentDTO departmentDTO = departmentDTOR.getData();
        if (departmentDTOR.getCode() != 200) {
            throw new ServiceException("远程调用部门信息失败 请联系管理员");
        }
        if (StringUtils.isNull(departmentDTO)) {
            throw new ServiceException("数据异常 当前部门信息已不存在");
        }
        return departmentDTO;
    }

    /**
     * 获取职级体系信息
     *
     * @param adjustOfficialRankSystemId 职级体系ID
     */
    private OfficialRankSystemDTO getOfficialRankSystem(Long adjustOfficialRankSystemId) {
        R<OfficialRankSystemDTO> listR = officialRankSystemService.selectById(adjustOfficialRankSystemId, SecurityConstants.INNER);
        OfficialRankSystemDTO officialRankSystemDTO = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用职级信息失败 请联系管理员");
        }
        if (StringUtils.isNull(officialRankSystemDTO)) {
            throw new ServiceException("数据异常 当前岗位配置的职级体系已不存在");
        }
        return officialRankSystemDTO;
    }

    /**
     * 新增个人调薪计划表
     *
     * @param empSalaryAdjustPlan 个人调薪DTO
     */
    private void addEmpSalaryAdjustPlan(EmpSalaryAdjustPlan empSalaryAdjustPlan) {
        empSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
        empSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
        empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        empSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        empSalaryAdjustPlanMapper.insertEmpSalaryAdjustPlan(empSalaryAdjustPlan);
    }

    /**
     * 根据员工ID获取员工信息
     *
     * @param employeeId 员工ID
     * @return EmployeeDTO
     */
    private EmployeeDTO getEmployee(Long employeeId) {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeId(employeeId);
        R<EmployeeDTO> listR = employeeService.empSalaryAdjustPlan(employee, SecurityConstants.INNER);
        EmployeeDTO employeeDTO = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程人员信息失败 请联系管理员");
        }
        if (StringUtils.isNull(employeeDTO)) {
            throw new ServiceException("当前员工已不存在 请查看员工配置");
        }
        return employeeDTO;
    }

    /**
     * 修改个人调薪计划表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 结果
     */
    @Override
    public int updateEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {

        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        BeanUtils.copyProperties(empSalaryAdjustPlanDTO, empSalaryAdjustPlan);
        empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        return empSalaryAdjustPlanMapper.updateEmpSalaryAdjustPlan(empSalaryAdjustPlan);
    }

    /**
     * 逻辑批量删除个人调薪计划表
     *
     * @param empSalaryAdjustPlanIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(List<Long> empSalaryAdjustPlanIds) {
        return empSalaryAdjustPlanMapper.logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(empSalaryAdjustPlanIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除个人调薪计划表信息
     *
     * @param empSalaryAdjustPlanId 个人调薪计划表主键
     * @return 结果
     */
    @Override
    public int deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(Long empSalaryAdjustPlanId) {
        return empSalaryAdjustPlanMapper.deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
    }

    /**
     * 逻辑删除个人调薪计划表信息
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 结果
     */
    @Override
    public int logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        empSalaryAdjustPlan.setEmpSalaryAdjustPlanId(empSalaryAdjustPlanDTO.getEmpSalaryAdjustPlanId());
        empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        return empSalaryAdjustPlanMapper.logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlan);
    }

    /**
     * 物理删除个人调薪计划表信息
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 结果
     */

    @Override
    public int deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        BeanUtils.copyProperties(empSalaryAdjustPlanDTO, empSalaryAdjustPlan);
        return empSalaryAdjustPlanMapper.deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlan.getEmpSalaryAdjustPlanId());
    }

    /**
     * 物理批量删除个人调薪计划表
     *
     * @param empSalaryAdjustPlanDtos 需要删除的个人调薪计划表主键
     * @return 结果
     */

    @Override
    public int deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDtos) {
        List<Long> stringList = new ArrayList<>();
        for (EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO : empSalaryAdjustPlanDtos) {
            stringList.add(empSalaryAdjustPlanDTO.getEmpSalaryAdjustPlanId());
        }
        return empSalaryAdjustPlanMapper.deleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(stringList);
    }

    /**
     * 批量新增个人调薪计划表信息
     *
     * @param empSalaryAdjustPlanDtos 个人调薪计划表对象
     */

    public int insertEmpSalaryAdjustPlans(List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDtos) {
        List<EmpSalaryAdjustPlan> empSalaryAdjustPlanList = new ArrayList<>();
        for (EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO : empSalaryAdjustPlanDtos) {
            EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
            BeanUtils.copyProperties(empSalaryAdjustPlanDTO, empSalaryAdjustPlan);
            empSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
            empSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
            empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
            empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
            empSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            empSalaryAdjustPlanList.add(empSalaryAdjustPlan);
        }
        return empSalaryAdjustPlanMapper.batchEmpSalaryAdjustPlan(empSalaryAdjustPlanList);
    }

    /**
     * 批量修改个人调薪计划表信息
     *
     * @param empSalaryAdjustPlanDtos 个人调薪计划表对象
     */

    public int updateEmpSalaryAdjustPlans(List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDtos) {
        List<EmpSalaryAdjustPlan> empSalaryAdjustPlanList = new ArrayList<>();

        for (EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO : empSalaryAdjustPlanDtos) {
            EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
            BeanUtils.copyProperties(empSalaryAdjustPlanDTO, empSalaryAdjustPlan);
            empSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
            empSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
            empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
            empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
            empSalaryAdjustPlanList.add(empSalaryAdjustPlan);
        }
        return empSalaryAdjustPlanMapper.updateEmpSalaryAdjustPlans(empSalaryAdjustPlanList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importEmpSalaryAdjustPlan(List<EmpSalaryAdjustPlanExcel> list) {
        List<EmpSalaryAdjustPlan> empSalaryAdjustPlanList = new ArrayList<>();
        list.forEach(l -> {
            EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
            BeanUtils.copyProperties(l, empSalaryAdjustPlan);
            empSalaryAdjustPlan.setCreateBy(SecurityUtils.getUserId());
            empSalaryAdjustPlan.setCreateTime(DateUtils.getNowDate());
            empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
            empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
            empSalaryAdjustPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            empSalaryAdjustPlanList.add(empSalaryAdjustPlan);
        });
        try {
            empSalaryAdjustPlanMapper.batchEmpSalaryAdjustPlan(empSalaryAdjustPlanList);
        } catch (Exception e) {
            throw new ServiceException("导入个人调薪计划表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param empSalaryAdjustPlanDTO
     * @return
     */
    @Override
    public List<EmpSalaryAdjustPlanExcel> exportEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        BeanUtils.copyProperties(empSalaryAdjustPlanDTO, empSalaryAdjustPlan);
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOList = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlan);
        return new ArrayList<>();
    }

    /**
     * 根据员工获取个人调薪
     *
     * @param employeeId 员工ID
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPlanDTO> selectByEmployeeId(Long employeeId) {
        return empSalaryAdjustPlanMapper.selectByEmployeeId(employeeId);
    }
}

