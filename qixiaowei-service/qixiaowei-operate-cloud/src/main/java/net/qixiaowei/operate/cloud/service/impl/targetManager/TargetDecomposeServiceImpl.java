package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.math.BigDecimal;
import java.util.Calendar;
import java.util.Comparator;
import java.util.List;

import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.basic.IndicatorCode;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.operate.cloud.api.domain.product.ProductSpecificationParam;
import net.qixiaowei.operate.cloud.api.domain.targetManager.*;
import net.qixiaowei.operate.cloud.api.dto.targetManager.*;
import net.qixiaowei.operate.cloud.mapper.targetManager.*;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteIndicatorService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.stream.Collectors;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetDecomposeExcel;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import org.springframework.transaction.annotation.Transactional;


/**
 * TargetDecomposeService业务层处理
 *
 * @author TANGMICHI
 * @since 2022-10-27
 */
@Service
public class TargetDecomposeServiceImpl implements ITargetDecomposeService {
    @Autowired
    private TargetDecomposeMapper targetDecomposeMapper;
    @Autowired
    private RemoteIndicatorService remoteIndicatorService;
    @Autowired
    private DecomposeDetailCyclesMapper decomposeDetailCyclesMapper;
    @Autowired
    private TargetDecomposeDetailsMapper targetDecomposeDetailsMapper;

    @Autowired
    private TargetDecomposeHistoryMapper targetDecomposeHistoryMapper;
    @Autowired
    private DecomposeDetailsSnapshotMapper decomposeDetailsSnapshotMapper;
    @Autowired
    private DetailCyclesSnapshotMapper detailCyclesSnapshotMapper;


