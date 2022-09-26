package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeDimension;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDimensionDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetDecomposeDimensionMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeDimensionService;


/**
 * TargetDecomposeDimensionService业务层处理
 *
 * @author Graves
 * @since 2022-09-26
 */
@Service
public class TargetDecomposeDimensionServiceImpl implements ITargetDecomposeDimensionService {
    @Autowired
    private TargetDecomposeDimensionMapper targetDecomposeDimensionMapper;

    /**
     * 查询目标分解维度配置
     *
     * @param targetDecomposeDimensionId 目标分解维度配置主键
     * @return 目标分解维度配置
     */
    @Override
    public TargetDecomposeDimensionDTO selectTargetDecomposeDimensionByTargetDecomposeDimensionId(Long targetDecomposeDimensionId) {
        return targetDecomposeDimensionMapper.selectTargetDecomposeDimensionByTargetDecomposeDimensionId(targetDecomposeDimensionId);
    }

    /**
     * 查询目标分解维度配置列表
     *
     * @param targetDecomposeDimensionDTO 目标分解维度配置
     * @return 目标分解维度配置
     */
    @Override
    public List<TargetDecomposeDimensionDTO> selectTargetDecomposeDimensionList(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
        return targetDecomposeDimensionMapper.selectTargetDecomposeDimensionList(targetDecomposeDimension);
    }

    /**
     * 新增目标分解维度配置
     *
     * @param targetDecomposeDimensionDTO 目标分解维度配置
     * @return 结果
     */
    @Transactional
    @Override
    public int insertTargetDecomposeDimension(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
        int maxTargetDimensionConfigSort = targetDecomposeDimensionMapper.getMaxTargetDimensionConfigSort() + 1;
        targetDecomposeDimension.setDeleteFlag(0);
        targetDecomposeDimension.setSort(maxTargetDimensionConfigSort);
        targetDecomposeDimension.setCreateTime(DateUtils.getNowDate());
        targetDecomposeDimension.setUpdateTime(DateUtils.getNowDate());
        // todo 创建人与更新人
//        targetDecomposeDimension.setCreateBy(SecurityUtils.getUserId());
//        targetDecomposeDimension.setUpdateBy(SecurityUtils.getUserId());
        return targetDecomposeDimensionMapper.insertTargetDecomposeDimension(targetDecomposeDimension);
    }

    /**
     * 修改目标分解维度配置
     *
     * @param targetDecomposeDimensionDTO 目标分解维度配置
     * @return 结果
     */
    @Transactional
    @Override
    public int updateTargetDecomposeDimension(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
        targetDecomposeDimension.setUpdateTime(DateUtils.getNowDate());
        // todo 更新人
//        targetDecomposeDimension.setUpdateBy(SecurityUtils.getUserId());
        return targetDecomposeDimensionMapper.updateTargetDecomposeDimension(targetDecomposeDimension);
    }

    /**
     * 逻辑批量删除目标分解维度配置
     *
     * @param targetDecomposeDimensionDtos 需要删除的目标分解维度配置主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
        List<Long> stringList = new ArrayList();
        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
            stringList.add(targetDecomposeDimensionDTO.getTargetDecomposeDimensionId());
        }
        return targetDecomposeDimensionMapper.logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(stringList, targetDecomposeDimensionDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标分解维度配置信息
     *
     * @param targetDecomposeDimensionId 目标分解维度配置主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteTargetDecomposeDimensionByTargetDecomposeDimensionId(Long targetDecomposeDimensionId) {
        return targetDecomposeDimensionMapper.deleteTargetDecomposeDimensionByTargetDecomposeDimensionId(targetDecomposeDimensionId);
    }

    /**
     * 逻辑删除目标分解维度配置信息
     *
     * @param targetDecomposeDimensionDTO 目标分解维度配置
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionId(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
        return targetDecomposeDimensionMapper.logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionId(targetDecomposeDimension);
    }

    /**
     * 物理删除目标分解维度配置信息
     *
     * @param targetDecomposeDimensionDTO 目标分解维度配置
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTargetDecomposeDimensionByTargetDecomposeDimensionId(TargetDecomposeDimensionDTO targetDecomposeDimensionDTO) {
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
        return targetDecomposeDimensionMapper.deleteTargetDecomposeDimensionByTargetDecomposeDimensionId(targetDecomposeDimension.getTargetDecomposeDimensionId());
    }

    /**
     * 物理批量删除目标分解维度配置
     *
     * @param targetDecomposeDimensionDtos 需要删除的目标分解维度配置主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
        List<Long> stringList = new ArrayList();
        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
            stringList.add(targetDecomposeDimensionDTO.getTargetDecomposeDimensionId());
        }
        return targetDecomposeDimensionMapper.deleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(stringList);
    }

    /**
     * 批量新增目标分解维度配置信息
     *
     * @param targetDecomposeDimensionDtos 目标分解维度配置对象
     */
    @Transactional
    public int insertTargetDecomposeDimensions(List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
        List<TargetDecomposeDimension> targetDecomposeDimensionList = new ArrayList();

        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
            TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
            BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
            targetDecomposeDimension.setCreateTime(DateUtils.getNowDate());
            targetDecomposeDimension.setUpdateTime(DateUtils.getNowDate());
//            targetDecomposeDimension.setCreateBy(SecurityUtils.getUserId());
//            targetDecomposeDimension.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeDimensionList.add(targetDecomposeDimension);
        }
        return targetDecomposeDimensionMapper.batchTargetDecomposeDimension(targetDecomposeDimensionList);
    }

    /**
     * 批量修改目标分解维度配置信息
     *
     * @param targetDecomposeDimensionDtos 目标分解维度配置对象
     */
    @Transactional
    public int updateTargetDecomposeDimensions(List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
        List<TargetDecomposeDimension> targetDecomposeDimensionList = new ArrayList();

        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
            TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
            BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
            targetDecomposeDimension.setUpdateTime(DateUtils.getNowDate());
//            targetDecomposeDimension.setCreateBy(SecurityUtils.getUserId());
            targetDecomposeDimension.setUpdateBy(SecurityUtils.getUserId().toString());
            targetDecomposeDimensionList.add(targetDecomposeDimension);
        }
        return targetDecomposeDimensionMapper.updateTargetDecomposeDimensions(targetDecomposeDimensionList);
    }

    /**
     * 校验分解维度的唯一性
     *
     * @param dimension                分解维度
     * @param targetDecomposeDimension 分解维度配置
     * @return boolean
     */
    @Override
    public boolean checkUnique(String dimension, TargetDecomposeDimension targetDecomposeDimension) {
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionList = targetDecomposeDimensionMapper.selectTargetDecomposeDimensionList(targetDecomposeDimension);
        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionList) {
            if (dimension.equals(targetDecomposeDimensionDTO.getDecompositionDimension())) {
                return true;
            }
        }
        return false;
    }

    /**
     * 分解维度引用校验
     *
     * @param targetDecomposeDimensionId
     * @return
     */
    @Override
    public boolean isQuote(Long targetDecomposeDimensionId) {
        //todo 引用校验
        return false;
    }
}

