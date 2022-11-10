package net.qixiaowei.operate.cloud.service.targetManager;

import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingOrderExcel;

import java.math.BigDecimal;
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
     * 获取订单目标制定
     *
     * @param historyNumS 历史年份
     * @return
     */
    public List<TargetSettingOrderDTO> selectTargetSettingOrderList(Long TargetSettingId, List<Integer> historyNumS);

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
     * @param targetSettingOrderDtoS 目标制定订单表
     * @return 结果
     */
    int updateTargetSettingOrders(List<TargetSettingOrderDTO> targetSettingOrderDtoS, TargetSettingDTO targetSetting);

    /**
     * 批量新增目标制定订单表
     *
     * @param targetSettingOrderDtoS 目标制定订单表
     * @return 结果
     */
    int insertTargetSettingOrders(List<TargetSettingOrderDTO> targetSettingOrderDtoS, TargetSettingDTO targetSetting);

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

    /**
     * 获取全部的订单目标制定列表
     *
     * @return
     */
    List<TargetSettingOrderDTO> selectTargetSettingOrderByTargetSettingId(Long targetSettingId);

    /**
     * 导入Excel
     *
     * @param list
     */
    void importTargetSettingOrder(List<TargetSettingOrderExcel> list);

    /**
     * 导出Excel
     *
     * @param startYear
     * @param endYear
     * @param targetSettingId
     * @param percentage
     * @return
     */
    List<TargetSettingOrderExcel> exportTargetSettingOrder(Integer startYear, Integer endYear, Long targetSettingId, BigDecimal percentage);
}
