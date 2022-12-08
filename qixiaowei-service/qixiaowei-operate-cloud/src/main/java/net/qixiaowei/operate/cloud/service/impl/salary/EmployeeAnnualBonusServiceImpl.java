package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptAnnualBonus;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpAnnualBonusObjects;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpAnnualBonusSnapshot;
import net.qixiaowei.operate.cloud.api.domain.salary.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.*;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalMapper;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalObjectsMapper;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceRankFactorMapper;
import net.qixiaowei.operate.cloud.mapper.salary.*;
import net.qixiaowei.operate.cloud.service.salary.IEmployeeAnnualBonusService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;


/**
 * EmployeeAnnualBonusService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-12-02
 */
@Service
public class EmployeeAnnualBonusServiceImpl implements IEmployeeAnnualBonusService {
    @Autowired
    private EmployeeAnnualBonusMapper employeeAnnualBonusMapper;
    @Autowired
    private EmpAnnualBonusObjectsMapper empAnnualBonusObjectsMapper;
    @Autowired
    private EmpAnnualBonusSnapshotMapper empAnnualBonusSnapshotMapper;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private SalaryPayMapper salaryPayMapper;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private PerformanceAppraisalObjectsMapper performanceAppraisalObjectsMapper;
    @Autowired
    private DeptAnnualBonusMapper deptAnnualBonusMapper;

