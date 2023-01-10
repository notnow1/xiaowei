package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetSettingRecovery;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetSettingRecoveryDTO;
import net.qixiaowei.operate.cloud.excel.targetManager.TargetSettingRecoveryExcel;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetSettingRecoveryMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetSettingRecoveryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * TargetSettingRecoveryService业务层处理
 *
 * @author Graves
 * @since 2022-11-01
 */
@Service
public class TargetSettingRecoveryServiceImpl implements ITargetSettingRecoveryService {
    @Autowired
    private TargetSettingRecoveryMapper targetSettingRecoveryMapper;

    /**
     * 查询目标制定回款表
     *
     * @param targetSettingRecoveriesId 目标制定回款表主键
     * @return 目标制定回款表
     */
    @Override
    public TargetSettingRecoveryDTO selectTargetSettingRecoveryByTargetSettingRecoveriesId(Long targetSettingRecoveriesId) {
        return targetSettingRecoveryMapper.selectTargetSettingRecoveryByTargetSettingRecoveriesId(targetSettingRecoveriesId);
    }

    /**
     * 查询目标制定回款表列表
     *
     * @param targetSettingRecoveryDTO 目标制定回款表
     * @return 目标制定回款表
     */
    @Override
    public List<TargetSettingRecoveryDTO> selectTargetSettingRecoveryList(TargetSettingRecoveryDTO targetSettingRecoveryDTO) {
        TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
        BeanUtils.copyProperties(targetSettingRecoveryDTO, targetSettingRecovery);
        return targetSettingRecoveryMapper.selectTargetSettingRecoveryList(targetSettingRecovery);
    }

    /**
     * 新增目标制定回款表
     *
     * @param targetSettingRecoveryDTO 目标制定回款表
     * @return 结果
     */
    @Override
    public TargetSettingRecoveryDTO insertTargetSettingRecovery(TargetSettingRecoveryDTO targetSettingRecoveryDTO) {
        TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
        BeanUtils.copyProperties(targetSettingRecoveryDTO, targetSettingRecovery);
        targetSettingRecovery.setCreateBy(SecurityUtils.getUserId());
        targetSettingRecovery.setCreateTime(DateUtils.getNowDate());
        targetSettingRecovery.setUpdateTime(DateUtils.getNowDate());
        targetSettingRecovery.setUpdateBy(SecurityUtils.getUserId());
        targetSettingRecovery.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetSettingRecoveryMapper.insertTargetSettingRecovery(targetSettingRecovery);
        targetSettingRecoveryDTO.setTargetSettingRecoveriesId(targetSettingRecovery.getTargetSettingRecoveriesId());
        return targetSettingRecoveryDTO;
    }

    /**
     * 修改目标制定回款表
     *
     * @param targetSettingRecoveryDTO 目标制定回款表
     * @return 结果
     */
    @Override
    public int updateTargetSettingRecovery(TargetSettingRecoveryDTO targetSettingRecoveryDTO) {
        TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
        targetSettingRecovery.setTargetSettingRecoveriesId(targetSettingRecoveryDTO.getTargetSettingRecoveriesId());
        targetSettingRecovery.setBalanceReceivables(targetSettingRecoveryDTO.getBalanceReceivables());
        targetSettingRecovery.setBaselineValue(targetSettingRecoveryDTO.getBaselineValue());
        targetSettingRecovery.setImproveDays(targetSettingRecoveryDTO.getImproveDays());
        targetSettingRecovery.setUpdateTime(DateUtils.getNowDate());
        targetSettingRecovery.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingRecoveryMapper.updateTargetSettingRecovery(targetSettingRecovery);
    }

    /**
     * 逻辑批量删除目标制定回款表
     *
     * @param targetSettingRecoveriesIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingRecoveryByTargetSettingRecoveriesIds(List<Long> targetSettingRecoveriesIds) {
        return targetSettingRecoveryMapper.logicDeleteTargetSettingRecoveryByTargetSettingRecoveriesIds(targetSettingRecoveriesIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标制定回款表信息
     *
     * @param targetSettingRecoveriesId 目标制定回款表主键
     * @return 结果
     */
    @Override
    public int deleteTargetSettingRecoveryByTargetSettingRecoveriesId(Long targetSettingRecoveriesId) {
        return targetSettingRecoveryMapper.deleteTargetSettingRecoveryByTargetSettingRecoveriesId(targetSettingRecoveriesId);
    }

    /**
     * 逻辑删除目标制定回款表信息
     *
     * @param targetSettingRecoveryDTO 目标制定回款表
     * @return 结果
     */
    @Override
    public int logicDeleteTargetSettingRecoveryByTargetSettingRecoveriesId(TargetSettingRecoveryDTO targetSettingRecoveryDTO) {
        TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
        targetSettingRecovery.setTargetSettingRecoveriesId(targetSettingRecoveryDTO.getTargetSettingRecoveriesId());
        targetSettingRecovery.setUpdateTime(DateUtils.getNowDate());
        targetSettingRecovery.setUpdateBy(SecurityUtils.getUserId());
        return targetSettingRecoveryMapper.logicDeleteTargetSettingRecoveryByTargetSettingRecoveriesId(targetSettingRecovery);
    }

    /**
     * 物理删除目标制定回款表信息
     *
     * @param targetSettingRecoveryDTO 目标制定回款表
     * @return 结果
     */

    @Override
    public int deleteTargetSettingRecoveryByTargetSettingRecoveriesId(TargetSettingRecoveryDTO targetSettingRecoveryDTO) {
        TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
        BeanUtils.copyProperties(targetSettingRecoveryDTO, targetSettingRecovery);
        return targetSettingRecoveryMapper.deleteTargetSettingRecoveryByTargetSettingRecoveriesId(targetSettingRecovery.getTargetSettingRecoveriesId());
    }

