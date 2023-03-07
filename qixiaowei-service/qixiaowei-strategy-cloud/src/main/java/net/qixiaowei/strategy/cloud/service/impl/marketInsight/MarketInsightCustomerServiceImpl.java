package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightCustomer;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightCustomerDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightCustomerMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightCustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* MarketInsightCustomerService业务层处理
* @author TANGMICHI
* @since 2023-03-07
*/
@Service
public class MarketInsightCustomerServiceImpl implements IMarketInsightCustomerService{
    @Autowired
    private MarketInsightCustomerMapper marketInsightCustomerMapper;

    /**
    * 查询市场洞察客户表
    *
    * @param marketInsightCustomerId 市场洞察客户表主键
    * @return 市场洞察客户表
    */
    @Override
    public MarketInsightCustomerDTO selectMarketInsightCustomerByMarketInsightCustomerId(Long marketInsightCustomerId)
    {
    return marketInsightCustomerMapper.selectMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomerId);
    }

    /**
    * 查询市场洞察客户表列表
    *
    * @param marketInsightCustomerDTO 市场洞察客户表
    * @return 市场洞察客户表
    */
    @Override
    public List<MarketInsightCustomerDTO> selectMarketInsightCustomerList(MarketInsightCustomerDTO marketInsightCustomerDTO)
    {
    MarketInsightCustomer marketInsightCustomer=new MarketInsightCustomer();
    BeanUtils.copyProperties(marketInsightCustomerDTO,marketInsightCustomer);
    return marketInsightCustomerMapper.selectMarketInsightCustomerList(marketInsightCustomer);
    }

    /**
    * 新增市场洞察客户表
    *
    * @param marketInsightCustomerDTO 市场洞察客户表
    * @return 结果
    */
    @Override
    public MarketInsightCustomerDTO insertMarketInsightCustomer(MarketInsightCustomerDTO marketInsightCustomerDTO){
    MarketInsightCustomer marketInsightCustomer=new MarketInsightCustomer();
    BeanUtils.copyProperties(marketInsightCustomerDTO,marketInsightCustomer);
    marketInsightCustomer.setCreateBy(SecurityUtils.getUserId());
    marketInsightCustomer.setCreateTime(DateUtils.getNowDate());
    marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
    marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
    marketInsightCustomer.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    marketInsightCustomerMapper.insertMarketInsightCustomer(marketInsightCustomer);
    marketInsightCustomerDTO.setMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
    return marketInsightCustomerDTO;
    }

    /**
    * 修改市场洞察客户表
    *
    * @param marketInsightCustomerDTO 市场洞察客户表
    * @return 结果
    */
    @Override
    public int updateMarketInsightCustomer(MarketInsightCustomerDTO marketInsightCustomerDTO)
    {
    MarketInsightCustomer marketInsightCustomer=new MarketInsightCustomer();
    BeanUtils.copyProperties(marketInsightCustomerDTO,marketInsightCustomer);
    marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
    marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
    return marketInsightCustomerMapper.updateMarketInsightCustomer(marketInsightCustomer);
    }

    /**
    * 逻辑批量删除市场洞察客户表
    *
    * @param marketInsightCustomerIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(List<Long> marketInsightCustomerIds){
    return marketInsightCustomerMapper.logicDeleteMarketInsightCustomerByMarketInsightCustomerIds(marketInsightCustomerIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除市场洞察客户表信息
    *
    * @param marketInsightCustomerId 市场洞察客户表主键
    * @return 结果
    */
    @Override
    public int deleteMarketInsightCustomerByMarketInsightCustomerId(Long marketInsightCustomerId)
    {
    return marketInsightCustomerMapper.deleteMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomerId);
    }

     /**
     * 逻辑删除市场洞察客户表信息
     *
     * @param  marketInsightCustomerDTO 市场洞察客户表
     * @return 结果
     */
     @Override
     public int logicDeleteMarketInsightCustomerByMarketInsightCustomerId(MarketInsightCustomerDTO marketInsightCustomerDTO)
     {
     MarketInsightCustomer marketInsightCustomer=new MarketInsightCustomer();
     marketInsightCustomer.setMarketInsightCustomerId(marketInsightCustomerDTO.getMarketInsightCustomerId());
     marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
     marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
     return marketInsightCustomerMapper.logicDeleteMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomer);
     }

     /**
     * 物理删除市场洞察客户表信息
     *
     * @param  marketInsightCustomerDTO 市场洞察客户表
     * @return 结果
     */
     
     @Override
     public int deleteMarketInsightCustomerByMarketInsightCustomerId(MarketInsightCustomerDTO marketInsightCustomerDTO)
     {
     MarketInsightCustomer marketInsightCustomer=new MarketInsightCustomer();
     BeanUtils.copyProperties(marketInsightCustomerDTO,marketInsightCustomer);
     return marketInsightCustomerMapper.deleteMarketInsightCustomerByMarketInsightCustomerId(marketInsightCustomer.getMarketInsightCustomerId());
     }
     /**
     * 物理批量删除市场洞察客户表
     *
     * @param marketInsightCustomerDtos 需要删除的市场洞察客户表主键
     * @return 结果
     */
     
     @Override
     public int deleteMarketInsightCustomerByMarketInsightCustomerIds(List<MarketInsightCustomerDTO> marketInsightCustomerDtos){
     List<Long> stringList = new ArrayList();
     for (MarketInsightCustomerDTO marketInsightCustomerDTO : marketInsightCustomerDtos) {
     stringList.add(marketInsightCustomerDTO.getMarketInsightCustomerId());
     }
     return marketInsightCustomerMapper.deleteMarketInsightCustomerByMarketInsightCustomerIds(stringList);
     }

    /**
    * 批量新增市场洞察客户表信息
    *
    * @param marketInsightCustomerDtos 市场洞察客户表对象
    */
    
    public int insertMarketInsightCustomers(List<MarketInsightCustomerDTO> marketInsightCustomerDtos){
      List<MarketInsightCustomer> marketInsightCustomerList = new ArrayList();

    for (MarketInsightCustomerDTO marketInsightCustomerDTO : marketInsightCustomerDtos) {
      MarketInsightCustomer marketInsightCustomer =new MarketInsightCustomer();
      BeanUtils.copyProperties(marketInsightCustomerDTO,marketInsightCustomer);
       marketInsightCustomer.setCreateBy(SecurityUtils.getUserId());
       marketInsightCustomer.setCreateTime(DateUtils.getNowDate());
       marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
       marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
       marketInsightCustomer.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      marketInsightCustomerList.add(marketInsightCustomer);
    }
    return marketInsightCustomerMapper.batchMarketInsightCustomer(marketInsightCustomerList);
    }

    /**
    * 批量修改市场洞察客户表信息
    *
    * @param marketInsightCustomerDtos 市场洞察客户表对象
    */
    
    public int updateMarketInsightCustomers(List<MarketInsightCustomerDTO> marketInsightCustomerDtos){
     List<MarketInsightCustomer> marketInsightCustomerList = new ArrayList();

     for (MarketInsightCustomerDTO marketInsightCustomerDTO : marketInsightCustomerDtos) {
     MarketInsightCustomer marketInsightCustomer =new MarketInsightCustomer();
     BeanUtils.copyProperties(marketInsightCustomerDTO,marketInsightCustomer);
        marketInsightCustomer.setCreateBy(SecurityUtils.getUserId());
        marketInsightCustomer.setCreateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateTime(DateUtils.getNowDate());
        marketInsightCustomer.setUpdateBy(SecurityUtils.getUserId());
     marketInsightCustomerList.add(marketInsightCustomer);
     }
     return marketInsightCustomerMapper.updateMarketInsightCustomers(marketInsightCustomerList);
    }
}

