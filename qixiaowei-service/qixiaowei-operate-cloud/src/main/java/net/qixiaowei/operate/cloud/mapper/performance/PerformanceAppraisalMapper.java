package net.qixiaowei.operate.cloud.mapper.performance;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisal;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* PerformanceAppraisalMapper接口
* @author Graves
* @since 2022-11-24
*/
public interface PerformanceAppraisalMapper{
    /**
    * 查询绩效考核表
    *
    * @param performanceAppraisalId 绩效考核表主键
    * @return 绩效考核表
    */
    PerformanceAppraisalDTO selectPerformanceAppraisalByPerformanceAppraisalId(@Param("performanceAppraisalId")Long performanceAppraisalId);


    /**
    * 批量查询绩效考核表
    *
    * @param performanceAppraisalIds 绩效考核表主键集合
    * @return 绩效考核表
    */
    List<PerformanceAppraisalDTO> selectPerformanceAppraisalByPerformanceAppraisalIds(@Param("performanceAppraisalIds") List<Long> performanceAppraisalIds);

    /**
    * 查询绩效考核表列表
    *
    * @param performanceAppraisal 绩效考核表
    * @return 绩效考核表集合
    */
    List<PerformanceAppraisalDTO> selectPerformanceAppraisalList(@Param("performanceAppraisal")PerformanceAppraisal performanceAppraisal);

    /**
    * 新增绩效考核表
    *
    * @param performanceAppraisal 绩效考核表
    * @return 结果
    */
    int insertPerformanceAppraisal(@Param("performanceAppraisal")PerformanceAppraisal performanceAppraisal);

    /**
    * 修改绩效考核表
    *
    * @param performanceAppraisal 绩效考核表
    * @return 结果
    */
    int updatePerformanceAppraisal(@Param("performanceAppraisal")PerformanceAppraisal performanceAppraisal);

    /**
    * 批量修改绩效考核表
    *
    * @param performanceAppraisalList 绩效考核表
    * @return 结果
    */
    int updatePerformanceAppraisals(@Param("performanceAppraisalList")List<PerformanceAppraisal> performanceAppraisalList);
    /**
    * 逻辑删除绩效考核表
    *
    * @param performanceAppraisal
    * @return 结果
    */
    int logicDeletePerformanceAppraisalByPerformanceAppraisalId(@Param("performanceAppraisal")PerformanceAppraisal performanceAppraisal);

    /**
    * 逻辑批量删除绩效考核表
    *
    * @param performanceAppraisalIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeletePerformanceAppraisalByPerformanceAppraisalIds(@Param("performanceAppraisalIds")List<Long> performanceAppraisalIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除绩效考核表
    *
    * @param performanceAppraisalId 绩效考核表主键
    * @return 结果
    */
    int deletePerformanceAppraisalByPerformanceAppraisalId(@Param("performanceAppraisalId")Long performanceAppraisalId);

    /**
    * 物理批量删除绩效考核表
    *
    * @param performanceAppraisalIds 需要删除的数据主键集合
    * @return 结果
    */
    int deletePerformanceAppraisalByPerformanceAppraisalIds(@Param("performanceAppraisalIds")List<Long> performanceAppraisalIds);

    /**
    * 批量新增绩效考核表
    *
    * @param PerformanceAppraisals 绩效考核表列表
    * @return 结果
    */
    int batchPerformanceAppraisal(@Param("performanceAppraisals")List<PerformanceAppraisal> PerformanceAppraisals);
}