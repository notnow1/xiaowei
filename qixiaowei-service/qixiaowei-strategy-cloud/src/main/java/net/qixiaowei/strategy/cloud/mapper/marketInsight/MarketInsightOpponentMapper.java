package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightOpponent;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightOpponentDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MarketInsightOpponentMapper接口
* @author TANGMICHI
* @since 2023-03-12
*/
public interface MarketInsightOpponentMapper{
    /**
    * 查询市场洞察对手表
    *
    * @param marketInsightOpponentId 市场洞察对手表主键
    * @return 市场洞察对手表
    */
    MarketInsightOpponentDTO selectMarketInsightOpponentByMarketInsightOpponentId(@Param("marketInsightOpponentId")Long marketInsightOpponentId);


    /**
    * 批量查询市场洞察对手表
    *
    * @param marketInsightOpponentIds 市场洞察对手表主键集合
    * @return 市场洞察对手表
    */
    List<MarketInsightOpponentDTO> selectMarketInsightOpponentByMarketInsightOpponentIds(@Param("marketInsightOpponentIds") List<Long> marketInsightOpponentIds);

    /**
    * 查询市场洞察对手表列表
    *
    * @param marketInsightOpponent 市场洞察对手表
    * @return 市场洞察对手表集合
    */
    List<MarketInsightOpponentDTO> selectMarketInsightOpponentList(@Param("marketInsightOpponent")MarketInsightOpponent marketInsightOpponent);

    /**
    * 新增市场洞察对手表
    *
    * @param marketInsightOpponent 市场洞察对手表
    * @return 结果
    */
    int insertMarketInsightOpponent(@Param("marketInsightOpponent")MarketInsightOpponent marketInsightOpponent);

    /**
    * 修改市场洞察对手表
    *
    * @param marketInsightOpponent 市场洞察对手表
    * @return 结果
    */
    int updateMarketInsightOpponent(@Param("marketInsightOpponent")MarketInsightOpponent marketInsightOpponent);

    /**
    * 批量修改市场洞察对手表
    *
    * @param marketInsightOpponentList 市场洞察对手表
    * @return 结果
    */
    int updateMarketInsightOpponents(@Param("marketInsightOpponentList")List<MarketInsightOpponent> marketInsightOpponentList);
    /**
    * 逻辑删除市场洞察对手表
    *
    * @param marketInsightOpponent
    * @return 结果
    */
    int logicDeleteMarketInsightOpponentByMarketInsightOpponentId(@Param("marketInsightOpponent")MarketInsightOpponent marketInsightOpponent);

    /**
    * 逻辑批量删除市场洞察对手表
    *
    * @param marketInsightOpponentIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMarketInsightOpponentByMarketInsightOpponentIds(@Param("marketInsightOpponentIds")List<Long> marketInsightOpponentIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察对手表
    *
    * @param marketInsightOpponentId 市场洞察对手表主键
    * @return 结果
    */
    int deleteMarketInsightOpponentByMarketInsightOpponentId(@Param("marketInsightOpponentId")Long marketInsightOpponentId);

    /**
    * 物理批量删除市场洞察对手表
    *
    * @param marketInsightOpponentIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMarketInsightOpponentByMarketInsightOpponentIds(@Param("marketInsightOpponentIds")List<Long> marketInsightOpponentIds);

    /**
    * 批量新增市场洞察对手表
    *
    * @param marketInsightOpponents 市场洞察对手表列表
    * @return 结果
    */
    int batchMarketInsightOpponent(@Param("marketInsightOpponents")List<MarketInsightOpponent> marketInsightOpponents);
}
