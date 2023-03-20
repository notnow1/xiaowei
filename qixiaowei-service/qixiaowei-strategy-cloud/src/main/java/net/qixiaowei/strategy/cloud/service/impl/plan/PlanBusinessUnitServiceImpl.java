package net.qixiaowei.strategy.cloud.service.impl.plan;

import com.alibaba.nacos.shaded.com.google.common.collect.ImmutableMap;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.enums.strategy.PlanBusinessUnitCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.businessDesign.BusinessDesign;
import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysis;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.*;
import net.qixiaowei.strategy.cloud.api.domain.plan.PlanBusinessUnit;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.AnnualKeyWork;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasure;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetrics;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.*;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDTO;
import net.qixiaowei.strategy.cloud.mapper.businessDesign.BusinessDesignMapper;
import net.qixiaowei.strategy.cloud.mapper.gap.GapAnalysisMapper;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.*;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.AnnualKeyWorkMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMeasureMapper;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.StrategyMetricsMapper;
import net.qixiaowei.strategy.cloud.service.impl.marketInsight.PackCopyMarketInsight;
import net.qixiaowei.strategy.cloud.service.plan.IPlanBusinessUnitService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;


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

    @Autowired
    private MarketInsightCustomerMapper marketInsightCustomerMapper;//看客户
    @Autowired
    private MarketInsightIndustryMapper marketInsightIndustryMapper;//看行业
    @Autowired
    private MarketInsightMacroMapper marketInsightMacroMapper;//看宏观
    @Autowired
    private MarketInsightOpponentMapper marketInsightOpponentMapper;//看对手
    @Autowired
    private MarketInsightSelfMapper marketInsightSelfMapper;//看自身
    @Autowired
    private GapAnalysisMapper gapAnalysisMapper;//差距分析
    @Autowired
    private BusinessDesignMapper businessDesignMapper;//业务设计
    @Autowired
    private AnnualKeyWorkMapper annualKeyWorkMapper;//年度重点工作
    @Autowired
    private StrategyMeasureMapper strategyMeasureMapper;//战略举措清单
    @Autowired
    private StrategyMetricsMapper strategyMetricsMapper;//战略衡量指标


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
            planBusinessUnitDTO.setBusinessUnitDecomposes(PlanBusinessUnitCode.getDropList(businessUnitDecompose));
            planBusinessUnitDTO.setBusinessUnitDecomposeName(PlanBusinessUnitCode.getBusinessUnitDecomposeName(businessUnitDecompose));
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
                planBusinessUnitDTO.setBusinessUnitDecomposes(PlanBusinessUnitCode.getDropList(businessUnitDecompose));
                planBusinessUnitDTO.setBusinessUnitDecomposeName(PlanBusinessUnitCode.getBusinessUnitDecomposeName(businessUnitDecompose));
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
            if (!PlanBusinessUnitCode.contains(business) && !business.equals("company"))
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
        // 差距分析
        List<GapAnalysisDTO> gapAnalysisDTOS = gapAnalysisMapper.selectGapAnalysisByPlanBusinessUnitIds(planBusinessUnitIds);
        if (StringUtils.isNotEmpty(gapAnalysisDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        // 业务设计
        List<BusinessDesignDTO> businessDesignDTOS = businessDesignMapper.selectBusinessDesignByPlanBusinessUnitIds(planBusinessUnitIds);
        if (StringUtils.isNotEmpty(businessDesignDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        // 年度重点工作
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = annualKeyWorkMapper.selectAnnualKeyWorkByPlanBusinessUnitIds(planBusinessUnitIds);
        if (StringUtils.isNotEmpty(annualKeyWorkDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        // 战略举措清单
        List<StrategyMeasureDTO> strategyMeasureDTOS = strategyMeasureMapper.selectStrategyMeasureByPlanBusinessUnitIds(planBusinessUnitIds);
        if (StringUtils.isNotEmpty(strategyMeasureDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        // 战略衡量指标
        List<StrategyMetricsDTO> strategyMetricsDTOS = strategyMetricsMapper.selectStrategyMetricsByPlanBusinessUnitIds(planBusinessUnitIds);
        if (StringUtils.isNotEmpty(strategyMetricsDTOS)) {
            throw new ServiceException("数据被引用！");
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
        int i = 0;
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
        //看客户
        MarketInsightCustomer marketInsightCustomer = new MarketInsightCustomer();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, marketInsightCustomer);
        List<MarketInsightCustomerDTO> marketInsightCustomerDTOS = marketInsightCustomerMapper.selectMarketInsightCustomerList(marketInsightCustomer);
        if (StringUtils.isNotEmpty(marketInsightCustomerDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        //看行业
        MarketInsightIndustry marketInsightIndustry = new MarketInsightIndustry();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, marketInsightIndustry);
        List<MarketInsightIndustryDTO> marketInsightIndustryDTOS = marketInsightIndustryMapper.selectMarketInsightIndustryList(marketInsightIndustry);
        if (StringUtils.isNotEmpty(marketInsightIndustryDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        //看宏观
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, marketInsightMacro);
        List<MarketInsightMacroDTO> marketInsightMacroDTOS = marketInsightMacroMapper.selectMarketInsightMacroList(marketInsightMacro);
        if (StringUtils.isNotEmpty(marketInsightMacroDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        //看对手
        MarketInsightOpponent marketInsightOpponent = new MarketInsightOpponent();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, marketInsightOpponent);
        List<MarketInsightOpponentDTO> marketInsightOpponentDTOS = marketInsightOpponentMapper.selectMarketInsightOpponentList(marketInsightOpponent);
        if (StringUtils.isNotEmpty(marketInsightOpponentDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        //看自身
        MarketInsightSelf marketInsightSelf = new MarketInsightSelf();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, marketInsightSelf);
        {
            List<MarketInsightSelfDTO> marketInsightSelfDTOS = marketInsightSelfMapper.selectMarketInsightSelfList(marketInsightSelf);
            if (StringUtils.isNotEmpty(marketInsightSelfDTOS)) {
                throw new ServiceException("数据被引用！");
            }
        }
        //差距分析
        GapAnalysis gapAnalysis = new GapAnalysis();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, gapAnalysis);
        List<GapAnalysisDTO> gapAnalysisDTOS = gapAnalysisMapper.selectGapAnalysisList(gapAnalysis);
        if (StringUtils.isNotEmpty(gapAnalysisDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        //业务设计
        BusinessDesign businessDesign = new BusinessDesign();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, businessDesign);
        List<BusinessDesignDTO> businessDesignDTOS = businessDesignMapper.selectBusinessDesignList(businessDesign);
        if (StringUtils.isNotEmpty(businessDesignDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        //年度重点工作
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, annualKeyWork);
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = annualKeyWorkMapper.selectAnnualKeyWorkList(annualKeyWork);
        if (StringUtils.isNotEmpty(annualKeyWorkDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        //战略举措清单
        StrategyMeasure strategyMeasure = new StrategyMeasure();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, strategyMeasure);
        List<StrategyMeasureDTO> strategyMeasureDTOS = strategyMeasureMapper.selectStrategyMeasureList(strategyMeasure);
        if (StringUtils.isNotEmpty(strategyMeasureDTOS)) {
            throw new ServiceException("数据被引用！");
        }
        //战略衡量指标
        StrategyMetrics strategyMetrics = new StrategyMetrics();
        PackCopyMarketInsight.copyProperties(planBusinessUnitDTO, strategyMetrics);
        List<StrategyMetricsDTO> strategyMetricsDTOS = strategyMetricsMapper.selectStrategyMetricsList(strategyMetrics);
        if (StringUtils.isNotEmpty(strategyMetricsDTOS)) {
            throw new ServiceException("数据被引用！");
        }

        i = planBusinessUnitMapper.logicDeletePlanBusinessUnitByPlanBusinessUnitId(planBusinessUnit);
        return i;
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

