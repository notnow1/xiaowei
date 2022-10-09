package net.qixiaowei.system.manage.service.basic;

import java.util.List;

import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;


/**
 * OfficialRankSystemService接口
 *
 * @author Graves
 * @since 2022-10-07
 */
public interface IOfficialRankSystemService {
    /**
     * 查询职级体系表
     *
     * @param officialRankSystemId 职级体系表主键
     * @return 职级体系表
     */
    OfficialRankSystemDTO selectOfficialRankSystemByOfficialRankSystemId(Long officialRankSystemId);

    /**
     * 查询职级体系表列表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 职级体系表集合
     */
    List<OfficialRankSystemDTO> selectOfficialRankSystemList(OfficialRankSystemDTO officialRankSystemDTO);

    /**
     * 新增职级体系表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    int insertOfficialRankSystem(OfficialRankSystemDTO officialRankSystemDTO);

    /**
     * 修改职级体系表
     *
     * @param officialRankSystemDTO 职级体系表
     * @return 结果
     */
    int updateOfficialRankSystem(OfficialRankSystemDTO officialRankSystemDTO);

    /**
     * 批量修改职级体系表
     *
     * @param officialRankSystemDtos 职级体系表
     * @return 结果
     */
    int updateOfficialRankSystems(List<OfficialRankSystemDTO> officialRankSystemDtos);

    /**
     * 批量新增职级体系表
     *
     * @param officialRankSystemDtos 职级体系表
     * @return 结果
     */
    int insertOfficialRankSystems(List<OfficialRankSystemDTO> officialRankSystemDtos);

    /**
     * 逻辑批量删除职级体系表
     *
     * @param OfficialRankSystemDtos 需要删除的职级体系表集合
     * @return 结果
     */
    int logicDeleteOfficialRankSystemByOfficialRankSystemIds(List<OfficialRankSystemDTO> OfficialRankSystemDtos);

    /**
     * 逻辑删除职级体系表信息
     *
     * @param officialRankSystemDTO
     * @return 结果
     */
    int logicDeleteOfficialRankSystemByOfficialRankSystemId(OfficialRankSystemDTO officialRankSystemDTO);

    /**
     * 逻辑批量删除职级体系表
     *
     * @param OfficialRankSystemDtos 需要删除的职级体系表集合
     * @return 结果
     */
    int deleteOfficialRankSystemByOfficialRankSystemIds(List<OfficialRankSystemDTO> OfficialRankSystemDtos);

    /**
     * 逻辑删除职级体系表信息
     *
     * @param officialRankSystemDTO
     * @return 结果
     */
    int deleteOfficialRankSystemByOfficialRankSystemId(OfficialRankSystemDTO officialRankSystemDTO);


    /**
     * 删除职级体系表信息
     *
     * @param officialRankSystemId 职级体系表主键
     * @return 结果
     */
    int deleteOfficialRankSystemByOfficialRankSystemId(Long officialRankSystemId);

    /**
     * 职级体系表详情
     *
     * @param officialRankSystemDTO
     * @return
     */
    OfficialRankSystemDTO detailOfficialRankSystem(OfficialRankSystemDTO officialRankSystemDTO);
}
