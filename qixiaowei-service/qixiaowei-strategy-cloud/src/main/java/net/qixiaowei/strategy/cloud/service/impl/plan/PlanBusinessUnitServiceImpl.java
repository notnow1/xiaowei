package net.qixiaowei.strategy.cloud.service.impl.plan;

import com.alibaba.nacos.shaded.com.google.common.collect.ImmutableMap;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.plan.PlanBusinessUnit;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.service.plan.IPlanBusinessUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;


/**
 * PlanBusinessUnitService业务层处理
 *
 * @author wangchen
 * @since 2023-02-17
 */
@Service
public class PlanBusinessUnitServiceImpl implements IPlanBusinessUnitService {
    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    public static Map<String, String> BUSINESS_UNIT_DECOMPOSE_MAP = ImmutableMap.<String, String>of(
            "region", "区域",
            "department", "部门",
            "product", "产品",
            "industry", "行业",
            "company", "公司级"
    );

    /**
     * 查询规划业务单元
     *
     * @param planBusinessUnitId 规划业务单元主键
     * @return 规划业务单元
     */
    @Override
    public PlanBusinessUnitDTO selectPlanBusinessUnitByPlanBusinessUnitId(Long planBusinessUnitId) {
        if (StringUtils.isNull(planBusinessUnitId)) {
            throw new ServiceException("请传入规划业务单元ID");
        }
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO)) {
            throw new ServiceException("当前规划业务单元已不存在");
        }
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        if (StringUtils.isNotNull(businessUnitDecompose)) {
            StringBuilder businessUnitDecomposeName = new StringBuilder("");
            List<String> businessUnitDecomposeList = Arrays.asList(businessUnitDecompose.split(";"));
            businessUnitDecomposeList.forEach(decompose -> {
                if (BUSINESS_UNIT_DECOMPOSE_MAP.containsKey(decompose)) {
                    businessUnitDecomposeName.append(BUSINESS_UNIT_DECOMPOSE_MAP.get(decompose)).append(";");
                }
            });
            planBusinessUnitDTO.setBusinessUnitDecomposeName(businessUnitDecomposeName.substring(0, businessUnitDecomposeName.length() - 1));
        }
        return planBusinessUnitDTO;
    }

    /**
     * 查询规划业务单元列表
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 规划业务单元
     */
    @Override
    public List<PlanBusinessUnitDTO> selectPlanBusinessUnitList(PlanBusinessUnitDTO planBusinessUnitDTO) {
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
        Map<String, Object> params = planBusinessUnitDTO.getParams();
        planBusinessUnit.setParams(params);
        List<PlanBusinessUnitDTO> planBusinessUnitDTOS = planBusinessUnitMapper.selectPlanBusinessUnitList(planBusinessUnit);
        for (PlanBusinessUnitDTO businessUnitDTO : planBusinessUnitDTOS) {
            String businessUnitDecompose = businessUnitDTO.getBusinessUnitDecompose();
            if (StringUtils.isNotNull(businessUnitDecompose)) {
                StringBuilder businessUnitDecomposeName = new StringBuilder("");
                List<String> businessUnitDecomposeList = Arrays.asList(businessUnitDecompose.split(";"));
                businessUnitDecomposeList.forEach(decompose -> {
                    if (BUSINESS_UNIT_DECOMPOSE_MAP.containsKey(decompose)) {
                        businessUnitDecomposeName.append(BUSINESS_UNIT_DECOMPOSE_MAP.get(decompose)).append(";");
                    }
                });
                businessUnitDTO.setBusinessUnitDecomposeName(businessUnitDecomposeName.substring(0, businessUnitDecomposeName.length() - 1));
                List<String> businessUnitDecomposes = Arrays.asList(businessUnitDecompose.split(","));
                businessUnitDTO.setBusinessUnitDecomposes(businessUnitDecomposes);
            }
        }
        return planBusinessUnitDTOS;
    }

    /**
     * 新增规划业务单元
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */
    @Override
    public PlanBusinessUnitDTO insertPlanBusinessUnit(PlanBusinessUnitDTO planBusinessUnitDTO) {
        String businessUnitCode = planBusinessUnitDTO.getBusinessUnitCode();
        String businessUnitName = planBusinessUnitDTO.getBusinessUnitName();
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        Integer status = planBusinessUnitDTO.getStatus();
        if (StringUtils.isNull(status)) {
            planBusinessUnitDTO.setStatus(1);
        }
        if (StringUtils.isNull(businessUnitCode) || StringUtils.isNull(businessUnitName)) {
            throw new ServiceException("请输入规划业务单元编码与名称");
        }
        //规划业务单元维度(region,department,product,industry,company)
        if (StringUtils.isNull(businessUnitDecompose)) {
            throw new ServiceException("规划业务单元维度不能为空");
        }
        if (businessUnitDecompose.contains("company") && !businessUnitDecompose.equals("company")) {
            throw new ServiceException("选择公司级后，不可以选择其他维度");
        }
        List<String> businessUnitDecomposeList = Arrays.asList(businessUnitDecompose.split(";"));
        businessUnitDecomposeList.forEach(business -> {
            if (!BUSINESS_UNIT_DECOMPOSE_MAP.containsKey(business))
                throw new ServiceException("规划业务单元维度标识不匹配 请检查");
        });
        // 名称重复校验
        PlanBusinessUnit planBusinessUnitRepeat = new PlanBusinessUnit();
        planBusinessUnitRepeat.setBusinessUnitName(businessUnitName);
        List<PlanBusinessUnitDTO> planBusinessUnitDTOS = planBusinessUnitMapper.selectPlanBusinessUnitRepeat(planBusinessUnitRepeat);
        if (StringUtils.isNotEmpty(planBusinessUnitDTOS)) {
            throw new ServiceException("规划业务单元名称重复 请重新输入");
        }
        // 编码重复校验
        planBusinessUnitRepeat = new PlanBusinessUnit();
        planBusinessUnitRepeat.setBusinessUnitCode(businessUnitCode);
        planBusinessUnitDTOS = planBusinessUnitMapper.selectPlanBusinessUnitRepeat(planBusinessUnitRepeat);
        if (StringUtils.isNotEmpty(planBusinessUnitDTOS)) {
            throw new ServiceException("规划业务单元编码重复 请重新输入");
        }
        // 规划业务单元维度重复校验
        planBusinessUnitRepeat = new PlanBusinessUnit();
        planBusinessUnitRepeat.setBusinessUnitDecompose(businessUnitDecompose);
        planBusinessUnitDTOS = planBusinessUnitMapper.selectPlanBusinessUnitRepeat(planBusinessUnitRepeat);
        if (StringUtils.isNotEmpty(planBusinessUnitDTOS)) {
            throw new ServiceException("规划业务单元维度重复 请重新输入");
        }
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
        planBusinessUnit.setCreateBy(SecurityUtils.getUserId());
        planBusinessUnit.setCreateTime(DateUtils.getNowDate());
        planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
        planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
        planBusinessUnit.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        planBusinessUnitMapper.insertPlanBusinessUnit(planBusinessUnit);
        planBusinessUnitDTO.setPlanBusinessUnitId(planBusinessUnit.getPlanBusinessUnitId());
        return planBusinessUnitDTO;
    }

    /**
     * 修改规划业务单元
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */
    @Override
    public int updatePlanBusinessUnit(PlanBusinessUnitDTO planBusinessUnitDTO) {
        Long planBusinessUnitId = planBusinessUnitDTO.getPlanBusinessUnitId();
        String businessUnitName = planBusinessUnitDTO.getBusinessUnitName();
        Integer status = planBusinessUnitDTO.getStatus();
        if (StringUtils.isNull(planBusinessUnitId)) {
            throw new ServiceException("请传入规划业务ID");
        }
        if (StringUtils.isNull(businessUnitName)) {
            throw new ServiceException("请输入规划业务名称");
        }
        // 名称重复校验
        PlanBusinessUnit planBusinessUnitRepeat = new PlanBusinessUnit();
        planBusinessUnitRepeat.setBusinessUnitName(businessUnitName);
        List<PlanBusinessUnitDTO> planBusinessUnitDTOS = planBusinessUnitMapper.selectPlanBusinessUnitRepeat(planBusinessUnitRepeat);
        if (StringUtils.isNotEmpty(planBusinessUnitDTOS) && !planBusinessUnitDTOS.get(0).getPlanBusinessUnitId().equals(planBusinessUnitId)) {
            throw new ServiceException("规划业务单元名称重复 请重新输入");
        }
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        planBusinessUnit.setPlanBusinessUnitId(planBusinessUnitId);
        planBusinessUnit.setBusinessUnitName(businessUnitName);
        planBusinessUnit.setStatus(status);
        planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
        planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
        return planBusinessUnitMapper.updatePlanBusinessUnit(planBusinessUnit);
    }

    /**
     * 逻辑批量删除规划业务单元
     *
     * @param planBusinessUnitIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeletePlanBusinessUnitByPlanBusinessUnitIds(List<Long> planBusinessUnitIds) {
        if (StringUtils.isEmpty(planBusinessUnitIds)) {
            throw new ServiceException("请传入要删除的规划业务单元ID集合");
        }
        List<PlanBusinessUnitDTO> planBusinessUnitDTOS = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitIds(planBusinessUnitIds);
        if (planBusinessUnitDTOS.size() != planBusinessUnitIds.size()) {
            throw new ServiceException("部分规划业务单元的数据已不存在");
        }
        return planBusinessUnitMapper.logicDeletePlanBusinessUnitByPlanBusinessUnitIds(planBusinessUnitIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除规划业务单元信息
     *
     * @param planBusinessUnitId 规划业务单元主键
     * @return 结果
     */
    @Override
    public int deletePlanBusinessUnitByPlanBusinessUnitId(Long planBusinessUnitId) {
        return planBusinessUnitMapper.deletePlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
    }

    /**
     * 生成规划业务单元编码
     *
     * @return String
     */
    @Override
    public String generatePlanBusinessUnitCode() {
        String planBusinessUnitCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.PLAN_BUSINESS_UNIT.getCode();
        List<String> planBusinessUnitCodes = planBusinessUnitMapper.getPlanBusinessUnitCode(prefixCodeRule);
        if (StringUtils.isNotEmpty(planBusinessUnitCodes)) {
            for (String code : planBusinessUnitCodes) {
                if (StringUtils.isEmpty(code) || code.length() != 5) {
                    continue;
                }
                code = code.replaceFirst(prefixCodeRule, "");
                try {
                    int codeOfNumber = Integer.parseInt(code);
                    if (number != codeOfNumber) {
                        break;
                    }
                    number++;
                } catch (Exception ignored) {
                }
            }
        }
        if (number > 1000) {
            throw new ServiceException("流水号溢出，请联系管理员");
        }
        planBusinessUnitCode = "000" + number;
        planBusinessUnitCode = prefixCodeRule + planBusinessUnitCode.substring(planBusinessUnitCode.length() - 3);
        return planBusinessUnitCode;
    }

    /**
     * 逻辑删除规划业务单元信息
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */
    @Override
    public int logicDeletePlanBusinessUnitByPlanBusinessUnitId(PlanBusinessUnitDTO planBusinessUnitDTO) {
        Long planBusinessUnitId = planBusinessUnitDTO.getPlanBusinessUnitId();
        if (StringUtils.isNull(planBusinessUnitId)) {
            throw new ServiceException("请传入规划业务单元ID");
        }
        PlanBusinessUnitDTO planBusinessUnitDTO1 = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO1)) {
            throw new ServiceException("当前规划业务单元已不存在");
        }
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        planBusinessUnit.setPlanBusinessUnitId(planBusinessUnitDTO.getPlanBusinessUnitId());
        planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
        planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
        return planBusinessUnitMapper.logicDeletePlanBusinessUnitByPlanBusinessUnitId(planBusinessUnit);
    }

    /**
     * 物理删除规划业务单元信息
     *
     * @param planBusinessUnitDTO 规划业务单元
     * @return 结果
     */

    @Override
    public int deletePlanBusinessUnitByPlanBusinessUnitId(PlanBusinessUnitDTO planBusinessUnitDTO) {
        PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
        BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
        return planBusinessUnitMapper.deletePlanBusinessUnitByPlanBusinessUnitId(planBusinessUnit.getPlanBusinessUnitId());
    }

    /**
     * 物理批量删除规划业务单元
     *
     * @param planBusinessUnitDtos 需要删除的规划业务单元主键
     * @return 结果
     */

    @Override
    public int deletePlanBusinessUnitByPlanBusinessUnitIds(List<PlanBusinessUnitDTO> planBusinessUnitDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PlanBusinessUnitDTO planBusinessUnitDTO : planBusinessUnitDtos) {
            stringList.add(planBusinessUnitDTO.getPlanBusinessUnitId());
        }
        return planBusinessUnitMapper.deletePlanBusinessUnitByPlanBusinessUnitIds(stringList);
    }

    /**
     * 批量新增规划业务单元信息
     *
     * @param planBusinessUnitDtoS 规划业务单元对象
     */

    @Override
    public int insertPlanBusinessUnits(List<PlanBusinessUnitDTO> planBusinessUnitDtoS) {
        List<PlanBusinessUnit> planBusinessUnitList = new ArrayList<>();

        for (PlanBusinessUnitDTO planBusinessUnitDTO : planBusinessUnitDtoS) {
            PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
            BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
            planBusinessUnit.setCreateBy(SecurityUtils.getUserId());
            planBusinessUnit.setCreateTime(DateUtils.getNowDate());
            planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
            planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
            planBusinessUnit.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            planBusinessUnitList.add(planBusinessUnit);
        }
        return planBusinessUnitMapper.batchPlanBusinessUnit(planBusinessUnitList);
    }

    /**
     * 批量修改规划业务单元信息
     *
     * @param planBusinessUnitDtos 规划业务单元对象
     */

    @Override
    public int updatePlanBusinessUnits(List<PlanBusinessUnitDTO> planBusinessUnitDtos) {
        List<PlanBusinessUnit> planBusinessUnitList = new ArrayList<>();

        for (PlanBusinessUnitDTO planBusinessUnitDTO : planBusinessUnitDtos) {
            PlanBusinessUnit planBusinessUnit = new PlanBusinessUnit();
            BeanUtils.copyProperties(planBusinessUnitDTO, planBusinessUnit);
            planBusinessUnit.setCreateBy(SecurityUtils.getUserId());
            planBusinessUnit.setCreateTime(DateUtils.getNowDate());
            planBusinessUnit.setUpdateTime(DateUtils.getNowDate());
            planBusinessUnit.setUpdateBy(SecurityUtils.getUserId());
            planBusinessUnitList.add(planBusinessUnit);
        }
        return planBusinessUnitMapper.updatePlanBusinessUnits(planBusinessUnitList);
    }
}

