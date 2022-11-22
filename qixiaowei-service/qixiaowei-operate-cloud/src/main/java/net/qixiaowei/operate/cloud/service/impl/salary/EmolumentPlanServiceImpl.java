package net.qixiaowei.operate.cloud.service.impl.salary;

import com.sun.org.apache.bcel.internal.generic.IF_ACMPEQ;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmolumentPlan;
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmolumentPlanMapper;
import net.qixiaowei.operate.cloud.service.salary.IEmolumentPlanService;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;


/**
 * EmolumentPlanService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-11-18
 */
@Service
public class EmolumentPlanServiceImpl implements IEmolumentPlanService {
    @Autowired
    private EmolumentPlanMapper emolumentPlanMapper;

    @Autowired
    private RemoteIndicatorService remoteIndicatorService;
    @Autowired
    private RemoteUserService remoteUserService;

    /**
     * 查询薪酬规划表
     *
     * @param emolumentPlanId 薪酬规划表主键
     * @return 薪酬规划表
     */
    @Override
    public EmolumentPlanDTO selectEmolumentPlanByEmolumentPlanId(Long emolumentPlanId) {
        EmolumentPlanDTO emolumentPlanDTO = emolumentPlanMapper.selectEmolumentPlanByEmolumentPlanId(emolumentPlanId);
        this.queryCalculate(emolumentPlanDTO);

        return emolumentPlanMapper.selectEmolumentPlanByEmolumentPlanId(emolumentPlanId);
    }

    /**
     * 查询详情计算方法
     * @param emolumentPlanDTO
     */
    private void queryCalculate(EmolumentPlanDTO emolumentPlanDTO) {
        if (StringUtils.isNotNull(emolumentPlanDTO)){
            //预算年销售收入
            BigDecimal revenue = emolumentPlanDTO.getRevenue();
            //预算年后一年销售收入
            BigDecimal revenueAfterOne = emolumentPlanDTO.getRevenueAfterOne();
            //预算年后二年销售收入
            BigDecimal revenueAfterTwo = emolumentPlanDTO.getRevenueAfterTwo();
            //预算年前一年E/R值(%)
            BigDecimal erBeforeOne = emolumentPlanDTO.getErBeforeOne();
            //预算年E/R值改进率(%)
            BigDecimal emolumentRevenueImprove = emolumentPlanDTO.getEmolumentRevenueImprove();
            // 预算年后一年E/R值改进率(%)
            BigDecimal erImproveAfterOne = emolumentPlanDTO.getErImproveAfterOne();
            //预算年后二年E/R值改进率(%)
            BigDecimal erImproveAfterTwo = emolumentPlanDTO.getErImproveAfterTwo();


            if (null !=erBeforeOne && erBeforeOne.compareTo(new BigDecimal("0"))!=0 && null !=emolumentRevenueImprove && emolumentRevenueImprove.compareTo(new BigDecimal("0"))!=0){
                //er值 公式=上年E/R值×（1-本年E/R值改进率）
                BigDecimal subtract = new BigDecimal("1").subtract(emolumentRevenueImprove);
                //er值
                BigDecimal multiply = erBeforeOne.multiply(subtract);
                emolumentPlanDTO.setEr(multiply);
                if (null != erImproveAfterOne && erImproveAfterOne.compareTo(new BigDecimal("0"))!= 0){
                    //er值
                    BigDecimal er = emolumentPlanDTO.getEr();
                    BigDecimal subtract1 = new BigDecimal("1").subtract(erImproveAfterOne);
                    //预算年后一年E/R值(%)
                    BigDecimal multiply1 = er.multiply(subtract1);
                    emolumentPlanDTO.setErAfterOne(multiply1);
                    if (null != erImproveAfterTwo && erImproveAfterTwo.compareTo(new BigDecimal("0"))!= 0){
                        //预算年后一年E/R值(%)
                        BigDecimal erAfterOne = emolumentPlanDTO.getErAfterOne();
                        BigDecimal subtract2 = new BigDecimal("1").subtract(erImproveAfterTwo);
                        //预算年后二年E/R值(%)
                        BigDecimal multiply2 = erAfterOne.multiply(subtract2);
                        emolumentPlanDTO.setErAfterTwo(multiply2);
                    }
                }

            }

            //总薪酬包 未来年度：公式=销售收入×E/R值
            //预算年E/R值(%)
            BigDecimal er = emolumentPlanDTO.getEr();
            //预算年后一年E/R值(%)
            BigDecimal erAfterOne = emolumentPlanDTO.getErAfterOne();
            //预算年后二年E/R值(%)
            BigDecimal erAfterTwo = emolumentPlanDTO.getErAfterTwo();
            if (null !=revenue && revenue.compareTo(new BigDecimal("0"))!=0 && null !=er && er.compareTo(new BigDecimal("0"))!=0){
                //预算年总薪酬包
                BigDecimal multiply = revenue.multiply(er);
                emolumentPlanDTO.setEmolumentPackage(multiply);
            }
            if (null !=revenueAfterOne && revenueAfterOne.compareTo(new BigDecimal("0"))!=0 && null !=erAfterOne && erAfterOne.compareTo(new BigDecimal("0"))!=0){
                //预算年后一年总薪酬包
                BigDecimal multiply1 = revenueAfterOne.multiply(erAfterOne);
                emolumentPlanDTO.setEmolumentPackageAfterOne(multiply1);
            }
            if (null !=revenueAfterTwo && revenueAfterTwo.compareTo(new BigDecimal("0"))!=0 && null !=erAfterTwo && erAfterTwo.compareTo(new BigDecimal("0"))!=0){
                //预算年后二年总薪酬包
                BigDecimal multiply2 = revenueAfterTwo.multiply(erAfterTwo);
                emolumentPlanDTO.setEmolumentPackageAfterTwo(multiply2);
            }
        }
    }

