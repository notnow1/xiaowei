package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.NationDTO;


/**
* NationService接口
* @author TANGMICHI
* @since 2022-10-20
*/
public interface INationService{
    /**
    * 查询民族表
    *
    * @param nationId 民族表主键
    * @return 民族表
    */
    NationDTO selectNationByNationId(Long nationId);

    /**
    * 查询民族表列表
    *
    * @param nationDTO 民族表
    * @return 民族表集合
    */
    List<NationDTO> selectNationList(NationDTO nationDTO);

    /**
    * 新增民族表
    *
    * @param nationDTO 民族表
    * @return 结果
    */
    NationDTO insertNation(NationDTO nationDTO);

    /**
    * 修改民族表
    *
    * @param nationDTO 民族表
    * @return 结果
    */
    int updateNation(NationDTO nationDTO);

    /**
    * 批量修改民族表
    *
    * @param nationDtos 民族表
    * @return 结果
    */
    int updateNations(List<NationDTO> nationDtos);

    /**
    * 批量新增民族表
    *
    * @param nationDtos 民族表
    * @return 结果
    */
    int insertNations(List<NationDTO> nationDtos);

    /**
    * 逻辑批量删除民族表
    *
    * @param nationIds 需要删除的民族表集合
    * @return 结果
    */
    int logicDeleteNationByNationIds(List<Long> nationIds);

    /**
    * 逻辑删除民族表信息
    *
    * @param nationDTO
    * @return 结果
    */
    int logicDeleteNationByNationId(NationDTO nationDTO);
    /**
    * 批量删除民族表
    *
    * @param NationDtos
    * @return 结果
    */
    int deleteNationByNationIds(List<NationDTO> NationDtos);

    /**
    * 逻辑删除民族表信息
    *
    * @param nationDTO
    * @return 结果
    */
    int deleteNationByNationId(NationDTO nationDTO);


    /**
    * 删除民族表信息
    *
    * @param nationId 民族表主键
    * @return 结果
    */
    int deleteNationByNationId(Long nationId);
}
