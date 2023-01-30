package net.qixiaowei.operate.cloud.service.impl.bonus;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.bonus.DeptAnnualBonus;
import net.qixiaowei.operate.cloud.api.domain.bonus.DeptAnnualBonusFactor;
import net.qixiaowei.operate.cloud.api.domain.bonus.DeptAnnualBonusOperate;
import net.qixiaowei.operate.cloud.api.dto.bonus.*;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import net.qixiaowei.operate.cloud.api.dto.salary.*;
import net.qixiaowei.operate.cloud.mapper.bonus.*;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalObjectsMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryItemMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.bonus.IDeptAnnualBonusService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * DeptAnnualBonusService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-12-06
 */
@Service
public class DeptAnnualBonusServiceImpl implements IDeptAnnualBonusService {
    @Autowired
    private DeptAnnualBonusMapper deptAnnualBonusMapper;
    @Autowired
    private DeptAnnualBonusFactorMapper deptAnnualBonusFactorMapper;
    @Autowired
    private DeptAnnualBonusOperateMapper deptAnnualBonusOperateMapper;
    @Autowired
    private BonusBudgetMapper bonusBudgetMapper;
    @Autowired
    private BonusBudgetParametersMapper bonusBudgetParametersMapper;
    @Autowired
    private RemoteIndicatorService remoteIndicatorService;
    @Autowired
    private SalaryPayMapper salaryPayMapper;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private PerformanceAppraisalObjectsMapper performanceAppraisalObjectsMapper;

    @Autowired
    private BonusPayApplicationMapper bonusPayApplicationMapper;
    @Autowired
    private BonusPayBudgetDeptMapper bonusPayBudgetDeptMapper;
    @Autowired
    private BonusPayObjectsMapper bonusPayObjectsMapper;
    @Autowired
    private SalaryItemMapper salaryItemMapper;
    @Autowired
    private DeptBonusBudgetMapper deptBonusBudgetMapper;
    @Autowired
    private DeptBonusBudgetDetailsMapper deptBonusBudgetDetailsMapper;

