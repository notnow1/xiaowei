package net.qixiaowei.operate.cloud.service.impl.performance;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformAppraisalEvaluate;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformAppraisalEvaluateDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformAppraisalEvaluateMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformAppraisalEvaluateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * PerformAppraisalEvaluateService业务层处理
 *
 * @author Graves
 * @since 2023-03-23
 */
@Service
public class PerformAppraisalEvaluateServiceImpl implements IPerformAppraisalEvaluateService {
    @Autowired
    private PerformAppraisalEvaluateMapper performAppraisalEvaluateMapper;

    /**
     * 查询绩效考核评议表
     *
     * @param performAppraisalEvaluateId 绩效考核评议表主键
     * @return 绩效考核评议表
     */
    @Override
    public PerformAppraisalEvaluateDTO selectPerformAppraisalEvaluateByPerformAppraisalEvaluateId(Long performAppraisalEvaluateId) {
        return performAppraisalEvaluateMapper.selectPerformAppraisalEvaluateByPerformAppraisalEvaluateId(performAppraisalEvaluateId);
    }

    /**
     * 查询绩效考核评议表列表
     *
     * @param performAppraisalEvaluateDTO 绩效考核评议表
     * @return 绩效考核评议表
     */
    @Override
    public List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateList(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO) {
        PerformAppraisalEvaluate performAppraisalEvaluate = new PerformAppraisalEvaluate();
        BeanUtils.copyProperties(performAppraisalEvaluateDTO, performAppraisalEvaluate);
        return performAppraisalEvaluateMapper.selectPerformAppraisalEvaluateList(performAppraisalEvaluate);
    }

    /**
     * 新增绩效考核评议表
     *
     * @param performAppraisalEvaluateDTO 绩效考核评议表
     * @return 结果
     */
    @Override
    public PerformAppraisalEvaluateDTO insertPerformAppraisalEvaluate(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO) {
        PerformAppraisalEvaluate performAppraisalEvaluate = new PerformAppraisalEvaluate();
        BeanUtils.copyProperties(performAppraisalEvaluateDTO, performAppraisalEvaluate);
        performAppraisalEvaluate.setCreateBy(SecurityUtils.getUserId());
        performAppraisalEvaluate.setCreateTime(DateUtils.getNowDate());
        performAppraisalEvaluate.setUpdateTime(DateUtils.getNowDate());
        performAppraisalEvaluate.setUpdateBy(SecurityUtils.getUserId());
        performAppraisalEvaluate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        performAppraisalEvaluateMapper.insertPerformAppraisalEvaluate(performAppraisalEvaluate);
        performAppraisalEvaluateDTO.setPerformAppraisalEvaluateId(performAppraisalEvaluate.getPerformAppraisalEvaluateId());
        return performAppraisalEvaluateDTO;
    }

    /**
     * 修改绩效考核评议表
     *
     * @param performAppraisalEvaluateDTO 绩效考核评议表
     * @return 结果
     */
    @Override
    public int updatePerformAppraisalEvaluate(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO) {
        PerformAppraisalEvaluate performAppraisalEvaluate = new PerformAppraisalEvaluate();
        BeanUtils.copyProperties(performAppraisalEvaluateDTO, performAppraisalEvaluate);
        performAppraisalEvaluate.setUpdateTime(DateUtils.getNowDate());
        performAppraisalEvaluate.setUpdateBy(SecurityUtils.getUserId());
        return performAppraisalEvaluateMapper.updatePerformAppraisalEvaluate(performAppraisalEvaluate);
    }

    /**
     * 逻辑批量删除绩效考核评议表
     *
     * @param performAppraisalEvaluateIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(List<Long> performAppraisalEvaluateIds) {
        return performAppraisalEvaluateMapper.logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(performAppraisalEvaluateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效考核评议表信息
     *
     * @param performAppraisalEvaluateId 绩效考核评议表主键
     * @return 结果
     */
    @Override
    public int deletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(Long performAppraisalEvaluateId) {
        return performAppraisalEvaluateMapper.deletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(performAppraisalEvaluateId);
    }

    /**
     * 根据指标ID集合查询评议列表
     *
     * @param delPerformanceAppraisalItemIds 指标ID集合
     * @return List
     */
    @Override
    public List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateByPerformAppraisalItemIds(List<Long> delPerformanceAppraisalItemIds) {
        return performAppraisalEvaluateMapper.selectPerformAppraisalEvaluateByPerformAppraisalItemIds(delPerformanceAppraisalItemIds);
    }

