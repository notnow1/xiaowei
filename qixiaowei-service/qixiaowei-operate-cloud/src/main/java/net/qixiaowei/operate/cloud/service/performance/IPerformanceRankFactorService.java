package net.qixiaowei.operate.cloud.service.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;


/**
 * PerformanceRankFactorService接口
 *
 * @author Graves
 * @since 2022-10-06
 */
public interface IPerformanceRankFactorService {
    /**
     * 查询绩效等级系数
     *
     * @param performanceRankFactorId 绩效等级系数主键
     * @return 绩效等级系数
     */
    PerformanceRankFactorDTO selectPerformanceRankFactorByPerformanceRankFactorId(Long performanceRankFactorId);

    /**
     * 查询绩效等级系数列表
     *
     * @param performanceRankFactorDTO 绩效等级系数
     * @return 绩效等级系数集合
     */
    List<PerformanceRankFactorDTO> selectPerformanceRankFactorList(PerformanceRankFactorDTO performanceRankFactorDTO);

    /**
     * 新增绩效等级系数
     *
     * @param performanceRankFactorDTO 绩效等级系数
     * @return 结果
     */
    int insertPerformanceRankFactor(PerformanceRankFactorDTO performanceRankFactorDTO);

    /**
     * 修改绩效等级系数
     *
     * @param performanceRankFactorDTO 绩效等级系数
     * @return 结果
     */
    int updatePerformanceRankFactor(PerformanceRankFactorDTO performanceRankFactorDTO);

    /**
     * 批量修改绩效等级系数
     *
     * @param performanceRankFactorDtos 绩效等级系数
     * @return 结果
     */
    int updatePerformanceRankFactors(List<PerformanceRankFactorDTO> performanceRankFactorDtos);

    /**
     * 更新绩效等级系数
     *
     * @param performanceRankFactorDtos 绩效等级系数
     * @return 结果
     */
    int operatePerformanceRankFactor(List<PerformanceRankFactorDTO> performanceRankFactorDtos, Long performanceRankId);

    /**
     * 批量新增绩效等级系数
     *
     * @param performanceRankFactorDtos 绩效等级系数
     * @return 结果
     */
    int insertPerformanceRankFactors(List<PerformanceRankFactorDTO> performanceRankFactorDtos);

    /**
     * 逻辑批量删除绩效等级系数
     *
     * @param PerformanceRankFactorDtos 需要删除的绩效等级系数集合
     * @return 结果
     */
    int logicDeletePerformanceRankFactorByPerformanceRankFactorIds(List<PerformanceRankFactorDTO> PerformanceRankFactorDtos);

    /**
     * 逻辑删除绩效等级系数信息
     *
     * @param performanceRankFactorDTO
     * @return 结果
     */
    int logicDeletePerformanceRankFactorByPerformanceRankFactorId(PerformanceRankFactorDTO performanceRankFactorDTO);

    /**
     * 逻辑批量删除绩效等级系数
     *
     * @param PerformanceRankFactorDtos 需要删除的绩效等级系数集合
     * @return 结果
     */
    int deletePerformanceRankFactorByPerformanceRankFactorIds(List<PerformanceRankFactorDTO> PerformanceRankFactorDtos);

    /**
     * 逻辑删除绩效等级系数信息
     *
     * @param performanceRankFactorDTO
     * @return 结果
     */
    int deletePerformanceRankFactorByPerformanceRankFactorId(PerformanceRankFactorDTO performanceRankFactorDTO);


    /**
     * 删除绩效等级系数信息
     *
     * @param performanceRankFactorId 绩效等级系数主键
     * @return 结果
     */
    int deletePerformanceRankFactorByPerformanceRankFactorId(Long performanceRankFactorId);

    /**
     * 根据绩效等级id查询等级系数
     *
     * @param performanceRankId
     * @return
     */
    List<PerformanceRankFactorDTO> selectPerformanceRankFactorByPerformanceRankId(Long performanceRankId);
}
