package net.qixiaowei.operate.cloud.service.impl.salary;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.DeptAnnualBonus;
import net.qixiaowei.operate.cloud.api.dto.salary.*;
import net.qixiaowei.operate.cloud.mapper.salary.BonusBudgetMapper;
import net.qixiaowei.operate.cloud.mapper.salary.BonusBudgetParametersMapper;
import net.qixiaowei.operate.cloud.mapper.salary.DeptAnnualBonusMapper;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.service.salary.IDeptAnnualBonusService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;


/**
* DeptAnnualBonusService业务层处理
* @author TANGMICHI
* @since 2022-12-06
*/
@Service
public class DeptAnnualBonusServiceImpl implements IDeptAnnualBonusService{
    @Autowired
    private DeptAnnualBonusMapper deptAnnualBonusMapper;
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

    /**
    * 查询部门年终奖表
    *
    * @param deptAnnualBonusId 部门年终奖表主键
    * @return 部门年终奖表
    */
    @Override
    public DeptAnnualBonusDTO selectDeptAnnualBonusByDeptAnnualBonusId(Long deptAnnualBonusId)
    {
    return deptAnnualBonusMapper.selectDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusId);
    }

    /**
    * 查询部门年终奖表列表
    *
    * @param deptAnnualBonusDTO 部门年终奖表
    * @return 部门年终奖表
    */
    @Override
    public List<DeptAnnualBonusDTO> selectDeptAnnualBonusList(DeptAnnualBonusDTO deptAnnualBonusDTO)
    {
    DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
    BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
    return deptAnnualBonusMapper.selectDeptAnnualBonusList(deptAnnualBonus);
    }

    /**
    * 新增部门年终奖表
    *
    * @param deptAnnualBonusDTO 部门年终奖表
    * @return 结果
    */
    @Override
    public DeptAnnualBonusDTO insertDeptAnnualBonus(DeptAnnualBonusDTO deptAnnualBonusDTO){
    DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
    BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
    deptAnnualBonus.setCreateBy(SecurityUtils.getUserId());
    deptAnnualBonus.setCreateTime(DateUtils.getNowDate());
    deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
    deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
    deptAnnualBonus.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    deptAnnualBonusMapper.insertDeptAnnualBonus(deptAnnualBonus);
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
    public int updateDeptAnnualBonus(DeptAnnualBonusDTO deptAnnualBonusDTO)
    {
    DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
    BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
    deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
    deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
    return deptAnnualBonusMapper.updateDeptAnnualBonus(deptAnnualBonus);
    }

    /**
    * 逻辑批量删除部门年终奖表
    *
    * @param deptAnnualBonusIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(List<Long> deptAnnualBonusIds){
    return deptAnnualBonusMapper.logicDeleteDeptAnnualBonusByDeptAnnualBonusIds(deptAnnualBonusIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除部门年终奖表信息
    *
    * @param deptAnnualBonusId 部门年终奖表主键
    * @return 结果
    */
    @Override
    public int deleteDeptAnnualBonusByDeptAnnualBonusId(Long deptAnnualBonusId)
    {
    return deptAnnualBonusMapper.deleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonusId);
    }

    /**
     * 部门年终奖预制数据
     * @param annualBonusYear
     * @return
     */
    @Override
    public DeptAnnualBonusDTO   addPrefabricate(int annualBonusYear) {
        //1+∑各驱动因素的奖金系数
        BigDecimal allActualPerformanceBonusFactorSum = new BigDecimal("0");
        //部门年终奖表
        DeptAnnualBonusDTO deptAnnualBonusDTO = new DeptAnnualBonusDTO();
        //奖金预算表
        BonusBudgetDTO bonusBudgetDTO = new BonusBudgetDTO();
        //部门年终奖经营绩效结果表集合
        List<DeptAnnualBonusOperateDTO> deptAnnualBonusOperateDTOList = new ArrayList<>();
        //总奖金预算
        List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS = bonusBudgetParametersMapper.selectBonusBudgetParametersByAnnualBonusYear(annualBonusYear);
        if (StringUtils.isNotEmpty(bonusBudgetParametersDTOS)){
            //封装部门年终奖经营绩效结果集合
            packDeptAnnualBonusOperates(deptAnnualBonusOperateDTOList, bonusBudgetParametersDTOS,deptAnnualBonusDTO,allActualPerformanceBonusFactorSum);

            //根据总奖金id查询奖金预算参数表
            List<BonusBudgetParametersDTO> bonusBudgetParametersDTOS1 = bonusBudgetParametersMapper.selectBonusBudgetParametersByBonusBudgetId(bonusBudgetParametersDTOS.get(0).getBonusBudgetId());
            //封装奖金预算参考值1
            BonusBudgetServiceImpl.packBounLadderNum(bonusBudgetDTO,bonusBudgetParametersDTOS1);
        }
        //2 可发经营奖总包
        packDeptAnnualBonus(allActualPerformanceBonusFactorSum, deptAnnualBonusDTO, bonusBudgetDTO);
        //远程调用查看所有一级部门
        R<List<DepartmentDTO>> listR = remoteDepartmentService.getAll(SecurityConstants.INNER);
        List<DepartmentDTO> data = listR.getData();
        if (StringUtils.isNotEmpty(data)){

        }
//        List<SalaryPayDTO> salaryPayDTOS = salaryPayMapper.selectDeptBonusBudgetPay(annualBonusYear);
        return null;
    }

    /**
     * 封装部门年终奖表实体类数据
     * @param allActualPerformanceBonusFactorSum
     * @param deptAnnualBonusDTO
     * @param bonusBudgetDTO
     */
    private void packDeptAnnualBonus(BigDecimal allActualPerformanceBonusFactorSum, DeptAnnualBonusDTO deptAnnualBonusDTO, BonusBudgetDTO bonusBudgetDTO) {
        //总奖金包预算
        BigDecimal amountBonusBudgetReferenceValueOne = bonusBudgetDTO.getAmountBonusBudgetReferenceValueOne();

        //年初总奖金包预算（不考虑目标完成率） 旧：总奖金包预算
        BigDecimal beYearAmountBonusBudget = new BigDecimal("0");
        //年底应发总奖金包（根据实际业绩测算) 旧：总奖金包实际
        BigDecimal endYearSalaryAmountBonus = new BigDecimal("0");
        //最终可发总奖金包 旧：公司年终奖总包
        BigDecimal companyAnnualBonus = new BigDecimal("0");

        //赋值
        if (null != amountBonusBudgetReferenceValueOne){
            beYearAmountBonusBudget=amountBonusBudgetReferenceValueOne;
            endYearSalaryAmountBonus= beYearAmountBonusBudget.multiply(new BigDecimal("1").add(allActualPerformanceBonusFactorSum));
        }
        //年初总奖金包预算（不考虑目标完成率) 旧：总奖金包预算 从总奖金包预算生成取总奖金包预算参考值1
        deptAnnualBonusDTO.setBeYearAmountBonusBudget(beYearAmountBonusBudget);
        //年底应发总奖金包（根据实际业绩测算) 总奖金包预算×（1+∑各驱动因素的奖金系数）
        deptAnnualBonusDTO.setEndYearSalaryAmountBonus(endYearSalaryAmountBonus);
        //最终可发总奖金包 旧：公司年终奖总包 默认等于总奖金包实际
        deptAnnualBonusDTO.setCompanyAnnualBonus(endYearSalaryAmountBonus);
        //战略奖实发
        deptAnnualBonusDTO.setStrategyDeveAward(endYearSalaryAmountBonus);
    }

    /**
     * 封装部门年终奖经营绩效结果集合
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
        if (StringUtils.isNotEmpty(collect)){
            R<List<IndicatorDTO>> listR = remoteIndicatorService.selectIndicatorByIds(collect, SecurityConstants.INNER);
            List<IndicatorDTO> data = listR.getData();
            if (StringUtils.isNotEmpty(data)){
                for (BonusBudgetParametersDTO bonusBudgetParametersDTO : bonusBudgetParametersDTOS) {
                    for (IndicatorDTO datum : data) {
                        if (bonusBudgetParametersDTO.getIndicatorId() == datum.getIndicatorId()){
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

            BeanUtils.copyProperties(bonusBudgetParametersDTO,deptAnnualBonusOperateDTO);
            if (null != targetValue && targetValue.compareTo(new BigDecimal("0"))>0&&
                    null != actualValue && actualValue.compareTo(new BigDecimal("0"))>0){
                targetExcessPerComp= actualValue.divide(targetValue,4,BigDecimal.ROUND_HALF_DOWN).subtract(new BigDecimal("1")).multiply(new BigDecimal("100"));
            }
            if (null != bonusWeight && bonusWeight.compareTo(new BigDecimal("0"))>0&&
                    targetExcessPerComp.compareTo(new BigDecimal("0"))>0){
                actualPerformanceBonusFactor= bonusWeight.divide(new BigDecimal("100")).multiply(targetExcessPerComp.divide(new BigDecimal("100")));
            }
            //目标超额完成率（%） 公式=（实际值÷目标值）-1
            deptAnnualBonusOperateDTO.setTargetExcessPerComp(targetExcessPerComp);
            //奖金系数（实际）公式=权重×目标超额完成率
            deptAnnualBonusOperateDTO.setActualPerformanceBonusFactor(actualPerformanceBonusFactor);
            deptAnnualBonusOperateDTOList.add(deptAnnualBonusOperateDTO);
        }
        if (StringUtils.isNotEmpty(deptAnnualBonusOperateDTOList)){
            for (DeptAnnualBonusOperateDTO deptAnnualBonusOperateDTO : deptAnnualBonusOperateDTOList) {
                //奖金系数（实际）
                BigDecimal actualPerformanceBonusFactor = deptAnnualBonusOperateDTO.getActualPerformanceBonusFactor();
                if (null != actualPerformanceBonusFactor && actualPerformanceBonusFactor.compareTo(new BigDecimal("0")) != 0){
                    allActualPerformanceBonusFactorSum=allActualPerformanceBonusFactorSum.add(actualPerformanceBonusFactor);
                }
            }
        }

    }

    /**
     * 逻辑删除部门年终奖表信息
     *
     * @param  deptAnnualBonusDTO 部门年终奖表
     * @return 结果
     */
     @Override
     public int logicDeleteDeptAnnualBonusByDeptAnnualBonusId(DeptAnnualBonusDTO deptAnnualBonusDTO)
     {
     DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
     deptAnnualBonus.setDeptAnnualBonusId(deptAnnualBonusDTO.getDeptAnnualBonusId());
     deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
     deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
     return deptAnnualBonusMapper.logicDeleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonus);
     }

     /**
     * 物理删除部门年终奖表信息
     *
     * @param  deptAnnualBonusDTO 部门年终奖表
     * @return 结果
     */
     
     @Override
     public int deleteDeptAnnualBonusByDeptAnnualBonusId(DeptAnnualBonusDTO deptAnnualBonusDTO)
     {
     DeptAnnualBonus deptAnnualBonus=new DeptAnnualBonus();
     BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
     return deptAnnualBonusMapper.deleteDeptAnnualBonusByDeptAnnualBonusId(deptAnnualBonus.getDeptAnnualBonusId());
     }
     /**
     * 物理批量删除部门年终奖表
     *
     * @param deptAnnualBonusDtos 需要删除的部门年终奖表主键
     * @return 结果
     */
     
     @Override
     public int deleteDeptAnnualBonusByDeptAnnualBonusIds(List<DeptAnnualBonusDTO> deptAnnualBonusDtos){
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
    
    public int insertDeptAnnualBonuss(List<DeptAnnualBonusDTO> deptAnnualBonusDtos){
      List<DeptAnnualBonus> deptAnnualBonusList = new ArrayList();

    for (DeptAnnualBonusDTO deptAnnualBonusDTO : deptAnnualBonusDtos) {
      DeptAnnualBonus deptAnnualBonus =new DeptAnnualBonus();
      BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
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
    
    public int updateDeptAnnualBonuss(List<DeptAnnualBonusDTO> deptAnnualBonusDtos){
     List<DeptAnnualBonus> deptAnnualBonusList = new ArrayList();

     for (DeptAnnualBonusDTO deptAnnualBonusDTO : deptAnnualBonusDtos) {
     DeptAnnualBonus deptAnnualBonus =new DeptAnnualBonus();
     BeanUtils.copyProperties(deptAnnualBonusDTO,deptAnnualBonus);
        deptAnnualBonus.setCreateBy(SecurityUtils.getUserId());
        deptAnnualBonus.setCreateTime(DateUtils.getNowDate());
        deptAnnualBonus.setUpdateTime(DateUtils.getNowDate());
        deptAnnualBonus.setUpdateBy(SecurityUtils.getUserId());
     deptAnnualBonusList.add(deptAnnualBonus);
     }
     return deptAnnualBonusMapper.updateDeptAnnualBonuss(deptAnnualBonusList);
    }
}

