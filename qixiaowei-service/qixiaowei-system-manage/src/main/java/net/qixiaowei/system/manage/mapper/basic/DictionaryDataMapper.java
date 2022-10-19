package net.qixiaowei.system.manage.mapper.basic;

import java.util.List;
import net.qixiaowei.system.manage.api.domain.basic.DictionaryData;
import net.qixiaowei.system.manage.api.dto.basic.DictionaryDataDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DictionaryDataMapper接口
* @author TANGMICHI
* @since 2022-10-15
*/
public interface DictionaryDataMapper{
    /**
    * 查询字典数据表
    *
    * @param dictionaryDataId 字典数据表主键
    * @return 字典数据表
    */
    DictionaryDataDTO selectDictionaryDataByDictionaryDataId(@Param("dictionaryDataId")Long dictionaryDataId);


    /**
    * 批量查询字典数据表
    *
    * @param dictionaryDataIds 字典数据表主键集合
    * @return 字典数据表
    */
    List<DictionaryDataDTO> selectDictionaryDataByDictionaryDataIds(@Param("dictionaryDataIds") List<Long> dictionaryDataIds);

    /**
    * 查询字典数据表列表
    *
    * @param dictionaryData 字典数据表
    * @return 字典数据表集合
    */
    List<DictionaryDataDTO> selectDictionaryDataList(@Param("dictionaryData")DictionaryData dictionaryData);

    /**
    * 新增字典数据表
    *
    * @param dictionaryData 字典数据表
    * @return 结果
    */
    int insertDictionaryData(@Param("dictionaryData")DictionaryData dictionaryData);

    /**
    * 修改字典数据表
    *
    * @param dictionaryData 字典数据表
    * @return 结果
    */
    int updateDictionaryData(@Param("dictionaryData")DictionaryData dictionaryData);

    /**
    * 批量修改字典数据表
    *
    * @param dictionaryDataList 字典数据表
    * @return 结果
    */
    int updateDictionaryDatas(@Param("dictionaryDataList")List<DictionaryData> dictionaryDataList);
    /**
    * 逻辑删除字典数据表
    *
    * @param dictionaryData
    * @return 结果
    */
    int logicDeleteDictionaryDataByDictionaryDataId(@Param("dictionaryData")DictionaryData dictionaryData);

    /**
    * 逻辑批量删除字典数据表
    *
    * @param dictionaryDataIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDictionaryDataByDictionaryDataIds(@Param("dictionaryDataIds")List<Long> dictionaryDataIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除字典数据表
    *
    * @param dictionaryDataId 字典数据表主键
    * @return 结果
    */
    int deleteDictionaryDataByDictionaryDataId(@Param("dictionaryDataId")Long dictionaryDataId);

    /**
    * 物理批量删除字典数据表
    *
    * @param dictionaryDataIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDictionaryDataByDictionaryDataIds(@Param("dictionaryDataIds")List<Long> dictionaryDataIds);

    /**
    * 批量新增字典数据表
    *
    * @param DictionaryDatas 字典数据表列表
    * @return 结果
    */
    int batchDictionaryData(@Param("dictionaryDatas")List<DictionaryData> DictionaryDatas);

    /**
     * 根据type Id查询数据做排序 自增
     * @param dictionaryTypeId
     * @return
     */
    int selectDictionaryTypeId( @Param("dictionaryTypeId")Long dictionaryTypeId);
}
