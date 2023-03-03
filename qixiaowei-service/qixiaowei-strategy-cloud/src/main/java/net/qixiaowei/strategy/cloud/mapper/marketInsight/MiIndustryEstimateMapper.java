package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiIndustryEstimate;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiIndustryEstimateDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiIndustryEstimateMapper接口
* @author TANGMICHI
* @since 2023-03-03
*/
public interface MiIndustryEstimateMapper{
    /**
    * 查询市场洞察行业预估表
    *
    * @param miMacroEstimateId 市场洞察行业预估表主键
    * @return 市场洞察行业预估表
    */
    MiIndustryEstimateDTO selectMiIndustryEstimateByMiMacroEstimateId(@Param("miMacroEstimateId")Long miMacroEstimateId);


    /**
    * 批量查询市场洞察行业预估表
    *
    * @param miMacroEstimateIds 市场洞察行业预估表主键集合
    * @return 市场洞察行业预估表
    */
    List<MiIndustryEstimateDTO> selectMiIndustryEstimateByMiMacroEstimateIds(@Param("miMacroEstimateIds") List<Long> miMacroEstimateIds);

    /**
    * 查询市场洞察行业预估表列表
    *
    * @param miIndustryEstimate 市场洞察行业预估表
    * @return 市场洞察行业预估表集合
    */
    List<MiIndustryEstimateDTO> selectMiIndustryEstimateList(@Param("miIndustryEstimate")MiIndustryEstimate miIndustryEstimate);

    /**
    * 新增市场洞察行业预估表
    *
    * @param miIndustryEstimate 市场洞察行业预估表
    * @return 结果
    */
    int insertMiIndustryEstimate(@Param("miIndustryEstimate")MiIndustryEstimate miIndustryEstimate);

    /**
    * 修改市场洞察行业预估表
    *
    * @param miIndustryEstimate 市场洞察行业预估表
    * @return 结果
    */
    int updateMiIndustryEstimate(@Param("miIndustryEstimate")MiIndustryEstimate miIndustryEstimate);

    /**
    * 批量修改市场洞察行业预估表
    *
    * @param miIndustryEstimateList 市场洞察行业预估表
    * @return 结果
    */
    int updateMiIndustryEstimates(@Param("miIndustryEstimateList")List<MiIndustryEstimate> miIndustryEstimateList);
    /**
    * 逻辑删除市场洞察行业预估表
    *
    * @param miIndustryEstimate
    * @return 结果
    */
    int logicDeleteMiIndustryEstimateByMiMacroEstimateId(@Param("miIndustryEstimate")MiIndustryEstimate miIndustryEstimate);

    /**
    * 逻辑批量删除市场洞察行业预估表
    *
    * @param miMacroEstimateIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiIndustryEstimateByMiMacroEstimateIds(@Param("miMacroEstimateIds")List<Long> miMacroEstimateIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察行业预估表
    *
    * @param miMacroEstimateId 市场洞察行业预估表主键
    * @return 结果
    */
    int deleteMiIndustryEstimateByMiMacroEstimateId(@Param("miMacroEstimateId")Long miMacroEstimateId);

    /**
    * 物理批量删除市场洞察行业预估表
    *
    * @param miMacroEstimateIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiIndustryEstimateByMiMacroEstimateIds(@Param("miMacroEstimateIds")List<Long> miMacroEstimateIds);

    /**
    * 批量新增市场洞察行业预估表
    *
    * @param miIndustryEstimates 市场洞察行业预估表列表
    * @return 结果
    */
    int batchMiIndustryEstimate(@Param("miIndustryEstimates")List<MiIndustryEstimate> miIndustryEstimates);
}
