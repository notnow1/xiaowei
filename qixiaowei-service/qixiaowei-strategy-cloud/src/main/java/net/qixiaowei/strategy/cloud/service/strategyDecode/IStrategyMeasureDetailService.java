package net.qixiaowei.strategy.cloud.service.strategyDecode;

import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.StrategyMeasureDetailDTO;
import net.qixiaowei.strategy.cloud.api.vo.strategyDecode.StrategyMeasureDetailVO;

import java.util.List;


/**
 * StrategyMeasureDetailService接口
 *
 * @author Graves
 * @since 2023-03-07
 */
public interface IStrategyMeasureDetailService {
    /**
     * 查询战略举措清单详情表
     *
     * @param strategyMeasureDetailId 战略举措清单详情表主键
     * @return 战略举措清单详情表
     */
    StrategyMeasureDetailDTO selectStrategyMeasureDetailByStrategyMeasureDetailId(Long strategyMeasureDetailId);

    /**
     * 查询战略举措清单详情表列表
     *
     * @param strategyMeasureDetailDTO 战略举措清单详情表
     * @return 战略举措清单详情表集合
     */
    List<StrategyMeasureDetailDTO> selectStrategyMeasureDetailList(StrategyMeasureDetailDTO strategyMeasureDetailDTO);

    /**
     * 新增战略举措清单详情表
     *
     * @param strategyMeasureDetailDTO 战略举措清单详情表
     * @return 结果
     */
    StrategyMeasureDetailDTO insertStrategyMeasureDetail(StrategyMeasureDetailDTO strategyMeasureDetailDTO);

    /**
     * 修改战略举措清单详情表
     *
     * @param strategyMeasureDetailDTO 战略举措清单详情表
     * @return 结果
     */
    int updateStrategyMeasureDetail(StrategyMeasureDetailDTO strategyMeasureDetailDTO);

    /**
     * 批量修改战略举措清单详情表
     *
     * @param strategyMeasureDetailDtos 战略举措清单详情表
     * @return 结果
     */
    int updateStrategyMeasureDetails(List<StrategyMeasureDetailDTO> strategyMeasureDetailDtos);

    /**
     * 批量新增战略举措清单详情表
     *
     * @param strategyMeasureDetailDtos 战略举措清单详情表
     * @return 结果
     */
    int insertStrategyMeasureDetails(List<StrategyMeasureDetailDTO> strategyMeasureDetailDtos);

    /**
     * 逻辑批量删除战略举措清单详情表
     *
     * @param strategyMeasureDetailIds 需要删除的战略举措清单详情表集合
     * @return 结果
     */
    int logicDeleteStrategyMeasureDetailByStrategyMeasureDetailIds(List<Long> strategyMeasureDetailIds);

    /**
     * 逻辑删除战略举措清单详情表信息
     *
     * @param strategyMeasureDetailDTO
     * @return 结果
     */
    int logicDeleteStrategyMeasureDetailByStrategyMeasureDetailId(StrategyMeasureDetailDTO strategyMeasureDetailDTO);

    /**
     * 批量删除战略举措清单详情表
     *
     * @param StrategyMeasureDetailDtos
     * @return 结果
     */
    int deleteStrategyMeasureDetailByStrategyMeasureDetailIds(List<StrategyMeasureDetailDTO> StrategyMeasureDetailDtos);

    /**
     * 逻辑删除战略举措清单详情表信息
     *
     * @param strategyMeasureDetailDTO
     * @return 结果
     */
    int deleteStrategyMeasureDetailByStrategyMeasureDetailId(StrategyMeasureDetailDTO strategyMeasureDetailDTO);


    /**
     * 删除战略举措清单详情表信息
     *
     * @param strategyMeasureDetailId 战略举措清单详情表主键
     * @return 结果
     */
    int deleteStrategyMeasureDetailByStrategyMeasureDetailId(Long strategyMeasureDetailId);

    /**
     * 根据战略举措id查询战略举措清单详情表
     *
     * @param strategyMeasureId 战略举措id
     * @return List
     */
    List<StrategyMeasureDetailVO> selectStrategyMeasureDetailVOByStrategyMeasureId(Long strategyMeasureId);
}
