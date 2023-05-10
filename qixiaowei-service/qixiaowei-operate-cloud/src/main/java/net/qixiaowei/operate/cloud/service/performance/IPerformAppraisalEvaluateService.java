package net.qixiaowei.operate.cloud.service.performance;

import net.qixiaowei.operate.cloud.api.dto.performance.PerformAppraisalEvaluateDTO;

import java.util.List;


/**
 * PerformAppraisalEvaluateService接口
 *
 * @author Graves
 * @since 2023-03-23
 */
public interface IPerformAppraisalEvaluateService {
    /**
     * 查询绩效考核评议表
     *
     * @param performAppraisalEvaluateId 绩效考核评议表主键
     * @return 绩效考核评议表
     */
    PerformAppraisalEvaluateDTO selectPerformAppraisalEvaluateByPerformAppraisalEvaluateId(Long performAppraisalEvaluateId);

    /**
     * 查询绩效考核评议表列表
     *
     * @param performAppraisalEvaluateDTO 绩效考核评议表
     * @return 绩效考核评议表集合
     */
    List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateList(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO);

    /**
     * 新增绩效考核评议表
     *
     * @param performAppraisalEvaluateDTO 绩效考核评议表
     * @return 结果
     */
    PerformAppraisalEvaluateDTO insertPerformAppraisalEvaluate(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO);

    /**
     * 修改绩效考核评议表
     *
     * @param performAppraisalEvaluateDTO 绩效考核评议表
     * @return 结果
     */
    int updatePerformAppraisalEvaluate(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO);

    /**
     * 批量修改绩效考核评议表
     *
     * @param performAppraisalEvaluateDtos 绩效考核评议表
     * @return 结果
     */
    int updatePerformAppraisalEvaluates(List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDtos);

    /**
     * 批量新增绩效考核评议表
     *
     * @param performAppraisalEvaluateDtos 绩效考核评议表
     * @return 结果
     */
    int insertPerformAppraisalEvaluates(List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDtos);

    /**
     * 逻辑批量删除绩效考核评议表
     *
     * @param performAppraisalEvaluateIds 需要删除的绩效考核评议表集合
     * @return 结果
     */
    int logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(List<Long> performAppraisalEvaluateIds);

    /**
     * 逻辑删除绩效考核评议表信息
     *
     * @param performAppraisalEvaluateDTO
     * @return 结果
     */
    int logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO);

    /**
     * 批量删除绩效考核评议表
     *
     * @param PerformAppraisalEvaluateDtos
     * @return 结果
     */
    int deletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(List<PerformAppraisalEvaluateDTO> PerformAppraisalEvaluateDtos);

    /**
     * 逻辑删除绩效考核评议表信息
     *
     * @param performAppraisalEvaluateDTO
     * @return 结果
     */
    int deletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO);


    /**
     * 删除绩效考核评议表信息
     *
     * @param performAppraisalEvaluateId 绩效考核评议表主键
     * @return 结果
     */
    int deletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(Long performAppraisalEvaluateId);

    /**
     * 根据指标ID集合查询评议列表
     *
     * @param delPerformanceAppraisalItemIds 指标ID集合
     * @return List
     */
    List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateByPerformAppraisalItemIds(List<Long> delPerformanceAppraisalItemIds);

    /**
     * 根据对象id获取评议周期列表
     *
     * @param performAppraisalObjectsId 对象id
     * @return 结果
     */
    List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateByPerformAppraisalObjectId(Long performAppraisalObjectsId);
}
