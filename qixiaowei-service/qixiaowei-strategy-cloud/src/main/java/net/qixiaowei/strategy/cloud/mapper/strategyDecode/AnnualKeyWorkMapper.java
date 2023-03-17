package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.AnnualKeyWork;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * AnnualKeyWorkMapper接口
 *
 * @author Graves
 * @since 2023-03-14
 */
public interface AnnualKeyWorkMapper {
    /**
     * 查询年度重点工作表
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 年度重点工作表
     */
    AnnualKeyWorkDTO selectAnnualKeyWorkByAnnualKeyWorkId(@Param("annualKeyWorkId") Long annualKeyWorkId);


    /**
     * 批量查询年度重点工作表
     *
     * @param annualKeyWorkIds 年度重点工作表主键集合
     * @return 年度重点工作表
     */
    List<AnnualKeyWorkDTO> selectAnnualKeyWorkByAnnualKeyWorkIds(@Param("annualKeyWorkIds") List<Long> annualKeyWorkIds);

    /**
     * 查询年度重点工作表列表
     *
     * @param annualKeyWork 年度重点工作表
     * @return 年度重点工作表集合
     */
    List<AnnualKeyWorkDTO> selectAnnualKeyWorkList(@Param("annualKeyWork") AnnualKeyWork annualKeyWork);

    /**
     * 新增年度重点工作表
     *
     * @param annualKeyWork 年度重点工作表
     * @return 结果
     */
    int insertAnnualKeyWork(@Param("annualKeyWork") AnnualKeyWork annualKeyWork);

    /**
     * 修改年度重点工作表
     *
     * @param annualKeyWork 年度重点工作表
     * @return 结果
     */
    int updateAnnualKeyWork(@Param("annualKeyWork") AnnualKeyWork annualKeyWork);

    /**
     * 批量修改年度重点工作表
     *
     * @param annualKeyWorkList 年度重点工作表
     * @return 结果
     */
    int updateAnnualKeyWorks(@Param("annualKeyWorkList") List<AnnualKeyWork> annualKeyWorkList);

    /**
     * 逻辑删除年度重点工作表
     *
     * @param annualKeyWork
     * @return 结果
     */
    int logicDeleteAnnualKeyWorkByAnnualKeyWorkId(@Param("annualKeyWork") AnnualKeyWork annualKeyWork);

    /**
     * 逻辑批量删除年度重点工作表
     *
     * @param annualKeyWorkIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(@Param("annualKeyWorkIds") List<Long> annualKeyWorkIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除年度重点工作表
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 结果
     */
    int deleteAnnualKeyWorkByAnnualKeyWorkId(@Param("annualKeyWorkId") Long annualKeyWorkId);

    /**
     * 物理批量删除年度重点工作表
     *
     * @param annualKeyWorkIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteAnnualKeyWorkByAnnualKeyWorkIds(@Param("annualKeyWorkIds") List<Long> annualKeyWorkIds);

    /**
     * 批量新增年度重点工作表
     *
     * @param annualKeyWorks 年度重点工作表列表
     * @return 结果
     */
    int batchAnnualKeyWork(@Param("annualKeyWorks") List<AnnualKeyWork> annualKeyWorks);

    /**
     * 根据维度ID集合查询
     *
     * @param planBusinessUnitIds 维度ID集合
     * @return list
     */
    List<AnnualKeyWorkDTO> selectAnnualKeyWorkByPlanBusinessUnitIds(@Param("planBusinessUnitIds") List<Long> planBusinessUnitIds);
}