    /**
     * 查询个人年终奖表
     *
     * @param employeeAnnualBonusId 个人年终奖表主键
     * @return 个人年终奖表
     */
    @Override
    public EmployeeAnnualBonusDTO selectEmployeeAnnualBonusByEmployeeAnnualBonusId(Long employeeAnnualBonusId) {
        EmployeeAnnualBonusDTO employeeAnnualBonusDTO = employeeAnnualBonusMapper.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId);
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsAndSnapshot(employeeAnnualBonusId);
        employeeAnnualBonusDTO.setEmpAnnualBonusSnapshotDTOs(empAnnualBonusSnapshotDTOList);
        return employeeAnnualBonusDTO;
    }

    /**
     * 查询个人年终奖表列表
     *
     * @param employeeAnnualBonusDTO 个人年终奖表
     * @return 个人年终奖表
     */
    @Override
    public List<EmployeeAnnualBonusDTO> selectEmployeeAnnualBonusList(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
        BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
        //列表数据
        List<EmployeeAnnualBonusDTO> employeeAnnualBonusDTOS = employeeAnnualBonusMapper.selectEmployeeAnnualBonusList(employeeAnnualBonus);
        if (StringUtils.isNotEmpty(employeeAnnualBonusDTOS)) {
            //申请年终奖总额
            packApplyBonusAmount(employeeAnnualBonusDTOS);
        }
        return employeeAnnualBonusDTOS;
    }

    /**
     * 封装申请年终奖总额
     *
     * @param employeeAnnualBonusDTOS
     */
    private void packApplyBonusAmount(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDTOS) {
        for (EmployeeAnnualBonusDTO annualBonusDTO : employeeAnnualBonusDTOS) {
            List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOS = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsByEmployeeAnnualBonusId(annualBonusDTO.getEmployeeAnnualBonusId());
            if (StringUtils.isNotEmpty(empAnnualBonusObjectsDTOS)) {
                //申请年终奖金额
                BigDecimal applyBonusAmount = new BigDecimal("0");
                //sterm流求和
                applyBonusAmount = empAnnualBonusObjectsDTOS.stream().map(EmpAnnualBonusObjectsDTO::getCommentValue).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                //申请年终奖金额
                annualBonusDTO.setApplyBonusAmount(applyBonusAmount);
            }
        }
    }

    /**
     * 赋值申请人名称
     *
     * @param employeeAnnualBonusDTOS
     */
    private void packApplyEmployeeName(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDTOS) {
        //申请人ID集合
        List<Long> collect = employeeAnnualBonusDTOS.stream().map(EmployeeAnnualBonusDTO::getApplyEmployeeId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            //远程人员数据赋值申请人名称
            R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(collect, SecurityConstants.INNER);
            List<EmployeeDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (EmployeeAnnualBonusDTO annualBonusDTO : employeeAnnualBonusDTOS) {
                    for (EmployeeDTO datum : data) {
                        if (annualBonusDTO.getApplyEmployeeId() == datum.getEmployeeId()) {
                            annualBonusDTO.setApplyEmployeeName(datum.getEmployeeName());
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装赋值申请部门名称
     *
     * @param employeeAnnualBonusDTOS
     */
    private void packApplyDeptName(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDTOS) {
        //申请部门ID集合
        List<Long> collect = employeeAnnualBonusDTOS.stream().map(EmployeeAnnualBonusDTO::getApplyDepartmentId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            //远程部门数据赋值申请部门名称
            R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
            List<DepartmentDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (EmployeeAnnualBonusDTO annualBonusDTO : employeeAnnualBonusDTOS) {
                    for (DepartmentDTO datum : data) {
                        if (annualBonusDTO.getApplyDepartmentId() == datum.getDepartmentId()) {
                            annualBonusDTO.setApplyDepartmentName(datum.getDepartmentName());
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装赋值部门名称
     *
     * @param employeeAnnualBonusDTOS
     */
    private void packDeptName(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDTOS) {
        //部门ID集合
        List<Long> collect = employeeAnnualBonusDTOS.stream().map(EmployeeAnnualBonusDTO::getDepartmentId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            //远程部门数据赋值部门名称
            R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
            List<DepartmentDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (EmployeeAnnualBonusDTO annualBonusDTO : employeeAnnualBonusDTOS) {
                    for (DepartmentDTO datum : data) {
                        if (annualBonusDTO.getDepartmentId() == datum.getDepartmentId()) {
                            annualBonusDTO.setDepartmentName(datum.getDepartmentName());
                        }
                    }
                }
            }
        }
    }

    /**
     * 新增个人年终奖表
     *
     * @param employeeAnnualBonusDTO 个人年终奖表
     * @return 结果
     */
    @Override
    @Transactional
    public EmployeeAnnualBonusDTO insertEmployeeAnnualBonus(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        //发起评议流程标记:0否;1是
        Integer commentFlag = employeeAnnualBonusDTO.getCommentFlag();
        //个人年终奖发放快照信息及发放对象表集合
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs = employeeAnnualBonusDTO.getEmpAnnualBonusSnapshotDTOs();
        //个人年终奖发放对象集合
        List<EmpAnnualBonusObjects> empAnnualBonusObjectsList = new ArrayList<>();
        //个人年终奖发放快照信息集合
        List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList = new ArrayList<>();
        //个人年终奖表
        EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
        try {
            BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
            employeeAnnualBonus.setCreateBy(SecurityUtils.getUserId());
            employeeAnnualBonus.setCreateTime(DateUtils.getNowDate());
            employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
            employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
            employeeAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            employeeAnnualBonusMapper.insertEmployeeAnnualBonus(employeeAnnualBonus);
        } catch (BeansException e) {
            throw new ServiceException("新增个人年终奖失败");
        }
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOs)) {
            for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOs) {
                //个人年终奖发放对象表
                EmpAnnualBonusObjects empAnnualBonusObjects = new EmpAnnualBonusObjects();
                //个人年终奖发放快照信息表
                EmpAnnualBonusSnapshot empAnnualBonusSnapshot = new EmpAnnualBonusSnapshot();
                BeanUtils.copyProperties(empAnnualBonusSnapshotDTO, empAnnualBonusSnapshot);
                BeanUtils.copyProperties(empAnnualBonusSnapshotDTO, empAnnualBonusObjects);
                //个人年终奖ID
                empAnnualBonusObjects.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
                //个人年终奖ID
                empAnnualBonusSnapshot.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
                empAnnualBonusObjects.setCreateBy(SecurityUtils.getUserId());
                empAnnualBonusObjects.setCreateTime(DateUtils.getNowDate());
                empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);

                empAnnualBonusSnapshot.setCreateBy(SecurityUtils.getUserId());
                empAnnualBonusSnapshot.setCreateTime(DateUtils.getNowDate());
                empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusSnapshot.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                empAnnualBonusObjectsList.add(empAnnualBonusObjects);
                empAnnualBonusSnapshotList.add(empAnnualBonusSnapshot);
            }
        }

        if (StringUtils.isNotEmpty(empAnnualBonusObjectsList)) {
            try {
                empAnnualBonusObjectsMapper.batchEmpAnnualBonusObjects(empAnnualBonusObjectsList);
            } catch (Exception e) {
                throw new ServiceException("批量插入个人年终奖发放对象失败");
            }
        }
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotList) && StringUtils.isNotEmpty(empAnnualBonusObjectsList)) {
            for (int i = 0; i < empAnnualBonusSnapshotList.size(); i++) {
                //个人年终奖发放对象ID
                empAnnualBonusSnapshotList.get(i).setEmpAnnualBonusObjectsId(empAnnualBonusObjectsList.get(i).getEmpAnnualBonusObjectsId());
            }
            try {
                empAnnualBonusSnapshotMapper.batchEmpAnnualBonusSnapshot(empAnnualBonusSnapshotList);
            } catch (Exception e) {
                throw new ServiceException("批量新增个人年终奖发放快照信息失败");
            }
        }
        employeeAnnualBonusDTO.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
        return employeeAnnualBonusDTO;
    }

    /**
     * 修改个人年终奖表
     *
     * @param employeeAnnualBonusDTO 个人年终奖表
     * @return 结果
     */
    @Override
    public int updateEmployeeAnnualBonus(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        //发起评议流程标记:0否;1是
        Integer commentFlag = employeeAnnualBonusDTO.getCommentFlag();
        //状态:0草稿;1待初评;2待评议;3已评议
        Integer status = employeeAnnualBonusDTO.getStatus();
        EmployeeAnnualBonusDTO employeeAnnualBonusDTO1 = employeeAnnualBonusMapper.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
        if (StringUtils.isNull(employeeAnnualBonusDTO1)) {
            throw new ServiceException("数据不存在！ 请联系管理员");
        }
        int i = 0;
        //个人年终奖发放快照信息及发放对象表集合
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs = employeeAnnualBonusDTO.getEmpAnnualBonusSnapshotDTOs();
        //个人年终奖发放对象集合
        List<EmpAnnualBonusObjects> empAnnualBonusObjectsList = new ArrayList<>();
        //个人年终奖发放快照信息集合
        List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList = new ArrayList<>();
        //个人年终奖表
        EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
        try {
            BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
            employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
            employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
            if (commentFlag == 0 || status==2 || status==3) {
                //评议日期评议日期
                employeeAnnualBonus.setCommentDate(DateUtils.getNowDate());
            }
            i = employeeAnnualBonusMapper.updateEmployeeAnnualBonus(employeeAnnualBonus);
        } catch (BeansException e) {
            throw new ServiceException("修改个人年终奖失败");
        }
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOs)) {
            for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOs) {
                //个人年终奖发放对象表
                EmpAnnualBonusObjects empAnnualBonusObjects = new EmpAnnualBonusObjects();
                //个人年终奖发放快照信息表
                EmpAnnualBonusSnapshot empAnnualBonusSnapshot = new EmpAnnualBonusSnapshot();
                BeanUtils.copyProperties(empAnnualBonusSnapshotDTO, empAnnualBonusSnapshot);
                BeanUtils.copyProperties(empAnnualBonusSnapshotDTO, empAnnualBonusObjects);
                empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusObjectsList.add(empAnnualBonusObjects);
                empAnnualBonusSnapshotList.add(empAnnualBonusSnapshot);
            }
        }

        if (StringUtils.isNotEmpty(empAnnualBonusObjectsList)) {
            try {
                empAnnualBonusObjectsMapper.updateEmpAnnualBonusObjectss(empAnnualBonusObjectsList);
            } catch (Exception e) {
                throw new ServiceException("批量修改个人年中奖发放对象失败");
            }
        }
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotList)) {
            try {
                empAnnualBonusSnapshotMapper.updateEmpAnnualBonusSnapshots(empAnnualBonusSnapshotList);
            } catch (Exception e) {
                throw new ServiceException("批量修改个人年终奖发放快照信息失败");
            }
        }
        return i;
    }

    /**
     * 逻辑批量删除个人年终奖表
     *
     * @param employeeAnnualBonusIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(List<Long> employeeAnnualBonusIds) {
        int i = 0;
        List<EmployeeAnnualBonusDTO> employeeAnnualBonusDTOS = employeeAnnualBonusMapper.selectEmployeeAnnualBonusByEmployeeAnnualBonusIds(employeeAnnualBonusIds);
        if (StringUtils.isEmpty(employeeAnnualBonusDTOS)) {
            throw new ServiceException("数据不存在！ 请联系管理员");
        }
        try {
            i = employeeAnnualBonusMapper.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(employeeAnnualBonusIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("逻辑批量删除个人年终奖失败");
        }
        //根据个人年终奖主表id集合批量查询个人年终奖发放快照信息表
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList = empAnnualBonusSnapshotMapper.selectEmpAnnualBonusSnapshotByEmployeeAnnualBonusIds(employeeAnnualBonusIds);
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOList)) {
            List<Long> collect = empAnnualBonusSnapshotDTOList.stream().map(EmpAnnualBonusSnapshotDTO::getEmpAnnualBonusSnapshotId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    empAnnualBonusSnapshotMapper.logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除个人年终奖发放快照信息表失败");
                }
            }
        }
        //根据个人年终奖主表id集合批量查询个人年终奖发放对象表
        List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOS = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsByEmployeeAnnualBonusIds(employeeAnnualBonusIds);
        if (StringUtils.isNotEmpty(empAnnualBonusObjectsDTOS)) {
            List<Long> collect = empAnnualBonusObjectsDTOS.stream().map(EmpAnnualBonusObjectsDTO::getEmpAnnualBonusObjectsId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    empAnnualBonusObjectsMapper.logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除个人年终奖发放对象表失败");
                }
            }
        }
        return i;
    }

    /**
     * 物理删除个人年终奖表信息
     *
     * @param employeeAnnualBonusId 个人年终奖表主键
     * @return 结果
     */
    @Override
    public int deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(Long employeeAnnualBonusId) {
        return employeeAnnualBonusMapper.deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId);
    }

    /**
     * 个人年终奖表选择部门后预制数据
     *
     * @param employeeAnnualBonusDTO
     * @return
     */
    @Override
    public List<EmpAnnualBonusSnapshotDTO> addPrefabricate(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        BigDecimal distributeBonusAmount = new BigDecimal("0");

        DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
        deptAnnualBonus.setAnnualBonusYear(employeeAnnualBonusDTO.getAnnualBonusYear());
        deptAnnualBonus.setDepartmentId(employeeAnnualBonusDTO.getDepartmentId());
        DeptAnnualBonusDTO deptAnnualBonusDTO = deptAnnualBonusMapper.selectDeptAnnualBonusByAnnualBonusYear(deptAnnualBonus);
        if (StringUtils.isNotNull(deptAnnualBonusDTO)){
            distributeBonusAmount=deptAnnualBonusDTO.getDepartmentAnnualBonus();
            //部门年终奖
            employeeAnnualBonusDTO.setDistributeBonusAmount(distributeBonusAmount);
        }


        //所有员工的薪酬奖金合计
        BigDecimal allPaymentBonusSum = new BigDecimal("0");
        //所有员工的奖金金额合计
        BigDecimal allBonusAmountSum = new BigDecimal("0");
        //员工奖金基数=员工倒推12个月的薪酬合计（若期间出现断层，则继续往前倒推，直至取满12个月，若实在无法取满12个月，则可以取几个月就取几个月）
        Map<Long, BigDecimal> paymentBonusSumMap = new HashMap<>();
        //员工上年奖金额=倒推12个月的工资条数据中的总奖金包（若期间出现断层，则继续往前倒推，直至取满12个月，若实在无法取满12个月，则可以取几个月就取几个月）
        Map<Long, BigDecimal> bonusAmountSumMap = new HashMap<>();
        //个人年终奖发放快照信息及发放对象表集合
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList = new ArrayList<>();
        //远程查看部门下人员信息
        R<List<EmployeeDTO>> listR = remoteEmployeeService.selectEmployeeByDepts(employeeAnnualBonusDTO.getDepartmentId(), SecurityConstants.INNER);
        List<EmployeeDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {
            for (EmployeeDTO datum : data) {
                EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO = new EmpAnnualBonusSnapshotDTO();
                BeanUtils.copyProperties(datum, empAnnualBonusSnapshotDTO);
                //部门名称
                empAnnualBonusSnapshotDTO.setDepartmentName(datum.getEmployeeDepartmentName());
                //岗位名称
                empAnnualBonusSnapshotDTO.setPostName(datum.getEmployeePostName());
                //职级
                empAnnualBonusSnapshotDTO.setOfficialRankName(datum.getEmployeeRankName());
                empAnnualBonusSnapshotDTO.setChoiceFlag(0);
                empAnnualBonusSnapshotDTOList.add(empAnnualBonusSnapshotDTO);
            }
        }
        //薪酬情况
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOList)) {
            List<Long> collect = empAnnualBonusSnapshotDTOList.stream().map(EmpAnnualBonusSnapshotDTO::getEmployeeId).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList1 = salaryPayMapper.selectSalaryPayCondition(collect, employeeAnnualBonusDTO.getAnnualBonusYear());
                for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList) {
                    BigDecimal employeeBasicWage = new BigDecimal("0");
                    BigDecimal emolumentBeforeOne = new BigDecimal("0");
                    BigDecimal bonusBeforeOne = new BigDecimal("0");
                    BigDecimal bonusBeforeTwo = new BigDecimal("0");
                    if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOList1)) {
                        for (EmpAnnualBonusSnapshotDTO annualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList1) {
                            if (empAnnualBonusSnapshotDTO.getEmployeeId() == annualBonusSnapshotDTO.getEmployeeId()) {

                                if (null != annualBonusSnapshotDTO.getEmployeeBasicWage()) {
                                    //基本工资
                                    employeeBasicWage = annualBonusSnapshotDTO.getEmployeeBasicWage();
                                }
                                if (null != annualBonusSnapshotDTO.getEmolumentBeforeOne()) {
                                    //前一年总薪酬
                                    emolumentBeforeOne = annualBonusSnapshotDTO.getEmolumentBeforeOne();
                                }
                                if (null != annualBonusSnapshotDTO.getBonusBeforeOne()) {
                                    //前一年奖金
                                    bonusBeforeOne = annualBonusSnapshotDTO.getBonusBeforeOne();
                                }
                                if (null != annualBonusSnapshotDTO.getBonusBeforeTwo()) {
                                    //前二年奖金
                                    bonusBeforeTwo = annualBonusSnapshotDTO.getBonusBeforeTwo();
                                }

                            }
                        }
                    }
                    //基本工资
                    empAnnualBonusSnapshotDTO.setEmployeeBasicWage(employeeBasicWage);
                    //前一年总薪酬
                    empAnnualBonusSnapshotDTO.setEmolumentBeforeOne(emolumentBeforeOne);
                    //前一年奖金
                    empAnnualBonusSnapshotDTO.setBonusBeforeOne(bonusBeforeOne);
                    //前二年奖金
                    empAnnualBonusSnapshotDTO.setBonusBeforeTwo(bonusBeforeTwo);
                }
            }
            //封装绩效
            packperformance(empAnnualBonusSnapshotDTOList);

            //参考值一 二数据计算必须数据
            packPerformanceRank(employeeAnnualBonusDTO, allPaymentBonusSum, allBonusAmountSum, paymentBonusSumMap, bonusAmountSumMap, empAnnualBonusSnapshotDTOList);
            //封装计算参考值
            packReferenceValue(distributeBonusAmount, allPaymentBonusSum, allBonusAmountSum, paymentBonusSumMap, bonusAmountSumMap, empAnnualBonusSnapshotDTOList);
        }

        return empAnnualBonusSnapshotDTOList;
    }

    /**
     * 封装计算参考值
     *
     * @param distributeBonusAmount
     * @param allPaymentBonusSum
     * @param allBonusAmountSum
     * @param paymentBonusSumMap
     * @param bonusAmountSumMap
     * @param empAnnualBonusSnapshotDTOList
     */
    private void packReferenceValue(BigDecimal distributeBonusAmount, BigDecimal allPaymentBonusSum, BigDecimal allBonusAmountSum, Map<Long, BigDecimal> paymentBonusSumMap, Map<Long, BigDecimal> bonusAmountSumMap, List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList) {
        for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList) {
            //参考值一奖金占比 公式=员工奖金基数÷一级部门所有员工奖金基数之和 员工奖金基数=员工倒推12个月的薪酬合计（若期间出现断层，则继续往前倒推，直至取满12个月，若实在无法取满12个月，则可以取几个月就取几个月）*绩效奖金系数*考勤系数
            BigDecimal bonusPercentageOne = new BigDecimal("0");
            //参考值一 公式=一级部门可分配年终奖*奖金占比。
            BigDecimal referenceValueOne = new BigDecimal("0");
            //奖金占比二 公式=员工上年奖金额÷一级部门所有员工上年奖金总额 员工上年奖金额=倒推12个月的工资条数据中的总奖金包（若期间出现断层，则继续往前倒推，直至取满12个月，若实在无法取满12个月，则可以取几个月就取几个月)
            BigDecimal bonusPercentageTwo = new BigDecimal("0");
            //参考值二 公式=一级部门可分配年终奖*奖金占比。
            BigDecimal referenceValueTwo = new BigDecimal("0");
            //薪酬合计
            BigDecimal paymentBonusSum = paymentBonusSumMap.get(empAnnualBonusSnapshotDTO.getEmployeeId());
            //奖金合计
            BigDecimal bonusAmountSum = bonusAmountSumMap.get(empAnnualBonusSnapshotDTO.getEmployeeId());
            if (null != paymentBonusSum && paymentBonusSum.compareTo(new BigDecimal("0")) != 0 &&
                    allPaymentBonusSum.compareTo(new BigDecimal("0")) != 0) {
                bonusPercentageOne = paymentBonusSum.divide(allPaymentBonusSum, 4, BigDecimal.ROUND_HALF_DOWN);
            }
            if (null != distributeBonusAmount && distributeBonusAmount.compareTo(new BigDecimal("0")) != 0 &&
                    bonusPercentageOne.compareTo(new BigDecimal("0")) != 0) {
                referenceValueOne = distributeBonusAmount.multiply(bonusPercentageOne);
            }
            if (null != bonusAmountSum && bonusAmountSum.compareTo(new BigDecimal("0")) != 0 &&
                    allBonusAmountSum.compareTo(new BigDecimal("0")) != 0) {
                bonusPercentageTwo = allBonusAmountSum.divide(bonusAmountSum, 4, BigDecimal.ROUND_HALF_DOWN);
            }
            if (null != distributeBonusAmount && distributeBonusAmount.compareTo(new BigDecimal("0")) != 0 &&
                    bonusPercentageTwo.compareTo(new BigDecimal("0")) != 0) {
                referenceValueTwo = distributeBonusAmount.multiply(bonusPercentageTwo);
            }
            empAnnualBonusSnapshotDTO.setBonusBeforeOne(bonusPercentageOne);
            empAnnualBonusSnapshotDTO.setReferenceValueOne(referenceValueOne);
            empAnnualBonusSnapshotDTO.setBonusBeforeTwo(bonusPercentageTwo);
            empAnnualBonusSnapshotDTO.setReferenceValueTwo(referenceValueTwo);
        }
    }

    /**
     * 封装绩效
     *
     * @param empAnnualBonusSnapshotDTOList
     */
    private void packperformance(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList) {
        for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList) {
            //考勤系数
            empAnnualBonusSnapshotDTO.setAttendanceFactor(new BigDecimal("1"));
            //绩效
            List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByEmployeeId(empAnnualBonusSnapshotDTO.getEmployeeId());
            if (StringUtils.isNotEmpty(performanceRankFactorDTOS)) {
                PerformanceRankFactorDTO performanceRankFactorDTO = performanceRankFactorDTOS.get(1);
                Long performanceRankId = performanceRankFactorDTO.getPerformanceRankId();
                //绩效名称
                empAnnualBonusSnapshotDTO.setPerformanceRank(performanceRankFactorDTO.getPerformanceRankName());
                //绩效等级ID
                empAnnualBonusSnapshotDTO.setPerformanceRankId(performanceRankId);
                //绩效等级下拉框集合
                if (null != performanceRankId){
                    List<PerformanceRankFactorDTO> performanceRankFactorDTOS1 = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
                    if (StringUtils.isNotEmpty(performanceRankFactorDTOS1)){
                        empAnnualBonusSnapshotDTO.setPerformanceRanks(performanceRankFactorDTOS1.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).collect(Collectors.toList()));
                    }
                }
                //绩效等级系数ID
                empAnnualBonusSnapshotDTO.setPerformanceRankFactorId(performanceRankFactorDTO.getPerformanceRankFactorId());

                //绩效奖金系数
                empAnnualBonusSnapshotDTO.setPerformanceBonusFactor(performanceRankFactorDTO.getBonusFactor());
                //最近绩效结果
                empAnnualBonusSnapshotDTO.setLastPerformanceResulted(performanceRankFactorDTOS.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).filter(StringUtils::isNotBlank).collect(Collectors.toList()).toString());
            }
        }
    }

    /**
     * 封装计算参考值一 二数据
     *
     * @param employeeAnnualBonusDTO
     * @param allPaymentBonusSum
     * @param allBonusAmountSum
     * @param paymentBonusSumMap
     * @param bonusAmountSumMap
     * @param empAnnualBonusSnapshotDTOList
     */
    private void packPerformanceRank(EmployeeAnnualBonusDTO employeeAnnualBonusDTO, BigDecimal allPaymentBonusSum, BigDecimal allBonusAmountSum, Map<Long, BigDecimal> paymentBonusSumMap, Map<Long, BigDecimal> bonusAmountSumMap, List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList) {
        for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList) {
            //绩效奖金系数
            BigDecimal performanceBonusFactor = empAnnualBonusSnapshotDTO.getPerformanceBonusFactor();
            //考勤系数
            BigDecimal attendanceFactor = empAnnualBonusSnapshotDTO.getAttendanceFactor();
            //薪酬合计
            BigDecimal paymentBonusSum = new BigDecimal("0");
            //倒退12个月奖金金额合计
            BigDecimal bonusAmountSum = new BigDecimal("0");
            //参考值一 参考值二
            SalaryPayDTO salaryPayDTO = salaryPayMapper.selectSalaryPaySumAndBonusSum(empAnnualBonusSnapshotDTO.getEmployeeId(), employeeAnnualBonusDTO.getAnnualBonusYear());
            if (StringUtils.isNotNull(salaryPayDTO)) {

                paymentBonusSum = salaryPayDTO.getPaymentBonusSum();
                bonusAmountSum = salaryPayDTO.getBonusAmountSum();
            }

            if (null != performanceBonusFactor && performanceBonusFactor.compareTo(new BigDecimal("0")) > 0) {
                paymentBonusSum = paymentBonusSum.multiply(performanceBonusFactor);
            }
            if (null != attendanceFactor && attendanceFactor.compareTo(new BigDecimal("0")) > 0) {
                paymentBonusSum = paymentBonusSum.multiply(attendanceFactor);
            }
            //薪酬合计
            paymentBonusSumMap.put(empAnnualBonusSnapshotDTO.getEmployeeId(), paymentBonusSum);
            //倒退12个月奖金金额合计
            bonusAmountSumMap.put(empAnnualBonusSnapshotDTO.getEmployeeId(), bonusAmountSum);
            if (null != paymentBonusSum) {
                allPaymentBonusSum = allPaymentBonusSum.add(paymentBonusSum);
            }
            if (null != bonusAmountSum) {
                allBonusAmountSum = allBonusAmountSum.add(bonusAmountSum);
            }
        }
    }

    /**
     * 新增提交个人年终奖表
     *
     * @param employeeAnnualBonusDTO
     * @return
     */
    @Override
    @Transactional
    public EmployeeAnnualBonusDTO submitSave(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        //主表id
        Long employeeAnnualBonusId = employeeAnnualBonusDTO.getEmployeeAnnualBonusId();
        //状态:0草稿;1待初评;2待评议;3已评议
        Integer status = employeeAnnualBonusDTO.getStatus();
        //发起评议流程标记:0否;1是
        Integer commentFlag = employeeAnnualBonusDTO.getCommentFlag();
        //评议环节:1管理团队评议;2主管初评+管理团队评议
        Integer commentStep = employeeAnnualBonusDTO.getCommentStep();
        //个人年终奖发放快照信息及发放对象表集合
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs = employeeAnnualBonusDTO.getEmpAnnualBonusSnapshotDTOs();
        //个人年终奖发放对象集合
        List<EmpAnnualBonusObjects> empAnnualBonusObjectsList = new ArrayList<>();
        //个人年终奖发放快照信息集合
        List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList = new ArrayList<>();
        //个人年终奖表
        EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();

        BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
        if (commentFlag == 0) {
            //评议日期
            employeeAnnualBonus.setCommentDate(DateUtils.getNowDate());
            employeeAnnualBonus.setStatus(Constants.THREE);
            if (null == employeeAnnualBonusId) {
                //直接提交 新增数据
                packsubmitAdd(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, commentFlag);
                //todo 发送通知
            }else {
                //保存提交 修改数据
                packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus);
            }
        }else {
            if (commentStep == 2){
                if (null == employeeAnnualBonusId) {
                    //直接提交 新增数据
                    packsubmitAdd(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, commentFlag);

                } else {
                    if (status == 1) {
                        //保存提交 修改数据
                        packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus);
                        //todo 发送通知
                    } else if (status == 2) {
                        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOs)) {
                            List<Long> collect = empAnnualBonusSnapshotDTOs.stream().map(EmpAnnualBonusSnapshotDTO::getEmpAnnualBonusObjectsId).collect(Collectors.toList());

                            List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOS = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsByEmployeeAnnualBonusId(employeeAnnualBonusId);
                            if (StringUtils.isNotEmpty(empAnnualBonusObjectsDTOS)) {
                                //去除自己
                                List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOList = empAnnualBonusObjectsDTOS.stream().filter(f -> !collect.contains(f.getEmpAnnualBonusObjectsId())).collect(Collectors.toList());
                                for (EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO : empAnnualBonusObjectsDTOList) {
                                    if (null == empAnnualBonusObjectsDTO.getRecommendValue()) {
                                        employeeAnnualBonus.setStatus(1);
                                    }
                                }
                            }
                            //保存提交 修改数据
                            packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus);
                            //所有主管已初评
                            if (employeeAnnualBonus.getStatus() == 2) {
                                //todo 发送通知
                            }
                        }
                    } else if (status == 3) {
                        List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOS = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsByEmployeeAnnualBonusId(employeeAnnualBonusId);
                        if (StringUtils.isNotEmpty(empAnnualBonusObjectsDTOS)) {
                            for (EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO : empAnnualBonusObjectsDTOS) {
                                if (null == empAnnualBonusObjectsDTO.getCommentValue()) {
                                    employeeAnnualBonus.setStatus(2);
                                }
                            }
                          employeeAnnualBonus.setCommentDate(DateUtils.getNowDate());

                            //保存提交 修改数据
                            packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus);
                        }

                    }
                }
            }else if (commentStep == 1){
                if (null == employeeAnnualBonusId) {
                    //直接提交 新增数据
                    packsubmitAdd(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, commentFlag);

                }else {
                    if (status == 2) {
                        //保存提交 修改数据
                        packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus);
                        //todo 发送通知
                    } else if (status == 3) {
                        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOs)) {
                            List<Long> collect = empAnnualBonusSnapshotDTOs.stream().map(EmpAnnualBonusSnapshotDTO::getEmpAnnualBonusObjectsId).collect(Collectors.toList());

                            List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOS = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsByEmployeeAnnualBonusId(employeeAnnualBonusId);
                            if (StringUtils.isNotEmpty(empAnnualBonusObjectsDTOS)) {
                                //去除自己
                                List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOList = empAnnualBonusObjectsDTOS.stream().filter(f -> !collect.contains(f.getEmpAnnualBonusObjectsId())).collect(Collectors.toList());
                                for (EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO : empAnnualBonusObjectsDTOList) {
                                    if (null == empAnnualBonusObjectsDTO.getRecommendValue()) {
                                        employeeAnnualBonus.setStatus(1);
                                    }
                                }
                            }
                           employeeAnnualBonus.setCommentDate(DateUtils.getNowDate());
                            //保存提交 修改数据
                            packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus);
                        }
                    }
                }
            }

        }

        employeeAnnualBonusDTO.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
        return employeeAnnualBonusDTO;
    }

    /**
     * 实时查询个人年终奖表详情
     *
     * @param employeeAnnualBonusDTO
     * @return
     */
    @Override
    public List<EmpAnnualBonusSnapshotDTO> realTimeDetails(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        //所有员工的薪酬奖金合计
        BigDecimal allPaymentBonusSum = new BigDecimal("0");
        //所有员工的奖金金额合计
        BigDecimal allBonusAmountSum = new BigDecimal("0");
        //员工奖金基数=员工倒推12个月的薪酬合计（若期间出现断层，则继续往前倒推，直至取满12个月，若实在无法取满12个月，则可以取几个月就取几个月）
        Map<Long, BigDecimal> paymentBonusSumMap = new HashMap<>();
        //员工上年奖金额=倒推12个月的工资条数据中的总奖金包（若期间出现断层，则继续往前倒推，直至取满12个月，若实在无法取满12个月，则可以取几个月就取几个月）
        Map<Long, BigDecimal> bonusAmountSumMap = new HashMap<>();

        //分配年终奖金额
        BigDecimal distributeBonusAmount = employeeAnnualBonusDTO.getDistributeBonusAmount();
        //个人年终奖发放快照信息及发放对象表集合
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOsTwo = employeeAnnualBonusDTO.getEmpAnnualBonusSnapshotDTOsTwo();
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOsTwo)) {
            //参考值一 二数据计算必须数据
            packPerformanceRank(employeeAnnualBonusDTO, allPaymentBonusSum, allBonusAmountSum, paymentBonusSumMap, bonusAmountSumMap, empAnnualBonusSnapshotDTOsTwo);
            //封装计算参考值
            packReferenceValue(distributeBonusAmount, allPaymentBonusSum, allBonusAmountSum, paymentBonusSumMap, bonusAmountSumMap, empAnnualBonusSnapshotDTOsTwo);
        }
        return empAnnualBonusSnapshotDTOsTwo;
    }

    /**
     * 保存提交 修改数据
     *
     * @param empAnnualBonusSnapshotDTOs
     * @param empAnnualBonusObjectsList
     * @param empAnnualBonusSnapshotList
     * @param employeeAnnualBonus
     */
    private void packSubmitEdit(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs, List<EmpAnnualBonusObjects> empAnnualBonusObjectsList, List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList, EmployeeAnnualBonus employeeAnnualBonus) {
        employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
        try {
            employeeAnnualBonusMapper.updateEmployeeAnnualBonus(employeeAnnualBonus);
        } catch (Exception e) {
            throw new ServiceException("修改个人年终奖失败");
        }

        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOs)) {
            for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOs) {
                //个人年终奖发放对象表
                EmpAnnualBonusObjects empAnnualBonusObjects = new EmpAnnualBonusObjects();
                //个人年终奖发放快照信息表
                EmpAnnualBonusSnapshot empAnnualBonusSnapshot = new EmpAnnualBonusSnapshot();
                BeanUtils.copyProperties(empAnnualBonusSnapshotDTO, empAnnualBonusSnapshot);
                BeanUtils.copyProperties(empAnnualBonusSnapshotDTO, empAnnualBonusObjects);
                empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusObjectsList.add(empAnnualBonusObjects);
                empAnnualBonusSnapshotList.add(empAnnualBonusSnapshot);
            }
        }

        if (StringUtils.isNotEmpty(empAnnualBonusObjectsList)) {
            try {
                empAnnualBonusObjectsMapper.updateEmpAnnualBonusObjectss(empAnnualBonusObjectsList);
            } catch (Exception e) {
                throw new ServiceException("批量修改个人年中奖发放对象失败");
            }
        }
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotList)) {
            try {
                empAnnualBonusSnapshotMapper.updateEmpAnnualBonusSnapshots(empAnnualBonusSnapshotList);
            } catch (Exception e) {
                throw new ServiceException("批量修改个人年终奖发放快照信息失败");
            }
        }
    }

    /**
     * 直接提交 新增数据
     *
     * @param empAnnualBonusSnapshotDTOs
     * @param empAnnualBonusObjectsList
     * @param empAnnualBonusSnapshotList
     * @param employeeAnnualBonus
     * @param commentFlag
     */
    private void packsubmitAdd(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs, List<EmpAnnualBonusObjects> empAnnualBonusObjectsList, List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList, EmployeeAnnualBonus employeeAnnualBonus, Integer commentFlag) {
        employeeAnnualBonus.setCreateBy(SecurityUtils.getUserId());
        employeeAnnualBonus.setCreateTime(DateUtils.getNowDate());
        employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
        employeeAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            employeeAnnualBonusMapper.insertEmployeeAnnualBonus(employeeAnnualBonus);
        } catch (Exception e) {
            throw new ServiceException("新增个人年终奖失败");
        }
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOs)) {
            for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOs) {
                //个人年终奖发放对象表
                EmpAnnualBonusObjects empAnnualBonusObjects = new EmpAnnualBonusObjects();
                //个人年终奖发放快照信息表
                EmpAnnualBonusSnapshot empAnnualBonusSnapshot = new EmpAnnualBonusSnapshot();
                BeanUtils.copyProperties(empAnnualBonusSnapshotDTO, empAnnualBonusSnapshot);
                BeanUtils.copyProperties(empAnnualBonusSnapshotDTO, empAnnualBonusObjects);
                //个人年终奖ID
                empAnnualBonusObjects.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
                //个人年终奖ID
                empAnnualBonusSnapshot.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
                empAnnualBonusObjects.setCreateBy(SecurityUtils.getUserId());
                empAnnualBonusObjects.setCreateTime(DateUtils.getNowDate());
                empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);

                empAnnualBonusSnapshot.setCreateBy(SecurityUtils.getUserId());
                empAnnualBonusSnapshot.setCreateTime(DateUtils.getNowDate());
                empAnnualBonusSnapshot.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusSnapshot.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusSnapshot.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                empAnnualBonusObjectsList.add(empAnnualBonusObjects);
                empAnnualBonusSnapshotList.add(empAnnualBonusSnapshot);
            }
        }
        if (StringUtils.isNotEmpty(empAnnualBonusObjectsList)) {
            try {
                empAnnualBonusObjectsMapper.batchEmpAnnualBonusObjects(empAnnualBonusObjectsList);
            } catch (Exception e) {
                throw new ServiceException("批量插入个人年中奖发放对象失败");
            }
        }
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotList) && StringUtils.isNotEmpty(empAnnualBonusObjectsList)) {
            for (int i = 0; i < empAnnualBonusSnapshotList.size(); i++) {
                //个人年终奖发放对象ID
                empAnnualBonusSnapshotList.get(i).setEmpAnnualBonusObjectsId(empAnnualBonusObjectsList.get(i).getEmpAnnualBonusObjectsId());
            }
            try {
                empAnnualBonusSnapshotMapper.batchEmpAnnualBonusSnapshot(empAnnualBonusSnapshotList);
            } catch (Exception e) {
                throw new ServiceException("批量新增个人年终奖发放快照信息失败");
            }
        }
    }

    /**
     * 逻辑删除个人年终奖表信息
     *
     * @param employeeAnnualBonusDTO 个人年终奖表
     * @return 结果
     */
    @Override
    public int logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        int i = 0;
        EmployeeAnnualBonusDTO employeeAnnualBonusDTO1 = employeeAnnualBonusMapper.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
        if (StringUtils.isNull(employeeAnnualBonusDTO1)) {
            throw new ServiceException("数据不存在！ 请联系管理员");
        }
        EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
        employeeAnnualBonus.setEmployeeAnnualBonusId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
        employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = employeeAnnualBonusMapper.logicDeleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonus);
        } catch (Exception e) {
            throw new ServiceException("删除个人年终奖失败");
        }
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList = empAnnualBonusSnapshotMapper.selectEmpAnnualBonusSnapshotByEmployeeAnnualBonusId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOList)) {
            List<Long> collect = empAnnualBonusSnapshotDTOList.stream().map(EmpAnnualBonusSnapshotDTO::getEmpAnnualBonusSnapshotId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    empAnnualBonusSnapshotMapper.logicDeleteEmpAnnualBonusSnapshotByEmpAnnualBonusSnapshotIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除个人年终奖发放快照信息失败");
                }
            }
        }

        List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOS = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsByEmployeeAnnualBonusId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
        if (StringUtils.isNotEmpty(empAnnualBonusObjectsDTOS)) {
            List<Long> collect = empAnnualBonusObjectsDTOS.stream().map(EmpAnnualBonusObjectsDTO::getEmployeeAnnualBonusId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    empAnnualBonusObjectsMapper.logicDeleteEmpAnnualBonusObjectsByEmpAnnualBonusObjectsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除个人年终奖发放对象表失败");
                }
            }
        }
        return i;
    }

    /**
     * 物理删除个人年终奖表信息
     *
     * @param employeeAnnualBonusDTO 个人年终奖表
     * @return 结果
     */

    @Override
    public int deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
        BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
        return employeeAnnualBonusMapper.deleteEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
    }

    /**
     * 物理批量删除个人年终奖表
     *
     * @param employeeAnnualBonusDtos 需要删除的个人年终奖表主键
     * @return 结果
     */

    @Override
    public int deleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos) {
        List<Long> stringList = new ArrayList();
        for (EmployeeAnnualBonusDTO employeeAnnualBonusDTO : employeeAnnualBonusDtos) {
            stringList.add(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
        }
        return employeeAnnualBonusMapper.deleteEmployeeAnnualBonusByEmployeeAnnualBonusIds(stringList);
    }

    /**
     * 批量新增个人年终奖表信息
     *
     * @param employeeAnnualBonusDtos 个人年终奖表对象
     */

    public int insertEmployeeAnnualBonuss(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos) {
        List<EmployeeAnnualBonus> employeeAnnualBonusList = new ArrayList();

        for (EmployeeAnnualBonusDTO employeeAnnualBonusDTO : employeeAnnualBonusDtos) {
            EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
            BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
            employeeAnnualBonus.setCreateBy(SecurityUtils.getUserId());
            employeeAnnualBonus.setCreateTime(DateUtils.getNowDate());
            employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
            employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
            employeeAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            employeeAnnualBonusList.add(employeeAnnualBonus);
        }
        return employeeAnnualBonusMapper.batchEmployeeAnnualBonus(employeeAnnualBonusList);
    }

    /**
     * 批量修改个人年终奖表信息
     *
     * @param employeeAnnualBonusDtos 个人年终奖表对象
     */

    public int updateEmployeeAnnualBonuss(List<EmployeeAnnualBonusDTO> employeeAnnualBonusDtos) {
        List<EmployeeAnnualBonus> employeeAnnualBonusList = new ArrayList();

        for (EmployeeAnnualBonusDTO employeeAnnualBonusDTO : employeeAnnualBonusDtos) {
            EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
            BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
            employeeAnnualBonus.setCreateBy(SecurityUtils.getUserId());
            employeeAnnualBonus.setCreateTime(DateUtils.getNowDate());
            employeeAnnualBonus.setUpdateTime(DateUtils.getNowDate());
            employeeAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
            employeeAnnualBonusList.add(employeeAnnualBonus);
        }
        return employeeAnnualBonusMapper.updateEmployeeAnnualBonuss(employeeAnnualBonusList);
    }
}

