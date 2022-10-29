package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;
import net.qixiaowei.operate.cloud.api.domain.targetManager.DecomposeDetailCycles;
import net.qixiaowei.operate.cloud.api.dto.targetManager.DecomposeDetailCyclesDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* DecomposeDetailCyclesMapper接口
* @author TANGMICHI
* @since 2022-10-28
*/
public interface DecomposeDetailCyclesMapper{
    /**
    * 查询目标分解详情周期表
    *
    * @param decomposeDetailCyclesId 目标分解详情周期表主键
    * @return 目标分解详情周期表
    */
    DecomposeDetailCyclesDTO selectDecomposeDetailCyclesByDecomposeDetailCyclesId(@Param("decomposeDetailCyclesId")Long decomposeDetailCyclesId);
    /**
     * 根据详情分解id查询目标分解详情周期表
     *
     * @param targetDecomposeDetailsId 目标分解详情周期表主键
     * @return 目标分解详情周期表
     */
    List<DecomposeDetailCyclesDTO> selectDecomposeDetailCyclesByTargetDecomposeDetailsId(@Param("targetDecomposeDetailsId")Long targetDecomposeDetailsId);

    /**
     * 根据详情分解id批量查询目标分解详情周期表
     *
     * @param targetDecomposeDetailsIds 目标分解详情周期表主键
     * @return 目标分解详情周期表
     */
    List<DecomposeDetailCyclesDTO> selectDecomposeDetailCyclesByTargetDecomposeDetailsIds(@Param("targetDecomposeDetailsIds")List<Long> targetDecomposeDetailsIds);

    /**
    * 批量查询目标分解详情周期表
    *
    * @param decomposeDetailCyclesIds 目标分解详情周期表主键集合
    * @return 目标分解详情周期表
    */
    List<DecomposeDetailCyclesDTO> selectDecomposeDetailCyclesByDecomposeDetailCyclesIds(@Param("decomposeDetailCyclesIds") List<Long> decomposeDetailCyclesIds);

    /**
    * 查询目标分解详情周期表列表
    *
    * @param decomposeDetailCycles 目标分解详情周期表
    * @return 目标分解详情周期表集合
    */
    List<DecomposeDetailCyclesDTO> selectDecomposeDetailCyclesList(@Param("decomposeDetailCycles")DecomposeDetailCycles decomposeDetailCycles);

    /**
    * 新增目标分解详情周期表
    *
    * @param decomposeDetailCycles 目标分解详情周期表
    * @return 结果
    */
    int insertDecomposeDetailCycles(@Param("decomposeDetailCycles")DecomposeDetailCycles decomposeDetailCycles);

    /**
    * 修改目标分解详情周期表
    *
    * @param decomposeDetailCycles 目标分解详情周期表
    * @return 结果
    */
    int updateDecomposeDetailCycles(@Param("decomposeDetailCycles")DecomposeDetailCycles decomposeDetailCycles);

    /**
    * 批量修改目标分解详情周期表
    *
    * @param decomposeDetailCyclesList 目标分解详情周期表
    * @return 结果
    */
    int updateDecomposeDetailCycless(@Param("decomposeDetailCyclesList")List<DecomposeDetailCycles> decomposeDetailCyclesList);
    /**
    * 逻辑删除目标分解详情周期表
    *
    * @param decomposeDetailCycles
    * @return 结果
    */
    int logicDeleteDecomposeDetailCyclesByDecomposeDetailCyclesId(@Param("decomposeDetailCycles")DecomposeDetailCycles decomposeDetailCycles);

    /**
    * 逻辑批量删除目标分解详情周期表
    *
    * @param decomposeDetailCyclesIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteDecomposeDetailCyclesByDecomposeDetailCyclesIds(@Param("decomposeDetailCyclesIds")List<Long> decomposeDetailCyclesIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);

    /**
     * 根据目标分解详情id逻辑批量删除目标分解详情周期表
     *
     * @param targetDecomposeDetailsIdS 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteDecomposeDetailCyclesByTargetDecomposeDetailsIds(@Param("targetDecomposeDetailsIdS")List<Long> targetDecomposeDetailsIdS,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除目标分解详情周期表
    *
    * @param decomposeDetailCyclesId 目标分解详情周期表主键
    * @return 结果
    */
    int deleteDecomposeDetailCyclesByDecomposeDetailCyclesId(@Param("decomposeDetailCyclesId")Long decomposeDetailCyclesId);

    /**
    * 物理批量删除目标分解详情周期表
    *
    * @param decomposeDetailCyclesIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteDecomposeDetailCyclesByDecomposeDetailCyclesIds(@Param("decomposeDetailCyclesIds")List<Long> decomposeDetailCyclesIds);

    /**
    * 批量新增目标分解详情周期表
    *
    * @param DecomposeDetailCycless 目标分解详情周期表列表
    * @return 结果
    */
    int batchDecomposeDetailCycles(@Param("decomposeDetailCycless")List<DecomposeDetailCycles> DecomposeDetailCycless);
}
