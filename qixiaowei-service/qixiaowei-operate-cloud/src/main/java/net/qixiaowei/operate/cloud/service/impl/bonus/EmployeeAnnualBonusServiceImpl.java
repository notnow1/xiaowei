package net.qixiaowei.operate.cloud.service.impl.bonus;

import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.integration.security.utils.UserUtils;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.api.dto.backlog.BacklogSendDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import net.qixiaowei.operate.cloud.api.domain.bonus.DeptAnnualBonus;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmpAnnualBonusObjects;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmpAnnualBonusSnapshot;
import net.qixiaowei.operate.cloud.api.domain.bonus.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.bonus.*;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.mapper.bonus.DeptAnnualBonusMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.EmpAnnualBonusObjectsMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.EmpAnnualBonusSnapshotMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.EmployeeAnnualBonusMapper;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalObjectsMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.bonus.IEmployeeAnnualBonusService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
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
    @Autowired
    private RemoteBacklogService remoteBacklogService;
    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 查询个人年终奖表
     *
     * @param employeeAnnualBonusId 个人年终奖表主键
     * @param inChargeTeamFlag
     * @return 个人年终奖表
     */
    @Override
    public EmployeeAnnualBonusDTO selectEmployeeAnnualBonusByEmployeeAnnualBonusId(Long employeeAnnualBonusId, Integer inChargeTeamFlag) {
        EmployeeAnnualBonusDTO employeeAnnualBonusDTO = employeeAnnualBonusMapper.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId);
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsAndSnapshot(employeeAnnualBonusId, inChargeTeamFlag);
        this.packperformanceDetails(empAnnualBonusSnapshotDTOList);
        employeeAnnualBonusDTO.setEmpAnnualBonusSnapshotDTOs(empAnnualBonusSnapshotDTOList);
        return employeeAnnualBonusDTO;
    }

    /**
     * 查询个人年终奖表列表
     *
     * @param employeeAnnualBonusDTO 个人年终奖表
     * @return 个人年终奖表
     */
    @DataScope(businessAlias = "eab")
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
        this.handleResult(employeeAnnualBonusDTOS);
        //模糊查询申请年终奖金额
        BigDecimal applyBonusAmount = employeeAnnualBonusDTO.getApplyBonusAmount();
        if (StringUtils.isNotNull(applyBonusAmount)) {
            List<EmployeeAnnualBonusDTO> emolumentPlanDTOList = new ArrayList<>();
            //模糊查询
            Pattern pattern = Pattern.compile(String.valueOf(employeeAnnualBonusDTO.getApplyBonusAmount()));
            for (EmployeeAnnualBonusDTO annualBonusDTO : employeeAnnualBonusDTOS) {
                BigDecimal applyBonusAmount1 = annualBonusDTO.getApplyBonusAmount();
                if (StringUtils.isNotNull(applyBonusAmount1)) {
                    Matcher matcher = pattern.matcher(String.valueOf(applyBonusAmount1));
                    if (matcher.find()) {  //matcher.find()-为模糊查询   matcher.matches()-为精确查询
                        emolumentPlanDTOList.add(annualBonusDTO);
                    }
                }
            }
            return emolumentPlanDTOList;
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
            BigDecimal distributeBonusAmount = annualBonusDTO.getDistributeBonusAmount();
            if (null != distributeBonusAmount && distributeBonusAmount.compareTo(new BigDecimal("0"))!=0){
                annualBonusDTO.setDistributeBonusAmount(distributeBonusAmount.multiply(new BigDecimal("10000")));
            }else {
                annualBonusDTO.setDistributeBonusAmount(new BigDecimal("0"));
            }
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
                        if (annualBonusDTO.getApplyEmployeeId().equals(datum.getEmployeeId())) {
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
                        if (annualBonusDTO.getApplyDepartmentId().equals(datum.getDepartmentId())) {
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
                        if (annualBonusDTO.getDepartmentId().equals(datum.getDepartmentId())) {
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
        //是否可提交 0保存 1提交
        Integer submitFlag = employeeAnnualBonusDTO.getSubmitFlag();
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
            if (commentFlag == 0) {
                if (submitFlag == 0) {
                    employeeAnnualBonus.setStatus(0);
                } else {
                    employeeAnnualBonus.setCommentDate(DateUtils.getNowDate());
                    employeeAnnualBonus.setStatus(3);
                }
            }
            if (commentFlag == 1) {
                if (submitFlag == 0) {
                    employeeAnnualBonus.setStatus(0);
                } else {
                    employeeAnnualBonus.setStatus(1);
                }
            }
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
                empAnnualBonusObjects.setStatus(employeeAnnualBonus.getStatus());
                empAnnualBonusObjects.setCreateBy(SecurityUtils.getUserId());
                empAnnualBonusObjects.setCreateTime(DateUtils.getNowDate());
                empAnnualBonusObjects.setUpdateTime(DateUtils.getNowDate());
                empAnnualBonusObjects.setUpdateBy(SecurityUtils.getUserId());
                empAnnualBonusObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                empAnnualBonusObjects.setDepartmentLeaderId(empAnnualBonusSnapshotDTO.getDepartmentLeaderId());
                empAnnualBonusObjects.setDepartmentLeaderName(empAnnualBonusSnapshotDTO.getDepartmentLeaderName());

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
        if (employeeAnnualBonus.getStatus() == 1) {
            this.packaddSubmit(empAnnualBonusObjectsList, employeeAnnualBonus);
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
            if (commentFlag == 0 || status == 2 || status == 3) {
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
    public EmployeeAnnualBonusDTO addPrefabricate(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        BigDecimal distributeBonusAmount = new BigDecimal("0");

        DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
        deptAnnualBonus.setAnnualBonusYear(employeeAnnualBonusDTO.getAnnualBonusYear());
        deptAnnualBonus.setDepartmentId(employeeAnnualBonusDTO.getDepartmentId());
        DeptAnnualBonusDTO deptAnnualBonusDTO = deptAnnualBonusMapper.selectDeptAnnualBonusByAnnualBonusYear(deptAnnualBonus);
        if (StringUtils.isNotNull(deptAnnualBonusDTO)) {
            distributeBonusAmount = deptAnnualBonusDTO.getDepartmentAnnualBonus();
        }
        //部门年终奖
        employeeAnnualBonusDTO.setDistributeBonusAmount(distributeBonusAmount);
        //所有员工的薪酬奖金合计和所有员工的奖金金额合计
        Map<String, BigDecimal> allMpa = new HashMap<>();
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
                empAnnualBonusSnapshotDTO.setResponsibleEmployeeId(datum.getExaminationLeaderId());
                empAnnualBonusSnapshotDTO.setResponsibleEmployeeName(datum.getExaminationLeaderName());
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
                            if (empAnnualBonusSnapshotDTO.getEmployeeId().equals(annualBonusSnapshotDTO.getEmployeeId())) {

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
            packPerformanceRank(employeeAnnualBonusDTO, allPaymentBonusSum, allBonusAmountSum, paymentBonusSumMap, bonusAmountSumMap, empAnnualBonusSnapshotDTOList, allMpa);
            allPaymentBonusSum = allMpa.get("allPaymentBonusSum");
            allBonusAmountSum = allMpa.get("allBonusAmountSum");
            //封装计算参考值
            packReferenceValue(distributeBonusAmount, allPaymentBonusSum, allBonusAmountSum, paymentBonusSumMap, bonusAmountSumMap, empAnnualBonusSnapshotDTOList);
        }
        employeeAnnualBonusDTO.setEmpAnnualBonusSnapshotDTOs(empAnnualBonusSnapshotDTOList);
        return employeeAnnualBonusDTO;
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
                bonusPercentageOne = paymentBonusSum.divide(allPaymentBonusSum, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(10, BigDecimal.ROUND_HALF_UP);
            }
            if (null != distributeBonusAmount && distributeBonusAmount.compareTo(new BigDecimal("0")) != 0 &&
                    bonusPercentageOne.compareTo(new BigDecimal("0")) != 0) {
                referenceValueOne = distributeBonusAmount.multiply(new BigDecimal("10000")).multiply(bonusPercentageOne.divide(new BigDecimal("100"),10, BigDecimal.ROUND_HALF_UP)).setScale(10, BigDecimal.ROUND_HALF_UP);
            }
            if (null != bonusAmountSum && bonusAmountSum.compareTo(new BigDecimal("0")) != 0 &&
                    allBonusAmountSum.compareTo(new BigDecimal("0")) != 0) {
                bonusPercentageTwo = bonusAmountSum.divide(allBonusAmountSum, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(10, BigDecimal.ROUND_HALF_UP);
            }
            if (null != distributeBonusAmount && distributeBonusAmount.compareTo(new BigDecimal("0")) != 0 &&
                    bonusPercentageTwo.compareTo(new BigDecimal("0")) != 0) {
                referenceValueTwo = distributeBonusAmount.multiply(new BigDecimal("10000")).multiply(bonusPercentageTwo.divide(new BigDecimal("100"),10, BigDecimal.ROUND_HALF_UP)).setScale(10, BigDecimal.ROUND_HALF_UP);;
            }
            empAnnualBonusSnapshotDTO.setBonusPercentageOne(bonusPercentageOne);
            empAnnualBonusSnapshotDTO.setReferenceValueOne(referenceValueOne);

            empAnnualBonusSnapshotDTO.setBonusPercentageTwo(bonusPercentageTwo);
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
            //考勤系数
            empAnnualBonusSnapshotDTO.setPerformanceBonusFactor(new BigDecimal("1"));
            //绩效
            List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByEmployeeId(empAnnualBonusSnapshotDTO.getEmployeeId());
            if (StringUtils.isNotEmpty(performanceRankFactorDTOS)) {
                PerformanceRankFactorDTO performanceRankFactorDTO = performanceRankFactorDTOS.get(0);
                Long performanceRankId = performanceRankFactorDTO.getPerformanceRankId();
                //绩效名称
                empAnnualBonusSnapshotDTO.setPerformanceRank(performanceRankFactorDTO.getPerformanceRankName());
                //绩效等级ID
                empAnnualBonusSnapshotDTO.setPerformanceRankId(performanceRankId);
                //绩效等级下拉框集合
                if (null != performanceRankId) {
                    List<PerformanceRankFactorDTO> performanceRankFactorDTOS1 = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
                    if (StringUtils.isNotEmpty(performanceRankFactorDTOS1)) {
                        Map<String, BigDecimal> map = new HashMap<>();
                        empAnnualBonusSnapshotDTO.setPerformanceRanks(performanceRankFactorDTOS1.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).collect(Collectors.toList()));
                        for (PerformanceRankFactorDTO rankFactorDTO : performanceRankFactorDTOS1) {
                            map.put(rankFactorDTO.getPerformanceRankName(), rankFactorDTO.getBonusFactor());
                        }
                        empAnnualBonusSnapshotDTO.setPerformanceRankMap(map);
                    }
                }
                //绩效等级系数ID
                empAnnualBonusSnapshotDTO.setPerformanceRankFactorId(performanceRankFactorDTO.getPerformanceRankFactorId());

                if (null != performanceRankFactorDTO.getBonusFactor()){
                    //绩效奖金系数
                    empAnnualBonusSnapshotDTO.setPerformanceBonusFactor(performanceRankFactorDTO.getBonusFactor());
                }

                //最近绩效结果
                empAnnualBonusSnapshotDTO.setLastPerformanceResulted(StringUtils.join(performanceRankFactorDTOS.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).filter(StringUtils::isNotBlank).collect(Collectors.toList()),","));
            }
        }
    }

    /**
     * 封装查询绩效详情
     *
     * @param empAnnualBonusSnapshotDTOList
     */
    private void packperformanceDetails(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList) {
        for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList) {
            //绩效
            List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByEmployeeId(empAnnualBonusSnapshotDTO.getEmployeeId());
            if (StringUtils.isNotEmpty(performanceRankFactorDTOS)) {
                PerformanceRankFactorDTO performanceRankFactorDTO = performanceRankFactorDTOS.get(0);
                Long performanceRankId = performanceRankFactorDTO.getPerformanceRankId();
                //绩效等级下拉框集合
                if (null != performanceRankId) {
                    List<PerformanceRankFactorDTO> performanceRankFactorDTOS1 = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
                    if (StringUtils.isNotEmpty(performanceRankFactorDTOS1)) {
                        Map<String, BigDecimal> map = new HashMap<>();
                        empAnnualBonusSnapshotDTO.setPerformanceRanks(performanceRankFactorDTOS1.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).filter(Objects::nonNull).collect(Collectors.toList()));
                        for (PerformanceRankFactorDTO rankFactorDTO : performanceRankFactorDTOS1) {
                            map.put(rankFactorDTO.getPerformanceRankName(), rankFactorDTO.getBonusFactor());
                        }
                        empAnnualBonusSnapshotDTO.setPerformanceRankMap(map);
                    }
                }
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
     * @param allMpa
     */
    private void packPerformanceRank(EmployeeAnnualBonusDTO employeeAnnualBonusDTO, BigDecimal allPaymentBonusSum, BigDecimal allBonusAmountSum, Map<Long, BigDecimal> paymentBonusSumMap, Map<Long, BigDecimal> bonusAmountSumMap, List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList, Map<String, BigDecimal> allMpa) {
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
        allMpa.put("allPaymentBonusSum", allPaymentBonusSum);
        allMpa.put("allBonusAmountSum", allBonusAmountSum);
    }

    /**
     * 修改个人年终奖表
     *
     * @param employeeAnnualBonusDTO
     * @return
     */
    @Override
    @Transactional
    public EmployeeAnnualBonusDTO edit(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        // 是否可提交 0保存 1提交
        Integer submitFlag = employeeAnnualBonusDTO.getSubmitFlag();
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
        BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
        if (commentFlag == 0) {
            if (submitFlag == 0) {
                packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus,null);
            } else {
                //评议日期
                employeeAnnualBonus.setCommentDate(DateUtils.getNowDate());
                employeeAnnualBonus.setStatus(Constants.THREE);
                packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, null);
            }
        } else {
            if (submitFlag == 0) {
                //保存 修改数据
                packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, null);
            } else {
                //保存提交 修改数据
                employeeAnnualBonus.setStatus(Constants.ONE);
                packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, null);
                //保存提交 编辑提交发送通知
                packaddSubmit(empAnnualBonusObjectsList, employeeAnnualBonus);
            }
        }
        employeeAnnualBonusDTO.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
        return employeeAnnualBonusDTO;
    }

    /**
     * 封装保存提交 编辑提交发送通知
     *
     * @param empAnnualBonusObjectsList
     * @param employeeAnnualBonus
     */
    private void packaddSubmit(List<EmpAnnualBonusObjects> empAnnualBonusObjectsList, EmployeeAnnualBonus employeeAnnualBonus) {
        if (StringUtils.isNotEmpty(empAnnualBonusObjectsList)){
            //远程查询人员取用户id发送通知
            List<Long> employeeIds = empAnnualBonusObjectsList.stream().filter(f -> f.getResponsibleEmployeeId() != null).map(EmpAnnualBonusObjects::getResponsibleEmployeeId).distinct().collect(Collectors.toList());

            R<List<UserDTO>> listR = remoteUserService.selectByemployeeIds(employeeIds, SecurityConstants.INNER);
            if (R.SUCCESS != listR.getCode()) {
                throw new ServiceException("查找用户id失败");
            }
            List<UserDTO> UserData = listR.getData();
            if (StringUtils.isNotEmpty(UserData)){
                for (EmpAnnualBonusObjects empAnnualBonusObjects : empAnnualBonusObjectsList) {
                    if (StringUtils.isNotNull(empAnnualBonusObjects.getResponsibleEmployeeId())){
                        for (UserDTO userDatum : UserData) {
                            if (StringUtils.isNotNull(userDatum.getEmployeeId())){
                                if (empAnnualBonusObjects.getResponsibleEmployeeId().equals(userDatum.getEmployeeId())){
                                    empAnnualBonusObjects.setUserId(userDatum.getUserId());
                                }
                            }
                        }
                    }

                }
                //根据主管id分组
                Map<Long, List<EmpAnnualBonusObjects>> employeeMap = empAnnualBonusObjectsList.parallelStream().filter(f -> f.getResponsibleEmployeeId() != null).collect(Collectors.groupingBy(EmpAnnualBonusObjects::getResponsibleEmployeeId));
                for (Long key : employeeMap.keySet()) {
                    List<EmpAnnualBonusObjects> empAnnualBonusObjects = employeeMap.get(key);
                    if (StringUtils.isNotEmpty(empAnnualBonusObjects)) {
                        List<Long> collect = empAnnualBonusObjects.stream().filter(f -> f.getUserId() != null).map(EmpAnnualBonusObjects::getUserId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(collect)) {
                            //todo 发送给主管通知
                            List<BacklogSendDTO> backlogSendDTOS = new ArrayList<>();
                            BacklogSendDTO backlogSendDTO = new BacklogSendDTO();
                            backlogSendDTO.setBusinessType(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_SUPERVISOR.getParentBusinessType().getCode());
                            backlogSendDTO.setBusinessSubtype(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_SUPERVISOR.getCode());
                            backlogSendDTO.setBusinessId(employeeAnnualBonus.getEmployeeAnnualBonusId());

                            backlogSendDTO.setUserId(collect.get(0));
                            backlogSendDTO.setBacklogName("个人年终奖生成主管初评");
                            backlogSendDTO.setBacklogInitiator(employeeAnnualBonus.getApplyEmployeeId());
                            backlogSendDTO.setBacklogInitiatorName(employeeAnnualBonus.getApplyEmployeeName());
                            backlogSendDTOS.add(backlogSendDTO);
                            R<?> insertBacklogs = remoteBacklogService.insertBacklogs(backlogSendDTOS, SecurityConstants.INNER);
                            if (R.SUCCESS != insertBacklogs.getCode()) {
                                throw new ServiceException("个人年终奖生成主管初评通知失败");
                            }
                        }
                    }

                }
            }

        }

    }

    /**
     * 主管修改个人年终奖表
     *
     * @param employeeAnnualBonusDTO
     * @return
     */
    @Override
    @Transactional
    public EmployeeAnnualBonusDTO inChargeEdit(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        // 是否可提交 0保存 1提交
        Integer submitFlag = employeeAnnualBonusDTO.getSubmitFlag();
        //个人年终奖发放快照信息及发放对象表集合
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs = employeeAnnualBonusDTO.getEmpAnnualBonusSnapshotDTOs().stream().filter(f->f.getChoiceFlag()==1 && f.getResponsibleEmployeeId().equals(SecurityUtils.getEmployeeId())).collect(Collectors.toList());
        //个人年终奖发放对象集合
        List<EmpAnnualBonusObjects> empAnnualBonusObjectsList = new ArrayList<>();
        //个人年终奖发放快照信息集合
        List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList = new ArrayList<>();
        //个人年终奖表
        EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
        BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);
        if (submitFlag == 0) {
            //保存 修改数据
            packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, null);
        } else {
            //保存提交 修改数据
            employeeAnnualBonus.setStatus(Constants.TWO);
            if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOs)) {
                List<Long> collect = empAnnualBonusSnapshotDTOs.stream().map(EmpAnnualBonusSnapshotDTO::getEmpAnnualBonusObjectsId).collect(Collectors.toList());

                List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOS = empAnnualBonusObjectsMapper.selectEmpAnnualBonusObjectsByEmployeeAnnualBonusId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
                if (StringUtils.isNotEmpty(empAnnualBonusObjectsDTOS)) {
                    //去除自己
                    List<EmpAnnualBonusObjectsDTO> empAnnualBonusObjectsDTOList = empAnnualBonusObjectsDTOS.stream().filter(f -> !collect.contains(f.getEmpAnnualBonusObjectsId())).collect(Collectors.toList());
                    for (EmpAnnualBonusObjectsDTO empAnnualBonusObjectsDTO : empAnnualBonusObjectsDTOList) {
                        if (empAnnualBonusObjectsDTO.getStatus().equals(Constants.ONE)) {
                            employeeAnnualBonus.setStatus(Constants.ONE);
                        }
                    }
                }
            }
            packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, String.valueOf(Constants.TWO));
            //待办事项表
            BacklogDTO backlogDTO = new BacklogDTO();
            backlogDTO.setBusinessType(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_SUPERVISOR.getParentBusinessType().getCode());
            backlogDTO.setBusinessSubtype(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_SUPERVISOR.getCode());
            backlogDTO.setBusinessId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
            backlogDTO.setUserId(SecurityUtils.getUserId());
            remoteBacklogService.handled(backlogDTO, SecurityConstants.INNER);
            //状态:0草稿;1待初评;2待评议;3已评议
            Integer status = employeeAnnualBonus.getStatus();
            if (status == 2) {
                //todo 发送给团队通知
                R<EmployeeDTO> employeeDTOR = remoteEmployeeService.selectByEmployeeId(employeeAnnualBonus.getCommentEmployeeId(), SecurityConstants.INNER);
                EmployeeDTO data = employeeDTOR.getData();
                if (StringUtils.isNotNull(data)) {
                    List<BacklogSendDTO> backlogSendDTOS = new ArrayList<>();
                    BacklogSendDTO backlogSendDTO = new BacklogSendDTO();
                    backlogSendDTO.setBusinessType(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_MANAGEMENT_TEAM.getParentBusinessType().getCode());
                    backlogSendDTO.setBusinessSubtype(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_MANAGEMENT_TEAM.getCode());
                    backlogSendDTO.setBusinessId(employeeAnnualBonus.getEmployeeAnnualBonusId());
                    backlogSendDTO.setUserId(data.getUserId());
                    backlogSendDTO.setBacklogName(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_MANAGEMENT_TEAM.getInfo());
                    backlogSendDTO.setBacklogInitiator(employeeAnnualBonus.getApplyEmployeeId());
                    backlogSendDTO.setBacklogInitiatorName(employeeAnnualBonus.getApplyEmployeeName());
                    backlogSendDTOS.add(backlogSendDTO);
                    R<?> insertBacklogs = remoteBacklogService.insertBacklogs(backlogSendDTOS, SecurityConstants.INNER);
                    if (R.SUCCESS != insertBacklogs.getCode()) {
                        throw new ServiceException("个人年终奖生成管理团队评议失败");

                    }
                }
            }
        }
        employeeAnnualBonusDTO.setEmployeeAnnualBonusId(employeeAnnualBonus.getEmployeeAnnualBonusId());
        return employeeAnnualBonusDTO;
    }

    /**
     * 团队修改个人年终奖表
     *
     * @param employeeAnnualBonusDTO
     * @return
     */
    @Override
    @Transactional
    public EmployeeAnnualBonusDTO teamEdit(EmployeeAnnualBonusDTO employeeAnnualBonusDTO) {
        // 是否可提交 0保存 1提交
        Integer submitFlag = employeeAnnualBonusDTO.getSubmitFlag();
        //个人年终奖发放快照信息及发放对象表集合
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs = employeeAnnualBonusDTO.getEmpAnnualBonusSnapshotDTOs();
        //个人年终奖发放对象集合
        List<EmpAnnualBonusObjects> empAnnualBonusObjectsList = new ArrayList<>();
        //个人年终奖发放快照信息集合
        List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList = new ArrayList<>();
        //个人年终奖表
        EmployeeAnnualBonus employeeAnnualBonus = new EmployeeAnnualBonus();
        BeanUtils.copyProperties(employeeAnnualBonusDTO, employeeAnnualBonus);

        if (submitFlag == 0) {
            //保存 修改数据
            packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, null);
        } else {
            //保存提交 修改数据
            employeeAnnualBonus.setStatus(Constants.THREE);
            packSubmitEdit(empAnnualBonusSnapshotDTOs, empAnnualBonusObjectsList, empAnnualBonusSnapshotList, employeeAnnualBonus, String.valueOf(Constants.THREE));
            //将所有客服待办中的关联的域名任务都处理成已处理
            BacklogDTO backlogDTO = new BacklogDTO();
            backlogDTO.setBusinessType(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_MANAGEMENT_TEAM.getParentBusinessType().getCode());
            backlogDTO.setBusinessSubtype(BusinessSubtype.EMPLOYEE_ANNUAL_BONUS_COMMENT_MANAGEMENT_TEAM.getCode());
            backlogDTO.setBusinessId(employeeAnnualBonusDTO.getEmployeeAnnualBonusId());
            backlogDTO.setUserId(SecurityUtils.getUserId());
            remoteBacklogService.handled(backlogDTO, SecurityConstants.INNER);
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
        //所有员工的薪酬奖金合计和所有员工的奖金金额合计
        Map<String, BigDecimal> allMpa = new HashMap<>();
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
        //个人年终奖发放快照信息及发放对象表集合
        List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOsOne = new ArrayList<>();
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
                empAnnualBonusSnapshotDTO.setDepartmentLeaderId(datum.getDepartmentLeaderId());
                empAnnualBonusSnapshotDTO.setDepartmentLeaderName(datum.getDepartmentLeaderName());
                empAnnualBonusSnapshotDTO.setChoiceFlag(0);
                empAnnualBonusSnapshotDTOsOne.add(empAnnualBonusSnapshotDTO);
            }
        }
        //薪酬情况
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOsOne)) {
            List<Long> collect = empAnnualBonusSnapshotDTOsOne.stream().map(EmpAnnualBonusSnapshotDTO::getEmployeeId).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList1 = salaryPayMapper.selectSalaryPayCondition(collect, employeeAnnualBonusDTO.getAnnualBonusYear());
                for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOsOne) {
                    BigDecimal employeeBasicWage = new BigDecimal("0");
                    BigDecimal emolumentBeforeOne = new BigDecimal("0");
                    BigDecimal bonusBeforeOne = new BigDecimal("0");
                    BigDecimal bonusBeforeTwo = new BigDecimal("0");
                    if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOList1)) {
                        for (EmpAnnualBonusSnapshotDTO annualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList1) {
                            if (empAnnualBonusSnapshotDTO.getEmployeeId().equals(annualBonusSnapshotDTO.getEmployeeId())) {

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
        }
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOsOne)) {
            //参考值一 二数据计算必须数据
            packPerformanceRank(employeeAnnualBonusDTO, allPaymentBonusSum, allBonusAmountSum, paymentBonusSumMap, bonusAmountSumMap, empAnnualBonusSnapshotDTOsOne, allMpa);
            allPaymentBonusSum = allMpa.get("allPaymentBonusSum");
            allBonusAmountSum = allMpa.get("allBonusAmountSum");
            //封装计算参考值
            packReferenceValue(distributeBonusAmount, allPaymentBonusSum, allBonusAmountSum, paymentBonusSumMap, bonusAmountSumMap, empAnnualBonusSnapshotDTOsTwo);
        }
        return empAnnualBonusSnapshotDTOsTwo;
    }

    /**
     * 根据人员id查询个人年终奖 申请人id
     *
     * @param employeeIds
     * @return
     */
    @Override
    public List<EmployeeAnnualBonus> selectEmployeeAnnualBonusByEmployeeIds(List<Long> employeeIds) {
        return employeeAnnualBonusMapper.selectEmployeeAnnualBonusByEmployeeIds(employeeIds);
    }

    /**
     * 根据部门id查询个人年终奖 (一级部门,申请部门)
     *
     * @param departmentIds
     * @return
     */
    @Override
    public List<EmployeeAnnualBonus> selectEmployeeAnnualBonusByDepartmentIds(List<Long> departmentIds) {
        return employeeAnnualBonusMapper.selectEmployeeAnnualBonusByDepartmentIds(departmentIds);
    }

    /**
     * 保存提交 修改数据
     *  @param empAnnualBonusSnapshotDTOs
     * @param empAnnualBonusObjectsList
     * @param empAnnualBonusSnapshotList
     * @param employeeAnnualBonus
     * @param status
     */
    private void packSubmitEdit(List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOs, List<EmpAnnualBonusObjects> empAnnualBonusObjectsList, List<EmpAnnualBonusSnapshot> empAnnualBonusSnapshotList, EmployeeAnnualBonus employeeAnnualBonus, String status) {
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
                if (StringUtils.isNotNull(status)){
                    empAnnualBonusObjects.setStatus(Integer.valueOf(status));
                }else {
                    empAnnualBonusObjects.setStatus(employeeAnnualBonus.getStatus());
                }
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
    @Transactional
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

    @Override
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

    @Override
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

    @Override
    public void handleResult(List<EmployeeAnnualBonusDTO> result) {
        if (StringUtils.isNotEmpty(result)) {
            Set<Long> userIds = result.stream().map(EmployeeAnnualBonusDTO::getCreateBy).collect(Collectors.toSet());
            Map<Long, String> employeeNameMap = UserUtils.getEmployeeNameMap(userIds);
            result.forEach(entity -> {
                Long userId = entity.getCreateBy();
                entity.setCreateByName(employeeNameMap.get(userId));
            });
        }
    }
}

