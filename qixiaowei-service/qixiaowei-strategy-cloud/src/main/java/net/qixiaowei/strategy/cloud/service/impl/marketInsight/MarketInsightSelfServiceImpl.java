package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightSelf;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightSelfDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightSelfMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightSelfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
* MarketInsightSelfService业务层处理
* @author TANGMICHI
* @since 2023-03-13
*/
@Service
public class MarketInsightSelfServiceImpl implements IMarketInsightSelfService{
    @Autowired
    private MarketInsightSelfMapper marketInsightSelfMapper;

    /**
    * 查询市场洞察自身表
    *
    * @param marketInsightSelfId 市场洞察自身表主键
    * @return 市场洞察自身表
    */
    @Override
    public MarketInsightSelfDTO selectMarketInsightSelfByMarketInsightSelfId(Long marketInsightSelfId)
    {
    return marketInsightSelfMapper.selectMarketInsightSelfByMarketInsightSelfId(marketInsightSelfId);
    }

    /**
    * 查询市场洞察自身表列表
    *
    * @param marketInsightSelfDTO 市场洞察自身表
    * @return 市场洞察自身表
    */
    @Override
    public List<MarketInsightSelfDTO> selectMarketInsightSelfList(MarketInsightSelfDTO marketInsightSelfDTO)
    {
    MarketInsightSelf marketInsightSelf=new MarketInsightSelf();
    BeanUtils.copyProperties(marketInsightSelfDTO,marketInsightSelf);
    return marketInsightSelfMapper.selectMarketInsightSelfList(marketInsightSelf);
    }

    /**
    * 新增市场洞察自身表
    *
    * @param marketInsightSelfDTO 市场洞察自身表
    * @return 结果
    */
    @Override
    public MarketInsightSelfDTO insertMarketInsightSelf(MarketInsightSelfDTO marketInsightSelfDTO){
    MarketInsightSelf marketInsightSelf=new MarketInsightSelf();
    BeanUtils.copyProperties(marketInsightSelfDTO,marketInsightSelf);
    marketInsightSelf.setCreateBy(SecurityUtils.getUserId());
    marketInsightSelf.setCreateTime(DateUtils.getNowDate());
    marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
    marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
    marketInsightSelf.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    marketInsightSelfMapper.insertMarketInsightSelf(marketInsightSelf);
    marketInsightSelfDTO.setMarketInsightSelfId(marketInsightSelf.getMarketInsightSelfId());
    return marketInsightSelfDTO;
    }

    /**
    * 修改市场洞察自身表
    *
    * @param marketInsightSelfDTO 市场洞察自身表
    * @return 结果
    */
    @Override
    public int updateMarketInsightSelf(MarketInsightSelfDTO marketInsightSelfDTO)
    {
    MarketInsightSelf marketInsightSelf=new MarketInsightSelf();
    BeanUtils.copyProperties(marketInsightSelfDTO,marketInsightSelf);
    marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
    marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
    return marketInsightSelfMapper.updateMarketInsightSelf(marketInsightSelf);
    }

    /**
    * 逻辑批量删除市场洞察自身表
    *
    * @param marketInsightSelfIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteMarketInsightSelfByMarketInsightSelfIds(List<Long> marketInsightSelfIds){
    return marketInsightSelfMapper.logicDeleteMarketInsightSelfByMarketInsightSelfIds(marketInsightSelfIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除市场洞察自身表信息
    *
    * @param marketInsightSelfId 市场洞察自身表主键
    * @return 结果
    */
    @Override
    public int deleteMarketInsightSelfByMarketInsightSelfId(Long marketInsightSelfId)
    {
    return marketInsightSelfMapper.deleteMarketInsightSelfByMarketInsightSelfId(marketInsightSelfId);
    }

