package net.qixiaowei.operate.cloud.service.impl.performance;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalExcel;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalObjectsService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


/**
 * PerformanceAppraisalService业务层处理
 *
 * @author Graves
 * @since 2022-11-23
 */
@Service
public class PerformanceAppraisalServiceImpl implements IPerformanceAppraisalService {
    @Autowired
    private PerformanceAppraisalMapper performanceAppraisalMapper;

    @Autowired
    private IPerformanceAppraisalObjectsService performanceAppraisalObjectsService;

    @Autowired
    private RemoteDepartmentService departmentService;

    @Autowired
    private RemoteEmployeeService employeeService;

    @Autowired
    private RemoteOfficialRankSystemService officialRankSystemService;

    /**
     * 查询绩效考核表
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 绩效考核表
     */
    @Override
    public PerformanceAppraisalDTO selectPerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId) {
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        List<OfficialRankSystemDTO> officialRankSystemDTOS = getOfficialRankSystemDTOS();
        for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDTOS) {
            if (appraisal.getPerformanceRankId().equals(officialRankSystemDTO.getOfficialRankSystemId())) {
                appraisal.setPerformanceRankName(officialRankSystemDTO.getOfficialRankSystemName());
                break;
            }
        }
        int appraisalObject = appraisal.getAppraisalObject();
        if (appraisalObject == 1) {// 组织
            List<DepartmentDTO> departmentData = getDepartmentData();
        } else {
            List<EmployeeDTO> employeeData = getEmployeeData();
        }
        // 考核周期类型/考核周期
        switch (appraisal.getCycleType()) {
            case 1:
                appraisal.setCycleTypeName("月度");
                appraisal.setCycleNumberName(appraisal.getCycleNumber().toString() + "月");
                break;
            case 2:
                appraisal.setCycleTypeName("季度");
                appraisal.setCycleNumberName(appraisal.getCycleNumber().toString() + "季度");
                break;
            case 3:
                appraisal.setCycleTypeName("半年度");
                if (appraisal.getCycleNumber() == 1) {
                    appraisal.setCycleNumberName("上半年");
                } else {
                    appraisal.setCycleNumberName("下半年");
                }
                break;
            case 4:
                appraisal.setCycleTypeName("年度");
                appraisal.setCycleNumberName("一整年");
                break;
        }
        // 考核状态
        switch (appraisal.getAppraisalStatus()) {
            case 1:
                appraisal.setAppraisalStatusName("制定目标");
                break;
            case 2:
                appraisal.setAppraisalStatusName("评议");
                break;
            case 3:
                appraisal.setAppraisalStatusName("排名");
                break;
            case 4:
                appraisal.setAppraisalStatusName("归档");
                break;
        }
        // 考核流程
        switch (appraisal.getAppraisalFlow()) {
            case 1:
                appraisal.setAppraisalFlowName("系统流程");
                break;
            case 2:
                appraisal.setAppraisalFlowName("仅导入结果");
                break;
        }
        return appraisal;
    }

    /**
     * 查询绩效考核表列表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 绩效考核表
     */
    @Override
    public List<PerformanceAppraisalDTO> selectPerformanceAppraisalList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        List<OfficialRankSystemDTO> officialRankSystemDTOS = getOfficialRankSystemDTOS();
        performanceAppraisalDTOS.forEach(appraisal -> {
            // 考核周期类型/考核周期
            switch (appraisal.getCycleType()) {
                case 1:
                    appraisal.setCycleTypeName("月度");
                    appraisal.setCycleNumberName(appraisal.getCycleNumber().toString() + "月");
                    break;
                case 2:
                    appraisal.setCycleTypeName("季度");
                    appraisal.setCycleNumberName(appraisal.getCycleNumber().toString() + "季度");
                    break;
                case 3:
                    appraisal.setCycleTypeName("半年度");
                    if (appraisal.getCycleNumber() == 1) {
                        appraisal.setCycleNumberName("上半年");
                    } else {
                        appraisal.setCycleNumberName("下半年");
                    }
                    break;
                case 4:
                    appraisal.setCycleTypeName("年度");
                    appraisal.setCycleNumberName("一整年");
                    break;
            }
            // 考核状态
            switch (appraisal.getAppraisalStatus()) {
                case 1:
                    appraisal.setAppraisalStatusName("制定目标");
                    break;
                case 2:
                    appraisal.setAppraisalStatusName("评议");
                    break;
                case 3:
                    appraisal.setAppraisalStatusName("排名");
                    break;
                case 4:
                    appraisal.setAppraisalStatusName("归档");
                    break;
            }
            // 考核流程
            switch (appraisal.getAppraisalFlow()) {
                case 1:
                    appraisal.setAppraisalFlowName("系统流程");
                    break;
                case 2:
                    appraisal.setAppraisalFlowName("仅导入结果");
                    break;
            }
            for (OfficialRankSystemDTO officialRankSystemDTO : officialRankSystemDTOS) {
                if (officialRankSystemDTO.getOfficialRankSystemId().equals(appraisal.getPerformanceRankId())) {
                    appraisal.setPerformanceRankName(officialRankSystemDTO.getOfficialRankSystemName());
                    break;
                }
            }
        });
        return performanceAppraisalDTOS;
    }

    /**
     * 获取职级体系等级列表
     *
     * @return List
     */
    private List<OfficialRankSystemDTO> getOfficialRankSystemDTOS() {
        R<List<OfficialRankSystemDTO>> listR = officialRankSystemService.selectAll(SecurityConstants.INNER);
        List<OfficialRankSystemDTO> officialRankSystemDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程获取职级体系等级失败");
        }
        if (StringUtils.isEmpty(officialRankSystemDTOS)) {
            throw new ServiceException("职级体系等级数据为空 请检查职级等级配置");
        }
        return officialRankSystemDTOS;
    }

    /**
     * 新增绩效考核表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    @Override
    @Transactional
    public int insertPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = checkAppraisal(performanceAppraisalDTO);
        Integer appraisalObject = performanceAppraisalDTO.getAppraisalObject();
        Integer appraisalYear = performanceAppraisalDTO.getAppraisalYear();
        Integer appraisalFlow = performanceAppraisalDTO.getAppraisalFlow();// 考核流程
        List<DepartmentDTO> departmentData = null;
        List<EmployeeDTO> employeeData = null;
        if (appraisalObject == 1) {// 组织
            departmentData = getDepartmentData();
        } else {
            employeeData = getEmployeeData();
        }
        // 周期性考核标记:0否;1是
        if (performanceAppraisalDTO.getCycleFlag().equals(1)) {
            Integer cycleType = performanceAppraisalDTO.getCycleType();
            int monthNow;
            int quarterNow;
            int halfYearNow;
            int yearNow = DateUtils.getYear();
            if (appraisalYear < yearNow) {
                throw new ServiceException("过去的年份无法");
            } else if (appraisalYear > yearNow) {
                monthNow = 1;
                quarterNow = 1;
                halfYearNow = 1;
                performanceAppraisalDTO.setCycleNumber(1);
            } else {
                monthNow = DateUtils.getMonth();
                quarterNow = DateUtils.getQuarter();
                halfYearNow = DateUtils.getHalfYears();
            }
            switch (cycleType) {//周期类型:1月度;2季度;3半年度;4年度
                case 1://1月度
                    for (int i = monthNow; i < 13; i++) {
                        addPerformances(DateUtils.getMonthStart(appraisalYear, i), DateUtils.getMonthLast(appraisalYear, i),
                                performanceAppraisalDTO, i, appraisalObject, departmentData,
                                performanceAppraisalObjectsDTOS, appraisalFlow, employeeData);
                    }
                    return 1;
                case 2: //2季度
                    for (int i = quarterNow; i < 5; i++) {
                        addPerformances(DateUtils.getQuarterStart(appraisalYear, i), DateUtils.getQuarterLast(appraisalYear, i),
                                performanceAppraisalDTO, i, appraisalObject, departmentData,
                                performanceAppraisalObjectsDTOS, appraisalFlow, employeeData);
                    }
                    return 1;
                case 3: //3半年度
                    for (int i = quarterNow; i < 3; i++) {
                        addPerformances(DateUtils.getHalfYearStart(appraisalYear, i), DateUtils.getHalfYearLast(appraisalYear, i),
                                performanceAppraisalDTO, i, appraisalObject, departmentData,
                                performanceAppraisalObjectsDTOS, appraisalFlow, employeeData);
                    }
                    return 1;
                case 4: //4年度
                    List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList;// 对象List
                    performanceAppraisalDTO.setCycleNumber(1);
                    performanceAppraisalDTO.setAppraisalStartDate(DateUtils.toLocalDate(DateUtils.getYearStart(appraisalYear)));
                    performanceAppraisalDTO.setAppraisalEndDate(DateUtils.toLocalDate(DateUtils.getYearLast(appraisalYear)));
                    PerformanceAppraisal performanceAppraisal = setAppraisalValue(performanceAppraisalDTO);
                    performanceAppraisalMapper.insertPerformanceAppraisal(performanceAppraisal);
                    if (appraisalObject == 1) {
                        performanceAppraisalObjectsDTOList = matchDepartmentObject(departmentData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow);
                    } else {
                        performanceAppraisalObjectsDTOList = matchEmployeeObject(employeeData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow);
                    }
                    return performanceAppraisalObjectsService.insertPerformanceAppraisalObjectss(performanceAppraisalObjectsDTOList);
            }
        }
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList;// 对象List
        Integer cycleNumber = performanceAppraisalDTO.getCycleNumber();
        LocalDate appraisalStartDate = performanceAppraisalDTO.getAppraisalStartDate();
        LocalDate appraisalEndDate = performanceAppraisalDTO.getAppraisalEndDate();
        if (StringUtils.isNull(cycleNumber) && StringUtils.isNull(appraisalEndDate) && StringUtils.isNull(appraisalStartDate)) {
            throw new ServiceException("开始时间,结束时间和周期不能为空");
        }
        PerformanceAppraisal performanceAppraisal = setAppraisalValue(performanceAppraisalDTO);
        performanceAppraisalMapper.insertPerformanceAppraisal(performanceAppraisal);
        if (appraisalObject == 1) {
            performanceAppraisalObjectsDTOList = matchDepartmentObject(departmentData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow);
        } else {
            performanceAppraisalObjectsDTOList = matchEmployeeObject(employeeData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow);
        }
        return performanceAppraisalObjectsService.insertPerformanceAppraisalObjectss(performanceAppraisalObjectsDTOList);
    }

    /**
     * @param startTime                       开始时间
     * @param endTime                         结束时间
     * @param performanceAppraisalDTO         考核任务DTO
     * @param i                               索引
     * @param appraisalObject                 对象类型-1组织/2人员
     * @param departmentData                  组织数据
     * @param employeeData                    人员数据
     * @param performanceAppraisalObjectsDTOS 对象信息
     * @param appraisalFlow                   考核类型
     */
    private void addPerformances(Date startTime, Date endTime, PerformanceAppraisalDTO performanceAppraisalDTO, int i,
                                 Integer appraisalObject, List<DepartmentDTO> departmentData, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS,
                                 Integer appraisalFlow, List<EmployeeDTO> employeeData) {
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = new ArrayList<>();// 对象List
        performanceAppraisalDTO.setAppraisalStartDate(DateUtils.toLocalDate(startTime));
        performanceAppraisalDTO.setAppraisalEndDate(DateUtils.toLocalDate(endTime));

        performanceAppraisalDTO.setCycleNumber(i);

        PerformanceAppraisal performanceAppraisal = setAppraisalValue(performanceAppraisalDTO);
        performanceAppraisalMapper.insertPerformanceAppraisal(performanceAppraisal);
        if (appraisalObject == 1) {
            performanceAppraisalObjectsDTOList.addAll(matchDepartmentObject(departmentData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow));
        } else {
            performanceAppraisalObjectsDTOList.addAll(matchEmployeeObject(employeeData, performanceAppraisalObjectsDTOS, performanceAppraisal, appraisalFlow));
        }
        performanceAppraisalObjectsService.insertPerformanceAppraisalObjectss(performanceAppraisalObjectsDTOList);
    }

    /**
     * 新增校验
     *
     * @param performanceAppraisalDTO 考核对象
     * @return
     */
    private List<PerformanceAppraisalObjectsDTO> checkAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO) {
        String appraisalName = performanceAppraisalDTO.getAppraisalName();
        PerformanceAppraisal performanceAppraisalName = new PerformanceAppraisal();
        performanceAppraisalName.setAppraisalName(appraisalName);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisalName);
        if (StringUtils.isNotEmpty(performanceAppraisalDTOS)) {
            throw new ServiceException("考核任务名称重复");
        }
        // 人员-组织 对象
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = performanceAppraisalDTO.getPerformanceAppraisalObjectsDTOS();
        List<Long> performAppraisalObjectsIds = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            Long objectId = performanceAppraisalObjectsDTO.getAppraisalObjectId();
            if (performAppraisalObjectsIds.contains(objectId)) {
                throw new ServiceException("人员/组织对象不可以重复");
            }
            performAppraisalObjectsIds.add(objectId);
        }

        return performanceAppraisalObjectsDTOS;
    }

    /**
     * 获取人员信息
     *
     * @return
     */
    private List<EmployeeDTO> getEmployeeData() {
        R<List<EmployeeDTO>> listR = employeeService.selectRemoteList(new EmployeeDTO(), SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程获取失败");
        }
        if (StringUtils.isEmpty(employeeDTOS)) {
            throw new ServiceException("个人信息为空，请检查");
        }
        return employeeDTOS;
    }

    /**
     * 获取部门信息
     *
     * @return
     */
    private List<DepartmentDTO> getDepartmentData() {
        R<List<DepartmentDTO>> listR = departmentService.selectDepartment(new DepartmentDTO(), SecurityConstants.INNER);
        List<DepartmentDTO> departmentDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程获取失败");
        }
        if (StringUtils.isEmpty(departmentDTOS)) {
            throw new ServiceException("部门信息为空，请检查");
        }
        return departmentDTOS;
    }

    /**
     * 为绩效任务表赋值0
     *
     * @param performanceAppraisalDTO 绩效任务dto
     * @return
     */
    private static PerformanceAppraisal setAppraisalValue(PerformanceAppraisalDTO performanceAppraisalDTO) {
        Integer appraisalFlow = performanceAppraisalDTO.getAppraisalFlow();
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        if (appraisalFlow == 1) {// 系统流程
            performanceAppraisal.setAppraisalStatus(1);
        } else {// 仅导入
            performanceAppraisal.setAppraisalStatus(4);
            performanceAppraisal.setFilingDate(DateUtils.getLocalDate());
        }
        performanceAppraisal.setCreateBy(SecurityUtils.getUserId());
        performanceAppraisal.setCreateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        performanceAppraisal.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return performanceAppraisal;
    }

    /**
     * 给组织DTO对象赋值
     *
     * @param departmentDTOS                  组织
     * @param performanceAppraisalObjectsDTOS 对象列表
     * @param performanceAppraisal            考核DTO
     * @param appraisalFlow                   考核流程
     */
    private List<PerformanceAppraisalObjectsDTO> matchDepartmentObject(List<DepartmentDTO> departmentDTOS, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS,
                                                                       PerformanceAppraisal performanceAppraisal, Integer appraisalFlow) {
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            boolean j = true;
            for (DepartmentDTO departmentDTO : departmentDTOS) {
                if (departmentDTO.getDepartmentId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    performanceAppraisalObjectsDTO.setPerformanceAppraisalId(performanceAppraisal.getPerformanceAppraisalId());
//                  performanceAppraisalObjectsDTO.setAppraisalPrincipalId(departmentDTO.getExaminationLeaderId());
                    j = false;
                    if (appraisalFlow == 1) {// 系统流程
                        performanceAppraisalObjectsDTO.setAppraisalObjectStatus(1);
                    } else {
                        performanceAppraisalObjectsDTO.setAppraisalObjectStatus(4);
                    }
                    break;
                }
            }
            if (j) {
                throw new ServiceException("未找到匹配到的部门信息");
            }
        }
        return performanceAppraisalObjectsDTOS;
    }

    /**
     * 给组织DTO对象赋值
     *
     * @param employeeDTOS                    人员
     * @param performanceAppraisalObjectsDTOS 对象列表
     * @param performanceAppraisal            考核DTO
     * @param appraisalFlow                   考核流程
     */
    private List<PerformanceAppraisalObjectsDTO> matchEmployeeObject(List<EmployeeDTO> employeeDTOS, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS,
                                                                     PerformanceAppraisal performanceAppraisal, Integer appraisalFlow) {
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOS) {
            boolean j = false;
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    performanceAppraisalObjectsDTO.setPerformanceAppraisalId(performanceAppraisal.getPerformanceAppraisalId());
//                  performanceAppraisalObjectsDTO.setAppraisalPrincipalId(departmentDTO.getExaminationLeaderId());
                    j = true;
                    if (appraisalFlow == 1) {// 系统流程
                        performanceAppraisalObjectsDTO.setAppraisalObjectStatus(1);
                    } else {
                        performanceAppraisalObjectsDTO.setAppraisalObjectStatus(4);
                    }
                    break;
                }
            }
            if (j) {
                throw new ServiceException("未找到匹配到的员工信息");
            }
        }
        return performanceAppraisalObjectsDTOS;
    }

    /**
     * 修改绩效考核表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    @Override
    public int updatePerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalMapper.updatePerformanceAppraisal(performanceAppraisal);
    }

    /**
     * 逻辑批量删除绩效考核表
     *
     * @param performanceAppraisalIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeletePerformanceAppraisalByPerformanceAppraisalIds(List<Long> performanceAppraisalIds) {
        return performanceAppraisalMapper.logicDeletePerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效考核表信息
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 结果
     */
    @Override
    public int deletePerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId) {
        return performanceAppraisalMapper.deletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
    }

    /**
     * 逻辑删除绩效考核表信息
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    @Override
    public int logicDeletePerformanceAppraisalByPerformanceAppraisalId(PerformanceAppraisalDTO
                                                                               performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        performanceAppraisal.setPerformanceAppraisalId(performanceAppraisalDTO.getPerformanceAppraisalId());
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalMapper.logicDeletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisal);
    }

    /**
     * 物理删除绩效考核表信息
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */

    @Override
    public int deletePerformanceAppraisalByPerformanceAppraisalId(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        return performanceAppraisalMapper.deletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisal.getPerformanceAppraisalId());
    }

    /**
     * 物理批量删除绩效考核表
     *
     * @param performanceAppraisalDtos 需要删除的绩效考核表主键
     * @return 结果
     */

    @Override
    public int deletePerformanceAppraisalByPerformanceAppraisalIds
    (List<PerformanceAppraisalDTO> performanceAppraisalDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDtos) {
            stringList.add(performanceAppraisalDTO.getPerformanceAppraisalId());
        }
        return performanceAppraisalMapper.deletePerformanceAppraisalByPerformanceAppraisalIds(stringList);
    }

    /**
     * 批量新增绩效考核表信息
     *
     * @param performanceAppraisalDtos 绩效考核表对象
     */

    public int insertPerformanceAppraisals(List<PerformanceAppraisal> performanceAppraisalDtos) {
        return performanceAppraisalMapper.batchPerformanceAppraisal(performanceAppraisalDtos);
    }

    /**
     * 批量修改绩效考核表信息
     *
     * @param performanceAppraisalDtos 绩效考核表对象
     */

    public int updatePerformanceAppraisals(List<PerformanceAppraisalDTO> performanceAppraisalDtos) {
        List<PerformanceAppraisal> performanceAppraisalList = new ArrayList<>();

        for (PerformanceAppraisalDTO performanceAppraisalDTO : performanceAppraisalDtos) {
            PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
            BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
            performanceAppraisal.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisal.setCreateTime(DateUtils.getNowDate());
            performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalList.add(performanceAppraisal);
        }
        return performanceAppraisalMapper.updatePerformanceAppraisals(performanceAppraisalList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importPerformanceAppraisal(List<PerformanceAppraisalExcel> list) {
        List<PerformanceAppraisal> performanceAppraisalList = new ArrayList<>();
        list.forEach(l -> {
            PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
            BeanUtils.copyProperties(l, performanceAppraisal);
            performanceAppraisal.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisal.setCreateTime(DateUtils.getNowDate());
            performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisal.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceAppraisalList.add(performanceAppraisal);
        });
        try {
            performanceAppraisalMapper.batchPerformanceAppraisal(performanceAppraisalList);
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param performanceAppraisalDTO
     * @return
     */
    @Override
    public List<PerformanceAppraisalExcel> exportPerformanceAppraisal(PerformanceAppraisalDTO
                                                                              performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOList = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        List<PerformanceAppraisalExcel> performanceAppraisalExcelList = new ArrayList<>();
        return performanceAppraisalExcelList;
    }
}

