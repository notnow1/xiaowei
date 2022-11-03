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
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.api.vo.TargetSettingIncomeVO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingMapper;
import net.qixiaowei.operate.cloud.service.targetManager.*;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
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
    private ITargetSettingIncomeService targetSettingIncomeService;

    @Autowired
    private ITargetSettingRecoveryService targetSettingRecoveryService;

    @Autowired
    private ITargetSettingRecoveriesService targetSettingRecoveriesServices;

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
    @Transactional
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
            return targetSetting;
        }
        Long targetSettingId = targetSetting.getTargetSettingId();
        targetSettingDTO.setTargetSettingId(targetSettingId);
        // 保存
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
        List<TargetSettingOrderDTO> targetSettingOrderBefore = targetSettingOrderService.selectTargetSettingOrderByTargetSettingId(targetSetting.getTargetSettingId());
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
     * @return settingDTO
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
            List<TargetSettingOrderDTO> targetSettingOrderDTOS = new ArrayList<>();
            insertOrderRow(historyNumS, targetSettingOrderDTOS);
            targetSettingDTO.setTargetSettingOrderDTOS(targetSettingOrderDTOS);
            return targetSettingDTO;
        }
        TargetSettingDTO settingDTO = targetSettingDTOS.get(0);
        List<TargetSettingOrderDTO> targetSettingOrderDTOS = targetSettingOrderService.selectTargetSettingOrderList(settingDTO.getTargetSettingId(), historyNumS);
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            return getTargetSettingDTO(historyNum, indicatorDTO, historyNumS, targetSettingOrderDTOS, targetSettingDTO);
        }

        Long targetSettingId = settingDTO.getTargetSettingId();
        return getTargetSettingDTO(historyNum, indicatorDTO, historyNumS, targetSettingOrderDTOS, settingDTO);
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
        List<TargetSettingOrderDTO> targetSettingOrderDTOS = targetSettingOrderService.selectTargetSettingOrderList(targetSettingDTO.getTargetSettingId(), historyNumS);
        if (StringUtils.isEmpty(targetSettingOrderDTOS)) {
            insertOrderRow(historyNumS, targetSettingOrderDTOS);
            return targetSettingOrderDTOS;
        }
        if (targetSettingOrderDTOS.size() == historyNum) {
            historyNumS = new ArrayList<>();
            getDropTargetSettingDTO(indicatorDTO, historyNumS, targetSettingOrderDTOS);
            return targetSettingOrderDTOS;
        }
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDTOS) {
            historyNumS.remove(targetSettingOrderDTO.getHistoryYear());
        }
        getDropTargetSettingDTO(indicatorDTO, historyNumS, targetSettingOrderDTOS);
        return targetSettingOrderDTOS;
    }

    /**
     * 处理销售订单列表
     *
     * @param historyNum             历史年份
     * @param indicatorDTO           指标dto
     * @param historyNumS            历史年份list
     * @param targetSettingOrderDTOS 销售订单目标制定列表
     * @param settingDTO             返回目标制定
     * @return 销售订单列表
     */
    private TargetSettingDTO getTargetSettingDTO(Integer historyNum, IndicatorDTO indicatorDTO, List<Integer> historyNumS, List<TargetSettingOrderDTO> targetSettingOrderDTOS, TargetSettingDTO settingDTO) {
        Integer targetYear = settingDTO.getTargetYear();
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
                insertOrderRow(historyNumS, targetSettingOrderDTOS);
            }
        } else {
            insertOrderRow(historyNumS, targetSettingOrderDTOS);
        }
        settingDTO.setTargetSettingOrderDTOS(targetSettingOrderDTOS);
        return settingDTO;
    }

    /**
     * 处理销售订单列表
     *
     * @param indicatorDTO           指标dto
     * @param historyNumS            历史年份list
     * @param targetSettingOrderDTOS 销售订单目标制定列表
     */
    private void getDropTargetSettingDTO(IndicatorDTO indicatorDTO, List<Integer> historyNumS, List<TargetSettingOrderDTO> targetSettingOrderDTOS) {
        // todo 通过目标年度直接去找滚动越策管理,获取历史年度实际值
        List<TargetSettingOrderDTO> settingOrderDTOS = null;
        if (StringUtils.isNotEmpty(historyNumS)) {
            settingOrderDTOS = selectRollForecastManagementByTargetYear(historyNumS, indicatorDTO.getIndicatorId());
        }
        if (settingOrderDTOS != null) {
            targetSettingOrderDTOS.addAll(settingOrderDTOS);
            for (TargetSettingOrderDTO settingOrderDTO : settingOrderDTOS) {
                historyNumS.remove(settingOrderDTO.getHistoryYear());
            }
        }
        if (StringUtils.isNotEmpty(historyNumS)) {
            insertOrderRow(historyNumS, targetSettingOrderDTOS);
        }
        // 排序，然后赋值年度长率
        targetSettingOrderDTOS.sort((TargetSettingOrderDTO o1, TargetSettingOrderDTO o2) -> {
            return o2.getHistoryYear() - o1.getHistoryYear();
        });
        BigDecimal beforeRate = new BigDecimal(0);
        for (TargetSettingOrderDTO targetSettingOrderDTO : targetSettingOrderDTOS) {
            BigDecimal historyActual = targetSettingOrderDTO.getHistoryActual();
            if (StringUtils.isNotNull(historyActual)) {
                BigDecimal subtract = historyActual.subtract(beforeRate);
                BigDecimal divide;
                if (!historyActual.equals(BigDecimal.ZERO)) {
                    divide = subtract.divide(historyActual, 2, RoundingMode.HALF_UP);
                } else {
                    divide = BigDecimal.ZERO;
                }
                targetSettingOrderDTO.setGrowthRate(divide);
                beforeRate = targetSettingOrderDTO.getHistoryActual();
            } else {
                beforeRate = new BigDecimal(0);
            }
            targetSettingOrderDTO.setGrowthRate(beforeRate);
        }
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
    private static void insertOrderRow(List<Integer> historyNumS, List<TargetSettingOrderDTO> targetSettingOrderDTOS) {
        BigDecimal zero = new BigDecimal(0);
        for (Integer history : historyNumS) {
            TargetSettingOrderDTO targetSettingOrderDTO = new TargetSettingOrderDTO();
            targetSettingOrderDTO.setHistoryYear(history);
            targetSettingOrderDTO.setHistoryActual(zero);
            targetSettingOrderDTOS.add(targetSettingOrderDTO);
        }
    }

    /**
     * 保存销售收入目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    @Override
    @Transactional
    public TargetSettingDTO saveIncomeTargetSetting(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        Long indicatorId = getIndicator(IndicatorCode.INCOME.getCode()).getIndicatorId();
        List<Integer> historyNumS = getHistoryYearList(targetYear, 3);
        List<TargetSettingIncomeVO> targetSettingIncomeVOS = targetSettingDTO.getTargetSettingIncomeVOS();
        TargetSettingDTO targetSetting = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, indicatorId);
        targetSettingDTO.setTargetSettingType(2);
        targetSettingDTO.setIndicatorId(indicatorId);
        targetSettingDTO.setSort(0);
        TargetSetting setting;
        if (StringUtils.isNull(targetSetting)) {//新增
            setting = insertTargetSetting(targetSettingDTO);
            Long targetSettingId = setting.getTargetSettingId();
            if (StringUtils.isNotEmpty(targetSettingIncomeVOS)) {
                TargetSettingIncomeDTO targetSettingIncomeDTO = incomeVoToDto(targetSettingIncomeVOS);
                targetSettingIncomeDTO.setTargetSettingId(targetSettingId);
                targetSettingIncomeService.insertTargetSettingIncome(targetSettingIncomeDTO);
            }
            targetSetting = new TargetSettingDTO();
            targetSetting.setTargetSettingId(setting.getTargetSettingId());
        } else {
            updateTargetSetting(targetSettingDTO);
            Long targetSettingId = targetSetting.getTargetSettingId();
            if (StringUtils.isNotEmpty(targetSettingIncomeVOS)) {
                TargetSettingIncomeDTO targetSettingIncomeDTO = incomeVoToDto(targetSettingIncomeVOS);
                targetSettingIncomeDTO.setTargetSettingId(targetSettingId);
                targetSettingIncomeService.updateTargetSettingIncome(targetSettingIncomeDTO);
            }
        }
        return targetSetting;
    }

    /**
     * VO → DTO
     *
     * @param targetSettingIncomeVOS
     * @return
     */
    private TargetSettingIncomeDTO incomeVoToDto(List<TargetSettingIncomeVO> targetSettingIncomeVOS) {
        TargetSettingIncomeDTO targetSettingIncomeDTO = new TargetSettingIncomeDTO();
        for (int i = 0; i < targetSettingIncomeVOS.size(); i++) {
            if (i == 0) {//前一年
                TargetSettingIncomeVO targetSettingIncomeVO = targetSettingIncomeVOS.get(i);
                targetSettingIncomeDTO.setConversionBeforeOne(targetSettingIncomeVO.getConversion());
                targetSettingIncomeDTO.setMoneyBeforeOne(targetSettingIncomeVO.getMoney());
            } else if (i == 1) {//前两年
                TargetSettingIncomeVO targetSettingIncomeVO = targetSettingIncomeVOS.get(i);
                targetSettingIncomeDTO.setConversionBeforeTwo(targetSettingIncomeVO.getConversion());
                targetSettingIncomeDTO.setMoneyBeforeTwo(targetSettingIncomeVO.getMoney());
            } else if (i == 2) {//前三年
                TargetSettingIncomeVO targetSettingIncomeVO = targetSettingIncomeVOS.get(i);
                targetSettingIncomeDTO.setConversionBeforeThree(targetSettingIncomeVO.getConversion());
                targetSettingIncomeDTO.setMoneyBeforeThree(targetSettingIncomeVO.getMoney());
            }
        }
        return targetSettingIncomeDTO;
    }

    /**
     * 查询销售收入目标制定列表
     *
     * @param targetSettingDTO
     * @return
     */
    @Override
    public TargetSettingDTO selectIncomeTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        IndicatorDTO indicatorIncomeDTO = getIndicator(IndicatorCode.INCOME.getCode());
        IndicatorDTO indicatorOrderDTO = getIndicator(IndicatorCode.ORDER.getCode());
        List<Integer> historyNumS = getHistoryYearList(targetYear + 1, 4);
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setIndicatorId(indicatorIncomeDTO.getIndicatorId());
        targetSetting.setTargetSettingType(2);
        targetSetting.setTargetYear(targetYear);
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingList(targetSetting);// 当年份销售收入的目标制定list
        // 当年份销售订单的目标制定-当年
        TargetSettingDTO targetSettingByIndicator = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, indicatorOrderDTO.getIndicatorId());
        BigDecimal zero = new BigDecimal(0);
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            List<TargetSettingIncomeVO> targetSettingIncomeVOS = new ArrayList<>();
            for (int i = 0; i < historyNumS.size(); i++) {
                TargetSettingIncomeVO targetSettingIncomeVO = new TargetSettingIncomeVO();
                if (i == 0) {//当年  // 目标值 挑战值 保底值  //本年增量订单-订单金额:从经营云-目标制定-公司目标生成-销售订单目标制定中获取当年目标值
                    if (StringUtils.isNotNull(targetSettingByIndicator)) {
                        BigDecimal targetValue = targetSettingByIndicator.getTargetValue();
                        targetSettingIncomeVO.setMoney(targetValue);
                    } else {
                        targetSettingIncomeVO.setMoney(zero);
                    }
                    targetSettingDTO.setChallengeValue(zero);
                    targetSettingDTO.setTargetValue(zero);
                    targetSettingDTO.setGuaranteedValue(zero);
                    targetSettingIncomeVO.setConversion(zero);
                    targetSettingIncomeVO.setIncome(zero);
                    targetSettingIncomeVO.setYearName("本年增量订单");
                    targetSettingIncomeVOS.add(targetSettingIncomeVO);
                } else if (i == 1) {//前一年
                    targetSettingIncomeVO.setMoney(zero);
                    targetSettingIncomeVO.setConversion(zero);
                    targetSettingIncomeVO.setIncome(zero);
                    targetSettingIncomeVO.setYearName(targetYear - i + "年存量订单");
                    targetSettingIncomeVOS.add(targetSettingIncomeVO);
                } else if (i == 2) {//前两年
                    targetSettingIncomeVO.setMoney(zero);
                    targetSettingIncomeVO.setConversion(zero);
                    targetSettingIncomeVO.setIncome(zero);
                    targetSettingIncomeVO.setYearName(targetYear - i + "年存量订单");
                    targetSettingIncomeVOS.add(targetSettingIncomeVO);
                } else if (i == 3) {//前三年
                    targetSettingIncomeVO.setMoney(zero);
                    targetSettingIncomeVO.setConversion(zero);
                    targetSettingIncomeVO.setIncome(zero);
                    targetSettingIncomeVO.setYearName(targetYear - i + "年及以前存量订单");
                    targetSettingIncomeVOS.add(targetSettingIncomeVO);
                }
            }
            targetSettingDTO.setTargetSettingIncomeVOS(targetSettingIncomeVOS);
            return targetSettingDTO;
        }
        TargetSettingDTO TargetSetting = targetSettingDTOS.get(0);
        Long targetSettingId = TargetSetting.getTargetSettingId();
        List<TargetSettingIncomeDTO> targetSettingIncomeDTOS = targetSettingIncomeService.selectTargetSettingIncomeByHistoryNumS(targetSettingId);
        TargetSettingIncomeDTO targetSettingIncomeDTO = targetSettingIncomeDTOS.get(0);
        List<TargetSettingIncomeVO> targetSettingIncomeVOS = new ArrayList<>();
        // dtoToVo
        for (int i = 0; i < historyNumS.size(); i++) {
            TargetSettingIncomeVO targetSettingIncomeVO = new TargetSettingIncomeVO();
            if (i == 0) {//当年
                targetSettingDTO.setChallengeValue(zero);
                targetSettingDTO.setTargetValue(zero);
                targetSettingDTO.setGuaranteedValue(zero);
                if (StringUtils.isNotNull(targetSettingByIndicator)) {
                    BigDecimal targetValue = targetSettingByIndicator.getTargetValue();
                    targetSettingIncomeVO.setMoney(targetValue);
                } else {
                    targetSettingIncomeVO.setMoney(zero);
                }
                targetSettingIncomeVO.setYearName("本年增量订单");
                targetSettingIncomeVOS.add(targetSettingIncomeVO);
            } else if (i == 1) {//前一年
                BigDecimal income = targetSettingIncomeDTO.getMoneyBeforeOne().multiply(targetSettingIncomeDTO.getConversionBeforeOne());
                targetSettingIncomeVO.setIncome(income);
                targetSettingIncomeVO.setMoney(targetSettingIncomeDTO.getMoneyBeforeOne());
                targetSettingIncomeVO.setConversion(targetSettingIncomeDTO.getConversionBeforeOne());
                targetSettingIncomeVO.setYearName(targetYear - i + "年存量订单");
                targetSettingIncomeVOS.add(targetSettingIncomeVO);
            } else if (i == 2) {//前两年
                BigDecimal income = targetSettingIncomeDTO.getMoneyBeforeTwo().multiply(targetSettingIncomeDTO.getConversionBeforeTwo());
                targetSettingIncomeVO.setIncome(income);
                targetSettingIncomeVO.setMoney(targetSettingIncomeDTO.getMoneyBeforeTwo());
                targetSettingIncomeVO.setConversion(targetSettingIncomeDTO.getConversionBeforeTwo());
                targetSettingIncomeVO.setYearName(targetYear - i + "年存量订单");
                targetSettingIncomeVOS.add(targetSettingIncomeVO);
            } else if (i == 3) {//前三年
                BigDecimal income = targetSettingIncomeDTO.getMoneyBeforeThree().multiply(targetSettingIncomeDTO.getConversionBeforeThree());
                targetSettingIncomeVO.setIncome(income);
                targetSettingIncomeVO.setMoney(targetSettingIncomeDTO.getMoneyBeforeThree());
                targetSettingIncomeVO.setConversion(targetSettingIncomeDTO.getConversionBeforeThree());
                targetSettingIncomeVO.setYearName(targetYear - i + "年及以前存量订单");
                targetSettingIncomeVOS.add(targetSettingIncomeVO);
            }
        }
        TargetSetting.setTargetSettingIncomeVOS(targetSettingIncomeVOS);
        return TargetSetting;
    }

    /**
     * 查询销售回款目标制定列表
     *
     * @param targetSettingDTO 目标制定列表s
     * @return
     */
    @Override
    public TargetSettingDTO selectRecoveryTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        IndicatorDTO indicatorReceivableDTO = getIndicator(IndicatorCode.RECEIVABLE.getCode());
        IndicatorDTO indicatorIncomeDTO = getIndicator(IndicatorCode.INCOME.getCode());
        TargetSettingDTO targetSettingByIndicator = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, indicatorReceivableDTO.getIndicatorId());
        TargetSettingDTO targetIncomeByIndicator = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, indicatorIncomeDTO.getIndicatorId());
        List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS = new ArrayList<>();
        List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS = new ArrayList<>();
        BigDecimal zero = new BigDecimal(0);
        if (StringUtils.isNotNull(targetSettingByIndicator)) {
            Long targetSettingId = targetSettingByIndicator.getTargetSettingId();
            BigDecimal percentage = targetSettingByIndicator.getPercentage();
            TargetSettingRecoveryDTO recoveryDTO = targetSettingRecoveryService.selectTargetSettingRecoveryByTargetSettingId(targetSettingId);
            if (StringUtils.isNull(recoveryDTO)) {
                recoveryDTO = setRecoveryZero(zero);
            }
            recoveryDTO.setAddRate(percentage);
            targetSettingByIndicator.setTargetSettingRecoveryDTO(recoveryDTO);
            BigDecimal DSOValue = new BigDecimal(recoveryDTO.getBaselineValue() + recoveryDTO.getImproveDays());
//               todo 查询销售回款列表--recoveries
//              【DSO（应收账款周转天数）】：公式=DSO基线-DSO改进天数。
//              【期末应收账款余额】：公式=（销售收入目标*DSO）/180-上年年末应收账款余额。
//              【回款总目标】：公式=上年年末应收账款余额+销售收入目标*（1+平均增值税率）-期末应收账款余额。

            List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDTOS = targetSettingRecoveriesServices.selectTargetSettingRecoveriesByTargetSettingId(targetSettingId);
            if (targetSettingRecoveriesDTOS.size() > 5) {
                for (TargetSettingRecoveriesDTO targetSettingRecoveriesDTO : targetSettingRecoveriesDTOS) {
                    switch (targetSettingRecoveriesDTO.getType()) {
                        case 1:
                            targetSettingRecoveriesDTO.setPrefixType("1.应回尽回");
                            targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                        case 2:
                            targetSettingRecoveriesDTO.setPrefixType("2.逾期清理");
                            targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                        case 3:
                            targetSettingRecoveriesDTO.setPrefixType("3.提前回款");
                            targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                        case 4:
                            targetSettingRecoveriesDTO.setPrefixType("销售收入目标");

                            targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
                        case 5:
                            targetSettingRecoveriesDTO.setPrefixType("期末应收账款余额");
                            targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
                    }
                }
                TargetSettingRecoveriesDTO targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                targetSettingRecoveriesDTO.setPrefixType("合计");
                targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);

                targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                targetSettingRecoveriesDTO.setPrefixType("DSO");
                targetSettingRecoveriesDTO.setChallengeValue(DSOValue);
                targetSettingRecoveriesDTO.setTargetValue(DSOValue);
                targetSettingRecoveriesDTO.setGuaranteedValue(DSOValue);
                targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                targetSettingRecoveriesDTO.setPrefixType("回款总目标");
                //回款总目标-1.挑战值，目标值，保底值
                BigDecimal challengeGoal = recoveryDTO.getBalanceReceivables().add(targetIncomeByIndicator.getChallengeValue().multiply(targetSettingByIndicator.getPercentage().add(new BigDecimal(1))));
                BigDecimal targetGoal = recoveryDTO.getBalanceReceivables().add(targetIncomeByIndicator.getTargetValue().multiply(targetSettingByIndicator.getPercentage().add(new BigDecimal(1))));
                BigDecimal guaranteedGoal = recoveryDTO.getBalanceReceivables().add(targetIncomeByIndicator.getGuaranteedValue().multiply(targetSettingByIndicator.getPercentage().add(new BigDecimal(1))));
                targetSettingRecoveriesDTO.setChallengeValue(challengeGoal);
                targetSettingRecoveriesDTO.setTargetValue(targetGoal);
                targetSettingRecoveriesDTO.setGuaranteedValue(guaranteedGoal);
                targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
            } else {
                setRecoveriesZero(targetSettingTypeDTOS, targetSettingIndicatorDTOS, zero, targetIncomeByIndicator);
            }
        } else {
            targetSettingByIndicator = new TargetSettingDTO();
            targetSettingByIndicator.setChallengeValue(zero);
            targetSettingByIndicator.setTargetValue(zero);
            targetSettingByIndicator.setGuaranteedValue(zero);
            TargetSettingRecoveryDTO recoveryDTO = setRecoveryZero(zero);
            targetSettingByIndicator.setTargetSettingRecoveryDTO(recoveryDTO);
            setRecoveriesZero(targetSettingTypeDTOS, targetSettingIndicatorDTOS, zero, targetIncomeByIndicator);
        }
        targetSettingByIndicator.setTargetSettingTypeDTOS(targetSettingTypeDTOS);
        targetSettingByIndicator.setTargetSettingIndicatorDTOS(targetSettingIndicatorDTOS);
        return targetSettingByIndicator;
    }

    /**
     * 销售回款给null赋值0
     *
     * @param zero
     * @return
     */
    private static TargetSettingRecoveryDTO setRecoveryZero(BigDecimal zero) {
        TargetSettingRecoveryDTO recoveryDTO;
        recoveryDTO = new TargetSettingRecoveryDTO();
        recoveryDTO.setBalanceReceivables(zero);
        recoveryDTO.setBaselineValue(0);
        recoveryDTO.setImproveDays(0);
        return recoveryDTO;
    }

    /**
     * 空值赋0
     *
     * @param targetSettingTypeDTOS
     * @param targetSettingIndicatorDTOS
     */
    private static void setRecoveriesZero(List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS, List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS, BigDecimal zero, TargetSettingDTO targetIncomeByIndicator) {
        TargetSettingRecoveriesDTO targetSettingRecoveriesDTO;
        for (int i = 0; i < 5; i++) {
            switch (i) {
                case 1:
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("1.应回尽回");
                    targetSettingRecoveriesDTO.setActualLastYear(zero);
                    setRecoveriesValue(targetSettingRecoveriesDTO, zero);
                    targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                case 2:
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("2.逾期清理");
                    targetSettingRecoveriesDTO.setActualLastYear(zero);
                    setRecoveriesValue(targetSettingRecoveriesDTO, zero);
                    targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                case 3:
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("3.提前回款");
                    targetSettingRecoveriesDTO.setActualLastYear(zero);
                    setRecoveriesValue(targetSettingRecoveriesDTO, zero);
                    targetSettingTypeDTOS.add(targetSettingRecoveriesDTO);
                case 4:
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("销售收入目标");
                    targetSettingRecoveriesDTO.setTargetValue(targetIncomeByIndicator.getTargetValue());
                    targetSettingRecoveriesDTO.setGuaranteedValue(targetIncomeByIndicator.getGuaranteedValue());
                    targetSettingRecoveriesDTO.setChallengeValue(targetIncomeByIndicator.getChallengeValue());

//                    targetIncomeByIndicator.getChallengeValue()
                    targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
                case 5:
                    targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
                    targetSettingRecoveriesDTO.setPrefixType("期末应收账款余额");
                    setRecoveriesValue(targetSettingRecoveriesDTO, zero);
                    targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
            }
        }
        targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
        targetSettingRecoveriesDTO.setPrefixType("期末应收账款余额");
        setRecoveriesValue(targetSettingRecoveriesDTO, zero);
        targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
        targetSettingRecoveriesDTO = new TargetSettingRecoveriesDTO();
        targetSettingRecoveriesDTO.setPrefixType("汇款总目标");
        setRecoveriesValue(targetSettingRecoveriesDTO, zero);
        targetSettingIndicatorDTOS.add(targetSettingRecoveriesDTO);
    }

    private static void setRecoveriesValue(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO, BigDecimal zero) {
        targetSettingRecoveriesDTO.setTargetValue(zero);
        targetSettingRecoveriesDTO.setGuaranteedValue(zero);
        targetSettingRecoveriesDTO.setChallengeValue(zero);
    }

    /**
     * 保存销售回款目标制定列表
     *
     * @param targetSettingDTO 目标制定列表
     * @return
     */
    @Override
    @Transactional
    public TargetSettingDTO saveRecoveryTargetSetting(TargetSettingDTO targetSettingDTO) {
        IndicatorDTO indicatorIncomeDTO = getIndicator(IndicatorCode.RECEIVABLE.getCode());
        Integer targetYear = targetSettingDTO.getTargetYear();
        List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS = targetSettingDTO.getTargetSettingIndicatorDTOS();
        List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS = targetSettingDTO.getTargetSettingTypeDTOS();
        TargetSettingRecoveryDTO targetSettingRecoveryDTO = targetSettingDTO.getTargetSettingRecoveryDTO();
        targetSettingDTO.setSort(0);
        targetSettingDTO.setTargetSettingType(3);
        targetSettingDTO.setIndicatorId(indicatorIncomeDTO.getIndicatorId());
        targetSettingDTO.setPercentage(targetSettingRecoveryDTO.getAddRate());
        TargetSettingDTO targetSettingByIndicator = targetSettingMapper.selectTargetSettingByTargetYearAndIndicator(targetYear, indicatorIncomeDTO.getIndicatorId());
        Long targetSettingId;
        if (StringUtils.isNull(targetSettingByIndicator)) {
            TargetSetting targetSetting = insertTargetSetting(targetSettingDTO);
            targetSettingId = targetSetting.getTargetSettingId();
        } else {
            targetSettingId = targetSettingByIndicator.getTargetSettingId();
            targetSettingDTO.setTargetSettingId(targetSettingId);
            updateTargetSetting(targetSettingDTO);
        }
        TargetSettingRecoveryDTO recovery = targetSettingRecoveryService.selectTargetSettingRecoveryByTargetSettingId(targetSettingId);
        targetSettingRecoveryDTO.setTargetSettingId(targetSettingId);
        if (StringUtils.isNull(recovery)) {
            TargetSettingRecoveryDTO targetSettingRecovery = targetSettingRecoveryService.insertTargetSettingRecovery(targetSettingRecoveryDTO);
        } else {
            Long targetSettingRecoveriesId = recovery.getTargetSettingRecoveriesId();
            targetSettingRecoveryDTO.setTargetSettingRecoveriesId(targetSettingRecoveriesId);
            targetSettingRecoveryService.updateTargetSettingRecovery(targetSettingRecoveryDTO);
        }
        List<TargetSettingRecoveriesDTO> recoveries = targetSettingRecoveriesServices.selectTargetSettingRecoveriesByTargetSettingId(targetSettingId);
        List<TargetSettingRecoveriesDTO> recoveryDTO = integration(targetSettingIndicatorDTOS, targetSettingTypeDTOS, targetSettingId);
        if (StringUtils.isNotEmpty(recoveries) && recoveries.size() > 5) {
            targetSettingRecoveriesServices.updateTargetSettingRecoveriess(recoveryDTO);
        } else {// 新增
            targetSettingRecoveriesServices.insertTargetSettingRecoveriess(recoveryDTO);
        }
        targetSettingDTO.setTargetSettingId(targetSettingId);
        return targetSettingDTO;
    }

    /**
     * 整合两个表
     *
     * @param targetSettingIndicatorDTOS 指标表
     * @param targetSettingTypeDTOS      类型表
     * @param targetSettingId            目标制定ID
     * @return
     */
    private List<TargetSettingRecoveriesDTO> integration(List<TargetSettingRecoveriesDTO> targetSettingIndicatorDTOS, List<TargetSettingRecoveriesDTO> targetSettingTypeDTOS, Long targetSettingId) {
        List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDTOS = new ArrayList<>();
        if (StringUtils.isNotEmpty(targetSettingIndicatorDTOS)) {
            for (TargetSettingRecoveriesDTO targetSettingIndicatorDTO : targetSettingIndicatorDTOS) {
                if (targetSettingIndicatorDTO.getPrefixType().equals("销售收入目标")) {
                    targetSettingIndicatorDTO.setType(4);
                    targetSettingIndicatorDTO.setTargetSettingId(targetSettingId);
                    targetSettingRecoveriesDTOS.add(targetSettingIndicatorDTO);
                } else if (targetSettingIndicatorDTO.getPrefixType().equals("期末应收账款余额")) {
                    targetSettingIndicatorDTO.setType(5);
                    targetSettingIndicatorDTO.setTargetSettingId(targetSettingId);
                    targetSettingRecoveriesDTOS.add(targetSettingIndicatorDTO);
                }
            }
        }
        if (StringUtils.isNotEmpty(targetSettingTypeDTOS)) {
            for (TargetSettingRecoveriesDTO targetSettingTypeDTO : targetSettingTypeDTOS) {
                TargetSettingRecoveriesDTO recoveriesDTO = new TargetSettingRecoveriesDTO();
                switch (targetSettingTypeDTO.getPrefixType()) {
                    case "1.应回尽回":
                        targetSettingTypeDTO.setType(1);
                        targetSettingTypeDTO.setTargetSettingId(targetSettingId);
                        targetSettingRecoveriesDTOS.add(targetSettingTypeDTO);
                    case "2.逾期清理":
                        targetSettingTypeDTO.setType(2);
                        targetSettingTypeDTO.setTargetSettingId(targetSettingId);
                        targetSettingRecoveriesDTOS.add(targetSettingTypeDTO);
                    case "3.提前回款":
                        targetSettingTypeDTO.setType(3);
                        targetSettingTypeDTO.setTargetSettingId(targetSettingId);
                        targetSettingRecoveriesDTOS.add(targetSettingTypeDTO);
                }
            }
        }
        return targetSettingRecoveriesDTOS;
    }

}

