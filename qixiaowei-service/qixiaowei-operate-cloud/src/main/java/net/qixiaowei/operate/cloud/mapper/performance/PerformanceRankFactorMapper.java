package net.qixiaowei.operate.cloud.mapper.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceRankFactor;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankFactorDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * PerformanceRankFactorMapper接口
 *
 * @author Graves
 * @since 2022-10-06
 */
public interface PerformanceRankFactorMapper {
    /**
     * 查询绩效等级系数
     *
     * @param performanceRankFactorId 绩效等级系数主键
     * @return 绩效等级系数
     */
    PerformanceRankFactorDTO selectPerformanceRankFactorByPerformanceRankFactorId(@Param("performanceRankFactorId") Long performanceRankFactorId);

    /**
     * 查询绩效等级系数列表
     *
     * @param performanceRankFactor 绩效等级系数
     * @return 绩效等级系数集合
     */
    List<PerformanceRankFactorDTO> selectPerformanceRankFactorList(@Param("performanceRankFactor") PerformanceRankFactor performanceRankFactor);

    /**
     * 新增绩效等级系数
     *
     * @param performanceRankFactor 绩效等级系数
     * @return 结果
     */
    int insertPerformanceRankFactor(@Param("performanceRankFactor") PerformanceRankFactor performanceRankFactor);

    /**
     * 修改绩效等级系数
     *
     * @param performanceRankFactor 绩效等级系数
     * @return 结果
     */
    int updatePerformanceRankFactor(@Param("performanceRankFactor") PerformanceRankFactor performanceRankFactor);

    /**
     * 批量修改绩效等级系数
     *
     * @param performanceRankFactorList 绩效等级系数
     * @return 结果
     */
    int updatePerformanceRankFactors(@Param("performanceRankFactorList") List<PerformanceRankFactor> performanceRankFactorList);

    /**
     * 逻辑删除绩效等级系数
     *
     * @param performanceRankFactor
     * @return 结果
     */
    int logicDeletePerformanceRankFactorByPerformanceRankFactorId(@Param("performanceRankFactor") PerformanceRankFactor performanceRankFactor, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除绩效等级系数
     *
     * @param performanceRankFactorIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformanceRankFactorByPerformanceRankFactorIds(@Param("performanceRankFactorIds") List<Long> performanceRankFactorIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效等级系数
     *
     * @param performanceRankFactorId 绩效等级系数主键
     * @return 结果
     */
    int deletePerformanceRankFactorByPerformanceRankFactorId(@Param("performanceRankFactorId") Long performanceRankFactorId);

    /**
     * 物理批量删除绩效等级系数
     *
     * @param performanceRankFactorIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformanceRankFactorByPerformanceRankFactorIds(@Param("performanceRankFactorIds") List<Long> performanceRankFactorIds);

    /**
     * 批量新增绩效等级系数
     *
     * @param PerformanceRankFactors 绩效等级系数列表
     * @return 结果
     */
    int batchPerformanceRankFactor(@Param("performanceRankFactors") List<PerformanceRankFactor> PerformanceRankFactors);

    /**
     * 根据绩效等级id查询等级系数
     *
     * @param performanceRankId
     * @return
     */
    List<PerformanceRankFactorDTO> selectPerformanceRankFactorByPerformanceRankId(@Param("performanceRankId") Long performanceRankId);

}
