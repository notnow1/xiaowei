package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;


/**
* DictionaryTypeService接口
* @author TANGMICHI
* @since 2022-10-15
*/
public interface IDictionaryTypeService{
    /**
    * 查询字典类型表
    *
    * @param dictionaryTypeId 字典类型表主键
    * @return 字典类型表
    */
    DictionaryTypeDTO selectDictionaryTypeByDictionaryTypeId(Long dictionaryTypeId);

    /**
    * 查询字典类型表列表
    *
    * @param dictionaryTypeDTO 字典类型表
    * @return 字典类型表集合
    */
    List<DictionaryTypeDTO> selectDictionaryTypeList(DictionaryTypeDTO dictionaryTypeDTO);

    /**
    * 新增字典类型表
    *
    * @param dictionaryTypeDTO 字典类型表
    * @return 结果
    */
    DictionaryTypeDTO insertDictionaryType(DictionaryTypeDTO dictionaryTypeDTO);

    /**
    * 修改字典类型表
    *
    * @param dictionaryTypeDTO 字典类型表
    * @return 结果
    */
    int updateDictionaryType(DictionaryTypeDTO dictionaryTypeDTO);

    /**
    * 批量修改字典类型表
    *
    * @param dictionaryTypeDtos 字典类型表
    * @return 结果
    */
    int updateDictionaryTypes(List<DictionaryTypeDTO> dictionaryTypeDtos);

    /**
    * 批量新增字典类型表
    *
    * @param dictionaryTypeDtos 字典类型表
    * @return 结果
    */
    int insertDictionaryTypes(List<DictionaryTypeDTO> dictionaryTypeDtos);

    /**
    * 逻辑批量删除字典类型表
    *
    * @param dictionaryTypeIds 需要删除的字典类型表集合
    * @return 结果
    */
    int logicDeleteDictionaryTypeByDictionaryTypeIds(List<Long> dictionaryTypeIds);

    /**
    * 逻辑删除字典类型表信息
    *
    * @param dictionaryTypeDTO
    * @return 结果
    */
    int logicDeleteDictionaryTypeByDictionaryTypeId(DictionaryTypeDTO dictionaryTypeDTO);
    /**
    * 批量删除字典类型表
    *
    * @param DictionaryTypeDtos
    * @return 结果
    */
    int deleteDictionaryTypeByDictionaryTypeIds(List<DictionaryTypeDTO> DictionaryTypeDtos);

    /**
    * 逻辑删除字典类型表信息
    *
    * @param dictionaryTypeDTO
    * @return 结果
    */
    int deleteDictionaryTypeByDictionaryTypeId(DictionaryTypeDTO dictionaryTypeDTO);


    /**
    * 删除字典类型表信息
    *
    * @param dictionaryTypeId 字典类型表主键
    * @return 结果
    */
    int deleteDictionaryTypeByDictionaryTypeId(Long dictionaryTypeId);
}
