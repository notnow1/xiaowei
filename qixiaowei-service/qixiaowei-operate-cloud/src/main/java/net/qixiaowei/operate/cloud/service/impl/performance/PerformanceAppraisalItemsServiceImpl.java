package net.qixiaowei.operate.cloud.service.impl.performance;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformanceAppraisalItems;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformanceAppraisalItemsDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformanceAppraisalItemsExcel;
import net.qixiaowei.operate.cloud.mapper.performance.PerformanceAppraisalItemsMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformanceAppraisalItemsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


/**
 * PerformanceAppraisalItemsService业务层处理
 *
 * @author Graves
 * @since 2022-12-06
 */
@Service
public class PerformanceAppraisalItemsServiceImpl implements IPerformanceAppraisalItemsService {
    @Autowired
    private PerformanceAppraisalItemsMapper performanceAppraisalItemsMapper;

    /**
     * 查询绩效考核项目表
     *
     * @param performAppraisalItemsId 绩效考核项目表主键
     * @return 绩效考核项目表
     */
    @Override
    public PerformanceAppraisalItemsDTO selectPerformanceAppraisalItemsByPerformAppraisalItemsId(Long performAppraisalItemsId) {
        return performanceAppraisalItemsMapper.selectPerformanceAppraisalItemsByPerformAppraisalItemsId(performAppraisalItemsId);
    }

    /**
     * 查询绩效考核项目表列表
     *
     * @param performanceAppraisalItemsDTO 绩效考核项目表
     * @return 绩效考核项目表
     */
    @Override
    public List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsList(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO) {
        PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
        BeanUtils.copyProperties(performanceAppraisalItemsDTO, performanceAppraisalItems);
        return performanceAppraisalItemsMapper.selectPerformanceAppraisalItemsList(performanceAppraisalItems);
    }

    /**
     * 新增绩效考核项目表
     *
     * @param performanceAppraisalItemsDTO 绩效考核项目表
     * @return 结果
     */
    @Override
    public PerformanceAppraisalItemsDTO insertPerformanceAppraisalItems(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO) {
        PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
        BeanUtils.copyProperties(performanceAppraisalItemsDTO, performanceAppraisalItems);
        performanceAppraisalItems.setCreateBy(SecurityUtils.getUserId());
        performanceAppraisalItems.setCreateTime(DateUtils.getNowDate());
        performanceAppraisalItems.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalItems.setUpdateBy(SecurityUtils.getUserId());
        performanceAppraisalItems.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        performanceAppraisalItemsMapper.insertPerformanceAppraisalItems(performanceAppraisalItems);
        performanceAppraisalItemsDTO.setPerformAppraisalItemsId(performanceAppraisalItems.getPerformAppraisalItemsId());
        return performanceAppraisalItemsDTO;
    }

    /**
     * 修改绩效考核项目表
     *
     * @param performanceAppraisalItemsDTO 绩效考核项目表
     * @return 结果
     */
    @Override
    public int updatePerformanceAppraisalItems(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO) {
        PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
        BeanUtils.copyProperties(performanceAppraisalItemsDTO, performanceAppraisalItems);
        performanceAppraisalItems.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalItems.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalItemsMapper.updatePerformanceAppraisalItems(performanceAppraisalItems);
    }

    /**
     * 逻辑批量删除绩效考核项目表
     *
     * @param performAppraisalItemsIds 主键集合
     */
    @Override
    public void logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsIds(List<Long> performAppraisalItemsIds) {
        performanceAppraisalItemsMapper.logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsIds(performAppraisalItemsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效考核项目表信息
     *
     * @param performAppraisalItemsId 绩效考核项目表主键
     * @return 结果
     */
    @Override
    public int deletePerformanceAppraisalItemsByPerformAppraisalItemsId(Long performAppraisalItemsId) {
        return performanceAppraisalItemsMapper.deletePerformanceAppraisalItemsByPerformAppraisalItemsId(performAppraisalItemsId);
    }

    /**
     * 逻辑删除绩效考核项目表信息
     *
     * @param performanceAppraisalItemsDTO 绩效考核项目表
     * @return 结果
     */
    @Override
    public int logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsId(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO) {
        PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
        performanceAppraisalItems.setPerformAppraisalItemsId(performanceAppraisalItemsDTO.getPerformAppraisalItemsId());
        performanceAppraisalItems.setUpdateTime(DateUtils.getNowDate());
        performanceAppraisalItems.setUpdateBy(SecurityUtils.getUserId());
        return performanceAppraisalItemsMapper.logicDeletePerformanceAppraisalItemsByPerformAppraisalItemsId(performanceAppraisalItems);
    }

    /**
     * 物理删除绩效考核项目表信息
     *
     * @param performanceAppraisalItemsDTO 绩效考核项目表
     * @return 结果
     */

    @Override
    public int deletePerformanceAppraisalItemsByPerformAppraisalItemsId(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO) {
        PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
        BeanUtils.copyProperties(performanceAppraisalItemsDTO, performanceAppraisalItems);
        return performanceAppraisalItemsMapper.deletePerformanceAppraisalItemsByPerformAppraisalItemsId(performanceAppraisalItems.getPerformAppraisalItemsId());
    }

    /**
     * 物理批量删除绩效考核项目表
     *
     * @param performanceAppraisalItemsDtos 需要删除的绩效考核项目表主键
     * @return 结果
     */

    @Override
    public int deletePerformanceAppraisalItemsByPerformAppraisalItemsIds(List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsDtos) {
            stringList.add(performanceAppraisalItemsDTO.getPerformAppraisalItemsId());
        }
        return performanceAppraisalItemsMapper.deletePerformanceAppraisalItemsByPerformAppraisalItemsIds(stringList);
    }

    /**
     * 批量新增绩效考核项目表信息
     *
     * @param performanceAppraisalItemsDtos 绩效考核项目表对象
     */

    @Override
    public List<PerformanceAppraisalItems> insertPerformanceAppraisalItemss(List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDtos) {
        List<PerformanceAppraisalItems> performanceAppraisalItemsList = new ArrayList<>();

        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsDtos) {
            PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
            BeanUtils.copyProperties(performanceAppraisalItemsDTO, performanceAppraisalItems);
            performanceAppraisalItems.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisalItems.setCreateTime(DateUtils.getNowDate());
            performanceAppraisalItems.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalItems.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalItems.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceAppraisalItemsList.add(performanceAppraisalItems);
        }
        performanceAppraisalItemsMapper.batchPerformanceAppraisalItems(performanceAppraisalItemsList);
        return performanceAppraisalItemsList;
    }

