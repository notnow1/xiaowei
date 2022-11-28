package net.qixiaowei.operate.cloud.service.impl.performance;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalColumns;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalColumnsDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalColumnsMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalColumnsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * PerformanceAppraisalColumnsService业务层处理
 *
 * @author Graves
 * @since 2022-11-28
 */
@Service
public class PerformanceAppraisalColumnsServiceImpl implements IPerformanceAppraisalColumnsService {
    @Autowired
    private PerformanceAppraisalColumnsMapper performanceAppraisalColumnsMapper;

    /**
     * 查询绩效考核自定义列表
     *
     * @param performAppraisalColumnsId 绩效考核自定义列表主键
     * @return 绩效考核自定义列表
     */
    @Override
    public PerformanceAppraisalColumnsDTO selectPerformanceAppraisalColumnsByPerformAppraisalColumnsId(Long performAppraisalColumnsId) {
        return performanceAppraisalColumnsMapper.selectPerformanceAppraisalColumnsByPerformAppraisalColumnsId(performAppraisalColumnsId);
    }

    /**
     * 查询绩效考核自定义列表列表
     *
     * @param performanceAppraisalColumnsDTO 绩效考核自定义列表
     * @return 绩效考核自定义列表
     */
    @Override
    public List<PerformanceAppraisalColumnsDTO> selectPerformanceAppraisalColumnsList(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO) {
        PerformanceAppraisalColumns performanceAppraisalColumns = new PerformanceAppraisalColumns();
        BeanUtils.copyProperties(performanceAppraisalColumnsDTO, performanceAppraisalColumns);
        return performanceAppraisalColumnsMapper.selectPerformanceAppraisalColumnsList(performanceAppraisalColumns);
    }

    /**
     * 新增绩效考核自定义列表
     *
     * @param performanceAppraisalColumnsDTO 绩效考核自定义列表
     * @return 结果
     */
    @Override
    public PerformanceAppraisalColumnsDTO insertPerformanceAppraisalColumns(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO) {
        PerformanceAppraisalColumns performanceAppraisalColumns = new PerformanceAppraisalColumns();
        BeanUtils.copyProperties(performanceAppraisalColumnsDTO, performanceAppraisalColumns);
        performanceAppraisalColumns.setCreateBy(SecurityUtils.getUserId());
        performanceAppraisalColumns.setCreateTime(DateUtils.getNowDate());
        performanceAppraisalColumns.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalColumns.setUpdateBy(SecurityUtils.getUserId());
        performanceAppraisalColumns.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        performanceAppraisalColumnsMapper.insertPerformanceAppraisalColumns(performanceAppraisalColumns);
        performanceAppraisalColumnsDTO.setPerformAppraisalColumnsId(performanceAppraisalColumns.getPerformAppraisalColumnsId());
        return performanceAppraisalColumnsDTO;
    }

    /**
     * 修改绩效考核自定义列表
     *
     * @param performanceAppraisalColumnsDTO 绩效考核自定义列表
     * @return 结果
     */
    @Override
    public int updatePerformanceAppraisalColumns(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO) {
        PerformanceAppraisalColumns performanceAppraisalColumns = new PerformanceAppraisalColumns();
        BeanUtils.copyProperties(performanceAppraisalColumnsDTO, performanceAppraisalColumns);
        performanceAppraisalColumns.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalColumns.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalColumnsMapper.updatePerformanceAppraisalColumns(performanceAppraisalColumns);
    }

    /**
     * 逻辑批量删除绩效考核自定义列表
     *
     * @param performAppraisalColumnsIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(List<Long> performAppraisalColumnsIds) {
        return performanceAppraisalColumnsMapper.logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(performAppraisalColumnsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效考核自定义列表信息
     *
     * @param performAppraisalColumnsId 绩效考核自定义列表主键
     * @return 结果
     */
    @Override
    public int deletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(Long performAppraisalColumnsId) {
        return performanceAppraisalColumnsMapper.deletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(performAppraisalColumnsId);
    }

