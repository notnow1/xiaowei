package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;


/**
* DictionaryDataService接口
* @author TANGMICHI
* @since 2022-10-15
*/
public interface IDictionaryDataService{
    /**
    * 查询字典数据表
    *
    * @param dictionaryDataId 字典数据表主键
    * @return 字典数据表
    */
    DictionaryDataDTO selectDictionaryDataByDictionaryDataId(Long dictionaryDataId);

    /**
    * 查询字典数据表列表
    *
    * @param dictionaryDataDTO 字典数据表
    * @return 字典数据表集合
    */
    List<DictionaryDataDTO> selectDictionaryDataList(DictionaryDataDTO dictionaryDataDTO);

    /**
    * 新增字典数据表
    *
    * @param dictionaryDataDTO 字典数据表
    * @return 结果
    */
    DictionaryDataDTO insertDictionaryData(DictionaryDataDTO dictionaryDataDTO);

    /**
    * 修改字典数据表
    *
    * @param dictionaryDataDTO 字典数据表
    * @return 结果
    */
    int updateDictionaryData(DictionaryDataDTO dictionaryDataDTO);

    /**
    * 批量修改字典数据表
    *
    * @param dictionaryDataDtos 字典数据表
    * @return 结果
    */
    int updateDictionaryDatas(List<DictionaryDataDTO> dictionaryDataDtos);

    /**
    * 批量新增字典数据表
    *
    * @param dictionaryDataDtos 字典数据表
    * @return 结果
    */
    int insertDictionaryDatas(List<DictionaryDataDTO> dictionaryDataDtos);

    /**
    * 逻辑批量删除字典数据表
    *
    * @param dictionaryDataIds 需要删除的字典数据表集合
    * @return 结果
    */
    int logicDeleteDictionaryDataByDictionaryDataIds(List<Long> dictionaryDataIds);

    /**
    * 逻辑删除字典数据表信息
    *
    * @param dictionaryDataDTO
    * @return 结果
    */
    int logicDeleteDictionaryDataByDictionaryDataId(DictionaryDataDTO dictionaryDataDTO);
    /**
    * 批量删除字典数据表
    *
    * @param DictionaryDataDtos
    * @return 结果
    */
    int deleteDictionaryDataByDictionaryDataIds(List<DictionaryDataDTO> DictionaryDataDtos);

    /**
    * 逻辑删除字典数据表信息
    *
    * @param dictionaryDataDTO
    * @return 结果
    */
    int deleteDictionaryDataByDictionaryDataId(DictionaryDataDTO dictionaryDataDTO);


    /**
    * 删除字典数据表信息
    *
    * @param dictionaryDataId 字典数据表主键
    * @return 结果
    */
    int deleteDictionaryDataByDictionaryDataId(Long dictionaryDataId);
}
