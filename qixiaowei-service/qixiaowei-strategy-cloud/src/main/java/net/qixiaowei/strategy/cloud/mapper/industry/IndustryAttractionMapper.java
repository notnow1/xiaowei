package net.qixiaowei.strategy.cloud.mapper.industry;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.domain.industry.IndustryAttraction;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * IndustryAttractionMapper接口
 *
 * @author TANGMICHI
 * @since 2023-02-17
 */
public interface IndustryAttractionMapper {
    /**
     * 查询行业吸引力表
     *
     * @param industryAttractionId 行业吸引力表主键
     * @return 行业吸引力表
     */
    IndustryAttractionDTO selectIndustryAttractionByIndustryAttractionId(@Param("industryAttractionId") Long industryAttractionId);


    /**
     * 批量查询行业吸引力表
     *
     * @param industryAttractionIds 行业吸引力表主键集合
     * @return 行业吸引力表
     */
    List<IndustryAttractionDTO> selectIndustryAttractionByIndustryAttractionIds(@Param("industryAttractionIds") List<Long> industryAttractionIds);

    /**
     * 查询行业吸引力表列表
     *
     * @param industryAttraction 行业吸引力表
     * @return 行业吸引力表集合
     */
    List<IndustryAttractionDTO> selectIndustryAttractionList(@Param("industryAttraction") IndustryAttraction industryAttraction);

    /**
     * 新增行业吸引力表
     *
     * @param industryAttraction 行业吸引力表
     * @return 结果
     */
    int insertIndustryAttraction(@Param("industryAttraction") IndustryAttraction industryAttraction);

    /**
     * 修改行业吸引力表
     *
     * @param industryAttraction 行业吸引力表
     * @return 结果
     */
    int updateIndustryAttraction(@Param("industryAttraction") IndustryAttraction industryAttraction);

    /**
     * 批量修改行业吸引力表
     *
     * @param industryAttractionList 行业吸引力表
     * @return 结果
     */
    int updateIndustryAttractions(@Param("industryAttractionList") List<IndustryAttraction> industryAttractionList);

    /**
     * 逻辑删除行业吸引力表
     *
     * @param industryAttraction
     * @return 结果
     */
    int logicDeleteIndustryAttractionByIndustryAttractionId(@Param("industryAttraction") IndustryAttraction industryAttraction);

    /**
     * 逻辑批量删除行业吸引力表
     *
     * @param industryAttractionIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteIndustryAttractionByIndustryAttractionIds(@Param("industryAttractionIds") List<Long> industryAttractionIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除行业吸引力表
     *
     * @param industryAttractionId 行业吸引力表主键
     * @return 结果
     */
    int deleteIndustryAttractionByIndustryAttractionId(@Param("industryAttractionId") Long industryAttractionId);

    /**
     * 物理批量删除行业吸引力表
     *
     * @param industryAttractionIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteIndustryAttractionByIndustryAttractionIds(@Param("industryAttractionIds") List<Long> industryAttractionIds);

    /**
     * 批量新增行业吸引力表
     *
     * @param industryAttractions 行业吸引力表列表
     * @return 结果
     */
    int batchIndustryAttraction(@Param("industryAttractions") List<IndustryAttraction> industryAttractions);
}
