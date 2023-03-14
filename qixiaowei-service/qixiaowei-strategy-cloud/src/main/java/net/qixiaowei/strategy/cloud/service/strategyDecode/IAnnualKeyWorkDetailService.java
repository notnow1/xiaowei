package net.qixiaowei.strategy.cloud.service.strategyDecode;

import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDetailDTO;

import java.util.List;


/**
 * AnnualKeyWorkDetailService接口
 *
 * @author Graves
 * @since 2023-03-14
 */
public interface IAnnualKeyWorkDetailService {
    /**
     * 查询年度重点工作详情表
     *
     * @param annualKeyWorkDetailId 年度重点工作详情表主键
     * @return 年度重点工作详情表
     */
    AnnualKeyWorkDetailDTO selectAnnualKeyWorkDetailByAnnualKeyWorkDetailId(Long annualKeyWorkDetailId);

    /**
     * 查询年度重点工作详情表列表
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return 年度重点工作详情表集合
     */
    List<AnnualKeyWorkDetailDTO> selectAnnualKeyWorkDetailList(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO);

    /**
     * 新增年度重点工作详情表
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return 结果
     */
    AnnualKeyWorkDetailDTO insertAnnualKeyWorkDetail(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO);

    /**
     * 修改年度重点工作详情表
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return 结果
     */
    int updateAnnualKeyWorkDetail(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO);

    /**
     * 批量修改年度重点工作详情表
     *
     * @param annualKeyWorkDetailDtos 年度重点工作详情表
     * @return 结果
     */
    int updateAnnualKeyWorkDetails(List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDtos);

    /**
     * 批量新增年度重点工作详情表
     *
     * @param annualKeyWorkDetailDtos 年度重点工作详情表
     * @return 结果
     */
    int insertAnnualKeyWorkDetails(List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDtos);

    /**
     * 逻辑批量删除年度重点工作详情表
     *
     * @param annualKeyWorkDetailIds 需要删除的年度重点工作详情表集合
     * @return 结果
     */
    int logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(List<Long> annualKeyWorkDetailIds);

    /**
     * 逻辑删除年度重点工作详情表信息
     *
     * @param annualKeyWorkDetailDTO
     * @return 结果
     */
    int logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO);

    /**
     * 批量删除年度重点工作详情表
     *
     * @param AnnualKeyWorkDetailDtos
     * @return 结果
     */
    int deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(List<AnnualKeyWorkDetailDTO> AnnualKeyWorkDetailDtos);

    /**
     * 逻辑删除年度重点工作详情表信息
     *
     * @param annualKeyWorkDetailDTO
     * @return 结果
     */
    int deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO);


    /**
     * 删除年度重点工作详情表信息
     *
     * @param annualKeyWorkDetailId 年度重点工作详情表主键
     * @return 结果
     */
    int deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(Long annualKeyWorkDetailId);
}