    /**
     * 根据考核ID查询自定义表
     *
     * @param performanceAppraisalId 绩效考核ID
     * @return
     */
    @Override
    public List<PerformanceAppraisalColumnsDTO> selectAppraisalColumnsByAppraisalId(Long performanceAppraisalId) {
        return performanceAppraisalColumnsMapper.selectAppraisalColumnsByAppraisalId(performanceAppraisalId);
    }

    /**
     * 逻辑删除绩效考核自定义列表信息
     *
     * @param performanceAppraisalColumnsDTO 绩效考核自定义列表
     * @return 结果
     */
    @Override
    public int logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO) {
        PerformanceAppraisalColumns performanceAppraisalColumns = new PerformanceAppraisalColumns();
        performanceAppraisalColumns.setPerformAppraisalColumnsId(performanceAppraisalColumnsDTO.getPerformAppraisalColumnsId());
        performanceAppraisalColumns.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalColumns.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalColumnsMapper.logicDeletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(performanceAppraisalColumns);
    }

    /**
     * 物理删除绩效考核自定义列表信息
     *
     * @param performanceAppraisalColumnsDTO 绩效考核自定义列表
     * @return 结果
     */

    @Override
    public int deletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO) {
        PerformanceAppraisalColumns performanceAppraisalColumns = new PerformanceAppraisalColumns();
        BeanUtils.copyProperties(performanceAppraisalColumnsDTO, performanceAppraisalColumns);
        return performanceAppraisalColumnsMapper.deletePerformanceAppraisalColumnsByPerformAppraisalColumnsId(performanceAppraisalColumns.getPerformAppraisalColumnsId());
    }

    /**
     * 物理批量删除绩效考核自定义列表
     *
     * @param performanceAppraisalColumnsDtos 需要删除的绩效考核自定义列表主键
     * @return 结果
     */

    @Override
    public int deletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDtos) {
        List<Long> stringList = new ArrayList();
        for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : performanceAppraisalColumnsDtos) {
            stringList.add(performanceAppraisalColumnsDTO.getPerformAppraisalColumnsId());
        }
        return performanceAppraisalColumnsMapper.deletePerformanceAppraisalColumnsByPerformAppraisalColumnsIds(stringList);
    }

    /**
     * 批量新增绩效考核自定义列表信息
     *
     * @param performanceAppraisalColumnsDtos 绩效考核自定义列表对象
     */

    public int insertPerformanceAppraisalColumnss(List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDtos) {
        List<PerformanceAppraisalColumns> performanceAppraisalColumnsList = new ArrayList();

        for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : performanceAppraisalColumnsDtos) {
            PerformanceAppraisalColumns performanceAppraisalColumns = new PerformanceAppraisalColumns();
            BeanUtils.copyProperties(performanceAppraisalColumnsDTO, performanceAppraisalColumns);
            performanceAppraisalColumns.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisalColumns.setCreateTime(DateUtils.getNowDate());
            performanceAppraisalColumns.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalColumns.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalColumns.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceAppraisalColumnsList.add(performanceAppraisalColumns);
        }
        return performanceAppraisalColumnsMapper.batchPerformanceAppraisalColumns(performanceAppraisalColumnsList);
    }

    /**
     * 批量修改绩效考核自定义列表信息
     *
     * @param performanceAppraisalColumnsDtos 绩效考核自定义列表对象
     */

    public int updatePerformanceAppraisalColumnss(List<PerformanceAppraisalColumnsDTO> performanceAppraisalColumnsDtos) {
        List<PerformanceAppraisalColumns> performanceAppraisalColumnsList = new ArrayList();

        for (PerformanceAppraisalColumnsDTO performanceAppraisalColumnsDTO : performanceAppraisalColumnsDtos) {
            PerformanceAppraisalColumns performanceAppraisalColumns = new PerformanceAppraisalColumns();
            BeanUtils.copyProperties(performanceAppraisalColumnsDTO, performanceAppraisalColumns);
            performanceAppraisalColumns.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisalColumns.setCreateTime(DateUtils.getNowDate());
            performanceAppraisalColumns.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalColumns.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalColumnsList.add(performanceAppraisalColumns);
        }
        return performanceAppraisalColumnsMapper.updatePerformanceAppraisalColumnss(performanceAppraisalColumnsList);
    }
}

