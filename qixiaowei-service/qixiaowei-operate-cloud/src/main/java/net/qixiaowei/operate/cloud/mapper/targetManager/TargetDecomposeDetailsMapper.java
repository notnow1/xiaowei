package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeDetails;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Map;


/**
 * TargetDecomposeDetailsMapper接口
 *
 * @author TANGMICHI
 * @since 2022-10-28
 */
public interface TargetDecomposeDetailsMapper {
    /**
     * 查询目标分解详情表
     *
     * @param targetDecomposeDetailsId 目标分解详情表主键
     * @return 目标分解详情表
     */
    TargetDecomposeDetailsDTO selectTargetDecomposeDetailsByTargetDecomposeDetailsId(@Param("targetDecomposeDetailsId") Long targetDecomposeDetailsId);


    /**
     * 根据分解id查询目标分解详情表
     *
     * @param targetDecomposeId 目标分解详情表主键
     * @return 目标分解详情表
     */
    List<TargetDecomposeDetailsDTO> selectTargetDecomposeDetailsByTargetDecomposeId(@Param("targetDecomposeId") Long targetDecomposeId);

    /**
     * 根据分解id查询目标分解详情表只查询自己的
     *
     * @param targetDecomposeId 目标分解详情表主键
     * @param employeeId        人员id
     * @return 目标分解详情表
     */
    List<TargetDecomposeDetailsDTO> selectTargetDecomposeDetailsByPowerTargetDecomposeId(@Param("targetDecomposeId") Long targetDecomposeId, @Param("employeeId") Long employeeId);

    /**
     * 根据分解id批量查询目标分解详情表只查询自己的
     *
     * @param targetDecomposeIds 目标分解详情表主键集合
     * @return 目标分解详情表
     */
    List<TargetDecomposeDetailsDTO> selectTargetDecomposeDetailsByTargetDecomposeIds(@Param("targetDecomposeIds") List<Long> targetDecomposeIds);

    /**
     * 批量查询目标分解详情表
     *
     * @param targetDecomposeDetailsIds 目标分解详情表主键集合
     * @return 目标分解详情表
     */
    List<TargetDecomposeDetailsDTO> selectTargetDecomposeDetailsByTargetDecomposeDetailsIds(@Param("targetDecomposeDetailsIds") List<Long> targetDecomposeDetailsIds);

    /**
     * 查询目标分解详情表列表
     *
     * @param targetDecomposeDetails 目标分解详情表
     * @return 目标分解详情表集合
     */
    List<TargetDecomposeDetailsDTO> selectTargetDecomposeDetailsList(@Param("targetDecomposeDetails") TargetDecomposeDetails targetDecomposeDetails);

    /**
     * 新增目标分解详情表
     *
     * @param targetDecomposeDetails 目标分解详情表
     * @return 结果
     */
    int insertTargetDecomposeDetails(@Param("targetDecomposeDetails") TargetDecomposeDetails targetDecomposeDetails);

    /**
     * 修改目标分解详情表
     *
     * @param targetDecomposeDetails 目标分解详情表
     * @return 结果
     */
    int updateTargetDecomposeDetails(@Param("targetDecomposeDetails") TargetDecomposeDetails targetDecomposeDetails);

    /**
     * 批量修改目标分解详情表
     *
     * @param targetDecomposeDetailsList 目标分解详情表
     * @return 结果
     */
    int updateTargetDecomposeDetailss(@Param("targetDecomposeDetailsList") List<TargetDecomposeDetails> targetDecomposeDetailsList);

    /**
     * 逻辑删除目标分解详情表
     *
     * @param targetDecomposeDetails
     * @return 结果
     */
    int logicDeleteTargetDecomposeDetailsByTargetDecomposeDetailsId(@Param("targetDecomposeDetails") TargetDecomposeDetails targetDecomposeDetails);

    /**
     * 逻辑批量删除目标分解详情表
     *
     * @param targetDecomposeDetailsIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetDecomposeDetailsByTargetDecomposeDetailsIds(@Param("targetDecomposeDetailsIds") List<Long> targetDecomposeDetailsIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除目标分解详情表
     *
     * @param targetDecomposeDetailsId 目标分解详情表主键
     * @return 结果
     */
    int deleteTargetDecomposeDetailsByTargetDecomposeDetailsId(@Param("targetDecomposeDetailsId") Long targetDecomposeDetailsId);

    /**
     * 物理批量删除目标分解详情表
     *
     * @param targetDecomposeDetailsIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTargetDecomposeDetailsByTargetDecomposeDetailsIds(@Param("targetDecomposeDetailsIds") List<Long> targetDecomposeDetailsIds);

    /**
     * 批量新增目标分解详情表
     *
     * @param TargetDecomposeDetailss 目标分解详情表列表
     * @return 结果
     */
    int batchTargetDecomposeDetails(@Param("targetDecomposeDetailss") List<TargetDecomposeDetails> TargetDecomposeDetailss);

    List<TargetDecomposeDetailsDTO> selectByIds(@Param("employeeIds") List<Long> employeeIds, @Param("areaIds") List<Long> areaIds, @Param("departmentIds") List<Long> departmentIds,
                                                @Param("productIds") List<Long> productIds, @Param("regionIds") List<Long> regionIds, @Param("industryIds") List<Long> industryIds,
                                                @Param("principalEmployeeIds") List<Long> principalEmployeeIds);
}
