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
import net.qixiaowei.operate.cloud.service.salary.IOfficialRankEmolumentService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import net.qixiaowei.system.manage.api.remote.basic.RemotePostService;
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalaryPlanVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;


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

    @Autowired
    private IOfficialRankEmolumentService officialRankEmolumentService;


    /**
     * 查询个人调薪计划表
     *
     * @param empSalaryAdjustPlanId 个人调薪计划表主键
     * @return 个人调薪计划表
     */
    @Override
    public EmpSalaryAdjustPlanDTO selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(Long empSalaryAdjustPlanId) {
        if (StringUtils.isNull(empSalaryAdjustPlanId)) {
            throw new ServiceException("请传入个人调薪计划ID");
        }
        EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        if (StringUtils.isNull(empSalaryAdjustPlanDTO)) {
            throw new ServiceException("当前个人调薪计划已不存在");
        }
        String adjustmentType = empSalaryAdjustPlanDTO.getAdjustmentType();
        List<Integer> adjustmentTypeList = setPlanListValue(adjustmentType);
        empSalaryAdjustPlanDTO.setAdjustmentTypeList(adjustmentTypeList);
        Long employeeId = empSalaryAdjustPlanDTO.getEmployeeId();
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOS = selectByEmployeeId(employeeId);
        if (StringUtils.isNotEmpty(empSalaryAdjustPlanDTOS)) {
            for (EmpSalaryAdjustPlanDTO empDTO : empSalaryAdjustPlanDTOS) {
                setHistoryPlanValue(empDTO);
            }
            empSalaryAdjustPlanDTO.setEmpSalaryAdjustPlanDTOS(empSalaryAdjustPlanDTOS);
        }
        List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDTOS = empSalaryAdjustPerformService.selectEmpSalaryAdjustPerformByPlanId(empSalaryAdjustPlanId);
        if (StringUtils.isNotEmpty(empSalaryAdjustPerformDTOS)) {
            for (EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO : empSalaryAdjustPerformDTOS) {
                setPerformName(empSalaryAdjustPerformDTO);
            }
        }
        return empSalaryAdjustPlanDTO;
    }

    /**
     * 为字段命名
     *
     * @param empSalaryAdjustPerformDTO 考核任务
     */
    private static void setPerformName(EmpSalaryAdjustPerformDTO empSalaryAdjustPerformDTO) {
        // 考核周期类型/考核周期
        if (StringUtils.isNotNull(empSalaryAdjustPerformDTO.getCycleType())) {
            switch (empSalaryAdjustPerformDTO.getCycleType()) {
                case 1:
                    empSalaryAdjustPerformDTO.setCycleTypeName("月度");
                    empSalaryAdjustPerformDTO.setCycleNumberName(empSalaryAdjustPerformDTO.getCycleNumber().toString() + "月");
                    break;
                case 2:
                    empSalaryAdjustPerformDTO.setCycleTypeName("季度");
                    empSalaryAdjustPerformDTO.setCycleNumberName(empSalaryAdjustPerformDTO.getCycleNumber().toString() + "季度");
                    break;
                case 3:
                    empSalaryAdjustPerformDTO.setCycleTypeName("半年度");
                    if (empSalaryAdjustPerformDTO.getCycleNumber() == 1) {
                        empSalaryAdjustPerformDTO.setCycleNumberName("上半年");
                    } else {
                        empSalaryAdjustPerformDTO.setCycleNumberName("下半年");
                    }
                    break;
                case 4:
                    empSalaryAdjustPerformDTO.setCycleTypeName("年度");
                    empSalaryAdjustPerformDTO.setCycleNumberName("整年度");
                    break;
            }
        }
    }

    /**
     * 给岗位类型赋值
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划DTO
     */
    private void setHistoryPlanValue(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        if (StringUtils.isNotNull(empSalaryAdjustPlanDTO.getAdjustmentType())) {
            String adjustmentType = empSalaryAdjustPlanDTO.getAdjustmentType();
            List<String> adjustmentTypeList = Arrays.asList(adjustmentType.split(","));
            if (StringUtils.isNotEmpty(adjustmentTypeList)) {
                StringBuilder stringBuilder = new StringBuilder();
                for (String adjustment : adjustmentTypeList) {
                    switch (adjustment) {//调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
                        case "1":
                            stringBuilder.append("调岗；");
                            break;
                        case "2":
                            stringBuilder.append("调级；");
                            break;
                        case "3":
                            stringBuilder.append("调薪；");
                            break;
                    }
                }
                String adjustTypeName = stringBuilder.toString();
                empSalaryAdjustPlanDTO.setAdjustmentTypeName(adjustTypeName.substring(0, adjustTypeName.length() - 1));
            }
        }
    }

    /**
     * 给岗位类型赋List<Integer>
     *
     * @param adjustmentType 调整类型
     */
    private List<Integer> setPlanListValue(String adjustmentType) {
        ArrayList<Integer> list = new ArrayList<>();
        if (StringUtils.isNotNull(adjustmentType)) {
            String[] adjustmentTypeList = adjustmentType.split(",");
            for (String s : adjustmentTypeList) {
                list.add(Integer.valueOf(s));
            }
        }
        return list;
    }

    /**
     * 查询个人调薪计划表列表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 个人调薪计划表
     */
    @Override
    public List<EmpSalaryAdjustPlanDTO> selectEmpSalaryAdjustPlanList(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOS = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlanDTO);
        for (EmpSalaryAdjustPlanDTO salaryAdjustPlanDTO : empSalaryAdjustPlanDTOS) {
            String adjustmentType = salaryAdjustPlanDTO.getAdjustmentType();
            List<Integer> adjustmentTypeList = setPlanListValue(adjustmentType);
            empSalaryAdjustPlanDTO.setAdjustmentTypeList(adjustmentTypeList);
        }
        return empSalaryAdjustPlanDTOS;
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
    private EmployeeSalaryPlanVO getEmployee(Long employeeId) {
        EmployeeDTO employee = new EmployeeDTO();
        employee.setEmployeeId(employeeId);
        R<EmployeeSalaryPlanVO> listR = employeeService.empSalaryAdjustPlan(employee, SecurityConstants.INNER);
        EmployeeSalaryPlanVO employeeSalaryPlanVO = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程人员信息失败 请联系管理员");
        }
        if (StringUtils.isNull(employeeSalaryPlanVO)) {
            throw new ServiceException("当前员工已不存在 请查看员工配置");
        }
        return employeeSalaryPlanVO;
    }

    /**
     * 修改个人调薪计划表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        if (StringUtils.isNull(empSalaryAdjustPlanDTO)) {
            throw new ServiceException("请传入个人调薪计划表");
        }
        Long employeeId = empSalaryAdjustPlanDTO.getEmployeeId();
        Integer isSubmit = empSalaryAdjustPlanDTO.getIsSubmit();
        Date effectiveDate = empSalaryAdjustPlanDTO.getEffectiveDate();
        List<Integer> adjustmentTypeList = empSalaryAdjustPlanDTO.getAdjustmentTypeList();
        if (StringUtils.isNull(empSalaryAdjustPlanDTO.getEmpSalaryAdjustPlanId())) {
            throw new ServiceException("调薪计划ID不可以为空");
        }
        planCheck(employeeId, isSubmit, effectiveDate, adjustmentTypeList);
        // 调薪计划表
        EmpSalaryAdjustPlan empSalaryAdjustPlan = operateEmpSalaryAdjustPlan(empSalaryAdjustPlanDTO, adjustmentTypeList);
        Long empSalaryAdjustPlanId = empSalaryAdjustPlanDTO.getEmpSalaryAdjustPlanId();
        List<EmpSalaryAdjustSnapDTO> empSalaryAdjustSnapDTOS = empSalaryAdjustSnapService.selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        if (StringUtils.isEmpty(empSalaryAdjustSnapDTOS)) {
            throw new ServiceException("调薪快照数据异常 请联系管理员");
        }
        if (isSubmit == 1) {
            empSalaryAdjustPlan.setStatus(1);
        } else {
            empSalaryAdjustPlan.setStatus(0);
        }
        empSalaryAdjustPlan.setEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        return empSalaryAdjustPlanMapper.updateEmpSalaryAdjustPlan(empSalaryAdjustPlan);
    }

    /**
     * 新增个人调薪计划表
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划表
     * @return 结果
     */
    @Override
    @Transactional
    public int insertEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        if (StringUtils.isNull(empSalaryAdjustPlanDTO)) {
            throw new ServiceException("请传入个人调薪计划表");
        }
        Long employeeId = empSalaryAdjustPlanDTO.getEmployeeId();
        Integer isSubmit = empSalaryAdjustPlanDTO.getIsSubmit();
        Date effectiveDate = empSalaryAdjustPlanDTO.getEffectiveDate();
        List<Integer> adjustmentTypeList = empSalaryAdjustPlanDTO.getAdjustmentTypeList();
        planCheck(employeeId, isSubmit, effectiveDate, adjustmentTypeList);
        EmployeeSalaryPlanVO employeeSalaryPlanVO = getEmployee(employeeId);
        // 调薪计划表
        EmpSalaryAdjustPlan empSalaryAdjustPlan = operateEmpSalaryAdjustPlan(empSalaryAdjustPlanDTO, adjustmentTypeList);
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
        operateEmpSalaryAdjustSnap(employeeSalaryPlanVO, empSalaryAdjustPlanId);
        // 近三次考核
        operateEmpSalaryPerformance(employeeId, empSalaryAdjustPlanId);
        return 1;
    }

    /**
     * 近三次考核
     *
     * @param employeeId            员工ID
     * @param empSalaryAdjustPlanId 调薪计划ID
     */
    private void operateEmpSalaryPerformance(Long employeeId, Long empSalaryAdjustPlanId) {
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
    }

    /**
     * 处理调薪快照表
     *
     * @param employeeSalaryPlanVO  员工DTO
     * @param empSalaryAdjustPlanId 调薪计划ID
     */
    private void operateEmpSalaryAdjustSnap(EmployeeSalaryPlanVO employeeSalaryPlanVO, Long empSalaryAdjustPlanId) {
        EmpSalaryAdjustSnapDTO empSalaryAdjustSnapDTO = new EmpSalaryAdjustSnapDTO();
        empSalaryAdjustSnapDTO.setEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        empSalaryAdjustSnapDTO.setEmployeeName(employeeSalaryPlanVO.getEmployeeName());
        empSalaryAdjustSnapDTO.setEmployeeCode(employeeSalaryPlanVO.getEmployeeCode());
        empSalaryAdjustSnapDTO.setEmploymentDate(employeeSalaryPlanVO.getEmploymentDate());
        empSalaryAdjustSnapDTO.setSeniority(employeeSalaryPlanVO.getSeniority());
        empSalaryAdjustSnapDTO.setDepartmentId(employeeSalaryPlanVO.getDepartmentId());
        empSalaryAdjustSnapDTO.setDepartmentName(employeeSalaryPlanVO.getDepartmentName());
        empSalaryAdjustSnapDTO.setDepartmentLeaderId(employeeSalaryPlanVO.getDepartmentLeaderId());
        empSalaryAdjustSnapDTO.setDepartmentLeaderName(employeeSalaryPlanVO.getDepartmentLeaderName());
        empSalaryAdjustSnapDTO.setPostId(employeeSalaryPlanVO.getPostId());
        empSalaryAdjustSnapDTO.setPostName(employeeSalaryPlanVO.getPostName());
        empSalaryAdjustSnapDTO.setOfficialRankSystemId(employeeSalaryPlanVO.getOfficialRankSystemId());
        empSalaryAdjustSnapDTO.setOfficialRankSystemName(employeeSalaryPlanVO.getOfficialRankSystemName());
        empSalaryAdjustSnapDTO.setOfficialRank(employeeSalaryPlanVO.getOfficialRank());
        empSalaryAdjustSnapDTO.setOfficialRankName(employeeSalaryPlanVO.getOfficialRankName());
        empSalaryAdjustSnapDTO.setEmployeeName(employeeSalaryPlanVO.getEmployeeName());
        empSalaryAdjustSnapDTO.setBasicWage(employeeSalaryPlanVO.getBasicWage());
        empSalaryAdjustSnapService.insertEmpSalaryAdjustSnap(empSalaryAdjustSnapDTO);
    }

    /**
     * 处理计划表
     *
     * @param empSalaryAdjustPlanDTO 计划DTO
     * @param adjustmentTypeList     调整选择列表
     * @return EmpSalaryAdjustPlan
     */
    private EmpSalaryAdjustPlan operateEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO, List<Integer> adjustmentTypeList) {
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        empSalaryAdjustPlan.setEmployeeId(empSalaryAdjustPlanDTO.getEmployeeId());
        empSalaryAdjustPlan.setEffectiveDate(empSalaryAdjustPlanDTO.getEffectiveDate());
        empSalaryAdjustPlan.setAdjustExplain(empSalaryAdjustPlanDTO.getAdjustExplain());
        String rankPrefixCode = "";
        // 1调岗;2调级;3调薪
        if (StringUtils.isEmpty(adjustmentTypeList)) {
            empSalaryAdjustPlan.setAdjustmentType(null);
            empSalaryAdjustPlan.setAdjustDepartmentId(null);
            empSalaryAdjustPlan.setAdjustDepartmentName(null);
            empSalaryAdjustPlan.setAdjustPostId(null);
            empSalaryAdjustPlan.setAdjustPostName(null);
            empSalaryAdjustPlan.setAdjustOfficialRankSystemId(null);
            empSalaryAdjustPlan.setAdjustOfficialRankSystemName(null);
            empSalaryAdjustPlan.setAdjustOfficialRank(null);
            empSalaryAdjustPlan.setAdjustOfficialRankName(null);
            empSalaryAdjustPlan.setAdjustEmolument(null);
            return empSalaryAdjustPlan;
        }
        if (adjustmentTypeList.contains(1)) {
            Long adjustDepartmentId = empSalaryAdjustPlanDTO.getAdjustDepartmentId();
            if (StringUtils.isNotNull(adjustDepartmentId)) {
                empSalaryAdjustPlan.setAdjustDepartmentId(adjustDepartmentId);
                DepartmentDTO departmentDTO = getDepartment(adjustDepartmentId);
                empSalaryAdjustPlan.setAdjustDepartmentName(departmentDTO.getDepartmentName());
            }
            Long adjustPostId = empSalaryAdjustPlanDTO.getAdjustPostId();
            if (StringUtils.isNotNull(adjustPostId)) {
                PostDTO post = getPost(adjustPostId);
                String adjustPostName = post.getPostName();
                empSalaryAdjustPlan.setAdjustPostId(adjustPostId);
                empSalaryAdjustPlan.setAdjustPostName(adjustPostName);
                empSalaryAdjustPlan.setAdjustOfficialRankSystemId(post.getOfficialRankSystemId());
                empSalaryAdjustPlan.setAdjustOfficialRankSystemName(post.getOfficialRankSystemName());
                rankPrefixCode = post.getRankPrefixCode();
            }
        } else {
            empSalaryAdjustPlan.setAdjustDepartmentId(null);
            empSalaryAdjustPlan.setAdjustDepartmentName(null);
            empSalaryAdjustPlan.setAdjustPostId(null);
            empSalaryAdjustPlan.setAdjustPostName(null);
            empSalaryAdjustPlan.setAdjustOfficialRankSystemId(null);
            empSalaryAdjustPlan.setAdjustOfficialRankSystemName(null);
            Long postId = empSalaryAdjustPlanDTO.getPostId();
            PostDTO post = getPost(postId);
            rankPrefixCode = post.getRankPrefixCode();
        }
        if (adjustmentTypeList.contains(2)) {
            empSalaryAdjustPlan.setAdjustOfficialRank(empSalaryAdjustPlanDTO.getAdjustOfficialRank());// 职级
            if (StringUtils.isNotNull(empSalaryAdjustPlanDTO.getAdjustOfficialRank())) {
                empSalaryAdjustPlan.setAdjustOfficialRankName(rankPrefixCode + empSalaryAdjustPlanDTO.getAdjustOfficialRank());// 职级名称
            } else {
                empSalaryAdjustPlan.setAdjustOfficialRankName(null);
            }
        } else {
            empSalaryAdjustPlan.setAdjustOfficialRank(null);
            empSalaryAdjustPlan.setAdjustOfficialRankName(null);
        }
        if (adjustmentTypeList.contains(3)) {
            empSalaryAdjustPlan.setAdjustEmolument(empSalaryAdjustPlanDTO.getAdjustEmolument());
        } else {
            empSalaryAdjustPlan.setAdjustEmolument(null);
        }
        StringBuilder adjustmentType = new StringBuilder();
        for (Integer adjust : adjustmentTypeList) {
            adjustmentType.append(adjust).append(",");
        }
        empSalaryAdjustPlan.setAdjustmentType(adjustmentType.toString().substring(0, adjustmentType.toString().length() - 1));

        return empSalaryAdjustPlan;
    }

    /**
     * 新增校验
     *
     * @param employeeId         员工ID
     * @param isSubmit           是否提交
     * @param effectiveDate      生效时间
     * @param adjustmentTypeList 提交时是否选择了
     */
    private static void planCheck(Long employeeId, Integer isSubmit, Date effectiveDate, List<Integer> adjustmentTypeList) {
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
    }

    /**
     * 逻辑批量删除个人调薪计划表
     *
     * @param empSalaryAdjustPlanIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(List<Long> empSalaryAdjustPlanIds) {
        if (StringUtils.isEmpty(empSalaryAdjustPlanIds)) {
            throw new ServiceException("请传入个人调薪计划ID集合");
        }
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOS = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(empSalaryAdjustPlanIds);
        if (empSalaryAdjustPlanDTOS.size() != empSalaryAdjustPlanIds.size()) {
            throw new ServiceException("个人调薪计划部分数据已不存在 请刷新");
        }
        empSalaryAdjustPlanMapper.logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanIds(empSalaryAdjustPlanIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<EmpSalaryAdjustSnapDTO> empSalaryAdjustSnapDTOS = empSalaryAdjustSnapService.selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanIds(empSalaryAdjustPlanIds);
        if (StringUtils.isNotEmpty(empSalaryAdjustSnapDTOS)) {
            List<Long> empSnapIds = empSalaryAdjustSnapDTOS.stream().map(EmpSalaryAdjustSnapDTO::getEmpSalaryAdjustSnapId).collect(Collectors.toList());
            empSalaryAdjustSnapService.logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(empSnapIds);
        }
        List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDTOS = empSalaryAdjustPerformService.selectEmpSalaryAdjustPerformByPlanIds(empSalaryAdjustPlanIds);
        if (StringUtils.isNotEmpty(empSalaryAdjustPerformDTOS)) {
            List<Long> empPerformanceIds = empSalaryAdjustPerformDTOS.stream().map(EmpSalaryAdjustPerformDTO::getEmpSalaryAdjustPerformId).collect(Collectors.toList());
            empSalaryAdjustPerformService.logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(empPerformanceIds);
        }
        return 1;
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
        Long empSalaryAdjustPlanId = empSalaryAdjustPlanDTO.getEmpSalaryAdjustPlanId();
        if (StringUtils.isNull(empSalaryAdjustPlanId)) {
            throw new ServiceException("请传入个人调薪计划ID");
        }
        EmpSalaryAdjustPlanDTO salaryAdjustPlanDTO = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        if (StringUtils.isNull(salaryAdjustPlanDTO)) {
            throw new ServiceException("当前个人调薪计划已不存在");
        }
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        empSalaryAdjustPlan.setEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        empSalaryAdjustPlanMapper.logicDeleteEmpSalaryAdjustPlanByEmpSalaryAdjustPlanId(empSalaryAdjustPlan);
        List<EmpSalaryAdjustSnapDTO> empSalaryAdjustSnapDTOS = empSalaryAdjustSnapService.selectEmpSalaryAdjustSnapByEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        if (StringUtils.isNotEmpty(empSalaryAdjustSnapDTOS)) {
            List<Long> empSnapIds = empSalaryAdjustSnapDTOS.stream().map(EmpSalaryAdjustSnapDTO::getEmpSalaryAdjustSnapId).collect(Collectors.toList());
            empSalaryAdjustSnapService.logicDeleteEmpSalaryAdjustSnapByEmpSalaryAdjustSnapIds(empSnapIds);
        }
        List<EmpSalaryAdjustPerformDTO> empSalaryAdjustPerformDTOS = empSalaryAdjustPerformService.selectEmpSalaryAdjustPerformByPlanId(empSalaryAdjustPlanId);
        if (StringUtils.isNotEmpty(empSalaryAdjustPerformDTOS)) {
            List<Long> empPerformIds = empSalaryAdjustPerformDTOS.stream().map(EmpSalaryAdjustPerformDTO::getEmpSalaryAdjustPerformId).collect(Collectors.toList());
            empSalaryAdjustPerformService.logicDeleteEmpSalaryAdjustPerformByEmpSalaryAdjustPerformIds(empPerformIds);
        }
        return 1;
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

    @Override
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

    @Override
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
     * @param empSalaryAdjustPlanDTO 隔热膜调薪计划dto
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPlanExcel> exportEmpSalaryAdjustPlan(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOList = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlanDTO);
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

    /**
     * 职级确定薪酬详情
     *
     * @param postId       岗位ID
     * @param officialRank 职级
     * @return String
     */
    @Override
    public String officialRankInfo(Long postId, Integer officialRank) {
        if (StringUtils.isNull(postId)) {
            return null;
        }
        if (StringUtils.isNull(officialRank)) {
            return null;
        }
        PostDTO postDTO = getPost(postId);
        Long officialRankSystemId = postDTO.getOfficialRankSystemId();
        if (StringUtils.isNull(officialRankSystemId)) {
            return null;
        }
        return officialRankEmolumentService.officialRankInfo(officialRankSystemId, officialRank);
    }

    /**
     * 根据ID集合查询个人调薪
     *
     * @param employeeIds 员工Id集合
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPlanDTO> selectByEmployeeIds(List<Long> employeeIds) {
        return empSalaryAdjustPlanMapper.selectByEmployeeIds(employeeIds);
    }

    /**
     * 根据职级体系ID集合获取个人调薪
     *
     * @param officialRankSystemIds 职级体系ID集合
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPlanDTO> selectBySystemIds(List<Long> officialRankSystemIds) {
        return empSalaryAdjustPlanMapper.selectBySystemIds(officialRankSystemIds);
    }

    /**
     * 根据部门Id查询个人调薪
     *
     * @param departmentId 部门ID
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPlanDTO> selectByDepartmentId(Long departmentId) {
        EmpSalaryAdjustPlanDTO empSalaryAdjustPlan = new EmpSalaryAdjustPlanDTO();
        empSalaryAdjustPlan.setAdjustDepartmentId(departmentId);
        return empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlan);
    }
}

