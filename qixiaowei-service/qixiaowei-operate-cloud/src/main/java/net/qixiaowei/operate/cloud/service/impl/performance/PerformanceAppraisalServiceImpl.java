package net.qixiaowei.operate.cloud.service.impl.performance;

import com.alibaba.excel.EasyExcel;
import com.alibaba.excel.read.builder.ExcelReaderBuilder;
import com.alibaba.fastjson.JSONObject;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalColumnsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalExcel;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalColumnsService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalObjectsService;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;


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
    private IPerformanceAppraisalColumnsService performanceAppraisalColumnsService;

    /**
     * 查询绩效考核表详情
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 绩效考核表
     */
    @Override
    public PerformanceAppraisalDTO selectPerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId) {
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前考核任务已不存在");
        }
        setFieldName(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOList)) {
            return appraisal;
        }
        setCodeAndName(appraisal, performanceAppraisalObjectsDTOList);
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOList);
        return appraisal;
    }

    /**
     * 查询组织绩效任务考核详情
     *
     * @param performanceAppraisalId 绩效考核表
     * @return List
     */
    @Override
    public PerformanceAppraisalDTO selectOrgAppraisalArchiveById(Long performanceAppraisalId) {
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前考核任务已不存在");
        }
        if (!appraisal.getAppraisalObject().equals(1)) {
            throw new ServiceException("当前考核对象不是组织的");
        }
        setFieldName(appraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        Integer appraisalObject = appraisal.getAppraisalObject();
        List<DepartmentDTO> departmentData;
        List<EmployeeDTO> employeeData;
        HashMap<String, BigDecimal> performanceRankMap = new HashMap<>();
        Integer sum = 0;
        if (appraisalObject == 1) {//组织
            departmentData = getDepartmentData();
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                sum = setRankMap(performanceRankMap, performanceAppraisalObjectsDTO, sum);
                for (DepartmentDTO departmentDTO : departmentData) {
                    if (departmentDTO.getDepartmentId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                        performanceAppraisalObjectsDTO.setAppraisalObjectName(departmentDTO.getDepartmentName());
                        performanceAppraisalObjectsDTO.setAppraisalObjectCode(departmentDTO.getDepartmentCode());
                        break;
                    }
                }
            }
        } else {
            employeeData = getEmployeeData();
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                sum = setRankMap(performanceRankMap, performanceAppraisalObjectsDTO, sum);
                for (EmployeeDTO employeeDTO : employeeData) {
                    if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                        performanceAppraisalObjectsDTO.setAppraisalObjectName(employeeDTO.getEmployeeName());
                        performanceAppraisalObjectsDTO.setAppraisalObjectCode(employeeDTO.getEmployeeCode());
                        break;
                    }
                }
            }
        }
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOList);
        List<Map<String, Object>> performanceAppraisalRankList = new ArrayList<>();
        Map<String, Object> map = new HashMap<>();
        performanceRankMap.remove(null);
        if (StringUtils.isEmpty(performanceRankMap)) {
            return appraisal;
        }
        for (String rank : performanceRankMap.keySet()) {
            BigDecimal number = performanceRankMap.get(rank);
            if (sum.equals(0)) {
                return appraisal;
            }
            BigDecimal proportion = number.divide(new BigDecimal(sum), 2, RoundingMode.HALF_UP).multiply(new BigDecimal(100));
            map.put("performanceRankName", rank);
            map.put("number", number);
            map.put("proportion", proportion);
            performanceAppraisalRankList.add(map);
        }
        appraisal.setPerformanceAppraisalRankDTOS(performanceAppraisalRankList);
        return appraisal;
    }

    /**
     * 查询组织绩效归档结果排名
     *
     * @param appraisalObjectsIds    考核对象ID集合
     * @param performanceAppraisalId 绩效考核ID
     * @return List
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalRankByDTO(List<Long> appraisalObjectsIds, Long performanceAppraisalId) {
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("请输入绩效考核ID");
        }
        if (StringUtils.isEmpty(appraisalObjectsIds)) {
            return new ArrayList<>();
        }
        PerformanceAppraisalDTO appraisal = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(appraisal)) {
            throw new ServiceException("当前绩效考核已不存在");
        }
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOS = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByIds(appraisalObjectsIds, performanceAppraisalId);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOS)) {
            return performanceAppraisalObjectsDTOS;
        }
        setCodeAndName(appraisal, performanceAppraisalObjectsDTOS);
        appraisal.setPerformanceAppraisalObjectsDTOS(performanceAppraisalObjectsDTOS);
        return performanceAppraisalObjectsDTOS;
    }

    /**
     * 给对象赋值 名称和编码
     *
     * @param appraisal                          考核表
     * @param performanceAppraisalObjectsDTOList 绩效考核对象
     */
    private void setCodeAndName(PerformanceAppraisalDTO appraisal, List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList) {
        Integer appraisalObject = appraisal.getAppraisalObject();
        List<DepartmentDTO> departmentData;
        List<EmployeeDTO> employeeData;
        if (appraisalObject == 1) {//组织
            departmentData = getDepartmentData();
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                for (DepartmentDTO departmentDTO : departmentData) {
                    if (departmentDTO.getDepartmentId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                        performanceAppraisalObjectsDTO.setAppraisalObjectName(departmentDTO.getDepartmentName());
                        performanceAppraisalObjectsDTO.setAppraisalObjectCode(departmentDTO.getDepartmentCode());
                        break;
                    }
                }
            }
        } else {
            employeeData = getEmployeeData();
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                for (EmployeeDTO employeeDTO : employeeData) {
                    if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                        performanceAppraisalObjectsDTO.setAppraisalObjectName(employeeDTO.getEmployeeName());
                        performanceAppraisalObjectsDTO.setAppraisalObjectCode(employeeDTO.getEmployeeCode());
                        performanceAppraisalObjectsDTO.setEmployeeDepartmentName(employeeDTO.getEmployeeDepartmentName());
                        break;
                    }
                }
            }
        }
    }

    /**
     * 给绩效等级
     *
     * @param performanceRankMap             绩效比例Map
     * @param performanceAppraisalObjectsDTO 绩效考核DTO
     * @param sum                            总数
     */
    private static Integer setRankMap(HashMap<String, BigDecimal> performanceRankMap, PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO, Integer sum) {
        sum += 1;
        if (!performanceRankMap.containsKey(performanceAppraisalObjectsDTO.getPerformanceRankName())) {
            performanceRankMap.put(performanceAppraisalObjectsDTO.getPerformanceRankName(), BigDecimal.ONE);
        } else {
            BigDecimal performanceSum = performanceRankMap.get(performanceAppraisalObjectsDTO.getPerformanceRankName()).add(BigDecimal.ONE);
            performanceRankMap.put(performanceAppraisalObjectsDTO.getPerformanceRankName(), performanceSum);
        }
        return sum;
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
        if (StringUtils.isEmpty(performanceAppraisalDTOS)) {
            return performanceAppraisalDTOS;
        }
        performanceAppraisalDTOS.forEach(PerformanceAppraisalServiceImpl::setFieldName);
        return performanceAppraisalDTOS;
    }

    /**
     * 查询组织绩效归档
     *
     * @param performanceAppraisalDTO 查询条件
     * @return 绩效考核表
     */
    @Override
    public List<PerformanceAppraisalDTO> selectOrgAppraisalArchiveList(PerformanceAppraisalDTO performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        performanceAppraisal.setAppraisalObject(1);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        performanceAppraisalDTOS.forEach(PerformanceAppraisalServiceImpl::setFieldName);
        for (PerformanceAppraisalDTO appraisalDTO : performanceAppraisalDTOS) {
            if (StringUtils.isNull(appraisalDTO.getFilingDate())) {
                appraisalDTO.setIsFiling(0);
            } else {
                appraisalDTO.setIsFiling(1);
            }
            break;
        }
        return performanceAppraisalDTOS;
    }


    /**
     * 为字段命名
     *
     * @param appraisal 考核任务
     */
    private static void setFieldName(PerformanceAppraisalDTO appraisal) {
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
        // 考核阶段
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
            boolean j = true;
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (employeeDTO.getEmployeeId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
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
        if (StringUtils.isEmpty(performanceAppraisalIds)) {
            throw new ServiceException("请选择要删除的考核任务ID");
        }
        List<PerformanceAppraisalDTO> performanceAppraisalDTOS = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds);
        if (StringUtils.isEmpty(performanceAppraisalDTOS) || performanceAppraisalDTOS.size() != performanceAppraisalIds.size()) {
            throw new ServiceException("要删除的考核任务已不存在");
        }
        performanceAppraisalMapper.logicDeletePerformanceAppraisalByPerformanceAppraisalIds(performanceAppraisalIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList =
                performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalIds(performanceAppraisalIds);
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOList)) {
            return 1;
        }
        List<Long> performanceAppraisalObjectsIds = new ArrayList<>();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectsIds.add(performanceAppraisalObjectsDTO.getPerformanceAppraisalId());
        }
        return performanceAppraisalObjectsService.logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(performanceAppraisalObjectsIds);
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
        Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
        if (StringUtils.isNull(performanceAppraisalId)) {
            throw new ServiceException("请输入考核任务ID");
        }
        PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        if (StringUtils.isNull(performanceAppraisalById)) {
            throw new ServiceException("当前考核任务不存在");
        }
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        performanceAppraisal.setPerformanceAppraisalId(performanceAppraisalDTO.getPerformanceAppraisalId());
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        performanceAppraisalMapper.logicDeletePerformanceAppraisalByPerformanceAppraisalId(performanceAppraisal);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
        List<Long> performanceAppraisalObjectIds = new ArrayList<>();
        if (StringUtils.isEmpty(performanceAppraisalObjectsDTOList)) {
            return 1;
        }
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            performanceAppraisalObjectIds.add(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
        }
        performanceAppraisalObjectsService.logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(performanceAppraisalObjectIds);
        return 1;
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
     * 导入系统的组织绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    @Override
    public void importSysOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        try {
            Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
            if (StringUtils.isNull(performanceAppraisalId)) {
                throw new ServiceException("绩效考核ID为空");
            }
            String filename = file.getOriginalFilename();
            if (StringUtils.isBlank(filename)) {
                throw new RuntimeException("请上传文件!");
            }
            if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
                throw new RuntimeException("请上传正确的excel文件!");
            }
            PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
            if (StringUtils.isNull(performanceAppraisalById)) {
                throw new ServiceException("当前绩效考核任务已不存在");
            }
            List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
            List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalMapper.selectRankFactorByAppraisalId(performanceAppraisalId);
            Map<String, Long> factorObjectMap = new HashMap<>();
            if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
                throw new ServiceException("当前绩效等级系数已不存在 请检查绩效等级");
            }
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                factorObjectMap.put(performanceRankFactorDTO.getPerformanceRankName(), performanceRankFactorDTO.getPerformanceRankFactorId());
            }
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
    }

    /**
     * 导入自定义的组织绩效考核Excel
     *
     * @param performanceAppraisalDTO 绩效考核
     * @param file                    文件
     */
    @Override
    @Transactional
    public void importCustomOrgPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO, MultipartFile file) {
        try {
            String filename = file.getOriginalFilename();
            if (StringUtils.isBlank(filename)) {
                throw new RuntimeException("请上传文件!");
            }
            if ((!StringUtils.endsWithIgnoreCase(filename, ".xls") && !StringUtils.endsWithIgnoreCase(filename, ".xlsx"))) {
                throw new RuntimeException("请上传正确的excel文件!");
            }
            Long performanceAppraisalId = performanceAppraisalDTO.getPerformanceAppraisalId();
            if (StringUtils.isNull(performanceAppraisalId)) {
                throw new ServiceException("绩效考核ID为空");
            }
            PerformanceAppraisalDTO performanceAppraisalById = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
            if (StringUtils.isNull(performanceAppraisalById)) {
                throw new ServiceException("当前绩效考核任务已不存在");
            }
            List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
            List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalMapper.selectRankFactorByAppraisalId(performanceAppraisalId);
            Map<String, Long> factorObjectMap = new HashMap<>();
            if (StringUtils.isEmpty(performanceRankFactorDTOS)) {
                throw new ServiceException("当前绩效等级系数已不存在 请检查绩效等级");
            }
            for (PerformanceRankFactorDTO performanceRankFactorDTO : performanceRankFactorDTOS) {
                factorObjectMap.put(performanceRankFactorDTO.getPerformanceRankName(), performanceRankFactorDTO.getPerformanceRankFactorId());
            }
            Integer appraisalObject = performanceAppraisalById.getAppraisalObject();
            List<DepartmentDTO> departmentData;
            Map<String, Long> orgObjectMap = new HashMap<>();
            departmentData = getDepartmentData();
            for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
                for (DepartmentDTO departmentDTO : departmentData) {
                    if (departmentDTO.getDepartmentId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                        orgObjectMap.put(departmentDTO.getDepartmentName(), performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
                        break;
                    }
                }
            }
            ExcelReaderBuilder read = EasyExcel.read(file.getInputStream());
            List<Map<Integer, String>> listMap = read.doReadAllSync();
            Map<Integer, String> head = listMap.get(2);
            listMap.remove(listMap.size() - 1);
            listMap.remove(2);
            listMap.remove(1);
            listMap.remove(0);
            if (StringUtils.isEmpty(listMap)) {
                throw new ServiceException("绩效考核Excel没有数据 请检查");
            }
            List<List<String>> dataList = new ArrayList<>();
            for (int i = 2; i < head.keySet().size(); i++) {
                dataList.add(new ArrayList<>());
            }
            for (int i = 0; i < listMap.size(); i++) {
                Map<Integer, String> valueMap = listMap.get(i);
//                performanceAppraisalObjectsDTO.setSelfDefinedColumnsFlag(1);// 是否自定义
                for (int j = 2; j < head.keySet().size(); j++) {
                    dataList.get(j - 2).add(valueMap.get(j));
                }
            }
            List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsAfter = new ArrayList<>();
            for (int j = 2; j < head.keySet().size(); j++) {
                PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO = new PerformanceAppraisalColumnsDTO();
                String value = JSONObject.toJSON(dataList.get(j - 2)).toString();
                performanceAppraisalColumnsDTO.setSort(j);
                performanceAppraisalColumnsDTO.setPerformanceAppraisalId(performanceAppraisalId);
                performanceAppraisalColumnsDTO.setColumnName(head.get(j));
                performanceAppraisalColumnsDTO.setColumnValue(value);
                performanceAppraisalColumnsAfter.add(performanceAppraisalColumnsDTO);
            }
            List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsBefore =
                    performanceAppraisalColumnsService.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
            for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : performanceAppraisalColumnsAfter) {
                for (PerformanceAppraisalColumnsDTO appraisalColumnsDTO : performanceAppraisalColumnsBefore) {
                    if (appraisalColumnsDTO.getColumnName().equals(performanceAppraisalColumnsDTO.getColumnName())) {
                        performanceAppraisalColumnsDTO.setPerformAppraisalColumnsId(appraisalColumnsDTO.getPerformAppraisalColumnsId());
                        break;
                    }
                }
            }
            // 交集
            List<PerformanceAppraisalColumnsDTO> updatePerformanceColumns =
                    performanceAppraisalColumnsAfter.stream().filter(performanceAppraisalColumnsDTO ->
                            performanceAppraisalColumnsBefore.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                    .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                    ).collect(Collectors.toList());
            // 差集 Before中After的补集
            List<PerformanceAppraisalColumnsDTO> delPerformanceColumns =
                    performanceAppraisalColumnsBefore.stream().filter(performanceAppraisalColumnsDTO ->
                            !performanceAppraisalColumnsAfter.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                    .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                    ).collect(Collectors.toList());
            // 差集 After中Before的补集
            List<PerformanceAppraisalColumnsDTO> addPerformanceColumns =
                    performanceAppraisalColumnsAfter.stream().filter(performanceAppraisalColumnsDTO ->
                            !performanceAppraisalColumnsBefore.stream().map(PerformanceAppraisalColumnsDTO::getColumnName)
                                    .collect(Collectors.toList()).contains(performanceAppraisalColumnsDTO.getColumnName())
                    ).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(updatePerformanceColumns)) {
                performanceAppraisalColumnsService.updatePerformanceAppraisalColumnss(updatePerformanceColumns);
            }
            if (StringUtils.isNotEmpty(delPerformanceColumns)) {
                List<Long> deleteIds = new ArrayList<>();
                for (PerformanceAppraisalColumnsDTO delPerformanceColumn : delPerformanceColumns) {
                    deleteIds.add(delPerformanceColumn.getPerformAppraisalColumnsId());
                }
                performanceAppraisalColumnsService.logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(deleteIds);
            }
            if (StringUtils.isNotEmpty(addPerformanceColumns)) {
                performanceAppraisalColumnsService.insertPerformanceAppraisalColumnss(addPerformanceColumns);
            }
        } catch (IOException e) {
            throw new ServiceException("导入绩效考核表Excel失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param performanceAppraisalDTO 绩效考核DTO
     * @return List
     */
    @Override
    public List<PerformanceAppraisalExcel> exportPerformanceAppraisal(PerformanceAppraisalDTO
                                                                              performanceAppraisalDTO) {
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        BeanUtils.copyProperties(performanceAppraisalDTO, performanceAppraisal);
        List<PerformanceAppraisalDTO> performanceAppraisalDTOList = performanceAppraisalMapper.selectPerformanceAppraisalList(performanceAppraisal);
        return new ArrayList<>();
    }

    /**
     * 根据appraisalId查询对象列表
     *
     * @param appraisalId 绩效任务ID
     * @return List
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectAppraisalObjectList(Long appraisalId) {
        if (StringUtils.isNull(appraisalId)) {
            throw new ServiceException("绩效考核任务ID为空 无法导出");
        }
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsService.selectPerformanceAppraisalObjectsByPerformAppraisalId(appraisalId);
        List<DepartmentDTO> departmentData = getDepartmentData();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDTOList) {
            for (DepartmentDTO departmentDTO : departmentData) {
                if (departmentDTO.getDepartmentId().equals(performanceAppraisalObjectsDTO.getAppraisalObjectId())) {
                    performanceAppraisalObjectsDTO.setAppraisalObjectName(departmentDTO.getDepartmentName());
                    break;
                }
            }
        }
        return performanceAppraisalObjectsDTOList;
    }

    /**
     * 根据绩效考核ID获取绩效下拉列表
     *
     * @param appraisalId 绩效任务ID
     * @return List
     */
    @Override
    public List<PerformanceRankFactorDTO> selectPerformanceRankFactor(Long appraisalId) {
        return performanceAppraisalMapper.selectRankFactorByAppraisalId(appraisalId);
    }

    /**
     * 归档组织
     *
     * @param performanceAppraisalId 绩效任务ID
     * @return int
     */
    @Override
    public int archiveOrg(Long performanceAppraisalId) {
        PerformanceAppraisalDTO performanceAppraisalByPerformanceAppraisalId = performanceAppraisalMapper.selectPerformanceAppraisalByPerformanceAppraisalId(performanceAppraisalId);
        LocalDate filingDate = performanceAppraisalByPerformanceAppraisalId.getFilingDate();
        if (StringUtils.isNotNull(filingDate)) {
            throw new ServiceException("当前组织绩效任务已归档");
        }
        PerformanceAppraisal performanceAppraisal = new PerformanceAppraisal();
        performanceAppraisal.setPerformanceAppraisalId(performanceAppraisalId);
        performanceAppraisal.setFilingDate(DateUtils.getLocalDate());
        performanceAppraisal.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisal.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalMapper.updatePerformanceAppraisal(performanceAppraisal);
    }
}