    /**
     * 查询目标分解(销售订单)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    @Override
    public TargetDecomposeDTO selectOrderTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        return this.packSelectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 查询目标分解(销售收入)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    @Override
    public TargetDecomposeDTO selectIncomeTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        return this.packSelectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 查询目标分解(销售回款)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    @Override
    public TargetDecomposeDTO selectReturnedTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        return this.packSelectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 查询目标分解(自定义)表
     *
     * @param targetDecomposeId 目标分解表主键
     * @return 目标分解表
     */
    @Override
    public TargetDecomposeDTO selectCustomTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        return this.packSelectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
    }

    /**
     * 封装统一查询
     *
     * @param targetDecomposeId
     * @return
     */
    public TargetDecomposeDTO packSelectTargetDecomposeByTargetDecomposeId(Long targetDecomposeId) {
        //目标分解主表数据
        TargetDecomposeDTO targetDecomposeDTO = targetDecomposeMapper.selectTargetDecomposeByTargetDecomposeId(targetDecomposeId);
        //目标分解详情数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeId);

        if (StringUtils.isNotEmpty(targetDecomposeDetailsDTOList)) {
            for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOList) {
                //目标分解周欺数据集合
                targetDecomposeDetailsDTO.setDecomposeDetailCyclesDTOS(decomposeDetailCyclesMapper.selectDecomposeDetailCyclesByTargetDecomposeDetailsId(targetDecomposeDetailsDTO.getTargetDecomposeDetailsId()));
            }
        }

        if (StringUtils.isNull(targetDecomposeDTO)) {
            return null;
        } else {
            return targetDecomposeDTO.setTargetDecomposeDetailsDTOS(targetDecomposeDetailsDTOList);
        }

    }

    /**
     * 查询目标分解(销售订单)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 目标分解(销售订单)表
     */
    @Override
    public List<TargetDecomposeDTO> selectOrderList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.ORDER.getCode());
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.ONE);
        return targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
    }

    /**
     * 查询目标分解(销售收入)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 目标分解(销售订单)表
     */
    @Override
    public List<TargetDecomposeDTO> selectIncomeList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.EARNING.getCode());
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.TWO);
        return targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
    }

    /**
     * 查询目标分解(销售回款)表列表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 目标分解(销售回款)表
     */
    @Override
    public List<TargetDecomposeDTO> selectReturnedList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.RECEIVABLE.getCode());
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.THREE);
        return targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
    }

    /**
     * 查询目标分解(自定义)表列表
     *
     * @param targetDecomposeDTO 目标分解(自定义)表
     * @return 目标分解(自定义)表
     */
    @Override
    public List<TargetDecomposeDTO> selectCustomList(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.ZERO);
        return targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
    }

    /**
     * 新增目标分解(销售订单)表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetDecomposeDTO insertOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        TargetDecomposeDTO targetDecomposeDTO1 = targetDecomposeMapper.selectTargetDecomposeUniteId(targetDecompose);
        if (StringUtils.isNotNull(targetDecomposeDTO1)) {
            throw new ServiceException(Calendar.getInstance().get(Calendar.YEAR) + "年已创建该维度目标分解，无需重复创建。");
        }
        targetDecompose.setCreateBy(SecurityUtils.getUserId());
        targetDecompose.setCreateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //待录入
        targetDecompose.setStatus(Constants.ZERO);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.ORDER.getCode());
        if (StringUtils.isNull(indicatorDTOR.getData())) {
            throw new ServiceException("指标不存在！请联系管理员");
        }
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.ONE);
        try {
            targetDecomposeMapper.insertTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("插入目标分解主表失败");

        }
        //插入周期表和详细信息表
        this.packInsertTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 新增目标分解(销售收入)表
     *
     * @param targetDecomposeDTO 目标分解(销售收入)表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetDecomposeDTO insertIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setCreateBy(SecurityUtils.getUserId());
        targetDecompose.setCreateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //待录入
        targetDecompose.setStatus(Constants.ZERO);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.EARNING.getCode());
        if (StringUtils.isNull(indicatorDTOR.getData())) {
            throw new ServiceException("指标不存在！请联系管理员");
        }
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.TWO);
        try {
            targetDecomposeMapper.insertTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("插入目标分解主表失败");

        }
        //插入周期表和详细信息表
        this.packInsertTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 新增目标分解(销售回款)表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetDecomposeDTO insertReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setCreateBy(SecurityUtils.getUserId());
        targetDecompose.setCreateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //待录入
        targetDecompose.setStatus(Constants.ZERO);
        //远程指标code调用
        R<IndicatorDTO> indicatorDTOR = remoteIndicatorService.selectIndicatorByCode(IndicatorCode.RECEIVABLE.getCode());
        if (StringUtils.isNull(indicatorDTOR.getData())) {
            throw new ServiceException("指标不存在！请联系管理员");
        }
        //指标id
        targetDecompose.setIndicatorId(indicatorDTOR.getData().getIndicatorId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.THREE);
        try {
            targetDecomposeMapper.insertTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("插入目标分解主表失败");

        }
        //插入周期表和详细信息表
        this.packInsertTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 新增目标分解(自定义)表
     *
     * @param targetDecomposeDTO 目标分解(自定义)表
     * @return 结果
     */
    @Override
    @Transactional
    public TargetDecomposeDTO insertCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setCreateBy(SecurityUtils.getUserId());
        targetDecompose.setCreateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        //待录入
        targetDecompose.setStatus(Constants.ZERO);
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.ZERO);
        try {
            targetDecomposeMapper.insertTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("插入目标分解主表失败");

        }
        //插入周期表和详细信息表
        this.packInsertTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        targetDecomposeDTO.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        return targetDecomposeDTO;
    }

    /**
     * 校检数据
     *
     * @param targetDecomposeDTO
     */
    private void validTargetDecomposeData(TargetDecomposeDTO targetDecomposeDTO) {
        //分解目标值
        BigDecimal decomposeTarget = targetDecomposeDTO.getDecomposeTarget();
        //分解目标值比对
        BigDecimal validDecomposeTarget = new BigDecimal("0");

        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        for (TargetDecomposeDetailsDTO targetDecomposeDetailsDTO : targetDecomposeDetailsDTOS) {
            //汇总金额
            BigDecimal amountTarget = targetDecomposeDetailsDTO.getAmountTarget();
            if (null != amountTarget) {
                validDecomposeTarget = validDecomposeTarget.add(amountTarget);
            }
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOS = targetDecomposeDetailsDTO.getDecomposeDetailCyclesDTOS();
            //行的汇总金额
            BigDecimal validAmountTarget = new BigDecimal("0");
            for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOS) {
                BigDecimal cycleForecast = decomposeDetailCyclesDTO.getCycleForecast();
                if (null != cycleForecast) {
                    validAmountTarget = validAmountTarget.add(cycleForecast);
                }
            }
            if (null != amountTarget) {
                if (amountTarget.compareTo(validAmountTarget) != 0) {
                    throw new ServiceException("汇总金额与行的预测总值不匹配！！！");
                }
            }

        }
        if (null != decomposeTarget) {
            if (decomposeTarget.compareTo(validDecomposeTarget) != 0) {
                throw new ServiceException("分解目标值与汇总金额总值不匹配！！！");
            }
        }

    }

    /**
     * 封装插入目标分解详情表和目标分解详细信息周期表
     *
     * @param targetDecomposeDTO
     * @param targetDecompose
     */
    public void packInsertTargetDecomposeData(TargetDecomposeDTO targetDecomposeDTO, TargetDecompose targetDecompose) {


        //目标分解详情表集合
        List<TargetDecomposeDetails> targetDecomposeDetailsList = new ArrayList<>();
        //目标分解详细信息周期表集合
        List<DecomposeDetailCycles> decomposeDetailCyclesList = new ArrayList<>();

        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //插入目标分解详情表
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            //目标分解详情表
            TargetDecomposeDetails targetDecomposeDetails = new TargetDecomposeDetails();
            BeanUtils.copyProperties(targetDecomposeDetailsDTOS.get(i), targetDecomposeDetails);
            //目标分解id
            targetDecomposeDetails.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
            targetDecomposeDetails.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeDetails.setCreateTime(DateUtils.getNowDate());
            targetDecomposeDetails.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeDetails.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            //目标分解详情表集合
            targetDecomposeDetailsList.add(targetDecomposeDetails);
        }
        try {
            if (StringUtils.isNotEmpty(targetDecomposeDetailsList)) {
                targetDecomposeDetailsMapper.batchTargetDecomposeDetails(targetDecomposeDetailsList);
            }
        } catch (Exception e) {
            throw new ServiceException("插入目标分解详情表失败");
        }
        //插入目标分解详细信息周期表
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            int cycleNumber = 1;
            //目标分解详细信息周期
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = targetDecomposeDetailsDTOS.get(i).getDecomposeDetailCyclesDTOS();
            for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                //目标分解详细信息周期表
                DecomposeDetailCycles decomposeDetailCycles = new DecomposeDetailCycles();
                BeanUtils.copyProperties(decomposeDetailCyclesDTO, decomposeDetailCycles);
                decomposeDetailCycles.setCycleNumber(cycleNumber);
                //目标分解id
                decomposeDetailCycles.setTargetDecomposeDetailsId(targetDecomposeDetailsList.get(i).getTargetDecomposeDetailsId());
                decomposeDetailCycles.setCreateBy(SecurityUtils.getUserId());
                decomposeDetailCycles.setCreateTime(DateUtils.getNowDate());
                decomposeDetailCycles.setUpdateTime(DateUtils.getNowDate());
                decomposeDetailCycles.setUpdateBy(SecurityUtils.getUserId());
                decomposeDetailCycles.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                cycleNumber++;
                //目标分解详细信息周期表集合
                decomposeDetailCyclesList.add(decomposeDetailCycles);
            }
        }
        try {
            if (StringUtils.isNotEmpty(decomposeDetailCyclesList)) {
                decomposeDetailCyclesMapper.batchDecomposeDetailCycles(decomposeDetailCyclesList);
            }
        } catch (Exception e) {
            throw new ServiceException("插入目标分解详细信息周期表失败");
        }
    }

    /**
     * 封装修改目标分解详情表和目标分解详细信息周期表
     *
     * @param targetDecomposeDTO
     * @param targetDecompose
     */
    public void packUpdateTargetDecomposeData(TargetDecomposeDTO targetDecomposeDTO, TargetDecompose targetDecompose) {

        //新增目标分解详情表集合
        List<TargetDecomposeDetails> targetDecomposeDetailsAddList = new ArrayList<>();
        //修改目标分解详情表集合
        List<TargetDecomposeDetails> targetDecomposeDetailsUpdateList = new ArrayList<>();
        //目标分解详情表所有集合
        List<TargetDecomposeDetails> targetDecomposeDetailsAllList = new ArrayList<>();


        //新增目标分解详细信息周期表集合
        List<DecomposeDetailCycles> decomposeDetailCyclesAddList = new ArrayList<>();
        //修改目标分解详细信息周期表集合
        List<DecomposeDetailCycles> decomposeDetailCyclesUpdateList = new ArrayList<>();
        //删除修改的目标分解详情表数据
        List<Long> longs = this.packUpdateTargetDecomposeDetails(targetDecomposeDTO);
        //传入数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //插入目标分解详情表
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            //目标分解详情表
            TargetDecomposeDetails targetDecomposeDetails = new TargetDecomposeDetails();
            BeanUtils.copyProperties(targetDecomposeDetailsDTOS.get(i), targetDecomposeDetails);
            //目标分解id
            targetDecomposeDetails.setTargetDecomposeId(targetDecompose.getTargetDecomposeId());
            if (null == targetDecomposeDetailsDTOS.get(i).getTargetDecomposeDetailsId()) {
                targetDecomposeDetails.setCreateBy(SecurityUtils.getUserId());
                targetDecomposeDetails.setCreateTime(DateUtils.getNowDate());
                targetDecomposeDetails.setUpdateTime(DateUtils.getNowDate());
                targetDecomposeDetails.setUpdateBy(SecurityUtils.getUserId());
                targetDecomposeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                //新增目标分解详情表集合
                targetDecomposeDetailsAddList.add(targetDecomposeDetails);
            } else {
                targetDecomposeDetails.setUpdateBy(SecurityUtils.getUserId());
                targetDecomposeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                //修改目标分解详情表集合
                targetDecomposeDetailsUpdateList.add(targetDecomposeDetails);
            }
        }

        try {
            if (StringUtils.isNotEmpty(targetDecomposeDetailsAddList)) {
                targetDecomposeDetailsMapper.batchTargetDecomposeDetails(targetDecomposeDetailsAddList);
            }
        } catch (Exception e) {
            throw new ServiceException("插入目标分解详情表失败");
        }

        try {
            if (StringUtils.isNotEmpty(targetDecomposeDetailsUpdateList)) {
                targetDecomposeDetailsMapper.updateTargetDecomposeDetailss(targetDecomposeDetailsUpdateList);
            }
        } catch (Exception e) {
            throw new ServiceException("修改目标分解详情表失败");
        }
        targetDecomposeDetailsAllList.addAll(targetDecomposeDetailsAddList);
        targetDecomposeDetailsAllList.addAll(targetDecomposeDetailsUpdateList);
        targetDecomposeDetailsAllList.sort(Comparator.comparing(TargetDecomposeDetails::getTargetDecomposeDetailsId));
        //修改目标分解详细信息周期表
        for (int i = 0; i < targetDecomposeDetailsDTOS.size(); i++) {
            //删除不存在的数据
            if (StringUtils.isNotEmpty(longs)) {
                try {
                    decomposeDetailCyclesMapper.logicDeleteDecomposeDetailCyclesByTargetDecomposeDetailsIds(longs, SecurityUtils.getUserId(), DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除目标分解详细信息周期表失败");
                }
            }
            int cycleNumber = 1;
            //接收目标分解详细信息周期数据
            List<DecomposeDetailCyclesDTO> decomposeDetailCyclesDTOList = targetDecomposeDetailsDTOS.get(i).getDecomposeDetailCyclesDTOS();
            for (DecomposeDetailCyclesDTO decomposeDetailCyclesDTO : decomposeDetailCyclesDTOList) {
                //目标分解详细信息周期表
                DecomposeDetailCycles decomposeDetailCycles = new DecomposeDetailCycles();
                BeanUtils.copyProperties(decomposeDetailCyclesDTO, decomposeDetailCycles);
                decomposeDetailCycles.setCycleNumber(cycleNumber);
                if (null == targetDecomposeDetailsDTOS.get(i).getTargetDecomposeDetailsId()) {
                    //目标分解id
                    decomposeDetailCycles.setTargetDecomposeDetailsId(targetDecomposeDetailsAllList.get(i).getTargetDecomposeDetailsId());
                    decomposeDetailCycles.setCreateBy(SecurityUtils.getUserId());
                    decomposeDetailCycles.setCreateTime(DateUtils.getNowDate());
                    decomposeDetailCycles.setUpdateTime(DateUtils.getNowDate());
                    decomposeDetailCycles.setUpdateBy(SecurityUtils.getUserId());
                    decomposeDetailCycles.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
                    //目标分解详细信息周期表集合
                    decomposeDetailCyclesAddList.add(decomposeDetailCycles);
                } else {
                    decomposeDetailCycles.setUpdateTime(DateUtils.getNowDate());
                    decomposeDetailCycles.setUpdateBy(SecurityUtils.getUserId());
                    //目标分解详细信息周期表集合
                    decomposeDetailCyclesUpdateList.add(decomposeDetailCycles);
                }
                cycleNumber++;
            }
        }
        if (StringUtils.isNotEmpty(decomposeDetailCyclesAddList)) {
            try {
                decomposeDetailCyclesMapper.batchDecomposeDetailCycles(decomposeDetailCyclesAddList);
            } catch (Exception e) {
                throw new ServiceException("插入目标分解详细信息周期表失败");
            }
        }
        if (StringUtils.isNotEmpty(decomposeDetailCyclesUpdateList)) {
            try {
                decomposeDetailCyclesMapper.updateDecomposeDetailCycless(decomposeDetailCyclesUpdateList);
            } catch (Exception e) {
                throw new ServiceException("修改目标分解详细信息周期表失败");
            }
        }
    }

    /**
     * 删除修改的目标分解详情表数据
     *
     * @param targetDecomposeDTO
     */
    private List<Long> packUpdateTargetDecomposeDetails(TargetDecomposeDTO targetDecomposeDTO) {
        //传入数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOS = targetDecomposeDTO.getTargetDecomposeDetailsDTOS();
        //数据库已存在的数据
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());

        //sterm流求差集
        List<Long> collect = targetDecomposeDetailsDTOList.stream().filter(a ->
                !targetDecomposeDetailsDTOS.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList()).contains(a.getTargetDecomposeDetailsId())
        ).collect(Collectors.toList()).stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
        //删除数据
        if (StringUtils.isNotEmpty(collect)) {
            try {
                targetDecomposeDetailsMapper.logicDeleteTargetDecomposeDetailsByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解详情表失败");
            }
        }
        return collect;
    }

    /**
     * 修改目标分解(销售订单)表
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        this.validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.ONE);

        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            return targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
    }

    /**
     * 修改目标分解(销售收入)表
     *
     * @param targetDecomposeDTO 目标分解(销售收入)表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        this.validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.TWO);

        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            return targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
    }

    /**
     * 修改目标分解(销售回款)表
     *
     * @param targetDecomposeDTO 目标分解(销售回款)表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        this.validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.THREE);

        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            return targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
    }

    /**
     * 修改目标分解(自定义)表
     *
     * @param targetDecomposeDTO 目标分解(自定义)表
     * @return 结果
     */
    @Override
    @Transactional
    public int updateCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        //校检数据
        this.validTargetDecomposeData(targetDecomposeDTO);
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //分解类型
        targetDecompose.setTargetDecomposeType(Constants.ZERO);

        //修改周期表和详细信息表
        this.packUpdateTargetDecomposeData(targetDecomposeDTO, targetDecompose);
        try {
            return targetDecomposeMapper.updateTargetDecompose(targetDecompose);
        } catch (Exception e) {
            throw new ServiceException("修改目标分解主表失败");
        }
    }

    /**
     * 逻辑批量删除目标分解(销售订单)表
     *
     * @param targetDecomposeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteOrderTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
    }

    /**
     * 逻辑批量删除目标分解(销售收入)表
     *
     * @param targetDecomposeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteIncomeTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
    }

    /**
     * 逻辑批量删除目标分解(销售回款)表
     *
     * @param targetDecomposeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteReturnedTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
    }

    /**
     * 逻辑批量删除目标分解(自定义)表
     *
     * @param targetDecomposeIds 主键集合
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteCustomTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds);
    }


    /**
     * 逻辑删除目标分解(销售订单)表信息
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteOrderTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeId(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(销售收入)表信息
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteIncomeTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeId(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(销售回款)表信息
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteReturnedTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeId(targetDecomposeDTO);
    }

    /**
     * 逻辑删除目标分解(自定义)表信息
     *
     * @param targetDecomposeDTO 目标分解(销售订单)表
     * @return 结果
     */
    @Override
    @Transactional
    public int logicDeleteCustomTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packLogicDeleteTargetDecomposeByTargetDecomposeId(targetDecomposeDTO);
    }

    /**
     * 封装统一删除接口
     *
     * @param targetDecomposeDTO
     * @return
     */
    public int packLogicDeleteTargetDecomposeByTargetDecomposeId(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        targetDecompose.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
        targetDecompose.setUpdateTime(DateUtils.getNowDate());
        targetDecompose.setUpdateBy(SecurityUtils.getUserId());
        //目标分解详情表
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeId(targetDecompose.getTargetDecomposeId());
        //目标分解详情表主键id集合
        List<Long> collect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            try {
                targetDecomposeDetailsMapper.logicDeleteTargetDecomposeDetailsByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解详情表失败");
            }
            try {
                decomposeDetailCyclesMapper.logicDeleteDecomposeDetailCyclesByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解周期表失败");
            }
        }
        //删除历史版本数据
        this.packLogicDeleteTargetDecomposeHistoryData(targetDecomposeDTO);
        return targetDecomposeMapper.logicDeleteTargetDecomposeByTargetDecomposeId(targetDecompose);
    }
    /**
     * 封装统一删除历史版本数据
     */
    private void packLogicDeleteTargetDecomposeHistoryData(TargetDecomposeDTO targetDecomposeDTO) {
        //目标分解历史版本表集合
        List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDTOS = new ArrayList<>();
        //目标分解详情快照表集合
        List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS = new ArrayList<>();
        try {
            TargetDecomposeHistory targetDecomposeHistory = new TargetDecomposeHistory();
            targetDecomposeHistory.setTargetDecomposeId(targetDecomposeDTO.getTargetDecomposeId());
            targetDecomposeHistory.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeHistory.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeHistoryDTOS = targetDecomposeHistoryMapper.selectTargetDecomposeHistoryList(targetDecomposeHistory);
            //删除历史版本
            targetDecomposeHistoryMapper.logicDeleteTargetDecomposeHistoryByTargetDecomposeId(targetDecomposeHistory);
        } catch (Exception e) {
            throw new ServiceException("删除关联历史版本失败");
        }

        if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)) {
            List<Long> collect1 = targetDecomposeHistoryDTOS.stream().map(TargetDecomposeHistoryDTO::getTargetDecomposeHistoryId).collect(Collectors.toList());
            try {

                if (StringUtils.isNotEmpty(collect1)) {
                    decomposeDetailsSnapshotDTOS = decomposeDetailsSnapshotMapper.selectDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(collect1);
                    //删除历史版本
                    decomposeDetailsSnapshotMapper.logicDeleteDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(collect1,SecurityUtils.getUserId(),DateUtils.getNowDate());
                }

            } catch (Exception e) {
                throw new ServiceException("删除关联历史版本详情失败");
            }
        }
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)){
            List<Long> collect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getDecomposeDetailsSnapshotId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)){
                try {
                    detailCyclesSnapshotMapper.logicDeleteDetailCyclesSnapshotByDecomposeDetailsSnapshotIds(collect,SecurityUtils.getUserId(),DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除目标分解详情周期快照失败");
                }
            }

        }
    }

    /**
     * 封装统一批量删除接口
     *
     * @param targetDecomposeIds
     * @return
     */
    public int packLogicDeleteTargetDecomposeByTargetDecomposeIds(List<Long> targetDecomposeIds) {
        //目标分解详情表
        List<TargetDecomposeDetailsDTO> targetDecomposeDetailsDTOList = targetDecomposeDetailsMapper.selectTargetDecomposeDetailsByTargetDecomposeIds(targetDecomposeIds);
        //目标分解详情表主键id集合
        List<Long> collect = targetDecomposeDetailsDTOList.stream().map(TargetDecomposeDetailsDTO::getTargetDecomposeDetailsId).collect(Collectors.toList());
        if (StringUtils.isNotEmpty(collect)) {
            try {
                targetDecomposeDetailsMapper.logicDeleteTargetDecomposeDetailsByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解详情表失败");
            }
            try {
                decomposeDetailCyclesMapper.logicDeleteDecomposeDetailCyclesByTargetDecomposeDetailsIds(collect, SecurityUtils.getUserId(), DateUtils.getNowDate());
            } catch (Exception e) {
                throw new ServiceException("删除目标分解周期表失败");
            }
        }
        this.packLogicDeleteTargetDecomposeHistoryDatas(targetDecomposeIds);
        return targetDecomposeMapper.logicDeleteTargetDecomposeByTargetDecomposeIds(targetDecomposeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 封装统一批量删除历史版本数据
     * @param targetDecomposeIds
     */
    private void packLogicDeleteTargetDecomposeHistoryDatas(List<Long> targetDecomposeIds) {
        //目标分解历史版本表集合
        List<TargetDecomposeHistoryDTO> targetDecomposeHistoryDTOS = new ArrayList<>();
        //目标分解详情快照表集合
        List<DecomposeDetailsSnapshotDTO> decomposeDetailsSnapshotDTOS = new ArrayList<>();
        try {
            targetDecomposeHistoryDTOS = targetDecomposeHistoryMapper.selectTargetDecomposeHistoryByTargetDecomposeIds(targetDecomposeIds);
            //删除历史版本
            targetDecomposeHistoryMapper.logicDeleteTargetDecomposeHistoryByTargetDecomposeIds(targetDecomposeIds,SecurityUtils.getUserId(),DateUtils.getNowDate());
        } catch (Exception e) {
            throw new ServiceException("删除关联历史版本失败");
        }

        if (StringUtils.isNotEmpty(targetDecomposeHistoryDTOS)) {
            List<Long> collect1 = targetDecomposeHistoryDTOS.stream().map(TargetDecomposeHistoryDTO::getTargetDecomposeHistoryId).collect(Collectors.toList());
            try {

                if (StringUtils.isNotEmpty(collect1)) {
                    decomposeDetailsSnapshotDTOS = decomposeDetailsSnapshotMapper.selectDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(collect1);
                    //删除历史版本
                    decomposeDetailsSnapshotMapper.logicDeleteDecomposeDetailsSnapshotByTargetDecomposeHistoryIds(collect1,SecurityUtils.getUserId(),DateUtils.getNowDate());
                }

            } catch (Exception e) {
                throw new ServiceException("删除关联历史版本详情失败");
            }
        }
        if (StringUtils.isNotEmpty(decomposeDetailsSnapshotDTOS)){
            List<Long> collect = decomposeDetailsSnapshotDTOS.stream().map(DecomposeDetailsSnapshotDTO::getDecomposeDetailsSnapshotId).collect(Collectors.toList());
            if (StringUtils.isNotEmpty(collect)){
                try {
                    detailCyclesSnapshotMapper.logicDeleteDetailCyclesSnapshotByDecomposeDetailsSnapshotIds(collect,SecurityUtils.getUserId(),DateUtils.getNowDate());
                } catch (Exception e) {
                    throw new ServiceException("删除目标分解详情周期快照失败");
                }
            }

        }
    }

    /**
     * 批量新增目标分解(销售订单)表信息
     *
     * @param targetDecomposeDtos 目标分解(销售订单)表对象
     */

    public int insertTargetDecomposes(List<TargetDecomposeDTO> targetDecomposeDtos) {
        List<TargetDecompose> targetDecomposeList = new ArrayList();

        for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDtos) {
            TargetDecompose targetDecompose = new TargetDecompose();
            BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
            targetDecompose.setCreateBy(SecurityUtils.getUserId());
            targetDecompose.setCreateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateBy(SecurityUtils.getUserId());
            targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeList.add(targetDecompose);
        }
        return targetDecomposeMapper.batchTargetDecompose(targetDecomposeList);
    }

    /**
     * 批量修改目标分解(销售订单)表信息
     *
     * @param targetDecomposeDtos 目标分解(销售订单)表对象
     */

    public int updateTargetDecomposes(List<TargetDecomposeDTO> targetDecomposeDtos) {
        List<TargetDecompose> targetDecomposeList = new ArrayList();

        for (TargetDecomposeDTO targetDecomposeDTO : targetDecomposeDtos) {
            TargetDecompose targetDecompose = new TargetDecompose();
            BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
            targetDecompose.setCreateBy(SecurityUtils.getUserId());
            targetDecompose.setCreateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeList.add(targetDecompose);
        }
        return targetDecomposeMapper.updateTargetDecomposes(targetDecomposeList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetDecompose(List<TargetDecomposeExcel> list) {
        List<TargetDecompose> targetDecomposeList = new ArrayList<>();
        list.forEach(l -> {
            TargetDecompose targetDecompose = new TargetDecompose();
            BeanUtils.copyProperties(l, targetDecompose);
            targetDecompose.setCreateBy(SecurityUtils.getUserId());
            targetDecompose.setCreateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateTime(DateUtils.getNowDate());
            targetDecompose.setUpdateBy(SecurityUtils.getUserId());
            targetDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetDecomposeList.add(targetDecompose);
        });
        try {
            targetDecomposeMapper.batchTargetDecompose(targetDecomposeList);
        } catch (Exception e) {
            throw new ServiceException("导入目标分解(销售订单)表失败");
        }
    }

    /**
     * 目标分解(销售订单)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeExcel> exportOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packExportOrderTargetDecompose(targetDecomposeDTO);
    }

    /**
     * 目标分解(销售收入)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeExcel> exportIncomeTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packExportOrderTargetDecompose(targetDecomposeDTO);
    }

    /**
     * 目标分解(销售回款)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeExcel> exportReturnedTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packExportOrderTargetDecompose(targetDecomposeDTO);
    }

    /**
     * 目标分解(自定义)导出列表Excel
     *
     * @param targetDecomposeDTO
     * @return
     */
    @Override
    public List<TargetDecomposeExcel> exportCustomTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        return this.packExportOrderTargetDecompose(targetDecomposeDTO);
    }

    /**
     * 统一封装目标分解导出
     *
     * @param targetDecomposeDTO
     * @return
     */
    public List<TargetDecomposeExcel> packExportOrderTargetDecompose(TargetDecomposeDTO targetDecomposeDTO) {
        TargetDecompose targetDecompose = new TargetDecompose();
        BeanUtils.copyProperties(targetDecomposeDTO, targetDecompose);
        //excel实体类
        TargetDecomposeExcel targetDecomposeExcel = new TargetDecomposeExcel();
        //excelList
        List<TargetDecomposeExcel> targetDecomposeExcelList = new ArrayList<>();
        List<TargetDecomposeDTO> targetDecomposeDTOList = targetDecomposeMapper.selectTargetDecomposeList(targetDecompose);
        if (StringUtils.isNotEmpty(targetDecomposeDTOList)) {
            for (TargetDecomposeDTO decomposeDTO : targetDecomposeDTOList) {
                BeanUtils.copyProperties(decomposeDTO, targetDecomposeExcel);
                //时间维度
                if (1 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("年度");
                } else if (2 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("半年度");
                } else if (3 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("季度");
                } else if (4 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("月度");
                } else if (5 == targetDecomposeExcel.getTimeDimension()) {
                    targetDecomposeExcel.setTimeDimensionName("周");
                }
                BigDecimal decomposeTarget = targetDecomposeExcel.getDecomposeTarget();
                BigDecimal targetValue = targetDecomposeExcel.getTargetValue();
                if (null != decomposeTarget && null != targetValue) {
                    //目标差异
                    targetDecomposeExcel.setTargetDifference(decomposeTarget.subtract(targetValue));
                }
                targetDecomposeExcelList.add(targetDecomposeExcel);
            }
        }
        return targetDecomposeExcelList;
    }


}

