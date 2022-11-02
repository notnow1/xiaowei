package net.qixiaowei.operate.cloud.service.targetManager;

import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingRecoveryDTO;

import java.util.List;


/**
 * TargetSettingRecoveryService接口
 *
 * @author Graves
 * @since 2022-11-01
 */
public interface ITargetSettingRecoveryService {
    /**
     * 查询目标制定回款表
     *
     * @param targetSettingRecoveriesId 目标制定回款表主键
     * @return 目标制定回款表
     */
    TargetSettingRecoveryDTO selectTargetSettingRecoveryByTargetSettingRecoveriesId(Long targetSettingRecoveriesId);

    /**
     * 查询目标制定回款表列表
     *
     * @param targetSettingRecoveryDTO 目标制定回款表
     * @return 目标制定回款表集合
     */
    List<TargetSettingRecoveryDTO> selectTargetSettingRecoveryList(TargetSettingRecoveryDTO targetSettingRecoveryDTO);

    /**
     * 新增目标制定回款表
     *
     * @param targetSettingRecoveryDTO 目标制定回款表
     * @return 结果
     */
    TargetSettingRecoveryDTO insertTargetSettingRecovery(TargetSettingRecoveryDTO targetSettingRecoveryDTO);

    /**
     * 修改目标制定回款表
     *
     * @param targetSettingRecoveryDTO 目标制定回款表
     * @return 结果
     */
    int updateTargetSettingRecovery(TargetSettingRecoveryDTO targetSettingRecoveryDTO);

    /**
     * 批量修改目标制定回款表
     *
     * @param targetSettingRecoveryDtos 目标制定回款表
     * @return 结果
     */
    int updateTargetSettingRecoverys(List<TargetSettingRecoveryDTO> targetSettingRecoveryDtos);

    /**
     * 批量新增目标制定回款表
     *
     * @param targetSettingRecoveryDtos 目标制定回款表
     * @return 结果
     */
    int insertTargetSettingRecoverys(List<TargetSettingRecoveryDTO> targetSettingRecoveryDtos);

    /**
     * 逻辑批量删除目标制定回款表
     *
     * @param targetSettingRecoveriesIds 需要删除的目标制定回款表集合
     * @return 结果
     */
    int logicDeleteTargetSettingRecoveryByTargetSettingRecoveriesIds(List<Long> targetSettingRecoveriesIds);

    /**
     * 逻辑删除目标制定回款表信息
     *
     * @param targetSettingRecoveryDTO
     * @return 结果
     */
    int logicDeleteTargetSettingRecoveryByTargetSettingRecoveriesId(TargetSettingRecoveryDTO targetSettingRecoveryDTO);

    /**
     * 批量删除目标制定回款表
     *
     * @param TargetSettingRecoveryDtos
     * @return 结果
     */
    int deleteTargetSettingRecoveryByTargetSettingRecoveriesIds(List<TargetSettingRecoveryDTO> TargetSettingRecoveryDtos);

    /**
     * 逻辑删除目标制定回款表信息
     *
     * @param targetSettingRecoveryDTO
     * @return 结果
     */
    int deleteTargetSettingRecoveryByTargetSettingRecoveriesId(TargetSettingRecoveryDTO targetSettingRecoveryDTO);


    /**
     * 删除目标制定回款表信息
     *
     * @param targetSettingRecoveriesId 目标制定回款表主键
     * @return 结果
     */
    int deleteTargetSettingRecoveryByTargetSettingRecoveriesId(Long targetSettingRecoveriesId);
//    /**
//    * 导入Excel
//    * @param list
//    */
//    void importTargetSettingRecovery(List<TargetSettingRecoveryExcel> list);
//    /**
//    * 导出Excel
//    * @param targetSettingRecoveryDTO
//    * @return
//    */
//    List<TargetSettingRecoveryExcel> exportTargetSettingRecovery(TargetSettingRecoveryDTO targetSettingRecoveryDTO);

    /**
     * 通过目标制定ID获取目标制定回款
     *
     * @param targetSettingId
     * @return
     */
    TargetSettingRecoveryDTO selectTargetSettingRecoveryByTargetSettingId(Long targetSettingId);
}
