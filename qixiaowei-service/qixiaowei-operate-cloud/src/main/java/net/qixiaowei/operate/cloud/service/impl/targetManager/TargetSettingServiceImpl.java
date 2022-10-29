package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingOrderService;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingService;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


/**
 * TargetSettingService业务层处理
 *
 * @author Graves
 * @since 2022-10-27
 */
@Service
public class TargetSettingServiceImpl implements ITargetSettingService {
    @Autowired
    private TargetSettingMapper targetSettingMapper;

    @Autowired
    private ITargetSettingOrderService targetSettingOrderService;

    @Autowired
    private RemoteIndicatorService indicatorService;

    /**
     * 查询目标制定
     *
     * @param targetSettingId 目标制定主键
     * @return 目标制定
     */
    @Override
    public TargetSettingDTO selectTargetSettingByTargetSettingId(Long targetSettingId) {
        return targetSettingMapper.selectTargetSettingByTargetSettingId(targetSettingId);
    }

    /**
     * 查询目标制定列表
     *
     * @param targetSettingDTO 目标制定
     * @return 目标制定
     */
    @Override
    public List<TargetSettingDTO> selectTargetSettingList(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingList(targetSetting);
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            return targetSettingDTOS;
        }
        for (TargetSettingDTO settingDTO : targetSettingDTOS) {
            Long indicatorId = settingDTO.getIndicatorId();
        }
        return null;
    }

    /**
     * 新增目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */
    @Override
    public TargetSetting insertTargetSetting(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setCreateBy(SecurityUtils.getUserId());
        targetSetting.setCreateTime(DateUtils.getNowDate());
        targetSetting.setUpdateTime(DateUtils.getNowDate());
        targetSetting.setUpdateBy(SecurityUtils.getUserId());
        targetSetting.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetSettingMapper.insertTargetSetting(targetSetting);
        return targetSetting;
    }

    /**
     * 修改目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */
    @Override
    public int updateTargetSetting(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setUpdateTime(DateUtils.getNowDate());
        targetSetting.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingMapper.updateTargetSetting(targetSetting);
    }

    /**
     * 逻辑批量删除目标制定
     *
     * @param targetSettingIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingByTargetSettingIds(List<Long> targetSettingIds) {
        return targetSettingMapper.logicDeleteTargetSettingByTargetSettingIds(targetSettingIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标制定信息
     *
     * @param targetSettingId 目标制定主键
     * @return 结果
     */
    @Override
    public int deleteTargetSettingByTargetSettingId(Long targetSettingId) {
        return targetSettingMapper.deleteTargetSettingByTargetSettingId(targetSettingId);
    }

    /**
     * 逻辑删除目标制定信息
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingByTargetSettingId(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        targetSetting.setTargetSettingId(targetSettingDTO.getTargetSettingId());
        targetSetting.setUpdateTime(DateUtils.getNowDate());
        targetSetting.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingMapper.logicDeleteTargetSettingByTargetSettingId(targetSetting);
    }

    /**
     * 物理删除目标制定信息
     *
     * @param targetSettingDTO 目标制定
     * @return 结果
     */

    @Override
    public int deleteTargetSettingByTargetSettingId(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        return targetSettingMapper.deleteTargetSettingByTargetSettingId(targetSetting.getTargetSettingId());
    }

    /**
     * 物理批量删除目标制定
     *
     * @param targetSettingDtos 需要删除的目标制定主键
     * @return 结果
     */

    @Override
    public int deleteTargetSettingByTargetSettingIds(List<TargetSettingDTO> targetSettingDtos) {
        List<Long> stringList = new ArrayList<>();
        for (TargetSettingDTO targetSettingDTO : targetSettingDtos) {
            stringList.add(targetSettingDTO.getTargetSettingId());
        }
        return targetSettingMapper.deleteTargetSettingByTargetSettingIds(stringList);
    }

    /**
     * 批量新增目标制定信息
     *
     * @param targetSettingDtos 目标制定对象
     */

    public int insertTargetSettings(List<TargetSettingDTO> targetSettingDtos) {
        List<TargetSetting> targetSettingList = new ArrayList<>();

        for (TargetSettingDTO targetSettingDTO : targetSettingDtos) {
            TargetSetting targetSetting = new TargetSetting();
            BeanUtils.copyProperties(targetSettingDTO, targetSetting);
            targetSetting.setCreateBy(SecurityUtils.getUserId());
            targetSetting.setCreateTime(DateUtils.getNowDate());
            targetSetting.setUpdateTime(DateUtils.getNowDate());
            targetSetting.setUpdateBy(SecurityUtils.getUserId());
            targetSetting.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingList.add(targetSetting);
        }
        return targetSettingMapper.batchTargetSetting(targetSettingList);
    }

    /**
     * 批量修改目标制定信息
     *
     * @param targetSettingDtos 目标制定对象
     */

    public int updateTargetSettings(List<TargetSettingDTO> targetSettingDtos) {
        List<TargetSetting> targetSettingList = new ArrayList<>();

        for (TargetSettingDTO targetSettingDTO : targetSettingDtos) {
            TargetSetting targetSetting = new TargetSetting();
            BeanUtils.copyProperties(targetSettingDTO, targetSetting);
            targetSetting.setCreateBy(SecurityUtils.getUserId());
            targetSetting.setCreateTime(DateUtils.getNowDate());
            targetSetting.setUpdateTime(DateUtils.getNowDate());
            targetSetting.setUpdateBy(SecurityUtils.getUserId());
            targetSettingList.add(targetSetting);
        }
        return targetSettingMapper.updateTargetSettings(targetSettingList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetSetting(List<TargetSettingExcel> list) {
        List<TargetSetting> targetSettingList = new ArrayList<>();
        list.forEach(l -> {
            TargetSetting targetSetting = new TargetSetting();
            BeanUtils.copyProperties(l, targetSetting);
            targetSetting.setCreateBy(SecurityUtils.getUserId());
            targetSetting.setCreateTime(DateUtils.getNowDate());
            targetSetting.setUpdateTime(DateUtils.getNowDate());
            targetSetting.setUpdateBy(SecurityUtils.getUserId());
            targetSetting.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingList.add(targetSetting);
        });
        try {
            targetSettingMapper.batchTargetSetting(targetSettingList);
        } catch (Exception e) {
            throw new ServiceException("导入目标制定失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param targetSettingDTO 目标制定列表
     * @return
     */
    @Override
    public List<TargetSettingExcel> exportTargetSetting(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingList(targetSetting);
        return new ArrayList<>();
    }

    /**
     * 保存销售订单目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    @Override
    public TargetSettingDTO saveOrderTargetSetting(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        Long indicatorId = getIndicator(IndicatorCode.ORDER.getCode()).getIndicatorId();
        List<TargetSettingOrderDTO> targetSettingOrderAfter = targetSettingDTO.getTargetSettingOrderDTOS();
        TargetSettingDTO targetSetting = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, indicatorId);
        targetSettingDTO.setTargetSettingType(1);
        targetSettingDTO.setIndicatorId(indicatorId);
        targetSettingDTO.setSort(0);
        if (StringUtils.isNull(targetSetting)) {//新增
            TargetSetting setting = insertTargetSetting(targetSettingDTO);
            targetSetting = new TargetSettingDTO();
            targetSetting.setTargetSettingId(setting.getTargetSettingId());
            operate(targetSettingOrderAfter, targetSetting);
            return targetSettingDTO;
        }
        // 保存
        Long targetSettingId = targetSetting.getTargetSettingId();
        updateTargetSetting(targetSettingDTO);
        if (StringUtils.isEmpty(targetSettingOrderAfter)) {
            return targetSettingDTO;
        }
        operate(targetSettingOrderAfter, targetSetting);
        targetSettingDTO.setTargetSettingId(targetSettingId);
        return targetSettingDTO;
    }

    /**
     * 销售订单目标制定竖表编辑
     *
     * @param targetSettingOrderAfter 订单目标制定后增
     * @param targetSetting           目标制定
     */
    private void operate(List<TargetSettingOrderDTO> targetSettingOrderAfter, TargetSettingDTO targetSetting) {
        List<TargetSettingOrderDTO> targetSettingOrderBefore = targetSettingOrderService.selectTargetSettingOrderLists();
        // 交集  更新
        List<TargetSettingOrderDTO> updateTargetSettingOrder =
                targetSettingOrderAfter.stream().filter(targetSettingOrderDTO ->
                        targetSettingOrderBefore.stream().map(TargetSettingOrderDTO::getHistoryYear)
                                .collect(Collectors.toList()).contains(targetSettingOrderDTO.getHistoryYear())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(updateTargetSettingOrder)) {
            targetSettingOrderService.updateTargetSettingOrders(updateTargetSettingOrder, targetSetting);
        }
        // 差集 After中Before的补集
        List<TargetSettingOrderDTO> addTargetSettingOrder =
                targetSettingOrderAfter.stream().filter(targetSettingOrderDTO ->
                        !targetSettingOrderBefore.stream().map(TargetSettingOrderDTO::getHistoryYear)
                                .collect(Collectors.toList()).contains(targetSettingOrderDTO.getHistoryYear())
                ).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(addTargetSettingOrder)) {
            targetSettingOrderService.insertTargetSettingOrders(addTargetSettingOrder, targetSetting);
        }
    }

    /**
     * todo 查询销售订单目标制定
     *
     * @param targetSettingDTO 目标定制
     * @return
     */
    @Override
    public TargetSettingDTO selectOrderTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        Integer historyNum = targetSettingDTO.getHistoryNum();
        IndicatorDTO indicatorDTO = getIndicator(IndicatorCode.ORDER.getCode());
        List<Integer> historyNumS = getHistoryYearList(targetYear, historyNum);
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setTargetSettingType(1);
        targetSetting.setTargetYear(targetYear);
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingList(targetSetting);
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            TargetSettingDTO settingDTO = new TargetSettingDTO();
            // todo 通过目标年度直接去找滚动越策管理,获取历史年度实际值
            List<TargetSettingOrderDTO> maps = selectRollForecastManagementByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
            if (StringUtils.isNotEmpty(maps)) {
                return settingDTO.setTargetSettingOrderDTOS(maps);
            }
            return settingDTO;
        }
        TargetSettingDTO settingDTO = targetSettingDTOS.get(0);
        Long targetSettingId = settingDTO.getTargetSettingId();
        List<TargetSettingOrderDTO> targetSettingOrderDTOS = targetSettingOrderService.selectTargetSettingOrderList(historyNumS);
        if (StringUtils.isEmpty(targetSettingOrderDTOS)) {
            // todo 通过目标年度直接去找滚动越策管理,获取历史年度实际值
            List<TargetSettingOrderDTO> maps = selectRollForecastManagementByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
            if (StringUtils.isNotEmpty(maps)) {
                return settingDTO.setTargetSettingOrderDTOS(maps);
            }
            return settingDTO;
        }
        if (targetSettingOrderDTOS.size() == historyNum) {
            settingDTO.setTargetSettingOrderDTOS(targetSettingOrderDTOS);
            return settingDTO;
        }
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDTOS) {
            historyNumS.remove(targetSettingOrderDTO.getHistoryYear());
        }
        List<TargetSettingOrderDTO> settingOrderDTOS = selectRollForecastManagementByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
        if (StringUtils.isNotNull(settingOrderDTOS)) {
            targetSettingOrderDTOS.addAll(settingOrderDTOS);
            for (TargetSettingOrderDTO settingOrderDTO : settingOrderDTOS) {
                historyNumS.remove(settingOrderDTO.getHistoryYear());
            }
            if (StringUtils.isNotEmpty(historyNumS)) {
                insertNewRow(historyNumS, targetSettingOrderDTOS);
            }
        } else {
            insertNewRow(historyNumS, targetSettingOrderDTOS);
        }
        settingDTO.setTargetSettingOrderDTOS(targetSettingOrderDTOS);
        return settingDTO;
    }

    /**
     * 查询销售订单目标制定-不带主表玩
     *
     * @param targetSettingDTO 目标制定DTO
     * @return
     */
    @Override
    public List<TargetSettingOrderDTO> selectOrderDropTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        Integer historyNum = targetSettingDTO.getHistoryNum();
        IndicatorDTO indicatorDTO = getIndicator(IndicatorCode.ORDER.getCode());
        List<Integer> historyNumS = getHistoryYearList(targetYear, historyNum);
        List<TargetSettingOrderDTO> targetSettingOrderDTOS = targetSettingOrderService.selectDropTargetSettingOrderList(historyNumS);
        if (StringUtils.isEmpty(targetSettingOrderDTOS)) {
            // todo 通过目标年度直接去找滚动越策管理,获取历史年度实际值
            return selectRollForecastManagementByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
        }
        if (targetSettingOrderDTOS.size() == historyNum) {
            return targetSettingOrderDTOS;
        }
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDTOS) {
            historyNumS.remove(targetSettingOrderDTO.getHistoryYear());
        }
        List<TargetSettingOrderDTO> settingOrderDTOS = selectRollForecastManagementByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
        if (StringUtils.isNotNull(settingOrderDTOS)) {
            targetSettingOrderDTOS.addAll(settingOrderDTOS);
            for (TargetSettingOrderDTO settingOrderDTO : settingOrderDTOS) {
                historyNumS.remove(settingOrderDTO.getHistoryYear());
            }
            if (StringUtils.isNotEmpty(historyNumS)) {
                insertNewRow(historyNumS, targetSettingOrderDTOS);
            } else {
                insertNewRow(historyNumS, targetSettingOrderDTOS);
            }
        }
        return targetSettingOrderDTOS;
    }

    /**
     * todo 在滚动预测管理找到order分表的值，若没有则返回空
     *
     * @param historyNumS 历史年度list
     * @param indicatorId 指标ID
     * @return
     */
    private List<TargetSettingOrderDTO> selectRollForecastManagementByTargetYear(List<Integer> historyNumS, Long indicatorId) {
        return null;
    }

    /**
     * 获取指标
     */
    private IndicatorDTO getIndicator(String code) {
        R<IndicatorDTO> indicatorDTOR = indicatorService.selectIndicatorByCode(code);
        if (indicatorDTOR.getCode() != 200) {
            throw new ServiceException("远程获取指标信息失败");
        }
        if (StringUtils.isNull(indicatorDTOR.getData())) {
            throw new ServiceException("当前销售订单指标无法获取");
        }
        //指标ID
        return indicatorDTOR.getData();
    }

    /**
     * 获取历史年份列表
     *
     * @param targetYear 目标年度
     * @param historyNum 历史年份数
     * @return
     */
    private static List<Integer> getHistoryYearList(Integer targetYear, Integer historyNum) {
        List<Integer> historyNumS = new ArrayList<>();
        for (int i = 1; i <= historyNum; i++) {
            historyNumS.add(targetYear - i);
        }
        return historyNumS;
    }

    /**
     * 给没有数据的年份创建新的对象
     *
     * @param historyNumS            给没有数据的历史年份
     * @param targetSettingOrderDTOS 目标订单列表
     */
    private static void insertNewRow(List<Integer> historyNumS, List<TargetSettingOrderDTO> targetSettingOrderDTOS) {
        for (Integer history : historyNumS) {
            TargetSettingOrderDTO targetSettingOrderDTO = new TargetSettingOrderDTO();
            targetSettingOrderDTO.setHistoryYear(history);
            targetSettingOrderDTOS.add(targetSettingOrderDTO);
        }
    }
}

