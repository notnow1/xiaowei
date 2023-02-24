package net.qixiaowei.strategy.cloud.service.impl.gap;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysis;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOpportunityDTO;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisPerformanceDTO;
import net.qixiaowei.strategy.cloud.api.dto.plan.PlanBusinessUnitDTO;
import net.qixiaowei.strategy.cloud.mapper.gap.GapAnalysisMapper;
import net.qixiaowei.strategy.cloud.mapper.plan.PlanBusinessUnitMapper;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisOperateService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisOpportunityService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisPerformanceService;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisService;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * GapAnalysisService业务层处理
 *
 * @author Graves
 * @since 2023-02-24
 */
@Service
public class GapAnalysisServiceImpl implements IGapAnalysisService {
    @Autowired
    private GapAnalysisMapper gapAnalysisMapper;

    @Autowired
    private PlanBusinessUnitMapper planBusinessUnitMapper;

    @Autowired
    private RemoteIndicatorService indicatorService;

    @Autowired
    private IGapAnalysisOperateService gapAnalysisOperateService;

    @Autowired
    private IGapAnalysisPerformanceService gapAnalysisPerformanceService;

    @Autowired
    private IGapAnalysisOpportunityService gapAnalysisOpportunityService;

    /**
     * 查询差距分析表
     *
     * @param gapAnalysisId 差距分析表主键
     * @return 差距分析表
     */
    @Override
    public GapAnalysisDTO selectGapAnalysisByGapAnalysisId(Long gapAnalysisId) {
        return gapAnalysisMapper.selectGapAnalysisByGapAnalysisId(gapAnalysisId);
    }

    /**
     * 查询差距分析表列表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 差距分析表
     */
    @Override
    public List<GapAnalysisDTO> selectGapAnalysisList(GapAnalysisDTO gapAnalysisDTO) {
        GapAnalysis gapAnalysis = new GapAnalysis();
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        return gapAnalysisMapper.selectGapAnalysisList(gapAnalysis);
    }

    /**
     * 新增差距分析表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */
    @Override
    public GapAnalysisDTO insertGapAnalysis(GapAnalysisDTO gapAnalysisDTO) {
        if (StringUtils.isNull(gapAnalysisDTO)) {
            throw new ServiceException("请传参");
        }
        Long planBusinessUnitId = gapAnalysisDTO.getPlanBusinessUnitId();
        PlanBusinessUnitDTO planBusinessUnitDTO = planBusinessUnitMapper.selectPlanBusinessUnitByPlanBusinessUnitId(planBusinessUnitId);
        if (StringUtils.isNull(planBusinessUnitDTO)) {
            throw new ServiceException("当前的规划业务单元已经不存在");
        }
        String businessUnitDecompose = planBusinessUnitDTO.getBusinessUnitDecompose();
        if (StringUtils.isNull(businessUnitDecompose)) {
            throw new ServiceException("规划业务单元维度数据异常 请检查规划业务单元配置");
        }
        // 判断是否选择校验：region,department,product,industry,company
        GapAnalysis gapAnalysisParams = getGapAnalysisParams(gapAnalysisDTO, businessUnitDecompose);
        // 是否重复
        Integer planYear = gapAnalysisDTO.getPlanYear();
        gapAnalysisParams.setPlanYear(planYear);
        gapAnalysisParams.setPlanBusinessUnitId(gapAnalysisDTO.getPlanBusinessUnitId());
        List<GapAnalysisDTO> gapAnalysisDTOS = gapAnalysisMapper.selectGapAnalysisList(gapAnalysisParams);
        if (StringUtils.isNotEmpty(gapAnalysisDTOS)) {
            throw new ServiceException("当前的规划业务单元内容重复 请重新筛选");
        }
        GapAnalysis gapAnalysis = new GapAnalysis();
        // 将规划业务单元的规划业务单元维度赋进主表
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        gapAnalysis.setBusinessUnitDecompose(businessUnitDecompose);
        gapAnalysis.setCreateBy(SecurityUtils.getUserId());
        gapAnalysis.setCreateTime(DateUtils.getNowDate());
        gapAnalysis.setUpdateTime(DateUtils.getNowDate());
        gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
        gapAnalysis.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        gapAnalysisMapper.insertGapAnalysis(gapAnalysis);
        Long gapAnalysisId = gapAnalysis.getGapAnalysisId();
        Integer operateHistoryYear = gapAnalysisDTO.getOperateHistoryYear();
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS = gapAnalysisDTO.getGapAnalysisOperateDTOS();
//        List<Integer> planYears = new ArrayList<>();
//        for (int year = planYear - 1; year >= planYear - operateHistoryYear; year--) {
//            planYears.add(year);
//        }
        // 历史经营情况
        addAnalysisOperateList(gapAnalysisId, gapAnalysisOperateDTOS);
        // 业绩差距
        List<GapAnalysisPerformanceDTO> gapAnalysisPerformanceDTOS = gapAnalysisDTO.getGapAnalysisPerformanceDTOS();// 机会差距
        if (StringUtils.isNotEmpty(gapAnalysisPerformanceDTOS)) {
            for (GapAnalysisPerformanceDTO gapAnalysisPerformanceDTO : gapAnalysisPerformanceDTOS) {
                gapAnalysisPerformanceDTO.setGapAnalysisId(gapAnalysisId);
            }
            gapAnalysisPerformanceService.insertGapAnalysisPerformances(gapAnalysisPerformanceDTOS);
        }
        List<GapAnalysisOpportunityDTO> gapAnalysisOpportunityDTOS = gapAnalysisDTO.getGapAnalysisOpportunityDTOS();// 业绩差距
        if (StringUtils.isNotEmpty(gapAnalysisOpportunityDTOS)) {
            for (GapAnalysisOpportunityDTO gapAnalysisOpportunityDTO : gapAnalysisOpportunityDTOS) {
                gapAnalysisOpportunityDTO.setGapAnalysisId(gapAnalysisId);
            }
            gapAnalysisOpportunityService.insertGapAnalysisOpportunitys(gapAnalysisOpportunityDTOS);
        }
        return gapAnalysisDTO;
    }

