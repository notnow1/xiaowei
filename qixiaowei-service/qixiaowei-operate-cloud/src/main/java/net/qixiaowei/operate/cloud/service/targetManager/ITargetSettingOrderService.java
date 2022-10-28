package net.qixiaowei.operate.cloud.service.targetManager;

import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;

import java.util.List;


/**
 * TargetSettingOrderService接口
 *
 * @author Graves
 * @since 2022-10-27
 */
public interface ITargetSettingOrderService {
    /**
     * 查询目标制定订单表
     *
     * @param targetSettingOrderId 目标制定订单表主键
     * @return 目标制定订单表
     */
    TargetSettingOrderDTO selectTargetSettingOrderByTargetSettingOrderId(Long targetSettingOrderId);

    /**
     * 查询目标制定订单表列表
     *
     * @param targetSettingOrderDTO 目标制定订单表
     * @return 目标制定订单表集合
     */
    List<TargetSettingOrderDTO> selectTargetSettingOrderList(TargetSettingOrderDTO targetSettingOrderDTO);

    /**
     * 查询销售订单目标制定列表
     *
     * @param historyNumS
     * @return
     */
    List<TargetSettingOrderDTO> selectTargetSettingOrderListByHistoryYears(List<Integer> historyNumS);

    /**
     * 新增目标制定订单表
     *
     * @param targetSettingOrderDTO 目标制定订单表
     * @return 结果
     */
    TargetSettingOrderDTO insertTargetSettingOrder(TargetSettingOrderDTO targetSettingOrderDTO);

    /**
     * 修改目标制定订单表
     *
     * @param targetSettingOrderDTO 目标制定订单表
     * @return 结果
     */
    int updateTargetSettingOrder(TargetSettingOrderDTO targetSettingOrderDTO);

    /**
     * 批量修改目标制定订单表
     *
     * @param targetSettingOrderDtos 目标制定订单表
     * @return 结果
     */
    int updateTargetSettingOrders(List<TargetSettingOrderDTO> targetSettingOrderDtos);

    /**
     * 批量新增目标制定订单表
     *
     * @param targetSettingOrderDtos 目标制定订单表
     * @return 结果
     */
    int insertTargetSettingOrders(List<TargetSettingOrderDTO> targetSettingOrderDtos);

    /**
     * 逻辑批量删除目标制定订单表
     *
     * @param targetSettingOrderIds 需要删除的目标制定订单表集合
     * @return 结果
     */
    int logicDeleteTargetSettingOrderByTargetSettingOrderIds(List<Long> targetSettingOrderIds);

    /**
     * 逻辑删除目标制定订单表信息
     *
     * @param targetSettingOrderDTO
     * @return 结果
     */
    int logicDeleteTargetSettingOrderByTargetSettingOrderId(TargetSettingOrderDTO targetSettingOrderDTO);

    /**
     * 批量删除目标制定订单表
     *
     * @param TargetSettingOrderDtos
     * @return 结果
     */
    int deleteTargetSettingOrderByTargetSettingOrderIds(List<TargetSettingOrderDTO> TargetSettingOrderDtos);

    /**
     * 逻辑删除目标制定订单表信息
     *
     * @param targetSettingOrderDTO
     * @return 结果
     */
    int deleteTargetSettingOrderByTargetSettingOrderId(TargetSettingOrderDTO targetSettingOrderDTO);


    /**
     * 删除目标制定订单表信息
     *
     * @param targetSettingOrderId 目标制定订单表主键
     * @return 结果
     */
    int deleteTargetSettingOrderByTargetSettingOrderId(Long targetSettingOrderId);
    
}
