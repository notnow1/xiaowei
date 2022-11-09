package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.Convert;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.DecomposeDetailsSnapshot;
import net.qixiaowei.operate.cloud.api.domain.targetManager.DetailCyclesSnapshot;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeHistory;
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeHistoryExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.*;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


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

    @Autowired
    private TargetDecomposeMapper targetDecomposeMapper;
    @Autowired
    private DecomposeDetailCyclesMapper decomposeDetailCyclesMapper;
    @Autowired
    private TargetDecomposeDetailsMapper targetDecomposeDetailsMapper;

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
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)) {
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
                    forecastYear = forecastYear.add(detailCyclesSnapshotDTO.getCycleForecast());
                    //实际值
                    actualTotal = actualTotal.add(detailCyclesSnapshotDTO.getCycleActual());
                }
                //保留一位小数
                targetPercentageComplete = targetPercentageComplete.divide(decomposeDetailsSnapshotDTO.getDecomposeTarget()).setScale(1);
                decomposeDetailsSnapshotDTO.setForecastYear(forecastYear);
                decomposeDetailsSnapshotDTO.setActualTotal(actualTotal);
                decomposeDetailsSnapshotDTO.setTargetPercentageComplete(targetPercentageComplete);
                decomposeDetailsSnapshotDTO.setDetailCyclesSnapshotDTOS(detailCyclesSnapshotDTOS);
            }
            return targetDecomposeHistoryDTO.setDecomposeDetailsSnapshotDTOS(decomposeDetailsSnapshotDTOS);
        } else {
            return targetDecomposeHistoryDTO;
        }

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

    /**
     * 定时任务生成目标分解历史数据
     */
    @Override
    public void cronCreateHistoryList(int timeDimension) {
        //历史目标分解集合
        List<TargetDecomposeHistory> targetDecomposeHistories = new ArrayList<>();
        //历史目标分解详情集合
        List<DecomposeDetailsSnapshot> decomposeDetailsSnapshots = new ArrayList<>();
        //历史目标分解详情周期集合
        List<DetailCyclesSnapshot> detailCyclesSnapshots = new ArrayList<>();
        List<TargetDecomposeDTO> targetDecomposeDTOS = targetDecomposeMapper.selectCronCreateHistoryList(timeDimension);

        if (StringUtils.isNotEmpty(targetDecomposeDTOS)) {
            for (int i = 0; i < targetDecomposeDTOS.size(); i++) {
                //插入历史目标分解主表集合
                this.packTargetDecomposeHistory(targetDecomposeDTOS.get(i), targetDecomposeHistories, i);
            }
            if (StringUtils.isNotEmpty(targetDecomposeHistories)) {
                try {
                    targetDecomposeHistoryMapper.batchTargetDecomposeHistory(targetDecomposeHistories);
                } catch (Exception e) {
                    throw new ServiceException("插入历史目标分解失败");
                }
            }
            for (int i = 0; i < targetDecomposeDTOS.size(); i++) {
                //插入历史目标分解详情集合
                this.packDecomposeDetailsSnapshot(targetDecomposeDTOS.get(i), decomposeDetailsSnapshots, targetDecomposeHistories);
            }
            if (StringUtils.isNotEmpty(decomposeDetailsSnapshots)) {
                try {
                    decomposeDetailsSnapshotMapper.batchDecomposeDetailsSnapshot(decomposeDetailsSnapshots);
                } catch (Exception e) {
                    throw new ServiceException("插入历史目标详情表失败");
                }
            }
            List<Long> collect = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getTargetDecomposeId).collect(Collectors.toList());
            List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeIds(collect);
            if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
                for (int i = 0; i < targetDecomposeDetailsDTOList.size(); i++) {
                    //插入历史目标分解详情周期集合
                    this.pacgkDetailCyclesSnapshot(targetDecomposeDetailsDTOList.get(i), detailCyclesSnapshots, decomposeDetailsSnapshots);
                }
            }
            if (StringUtils.isNotEmpty(detailCyclesSnapshots)) {
                try {
                    detailCyclesSnapshotMapper.batchDetailCyclesSnapshot(detailCyclesSnapshots);
                } catch (Exception e) {
                    throw new ServiceException("插入历史目标详情周期表失败");
                }
            }
        }


    }

    /**
     * 封装历史目标分解详情周期集合
     *
     * @param detailCyclesSnapshots
     * @param decomposeDetailsSnapshots
     */
    private void pacgkDetailCyclesSnapshot(TargetDecomposeDetailsDTO targetDecomposeDetailsDTO, List<DetailCyclesSnapshot> detailCyclesSnapshots, List<DecomposeDetailsSnapshot> decomposeDetailsSnapshots) {
        //详情快照表不能为空
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshots)) {
            for (DecomposeDetailsSnapshot decomposeDetailsSnapshot : decomposeDetailsSnapshots) {
                List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
                if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOList)) {
                    for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                        DetailCyclesSnapshot detailCyclesSnapshot = new DetailCyclesSnapshot();
                        BeanUtils.copyProperties(decomposeDetailCyclesDTO, detailCyclesSnapshot);
                        //目标分解详情快照ID
                        detailCyclesSnapshot.setDecomposeDetailsSnapshotId(decomposeDetailsSnapshot.getDecomposeDetailsSnapshotId());
                        detailCyclesSnapshot.setCreateBy(SecurityUtils.getUserId());
                        detailCyclesSnapshot.setCreateTime(DateUtils.getNowDate());
                        detailCyclesSnapshot.setUpdateTime(DateUtils.getNowDate());
                        detailCyclesSnapshot.setUpdateBy(SecurityUtils.getUserId());
                        detailCyclesSnapshot.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        detailCyclesSnapshots.add(detailCyclesSnapshot);
                    }
                }
            }
        }

    }

    /**
     * 历史目标分解详情集合
     *
     * @param targetDecomposeDTO
     * @param decomposeDetailsSnapshots
     */
    private void packDecomposeDetailsSnapshot(TargetDecomposeDTO targetDecomposeDTO, List<DecomposeDetailsSnapshot> decomposeDetailsSnapshots, List<TargetDecomposeHistory> targetDecomposeHistories) {
        //历史主表数据不能为空
        if (StringUtils.isNotEmpty(targetDecomposeHistories)){
            //循环添加
            for (TargetDecomposeHistory targetDecomposeHistory : targetDecomposeHistories) {
                List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
                if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        DecomposeDetailsSnapshot decomposeDetailsSnapshot = new DecomposeDetailsSnapshot();
                        BeanUtils.copyProperties(targetDecomposeDetailsDTO, decomposeDetailsSnapshot);
                        //目标分解历史版本ID
                        decomposeDetailsSnapshot.setTargetDecomposeHistoryId(targetDecomposeHistory.getTargetDecomposeHistoryId());
                        decomposeDetailsSnapshot.setCreateBy(SecurityUtils.getUserId());
                        decomposeDetailsSnapshot.setCreateTime(DateUtils.getNowDate());
                        decomposeDetailsSnapshot.setUpdateTime(DateUtils.getNowDate());
                        decomposeDetailsSnapshot.setUpdateBy(SecurityUtils.getUserId());
                        decomposeDetailsSnapshot.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                        decomposeDetailsSnapshots.add(decomposeDetailsSnapshot);
                    }
                }
            }
            }
    }

    /**
     * 封装历史目标分解集合
     *
     * @param targetDecomposeDTO
     * @return
     */
    private void packTargetDecomposeHistory(TargetDecomposeDTO targetDecomposeDTO, List<TargetDecomposeHistory> targetDecomposeHistories, int i) {
        String verNum = null;
        //查询是否有生成之前的历史数据 取得版本号
        List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDTOS = targetDecomposeHistoryMapper.selectTargetDecomposeHistoryByTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
        if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)){
            TargetDecomposeHistoryDTO targetDecomposeHistoryDTO = targetDecomposeHistoryDTOS.get(targetDecomposeHistoryDTOS.size() - 1);
            String version = targetDecomposeHistoryDTO.getVersion();
            String substring = version.substring(1, 2);
            int veri = Integer.parseInt(substring);
            verNum = String.valueOf(veri+1);
        }

        if (StringUtils.isEmpty(targetDecomposeHistoryDTOS)){
            String forecastCycleFlag = this.packForecastCycleFlag(targetDecomposeDTO);
            int versionFlag = Integer.parseInt(forecastCycleFlag);
            if (StringUtils.equals(forecastCycleFlag,"下半年")){
                this.packTargetDecomposeHistoryYear(targetDecomposeDTO,verNum,targetDecomposeHistories);
            }else if (versionFlag > 1){
                this.packTargetDecomposeHistoryNum(targetDecomposeDTO,verNum,targetDecomposeHistories,versionFlag);
            }
        }else {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            //目标分解id
            targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            if (verNum != null){
                //版本号
                targetDecomposeHistory.setVersion("V" + verNum + ".0");
            }else {
                //版本号
                targetDecomposeHistory.setVersion("V1.0");
            }
            //预测周期
            targetDecomposeHistory.setForecastCycle(this.packForecastCycle(targetDecomposeDTO));
            targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeHistories.add(targetDecomposeHistory);
        }
    }

    /**
     * 封装补偿月 季度 周数据
     * @param targetDecomposeDTO
     * @param verNum
     * @param targetDecomposeHistories
     * @param versionFlag
     */
    private void packTargetDecomposeHistoryNum(TargetDecomposeDTO targetDecomposeDTO, String verNum, List<TargetDecomposeHistory> targetDecomposeHistories, int versionFlag) {
        if (verNum != null){
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            //目标分解id
            targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            //版本号
            targetDecomposeHistory.setVersion("V" + verNum + ".0");
            //预测周期
            targetDecomposeHistory.setForecastCycle(this.packForecastCycle(targetDecomposeDTO));
            targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeHistories.add(targetDecomposeHistory);
        }else {
            for (int i = 1; i <= versionFlag; i++) {
                TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
                //目标分解id
                targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
                //版本号
                targetDecomposeHistory.setVersion("V"+i+".0");
                //预测周期
                targetDecomposeHistory.setForecastCycle(Convert.int2chineseNum(i)+this.packForecastCycleType(targetDecomposeDTO));
                targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
                targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
                targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
                targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
                targetDecomposeHistory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                targetDecomposeHistories.add(targetDecomposeHistory);
            }
        }
    }

    /**
     * 封装补偿半年数据
     * @param targetDecomposeDTO
     * @param verNum
     * @param targetDecomposeHistories
     */
    private void packTargetDecomposeHistoryYear(TargetDecomposeDTO targetDecomposeDTO,String verNum,List<TargetDecomposeHistory> targetDecomposeHistories) {
        if (verNum != null){
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            //目标分解id
            targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            //版本号
            targetDecomposeHistory.setVersion("V" + verNum + ".0");
            //预测周期
            targetDecomposeHistory.setForecastCycle(this.packForecastCycle(targetDecomposeDTO));
            targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeHistories.add(targetDecomposeHistory);
        }else {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            //目标分解id
            targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            //版本号
            targetDecomposeHistory.setVersion("V1.0");
            //预测周期
            targetDecomposeHistory.setForecastCycle("上半年");
            targetDecomposeHistory.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setCreateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistory.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeHistories.add(targetDecomposeHistory);

            TargetDecomposeHistory targetDecomposeHistory2 = new TargetDecomposeHistory();
            //目标分解id
            targetDecomposeHistory2.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            //版本号
            targetDecomposeHistory2.setVersion("V2.0");
            //预测周期
            targetDecomposeHistory2.setForecastCycle("下半年");
            targetDecomposeHistory2.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeHistory2.setCreateTime(DateUtils.getNowDate());
            targetDecomposeHistory2.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory2.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistory2.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeHistories.add(targetDecomposeHistory2);
        }


    }

    /**
     * 返回预测周期字段
     *
     * @param targetDecomposeDTO
     * @return
     */
    private String packForecastCycle(TargetDecomposeDTO targetDecomposeDTO) {
        String forecastCycle = null;
        if (targetDecomposeDTO.getTimeDimension() == 1) {
            int year = DateUtils.getYear();
            forecastCycle = year + "年";
        } else if (targetDecomposeDTO.getTimeDimension() == 2) {
            if (DateUtils.getMonth() <= 6) {
                forecastCycle = "上半年";
            } else {
                forecastCycle = "下半年";
            }
        } else if (targetDecomposeDTO.getTimeDimension() == 3) {
            forecastCycle = Convert.int2chineseNum(DateUtils.getQuarter()) + "季度";
        } else if (targetDecomposeDTO.getTimeDimension() == 4) {
            forecastCycle = Convert.int2chineseNum(DateUtils.getMonth()) + "月";
        } else if (targetDecomposeDTO.getTimeDimension() == 5) {
            forecastCycle = Convert.int2chineseNum(DateUtils.getDayOfWeek()) + "周";
        }
        return forecastCycle;
    }

    /**
     * 返回预测周期是那种类型
     *
     * @param targetDecomposeDTO
     * @return
     */
    private String packForecastCycleType(TargetDecomposeDTO targetDecomposeDTO) {
        String forecastCycle = null;
         if (targetDecomposeDTO.getTimeDimension() == 3) {
            forecastCycle = "季度";
        } else if (targetDecomposeDTO.getTimeDimension() == 4) {
            forecastCycle = "月";
        } else if (targetDecomposeDTO.getTimeDimension() == 5) {
            forecastCycle = "周";
        }
        return forecastCycle;
    }
    /**
     * 返回预测周期判断是否要生成之前的历史数据
     *
     * @param targetDecomposeDTO
     * @return
     */
    private String packForecastCycleFlag(TargetDecomposeDTO targetDecomposeDTO) {
        String forecastCycle = null;
        if (targetDecomposeDTO.getTimeDimension() == 2) {
            if (DateUtils.getMonth() <= 6) {
                forecastCycle = "上半年";
            } else {
                forecastCycle = "下半年";
            }
        } else if (targetDecomposeDTO.getTimeDimension() == 3) {
            forecastCycle = String.valueOf(DateUtils.getQuarter());
        } else if (targetDecomposeDTO.getTimeDimension() == 4) {
            forecastCycle = String.valueOf(DateUtils.getMonth());
        } else if (targetDecomposeDTO.getTimeDimension() == 5) {
            forecastCycle = String.valueOf(DateUtils.getDayOfWeek());
        }
        return forecastCycle;
    }
}

