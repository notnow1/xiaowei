package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingOrder;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingOrderExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingOrderMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;


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
     * 获取订单目标制定
     *
     * @param historyNumS     历史年份
     * @param targetSettingId 目标制定ID
     * @return
     */
    @Override
    public List<TargetSettingOrderDTO> selectTargetSettingOrderList(Long targetSettingId, List<Integer> historyNumS) {
        return targetSettingOrderMapper.selectTargetSettingOrderListByTargetSettingId(targetSettingId, historyNumS);
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
     * 获取全部的订单目标制定列表
     *
     * @return
     */
    @Override
    public List<TargetSettingOrderDTO> selectTargetSettingOrderByTargetSettingId(Long targetSettingId) {
        return targetSettingOrderMapper.selectTargetSettingOrderByTargetSettingId(targetSettingId);
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
     * @param targetSettingOrderDtoS 需要删除的目标制定订单表主键
     * @return 结果
     */

    @Override
    public int deleteTargetSettingOrderByTargetSettingOrderIds(List<TargetSettingOrderDTO> targetSettingOrderDtoS) {
        List<Long> stringList = new ArrayList<>();
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDtoS) {
            stringList.add(targetSettingOrderDTO.getTargetSettingOrderId());
        }
        return targetSettingOrderMapper.deleteTargetSettingOrderByTargetSettingOrderIds(stringList);
    }

    /**
     * 批量新增目标制定订单表信息
     *
     * @param targetSettingOrderDtoS 目标制定订单表对象
     */

    public int insertTargetSettingOrders(List<TargetSettingOrderDTO> targetSettingOrderDtoS, TargetSettingDTO targetSettingDTO) {
        List<TargetSettingOrder> targetSettingOrderList = new ArrayList<>();
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDtoS) {
            TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
            BeanUtils.copyProperties(targetSettingOrderDTO, targetSettingOrder);
            targetSettingOrder.setCreateBy(SecurityUtils.getUserId());
            targetSettingOrder.setCreateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateBy(SecurityUtils.getUserId());
            if (StringUtils.isNull(targetSettingOrder.getTargetSettingId())) {
                targetSettingOrder.setTargetSettingId(targetSettingDTO.getTargetSettingId());
            }
            targetSettingOrder.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingOrderList.add(targetSettingOrder);
        }
        return targetSettingOrderMapper.batchTargetSettingOrder(targetSettingOrderList);
    }

    /**
     * 批量修改目标制定订单表信息
     *
     * @param targetSettingOrderDtoS 目标制定订单表对象
     */

    public int updateTargetSettingOrders(List<TargetSettingOrderDTO> targetSettingOrderDtoS, TargetSettingDTO targetSetting) {
        List<TargetSettingOrder> targetSettingOrderList = new ArrayList<>();
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDtoS) {
            TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
            BeanUtils.copyProperties(targetSettingOrderDTO, targetSettingOrder);
            targetSettingOrder.setTargetSettingId(targetSetting.getTargetSettingId());
            targetSettingOrder.setCreateBy(SecurityUtils.getUserId());
            targetSettingOrder.setCreateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateBy(SecurityUtils.getUserId());
            targetSettingOrder.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingOrderList.add(targetSettingOrder);
        }
        return targetSettingOrderMapper.updateTargetSettingOrders(targetSettingOrderList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetSettingOrder(List<TargetSettingOrderExcel> list) {
        List<TargetSettingOrder> targetSettingOrderList = new ArrayList<>();
        list.forEach(l -> {
            TargetSettingOrder targetSettingOrder = new TargetSettingOrder();
            BeanUtils.copyProperties(l, targetSettingOrder);
            targetSettingOrder.setCreateBy(SecurityUtils.getUserId());
            targetSettingOrder.setCreateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateTime(DateUtils.getNowDate());
            targetSettingOrder.setUpdateBy(SecurityUtils.getUserId());
            targetSettingOrder.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingOrderList.add(targetSettingOrder);
        });
        try {
            targetSettingOrderMapper.batchTargetSettingOrder(targetSettingOrderList);
        } catch (Exception e) {
            throw new ServiceException("导入目标制定订单表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param startYear
     * @param endYear
     * @param targetSettingId
     * @param percentage
     * @return
     */
    @Override
    public List<TargetSettingOrderExcel> exportTargetSettingOrder(Integer startYear, Integer endYear, Long targetSettingId, BigDecimal percentage) {
        
        List<TargetSettingOrderExcel> targetSettingOrderExcelList = new ArrayList<>();
        return targetSettingOrderExcelList;
    }

}