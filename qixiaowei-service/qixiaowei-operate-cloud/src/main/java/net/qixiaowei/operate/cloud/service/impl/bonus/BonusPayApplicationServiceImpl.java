package net.qixiaowei.operate.cloud.service.impl.bonus;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayApplication;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayBudgetDept;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayObjects;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayBudgetDeptDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayObjectsDTO;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayStandingDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.SalaryItemDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusPayApplicationMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusPayBudgetDeptMapper;
import net.qixiaowei.operate.cloud.mapper.bonus.BonusPayObjectsMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryItemMapper;
import net.qixiaowei.operate.cloud.service.bonus.IBonusPayApplicationService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import net.sf.jsqlparser.util.deparser.GroupByDeParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;
import java.util.stream.Collectors;


/**
 * BonusPayApplicationService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-12-08
 */
@Service
public class BonusPayApplicationServiceImpl implements IBonusPayApplicationService {
    @Autowired
    private BonusPayApplicationMapper bonusPayApplicationMapper;
    @Autowired
    private BonusPayBudgetDeptMapper bonusPayBudgetDeptMapper;
    @Autowired
    private BonusPayObjectsMapper bonusPayObjectsMapper;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private RemoteUserService remoteUserService;
    @Autowired
    private SalaryItemMapper salaryItemMapper;

