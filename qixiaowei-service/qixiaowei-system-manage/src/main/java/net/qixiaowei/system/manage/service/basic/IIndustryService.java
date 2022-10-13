package net.qixiaowei.system.manage.service.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;

import java.util.List;


/**
 * IndustryService接口
 *
 * @author Graves
 * @since 2022-09-26
 */
public interface IIndustryService {
    /**
     * 查询行业
     *
     * @param industryId 行业主键
     * @return 行业
     */
    IndustryDTO selectIndustryByIndustryId(Long industryId);

    /**
     * 查询行业分页列表
     *
     * @param industryDTO 行业
     * @return 行业集合
     */
    List<IndustryDTO> selectIndustryPageList(IndustryDTO industryDTO);

    /**
     * 查询行业列表
     *
     * @param industryDTO
     * @return
     */
    List<IndustryDTO> selectIndustryList(IndustryDTO industryDTO);

    /**
     * 树结构
     *
     * @param industryDTO
     * @return
     */
    public List<Tree<Long>> selectIndustryTreeList(IndustryDTO industryDTO);

    /**
     * 新增行业
     *
     * @param industryDTO 行业
     * @return 结果
     */
    int insertIndustry(IndustryDTO industryDTO);

    /**
     * 修改行业
     *
     * @param industryDTO 行业
     * @return 结果
     */
    int updateIndustry(IndustryDTO industryDTO);

    /**
     * 批量修改行业
     *
     * @param industryDtos 行业
     * @return 结果
     */
    int updateIndustrys(List<IndustryDTO> industryDtos);

    /**
     * 批量新增行业
     *
     * @param industryDtos 行业
     * @return 结果
     */
    int insertIndustrys(List<IndustryDTO> industryDtos);

    /**
     * 逻辑批量删除行业
     *
     * @param industryIds 需要删除的行业集合
     * @return 结果
     */
    int logicDeleteIndustryByIndustryIds(List<Long> industryIds);

    /**
     * 逻辑删除行业信息
     *
     * @param industryId
     * @return 结果
     */
    int logicDeleteIndustryByIndustryId(Long industryId);

    /**
     * 逻辑批量删除行业
     *
     * @param IndustryDtos 需要删除的行业集合
     * @return 结果
     */
    int deleteIndustryByIndustryIds(List<IndustryDTO> IndustryDtos);

    /**
     * 逻辑删除行业信息
     *
     * @param industryDTO
     * @return 结果
     */
    int deleteIndustryByIndustryId(IndustryDTO industryDTO);


    /**
     * 删除行业信息
     *
     * @param industryId 行业主键
     * @return 结果
     */
    int deleteIndustryByIndustryId(Long industryId);

    /**
     * 获取启用行业类型
     *
     * @return
     */
    int getEnableType();

    /**
     * 修改启用行业类型
     *
     * @return
     */
    int updateEnableType(IndustryDTO industryDTO);

    /**
     * 行业配置详情
     *
     * @param industryId
     * @return
     */
    IndustryDTO detailIndustry(Long industryId);

}
