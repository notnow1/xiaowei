package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiIndustryAttraction;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiIndustryAttractionDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiIndustryAttractionMapper接口
* @author TANGMICHI
* @since 2023-03-03
*/
public interface MiIndustryAttractionMapper{
    /**
    * 查询市场洞察行业吸引力表
    *
    * @param miIndustryAttractionId 市场洞察行业吸引力表主键
    * @return 市场洞察行业吸引力表
    */
    MiIndustryAttractionDTO selectMiIndustryAttractionByMiIndustryAttractionId(@Param("miIndustryAttractionId")Long miIndustryAttractionId);

    /**
     * 根据市场洞察行业主表主键查询市场洞察行业吸引力表
     *
     * @param marketInsightIndustryId 市场洞察行业主表主键
     * @return 市场洞察行业吸引力表
     */
    List<MiIndustryAttractionDTO> selectMiIndustryAttractionByMarketInsightIndustryId(@Param("marketInsightIndustryId")Long marketInsightIndustryId);
    /**
    * 批量查询市场洞察行业吸引力表
    *
    * @param miIndustryAttractionIds 市场洞察行业吸引力表主键集合
    * @return 市场洞察行业吸引力表
    */
    List<MiIndustryAttractionDTO> selectMiIndustryAttractionByMiIndustryAttractionIds(@Param("miIndustryAttractionIds") List<Long> miIndustryAttractionIds);

    /**
     * 根据市场洞察行业主表主键集合批量查询市场洞察行业吸引力表
     *
     * @param marketInsightIndustryIds 市场洞察行业主表主键集合
     * @return 市场洞察行业吸引力表
     */
    List<MiIndustryAttractionDTO> selectMiIndustryAttractionByMarketInsightIndustryIds(@Param("marketInsightIndustryIds") List<Long> marketInsightIndustryIds);

    /**
    * 查询市场洞察行业吸引力表列表
    *
    * @param miIndustryAttraction 市场洞察行业吸引力表
    * @return 市场洞察行业吸引力表集合
    */
    List<MiIndustryAttractionDTO> selectMiIndustryAttractionList(@Param("miIndustryAttraction")MiIndustryAttraction miIndustryAttraction);

    /**
    * 新增市场洞察行业吸引力表
    *
    * @param miIndustryAttraction 市场洞察行业吸引力表
    * @return 结果
    */
    int insertMiIndustryAttraction(@Param("miIndustryAttraction")MiIndustryAttraction miIndustryAttraction);

    /**
    * 修改市场洞察行业吸引力表
    *
    * @param miIndustryAttraction 市场洞察行业吸引力表
    * @return 结果
    */
    int updateMiIndustryAttraction(@Param("miIndustryAttraction")MiIndustryAttraction miIndustryAttraction);

    /**
    * 批量修改市场洞察行业吸引力表
    *
    * @param miIndustryAttractionList 市场洞察行业吸引力表
    * @return 结果
    */
    int updateMiIndustryAttractions(@Param("miIndustryAttractionList")List<MiIndustryAttraction> miIndustryAttractionList);
    /**
    * 逻辑删除市场洞察行业吸引力表
    *
    * @param miIndustryAttraction
    * @return 结果
    */
    int logicDeleteMiIndustryAttractionByMiIndustryAttractionId(@Param("miIndustryAttraction")MiIndustryAttraction miIndustryAttraction);

    /**
    * 逻辑批量删除市场洞察行业吸引力表
    *
    * @param miIndustryAttractionIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiIndustryAttractionByMiIndustryAttractionIds(@Param("miIndustryAttractionIds")List<Long> miIndustryAttractionIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察行业吸引力表
    *
    * @param miIndustryAttractionId 市场洞察行业吸引力表主键
    * @return 结果
    */
    int deleteMiIndustryAttractionByMiIndustryAttractionId(@Param("miIndustryAttractionId")Long miIndustryAttractionId);

    /**
    * 物理批量删除市场洞察行业吸引力表
    *
    * @param miIndustryAttractionIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiIndustryAttractionByMiIndustryAttractionIds(@Param("miIndustryAttractionIds")List<Long> miIndustryAttractionIds);

    /**
    * 批量新增市场洞察行业吸引力表
    *
    * @param miIndustryAttractions 市场洞察行业吸引力表列表
    * @return 结果
    */
    int batchMiIndustryAttraction(@Param("miIndustryAttractions")List<MiIndustryAttraction> miIndustryAttractions);
}
