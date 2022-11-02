package net.qixiaowei.operate.cloud.service.targetManager;

import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingRecoveriesDTO;

import java.util.List;


/**
 * TargetSettingRecoveriesService接口
 *
 * @author Graves
 * @since 2022-11-01
 */
public interface ITargetSettingRecoveriesService {
    /**
     * 查询目标制定回款集合表
     *
     * @param targetSettingRecoveriesId 目标制定回款集合表主键
     * @return 目标制定回款集合表
     */
    TargetSettingRecoveriesDTO selectTargetSettingRecoveriesByTargetSettingRecoveriesId(Long targetSettingRecoveriesId);

    /**
     * 查询目标制定回款集合表列表
     *
     * @param targetSettingRecoveriesDTO 目标制定回款集合表
     * @return 目标制定回款集合表集合
     */
    List<TargetSettingRecoveriesDTO> selectTargetSettingRecoveriesList(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO);

    /**
     * 新增目标制定回款集合表
     *
     * @param targetSettingRecoveriesDTO 目标制定回款集合表
     * @return 结果
     */
    TargetSettingRecoveriesDTO insertTargetSettingRecoveries(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO);

    /**
     * 修改目标制定回款集合表
     *
     * @param targetSettingRecoveriesDTO 目标制定回款集合表
     * @return 结果
     */
    int updateTargetSettingRecoveries(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO);

    /**
     * 批量修改目标制定回款集合表
     *
     * @param targetSettingRecoveriesDtos 目标制定回款集合表
     * @return 结果
     */
    int updateTargetSettingRecoveriess(List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDtos);

    /**
     * 批量新增目标制定回款集合表
     *
     * @param targetSettingRecoveriesDtos 目标制定回款集合表
     * @return 结果
     */
    int insertTargetSettingRecoveriess(List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDtos);

    /**
     * 逻辑批量删除目标制定回款集合表
     *
     * @param targetSettingRecoveriesIds 需要删除的目标制定回款集合表集合
     * @return 结果
     */
    int logicDeleteTargetSettingRecoveriesByTargetSettingRecoveriesIds(List<Long> targetSettingRecoveriesIds);

    /**
     * 逻辑删除目标制定回款集合表信息
     *
     * @param targetSettingRecoveriesDTO
     * @return 结果
     */
    int logicDeleteTargetSettingRecoveriesByTargetSettingRecoveriesId(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO);

    /**
     * 批量删除目标制定回款集合表
     *
     * @param TargetSettingRecoveriesDtos
     * @return 结果
     */
    int deleteTargetSettingRecoveriesByTargetSettingRecoveriesIds(List<TargetSettingRecoveriesDTO> TargetSettingRecoveriesDtos);

    /**
     * 逻辑删除目标制定回款集合表信息
     *
     * @param targetSettingRecoveriesDTO
     * @return 结果
     */
    int deleteTargetSettingRecoveriesByTargetSettingRecoveriesId(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO);


    /**
     * 删除目标制定回款集合表信息
     *
     * @param targetSettingRecoveriesId 目标制定回款集合表主键
     * @return 结果
     */
    int deleteTargetSettingRecoveriesByTargetSettingRecoveriesId(Long targetSettingRecoveriesId);
//    /**
//    * 导入Excel
//    * @param list
//    */
//    void importTargetSettingRecoveries(List<TargetSettingRecoveriesExcel> list);
//    /**
//    * 导出Excel
//    * @param targetSettingRecoveriesDTO
//    * @return
//    */
//    List<TargetSettingRecoveriesExcel> exportTargetSettingRecoveries(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO);

    /**
     * 通过目标制定ID查找目标回款列表
     *
     * @param targetSettingId 目标制定ID
     * @return
     */
    List<TargetSettingRecoveriesDTO> selectTargetSettingRecoveriesByTargetSettingId(Long targetSettingId);
}
