package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpAnnualBonusObjects;
import net.qixiaowei.operate.cloud.api.domain.salary.EmpAnnualBonusSnapshot;
import net.qixiaowei.operate.cloud.api.domain.salary.EmployeeAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmpAnnualBonusSnapshotDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.EmployeeAnnualBonusDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryPayDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmpAnnualBonusObjectsMapper;
import net.qixiaowei.operate.cloud.mapper.salary.EmpAnnualBonusSnapshotMapper;
import net.qixiaowei.operate.cloud.mapper.salary.EmployeeAnnualBonusMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.salary.IEmployeeAnnualBonusService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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

    /**
     * 查询个人年终奖表
     *
     * @param employeeAnnualBonusId 个人年终奖表主键
     * @return 个人年终奖表
     */
    @Override
    public EmployeeAnnualBonusDTO selectEmployeeAnnualBonusByEmployeeAnnualBonusId(Long employeeAnnualBonusId) {
        return employeeAnnualBonusMapper.selectEmployeeAnnualBonusByEmployeeAnnualBonusId(employeeAnnualBonusId);
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
            //赋值部门名称
            packDeptName(employeeAnnualBonusDTOS);
            //赋值申请部门名称
            packApplyDeptName(employeeAnnualBonusDTOS);
            //赋值申请人名称
            packApplyEmployeeName(employeeAnnualBonusDTOS);
            //申请年终奖总额
            packApplyBonusAmount(employeeAnnualBonusDTOS);
        }
        return employeeAnnualBonusDTOS;
    }

    /**
     * 封装申请年终奖总额
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
            if (commentFlag == 0) {
                //评议日期
                employeeAnnualBonus.setCommentDate(DateUtils.getNowDate());
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
                empAnnualBonusObjects.setEmployeeAnnualBonusId(empAnnualBonusObjects.getEmployeeAnnualBonusId());
                //个人年终奖ID
                empAnnualBonusSnapshot.setEmployeeAnnualBonusId(empAnnualBonusObjects.getEmployeeAnnualBonusId());
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
        if (commentFlag == 1) {
            //todo 发送通知

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
            if (commentFlag == 1) {
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
                empAnnualBonusSnapshotDTOList.add(empAnnualBonusSnapshotDTO);
            }
        }
        //薪酬情况
        if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOList)) {
            List<Long> collect = empAnnualBonusSnapshotDTOList.stream().map(EmpAnnualBonusSnapshotDTO::getEmployeeId).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                List<EmpAnnualBonusSnapshotDTO> empAnnualBonusSnapshotDTOList1 = salaryPayMapper.selectSalaryPayCondition(collect, employeeAnnualBonusDTO.getAnnualBonusYear());
                if (StringUtils.isNotEmpty(empAnnualBonusSnapshotDTOList1)) {
                    for (EmpAnnualBonusSnapshotDTO empAnnualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList) {
                        for (EmpAnnualBonusSnapshotDTO annualBonusSnapshotDTO : empAnnualBonusSnapshotDTOList1) {
                            if (empAnnualBonusSnapshotDTO.getEmployeeId() == annualBonusSnapshotDTO.getEmployeeId()) {
                                //基本工资
                                annualBonusSnapshotDTO.setEmployeeBasicWage(annualBonusSnapshotDTO.getEmployeeBasicWage());
                                //前一年总薪酬
                                annualBonusSnapshotDTO.setEmolumentBeforeOne(annualBonusSnapshotDTO.getEmolumentBeforeOne());
                                //前一年奖金
                                annualBonusSnapshotDTO.setBonusBeforeOne(annualBonusSnapshotDTO.getBonusBeforeOne());
                                //前二年奖金
                                annualBonusSnapshotDTO.setBonusBeforeTwo(annualBonusSnapshotDTO.getBonusBeforeTwo());
                            }
                        }
                    }

                }
            }
        }
        //todo 绩效

        return empAnnualBonusSnapshotDTOList;
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

