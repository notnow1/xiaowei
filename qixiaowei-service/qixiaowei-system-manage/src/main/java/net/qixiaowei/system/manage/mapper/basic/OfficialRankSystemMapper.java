package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.basic.OfficialRankSystem;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * OfficialRankSystemMapper接口
 *
 * @author Graves
 * @since 2022-10-07
 */
public interface OfficialRankSystemMapper {
    /**
     * 查询职级体系表
     *
     * @param officialRankSystemId 职级体系表主键
     * @return 职级体系表
     */
    OfficialRankSystemDTO selectOfficialRankSystemByOfficialRankSystemId(@Param("officialRankSystemId") Long officialRankSystemId);

    /**
     * 查询职级体系表列表
     *
     * @param officialRankSystem 职级体系表
     * @return 职级体系表集合
     */
    List<OfficialRankSystemDTO> selectOfficialRankSystemList(@Param("officialRankSystem") OfficialRankSystem officialRankSystem);

    /**
     * 新增职级体系表
     *
     * @param officialRankSystem 职级体系表
     * @return 结果
     */
    int insertOfficialRankSystem(@Param("officialRankSystem") OfficialRankSystem officialRankSystem);

    /**
     * 修改职级体系表
     *
     * @param officialRankSystem 职级体系表
     * @return 结果
     */
    int updateOfficialRankSystem(@Param("officialRankSystem") OfficialRankSystem officialRankSystem);

    /**
     * 批量修改职级体系表
     *
     * @param officialRankSystemList 职级体系表
     * @return 结果
     */
    int updateOfficialRankSystems(@Param("officialRankSystemList") List<OfficialRankSystem> officialRankSystemList);

    /**
     * 逻辑删除职级体系表
     *
     * @param officialRankSystem
     * @return 结果
     */
    int logicDeleteOfficialRankSystemByOfficialRankSystemId(@Param("officialRankSystem") OfficialRankSystem officialRankSystem, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除职级体系表
     *
     * @param officialRankSystemIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteOfficialRankSystemByOfficialRankSystemIds(@Param("officialRankSystemIds") List<Long> officialRankSystemIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 物理删除职级体系表
     *
     * @param officialRankSystemId 职级体系表主键
     * @return 结果
     */
    int deleteOfficialRankSystemByOfficialRankSystemId(@Param("officialRankSystemId") Long officialRankSystemId);

    /**
     * 物理批量删除职级体系表
     *
     * @param officialRankSystemIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteOfficialRankSystemByOfficialRankSystemIds(@Param("officialRankSystemIds") List<Long> officialRankSystemIds);

    /**
     * 批量新增职级体系表
     *
     * @param OfficialRankSystems 职级体系表列表
     * @return 结果
     */
    int batchOfficialRankSystem(@Param("officialRankSystems") List<OfficialRankSystem> OfficialRankSystems);

    /**
     * 职级体系名称重复校验
     *
     * @param officialRankSystemName
     * @return
     */
    int officialRankNameCheckUnique(@Param("officialRankSystemName") String officialRankSystemName);

    /**
     * 职级体系前缀重复校验
     *
     * @param rankPrefixCode
     * @return
     */
    int rankPrefixCodeCheckUnique(@Param("rankPrefixCode") String rankPrefixCode);

    /**
     * 根据officialRankSystemId判断该职级体系是否存在
     *
     * @param officialRankSystemId
     * @return
     */
    OfficialRankSystemDTO isExistByOfficialRankSystemId(@Param("officialRankSystemId") Long officialRankSystemId);

    /**
     * 根据officialRankSystemIds判断该职级体系是否存在
     *
     * @param officialRankSystemIds
     * @return
     */
    List<OfficialRankSystemDTO> isExistByOfficialRankSystemIds(@Param("officialRankSystemIds") List<Long> officialRankSystemIds);
}
