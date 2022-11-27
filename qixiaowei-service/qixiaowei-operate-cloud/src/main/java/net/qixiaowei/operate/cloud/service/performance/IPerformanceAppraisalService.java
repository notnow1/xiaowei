package net.qixiaowei.operate.cloud.service.performance;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalExcel;

import java.util.List;


/**
 * PerformanceAppraisalService接口
 *
 * @author Graves
 * @since 2022-11-23
 */
public interface IPerformanceAppraisalService {
    /**
     * 查询绩效任务考核表
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 绩效考核表
     */
    PerformanceAppraisalDTO selectPerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId);

    /**
     * 查询组织绩效任务考核详情
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return
     */
    PerformanceAppraisalDTO selectOrgAppraisalArchiveById(Long performanceAppraisalDTO);

    /**
     * 查询绩效考核表列表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 绩效考核表集合
     */
    List<PerformanceAppraisalDTO> selectPerformanceAppraisalList(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 查询组织绩效归档
     *
     * @param performanceAppraisalDTO
     * @return
     */
    List<PerformanceAppraisalDTO> selectOrgAppraisalArchiveList(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 查询组织绩效结果排名
     *
     * @param appraisalObjectsIds
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> selectOrgAppraisalRankByDTO(List<Long> appraisalObjectsIds, Long performanceAppraisalId);

    /**
     * 新增绩效考核表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    int insertPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 修改绩效考核表
     *
     * @param performanceAppraisalDTO 绩效考核表
     * @return 结果
     */
    int updatePerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 批量修改绩效考核表
     *
     * @param performanceAppraisalDtos 绩效考核表
     * @return 结果
     */
    int updatePerformanceAppraisals(List<PerformanceAppraisalDTO> performanceAppraisalDtos);

    /**
     * 批量新增绩效考核表
     *
     * @param performanceAppraisalDtos 绩效考核表
     * @return 结果
     */
    int insertPerformanceAppraisals(List<PerformanceAppraisal> performanceAppraisalDtos);

    /**
     * 逻辑批量删除绩效考核表
     *
     * @param performanceAppraisalIds 需要删除的绩效考核表集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalByPerformanceAppraisalIds(List<Long> performanceAppraisalIds);

    /**
     * 逻辑删除绩效考核表信息
     *
     * @param performanceAppraisalDTO
     * @return 结果
     */
    int logicDeletePerformanceAppraisalByPerformanceAppraisalId(PerformanceAppraisalDTO performanceAppraisalDTO);

    /**
     * 批量删除绩效考核表
     *
     * @param PerformanceAppraisalDtos
     * @return 结果
     */
    int deletePerformanceAppraisalByPerformanceAppraisalIds(List<PerformanceAppraisalDTO> PerformanceAppraisalDtos);

    /**
     * 逻辑删除绩效考核表信息
     *
     * @param performanceAppraisalDTO
     * @return 结果
     */
    int deletePerformanceAppraisalByPerformanceAppraisalId(PerformanceAppraisalDTO performanceAppraisalDTO);


    /**
     * 删除绩效考核表信息
     *
     * @param performanceAppraisalId 绩效考核表主键
     * @return 结果
     */
    int deletePerformanceAppraisalByPerformanceAppraisalId(Long performanceAppraisalId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importPerformanceAppraisal(List<PerformanceAppraisalExcel> list);

    /**
     * 导出Excel
     *
     * @param performanceAppraisalDTO
     * @return
     */
    List<PerformanceAppraisalExcel> exportPerformanceAppraisal(PerformanceAppraisalDTO performanceAppraisalDTO);
}
