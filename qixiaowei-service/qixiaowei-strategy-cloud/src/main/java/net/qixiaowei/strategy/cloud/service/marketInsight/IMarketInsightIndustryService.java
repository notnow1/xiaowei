package net.qixiaowei.strategy.cloud.service.marketInsight;

import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightIndustryDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiIndustryDetailDTO;


import java.util.List;


/**
* MarketInsightIndustryService接口
* @author TANGMICHI
* @since 2023-03-03
*/
public interface IMarketInsightIndustryService{
    /**
    * 查询市场洞察行业表
    *
    * @param marketInsightIndustryId 市场洞察行业表主键
    * @return 市场洞察行业表
    */
    MarketInsightIndustryDTO selectMarketInsightIndustryByMarketInsightIndustryId(Long marketInsightIndustryId);

    /**
    * 查询市场洞察行业表列表
    *
    * @param marketInsightIndustryDTO 市场洞察行业表
    * @return 市场洞察行业表集合
    */
    List<MarketInsightIndustryDTO> selectMarketInsightIndustryList(MarketInsightIndustryDTO marketInsightIndustryDTO);

    /**
    * 新增市场洞察行业表
    *
    * @param marketInsightIndustryDTO 市场洞察行业表
    * @return 结果
    */
    MarketInsightIndustryDTO insertMarketInsightIndustry(MarketInsightIndustryDTO marketInsightIndustryDTO);

    /**
    * 修改市场洞察行业表
    *
    * @param marketInsightIndustryDTO 市场洞察行业表
    * @return 结果
    */
    int updateMarketInsightIndustry(MarketInsightIndustryDTO marketInsightIndustryDTO);



    /**
    * 逻辑批量删除市场洞察行业表
    *
    * @param marketInsightIndustryIds 需要删除的市场洞察行业表集合
    * @return 结果
    */
    int logicDeleteMarketInsightIndustryByMarketInsightIndustryIds(List<Long> marketInsightIndustryIds);

    /**
    * 逻辑删除市场洞察行业表信息
    *
    * @param marketInsightIndustryDTO
    * @return 结果
    */
    int logicDeleteMarketInsightIndustryByMarketInsightIndustryId(MarketInsightIndustryDTO marketInsightIndustryDTO);
    /**
    * 批量删除市场洞察行业表
    *
    * @param MarketInsightIndustryDtos
    * @return 结果
    */
    int deleteMarketInsightIndustryByMarketInsightIndustryIds(List<MarketInsightIndustryDTO> MarketInsightIndustryDtos);

    /**
    * 逻辑删除市场洞察行业表信息
    *
    * @param marketInsightIndustryDTO
    * @return 结果
    */
    int deleteMarketInsightIndustryByMarketInsightIndustryId(MarketInsightIndustryDTO marketInsightIndustryDTO);


    /**
    * 删除市场洞察行业表信息
    *
    * @param marketInsightIndustryId 市场洞察行业表主键
    * @return 结果
    */
    int deleteMarketInsightIndustryByMarketInsightIndustryId(Long marketInsightIndustryId);

    /**
     * 预制表头查询行业吸引力表列表
     * @param industryAttractionDTO
     * @return
     */
    List<IndustryAttractionDTO> selectPreIndustryAttractionList(IndustryAttractionDTO industryAttractionDTO);

    /**
     * 远程查询看行业列表是否被引用
     * @param marketInsightIndustryDTO
     * @return
     */
    List<MarketInsightIndustryDTO> remoteMarketInsightCustomerList(MarketInsightIndustryDTO marketInsightIndustryDTO);

    /**
     * 远程查询市场洞察行业详情是否被引用
     * @param miIndustryDetailDTO
     * @return
     */
    List<MiIndustryDetailDTO> remoteMiIndustryDetailList(MiIndustryDetailDTO miIndustryDetailDTO);
}