    /**
     * 查询薪酬规划表列表
     *
     * @param emolumentPlanDTO 薪酬规划表
     * @return 薪酬规划表
     */
    @Override
    public List<EmolumentPlanDTO> selectEmolumentPlanList(EmolumentPlanDTO emolumentPlanDTO) {
        EmolumentPlan emolumentPlan = new EmolumentPlan();
        BeanUtils.copyProperties(emolumentPlanDTO, emolumentPlan);
        List<EmolumentPlanDTO> emolumentPlanDTOS = emolumentPlanMapper.selectEmolumentPlanList(emolumentPlan);
        //远程查询创建人姓名
        Set<Long> collect = emolumentPlanDTOS.stream().map(EmolumentPlanDTO::getCreateBy).collect(Collectors.toSet());
        if (StringUtils.isNotEmpty(collect)){
            R<List<UserDTO>> usersByUserIds = remoteUserService.getUsersByUserIds(collect, SecurityConstants.INNER);
            List<UserDTO> data = usersByUserIds.getData();
            if (StringUtils.isNotEmpty(data)){
                for (EmolumentPlanDTO planDTO : emolumentPlanDTOS) {
                    for (UserDTO datum : data) {
                        if (planDTO.getCreateBy() == datum.getUserId()){
                            //员工姓名
                            planDTO.setCreateName(datum.getEmployeeName());
                        }
                    }
                }
            }
        }
        this.queryCalculateList(emolumentPlanDTOS);
        return emolumentPlanDTOS;
    }

    /**
     * 薪酬规划列表list计算
     * @param emolumentPlanDTOS
     */
    private void queryCalculateList(List<EmolumentPlanDTO> emolumentPlanDTOS) {

        for (EmolumentPlanDTO emolumentPlanDTO : emolumentPlanDTOS) {
            //预算年销售收入
            BigDecimal revenue = emolumentPlanDTO.getRevenue();
            //预算年前一年E/R值(%)
            BigDecimal erBeforeOne = emolumentPlanDTO.getErBeforeOne();
            //预算年E/R值改进率(%)
            BigDecimal emolumentRevenueImprove = emolumentPlanDTO.getEmolumentRevenueImprove();

            if (null !=erBeforeOne && erBeforeOne.compareTo(new BigDecimal("0"))!=0 && null !=emolumentRevenueImprove && emolumentRevenueImprove.compareTo(new BigDecimal("0"))!=0){
                //er值 公式=上年E/R值×（1-本年E/R值改进率）
                BigDecimal subtract = new BigDecimal("1").subtract(emolumentRevenueImprove);
                //er值
                BigDecimal multiply = erBeforeOne.multiply(subtract);
                emolumentPlanDTO.setEr(multiply);
            }
            //总薪酬包 未来年度：公式=销售收入×E/R值
            //预算年E/R值(%)
            BigDecimal er = emolumentPlanDTO.getEr();
            if (null !=revenue && revenue.compareTo(new BigDecimal("0"))!=0 && null !=er && er.compareTo(new BigDecimal("0"))!=0){
                //预算年总薪酬包
                BigDecimal multiply = revenue.multiply(er);
                emolumentPlanDTO.setEmolumentPackage(multiply);
            }
            //E/R值实际改进率（%）：公式=（上年E/R值÷本年E/R值-1）*100%
            if (null !=erBeforeOne && erBeforeOne.compareTo(new BigDecimal("0"))!=0 && null !=er && er.compareTo(new BigDecimal("0"))!=0){
                BigDecimal subtract = erBeforeOne.divide(er, BigDecimal.ROUND_CEILING).subtract(new BigDecimal("1"));
                //E/R值实际改进率（%）
                BigDecimal multiply = subtract.multiply(new BigDecimal("100"));
                emolumentPlanDTO.setEmolumentPracticalRevenueImprove(multiply);
            }
        }
    }

