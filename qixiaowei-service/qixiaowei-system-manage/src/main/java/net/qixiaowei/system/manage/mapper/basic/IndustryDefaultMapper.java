package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.basic.IndustryDefault;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDefaultDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * IndustryDefaultMapper接口
 *
 * @author Graves
 * @since 2022-09-26
 */
public interface IndustryDefaultMapper {
    /**
     * 查询默认行业
     *
     * @param industryId 默认行业主键
     * @return 默认行业
     */
    IndustryDefaultDTO selectIndustryDefaultByIndustryId(@Param("industryId") Long industryId);

    /**
     * 查询默认行业列表
     *
     * @param industryDefault 默认行业
     * @return 默认行业集合
     */
    List<IndustryDefaultDTO> selectIndustryDefaultList(@Param("industryDefault") IndustryDefault industryDefault);

    /**
     * 新增默认行业
     *
     * @param industryDefault 默认行业
     * @return 结果
     */
    int insertIndustryDefault(@Param("industryDefault") IndustryDefault industryDefault);

    /**
     * 修改默认行业
     *
     * @param industryDefault 默认行业
     * @return 结果
     */
    int updateIndustryDefault(@Param("industryDefault") IndustryDefault industryDefault);

    /**
     * 批量修改默认行业
     *
     * @param industryDefaultList 默认行业
     * @return 结果
     */
    int updateIndustryDefaults(@Param("industryDefaultList") List<IndustryDefault> industryDefaultList);

    /**
     * 逻辑删除默认行业
     *
     * @param industryDefault
     * @return 结果
     */
    int logicDeleteIndustryDefaultByIndustryId(@Param("industryDefault") IndustryDefault industryDefault, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除默认行业
     *
     * @param industryIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteIndustryDefaultByIndustryIds(@Param("industryIds") List<Long> industryIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除默认行业
     *
     * @param industryId 默认行业主键
     * @return 结果
     */
    int deleteIndustryDefaultByIndustryId(@Param("industryId") Long industryId);

    /**
     * 物理批量删除默认行业
     *
     * @param industryIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteIndustryDefaultByIndustryIds(@Param("industryIds") List<Long> industryIds);

    /**
     * 批量新增默认行业
     *
     * @param IndustryDefaults 默认行业列表
     * @return 结果
     */
    int batchIndustryDefault(@Param("industryDefaults") List<IndustryDefault> IndustryDefaults);

    /**
     * 默认行业唯一性校验
     *
     * @param industryCode
     * @return
     */
    IndustryDefaultDTO checkUnique(@Param("industryCode") String industryCode);

    /**
     * 根据父级id查找子级
     *
     * @param industryId
     * @return
     */
    List<Long> selectSon(@Param("industryId") Long industryId);

    /**
     * 根据父级id批量查找子级
     *
     * @param industryIds
     * @return
     */
    List<Long> selectSons(@Param("industryIds") List<Long> industryIds);

    /**
     * 根据ids批量修改子级
     *
     * @param status
     * @param industryIds
     * @return
     */
    int updateStatus(@Param("status") Integer status, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime, @Param("industryIds") List<Long> industryIds);

    /**
     * 根据id集合判断是否存在
     *
     * @return
     */
    List<Long> isExist(@Param("industryIds") List<Long> industryIds);

    /**
     * 返回默认行业树结构
     *
     * @param industryDefault
     * @return
     */
    List<IndustryDefaultDTO> selectIndustryDefaultTreeList(@Param("industryDefault") IndustryDefault industryDefault);
}
