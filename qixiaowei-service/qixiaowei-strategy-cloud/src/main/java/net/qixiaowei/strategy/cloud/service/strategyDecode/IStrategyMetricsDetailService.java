package net.qixiaowei.strategy.cloud.service.strategyDecode;

import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetricsDetail;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDetailDTO;

import java.util.List;


/**
 * StrategyMetricsDetailService接口
 *
 * @author Graves
 * @since 2023-03-07
 */
public interface IStrategyMetricsDetailService {
    /**
     * 查询战略衡量指标详情表
     *
     * @param strategyMetricsDetailId 战略衡量指标详情表主键
     * @return 战略衡量指标详情表
     */
    StrategyMetricsDetailDTO selectStrategyMetricsDetailByStrategyMetricsDetailId(Long strategyMetricsDetailId);

    /**
     * 查询战略衡量指标详情表列表
     *
     * @param strategyMetricsDetailDTO 战略衡量指标详情表
     * @return 战略衡量指标详情表集合
     */
    List<StrategyMetricsDetailDTO> selectStrategyMetricsDetailList(StrategyMetricsDetailDTO strategyMetricsDetailDTO);

    /**
     * 新增战略衡量指标详情表
     *
     * @param strategyMetricsDetailDTO 战略衡量指标详情表
     * @return 结果
     */
    StrategyMetricsDetailDTO insertStrategyMetricsDetail(StrategyMetricsDetailDTO strategyMetricsDetailDTO);

    /**
     * 修改战略衡量指标详情表
     *
     * @param strategyMetricsDetailDTO 战略衡量指标详情表
     * @return 结果
     */
    int updateStrategyMetricsDetail(StrategyMetricsDetailDTO strategyMetricsDetailDTO);

    /**
     * 批量修改战略衡量指标详情表
     *
     * @param strategyMetricsDetailDtos 战略衡量指标详情表
     * @return 结果
     */
    int updateStrategyMetricsDetails(List<StrategyMetricsDetailDTO> strategyMetricsDetailDtos);

    /**
     * 批量新增战略衡量指标详情表
     *
     * @param strategyMetricsDetailDtos 战略衡量指标详情表
     * @return 结果
     */
    List<StrategyMetricsDetail> insertStrategyMetricsDetails(List<StrategyMetricsDetailDTO> strategyMetricsDetailDtos);

    /**
     * 逻辑批量删除战略衡量指标详情表
     *
     * @param strategyMetricsDetailIds 需要删除的战略衡量指标详情表集合
     * @return 结果
     */
    int logicDeleteStrategyMetricsDetailByStrategyMetricsDetailIds(List<Long> strategyMetricsDetailIds);

    /**
     * 逻辑删除战略衡量指标详情表信息
     *
     * @param strategyMetricsDetailDTO
     * @return 结果
     */
    int logicDeleteStrategyMetricsDetailByStrategyMetricsDetailId(StrategyMetricsDetailDTO strategyMetricsDetailDTO);

    /**
     * 批量删除战略衡量指标详情表
     *
     * @param StrategyMetricsDetailDtos
     * @return 结果
     */
    int deleteStrategyMetricsDetailByStrategyMetricsDetailIds(List<StrategyMetricsDetailDTO> StrategyMetricsDetailDtos);

    /**
     * 逻辑删除战略衡量指标详情表信息
     *
     * @param strategyMetricsDetailDTO
     * @return 结果
     */
    int deleteStrategyMetricsDetailByStrategyMetricsDetailId(StrategyMetricsDetailDTO strategyMetricsDetailDTO);


    /**
     * 删除战略衡量指标详情表信息
     *
     * @param strategyMetricsDetailId 战略衡量指标详情表主键
     * @return 结果
     */
    int deleteStrategyMetricsDetailByStrategyMetricsDetailId(Long strategyMetricsDetailId);

}
