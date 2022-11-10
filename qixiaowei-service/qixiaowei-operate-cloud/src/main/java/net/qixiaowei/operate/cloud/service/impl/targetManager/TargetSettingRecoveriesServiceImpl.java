package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingRecoveries;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingRecoveriesDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingRecoveriesExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingRecoveriesMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingRecoveriesService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * TargetSettingRecoveriesService业务层处理
 *
 * @author Graves
 * @since 2022-11-01
 */
@Service
public class TargetSettingRecoveriesServiceImpl implements ITargetSettingRecoveriesService {
    @Autowired
    private TargetSettingRecoveriesMapper targetSettingRecoveriesMapper;

    /**
     * 查询目标制定回款集合表
     *
     * @param targetSettingRecoveriesId 目标制定回款集合表主键
     * @return 目标制定回款集合表
     */
    @Override
    public TargetSettingRecoveriesDTO selectTargetSettingRecoveriesByTargetSettingRecoveriesId(Long targetSettingRecoveriesId) {
        return targetSettingRecoveriesMapper.selectTargetSettingRecoveriesByTargetSettingRecoveriesId(targetSettingRecoveriesId);
    }

    /**
     * 查询目标制定回款集合表列表
     *
     * @param targetSettingRecoveriesDTO 目标制定回款集合表
     * @return 目标制定回款集合表
     */
    @Override
    public List<TargetSettingRecoveriesDTO> selectTargetSettingRecoveriesList(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO) {
        TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
        BeanUtils.copyProperties(targetSettingRecoveriesDTO, targetSettingRecoveries);
        return targetSettingRecoveriesMapper.selectTargetSettingRecoveriesList(targetSettingRecoveries);
    }

    /**
     * 新增目标制定回款集合表
     *
     * @param targetSettingRecoveriesDTO 目标制定回款集合表
     * @return 结果
     */
    @Override
    public TargetSettingRecoveriesDTO insertTargetSettingRecoveries(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO) {
        TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
        BeanUtils.copyProperties(targetSettingRecoveriesDTO, targetSettingRecoveries);
        targetSettingRecoveries.setCreateBy(SecurityUtils.getUserId());
        targetSettingRecoveries.setCreateTime(DateUtils.getNowDate());
        targetSettingRecoveries.setUpdateTime(DateUtils.getNowDate());
        targetSettingRecoveries.setUpdateBy(SecurityUtils.getUserId());
        targetSettingRecoveries.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetSettingRecoveriesMapper.insertTargetSettingRecoveries(targetSettingRecoveries);
        targetSettingRecoveriesDTO.setTargetSettingRecoveriesId(targetSettingRecoveries.getTargetSettingRecoveriesId());
        return targetSettingRecoveriesDTO;
    }

    /**
     * 修改目标制定回款集合表
     *
     * @param targetSettingRecoveriesDTO 目标制定回款集合表
     * @return 结果
     */
    @Override
    public int updateTargetSettingRecoveries(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO) {
        TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
        BeanUtils.copyProperties(targetSettingRecoveriesDTO, targetSettingRecoveries);
        targetSettingRecoveries.setUpdateTime(DateUtils.getNowDate());
        targetSettingRecoveries.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingRecoveriesMapper.updateTargetSettingRecoveries(targetSettingRecoveries);
    }

    /**
     * 逻辑批量删除目标制定回款集合表
     *
     * @param targetSettingRecoveriesIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingRecoveriesByTargetSettingRecoveriesIds(List<Long> targetSettingRecoveriesIds) {
        return targetSettingRecoveriesMapper.logicDeleteTargetSettingRecoveriesByTargetSettingRecoveriesIds(targetSettingRecoveriesIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标制定回款集合表信息
     *
     * @param targetSettingRecoveriesId 目标制定回款集合表主键
     * @return 结果
     */
    @Override
    public int deleteTargetSettingRecoveriesByTargetSettingRecoveriesId(Long targetSettingRecoveriesId) {
        return targetSettingRecoveriesMapper.deleteTargetSettingRecoveriesByTargetSettingRecoveriesId(targetSettingRecoveriesId);
    }

    /**
     * 逻辑删除目标制定回款集合表信息
     *
     * @param targetSettingRecoveriesDTO 目标制定回款集合表
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingRecoveriesByTargetSettingRecoveriesId(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO) {
        TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
        targetSettingRecoveries.setTargetSettingRecoveriesId(targetSettingRecoveriesDTO.getTargetSettingRecoveriesId());
        targetSettingRecoveries.setUpdateTime(DateUtils.getNowDate());
        targetSettingRecoveries.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingRecoveriesMapper.logicDeleteTargetSettingRecoveriesByTargetSettingRecoveriesId(targetSettingRecoveries);
    }

    /**
     * 物理删除目标制定回款集合表信息
     *
     * @param targetSettingRecoveriesDTO 目标制定回款集合表
     * @return 结果
     */

    @Override
    public int deleteTargetSettingRecoveriesByTargetSettingRecoveriesId(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO) {
        TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
        BeanUtils.copyProperties(targetSettingRecoveriesDTO, targetSettingRecoveries);
        return targetSettingRecoveriesMapper.deleteTargetSettingRecoveriesByTargetSettingRecoveriesId(targetSettingRecoveries.getTargetSettingRecoveriesId());
    }

