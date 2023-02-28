package net.qixiaowei.strategy.cloud.service.impl.marketInsight;

import java.util.List;
import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MarketInsightMacro;
import net.qixiaowei.strategy.cloud.excel.marketInsight.MarketInsightMacroExcel;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MarketInsightMacroDTO;
import net.qixiaowei.strategy.cloud.mapper.marketInsight.MarketInsightMacroMapper;
import net.qixiaowei.strategy.cloud.service.marketInsight.IMarketInsightMacroService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
* MarketInsightMacroService业务层处理
* @author TANGMICHI
* @since 2023-02-28
*/
@Service
public class MarketInsightMacroServiceImpl implements IMarketInsightMacroService{
    @Autowired
    private MarketInsightMacroMapper marketInsightMacroMapper;

    /**
    * 查询市场洞察宏观表
    *
    * @param marketInsightMacroId 市场洞察宏观表主键
    * @return 市场洞察宏观表
    */
    @Override
    public MarketInsightMacroDTO selectMarketInsightMacroByMarketInsightMacroId(Long marketInsightMacroId)
    {
    return marketInsightMacroMapper.selectMarketInsightMacroByMarketInsightMacroId(marketInsightMacroId);
    }

    /**
    * 查询市场洞察宏观表列表
    *
    * @param marketInsightMacroDTO 市场洞察宏观表
    * @return 市场洞察宏观表
    */
    @Override
    public List<MarketInsightMacroDTO> selectMarketInsightMacroList(MarketInsightMacroDTO marketInsightMacroDTO)
    {
    MarketInsightMacro marketInsightMacro=new MarketInsightMacro();
    BeanUtils.copyProperties(marketInsightMacroDTO,marketInsightMacro);
    return marketInsightMacroMapper.selectMarketInsightMacroList(marketInsightMacro);
    }

    /**
    * 新增市场洞察宏观表
    *
    * @param marketInsightMacroDTO 市场洞察宏观表
    * @return 结果
    */
    @Override
    public MarketInsightMacroDTO insertMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO){
    MarketInsightMacro marketInsightMacro=new MarketInsightMacro();
    BeanUtils.copyProperties(marketInsightMacroDTO,marketInsightMacro);
    marketInsightMacro.setCreateBy(SecurityUtils.getUserId());
    marketInsightMacro.setCreateTime(DateUtils.getNowDate());
    marketInsightMacro.setUpdateTime(DateUtils.getNowDate());
    marketInsightMacro.setUpdateBy(SecurityUtils.getUserId());
    marketInsightMacro.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
    marketInsightMacroMapper.insertMarketInsightMacro(marketInsightMacro);
    marketInsightMacroDTO.setMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
    return marketInsightMacroDTO;
    }

    /**
    * 修改市场洞察宏观表
    *
    * @param marketInsightMacroDTO 市场洞察宏观表
    * @return 结果
    */
    @Override
    public int updateMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO)
    {
    MarketInsightMacro marketInsightMacro=new MarketInsightMacro();
    BeanUtils.copyProperties(marketInsightMacroDTO,marketInsightMacro);
    marketInsightMacro.setUpdateTime(DateUtils.getNowDate());
    marketInsightMacro.setUpdateBy(SecurityUtils.getUserId());
    return marketInsightMacroMapper.updateMarketInsightMacro(marketInsightMacro);
    }

    /**
    * 逻辑批量删除市场洞察宏观表
    *
    * @param marketInsightMacroIds 主键集合
    * @return 结果
    */
    @Override
    public int logicDeleteMarketInsightMacroByMarketInsightMacroIds(List<Long> marketInsightMacroIds){
    return marketInsightMacroMapper.logicDeleteMarketInsightMacroByMarketInsightMacroIds(marketInsightMacroIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
    }

    /**
    * 物理删除市场洞察宏观表信息
    *
    * @param marketInsightMacroId 市场洞察宏观表主键
    * @return 结果
    */
    @Override
    public int deleteMarketInsightMacroByMarketInsightMacroId(Long marketInsightMacroId)
    {
    return marketInsightMacroMapper.deleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacroId);
    }

     /**
     * 逻辑删除市场洞察宏观表信息
     *
     * @param  marketInsightMacroDTO 市场洞察宏观表
     * @return 结果
     */
     @Override
     public int logicDeleteMarketInsightMacroByMarketInsightMacroId(MarketInsightMacroDTO marketInsightMacroDTO)
     {
     MarketInsightMacro marketInsightMacro=new MarketInsightMacro();
     marketInsightMacro.setMarketInsightMacroId(marketInsightMacroDTO.getMarketInsightMacroId());
     marketInsightMacro.setUpdateTime(DateUtils.getNowDate());
     marketInsightMacro.setUpdateBy(SecurityUtils.getUserId());
     return marketInsightMacroMapper.logicDeleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacro);
     }

     /**
     * 物理删除市场洞察宏观表信息
     *
     * @param  marketInsightMacroDTO 市场洞察宏观表
     * @return 结果
     */
     
     @Override
     public int deleteMarketInsightMacroByMarketInsightMacroId(MarketInsightMacroDTO marketInsightMacroDTO)
     {
     MarketInsightMacro marketInsightMacro=new MarketInsightMacro();
     BeanUtils.copyProperties(marketInsightMacroDTO,marketInsightMacro);
     return marketInsightMacroMapper.deleteMarketInsightMacroByMarketInsightMacroId(marketInsightMacro.getMarketInsightMacroId());
     }
     /**
     * 物理批量删除市场洞察宏观表
     *
     * @param marketInsightMacroDtos 需要删除的市场洞察宏观表主键
     * @return 结果
     */
     
     @Override
     public int deleteMarketInsightMacroByMarketInsightMacroIds(List<MarketInsightMacroDTO> marketInsightMacroDtos){
     List<Long> stringList = new ArrayList();
     for (MarketInsightMacroDTO marketInsightMacroDTO : marketInsightMacroDtos) {
     stringList.add(marketInsightMacroDTO.getMarketInsightMacroId());
     }
     return marketInsightMacroMapper.deleteMarketInsightMacroByMarketInsightMacroIds(stringList);
     }
    /**
    * 导出Excel
    * @param marketInsightMacroDTO
    * @return
    */
    @Override
    public List<MarketInsightMacroExcel> exportMarketInsightMacro(MarketInsightMacroDTO marketInsightMacroDTO) {
        MarketInsightMacro marketInsightMacro = new MarketInsightMacro();
        BeanUtils.copyProperties(marketInsightMacroDTO, marketInsightMacro);
        List<MarketInsightMacroDTO> marketInsightMacroDTOList = marketInsightMacroMapper.selectMarketInsightMacroList(marketInsightMacro);
        List<MarketInsightMacroExcel> marketInsightMacroExcelList = new ArrayList<>();
        return marketInsightMacroExcelList;
    }
}

