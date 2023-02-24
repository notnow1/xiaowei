package net.qixiaowei.strategy.cloud.mapper.strategyIntent;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.strategyIntent.StrategyIntentOperate;
import net.qixiaowei.strategy.cloud.api.dto.strategyIntent.StrategyIntentOperateDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* StrategyIntentOperateMapper接口
* @author TANGMICHI
* @since 2023-02-23
*/
public interface StrategyIntentOperateMapper{
    /**
    * 查询战略意图经营表
    *
    * @param strategyIntentOperateId 战略意图经营表主键
    * @return 战略意图经营表
    */
    StrategyIntentOperateDTO selectStrategyIntentOperateByStrategyIntentOperateId(@Param("strategyIntentOperateId")Long strategyIntentOperateId);

    /**
     * 根据战略意图主表主键查询战略意图经营表
     *
     * @param strategyIntentId 战略意图主表主键
     * @return 战略意图经营表
     */
    List<StrategyIntentOperateDTO> selectStrategyIntentOperateByStrategyIntentId(@Param("strategyIntentId")Long strategyIntentId);


    /**
    * 批量查询战略意图经营表
    *
    * @param strategyIntentOperateIds 战略意图经营表主键集合
    * @return 战略意图经营表
    */
    List<StrategyIntentOperateDTO> selectStrategyIntentOperateByStrategyIntentOperateIds(@Param("strategyIntentOperateIds") List<Long> strategyIntentOperateIds);

    /**
     * 根据战略意图主表主键集合批量查询战略意图经营表
     *
     * @param strategyIntentIds 战略意图主表主键集合
     * @return 战略意图经营表
     */
    List<StrategyIntentOperateDTO> selectStrategyIntentOperateByStrategyIntentIds(@Param("strategyIntentIds") List<Long> strategyIntentIds);

    /**
    * 查询战略意图经营表列表
    *
    * @param strategyIntentOperate 战略意图经营表
    * @return 战略意图经营表集合
    */
    List<StrategyIntentOperateDTO> selectStrategyIntentOperateList(@Param("strategyIntentOperate")StrategyIntentOperate strategyIntentOperate);

    /**
    * 新增战略意图经营表
    *
    * @param strategyIntentOperate 战略意图经营表
    * @return 结果
    */
    int insertStrategyIntentOperate(@Param("strategyIntentOperate")StrategyIntentOperate strategyIntentOperate);

    /**
    * 修改战略意图经营表
    *
    * @param strategyIntentOperate 战略意图经营表
    * @return 结果
    */
    int updateStrategyIntentOperate(@Param("strategyIntentOperate")StrategyIntentOperate strategyIntentOperate);

    /**
    * 批量修改战略意图经营表
    *
    * @param strategyIntentOperateList 战略意图经营表
    * @return 结果
    */
    int updateStrategyIntentOperates(@Param("strategyIntentOperateList")List<StrategyIntentOperate> strategyIntentOperateList);
    /**
    * 逻辑删除战略意图经营表
    *
    * @param strategyIntentOperate
    * @return 结果
    */
    int logicDeleteStrategyIntentOperateByStrategyIntentOperateId(@Param("strategyIntentOperate")StrategyIntentOperate strategyIntentOperate);

    /**
    * 逻辑批量删除战略意图经营表
    *
    * @param strategyIntentOperateIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteStrategyIntentOperateByStrategyIntentOperateIds(@Param("strategyIntentOperateIds")List<Long> strategyIntentOperateIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除战略意图经营表
    *
    * @param strategyIntentOperateId 战略意图经营表主键
    * @return 结果
    */
    int deleteStrategyIntentOperateByStrategyIntentOperateId(@Param("strategyIntentOperateId")Long strategyIntentOperateId);

    /**
    * 物理批量删除战略意图经营表
    *
    * @param strategyIntentOperateIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteStrategyIntentOperateByStrategyIntentOperateIds(@Param("strategyIntentOperateIds")List<Long> strategyIntentOperateIds);

    /**
    * 批量新增战略意图经营表
    *
    * @param strategyIntentOperates 战略意图经营表列表
    * @return 结果
    */
    int batchStrategyIntentOperate(@Param("strategyIntentOperates")List<StrategyIntentOperate> strategyIntentOperates);
}
