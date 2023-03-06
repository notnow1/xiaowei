package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiIndustryDetail;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiIndustryDetailDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiIndustryDetailMapper接口
* @author TANGMICHI
* @since 2023-03-03
*/
public interface MiIndustryDetailMapper{
    /**
    * 查询市场洞察行业详情表
    *
    * @param miIndustryDetailId 市场洞察行业详情表主键
    * @return 市场洞察行业详情表
    */
    MiIndustryDetailDTO selectMiIndustryDetailByMiIndustryDetailId(@Param("miIndustryDetailId")Long miIndustryDetailId);


    /**
     * 根据市场洞察行业主表主键查询市场洞察行业详情表
     *
     * @param marketInsightIndustryId 市场洞察行业主表主键
     * @return 市场洞察行业详情表
     */
    List<MiIndustryDetailDTO> selectMiIndustryDetailByMarketInsightIndustryId(@Param("marketInsightIndustryId")Long marketInsightIndustryId);
    /**
    * 批量查询市场洞察行业详情表
    *
    * @param miIndustryDetailIds 市场洞察行业详情表主键集合
    * @return 市场洞察行业详情表
    */
    List<MiIndustryDetailDTO> selectMiIndustryDetailByMiIndustryDetailIds(@Param("miIndustryDetailIds") List<Long> miIndustryDetailIds);

    /**
     * 根据市场洞察行业主表主键集合批量查询市场洞察行业详情表
     *
     * @param marketInsightIndustryIds 市场洞察行业主表主键集合
     * @return 市场洞察行业详情表
     */
    List<MiIndustryDetailDTO> selectMiIndustryDetailByMarketInsightIndustryIds(@Param("marketInsightIndustryIds") List<Long> marketInsightIndustryIds);


    /**
    * 查询市场洞察行业详情表列表
    *
    * @param miIndustryDetail 市场洞察行业详情表
    * @return 市场洞察行业详情表集合
    */
    List<MiIndustryDetailDTO> selectMiIndustryDetailList(@Param("miIndustryDetail")MiIndustryDetail miIndustryDetail);

    /**
    * 新增市场洞察行业详情表
    *
    * @param miIndustryDetail 市场洞察行业详情表
    * @return 结果
    */
    int insertMiIndustryDetail(@Param("miIndustryDetail")MiIndustryDetail miIndustryDetail);

    /**
    * 修改市场洞察行业详情表
    *
    * @param miIndustryDetail 市场洞察行业详情表
    * @return 结果
    */
    int updateMiIndustryDetail(@Param("miIndustryDetail")MiIndustryDetail miIndustryDetail);

    /**
    * 批量修改市场洞察行业详情表
    *
    * @param miIndustryDetailList 市场洞察行业详情表
    * @return 结果
    */
    int updateMiIndustryDetails(@Param("miIndustryDetailList")List<MiIndustryDetail> miIndustryDetailList);
    /**
    * 逻辑删除市场洞察行业详情表
    *
    * @param miIndustryDetail
    * @return 结果
    */
    int logicDeleteMiIndustryDetailByMiIndustryDetailId(@Param("miIndustryDetail")MiIndustryDetail miIndustryDetail);

    /**
    * 逻辑批量删除市场洞察行业详情表
    *
    * @param miIndustryDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiIndustryDetailByMiIndustryDetailIds(@Param("miIndustryDetailIds")List<Long> miIndustryDetailIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察行业详情表
    *
    * @param miIndustryDetailId 市场洞察行业详情表主键
    * @return 结果
    */
    int deleteMiIndustryDetailByMiIndustryDetailId(@Param("miIndustryDetailId")Long miIndustryDetailId);

    /**
    * 物理批量删除市场洞察行业详情表
    *
    * @param miIndustryDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiIndustryDetailByMiIndustryDetailIds(@Param("miIndustryDetailIds")List<Long> miIndustryDetailIds);

    /**
    * 批量新增市场洞察行业详情表
    *
    * @param miIndustryDetails 市场洞察行业详情表列表
    * @return 结果
    */
    int batchMiIndustryDetail(@Param("miIndustryDetails")List<MiIndustryDetail> miIndustryDetails);
}
