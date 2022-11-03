package net.qixiaowei.operate.cloud.service.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingExcel;


/**
 * TargetSettingService接口
 *
 * @author Graves
 * @since 2022-10-27
 */
public interface ITargetSettingService {
    /**
     * 查询目标制定
     *
     * @param targetSettingId 目标制定主键
     * @return 目标制定
     */
    TargetSettingDTO selectTargetSettingByTargetSettingId(Long targetSettingId);

    /**
     * 查询目标制定列表
     *
     * @param targetSettingDTO 目标制定
     * @return 目标制定集合
     */
    List<TargetSettingDTO> selectTargetSettingList(TargetSettingDTO targetSettingDTO);

    /**
     * 查询经营分析报表列表
     *
     * @param targetSettingDTO 目标制定
     * @return 目标制定集合
     */
    List<TargetSettingDTO> analyseList(TargetSettingDTO targetSettingDTO);

    /**
     * 新增目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */
    TargetSetting insertTargetSetting(TargetSettingDTO targetSettingDTO);

    /**
     * 修改目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */
    int updateTargetSetting(TargetSettingDTO targetSettingDTO);

    /**
     * 批量修改目标制定
     *
     * @param targetSettingDtos 目标制定
     * @return 结果
     */
    int updateTargetSettings(List<TargetSettingDTO> targetSettingDtos);

    /**
     * 批量新增目标制定
     *
     * @param targetSettingDtos 目标制定
     * @return 结果
     */
    int insertTargetSettings(List<TargetSettingDTO> targetSettingDtos);

    /**
     * 逻辑批量删除目标制定
     *
     * @param targetSettingIds 需要删除的目标制定集合
     * @return 结果
     */
    int logicDeleteTargetSettingByTargetSettingIds(List<Long> targetSettingIds);

    /**
     * 逻辑删除目标制定信息
     *
     * @param targetSettingDTO
     * @return 结果
     */
    int logicDeleteTargetSettingByTargetSettingId(TargetSettingDTO targetSettingDTO);

    /**
     * 批量删除目标制定
     *
     * @param TargetSettingDtos
     * @return 结果
     */
    int deleteTargetSettingByTargetSettingIds(List<TargetSettingDTO> TargetSettingDtos);

    /**
     * 逻辑删除目标制定信息
     *
     * @param targetSettingDTO
     * @return 结果
     */
    int deleteTargetSettingByTargetSettingId(TargetSettingDTO targetSettingDTO);


    /**
     * 删除目标制定信息
     *
     * @param targetSettingId 目标制定主键
     * @return 结果
     */
    int deleteTargetSettingByTargetSettingId(Long targetSettingId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importTargetSetting(List<TargetSettingExcel> list);

    /**
     * 导出Excel
     *
     * @param targetSettingDTO
     * @return
     */
    List<TargetSettingExcel> exportTargetSetting(TargetSettingDTO targetSettingDTO);

    /**
     * 保存销售订单目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    TargetSettingDTO saveOrderTargetSetting(TargetSettingDTO targetSettingDTO);

    /**
     * 查询销售订单目标制定
     *
     * @param targetSettingDTO
     * @return
     */
    TargetSettingDTO selectOrderTargetSettingList(TargetSettingDTO targetSettingDTO);

    /**
     * 查询销售订单目标制定-不带主表玩
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    List<TargetSettingOrderDTO> selectOrderDropTargetSettingList(TargetSettingDTO targetSettingDTO);

    /**
     * 保存销售收入目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    TargetSettingDTO saveIncomeTargetSetting(TargetSettingDTO targetSettingDTO);

    /**
     * 查询销售收入目标制定列表
     *
     * @param targetSettingDTO
     * @return
     */
    TargetSettingDTO selectIncomeTargetSettingList(TargetSettingDTO targetSettingDTO);

    /**
     * 查询销售回款目标制定列表
     *
     * @param targetSettingDTO
     * @return
     */
    TargetSettingDTO selectRecoveryTargetSettingList(TargetSettingDTO targetSettingDTO);

    /**
     * 保存销售回款目标制定列表
     *
     * @param targetSettingDTO
     * @return
     */
    TargetSettingDTO saveRecoveryTargetSetting(TargetSettingDTO targetSettingDTO);
}
