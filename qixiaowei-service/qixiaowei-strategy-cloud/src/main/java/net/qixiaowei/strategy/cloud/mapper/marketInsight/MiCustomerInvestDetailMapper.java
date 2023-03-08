package net.qixiaowei.strategy.cloud.mapper.marketInsight;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.marketInsight.MiCustomerInvestDetail;
import net.qixiaowei.strategy.cloud.api.dto.marketInsight.MiCustomerInvestDetailDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* MiCustomerInvestDetailMapper接口
* @author TANGMICHI
* @since 2023-03-07
*/
public interface MiCustomerInvestDetailMapper{
    /**
    * 查询市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetailId 市场洞察客户投资详情表主键
    * @return 市场洞察客户投资详情表
    */
    MiCustomerInvestDetailDTO selectMiCustomerInvestDetailByMiCustomerInvestDetailId(@Param("miCustomerInvestDetailId")Long miCustomerInvestDetailId);

    /**
     * 根据市场洞察客户投资表主键查询市场洞察客户投资详情表
     *
     * @param miCustomerInvestPlanId 市场洞察客户投资表主键
     * @return 市场洞察客户投资详情表
     */
    List<MiCustomerInvestDetailDTO> selectMiCustomerInvestDetailByMiCustomerInvestPlanId(@Param("miCustomerInvestPlanId")Long miCustomerInvestPlanId);

    /**
    * 批量查询市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetailIds 市场洞察客户投资详情表主键集合
    * @return 市场洞察客户投资详情表
    */
    List<MiCustomerInvestDetailDTO> selectMiCustomerInvestDetailByMiCustomerInvestDetailIds(@Param("miCustomerInvestDetailIds") List<Long> miCustomerInvestDetailIds);

    /**
     * 根据市场洞察客户投资表主键集合批量查询市场洞察客户投资详情表
     *
     * @param miCustomerInvestPlanIds 市场洞察客户投资表主键集合
     * @return 市场洞察客户投资详情表
     */
    List<MiCustomerInvestDetailDTO> selectMiCustomerInvestDetailByMiCustomerInvestPlanIds(@Param("miCustomerInvestPlanIds")List<Long> miCustomerInvestPlanIds);
    /**
    * 查询市场洞察客户投资详情表列表
    *
    * @param miCustomerInvestDetail 市场洞察客户投资详情表
    * @return 市场洞察客户投资详情表集合
    */
    List<MiCustomerInvestDetailDTO> selectMiCustomerInvestDetailList(@Param("miCustomerInvestDetail")MiCustomerInvestDetail miCustomerInvestDetail);

    /**
    * 新增市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetail 市场洞察客户投资详情表
    * @return 结果
    */
    int insertMiCustomerInvestDetail(@Param("miCustomerInvestDetail")MiCustomerInvestDetail miCustomerInvestDetail);

    /**
    * 修改市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetail 市场洞察客户投资详情表
    * @return 结果
    */
    int updateMiCustomerInvestDetail(@Param("miCustomerInvestDetail")MiCustomerInvestDetail miCustomerInvestDetail);

    /**
    * 批量修改市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetailList 市场洞察客户投资详情表
    * @return 结果
    */
    int updateMiCustomerInvestDetails(@Param("miCustomerInvestDetailList")List<MiCustomerInvestDetail> miCustomerInvestDetailList);
    /**
    * 逻辑删除市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetail
    * @return 结果
    */
    int logicDeleteMiCustomerInvestDetailByMiCustomerInvestDetailId(@Param("miCustomerInvestDetail")MiCustomerInvestDetail miCustomerInvestDetail);

    /**
    * 逻辑批量删除市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteMiCustomerInvestDetailByMiCustomerInvestDetailIds(@Param("miCustomerInvestDetailIds")List<Long> miCustomerInvestDetailIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetailId 市场洞察客户投资详情表主键
    * @return 结果
    */
    int deleteMiCustomerInvestDetailByMiCustomerInvestDetailId(@Param("miCustomerInvestDetailId")Long miCustomerInvestDetailId);

    /**
    * 物理批量删除市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetailIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteMiCustomerInvestDetailByMiCustomerInvestDetailIds(@Param("miCustomerInvestDetailIds")List<Long> miCustomerInvestDetailIds);

    /**
    * 批量新增市场洞察客户投资详情表
    *
    * @param miCustomerInvestDetails 市场洞察客户投资详情表列表
    * @return 结果
    */
    int batchMiCustomerInvestDetail(@Param("miCustomerInvestDetails")List<MiCustomerInvestDetail> miCustomerInvestDetails);
}
