package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.targetManager.DetailCyclesSnapshot;
import net.qixiaowei.operate.cloud.api.dto.targetManager.DetailCyclesSnapshotDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DetailCyclesSnapshotMapper接口
* @author TANGMICHI
* @since 2022-10-31
*/
public interface DetailCyclesSnapshotMapper{
    /**
    * 查询目标分解详情周期快照表
    *
    * @param detailCyclesSnapshotId 目标分解详情周期快照表主键
    * @return 目标分解详情周期快照表
    */
    DetailCyclesSnapshotDTO selectDetailCyclesSnapshotByDetailCyclesSnapshotId(@Param("detailCyclesSnapshotId")Long detailCyclesSnapshotId);


    /**
    * 批量查询目标分解详情周期快照表
    *
    * @param detailCyclesSnapshotIds 目标分解详情周期快照表主键集合
    * @return 目标分解详情周期快照表
    */
    List<DetailCyclesSnapshotDTO> selectDetailCyclesSnapshotByDetailCyclesSnapshotIds(@Param("detailCyclesSnapshotIds") List<Long> detailCyclesSnapshotIds);

    /**
    * 查询目标分解详情周期快照表列表
    *
    * @param detailCyclesSnapshot 目标分解详情周期快照表
    * @return 目标分解详情周期快照表集合
    */
    List<DetailCyclesSnapshotDTO> selectDetailCyclesSnapshotList(@Param("detailCyclesSnapshot")DetailCyclesSnapshot detailCyclesSnapshot);

    /**
    * 新增目标分解详情周期快照表
    *
    * @param detailCyclesSnapshot 目标分解详情周期快照表
    * @return 结果
    */
    int insertDetailCyclesSnapshot(@Param("detailCyclesSnapshot")DetailCyclesSnapshot detailCyclesSnapshot);

    /**
    * 修改目标分解详情周期快照表
    *
    * @param detailCyclesSnapshot 目标分解详情周期快照表
    * @return 结果
    */
    int updateDetailCyclesSnapshot(@Param("detailCyclesSnapshot")DetailCyclesSnapshot detailCyclesSnapshot);

    /**
    * 批量修改目标分解详情周期快照表
    *
    * @param detailCyclesSnapshotList 目标分解详情周期快照表
    * @return 结果
    */
    int updateDetailCyclesSnapshots(@Param("detailCyclesSnapshotList")List<DetailCyclesSnapshot> detailCyclesSnapshotList);
    /**
    * 逻辑删除目标分解详情周期快照表
    *
    * @param detailCyclesSnapshot
    * @return 结果
    */
    int logicDeleteDetailCyclesSnapshotByDetailCyclesSnapshotId(@Param("detailCyclesSnapshot")DetailCyclesSnapshot detailCyclesSnapshot);

    /**
    * 逻辑批量删除目标分解详情周期快照表
    *
    * @param detailCyclesSnapshotIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDetailCyclesSnapshotByDetailCyclesSnapshotIds(@Param("detailCyclesSnapshotIds")List<Long> detailCyclesSnapshotIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
     * 根据目标分解详情历史id逻辑批量删除目标分解详情周期快照表
     *
     * @param decomposeDetailsSnapshotIds 目标分解详情历史id主键集合
     * @return 结果
     */
    int logicDeleteDetailCyclesSnapshotByDecomposeDetailsSnapshotIds(@Param("decomposeDetailsSnapshotIds")List<Long> decomposeDetailsSnapshotIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
    * 物理删除目标分解详情周期快照表
    *
    * @param detailCyclesSnapshotId 目标分解详情周期快照表主键
    * @return 结果
    */
    int deleteDetailCyclesSnapshotByDetailCyclesSnapshotId(@Param("detailCyclesSnapshotId")Long detailCyclesSnapshotId);

    /**
    * 物理批量删除目标分解详情周期快照表
    *
    * @param detailCyclesSnapshotIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDetailCyclesSnapshotByDetailCyclesSnapshotIds(@Param("detailCyclesSnapshotIds")List<Long> detailCyclesSnapshotIds);

    /**
    * 批量新增目标分解详情周期快照表
    *
    * @param DetailCyclesSnapshots 目标分解详情周期快照表列表
    * @return 结果
    */
    int batchDetailCyclesSnapshot(@Param("detailCyclesSnapshots")List<DetailCyclesSnapshot> DetailCyclesSnapshots);
}
