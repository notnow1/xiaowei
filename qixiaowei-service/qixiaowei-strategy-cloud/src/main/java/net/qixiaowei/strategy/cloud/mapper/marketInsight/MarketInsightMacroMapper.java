package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightMacro;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MarketInsightMacroMapper接口
* @author TANGMICHI
* @since 2023-02-28
*/
public interface MarketInsightMacroMapper{
    /**
    * 查询市场洞察宏观表
    *
    * @param marketInsightMacroId 市场洞察宏观表主键
    * @return 市场洞察宏观表
    */
    MarketInsightMacroDTO selectMarketInsightMacroByMarketInsightMacroId(@Param("marketInsightMacroId")Long marketInsightMacroId);


    /**
    * 批量查询市场洞察宏观表
    *
    * @param marketInsightMacroIds 市场洞察宏观表主键集合
    * @return 市场洞察宏观表
    */
    List<MarketInsightMacroDTO> selectMarketInsightMacroByMarketInsightMacroIds(@Param("marketInsightMacroIds") List<Long> marketInsightMacroIds);

    /**
    * 查询市场洞察宏观表列表
    *
    * @param marketInsightMacro 市场洞察宏观表
    * @return 市场洞察宏观表集合
    */
    List<MarketInsightMacroDTO> selectMarketInsightMacroList(@Param("marketInsightMacro")MarketInsightMacro marketInsightMacro);

    /**
    * 新增市场洞察宏观表
    *
    * @param marketInsightMacro 市场洞察宏观表
    * @return 结果
    */
    int insertMarketInsightMacro(@Param("marketInsightMacro")MarketInsightMacro marketInsightMacro);

    /**
    * 修改市场洞察宏观表
    *
    * @param marketInsightMacro 市场洞察宏观表
    * @return 结果
    */
    int updateMarketInsightMacro(@Param("marketInsightMacro")MarketInsightMacro marketInsightMacro);

    /**
    * 批量修改市场洞察宏观表
    *
    * @param marketInsightMacroList 市场洞察宏观表
    * @return 结果
    */
    int updateMarketInsightMacros(@Param("marketInsightMacroList")List<MarketInsightMacro> marketInsightMacroList);
    /**
    * 逻辑删除市场洞察宏观表
    *
    * @param marketInsightMacro
    * @return 结果
    */
    int logicDeleteMarketInsightMacroByMarketInsightMacroId(@Param("marketInsightMacro")MarketInsightMacro marketInsightMacro);

    /**
    * 逻辑批量删除市场洞察宏观表
    *
    * @param marketInsightMacroIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMarketInsightMacroByMarketInsightMacroIds(@Param("marketInsightMacroIds")List<Long> marketInsightMacroIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察宏观表
    *
    * @param marketInsightMacroId 市场洞察宏观表主键
    * @return 结果
    */
    int deleteMarketInsightMacroByMarketInsightMacroId(@Param("marketInsightMacroId")Long marketInsightMacroId);

    /**
    * 物理批量删除市场洞察宏观表
    *
    * @param marketInsightMacroIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMarketInsightMacroByMarketInsightMacroIds(@Param("marketInsightMacroIds")List<Long> marketInsightMacroIds);

    /**
    * 批量新增市场洞察宏观表
    *
    * @param marketInsightMacros 市场洞察宏观表列表
    * @return 结果
    */
    int batchMarketInsightMacro(@Param("marketInsightMacros")List<MarketInsightMacro> marketInsightMacros);
}
