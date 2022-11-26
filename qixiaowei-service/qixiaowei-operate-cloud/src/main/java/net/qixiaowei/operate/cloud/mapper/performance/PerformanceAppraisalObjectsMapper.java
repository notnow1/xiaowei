package net.qixiaowei.operate.cloud.mapper.performance;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalObjects;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * PerformanceAppraisalObjectsMapper接口
 *
 * @author Graves
 * @since 2022-11-24
 */
public interface PerformanceAppraisalObjectsMapper {
    /**
     * 查询绩效考核对象表
     *
     * @param performAppraisalObjectsId 绩效考核对象表主键
     * @return 绩效考核对象表
     */
    PerformanceAppraisalObjectsDTO selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(@Param("performAppraisalObjectsId") Long performAppraisalObjectsId);


    /**
     * 批量查询绩效考核对象表
     *
     * @param performAppraisalObjectsIds 绩效考核对象表主键集合
     * @return 绩效考核对象表
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalObjectsIds(@Param("performAppraisalObjectsIds") List<Long> performAppraisalObjectsIds);

    /**
     * 查询绩效考核对象表列表
     *
     * @param performanceAppraisalObjects 绩效考核对象表
     * @return 绩效考核对象表集合
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsList(@Param("performanceAppraisalObjects") PerformanceAppraisalObjects performanceAppraisalObjects);

    /**
     * 新增绩效考核对象表
     *
     * @param performanceAppraisalObjects 绩效考核对象表
     * @return 结果
     */
    int insertPerformanceAppraisalObjects(@Param("performanceAppraisalObjects") PerformanceAppraisalObjects performanceAppraisalObjects);

    /**
     * 修改绩效考核对象表
     *
     * @param performanceAppraisalObjects 绩效考核对象表
     * @return 结果
     */
    int updatePerformanceAppraisalObjects(@Param("performanceAppraisalObjects") PerformanceAppraisalObjects performanceAppraisalObjects);

    /**
     * 批量修改绩效考核对象表
     *
     * @param performanceAppraisalObjectsList 绩效考核对象表
     * @return 结果
     */
    int updatePerformanceAppraisalObjectss(@Param("performanceAppraisalObjectsList") List<PerformanceAppraisalObjects> performanceAppraisalObjectsList);

    /**
     * 逻辑删除绩效考核对象表
     *
     * @param performanceAppraisalObjects
     * @return 结果
     */
    int logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(@Param("performanceAppraisalObjects") PerformanceAppraisalObjects performanceAppraisalObjects);

    /**
     * 逻辑批量删除绩效考核对象表
     *
     * @param performAppraisalObjectsIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(@Param("performAppraisalObjectsIds") List<Long> performAppraisalObjectsIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效考核对象表
     *
     * @param performAppraisalObjectsId 绩效考核对象表主键
     * @return 结果
     */
    int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(@Param("performAppraisalObjectsId") Long performAppraisalObjectsId);

    /**
     * 物理批量删除绩效考核对象表
     *
     * @param performAppraisalObjectsIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(@Param("performAppraisalObjectsIds") List<Long> performAppraisalObjectsIds);

    /**
     * 批量新增绩效考核对象表
     *
     * @param PerformanceAppraisalObjectss 绩效考核对象表列表
     * @return 结果
     */
    int batchPerformanceAppraisalObjects(@Param("performanceAppraisalObjectss") List<PerformanceAppraisalObjects> PerformanceAppraisalObjectss);

    /**
     * 查找根据任务ID对象列表
     *
     * @param performanceAppraisalId
     * @return
     */
    List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalId(@Param("performanceAppraisalId") Long performanceAppraisalId);
}