    /**
     * 物理批量删除目标制定回款表
     *
     * @param targetSettingRecoveryDtos 需要删除的目标制定回款表主键
     * @return 结果
     */

    @Override
    public int deleteTargetSettingRecoveryByTargetSettingRecoveriesIds(List<TargetSettingRecoveryDTO> targetSettingRecoveryDtos) {
        List<Long> stringList = new ArrayList();
        for (TargetSettingRecoveryDTO targetSettingRecoveryDTO : targetSettingRecoveryDtos) {
            stringList.add(targetSettingRecoveryDTO.getTargetSettingRecoveriesId());
        }
        return targetSettingRecoveryMapper.deleteTargetSettingRecoveryByTargetSettingRecoveriesIds(stringList);
    }

    /**
     * 批量新增目标制定回款表信息
     *
     * @param targetSettingRecoveryDtos 目标制定回款表对象
     */

    @Override
    public int insertTargetSettingRecoverys(List<TargetSettingRecoveryDTO> targetSettingRecoveryDtos) {
        List<TargetSettingRecovery> targetSettingRecoveryList = new ArrayList();

        for (TargetSettingRecoveryDTO targetSettingRecoveryDTO : targetSettingRecoveryDtos) {
            TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
            BeanUtils.copyProperties(targetSettingRecoveryDTO, targetSettingRecovery);
            targetSettingRecovery.setCreateBy(SecurityUtils.getUserId());
            targetSettingRecovery.setCreateTime(DateUtils.getNowDate());
            targetSettingRecovery.setUpdateTime(DateUtils.getNowDate());
            targetSettingRecovery.setUpdateBy(SecurityUtils.getUserId());
            targetSettingRecovery.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingRecoveryList.add(targetSettingRecovery);
        }
        return targetSettingRecoveryMapper.batchTargetSettingRecovery(targetSettingRecoveryList);
    }

    /**
     * 批量修改目标制定回款表信息
     *
     * @param targetSettingRecoveryDtos 目标制定回款表对象
     */

    @Override
    public int updateTargetSettingRecoverys(List<TargetSettingRecoveryDTO> targetSettingRecoveryDtos) {
        List<TargetSettingRecovery> targetSettingRecoveryList = new ArrayList();

        for (TargetSettingRecoveryDTO targetSettingRecoveryDTO : targetSettingRecoveryDtos) {
            TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
            BeanUtils.copyProperties(targetSettingRecoveryDTO, targetSettingRecovery);
            targetSettingRecovery.setCreateBy(SecurityUtils.getUserId());
            targetSettingRecovery.setCreateTime(DateUtils.getNowDate());
            targetSettingRecovery.setUpdateTime(DateUtils.getNowDate());
            targetSettingRecovery.setUpdateBy(SecurityUtils.getUserId());
            targetSettingRecoveryList.add(targetSettingRecovery);
        }
        return targetSettingRecoveryMapper.updateTargetSettingRecoverys(targetSettingRecoveryList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importTargetSettingRecovery(List<TargetSettingRecoveryExcel> list) {
        List<TargetSettingRecovery> targetSettingRecoveryList = new ArrayList<>();
        list.forEach(l -> {
            TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
            BeanUtils.copyProperties(l, targetSettingRecovery);
            targetSettingRecovery.setCreateBy(SecurityUtils.getUserId());
            targetSettingRecovery.setCreateTime(DateUtils.getNowDate());
            targetSettingRecovery.setUpdateTime(DateUtils.getNowDate());
            targetSettingRecovery.setUpdateBy(SecurityUtils.getUserId());
            targetSettingRecovery.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetSettingRecoveryList.add(targetSettingRecovery);
        });
        try {
            targetSettingRecoveryMapper.batchTargetSettingRecovery(targetSettingRecoveryList);
        } catch (Exception e) {
            throw new ServiceException("导入目标制定回款表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param targetSettingRecoveryDTO
     * @return
     */
    @Override
    public List<TargetSettingRecoveryExcel> exportTargetSettingRecovery(TargetSettingRecoveryDTO targetSettingRecoveryDTO) {
        TargetSettingRecovery targetSettingRecovery = new TargetSettingRecovery();
        BeanUtils.copyProperties(targetSettingRecoveryDTO, targetSettingRecovery);
        List<TargetSettingRecoveryDTO> targetSettingRecoveryDTOList = targetSettingRecoveryMapper.selectTargetSettingRecoveryList(targetSettingRecovery);
        List<TargetSettingRecoveryExcel> targetSettingRecoveryExcelList = new ArrayList<>();
        return targetSettingRecoveryExcelList;
    }

    /**
     * 通过目标制定ID获取目标制定回款
     *
     * @param targetSettingId
     * @return
     */
    @Override
    public TargetSettingRecoveryDTO selectTargetSettingRecoveryByTargetSettingId(Long targetSettingId) {
        return targetSettingRecoveryMapper.selectTargetSettingRecoveryByTargetSettingId(targetSettingId);
    }

    /**
     * 根据TargetSettingID集合查询Recovery列表
     *
     * @param targetSettingIds
     * @return
     */
    @Override
    public List<TargetSettingRecoveryDTO> selectTargetSettingRecoveryByTargetSettingIds(List<Long> targetSettingIds) {
        return targetSettingRecoveryMapper.selectTargetSettingRecoveryByTargetSettingIds(targetSettingIds);
    }
}