    /**
     * 批量修改绩效考核项目表信息
     *
     * @param performanceAppraisalItemsDtoS 绩效考核项目表对象
     */
    @Override
    public void updatePerformanceAppraisalItemsS(List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDtoS) {
        List<PerformanceAppraisalItems> performanceAppraisalItemsList = new ArrayList<>();
        for (PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO : performanceAppraisalItemsDtoS) {
            PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
            BeanUtils.copyProperties(performanceAppraisalItemsDTO, performanceAppraisalItems);
            performanceAppraisalItems.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalItems.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalItemsList.add(performanceAppraisalItems);
        }
        performanceAppraisalItemsMapper.updatePerformanceAppraisalItemss(performanceAppraisalItemsList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importPerformanceAppraisalItems(List<PerformanceAppraisalItemsExcel> list) {
        List<PerformanceAppraisalItems> performanceAppraisalItemsList = new ArrayList<>();
        list.forEach(l -> {
            PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
            BeanUtils.copyProperties(l, performanceAppraisalItems);
            performanceAppraisalItems.setCreateBy(SecurityUtils.getUserId());
            performanceAppraisalItems.setCreateTime(DateUtils.getNowDate());
            performanceAppraisalItems.setUpdateTime(DateUtils.getNowDate());
            performanceAppraisalItems.setUpdateBy(SecurityUtils.getUserId());
            performanceAppraisalItems.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performanceAppraisalItemsList.add(performanceAppraisalItems);
        });
        try {
            performanceAppraisalItemsMapper.batchPerformanceAppraisalItems(performanceAppraisalItemsList);
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核项目表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param performanceAppraisalItemsDTO
     * @return
     */
    @Override
    public List<PerformanceAppraisalItemsExcel> exportPerformanceAppraisalItems(PerformanceAppraisalItemsDTO performanceAppraisalItemsDTO) {
        PerformanceAppraisalItems performanceAppraisalItems = new PerformanceAppraisalItems();
        BeanUtils.copyProperties(performanceAppraisalItemsDTO, performanceAppraisalItems);
        List<PerformanceAppraisalItemsDTO> performanceAppraisalItemsDTOList = performanceAppraisalItemsMapper.selectPerformanceAppraisalItemsList(performanceAppraisalItems);
        List<PerformanceAppraisalItemsExcel> performanceAppraisalItemsExcelList = new ArrayList<>();
        return performanceAppraisalItemsExcelList;
    }

    /**
     * 根据对象ID查找指标列表
     *
     * @param performAppraisalObjectsId 对象ID
     * @return
     */
    @Override
    public List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsByPerformAppraisalObjectId(Long performAppraisalObjectsId) {
        return performanceAppraisalItemsMapper.selectPerformanceAppraisalItemsByPerformAppraisalObjectId(performAppraisalObjectsId);
    }

    /**
     * 评议撤回
     *
     * @param itemsDTOList 评议指标DTO
     * @return int
     */
    @Override
    public int withdrawPerformanceAppraisalItems(List<PerformanceAppraisalItemsDTO> itemsDTOList) {
        return performanceAppraisalItemsMapper.withdrawPerformanceAppraisalItems(itemsDTOList);
    }

    /**
     * 查询匹配的对象指标列表
     *
     * @param performanceAppraisalObjectsIds 对象ID集合
     * @return List
     */
    @Override
    public List<PerformanceAppraisalItemsDTO> selectPerformanceAppraisalItemsByPerformAppraisalObjectIds(List<Long> performanceAppraisalObjectsIds) {
        return performanceAppraisalItemsMapper.selectPerformanceAppraisalItemsByPerformAppraisalObjectIds(performanceAppraisalObjectsIds);
    }

    /**
     * 根据指标ID集合查询绩效
     *
     * @param map 指标ID集合
     * @return List
     */
    @Override
    public List<PerformanceAppraisalItemsDTO> selectByIndicatorIds(Map<Integer, List<Long>> map) {
        if (StringUtils.isNotNull(map) && map.size() == 1) {
            for (Integer integer : map.keySet()) {
                List<Long> indicatorIds = map.get(integer);
                return performanceAppraisalItemsMapper.selectByIndicatorIds(indicatorIds, integer);
            }
        }
        throw new ServiceException("系统错误 入参不对");
    }
}

