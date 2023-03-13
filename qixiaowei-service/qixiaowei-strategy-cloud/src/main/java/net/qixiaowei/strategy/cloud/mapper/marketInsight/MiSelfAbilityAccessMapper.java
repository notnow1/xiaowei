package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiSelfAbilityAccess;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiSelfAbilityAccessDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiSelfAbilityAccessMapper接口
* @author TANGMICHI
* @since 2023-03-13
*/
public interface MiSelfAbilityAccessMapper{
    /**
    * 查询市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccessId 市场洞察自身能力评估表主键
    * @return 市场洞察自身能力评估表
    */
    MiSelfAbilityAccessDTO selectMiSelfAbilityAccessByMiSelfAbilityAccessId(@Param("miSelfAbilityAccessId")Long miSelfAbilityAccessId);


    /**
    * 批量查询市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccessIds 市场洞察自身能力评估表主键集合
    * @return 市场洞察自身能力评估表
    */
    List<MiSelfAbilityAccessDTO> selectMiSelfAbilityAccessByMiSelfAbilityAccessIds(@Param("miSelfAbilityAccessIds") List<Long> miSelfAbilityAccessIds);

    /**
    * 查询市场洞察自身能力评估表列表
    *
    * @param miSelfAbilityAccess 市场洞察自身能力评估表
    * @return 市场洞察自身能力评估表集合
    */
    List<MiSelfAbilityAccessDTO> selectMiSelfAbilityAccessList(@Param("miSelfAbilityAccess")MiSelfAbilityAccess miSelfAbilityAccess);

    /**
    * 新增市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccess 市场洞察自身能力评估表
    * @return 结果
    */
    int insertMiSelfAbilityAccess(@Param("miSelfAbilityAccess")MiSelfAbilityAccess miSelfAbilityAccess);

    /**
    * 修改市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccess 市场洞察自身能力评估表
    * @return 结果
    */
    int updateMiSelfAbilityAccess(@Param("miSelfAbilityAccess")MiSelfAbilityAccess miSelfAbilityAccess);

    /**
    * 批量修改市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccessList 市场洞察自身能力评估表
    * @return 结果
    */
    int updateMiSelfAbilityAccesss(@Param("miSelfAbilityAccessList")List<MiSelfAbilityAccess> miSelfAbilityAccessList);
    /**
    * 逻辑删除市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccess
    * @return 结果
    */
    int logicDeleteMiSelfAbilityAccessByMiSelfAbilityAccessId(@Param("miSelfAbilityAccess")MiSelfAbilityAccess miSelfAbilityAccess);

    /**
    * 逻辑批量删除市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccessIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiSelfAbilityAccessByMiSelfAbilityAccessIds(@Param("miSelfAbilityAccessIds")List<Long> miSelfAbilityAccessIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccessId 市场洞察自身能力评估表主键
    * @return 结果
    */
    int deleteMiSelfAbilityAccessByMiSelfAbilityAccessId(@Param("miSelfAbilityAccessId")Long miSelfAbilityAccessId);

    /**
    * 物理批量删除市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccessIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiSelfAbilityAccessByMiSelfAbilityAccessIds(@Param("miSelfAbilityAccessIds")List<Long> miSelfAbilityAccessIds);

    /**
    * 批量新增市场洞察自身能力评估表
    *
    * @param miSelfAbilityAccesss 市场洞察自身能力评估表列表
    * @return 结果
    */
    int batchMiSelfAbilityAccess(@Param("miSelfAbilityAccesss")List<MiSelfAbilityAccess> miSelfAbilityAccesss);
}
