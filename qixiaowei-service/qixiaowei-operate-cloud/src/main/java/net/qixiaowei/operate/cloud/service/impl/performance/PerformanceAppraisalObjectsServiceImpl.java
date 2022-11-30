package net.qixiaowei.operate.cloud.service.impl.performance;

import java.util.List;

import net.qixiaowei.integration.common.utils.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalObjects;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalObjectsExcel;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalObjectsDTO;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalObjectsMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalObjectsService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;


/**
 * PerformanceAppraisalObjectsService业务层处理
 *
 * @author Graves
 * @since 2022-11-23
 */
@Service
public class PerformanceAppraisalObjectsServiceImpl implements IPerformanceAppraisalObjectsService {
    @Autowired
    private PerformanceAppraisalObjectsMapper performanceAppraisalObjectsMapper;

    /**
     * 查询绩效考核对象表
     *
     * @param performAppraisalObjectsId 绩效考核对象表主键
     * @return 绩效考核对象表
     */
    @Override
    public PerformanceAppraisalObjectsDTO selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(Long performAppraisalObjectsId) {
        return performanceAppraisalObjectsMapper.selectPerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
    }

    /**
     * 查询绩效考核对象表列表
     *
     * @param performanceAppraisalObjectsDTO 绩效考核对象表
     * @return 绩效考核对象表
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsList(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
        BeanUtils.copyProperties(performanceAppraisalObjectsDTO, performanceAppraisalObjects);
        return performanceAppraisalObjectsMapper.selectPerformanceAppraisalObjectsList(performanceAppraisalObjects);
    }

    /**
     * 新增绩效考核对象表
     *
     * @param performanceAppraisalObjectsDTO 绩效考核对象表
     * @return 结果
     */
    @Override
    public PerformanceAppraisalObjectsDTO insertPerformanceAppraisalObjects(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
        BeanUtils.copyProperties(performanceAppraisalObjectsDTO, performanceAppraisalObjects);
        performanceAppraisalObjects.setCreateBy(SecurityUtils.getUserId());
        performanceAppraisalObjects.setCreateTime(DateUtils.getNowDate());
        performanceAppraisalObjects.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalObjects.setUpdateBy(SecurityUtils.getUserId());
        performanceAppraisalObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        performanceAppraisalObjectsMapper.insertPerformanceAppraisalObjects(performanceAppraisalObjects);
        performanceAppraisalObjectsDTO.setPerformAppraisalObjectsId(performanceAppraisalObjects.getPerformAppraisalObjectsId());
        return performanceAppraisalObjectsDTO;
    }

    /**
     * 修改绩效考核对象表
     *
     * @param performanceAppraisalObjectsDTO 绩效考核对象表
     * @return 结果
     */
    @Override
    public int updatePerformanceAppraisalObjects(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
        BeanUtils.copyProperties(performanceAppraisalObjectsDTO, performanceAppraisalObjects);
        performanceAppraisalObjects.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalObjects.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalObjectsMapper.updatePerformanceAppraisalObjects(performanceAppraisalObjects);
    }

    /**
     * 逻辑批量删除绩效考核对象表
     *
     * @param performAppraisalObjectsIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(List<Long> performAppraisalObjectsIds) {
        return performanceAppraisalObjectsMapper.logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(performAppraisalObjectsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效考核对象表信息
     *
     * @param performAppraisalObjectsId 绩效考核对象表主键
     * @return 结果
     */
    @Override
    public int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(Long performAppraisalObjectsId) {
        return performanceAppraisalObjectsMapper.deletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(performAppraisalObjectsId);
    }

    /**
     * 逻辑删除绩效考核对象表信息
     *
     * @param performanceAppraisalObjectsDTO 绩效考核对象表
     * @return 结果
     */
    @Override
    public int logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
        performanceAppraisalObjects.setPerformAppraisalObjectsId(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
        performanceAppraisalObjects.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalObjects.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalObjectsMapper.logicDeletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(performanceAppraisalObjects);
    }

    /**
     * 物理删除绩效考核对象表信息
     *
     * @param performanceAppraisalObjectsDTO 绩效考核对象表
     * @return 结果
     */

