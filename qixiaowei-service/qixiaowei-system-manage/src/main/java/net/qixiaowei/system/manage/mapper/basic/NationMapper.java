package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.basic.Nation;
import net.qixiaowei.system.manage.api.dto.basic.NationDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* NationMapper接口
* @author TANGMICHI
* @since 2022-10-20
*/
public interface NationMapper{
    /**
    * 查询民族表
    *
    * @param nationId 民族表主键
    * @return 民族表
    */
    NationDTO selectNationByNationId(@Param("nationId")Long nationId);


    /**
    * 批量查询民族表
    *
    * @param nationIds 民族表主键集合
    * @return 民族表
    */
    List<NationDTO> selectNationByNationIds(@Param("nationIds") List<Long> nationIds);

    /**
    * 查询民族表列表
    *
    * @param nation 民族表
    * @return 民族表集合
    */
    List<NationDTO> selectNationList(@Param("nation")Nation nation);

    /**
    * 新增民族表
    *
    * @param nation 民族表
    * @return 结果
    */
    int insertNation(@Param("nation")Nation nation);

    /**
    * 修改民族表
    *
    * @param nation 民族表
    * @return 结果
    */
    int updateNation(@Param("nation")Nation nation);

    /**
    * 批量修改民族表
    *
    * @param nationList 民族表
    * @return 结果
    */
    int updateNations(@Param("nationList")List<Nation> nationList);
    /**
    * 逻辑删除民族表
    *
    * @param nation
    * @return 结果
    */
    int logicDeleteNationByNationId(@Param("nation")Nation nation);

    /**
    * 逻辑批量删除民族表
    *
    * @param nationIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteNationByNationIds(@Param("nationIds")List<Long> nationIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除民族表
    *
    * @param nationId 民族表主键
    * @return 结果
    */
    int deleteNationByNationId(@Param("nationId")Long nationId);

    /**
    * 物理批量删除民族表
    *
    * @param nationIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteNationByNationIds(@Param("nationIds")List<Long> nationIds);

    /**
    * 批量新增民族表
    *
    * @param Nations 民族表列表
    * @return 结果
    */
    int batchNation(@Param("nations")List<Nation> Nations);
}
