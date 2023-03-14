package net.qixiaowei.system.manage.service.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryTypeDTO;


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
     * 查询字典数据表
     *
     * @param dictionaryDataIds 字典数据表主键集合
     * @return 字典数据表
     */
    List<DictionaryDataDTO> selectDictionaryDataByDictionaryDataIds(List<Long> dictionaryDataIds);
    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<DictionaryDataDTO> result);
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

    /**
     * 根据枚举查询产品应用字典名称数据
     * @return
     * @param dictionaryType
     */
    DictionaryTypeDTO selectDictionaryTypeByProduct(String dictionaryType);

    /**
     * 根据typeId查询字典数据
     * @return
     * @param dictionaryTypeId
     */
    List<DictionaryDataDTO> selectDictionaryDataByProduct(Long dictionaryTypeId);

    /**
     * 根据实体类远程查询字典数据
     * @param dictionaryDataDTO
     * @return
     */
    List<DictionaryDataDTO> remoteDictionaryDataId(DictionaryDataDTO dictionaryDataDTO);
}
