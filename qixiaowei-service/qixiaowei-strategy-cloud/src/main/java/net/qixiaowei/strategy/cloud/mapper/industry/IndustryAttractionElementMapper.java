package net.qixiaowei.strategy.cloud.mapper.industry;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.industry.IndustryAttractionElement;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionElementDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* IndustryAttractionElementMapper接口
* @author TANGMICHI
* @since 2023-02-20
*/
public interface IndustryAttractionElementMapper{
    /**
    * 查询行业吸引力要素表
    *
    * @param industryAttractionElementId 行业吸引力要素表主键
    * @return 行业吸引力要素表
    */
    IndustryAttractionElementDTO selectIndustryAttractionElementByIndustryAttractionElementId(@Param("industryAttractionElementId")Long industryAttractionElementId);


    /**
     * 根据行业吸引力主表id查询行业吸引力要素表
     *
     * @param industryAttractionId 行业吸引力主表主键
     * @return 行业吸引力要素表
     */
    List<IndustryAttractionElementDTO> selectIndustryAttractionElementByIndustryAttractionId(@Param("industryAttractionId")Long industryAttractionId);
    /**
    * 批量查询行业吸引力要素表
    *
    * @param industryAttractionElementIds 行业吸引力要素表主键集合
    * @return 行业吸引力要素表
    */
    List<IndustryAttractionElementDTO> selectIndustryAttractionElementByIndustryAttractionElementIds(@Param("industryAttractionElementIds") List<Long> industryAttractionElementIds);

    /**
     * 根据行业吸引力主表id集合批量查询行业吸引力要素表
     *
     * @param industryAttractionIds 行业吸引力主表主键集合
     * @return 行业吸引力要素表
     */
    List<IndustryAttractionElementDTO> selectIndustryAttractionElementByIndustryAttractionIds(@Param("industryAttractionIds") List<Long> industryAttractionIds);
    /**
    * 查询行业吸引力要素表列表
    *
    * @param industryAttractionElement 行业吸引力要素表
    * @return 行业吸引力要素表集合
    */
    List<IndustryAttractionElementDTO> selectIndustryAttractionElementList(@Param("industryAttractionElement")IndustryAttractionElement industryAttractionElement);

    /**
    * 新增行业吸引力要素表
    *
    * @param industryAttractionElement 行业吸引力要素表
    * @return 结果
    */
    int insertIndustryAttractionElement(@Param("industryAttractionElement")IndustryAttractionElement industryAttractionElement);

    /**
    * 修改行业吸引力要素表
    *
    * @param industryAttractionElement 行业吸引力要素表
    * @return 结果
    */
    int updateIndustryAttractionElement(@Param("industryAttractionElement")IndustryAttractionElement industryAttractionElement);

    /**
    * 批量修改行业吸引力要素表
    *
    * @param industryAttractionElementList 行业吸引力要素表
    * @return 结果
    */
    int updateIndustryAttractionElements(@Param("industryAttractionElementList")List<IndustryAttractionElement> industryAttractionElementList);
    /**
    * 逻辑删除行业吸引力要素表
    *
    * @param industryAttractionElement
    * @return 结果
    */
    int logicDeleteIndustryAttractionElementByIndustryAttractionElementId(@Param("industryAttractionElement")IndustryAttractionElement industryAttractionElement);

    /**
    * 逻辑批量删除行业吸引力要素表
    *
    * @param industryAttractionElementIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteIndustryAttractionElementByIndustryAttractionElementIds(@Param("industryAttractionElementIds")List<Long> industryAttractionElementIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除行业吸引力要素表
    *
    * @param industryAttractionElementId 行业吸引力要素表主键
    * @return 结果
    */
    int deleteIndustryAttractionElementByIndustryAttractionElementId(@Param("industryAttractionElementId")Long industryAttractionElementId);

    /**
    * 物理批量删除行业吸引力要素表
    *
    * @param industryAttractionElementIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteIndustryAttractionElementByIndustryAttractionElementIds(@Param("industryAttractionElementIds")List<Long> industryAttractionElementIds);

    /**
    * 批量新增行业吸引力要素表
    *
    * @param industryAttractionElements 行业吸引力要素表列表
    * @return 结果
    */
    int batchIndustryAttractionElement(@Param("industryAttractionElements")List<IndustryAttractionElement> industryAttractionElements);
}
