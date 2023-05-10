package net.qixiaowei.operate.cloud.mapper.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformAppraisalEvaluate;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformAppraisalEvaluateDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * PerformAppraisalEvaluateMapper接口
 *
 * @author Graves
 * @since 2023-03-23
 */
public interface PerformAppraisalEvaluateMapper {
    /**
     * 查询绩效考核评议表
     *
     * @param performAppraisalEvaluateId 绩效考核评议表主键
     * @return 绩效考核评议表
     */
    PerformAppraisalEvaluateDTO selectPerformAppraisalEvaluateByPerformAppraisalEvaluateId(@Param("performAppraisalEvaluateId") Long performAppraisalEvaluateId);


    /**
     * 批量查询绩效考核评议表
     *
     * @param performAppraisalEvaluateIds 绩效考核评议表主键集合
     * @return 绩效考核评议表
     */
    List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateByPerformAppraisalEvaluateIds(@Param("performAppraisalEvaluateIds") List<Long> performAppraisalEvaluateIds);

    /**
     * 查询绩效考核评议表列表
     *
     * @param performAppraisalEvaluate 绩效考核评议表
     * @return 绩效考核评议表集合
     */
    List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateList(@Param("performAppraisalEvaluate") PerformAppraisalEvaluate performAppraisalEvaluate);

    /**
     * 新增绩效考核评议表
     *
     * @param performAppraisalEvaluate 绩效考核评议表
     * @return 结果
     */
    int insertPerformAppraisalEvaluate(@Param("performAppraisalEvaluate") PerformAppraisalEvaluate performAppraisalEvaluate);

    /**
     * 修改绩效考核评议表
     *
     * @param performAppraisalEvaluate 绩效考核评议表
     * @return 结果
     */
    int updatePerformAppraisalEvaluate(@Param("performAppraisalEvaluate") PerformAppraisalEvaluate performAppraisalEvaluate);

    /**
     * 批量修改绩效考核评议表
     *
     * @param performAppraisalEvaluateList 绩效考核评议表
     * @return 结果
     */
    int updatePerformAppraisalEvaluates(@Param("performAppraisalEvaluateList") List<PerformAppraisalEvaluate> performAppraisalEvaluateList);

    /**
     * 逻辑删除绩效考核评议表
     *
     * @param performAppraisalEvaluate
     * @return 结果
     */
    int logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(@Param("performAppraisalEvaluate") PerformAppraisalEvaluate performAppraisalEvaluate);

    /**
     * 逻辑批量删除绩效考核评议表
     *
     * @param performAppraisalEvaluateIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(@Param("performAppraisalEvaluateIds") List<Long> performAppraisalEvaluateIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效考核评议表
     *
     * @param performAppraisalEvaluateId 绩效考核评议表主键
     * @return 结果
     */
    int deletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(@Param("performAppraisalEvaluateId") Long performAppraisalEvaluateId);

    /**
     * 物理批量删除绩效考核评议表
     *
     * @param performAppraisalEvaluateIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(@Param("performAppraisalEvaluateIds") List<Long> performAppraisalEvaluateIds);

    /**
     * 批量新增绩效考核评议表
     *
     * @param performAppraisalEvaluates 绩效考核评议表列表
     * @return 结果
     */
    int batchPerformAppraisalEvaluate(@Param("performAppraisalEvaluates") List<PerformAppraisalEvaluate> performAppraisalEvaluates);

    /**
     * 根据指标ID集合查询评议列表
     *
     * @param delPerformanceAppraisalItemIds 指标ID集合
     * @return List
     */
    List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateByPerformAppraisalItemIds(@Param("delPerformanceAppraisalItemIds") List<Long> delPerformanceAppraisalItemIds);

    /**
     * 根据对象id获取评议周期列表
     *
     * @param performAppraisalObjectsId 对象id
     * @return 结果
     */
    List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateByPerformAppraisalObjectId(@Param("performAppraisalObjectsId") Long performAppraisalObjectsId);
}
