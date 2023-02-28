package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiMacroDetail;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiMacroDetailDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiMacroDetailMapper接口
* @author TANGMICHI
* @since 2023-02-28
*/
public interface MiMacroDetailMapper{
    /**
    * 查询市场洞察宏观详情表
    *
    * @param miMacroDetailId 市场洞察宏观详情表主键
    * @return 市场洞察宏观详情表
    */
    MiMacroDetailDTO selectMiMacroDetailByMiMacroDetailId(@Param("miMacroDetailId")Long miMacroDetailId);


    /**
    * 批量查询市场洞察宏观详情表
    *
    * @param miMacroDetailIds 市场洞察宏观详情表主键集合
    * @return 市场洞察宏观详情表
    */
    List<MiMacroDetailDTO> selectMiMacroDetailByMiMacroDetailIds(@Param("miMacroDetailIds") List<Long> miMacroDetailIds);

    /**
    * 查询市场洞察宏观详情表列表
    *
    * @param miMacroDetail 市场洞察宏观详情表
    * @return 市场洞察宏观详情表集合
    */
    List<MiMacroDetailDTO> selectMiMacroDetailList(@Param("miMacroDetail")MiMacroDetail miMacroDetail);

    /**
    * 新增市场洞察宏观详情表
    *
    * @param miMacroDetail 市场洞察宏观详情表
    * @return 结果
    */
    int insertMiMacroDetail(@Param("miMacroDetail")MiMacroDetail miMacroDetail);

    /**
    * 修改市场洞察宏观详情表
    *
    * @param miMacroDetail 市场洞察宏观详情表
    * @return 结果
    */
    int updateMiMacroDetail(@Param("miMacroDetail")MiMacroDetail miMacroDetail);

    /**
    * 批量修改市场洞察宏观详情表
    *
    * @param miMacroDetailList 市场洞察宏观详情表
    * @return 结果
    */
    int updateMiMacroDetails(@Param("miMacroDetailList")List<MiMacroDetail> miMacroDetailList);
    /**
    * 逻辑删除市场洞察宏观详情表
    *
    * @param miMacroDetail
    * @return 结果
    */
    int logicDeleteMiMacroDetailByMiMacroDetailId(@Param("miMacroDetail")MiMacroDetail miMacroDetail);

    /**
    * 逻辑批量删除市场洞察宏观详情表
    *
    * @param miMacroDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiMacroDetailByMiMacroDetailIds(@Param("miMacroDetailIds")List<Long> miMacroDetailIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察宏观详情表
    *
    * @param miMacroDetailId 市场洞察宏观详情表主键
    * @return 结果
    */
    int deleteMiMacroDetailByMiMacroDetailId(@Param("miMacroDetailId")Long miMacroDetailId);

    /**
    * 物理批量删除市场洞察宏观详情表
    *
    * @param miMacroDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiMacroDetailByMiMacroDetailIds(@Param("miMacroDetailIds")List<Long> miMacroDetailIds);

    /**
    * 批量新增市场洞察宏观详情表
    *
    * @param miMacroDetails 市场洞察宏观详情表列表
    * @return 结果
    */
    int batchMiMacroDetail(@Param("miMacroDetails")List<MiMacroDetail> miMacroDetails);
}
