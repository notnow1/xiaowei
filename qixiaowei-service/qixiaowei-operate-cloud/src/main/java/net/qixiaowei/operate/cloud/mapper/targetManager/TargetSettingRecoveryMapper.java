package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingRecovery;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingRecoveryDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * TargetSettingRecoveryMapper接口
 *
 * @author Graves
 * @since 2022-11-01
 */
public interface TargetSettingRecoveryMapper {
    /**
     * 查询目标制定回款表
     *
     * @param targetSettingRecoveriesId 目标制定回款表主键
     * @return 目标制定回款表
     */
    TargetSettingRecoveryDTO selectTargetSettingRecoveryByTargetSettingRecoveriesId(@Param("targetSettingRecoveriesId") Long targetSettingRecoveriesId);


    /**
     * 批量查询目标制定回款表
     *
     * @param targetSettingRecoveriesIds 目标制定回款表主键集合
     * @return 目标制定回款表
     */
    List<TargetSettingRecoveryDTO> selectTargetSettingRecoveryByTargetSettingRecoveriesIds(@Param("targetSettingRecoveriesIds") List<Long> targetSettingRecoveriesIds);

    /**
     * 查询目标制定回款表列表
     *
     * @param targetSettingRecovery 目标制定回款表
     * @return 目标制定回款表集合
     */
    List<TargetSettingRecoveryDTO> selectTargetSettingRecoveryList(@Param("targetSettingRecovery") TargetSettingRecovery targetSettingRecovery);

    /**
     * 新增目标制定回款表
     *
     * @param targetSettingRecovery 目标制定回款表
     * @return 结果
     */
    int insertTargetSettingRecovery(@Param("targetSettingRecovery") TargetSettingRecovery targetSettingRecovery);

    /**
     * 修改目标制定回款表
     *
     * @param targetSettingRecovery 目标制定回款表
     * @return 结果
     */
    int updateTargetSettingRecovery(@Param("targetSettingRecovery") TargetSettingRecovery targetSettingRecovery);

    /**
     * 批量修改目标制定回款表
     *
     * @param targetSettingRecoveryList 目标制定回款表
     * @return 结果
     */
    int updateTargetSettingRecoverys(@Param("targetSettingRecoveryList") List<TargetSettingRecovery> targetSettingRecoveryList);

    /**
     * 逻辑删除目标制定回款表
     *
     * @param targetSettingRecovery
     * @return 结果
     */
    int logicDeleteTargetSettingRecoveryByTargetSettingRecoveriesId(@Param("targetSettingRecovery") TargetSettingRecovery targetSettingRecovery);

    /**
     * 逻辑批量删除目标制定回款表
     *
     * @param targetSettingRecoveriesIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetSettingRecoveryByTargetSettingRecoveriesIds(@Param("targetSettingRecoveriesIds") List<Long> targetSettingRecoveriesIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除目标制定回款表
     *
     * @param targetSettingRecoveriesId 目标制定回款表主键
     * @return 结果
     */
    int deleteTargetSettingRecoveryByTargetSettingRecoveriesId(@Param("targetSettingRecoveriesId") Long targetSettingRecoveriesId);

    /**
     * 物理批量删除目标制定回款表
     *
     * @param targetSettingRecoveriesIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTargetSettingRecoveryByTargetSettingRecoveriesIds(@Param("targetSettingRecoveriesIds") List<Long> targetSettingRecoveriesIds);

    /**
     * 批量新增目标制定回款表
     *
     * @param TargetSettingRecoverys 目标制定回款表列表
     * @return 结果
     */
    int batchTargetSettingRecovery(@Param("targetSettingRecoverys") List<TargetSettingRecovery> TargetSettingRecoverys);

    /**
     * 通过目标制定ID获取目标制定回款
     *
     * @param targetSettingId
     * @return
     */
    TargetSettingRecoveryDTO selectTargetSettingRecoveryByTargetSettingId(@Param("targetSettingId") Long targetSettingId);
}
