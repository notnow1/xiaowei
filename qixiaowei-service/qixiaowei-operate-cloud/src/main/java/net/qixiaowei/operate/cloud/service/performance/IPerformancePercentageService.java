package net.qixiaowei.operate.cloud.service.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;


/**
 * PerformancePercentageService接口
 *
 * @author Graves
 * @since 2022-10-10
 */
public interface IPerformancePercentageService {
    /**
     * 查询绩效比例表
     *
     * @param performancePercentageId 绩效比例表主键
     * @return 绩效比例表
     */
    PerformancePercentageDTO selectPerformancePercentageByPerformancePercentageId(Long performancePercentageId);

    /**
     * 查询绩效比例表列表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 绩效比例表集合
     */
    List<PerformancePercentageDTO> selectPerformancePercentageList(PerformancePercentageDTO performancePercentageDTO);

    /**
     * 新增绩效比例表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    int insertPerformancePercentage(PerformancePercentageDTO performancePercentageDTO);

    /**
     * 修改绩效比例表
     *
     * @param performancePercentageDTO 绩效比例表
     * @return 结果
     */
    int updatePerformancePercentage(PerformancePercentageDTO performancePercentageDTO);

    /**
     * 批量修改绩效比例表
     *
     * @param performancePercentageDtos 绩效比例表
     * @return 结果
     */
    int updatePerformancePercentages(List<PerformancePercentageDTO> performancePercentageDtos);

    /**
     * 逻辑批量删除绩效比例表
     *
     * @param performancePercentageIds 需要删除的绩效比例表集合
     * @return 结果
     */
    int logicDeletePerformancePercentageByPerformancePercentageIds(List<Long> performancePercentageIds);

    /**
     * 逻辑删除绩效比例表信息
     *
     * @param performancePercentageDTO
     * @return 结果
     */
    int logicDeletePerformancePercentageByPerformancePercentageId(PerformancePercentageDTO performancePercentageDTO);

    /**
     * 逻辑批量删除绩效比例表
     *
     * @param PerformancePercentageDtos 需要删除的绩效比例表集合
     * @return 结果
     */
    int deletePerformancePercentageByPerformancePercentageIds(List<PerformancePercentageDTO> PerformancePercentageDtos);

    /**
     * 逻辑删除绩效比例表信息
     *
     * @param performancePercentageDTO
     * @return 结果
     */
    int deletePerformancePercentageByPerformancePercentageId(PerformancePercentageDTO performancePercentageDTO);


    /**
     * 删除绩效比例表信息
     *
     * @param performancePercentageId 绩效比例表主键
     * @return 结果
     */
    int deletePerformancePercentageByPerformancePercentageId(Long performancePercentageId);

    /**
     * 判断
     *
     * @param performanceRankId
     * @param performanceRankCategory
     * @return
     */
    int isQuote(Long performanceRankId, Integer performanceRankCategory);
}