    /**
     * 新增薪酬规划表
     *
     * @param emolumentPlanDTO 薪酬规划表
     * @return 结果
     */
    @Override
    public EmolumentPlanDTO insertEmolumentPlan(EmolumentPlanDTO emolumentPlanDTO) {
        EmolumentPlan emolumentPlan = new EmolumentPlan();
        BeanUtils.copyProperties(emolumentPlanDTO, emolumentPlan);
        EmolumentPlanDTO emolumentPlanDTO1 = emolumentPlanMapper.selectEmolumentPlanByPlanYear(emolumentPlan.getPlanYear());
        if (StringUtils.isNotNull(emolumentPlanDTO1)) {
            throw new ServiceException("该预算年度已操作薪酬规划计划！！！");
        }
        emolumentPlan.setCreateBy(SecurityUtils.getUserId());
        emolumentPlan.setCreateTime(DateUtils.getNowDate());
        emolumentPlan.setUpdateTime(DateUtils.getNowDate());
        emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
        emolumentPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        emolumentPlanMapper.insertEmolumentPlan(emolumentPlan);
        emolumentPlanDTO.setEmolumentPlanId(emolumentPlan.getEmolumentPlanId());
        return emolumentPlanDTO;
    }

    /**
     * 修改薪酬规划表
     *
     * @param emolumentPlanDTO 薪酬规划表
     * @return 结果
     */
    @Override
    public int updateEmolumentPlan(EmolumentPlanDTO emolumentPlanDTO) {
        EmolumentPlan emolumentPlan = new EmolumentPlan();
        BeanUtils.copyProperties(emolumentPlanDTO, emolumentPlan);
        EmolumentPlanDTO emolumentPlanDTO1 = emolumentPlanMapper.selectEmolumentPlanByEmolumentPlanId(emolumentPlan.getEmolumentPlanId());
        if (StringUtils.isNull(emolumentPlanDTO1)){
            throw new ServiceException("数据不存在！！！");
        }
        emolumentPlan.setUpdateTime(DateUtils.getNowDate());
        emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
        return emolumentPlanMapper.updateEmolumentPlan(emolumentPlan);
    }

    /**
     * 逻辑批量删除薪酬规划表
     *
     * @param emolumentPlanIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteEmolumentPlanByEmolumentPlanIds(List<Long> emolumentPlanIds) {
        List<EmolumentPlanDTO> emolumentPlanDTOS = emolumentPlanMapper.selectEmolumentPlanByEmolumentPlanIds(emolumentPlanIds);
        if (StringUtils.isNotEmpty(emolumentPlanDTOS)){
            throw new ServiceException("数据不存在！！！");
        }
        return emolumentPlanMapper.logicDeleteEmolumentPlanByEmolumentPlanIds(emolumentPlanIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除薪酬规划表信息
     *
     * @param emolumentPlanId 薪酬规划表主键
     * @return 结果
     */
    @Override
    public int deleteEmolumentPlanByEmolumentPlanId(Long emolumentPlanId) {
        EmolumentPlanDTO emolumentPlanDTO1 = emolumentPlanMapper.selectEmolumentPlanByEmolumentPlanId(emolumentPlanId);
        if (StringUtils.isNull(emolumentPlanDTO1)){
            throw new ServiceException("数据不存在！！！");
        }
        return emolumentPlanMapper.deleteEmolumentPlanByEmolumentPlanId(emolumentPlanId);
    }

    /**
     * 新增薪酬规划时预制数据
     *
     * @param planYear
     * @return
     */
    @Override
    public EmolumentPlanDTO prefabricateAddEmolumentPlan(int planYear) {
        //远程查询指标id
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.ORDER.getCode(), SecurityConstants.INNER);
        IndicatorDTO data = indicatorDTOR.getData();
        if (StringUtils.isNull(data)) {
            throw new ServiceException("指标数据为空");
        }

        EmolumentPlan emolumentPlan = new EmolumentPlan();
        emolumentPlan.setPlanYear(planYear);
        emolumentPlan.setIndicatorId(data.getIndicatorId());

