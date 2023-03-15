package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightSelf;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiOpponentChoiceDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MarketInsightSelfMapper接口
* @author TANGMICHI
* @since 2023-03-13
*/
public interface MarketInsightSelfMapper{
    /**
    * 查询市场洞察自身表
    *
    * @param marketInsightSelfId 市场洞察自身表主键
    * @return 市场洞察自身表
    */
    MarketInsightSelfDTO selectMarketInsightSelfByMarketInsightSelfId(@Param("marketInsightSelfId")Long marketInsightSelfId);


    /**
    * 批量查询市场洞察自身表
    *
    * @param marketInsightSelfIds 市场洞察自身表主键集合
    * @return 市场洞察自身表
    */
    List<MarketInsightSelfDTO> selectMarketInsightSelfByMarketInsightSelfIds(@Param("marketInsightSelfIds") List<Long> marketInsightSelfIds);

    /**
    * 查询市场洞察自身表列表
    *
    * @param marketInsightSelf 市场洞察自身表
    * @return 市场洞察自身表集合
    */
    List<MarketInsightSelfDTO> selectMarketInsightSelfList(@Param("marketInsightSelf")MarketInsightSelf marketInsightSelf);

    /**
    * 新增市场洞察自身表
    *
    * @param marketInsightSelf 市场洞察自身表
    * @return 结果
    */
    int insertMarketInsightSelf(@Param("marketInsightSelf")MarketInsightSelf marketInsightSelf);

    /**
    * 修改市场洞察自身表
    *
    * @param marketInsightSelf 市场洞察自身表
    * @return 结果
    */
    int updateMarketInsightSelf(@Param("marketInsightSelf")MarketInsightSelf marketInsightSelf);

    /**
    * 批量修改市场洞察自身表
    *
    * @param marketInsightSelfList 市场洞察自身表
    * @return 结果
    */
    int updateMarketInsightSelfs(@Param("marketInsightSelfList")List<MarketInsightSelf> marketInsightSelfList);
    /**
    * 逻辑删除市场洞察自身表
    *
    * @param marketInsightSelf
    * @return 结果
    */
    int logicDeleteMarketInsightSelfByMarketInsightSelfId(@Param("marketInsightSelf")MarketInsightSelf marketInsightSelf);

    /**
    * 逻辑批量删除市场洞察自身表
    *
    * @param marketInsightSelfIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMarketInsightSelfByMarketInsightSelfIds(@Param("marketInsightSelfIds")List<Long> marketInsightSelfIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察自身表
    *
    * @param marketInsightSelfId 市场洞察自身表主键
    * @return 结果
    */
    int deleteMarketInsightSelfByMarketInsightSelfId(@Param("marketInsightSelfId")Long marketInsightSelfId);

    /**
    * 物理批量删除市场洞察自身表
    *
    * @param marketInsightSelfIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMarketInsightSelfByMarketInsightSelfIds(@Param("marketInsightSelfIds")List<Long> marketInsightSelfIds);

    /**
    * 批量新增市场洞察自身表
    *
    * @param marketInsightSelfs 市场洞察自身表列表
    * @return 结果
    */
    int batchMarketInsightSelf(@Param("marketInsightSelfs")List<MarketInsightSelf> marketInsightSelfs);

    /**
     * 根据规划年度和业务单元查询看对手详情表表
     * @param marketInsightSelf
     * @return
     */
    List<MiOpponentChoiceDTO> opponentNameList(MarketInsightSelf marketInsightSelf);
}
