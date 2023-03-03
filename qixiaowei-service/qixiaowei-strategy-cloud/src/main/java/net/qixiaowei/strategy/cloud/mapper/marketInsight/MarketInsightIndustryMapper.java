package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightIndustry;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MarketInsightIndustryMapper接口
* @author TANGMICHI
* @since 2023-03-03
*/
public interface MarketInsightIndustryMapper{
    /**
    * 查询市场洞察行业表
    *
    * @param marketInsightIndustryId 市场洞察行业表主键
    * @return 市场洞察行业表
    */
    MarketInsightIndustryDTO selectMarketInsightIndustryByMarketInsightIndustryId(@Param("marketInsightIndustryId")Long marketInsightIndustryId);


    /**
    * 批量查询市场洞察行业表
    *
    * @param marketInsightIndustryIds 市场洞察行业表主键集合
    * @return 市场洞察行业表
    */
    List<MarketInsightIndustryDTO> selectMarketInsightIndustryByMarketInsightIndustryIds(@Param("marketInsightIndustryIds") List<Long> marketInsightIndustryIds);

    /**
    * 查询市场洞察行业表列表
    *
    * @param marketInsightIndustry 市场洞察行业表
    * @return 市场洞察行业表集合
    */
    List<MarketInsightIndustryDTO> selectMarketInsightIndustryList(@Param("marketInsightIndustry")MarketInsightIndustry marketInsightIndustry);

    /**
    * 新增市场洞察行业表
    *
    * @param marketInsightIndustry 市场洞察行业表
    * @return 结果
    */
    int insertMarketInsightIndustry(@Param("marketInsightIndustry")MarketInsightIndustry marketInsightIndustry);

    /**
    * 修改市场洞察行业表
    *
    * @param marketInsightIndustry 市场洞察行业表
    * @return 结果
    */
    int updateMarketInsightIndustry(@Param("marketInsightIndustry")MarketInsightIndustry marketInsightIndustry);

    /**
    * 批量修改市场洞察行业表
    *
    * @param marketInsightIndustryList 市场洞察行业表
    * @return 结果
    */
    int updateMarketInsightIndustrys(@Param("marketInsightIndustryList")List<MarketInsightIndustry> marketInsightIndustryList);
    /**
    * 逻辑删除市场洞察行业表
    *
    * @param marketInsightIndustry
    * @return 结果
    */
    int logicDeleteMarketInsightIndustryByMarketInsightIndustryId(@Param("marketInsightIndustry")MarketInsightIndustry marketInsightIndustry);

    /**
    * 逻辑批量删除市场洞察行业表
    *
    * @param marketInsightIndustryIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMarketInsightIndustryByMarketInsightIndustryIds(@Param("marketInsightIndustryIds")List<Long> marketInsightIndustryIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察行业表
    *
    * @param marketInsightIndustryId 市场洞察行业表主键
    * @return 结果
    */
    int deleteMarketInsightIndustryByMarketInsightIndustryId(@Param("marketInsightIndustryId")Long marketInsightIndustryId);

    /**
    * 物理批量删除市场洞察行业表
    *
    * @param marketInsightIndustryIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMarketInsightIndustryByMarketInsightIndustryIds(@Param("marketInsightIndustryIds")List<Long> marketInsightIndustryIds);

    /**
    * 批量新增市场洞察行业表
    *
    * @param marketInsightIndustrys 市场洞察行业表列表
    * @return 结果
    */
    int batchMarketInsightIndustry(@Param("marketInsightIndustrys")List<MarketInsightIndustry> marketInsightIndustrys);
}
