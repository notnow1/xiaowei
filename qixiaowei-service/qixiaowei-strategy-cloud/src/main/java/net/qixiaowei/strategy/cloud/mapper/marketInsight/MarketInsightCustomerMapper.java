package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightCustomer;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MarketInsightCustomerMapper接口
* @author TANGMICHI
* @since 2023-03-07
*/
public interface MarketInsightCustomerMapper{
    /**
    * 查询市场洞察客户表
    *
    * @param marketInsightCustomerId 市场洞察客户表主键
    * @return 市场洞察客户表
    */
    MarketInsightCustomerDTO selectMarketInsightCustomerByMarketInsightCustomerId(@Param("marketInsightCustomerId")Long marketInsightCustomerId);


    /**
    * 批量查询市场洞察客户表
    *
    * @param marketInsightCustomerIds 市场洞察客户表主键集合
    * @return 市场洞察客户表
    */
    List<MarketInsightCustomerDTO> selectMarketInsightCustomerByMarketInsightCustomerIds(@Param("marketInsightCustomerIds") List<Long> marketInsightCustomerIds);

    /**
     * 根据规划业务单元集合批量查询市场洞察客户表
     *
     * @param planBusinessUnitIds 规划业务单元集合
     * @return 市场洞察客户表
     */
    List<MarketInsightCustomerDTO> selectMarketInsightCustomerByPlanBusinessUnitIds(@Param("planBusinessUnitIds") List<Long> planBusinessUnitIds);

    /**
    * 查询市场洞察客户表列表
    *
    * @param marketInsightCustomer 市场洞察客户表
    * @return 市场洞察客户表集合
    */
    List<MarketInsightCustomerDTO> selectMarketInsightCustomerList(@Param("marketInsightCustomer")MarketInsightCustomer marketInsightCustomer);

    /**
    * 新增市场洞察客户表
    *
    * @param marketInsightCustomer 市场洞察客户表
    * @return 结果
    */
    int insertMarketInsightCustomer(@Param("marketInsightCustomer")MarketInsightCustomer marketInsightCustomer);

    /**
    * 修改市场洞察客户表
    *
    * @param marketInsightCustomer 市场洞察客户表
    * @return 结果
    */
    int updateMarketInsightCustomer(@Param("marketInsightCustomer")MarketInsightCustomer marketInsightCustomer);

    /**
    * 批量修改市场洞察客户表
    *
    * @param marketInsightCustomerList 市场洞察客户表
    * @return 结果
    */
    int updateMarketInsightCustomers(@Param("marketInsightCustomerList")List<MarketInsightCustomer> marketInsightCustomerList);
    /**
    * 逻辑删除市场洞察客户表
    *
    * @param marketInsightCustomer
    * @return 结果
    */
    int logicDeleteMarketInsightCustomerByMarketInsightCustomerId(@Param("marketInsightCustomer")MarketInsightCustomer marketInsightCustomer);

    /**
    * 逻辑批量删除市场洞察客户表
    *
    * @param marketInsightCustomerIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(@Param("marketInsightCustomerIds")List<Long> marketInsightCustomerIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察客户表
    *
    * @param marketInsightCustomerId 市场洞察客户表主键
    * @return 结果
    */
    int deleteMarketInsightCustomerByMarketInsightCustomerId(@Param("marketInsightCustomerId")Long marketInsightCustomerId);

    /**
    * 物理批量删除市场洞察客户表
    *
    * @param marketInsightCustomerIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMarketInsightCustomerByMarketInsightCustomerIds(@Param("marketInsightCustomerIds")List<Long> marketInsightCustomerIds);

    /**
    * 批量新增市场洞察客户表
    *
    * @param marketInsightCustomers 市场洞察客户表列表
    * @return 结果
    */
    int batchMarketInsightCustomer(@Param("marketInsightCustomers")List<MarketInsightCustomer> marketInsightCustomers);

    /**
     * 远程查询看客户是否被引用
     * @param marketInsightCustomer
     * @return
     */
    List<MarketInsightCustomerDTO> remoteMarketInsightCustomerList(@Param("marketInsightCustomer")MarketInsightCustomer marketInsightCustomer);
}
