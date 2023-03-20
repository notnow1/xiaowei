package net.qixiaowei.strategy.cloud.service.marketInsight;

import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightOpponentDTO;

import java.util.List;


/**
* MarketInsightOpponentService接口
* @author TANGMICHI
* @since 2023-03-12
*/
public interface IMarketInsightOpponentService{
    /**
    * 查询市场洞察对手表
    *
    * @param marketInsightOpponentId 市场洞察对手表主键
    * @return 市场洞察对手表
    */
    MarketInsightOpponentDTO selectMarketInsightOpponentByMarketInsightOpponentId(Long marketInsightOpponentId);

    /**
    * 查询市场洞察对手表列表
    *
    * @param marketInsightOpponentDTO 市场洞察对手表
    * @return 市场洞察对手表集合
    */
    List<MarketInsightOpponentDTO> selectMarketInsightOpponentList(MarketInsightOpponentDTO marketInsightOpponentDTO);

    /**
    * 新增市场洞察对手表
    *
    * @param marketInsightOpponentDTO 市场洞察对手表
    * @return 结果
    */
    MarketInsightOpponentDTO insertMarketInsightOpponent(MarketInsightOpponentDTO marketInsightOpponentDTO);

    /**
    * 修改市场洞察对手表
    *
    * @param marketInsightOpponentDTO 市场洞察对手表
    * @return 结果
    */
    int updateMarketInsightOpponent(MarketInsightOpponentDTO marketInsightOpponentDTO);


    /**
    * 逻辑批量删除市场洞察对手表
    *
    * @param marketInsightOpponentIds 需要删除的市场洞察对手表集合
    * @return 结果
    */
    int logicDeleteMarketInsightOpponentByMarketInsightOpponentIds(List<Long> marketInsightOpponentIds);

    /**
    * 逻辑删除市场洞察对手表信息
    *
    * @param marketInsightOpponentDTO
    * @return 结果
    */
    int logicDeleteMarketInsightOpponentByMarketInsightOpponentId(MarketInsightOpponentDTO marketInsightOpponentDTO);
    /**
    * 批量删除市场洞察对手表
    *
    * @param MarketInsightOpponentDtos
    * @return 结果
    */
    int deleteMarketInsightOpponentByMarketInsightOpponentIds(List<MarketInsightOpponentDTO> MarketInsightOpponentDtos);

    /**
    * 逻辑删除市场洞察对手表信息
    *
    * @param marketInsightOpponentDTO
    * @return 结果
    */
    int deleteMarketInsightOpponentByMarketInsightOpponentId(MarketInsightOpponentDTO marketInsightOpponentDTO);


    /**
    * 删除市场洞察对手表信息
    *
    * @param marketInsightOpponentId 市场洞察对手表主键
    * @return 结果
    */
    int deleteMarketInsightOpponentByMarketInsightOpponentId(Long marketInsightOpponentId);

    /**
     * 看对手远程查询列表是否被引用
     * @param marketInsightOpponentDTO
     * @return
     */
    List<MarketInsightOpponentDTO> remoteMarketInsightOpponentList(MarketInsightOpponentDTO marketInsightOpponentDTO);
}
