package net.qixiaowei.operate.cloud.mapper.bonus;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.bonus.BonusPayApplication;
import net.qixiaowei.operate.cloud.api.dto.bonus.BonusPayApplicationDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* BonusPayApplicationMapper接口
* @author TANGMICHI
* @since 2022-12-08
*/
public interface BonusPayApplicationMapper{
    /**
    * 查询奖金发放申请表
    *
    * @param bonusPayApplicationId 奖金发放申请表主键
    * @return 奖金发放申请表
    */
    BonusPayApplicationDTO selectBonusPayApplicationByBonusPayApplicationId(@Param("bonusPayApplicationId")Long bonusPayApplicationId);


    /**
    * 批量查询奖金发放申请表
    *
    * @param bonusPayApplicationIds 奖金发放申请表主键集合
    * @return 奖金发放申请表
    */
    List<BonusPayApplicationDTO> selectBonusPayApplicationByBonusPayApplicationIds(@Param("bonusPayApplicationIds") List<Long> bonusPayApplicationIds);

    /**
    * 查询奖金发放申请表列表
    *
    * @param bonusPayApplication 奖金发放申请表
    * @return 奖金发放申请表集合
    */
    List<BonusPayApplicationDTO> selectBonusPayApplicationList(@Param("bonusPayApplication")BonusPayApplication bonusPayApplication);

    /**
    * 新增奖金发放申请表
    *
    * @param bonusPayApplication 奖金发放申请表
    * @return 结果
    */
    int insertBonusPayApplication(@Param("bonusPayApplication")BonusPayApplication bonusPayApplication);

    /**
    * 修改奖金发放申请表
    *
    * @param bonusPayApplication 奖金发放申请表
    * @return 结果
    */
    int updateBonusPayApplication(@Param("bonusPayApplication")BonusPayApplication bonusPayApplication);

    /**
    * 批量修改奖金发放申请表
    *
    * @param bonusPayApplicationList 奖金发放申请表
    * @return 结果
    */
    int updateBonusPayApplications(@Param("bonusPayApplicationList")List<BonusPayApplication> bonusPayApplicationList);
    /**
    * 逻辑删除奖金发放申请表
    *
    * @param bonusPayApplication
    * @return 结果
    */
    int logicDeleteBonusPayApplicationByBonusPayApplicationId(@Param("bonusPayApplication")BonusPayApplication bonusPayApplication);

    /**
    * 逻辑批量删除奖金发放申请表
    *
    * @param bonusPayApplicationIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteBonusPayApplicationByBonusPayApplicationIds(@Param("bonusPayApplicationIds")List<Long> bonusPayApplicationIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除奖金发放申请表
    *
    * @param bonusPayApplicationId 奖金发放申请表主键
    * @return 结果
    */
    int deleteBonusPayApplicationByBonusPayApplicationId(@Param("bonusPayApplicationId")Long bonusPayApplicationId);

    /**
    * 物理批量删除奖金发放申请表
    *
    * @param bonusPayApplicationIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteBonusPayApplicationByBonusPayApplicationIds(@Param("bonusPayApplicationIds")List<Long> bonusPayApplicationIds);

    /**
    * 批量新增奖金发放申请表
    *
    * @param BonusPayApplications 奖金发放申请表列表
    * @return 结果
    */
    int batchBonusPayApplication(@Param("bonusPayApplications")List<BonusPayApplication> BonusPayApplications);
}
