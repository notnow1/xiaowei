package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryType;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DictionaryTypeMapper接口
* @author TANGMICHI
* @since 2022-10-07
*/
public interface DictionaryTypeMapper{
    /**
    * 查询字典类型表
    *
    * @param dictionaryTypeId 字典类型表主键
    * @return 字典类型表
    */
    DictionaryTypeDTO selectDictionaryTypeByDictionaryTypeId(@Param("dictionaryTypeId")Long dictionaryTypeId);

    /**
    * 查询字典类型表列表
    *
    * @param dictionaryType 字典类型表
    * @return 字典类型表集合
    */
    List<DictionaryTypeDTO> selectDictionaryTypeList(@Param("dictionaryType")DictionaryType dictionaryType);

    /**
    * 新增字典类型表
    *
    * @param dictionaryType 字典类型表
    * @return 结果
    */
    int insertDictionaryType(@Param("dictionaryType")DictionaryType dictionaryType);

    /**
    * 修改字典类型表
    *
    * @param dictionaryType 字典类型表
    * @return 结果
    */
    int updateDictionaryType(@Param("dictionaryType")DictionaryType dictionaryType);

    /**
    * 批量修改字典类型表
    *
    * @param dictionaryTypeList 字典类型表
    * @return 结果
    */
    int updateDictionaryTypes(@Param("dictionaryTypeList")List<DictionaryType> dictionaryTypeList);
    /**
    * 逻辑删除字典类型表
    *
    * @param dictionaryType
    * @return 结果
    */
    int logicDeleteDictionaryTypeByDictionaryTypeId(@Param("dictionaryType")DictionaryType dictionaryType,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 逻辑批量删除字典类型表
    *
    * @param dictionaryTypeIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDictionaryTypeByDictionaryTypeIds(@Param("dictionaryTypeIds")List<Long> dictionaryTypeIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除字典类型表
    *
    * @param dictionaryTypeId 字典类型表主键
    * @return 结果
    */
    int deleteDictionaryTypeByDictionaryTypeId(@Param("dictionaryTypeId")Long dictionaryTypeId);

    /**
    * 物理批量删除字典类型表
    *
    * @param dictionaryTypeIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDictionaryTypeByDictionaryTypeIds(@Param("dictionaryTypeIds")List<Long> dictionaryTypeIds);

    /**
    * 批量新增字典类型表
    *
    * @param DictionaryTypes 字典类型表列表
    * @return 结果
    */
    int batchDictionaryType(@Param("dictionaryTypes")List<DictionaryType> DictionaryTypes);
}
