package net.qixiaowei.operate.cloud.service.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingIncomeDTO;
import net.qixiaowei.operate.cloud.api.vo.TargetSettingIncomeVO;
//import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingIncomeExcel;


/**
 * TargetSettingIncomeService接口
 *
 * @author Graves
 * @since 2022-10-31
 */
public interface ITargetSettingIncomeService {
    /**
     * 查询目标制定收入表
     *
     * @param targetSettingIncomeId 目标制定收入表主键
     * @return 目标制定收入表
     */
    TargetSettingIncomeDTO selectTargetSettingIncomeByTargetSettingIncomeId(Long targetSettingIncomeId);

    /**
     * 查询目标制定收入表列表
     *
     * @param targetSettingIncomeDTO 目标制定收入表
     * @return 目标制定收入表集合
     */
    List<TargetSettingIncomeDTO> selectTargetSettingIncomeList(TargetSettingIncomeDTO targetSettingIncomeDTO);

    /**
     * 新增目标制定收入表
     *
     * @param targetSettingIncomeDTO 目标制定收入表
     * @return 结果
     */
    TargetSettingIncomeDTO insertTargetSettingIncome(TargetSettingIncomeDTO targetSettingIncomeDTO);

    /**
     * 修改目标制定收入表
     *
     * @param targetSettingIncomeDTO 目标制定收入表
     * @return 结果
     */
    int updateTargetSettingIncome(TargetSettingIncomeDTO targetSettingIncomeDTO);

    /**
     * 批量修改目标制定收入表
     *
     * @param targetSettingIncomeDtos 目标制定收入表
     * @return 结果
     */
    int updateTargetSettingIncomes(List<TargetSettingIncomeDTO> targetSettingIncomeDtos);

    /**
     * 批量新增目标制定收入表
     *
     * @param targetSettingIncomeDtos 目标制定收入表
     * @return 结果
     */
    int insertTargetSettingIncomes(List<TargetSettingIncomeDTO> targetSettingIncomeDtos);

    /**
     * 逻辑批量删除目标制定收入表
     *
     * @param targetSettingIncomeIds 需要删除的目标制定收入表集合
     * @return 结果
     */
    int logicDeleteTargetSettingIncomeByTargetSettingIncomeIds(List<Long> targetSettingIncomeIds);

    /**
     * 逻辑删除目标制定收入表信息
     *
     * @param targetSettingIncomeDTO
     * @return 结果
     */
    int logicDeleteTargetSettingIncomeByTargetSettingIncomeId(TargetSettingIncomeDTO targetSettingIncomeDTO);

    /**
     * 批量删除目标制定收入表
     *
     * @param TargetSettingIncomeDtos
     * @return 结果
     */
    int deleteTargetSettingIncomeByTargetSettingIncomeIds(List<TargetSettingIncomeDTO> TargetSettingIncomeDtos);

    /**
     * 逻辑删除目标制定收入表信息
     *
     * @param targetSettingIncomeDTO
     * @return 结果
     */
    int deleteTargetSettingIncomeByTargetSettingIncomeId(TargetSettingIncomeDTO targetSettingIncomeDTO);

    /**
     * 删除目标制定收入表信息
     *
     * @param targetSettingIncomeId 目标制定收入表主键
     * @return 结果
     */
    int deleteTargetSettingIncomeByTargetSettingIncomeId(Long targetSettingIncomeId);

    /**
     * 通过年限List获取目标收入列表
     *
     * @param targetSettingId 目标制定ID
     */
    List<TargetSettingIncomeDTO> selectTargetSettingIncomeByHistoryNumS(Long targetSettingId);

//    /**
//     * 导入Excel
//     *
//     * @param list
//     */
//    void importTargetSettingIncome(List<TargetSettingIncomeExcel> list);
//
//    /**
//     * 导出Excel
//     *
//     * @param targetSettingIncomeDTO
//     * @return
//     */
//    List<TargetSettingIncomeExcel> exportTargetSettingIncome(TargetSettingIncomeDTO targetSettingIncomeDTO);
}
