package net.qixiaowei.operate.cloud.service.impl.salary;

import java.util.List;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.mapper.salary.SalaryPayMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeMapper;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.salary.EmolumentPlan;
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;
import net.qixiaowei.operate.cloud.mapper.salary.EmolumentPlanMapper;
import net.qixiaowei.operate.cloud.service.salary.IEmolumentPlanService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


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
        return emolumentPlanMapper.selectEmolumentPlanByEmolumentPlanId(emolumentPlanId);
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

        return emolumentPlanMapper.selectEmolumentPlanList(emolumentPlan);
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

        EmolumentPlanDTO emolumentPlanDTO = new EmolumentPlanDTO();
        emolumentPlanDTO.setPlanYear(planYear);
        emolumentPlanDTO.setIndicatorId(data.getIndicatorId());
        return emolumentPlanMapper.prefabricateAddEmolumentPlan(emolumentPlanDTO);
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

