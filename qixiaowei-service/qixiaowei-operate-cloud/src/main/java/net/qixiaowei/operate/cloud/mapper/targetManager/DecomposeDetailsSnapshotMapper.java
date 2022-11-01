package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.targetManager.DecomposeDetailsSnapshot;
import net.qixiaowei.operate.cloud.api.dto.targetManager.DecomposeDetailsSnapshotDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DecomposeDetailsSnapshotMapper接口
* @author TANGMICHI
* @since 2022-10-31
*/
public interface DecomposeDetailsSnapshotMapper{
    /**
    * 查询目标分解详情快照表
    *
    * @param decomposeDetailsSnapshotId 目标分解详情快照表主键
    * @return 目标分解详情快照表
    */
    DecomposeDetailsSnapshotDTO selectDecomposeDetailsSnapshotByDecomposeDetailsSnapshotId(@Param("decomposeDetailsSnapshotId")Long decomposeDetailsSnapshotId);


    /**
     * 根据历史目标分解主键id查询目标分解详情快照表
     *
     * @param targetDecomposeHistoryId 目标分解详情快照表主键
     * @return 目标分解详情快照表
     */
    List<DecomposeDetailsSnapshotDTO> selectDecomposeDetailsSnapshotByTargetDecomposeHistoryId(@Param("targetDecomposeHistoryId")Long targetDecomposeHistoryId);
    /**
    * 批量查询目标分解详情快照表
    *
    * @param decomposeDetailsSnapshotIds 目标分解详情快照表主键集合
    * @return 目标分解详情快照表
    */
    List<DecomposeDetailsSnapshotDTO> selectDecomposeDetailsSnapshotByDecomposeDetailsSnapshotIds(@Param("decomposeDetailsSnapshotIds") List<Long> decomposeDetailsSnapshotIds);

    /**
     * 根据目标分解历史id批量查询目标分解详情快照表
     *
     * @param targetDecomposeHistoryIds 目标分解历史主键集合
     * @return 目标分解详情快照表
     */
    List<DecomposeDetailsSnapshotDTO> selectDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(@Param("targetDecomposeHistoryIds") List<Long> targetDecomposeHistoryIds);
    /**
    * 查询目标分解详情快照表列表
    *
    * @param decomposeDetailsSnapshot 目标分解详情快照表
    * @return 目标分解详情快照表集合
    */
    List<DecomposeDetailsSnapshotDTO> selectDecomposeDetailsSnapshotList(@Param("decomposeDetailsSnapshot")DecomposeDetailsSnapshot decomposeDetailsSnapshot);

    /**
    * 新增目标分解详情快照表
    *
    * @param decomposeDetailsSnapshot 目标分解详情快照表
    * @return 结果
    */
    int insertDecomposeDetailsSnapshot(@Param("decomposeDetailsSnapshot")DecomposeDetailsSnapshot decomposeDetailsSnapshot);

    /**
    * 修改目标分解详情快照表
    *
    * @param decomposeDetailsSnapshot 目标分解详情快照表
    * @return 结果
    */
    int updateDecomposeDetailsSnapshot(@Param("decomposeDetailsSnapshot")DecomposeDetailsSnapshot decomposeDetailsSnapshot);

    /**
    * 批量修改目标分解详情快照表
    *
    * @param decomposeDetailsSnapshotList 目标分解详情快照表
    * @return 结果
    */
    int updateDecomposeDetailsSnapshots(@Param("decomposeDetailsSnapshotList")List<DecomposeDetailsSnapshot> decomposeDetailsSnapshotList);
    /**
    * 逻辑删除目标分解详情快照表
    *
    * @param decomposeDetailsSnapshot
    * @return 结果
    */
    int logicDeleteDecomposeDetailsSnapshotByDecomposeDetailsSnapshotId(@Param("decomposeDetailsSnapshot")DecomposeDetailsSnapshot decomposeDetailsSnapshot);


    /**
    * 逻辑批量删除目标分解详情快照表
    *
    * @param decomposeDetailsSnapshotIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDecomposeDetailsSnapshotByDecomposeDetailsSnapshotIds(@Param("decomposeDetailsSnapshotIds")List<Long> decomposeDetailsSnapshotIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
     * 根据分解历史详情id删除逻辑批量删除目标分解详情快照表
     *
     * @param targetDecomposeHistoryIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(@Param("targetDecomposeHistoryIds")List<Long> targetDecomposeHistoryIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);
    /**
    * 物理删除目标分解详情快照表
    *
    * @param decomposeDetailsSnapshotId 目标分解详情快照表主键
    * @return 结果
    */
    int deleteDecomposeDetailsSnapshotByDecomposeDetailsSnapshotId(@Param("decomposeDetailsSnapshotId")Long decomposeDetailsSnapshotId);

    /**
    * 物理批量删除目标分解详情快照表
    *
    * @param decomposeDetailsSnapshotIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDecomposeDetailsSnapshotByDecomposeDetailsSnapshotIds(@Param("decomposeDetailsSnapshotIds")List<Long> decomposeDetailsSnapshotIds);

    /**
    * 批量新增目标分解详情快照表
    *
    * @param DecomposeDetailsSnapshots 目标分解详情快照表列表
    * @return 结果
    */
    int batchDecomposeDetailsSnapshot(@Param("decomposeDetailsSnapshots")List<DecomposeDetailsSnapshot> DecomposeDetailsSnapshots);
}
