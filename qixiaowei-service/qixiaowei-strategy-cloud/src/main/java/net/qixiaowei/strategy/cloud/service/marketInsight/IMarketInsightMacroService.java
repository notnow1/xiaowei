package net.qixiaowei.strategy.cloud.service.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.excel.marketInsight.MarketInsightMacroExcel;


/**
* MarketInsightMacroService接口
* @author TANGMICHI
* @since 2023-02-28
*/
public interface IMarketInsightMacroService{
    /**
    * 查询市场洞察宏观表
    *
    * @param marketInsightMacroId 市场洞察宏观表主键
    * @return 市场洞察宏观表
    */
    MarketInsightMacroDTO selectMarketInsightMacroByMarketInsightMacroId(Long marketInsightMacroId);

    /**
    * 查询市场洞察宏观表列表
    *
    * @param marketInsightMacroDTO 市场洞察宏观表
    * @return 市场洞察宏观表集合
    */
    List<MarketInsightMacroDTO> selectMarketInsightMacroList(MarketInsightMacroDTO marketInsightMacroDTO);

    /**
    * 新增市场洞察宏观表
    *
    * @param marketInsightMacroDTO 市场洞察宏观表
    * @return 结果
    */
    MarketInsightMacroDTO insertMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO);

    /**
    * 修改市场洞察宏观表
    *
    * @param marketInsightMacroDTO 市场洞察宏观表
    * @return 结果
    */
    int updateMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO);


    /**
    * 逻辑批量删除市场洞察宏观表
    *
    * @param marketInsightMacroIds 需要删除的市场洞察宏观表集合
    * @return 结果
    */
    int logicDeleteMarketInsightMacroByMarketInsightMacroIds(List<Long> marketInsightMacroIds);

    /**
    * 逻辑删除市场洞察宏观表信息
    *
    * @param marketInsightMacroDTO
    * @return 结果
    */
    int logicDeleteMarketInsightMacroByMarketInsightMacroId(MarketInsightMacroDTO marketInsightMacroDTO);
    /**
    * 批量删除市场洞察宏观表
    *
    * @param MarketInsightMacroDtos
    * @return 结果
    */
    int deleteMarketInsightMacroByMarketInsightMacroIds(List<MarketInsightMacroDTO> MarketInsightMacroDtos);

    /**
    * 逻辑删除市场洞察宏观表信息
    *
    * @param marketInsightMacroDTO
    * @return 结果
    */
    int deleteMarketInsightMacroByMarketInsightMacroId(MarketInsightMacroDTO marketInsightMacroDTO);


    /**
    * 删除市场洞察宏观表信息
    *
    * @param marketInsightMacroId 市场洞察宏观表主键
    * @return 结果
    */
    int deleteMarketInsightMacroByMarketInsightMacroId(Long marketInsightMacroId);

    /**
    * 导出Excel
    * @param marketInsightMacroDTO
    * @return
    */
    List<MarketInsightMacroExcel> exportMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO);
}
