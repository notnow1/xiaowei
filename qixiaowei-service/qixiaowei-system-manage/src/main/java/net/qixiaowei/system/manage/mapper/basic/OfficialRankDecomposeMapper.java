package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.basic.OfficialRankDecompose;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * OfficialRankDecomposeMapper接口
 *
 * @author Graves
 * @since 2022-10-07
 */
public interface OfficialRankDecomposeMapper {
    /**
     * 查询职级分解表
     *
     * @param officialRankDecomposeId 职级分解表主键
     * @return 职级分解表
     */
    OfficialRankDecomposeDTO selectOfficialRankDecomposeByOfficialRankDecomposeId(@Param("officialRankDecomposeId") Long officialRankDecomposeId);

    /**
     * 查询职级分解表列表
     *
     * @param officialRankDecompose 职级分解表
     * @return 职级分解表集合
     */
    List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeList(@Param("officialRankDecompose") OfficialRankDecompose officialRankDecompose);

    /**
     * 新增职级分解表
     *
     * @param officialRankDecompose 职级分解表
     * @return 结果
     */
    int insertOfficialRankDecompose(@Param("officialRankDecompose") OfficialRankDecompose officialRankDecompose);

    /**
     * 修改职级分解表
     *
     * @param officialRankDecompose 职级分解表
     * @return 结果
     */
    int updateOfficialRankDecompose(@Param("officialRankDecompose") OfficialRankDecompose officialRankDecompose);

    /**
     * 批量修改职级分解表
     *
     * @param officialRankDecomposeList 职级分解表
     * @return 结果
     */
    int updateOfficialRankDecomposes(@Param("officialRankDecomposeList") List<OfficialRankDecompose> officialRankDecomposeList);

    /**
     * 逻辑删除职级分解表
     *
     * @param officialRankDecompose
     * @return 结果
     */
    int logicDeleteOfficialRankDecomposeByOfficialRankDecomposeId(@Param("officialRankDecompose") OfficialRankDecompose officialRankDecompose, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除职级分解表
     *
     * @param officialRankDecomposeIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(@Param("officialRankDecomposeIds") List<Long> officialRankDecomposeIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除职级分解表
     *
     * @param officialRankDecomposeId 职级分解表主键
     * @return 结果
     */
    int deleteOfficialRankDecomposeByOfficialRankDecomposeId(@Param("officialRankDecomposeId") Long officialRankDecomposeId);

    /**
     * 物理批量删除职级分解表
     *
     * @param officialRankDecomposeIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteOfficialRankDecomposeByOfficialRankDecomposeIds(@Param("officialRankDecomposeIds") List<Long> officialRankDecomposeIds);

    /**
     * 批量新增职级分解表
     *
     * @param OfficialRankDecomposes 职级分解表列表
     * @return 结果
     */
    int batchOfficialRankDecompose(@Param("officialRankDecomposes") List<OfficialRankDecompose> OfficialRankDecomposes);

    /**
     * 根据officialRankSystemId查询
     *
     * @param officialRankSystemId
     * @return
     */
    List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeByOfficialRankSystemId(@Param("officialRankSystemId") Long officialRankSystemId);

    /**
     * 校验当前职级分解是否存在
     *
     * @param officialRankDecomposeIds
     * @return
     */
    int isExist(@Param("officialRankDecomposeIds") List<Long> officialRankDecomposeIds);

    /**
     * 通过officialRankSystemId，rankDecomposeDimensionBefore删除rankDecomposeDimension
     *
     * @param officialRankSystemId
     * @param rankDecomposeDimension
     * @return
     */
    int logicDeleteOfficialRankDecomposeByOfficialRankDecompose(@Param("officialRankSystemId") Long officialRankSystemId, @Param("rankDecomposeDimension") Integer rankDecomposeDimension);

    /**
     * 通过职级体系ids查找职级分解ids
     *
     * @param officialRankSystemIds
     * @return
     */
    List<Long> selectOfficialRankDecomposeByOfficialRankSystemIds(@Param("officialRankSystemIds") List<Long> officialRankSystemIds);

    /**
     * 根据officialRankSystemId删除officialRankDecomposeDTO
     *
     * @param officialRankSystemId
     * @param updateBy
     * @param updateTime
     * @return
     */
    int logicDeleteOfficialRankDecomposeByOfficialSystemId(@Param("officialRankSystemId") Long officialRankSystemId, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);
}
