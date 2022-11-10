package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingIncome;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingIncomeDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingIncomeExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingIncomeMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingIncomeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * TargetSettingIncomeService业务层处理
 *
 * @author Graves
 * @since 2022-10-31
 */
@Service
public class TargetSettingIncomeServiceImpl implements ITargetSettingIncomeService {
    @Autowired
    private TargetSettingIncomeMapper targetSettingIncomeMapper;

    /**
     * 查询目标制定收入表
     *
     * @param targetSettingIncomeId 目标制定收入表主键
     * @return 目标制定收入表
     */
    @Override
    public TargetSettingIncomeDTO selectTargetSettingIncomeByTargetSettingIncomeId(Long targetSettingIncomeId) {
        return targetSettingIncomeMapper.selectTargetSettingIncomeByTargetSettingIncomeId(targetSettingIncomeId);
    }

    /**
     * 查询目标制定收入表列表
     *
     * @param targetSettingIncomeDTO 目标制定收入表
     * @return 目标制定收入表
     */
    @Override
    public List<TargetSettingIncomeDTO> selectTargetSettingIncomeList(TargetSettingIncomeDTO targetSettingIncomeDTO) {
        TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
        BeanUtils.copyProperties(targetSettingIncomeDTO, targetSettingIncome);
        return targetSettingIncomeMapper.selectTargetSettingIncomeList(targetSettingIncome);
    }

    /**
     * 新增目标制定收入表
     *
     * @param targetSettingIncomeDTO 目标制定收入表
     * @return 结果
     */
    @Override
    public TargetSettingIncomeDTO insertTargetSettingIncome(TargetSettingIncomeDTO targetSettingIncomeDTO) {
        TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
        BeanUtils.copyProperties(targetSettingIncomeDTO, targetSettingIncome);
        targetSettingIncome.setCreateBy(SecurityUtils.getUserId());
        targetSettingIncome.setCreateTime(DateUtils.getNowDate());
        targetSettingIncome.setUpdateTime(DateUtils.getNowDate());
        targetSettingIncome.setUpdateBy(SecurityUtils.getUserId());
        targetSettingIncome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetSettingIncomeMapper.insertTargetSettingIncome(targetSettingIncome);
        targetSettingIncomeDTO.setTargetSettingIncomeId(targetSettingIncome.getTargetSettingIncomeId());
        return targetSettingIncomeDTO;
    }

    /**
     * 修改目标制定收入表
     *
     * @param targetSettingIncomeDTO 目标制定收入表
     * @return 结果
     */
    @Override
    public int updateTargetSettingIncome(TargetSettingIncomeDTO targetSettingIncomeDTO) {
        TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
        BeanUtils.copyProperties(targetSettingIncomeDTO, targetSettingIncome);
        targetSettingIncome.setUpdateTime(DateUtils.getNowDate());
        targetSettingIncome.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingIncomeMapper.updateTargetSettingIncome(targetSettingIncome);
    }

    /**
     * 逻辑批量删除目标制定收入表
     *
     * @param targetSettingIncomeIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingIncomeByTargetSettingIncomeIds(List<Long> targetSettingIncomeIds) {
        return targetSettingIncomeMapper.logicDeleteTargetSettingIncomeByTargetSettingIncomeIds(targetSettingIncomeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标制定收入表信息
     *
     * @param targetSettingIncomeId 目标制定收入表主键
     * @return 结果
     */
    @Override
    public int deleteTargetSettingIncomeByTargetSettingIncomeId(Long targetSettingIncomeId) {
        return targetSettingIncomeMapper.deleteTargetSettingIncomeByTargetSettingIncomeId(targetSettingIncomeId);
    }

    /**
     * 通过年限List获取目标收入列表
     *
     * @param targetSettingId 目标制定ID
     */
    @Override
    public List<TargetSettingIncomeDTO> selectTargetSettingIncomeByHistoryNumS(Long targetSettingId) {
        return targetSettingIncomeMapper.selectTargetSettingIncomeByHistoryNumS(targetSettingId);
    }

    /**
     * 逻辑删除目标制定收入表信息
     *
     * @param targetSettingIncomeDTO 目标制定收入表
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingIncomeByTargetSettingIncomeId(TargetSettingIncomeDTO targetSettingIncomeDTO) {
        TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
        targetSettingIncome.setTargetSettingIncomeId(targetSettingIncomeDTO.getTargetSettingIncomeId());
        targetSettingIncome.setUpdateTime(DateUtils.getNowDate());
        targetSettingIncome.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingIncomeMapper.logicDeleteTargetSettingIncomeByTargetSettingIncomeId(targetSettingIncome);
    }

    /**
     * 物理删除目标制定收入表信息
     *
     * @param targetSettingIncomeDTO 目标制定收入表
     * @return 结果
     */