    @Override
    public int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
        BeanUtils.copyProperties(performanceAppraisalObjectsDTO, performanceAppraisalObjects);
        return performanceAppraisalObjectsMapper.deletePerformanceAppraisalObjectsByPerformAppraisalObjectsId(performanceAppraisalObjects.getPerformAppraisalObjectsId());
    }

    /**
     * 物理批量删除绩效考核对象表
     *
     * @param performanceAppraisalObjectsDtos 需要删除的绩效考核对象表主键
     * @return 结果
     */

    @Override
    public int deletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDtos) {
        List<Long> stringList = new ArrayList();
        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDtos) {
            stringList.add(performanceAppraisalObjectsDTO.getPerformAppraisalObjectsId());
        }
        return performanceAppraisalObjectsMapper.deletePerformanceAppraisalObjectsByPerformAppraisalObjectsIds(stringList);
    }

    /**
     * 批量新增绩效考核对象表信息
     *
     * @param performanceAppraisalObjectsDtos 绩效考核对象表对象
     */

    public int insertPerformanceAppraisalObjectss(List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDtos) {
        List<PerformanceAppraisalObjects> performanceAppraisalObjectsList = new ArrayList();

        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDtos) {
            PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
            BeanUtils.copyProperties(performanceAppraisalObjectsDTO, performanceAppraisalObjects);
            performanceAppraisalObjects.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisalObjects.setCreateTime(DateUtils.getNowDate());
            performanceAppraisalObjects.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalObjects.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceAppraisalObjectsList.add(performanceAppraisalObjects);
        }
        return performanceAppraisalObjectsMapper.batchPerformanceAppraisalObjects(performanceAppraisalObjectsList);
    }

    /**
     * 批量修改绩效考核对象表信息
     *
     * @param performanceAppraisalObjectsDtos 绩效考核对象表对象
     */

    public int updatePerformanceAppraisalObjectss(List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDtos) {
        List<PerformanceAppraisalObjects> performanceAppraisalObjectsList = new ArrayList();

        for (PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO : performanceAppraisalObjectsDtos) {
            PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
            BeanUtils.copyProperties(performanceAppraisalObjectsDTO, performanceAppraisalObjects);
            performanceAppraisalObjects.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalObjects.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalObjectsList.add(performanceAppraisalObjects);
        }
        return performanceAppraisalObjectsMapper.updatePerformanceAppraisalObjectss(performanceAppraisalObjectsList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importPerformanceAppraisalObjects(List<PerformanceAppraisalObjectsExcel> list) {
        List<PerformanceAppraisalObjects> performanceAppraisalObjectsList = new ArrayList<>();
        list.forEach(l -> {
            PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
            BeanUtils.copyProperties(l, performanceAppraisalObjects);
            performanceAppraisalObjects.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisalObjects.setCreateTime(DateUtils.getNowDate());
            performanceAppraisalObjects.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalObjects.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalObjects.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceAppraisalObjectsList.add(performanceAppraisalObjects);
        });
        try {
            performanceAppraisalObjectsMapper.batchPerformanceAppraisalObjects(performanceAppraisalObjectsList);
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核对象表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param performanceAppraisalObjectsDTO
     * @return
     */
    @Override
    public List<PerformanceAppraisalObjectsExcel> exportPerformanceAppraisalObjects(PerformanceAppraisalObjectsDTO performanceAppraisalObjectsDTO) {
        PerformanceAppraisalObjects performanceAppraisalObjects = new PerformanceAppraisalObjects();
        BeanUtils.copyProperties(performanceAppraisalObjectsDTO, performanceAppraisalObjects);
        List<PerformanceAppraisalObjectsDTO> performanceAppraisalObjectsDTOList = performanceAppraisalObjectsMapper.selectPerformanceAppraisalObjectsList(performanceAppraisalObjects);
        List<PerformanceAppraisalObjectsExcel> performanceAppraisalObjectsExcelList = new ArrayList<>();
        return performanceAppraisalObjectsExcelList;
    }

    /**
     * 查询组织绩效任务考核详情
     *
     * @param performanceAppraisalId
     * @return
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalId(Long performanceAppraisalId) {
        return performanceAppraisalObjectsMapper.selectPerformanceAppraisalObjectsByPerformAppraisalId(performanceAppraisalId);
    }

    /**
     * 查询组织绩效归档结果排名
     *
     * @param appraisalObjectsIds
     * @param performanceAppraisalId
     * @return
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByIds(List<Long> appraisalObjectsIds, Long performanceAppraisalId) {
        return performanceAppraisalObjectsMapper.selectPerformanceAppraisalObjectsByIds(appraisalObjectsIds, performanceAppraisalId);
    }

    /**
     * 根据考核任务ID集合查找考核对象表
     *
     * @param performanceAppraisalIds
     * @return
     */
    @Override
    public List<PerformanceAppraisalObjectsDTO> selectPerformanceAppraisalObjectsByPerformAppraisalIds(List<Long> performanceAppraisalIds) {
        return performanceAppraisalObjectsMapper.selectPerformanceAppraisalObjectsByPerformAppraisalIds(performanceAppraisalIds);
    }
}

