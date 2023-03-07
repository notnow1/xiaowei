package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMetricsDetail;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMetricsDetailDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* StrategyMetricsDetailMapper接口
* @author Graves
* @since 2023-03-07
*/
public interface StrategyMetricsDetailMapper{
    /**
    * 查询战略衡量指标详情表
    *
    * @param strategyMetricsDetailId 战略衡量指标详情表主键
    * @return 战略衡量指标详情表
    */
    StrategyMetricsDetailDTO selectStrategyMetricsDetailByStrategyMetricsDetailId(@Param("strategyMetricsDetailId")Long strategyMetricsDetailId);


    /**
    * 批量查询战略衡量指标详情表
    *
    * @param strategyMetricsDetailIds 战略衡量指标详情表主键集合
    * @return 战略衡量指标详情表
    */
    List<StrategyMetricsDetailDTO> selectStrategyMetricsDetailByStrategyMetricsDetailIds(@Param("strategyMetricsDetailIds") List<Long> strategyMetricsDetailIds);

    /**
    * 查询战略衡量指标详情表列表
    *
    * @param strategyMetricsDetail 战略衡量指标详情表
    * @return 战略衡量指标详情表集合
    */
    List<StrategyMetricsDetailDTO> selectStrategyMetricsDetailList(@Param("strategyMetricsDetail")StrategyMetricsDetail strategyMetricsDetail);

    /**
    * 新增战略衡量指标详情表
    *
    * @param strategyMetricsDetail 战略衡量指标详情表
    * @return 结果
    */
    int insertStrategyMetricsDetail(@Param("strategyMetricsDetail")StrategyMetricsDetail strategyMetricsDetail);

    /**
    * 修改战略衡量指标详情表
    *
    * @param strategyMetricsDetail 战略衡量指标详情表
    * @return 结果
    */
    int updateStrategyMetricsDetail(@Param("strategyMetricsDetail")StrategyMetricsDetail strategyMetricsDetail);

    /**
    * 批量修改战略衡量指标详情表
    *
    * @param strategyMetricsDetailList 战略衡量指标详情表
    * @return 结果
    */
    int updateStrategyMetricsDetails(@Param("strategyMetricsDetailList")List<StrategyMetricsDetail> strategyMetricsDetailList);
    /**
    * 逻辑删除战略衡量指标详情表
    *
    * @param strategyMetricsDetail
    * @return 结果
    */
    int logicDeleteStrategyMetricsDetailByStrategyMetricsDetailId(@Param("strategyMetricsDetail")StrategyMetricsDetail strategyMetricsDetail);

    /**
    * 逻辑批量删除战略衡量指标详情表
    *
    * @param strategyMetricsDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteStrategyMetricsDetailByStrategyMetricsDetailIds(@Param("strategyMetricsDetailIds")List<Long> strategyMetricsDetailIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除战略衡量指标详情表
    *
    * @param strategyMetricsDetailId 战略衡量指标详情表主键
    * @return 结果
    */
    int deleteStrategyMetricsDetailByStrategyMetricsDetailId(@Param("strategyMetricsDetailId")Long strategyMetricsDetailId);

    /**
    * 物理批量删除战略衡量指标详情表
    *
    * @param strategyMetricsDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteStrategyMetricsDetailByStrategyMetricsDetailIds(@Param("strategyMetricsDetailIds")List<Long> strategyMetricsDetailIds);

    /**
    * 批量新增战略衡量指标详情表
    *
    * @param strategyMetricsDetails 战略衡量指标详情表列表
    * @return 结果
    */
    int batchStrategyMetricsDetail(@Param("strategyMetricsDetails")List<StrategyMetricsDetail> strategyMetricsDetails);
}