    /**
     * 查询部门年终奖表
     *
     * @param deptAnnualBonusId 部门年终奖表主键
     * @return 部门年终奖表
     */
    @Override
    public DeptAnnualBonusDTO selectDeptAnnualBonusByDeptAnnualBonusId(Long deptAnnualBonusId) {
        DeptAnnualBonusDTO deptAnnualBonusDTO = deptAnnualBonusMapper.selectDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusId);
        if (StringUtils.isNull(deptAnnualBonusDTO)) {
            throw new ServiceException("数据不存在 请刷新重新！");
        }
        //部门年终奖经营绩效结果表集合
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOList = deptAnnualBonusOperateMapper.selectDeptAnnualBonusOperateByDeptAnnualBonusId(deptAnnualBonusId);
        //1+∑各驱动因素的奖金系数
        BigDecimal allActualPerformanceBonusFactorSum = new BigDecimal("0");
        //奖金预算表
        BonusBudgetDTO bonusBudgetDTO = new BonusBudgetDTO();
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOList)) {
            for (DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO : deptAnnualBonusOperateDTOList) {
                //目标超额完成率（%） 公式=（实际值÷目标值）-1。
                BigDecimal targetExcessPerComp = new BigDecimal("0");
                //奖金系数（实际）公式=权重×目标超额完成率。
                BigDecimal actualPerformanceBonusFactor = new BigDecimal("0");
                //目标值
                BigDecimal targetValue = deptAnnualBonusOperateDTO.getTargetValue();
                //实际值
                BigDecimal actualValue = deptAnnualBonusOperateDTO.getActualValue();
                //奖金权重(%)
                BigDecimal bonusWeight = deptAnnualBonusOperateDTO.getBonusWeight();
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != actualValue && actualValue.compareTo(new BigDecimal("0")) != 0) {
                    targetExcessPerComp = actualValue.divide(targetValue, 10, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                if (null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0 &&
                        targetExcessPerComp.compareTo(new BigDecimal("0")) != 0) {
                    actualPerformanceBonusFactor = bonusWeight.divide(new BigDecimal("100")).multiply(targetExcessPerComp.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                deptAnnualBonusOperateDTO.setTargetExcessPerComp(targetExcessPerComp);
                deptAnnualBonusOperateDTO.setActualPerformanceBonusFactor(actualPerformanceBonusFactor);
            }
            if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOList)) {
                for (DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO : deptAnnualBonusOperateDTOList) {
                    //奖金系数（实际）
                    BigDecimal actualPerformanceBonusFactor = deptAnnualBonusOperateDTO.getActualPerformanceBonusFactor();
                    if (null != actualPerformanceBonusFactor && actualPerformanceBonusFactor.compareTo(new BigDecimal("0")) != 0) {
                        allActualPerformanceBonusFactorSum = allActualPerformanceBonusFactorSum.add(actualPerformanceBonusFactor);
                    }
                }
            }
        }

        //部门年终奖系数表集合
        List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOS = deptAnnualBonusFactorMapper.selectDeptAnnualBonusFactorByDeptAnnualBonusId(deptAnnualBonusId);
        if (StringUtils.isNotEmpty(deptAnnualBonusFactorDTOS)) {
            //远程调用部门赋值名称
            List<Long> collect = deptAnnualBonusFactorDTOS.stream().map(DeptAnnualBonusFactorDTO::getDepartmentId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(collect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO : deptAnnualBonusFactorDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (datum.getDepartmentId().equals(deptAnnualBonusFactorDTO.getDepartmentId())) {
                                deptAnnualBonusFactorDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }
        }
        //部门可发年终奖集合
        List<DeptAnnualBonusCanGrantDTO> deptAnnualBonusCanGrantDTOs = new ArrayList<>();
        //所有的员工的总薪酬包
        BigDecimal deptPaymentBonusSum = new BigDecimal("0");
        //组织权重
        BigDecimal weight = new BigDecimal("0");
        //总奖金预算
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudgetParametersMapper.selectBonusBudgetParametersByAnnualBonusYear(deptAnnualBonusDTO.getAnnualBonusYear());
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            //年初可发总奖金包预算
            deptAnnualBonusDTO.setBeYearDeveAmountBonus(bonusBudgetParametersDTOS.get(0).getAmountBonusBudget());
            //根据总奖金id查询奖金预算参数表
            List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS1 = bonusBudgetParametersMapper.selectBonusBudgetParametersByBonusBudgetId(bonusBudgetParametersDTOS.get(0).getBonusBudgetId());
            //封装奖金预算参考值1
            BonusBudgetServiceImpl.packBounLadderNum(bonusBudgetDTO, bonusBudgetParametersDTOS1);
        }

        //2 可发经营奖总包
        packDeptAnnualBonus(deptAnnualBonusDTO.getAnnualBonusYear(), allActualPerformanceBonusFactorSum, deptAnnualBonusDTO, bonusBudgetDTO);
        //3 部门年终奖系数表数据
        packDeptAnnualBonusFactor(deptAnnualBonusDTO.getAnnualBonusYear(), deptAnnualBonusFactorDTOS, deptPaymentBonusSum, weight);


        //4 部门可发年终奖
        packQueryDeptAnnualBonusCanGrantDTO(deptAnnualBonusDTO.getAnnualBonusYear(), deptAnnualBonusCanGrantDTOs, deptAnnualBonusFactorDTOS, deptAnnualBonusDTO.getDepartmentAnnualBonus());
        //部门年终奖经营绩效结果表集合
        deptAnnualBonusDTO.setDeptAnnualBonusOperateDTOs(deptAnnualBonusOperateDTOList);
        deptAnnualBonusDTO.setDeptAnnualBonusFactorDTOs(deptAnnualBonusFactorDTOS);
        deptAnnualBonusDTO.setDeptAnnualBonusCanGrantDTOs(deptAnnualBonusCanGrantDTOs);
        return deptAnnualBonusDTO;
    }

    /**
     * 查询部门年终奖表列表
     *
     * @param deptAnnualBonusDTO 部门年终奖表
     * @return 部门年终奖表
     */
    @Override
    public List<DeptAnnualBonusDTO> selectDeptAnnualBonusList(DeptAnnualBonusDTO deptAnnualBonusDTO) {
        DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
        if (StringUtils.isNotNull(deptAnnualBonusDTO)) {
            Date createTime = deptAnnualBonusDTO.getCreateTime();
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
            if (StringUtils.isNotNull(createTime)) {
                String createTimeFormat = simpleDateFormat.format(createTime);
                deptAnnualBonus.setCreateTimeFormat(createTimeFormat);
            }

        }
        BeanUtils.copyProperties(deptAnnualBonusDTO, deptAnnualBonus);
        return deptAnnualBonusMapper.selectDeptAnnualBonusList(deptAnnualBonus);
    }

    /**
     * 新增部门年终奖表
     *
     * @param deptAnnualBonusDTO 部门年终奖表
     * @return 结果
     */
    @Override
    @Transactional
    public DeptAnnualBonusDTO insertDeptAnnualBonus(DeptAnnualBonusDTO deptAnnualBonusDTO) {
        DeptAnnualBonusDTO deptAnnualBonusDTO1 = deptAnnualBonusMapper.selectDeptAnnualBonusByAnnualBonusYearAdd(deptAnnualBonusDTO.getAnnualBonusYear());
        if (StringUtils.isNotNull(deptAnnualBonusDTO1)) {
            throw new ServiceException(deptAnnualBonusDTO.getAnnualBonusYear() + "年 数据已存在 无需重复添加！");
        }
        //部门年终奖系数表集合
        List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOs = deptAnnualBonusDTO.getDeptAnnualBonusFactorDTOs();
        //新增部门年终奖系数表集合
        List<DeptAnnualBonusFactor> deptAnnualBonusFactorList = new ArrayList<>();
        //部门年终奖经营绩效结果表集合
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOs = deptAnnualBonusDTO.getDeptAnnualBonusOperateDTOs();
        //新增部门年终奖经营绩效结果表集合
        List<DeptAnnualBonusOperate> deptAnnualBonusOperateList = new ArrayList<>();

        DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
        BeanUtils.copyProperties(deptAnnualBonusDTO, deptAnnualBonus);
        deptAnnualBonus.setCreateBy(SecurityUtils.getUserId());
        deptAnnualBonus.setCreateTime(DateUtils.getNowDate());
        deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
        deptAnnualBonus.setStatus(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        deptAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        try {
            deptAnnualBonusMapper.insertDeptAnnualBonus(deptAnnualBonus);
        } catch (Exception e) {
            throw new ServiceException("新增部门年终奖失败");
        }

        if (StringUtils.isNotEmpty(deptAnnualBonusFactorDTOs)) {
            for (DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO : deptAnnualBonusFactorDTOs) {

                DeptAnnualBonusFactor deptAnnualBonusFactor = new DeptAnnualBonusFactor();
                BeanUtils.copyProperties(deptAnnualBonusFactorDTO, deptAnnualBonusFactor);
                //部门年终奖ID
                deptAnnualBonusFactor.setDeptAnnualBonusId(deptAnnualBonus.getDeptAnnualBonusId());
                deptAnnualBonusFactor.setCreateBy(SecurityUtils.getUserId());
                deptAnnualBonusFactor.setCreateTime(DateUtils.getNowDate());
                deptAnnualBonusFactor.setUpdateTime(DateUtils.getNowDate());
                deptAnnualBonusFactor.setUpdateBy(SecurityUtils.getUserId());
                deptAnnualBonusFactor.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                deptAnnualBonusFactorList.add(deptAnnualBonusFactor);
            }

        }
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOs)) {
            for (DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO : deptAnnualBonusOperateDTOs) {
                DeptAnnualBonusOperate deptAnnualBonusOperate = new DeptAnnualBonusOperate();
                BeanUtils.copyProperties(deptAnnualBonusOperateDTO, deptAnnualBonusOperate);

                //部门年终奖ID
                deptAnnualBonusOperate.setDeptAnnualBonusId(deptAnnualBonus.getDeptAnnualBonusId());
                deptAnnualBonusOperate.setCreateBy(SecurityUtils.getUserId());
                deptAnnualBonusOperate.setCreateTime(DateUtils.getNowDate());
                deptAnnualBonusOperate.setUpdateTime(DateUtils.getNowDate());
                deptAnnualBonusOperate.setUpdateBy(SecurityUtils.getUserId());
                deptAnnualBonusOperate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                deptAnnualBonusOperateList.add(deptAnnualBonusOperate);
            }
        }
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateList)) {
            try {
                deptAnnualBonusOperateMapper.batchDeptAnnualBonusOperate(deptAnnualBonusOperateList);
            } catch (Exception e) {
                throw new ServiceException("批量新增部门年终奖经营绩效结果表失败");
            }
        }
        if (StringUtils.isNotEmpty(deptAnnualBonusFactorList)) {
            try {
                deptAnnualBonusFactorMapper.batchDeptAnnualBonusFactor(deptAnnualBonusFactorList);
            } catch (Exception e) {
                throw new ServiceException("批量新增部门年终奖系数表失败");
            }
        }
        deptAnnualBonusDTO.setDeptAnnualBonusId(deptAnnualBonus.getDeptAnnualBonusId());
        return deptAnnualBonusDTO;
    }

    /**
     * 修改部门年终奖表
     *
     * @param deptAnnualBonusDTO 部门年终奖表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateDeptAnnualBonus(DeptAnnualBonusDTO deptAnnualBonusDTO) {
        int i = 0;
        //部门年终奖系数表集合
        List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOs = deptAnnualBonusDTO.getDeptAnnualBonusFactorDTOs();
        //修改部门年终奖系数表集合
        List<DeptAnnualBonusFactor> deptAnnualBonusFactorList = new ArrayList<>();
        //部门年终奖经营绩效结果表集合
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOs = deptAnnualBonusDTO.getDeptAnnualBonusOperateDTOs();
        //修改部门年终奖经营绩效结果表集合
        List<DeptAnnualBonusOperate> deptAnnualBonusOperateList = new ArrayList<>();

        DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
        BeanUtils.copyProperties(deptAnnualBonusDTO, deptAnnualBonus);
        deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = deptAnnualBonusMapper.updateDeptAnnualBonus(deptAnnualBonus);
        } catch (Exception e) {
            throw new ServiceException("新增部门年终奖失败");
        }

        if (StringUtils.isNotEmpty(deptAnnualBonusFactorDTOs)) {
            for (DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO : deptAnnualBonusFactorDTOs) {
                DeptAnnualBonusFactor deptAnnualBonusFactor = new DeptAnnualBonusFactor();
                BeanUtils.copyProperties(deptAnnualBonusFactorDTO, deptAnnualBonusFactor);
                deptAnnualBonusFactor.setUpdateTime(DateUtils.getNowDate());
                deptAnnualBonusFactor.setUpdateBy(SecurityUtils.getUserId());
                deptAnnualBonusFactorList.add(deptAnnualBonusFactor);
            }

        }
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOs)) {
            for (DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO : deptAnnualBonusOperateDTOs) {
                DeptAnnualBonusOperate deptAnnualBonusOperate = new DeptAnnualBonusOperate();
                BeanUtils.copyProperties(deptAnnualBonusOperateDTO, deptAnnualBonusOperate);
                deptAnnualBonusOperate.setUpdateTime(DateUtils.getNowDate());
                deptAnnualBonusOperate.setUpdateBy(SecurityUtils.getUserId());
                deptAnnualBonusOperateList.add(deptAnnualBonusOperate);
            }
        }
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateList)) {
            try {
                deptAnnualBonusOperateMapper.updateDeptAnnualBonusOperates(deptAnnualBonusOperateList);
            } catch (Exception e) {
                throw new ServiceException("批量修改部门年终奖经营绩效结果表");
            }
        }
        if (StringUtils.isNotEmpty(deptAnnualBonusFactorList)) {
            try {
                deptAnnualBonusFactorMapper.updateDeptAnnualBonusFactors(deptAnnualBonusFactorList);
            } catch (Exception e) {
                throw new ServiceException("批量修改部门年终奖系数表");
            }
        }
        return i;
    }

    /**
     * 逻辑批量删除部门年终奖表
     *
     * @param deptAnnualBonusIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(List<Long> deptAnnualBonusIds) {
        int i = 0;
        List<DeptAnnualBonusDTO> deptAnnualBonusDTOS = deptAnnualBonusMapper.selectDeptAnnualBonusByDeptAnnualBonusIds(deptAnnualBonusIds);
        if (StringUtils.isEmpty(deptAnnualBonusDTOS)) {
            throw new ServiceException("数据不存在 请刷新重试！");
        }
        try {
            i = deptAnnualBonusMapper.logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(deptAnnualBonusIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("批量删除部门年终奖表失败");
        }
        List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOS = deptAnnualBonusFactorMapper.selectDeptAnnualBonusFactorByDeptAnnualBonusIds(deptAnnualBonusIds);
        if (StringUtils.isNotEmpty(deptAnnualBonusFactorDTOS)) {
            List<Long> collect = deptAnnualBonusFactorDTOS.stream().map(DeptAnnualBonusFactorDTO::getDeptAnnualBonusFactorId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    deptAnnualBonusFactorMapper.logicDeleteDeptAnnualBonusFactorByDeptAnnualBonusFactorIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("批量删除部门年终奖系数表失败");
                }
            }
        }
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOList = deptAnnualBonusOperateMapper.selectDeptAnnualBonusOperateByDeptAnnualBonusIds(deptAnnualBonusIds);
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOList)) {
            List<Long> collect = deptAnnualBonusOperateDTOList.stream().map(DeptAnnualBonusOperateDTO::getDeptAnnualBonusOperateId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    deptAnnualBonusOperateMapper.logicDeleteDeptAnnualBonusOperateByDeptAnnualBonusOperateIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("批量删除部门年终奖经营绩效结果表失败");
                }
            }
        }
        return i;
    }

    /**
     * 物理删除部门年终奖表信息
     *
     * @param deptAnnualBonusId 部门年终奖表主键
     * @return 结果
     */
    @Override
    public int deleteDeptAnnualBonusByDeptAnnualBonusId(Long deptAnnualBonusId) {
        return deptAnnualBonusMapper.deleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusId);
    }

    /**
     * 部门年终奖预制数据
     *
     * @param annualBonusYear
     * @return
     */
    @Override
    public DeptAnnualBonusDTO addPrefabricate(int annualBonusYear) {
        //1+∑各驱动因素的奖金系数
        BigDecimal allActualPerformanceBonusFactorSum = new BigDecimal("0");
        //部门年终奖表
        DeptAnnualBonusDTO deptAnnualBonusDTO = new DeptAnnualBonusDTO();
        //部门年终奖系数表集合
        List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOs = new ArrayList<>();
        //部门年终奖经营绩效结果表集合
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOs = new ArrayList<>();
        //部门可发年终奖集合
        List<DeptAnnualBonusCanGrantDTO> deptAnnualBonusCanGrantDTOs = new ArrayList<>();
        //奖金预算表
        BonusBudgetDTO bonusBudgetDTO = new BonusBudgetDTO();
        //部门年终奖经营绩效结果表集合
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOList = new ArrayList<>();
        //总奖金预算
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudgetParametersMapper.selectBonusBudgetParametersByAnnualBonusYear(annualBonusYear);
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)) {
            //封装部门年终奖经营绩效结果集合
            packDeptAnnualBonusOperates(deptAnnualBonusOperateDTOList, bonusBudgetParametersDTOS, deptAnnualBonusDTO, allActualPerformanceBonusFactorSum);

            //根据总奖金id查询奖金预算参数表
            List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS1 = bonusBudgetParametersMapper.selectBonusBudgetParametersByBonusBudgetId(bonusBudgetParametersDTOS.get(0).getBonusBudgetId());
            //封装奖金预算参考值1
            BonusBudgetServiceImpl.packBounLadderNum(bonusBudgetDTO, bonusBudgetParametersDTOS1);
        }
        // 1经营绩效结果
        packDeptAnnualBonusOperate(deptAnnualBonusOperateDTOs, annualBonusYear);
        //2 可发经营奖总包
        packDeptAnnualBonus(annualBonusYear, allActualPerformanceBonusFactorSum, deptAnnualBonusDTO, bonusBudgetDTO);

        //所有的员工的总薪酬包
        BigDecimal deptPaymentBonusSum = new BigDecimal("0");
        //组织权重
        BigDecimal weight = new BigDecimal("0");
        //3 部门年终奖系数表数据
        packDeptAnnualBonusFactor(annualBonusYear, deptAnnualBonusFactorDTOs, deptPaymentBonusSum, weight);

        //4 部门可发年终奖
        packAddDeptAnnualBonusCanGrantDTO(annualBonusYear, deptAnnualBonusCanGrantDTOs, deptAnnualBonusFactorDTOs, deptAnnualBonusDTO.getDepartmentAnnualBonus());

        deptAnnualBonusDTO.setDeptAnnualBonusOperateDTOs(deptAnnualBonusOperateDTOs);
        deptAnnualBonusDTO.setDeptAnnualBonusFactorDTOs(deptAnnualBonusFactorDTOs);
        deptAnnualBonusDTO.setDeptAnnualBonusCanGrantDTOs(deptAnnualBonusCanGrantDTOs);
        deptAnnualBonusDTO.setAnnualBonusYear(annualBonusYear);
        return deptAnnualBonusDTO;
    }

    /**
     * 预制数据封装部门可发年终奖
     *
     * @param annualBonusYear
     * @param deptAnnualBonusCanGrantDTOs
     * @param deptAnnualBonusFactorDTOs
     * @param departmentAnnualBonus
     */
    private void packAddDeptAnnualBonusCanGrantDTO(int annualBonusYear, List<DeptAnnualBonusCanGrantDTO> deptAnnualBonusCanGrantDTOs, List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOs, BigDecimal departmentAnnualBonus) {
        //总奖金包预算
        BigDecimal amountBonusBudget = new BigDecimal("0");

        BonusBudgetDTO bonusBudgetDTO = bonusBudgetMapper.selectBonusBudgetByBudgetYear(annualBonusYear);
        if (StringUtils.isNotNull(bonusBudgetDTO)) {
            amountBonusBudget = bonusBudgetDTO.getAmountBonusBudget();
        }


        //部门奖金预算 根据部门id分组
        Map<Long, List<DeptBonusBudgetDetailsDTO>> deptMap = new HashMap<>();
        //远程调用查看所有一级部门
        R<List<DepartmentDTO>> listR = remoteDepartmentService.getParentAll(SecurityConstants.INNER);
        List<DepartmentDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {
            List<Long> collect = data.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //部门奖金预算
                List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOList = deptBonusBudgetDetailsMapper.selectDeptBonusBudgetBybudgetYearAnnua(annualBonusYear, collect);
                //根据部门id分组
                deptMap = deptBonusBudgetDetailsDTOList.parallelStream().collect(Collectors.groupingBy(DeptBonusBudgetDetailsDTO::getDepartmentId));

            }

            for (DepartmentDTO datum : data) {
                //部门奖金包发放奖金项目
                List<DeptAnnualBonusItemDTO> deptAnnualBonusItemDTOS = new ArrayList<>();
                //部门奖金占比
                BigDecimal deptBonusPercentage = new BigDecimal("0");
                //年初奖金预算-金额
                BigDecimal beYearCanGrantBonusBudgetAmount = new BigDecimal("0");
                DeptAnnualBonusCanGrantDTO deptAnnualBonusCanGrantDTO = new DeptAnnualBonusCanGrantDTO();
                deptAnnualBonusCanGrantDTO.setDepartmentId(datum.getDepartmentId());
                deptAnnualBonusCanGrantDTO.setDepartmentName(datum.getDepartmentName());

                if (StringUtils.isNotEmpty(deptMap)) {
                    List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOList = deptMap.get(datum.getDepartmentId());
                    if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOList)) {
                        for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOList) {
                            //部门总奖金包
                            BigDecimal deptAmountBonus = new BigDecimal("0");
                            //部门奖金占比
                            deptBonusPercentage = deptBonusBudgetDetailsDTO.getDeptBonusPercentage();
                            //战略奖比例
                            BigDecimal strategyAwardPercentage = deptBonusBudgetDetailsDTO.getStrategyAwardPercentage();

                            if (amountBonusBudget.compareTo(new BigDecimal("0")) != 0 && strategyAwardPercentage.compareTo(new BigDecimal("0")) != 0) {
                                deptAmountBonus = amountBonusBudget.subtract(amountBonusBudget.multiply(strategyAwardPercentage.divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                            if (deptAmountBonus.compareTo(new BigDecimal("0")) != 0 && deptBonusPercentage.compareTo(new BigDecimal("0")) != 0) {
                                beYearCanGrantBonusBudgetAmount = deptAmountBonus.multiply(deptBonusPercentage.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                            }
                        }

                    }
                }
                //年初奖金预算-奖金占比
                deptAnnualBonusCanGrantDTO.setBeYearCanGrantBonusBudgetPro(deptBonusPercentage);
                //年初奖金预算-金额
                deptAnnualBonusCanGrantDTO.setBeYearCanGrantBonusBudgetAmount(beYearCanGrantBonusBudgetAmount);
                //所有二级工资项目为奖金且级别为部门级的三级工资项目
                List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemByBonus();
                if (StringUtils.isNotEmpty(salaryItemDTOS)) {
                    for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                        //奖金金额
                        BigDecimal bonusAmount = new BigDecimal("0");
                        //部门可发工资项目年终奖
                        DeptAnnualBonusItemDTO deptAnnualBonusItemDTO = new DeptAnnualBonusItemDTO();
                        //工资项名称
                        deptAnnualBonusItemDTO.setSalaryItemName(salaryItemDTO.getThirdLevelItem());
                        //工资项ID
                        deptAnnualBonusItemDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
                        //部门ID
                        deptAnnualBonusItemDTO.setDepartmentId(datum.getDepartmentId());
                        //查询一级部门及子级部门
                        R<List<DepartmentDTO>> sublevelDepartmentList = remoteDepartmentService.selectSublevelDepartment(datum.getDepartmentId(), SecurityConstants.INNER);
                        List<DepartmentDTO> sublevelDepartmentListData = sublevelDepartmentList.getData();
                        if (StringUtils.isNotEmpty(sublevelDepartmentListData)) {
                            List<Long> departmentIdAll = sublevelDepartmentListData.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
                            if (StringUtils.isNotEmpty(departmentIdAll)) {
                                //对应一级部门以及其下属部门作为预算部门的奖金发放申请单据中的奖项总金额和奖金比例
                                List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS = bonusPayBudgetDeptMapper.selectBonusPayBudgetDeptByBonusAnnual(departmentIdAll);
                                //根据部门id分组
                                Map<Long, List<BonusPayBudgetDeptDTO>> bonusPayMap = bonusPayBudgetDeptDTOS.parallelStream().collect(Collectors.groupingBy(BonusPayBudgetDeptDTO::getDepartmentId));
                                for (Long key : bonusPayMap.keySet()) {
                                    List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS1 = bonusPayMap.get(key);
                                    for (BonusPayBudgetDeptDTO bonusPayBudgetDeptDTO : bonusPayBudgetDeptDTOS1) {
                                        if (salaryItemDTO.getSalaryItemId().equals(bonusPayBudgetDeptDTO.getSalaryItemId())) {
                                            //奖项总金额
                                            BigDecimal awardTotalAmount = bonusPayBudgetDeptDTO.getAwardTotalAmount();
                                            //奖金比例
                                            BigDecimal bonusPercentage = bonusPayBudgetDeptDTO.getBonusPercentage();
                                            if (awardTotalAmount.compareTo(new BigDecimal("0")) != 0 && bonusPercentage.compareTo(new BigDecimal("0")) != 0) {
                                                bonusAmount = bonusAmount.add(awardTotalAmount.multiply(bonusPercentage.divide(new BigDecimal("100"))).divide(new BigDecimal("10000")));
                                            }
                                        }
                                    }
                                }


                            }

                        }
                        //奖金金额
                        deptAnnualBonusItemDTO.setBonusAmount(bonusAmount);
                        deptAnnualBonusItemDTOS.add(deptAnnualBonusItemDTO);
                    }
                }
                deptAnnualBonusCanGrantDTO.setDeptAnnualBonusItemDTOS(deptAnnualBonusItemDTOS);
                deptAnnualBonusCanGrantDTOs.add(deptAnnualBonusCanGrantDTO);
            }
        }
        if (StringUtils.isNotEmpty(deptAnnualBonusCanGrantDTOs)) {
            for (int i = 0; i < deptAnnualBonusCanGrantDTOs.size(); i++) {
                if (StringUtils.isNotEmpty(deptAnnualBonusFactorDTOs)) {
                    //奖金包占比终值（%)
                    deptAnnualBonusCanGrantDTOs.get(i).setBeYearCanGrantManagePro(deptAnnualBonusFactorDTOs.get(i).getBonusPercentage());
                }
            }
            for (DeptAnnualBonusCanGrantDTO deptAnnualBonusCanGrantDTO : deptAnnualBonusCanGrantDTOs) {
                BigDecimal beYearCanGrantManageAmount = new BigDecimal("0");
                //可发经营奖总包-奖金占比
                BigDecimal beYearCanGrantManagePro = deptAnnualBonusCanGrantDTO.getBeYearCanGrantManagePro();
                if (departmentAnnualBonus.compareTo(new BigDecimal("0")) != 0 && beYearCanGrantManagePro.compareTo(new BigDecimal("0")) != 0) {
                    beYearCanGrantManageAmount = departmentAnnualBonus.multiply(beYearCanGrantManagePro.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                deptAnnualBonusCanGrantDTO.setBeYearCanGrantManageAmount(beYearCanGrantManageAmount);
            }
            for (int i = 0; i < deptAnnualBonusCanGrantDTOs.size(); i++) {
                //可分配年终奖
                BigDecimal distributeBonus = new BigDecimal("0");

                //已发放年终奖
                BigDecimal bonusAmountSum = new BigDecimal("0");
                List<DeptAnnualBonusItemDTO> deptAnnualBonusItemDTOS = deptAnnualBonusCanGrantDTOs.get(i).getDeptAnnualBonusItemDTOS();
                if (StringUtils.isNotEmpty(deptAnnualBonusItemDTOS)) {
                    bonusAmountSum = bonusAmountSum.add(deptAnnualBonusItemDTOS.stream().map(DeptAnnualBonusItemDTO::getBonusAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                }
                //可发经营奖总包-金额
                BigDecimal beYearCanGrantManageAmount = deptAnnualBonusCanGrantDTOs.get(i).getBeYearCanGrantManageAmount().divide(new BigDecimal("10000"));

                    distributeBonus = beYearCanGrantManageAmount.subtract(bonusAmountSum);

                if (null == deptAnnualBonusFactorDTOs.get(i).getDistributeBonus()) {
                    deptAnnualBonusCanGrantDTOs.get(i).setDistributeBonus(distributeBonus);
                } else {
                    deptAnnualBonusCanGrantDTOs.get(i).setDistributeBonus(deptAnnualBonusFactorDTOs.get(i).getDistributeBonus());
                }
                deptAnnualBonusCanGrantDTOs.get(i).setDistributeBonusReference(distributeBonus);
            }

        }


    }

    /**
     * 查询详情数据封装部门可发年终奖
     *
     * @param annualBonusYear
     * @param deptAnnualBonusCanGrantDTOs
     * @param deptAnnualBonusFactorDTOs
     * @param departmentAnnualBonus
     */
    private void packQueryDeptAnnualBonusCanGrantDTO(int annualBonusYear, List<DeptAnnualBonusCanGrantDTO> deptAnnualBonusCanGrantDTOs, List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOs, BigDecimal departmentAnnualBonus) {
        //总奖金包预算
        BigDecimal amountBonusBudget = new BigDecimal("0");

        BonusBudgetDTO bonusBudgetDTO = bonusBudgetMapper.selectBonusBudgetByBudgetYear(annualBonusYear);
        if (StringUtils.isNotNull(bonusBudgetDTO)) {
            amountBonusBudget = bonusBudgetDTO.getAmountBonusBudget();
        }


        //部门奖金预算 根据部门id分组
        Map<Long, List<DeptBonusBudgetDetailsDTO>> deptMap = new HashMap<>();
        //远程调用查看所有一级部门
        R<List<DepartmentDTO>> listR = remoteDepartmentService.getParentAll(SecurityConstants.INNER);
        List<DepartmentDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)) {
            List<Long> collect = data.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //部门奖金预算
                List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOList = deptBonusBudgetDetailsMapper.selectDeptBonusBudgetBybudgetYearAnnua(annualBonusYear, collect);
                //根据部门id分组
                deptMap = deptBonusBudgetDetailsDTOList.parallelStream().collect(Collectors.groupingBy(DeptBonusBudgetDetailsDTO::getDepartmentId));

            }
            for (DepartmentDTO datum : data) {
                for (DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO : deptAnnualBonusFactorDTOs) {
                    if (datum.getDepartmentId().equals(deptAnnualBonusFactorDTO.getDepartmentId())) {
                        //部门奖金包发放奖金项目
                        List<DeptAnnualBonusItemDTO> deptAnnualBonusItemDTOS = new ArrayList<>();
                        //部门奖金占比
                        BigDecimal deptBonusPercentage = new BigDecimal("0");
                        //年初奖金预算-金额
                        BigDecimal beYearCanGrantBonusBudgetAmount = new BigDecimal("0");
                        DeptAnnualBonusCanGrantDTO deptAnnualBonusCanGrantDTO = new DeptAnnualBonusCanGrantDTO();
                        deptAnnualBonusCanGrantDTO.setDepartmentId(datum.getDepartmentId());
                        deptAnnualBonusCanGrantDTO.setDepartmentName(datum.getDepartmentName());

                        if (StringUtils.isNotEmpty(deptMap)) {
                            List<DeptBonusBudgetDetailsDTO> deptBonusBudgetDetailsDTOList = deptMap.get(datum.getDepartmentId());
                            if (StringUtils.isNotEmpty(deptBonusBudgetDetailsDTOList)) {
                                for (DeptBonusBudgetDetailsDTO deptBonusBudgetDetailsDTO : deptBonusBudgetDetailsDTOList) {
                                    //部门总奖金包
                                    BigDecimal deptAmountBonus = new BigDecimal("0");
                                    //部门奖金占比
                                    deptBonusPercentage = deptBonusBudgetDetailsDTO.getDeptBonusPercentage();
                                    //战略奖比例
                                    BigDecimal strategyAwardPercentage = deptBonusBudgetDetailsDTO.getStrategyAwardPercentage();

                                    if (amountBonusBudget.compareTo(new BigDecimal("0")) != 0 && strategyAwardPercentage.compareTo(new BigDecimal("0")) != 0) {
                                        deptAmountBonus = amountBonusBudget.subtract(amountBonusBudget.multiply(strategyAwardPercentage.divide(new BigDecimal("100")))).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    }
                                    if (deptAmountBonus.compareTo(new BigDecimal("0")) != 0 && deptBonusPercentage.compareTo(new BigDecimal("0")) != 0) {
                                        beYearCanGrantBonusBudgetAmount = deptAmountBonus.multiply(deptBonusPercentage.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                                    }
                                }

                            }
                        }
                        //年初奖金预算-奖金占比
                        deptAnnualBonusCanGrantDTO.setBeYearCanGrantBonusBudgetPro(deptBonusPercentage);
                        //年初奖金预算-金额
                        deptAnnualBonusCanGrantDTO.setBeYearCanGrantBonusBudgetAmount(beYearCanGrantBonusBudgetAmount);
                        //所有二级工资项目为奖金且级别为部门级的三级工资项目
                        List<SalaryItemDTO> salaryItemDTOS = salaryItemMapper.selectSalaryItemByBonus();
                        if (StringUtils.isNotEmpty(salaryItemDTOS)) {
                            for (SalaryItemDTO salaryItemDTO : salaryItemDTOS) {
                                //奖金金额
                                BigDecimal bonusAmount = new BigDecimal("0");
                                //部门可发工资项目年终奖
                                DeptAnnualBonusItemDTO deptAnnualBonusItemDTO = new DeptAnnualBonusItemDTO();
                                //工资项名称
                                deptAnnualBonusItemDTO.setSalaryItemName(salaryItemDTO.getThirdLevelItem());
                                //工资项ID
                                deptAnnualBonusItemDTO.setSalaryItemId(salaryItemDTO.getSalaryItemId());
                                //部门ID
                                deptAnnualBonusItemDTO.setDepartmentId(datum.getDepartmentId());
                                //查询一级部门及子级部门
                                R<List<DepartmentDTO>> sublevelDepartmentList = remoteDepartmentService.selectSublevelDepartment(datum.getDepartmentId(), SecurityConstants.INNER);
                                List<DepartmentDTO> sublevelDepartmentListData = sublevelDepartmentList.getData();
                                if (StringUtils.isNotEmpty(sublevelDepartmentListData)) {
                                    List<Long> departmentIdAll = sublevelDepartmentListData.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
                                    if (StringUtils.isNotEmpty(departmentIdAll)) {
                                        //对应一级部门以及其下属部门作为预算部门的奖金发放申请单据中的奖项总金额和奖金比例
                                        List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS = bonusPayBudgetDeptMapper.selectBonusPayBudgetDeptByBonusAnnual(departmentIdAll);
                                        //根据部门id分组
                                        Map<Long, List<BonusPayBudgetDeptDTO>> bonusPayMap = bonusPayBudgetDeptDTOS.parallelStream().collect(Collectors.groupingBy(BonusPayBudgetDeptDTO::getDepartmentId));
                                        for (Long key : bonusPayMap.keySet()) {
                                            List<BonusPayBudgetDeptDTO> bonusPayBudgetDeptDTOS1 = bonusPayMap.get(key);
                                            for (BonusPayBudgetDeptDTO bonusPayBudgetDeptDTO : bonusPayBudgetDeptDTOS1) {
                                                if (salaryItemDTO.getSalaryItemId().equals(bonusPayBudgetDeptDTO.getSalaryItemId())) {
                                                    //奖项总金额
                                                    BigDecimal awardTotalAmount = bonusPayBudgetDeptDTO.getAwardTotalAmount();
                                                    //奖金比例
                                                    BigDecimal bonusPercentage = bonusPayBudgetDeptDTO.getBonusPercentage();
                                                    if (awardTotalAmount.compareTo(new BigDecimal("0")) != 0 && bonusPercentage.compareTo(new BigDecimal("0")) != 0) {
                                                        bonusAmount = bonusAmount.add(awardTotalAmount.multiply(bonusPercentage.divide(new BigDecimal("100"))).divide(new BigDecimal("10000")));
                                                    }
                                                }
                                            }
                                        }


                                    }

                                }
                                //奖金金额
                                deptAnnualBonusItemDTO.setBonusAmount(bonusAmount);
                                deptAnnualBonusItemDTOS.add(deptAnnualBonusItemDTO);
                            }
                        }
                        deptAnnualBonusCanGrantDTO.setDeptAnnualBonusItemDTOS(deptAnnualBonusItemDTOS);
                        deptAnnualBonusCanGrantDTOs.add(deptAnnualBonusCanGrantDTO);
                    }
                }

            }
        }
        if (StringUtils.isNotEmpty(deptAnnualBonusCanGrantDTOs)) {
            for (int i = 0; i < deptAnnualBonusCanGrantDTOs.size(); i++) {
                if (StringUtils.isNotEmpty(deptAnnualBonusFactorDTOs)) {
                    //奖金包占比终值（%)
                    deptAnnualBonusCanGrantDTOs.get(i).setBeYearCanGrantManagePro(deptAnnualBonusFactorDTOs.get(i).getBonusPercentage());
                }
            }
            for (DeptAnnualBonusCanGrantDTO deptAnnualBonusCanGrantDTO : deptAnnualBonusCanGrantDTOs) {
                BigDecimal beYearCanGrantManageAmount = new BigDecimal("0");
                //可发经营奖总包-奖金占比
                BigDecimal beYearCanGrantManagePro = deptAnnualBonusCanGrantDTO.getBeYearCanGrantManagePro();
                if (departmentAnnualBonus.compareTo(new BigDecimal("0")) != 0 && beYearCanGrantManagePro.compareTo(new BigDecimal("0")) != 0) {
                    beYearCanGrantManageAmount = departmentAnnualBonus.multiply(beYearCanGrantManagePro.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                deptAnnualBonusCanGrantDTO.setBeYearCanGrantManageAmount(beYearCanGrantManageAmount);
            }
            for (int i = 0; i < deptAnnualBonusCanGrantDTOs.size(); i++) {
                //可分配年终奖
                BigDecimal distributeBonus = new BigDecimal("0");

                //已发放年终奖
                BigDecimal bonusAmountSum = new BigDecimal("0");
                List<DeptAnnualBonusItemDTO> deptAnnualBonusItemDTOS = deptAnnualBonusCanGrantDTOs.get(i).getDeptAnnualBonusItemDTOS();
                if (StringUtils.isNotEmpty(deptAnnualBonusItemDTOS)) {
                    bonusAmountSum = bonusAmountSum.add(deptAnnualBonusItemDTOS.stream().map(DeptAnnualBonusItemDTO::getBonusAmount).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add));
                }
                //可发经营奖总包-金额
                BigDecimal beYearCanGrantManageAmount = deptAnnualBonusCanGrantDTOs.get(i).getBeYearCanGrantManageAmount();
                if (null != beYearCanGrantManageAmount) {
                    distributeBonus = beYearCanGrantManageAmount.subtract(bonusAmountSum);
                }
                if (null == deptAnnualBonusFactorDTOs.get(i).getDistributeBonus()) {
                    deptAnnualBonusCanGrantDTOs.get(i).setDistributeBonus(distributeBonus);
                } else {
                    deptAnnualBonusCanGrantDTOs.get(i).setDistributeBonus(deptAnnualBonusFactorDTOs.get(i).getDistributeBonus());
                }
                deptAnnualBonusCanGrantDTOs.get(i).setDistributeBonusReference(distributeBonus);
            }

        }


    }

    /**
     * 封装1经营绩效结果
     *
     * @param deptAnnualBonusOperateDTOs
     * @param annualBonusYear
     */
    private void packDeptAnnualBonusOperate(List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOs, int annualBonusYear) {
        //查询总奖金预算赋值经营绩效结果
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOList = bonusBudgetMapper.selectDeptAnnualBonusOperate(annualBonusYear);
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOList)) {
            List<Long> collect = deptAnnualBonusOperateDTOList.stream().filter(f -> f.getIndicatorId() != null).map(DeptAnnualBonusOperateDTO::getIndicatorId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                //远程获取指标名称
                R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(collect, SecurityConstants.INNER);
                List<IndicatorDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO : deptAnnualBonusOperateDTOList) {
                        for (IndicatorDTO datum : data) {
                            if (datum.getIndicatorId().equals(deptAnnualBonusOperateDTO.getIndicatorId())) {
                                deptAnnualBonusOperateDTO.setIndicatorName(datum.getIndicatorName());
                            }
                        }
                    }
                }
            }

            for (DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO : deptAnnualBonusOperateDTOList) {
                //目标超额完成率（%） 公式=（实际值÷目标值）-1。
                BigDecimal targetExcessPerComp = new BigDecimal("0");
                //奖金系数（实际）公式=权重×目标超额完成率。
                BigDecimal actualPerformanceBonusFactor = new BigDecimal("0");
                //目标值
                BigDecimal targetValue = deptAnnualBonusOperateDTO.getTargetValue();
                //实际值
                BigDecimal actualValue = deptAnnualBonusOperateDTO.getActualValue();
                //奖金权重(%)
                BigDecimal bonusWeight = deptAnnualBonusOperateDTO.getBonusWeight();
                if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) != 0 &&
                        null != actualValue && actualValue.compareTo(new BigDecimal("0")) != 0) {
                    targetExcessPerComp = actualValue.divide(targetValue, 10, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                if (null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) != 0 &&
                        targetExcessPerComp.compareTo(new BigDecimal("0")) != 0) {
                    actualPerformanceBonusFactor = bonusWeight.divide(new BigDecimal("100")).multiply(targetExcessPerComp.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                deptAnnualBonusOperateDTO.setTargetExcessPerComp(targetExcessPerComp);
                deptAnnualBonusOperateDTO.setActualPerformanceBonusFactor(actualPerformanceBonusFactor);
            }
        }
        deptAnnualBonusOperateDTOs.addAll(deptAnnualBonusOperateDTOList);
    }

    /**
     * 封装部门年终奖系数表数据
     *
     * @param annualBonusYear
     * @param deptAnnualBonusFactorDTOs
     * @param deptPaymentBonusSum
     * @param weight
     */
    private void packDeptAnnualBonusFactor(int annualBonusYear, List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOs, BigDecimal deptPaymentBonusSum, BigDecimal weight) {
        this.packDeptData(annualBonusYear, deptAnnualBonusFactorDTOs, deptPaymentBonusSum, weight);


    }

    /**
     * 封装部门年终奖系数表部门数据
     *
     * @param annualBonusYear
     * @param deptAnnualBonusFactorDTOs
     * @param deptPaymentBonusSum
     * @param weight
     */
    private void packDeptData(int annualBonusYear, List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOs, BigDecimal deptPaymentBonusSum, BigDecimal weight) {
        //远程调用查看所有在职员工
        R<List<EmployeeDTO>> employeeAll = remoteEmployeeService.getAll(SecurityConstants.INNER);
        List<EmployeeDTO> data2 = employeeAll.getData();
        if (StringUtils.isNotEmpty(data2)) {
            for (EmployeeDTO employeeDTO : data2) {
                //部门奖金预算 某职级的平均薪酬：从月度工资管理取数，取数范围为倒推12个月的数据（年工资）
                List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectDeptAnnualBonusBudgetPay(employeeDTO.getEmployeeId(), annualBonusYear,SecurityUtils.getTenantId());
                if (StringUtils.isNotEmpty(salaryPayDTOS)) {
                    //sterm流求和 总薪酬包 公式= 工资+津贴+福利+奖金
                    BigDecimal paymentBonus = salaryPayDTOS.stream().map(SalaryPayDTO::getPaymentBonus).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                    deptPaymentBonusSum = deptPaymentBonusSum.add(paymentBonus);
                }
            }

        }
        if (StringUtils.isEmpty(deptAnnualBonusFactorDTOs)) {
            //远程调用查看所有一级部门
            R<List<DepartmentDTO>> listR = remoteDepartmentService.getParentAll(SecurityConstants.INNER);
            List<DepartmentDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (DepartmentDTO datum : data) {
                    DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO = new DeptAnnualBonusFactorDTO();
                    deptAnnualBonusFactorDTO.setDepartmentId(datum.getDepartmentId());
                    deptAnnualBonusFactorDTO.setDepartmentName(datum.getDepartmentName());
                    deptAnnualBonusFactorDTO.setImportanceFactor(datum.getDepartmentImportanceFactor());

                    //远程查询一级部门及子级部门
                    R<List<DepartmentDTO>> sublevelDepartment = remoteDepartmentService.selectSublevelDepartment(datum.getDepartmentId(), SecurityConstants.INNER);
                    List<DepartmentDTO> sublevelDepartmentData = sublevelDepartment.getData();
                    if (StringUtils.isNotEmpty(sublevelDepartmentData)) {
                        //一级部门及子级下所有的员工的总薪酬包
                        BigDecimal deptPaymentBonus = new BigDecimal("0");
                        List<Long> departmentIdAll = sublevelDepartmentData.stream().map(DepartmentDTO::getDepartmentId).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(departmentIdAll)) {
                            //远程查询一级部门及子级部门下 所有人员
                            R<List<EmployeeDTO>> listR1 = remoteEmployeeService.selectParentDepartmentIdAndOfficialRankSystem(departmentIdAll, SecurityConstants.INNER);
                            List<EmployeeDTO> data1 = listR1.getData();
                            if (StringUtils.isNotEmpty(data1)) {
                                for (EmployeeDTO employeeDTO : data1) {
                                    //部门奖金预算 某职级的平均薪酬：从月度工资管理取数，取数范围为倒推12个月的数据（年工资）
                                    List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectDeptAnnualBonusBudgetPay(employeeDTO.getEmployeeId(), annualBonusYear,SecurityUtils.getTenantId());
                                    if (StringUtils.isNotEmpty(salaryPayDTOS)) {
                                        //sterm流求和 总薪酬包 公式= 工资+津贴+福利+奖金
                                        BigDecimal reduce = salaryPayDTOS.stream().map(SalaryPayDTO::getPaymentBonus).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
                                        deptPaymentBonus = deptPaymentBonus.add(reduce);
                                    }
                                }

                            }
                        }
                        if (deptPaymentBonus.compareTo(new BigDecimal("0")) > 0 && deptPaymentBonusSum.compareTo(new BigDecimal("0")) > 0) {
                            weight = deptPaymentBonus.divide(deptPaymentBonusSum, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                        }

                    }
                    //权重
                    deptAnnualBonusFactorDTO.setWeight(weight);

                    //封装组织绩效
                    this.packDeptPerformanceRank(datum, deptAnnualBonusFactorDTO);

                    deptAnnualBonusFactorDTOs.add(deptAnnualBonusFactorDTO);
                    //每次循环赋0
                    weight = new BigDecimal("0");
                }
            }
        }

        if (StringUtils.isNotEmpty(deptAnnualBonusFactorDTOs)) {
            for (DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO : deptAnnualBonusFactorDTOs) {
                //奖金综合系数    公式=组织权重×组织绩效奖金系数×组织重要性系数
                BigDecimal syntheticalBonusFactor = new BigDecimal("0");
                //权重
                BigDecimal weight1 = deptAnnualBonusFactorDTO.getWeight();
                //组织绩效奖金系数
                BigDecimal performanceBonusFactor = deptAnnualBonusFactorDTO.getPerformanceBonusFactor();
                //组织重要性系数
                BigDecimal importanceFactor = deptAnnualBonusFactorDTO.getImportanceFactor();
                if (null != weight1 && weight1.compareTo(new BigDecimal("0")) > 0 &&
                        null != performanceBonusFactor && performanceBonusFactor.compareTo(new BigDecimal("0")) > 0 &&
                        null != importanceFactor && importanceFactor.compareTo(new BigDecimal("0")) > 0) {
                    syntheticalBonusFactor = weight1.multiply(performanceBonusFactor).multiply(importanceFactor).divide(new BigDecimal("100"));
                }
                deptAnnualBonusFactorDTO.setSyntheticalBonusFactor(syntheticalBonusFactor);
            }
            BigDecimal syntheticalBonusFactorSum = deptAnnualBonusFactorDTOs.stream().map(DeptAnnualBonusFactorDTO::getSyntheticalBonusFactor).filter(Objects::nonNull).reduce(BigDecimal.ZERO, BigDecimal::add);
            for (DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO : deptAnnualBonusFactorDTOs) {
                //奖金综合系数
                BigDecimal syntheticalBonusFactor = deptAnnualBonusFactorDTO.getSyntheticalBonusFactor();
                //部门奖金包占比参考值 该部门奖金综合系数÷奖金综合系数合计
                BigDecimal deptBonusPercentageReference = new BigDecimal("0");
                if (syntheticalBonusFactorSum.compareTo(new BigDecimal("0")) > 0 &&
                        null != syntheticalBonusFactor && syntheticalBonusFactor.compareTo(new BigDecimal("0")) > 0) {
                    deptBonusPercentageReference = syntheticalBonusFactor.divide(syntheticalBonusFactorSum, 10, BigDecimal.ROUND_HALF_UP).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
                }
                //部门奖金包占比参考值
                deptAnnualBonusFactorDTO.setDeptBonusPercentageReference(deptBonusPercentageReference);
                if (null == deptAnnualBonusFactorDTO.getBonusPercentage()) {
                    //奖金包占比终值（%)
                    deptAnnualBonusFactorDTO.setBonusPercentage(deptBonusPercentageReference);
                }
                //封装详情组织绩效
                this.packDeptPerformanceRankDetails(deptAnnualBonusFactorDTO.getDepartmentId(), deptAnnualBonusFactorDTO);
            }

        }
    }

    /**
     * 封装详情组织绩效
     *
     * @param departmentId
     * @param deptAnnualBonusFactorDTO
     */
    private void packDeptPerformanceRankDetails(Long departmentId, DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO) {
        //绩效
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByDeptId(departmentId);
        if (StringUtils.isNotEmpty(performanceRankFactorDTOS)) {
            PerformanceRankFactorDTO performanceRankFactorDTO = performanceRankFactorDTOS.get(0);
            Long performanceRankId = performanceRankFactorDTO.getPerformanceRankId();
            //绩效等级下拉框集合
            if (null != deptAnnualBonusFactorDTO.getLastPerformanceResulted()) {
                Map<String, BigDecimal> map = new HashMap<>();
                List<PerformanceRankFactorDTO> performanceRankFactorDTOS1 = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
                if (StringUtils.isNotEmpty(performanceRankFactorDTOS1)) {
                    deptAnnualBonusFactorDTO.setPerformanceRanks(performanceRankFactorDTOS1.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).collect(Collectors.toList()));
                }
                for (PerformanceRankFactorDTO rankFactorDTO : performanceRankFactorDTOS1) {
                    map.put(rankFactorDTO.getPerformanceRankName(), rankFactorDTO.getBonusFactor());
                }
                deptAnnualBonusFactorDTO.setPerformanceRankMap(map);
            }

        }
    }

    /**
     * 封装组织绩效
     *
     * @param datum
     * @param deptAnnualBonusFactorDTO
     */
    private void packDeptPerformanceRank(DepartmentDTO datum, DeptAnnualBonusFactorDTO deptAnnualBonusFactorDTO) {
        //绩效
        List<PerformanceRankFactorDTO> performanceRankFactorDTOS = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByDeptId(datum.getDepartmentId());
        if (StringUtils.isNotEmpty(performanceRankFactorDTOS)) {
            PerformanceRankFactorDTO performanceRankFactorDTO = performanceRankFactorDTOS.get(0);
            Long performanceRankId = performanceRankFactorDTO.getPerformanceRankId();
            //绩效名称
            deptAnnualBonusFactorDTO.setPerformanceRank(performanceRankFactorDTO.getPerformanceRankName());
            //绩效等级ID
            deptAnnualBonusFactorDTO.setPerformanceRankId(performanceRankId);
            //绩效等级下拉框集合
            if (null != performanceRankId) {
                Map<String, BigDecimal> map = new HashMap<>();
                List<PerformanceRankFactorDTO> performanceRankFactorDTOS1 = performanceAppraisalObjectsMapper.selectPerformanceRankFactorByPerformanceRankId(performanceRankId);
                if (StringUtils.isNotEmpty(performanceRankFactorDTOS1)) {
                    deptAnnualBonusFactorDTO.setPerformanceRanks(performanceRankFactorDTOS1.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).collect(Collectors.toList()));
                }
                for (PerformanceRankFactorDTO rankFactorDTO : performanceRankFactorDTOS1) {
                    map.put(rankFactorDTO.getPerformanceRankName(), rankFactorDTO.getBonusFactor());
                }
                deptAnnualBonusFactorDTO.setPerformanceRankMap(map);
            }
            //绩效等级系数ID
            deptAnnualBonusFactorDTO.setPerformanceRankFactorId(performanceRankFactorDTO.getPerformanceRankFactorId());

            //绩效奖金系数
            deptAnnualBonusFactorDTO.setPerformanceBonusFactor(performanceRankFactorDTO.getBonusFactor());
            //最近绩效结果
            deptAnnualBonusFactorDTO.setLastPerformanceResulted(StringUtils.join(performanceRankFactorDTOS.stream().map(PerformanceRankFactorDTO::getPerformanceRankName).filter(StringUtils::isNotBlank).collect(Collectors.toList()),","));
        }
    }

    /**
     * 封装部门年终奖表实体类数据
     *
     * @param annualBonusYear
     * @param allActualPerformanceBonusFactorSum
     * @param deptAnnualBonusDTO
     * @param bonusBudgetDTO
     */
    private void packDeptAnnualBonus(int annualBonusYear, BigDecimal allActualPerformanceBonusFactorSum, DeptAnnualBonusDTO deptAnnualBonusDTO, BonusBudgetDTO bonusBudgetDTO) {
        //总奖金包预算
        BigDecimal amountBonusBudgetReferenceValueOne = bonusBudgetDTO.getAmountBonusBudgetReferenceValueOne();

        //年初总奖金包预算（不考虑目标完成率） 旧：总奖金包预算
        BigDecimal beYearAmountBonusBudget = new BigDecimal("0");
        //年底应发总奖金包（根据实际业绩测算) 旧：总奖金包实际
        BigDecimal endYearSalaryAmountBonus = new BigDecimal("0");
        //可发经营奖总包 旧：部门年终奖总包
        BigDecimal departmentAnnualBonus = new BigDecimal("0");

        //赋值
        if (null != amountBonusBudgetReferenceValueOne) {
            beYearAmountBonusBudget = amountBonusBudgetReferenceValueOne;
            endYearSalaryAmountBonus = beYearAmountBonusBudget.multiply(new BigDecimal("1").add(allActualPerformanceBonusFactorSum));
        }
        //年初总奖金包预算（不考虑目标完成率) 旧：总奖金包预算 从总奖金包预算生成取总奖金包预算参考值1
        deptAnnualBonusDTO.setBeYearAmountBonusBudget(beYearAmountBonusBudget);
        //年底应发总奖金包（根据实际业绩测算) 总奖金包预算×（1+∑各驱动因素的奖金系数）
        deptAnnualBonusDTO.setEndYearSalaryAmountBonus(endYearSalaryAmountBonus);
        if (null == deptAnnualBonusDTO.getCompanyAnnualBonus()) {
            //最终可发总奖金包 旧：公司年终奖总包 默认等于总奖金包实际
            deptAnnualBonusDTO.setCompanyAnnualBonus(endYearSalaryAmountBonus);
        }
        //查询战略奖的工资项id
        SalaryItemDTO salaryItemDTO = salaryItemMapper.selectSalaryItemByAward();
        if (StringUtils.isNull(salaryItemDTO)) {
            throw new ServiceException("工资项战略奖未配置 请联系管理员！");
        }

        BigDecimal strategyDeveAward = bonusPayApplicationMapper.selectBonusPayApplicationAddDeptAnnual(annualBonusYear, salaryItemDTO.getSalaryItemId());
        if (strategyDeveAward.compareTo(new BigDecimal("0")) !=0 ){
            strategyDeveAward=strategyDeveAward.divide(new BigDecimal("10000")).setScale(10, BigDecimal.ROUND_HALF_UP);

        }
        //战略奖实发 公式 取相同年度下，奖项类别为战略奖的所有奖金发放申请单中，奖金总金额的合计
        deptAnnualBonusDTO.setStrategyDeveAward(strategyDeveAward);
        if (null != strategyDeveAward) {
            if (null == deptAnnualBonusDTO.getCompanyAnnualBonus()) {
                departmentAnnualBonus = endYearSalaryAmountBonus.subtract(strategyDeveAward);
            }else {
                departmentAnnualBonus = deptAnnualBonusDTO.getCompanyAnnualBonus().subtract(strategyDeveAward);
            }

        }
        //可发经营奖总包 旧：部门年终奖总包 公式=公司年终奖总包-战略奖实发。
        deptAnnualBonusDTO.setDepartmentAnnualBonus(departmentAnnualBonus);


    }

    /**
     * 封装部门年终奖经营绩效结果集合
     *
     * @param deptAnnualBonusOperateDTOList
     * @param bonusBudgetParametersDTOS
     * @param deptAnnualBonusDTO
     * @param allActualPerformanceBonusFactorSum
     */
    private void packDeptAnnualBonusOperates(List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOList, List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS, DeptAnnualBonusDTO deptAnnualBonusDTO, BigDecimal allActualPerformanceBonusFactorSum) {
        //年初可发总奖金包预算
        deptAnnualBonusDTO.setBeYearDeveAmountBonus(bonusBudgetParametersDTOS.get(0).getAmountBonusBudget());
        //指标id集合
        List<Long> collect = bonusBudgetParametersDTOS.stream().map(BonusBudgetParametersDTO::getIndicatorId).filter(Objects::nonNull).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(collect, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)) {
                for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                    for (IndicatorDTO datum : data) {
                        if (bonusBudgetParametersDTO.getIndicatorId().equals(datum.getIndicatorId())) {
                            bonusBudgetParametersDTO.setIndicatorName(datum.getIndicatorName());
                        }
                    }

                }
            }
        }
        //赋值部门年终奖经营绩效结果表集合
        for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
            DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO = new DeptAnnualBonusOperateDTO();
            //目标超额完成率（%） 公式=（实际值÷目标值）-1
            BigDecimal targetExcessPerComp = new BigDecimal("0");
            //奖金系数（实际）公式=权重×目标超额完成率
            BigDecimal actualPerformanceBonusFactor = new BigDecimal("0");
            //目标值
            BigDecimal targetValue = bonusBudgetParametersDTO.getTargetValue();
            //(部门年终奖用)实际值
            BigDecimal actualValue = bonusBudgetParametersDTO.getActualValue();
            //奖金权重(%)
            BigDecimal bonusWeight = bonusBudgetParametersDTO.getBonusWeight();

            BeanUtils.copyProperties(bonusBudgetParametersDTO, deptAnnualBonusOperateDTO);
            if (null != targetValue && targetValue.compareTo(new BigDecimal("0")) > 0 &&
                    null != actualValue && actualValue.compareTo(new BigDecimal("0")) > 0) {
                targetExcessPerComp = actualValue.divide(targetValue, 10, BigDecimal.ROUND_HALF_UP).subtract(new BigDecimal("1")).multiply(new BigDecimal("100")).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            if (null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0")) > 0 &&
                    targetExcessPerComp.compareTo(new BigDecimal("0")) > 0) {
                actualPerformanceBonusFactor = bonusWeight.divide(new BigDecimal("100")).multiply(targetExcessPerComp.divide(new BigDecimal("100"))).setScale(2, BigDecimal.ROUND_HALF_UP);
            }
            //目标超额完成率（%） 公式=（实际值÷目标值）-1
            deptAnnualBonusOperateDTO.setTargetExcessPerComp(targetExcessPerComp);
            //奖金系数（实际）公式=权重×目标超额完成率
            deptAnnualBonusOperateDTO.setActualPerformanceBonusFactor(actualPerformanceBonusFactor);
            deptAnnualBonusOperateDTOList.add(deptAnnualBonusOperateDTO);
        }
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOList)) {
            for (DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO : deptAnnualBonusOperateDTOList) {
                //奖金系数（实际）
                BigDecimal actualPerformanceBonusFactor = deptAnnualBonusOperateDTO.getActualPerformanceBonusFactor();
                if (null != actualPerformanceBonusFactor && actualPerformanceBonusFactor.compareTo(new BigDecimal("0")) != 0) {
                    allActualPerformanceBonusFactorSum = allActualPerformanceBonusFactorSum.add(actualPerformanceBonusFactor);
                }
            }
        }


    }

    /**
     * 逻辑删除部门年终奖表信息
     *
     * @param deptAnnualBonusDTO 部门年终奖表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteDeptAnnualBonusByDeptAnnualBonusId(DeptAnnualBonusDTO deptAnnualBonusDTO) {
        int i = 0;
        DeptAnnualBonusDTO deptAnnualBonusDTO1 = deptAnnualBonusMapper.selectDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusDTO.getDeptAnnualBonusId());
        if (StringUtils.isNull(deptAnnualBonusDTO1)) {
            throw new ServiceException("数据不存在 请刷新重试！");
        }
        DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
        deptAnnualBonus.setDeptAnnualBonusId(deptAnnualBonusDTO.getDeptAnnualBonusId());
        deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
        try {
            i = deptAnnualBonusMapper.logicDeleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonus);
        } catch (Exception e) {
            throw new ServiceException("删除部门年终奖失败");
        }
        List<DeptAnnualBonusFactorDTO> deptAnnualBonusFactorDTOS = deptAnnualBonusFactorMapper.selectDeptAnnualBonusFactorByDeptAnnualBonusId(deptAnnualBonusDTO.getDeptAnnualBonusId());
        if (StringUtils.isNotEmpty(deptAnnualBonusFactorDTOS)) {
            List<Long> collect = deptAnnualBonusFactorDTOS.stream().map(DeptAnnualBonusFactorDTO::getDeptAnnualBonusFactorId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    deptAnnualBonusFactorMapper.logicDeleteDeptAnnualBonusFactorByDeptAnnualBonusFactorIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除部门年终奖系数失败");
                }
            }
        }
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOList = deptAnnualBonusOperateMapper.selectDeptAnnualBonusOperateByDeptAnnualBonusId(deptAnnualBonusDTO.getDeptAnnualBonusId());
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOList)) {
            List<Long> collect = deptAnnualBonusOperateDTOList.stream().map(DeptAnnualBonusOperateDTO::getDeptAnnualBonusOperateId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)) {
                try {
                    deptAnnualBonusOperateMapper.logicDeleteDeptAnnualBonusOperateByDeptAnnualBonusOperateIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除部门年终奖经营绩效结果表失败");
                }
            }
        }
        return i;
    }

    /**
     * 物理删除部门年终奖表信息
     *
     * @param deptAnnualBonusDTO 部门年终奖表
     * @return 结果
     */

    @Override
    public int deleteDeptAnnualBonusByDeptAnnualBonusId(DeptAnnualBonusDTO deptAnnualBonusDTO) {
        DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
        BeanUtils.copyProperties(deptAnnualBonusDTO, deptAnnualBonus);
        return deptAnnualBonusMapper.deleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonus.getDeptAnnualBonusId());
    }

    /**
     * 物理批量删除部门年终奖表
     *
     * @param deptAnnualBonusDtos 需要删除的部门年终奖表主键
     * @return 结果
     */

    @Override
    public int deleteDeptAnnualBonusByDeptAnnualBonusIds(List<DeptAnnualBonusDTO> deptAnnualBonusDtos) {
        List<Long> stringList = new ArrayList();
        for (DeptAnnualBonusDTO deptAnnualBonusDTO : deptAnnualBonusDtos) {
            stringList.add(deptAnnualBonusDTO.getDeptAnnualBonusId());
        }
        return deptAnnualBonusMapper.deleteDeptAnnualBonusByDeptAnnualBonusIds(stringList);
    }

    /**
     * 批量新增部门年终奖表信息
     *
     * @param deptAnnualBonusDtos 部门年终奖表对象
     */

    @Override
    public int insertDeptAnnualBonuss(List<DeptAnnualBonusDTO> deptAnnualBonusDtos) {
        List<DeptAnnualBonus> deptAnnualBonusList = new ArrayList();

        for (DeptAnnualBonusDTO deptAnnualBonusDTO : deptAnnualBonusDtos) {
            DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
            BeanUtils.copyProperties(deptAnnualBonusDTO, deptAnnualBonus);
            deptAnnualBonus.setCreateBy(SecurityUtils.getUserId());
            deptAnnualBonus.setCreateTime(DateUtils.getNowDate());
            deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
            deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
            deptAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            deptAnnualBonusList.add(deptAnnualBonus);
        }
        return deptAnnualBonusMapper.batchDeptAnnualBonus(deptAnnualBonusList);
    }

    /**
     * 批量修改部门年终奖表信息
     *
     * @param deptAnnualBonusDtos 部门年终奖表对象
     */

    @Override
    public int updateDeptAnnualBonuss(List<DeptAnnualBonusDTO> deptAnnualBonusDtos) {
        List<DeptAnnualBonus> deptAnnualBonusList = new ArrayList();

        for (DeptAnnualBonusDTO deptAnnualBonusDTO : deptAnnualBonusDtos) {
            DeptAnnualBonus deptAnnualBonus = new DeptAnnualBonus();
            BeanUtils.copyProperties(deptAnnualBonusDTO, deptAnnualBonus);
            deptAnnualBonus.setCreateBy(SecurityUtils.getUserId());
            deptAnnualBonus.setCreateTime(DateUtils.getNowDate());
            deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
            deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
            deptAnnualBonusList.add(deptAnnualBonus);
        }
        return deptAnnualBonusMapper.updateDeptAnnualBonuss(deptAnnualBonusList);
    }
}

