package net.qixiaowei.strategy.cloud.service.marketInsight;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerInvestDetailDTO;


import java.util.List;


/**
* MarketInsightCustomerService接口
* @author TANGMICHI
* @since 2023-03-07
*/
public interface IMarketInsightCustomerService{
    /**
    * 查询市场洞察客户表
    *
    * @param marketInsightCustomerId 市场洞察客户表主键
    * @return 市场洞察客户表
    */
    MarketInsightCustomerDTO selectMarketInsightCustomerByMarketInsightCustomerId(Long marketInsightCustomerId);

    /**
    * 查询市场洞察客户表列表
    *
    * @param marketInsightCustomerDTO 市场洞察客户表
    * @return 市场洞察客户表集合
    */
    List<MarketInsightCustomerDTO> selectMarketInsightCustomerList(MarketInsightCustomerDTO marketInsightCustomerDTO);

    /**
    * 新增市场洞察客户表
    *
    * @param marketInsightCustomerDTO 市场洞察客户表
    * @return 结果
    */
    MarketInsightCustomerDTO insertMarketInsightCustomer(MarketInsightCustomerDTO marketInsightCustomerDTO);

    /**
    * 修改市场洞察客户表
    *
    * @param marketInsightCustomerDTO 市场洞察客户表
    * @return 结果
    */
    int updateMarketInsightCustomer(MarketInsightCustomerDTO marketInsightCustomerDTO);


    /**
    * 逻辑批量删除市场洞察客户表
    *
    * @param marketInsightCustomerIds 需要删除的市场洞察客户表集合
    * @return 结果
    */
    int logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(List<Long> marketInsightCustomerIds);

    /**
    * 逻辑删除市场洞察客户表信息
    *
    * @param marketInsightCustomerDTO
    * @return 结果
    */
    int logicDeleteMarketInsightCustomerByMarketInsightCustomerId(MarketInsightCustomerDTO marketInsightCustomerDTO);
    /**
    * 批量删除市场洞察客户表
    *
    * @param MarketInsightCustomerDtos
    * @return 结果
    */
    int deleteMarketInsightCustomerByMarketInsightCustomerIds(List<MarketInsightCustomerDTO> MarketInsightCustomerDtos);

    /**
    * 逻辑删除市场洞察客户表信息
    *
    * @param marketInsightCustomerDTO
    * @return 结果
    */
    int deleteMarketInsightCustomerByMarketInsightCustomerId(MarketInsightCustomerDTO marketInsightCustomerDTO);


    /**
    * 删除市场洞察客户表信息
    *
    * @param marketInsightCustomerId 市场洞察客户表主键
    * @return 结果
    */
    int deleteMarketInsightCustomerByMarketInsightCustomerId(Long marketInsightCustomerId);

    /**
     * 远程查询看客户是否被引用
     * @param marketInsightCustomerDTO
     * @return
     */
    List<MarketInsightCustomerDTO> remoteMarketInsightCustomerList(MarketInsightCustomerDTO marketInsightCustomerDTO);

    /**
     * 远程查询看客户投资计划详情是否被引用
     * @param miCustomerInvestDetailDTO
     * @return
     */
    List<MiCustomerInvestDetailDTO> remoteMiCustomerInvestDetailList(MiCustomerInvestDetailDTO miCustomerInvestDetailDTO);
}
