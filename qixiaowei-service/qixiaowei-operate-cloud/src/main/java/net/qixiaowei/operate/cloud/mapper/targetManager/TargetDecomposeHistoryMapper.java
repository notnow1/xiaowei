package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeHistory;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeHistoryDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* TargetDecomposeHistoryMapper接口
* @author TANGMICHI
* @since 2022-10-31
*/
public interface TargetDecomposeHistoryMapper{
    /**
    * 查询目标分解历史版本表
    *
    * @param targetDecomposeHistoryId 目标分解历史版本表主键
    * @return 目标分解历史版本表
    */
    TargetDecomposeHistoryDTO selectTargetDecomposeHistoryByTargetDecomposeHistoryId(@Param("targetDecomposeHistoryId")Long targetDecomposeHistoryId);


    /**
    * 批量查询目标分解历史版本表
    *
    * @param targetDecomposeHistoryIds 目标分解历史版本表主键集合
    * @return 目标分解历史版本表
    */
    List<TargetDecomposeHistoryDTO> selectTargetDecomposeHistoryByTargetDecomposeHistoryIds(@Param("targetDecomposeHistoryIds") List<Long> targetDecomposeHistoryIds);

    /**
     * 工具目标分解id集合批量查询目标分解历史版本表
     *
     * @param targetDecomposeIds 目标分解id集合
     * @return 目标分解历史版本表
     */
    List<TargetDecomposeHistoryDTO> selectTargetDecomposeHistoryByTargetDecomposeIds(@Param("targetDecomposeIds") List<Long> targetDecomposeIds);

    /**
    * 查询目标分解历史版本表列表
    *
    * @param targetDecomposeHistory 目标分解历史版本表
    * @return 目标分解历史版本表集合
    */
    List<TargetDecomposeHistoryDTO> selectTargetDecomposeHistoryList(@Param("targetDecomposeHistory")TargetDecomposeHistory targetDecomposeHistory);

    /**
    * 新增目标分解历史版本表
    *
    * @param targetDecomposeHistory 目标分解历史版本表
    * @return 结果
    */
    int insertTargetDecomposeHistory(@Param("targetDecomposeHistory")TargetDecomposeHistory targetDecomposeHistory);

    /**
    * 修改目标分解历史版本表
    *
    * @param targetDecomposeHistory 目标分解历史版本表
    * @return 结果
    */
    int updateTargetDecomposeHistory(@Param("targetDecomposeHistory")TargetDecomposeHistory targetDecomposeHistory);

    /**
    * 批量修改目标分解历史版本表
    *
    * @param targetDecomposeHistoryList 目标分解历史版本表
    * @return 结果
    */
    int updateTargetDecomposeHistorys(@Param("targetDecomposeHistoryList")List<TargetDecomposeHistory> targetDecomposeHistoryList);
    /**
    * 逻辑删除目标分解历史版本表
    *
    * @param targetDecomposeHistory
    * @return 结果
    */
    int logicDeleteTargetDecomposeHistoryByTargetDecomposeHistoryId(@Param("targetDecomposeHistory")TargetDecomposeHistory targetDecomposeHistory);

    /**
     * 根据目标分解id逻辑删除目标分解历史版本表
     *
     * @param targetDecomposeHistory
     * @return 结果
     */
    int logicDeleteTargetDecomposeHistoryByTargetDecomposeId(@Param("targetDecomposeHistory")TargetDecomposeHistory targetDecomposeHistory);

    /**
    * 逻辑批量删除目标分解历史版本表
    *
    * @param targetDecomposeHistoryIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteTargetDecomposeHistoryByTargetDecomposeHistoryIds(@Param("targetDecomposeHistoryIds")List<Long> targetDecomposeHistoryIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
     * 根据目标分解id集合逻辑批量删除目标分解历史版本表
     *
     * @param targetDecomposeIds 目标分解id集合
     * @return 结果
     */
    int logicDeleteTargetDecomposeHistoryByTargetDecomposeIds(@Param("targetDecomposeIds")List<Long> targetDecomposeIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);
    /**
    * 物理删除目标分解历史版本表
    *
    * @param targetDecomposeHistoryId 目标分解历史版本表主键
    * @return 结果
    */
    int deleteTargetDecomposeHistoryByTargetDecomposeHistoryId(@Param("targetDecomposeHistoryId")Long targetDecomposeHistoryId);

    /**
    * 物理批量删除目标分解历史版本表
    *
    * @param targetDecomposeHistoryIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteTargetDecomposeHistoryByTargetDecomposeHistoryIds(@Param("targetDecomposeHistoryIds")List<Long> targetDecomposeHistoryIds);

    /**
    * 批量新增目标分解历史版本表
    *
    * @param TargetDecomposeHistorys 目标分解历史版本表列表
    * @return 结果
    */
    int batchTargetDecomposeHistory(@Param("targetDecomposeHistorys")List<TargetDecomposeHistory> TargetDecomposeHistorys);
}
