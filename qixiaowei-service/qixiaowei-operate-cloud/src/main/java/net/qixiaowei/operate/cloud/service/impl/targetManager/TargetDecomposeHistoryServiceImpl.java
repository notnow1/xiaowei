package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.math.BigDecimal;
import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.mapper.targetManager.DecomposeDetailsSnapshotMapper;
import net.qixiaowei.operate.cloud.mapper.targetManager.DetailCyclesSnapshotMapper;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeHistory;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeHistoryExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetDecomposeHistoryMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeHistoryService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
 * TargetDecomposeHistoryService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-31
 */
@Service
public class TargetDecomposeHistoryServiceImpl implements ITargetDecomposeHistoryService {
    @Autowired
    private TargetDecomposeHistoryMapper targetDecomposeHistoryMapper;
    @Autowired
    private DecomposeDetailsSnapshotMapper decomposeDetailsSnapshotMapper;
    @Autowired
    private DetailCyclesSnapshotMapper detailCyclesSnapshotMapper;

    /**
     * 查询目标分解历史版本表
     *
     * @param targetDecomposeHistoryId 目标分解历史版本表主键
     * @return 目标分解历史版本表
     */
    @Override
    public TargetDecomposeHistoryDTO selectTargetDecomposeHistoryByTargetDecomposeHistoryId(Long targetDecomposeHistoryId) {
        //历史目标分解主表数据
        TargetDecomposeHistoryDTO targetDecomposeHistoryDTO = targetDecomposeHistoryMapper.selectTargetDecomposeHistoryByTargetDecomposeHistoryId(targetDecomposeHistoryId);

        List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS = decomposeDetailsSnapshotMapper.selectDecomposeDetailsSnapshotByTargetDecomposeHistoryId(targetDecomposeHistoryId);
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)){
            for (DecomposeDetailsSnapshotDTO decomposeDetailsSnapshotDTO : decomposeDetailsSnapshotDTOS) {
                //年度预测值
                BigDecimal forecastYear = new BigDecimal("0");
                //累计实际值
                BigDecimal actualTotal = new BigDecimal("0");
                //目标完成率
                BigDecimal targetPercentageComplete = new BigDecimal("0");
                //周期表数据
                List<DetailCyclesSnapshotDTO> detailCyclesSnapshotDTOS = detailCyclesSnapshotMapper.selectDetailCyclesSnapshotByTargetDecomposeHistoryId(decomposeDetailsSnapshotDTO.getTargetDecomposeHistoryId());
                for (DetailCyclesSnapshotDTO detailCyclesSnapshotDTO : detailCyclesSnapshotDTOS) {
                    //预测值
                    forecastYear=forecastYear.add(detailCyclesSnapshotDTO.getCycleForecast());
                    //实际值
                    actualTotal=actualTotal.add(detailCyclesSnapshotDTO.getCycleActual());
                }
                //保留一位小数
                targetPercentageComplete=targetPercentageComplete.divide(decomposeDetailsSnapshotDTO.getDecomposeTarget()).setScale(1);
                decomposeDetailsSnapshotDTO.setForecastYear(forecastYear);
                decomposeDetailsSnapshotDTO.setActualTotal(actualTotal);
                decomposeDetailsSnapshotDTO.setTargetPercentageComplete(targetPercentageComplete);
                decomposeDetailsSnapshotDTO.setDetailCyclesSnapshotDTOS(detailCyclesSnapshotDTOS);
            }
        }
            return targetDecomposeHistoryDTO.setDecomposeDetailsSnapshotDTOS(decomposeDetailsSnapshotDTOS);
    }

    /**
     * 查询目标分解历史版本表列表
     *
     * @param targetDecomposeHistoryDTO 目标分解历史版本表
     * @return 目标分解历史版本表
     */
    @Override
    public List<TargetDecomposeHistoryDTO> selectTargetDecomposeHistoryList(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
        BeanUtils.copyProperties(targetDecomposeHistoryDTO, targetDecomposeHistory);
        return targetDecomposeHistoryMapper.selectTargetDecomposeHistoryList(targetDecomposeHistory);
    }

    /**
     * 根据目标分解id查询目标分解历史版本表列表
     *
     * @param targetDecomposeId 目标分解历史版本表
     * @return
     */
    @Override
    public List<TargetDecomposeHistoryDTO> targetDecomposeIdInfo(Long targetDecomposeId) {
        return targetDecomposeHistoryMapper.selectTargetDecomposeHistoryByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 新增目标分解历史版本表
     *
     * @param targetDecomposeHistoryDTO 目标分解历史版本表
     * @return 结果
     */
    @Override
    public TargetDecomposeHistoryDTO insertTargetDecomposeHistory(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
        BeanUtils.copyProperties(targetDecomposeHistoryDTO, targetDecomposeHistory);
        targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
        targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
        targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
        targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
        targetDecomposeHistory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetDecomposeHistoryMapper.insertTargetDecomposeHistory(targetDecomposeHistory);
        targetDecomposeHistoryDTO.setTargetDecomposeHistoryId(targetDecomposeHistory.getTargetDecomposeHistoryId());
        return targetDecomposeHistoryDTO;
    }

    /**
     * 修改目标分解历史版本表
     *
     * @param targetDecomposeHistoryDTO 目标分解历史版本表
     * @return 结果
     */
    @Override
    public int updateTargetDecomposeHistory(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
        BeanUtils.copyProperties(targetDecomposeHistoryDTO, targetDecomposeHistory);
        targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
        targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
        return targetDecomposeHistoryMapper.updateTargetDecomposeHistory(targetDecomposeHistory);
    }

    /**
     * 逻辑批量删除目标分解历史版本表
     *
     * @param targetDecomposeHistoryIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetDecomposeHistoryByTargetDecomposeHistoryIds(List<Long> targetDecomposeHistoryIds) {
        return targetDecomposeHistoryMapper.logicDeleteTargetDecomposeHistoryByTargetDecomposeHistoryIds(targetDecomposeHistoryIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标分解历史版本表信息
     *
     * @param targetDecomposeHistoryId 目标分解历史版本表主键
     * @return 结果
     */
    @Override
    public int deleteTargetDecomposeHistoryByTargetDecomposeHistoryId(Long targetDecomposeHistoryId) {
        return targetDecomposeHistoryMapper.deleteTargetDecomposeHistoryByTargetDecomposeHistoryId(targetDecomposeHistoryId);
    }

    /**
     * 逻辑删除目标分解历史版本表信息
     *
     * @param targetDecomposeHistoryDTO 目标分解历史版本表
     * @return 结果
     */
    @Override
    public int logicDeleteTargetDecomposeHistoryByTargetDecomposeHistoryId(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
        targetDecomposeHistory.setTargetDecomposeHistoryId(targetDecomposeHistoryDTO.getTargetDecomposeHistoryId());
        targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
        targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
        return targetDecomposeHistoryMapper.logicDeleteTargetDecomposeHistoryByTargetDecomposeHistoryId(targetDecomposeHistory);
    }

    /**
     * 物理删除目标分解历史版本表信息
     *
     * @param targetDecomposeHistoryDTO 目标分解历史版本表
     * @return 结果
     */

    @Override
    public int deleteTargetDecomposeHistoryByTargetDecomposeHistoryId(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
        BeanUtils.copyProperties(targetDecomposeHistoryDTO, targetDecomposeHistory);
        return targetDecomposeHistoryMapper.deleteTargetDecomposeHistoryByTargetDecomposeHistoryId(targetDecomposeHistory.getTargetDecomposeHistoryId());
    }

    /**
     * 物理批量删除目标分解历史版本表
     *
     * @param targetDecomposeHistoryDtos 需要删除的目标分解历史版本表主键
     * @return 结果
     */

    @Override
    public int deleteTargetDecomposeHistoryByTargetDecomposeHistoryIds(List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDtos) {
        List<Long> stringList = new ArrayList();
        for (TargetDecomposeHistoryDTO targetDecomposeHistoryDTO : targetDecomposeHistoryDtos) {
            stringList.add(targetDecomposeHistoryDTO.getTargetDecomposeHistoryId());
        }
        return targetDecomposeHistoryMapper.deleteTargetDecomposeHistoryByTargetDecomposeHistoryIds(stringList);
    }

    /**
     * 批量新增目标分解历史版本表信息
     *
     * @param targetDecomposeHistoryDtos 目标分解历史版本表对象
     */

    public int insertTargetDecomposeHistorys(List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDtos) {
        List<TargetDecomposeHistory> targetDecomposeHistoryList = new ArrayList();

        for (TargetDecomposeHistoryDTO targetDecomposeHistoryDTO : targetDecomposeHistoryDtos) {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            BeanUtils.copyProperties(targetDecomposeHistoryDTO, targetDecomposeHistory);
            targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeHistoryList.add(targetDecomposeHistory);
        }
        return targetDecomposeHistoryMapper.batchTargetDecomposeHistory(targetDecomposeHistoryList);
    }

    /**
     * 批量修改目标分解历史版本表信息
     *
     * @param targetDecomposeHistoryDtos 目标分解历史版本表对象
     */

    public int updateTargetDecomposeHistorys(List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDtos) {
        List<TargetDecomposeHistory> targetDecomposeHistoryList = new ArrayList();

        for (TargetDecomposeHistoryDTO targetDecomposeHistoryDTO : targetDecomposeHistoryDtos) {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            BeanUtils.copyProperties(targetDecomposeHistoryDTO, targetDecomposeHistory);
            targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistoryList.add(targetDecomposeHistory);
        }
        return targetDecomposeHistoryMapper.updateTargetDecomposeHistorys(targetDecomposeHistoryList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetDecomposeHistory(List<TargetDecomposeHistoryExcel> list) {
        List<TargetDecomposeHistory> targetDecomposeHistoryList = new ArrayList<>();
        list.forEach(l -> {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            BeanUtils.copyProperties(l, targetDecomposeHistory);
            targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeHistoryList.add(targetDecomposeHistory);
        });
        try {
            targetDecomposeHistoryMapper.batchTargetDecomposeHistory(targetDecomposeHistoryList);
        } catch (Exception e) {
            throw new ServiceException("导入目标分解历史版本表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param targetDecomposeHistoryDTO
     * @return
     */
    @Override
    public List<TargetDecomposeHistoryExcel> exportTargetDecomposeHistory(TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
        BeanUtils.copyProperties(targetDecomposeHistoryDTO, targetDecomposeHistory);
        List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDTOList = targetDecomposeHistoryMapper.selectTargetDecomposeHistoryList(targetDecomposeHistory);
        List<TargetDecomposeHistoryExcel> targetDecomposeHistoryExcelList = new ArrayList<>();
        return targetDecomposeHistoryExcelList;
    }
}