        EmolumentPlanDTO emolumentPlanDTO = emolumentPlanMapper.prefabricateAddEmolumentPlan(emolumentPlan);
        this.addCalculate(emolumentPlanDTO);
        return emolumentPlanDTO;
    }

    /**
     * 新增薪酬规划时预制数据计算
     * @param emolumentPlanDTO
     */
    private void addCalculate(EmolumentPlanDTO emolumentPlanDTO) {
        if (StringUtils.isNotNull(emolumentPlanDTO)){
            //预算年前一年销售收入
            BigDecimal revenueBeforeOne = emolumentPlanDTO.getRevenueBeforeOne();
            //预算年前一年总薪酬包
            BigDecimal emolumentPackageBeforeOne = emolumentPlanDTO.getEmolumentPackageBeforeOne();
            if (null != revenueBeforeOne && revenueBeforeOne.compareTo(new BigDecimal("0")) != 0 && null != emolumentPackageBeforeOne && emolumentPackageBeforeOne.compareTo(new BigDecimal("0")) != 0){
                BigDecimal er = emolumentPackageBeforeOne.divide(revenueBeforeOne, BigDecimal.ROUND_CEILING);
                if (er.compareTo(new BigDecimal("0")) > 0){
                    emolumentPlanDTO.setEr(er);
                }
            }
        }

    }

    /**
     * 逻辑删除薪酬规划表信息
     *
     * @param emolumentPlanDTO 薪酬规划表
     * @return 结果
     */
    @Override
    public int logicDeleteEmolumentPlanByEmolumentPlanId(EmolumentPlanDTO emolumentPlanDTO) {
        EmolumentPlan emolumentPlan = new EmolumentPlan();
        emolumentPlan.setEmolumentPlanId(emolumentPlanDTO.getEmolumentPlanId());
        emolumentPlan.setUpdateTime(DateUtils.getNowDate());
        emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
        return emolumentPlanMapper.logicDeleteEmolumentPlanByEmolumentPlanId(emolumentPlan);
    }

    /**
     * 物理删除薪酬规划表信息
     *
     * @param emolumentPlanDTO 薪酬规划表
     * @return 结果
     */

    @Override
    public int deleteEmolumentPlanByEmolumentPlanId(EmolumentPlanDTO emolumentPlanDTO) {
        EmolumentPlan emolumentPlan = new EmolumentPlan();
        BeanUtils.copyProperties(emolumentPlanDTO, emolumentPlan);
        return emolumentPlanMapper.deleteEmolumentPlanByEmolumentPlanId(emolumentPlan.getEmolumentPlanId());
    }

    /**
     * 物理批量删除薪酬规划表
     *
     * @param emolumentPlanDtos 需要删除的薪酬规划表主键
     * @return 结果
     */

    @Override
    public int deleteEmolumentPlanByEmolumentPlanIds(List<EmolumentPlanDTO> emolumentPlanDtos) {
        List<Long> stringList = new ArrayList();
        for (EmolumentPlanDTO emolumentPlanDTO : emolumentPlanDtos) {
            stringList.add(emolumentPlanDTO.getEmolumentPlanId());
        }
        return emolumentPlanMapper.deleteEmolumentPlanByEmolumentPlanIds(stringList);
    }

    /**
     * 批量新增薪酬规划表信息
     *
     * @param emolumentPlanDtos 薪酬规划表对象
     */

    public int insertEmolumentPlans(List<EmolumentPlanDTO> emolumentPlanDtos) {
        List<EmolumentPlan> emolumentPlanList = new ArrayList();

        for (EmolumentPlanDTO emolumentPlanDTO : emolumentPlanDtos) {
            EmolumentPlan emolumentPlan = new EmolumentPlan();
            BeanUtils.copyProperties(emolumentPlanDTO, emolumentPlan);
            emolumentPlan.setCreateBy(SecurityUtils.getUserId());
            emolumentPlan.setCreateTime(DateUtils.getNowDate());
            emolumentPlan.setUpdateTime(DateUtils.getNowDate());
            emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
            emolumentPlan.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            emolumentPlanList.add(emolumentPlan);
        }
        return emolumentPlanMapper.batchEmolumentPlan(emolumentPlanList);
    }

    /**
     * 批量修改薪酬规划表信息
     *
     * @param emolumentPlanDtos 薪酬规划表对象
     */

    public int updateEmolumentPlans(List<EmolumentPlanDTO> emolumentPlanDtos) {
        List<EmolumentPlan> emolumentPlanList = new ArrayList();

        for (EmolumentPlanDTO emolumentPlanDTO : emolumentPlanDtos) {
            EmolumentPlan emolumentPlan = new EmolumentPlan();
            BeanUtils.copyProperties(emolumentPlanDTO, emolumentPlan);
            emolumentPlan.setCreateBy(SecurityUtils.getUserId());
            emolumentPlan.setCreateTime(DateUtils.getNowDate());
            emolumentPlan.setUpdateTime(DateUtils.getNowDate());
            emolumentPlan.setUpdateBy(SecurityUtils.getUserId());
            emolumentPlanList.add(emolumentPlan);
        }
        return emolumentPlanMapper.updateEmolumentPlans(emolumentPlanList);
    }
}

