package net.qixiaowei.system.manage.service.basic;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import org.apache.ibatis.annotations.Param;

import java.util.List;


/**
 * IndustryDefaultService接口
 *
 * @author Graves
 * @since 2022-09-26
 */
public interface IIndustryDefaultService {
    /**
     * 查询默认行业
     *
     * @param industryId 默认行业主键
     * @return 默认行业
     */
    IndustryDefaultDTO selectIndustryDefaultByIndustryId(Long industryId);

    /**
     * 查询默认行业列表
     *
     * @param industryDefaultDTO 默认行业
     * @return 默认行业集合
     */
    List<IndustryDefaultDTO> selectIndustryDefaultList(IndustryDefaultDTO industryDefaultDTO);

    /**
     * 查询默认行业分页列表
     *
     * @param industryDefaultDTO 行业
     * @return 行业集合
     */
    List<IndustryDefaultDTO> selectIndustryDefaultPageList(IndustryDefaultDTO industryDefaultDTO);

    /**
     * 新增默认行业
     *
     * @param industryDefaultDTO 默认行业
     * @return 结果
     */
    int insertIndustryDefault(IndustryDefaultDTO industryDefaultDTO);

    /**
     * 修改默认行业
     *
     * @param industryDefaultDTO 默认行业
     * @return 结果
     */
    int updateIndustryDefault(IndustryDefaultDTO industryDefaultDTO);

    /**
     * 批量修改默认行业
     *
     * @param industryDefaultDtos 默认行业
     * @return 结果
     */
    int updateIndustryDefaults(List<IndustryDefaultDTO> industryDefaultDtos);

    /**
     * 批量新增默认行业
     *
     * @param industryDefaultDtos 默认行业
     * @return 结果
     */
    int insertIndustryDefaults(List<IndustryDefaultDTO> industryDefaultDtos);

    /**
     * 逻辑批量删除默认行业
     *
     * @param industryIds 需要删除的默认行业集合
     * @return 结果
     */
    int logicDeleteIndustryDefaultByIndustryIds(List<Long> industryIds);

    /**
     * 逻辑删除默认行业信息
     *
     * @param industryId
     * @return 结果
     */
    int logicDeleteIndustryDefaultByIndustryId(Long industryId);

    /**
     * 逻辑批量删除默认行业
     *
     * @param IndustryDefaultDtos 需要删除的默认行业集合
     * @return 结果
     */
    int deleteIndustryDefaultByIndustryIds(List<IndustryDefaultDTO> IndustryDefaultDtos);

    /**
     * 逻辑删除默认行业信息
     *
     * @param industryDefaultDTO
     * @return 结果
     */
    int deleteIndustryDefaultByIndustryId(IndustryDefaultDTO industryDefaultDTO);


    /**
     * 删除默认行业信息
     *
     * @param industryId 默认行业主键
     * @return 结果
     */
    int deleteIndustryDefaultByIndustryId(Long industryId);

    /**
     * 默认行业配置详情
     *
     * @param industryId
     * @return
     */
    IndustryDefaultDTO detailIndustryDefault(Long industryId);

    /**
     * 树结构默认行业信息
     *
     * @param industryDefaultDTO
     * @return
     */
    List<Tree<Long>> selectIndustryDefaultTreeList(IndustryDefaultDTO industryDefaultDTO);

    /**
     * 获取指标最大层级
     *
     * @return List
     */
    List<Integer> getLevel();

    /**
     * 根据ID集合查询默认行业
     *
     * @param defaultIndustryIds 默认ID集合
     * @return List
     */
    List<IndustryDefaultDTO> selectIndustryDefaultByIndustryIds(List<Long> defaultIndustryIds);

    /**
     * 根据编码查询默认行业列表
     *
     * @param industryCodes 行业编码集合
     * @return List
     */
    List<IndustryDefaultDTO> selectDefaultByCodes(List<String> industryCodes);
}
