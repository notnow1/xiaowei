package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiMacroEstimate;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiMacroEstimateDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiMacroEstimateMapper接口
* @author TANGMICHI
* @since 2023-02-28
*/
public interface MiMacroEstimateMapper{
    /**
    * 查询市场洞察宏观预估表
    *
    * @param miMacroEstimateId 市场洞察宏观预估表主键
    * @return 市场洞察宏观预估表
    */
    MiMacroEstimateDTO selectMiMacroEstimateByMiMacroEstimateId(@Param("miMacroEstimateId")Long miMacroEstimateId);
    /**
     * 根据市场洞察宏观详情表主键查询市场洞察宏观预估表
     *
     * @param miMacroDetailId 市场洞察宏观详情表主键
     * @return 市场洞察宏观预估表
     */
    List<MiMacroEstimateDTO> selectMiMacroEstimateByMiMacroDetailId(@Param("miMacroDetailId")Long miMacroDetailId);


    /**
    * 批量查询市场洞察宏观预估表
    *
    * @param miMacroEstimateIds 市场洞察宏观预估表主键集合
    * @return 市场洞察宏观预估表
    */
    List<MiMacroEstimateDTO> selectMiMacroEstimateByMiMacroEstimateIds(@Param("miMacroEstimateIds") List<Long> miMacroEstimateIds);

    /**
     * 根据市场洞察宏观详情表主键集合批量查询市场洞察宏观预估表
     *
     * @param miMacroDetailIds 市场洞察宏观详情表主键集合
     * @return 市场洞察宏观预估表
     */
    List<MiMacroEstimateDTO> selectMiMacroEstimateByMiMacroDetailIds(@Param("miMacroDetailIds") List<Long> miMacroDetailIds);

    /**
    * 查询市场洞察宏观预估表列表
    *
    * @param miMacroEstimate 市场洞察宏观预估表
    * @return 市场洞察宏观预估表集合
    */
    List<MiMacroEstimateDTO> selectMiMacroEstimateList(@Param("miMacroEstimate")MiMacroEstimate miMacroEstimate);

    /**
    * 新增市场洞察宏观预估表
    *
    * @param miMacroEstimate 市场洞察宏观预估表
    * @return 结果
    */
    int insertMiMacroEstimate(@Param("miMacroEstimate")MiMacroEstimate miMacroEstimate);

    /**
    * 修改市场洞察宏观预估表
    *
    * @param miMacroEstimate 市场洞察宏观预估表
    * @return 结果
    */
    int updateMiMacroEstimate(@Param("miMacroEstimate")MiMacroEstimate miMacroEstimate);

    /**
    * 批量修改市场洞察宏观预估表
    *
    * @param miMacroEstimateList 市场洞察宏观预估表
    * @return 结果
    */
    int updateMiMacroEstimates(@Param("miMacroEstimateList")List<MiMacroEstimate> miMacroEstimateList);
    /**
    * 逻辑删除市场洞察宏观预估表
    *
    * @param miMacroEstimate
    * @return 结果
    */
    int logicDeleteMiMacroEstimateByMiMacroEstimateId(@Param("miMacroEstimate")MiMacroEstimate miMacroEstimate);

    /**
    * 逻辑批量删除市场洞察宏观预估表
    *
    * @param miMacroEstimateIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiMacroEstimateByMiMacroEstimateIds(@Param("miMacroEstimateIds")List<Long> miMacroEstimateIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察宏观预估表
    *
    * @param miMacroEstimateId 市场洞察宏观预估表主键
    * @return 结果
    */
    int deleteMiMacroEstimateByMiMacroEstimateId(@Param("miMacroEstimateId")Long miMacroEstimateId);

    /**
    * 物理批量删除市场洞察宏观预估表
    *
    * @param miMacroEstimateIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiMacroEstimateByMiMacroEstimateIds(@Param("miMacroEstimateIds")List<Long> miMacroEstimateIds);

    /**
    * 批量新增市场洞察宏观预估表
    *
    * @param miMacroEstimates 市场洞察宏观预估表列表
    * @return 结果
    */
    int batchMiMacroEstimate(@Param("miMacroEstimates")List<MiMacroEstimate> miMacroEstimates);
}
