package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiOpponentFinance;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiOpponentFinanceDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiOpponentFinanceMapper接口
* @author TANGMICHI
* @since 2023-03-12
*/
public interface MiOpponentFinanceMapper{
    /**
    * 查询市场洞察对手财务表
    *
    * @param miOpponentFinanceId 市场洞察对手财务表主键
    * @return 市场洞察对手财务表
    */
    MiOpponentFinanceDTO selectMiOpponentFinanceByMiOpponentFinanceId(@Param("miOpponentFinanceId")Long miOpponentFinanceId);

    /**
     * 根据市场洞察对手选择表主键查询市场洞察对手财务表
     *
     * @param miOpponentChoiceId 市场洞察对手选择表主键
     * @return 市场洞察对手财务表
     */
    List<MiOpponentFinanceDTO> selectMiOpponentFinanceByMiOpponentChoiceId(@Param("miOpponentChoiceId")Long miOpponentChoiceId);

    /**
    * 批量查询市场洞察对手财务表
    *
    * @param miOpponentFinanceIds 市场洞察对手财务表主键集合
    * @return 市场洞察对手财务表
    */
    List<MiOpponentFinanceDTO> selectMiOpponentFinanceByMiOpponentFinanceIds(@Param("miOpponentFinanceIds") List<Long> miOpponentFinanceIds);


    /**
     * 根据市场洞察对手选择表主键集合批量查询市场洞察对手财务表
     *
     * @param miOpponentChoiceIds 市场洞察对手选择表主键集合
     * @return 市场洞察对手财务表
     */
    List<MiOpponentFinanceDTO> selectMiOpponentFinanceByMiOpponentChoiceIds(@Param("miOpponentChoiceIds") List<Long> miOpponentChoiceIds);
    /**
    * 查询市场洞察对手财务表列表
    *
    * @param miOpponentFinance 市场洞察对手财务表
    * @return 市场洞察对手财务表集合
    */
    List<MiOpponentFinanceDTO> selectMiOpponentFinanceList(@Param("miOpponentFinance")MiOpponentFinance miOpponentFinance);

    /**
    * 新增市场洞察对手财务表
    *
    * @param miOpponentFinance 市场洞察对手财务表
    * @return 结果
    */
    int insertMiOpponentFinance(@Param("miOpponentFinance")MiOpponentFinance miOpponentFinance);

    /**
    * 修改市场洞察对手财务表
    *
    * @param miOpponentFinance 市场洞察对手财务表
    * @return 结果
    */
    int updateMiOpponentFinance(@Param("miOpponentFinance")MiOpponentFinance miOpponentFinance);

    /**
    * 批量修改市场洞察对手财务表
    *
    * @param miOpponentFinanceList 市场洞察对手财务表
    * @return 结果
    */
    int updateMiOpponentFinances(@Param("miOpponentFinanceList")List<MiOpponentFinance> miOpponentFinanceList);
    /**
    * 逻辑删除市场洞察对手财务表
    *
    * @param miOpponentFinance
    * @return 结果
    */
    int logicDeleteMiOpponentFinanceByMiOpponentFinanceId(@Param("miOpponentFinance")MiOpponentFinance miOpponentFinance);

    /**
    * 逻辑批量删除市场洞察对手财务表
    *
    * @param miOpponentFinanceIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiOpponentFinanceByMiOpponentFinanceIds(@Param("miOpponentFinanceIds")List<Long> miOpponentFinanceIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察对手财务表
    *
    * @param miOpponentFinanceId 市场洞察对手财务表主键
    * @return 结果
    */
    int deleteMiOpponentFinanceByMiOpponentFinanceId(@Param("miOpponentFinanceId")Long miOpponentFinanceId);

    /**
    * 物理批量删除市场洞察对手财务表
    *
    * @param miOpponentFinanceIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiOpponentFinanceByMiOpponentFinanceIds(@Param("miOpponentFinanceIds")List<Long> miOpponentFinanceIds);

    /**
    * 批量新增市场洞察对手财务表
    *
    * @param miOpponentFinances 市场洞察对手财务表列表
    * @return 结果
    */
    int batchMiOpponentFinance(@Param("miOpponentFinances")List<MiOpponentFinance> miOpponentFinances);

    /**
     * 看对手竞争对手财务详情远程查询列表是否被引用
     * @param miOpponentFinance
     * @return
     */
    List<MiOpponentFinanceDTO> remoteMiOpponentFinanceList(@Param("miOpponentFinance")MiOpponentFinance miOpponentFinance);
}
