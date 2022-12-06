package net.qixiaowei.operate.cloud.mapper.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalItems;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalItemsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * PerformanceAppraisalItemsMapper接口
 *
 * @author Graves
 * @since 2022-12-06
 */
public interface PerformanceAppraisalItemsMapper {
    /**
     * 查询绩效考核项目表
     *
     * @param performAppraisalItemsId 绩效考核项目表主键
     * @return 绩效考核项目表
     */
    PerformanceAppraisalItemsDTO selectPerformanceAppraisalItemsByPerformAppraisalItemsId(@Param("performAppraisalItemsId") Long performAppraisalItemsId);


    /**
     * 批量查询绩效考核项目表
     *
     * @param performAppraisalItemsIds 绩效考核项目表主键集合
     * @return 绩效考核项目表
     */
    List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsByPerformAppraisalItemsIds(@Param("performAppraisalItemsIds") List<Long> performAppraisalItemsIds);

    /**
     * 查询绩效考核项目表列表
     *
     * @param performanceAppraisalItems 绩效考核项目表
     * @return 绩效考核项目表集合
     */
    List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsList(@Param("performanceAppraisalItems") PerformanceAppraisalItems performanceAppraisalItems);

    /**
     * 新增绩效考核项目表
     *
     * @param performanceAppraisalItems 绩效考核项目表
     * @return 结果
     */
    int insertPerformanceAppraisalItems(@Param("performanceAppraisalItems") PerformanceAppraisalItems performanceAppraisalItems);

    /**
     * 修改绩效考核项目表
     *
     * @param performanceAppraisalItems 绩效考核项目表
     * @return 结果
     */
    int updatePerformanceAppraisalItems(@Param("performanceAppraisalItems") PerformanceAppraisalItems performanceAppraisalItems);

    /**
     * 批量修改绩效考核项目表
     *
     * @param performanceAppraisalItemsList 绩效考核项目表
     * @return 结果
     */
    int updatePerformanceAppraisalItemss(@Param("performanceAppraisalItemsList") List<PerformanceAppraisalItems> performanceAppraisalItemsList);

    /**
     * 逻辑删除绩效考核项目表
     *
     * @param performanceAppraisalItems
     * @return 结果
     */
    int logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsId(@Param("performanceAppraisalItems") PerformanceAppraisalItems performanceAppraisalItems);

    /**
     * 逻辑批量删除绩效考核项目表
     *
     * @param performAppraisalItemsIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsIds(@Param("performAppraisalItemsIds") List<Long> performAppraisalItemsIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效考核项目表
     *
     * @param performAppraisalItemsId 绩效考核项目表主键
     * @return 结果
     */
    int deletePerformanceAppraisalItemsByPerformAppraisalItemsId(@Param("performAppraisalItemsId") Long performAppraisalItemsId);

    /**
     * 物理批量删除绩效考核项目表
     *
     * @param performAppraisalItemsIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformanceAppraisalItemsByPerformAppraisalItemsIds(@Param("performAppraisalItemsIds") List<Long> performAppraisalItemsIds);

    /**
     * 批量新增绩效考核项目表
     *
     * @param PerformanceAppraisalItemss 绩效考核项目表列表
     * @return 结果
     */
    int batchPerformanceAppraisalItems(@Param("performanceAppraisalItemss") List<PerformanceAppraisalItems> PerformanceAppraisalItemss);

    /**
     * 根据对象ID查找指标列表
     *
     * @param performAppraisalObjectsId 对象ID
     * @return
     */
    List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsByPerformAppraisalObjectId(@Param("performAppraisalObjectsId") Long performAppraisalObjectsId);
}
