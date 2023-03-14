package net.qixiaowei.strategy.cloud.service.strategyDecode;

import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;

import java.util.List;


/**
 * AnnualKeyWorkService接口
 *
 * @author Graves
 * @since 2023-03-14
 */
public interface IAnnualKeyWorkService {
    /**
     * 查询年度重点工作表
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 年度重点工作表
     */
    AnnualKeyWorkDTO selectAnnualKeyWorkByAnnualKeyWorkId(Long annualKeyWorkId);

    /**
     * 查询年度重点工作表列表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 年度重点工作表集合
     */
    List<AnnualKeyWorkDTO> selectAnnualKeyWorkList(AnnualKeyWorkDTO annualKeyWorkDTO);

    /**
     * 新增年度重点工作表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    AnnualKeyWorkDTO insertAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO);

    /**
     * 修改年度重点工作表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    int updateAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO);

    /**
     * 批量修改年度重点工作表
     *
     * @param annualKeyWorkDtos 年度重点工作表
     * @return 结果
     */
    int updateAnnualKeyWorks(List<AnnualKeyWorkDTO> annualKeyWorkDtos);

    /**
     * 批量新增年度重点工作表
     *
     * @param annualKeyWorkDtos 年度重点工作表
     * @return 结果
     */
    int insertAnnualKeyWorks(List<AnnualKeyWorkDTO> annualKeyWorkDtos);

    /**
     * 逻辑批量删除年度重点工作表
     *
     * @param annualKeyWorkIds 需要删除的年度重点工作表集合
     * @return 结果
     */
    int logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(List<Long> annualKeyWorkIds);

    /**
     * 逻辑删除年度重点工作表信息
     *
     * @param annualKeyWorkDTO
     * @return 结果
     */
    int logicDeleteAnnualKeyWorkByAnnualKeyWorkId(AnnualKeyWorkDTO annualKeyWorkDTO);

    /**
     * 批量删除年度重点工作表
     *
     * @param AnnualKeyWorkDtos
     * @return 结果
     */
    int deleteAnnualKeyWorkByAnnualKeyWorkIds(List<AnnualKeyWorkDTO> AnnualKeyWorkDtos);

    /**
     * 逻辑删除年度重点工作表信息
     *
     * @param annualKeyWorkDTO
     * @return 结果
     */
    int deleteAnnualKeyWorkByAnnualKeyWorkId(AnnualKeyWorkDTO annualKeyWorkDTO);


    /**
     * 删除年度重点工作表信息
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 结果
     */
    int deleteAnnualKeyWorkByAnnualKeyWorkId(Long annualKeyWorkId);
}