     /**
     * 逻辑删除市场洞察自身表信息
     *
     * @param  marketInsightSelfDTO 市场洞察自身表
     * @return 结果
     */
     @Override
     public int logicDeleteMarketInsightSelfByMarketInsightSelfId(MarketInsightSelfDTO marketInsightSelfDTO)
     {
     MarketInsightSelf marketInsightSelf=new MarketInsightSelf();
     marketInsightSelf.setMarketInsightSelfId(marketInsightSelfDTO.getMarketInsightSelfId());
     marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
     marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
     return marketInsightSelfMapper.logicDeleteMarketInsightSelfByMarketInsightSelfId(marketInsightSelf);
     }

     /**
     * 物理删除市场洞察自身表信息
     *
     * @param  marketInsightSelfDTO 市场洞察自身表
     * @return 结果
     */
     
     @Override
     public int deleteMarketInsightSelfByMarketInsightSelfId(MarketInsightSelfDTO marketInsightSelfDTO)
     {
     MarketInsightSelf marketInsightSelf=new MarketInsightSelf();
     BeanUtils.copyProperties(marketInsightSelfDTO,marketInsightSelf);
     return marketInsightSelfMapper.deleteMarketInsightSelfByMarketInsightSelfId(marketInsightSelf.getMarketInsightSelfId());
     }
     /**
     * 物理批量删除市场洞察自身表
     *
     * @param marketInsightSelfDtos 需要删除的市场洞察自身表主键
     * @return 结果
     */
     
     @Override
     public int deleteMarketInsightSelfByMarketInsightSelfIds(List<MarketInsightSelfDTO> marketInsightSelfDtos){
     List<Long> stringList = new ArrayList();
     for (MarketInsightSelfDTO marketInsightSelfDTO : marketInsightSelfDtos) {
     stringList.add(marketInsightSelfDTO.getMarketInsightSelfId());
     }
     return marketInsightSelfMapper.deleteMarketInsightSelfByMarketInsightSelfIds(stringList);
     }

    /**
    * 批量新增市场洞察自身表信息
    *
    * @param marketInsightSelfDtos 市场洞察自身表对象
    */
    
    public int insertMarketInsightSelfs(List<MarketInsightSelfDTO> marketInsightSelfDtos){
      List<MarketInsightSelf> marketInsightSelfList = new ArrayList();

    for (MarketInsightSelfDTO marketInsightSelfDTO : marketInsightSelfDtos) {
      MarketInsightSelf marketInsightSelf =new MarketInsightSelf();
      BeanUtils.copyProperties(marketInsightSelfDTO,marketInsightSelf);
       marketInsightSelf.setCreateBy(SecurityUtils.getUserId());
       marketInsightSelf.setCreateTime(DateUtils.getNowDate());
       marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
       marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
       marketInsightSelf.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
      marketInsightSelfList.add(marketInsightSelf);
    }
    return marketInsightSelfMapper.batchMarketInsightSelf(marketInsightSelfList);
    }

    /**
    * 批量修改市场洞察自身表信息
    *
    * @param marketInsightSelfDtos 市场洞察自身表对象
    */
    
    public int updateMarketInsightSelfs(List<MarketInsightSelfDTO> marketInsightSelfDtos){
     List<MarketInsightSelf> marketInsightSelfList = new ArrayList();

     for (MarketInsightSelfDTO marketInsightSelfDTO : marketInsightSelfDtos) {
     MarketInsightSelf marketInsightSelf =new MarketInsightSelf();
     BeanUtils.copyProperties(marketInsightSelfDTO,marketInsightSelf);
        marketInsightSelf.setCreateBy(SecurityUtils.getUserId());
        marketInsightSelf.setCreateTime(DateUtils.getNowDate());
        marketInsightSelf.setUpdateTime(DateUtils.getNowDate());
        marketInsightSelf.setUpdateBy(SecurityUtils.getUserId());
     marketInsightSelfList.add(marketInsightSelf);
     }
     return marketInsightSelfMapper.updateMarketInsightSelfs(marketInsightSelfList);
    }
}

