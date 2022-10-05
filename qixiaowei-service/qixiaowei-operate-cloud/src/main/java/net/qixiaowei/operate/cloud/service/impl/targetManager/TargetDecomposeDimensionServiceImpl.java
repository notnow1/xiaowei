package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
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
        //校验
        String decompositionDimension = targetDecomposeDimensionDTO.getDecompositionDimension();
        if (StringUtils.isEmpty(decompositionDimension)) {
            throw new ServiceException("分解维度不能为空");
        }
        if (checkUnique(decompositionDimension)) {
            throw new ServiceException("分解维度不能重复");
        }
        TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
        BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
        int maxSort = targetDecomposeDimensionMapper.getMaxTargetDimensionConfigSort() + 1;
        targetDecomposeDimension.setDeleteFlag(0);
        targetDecomposeDimension.setSort(maxSort);
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
        int exist = targetDecomposeDimensionMapper.isExist(targetDecomposeDimensionIds);
        if (exist == 0) {
            throw new ServiceException("目标分解维度配置不存在");
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
        if (isQuote(targetDecomposeDimensionId)) {
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
     * @param targetDecomposeDimensionDto 目标分解维度配置对象
     */
    @Transactional
    public int updateTargetDecomposeDimensions(List<TargetDecomposeDimensionDTO> targetDecomposeDimensionDtos) {
//        ArrayList<Integer> sorts = new ArrayList<>();
//        targetDecomposeDimensionDTO.getTargetDecomposeDimensionId()
//        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
//            //校验
//            if (targetDecomposeDimensionDTO.getSort() == null &&  == null) {
//                return AjaxResult.error("sort与id不能为空");
//            }
//            if (sorts.contains(targetDecomposeDimensionDTO.getSort())) {
//                return AjaxResult.error("sort存在重复");
//            }
//            sorts.add(targetDecomposeDimensionDTO.getSort());
//        }

        List<TargetDecomposeDimension> targetDecomposeDimensionList = new ArrayList<>();
        for (TargetDecomposeDimensionDTO targetDecomposeDimensionDTO : targetDecomposeDimensionDtos) {
            TargetDecomposeDimensionDTO dto = targetDecomposeDimensionMapper.selectTargetDecomposeDimensionByTargetDecomposeDimensionId(targetDecomposeDimensionDTO.getTargetDecomposeDimensionId());
            if (dto == null) {
                throw new ServiceException("数据已经不存在");
            }
            TargetDecomposeDimension targetDecomposeDimension = new TargetDecomposeDimension();
            BeanUtils.copyProperties(targetDecomposeDimensionDTO, targetDecomposeDimension);
            targetDecomposeDimension.setUpdateTime(DateUtils.getNowDate());
//            targetDecomposeDimension.setCreateBy(SecurityUtils.getUserId());
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
     * @param targetDecomposeDimensionId
     * @return
     */
    private boolean isQuote(Long targetDecomposeDimensionId) {
        //todo 引用校验
        return false;
    }
}

