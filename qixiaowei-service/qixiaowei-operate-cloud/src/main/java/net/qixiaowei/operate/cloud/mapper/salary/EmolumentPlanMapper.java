package net.qixiaowei.operate.cloud.mapper.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.salary.EmolumentPlan;
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* EmolumentPlanMapper接口
* @author TANGMICHI
* @since 2022-11-18
*/
public interface EmolumentPlanMapper{
    /**
    * 查询薪酬规划表
    *
    * @param emolumentPlanId 薪酬规划表主键
    * @return 薪酬规划表
    */
    EmolumentPlanDTO selectEmolumentPlanByEmolumentPlanId(@Param("emolumentPlanId")Long emolumentPlanId);


    /**
     * 查询薪酬规划表
     *
     * @param planYear 薪酬规划表预算年度
     * @return 薪酬规划表
     */
    EmolumentPlanDTO selectEmolumentPlanByPlanYear(@Param("planYear")int planYear);
    /**
    * 批量查询薪酬规划表
    *
    * @param emolumentPlanIds 薪酬规划表主键集合
    * @return 薪酬规划表
    */
    List<EmolumentPlanDTO> selectEmolumentPlanByEmolumentPlanIds(@Param("emolumentPlanIds") List<Long> emolumentPlanIds);

    /**
    * 查询薪酬规划表列表
    *
    * @param emolumentPlan 薪酬规划表
    * @return 薪酬规划表集合
    */
    List<EmolumentPlanDTO> selectEmolumentPlanList(@Param("emolumentPlan")EmolumentPlan emolumentPlan);

    /**
    * 新增薪酬规划表
    *
    * @param emolumentPlan 薪酬规划表
    * @return 结果
    */
    int insertEmolumentPlan(@Param("emolumentPlan")EmolumentPlan emolumentPlan);

    /**
    * 修改薪酬规划表
    *
    * @param emolumentPlan 薪酬规划表
    * @return 结果
    */
    int updateEmolumentPlan(@Param("emolumentPlan")EmolumentPlan emolumentPlan);

    /**
    * 批量修改薪酬规划表
    *
    * @param emolumentPlanList 薪酬规划表
    * @return 结果
    */
    int updateEmolumentPlans(@Param("emolumentPlanList")List<EmolumentPlan> emolumentPlanList);
    /**
    * 逻辑删除薪酬规划表
    *
    * @param emolumentPlan
    * @return 结果
    */
    int logicDeleteEmolumentPlanByEmolumentPlanId(@Param("emolumentPlan")EmolumentPlan emolumentPlan);

    /**
    * 逻辑批量删除薪酬规划表
    *
    * @param emolumentPlanIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteEmolumentPlanByEmolumentPlanIds(@Param("emolumentPlanIds")List<Long> emolumentPlanIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除薪酬规划表
    *
    * @param emolumentPlanId 薪酬规划表主键
    * @return 结果
    */
    int deleteEmolumentPlanByEmolumentPlanId(@Param("emolumentPlanId")Long emolumentPlanId);

    /**
    * 物理批量删除薪酬规划表
    *
    * @param emolumentPlanIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteEmolumentPlanByEmolumentPlanIds(@Param("emolumentPlanIds")List<Long> emolumentPlanIds);

    /**
    * 批量新增薪酬规划表
    *
    * @param EmolumentPlans 薪酬规划表列表
    * @return 结果
    */
    int batchEmolumentPlan(@Param("emolumentPlans")List<EmolumentPlan> EmolumentPlans);

    /**
     * 新增薪酬规划时预制数据
     * @param emolumentPlanDTO
     * @return
     */
    EmolumentPlanDTO prefabricateAddEmolumentPlan(@Param("emolumentPlanDTO") EmolumentPlanDTO emolumentPlanDTO);
}
