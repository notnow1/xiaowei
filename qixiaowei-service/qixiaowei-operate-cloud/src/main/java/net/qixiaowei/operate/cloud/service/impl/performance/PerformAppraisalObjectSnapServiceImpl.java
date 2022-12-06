package net.qixiaowei.operate.cloud.service.impl.performance;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.performance.PerformAppraisalObjectSnap;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformAppraisalObjectSnapDTO;
import net.qixiaowei.operate.cloud.excel.performance.PerformAppraisalObjectSnapExcel;
import net.qixiaowei.operate.cloud.mapper.performance.PerformAppraisalObjectSnapMapper;
import net.qixiaowei.operate.cloud.service.performance.IPerformAppraisalObjectSnapService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * PerformAppraisalObjectSnapService业务层处理
 *
 * @author Graves
 * @since 2022-12-05
 */
@Service
public class PerformAppraisalObjectSnapServiceImpl implements IPerformAppraisalObjectSnapService {
    @Autowired
    private PerformAppraisalObjectSnapMapper performAppraisalObjectSnapMapper;

    /**
     * 查询绩效考核对象快照表
     *
     * @param appraisalObjectSnapId 绩效考核对象快照表主键
     * @return 绩效考核对象快照表
     */
    @Override
    public PerformAppraisalObjectSnapDTO selectPerformAppraisalObjectSnapByAppraisalObjectSnapId(Long appraisalObjectSnapId) {
        return performAppraisalObjectSnapMapper.selectPerformAppraisalObjectSnapByAppraisalObjectSnapId(appraisalObjectSnapId);
    }

    /**
     * 查询绩效考核对象快照表列表
     *
     * @param performAppraisalObjectSnapDTO 绩效考核对象快照表
     * @return 绩效考核对象快照表
     */
    @Override
    public List<PerformAppraisalObjectSnapDTO> selectPerformAppraisalObjectSnapList(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO) {
        PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
        BeanUtils.copyProperties(performAppraisalObjectSnapDTO, performAppraisalObjectSnap);
        return performAppraisalObjectSnapMapper.selectPerformAppraisalObjectSnapList(performAppraisalObjectSnap);
    }

    /**
     * 新增绩效考核对象快照表
     *
     * @param performAppraisalObjectSnapDTO 绩效考核对象快照表
     * @return 结果
     */
    @Override
    public PerformAppraisalObjectSnapDTO insertPerformAppraisalObjectSnap(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO) {
        PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
        BeanUtils.copyProperties(performAppraisalObjectSnapDTO, performAppraisalObjectSnap);
        performAppraisalObjectSnap.setCreateBy(SecurityUtils.getUserId());
        performAppraisalObjectSnap.setCreateTime(DateUtils.getNowDate());
        performAppraisalObjectSnap.setUpdateTime(DateUtils.getNowDate());
        performAppraisalObjectSnap.setUpdateBy(SecurityUtils.getUserId());
        performAppraisalObjectSnap.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        performAppraisalObjectSnapMapper.insertPerformAppraisalObjectSnap(performAppraisalObjectSnap);
        performAppraisalObjectSnapDTO.setAppraisalObjectSnapId(performAppraisalObjectSnap.getAppraisalObjectSnapId());
        return performAppraisalObjectSnapDTO;
    }

    /**
     * 修改绩效考核对象快照表
     *
     * @param performAppraisalObjectSnapDTO 绩效考核对象快照表
     * @return 结果
     */
    @Override
    public int updatePerformAppraisalObjectSnap(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO) {
        PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
        BeanUtils.copyProperties(performAppraisalObjectSnapDTO, performAppraisalObjectSnap);
        performAppraisalObjectSnap.setUpdateTime(DateUtils.getNowDate());
        performAppraisalObjectSnap.setUpdateBy(SecurityUtils.getUserId());
        return performAppraisalObjectSnapMapper.updatePerformAppraisalObjectSnap(performAppraisalObjectSnap);
    }

    /**
     * 逻辑批量删除绩效考核对象快照表
     *
     * @param appraisalObjectSnapIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(List<Long> appraisalObjectSnapIds) {
        return performAppraisalObjectSnapMapper.logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(appraisalObjectSnapIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除绩效考核对象快照表信息
     *
     * @param appraisalObjectSnapId 绩效考核对象快照表主键
     * @return 结果
     */
    @Override
    public int deletePerformAppraisalObjectSnapByAppraisalObjectSnapId(Long appraisalObjectSnapId) {
        return performAppraisalObjectSnapMapper.deletePerformAppraisalObjectSnapByAppraisalObjectSnapId(appraisalObjectSnapId);
    }

    /**
     * 逻辑删除绩效考核对象快照表信息
     *
     * @param performAppraisalObjectSnapDTO 绩效考核对象快照表
     * @return 结果
     */
    @Override
    public int logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapId(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO) {
        PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
        performAppraisalObjectSnap.setAppraisalObjectSnapId(performAppraisalObjectSnapDTO.getAppraisalObjectSnapId());
        performAppraisalObjectSnap.setUpdateTime(DateUtils.getNowDate());
        performAppraisalObjectSnap.setUpdateBy(SecurityUtils.getUserId());
        return performAppraisalObjectSnapMapper.logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapId(performAppraisalObjectSnap);
    }

    /**
     * 物理删除绩效考核对象快照表信息
     *
     * @param performAppraisalObjectSnapDTO 绩效考核对象快照表
     * @return 结果
     */

