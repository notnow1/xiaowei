package net.qixiaowei.strategy.cloud.service.industry;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionDTO;



/**
* IndustryAttractionService接口
* @author TANGMICHI
* @since 2023-02-20
*/
public interface IIndustryAttractionService{
    /**
    * 查询行业吸引力表
    *
    * @param industryAttractionId 行业吸引力表主键
    * @return 行业吸引力表
    */
    IndustryAttractionDTO selectIndustryAttractionByIndustryAttractionId(Long industryAttractionId);

    /**
    * 查询行业吸引力表列表
    *
    * @param industryAttractionDTO 行业吸引力表
    * @return 行业吸引力表集合
    */
    List<IndustryAttractionDTO> selectIndustryAttractionList(IndustryAttractionDTO industryAttractionDTO);

    /**
    * 新增行业吸引力表
    *
    * @param industryAttractionDTO 行业吸引力表
    * @return 结果
    */
    IndustryAttractionDTO insertIndustryAttraction(IndustryAttractionDTO industryAttractionDTO);

    /**
    * 修改行业吸引力表
    *
    * @param industryAttractionDTO 行业吸引力表
    * @return 结果
    */
    int updateIndustryAttraction(IndustryAttractionDTO industryAttractionDTO);

    /**
    * 批量修改行业吸引力表
    *
    * @param industryAttractionDtos 行业吸引力表
    * @return 结果
    */
    int updateIndustryAttractions(List<IndustryAttractionDTO> industryAttractionDtos);

    /**
    * 批量新增行业吸引力表
    *
    * @param industryAttractionDtos 行业吸引力表
    * @return 结果
    */
    int insertIndustryAttractions(List<IndustryAttractionDTO> industryAttractionDtos);

    /**
    * 逻辑批量删除行业吸引力表
    *
    * @param industryAttractionIds 需要删除的行业吸引力表集合
    * @return 结果
    */
    int logicDeleteIndustryAttractionByIndustryAttractionIds(List<Long> industryAttractionIds);

    /**
    * 逻辑删除行业吸引力表信息
    *
    * @param industryAttractionDTO
    * @return 结果
    */
    int logicDeleteIndustryAttractionByIndustryAttractionId(IndustryAttractionDTO industryAttractionDTO);
    /**
    * 批量删除行业吸引力表
    *
    * @param IndustryAttractionDtos
    * @return 结果
    */
    int deleteIndustryAttractionByIndustryAttractionIds(List<IndustryAttractionDTO> IndustryAttractionDtos);

    /**
    * 逻辑删除行业吸引力表信息
    *
    * @param industryAttractionDTO
    * @return 结果
    */
    int deleteIndustryAttractionByIndustryAttractionId(IndustryAttractionDTO industryAttractionDTO);


    /**
    * 删除行业吸引力表信息
    *
    * @param industryAttractionId 行业吸引力表主键
    * @return 结果
    */
    int deleteIndustryAttractionByIndustryAttractionId(Long industryAttractionId);
}
