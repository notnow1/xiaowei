package net.qixiaowei.strategy.cloud.service.marketInsight;

import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiOpponentChoiceDTO;


import java.util.List;
import java.util.Map;


/**
* MarketInsightSelfService接口
* @author TANGMICHI
* @since 2023-03-13
*/
public interface IMarketInsightSelfService{
    /**
    * 查询市场洞察自身表
    *
    * @param marketInsightSelfId 市场洞察自身表主键
    * @return 市场洞察自身表
    */
    MarketInsightSelfDTO selectMarketInsightSelfByMarketInsightSelfId(Long marketInsightSelfId);

    /**
    * 查询市场洞察自身表列表
    *
    * @param marketInsightSelfDTO 市场洞察自身表
    * @return 市场洞察自身表集合
    */
    List<MarketInsightSelfDTO> selectMarketInsightSelfList(MarketInsightSelfDTO marketInsightSelfDTO);

    /**
    * 新增市场洞察自身表
    *
    * @param marketInsightSelfDTO 市场洞察自身表
    * @return 结果
    */
    MarketInsightSelfDTO insertMarketInsightSelf(MarketInsightSelfDTO marketInsightSelfDTO);

    /**
    * 修改市场洞察自身表
    *
    * @param marketInsightSelfDTO 市场洞察自身表
    * @return 结果
    */
    int updateMarketInsightSelf(MarketInsightSelfDTO marketInsightSelfDTO);


    /**
    * 逻辑批量删除市场洞察自身表
    *
    * @param marketInsightSelfIds 需要删除的市场洞察自身表集合
    * @return 结果
    */
    int logicDeleteMarketInsightSelfByMarketInsightSelfIds(List<Long> marketInsightSelfIds);

    /**
    * 逻辑删除市场洞察自身表信息
    *
    * @param marketInsightSelfDTO
    * @return 结果
    */
    int logicDeleteMarketInsightSelfByMarketInsightSelfId(MarketInsightSelfDTO marketInsightSelfDTO);
    /**
    * 批量删除市场洞察自身表
    *
    * @param MarketInsightSelfDtos
    * @return 结果
    */
    int deleteMarketInsightSelfByMarketInsightSelfIds(List<MarketInsightSelfDTO> MarketInsightSelfDtos);

    /**
    * 逻辑删除市场洞察自身表信息
    *
    * @param marketInsightSelfDTO
    * @return 结果
    */
    int deleteMarketInsightSelfByMarketInsightSelfId(MarketInsightSelfDTO marketInsightSelfDTO);


    /**
    * 删除市场洞察自身表信息
    *
    * @param marketInsightSelfId 市场洞察自身表主键
    * @return 结果
    */
    int deleteMarketInsightSelfByMarketInsightSelfId(Long marketInsightSelfId);

    /**
     * 根据规划年度和业务单元查询看对手详情表表
     * @param marketInsightSelfDTO
     * @return
     */
    Map<String, List<MiOpponentChoiceDTO>> opponentNameList(MarketInsightSelfDTO marketInsightSelfDTO);

    /**
     * 看自身远程调用列表查询是否被引用
     * @param marketInsightSelfDTO
     * @return
     */
    List<MarketInsightSelfDTO> remoteMarketInsightSelfList(MarketInsightSelfDTO marketInsightSelfDTO);
}
