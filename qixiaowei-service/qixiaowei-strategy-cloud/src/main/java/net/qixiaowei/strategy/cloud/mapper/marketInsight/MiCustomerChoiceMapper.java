package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiCustomerChoice;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerChoiceDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiCustomerChoiceMapper接口
* @author TANGMICHI
* @since 2023-03-07
*/
public interface MiCustomerChoiceMapper{
    /**
    * 查询市场洞察客户选择表
    *
    * @param miCustomerChoiceId 市场洞察客户选择表主键
    * @return 市场洞察客户选择表
    */
    MiCustomerChoiceDTO selectMiCustomerChoiceByMiCustomerChoiceId(@Param("miCustomerChoiceId")Long miCustomerChoiceId);


    /**
    * 批量查询市场洞察客户选择表
    *
    * @param miCustomerChoiceIds 市场洞察客户选择表主键集合
    * @return 市场洞察客户选择表
    */
    List<MiCustomerChoiceDTO> selectMiCustomerChoiceByMiCustomerChoiceIds(@Param("miCustomerChoiceIds") List<Long> miCustomerChoiceIds);

    /**
    * 查询市场洞察客户选择表列表
    *
    * @param miCustomerChoice 市场洞察客户选择表
    * @return 市场洞察客户选择表集合
    */
    List<MiCustomerChoiceDTO> selectMiCustomerChoiceList(@Param("miCustomerChoice")MiCustomerChoice miCustomerChoice);

    /**
    * 新增市场洞察客户选择表
    *
    * @param miCustomerChoice 市场洞察客户选择表
    * @return 结果
    */
    int insertMiCustomerChoice(@Param("miCustomerChoice")MiCustomerChoice miCustomerChoice);

    /**
    * 修改市场洞察客户选择表
    *
    * @param miCustomerChoice 市场洞察客户选择表
    * @return 结果
    */
    int updateMiCustomerChoice(@Param("miCustomerChoice")MiCustomerChoice miCustomerChoice);

    /**
    * 批量修改市场洞察客户选择表
    *
    * @param miCustomerChoiceList 市场洞察客户选择表
    * @return 结果
    */
    int updateMiCustomerChoices(@Param("miCustomerChoiceList")List<MiCustomerChoice> miCustomerChoiceList);
    /**
    * 逻辑删除市场洞察客户选择表
    *
    * @param miCustomerChoice
    * @return 结果
    */
    int logicDeleteMiCustomerChoiceByMiCustomerChoiceId(@Param("miCustomerChoice")MiCustomerChoice miCustomerChoice);

    /**
    * 逻辑批量删除市场洞察客户选择表
    *
    * @param miCustomerChoiceIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiCustomerChoiceByMiCustomerChoiceIds(@Param("miCustomerChoiceIds")List<Long> miCustomerChoiceIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察客户选择表
    *
    * @param miCustomerChoiceId 市场洞察客户选择表主键
    * @return 结果
    */
    int deleteMiCustomerChoiceByMiCustomerChoiceId(@Param("miCustomerChoiceId")Long miCustomerChoiceId);

    /**
    * 物理批量删除市场洞察客户选择表
    *
    * @param miCustomerChoiceIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiCustomerChoiceByMiCustomerChoiceIds(@Param("miCustomerChoiceIds")List<Long> miCustomerChoiceIds);

    /**
    * 批量新增市场洞察客户选择表
    *
    * @param miCustomerChoices 市场洞察客户选择表列表
    * @return 结果
    */
    int batchMiCustomerChoice(@Param("miCustomerChoices")List<MiCustomerChoice> miCustomerChoices);
}
