package net.qixiaowei.operate.cloud.service.salary;

import java.util.List;
import net.qixiaowei.operate.cloud.api.dto.salary.EmolumentPlanDTO;



/**
* EmolumentPlanService接口
* @author TANGMICHI
* @since 2022-11-18
*/
public interface IEmolumentPlanService{
    /**
    * 查询薪酬规划表
    *
    * @param emolumentPlanId 薪酬规划表主键
    * @return 薪酬规划表
    */
    EmolumentPlanDTO selectEmolumentPlanByEmolumentPlanId(Long emolumentPlanId);

    /**
    * 查询薪酬规划表列表
    *
    * @param emolumentPlanDTO 薪酬规划表
    * @return 薪酬规划表集合
    */
    List<EmolumentPlanDTO> selectEmolumentPlanList(EmolumentPlanDTO emolumentPlanDTO);

    /**
    * 新增薪酬规划表
    *
    * @param emolumentPlanDTO 薪酬规划表
    * @return 结果
    */
    EmolumentPlanDTO insertEmolumentPlan(EmolumentPlanDTO emolumentPlanDTO);

    /**
    * 修改薪酬规划表
    *
    * @param emolumentPlanDTO 薪酬规划表
    * @return 结果
    */
    int updateEmolumentPlan(EmolumentPlanDTO emolumentPlanDTO);

    /**
    * 批量修改薪酬规划表
    *
    * @param emolumentPlanDtos 薪酬规划表
    * @return 结果
    */
    int updateEmolumentPlans(List<EmolumentPlanDTO> emolumentPlanDtos);

    /**
    * 批量新增薪酬规划表
    *
    * @param emolumentPlanDtos 薪酬规划表
    * @return 结果
    */
    int insertEmolumentPlans(List<EmolumentPlanDTO> emolumentPlanDtos);

    /**
    * 逻辑批量删除薪酬规划表
    *
    * @param emolumentPlanIds 需要删除的薪酬规划表集合
    * @return 结果
    */
    int logicDeleteEmolumentPlanByEmolumentPlanIds(List<Long> emolumentPlanIds);

    /**
    * 逻辑删除薪酬规划表信息
    *
    * @param emolumentPlanDTO
    * @return 结果
    */
    int logicDeleteEmolumentPlanByEmolumentPlanId(EmolumentPlanDTO emolumentPlanDTO);
    /**
    * 批量删除薪酬规划表
    *
    * @param EmolumentPlanDtos
    * @return 结果
    */
    int deleteEmolumentPlanByEmolumentPlanIds(List<EmolumentPlanDTO> EmolumentPlanDtos);

    /**
    * 逻辑删除薪酬规划表信息
    *
    * @param emolumentPlanDTO
    * @return 结果
    */
    int deleteEmolumentPlanByEmolumentPlanId(EmolumentPlanDTO emolumentPlanDTO);


    /**
    * 删除薪酬规划表信息
    *
    * @param emolumentPlanId 薪酬规划表主键
    * @return 结果
    */
    int deleteEmolumentPlanByEmolumentPlanId(Long emolumentPlanId);

    /**
     *新增薪酬规划时预制数据
     * @param planYear
     * @return
     */
    EmolumentPlanDTO prefabricateAddEmolumentPlan(int planYear);
}
