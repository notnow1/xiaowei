package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingOrder;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * TargetSettingOrderMapper接口
 *
 * @author Graves
 * @since 2022-10-27
 */
public interface TargetSettingOrderMapper {
    /**
     * 查询目标制定订单表
     *
     * @param targetSettingOrderId 目标制定订单表主键
     * @return 目标制定订单表
     */
    TargetSettingOrderDTO selectTargetSettingOrderByTargetSettingOrderId(@Param("targetSettingOrderId") Long targetSettingOrderId);


    /**
     * 批量查询目标制定订单表
     *
     * @param targetSettingOrderIds 目标制定订单表主键集合
     * @return 目标制定订单表
     */
    List<TargetSettingOrderDTO> selectTargetSettingOrderByTargetSettingOrderIds(@Param("targetSettingOrderIds") List<Long> targetSettingOrderIds);

    /**
     * 查询目标制定订单表列表
     *
     * @param targetSettingOrder 目标制定订单表
     * @return 目标制定订单表集合
     */
    List<TargetSettingOrderDTO> selectTargetSettingOrderList(@Param("targetSettingOrder") TargetSettingOrder targetSettingOrder);

    /**
     * 新增目标制定订单表
     *
     * @param targetSettingOrder 目标制定订单表
     * @return 结果
     */
    int insertTargetSettingOrder(@Param("targetSettingOrder") TargetSettingOrder targetSettingOrder);

    /**
     * 修改目标制定订单表
     *
     * @param targetSettingOrder 目标制定订单表
     * @return 结果
     */
    int updateTargetSettingOrder(@Param("targetSettingOrder") TargetSettingOrder targetSettingOrder);

    /**
     * 批量修改目标制定订单表
     *
     * @param targetSettingOrderList 目标制定订单表
     * @return 结果
     */
    int updateTargetSettingOrders(@Param("targetSettingOrderList") List<TargetSettingOrder> targetSettingOrderList);

    /**
     * 逻辑删除目标制定订单表
     *
     * @param targetSettingOrder
     * @return 结果
     */
    int logicDeleteTargetSettingOrderByTargetSettingOrderId(@Param("targetSettingOrder") TargetSettingOrder targetSettingOrder);

    /**
     * 逻辑批量删除目标制定订单表
     *
     * @param targetSettingOrderIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteTargetSettingOrderByTargetSettingOrderIds(@Param("targetSettingOrderIds") List<Long> targetSettingOrderIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除目标制定订单表
     *
     * @param targetSettingOrderId 目标制定订单表主键
     * @return 结果
     */
    int deleteTargetSettingOrderByTargetSettingOrderId(@Param("targetSettingOrderId") Long targetSettingOrderId);

    /**
     * 物理批量删除目标制定订单表
     *
     * @param targetSettingOrderIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteTargetSettingOrderByTargetSettingOrderIds(@Param("targetSettingOrderIds") List<Long> targetSettingOrderIds);

    /**
     * 批量新增目标制定订单表
     *
     * @param TargetSettingOrders 目标制定订单表列表
     * @return 结果
     */
    int batchTargetSettingOrder(@Param("targetSettingOrders") List<TargetSettingOrder> TargetSettingOrders);
}