    /**
     * 新增历史经营情况
     *
     * @param gapAnalysisId          差距分析ID
     * @param gapAnalysisOperateDTOS 历史经营情况列表
     */
    private void addAnalysisOperateList(Long gapAnalysisId, List<GapAnalysisOperateDTO> gapAnalysisOperateDTOS) {
        if (StringUtils.isNotEmpty(gapAnalysisOperateDTOS)) {
            List<String> indicatorNames = gapAnalysisOperateDTOS.stream().map(GapAnalysisOperateDTO::getIndicatorName).collect(Collectors.toList());
            R<List<IndicatorDTO>> indicatorByNamesR = indicatorService.selectIndicatorByNames(indicatorNames, SecurityConstants.INNER);
            List<IndicatorDTO> indicatorByNames = indicatorByNamesR.getData();
            if (StringUtils.isNull(indicatorByNames)) {
                throw new ServiceException("历史经营情况列表指标已不存在 请检查指标配置");
            }
            List<String> indicatorNamesByName = indicatorByNames.stream().map(IndicatorDTO::getIndicatorName).collect(Collectors.toList());
            List<GapAnalysisOperateDTO> gapAnalysisOperates = new ArrayList<>();
            for (int i = 0; i < gapAnalysisOperateDTOS.size(); i++) {
                GapAnalysisOperateDTO gapAnalysisOperateDTO = gapAnalysisOperateDTOS.get(i);
                if (!indicatorNamesByName.contains(gapAnalysisOperateDTO.getIndicatorName())) {
                    throw new ServiceException("历史经营情况列表指标已不存在 请检查指标配置");
                }
                IndicatorDTO matchIndicatorDTO = indicatorByNames.stream().filter(indicatorDTO //匹配的指标DTO
                        -> StringUtils.equals(indicatorDTO.getIndicatorName(), gapAnalysisOperateDTO.getIndicatorName())).collect(Collectors.toList()).get(0);
                Long indicatorId = matchIndicatorDTO.getIndicatorId();
                String indicatorName = matchIndicatorDTO.getIndicatorName();
                GapAnalysisOperateDTO gapAnalysisOperate = new GapAnalysisOperateDTO();
                List<GapAnalysisOperateDTO> gapAnalysisOperateDTOSList = gapAnalysisOperateDTO.getGapAnalysisOperateDTOS();
                for (GapAnalysisOperateDTO analysisOperateDTO : gapAnalysisOperateDTOSList) {
//                    gapAnalysisOperate.setHistoryYear(analysisOperateDTO.getHistoryYear());
                    gapAnalysisOperate.setGapAnalysisId(gapAnalysisId);
                    gapAnalysisOperate.setIndicatorId(indicatorId);
                    gapAnalysisOperate.setIndicatorName(indicatorName);
                    gapAnalysisOperate.setTargetValue(analysisOperateDTO.getTargetValue());
                    gapAnalysisOperate.setActualValue(analysisOperateDTO.getActualValue());
                    gapAnalysisOperate.setSort(i);
                    gapAnalysisOperates.add(gapAnalysisOperate);
                }
            }
            gapAnalysisOperateService.insertGapAnalysisOperates(gapAnalysisOperates);
        }
    }

