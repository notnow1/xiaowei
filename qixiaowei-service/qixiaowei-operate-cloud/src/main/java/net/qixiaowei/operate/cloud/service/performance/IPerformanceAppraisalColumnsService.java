package net.qixiaowei.operate.cloud.service.performance;

import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalColumnsDTO;

import java.util.List;


/**
 * PerformanceAppraisalColumnsService接口
 *
 * @author Graves
 * @since 2022-11-28
 */
public interface IPerformanceAppraisalColumnsService {
    /**
     * 查询绩效考核自定义列表
     *
     * @param performAppraisalColumnsId 绩效考核自定义列表主键
     * @return 绩效考核自定义列表
     */
    PerformanceAppraisalColumnsDTO selectPerformanceAppraisalColumnsByPerformAppraisalColumnsId(Long performAppraisalColumnsId);

    /**
     * 查询绩效考核自定义列表列表
     *
     * @param performanceAppraisalColumnsDTO 绩效考核自定义列表
     * @return 绩效考核自定义列表集合
     */
    List<PerformanceAppraisalColumnsDTO> selectPerformanceAppraisalColumnsList(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO);

    /**
     * 新增绩效考核自定义列表
     *
     * @param performanceAppraisalColumnsDTO 绩效考核自定义列表
     * @return 结果
     */
    PerformanceAppraisalColumnsDTO insertPerformanceAppraisalColumns(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO);

    /**
     * 修改绩效考核自定义列表
     *
     * @param performanceAppraisalColumnsDTO 绩效考核自定义列表
     * @return 结果
     */
    int updatePerformanceAppraisalColumns(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO);

    /**
     * 批量修改绩效考核自定义列表
     *
     * @param performanceAppraisalColumnsDtos 绩效考核自定义列表
     * @return 结果
     */
    int updatePerformanceAppraisalColumnss(List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDtos);

    /**
     * 批量新增绩效考核自定义列表
     *
     * @param performanceAppraisalColumnsDtos 绩效考核自定义列表
     * @return 结果
     */
    int insertPerformanceAppraisalColumnss(List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDtos);

    /**
     * 逻辑批量删除绩效考核自定义列表
     *
     * @param performAppraisalColumnsIds 需要删除的绩效考核自定义列表集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(List<Long> performAppraisalColumnsIds);

    /**
     * 逻辑删除绩效考核自定义列表信息
     *
     * @param performanceAppraisalColumnsDTO
     * @return 结果
     */
    int logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO);

    /**
     * 批量删除绩效考核自定义列表
     *
     * @param PerformanceAppraisalColumnsDtos
     * @return 结果
     */
    int deletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(List<PerformanceAppraisalColumnsDTO> PerformanceAppraisalColumnsDtos);

    /**
     * 逻辑删除绩效考核自定义列表信息
     *
     * @param performanceAppraisalColumnsDTO
     * @return 结果
     */
    int deletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO);


    /**
     * 删除绩效考核自定义列表信息
     *
     * @param performAppraisalColumnsId 绩效考核自定义列表主键
     * @return 结果
     */
    int deletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(Long performAppraisalColumnsId);

    /**
     * 根据考核ID查询自定义表
     *
     * @param performanceAppraisalId 绩效考核ID
     * @return
     */
    List<PerformanceAppraisalColumnsDTO> selectAppraisalColumnsByAppraisalId(Long performanceAppraisalId);
}
