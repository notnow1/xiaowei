package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingOrder;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingOrderMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingOrderService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
 * TargetSettingOrderService业务层处理
 *
 * @author Graves
 * @since 2022-10-27
 */
@Service
public class TargetSettingOrderServiceImpl implements ITargetSettingOrderService {
    @Autowired
    private TargetSettingOrderMapper targetSettingOrderMapper;

    /**
     * 查询目标制定订单表
     *
     * @param targetSettingOrderId 目标制定订单表主键
     * @return 目标制定订单表
     */
    @Override
    public TargetSettingOrderDTO selectTargetSettingOrderByTargetSettingOrderId(Long targetSettingOrderId) {
        return targetSettingOrderMapper.selectTargetSettingOrderByTargetSettingOrderId(targetSettingOrderId);
    }

    /**
     * 查询目标制定订单表列表
     *
     * @param targetSettingOrderDTO 目标制定订单表
     * @return 目标制定订单表
     */
    @Override
    public List<TargetSettingOrderDTO> selectTargetSettingOrderList(TargetSettingOrderDTO targetSettingOrderDTO) {
        TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
        BeanUtils.copyProperties(targetSettingOrderDTO, targetSettingOrder);
        return targetSettingOrderMapper.selectTargetSettingOrderList(targetSettingOrder);
    }

    @Override
    public List<TargetSettingOrderDTO> selectTargetSettingOrderListByHistoryYears(List<Integer> historyNumS) {
        return null;
    }

    /**
     * 新增目标制定订单表
     *
     * @param targetSettingOrderDTO 目标制定订单表
     * @return 结果
     */
    @Override
    public TargetSettingOrderDTO insertTargetSettingOrder(TargetSettingOrderDTO targetSettingOrderDTO) {
        TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
        BeanUtils.copyProperties(targetSettingOrderDTO, targetSettingOrder);
        targetSettingOrder.setCreateBy(SecurityUtils.getUserId());
        targetSettingOrder.setCreateTime(DateUtils.getNowDate());
        targetSettingOrder.setUpdateTime(DateUtils.getNowDate());
        targetSettingOrder.setUpdateBy(SecurityUtils.getUserId());
        targetSettingOrder.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetSettingOrderMapper.insertTargetSettingOrder(targetSettingOrder);
        targetSettingOrderDTO.setTargetSettingOrderId(targetSettingOrder.getTargetSettingOrderId());
        return targetSettingOrderDTO;
    }

    /**
     * 修改目标制定订单表
     *
     * @param targetSettingOrderDTO 目标制定订单表
     * @return 结果
     */
    @Override
    public int updateTargetSettingOrder(TargetSettingOrderDTO targetSettingOrderDTO) {
        TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
        BeanUtils.copyProperties(targetSettingOrderDTO, targetSettingOrder);
        targetSettingOrder.setUpdateTime(DateUtils.getNowDate());
        targetSettingOrder.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingOrderMapper.updateTargetSettingOrder(targetSettingOrder);
    }

    /**
     * 逻辑批量删除目标制定订单表
     *
     * @param targetSettingOrderIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingOrderByTargetSettingOrderIds(List<Long> targetSettingOrderIds) {
        return targetSettingOrderMapper.logicDeleteTargetSettingOrderByTargetSettingOrderIds(targetSettingOrderIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标制定订单表信息
     *
     * @param targetSettingOrderId 目标制定订单表主键
     * @return 结果
     */
    @Override
    public int deleteTargetSettingOrderByTargetSettingOrderId(Long targetSettingOrderId) {
        return targetSettingOrderMapper.deleteTargetSettingOrderByTargetSettingOrderId(targetSettingOrderId);
    }

    /**
     * 逻辑删除目标制定订单表信息
     *
     * @param targetSettingOrderDTO 目标制定订单表
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingOrderByTargetSettingOrderId(TargetSettingOrderDTO targetSettingOrderDTO) {
        TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
        targetSettingOrder.setTargetSettingOrderId(targetSettingOrderDTO.getTargetSettingOrderId());
        targetSettingOrder.setUpdateTime(DateUtils.getNowDate());
        targetSettingOrder.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingOrderMapper.logicDeleteTargetSettingOrderByTargetSettingOrderId(targetSettingOrder);
    }

    /**
     * 物理删除目标制定订单表信息
     *
     * @param targetSettingOrderDTO 目标制定订单表
     * @return 结果
     */

    @Override
    public int deleteTargetSettingOrderByTargetSettingOrderId(TargetSettingOrderDTO targetSettingOrderDTO) {
        TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
        BeanUtils.copyProperties(targetSettingOrderDTO, targetSettingOrder);
        return targetSettingOrderMapper.deleteTargetSettingOrderByTargetSettingOrderId(targetSettingOrder.getTargetSettingOrderId());
    }

    /**
     * 物理批量删除目标制定订单表
     *
     * @param targetSettingOrderDtos 需要删除的目标制定订单表主键
     * @return 结果
     */

    @Override
    public int deleteTargetSettingOrderByTargetSettingOrderIds(List<TargetSettingOrderDTO> targetSettingOrderDtos) {
        List<Long> stringList = new ArrayList();
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDtos) {
            stringList.add(targetSettingOrderDTO.getTargetSettingOrderId());
        }
        return targetSettingOrderMapper.deleteTargetSettingOrderByTargetSettingOrderIds(stringList);
    }

    /**
     * 批量新增目标制定订单表信息
     *
     * @param targetSettingOrderDtos 目标制定订单表对象
     */

    public int insertTargetSettingOrders(List<TargetSettingOrderDTO> targetSettingOrderDtos) {
        List<TargetSettingOrder> targetSettingOrderList = new ArrayList();

        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDtos) {
            TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
            BeanUtils.copyProperties(targetSettingOrderDTO, targetSettingOrder);
            targetSettingOrder.setCreateBy(SecurityUtils.getUserId());
            targetSettingOrder.setCreateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateBy(SecurityUtils.getUserId());
            targetSettingOrder.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingOrderList.add(targetSettingOrder);
        }
        return targetSettingOrderMapper.batchTargetSettingOrder(targetSettingOrderList);
    }

    /**
     * 批量修改目标制定订单表信息
     *
     * @param targetSettingOrderDtos 目标制定订单表对象
     */

    public int updateTargetSettingOrders(List<TargetSettingOrderDTO> targetSettingOrderDtos) {
        List<TargetSettingOrder> targetSettingOrderList = new ArrayList();

        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDtos) {
            TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
            BeanUtils.copyProperties(targetSettingOrderDTO, targetSettingOrder);
            targetSettingOrder.setCreateBy(SecurityUtils.getUserId());
            targetSettingOrder.setCreateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateBy(SecurityUtils.getUserId());
            targetSettingOrderList.add(targetSettingOrder);
        }
        return targetSettingOrderMapper.updateTargetSettingOrders(targetSettingOrderList);
    }

}