    /**
     * 物理批量删除目标制定回款集合表
     *
     * @param targetSettingRecoveriesDtos 需要删除的目标制定回款集合表主键
     * @return 结果
     */

    @Override
    public int deleteTargetSettingRecoveriesByTargetSettingRecoveriesIds(List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDtos) {
        List<Long> stringList = new ArrayList();
        for (TargetSettingRecoveriesDTO targetSettingRecoveriesDTO : targetSettingRecoveriesDtos) {
            stringList.add(targetSettingRecoveriesDTO.getTargetSettingRecoveriesId());
        }
        return targetSettingRecoveriesMapper.deleteTargetSettingRecoveriesByTargetSettingRecoveriesIds(stringList);
    }

    /**
     * 批量新增目标制定回款集合表信息
     *
     * @param targetSettingRecoveriesDtos 目标制定回款集合表对象
     */

    public int insertTargetSettingRecoveriess(List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDtos) {
        List<TargetSettingRecoveries> targetSettingRecoveriesList = new ArrayList();

        for (TargetSettingRecoveriesDTO targetSettingRecoveriesDTO : targetSettingRecoveriesDtos) {
            TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
            BeanUtils.copyProperties(targetSettingRecoveriesDTO, targetSettingRecoveries);
            targetSettingRecoveries.setCreateBy(SecurityUtils.getUserId());
            targetSettingRecoveries.setCreateTime(DateUtils.getNowDate());
            targetSettingRecoveries.setUpdateTime(DateUtils.getNowDate());
            targetSettingRecoveries.setUpdateBy(SecurityUtils.getUserId());
            targetSettingRecoveries.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingRecoveriesList.add(targetSettingRecoveries);
        }
        return targetSettingRecoveriesMapper.batchTargetSettingRecoveries(targetSettingRecoveriesList);
    }

    /**
     * 批量修改目标制定回款集合表信息
     *
     * @param targetSettingRecoveriesDtos 目标制定回款集合表对象
     */

    public int updateTargetSettingRecoveriess(List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDtos) {
        List<TargetSettingRecoveries> targetSettingRecoveriesList = new ArrayList();

        for (TargetSettingRecoveriesDTO targetSettingRecoveriesDTO : targetSettingRecoveriesDtos) {
            TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
            BeanUtils.copyProperties(targetSettingRecoveriesDTO, targetSettingRecoveries);
            targetSettingRecoveries.setCreateBy(SecurityUtils.getUserId());
            targetSettingRecoveries.setCreateTime(DateUtils.getNowDate());
            targetSettingRecoveries.setUpdateTime(DateUtils.getNowDate());
            targetSettingRecoveries.setUpdateBy(SecurityUtils.getUserId());
            targetSettingRecoveriesList.add(targetSettingRecoveries);
        }
        return targetSettingRecoveriesMapper.updateTargetSettingRecoveriess(targetSettingRecoveriesList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetSettingRecoveries(List<TargetSettingRecoveriesExcel> list) {
        List<TargetSettingRecoveries> targetSettingRecoveriesList = new ArrayList<>();
        list.forEach(l -> {
            TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
            BeanUtils.copyProperties(l, targetSettingRecoveries);
            targetSettingRecoveries.setCreateBy(SecurityUtils.getUserId());
            targetSettingRecoveries.setCreateTime(DateUtils.getNowDate());
            targetSettingRecoveries.setUpdateTime(DateUtils.getNowDate());
            targetSettingRecoveries.setUpdateBy(SecurityUtils.getUserId());
            targetSettingRecoveries.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingRecoveriesList.add(targetSettingRecoveries);
        });
        try {
            targetSettingRecoveriesMapper.batchTargetSettingRecoveries(targetSettingRecoveriesList);
        } catch (Exception e) {
            throw new ServiceException("导入目标制定回款集合表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param targetSettingRecoveriesDTO
     * @return
     */
    @Override
    public List<TargetSettingRecoveriesExcel> exportTargetSettingRecoveries(TargetSettingRecoveriesDTO targetSettingRecoveriesDTO) {
        TargetSettingRecoveries targetSettingRecoveries = new TargetSettingRecoveries();
        BeanUtils.copyProperties(targetSettingRecoveriesDTO, targetSettingRecoveries);
        List<TargetSettingRecoveriesDTO> targetSettingRecoveriesDTOList = targetSettingRecoveriesMapper.selectTargetSettingRecoveriesList(targetSettingRecoveries);
        List<TargetSettingRecoveriesExcel> targetSettingRecoveriesExcelList = new ArrayList<>();
        return targetSettingRecoveriesExcelList;
    }

    /**
     * 通过目标制定ID查找目标回款列表
     *
     * @param targetSettingId 目标制定ID
     * @return
     */
    @Override
    public List<TargetSettingRecoveriesDTO> selectTargetSettingRecoveriesByTargetSettingId(Long targetSettingId) {
        return targetSettingRecoveriesMapper.selectTargetSettingRecoveriesByTargetSettingId(targetSettingId);
    }
}

