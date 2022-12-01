package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.OfficialRankEmolument;
import net.qixiaowei.operate.cloud.api.dto.salary.OfficialRankEmolumentDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* OfficialRankEmolumentMapper接口
* @author Graves
* @since 2022-11-30
*/
public interface OfficialRankEmolumentMapper{
    /**
    * 查询职级薪酬表
    *
    * @param officialRankEmolumentId 职级薪酬表主键
    * @return 职级薪酬表
    */
    OfficialRankEmolumentDTO selectOfficialRankEmolumentByOfficialRankEmolumentId(@Param("officialRankEmolumentId")Long officialRankEmolumentId);


    /**
    * 批量查询职级薪酬表
    *
    * @param officialRankEmolumentIds 职级薪酬表主键集合
    * @return 职级薪酬表
    */
    List<OfficialRankEmolumentDTO> selectOfficialRankEmolumentByOfficialRankEmolumentIds(@Param("officialRankEmolumentIds") List<Long> officialRankEmolumentIds);

    /**
    * 查询职级薪酬表列表
    *
    * @param officialRankEmolument 职级薪酬表
    * @return 职级薪酬表集合
    */
    List<OfficialRankEmolumentDTO> selectOfficialRankEmolumentList(@Param("officialRankEmolument")OfficialRankEmolument officialRankEmolument);

    /**
    * 新增职级薪酬表
    *
    * @param officialRankEmolument 职级薪酬表
    * @return 结果
    */
    int insertOfficialRankEmolument(@Param("officialRankEmolument")OfficialRankEmolument officialRankEmolument);

    /**
    * 修改职级薪酬表
    *
    * @param officialRankEmolument 职级薪酬表
    * @return 结果
    */
    int updateOfficialRankEmolument(@Param("officialRankEmolument")OfficialRankEmolument officialRankEmolument);

    /**
    * 批量修改职级薪酬表
    *
    * @param officialRankEmolumentList 职级薪酬表
    * @return 结果
    */
    int updateOfficialRankEmoluments(@Param("officialRankEmolumentList")List<OfficialRankEmolument> officialRankEmolumentList);
    /**
    * 逻辑删除职级薪酬表
    *
    * @param officialRankEmolument
    * @return 结果
    */
    int logicDeleteOfficialRankEmolumentByOfficialRankEmolumentId(@Param("officialRankEmolument")OfficialRankEmolument officialRankEmolument);

    /**
    * 逻辑批量删除职级薪酬表
    *
    * @param officialRankEmolumentIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteOfficialRankEmolumentByOfficialRankEmolumentIds(@Param("officialRankEmolumentIds")List<Long> officialRankEmolumentIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除职级薪酬表
    *
    * @param officialRankEmolumentId 职级薪酬表主键
    * @return 结果
    */
    int deleteOfficialRankEmolumentByOfficialRankEmolumentId(@Param("officialRankEmolumentId")Long officialRankEmolumentId);

    /**
    * 物理批量删除职级薪酬表
    *
    * @param officialRankEmolumentIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteOfficialRankEmolumentByOfficialRankEmolumentIds(@Param("officialRankEmolumentIds")List<Long> officialRankEmolumentIds);

    /**
    * 批量新增职级薪酬表
    *
    * @param OfficialRankEmoluments 职级薪酬表列表
    * @return 结果
    */
    int batchOfficialRankEmolument(@Param("officialRankEmoluments")List<OfficialRankEmolument> OfficialRankEmoluments);

    /**
     * 通过职级ID获取职级薪酬表
     * @param officialRankSystemId 职级ID
     * @return
     */
    List<OfficialRankEmolumentDTO> selectOfficialRankEmolumentBySystemId(Long officialRankSystemId);
}
