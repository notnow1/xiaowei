package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiIndustryAttractionData;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiIndustryAttractionDataDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiIndustryAttractionDataMapper接口
* @author TANGMICHI
* @since 2023-03-03
*/
public interface MiIndustryAttractionDataMapper{
    /**
    * 查询市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionDataId 市场洞察行业吸引力数据表主键
    * @return 市场洞察行业吸引力数据表
    */
    MiIndustryAttractionDataDTO selectMiIndustryAttractionDataByMiIndustryAttractionDataId(@Param("miIndustryAttractionDataId")Long miIndustryAttractionDataId);


    /**
     * 根据行业吸引力要素主键查询市场洞察行业吸引力数据表
     *
     * @param industryAttractionElementId 行业吸引力要素主键
     * @return 市场洞察行业吸引力数据表
     */
    List<MiIndustryAttractionDataDTO> selectMiIndustryAttractionDataByIndustryAttractionElementId(@Param("industryAttractionElementId")Long industryAttractionElementId);
    /**
     * 根据市场洞察行业详情表主键查询市场洞察行业吸引力数据表
     *
     * @param miIndustryDetailId 市场洞察行业详情表主键
     * @return 市场洞察行业吸引力数据表
     */
    List<MiIndustryAttractionDataDTO> selectMiIndustryAttractionDataByNiIndustryDetailId(@Param("miIndustryDetailId")Long miIndustryDetailId);

    /**
    * 批量查询市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionDataIds 市场洞察行业吸引力数据表主键集合
    * @return 市场洞察行业吸引力数据表
    */
    List<MiIndustryAttractionDataDTO> selectMiIndustryAttractionDataByMiIndustryAttractionDataIds(@Param("miIndustryAttractionDataIds") List<Long> miIndustryAttractionDataIds);

    /**
     * 根据市场洞察行业详情表主键集合批量查询市场洞察行业吸引力数据表
     *
     * @param miIndustryDetailIds 市场洞察行业详情表主键集合
     * @return 市场洞察行业吸引力数据表
     */
    List<MiIndustryAttractionDataDTO> selectMiIndustryAttractionDataByMiIndustryDetailIds(@Param("miIndustryDetailIds")List<Long> miIndustryDetailIds);
    /**
    * 查询市场洞察行业吸引力数据表列表
    *
    * @param miIndustryAttractionData 市场洞察行业吸引力数据表
    * @return 市场洞察行业吸引力数据表集合
    */
    List<MiIndustryAttractionDataDTO> selectMiIndustryAttractionDataList(@Param("miIndustryAttractionData")MiIndustryAttractionData miIndustryAttractionData);

    /**
    * 新增市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionData 市场洞察行业吸引力数据表
    * @return 结果
    */
    int insertMiIndustryAttractionData(@Param("miIndustryAttractionData")MiIndustryAttractionData miIndustryAttractionData);

    /**
    * 修改市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionData 市场洞察行业吸引力数据表
    * @return 结果
    */
    int updateMiIndustryAttractionData(@Param("miIndustryAttractionData")MiIndustryAttractionData miIndustryAttractionData);

    /**
    * 批量修改市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionDataList 市场洞察行业吸引力数据表
    * @return 结果
    */
    int updateMiIndustryAttractionDatas(@Param("miIndustryAttractionDataList")List<MiIndustryAttractionData> miIndustryAttractionDataList);
    /**
    * 逻辑删除市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionData
    * @return 结果
    */
    int logicDeleteMiIndustryAttractionDataByMiIndustryAttractionDataId(@Param("miIndustryAttractionData")MiIndustryAttractionData miIndustryAttractionData);

    /**
    * 逻辑批量删除市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionDataIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiIndustryAttractionDataByMiIndustryAttractionDataIds(@Param("miIndustryAttractionDataIds")List<Long> miIndustryAttractionDataIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionDataId 市场洞察行业吸引力数据表主键
    * @return 结果
    */
    int deleteMiIndustryAttractionDataByMiIndustryAttractionDataId(@Param("miIndustryAttractionDataId")Long miIndustryAttractionDataId);

    /**
    * 物理批量删除市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionDataIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiIndustryAttractionDataByMiIndustryAttractionDataIds(@Param("miIndustryAttractionDataIds")List<Long> miIndustryAttractionDataIds);

    /**
    * 批量新增市场洞察行业吸引力数据表
    *
    * @param miIndustryAttractionDatas 市场洞察行业吸引力数据表列表
    * @return 结果
    */
    int batchMiIndustryAttractionData(@Param("miIndustryAttractionDatas")List<MiIndustryAttractionData> miIndustryAttractionDatas);
}
