package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiOpponentChoice;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiOpponentChoiceDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiOpponentChoiceMapper接口
* @author TANGMICHI
* @since 2023-03-12
*/
public interface MiOpponentChoiceMapper{
    /**
    * 查询市场洞察对手选择表
    *
    * @param miOpponentChoiceId 市场洞察对手选择表主键
    * @return 市场洞察对手选择表
    */
    MiOpponentChoiceDTO selectMiOpponentChoiceByMiOpponentChoiceId(@Param("miOpponentChoiceId")Long miOpponentChoiceId);

    /**
     * 根据市场洞察对手主表主键查询市场洞察对手选择表
     *
     * @param marketInsightOpponentId 市场洞察对手主表主键
     * @return 市场洞察对手选择表
     */
    List<MiOpponentChoiceDTO> selectMiOpponentChoiceByMarketInsightOpponentId(@Param("marketInsightOpponentId")Long marketInsightOpponentId);


    /**
    * 批量查询市场洞察对手选择表
    *
    * @param miOpponentChoiceIds 市场洞察对手选择表主键集合
    * @return 市场洞察对手选择表
    */
    List<MiOpponentChoiceDTO> selectMiOpponentChoiceByMiOpponentChoiceIds(@Param("miOpponentChoiceIds") List<Long> miOpponentChoiceIds);

    /**
     * 根据市场洞察对手主表主键集合批量查询市场洞察对手选择表
     *
     * @param marketInsightOpponentIds 市场洞察对手主表主键集合
     * @return 市场洞察对手选择表
     */
    List<MiOpponentChoiceDTO> selectMiOpponentChoiceByMarketInsightOpponentIds(@Param("marketInsightOpponentIds") List<Long> marketInsightOpponentIds);

    /**
    * 查询市场洞察对手选择表列表
    *
    * @param miOpponentChoice 市场洞察对手选择表
    * @return 市场洞察对手选择表集合
    */
    List<MiOpponentChoiceDTO> selectMiOpponentChoiceList(@Param("miOpponentChoice")MiOpponentChoice miOpponentChoice);

    /**
    * 新增市场洞察对手选择表
    *
    * @param miOpponentChoice 市场洞察对手选择表
    * @return 结果
    */
    int insertMiOpponentChoice(@Param("miOpponentChoice")MiOpponentChoice miOpponentChoice);

    /**
    * 修改市场洞察对手选择表
    *
    * @param miOpponentChoice 市场洞察对手选择表
    * @return 结果
    */
    int updateMiOpponentChoice(@Param("miOpponentChoice")MiOpponentChoice miOpponentChoice);

    /**
    * 批量修改市场洞察对手选择表
    *
    * @param miOpponentChoiceList 市场洞察对手选择表
    * @return 结果
    */
    int updateMiOpponentChoices(@Param("miOpponentChoiceList")List<MiOpponentChoice> miOpponentChoiceList);
    /**
    * 逻辑删除市场洞察对手选择表
    *
    * @param miOpponentChoice
    * @return 结果
    */
    int logicDeleteMiOpponentChoiceByMiOpponentChoiceId(@Param("miOpponentChoice")MiOpponentChoice miOpponentChoice);

    /**
    * 逻辑批量删除市场洞察对手选择表
    *
    * @param miOpponentChoiceIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiOpponentChoiceByMiOpponentChoiceIds(@Param("miOpponentChoiceIds")List<Long> miOpponentChoiceIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察对手选择表
    *
    * @param miOpponentChoiceId 市场洞察对手选择表主键
    * @return 结果
    */
    int deleteMiOpponentChoiceByMiOpponentChoiceId(@Param("miOpponentChoiceId")Long miOpponentChoiceId);

    /**
    * 物理批量删除市场洞察对手选择表
    *
    * @param miOpponentChoiceIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiOpponentChoiceByMiOpponentChoiceIds(@Param("miOpponentChoiceIds")List<Long> miOpponentChoiceIds);

    /**
    * 批量新增市场洞察对手选择表
    *
    * @param miOpponentChoices 市场洞察对手选择表列表
    * @return 结果
    */
    int batchMiOpponentChoice(@Param("miOpponentChoices")List<MiOpponentChoice> miOpponentChoices);
}
