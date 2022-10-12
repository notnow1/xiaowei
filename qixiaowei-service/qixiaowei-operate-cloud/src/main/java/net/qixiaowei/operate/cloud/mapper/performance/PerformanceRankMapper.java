package net.qixiaowei.operate.cloud.mapper.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceRank;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceRankDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * PerformanceRankMapper接口
 *
 * @author Graves
 * @since 2022-10-06
 */
public interface PerformanceRankMapper {
    /**
     * 查询绩效等级表
     *
     * @param performanceRankId 绩效等级表主键
     * @return 绩效等级表
     */
    PerformanceRankDTO selectPerformanceRankByPerformanceRankId(@Param("performanceRankId") Long performanceRankId);

    /**
     * 查询绩效等级表列表
     *
     * @param performanceRank 绩效等级表
     * @return 绩效等级表集合
     */
    List<PerformanceRankDTO> selectPerformanceRankList(@Param("performanceRank") PerformanceRank performanceRank);

    /**
     * 新增绩效等级表
     *
     * @param performanceRank 绩效等级表
     * @return 结果
     */
    int insertPerformanceRank(@Param("performanceRank") PerformanceRank performanceRank);

    /**
     * 修改绩效等级表
     *
     * @param performanceRank 绩效等级表
     * @return 结果
     */
    int updatePerformanceRank(@Param("performanceRank") PerformanceRank performanceRank);

    /**
     * 批量修改绩效等级表
     *
     * @param performanceRankList 绩效等级表
     * @return 结果
     */
    int updatePerformanceRanks(@Param("performanceRankList") List<PerformanceRank> performanceRankList);

    /**
     * 逻辑删除绩效等级表
     *
     * @param performanceRank
     * @return 结果
     */
    int logicDeletePerformanceRankByPerformanceRankId(@Param("performanceRank") PerformanceRank performanceRank, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除绩效等级表
     *
     * @param performanceRankIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformanceRankByPerformanceRankIds(@Param("performanceRankIds") List<Long> performanceRankIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效等级表
     *
     * @param performanceRankId 绩效等级表主键
     * @return 结果
     */
    int deletePerformanceRankByPerformanceRankId(@Param("performanceRankId") Long performanceRankId);

    /**
     * 物理批量删除绩效等级表
     *
     * @param performanceRankIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformanceRankByPerformanceRankIds(@Param("performanceRankIds") List<Long> performanceRankIds);

    /**
     * 批量新增绩效等级表
     *
     * @param PerformanceRanks 绩效等级表列表
     * @return 结果
     */
    int batchPerformanceRank(@Param("performanceRanks") List<PerformanceRank> PerformanceRanks);

    /**
     * 绩效等级名称重复校验
     *
     * @param performanceRankName
     * @return
     */
    int checkUniqueName(@Param("performanceRankName") String performanceRankName);

    /**
     * 查询与绩效等级关联的绩效等级系数Ids
     *
     * @param performanceRankIds
     * @return
     */
    List<Long> selectPerformanceRankFactorIds(@Param("performanceRankIds") List<Long> performanceRankIds);

    /**
     * 删除前查看ids是否存在
     *
     * @param performanceRankIds
     * @return
     */
    int isExist(@Param("performanceRankIds") List<Long> performanceRankIds);

    /**
     * 获取组织绩效等级体系表
     *
     * @return
     */
    List<PerformanceRank> selectOrganizeDto();

    /**
     * 获取个人绩效等级体系表
     *
     * @return
     */
    List<PerformanceRank> selectPersonDto();

    /**
     * 通过ids查找绩效等级列表
     *
     * @param orgPerformanceRankIds
     * @return
     */
    List<PerformanceRank> selectPerformanceRank(@Param("orgPerformanceRankIds") List<Long> orgPerformanceRankIds);

}
