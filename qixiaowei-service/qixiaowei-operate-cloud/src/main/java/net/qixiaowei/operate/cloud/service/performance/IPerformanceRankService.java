package net.qixiaowei.operate.cloud.service.performance;

import java.util.List;
import java.util.Map;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceRank;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankDTO;


/**
 * PerformanceRankService接口
 *
 * @author Graves
 * @since 2022-10-06
 */
public interface IPerformanceRankService {
    /**
     * 查询绩效等级表
     *
     * @param performanceRankId 绩效等级表主键
     * @return 绩效等级表
     */
    PerformanceRankDTO selectPerformanceRankByPerformanceRankId(Long performanceRankId);

    /**
     * 查询绩效等级表列表
     *
     * @param performanceRankDTO 绩效等级表
     * @return 绩效等级表集合
     */
    List<PerformanceRankDTO> selectPerformanceRankList(PerformanceRankDTO performanceRankDTO);
    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<PerformanceRankDTO> result);
    /**
     * 新增绩效等级表
     *
     * @param performanceRankDTO 绩效等级表
     * @return 结果
     */
    int insertPerformanceRank(PerformanceRankDTO performanceRankDTO);

    /**
     * 修改绩效等级表
     *
     * @param performanceRankDTO 绩效等级表
     * @return 结果
     */
    int updatePerformanceRank(PerformanceRankDTO performanceRankDTO);

    /**
     * 批量修改绩效等级表
     *
     * @param performanceRankDtos 绩效等级表
     * @return 结果
     */
    int updatePerformanceRanks(List<PerformanceRankDTO> performanceRankDtos);

    /**
     * 批量新增绩效等级表
     *
     * @param performanceRankDtos 绩效等级表
     * @return 结果
     */
    int insertPerformanceRanks(List<PerformanceRankDTO> performanceRankDtos);

    /**
     * 逻辑批量删除绩效等级表
     *
     * @param PerformanceRankIds 需要删除的绩效等级表集合
     * @return 结果
     */
    int logicDeletePerformanceRankByPerformanceRankIds(List<Long> PerformanceRankIds);

    /**
     * 逻辑删除绩效等级表信息
     *
     * @param performanceRankDTO
     * @return 结果
     */
    int logicDeletePerformanceRankByPerformanceRankId(PerformanceRankDTO performanceRankDTO);

    /**
     * 逻辑批量删除绩效等级表
     *
     * @param PerformanceRankDtos 需要删除的绩效等级表集合
     * @return 结果
     */
    int deletePerformanceRankByPerformanceRankIds(List<PerformanceRankDTO> PerformanceRankDtos);

    /**
     * 逻辑删除绩效等级表信息
     *
     * @param performanceRankDTO
     * @return 结果
     */
    int deletePerformanceRankByPerformanceRankId(PerformanceRankDTO performanceRankDTO);

    /**
     * 删除绩效等级表信息
     *
     * @param performanceRankId 绩效等级表主键
     * @return 结果
     */
    int deletePerformanceRankByPerformanceRankId(Long performanceRankId);

    /***
     * 绩效等级详情
     *
     * @param performanceRankId
     * @return
     */
    PerformanceRankDTO detailPerformanceRank(Long performanceRankId);

    /**
     * 返回不同performance_rank_category的PerformanceRankDTO
     *
     * @return
     */
    Map<String, List<PerformanceRank>> detailLevelInfo();

    /**
     * 通过ids查找绩效等级
     *
     * @param orgPerformanceRankIds
     * @return
     */
    List<PerformanceRank> selectPerformanceRank(List<Long> orgPerformanceRankIds);
}
