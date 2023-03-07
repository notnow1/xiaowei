package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiCustomerInvestPlan;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerInvestPlanDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiCustomerInvestPlanMapper接口
* @author TANGMICHI
* @since 2023-03-07
*/
public interface MiCustomerInvestPlanMapper{
    /**
    * 查询市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlanId 市场洞察客户投资计划表主键
    * @return 市场洞察客户投资计划表
    */
    MiCustomerInvestPlanDTO selectMiCustomerInvestPlanByMiCustomerInvestPlanId(@Param("miCustomerInvestPlanId")Long miCustomerInvestPlanId);


    /**
    * 批量查询市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlanIds 市场洞察客户投资计划表主键集合
    * @return 市场洞察客户投资计划表
    */
    List<MiCustomerInvestPlanDTO> selectMiCustomerInvestPlanByMiCustomerInvestPlanIds(@Param("miCustomerInvestPlanIds") List<Long> miCustomerInvestPlanIds);

    /**
    * 查询市场洞察客户投资计划表列表
    *
    * @param miCustomerInvestPlan 市场洞察客户投资计划表
    * @return 市场洞察客户投资计划表集合
    */
    List<MiCustomerInvestPlanDTO> selectMiCustomerInvestPlanList(@Param("miCustomerInvestPlan")MiCustomerInvestPlan miCustomerInvestPlan);

    /**
    * 新增市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlan 市场洞察客户投资计划表
    * @return 结果
    */
    int insertMiCustomerInvestPlan(@Param("miCustomerInvestPlan")MiCustomerInvestPlan miCustomerInvestPlan);

    /**
    * 修改市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlan 市场洞察客户投资计划表
    * @return 结果
    */
    int updateMiCustomerInvestPlan(@Param("miCustomerInvestPlan")MiCustomerInvestPlan miCustomerInvestPlan);

    /**
    * 批量修改市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlanList 市场洞察客户投资计划表
    * @return 结果
    */
    int updateMiCustomerInvestPlans(@Param("miCustomerInvestPlanList")List<MiCustomerInvestPlan> miCustomerInvestPlanList);
    /**
    * 逻辑删除市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlan
    * @return 结果
    */
    int logicDeleteMiCustomerInvestPlanByMiCustomerInvestPlanId(@Param("miCustomerInvestPlan")MiCustomerInvestPlan miCustomerInvestPlan);

    /**
    * 逻辑批量删除市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlanIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiCustomerInvestPlanByMiCustomerInvestPlanIds(@Param("miCustomerInvestPlanIds")List<Long> miCustomerInvestPlanIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlanId 市场洞察客户投资计划表主键
    * @return 结果
    */
    int deleteMiCustomerInvestPlanByMiCustomerInvestPlanId(@Param("miCustomerInvestPlanId")Long miCustomerInvestPlanId);

    /**
    * 物理批量删除市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlanIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiCustomerInvestPlanByMiCustomerInvestPlanIds(@Param("miCustomerInvestPlanIds")List<Long> miCustomerInvestPlanIds);

    /**
    * 批量新增市场洞察客户投资计划表
    *
    * @param miCustomerInvestPlans 市场洞察客户投资计划表列表
    * @return 结果
    */
    int batchMiCustomerInvestPlan(@Param("miCustomerInvestPlans")List<MiCustomerInvestPlan> miCustomerInvestPlans);
}
