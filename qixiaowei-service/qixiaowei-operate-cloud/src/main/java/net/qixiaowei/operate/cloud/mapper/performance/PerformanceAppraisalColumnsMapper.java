package net.qixiaowei.operate.cloud.mapper.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalColumns;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalColumnsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * PerformanceAppraisalColumnsMapper接口
 *
 * @author Graves
 * @since 2022-11-28
 */
public interface PerformanceAppraisalColumnsMapper {
    /**
     * 查询绩效考核自定义列表
     *
     * @param performAppraisalColumnsId 绩效考核自定义列表主键
     * @return 绩效考核自定义列表
     */
    PerformanceAppraisalColumnsDTO selectPerformanceAppraisalColumnsByPerformAppraisalColumnsId(@Param("performAppraisalColumnsId") Long performAppraisalColumnsId);


    /**
     * 批量查询绩效考核自定义列表
     *
     * @param performAppraisalColumnsIds 绩效考核自定义列表主键集合
     * @return 绩效考核自定义列表
     */
    List<PerformanceAppraisalColumnsDTO> selectPerformanceAppraisalColumnsByPerformAppraisalColumnsIds(@Param("performAppraisalColumnsIds") List<Long> performAppraisalColumnsIds);

    /**
     * 查询绩效考核自定义列表列表
     *
     * @param performanceAppraisalColumns 绩效考核自定义列表
     * @return 绩效考核自定义列表集合
     */
    List<PerformanceAppraisalColumnsDTO> selectPerformanceAppraisalColumnsList(@Param("performanceAppraisalColumns") PerformanceAppraisalColumns performanceAppraisalColumns);

    /**
     * 新增绩效考核自定义列表
     *
     * @param performanceAppraisalColumns 绩效考核自定义列表
     * @return 结果
     */
    int insertPerformanceAppraisalColumns(@Param("performanceAppraisalColumns") PerformanceAppraisalColumns performanceAppraisalColumns);

    /**
     * 修改绩效考核自定义列表
     *
     * @param performanceAppraisalColumns 绩效考核自定义列表
     * @return 结果
     */
    int updatePerformanceAppraisalColumns(@Param("performanceAppraisalColumns") PerformanceAppraisalColumns performanceAppraisalColumns);

    /**
     * 批量修改绩效考核自定义列表
     *
     * @param performanceAppraisalColumnsList 绩效考核自定义列表
     * @return 结果
     */
    int updatePerformanceAppraisalColumnss(@Param("performanceAppraisalColumnsList") List<PerformanceAppraisalColumns> performanceAppraisalColumnsList);

    /**
     * 逻辑删除绩效考核自定义列表
     *
     * @param performanceAppraisalColumns
     * @return 结果
     */
    int logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(@Param("performanceAppraisalColumns") PerformanceAppraisalColumns performanceAppraisalColumns);

    /**
     * 逻辑批量删除绩效考核自定义列表
     *
     * @param performAppraisalColumnsIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(@Param("performAppraisalColumnsIds") List<Long> performAppraisalColumnsIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效考核自定义列表
     *
     * @param performAppraisalColumnsId 绩效考核自定义列表主键
     * @return 结果
     */
    int deletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(@Param("performAppraisalColumnsId") Long performAppraisalColumnsId);

    /**
     * 物理批量删除绩效考核自定义列表
     *
     * @param performAppraisalColumnsIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(@Param("performAppraisalColumnsIds") List<Long> performAppraisalColumnsIds);

    /**
     * 批量新增绩效考核自定义列表
     *
     * @param PerformanceAppraisalColumnss 绩效考核自定义列表列表
     * @return 结果
     */
    int batchPerformanceAppraisalColumns(@Param("performanceAppraisalColumnss") List<PerformanceAppraisalColumns> PerformanceAppraisalColumnss);

    /**
     * @param performanceAppraisalId 绩效考核ID
     * @return List
     */
    List<PerformanceAppraisalColumnsDTO> selectAppraisalColumnsByAppraisalId(@Param("performanceAppraisalId") Long performanceAppraisalId);

    /**
     * 根据考核ID集合查询自定义表
     *
     * @param performanceAppraisalIds 绩效考核ID集合
     * @return List
     */
    List<PerformanceAppraisalColumnsDTO> selectAppraisalColumnsByAppraisalIds(@Param("performanceAppraisalIds") List<Long> performanceAppraisalIds);
}
