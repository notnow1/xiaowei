package net.qixiaowei.system.manage.service.basic;

import net.qixiaowei.system.manage.api.domain.basic.OfficialRankDecompose;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankSystem;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;

import java.util.List;


/**
 * OfficialRankDecomposeService接口
 *
 * @author Graves
 * @since 2022-10-07
 */
public interface IOfficialRankDecomposeService {
    /**
     * 查询职级分解表
     *
     * @param officialRankDecomposeId 职级分解表主键
     * @return 职级分解表
     */
    OfficialRankDecomposeDTO selectOfficialRankDecomposeByOfficialRankDecomposeId(Long officialRankDecomposeId);

    /**
     * 查询职级分解表列表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 职级分解表集合
     */
    List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeList(OfficialRankDecomposeDTO officialRankDecomposeDTO);

    /**
     * 新增职级分解表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    int insertOfficialRankDecompose(OfficialRankDecomposeDTO officialRankDecomposeDTO);

    /**
     * 修改职级分解表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    int updateOfficialRankDecompose(OfficialRankDecomposeDTO officialRankDecomposeDTO);

    /**
     * 批量修改职级分解表
     *
     * @param officialRankDecomposeDtos 职级分解表
     * @return 结果
     */
    int updateOfficialRankDecomposes(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos, OfficialRankSystem officialRankSystem);

    /**
     * 批量新增职级分解表
     *
     * @param officialRankDecomposeDtos
     * @param officialRankSystem
     * @return
     */
    List<OfficialRankDecompose> insertOfficialRankDecomposes(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos, OfficialRankSystem officialRankSystem);

    /**
     * 逻辑批量删除职级分解表
     *
     * @param OfficialRankDecomposeDtos 需要删除的职级分解表集合
     * @return 结果
     */
    int logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(List<OfficialRankDecomposeDTO> OfficialRankDecomposeDtos);

    /**
     * 逻辑删除职级分解表信息
     *
     * @param officialRankDecomposeDTO
     * @return 结果
     */
    int logicDeleteOfficialRankDecomposeByOfficialRankDecomposeId(OfficialRankDecomposeDTO officialRankDecomposeDTO);

    /**
     * 逻辑批量删除职级分解表
     *
     * @param OfficialRankDecomposeDtos 需要删除的职级分解表集合
     * @return 结果
     */
    int deleteOfficialRankDecomposeByOfficialRankDecomposeIds(List<OfficialRankDecomposeDTO> OfficialRankDecomposeDtos);

    /**
     * 逻辑删除职级分解表信息
     *
     * @param officialRankDecomposeDTO
     * @return 结果
     */
    int deleteOfficialRankDecomposeByOfficialRankDecomposeId(OfficialRankDecomposeDTO officialRankDecomposeDTO);


    /**
     * 删除职级分解表信息
     *
     * @param officialRankDecomposeId 职级分解表主键
     * @return 结果
     */
    int deleteOfficialRankDecomposeByOfficialRankDecomposeId(Long officialRankDecomposeId);

    /**
     * 通过officialRankSystemId查找职级分解
     *
     * @param officialRankSystemId
     * @return
     */
    List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeByOfficialRankSystemId(Long officialRankSystemId);

    /**
     * 通过officialRankSystemId查找职级分解
     *
     * @param officialRankSystemId   职级体系id
     * @param rankDecomposeDimension 职级维度
     * @return
     */
    List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeAndNameByOfficialRankSystemId(Long officialRankSystemId, Integer rankDecomposeDimension);

    /**
     * 通过officialRankSystemId查找职级分解
     *
     * @param officialRankSystemIds
     * @return
     */
    List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeBySystemIds(List<Long> officialRankSystemIds);

    /**
     * 通过officialRankSystemId，rankDecomposeDimensionBefore删除rankDecomposeDimension
     *
     * @param officialRankSystemId
     * @param rankDecomposeDimensionBefore
     * @return
     */
    int logicDeleteOfficialRankDecomposeByOfficialRankDecompose(Long officialRankSystemId, Integer rankDecomposeDimensionBefore);

    /**
     * 通过officialRankSystemIds删除职级分解
     *
     * @param officialRankSystemIds
     * @return
     */
    int logicDeleteOfficialRankDecomposeByOfficialRankSystemIds(List<Long> officialRankSystemIds);

    /**
     * 通过officialRankSystemId删除职级分解
     *
     * @param officialRankSystemId
     * @return
     */
    int logicDeleteOfficialRankDecomposeByOfficialRankSystemId(Long officialRankSystemId);
}
