package net.qixiaowei.strategy.cloud.service.strategyDecode;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDTO;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureTaskDTO;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;

import java.util.List;


/**
 * StrategyMeasureService接口
 *
 * @author Graves
 * @since 2023-03-07
 */
public interface IStrategyMeasureService {
    /**
     * 查询战略举措清单表
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 战略举措清单表
     */
    StrategyMeasureDTO selectStrategyMeasureByStrategyMeasureId(Long strategyMeasureId);

    /**
     * 查询战略举措清单表列表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 战略举措清单表集合
     */
    List<StrategyMeasureDTO> selectStrategyMeasureList(StrategyMeasureDTO strategyMeasureDTO);

    /**
     * 新增战略举措清单表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    StrategyMeasureDTO insertStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO);

    /**
     * 修改战略举措清单表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return 结果
     */
    int updateStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO);

    /**
     * 批量修改战略举措清单表
     *
     * @param strategyMeasureDtos 战略举措清单表
     * @return 结果
     */
    int updateStrategyMeasures(List<StrategyMeasureDTO> strategyMeasureDtos);

    /**
     * 批量新增战略举措清单表
     *
     * @param strategyMeasureDtos 战略举措清单表
     * @return 结果
     */
    int insertStrategyMeasures(List<StrategyMeasureDTO> strategyMeasureDtos);

    /**
     * 逻辑批量删除战略举措清单表
     *
     * @param strategyMeasureIds 需要删除的战略举措清单表集合
     * @return 结果
     */
    int logicDeleteStrategyMeasureByStrategyMeasureIds(List<Long> strategyMeasureIds);

    /**
     * 逻辑删除战略举措清单表信息
     *
     * @param strategyMeasureDTO
     * @return 结果
     */
    int logicDeleteStrategyMeasureByStrategyMeasureId(StrategyMeasureDTO strategyMeasureDTO);

    /**
     * 批量删除战略举措清单表
     *
     * @param StrategyMeasureDtos
     * @return 结果
     */
    int deleteStrategyMeasureByStrategyMeasureIds(List<StrategyMeasureDTO> StrategyMeasureDtos);

    /**
     * 逻辑删除战略举措清单表信息
     *
     * @param strategyMeasureDTO
     * @return 结果
     */
    int deleteStrategyMeasureByStrategyMeasureId(StrategyMeasureDTO strategyMeasureDTO);


    /**
     * 删除战略举措清单表信息
     *
     * @param strategyMeasureId 战略举措清单表主键
     * @return 结果
     */
    int deleteStrategyMeasureByStrategyMeasureId(Long strategyMeasureId);

    /**
     * 远程获取列表
     *
     * @param strategyMeasureDTO 战略举措清单表
     * @return List
     */
    List<StrategyMeasureDTO> remoteStrategyMeasure(StrategyMeasureDTO strategyMeasureDTO);

    /**
     * 远程责任人
     *
     * @param strategyMeasureDetailVO 详情VO
     * @return R
     */
    List<StrategyMeasureTaskDTO> remoteDutyMeasure(StrategyMeasureDetailVO strategyMeasureDetailVO);
}