    @Override
    public int deleteTargetSettingIncomeByTargetSettingIncomeId(TargetSettingIncomeDTO targetSettingIncomeDTO) {
        TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
        BeanUtils.copyProperties(targetSettingIncomeDTO, targetSettingIncome);
        return targetSettingIncomeMapper.deleteTargetSettingIncomeByTargetSettingIncomeId(targetSettingIncome.getTargetSettingIncomeId());
    }

    /**
     * 物理批量删除目标制定收入表
     *
     * @param targetSettingIncomeDtos 需要删除的目标制定收入表主键
     * @return 结果
     */

    @Override
    public int deleteTargetSettingIncomeByTargetSettingIncomeIds(List<TargetSettingIncomeDTO> targetSettingIncomeDtos) {
        List<Long> stringList = new ArrayList<>();
        for (TargetSettingIncomeDTO targetSettingIncomeDTO : targetSettingIncomeDtos) {
            stringList.add(targetSettingIncomeDTO.getTargetSettingIncomeId());
        }
        return targetSettingIncomeMapper.deleteTargetSettingIncomeByTargetSettingIncomeIds(stringList);
    }

    /**
     * 批量新增目标制定收入表信息
     *
     * @param targetSettingIncomeDtos 目标制定收入表对象
     */

    public int insertTargetSettingIncomes(List<TargetSettingIncomeDTO> targetSettingIncomeDtos) {
        List<TargetSettingIncome> targetSettingIncomeList = new ArrayList<>();
        for (TargetSettingIncomeDTO targetSettingIncomeDTO : targetSettingIncomeDtos) {
            TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
            BeanUtils.copyProperties(targetSettingIncomeDTO, targetSettingIncome);
            targetSettingIncome.setCreateBy(SecurityUtils.getUserId());
            targetSettingIncome.setCreateTime(DateUtils.getNowDate());
            targetSettingIncome.setUpdateTime(DateUtils.getNowDate());
            targetSettingIncome.setUpdateBy(SecurityUtils.getUserId());
            targetSettingIncome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingIncomeList.add(targetSettingIncome);
        }
        return targetSettingIncomeMapper.batchTargetSettingIncome(targetSettingIncomeList);
    }

    /**
     * 批量修改目标制定收入表信息
     *
     * @param targetSettingIncomeDtos 目标制定收入表对象
     */

    public int updateTargetSettingIncomes(List<TargetSettingIncomeDTO> targetSettingIncomeDtos) {
        List<TargetSettingIncome> targetSettingIncomeList = new ArrayList<>();

        for (TargetSettingIncomeDTO targetSettingIncomeDTO : targetSettingIncomeDtos) {
            TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
            BeanUtils.copyProperties(targetSettingIncomeDTO, targetSettingIncome);
            targetSettingIncome.setCreateBy(SecurityUtils.getUserId());
            targetSettingIncome.setCreateTime(DateUtils.getNowDate());
            targetSettingIncome.setUpdateTime(DateUtils.getNowDate());
            targetSettingIncome.setUpdateBy(SecurityUtils.getUserId());
            targetSettingIncomeList.add(targetSettingIncome);
        }
        return targetSettingIncomeMapper.updateTargetSettingIncomes(targetSettingIncomeList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetSettingIncome(List<TargetSettingIncomeExcel> list) {
        List<TargetSettingIncome> targetSettingIncomeList = new ArrayList<>();
        list.forEach(l -> {
            TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
            BeanUtils.copyProperties(l, targetSettingIncome);
            targetSettingIncome.setCreateBy(SecurityUtils.getUserId());
            targetSettingIncome.setCreateTime(DateUtils.getNowDate());
            targetSettingIncome.setUpdateTime(DateUtils.getNowDate());
            targetSettingIncome.setUpdateBy(SecurityUtils.getUserId());
            targetSettingIncome.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingIncomeList.add(targetSettingIncome);
        });
        try {
            targetSettingIncomeMapper.batchTargetSettingIncome(targetSettingIncomeList);
        } catch (Exception e) {
            throw new ServiceException("导入目标制定收入表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param targetSettingIncomeDTO
     * @return
     */
    @Override
    public List<TargetSettingIncomeExcel> exportTargetSettingIncome(TargetSettingIncomeDTO targetSettingIncomeDTO) {
        TargetSettingIncome targetSettingIncome = new TargetSettingIncome();
        BeanUtils.copyProperties(targetSettingIncomeDTO, targetSettingIncome);
        List<TargetSettingIncomeDTO> targetSettingIncomeDTOList = targetSettingIncomeMapper.selectTargetSettingIncomeList(targetSettingIncome);
        List<TargetSettingIncomeExcel> targetSettingIncomeExcelList = new ArrayList<>();
        return targetSettingIncomeExcelList;
    }
}

