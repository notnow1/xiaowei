package net.qixiaowei.operate.cloud.service.impl.targetManager;

import com.alibaba.nacos.shaded.com.google.common.collect.Lists;
import net.qixiaowei.integration.common.enums.targetManager.TargetDecomposeDimensionCode;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecomposeDimension;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDimensionDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetDecomposeDimensionMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetDecomposeDimensionService;
import org.apache.ibatis.binding.BindingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


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
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDTOS = targetDecomposeDimensionMapper.selectTargetDecomposeDimensionList(targetDecomposeDimension);
        StringBuilder targetDecomposeDimensionName;
        for (TargetDecomposeDimensionDTO decomposeDimensionDTO : targetDecomposeDimensionDTOS) {
            targetDecomposeDimensionName = new StringBuilder("");
            String decompositionDimension = decomposeDimensionDTO.getDecompositionDimension();
            if (StringUtils.isNotEmpty(decompositionDimension)) {
                for (String dimension : decompositionDimension.split(",")) {
                    String info = TargetDecomposeDimensionCode.selectInfo(dimension);
                    if (StringUtils.isNotNull(info)) {
                        targetDecomposeDimensionName.append(info).append("+");
                    }
                }
                String substring = targetDecomposeDimensionName.substring(0, targetDecomposeDimensionName.length() - 1);
                decomposeDimensionDTO.setDecompositionDimensionName(substring);
            }
        }
        return targetDecomposeDimensionDTOS;
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
        //校验
        String decompositionDimension = targetDecomposeDimensionDTO.getDecompositionDimension();
        if (StringUtils.isEmpty(decompositionDimension)) {
            throw new ServiceException("分解维度不能为空");
        }
        if (checkUnique(decompositionDimension)) {
            throw new ServiceException("分解维度不能重复");
        }
        String[] targetDecomposeDimensions = decompositionDimension.split(",");
        for (String target : targetDecomposeDimensions) {
            if (TargetDecomposeDimensionCode.containCode(target)) {
                throw new ServiceException("分解维度不符合规范");
            }
        }
        // todo 分解维度是否正确校验
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
        int maxTargetDimensionConfigSort;
        try {
            maxTargetDimensionConfigSort = targetDecomposeDimensionMapper.getMaxTargetDimensionConfigSort() + 1;
        } catch (BindingException e) {
            maxTargetDimensionConfigSort = 1;
        }
        targetDecomposeDimension.setSort(maxTargetDimensionConfigSort);
        targetDecomposeDimension.setDeleteFlag(0);
        targetDecomposeDimension.setCreateTime(DateUtils.getNowDate());
        targetDecomposeDimension.setUpdateTime(DateUtils.getNowDate());
        targetDecomposeDimension.setCreateBy(SecurityUtils.getUserId());
        targetDecomposeDimension.setUpdateBy(SecurityUtils.getUserId());
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
        targetDecomposeDimension.setUpdateBy(SecurityUtils.getUserId());
        return targetDecomposeDimensionMapper.updateTargetDecomposeDimension(targetDecomposeDimension);
    }

    /**
     * 逻辑批量删除目标分解维度配置
     *
     * @param targetDecomposeDimensionIds 需要删除的目标分解维度配置主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(List<Long> targetDecomposeDimensionIds) {
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDTO = targetDecomposeDimensionMapper.isExist(targetDecomposeDimensionIds);
        if (StringUtils.isNull(targetDecomposeDimensionDTO)) {
            throw new ServiceException("目标分解维度配置不存在");
        }
        if (isQuote(targetDecomposeDimensionIds)) {
            throw new ServiceException("该分解维度已被引用");
        }
        return targetDecomposeDimensionMapper.logicDeleteTargetDecomposeDimensionByTargetDecomposeDimensionIds(targetDecomposeDimensionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
        Long targetDecomposeDimensionId = targetDecomposeDimensionDTO.getTargetDecomposeDimensionId();
        if (StringUtils.isNull(targetDecomposeDimensionId)) {
            throw new ServiceException("分解维度id为空");
        }
        List<Long> targetDecomposeDimensionIds = Lists.newArrayList(targetDecomposeDimensionId);
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionByIds = targetDecomposeDimensionMapper.isExist(targetDecomposeDimensionIds);
        if (StringUtils.isNull(targetDecomposeDimensionByIds)) {
            throw new ServiceException("目标分解维度配置不存在");
        }
        if (isQuote(targetDecomposeDimensionIds)) {
            throw new ServiceException("该分解维度已被引用");
        }
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
        targetDecomposeDimension.setUpdateTime(DateUtils.getNowDate());
        targetDecomposeDimension.setUpdateBy(SecurityUtils.getUserId());
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
        List<Long> stringList = new ArrayList<>();
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
        List<TargetDecomposeDimension> targetDecomposeDimensionList = new ArrayList<>();

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
        Long targetDecomposeDimensionId;

        List<Long> targetDecomposeDimensionIds = new ArrayList<>();
        int i = 1;
        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
            targetDecomposeDimensionId = targetDecomposeDimensionDTO.getTargetDecomposeDimensionId();
            targetDecomposeDimensionDTO.setSort(i);
            if (StringUtils.isNull(targetDecomposeDimensionId)) {
                throw new ServiceException("id不能为空！");
            }
            targetDecomposeDimensionIds.add(targetDecomposeDimensionDTO.getTargetDecomposeDimensionId());
            i++;
        }
        List<TargetDecomposeDimensionDTO> targetDecomposeDimensionByIds = targetDecomposeDimensionMapper.isExist(targetDecomposeDimensionIds);
        if (targetDecomposeDimensionByIds.size() < targetDecomposeDimensionIds.size()) {//查询到的数量小于ids的数量
            throw new ServiceException("数据已经不存在");
        }
        List<TargetDecomposeDimension> targetDecomposeDimensionList = new ArrayList<>();
        TargetDecomposeDimension targetDecomposeDimension;
        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
            targetDecomposeDimension = new TargetDecomposeDimension();
            BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
            targetDecomposeDimension.setUpdateTime(DateUtils.getNowDate());
            targetDecomposeDimension.setUpdateBy(SecurityUtils.getUserId());
            targetDecomposeDimensionList.add(targetDecomposeDimension);
        }
        return targetDecomposeDimensionMapper.updateTargetDecomposeDimensions(targetDecomposeDimensionList);
    }

    /**
     * 校验分解维度的唯一性
     *
     * @param dimension 分解维度
     * @return boolean
     */
    private boolean checkUnique(String dimension) {
        int count = targetDecomposeDimensionMapper.checkDimension(dimension);
        if (count > 0) {
            return true;
        }
        return false;
    }

    /**
     * 分解维度引用校验
     *
     * @param targetDecomposeDimensionIds
     * @return
     */
    private boolean isQuote(List<Long> targetDecomposeDimensionIds) {
        //todo 引用校验
        return false;
    }
}

