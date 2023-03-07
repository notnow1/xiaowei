package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.StrategyMeasureDetail;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDetailDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* StrategyMeasureDetailMapper接口
* @author Graves
* @since 2023-03-07
*/
public interface StrategyMeasureDetailMapper{
    /**
    * 查询战略举措清单详情表
    *
    * @param strategyMeasureDetailId 战略举措清单详情表主键
    * @return 战略举措清单详情表
    */
    StrategyMeasureDetailDTO selectStrategyMeasureDetailByStrategyMeasureDetailId(@Param("strategyMeasureDetailId")Long strategyMeasureDetailId);


    /**
    * 批量查询战略举措清单详情表
    *
    * @param strategyMeasureDetailIds 战略举措清单详情表主键集合
    * @return 战略举措清单详情表
    */
    List<StrategyMeasureDetailDTO> selectStrategyMeasureDetailByStrategyMeasureDetailIds(@Param("strategyMeasureDetailIds") List<Long> strategyMeasureDetailIds);

    /**
    * 查询战略举措清单详情表列表
    *
    * @param strategyMeasureDetail 战略举措清单详情表
    * @return 战略举措清单详情表集合
    */
    List<StrategyMeasureDetailDTO> selectStrategyMeasureDetailList(@Param("strategyMeasureDetail")StrategyMeasureDetail strategyMeasureDetail);

    /**
    * 新增战略举措清单详情表
    *
    * @param strategyMeasureDetail 战略举措清单详情表
    * @return 结果
    */
    int insertStrategyMeasureDetail(@Param("strategyMeasureDetail")StrategyMeasureDetail strategyMeasureDetail);

    /**
    * 修改战略举措清单详情表
    *
    * @param strategyMeasureDetail 战略举措清单详情表
    * @return 结果
    */
    int updateStrategyMeasureDetail(@Param("strategyMeasureDetail")StrategyMeasureDetail strategyMeasureDetail);

    /**
    * 批量修改战略举措清单详情表
    *
    * @param strategyMeasureDetailList 战略举措清单详情表
    * @return 结果
    */
    int updateStrategyMeasureDetails(@Param("strategyMeasureDetailList")List<StrategyMeasureDetail> strategyMeasureDetailList);
    /**
    * 逻辑删除战略举措清单详情表
    *
    * @param strategyMeasureDetail
    * @return 结果
    */
    int logicDeleteStrategyMeasureDetailByStrategyMeasureDetailId(@Param("strategyMeasureDetail")StrategyMeasureDetail strategyMeasureDetail);

    /**
    * 逻辑批量删除战略举措清单详情表
    *
    * @param strategyMeasureDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteStrategyMeasureDetailByStrategyMeasureDetailIds(@Param("strategyMeasureDetailIds")List<Long> strategyMeasureDetailIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除战略举措清单详情表
    *
    * @param strategyMeasureDetailId 战略举措清单详情表主键
    * @return 结果
    */
    int deleteStrategyMeasureDetailByStrategyMeasureDetailId(@Param("strategyMeasureDetailId")Long strategyMeasureDetailId);

    /**
    * 物理批量删除战略举措清单详情表
    *
    * @param strategyMeasureDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteStrategyMeasureDetailByStrategyMeasureDetailIds(@Param("strategyMeasureDetailIds")List<Long> strategyMeasureDetailIds);

    /**
    * 批量新增战略举措清单详情表
    *
    * @param strategyMeasureDetails 战略举措清单详情表列表
    * @return 结果
    */
    int batchStrategyMeasureDetail(@Param("strategyMeasureDetails")List<StrategyMeasureDetail> strategyMeasureDetails);
}
