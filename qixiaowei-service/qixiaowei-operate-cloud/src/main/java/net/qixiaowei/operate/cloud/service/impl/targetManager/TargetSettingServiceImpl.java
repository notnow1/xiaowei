package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingOrderDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingOrderMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Map;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSetting;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingExcel;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


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
    public TargetSettingDTO insertTargetSetting(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setCreateBy(SecurityUtils.getUserId());
        targetSetting.setCreateTime(DateUtils.getNowDate());
        targetSetting.setUpdateTime(DateUtils.getNowDate());
        targetSetting.setUpdateBy(SecurityUtils.getUserId());
        targetSetting.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetSettingMapper.insertTargetSetting(targetSetting);
        targetSettingDTO.setTargetSettingId(targetSetting.getTargetSettingId());
        return targetSettingDTO;
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
        List<Long> stringList = new ArrayList();
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
        List<TargetSetting> targetSettingList = new ArrayList();

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
        List<TargetSetting> targetSettingList = new ArrayList();

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
     * @param targetSettingDTO
     * @return
     */
    @Override
    public List<TargetSettingExcel> exportTargetSetting(TargetSettingDTO targetSettingDTO) {
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        List<TargetSettingDTO> targetSettingDTOList = targetSettingMapper.selectTargetSettingList(targetSetting);
        List<TargetSettingExcel> targetSettingExcelList = new ArrayList<>();
        return targetSettingExcelList;
    }

    /**
     * 保存销售订单目标制定
     *
     * @param targetSettingDTO 目标制定
     * @return
     */
    @Override
    public int saveOrderTargetSetting(TargetSettingDTO targetSettingDTO) {
        return 0;
    }

    /**
     * todo 查询销售订单目标制定
     *
     * @param targetSettingDTO
     * @return
     */
    @Override
    public TargetSettingDTO selectOrderTargetSettingList(TargetSettingDTO targetSettingDTO) {
        Integer targetYear = targetSettingDTO.getTargetYear();
        Integer historyNum = targetSettingDTO.getHistoryNum();
        List<Integer> historyNumS = new ArrayList<>();
        for (int i = -1; i < historyNum; i++) {
            historyNumS.add(targetYear);
        }
        TargetSetting targetSetting = new TargetSetting();
        BeanUtils.copyProperties(targetSettingDTO, targetSetting);
        targetSetting.setTargetSettingType(1);
        targetSetting.setTargetYear(targetYear);
        List<TargetSettingDTO> targetSettingDTOS = targetSettingMapper.selectTargetSettingList(targetSetting);
        if (StringUtils.isEmpty(targetSettingDTOS)) {
            // todo 通过目标年度直接去找滚动越策管理,获取历史年度实际值
            List<TargetSettingOrderDTO> maps = selectRollForecastManagementByTargetYear(historyNumS);
        }
        TargetSettingDTO settingDTO = targetSettingDTOS.get(0);
        Long targetSettingId = settingDTO.getTargetSettingId();
        List<TargetSettingOrderDTO> targetSettingOrderDTOS = targetSettingOrderService.selectTargetSettingOrderListByHistoryYears(historyNumS);
        return null;
    }

    /**
     * todo 在滚动预测管理找到order分表的值，若没有则返回空
     *
     * @param historyNumS 历史年度list
     * @return
     */
    private List<TargetSettingOrderDTO> selectRollForecastManagementByTargetYear(List<Integer> historyNumS  ) {
        return null;
    }
}