    /**
     * 查询奖金发放申请表
     *
     * @param bonusPayApplicationId 奖金发放申请表主键
     * @return 奖金发放申请表
     */
    @Override
    public BonusPayApplicationDTO selectBonusPayApplicationByBonusPayApplicationId(Long bonusPayApplicationId) {
        BonusPayApplicationDTO bonusPayApplicationDTO = bonusPayApplicationMapper.selectBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationId);
        //封装申请部门名称
        this.packBonusPayApplicationDeptDeptName(bonusPayApplicationDTO);
        //预算部门
        List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS = bonusPayBudgetDeptMapper.selectBonusPayBudgetDeptByBonusPayApplicationId(bonusPayApplicationId);
        //封装预算部门名称
        this.packBonusPayBudgetDeptDeptName(bonusPayBudgetDeptDTOS);
        //查询获奖员工集合
        List<BonusPayObjectsDTO> bonusPayEmployeeObjectsDTOS = bonusPayObjectsMapper.selectBonusPayEmployeeObjectsByBonusPayApplicationId(bonusPayApplicationId);
        //封装获奖员工名称
        this.packbonusPayEmployeeObjectsName(bonusPayEmployeeObjectsDTOS);
        //查询获奖部门集合
        List<BonusPayObjectsDTO> bonusPayDeptObjectsDTOS = bonusPayObjectsMapper.selectBonusPayDeptObjectsByBonusPayApplicationId(bonusPayApplicationId);
        //封装获奖部门名称
        this.packbonusPayDeptObjectsName(bonusPayDeptObjectsDTOS);
        bonusPayApplicationDTO.setAwardYearMonth(bonusPayApplicationDTO.getAwardYear() + "/0" + bonusPayApplicationDTO.getAwardMonth());
        bonusPayApplicationDTO.setBonusPayBudgetDeptDTOs(bonusPayBudgetDeptDTOS);
        bonusPayApplicationDTO.setBonusPayObjectsEmployeeDTOs(bonusPayEmployeeObjectsDTOS);
        bonusPayApplicationDTO.setBonusPayObjectsDeptDTOs(bonusPayDeptObjectsDTOS);
        return bonusPayApplicationDTO;
    }

    /**
     * 封装获奖部门名称
     *
     * @param bonusPayDeptObjectsDTOS
     */
    private void packbonusPayDeptObjectsName(List<BonusPayObjectsDTO> bonusPayDeptObjectsDTOS) {
        if (StringUtils.isNotEmpty(bonusPayDeptObjectsDTOS)) {
            //员工id集合
            List<Long> collect = bonusPayDeptObjectsDTOS.stream().map(BonusPayObjectsDTO::getBonusPayObjectId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //远程查询部门信息集合
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (BonusPayObjectsDTO bonusPayObjectsDTO : bonusPayDeptObjectsDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (bonusPayObjectsDTO.getBonusPayObjectId() == datum.getDepartmentId()) {
                                //部门名称
                                bonusPayObjectsDTO.setBonusPayObjectName(datum.getDepartmentName());
                                //编码
                                bonusPayObjectsDTO.setDepartmentCode(datum.getDepartmentCode());
                                //部门负责人名称
                                bonusPayObjectsDTO.setDepartmentLeaderName(datum.getDepartmentLeaderName());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装获奖员工名称
     *
     * @param bonusPayEmployeeObjectsDTOS
     */
    private void packbonusPayEmployeeObjectsName(List<BonusPayObjectsDTO> bonusPayEmployeeObjectsDTOS) {
        if (StringUtils.isNotEmpty(bonusPayEmployeeObjectsDTOS)) {
            //员工id集合
            List<Long> collect = bonusPayEmployeeObjectsDTOS.stream().map(BonusPayObjectsDTO::getBonusPayObjectId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //远程查询员工信息集合
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(collect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (BonusPayObjectsDTO bonusPayObjectsDTO : bonusPayEmployeeObjectsDTOS) {
                        for (EmployeeDTO datum : data) {
                            if (bonusPayObjectsDTO.getBonusPayObjectId() == datum.getEmployeeId()) {
                                //员工名称
                                bonusPayObjectsDTO.setBonusPayObjectName(datum.getEmployeeName());
                                //工号
                                bonusPayObjectsDTO.setEmployeeCode(datum.getEmployeeCode());
                                //员工部门名称
                                bonusPayObjectsDTO.setEmployeeDepartmentName(datum.getEmployeeDepartmentName());
                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * 封装申请部门名称
     *
     * @param bonusPayApplicationDTO
     */
    private void packBonusPayApplicationDeptDeptName(BonusPayApplicationDTO bonusPayApplicationDTO) {
        if (StringUtils.isNotNull(bonusPayApplicationDTO)) {
            R<DepartmentDTO> departmentDTOR = remoteDepartmentService.selectdepartmentId(bonusPayApplicationDTO.getApplyDepartmentId(), SecurityConstants.INNER);
            DepartmentDTO data = departmentDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                if (bonusPayApplicationDTO.getApplyDepartmentId() == data.getDepartmentId()) {
                    //赋值申请部门名称
                    bonusPayApplicationDTO.setApplyDepartmentName(data.getDepartmentName());
                }
            }
            List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS = bonusPayBudgetDeptMapper.selectBonusPayBudgetDeptByBonusPayApplicationId(bonusPayApplicationDTO.getBonusPayApplicationId());
            if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOS)) {
                List<Long> collect = bonusPayBudgetDeptDTOS.stream().map(BonusPayBudgetDeptDTO::getDepartmentId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(collect)) {
                    //远程预算部门名称赋值
                    R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
                    List<DepartmentDTO> data2 = listR.getData();
                    if (StringUtils.isNotEmpty(data2)) {
                        bonusPayApplicationDTO.setBudgetDepartmentNames(data2.stream().map(DepartmentDTO::getDepartmentName).collect(Collectors.toList()).toString());
                        bonusPayApplicationDTO.setBudgetDepartmentIds(collect);
                    }
                }
            }

        }
    }

    /**
     * 封装预算部门名称
     *
     * @param bonusPayBudgetDeptDTOS
     */
    private void packBonusPayBudgetDeptDeptName(List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS) {
        if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOS)) {
            List<Long> collect = bonusPayBudgetDeptDTOS.stream().map(BonusPayBudgetDeptDTO::getDepartmentId).distinct().collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //远程调用部门赋值
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (BonusPayBudgetDeptDTO bonusPayBudgetDeptDTO : bonusPayBudgetDeptDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (bonusPayBudgetDeptDTO.getDepartmentId() == datum.getDepartmentId()) {
                                //赋值部门名称
                                bonusPayBudgetDeptDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }

        }
    }

    /**
     * 查询奖金发放申请表列表
     *
     * @param bonusPayApplicationDTO 奖金发放申请表
     * @return 奖金发放申请表
     */
    @Override
    public List<BonusPayApplicationDTO> selectBonusPayApplicationList(BonusPayApplicationDTO bonusPayApplicationDTO) {
        BonusPayApplication bonusPayApplication = new BonusPayApplication();
        BeanUtils.copyProperties(bonusPayApplicationDTO, bonusPayApplication);
        List<BonusPayApplicationDTO> bonusPayApplicationDTOS = bonusPayApplicationMapper.selectBonusPayApplicationList(bonusPayApplication);
        if (StringUtils.isNotEmpty(bonusPayApplicationDTOS)) {
            //申请部门ID集合
            List<Long> collect = bonusPayApplicationDTOS.stream().map(BonusPayApplicationDTO::getApplyDepartmentId).collect(Collectors.toList());
            //创建人id
            Set<Long> collect3 = bonusPayApplicationDTOS.stream().map(BonusPayApplicationDTO::getCreateBy).collect(Collectors.toSet());

            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS) {
                List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS = bonusPayBudgetDeptMapper.selectBonusPayBudgetDeptByBonusPayApplicationId(payApplicationDTO.getBonusPayApplicationId());
                if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOS)) {
                    payApplicationDTO.setBudgetDepartmentIds(bonusPayBudgetDeptDTOS.stream().map(BonusPayBudgetDeptDTO::getDepartmentId).collect(Collectors.toList()));
                }
            }

            //预算id集合
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS) {
                List<Long> budgetDepartmentIds = payApplicationDTO.getBudgetDepartmentIds();

                if (StringUtils.isNotEmpty(budgetDepartmentIds)) {
                    //远程部门赋值
                    R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(budgetDepartmentIds, SecurityConstants.INNER);
                    List<DepartmentDTO> data = listR.getData();
                    if (StringUtils.isNotEmpty(data)) {
                        payApplicationDTO.setBudgetDepartmentNames(data.stream().map(DepartmentDTO::getDepartmentName).collect(Collectors.toList()).toString());
                    }
                }
            }


            //远程部门赋值
            R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
            List<DepartmentDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS) {
                    for (DepartmentDTO datum : data) {
                        if (payApplicationDTO.getApplyDepartmentId() == datum.getDepartmentId()) {
                            //申请部门名称
                            payApplicationDTO.setApplyDepartmentName(datum.getDepartmentName());
                        }
                    }
                }
            }
            //远程人员名称赋值
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(collect3, SecurityConstants.INNER);
            List<UserDTO> data1 = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(data1)) {
                for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS) {
                    for (UserDTO userDTO : data1) {
                        if (payApplicationDTO.getCreateBy() == userDTO.getUserId()) {
                            //创建人名称
                            payApplicationDTO.setCreateName(userDTO.getEmployeeName());
                        }
                    }
                }
            }
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS) {
                if (payApplicationDTO.getAwardMonth() >= 10) {
                    payApplicationDTO.setAwardYearMonth(payApplicationDTO.getAwardYear() + "/" + payApplicationDTO.getAwardMonth());
                } else {
                    payApplicationDTO.setAwardYearMonth(payApplicationDTO.getAwardYear() + "/0" + payApplicationDTO.getAwardMonth());
                }

            }
        }

        return bonusPayApplicationDTOS;
    }

    /**
     * 新增奖金发放申请表
     *
     * @param bonusPayApplicationDTO 奖金发放申请表
     * @return 结果
     */
    @Override
    public BonusPayApplicationDTO insertBonusPayApplication(BonusPayApplicationDTO bonusPayApplicationDTO) {
        //奖金发放预算部门比例集合
        List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOs = bonusPayApplicationDTO.getBonusPayBudgetDeptDTOs();
        //奖金发放预算部门比例集合
        List<BonusPayBudgetDept> bonusPayBudgetDeptDTOList = new ArrayList<>();

        //获奖员工集合
        List<BonusPayObjectsDTO> bonusPayObjectsEmployeeDTOs = bonusPayApplicationDTO.getBonusPayObjectsEmployeeDTOs();
        //获奖部门集合
        List<BonusPayObjectsDTO> bonusPayObjectsDeptDTOs = bonusPayApplicationDTO.getBonusPayObjectsDeptDTOs();
        //奖金发放对象所有集合
        List<BonusPayObjects> bonusPayObjectsAllList = new ArrayList<>();

        BonusPayApplication bonusPayApplication = new BonusPayApplication();
        BeanUtils.copyProperties(bonusPayApplicationDTO, bonusPayApplication);
        bonusPayApplication.setCreateBy(SecurityUtils.getUserId());
        bonusPayApplication.setCreateTime(DateUtils.getNowDate());
        bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
        bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
        bonusPayApplication.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            bonusPayApplicationMapper.insertBonusPayApplication(bonusPayApplication);
        } catch (Exception e) {
            throw new ServiceException("新增奖金发放申请失败");
        }
        if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOs)) {
            for (BonusPayBudgetDeptDTO bonusPayBudgetDeptDTO : bonusPayBudgetDeptDTOs) {
                BonusPayBudgetDept bonusPayBudgetDept = new BonusPayBudgetDept();
                BeanUtils.copyProperties(bonusPayBudgetDeptDTO, bonusPayBudgetDept);
                //奖金发放申请ID
                bonusPayBudgetDept.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
                bonusPayBudgetDept.setCreateBy(SecurityUtils.getUserId());
                bonusPayBudgetDept.setCreateTime(DateUtils.getNowDate());
                bonusPayBudgetDept.setUpdateTime(DateUtils.getNowDate());
                bonusPayBudgetDept.setUpdateBy(SecurityUtils.getUserId());
                bonusPayBudgetDept.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                bonusPayBudgetDeptDTOList.add(bonusPayBudgetDept);
            }
        } else {
            BonusPayBudgetDept bonusPayBudgetDept = new BonusPayBudgetDept();
            //奖金发放申请ID
            bonusPayBudgetDept.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
            bonusPayBudgetDept.setDepartmentId(0L);
            bonusPayBudgetDept.setBonusPercentage(new BigDecimal("100"));
            bonusPayBudgetDept.setCreateBy(SecurityUtils.getUserId());
            bonusPayBudgetDept.setCreateTime(DateUtils.getNowDate());
            bonusPayBudgetDept.setUpdateTime(DateUtils.getNowDate());
            bonusPayBudgetDept.setUpdateBy(SecurityUtils.getUserId());
            bonusPayBudgetDept.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            try {
                bonusPayBudgetDeptMapper.insertBonusPayBudgetDept(bonusPayBudgetDept);
            } catch (Exception e) {
                throw new ServiceException("插入公司级奖金发放预算部门失败");
            }
        }
        if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOList)) {
            try {
                bonusPayBudgetDeptMapper.batchBonusPayBudgetDept(bonusPayBudgetDeptDTOList);
            } catch (Exception e) {
                throw new ServiceException("批量新增奖金发放预算部门失败");
            }
        }

        //员工发放对象
        if (StringUtils.isNotEmpty(bonusPayObjectsEmployeeDTOs)) {
            for (BonusPayObjectsDTO bonusPayObjectsEmployeeDTO : bonusPayObjectsEmployeeDTOs) {
                BonusPayObjects bonusPayObjects = new BonusPayObjects();
                BeanUtils.copyProperties(bonusPayObjectsEmployeeDTO, bonusPayObjects);
                //奖金发放申请ID
                bonusPayObjects.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
                //员工
                bonusPayObjects.setBonusPayObject(2);
                bonusPayObjects.setCreateBy(SecurityUtils.getUserId());
                bonusPayObjects.setCreateTime(DateUtils.getNowDate());
                bonusPayObjects.setUpdateTime(DateUtils.getNowDate());
                bonusPayObjects.setUpdateBy(SecurityUtils.getUserId());
                bonusPayObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                bonusPayObjectsAllList.add(bonusPayObjects);
            }
        }

        //部门发放对象
        if (StringUtils.isNotEmpty(bonusPayObjectsDeptDTOs)) {
            for (BonusPayObjectsDTO bonusPayObjectsDeptDTO : bonusPayObjectsDeptDTOs) {
                BonusPayObjects bonusPayObjects = new BonusPayObjects();
                BeanUtils.copyProperties(bonusPayObjectsDeptDTO, bonusPayObjects);
                //奖金发放申请ID
                bonusPayObjects.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
                //部门
                bonusPayObjects.setBonusPayObject(1);
                bonusPayObjects.setCreateBy(SecurityUtils.getUserId());
                bonusPayObjects.setCreateTime(DateUtils.getNowDate());
                bonusPayObjects.setUpdateTime(DateUtils.getNowDate());
                bonusPayObjects.setUpdateBy(SecurityUtils.getUserId());
                bonusPayObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                bonusPayObjectsAllList.add(bonusPayObjects);
            }
        }
        if (StringUtils.isNotEmpty(bonusPayObjectsAllList)) {
            try {
                bonusPayObjectsMapper.batchBonusPayObjects(bonusPayObjectsAllList);
            } catch (Exception e) {
                throw new ServiceException("批量新增奖金发放对象失败");
            }
        }
        bonusPayApplicationDTO.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
        return bonusPayApplicationDTO;
    }

    /**
     * 修改奖金发放申请表
     *
     * @param bonusPayApplicationDTO 奖金发放申请表
     * @return 结果
     */
    @Override
    public int updateBonusPayApplication(BonusPayApplicationDTO bonusPayApplicationDTO) {
        int i = 0;
        //奖金发放预算部门比例集合
        List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOs = bonusPayApplicationDTO.getBonusPayBudgetDeptDTOs();
        //要删除的差集
        List<Long> bonusPayBudgetDeptIds = new ArrayList<>();
        //修改奖金发放预算部门比例集合
        List<BonusPayBudgetDept> bonusPayBudgetDeptDTOUpdateList = new ArrayList<>();
        //新增奖金发放预算部门比例集合
        List<BonusPayBudgetDept> bonusPayBudgetDeptDTOAddList = new ArrayList<>();

        //获奖员工集合
        List<BonusPayObjectsDTO> bonusPayObjectsEmployeeDTOs = bonusPayApplicationDTO.getBonusPayObjectsEmployeeDTOs();
        //要删除的差集
        List<Long> bonusPayObjectsEmployeeId = new ArrayList<>();

        //获奖部门集合
        List<BonusPayObjectsDTO> bonusPayObjectsDeptDTOs = bonusPayApplicationDTO.getBonusPayObjectsDeptDTOs();
        //要删除的差集
        List<Long> bonusPayObjectsDeptId = new ArrayList<>();
        //新增奖金发放对象所有集合
        List<BonusPayObjects> bonusPayObjectsAddAllList = new ArrayList<>();
        //修改奖金发放对象所有集合
        List<BonusPayObjects> bonusPayObjectsUpdateAllList = new ArrayList<>();

        BonusPayApplication bonusPayApplication = new BonusPayApplication();
        BeanUtils.copyProperties(bonusPayApplicationDTO, bonusPayApplication);
        bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
        bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = bonusPayApplicationMapper.updateBonusPayApplication(bonusPayApplication);
        } catch (Exception e) {
            throw new ServiceException("新增奖金发放申请失败");
        }

        if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOs)) {
            //数据已存在的预算部门
            List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOList = bonusPayBudgetDeptMapper.selectBonusPayBudgetDeptByBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
            if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOList) && StringUtils.isNotEmpty(bonusPayBudgetDeptDTOs)) {
                //sterm流求差集
                bonusPayBudgetDeptIds = bonusPayBudgetDeptDTOList.stream().filter(a ->
                        !bonusPayBudgetDeptDTOs.stream().map(BonusPayBudgetDeptDTO::getBonusPayBudgetDeptId).collect(Collectors.toList()).contains(a.getBonusPayBudgetDeptId())
                ).collect(Collectors.toList()).stream().map(BonusPayBudgetDeptDTO::getBonusPayBudgetDeptId).collect(Collectors.toList());
            }


            if (StringUtils.isNotEmpty(bonusPayBudgetDeptIds)) {
                try {
                    bonusPayBudgetDeptMapper.logicDeleteBonusPayBudgetDeptByBonusPayBudgetDeptIds(bonusPayBudgetDeptIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除奖金发放预算失败");
                }
            }
            for (BonusPayBudgetDeptDTO bonusPayBudgetDeptDTO : bonusPayBudgetDeptDTOs) {
                BonusPayBudgetDept bonusPayBudgetDept = new BonusPayBudgetDept();
                BeanUtils.copyProperties(bonusPayBudgetDeptDTO, bonusPayBudgetDept);
                Long bonusPayBudgetDeptId = bonusPayBudgetDeptDTO.getBonusPayBudgetDeptId();
                if (null == bonusPayBudgetDeptId) {
                    //奖金发放申请ID
                    bonusPayBudgetDept.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
                    bonusPayBudgetDept.setUpdateTime(DateUtils.getNowDate());
                    bonusPayBudgetDept.setUpdateBy(SecurityUtils.getUserId());
                    bonusPayBudgetDept.setCreateTime(DateUtils.getNowDate());
                    bonusPayBudgetDept.setCreateBy(SecurityUtils.getUserId());
                    bonusPayBudgetDept.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    bonusPayBudgetDeptDTOAddList.add(bonusPayBudgetDept);
                } else {
                    bonusPayBudgetDept.setUpdateBy(SecurityUtils.getUserId());
                    bonusPayBudgetDept.setCreateTime(DateUtils.getNowDate());
                    bonusPayBudgetDeptDTOUpdateList.add(bonusPayBudgetDept);
                }
            }
        }

        if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOUpdateList)) {
            try {
                bonusPayBudgetDeptMapper.updateBonusPayBudgetDepts(bonusPayBudgetDeptDTOUpdateList);
            } catch (Exception e) {
                throw new ServiceException("批量修改奖金发放预算部门失败");
            }
        }
        if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOAddList)) {
            try {
                bonusPayBudgetDeptMapper.batchBonusPayBudgetDept(bonusPayBudgetDeptDTOAddList);
            } catch (Exception e) {
                throw new ServiceException("批量新增奖金发放预算部门失败");
            }
        }


        //员工发放对象
        if (StringUtils.isNotEmpty(bonusPayObjectsEmployeeDTOs)) {
            List<BonusPayObjectsDTO> bonusPayObjectsEmployeeDTOList = bonusPayObjectsMapper.selectBonusPayEmployeeObjectsByBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
            if (StringUtils.isNotEmpty(bonusPayObjectsEmployeeDTOList) && StringUtils.isNotEmpty(bonusPayObjectsEmployeeDTOs)) {
                //sterm流求差集
                bonusPayObjectsEmployeeId = bonusPayObjectsEmployeeDTOList.stream().filter(a ->
                        !bonusPayObjectsEmployeeDTOs.stream().map(BonusPayObjectsDTO::getBonusPayObjectsId).collect(Collectors.toList()).contains(a.getBonusPayObjectsId())
                ).collect(Collectors.toList()).stream().map(BonusPayObjectsDTO::getBonusPayObjectsId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(bonusPayObjectsEmployeeId)) {
                    try {
                        bonusPayObjectsMapper.logicDeleteBonusPayObjectsByBonusPayObjectsIds(bonusPayObjectsEmployeeId, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除奖金发放员工对象失败");
                    }
                }
            }
            for (BonusPayObjectsDTO bonusPayObjectsEmployeeDTO : bonusPayObjectsEmployeeDTOs) {
                BonusPayObjects bonusPayObjects = new BonusPayObjects();
                BeanUtils.copyProperties(bonusPayObjectsEmployeeDTO, bonusPayObjects);

                Integer bonusPayObject = bonusPayObjectsEmployeeDTO.getBonusPayObject();
                if (null == bonusPayObject) {
                    //奖金发放申请ID
                    bonusPayObjects.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
                    bonusPayObjects.setBonusPayObject(2);
                    bonusPayObjects.setCreateTime(DateUtils.getNowDate());
                    bonusPayObjects.setCreateBy(SecurityUtils.getUserId());
                    bonusPayObjects.setUpdateTime(DateUtils.getNowDate());
                    bonusPayObjects.setUpdateBy(SecurityUtils.getUserId());
                    bonusPayObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    bonusPayObjectsAddAllList.add(bonusPayObjects);
                } else {
                    bonusPayObjects.setUpdateTime(DateUtils.getNowDate());
                    bonusPayObjects.setUpdateBy(SecurityUtils.getUserId());
                    bonusPayObjectsUpdateAllList.add(bonusPayObjects);
                }
            }
        }

        //部门发放对象
        if (StringUtils.isNotEmpty(bonusPayObjectsDeptDTOs)) {
            List<BonusPayObjectsDTO> bonusPayObjectsEmployeeDTOList = bonusPayObjectsMapper.selectBonusPayDeptObjectsByBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
            if (StringUtils.isNotEmpty(bonusPayObjectsEmployeeDTOList) && StringUtils.isNotEmpty(bonusPayObjectsEmployeeDTOs)) {
                //sterm流求差集
                bonusPayObjectsDeptId = bonusPayObjectsEmployeeDTOList.stream().filter(a ->
                        !bonusPayObjectsEmployeeDTOs.stream().map(BonusPayObjectsDTO::getBonusPayObjectsId).collect(Collectors.toList()).contains(a.getBonusPayObjectsId())
                ).collect(Collectors.toList()).stream().map(BonusPayObjectsDTO::getBonusPayObjectsId).collect(Collectors.toList());
                if (StringUtils.isNotEmpty(bonusPayObjectsDeptId)) {
                    try {
                        bonusPayObjectsMapper.logicDeleteBonusPayObjectsByBonusPayObjectsIds(bonusPayObjectsDeptId, SecurityUtils.getUserId(), DateUtils.getNowDate());
                    } catch (Exception e) {
                        throw new ServiceException("逻辑批量删除奖金发放部门对象失败");
                    }
                }
            }
            for (BonusPayObjectsDTO bonusPayObjectsDeptDTO : bonusPayObjectsDeptDTOs) {
                BonusPayObjects bonusPayObjects = new BonusPayObjects();
                BeanUtils.copyProperties(bonusPayObjectsDeptDTO, bonusPayObjects);
                Integer bonusPayObject = bonusPayObjectsDeptDTO.getBonusPayObject();
                if (null == bonusPayObject) {
                    //奖金发放申请ID
                    bonusPayObjects.setBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
                    bonusPayObjects.setBonusPayObject(1);
                    bonusPayObjects.setCreateTime(DateUtils.getNowDate());
                    bonusPayObjects.setCreateBy(SecurityUtils.getUserId());
                    bonusPayObjects.setUpdateTime(DateUtils.getNowDate());
                    bonusPayObjects.setUpdateBy(SecurityUtils.getUserId());
                    bonusPayObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    bonusPayObjectsAddAllList.add(bonusPayObjects);
                } else {
                    bonusPayObjects.setUpdateTime(DateUtils.getNowDate());
                    bonusPayObjects.setUpdateBy(SecurityUtils.getUserId());
                    bonusPayObjectsUpdateAllList.add(bonusPayObjects);
                }
            }
        }
        if (StringUtils.isNotEmpty(bonusPayObjectsUpdateAllList)) {
            try {
                bonusPayObjectsMapper.updateBonusPayObjectss(bonusPayObjectsUpdateAllList);
            } catch (Exception e) {
                throw new ServiceException("批量修改奖金发放对象失败");
            }
        }
        if (StringUtils.isNotEmpty(bonusPayObjectsAddAllList)) {
            try {
                bonusPayObjectsMapper.batchBonusPayObjects(bonusPayObjectsAddAllList);
            } catch (Exception e) {
                throw new ServiceException("批量新增奖金发放对象失败");
            }
        }
        return i;
    }

    /**
     * 逻辑批量删除奖金发放申请表
     *
     * @param bonusPayApplicationIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteBonusPayApplicationByBonusPayApplicationIds(List<Long> bonusPayApplicationIds) {
        int i = 0;
        List<BonusPayApplicationDTO> bonusPayApplicationDTOS = bonusPayApplicationMapper.selectBonusPayApplicationByBonusPayApplicationIds(bonusPayApplicationIds);
        if (StringUtils.isEmpty(bonusPayApplicationDTOS)) {
            throw new ServiceException("数据不存在 请刷新重试！");
        }
        try {
            i = bonusPayApplicationMapper.logicDeleteBonusPayApplicationByBonusPayApplicationIds(bonusPayApplicationIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("逻辑批量删除奖金发放申请失败");
        }
        List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS = bonusPayBudgetDeptMapper.selectBonusPayBudgetDeptByBonusPayApplicationIds(bonusPayApplicationIds);
        if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOS)) {
            List<Long> collect = bonusPayBudgetDeptDTOS.stream().map(BonusPayBudgetDeptDTO::getBonusPayBudgetDeptId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    bonusPayBudgetDeptMapper.logicDeleteBonusPayBudgetDeptByBonusPayBudgetDeptIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除奖金发放预算部门失败");
                }
            }
        }
        List<BonusPayObjectsDTO> bonusPayObjectsDTOS = bonusPayObjectsMapper.selectBonusPayObjectsByBonusPayApplicationIds(bonusPayApplicationIds);
        if (StringUtils.isNotEmpty(bonusPayObjectsDTOS)) {
            List<Long> collect = bonusPayObjectsDTOS.stream().map(BonusPayObjectsDTO::getBonusPayObjectsId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    bonusPayObjectsMapper.logicDeleteBonusPayObjectsByBonusPayObjectsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除奖金发放对象失败");
                }
            }
        }
        return i;
    }

    /**
     * 物理删除奖金发放申请表信息
     *
     * @param bonusPayApplicationId 奖金发放申请表主键
     * @return 结果
     */
    @Override
    public int deleteBonusPayApplicationByBonusPayApplicationId(Long bonusPayApplicationId) {
        return bonusPayApplicationMapper.deleteBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationId);
    }

    /**
     * 分页查询奖金发放台账
     *
     * @param bonusPayApplicationDTO
     * @return
     */
    @Override
    public List<BonusPayStandingDTO> bonusGrantStandingList(BonusPayApplicationDTO bonusPayApplicationDTO) {
        List<BonusPayStandingDTO> bonusPayStandingDTOList = new ArrayList<>();
        //查看所有一级部门
        R<List<DepartmentDTO>> parentAll = remoteDepartmentService.getParentAll(SecurityConstants.INNER);
        List<DepartmentDTO> data = parentAll.getData();
        //所有二级工资项目为奖金且级别为部门级的三级工资项目
        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemByBonusId(bonusPayApplicationDTO.getSalaryItemId());
        //查找二级为奖金的三级工资条包含公司
        List<SalaryItemDTO> salaryItemDTOS1 = salaryItemMapper.applyByIdList(bonusPayApplicationDTO.getSalaryItemId());

        Integer departmentType = bonusPayApplicationDTO.getDepartmentType();
        if (null != departmentType) {
            if (departmentType == 0) {
                this.bonusGrantStandingApplyList(salaryItemDTOS1, data, bonusPayStandingDTOList,bonusPayApplicationDTO);
            } else if (departmentType == 1) {
                this.bonusGrantStandingBudgetList(salaryItemDTOS, data, bonusPayStandingDTOList,bonusPayApplicationDTO);
            } else {
                this.bonusGrantStandingBenefitList(salaryItemDTOS, data, bonusPayStandingDTOList,bonusPayApplicationDTO);
            }
        }
        return bonusPayStandingDTOList;
    }

    /**
     * 受益部门查询
     * @param salaryItemDTOS
     * @param data
     * @param bonusPayStandingDTOList
     * @param bonusPayApplicationDTO
     */
    private void bonusGrantStandingBenefitList(List<SalaryItemDTO> salaryItemDTOS, List<DepartmentDTO> data, List<BonusPayStandingDTO> bonusPayStandingDTOList, BonusPayApplicationDTO bonusPayApplicationDTO) {
        if (StringUtils.isNotEmpty(data)) {
            if (StringUtils.isNotEmpty(salaryItemDTOS)) {
                for (DepartmentDTO datum : data) {
                    for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                        BonusPayStandingDTO bonusPayStandingDTO = new BonusPayStandingDTO();
                        //部门id
                        bonusPayStandingDTO.setDepartmentId(datum.getDepartmentId());
                        //部门名称
                        bonusPayStandingDTO.setDepartmentName(datum.getDepartmentName());
                        //奖项类别,工资条ID
                        bonusPayStandingDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
                        //三级项目(奖项名称)
                        bonusPayStandingDTO.setThirdLevelItem(salaryItemDTO.getThirdLevelItem());
                        bonusPayStandingDTOList.add(bonusPayStandingDTO);
                    }
                }

                if (StringUtils.isNotEmpty(bonusPayStandingDTOList)){
                    for (BonusPayStandingDTO bonusPayStandingDTO : bonusPayStandingDTOList) {
                        R<List<DepartmentDTO>> sublevelDepartment = remoteDepartmentService.selectSublevelDepartment(bonusPayStandingDTO.getDepartmentId(), SecurityConstants.INNER);
                        List<DepartmentDTO> data1 = sublevelDepartment.getData();
                        if (StringUtils.isNotEmpty(data1)){
                            bonusPayApplicationDTO.setApplyDepartmentIds(data1.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList()));
                        }
                        List<BonusPayApplicationDTO> bonusPayApplicationDTOS = bonusPayApplicationMapper.bonusGrantStandingApplyList(bonusPayApplicationDTO);
                        if (StringUtils.isNotEmpty(bonusPayApplicationDTOS) && bonusPayApplicationDTOS.get(0) != null){
                            //根据月份分组
                            Map<Integer, List<BonusPayApplicationDTO>> bonusPayApplicationMonthMap = bonusPayApplicationDTOS.parallelStream().filter(f -> null != f.getAwardMonth()).collect(Collectors.groupingBy(BonusPayApplicationDTO::getAwardMonth));
                            for (Integer integer : bonusPayApplicationMonthMap.keySet()) {
                                List<BonusPayApplicationDTO> bonusPayApplicationDTOS1 = bonusPayApplicationMonthMap.get(integer);
                                //封装月份金额
                                this.packAmoutMonth(bonusPayStandingDTO, integer, bonusPayApplicationDTOS1);
                            }
                        }
                    }
                    //封装累计发放
                    packAmountTotal(bonusPayStandingDTOList);
                }
            }
        }
    }

    /**
     * 预算部门查询
     * @param salaryItemDTOS
     * @param data
     * @param bonusPayStandingDTOList
     * @param bonusPayApplicationDTO
     */
    private void bonusGrantStandingBudgetList(List<SalaryItemDTO> salaryItemDTOS, List<DepartmentDTO> data, List<BonusPayStandingDTO> bonusPayStandingDTOList, BonusPayApplicationDTO bonusPayApplicationDTO) {
        if (StringUtils.isNotEmpty(data)) {
            if (StringUtils.isNotEmpty(salaryItemDTOS)) {
                for (DepartmentDTO datum : data) {
                    for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                        BonusPayStandingDTO bonusPayStandingDTO = new BonusPayStandingDTO();
                        //部门id
                        bonusPayStandingDTO.setDepartmentId(datum.getDepartmentId());
                        //部门名称
                        bonusPayStandingDTO.setDepartmentName(datum.getDepartmentName());
                        //奖项类别,工资条ID
                        bonusPayStandingDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
                        //三级项目(奖项名称)
                        bonusPayStandingDTO.setThirdLevelItem(salaryItemDTO.getThirdLevelItem());
                        bonusPayStandingDTOList.add(bonusPayStandingDTO);
                    }
                }
                for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                    //公司级别
                    BonusPayStandingDTO bonusPayStandingDTO2 = new BonusPayStandingDTO();
                    bonusPayStandingDTO2.setDepartmentName("公司");
                    bonusPayStandingDTO2.setDepartmentId(0L);
                    //奖项类别,工资条ID
                    bonusPayStandingDTO2.setSalaryItemId(salaryItemDTO.getSalaryItemId());
                    //三级项目(奖项名称)
                    bonusPayStandingDTO2.setThirdLevelItem(salaryItemDTO.getThirdLevelItem());
                    bonusPayStandingDTOList.add(bonusPayStandingDTO2);
                }


                if (StringUtils.isNotEmpty(bonusPayStandingDTOList)){
                    for (BonusPayStandingDTO bonusPayStandingDTO : bonusPayStandingDTOList) {
                        Long departmentId = bonusPayStandingDTO.getDepartmentId();
                        if (0 != departmentId){
                            R<List<DepartmentDTO>> sublevelDepartment = remoteDepartmentService.selectSublevelDepartment(departmentId, SecurityConstants.INNER);
                            List<DepartmentDTO> data1 = sublevelDepartment.getData();
                            if (StringUtils.isNotEmpty(data1)){
                                bonusPayApplicationDTO.setApplyDepartmentIds(data1.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList()));
                            }
                        }else {
                            List<Long> applyDepartmentIds = new ArrayList<>();
                            applyDepartmentIds.add(0L);
                            bonusPayApplicationDTO.setApplyDepartmentIds(applyDepartmentIds);
                        }

                        List<BonusPayApplicationDTO> bonusPayApplicationDTOS = bonusPayApplicationMapper.bonusGrantStandingBudgetList(bonusPayApplicationDTO);
                        if (StringUtils.isNotEmpty(bonusPayApplicationDTOS) && bonusPayApplicationDTOS.get(0) != null){
                            //根据月份分组
                            Map<Integer, List<BonusPayApplicationDTO>> bonusPayApplicationMonthMap = bonusPayApplicationDTOS.parallelStream().filter(f -> null != f.getAwardMonth()).collect(Collectors.groupingBy(BonusPayApplicationDTO::getAwardMonth));
                            for (Integer integer : bonusPayApplicationMonthMap.keySet()) {
                                List<BonusPayApplicationDTO> bonusPayApplicationDTOS1 = bonusPayApplicationMonthMap.get(integer);
                                for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                                    BigDecimal bigDecimal = new BigDecimal("0");
                                    //奖项总金额
                                    BigDecimal awardTotalAmount = payApplicationDTO.getAwardTotalAmount();
                                    //奖金比例
                                    BigDecimal bonusPercentage = payApplicationDTO.getBonusPercentage();
                                    if (null != awardTotalAmount && awardTotalAmount.compareTo(new BigDecimal("0")) != 0 &&
                                            null != bonusPercentage && bonusPercentage.compareTo(new BigDecimal("0")) != 0){
                                        bigDecimal=awardTotalAmount.multiply(bonusPercentage.divide(new BigDecimal("100"))).setScale(10, RoundingMode.HALF_UP);
                                    }
                                    payApplicationDTO.setAwardTotalAmount(bigDecimal);
                                }
                                //封装月份金额
                                this.packAmoutMonth(bonusPayStandingDTO, integer, bonusPayApplicationDTOS1);
                            }
                        }
                    }
                    //封装累计发放
                    packAmountTotal(bonusPayStandingDTOList);
                }
            }
        }
    }

    /**
     * 申请部门查询
     *  @param salaryItemDTOS
     * @param data
     * @param bonusPayStandingDTOList
     * @param bonusPayApplicationDTO
     */
    private void bonusGrantStandingApplyList(List<SalaryItemDTO> salaryItemDTOS, List<DepartmentDTO> data, List<BonusPayStandingDTO> bonusPayStandingDTOList, BonusPayApplicationDTO bonusPayApplicationDTO) {
        if (StringUtils.isNotEmpty(data)) {
            if (StringUtils.isNotEmpty(salaryItemDTOS)) {
                for (DepartmentDTO datum : data) {
                    for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                        BonusPayStandingDTO bonusPayStandingDTO = new BonusPayStandingDTO();
                        //部门id
                        bonusPayStandingDTO.setDepartmentId(datum.getDepartmentId());
                        //部门名称
                        bonusPayStandingDTO.setDepartmentName(datum.getDepartmentName());
                        //奖项类别,工资条ID
                        bonusPayStandingDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
                        //三级项目(奖项名称)
                        bonusPayStandingDTO.setThirdLevelItem(salaryItemDTO.getThirdLevelItem());
                        bonusPayStandingDTOList.add(bonusPayStandingDTO);
                    }
                }

                if (StringUtils.isNotEmpty(bonusPayStandingDTOList)){
                    for (BonusPayStandingDTO bonusPayStandingDTO : bonusPayStandingDTOList) {
                        R<List<DepartmentDTO>> sublevelDepartment = remoteDepartmentService.selectSublevelDepartment(bonusPayStandingDTO.getDepartmentId(), SecurityConstants.INNER);
                        List<DepartmentDTO> data1 = sublevelDepartment.getData();
                        if (StringUtils.isNotEmpty(data1)){
                            bonusPayApplicationDTO.setApplyDepartmentIds(data1.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList()));
                        }
                        List<BonusPayApplicationDTO> bonusPayApplicationDTOS = bonusPayApplicationMapper.bonusGrantStandingApplyList(bonusPayApplicationDTO);
                        if (StringUtils.isNotEmpty(bonusPayApplicationDTOS) && bonusPayApplicationDTOS.get(0) != null){
                            //根据月份分组
                            Map<Integer, List<BonusPayApplicationDTO>> bonusPayApplicationMonthMap = bonusPayApplicationDTOS.parallelStream().filter(f -> null != f.getAwardMonth()).collect(Collectors.groupingBy(BonusPayApplicationDTO::getAwardMonth));
                            for (Integer integer : bonusPayApplicationMonthMap.keySet()) {
                                List<BonusPayApplicationDTO> bonusPayApplicationDTOS1 = bonusPayApplicationMonthMap.get(integer);
                                //封装月份金额
                                this.packAmoutMonth(bonusPayStandingDTO, integer, bonusPayApplicationDTOS1);
                            }
                        }
                    }
                    //封装累计发放
                    packAmountTotal(bonusPayStandingDTOList);
                }
            }
        }
    }

    /**
     * 封装累计发放
     * @param bonusPayStandingDTOList
     */
    private void packAmountTotal(List<BonusPayStandingDTO> bonusPayStandingDTOList) {
        if (StringUtils.isNotEmpty(bonusPayStandingDTOList)){
            for (BonusPayStandingDTO bonusPayStandingDTO : bonusPayStandingDTOList) {
                //累计发放
                BigDecimal amountTotal = new BigDecimal("0");
                //一月金额
                BigDecimal amountJanuary = bonusPayStandingDTO.getAmountJanuary();
                //二月金额
                BigDecimal amountFebruary = bonusPayStandingDTO.getAmountFebruary();
                //三月金额
                BigDecimal amountMarch = bonusPayStandingDTO.getAmountMarch();
                //四月金额
                BigDecimal amountApril = bonusPayStandingDTO.getAmountApril();
                //五月金额
                BigDecimal amountMay = bonusPayStandingDTO.getAmountMay();
                //六月金额
                BigDecimal amountJune = bonusPayStandingDTO.getAmountJune();
                //七月金额
                BigDecimal amountJuly = bonusPayStandingDTO.getAmountJuly();
                //八月金额
                BigDecimal amountAugust = bonusPayStandingDTO.getAmountAugust();
                //九月金额
                BigDecimal amountSeptember = bonusPayStandingDTO.getAmountSeptember();
                //十月金额
                BigDecimal amountOctober = bonusPayStandingDTO.getAmountOctober();
                //十一月金额
                BigDecimal amountNovember = bonusPayStandingDTO.getAmountNovember();
                //十二月金额
                BigDecimal amountDecember = bonusPayStandingDTO.getAmountDecember();
                try {
                    //累计发放
                    amountTotal.add(amountJanuary).add(amountFebruary).add(amountMarch).add(amountApril).add(amountMay).add(amountJune).add(amountJuly).add(amountAugust).add(amountSeptember).add(amountOctober).add(amountNovember).add(amountDecember).setScale(10,RoundingMode.HALF_UP);
                } catch (Exception e) {
                    //累计发放
                    bonusPayStandingDTO.setAmountTotal(new BigDecimal("0"));
                }
                //累计发放
                bonusPayStandingDTO.setAmountTotal(amountTotal);
            }
        }
    }

    /**
     * 封装月份金额
     * @param bonusPayStandingDTO
     * @param integer
     * @param bonusPayApplicationDTOS1
     */
    private void packAmoutMonth(BonusPayStandingDTO bonusPayStandingDTO, Integer integer, List<BonusPayApplicationDTO> bonusPayApplicationDTOS1) {
        if (integer ==1 ){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //一月金额
                    bonusPayStandingDTO.setAmountJanuary(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 2){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //二月金额
                    bonusPayStandingDTO.setAmountFebruary(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 3){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //三月金额
                    bonusPayStandingDTO.setAmountMarch(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 4){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //四月金额
                    bonusPayStandingDTO.setAmountApril(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 5){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //五月金额
                    bonusPayStandingDTO.setAmountMay(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 6){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //六月金额
                    bonusPayStandingDTO.setAmountJune(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 7){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //七月金额
                    bonusPayStandingDTO.setAmountJuly(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 8){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //八月金额
                    bonusPayStandingDTO.setAmountAugust(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 9){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //九月金额
                    bonusPayStandingDTO.setAmountSeptember(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 10){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //十月金额
                    bonusPayStandingDTO.setAmountOctober(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 11){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //十一月金额
                    bonusPayStandingDTO.setAmountNovember(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }else if (integer == 12){
            for (BonusPayApplicationDTO payApplicationDTO : bonusPayApplicationDTOS1) {
                if (bonusPayStandingDTO.getSalaryItemId() == payApplicationDTO.getSalaryItemId()){
                    //十二月金额
                    bonusPayStandingDTO.setAmountDecember(payApplicationDTO.getAwardTotalAmount());
                }
            }
        }
    }

    /**
     * 逻辑删除奖金发放申请表信息
     *
     * @param bonusPayApplicationDTO 奖金发放申请表
     * @return 结果
     */
    @Override
    public int logicDeleteBonusPayApplicationByBonusPayApplicationId(BonusPayApplicationDTO bonusPayApplicationDTO) {
        int i = 0;
        BonusPayApplicationDTO bonusPayApplicationDTO1 = bonusPayApplicationMapper.selectBonusPayApplicationByBonusPayApplicationId(bonusPayApplicationDTO.getBonusPayApplicationId());
        if (StringUtils.isNull(bonusPayApplicationDTO1)) {
            throw new ServiceException("数据不存在 请刷新页面重试！");
        }
        BonusPayApplication bonusPayApplication = new BonusPayApplication();
        bonusPayApplication.setBonusPayApplicationId(bonusPayApplicationDTO.getBonusPayApplicationId());
        bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
        bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());

        try {
            i = bonusPayApplicationMapper.logicDeleteBonusPayApplicationByBonusPayApplicationId(bonusPayApplication);
        } catch (Exception e) {
            throw new ServiceException("逻辑删除奖金发放申请失败");
        }
        List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS = bonusPayBudgetDeptMapper.selectBonusPayBudgetDeptByBonusPayApplicationId(bonusPayApplicationDTO.getBonusPayApplicationId());
        if (StringUtils.isNotEmpty(bonusPayBudgetDeptDTOS)) {
            List<Long> collect = bonusPayBudgetDeptDTOS.stream().map(BonusPayBudgetDeptDTO::getBonusPayBudgetDeptId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    bonusPayBudgetDeptMapper.logicDeleteBonusPayBudgetDeptByBonusPayBudgetDeptIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除奖金发放预算部门失败");
                }
            }
        }
        //根据奖金发放主表主键查询奖金发放对象员工部门表
        List<BonusPayObjectsDTO> bonusPayObjectsDTOS = bonusPayObjectsMapper.selectBonusPayObjectsByBonusPayApplicationId(bonusPayApplicationDTO.getBonusPayApplicationId());
        if (StringUtils.isNotEmpty(bonusPayObjectsDTOS)) {
            List<Long> collect = bonusPayObjectsDTOS.stream().map(BonusPayObjectsDTO::getBonusPayObjectsId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    bonusPayObjectsMapper.logicDeleteBonusPayObjectsByBonusPayObjectsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("逻辑批量删除奖金发放对象失败");
                }
            }
        }

        return i;
    }

    /**
     * 物理删除奖金发放申请表信息
     *
     * @param bonusPayApplicationDTO 奖金发放申请表
     * @return 结果
     */

    @Override
    public int deleteBonusPayApplicationByBonusPayApplicationId(BonusPayApplicationDTO bonusPayApplicationDTO) {
        BonusPayApplication bonusPayApplication = new BonusPayApplication();
        BeanUtils.copyProperties(bonusPayApplicationDTO, bonusPayApplication);
        return bonusPayApplicationMapper.deleteBonusPayApplicationByBonusPayApplicationId(bonusPayApplication.getBonusPayApplicationId());
    }

    /**
     * 物理批量删除奖金发放申请表
     *
     * @param bonusPayApplicationDtos 需要删除的奖金发放申请表主键
     * @return 结果
     */

    @Override
    public int deleteBonusPayApplicationByBonusPayApplicationIds(List<BonusPayApplicationDTO> bonusPayApplicationDtos) {
        List<Long> stringList = new ArrayList();
        for (BonusPayApplicationDTO bonusPayApplicationDTO : bonusPayApplicationDtos) {
            stringList.add(bonusPayApplicationDTO.getBonusPayApplicationId());
        }
        return bonusPayApplicationMapper.deleteBonusPayApplicationByBonusPayApplicationIds(stringList);
    }

    /**
     * 批量新增奖金发放申请表信息
     *
     * @param bonusPayApplicationDtos 奖金发放申请表对象
     */

    public int insertBonusPayApplications(List<BonusPayApplicationDTO> bonusPayApplicationDtos) {
        List<BonusPayApplication> bonusPayApplicationList = new ArrayList();

        for (BonusPayApplicationDTO bonusPayApplicationDTO : bonusPayApplicationDtos) {
            BonusPayApplication bonusPayApplication = new BonusPayApplication();
            BeanUtils.copyProperties(bonusPayApplicationDTO, bonusPayApplication);
            bonusPayApplication.setCreateBy(SecurityUtils.getUserId());
            bonusPayApplication.setCreateTime(DateUtils.getNowDate());
            bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
            bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
            bonusPayApplication.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            bonusPayApplicationList.add(bonusPayApplication);
        }
        return bonusPayApplicationMapper.batchBonusPayApplication(bonusPayApplicationList);
    }

    /**
     * 批量修改奖金发放申请表信息
     *
     * @param bonusPayApplicationDtos 奖金发放申请表对象
     */

    public int updateBonusPayApplications(List<BonusPayApplicationDTO> bonusPayApplicationDtos) {
        List<BonusPayApplication> bonusPayApplicationList = new ArrayList();

        for (BonusPayApplicationDTO bonusPayApplicationDTO : bonusPayApplicationDtos) {
            BonusPayApplication bonusPayApplication = new BonusPayApplication();
            BeanUtils.copyProperties(bonusPayApplicationDTO, bonusPayApplication);
            bonusPayApplication.setCreateBy(SecurityUtils.getUserId());
            bonusPayApplication.setCreateTime(DateUtils.getNowDate());
            bonusPayApplication.setUpdateTime(DateUtils.getNowDate());
            bonusPayApplication.setUpdateBy(SecurityUtils.getUserId());
            bonusPayApplicationList.add(bonusPayApplication);
        }
        return bonusPayApplicationMapper.updateBonusPayApplications(bonusPayApplicationList);
    }

}

