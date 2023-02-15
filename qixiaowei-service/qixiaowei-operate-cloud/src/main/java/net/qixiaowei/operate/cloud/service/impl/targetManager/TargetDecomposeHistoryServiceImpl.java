package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.message.BusinessSubtype;
import net.qixiaowei.integration.common.enums.targetManager.TargetDecomposeDimensionCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.text.Convert;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.message.api.dto.backlog.BacklogSendDTO;
import net.qixiaowei.message.api.remote.backlog.RemoteBacklogService;
import net.qixiaowei.operate.cloud.api.domain.targetManager.DecomposeDetailsSnapshot;
import net.qixiaowei.operate.cloud.api.domain.targetManager.DetailCyclesSnapshot;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeDimension;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeHistory;
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeHistoryExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.*;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeHistoryService;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.dto.basic.IndustryDTO;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndustryService;
import net.qixiaowei.system.manage.api.remote.system.RemoteRegionService;
import net.qixiaowei.system.manage.api.remote.user.RemoteUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.*;
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
    private RemoteIndicatorService remoteIndicatorService;

    @Autowired
    private TargetDecomposeMapper targetDecomposeMapper;
    @Autowired
    private DecomposeDetailCyclesMapper decomposeDetailCyclesMapper;
    @Autowired
    private TargetDecomposeDetailsMapper targetDecomposeDetailsMapper;
    @Autowired
    private TargetDecomposeDimensionMapper targetDecomposeDimensionMapper;
    @Autowired
    private RemoteEmployeeService remoteEmployeeService;
    @Autowired
    private RemoteDepartmentService remoteDepartmentService;
    @Autowired
    private RemoteIndustryService remoteIndustryService;
    @Autowired
    private RemoteRegionService remoteRegionService;
    @Autowired
    private RemoteBacklogService remoteBacklogService;
    @Autowired
    private RemoteEmployeeService employeeService;
    @Autowired
    private RemoteUserService userService;

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
        //指标远程调用
        if (StringUtils.isNotNull(targetDecomposeHistoryDTO)) {
            TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeHistoryDTO.getTargetDecomposeId());
            R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
            IndicatorDTO data = indicatorDTOR.getData();
            if (StringUtils.isNotNull(data)) {
                targetDecomposeHistoryDTO.setIndicatorName(data.getIndicatorName());
            }
        }

        if (StringUtils.isNull(targetDecomposeHistoryDTO)) {
            throw new ServiceException("数据不存在！");
        } else {
            TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeHistoryDTO.getTargetDecomposeId());
            if (StringUtils.isNotNull(targetDecomposeDTO)) {
                R<IndicatorDTO> R = remoteIndicatorService.selectIndicatorById(targetDecomposeDTO.getIndicatorId(), SecurityConstants.INNER);
                IndicatorDTO data = R.getData();
                if (StringUtils.isNotNull(data)) {
                    targetDecomposeDTO.setIndicatorName(data.getIndicatorName());
                }
                this.packDecompositionHistoryDimension(targetDecomposeDTO, targetDecomposeHistoryDTO);
                String forecastCycle = this.packForecastCycle(targetDecomposeDTO);
                targetDecomposeHistoryDTO.setForecastCycle(forecastCycle);
            }

        }
        List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS = decomposeDetailsSnapshotMapper.selectDecomposeDetailsSnapshotByTargetDecomposeHistoryId(targetDecomposeHistoryId);
        this.packHistoryRemote(decomposeDetailsSnapshotDTOS);
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)) {
            for (DecomposeDetailsSnapshotDTO decomposeDetailsSnapshotDTO : decomposeDetailsSnapshotDTOS) {
                //年度预测值
                BigDecimal forecastYear = new BigDecimal("0");
                //累计实际值
                BigDecimal actualTotal = new BigDecimal("0");
                //目标完成率
                BigDecimal targetPercentageComplete = new BigDecimal("0");
                List<DetailCyclesSnapshotDTO> detailCyclesSnapshotDTOS = new ArrayList<>();
                //周期表数据
                detailCyclesSnapshotDTOS = detailCyclesSnapshotMapper.selectDetailCyclesSnapshotByDecomposeDetailsSnapshotId(decomposeDetailsSnapshotDTO.getDecomposeDetailsSnapshotId());
                for (DetailCyclesSnapshotDTO detailCyclesSnapshotDTO : detailCyclesSnapshotDTOS) {
                    if (null != detailCyclesSnapshotDTO.getCycleForecast() && detailCyclesSnapshotDTO.getCycleForecast().compareTo(BigDecimal.ZERO) != 0) {
                        //预测值
                        forecastYear = forecastYear.add(detailCyclesSnapshotDTO.getCycleForecast());
                    }
                    if (null != detailCyclesSnapshotDTO.getCycleActual() && detailCyclesSnapshotDTO.getCycleActual().compareTo(BigDecimal.ZERO) != 0) {
                        //实际值
                        actualTotal = actualTotal.add(detailCyclesSnapshotDTO.getCycleActual());
                    }

                }
                if (null != targetPercentageComplete && targetPercentageComplete.compareTo(BigDecimal.ZERO) != 0) {
                    //被除数 不能为0和空
                    if (null != decomposeDetailsSnapshotDTO.getDecomposeTarget() && decomposeDetailsSnapshotDTO.getDecomposeTarget().compareTo(BigDecimal.ZERO) != 0) {
                        //保留一位小数
                        targetPercentageComplete = targetPercentageComplete.divide(decomposeDetailsSnapshotDTO.getDecomposeTarget(), BigDecimal.ROUND_HALF_UP);
                    }
                }
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
     * 封装分解维度
     *
     * @param targetDecomposeDTO
     */
    private void packDecompositionHistoryDimension(TargetDecomposeDTO targetDecomposeDTO, TargetDecomposeHistoryDTO targetDecomposeHistoryDTO) {
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        targetDecomposeDimension.setTargetDecomposeDimensionId(targetDecomposeDTO.getTargetDecomposeDimensionId());

        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDTOS = targetDecomposeDimensionMapper.selectTargetDecomposeDimensionList(targetDecomposeDimension);
        StringBuilder targetDecomposeDimensionName;
        for (TargetDecomposeDimensionDTO decomposeDimensionDTO : targetDecomposeDimensionDTOS) {
            targetDecomposeDimensionName = new StringBuilder("");
            List<Map<String, String>> fileNameList = new ArrayList<>();
            String decompositionDimension = decomposeDimensionDTO.getDecompositionDimension();
            if (StringUtils.isNotEmpty(decompositionDimension)) {
                for (String dimension : decompositionDimension.split(",")) {
                    String info = TargetDecomposeDimensionCode.selectInfo(dimension);
                    String filedName = TargetDecomposeDimensionCode.selectFiledName(dimension);
                    String filedValue = TargetDecomposeDimensionCode.selectFiledValue(dimension);
                    TargetDecomposeDimensionCode.selectFiledName(dimension);
                    if (StringUtils.isNotNull(info)) {
                        targetDecomposeDimensionName.append(info).append("+");
                    }
                    Map<String, String> fileNameMap = new HashMap<>();
                    fileNameMap.put("label", info);
                    fileNameMap.put("value", filedName);
                    fileNameMap.put("name", filedValue);
                    fileNameList.add(fileNameMap);
                }
                String substring = targetDecomposeDimensionName.substring(0, targetDecomposeDimensionName.length() - 1);
                targetDecomposeHistoryDTO.setFileNameList(fileNameList);
                targetDecomposeHistoryDTO.setDecompositionDimension(substring);
            }
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

    @Override
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

    @Override
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
    @Transactional
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


            if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList) && StringUtils.isNotEmpty(decomposeDetailsSnapshots)) {
                for (int i = 0; i < targetDecomposeDetailsDTOList.size(); i++) {
                    //插入历史目标分解详情周期集合
                    this.pacgkDetailCyclesSnapshot(targetDecomposeDetailsDTOList.get(i), detailCyclesSnapshots, decomposeDetailsSnapshots, i);
                }
            }
            if (StringUtils.isNotEmpty(detailCyclesSnapshots)) {
                try {
                    detailCyclesSnapshotMapper.batchDetailCyclesSnapshot(detailCyclesSnapshots);
                } catch (Exception e) {
                    throw new ServiceException("插入历史目标详情周期表失败");
                }
            }
            //人员id集合滚动预测负责人
            List<Long> principalEmployeeIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getPrincipalEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            List<Long> createBys = targetDecomposeDTOS.stream().map(TargetDecomposeDTO::getCreateBy).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            List<UserDTO> userDTOS = getUserByCreateBys(createBys);
            List<EmployeeDTO> employeeDTOS = getEmployeeDTOS(principalEmployeeIdCollect);
            for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDTOS) {
                if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        sendMessage(targetDecomposeDetailsDTO, targetDecomposeDTO, employeeDTOS, userDTOS);
                    }
                }
            }
        }
    }

    /**
     * 发送消息
     *
     * @param targetDecomposeDetailsDTO 目标分解详情
     * @param targetDecomposeDTO        主表DTO
     * @param employeeDTOS              员工列表
     * @param userDTOS                  用户列表
     */
    private void sendMessage(TargetDecomposeDetailsDTO targetDecomposeDetailsDTO, TargetDecomposeDTO targetDecomposeDTO,
                             List<EmployeeDTO> employeeDTOS, List<UserDTO> userDTOS) {
        if (StringUtils.isNotEmpty(employeeDTOS)) {
            for (EmployeeDTO employeeDTO : employeeDTOS) {
                if (StringUtils.isNull(employeeDTO.getUserId())) {
                    continue;
                }
                if (targetDecomposeDetailsDTO.getPrincipalEmployeeId().equals(employeeDTO.getEmployeeId())) {
                    UserDTO user = new UserDTO();
                    for (UserDTO userDTO : userDTOS) {
                        if (targetDecomposeDTO.getCreateBy().equals(userDTO.getUserId())) {
                            user = userDTO;
                            break;
                        }
                    }
                    // 发送待办
                    BacklogSendDTO backlogSendDTO = new BacklogSendDTO();
                    backlogSendDTO.setBusinessType(BusinessSubtype.ROLLING_PREDICTION_MANAGE_BACKLOG.getParentBusinessType().getCode());
                    backlogSendDTO.setBusinessSubtype(BusinessSubtype.ROLLING_PREDICTION_MANAGE_BACKLOG.getCode());
                    backlogSendDTO.setBusinessId(targetDecomposeDTO.getTargetDecomposeId());
                    backlogSendDTO.setUserId(employeeDTO.getUserId());
                    backlogSendDTO.setBacklogInitiator(user.getUserId());
                    backlogSendDTO.setBacklogInitiatorName(user.getEmployeeName());
                    backlogSendDTO.setBacklogName("滚动预测");
                    R<?> insertBacklog = remoteBacklogService.add(backlogSendDTO, SecurityConstants.INNER);
                    if (R.SUCCESS != insertBacklog.getCode()) {
                        throw new ServiceException("申请域名通知失败");
                    }
                    break;
                }
            }
        }
    }

    /**
     * 根据创建人获取用户信息
     *
     * @param createBys 创建人集合
     * @return List
     */
    private List<UserDTO> getUserByCreateBys(List<Long> createBys) {
        Set<Long> createBySets = new HashSet<>(createBys);
        R<List<UserDTO>> usersByUserIds = userService.getUsersByUserIds(createBySets, SecurityConstants.INNER);
        List<UserDTO> userDTOS = usersByUserIds.getData();
        if (usersByUserIds.getCode() != 200) {
            throw new ServiceException("远程访问用户信息失败");
        }
        if (StringUtils.isEmpty(userDTOS)) {
            throw new ServiceException("部分用户信息已被删除 请检查用户配置");
        }
        return userDTOS;
    }

    /**
     * 获取用户信息
     *
     * @return userDTO
     */
    private List<EmployeeDTO> getEmployeeDTOS(List<Long> principalEmployeeIdCollect) {
        R<List<EmployeeDTO>> listR = employeeService.selectByEmployeeIds(principalEmployeeIdCollect, SecurityConstants.INNER);
        List<EmployeeDTO> employeeDTOS = listR.getData();
        if (listR.getCode() != 200) {
            throw new ServiceException("远程访问用户信息失败");
        }
        if (StringUtils.isEmpty(employeeDTOS)) {
            throw new ServiceException("当前滚动预测负责人已被删除 请检查员工配置");
        }
        return employeeDTOS;
    }


    /**
     * 封装历史目标分解详情周期集合
     *
     * @param targetDecomposeDetailsDTO
     * @param detailCyclesSnapshots
     * @param decomposeDetailsSnapshots
     * @param i
     */
    private void pacgkDetailCyclesSnapshot(TargetDecomposeDetailsDTO targetDecomposeDetailsDTO, List<DetailCyclesSnapshot> detailCyclesSnapshots, List<DecomposeDetailsSnapshot> decomposeDetailsSnapshots, int i) {
        //详情快照表不能为空
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshots)) {
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId());
            if (StringUtils.isNotEmpty(decomposeDetailCyclesDTOList)) {
                for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                    DetailCyclesSnapshot detailCyclesSnapshot = new DetailCyclesSnapshot();
                    BeanUtils.copyProperties(decomposeDetailCyclesDTO, detailCyclesSnapshot);
                    //目标分解详情快照ID
                    detailCyclesSnapshot.setDecomposeDetailsSnapshotId(decomposeDetailsSnapshots.get(i).getDecomposeDetailsSnapshotId());
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

    /**
     * 历史目标分解详情集合
     *
     * @param targetDecomposeDTO
     * @param decomposeDetailsSnapshots
     */
    private void packDecomposeDetailsSnapshot(TargetDecomposeDTO targetDecomposeDTO, List<DecomposeDetailsSnapshot> decomposeDetailsSnapshots, List<TargetDecomposeHistory> targetDecomposeHistories) {
        //历史主表数据不能为空
        if (StringUtils.isNotEmpty(targetDecomposeHistories)) {
            //循环添加
            for (TargetDecomposeHistory targetDecomposeHistory : targetDecomposeHistories) {
                if (targetDecomposeHistory.getTargetDecomposeId().equals(targetDecomposeDTO.getTargetDecomposeId())) {
                    List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
                    this.packRemote(targetDecomposeDetailsDTOList);
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
    }

    /**
     * 封装远程调用数据
     *
     * @param targetDecomposeDetailsDTOList
     * @return
     */
    public void packRemote(List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList) {
        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            //人员id集合
            List<Long> employeeIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getEmployeeId).collect(Collectors.toList());

            //人员id集合滚动预测负责人
            List<Long> principalEmployeeIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getPrincipalEmployeeId).distinct().filter(p -> p != null).collect(Collectors.toList());

            //部门id集合
            List<Long> departmentIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getDepartmentId).distinct().filter(d -> d != null).collect(Collectors.toList());

            //省份id集合
            Set<Long> regionIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getRegionId).distinct().filter(r -> r != null).collect(Collectors.toSet());

            //行业id集合
            List<Long> industryIdCollect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getIndustryId).distinct().filter(x -> x != null).collect(Collectors.toList());
            //人员远程
            if (StringUtils.isNotEmpty(employeeIdCollect)) {
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(employeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (EmployeeDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getEmployeeId().equals(datum.getEmployeeId())) {
                                targetDecomposeDetailsDTO.setEmployeeId(datum.getEmployeeId());
                                targetDecomposeDetailsDTO.setEmployeeName(datum.getEmployeeName());
                            }
                        }
                    }
                }
            }
            //人员远程
            if (StringUtils.isNotEmpty(principalEmployeeIdCollect)) {
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(principalEmployeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (EmployeeDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getPrincipalEmployeeId().equals(datum.getEmployeeId())) {
                                targetDecomposeDetailsDTO.setPrincipalEmployeeId(datum.getEmployeeId());
                                targetDecomposeDetailsDTO.setPrincipalEmployeeName(datum.getEmployeeName());
                            }
                        }
                    }
                }
            }
            //部门远程
            if (StringUtils.isNotEmpty(departmentIdCollect)) {
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(departmentIdCollect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (DepartmentDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                targetDecomposeDetailsDTO.setDepartmentId(datum.getDepartmentId());
                                targetDecomposeDetailsDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }
            //省份远程
            if (StringUtils.isNotEmpty(regionIdCollect)) {
                R<List<RegionDTO>> regionsByIds = remoteRegionService.getRegionsByIds(regionIdCollect, SecurityConstants.INNER);
                List<RegionDTO> data = regionsByIds.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (RegionDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getRegionId().equals(datum.getRegionId())) {
                                targetDecomposeDetailsDTO.setRegionId(datum.getRegionId());
                                targetDecomposeDetailsDTO.setRegionName(datum.getRegionName());
                            }
                        }
                    }
                }
            }
            //行业远程
            if (StringUtils.isNotEmpty(industryIdCollect)) {
                R<List<IndustryDTO>> listR = remoteIndustryService.selectByIds(industryIdCollect, SecurityConstants.INNER);
                List<IndustryDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                        for (IndustryDTO datum : data) {
                            if (targetDecomposeDetailsDTO.getIndustryId().equals(datum.getIndustryId())) {
                                targetDecomposeDetailsDTO.setIndustryId(datum.getIndustryId());
                                targetDecomposeDetailsDTO.setIndustryName(datum.getIndustryName());
                            }
                        }
                    }

                }

            }
        }
    }

    /**
     * 封装分解快照数据远程调用数据
     *
     * @param decomposeDetailsSnapshotDTOS
     * @return
     */
    public void packHistoryRemote(List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS) {
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)) {
            //人员id集合
            List<Long> employeeIdCollect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            //人员id集合滚动预测负责人
            List<Long> principalEmployeeIdCollect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getPrincipalEmployeeId).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            //部门id集合
            List<Long> departmentIdCollect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getDepartmentId).distinct().filter(Objects::nonNull).collect(Collectors.toList());

            //省份id集合
            Set<Long> regionIdCollect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getRegionId).distinct().filter(Objects::nonNull).collect(Collectors.toSet());

            //行业id集合
            List<Long> industryIdCollect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getIndustryId).distinct().filter(Objects::nonNull).collect(Collectors.toList());
            //人员远程
            if (StringUtils.isNotEmpty(employeeIdCollect)) {
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(employeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (DecomposeDetailsSnapshotDTO decomposeDetailsSnapshotDTO : decomposeDetailsSnapshotDTOS) {
                        for (EmployeeDTO datum : data) {
                            if (decomposeDetailsSnapshotDTO.getEmployeeId().equals(datum.getEmployeeId())) {
                                decomposeDetailsSnapshotDTO.setEmployeeId(datum.getEmployeeId());
                                decomposeDetailsSnapshotDTO.setEmployeeName(datum.getEmployeeName());
                            }
                        }
                    }
                }
            }
            //人员远程
            if (StringUtils.isNotEmpty(principalEmployeeIdCollect)) {
                R<List<EmployeeDTO>> listR = remoteEmployeeService.selectByEmployeeIds(principalEmployeeIdCollect, SecurityConstants.INNER);
                List<EmployeeDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (DecomposeDetailsSnapshotDTO decomposeDetailsSnapshotDTO : decomposeDetailsSnapshotDTOS) {
                        for (EmployeeDTO datum : data) {
                            if (decomposeDetailsSnapshotDTO.getPrincipalEmployeeId().equals(datum.getEmployeeId())) {
                                decomposeDetailsSnapshotDTO.setPrincipalEmployeeId(datum.getEmployeeId());
                                decomposeDetailsSnapshotDTO.setPrincipalEmployeeName(datum.getEmployeeName());
                            }
                        }
                    }
                }
            }
            //部门远程
            if (StringUtils.isNotEmpty(departmentIdCollect)) {
                R<List<DepartmentDTO>> listR = remoteDepartmentService.selectdepartmentIds(departmentIdCollect, SecurityConstants.INNER);
                List<DepartmentDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (DecomposeDetailsSnapshotDTO decomposeDetailsSnapshotDTO : decomposeDetailsSnapshotDTOS) {
                        for (DepartmentDTO datum : data) {
                            if (decomposeDetailsSnapshotDTO.getDepartmentId().equals(datum.getDepartmentId())) {
                                decomposeDetailsSnapshotDTO.setDepartmentId(datum.getDepartmentId());
                                decomposeDetailsSnapshotDTO.setDepartmentName(datum.getDepartmentName());
                            }
                        }
                    }
                }
            }
            //省份远程
            if (StringUtils.isNotEmpty(regionIdCollect)) {
                R<List<RegionDTO>> regionsByIds = remoteRegionService.getRegionsByIds(regionIdCollect, SecurityConstants.INNER);
                List<RegionDTO> data = regionsByIds.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (DecomposeDetailsSnapshotDTO decomposeDetailsSnapshotDTO : decomposeDetailsSnapshotDTOS) {
                        for (RegionDTO datum : data) {
                            if (decomposeDetailsSnapshotDTO.getRegionId().equals(datum.getRegionId())) {
                                decomposeDetailsSnapshotDTO.setRegionId(datum.getRegionId());
                                decomposeDetailsSnapshotDTO.setRegionName(datum.getRegionName());
                            }
                        }
                    }
                }
            }
            //行业远程
            if (StringUtils.isNotEmpty(industryIdCollect)) {
                R<List<IndustryDTO>> listR = remoteIndustryService.selectByIds(industryIdCollect, SecurityConstants.INNER);
                List<IndustryDTO> data = listR.getData();
                if (StringUtils.isNotEmpty(data)) {
                    for (DecomposeDetailsSnapshotDTO decomposeDetailsSnapshotDTO : decomposeDetailsSnapshotDTOS) {
                        for (IndustryDTO datum : data) {
                            if (decomposeDetailsSnapshotDTO.getIndustryId().equals(datum.getIndustryId())) {
                                decomposeDetailsSnapshotDTO.setIndustryId(datum.getIndustryId());
                                decomposeDetailsSnapshotDTO.setIndustryName(datum.getIndustryName());
                            }
                        }
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
        if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)) {
            String version1 = targetDecomposeHistoryDTOS.get(targetDecomposeHistoryDTOS.size() - 1).getVersion();
            if (StringUtils.equals(String.valueOf(DateUtils.getMonth() > 1 ? (DateUtils.getMonth() - 1) : DateUtils.getMonth()), version1.substring(0, version1.indexOf(".")).replaceAll("V", ""))) {
                return;
            }
            //删除重复数据
            List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDataDelete = new ArrayList<>();
            //分组数据 判断是否重复
            Map<String, List<TargetDecomposeHistoryDTO>> targetDecomposeHistoryMap = targetDecomposeHistoryDTOS.parallelStream().collect(Collectors.groupingBy(TargetDecomposeHistoryDTO::getVersion));
            for (String key : targetDecomposeHistoryMap.keySet()) {
                List<TargetDecomposeHistoryDTO> targetDecomposeHistoryData = targetDecomposeHistoryMap.get(key);
                if (StringUtils.isNotEmpty(targetDecomposeHistoryData)) {
                    for (int i1 = 0; i1 < targetDecomposeHistoryData.size(); i1++) {
                        if (targetDecomposeHistoryData.size() > 1 && i1 < targetDecomposeHistoryData.size() - 1) {
                            targetDecomposeHistoryData.get(i1).setDeleteFlag(1);
                            targetDecomposeHistoryDataDelete.add(targetDecomposeHistoryData.get(i1));
                        }
                    }
                }
            }
            if (StringUtils.isNotEmpty(targetDecomposeHistoryDataDelete)) {
                List<Long> targetDecomposeHistoryIds = targetDecomposeHistoryDataDelete.stream().map(TargetDecomposeHistoryDTO::getTargetDecomposeHistoryId).filter(Objects::nonNull).collect(Collectors.toList());
                //删除重复数据
                List<TargetDecomposeHistory> targetDecomposeHistoryList = new ArrayList<>();
                for (TargetDecomposeHistoryDTO targetDecomposeHistoryDTO : targetDecomposeHistoryDataDelete) {
                    TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
                    BeanUtils.copyProperties(targetDecomposeHistoryDTO, targetDecomposeHistory);
                    targetDecomposeHistoryList.add(targetDecomposeHistory);
                }
                try {
                    targetDecomposeHistoryMapper.updateTargetDecomposeHistorys(targetDecomposeHistoryList);
                } catch (Exception e) {
                    throw new ServiceException("删除历史目标分解重复数据失败");
                }

                if (StringUtils.isNotEmpty(targetDecomposeHistoryIds)) {
                    List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS = decomposeDetailsSnapshotMapper.selectDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(targetDecomposeHistoryIds);
                    if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)) {
                        List<Long> decomposeDetailsSnapshotIds = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getDecomposeDetailsSnapshotId).filter(Objects::nonNull).collect(Collectors.toList());
                        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotIds)) {
                            try {
                                decomposeDetailsSnapshotMapper.logicDeleteDecomposeDetailsSnapshotByDecomposeDetailsSnapshotIds(decomposeDetailsSnapshotIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                            } catch (Exception e) {
                                throw new ServiceException("删除历史目标分解详情重复数据失败");
                            }
                            try {
                                detailCyclesSnapshotMapper.logicDeleteDetailCyclesSnapshotByDecomposeDetailsSnapshotIds(decomposeDetailsSnapshotIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
                            } catch (Exception e) {
                                throw new ServiceException("删除历史目标分解周期重复数据失败");
                            }
                        }

                    }
                }
            }
            //删除重复数据
            targetDecomposeHistoryDTOS.removeAll(targetDecomposeHistoryDataDelete);
        }

        if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)) {
            TargetDecomposeHistoryDTO targetDecomposeHistoryDTO = targetDecomposeHistoryDTOS.get(targetDecomposeHistoryDTOS.size() - 1);
            String version = targetDecomposeHistoryDTO.getVersion();
            String substring = version.substring(0, version.indexOf(".")).replaceAll("V", "");
            int veri = Integer.parseInt(substring);
            verNum = String.valueOf(veri + 1);
        }

        if (StringUtils.isEmpty(targetDecomposeHistoryDTOS)) {
            String forecastCycleFlag = this.packForecastCycleFlag(targetDecomposeDTO);
            if (StringUtils.equals(forecastCycleFlag, "下半年")) {
                this.packTargetDecomposeHistoryHalfYear(targetDecomposeDTO, verNum, targetDecomposeHistories);
            } else if (StringUtils.equals(forecastCycleFlag, "年度")) {
                this.packTargetDecomposeHistoryYear(targetDecomposeDTO, verNum, targetDecomposeHistories);
            } else if (!StringUtils.equals(forecastCycleFlag, "下半年") && !StringUtils.equals(forecastCycleFlag, "上半年") && !StringUtils.equals(forecastCycleFlag, "年度")) {
                int versionFlag = Integer.parseInt(forecastCycleFlag);
                if (versionFlag >= 1) {
                    this.packTargetDecomposeHistoryNum(targetDecomposeDTO, verNum, targetDecomposeHistories, versionFlag);
                }
            }
        } else {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            //目标分解id
            targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            if (verNum != null) {
                //版本号
                targetDecomposeHistory.setVersion("V" + verNum + ".0");
            } else {
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
     *
     * @param targetDecomposeDTO
     * @param verNum
     * @param targetDecomposeHistories
     * @param versionFlag
     */
    private void packTargetDecomposeHistoryNum(TargetDecomposeDTO targetDecomposeDTO, String verNum, List<TargetDecomposeHistory> targetDecomposeHistories, int versionFlag) {
        if (verNum != null) {
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
        } else {
            for (int i = 1; i <= versionFlag; i++) {
                TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
                //目标分解id
                targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
                //版本号
                targetDecomposeHistory.setVersion("V" + i + ".0");
                if (targetDecomposeDTO.getTimeDimension() == 3) {
                    //预测周期
                    targetDecomposeHistory.setForecastCycle(Convert.int2chineseNum(i) + this.packForecastCycleType(targetDecomposeDTO));
                } else {
                    //预测周期
                    targetDecomposeHistory.setForecastCycle(i + this.packForecastCycleType(targetDecomposeDTO));
                }

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
     *
     * @param targetDecomposeDTO
     * @param verNum
     * @param targetDecomposeHistories
     */
    private void packTargetDecomposeHistoryHalfYear(TargetDecomposeDTO targetDecomposeDTO, String verNum, List<TargetDecomposeHistory> targetDecomposeHistories) {
        if (verNum != null) {
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
        } else {
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
     * 封装年数据
     *
     * @param targetDecomposeDTO
     * @param verNum
     * @param targetDecomposeHistories
     */
    private void packTargetDecomposeHistoryYear(TargetDecomposeDTO targetDecomposeDTO, String verNum, List<TargetDecomposeHistory> targetDecomposeHistories) {
        if (verNum == null) {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            //目标分解id
            targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            //版本号
            targetDecomposeHistory.setVersion("V1.0");
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
            forecastCycle = DateUtils.getMonth() + "月";
        } else if (targetDecomposeDTO.getTimeDimension() == 5) {
            forecastCycle = DateUtils.getDayOfWeek() + "周";
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
        if (targetDecomposeDTO.getTimeDimension() == 1) {
            forecastCycle = "年度";
        }
        if (targetDecomposeDTO.getTimeDimension() == 2) {
            if (DateUtils.getMonth() <= 6) {
                forecastCycle = "上半年";
            } else {
                forecastCycle = "下半年";
            }
        } else if (targetDecomposeDTO.getTimeDimension() == 3) {
            forecastCycle = String.valueOf(DateUtils.getQuarter() > 1 ? (DateUtils.getQuarter() - 1) : DateUtils.getQuarter());
        } else if (targetDecomposeDTO.getTimeDimension() == 4) {
            forecastCycle = String.valueOf(DateUtils.getMonth() > 1 ? (DateUtils.getMonth() - 1) : DateUtils.getMonth());
        } else if (targetDecomposeDTO.getTimeDimension() == 5) {
            forecastCycle = String.valueOf(DateUtils.getDayOfWeek() > 1 ? (DateUtils.getDayOfWeek() - 1) : DateUtils.getDayOfWeek());
        }
        return forecastCycle;
    }
}

