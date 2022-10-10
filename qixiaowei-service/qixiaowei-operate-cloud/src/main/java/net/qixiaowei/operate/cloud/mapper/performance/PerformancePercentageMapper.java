package net.qixiaowei.operate.cloud.mapper.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformancePercentage;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformancePercentageDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * PerformancePercentageMapper接口
 *
 * @author Graves
 * @since 2022-10-10
 */
public interface PerformancePercentageMapper {
    /**
     * 查询绩效比例表
     *
     * @param performancePercentageId 绩效比例表主键
     * @return 绩效比例表
     */
    PerformancePercentageDTO selectPerformancePercentageByPerformancePercentageId(@Param("performancePercentageId") Long performancePercentageId);

    /**
     * 查询绩效比例表列表
     *
     * @param performancePercentage 绩效比例表
     * @return 绩效比例表集合
     */
    List<PerformancePercentageDTO> selectPerformancePercentageList(@Param("performancePercentage") PerformancePercentage performancePercentage);

    /**
     * 新增绩效比例表
     *
     * @param performancePercentage 绩效比例表
     * @return 结果
     */
    int insertPerformancePercentage(@Param("performancePercentage") PerformancePercentage performancePercentage);

    /**
     * 修改绩效比例表
     *
     * @param performancePercentage 绩效比例表
     * @return 结果
     */
    int updatePerformancePercentage(@Param("performancePercentage") PerformancePercentage performancePercentage);

    /**
     * 批量修改绩效比例表
     *
     * @param performancePercentageList 绩效比例表
     * @return 结果
     */
    int updatePerformancePercentages(@Param("performancePercentageList") List<PerformancePercentage> performancePercentageList);

    /**
     * 逻辑删除绩效比例表
     *
     * @param performancePercentage
     * @return 结果
     */
    int logicDeletePerformancePercentageByPerformancePercentageId(@Param("performancePercentage") PerformancePercentage performancePercentage);

    /**
     * 逻辑批量删除绩效比例表
     *
     * @param performancePercentageIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformancePercentageByPerformancePercentageIds(@Param("performancePercentageIds") List<Long> performancePercentageIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效比例表
     *
     * @param performancePercentageId 绩效比例表主键
     * @return 结果
     */
    int deletePerformancePercentageByPerformancePercentageId(@Param("performancePercentageId") Long performancePercentageId);

    /**
     * 物理批量删除绩效比例表
     *
     * @param performancePercentageIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformancePercentageByPerformancePercentageIds(@Param("performancePercentageIds") List<Long> performancePercentageIds);

    /**
     * 批量新增绩效比例表
     *
     * @param PerformancePercentages 绩效比例表列表
     * @return 结果
     */
    int batchPerformancePercentage(@Param("performancePercentages") List<PerformancePercentage> PerformancePercentages);

    /**
     * performancePercentageName唯一性校验
     *
     * @param performancePercentageName
     * @return
     */
    int isUnique(@Param("performancePercentageName") String performancePercentageName);
}
