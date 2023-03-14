package net.qixiaowei.strategy.cloud.mapper.strategyDecode;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.AnnualKeyWorkDetail;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDetailDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* AnnualKeyWorkDetailMapper接口
* @author Graves
* @since 2023-03-14
*/
public interface AnnualKeyWorkDetailMapper{
    /**
    * 查询年度重点工作详情表
    *
    * @param annualKeyWorkDetailId 年度重点工作详情表主键
    * @return 年度重点工作详情表
    */
    AnnualKeyWorkDetailDTO selectAnnualKeyWorkDetailByAnnualKeyWorkDetailId(@Param("annualKeyWorkDetailId")Long annualKeyWorkDetailId);


    /**
    * 批量查询年度重点工作详情表
    *
    * @param annualKeyWorkDetailIds 年度重点工作详情表主键集合
    * @return 年度重点工作详情表
    */
    List<AnnualKeyWorkDetailDTO> selectAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(@Param("annualKeyWorkDetailIds") List<Long> annualKeyWorkDetailIds);

    /**
    * 查询年度重点工作详情表列表
    *
    * @param annualKeyWorkDetail 年度重点工作详情表
    * @return 年度重点工作详情表集合
    */
    List<AnnualKeyWorkDetailDTO> selectAnnualKeyWorkDetailList(@Param("annualKeyWorkDetail")AnnualKeyWorkDetail annualKeyWorkDetail);

    /**
    * 新增年度重点工作详情表
    *
    * @param annualKeyWorkDetail 年度重点工作详情表
    * @return 结果
    */
    int insertAnnualKeyWorkDetail(@Param("annualKeyWorkDetail")AnnualKeyWorkDetail annualKeyWorkDetail);

    /**
    * 修改年度重点工作详情表
    *
    * @param annualKeyWorkDetail 年度重点工作详情表
    * @return 结果
    */
    int updateAnnualKeyWorkDetail(@Param("annualKeyWorkDetail")AnnualKeyWorkDetail annualKeyWorkDetail);

    /**
    * 批量修改年度重点工作详情表
    *
    * @param annualKeyWorkDetailList 年度重点工作详情表
    * @return 结果
    */
    int updateAnnualKeyWorkDetails(@Param("annualKeyWorkDetailList")List<AnnualKeyWorkDetail> annualKeyWorkDetailList);
    /**
    * 逻辑删除年度重点工作详情表
    *
    * @param annualKeyWorkDetail
    * @return 结果
    */
    int logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(@Param("annualKeyWorkDetail")AnnualKeyWorkDetail annualKeyWorkDetail);

    /**
    * 逻辑批量删除年度重点工作详情表
    *
    * @param annualKeyWorkDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(@Param("annualKeyWorkDetailIds")List<Long> annualKeyWorkDetailIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除年度重点工作详情表
    *
    * @param annualKeyWorkDetailId 年度重点工作详情表主键
    * @return 结果
    */
    int deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(@Param("annualKeyWorkDetailId")Long annualKeyWorkDetailId);

    /**
    * 物理批量删除年度重点工作详情表
    *
    * @param annualKeyWorkDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(@Param("annualKeyWorkDetailIds")List<Long> annualKeyWorkDetailIds);

    /**
    * 批量新增年度重点工作详情表
    *
    * @param annualKeyWorkDetails 年度重点工作详情表列表
    * @return 结果
    */
    int batchAnnualKeyWorkDetail(@Param("annualKeyWorkDetails")List<AnnualKeyWorkDetail> annualKeyWorkDetails);
}
