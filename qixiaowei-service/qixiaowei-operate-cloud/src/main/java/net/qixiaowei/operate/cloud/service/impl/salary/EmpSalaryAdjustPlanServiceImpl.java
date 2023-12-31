package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
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
import net.qixiaowei.system.manage.api.vo.basic.EmployeeSalarySnapVO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
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
        EmployeeSalaryPlanVO employee = getEmployee(empSalaryAdjustPlanDTO.getEmployeeId());
        empSalaryAdjustPlanDTO.setEmployeeBasicWage(employee.getBasicWage());
        empSalaryAdjustPlanDTO.setEmployeeDepartmentId(employee.getDepartmentId());
        empSalaryAdjustPlanDTO.setEmployeeDepartmentName(employee.getDepartmentName());
        empSalaryAdjustPlanDTO.setEmployeePostId(employee.getPostId());
        empSalaryAdjustPlanDTO.setEmployeePostName(employee.getPostName());
        empSalaryAdjustPlanDTO.setEmployeeOfficialRank(employee.getOfficialRank());
        empSalaryAdjustPlanDTO.setEmployeeOfficialRankName(employee.getOfficialRankName());
        empSalaryAdjustPlanDTO.setEmployeeDepartmentLeaderId(employee.getDepartmentLeaderId());
        empSalaryAdjustPlanDTO.setEmployeeDepartmentLeaderName(employee.getDepartmentLeaderName());
        String adjustmentType = empSalaryAdjustPlanDTO.getAdjustmentType();
        List<Integer> adjustmentTypeList = setPlanListValue(adjustmentType);
        empSalaryAdjustPlanDTO.setAdjustmentTypeList(adjustmentTypeList);
        Long employeeId = empSalaryAdjustPlanDTO.getEmployeeId();
        // 最近三次绩效结果
        List<Map<String, String>> performanceResultList = new ArrayList<>();
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = performanceAppraisalObjectsService.performanceResult(employeeId);
        if (StringUtils.isNotEmpty(performanceAppraisalObjectsDTOS)) {
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
                setObjectFieldName(performanceAppraisalObjectsDTO);
                HashMap<String, String> map = new HashMap<>();
                map.put("appraisalResult", performanceAppraisalObjectsDTO.getAppraisalResult());
                map.put("cycleNumberName", performanceAppraisalObjectsDTO.getCycleNumberName());
                map.put("filingDate", DateUtils.localToString(performanceAppraisalObjectsDTO.getAppraisalStartDate())
                        + "~"
                        + DateUtils.localToString(performanceAppraisalObjectsDTO.getAppraisalEndDate()));
                performanceResultList.add(map);
            }
        }
        empSalaryAdjustPlanDTO.setPerformanceResultList(performanceResultList);
        // 个人调薪计划
        List<EmpSalaryAdjustPlanDTO> empHistoryPlanDTOS = empSalaryAdjustPlanMapper.selectByEmployeeId(employeeId);
        List<EmpSalaryAdjustPlanDTO> employeeSalarySnapVOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(empHistoryPlanDTOS)) {
            for (EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO1 : empHistoryPlanDTOS) {
                setPlanValue(empSalaryAdjustPlanDTO1);
                EmpSalaryAdjustPlanDTO employeeSalarySnapVO = new EmpSalaryAdjustPlanDTO();
                BeanUtils.copyProperties(empSalaryAdjustPlanDTO1, employeeSalarySnapVO);
                employeeSalarySnapVOS.add(employeeSalarySnapVO);
            }
        }
        empSalaryAdjustPlanDTO.setEmployeeSalarySnapVOS(employeeSalarySnapVOS);
        return empSalaryAdjustPlanDTO;
    }

    /**
     * 给岗位类型赋值
     *
     * @param empSalaryAdjustPlanDTO 个人调薪计划DTO
     */
    private void setPlanValue(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        if (StringUtils.isNotNull(empSalaryAdjustPlanDTO) && StringUtils.isNotNull(empSalaryAdjustPlanDTO.getAdjustmentType())) {
            String adjustmentType = empSalaryAdjustPlanDTO.getAdjustmentType();
            List<String> adjustmentTypeList = Arrays.asList(adjustmentType.split(","));
            if (StringUtils.isNotEmpty(adjustmentTypeList)) {
                List<String> adjustmentList = new ArrayList<>();
                for (String adjustment : adjustmentTypeList) {
                    switch (adjustment) {//调整类型(1调岗;2调级;3调薪),多个用英文逗号隔开
                        case "1":
                            adjustmentList.add("调岗");
                            break;
                        case "2":
                            adjustmentList.add("调级");
                            break;
                        case "3":
                            adjustmentList.add("调薪");
                            break;
                    }
                }
                StringBuilder adjustment = new StringBuilder();
                for (String adjust : adjustmentList) {
                    adjustment.append(adjust);
                }
                empSalaryAdjustPlanDTO.setAdjustmentType(adjustment.toString());
            }
        }
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
    @DataScope(businessAlias = "esa")
    @Override
    public List<EmpSalaryAdjustPlanDTO> selectEmpSalaryAdjustPlanList(EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO) {
        Map<String, Object> params;
        if (StringUtils.isNotNull(empSalaryAdjustPlanDTO.getParams())) {
            params = empSalaryAdjustPlanDTO.getParams();
        } else {
            params = new HashMap<>();
        }
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        BeanUtils.copyProperties(empSalaryAdjustPlanDTO, empSalaryAdjustPlan);
        String employeeName = empSalaryAdjustPlanDTO.getEmployeeName();
        if (StringUtils.isNotNull(employeeName)) {
            params.put("employeeName", employeeName);
        }
        String employeeCode = empSalaryAdjustPlanDTO.getEmployeeCode();
        if (StringUtils.isNotNull(employeeCode)) {
            params.put("employeeCode", employeeCode);
        }
        empSalaryAdjustPlan.setParams(params);
        // 司龄搜索
//        Map<String, Object> params1 = empSalaryAdjustPlan.getParams();
//        List<String> workingAgeEqualList = (List<String>) params1.get("workingAgeEqual");
//        List<String> workingAgeNotEqualList = (List<String>) params1.get("workingAgeNotEqual");
//        List<Integer> workingAgeEqual = new ArrayList<>();
//        List<Integer> workingAgeNotEqual = new ArrayList<>();
//        if (StringUtils.isNotNull(workingAgeEqualList)) {
//            for (String s : workingAgeEqualList) {
//                workingAgeEqual.add(Integer.valueOf(s));
//            }
//            params1.put("workingAgeEqual", workingAgeEqual);
//        }
//        if (StringUtils.isNotNull(workingAgeNotEqualList)) {
//            for (String s : workingAgeNotEqualList) {
//                workingAgeNotEqual.add(Integer.valueOf(s));
//            }
//            params1.put("workingAgeNotEqual", workingAgeNotEqual);
//        }
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOS = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlan);
        for (EmpSalaryAdjustPlanDTO salaryAdjustPlanDTO : empSalaryAdjustPlanDTOS) {
            String adjustmentType = salaryAdjustPlanDTO.getAdjustmentType();
            List<Integer> adjustmentTypeList = setPlanListValue(adjustmentType);
            empSalaryAdjustPlanDTO.setAdjustmentTypeList(adjustmentTypeList);
        }
        this.handleResult(empSalaryAdjustPlanDTOS);
        return empSalaryAdjustPlanDTOS;
    }

    /**
     * 人员远程高级搜索
     *
     * @param params 请求参数
     * @return List
     */
    private List<EmployeeDTO> empAdvancedSearch(Map<String, Object> params) {
        R<List<EmployeeDTO>> listR = employeeService.empAdvancedSearch(params, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("人员远程高级搜索失败 请联系管理员");
        }
        return employeeDTOS;
    }

    /**
     * 组织远程高级搜索
     *
     * @param params 请求参数
     * @return List
     */
    private List<DepartmentDTO> depAdvancedSearch(Map<String, Object> params) {
        R<List<DepartmentDTO>> listR = departmentService.depAdvancedSearch(params, SecurityConstants.INNER);
        List<DepartmentDTO> departmentDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("组织远程高级搜索失败 请联系管理员");
        }
        return departmentDTOS;
    }

    /**
     * 岗位远程高级搜索
     *
     * @param params 请求参数
     * @return List
     */
    private List<PostDTO> postAdvancedSearch(Map<String, Object> params) {
        R<List<PostDTO>> listR = postService.postAdvancedSearch(params, SecurityConstants.INNER);
        List<PostDTO> postDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("组织远程高级搜索失败 请联系管理员");
        }
        return postDTOS;
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
        if (empSalaryAdjustPlanDTO.getStatus() != 0) {
            throw new ServiceException("当前个人调薪已提交 不可修改");
        }
        Long employeeId = empSalaryAdjustPlanDTO.getEmployeeId();
        Integer isSubmit = empSalaryAdjustPlanDTO.getIsSubmit();
        Date effectiveDate = empSalaryAdjustPlanDTO.getEffectiveDate();
        List<Integer> adjustmentTypeList = empSalaryAdjustPlanDTO.getAdjustmentTypeList();
        if (StringUtils.isNull(empSalaryAdjustPlanDTO.getEmpSalaryAdjustPlanId())) {
            throw new ServiceException("调薪计划ID不可以为空");
        }
        EmpSalaryAdjustPlan checkPlan = new EmpSalaryAdjustPlan();
        checkPlan.setEffectiveDate(effectiveDate);
        checkPlan.setEmployeeId(employeeId);
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOS = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(checkPlan);
        if (StringUtils.isNotEmpty(empSalaryAdjustPlanDTOS) &&
                !empSalaryAdjustPlanDTOS.get(0).getEmpSalaryAdjustPlanId()
                        .equals(empSalaryAdjustPlanDTO.getEmpSalaryAdjustPlanId())) {
            throw new ServiceException("该员工在此生效日期下已存在个人调薪计划 请勿重复创建");
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
            EmployeeSalarySnapVO employeeSalarySnapVO = new EmployeeSalarySnapVO();
            BeanUtils.copyProperties(empSalaryAdjustPlanDTO, employeeSalarySnapVO);
            if (empSalaryAdjustPlanDTO.getEffectiveDate().compareTo(DateUtils.getNowDate()) <= 0) {
                R<Integer> integerR = employeeService.empAdjustUpdate(employeeSalarySnapVO, SecurityConstants.FROM_SOURCE);
                if (integerR.getCode() != 200) {
                    throw new ServiceException("远程根据调整策略进行更新人员薪资，岗位，职级失败 请联系管理员");
                }
            }
            if (effectiveDate.compareTo(DateUtils.getNowDate()) <= 0) {
                empSalaryAdjustPlan.setStatus(2);
            } else {
                empSalaryAdjustPlan.setStatus(1);
            }
        } else {
            empSalaryAdjustPlan.setStatus(0);
        }
        empSalaryAdjustPlan.setEmpSalaryAdjustPlanId(empSalaryAdjustPlanId);
        empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
        empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
        empSalaryAdjustPlanMapper.updateEmpSalaryAdjustPlan(empSalaryAdjustPlan);
        return 1;
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
            EmployeeSalarySnapVO employeeSalarySnapVO = new EmployeeSalarySnapVO();
            BeanUtils.copyProperties(empSalaryAdjustPlanDTO, employeeSalarySnapVO);
            if (empSalaryAdjustPlanDTO.getEffectiveDate().compareTo(DateUtils.getNowDate()) <= 0) {
                R<Integer> integerR = employeeService.empAdjustUpdate(employeeSalarySnapVO, SecurityConstants.FROM_SOURCE);
                if (integerR.getCode() != 200) {
                    throw new ServiceException("远程根据调整策略进行更新人员薪资，岗位，职级失败 请联系管理员");
                }
            }
            if (effectiveDate.compareTo(DateUtils.getNowDate()) <= 0) {
                empSalaryAdjustPlan.setStatus(2);
            } else {
                empSalaryAdjustPlan.setStatus(1);
            }
        } else {
            empSalaryAdjustPlan.setStatus(0);
        }
        addEmpSalaryAdjustPlan(empSalaryAdjustPlan);
        empSalaryAdjustPlanDTO.setEmpSalaryAdjustPlanId(empSalaryAdjustPlan.getEmpSalaryAdjustPlanId());
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
                empSalaryAdjustPerformDTO.setFilingDate(objectsDTO.getAppraisalStartDate());
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
        empSalaryAdjustPlan.setAdjustmentType(adjustmentType.substring(0, adjustmentType.toString().length() - 1));

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
                throw new ServiceException("请选择调整类型");
            }
            if (StringUtils.isNull(effectiveDate)) {
                throw new ServiceException("请选择生效日期");
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
     * @param empSalaryAdjustPlan 隔热膜调薪计划dto
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPlanExcel> exportEmpSalaryAdjustPlan(EmpSalaryAdjustPlan empSalaryAdjustPlan) {
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
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        empSalaryAdjustPlan.setAdjustDepartmentId(departmentId);
        return empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlan);
    }

    /**
     * 根据岗位ID集合获取个人调薪
     *
     * @param postId 岗位ID
     * @return List
     */
    @Override
    public List<EmpSalaryAdjustPlanDTO> selectByPostId(Long postId) {
        EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        empSalaryAdjustPlan.setAdjustPostId(postId);
        return empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(empSalaryAdjustPlan);
    }

    /**
     * 个人调薪到达生效日期更新员工信息
     *
     * @return int
     */
    @Override
    @Transactional
    public int empAdjustUpdate() {
        EmpSalaryAdjustPlan selectSalaryAdjustPlan = new EmpSalaryAdjustPlan();
        Map<String, Object> params = new HashMap<>();
        params.put("nowDate", DateUtils.getNowDate());
        selectSalaryAdjustPlan.setParams(params);
        selectSalaryAdjustPlan.setStatus(1);
        List<EmpSalaryAdjustPlanDTO> empSalaryAdjustPlanDTOS = empSalaryAdjustPlanMapper.selectEmpSalaryAdjustPlanList(selectSalaryAdjustPlan);
        if (StringUtils.isEmpty(empSalaryAdjustPlanDTOS)) {
            return 1;
        }
        Map<Long, List<EmpSalaryAdjustPlanDTO>> map = empSalaryAdjustPlanDTOS.stream().collect(Collectors.groupingBy(EmpSalaryAdjustPlanDTO::getEmployeeId));
        // 更新状态
        ArrayList<EmpSalaryAdjustPlan> updateEmpSalaryAdjustPlan = new ArrayList<>();
        for (EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO : empSalaryAdjustPlanDTOS) {
            EmpSalaryAdjustPlan empSalaryAdjustPlan = new EmpSalaryAdjustPlan();
            empSalaryAdjustPlan.setEmpSalaryAdjustPlanId(empSalaryAdjustPlanDTO.getEmpSalaryAdjustPlanId());
            empSalaryAdjustPlan.setStatus(2);
            empSalaryAdjustPlan.setUpdateBy(SecurityUtils.getUserId());
            empSalaryAdjustPlan.setUpdateTime(DateUtils.getNowDate());
            updateEmpSalaryAdjustPlan.add(empSalaryAdjustPlan);
        }
        empSalaryAdjustPlanMapper.updateEmpSalaryAdjustPlans(updateEmpSalaryAdjustPlan);
        List<EmployeeSalarySnapVO> employeeSalarySnapVOS = new ArrayList<>();
        for (EmpSalaryAdjustPlanDTO empSalaryAdjustPlanDTO : empSalaryAdjustPlanDTOS) {
            EmployeeSalarySnapVO employeeSalarySnapVO = new EmployeeSalarySnapVO();
            BeanUtils.copyProperties(empSalaryAdjustPlanDTO, employeeSalarySnapVO);
            String adjustmentType = empSalaryAdjustPlanDTO.getAdjustmentType();
            List<String> adjustmentTypeList = Arrays.asList(adjustmentType.split(","));
            List<Integer> adjustmentTypeList2 = adjustmentTypeList.stream().map(Integer::parseInt).collect(Collectors.toList());
            employeeSalarySnapVO.setAdjustmentTypeList(adjustmentTypeList2);
            employeeSalarySnapVOS.add(employeeSalarySnapVO);
        }
        R<Integer> empAdjustR = employeeService.empAdjustUpdates(employeeSalarySnapVOS, SecurityConstants.FROM_SOURCE);
        if (empAdjustR.getCode() != 200) {
            throw new ServiceException(empAdjustR.getMsg());
//            throw new ServiceException("个人调薪到达生效日期更新员工信息失败 请联系管理员");
        }
        return 1;
    }

    @Override
    public void handleResult(List<EmpSalaryAdjustPlanDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(EmpSalaryAdjustPlanDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }
}

