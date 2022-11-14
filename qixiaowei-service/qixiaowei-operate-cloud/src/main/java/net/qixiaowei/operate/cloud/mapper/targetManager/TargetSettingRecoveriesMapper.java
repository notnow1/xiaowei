package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingRecoveries;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingRecoveriesDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * TargetSettingRecoveriesMapper接口
 *
 * @author Graves
 * @since 2022-11-01
 */
public interface TargetSettingRecoveriesMapper {
    /**
     * 查询目标制定回款集合表
     *
     * @param targetSettingRecoveriesId 目标制定回款集合表主键
     * @return 目标制定回款集合表
     */
    TargetSettingRecoveriesDTO selectTargetSettingRecoveriesByTargetSettingRecoveriesId(@Param("targetSettingRecoveriesId") Long targetSettingRecoveriesId);


    /**
     * 批量查询目标制定回款集合表
     *
     * @param targetSettingRecoveriesIds 目标制定回款集合表主键集合
     * @return 目标制定回款集合表
     */
    List<TargetSettingRecoveriesDTO> selectTargetSettingRecoveriesByTargetSettingRecoveriesIds(@Param("targetSettingRecoveriesIds") List<Long> targetSettingRecoveriesIds);

    /**
     * 查询目标制定回款集合表列表
     *
     * @param targetSettingRecoveries 目标制定回款集合表
     * @return 目标制定回款集合表集合
     */
    List<TargetSettingRecoveriesDTO> selectTargetSettingRecoveriesList(@Param("targetSettingRecoveries") TargetSettingRecoveries targetSettingRecoveries);

    /**
     * 新增目标制定回款集合表
     *
     * @param targetSettingRecoveries 目标制定回款集合表
     * @return 结果
     */
    int insertTargetSettingRecoveries(@Param("targetSettingRecoveries") TargetSettingRecoveries targetSettingRecoveries);

    /**
     * 修改目标制定回款集合表
     *
     * @param targetSettingRecoveries 目标制定回款集合表
     * @return 结果
     */
    int updateTargetSettingRecoveries(@Param("targetSettingRecoveries") TargetSettingRecoveries targetSettingRecoveries);

    /**
     * 批量修改目标制定回款集合表
     *
     * @param targetSettingRecoveriesList 目标制定回款集合表
     * @return 结果
     */
    int updateTargetSettingRecoveriess(@Param("targetSettingRecoveriesList") List<TargetSettingRecoveries> targetSettingRecoveriesList);

    /**
     * 逻辑删除目标制定回款集合表
     *
     * @param targetSettingRecoveries
     * @return 结果
     */
    int logicDeleteTargetSettingRecoveriesByTargetSettingRecoveriesId(@Param("targetSettingRecoveries") TargetSettingRecoveries targetSettingRecoveries);

    /**
     * 逻辑批量删除目标制定回款集合表
     *
     * @param targetSettingRecoveriesIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetSettingRecoveriesByTargetSettingRecoveriesIds(@Param("targetSettingRecoveriesIds") List<Long> targetSettingRecoveriesIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除目标制定回款集合表
     *
     * @param targetSettingRecoveriesId 目标制定回款集合表主键
     * @return 结果
     */
    int deleteTargetSettingRecoveriesByTargetSettingRecoveriesId(@Param("targetSettingRecoveriesId") Long targetSettingRecoveriesId);

    /**
     * 物理批量删除目标制定回款集合表
     *
     * @param targetSettingRecoveriesIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTargetSettingRecoveriesByTargetSettingRecoveriesIds(@Param("targetSettingRecoveriesIds") List<Long> targetSettingRecoveriesIds);

    /**
     * 批量新增目标制定回款集合表
     *
     * @param TargetSettingRecoveriess 目标制定回款集合表列表
     * @return 结果
     */
    int batchTargetSettingRecoveries(@Param("targetSettingRecoveriess") List<TargetSettingRecoveries> TargetSettingRecoveriess);

    /**
     * 通过目标制定ID查找目标回款列表
     *
     * @param targetSettingId 目标制定ID
     * @return
     */
    List<TargetSettingRecoveriesDTO> selectTargetSettingRecoveriesByTargetSettingId(@Param("targetSettingId") Long targetSettingId);

    /**
     * 根据目标制定ID集合查找目标汇款详情表
     *
     * @param targetSettingIds
     * @return
     */
    List<TargetSettingRecoveriesDTO> selectTargetSettingRecoveriesByTargetSettingIds(@Param("targetSettingIds")List<Long> targetSettingIds);
}
