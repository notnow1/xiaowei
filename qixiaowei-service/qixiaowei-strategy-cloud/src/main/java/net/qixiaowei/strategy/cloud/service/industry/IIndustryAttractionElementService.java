package net.qixiaowei.strategy.cloud.service.industry;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.dto.industry.IndustryAttractionElementDTO;


/**
 * IndustryAttractionElementService接口
 *
 * @author TANGMICHI
 * @since 2023-02-17
 */
public interface IIndustryAttractionElementService {
    /**
     * 查询行业吸引力要素表
     *
     * @param industryAttractionElementId 行业吸引力要素表主键
     * @return 行业吸引力要素表
     */
    IndustryAttractionElementDTO selectIndustryAttractionElementByIndustryAttractionElementId(Long industryAttractionElementId);

    /**
     * 查询行业吸引力要素表列表
     *
     * @param industryAttractionElementDTO 行业吸引力要素表
     * @return 行业吸引力要素表集合
     */
    List<IndustryAttractionElementDTO> selectIndustryAttractionElementList(IndustryAttractionElementDTO industryAttractionElementDTO);

    /**
     * 新增行业吸引力要素表
     *
     * @param industryAttractionElementDTO 行业吸引力要素表
     * @return 结果
     */
    IndustryAttractionElementDTO insertIndustryAttractionElement(IndustryAttractionElementDTO industryAttractionElementDTO);

    /**
     * 修改行业吸引力要素表
     *
     * @param industryAttractionElementDTO 行业吸引力要素表
     * @return 结果
     */
    int updateIndustryAttractionElement(IndustryAttractionElementDTO industryAttractionElementDTO);

    /**
     * 批量修改行业吸引力要素表
     *
     * @param industryAttractionElementDtos 行业吸引力要素表
     * @return 结果
     */
    int updateIndustryAttractionElements(List<IndustryAttractionElementDTO> industryAttractionElementDtos);

    /**
     * 批量新增行业吸引力要素表
     *
     * @param industryAttractionElementDtos 行业吸引力要素表
     * @return 结果
     */
    int insertIndustryAttractionElements(List<IndustryAttractionElementDTO> industryAttractionElementDtos);

    /**
     * 逻辑批量删除行业吸引力要素表
     *
     * @param industryAttractionElementIds 需要删除的行业吸引力要素表集合
     * @return 结果
     */
    int logicDeleteIndustryAttractionElementByIndustryAttractionElementIds(List<Long> industryAttractionElementIds);

    /**
     * 逻辑删除行业吸引力要素表信息
     *
     * @param industryAttractionElementDTO
     * @return 结果
     */
    int logicDeleteIndustryAttractionElementByIndustryAttractionElementId(IndustryAttractionElementDTO industryAttractionElementDTO);

    /**
     * 批量删除行业吸引力要素表
     *
     * @param IndustryAttractionElementDtos
     * @return 结果
     */
    int deleteIndustryAttractionElementByIndustryAttractionElementIds(List<IndustryAttractionElementDTO> IndustryAttractionElementDtos);

    /**
     * 逻辑删除行业吸引力要素表信息
     *
     * @param industryAttractionElementDTO
     * @return 结果
     */
    int deleteIndustryAttractionElementByIndustryAttractionElementId(IndustryAttractionElementDTO industryAttractionElementDTO);


    /**
     * 删除行业吸引力要素表信息
     *
     * @param industryAttractionElementId 行业吸引力要素表主键
     * @return 结果
     */
    int deleteIndustryAttractionElementByIndustryAttractionElementId(Long industryAttractionElementId);

}
