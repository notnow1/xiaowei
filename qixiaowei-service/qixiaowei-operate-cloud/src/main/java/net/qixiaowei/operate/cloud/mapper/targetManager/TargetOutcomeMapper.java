package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcome;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* TargetOutcomeMapper接口
* @author TANGMICHI
* @since 2022-11-07
*/
public interface TargetOutcomeMapper{
    /**
    * 查询目标结果表
    *
    * @param targetOutcomeId 目标结果表主键
    * @return 目标结果表
    */
    TargetOutcomeDTO selectTargetOutcomeByTargetOutcomeId(@Param("targetOutcomeId")Long targetOutcomeId);


    /**
    * 批量查询目标结果表
    *
    * @param targetOutcomeIds 目标结果表主键集合
    * @return 目标结果表
    */
    List<TargetOutcomeDTO> selectTargetOutcomeByTargetOutcomeIds(@Param("targetOutcomeIds") List<Long> targetOutcomeIds);

    /**
    * 查询目标结果表列表
    *
    * @param targetOutcome 目标结果表
    * @return 目标结果表集合
    */
    List<TargetOutcomeDTO> selectTargetOutcomeList(@Param("targetOutcome")TargetOutcome targetOutcome);

    /**
    * 新增目标结果表
    *
    * @param targetOutcome 目标结果表
    * @return 结果
    */
    int insertTargetOutcome(@Param("targetOutcome")TargetOutcome targetOutcome);

    /**
    * 修改目标结果表
    *
    * @param targetOutcome 目标结果表
    * @return 结果
    */
    int updateTargetOutcome(@Param("targetOutcome")TargetOutcome targetOutcome);

    /**
    * 批量修改目标结果表
    *
    * @param targetOutcomeList 目标结果表
    * @return 结果
    */
    int updateTargetOutcomes(@Param("targetOutcomeList")List<TargetOutcome> targetOutcomeList);
    /**
    * 逻辑删除目标结果表
    *
    * @param targetOutcome
    * @return 结果
    */
    int logicDeleteTargetOutcomeByTargetOutcomeId(@Param("targetOutcome")TargetOutcome targetOutcome);

    /**
    * 逻辑批量删除目标结果表
    *
    * @param targetOutcomeIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteTargetOutcomeByTargetOutcomeIds(@Param("targetOutcomeIds")List<Long> targetOutcomeIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除目标结果表
    *
    * @param targetOutcomeId 目标结果表主键
    * @return 结果
    */
    int deleteTargetOutcomeByTargetOutcomeId(@Param("targetOutcomeId")Long targetOutcomeId);

    /**
    * 物理批量删除目标结果表
    *
    * @param targetOutcomeIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteTargetOutcomeByTargetOutcomeIds(@Param("targetOutcomeIds")List<Long> targetOutcomeIds);

    /**
    * 批量新增目标结果表
    *
    * @param TargetOutcomes 目标结果表列表
    * @return 结果
    */
    int batchTargetOutcome(@Param("targetOutcomes")List<TargetOutcome> TargetOutcomes);
}