    @Override
    public int deletePerformAppraisalObjectSnapByAppraisalObjectSnapId(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO) {
        PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
        BeanUtils.copyProperties(performAppraisalObjectSnapDTO, performAppraisalObjectSnap);
        return performAppraisalObjectSnapMapper.deletePerformAppraisalObjectSnapByAppraisalObjectSnapId(performAppraisalObjectSnap.getAppraisalObjectSnapId());
    }

    /**
     * 物理批量删除绩效考核对象快照表
     *
     * @param performAppraisalObjectSnapDtos 需要删除的绩效考核对象快照表主键
     * @return 结果
     */

    @Override
    public int deletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDtos) {
        List<Long> stringList = new ArrayList<>();
        for (PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO : performAppraisalObjectSnapDtos) {
            stringList.add(performAppraisalObjectSnapDTO.getAppraisalObjectSnapId());
        }
        return performAppraisalObjectSnapMapper.deletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(stringList);
    }

    /**
     * 批量新增绩效考核对象快照表信息
     *
     * @param performAppraisalObjectSnapDtos 绩效考核对象快照表对象
     */

    public int insertPerformAppraisalObjectSnaps(List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDtos) {
        if (StringUtils.isEmpty(performAppraisalObjectSnapDtos)) {
            return 1;
        }
        List<PerformAppraisalObjectSnap> performAppraisalObjectSnapList = new ArrayList<>();
        for (PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO : performAppraisalObjectSnapDtos) {
            PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
            BeanUtils.copyProperties(performAppraisalObjectSnapDTO, performAppraisalObjectSnap);
            performAppraisalObjectSnap.setCreateBy(SecurityUtils.getUserId());
            performAppraisalObjectSnap.setCreateTime(DateUtils.getNowDate());
            performAppraisalObjectSnap.setUpdateTime(DateUtils.getNowDate());
            performAppraisalObjectSnap.setUpdateBy(SecurityUtils.getUserId());
            performAppraisalObjectSnap.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performAppraisalObjectSnapList.add(performAppraisalObjectSnap);
        }
        return performAppraisalObjectSnapMapper.batchPerformAppraisalObjectSnap(performAppraisalObjectSnapList);
    }

    /**
     * 批量修改绩效考核对象快照表信息
     *
     * @param performAppraisalObjectSnapDtos 绩效考核对象快照表对象
     */

    public int updatePerformAppraisalObjectSnaps(List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDtos) {
        List<PerformAppraisalObjectSnap> performAppraisalObjectSnapList = new ArrayList<>();

        for (PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO : performAppraisalObjectSnapDtos) {
            PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
            BeanUtils.copyProperties(performAppraisalObjectSnapDTO, performAppraisalObjectSnap);
            performAppraisalObjectSnap.setCreateBy(SecurityUtils.getUserId());
            performAppraisalObjectSnap.setCreateTime(DateUtils.getNowDate());
            performAppraisalObjectSnap.setUpdateTime(DateUtils.getNowDate());
            performAppraisalObjectSnap.setUpdateBy(SecurityUtils.getUserId());
            performAppraisalObjectSnapList.add(performAppraisalObjectSnap);
        }
        return performAppraisalObjectSnapMapper.updatePerformAppraisalObjectSnaps(performAppraisalObjectSnapList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importPerformAppraisalObjectSnap(List<PerformAppraisalObjectSnapExcel> list) {
        List<PerformAppraisalObjectSnap> performAppraisalObjectSnapList = new ArrayList<>();
        list.forEach(l -> {
            PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
            BeanUtils.copyProperties(l, performAppraisalObjectSnap);
            performAppraisalObjectSnap.setCreateBy(SecurityUtils.getUserId());
            performAppraisalObjectSnap.setCreateTime(DateUtils.getNowDate());
            performAppraisalObjectSnap.setUpdateTime(DateUtils.getNowDate());
            performAppraisalObjectSnap.setUpdateBy(SecurityUtils.getUserId());
            performAppraisalObjectSnap.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            performAppraisalObjectSnapList.add(performAppraisalObjectSnap);
        });
        try {
            performAppraisalObjectSnapMapper.batchPerformAppraisalObjectSnap(performAppraisalObjectSnapList);
        } catch (Exception e) {
            throw new ServiceException("导入绩效考核对象快照表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param performAppraisalObjectSnapDTO
     * @return
     */
    @Override
    public List<PerformAppraisalObjectSnapExcel> exportPerformAppraisalObjectSnap(PerformAppraisalObjectSnapDTO performAppraisalObjectSnapDTO) {
        PerformAppraisalObjectSnap performAppraisalObjectSnap = new PerformAppraisalObjectSnap();
        BeanUtils.copyProperties(performAppraisalObjectSnapDTO, performAppraisalObjectSnap);
        List<PerformAppraisalObjectSnapDTO> performAppraisalObjectSnapDTOList = performAppraisalObjectSnapMapper.selectPerformAppraisalObjectSnapList(performAppraisalObjectSnap);
        List<PerformAppraisalObjectSnapExcel> performAppraisalObjectSnapExcelList = new ArrayList<>();
        return performAppraisalObjectSnapExcelList;
    }

    /**
     * 通过对象ID集合查询快照
     *
     * @param performanceObjectIds 对象ID集合
     * @return
     */
    @Override
    public List<PerformAppraisalObjectSnapDTO> selectPerformAppraisalObjectSnapByAppraisalObjectsIds(List<Long> performanceObjectIds) {
        return performAppraisalObjectSnapMapper.selectPerformAppraisalObjectSnapByAppraisalObjectsIds(performanceObjectIds);
    }
}