    /**
     * 获取差距分析的入参
     * 校验入参的ID是否与维度匹配
     *
     * @param gapAnalysisDTO        差距分析DTO
     * @param businessUnitDecompose 维度
     * @return 差距分析入参DTO
     */
    private GapAnalysis getGapAnalysisParams(GapAnalysisDTO gapAnalysisDTO, String businessUnitDecompose) {
        Long areaId = gapAnalysisDTO.getAreaId();
        Long industryId = gapAnalysisDTO.getIndustryId();
        Long productId = gapAnalysisDTO.getProductId();
        Long departmentId = gapAnalysisDTO.getDepartmentId();
        GapAnalysis gapAnalysisParams = new GapAnalysis();
        if (businessUnitDecompose.contains("region")) {
            if (StringUtils.isNull(areaId)) {
                throw new ServiceException("请选择区域");
            }
            gapAnalysisParams.setAreaId(areaId);
        }
        if (businessUnitDecompose.contains("department")) {
            if (StringUtils.isNull(areaId)) {
                throw new ServiceException("请选择部门");
            }
            gapAnalysisParams.setDepartmentId(departmentId);
        }
        if (businessUnitDecompose.contains("product")) {
            if (StringUtils.isNull(areaId)) {
                throw new ServiceException("请选择产品");
            }
            gapAnalysisParams.setProductId(productId);
        }
        if (businessUnitDecompose.contains("industry")) {
            if (StringUtils.isNull(areaId)) {
                throw new ServiceException("请选择行业");
            }
            gapAnalysisParams.setIndustryId(industryId);
        }
        return gapAnalysisParams;
    }

    /**
     * 修改差距分析表
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */
    @Override
    public int updateGapAnalysis(GapAnalysisDTO gapAnalysisDTO) {
        GapAnalysis gapAnalysis = new GapAnalysis();
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        gapAnalysis.setUpdateTime(DateUtils.getNowDate());
        gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
        return gapAnalysisMapper.updateGapAnalysis(gapAnalysis);
    }

    /**
     * 逻辑批量删除差距分析表
     *
     * @param gapAnalysisIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisByGapAnalysisIds(List<Long> gapAnalysisIds) {
        return gapAnalysisMapper.logicDeleteGapAnalysisByGapAnalysisIds(gapAnalysisIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除差距分析表信息
     *
     * @param gapAnalysisId 差距分析表主键
     * @return 结果
     */
    @Override
    public int deleteGapAnalysisByGapAnalysisId(Long gapAnalysisId) {
        return gapAnalysisMapper.deleteGapAnalysisByGapAnalysisId(gapAnalysisId);
    }

    /**
     * 逻辑删除差距分析表信息
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisByGapAnalysisId(GapAnalysisDTO gapAnalysisDTO) {
        GapAnalysis gapAnalysis = new GapAnalysis();
        gapAnalysis.setGapAnalysisId(gapAnalysisDTO.getGapAnalysisId());
        gapAnalysis.setUpdateTime(DateUtils.getNowDate());
        gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
        return gapAnalysisMapper.logicDeleteGapAnalysisByGapAnalysisId(gapAnalysis);
    }

    /**
     * 物理删除差距分析表信息
     *
     * @param gapAnalysisDTO 差距分析表
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisByGapAnalysisId(GapAnalysisDTO gapAnalysisDTO) {
        GapAnalysis gapAnalysis = new GapAnalysis();
        BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
        return gapAnalysisMapper.deleteGapAnalysisByGapAnalysisId(gapAnalysis.getGapAnalysisId());
    }

    /**
     * 物理批量删除差距分析表
     *
     * @param gapAnalysisDtos 需要删除的差距分析表主键
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisByGapAnalysisIds(List<GapAnalysisDTO> gapAnalysisDtos) {
        List<Long> stringList = new ArrayList();
        for (GapAnalysisDTO gapAnalysisDTO : gapAnalysisDtos) {
            stringList.add(gapAnalysisDTO.getGapAnalysisId());
        }
        return gapAnalysisMapper.deleteGapAnalysisByGapAnalysisIds(stringList);
    }

    /**
     * 批量新增差距分析表信息
     *
     * @param gapAnalysisDtos 差距分析表对象
     */

    public int insertGapAnalysiss(List<GapAnalysisDTO> gapAnalysisDtos) {
        List<GapAnalysis> gapAnalysisList = new ArrayList();

        for (GapAnalysisDTO gapAnalysisDTO : gapAnalysisDtos) {
            GapAnalysis gapAnalysis = new GapAnalysis();
            BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
            gapAnalysis.setCreateBy(SecurityUtils.getUserId());
            gapAnalysis.setCreateTime(DateUtils.getNowDate());
            gapAnalysis.setUpdateTime(DateUtils.getNowDate());
            gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysis.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            gapAnalysisList.add(gapAnalysis);
        }
        return gapAnalysisMapper.batchGapAnalysis(gapAnalysisList);
    }

    /**
     * 批量修改差距分析表信息
     *
     * @param gapAnalysisDtos 差距分析表对象
     */

    public int updateGapAnalysiss(List<GapAnalysisDTO> gapAnalysisDtos) {
        List<GapAnalysis> gapAnalysisList = new ArrayList();

        for (GapAnalysisDTO gapAnalysisDTO : gapAnalysisDtos) {
            GapAnalysis gapAnalysis = new GapAnalysis();
            BeanUtils.copyProperties(gapAnalysisDTO, gapAnalysis);
            gapAnalysis.setCreateBy(SecurityUtils.getUserId());
            gapAnalysis.setCreateTime(DateUtils.getNowDate());
            gapAnalysis.setUpdateTime(DateUtils.getNowDate());
            gapAnalysis.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisList.add(gapAnalysis);
        }
        return gapAnalysisMapper.updateGapAnalysiss(gapAnalysisList);
    }

}

