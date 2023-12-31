package net.qixiaowei.operate.cloud.service.targetManager;

import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.vo.strategyIntent.StrategyIntentOperateVO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetOutcomeExcel;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;

import java.util.List;
import java.util.Map;


/**
 * TargetOutcomeService接口
 *
 * @author TANGMICHI
 * @since 2022-11-07
 */
public interface ITargetOutcomeService {
    /**
     * 查询目标结果表
     *
     * @param targetOutcomeId 目标结果表主键
     * @return 目标结果表
     */
    TargetOutcomeDTO selectTargetOutcomeByTargetOutcomeId(Long targetOutcomeId);

    /**
     * 查询目标结果表列表
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 目标结果表集合
     */
    List<TargetOutcomeDTO> selectTargetOutcomeList(TargetOutcomeDTO targetOutcomeDTO);

    /**
     * 处理返回
     *
     * @param result 返回集合
     * @return 返回集合
     */
    void handleResult(List<TargetOutcomeDTO> result);

    /**
     * 新增目标结果表
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 结果
     */
    TargetOutcomeDTO insertTargetOutcome(TargetOutcomeDTO targetOutcomeDTO);

    /**
     * 新增目标结果表--订单，收入，回款
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 结果
     */
    TargetOutcomeDTO insertTargetOutcome(TargetOutcomeDTO targetOutcomeDTO, IndicatorDTO indicatorDTO);

    /**
     * 修改目标结果表
     *
     * @param targetOutcomeDTO 目标结果表
     * @return 结果
     */
    int updateTargetOutcome(TargetOutcomeDTO targetOutcomeDTO);

    /**
     * 批量修改目标结果表
     *
     * @param targetOutcomeDtos 目标结果表
     * @return 结果
     */
    int updateTargetOutcomes(List<TargetOutcomeDTO> targetOutcomeDtos);

    /**
     * 批量新增目标结果表
     *
     * @param targetOutcomeDtos 目标结果表
     * @return 结果
     */
    int insertTargetOutcomes(List<TargetOutcomeDTO> targetOutcomeDtos);

    /**
     * 逻辑批量删除目标结果表
     *
     * @param targetOutcomeIds 需要删除的目标结果表集合
     * @return 结果
     */
    int logicDeleteTargetOutcomeByTargetOutcomeIds(List<Long> targetOutcomeIds);

    /**
     * 逻辑删除目标结果表信息
     *
     * @param targetOutcomeDTO
     * @return 结果
     */
    int logicDeleteTargetOutcomeByTargetOutcomeId(TargetOutcomeDTO targetOutcomeDTO);

    /**
     * 批量删除目标结果表
     *
     * @param TargetOutcomeDtos
     * @return 结果
     */
    int deleteTargetOutcomeByTargetOutcomeIds(List<TargetOutcomeDTO> TargetOutcomeDtos);

    /**
     * 逻辑删除目标结果表信息
     *
     * @param targetOutcomeDTO
     * @return 结果
     */
    int deleteTargetOutcomeByTargetOutcomeId(TargetOutcomeDTO targetOutcomeDTO);


    /**
     * 删除目标结果表信息
     *
     * @param targetOutcomeId 目标结果表主键
     * @return 结果
     */
    int deleteTargetOutcomeByTargetOutcomeId(Long targetOutcomeId);

    /**
     * 导入Excel
     *
     * @param dataList        数据列表
     * @param targetOutcomeId 结果ID
     */
    Map<Object, Object> importTargetOutcome(List<Map<Integer, String>> dataList, Long targetOutcomeId);

    /**
     * 导出Excel
     *
     * @param targetOutcomeDTO
     * @return
     */
    List<TargetOutcomeExcel> exportTargetOutcome(TargetOutcomeDTO targetOutcomeDTO);

    /**
     * 通过targetYear查找Target Outcome DTO
     *
     * @param targetYear
     * @return
     */
    TargetOutcomeDTO selectTargetOutcomeByTargetYear(Integer targetYear);

    /**
     * 通过targetYear列表查找Target Outcome DTO
     *
     * @param targetYears 目标年度列表
     * @param indicatorId 指标id
     * @return 结果
     */
    List<TargetOutcomeDetailsDTO> selectTargetOutcomeByTargetYears(List<Integer> targetYears, Long indicatorId);

    /**
     * 战略云获取指标实际值
     *
     * @param strategyIntentOperateVOS 战略vos
     * @return 结果
     */
    List<StrategyIntentOperateVO> getResultIndicator(List<StrategyIntentOperateVO> strategyIntentOperateVOS);

    /**
     * 迁移数据
     *
     * @param targetYear 目标年度
     * @return 结果
     */
    List<TargetDecomposeDTO> migrationData(Integer targetYear);
}
