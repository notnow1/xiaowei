package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.basic.Industry;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * IndustryMapper接口
 *
 * @author Graves
 * @since 2022-09-26
 */
public interface IndustryMapper {
    /**
     * 查询行业
     *
     * @param industryId 行业主键
     * @return 行业
     */
    IndustryDTO selectIndustryByIndustryId(@Param("industryId") Long industryId);

    /**
     * 行业编码唯一性校验
     *
     * @param industryCode
     * @return
     */
    int checkUnique(@Param("industryCode") String industryCode);

    /**
     * 查询行业列表
     *
     * @param industry 行业
     * @return 行业集合
     */
    List<IndustryDTO> selectIndustryList(@Param("industry") Industry industry);

    /**
     * 新增行业
     *
     * @param industry 行业
     * @return 结果
     */
    int insertIndustry(@Param("industry") Industry industry);

    /**
     * 修改行业
     *
     * @param industry 行业
     * @return 结果
     */
    int updateIndustry(@Param("industry") Industry industry);

    /**
     * 修改行业祖级列表ID
     *
     * @param industry 行业
     * @return 结果
     */
    int updateAncestors(@Param("industry") Industry industry);

    /**
     * 批量修改行业
     *
     * @param industryList 行业
     * @return 结果
     */
    int updateIndustrys(@Param("industryList") List<Industry> industryList);

    /**
     * 逻辑删除行业
     *
     * @param industry
     * @return 结果
     */
    int logicDeleteIndustryByIndustryId(@Param("industry") Industry industry, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 查找子级
     * @param industryId
     * @return
     */
    List<Long> selectSon(@Param("industryId") Long industryId);

    /**
     * 逻辑批量删除行业
     *
     * @param industryIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteIndustryByIndustryIds(@Param("industryIds") List<Long> industryIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 物理删除行业
     *
     * @param industryId 行业主键
     * @return 结果
     */
    int deleteIndustryByIndustryId(@Param("industryId") Long industryId);

    /**
     * 物理批量删除行业
     *
     * @param industryIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteIndustryByIndustryIds(@Param("industryIds") List<Long> industryIds);

    /**
     * 批量新增行业
     *
     * @param Industrys 行业列表
     * @return 结果
     */
    int batchIndustry(@Param("industrys") List<Industry> Industrys);


    int updateStatus(@Param("status") Integer status, @Param("industrysId") Long industrysId);
}