    /**
     * 根据对象id获取评议周期列表
     *
     * @param performAppraisalObjectsId 对象id
     * @return 结果
     */
    @Override
    public List<PerformAppraisalEvaluateDTO> selectPerformAppraisalEvaluateByPerformAppraisalObjectId(Long performAppraisalObjectsId) {
        return performAppraisalEvaluateMapper.selectPerformAppraisalEvaluateByPerformAppraisalObjectId(performAppraisalObjectsId);
    }

    /**
     * 逻辑删除绩效考核评议表信息
     *
     * @param performAppraisalEvaluateDTO 绩效考核评议表
     * @return 结果
     */
    @Override
    public int logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO) {
        PerformAppraisalEvaluate performAppraisalEvaluate = new PerformAppraisalEvaluate();
        performAppraisalEvaluate.setPerformAppraisalEvaluateId(performAppraisalEvaluateDTO.getPerformAppraisalEvaluateId());
        performAppraisalEvaluate.setUpdateTime(DateUtils.getNowDate());
        performAppraisalEvaluate.setUpdateBy(SecurityUtils.getUserId());
        return performAppraisalEvaluateMapper.logicDeletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(performAppraisalEvaluate);
    }

    /**
     * 物理删除绩效考核评议表信息
     *
     * @param performAppraisalEvaluateDTO 绩效考核评议表
     * @return 结果
     */

    @Override
    public int deletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO) {
        PerformAppraisalEvaluate performAppraisalEvaluate = new PerformAppraisalEvaluate();
        BeanUtils.copyProperties(performAppraisalEvaluateDTO, performAppraisalEvaluate);
        return performAppraisalEvaluateMapper.deletePerformAppraisalEvaluateByPerformAppraisalEvaluateId(performAppraisalEvaluate.getPerformAppraisalEvaluateId());
    }

    /**
     * 物理批量删除绩效考核评议表
     *
     * @param performAppraisalEvaluateDtos 需要删除的绩效考核评议表主键
     * @return 结果
     */

    @Override
    public int deletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO : performAppraisalEvaluateDtos) {
            stringList.add(performAppraisalEvaluateDTO.getPerformAppraisalEvaluateId());
        }
        return performAppraisalEvaluateMapper.deletePerformAppraisalEvaluateByPerformAppraisalEvaluateIds(stringList);
    }

    /**
     * 批量新增绩效考核评议表信息
     *
     * @param performAppraisalEvaluateDtos 绩效考核评议表对象
     */

    public int insertPerformAppraisalEvaluates(List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDtos) {
        List<PerformAppraisalEvaluate> performAppraisalEvaluateList = new ArrayList<>();

        for (PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO : performAppraisalEvaluateDtos) {
            PerformAppraisalEvaluate performAppraisalEvaluate = new PerformAppraisalEvaluate();
            BeanUtils.copyProperties(performAppraisalEvaluateDTO, performAppraisalEvaluate);
            performAppraisalEvaluate.setCreateBy(SecurityUtils.getUserId());
            performAppraisalEvaluate.setCreateTime(DateUtils.getNowDate());
            performAppraisalEvaluate.setUpdateTime(DateUtils.getNowDate());
            performAppraisalEvaluate.setUpdateBy(SecurityUtils.getUserId());
            performAppraisalEvaluate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performAppraisalEvaluateList.add(performAppraisalEvaluate);
        }
        return performAppraisalEvaluateMapper.batchPerformAppraisalEvaluate(performAppraisalEvaluateList);
    }

    /**
     * 批量修改绩效考核评议表信息
     *
     * @param performAppraisalEvaluateDtos 绩效考核评议表对象
     */

    public int updatePerformAppraisalEvaluates(List<PerformAppraisalEvaluateDTO> performAppraisalEvaluateDtos) {
        List<PerformAppraisalEvaluate> performAppraisalEvaluateList = new ArrayList<>();

        for (PerformAppraisalEvaluateDTO performAppraisalEvaluateDTO : performAppraisalEvaluateDtos) {
            PerformAppraisalEvaluate performAppraisalEvaluate = new PerformAppraisalEvaluate();
            BeanUtils.copyProperties(performAppraisalEvaluateDTO, performAppraisalEvaluate);
            performAppraisalEvaluate.setCreateBy(SecurityUtils.getUserId());
            performAppraisalEvaluate.setCreateTime(DateUtils.getNowDate());
            performAppraisalEvaluate.setUpdateTime(DateUtils.getNowDate());
            performAppraisalEvaluate.setUpdateBy(SecurityUtils.getUserId());
            performAppraisalEvaluateList.add(performAppraisalEvaluate);
        }
        return performAppraisalEvaluateMapper.updatePerformAppraisalEvaluates(performAppraisalEvaluateList);
    }

}

