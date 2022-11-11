package net.qixiaowei.operate.cloud.service.targetManager;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingIncomeExcel;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingOrderExcel;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;

import java.util.List;


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
     * 查询目标制定列表-树结构
     *
     * @param targetSettingDTO 目标制定
     * @return 目标制定集合
     */
    List<TargetSettingDTO> selectTargetSettingList(TargetSettingDTO targetSettingDTO);

    /**
     * 查询目标制定列表 - 树结构
     *
     * @param targetSettingDTO 目标制定
     * @return 目标制定集合
     */
    List<Tree<Long>> selectTargetSettingTreeList(TargetSettingDTO targetSettingDTO);

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
    int saveTargetSetting(TargetSettingDTO targetSettingDTO);

    /**
     * 批量修改目标制定
     *
     * @param targetSettingDtos 目标制定
     * @return 结果
     */
    int updateTargetSettings(List<TargetSettingDTO> targetSettingDtos);

    /**
     * 逻辑批量删除目标制定
     *
     * @param targetSettingIds 需要删除的目标制定集合
     * @return 结果
     */
    int logicDeleteTargetSettingByTargetSettingIds(List<TargetSettingDTO> targetSettingIds);

    /**
     * 逻辑删除目标制定信息
     *
     * @param targetSettingDTO
     * @return 结果
     */
    int logicDeleteTargetSettingByTargetSettingId(TargetSettingDTO targetSettingDTO);

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
    List<List<TargetSettingExcel>> exportTargetSetting(TargetSettingDTO targetSettingDTO);

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
     * 导出销售订单目标制定
     *
     * @param targetSettingDTO
     * @return
     */
    List<TargetSettingOrderExcel> exportOrderTargetSetting(TargetSettingDTO targetSettingDTO);

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
     * 导出销售收入目标制定列表
     *
     * @param targetSettingDTO
     * @return
     */
    List<TargetSettingIncomeExcel> exportIncomeTargetSetting(TargetSettingDTO targetSettingDTO);

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

    /**
     * 导出销售回款目标制定
     *
     * @param targetSettingDTO
     * @return
     */
    List<TargetSettingIncomeExcel> exportRecoveryTargetSetting(TargetSettingDTO targetSettingDTO);

    /**
     * 获取指标列表
     *
     * @return
     */
    List<IndicatorDTO> selectIndicatorList(TargetSettingDTO targetSettingDTO);

    /**
     * 获取指标树结构列表
     *
     * @return
     */
    List<Tree<Long>> selectIndicatorTree(TargetSettingDTO targetSettingDTO);

    /**
     * 批量保存目标制定
     *
     * @param targetSettingDTOS
     * @return
     */
    TargetSettingDTO saveTargetSettings(List<TargetSettingDTO> targetSettingDTOS);


}
