package net.qixiaowei.strategy.cloud.service.impl.gap;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.gap.GapAnalysisOperate;
import net.qixiaowei.strategy.cloud.api.dto.gap.GapAnalysisOperateDTO;
import net.qixiaowei.strategy.cloud.excel.gap.GapAnalysisOperateExcel;
import net.qixiaowei.strategy.cloud.mapper.gap.GapAnalysisOperateMapper;
import net.qixiaowei.strategy.cloud.service.gap.IGapAnalysisOperateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * GapAnalysisOperateService业务层处理
 *
 * @author Graves
 * @since 2023-02-24
 */
@Service
public class GapAnalysisOperateServiceImpl implements IGapAnalysisOperateService {
    @Autowired
    private GapAnalysisOperateMapper gapAnalysisOperateMapper;

    /**
     * 查询差距分析经营情况表
     *
     * @param gapAnalysisOperateId 差距分析经营情况表主键
     * @return 差距分析经营情况表
     */
    @Override
    public GapAnalysisOperateDTO selectGapAnalysisOperateByGapAnalysisOperateId(Long gapAnalysisOperateId) {
        return gapAnalysisOperateMapper.selectGapAnalysisOperateByGapAnalysisOperateId(gapAnalysisOperateId);
    }

    /**
     * 查询差距分析经营情况表列表
     *
     * @param gapAnalysisOperateDTO 差距分析经营情况表
     * @return 差距分析经营情况表
     */
    @Override
    public List<GapAnalysisOperateDTO> selectGapAnalysisOperateList(GapAnalysisOperateDTO gapAnalysisOperateDTO) {
        GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
        BeanUtils.copyProperties(gapAnalysisOperateDTO, gapAnalysisOperate);
        return gapAnalysisOperateMapper.selectGapAnalysisOperateList(gapAnalysisOperate);
    }

    /**
     * 新增差距分析经营情况表
     *
     * @param gapAnalysisOperateDTO 差距分析经营情况表
     * @return 结果
     */
    @Override
    public GapAnalysisOperateDTO insertGapAnalysisOperate(GapAnalysisOperateDTO gapAnalysisOperateDTO) {
        GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
        BeanUtils.copyProperties(gapAnalysisOperateDTO, gapAnalysisOperate);
        gapAnalysisOperate.setCreateBy(SecurityUtils.getUserId());
        gapAnalysisOperate.setCreateTime(DateUtils.getNowDate());
        gapAnalysisOperate.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisOperate.setUpdateBy(SecurityUtils.getUserId());
        gapAnalysisOperate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        gapAnalysisOperateMapper.insertGapAnalysisOperate(gapAnalysisOperate);
        gapAnalysisOperateDTO.setGapAnalysisOperateId(gapAnalysisOperate.getGapAnalysisOperateId());
        return gapAnalysisOperateDTO;
    }

    /**
     * 修改差距分析经营情况表
     *
     * @param gapAnalysisOperateDTO 差距分析经营情况表
     * @return 结果
     */
    @Override
    public int updateGapAnalysisOperate(GapAnalysisOperateDTO gapAnalysisOperateDTO) {
        GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
        BeanUtils.copyProperties(gapAnalysisOperateDTO, gapAnalysisOperate);
        gapAnalysisOperate.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisOperate.setUpdateBy(SecurityUtils.getUserId());
        return gapAnalysisOperateMapper.updateGapAnalysisOperate(gapAnalysisOperate);
    }

    /**
     * 逻辑批量删除差距分析经营情况表
     *
     * @param gapAnalysisOperateIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisOperateByGapAnalysisOperateIds(List<Long> gapAnalysisOperateIds) {
        return gapAnalysisOperateMapper.logicDeleteGapAnalysisOperateByGapAnalysisOperateIds(gapAnalysisOperateIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除差距分析经营情况表信息
     *
     * @param gapAnalysisOperateId 差距分析经营情况表主键
     * @return 结果
     */
    @Override
    public int deleteGapAnalysisOperateByGapAnalysisOperateId(Long gapAnalysisOperateId) {
        return gapAnalysisOperateMapper.deleteGapAnalysisOperateByGapAnalysisOperateId(gapAnalysisOperateId);
    }

    /**
     * 逻辑删除差距分析经营情况表信息
     *
     * @param gapAnalysisOperateDTO 差距分析经营情况表
     * @return 结果
     */
    @Override
    public int logicDeleteGapAnalysisOperateByGapAnalysisOperateId(GapAnalysisOperateDTO gapAnalysisOperateDTO) {
        GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
        gapAnalysisOperate.setGapAnalysisOperateId(gapAnalysisOperateDTO.getGapAnalysisOperateId());
        gapAnalysisOperate.setUpdateTime(DateUtils.getNowDate());
        gapAnalysisOperate.setUpdateBy(SecurityUtils.getUserId());
        return gapAnalysisOperateMapper.logicDeleteGapAnalysisOperateByGapAnalysisOperateId(gapAnalysisOperate);
    }

    /**
     * 物理删除差距分析经营情况表信息
     *
     * @param gapAnalysisOperateDTO 差距分析经营情况表
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisOperateByGapAnalysisOperateId(GapAnalysisOperateDTO gapAnalysisOperateDTO) {
        GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
        BeanUtils.copyProperties(gapAnalysisOperateDTO, gapAnalysisOperate);
        return gapAnalysisOperateMapper.deleteGapAnalysisOperateByGapAnalysisOperateId(gapAnalysisOperate.getGapAnalysisOperateId());
    }

    /**
     * 物理批量删除差距分析经营情况表
     *
     * @param gapAnalysisOperateDtos 需要删除的差距分析经营情况表主键
     * @return 结果
     */

    @Override
    public int deleteGapAnalysisOperateByGapAnalysisOperateIds(List<GapAnalysisOperateDTO> gapAnalysisOperateDtos) {
        List<Long> stringList = new ArrayList();
        for (GapAnalysisOperateDTO gapAnalysisOperateDTO : gapAnalysisOperateDtos) {
            stringList.add(gapAnalysisOperateDTO.getGapAnalysisOperateId());
        }
        return gapAnalysisOperateMapper.deleteGapAnalysisOperateByGapAnalysisOperateIds(stringList);
    }

    /**
     * 批量新增差距分析经营情况表信息
     *
     * @param gapAnalysisOperateDtos 差距分析经营情况表对象
     */

    public int insertGapAnalysisOperates(List<GapAnalysisOperateDTO> gapAnalysisOperateDtos) {
        List<GapAnalysisOperate> gapAnalysisOperateList = new ArrayList();

        for (GapAnalysisOperateDTO gapAnalysisOperateDTO : gapAnalysisOperateDtos) {
            GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
            BeanUtils.copyProperties(gapAnalysisOperateDTO, gapAnalysisOperate);
            gapAnalysisOperate.setCreateBy(SecurityUtils.getUserId());
            gapAnalysisOperate.setCreateTime(DateUtils.getNowDate());
            gapAnalysisOperate.setUpdateTime(DateUtils.getNowDate());
            gapAnalysisOperate.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisOperate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            gapAnalysisOperateList.add(gapAnalysisOperate);
        }
        return gapAnalysisOperateMapper.batchGapAnalysisOperate(gapAnalysisOperateList);
    }

    /**
     * 批量修改差距分析经营情况表信息
     *
     * @param gapAnalysisOperateDtos 差距分析经营情况表对象
     */

    public int updateGapAnalysisOperates(List<GapAnalysisOperateDTO> gapAnalysisOperateDtos) {
        List<GapAnalysisOperate> gapAnalysisOperateList = new ArrayList();

        for (GapAnalysisOperateDTO gapAnalysisOperateDTO : gapAnalysisOperateDtos) {
            GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
            BeanUtils.copyProperties(gapAnalysisOperateDTO, gapAnalysisOperate);
            gapAnalysisOperate.setCreateBy(SecurityUtils.getUserId());
            gapAnalysisOperate.setCreateTime(DateUtils.getNowDate());
            gapAnalysisOperate.setUpdateTime(DateUtils.getNowDate());
            gapAnalysisOperate.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisOperateList.add(gapAnalysisOperate);
        }
        return gapAnalysisOperateMapper.updateGapAnalysisOperates(gapAnalysisOperateList);
    }

    /**
     * 导入Excel
     *
     * @param list
     */
    @Override
    public void importGapAnalysisOperate(List<GapAnalysisOperateExcel> list) {
        List<GapAnalysisOperate> gapAnalysisOperateList = new ArrayList<>();
        list.forEach(l -> {
            GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
            BeanUtils.copyProperties(l, gapAnalysisOperate);
            gapAnalysisOperate.setCreateBy(SecurityUtils.getUserId());
            gapAnalysisOperate.setCreateTime(DateUtils.getNowDate());
            gapAnalysisOperate.setUpdateTime(DateUtils.getNowDate());
            gapAnalysisOperate.setUpdateBy(SecurityUtils.getUserId());
            gapAnalysisOperate.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            gapAnalysisOperateList.add(gapAnalysisOperate);
        });
        try {
            gapAnalysisOperateMapper.batchGapAnalysisOperate(gapAnalysisOperateList);
        } catch (Exception e) {
            throw new ServiceException("导入差距分析经营情况表失败");
        }
    }

    /**
     * 导出Excel
     *
     * @param gapAnalysisOperateDTO
     * @return
     */
    @Override
    public List<GapAnalysisOperateExcel> exportGapAnalysisOperate(GapAnalysisOperateDTO gapAnalysisOperateDTO) {
        GapAnalysisOperate gapAnalysisOperate = new GapAnalysisOperate();
        BeanUtils.copyProperties(gapAnalysisOperateDTO, gapAnalysisOperate);
        List<GapAnalysisOperateDTO> gapAnalysisOperateDTOList = gapAnalysisOperateMapper.selectGapAnalysisOperateList(gapAnalysisOperate);
        List<GapAnalysisOperateExcel> gapAnalysisOperateExcelList = new ArrayList<>();
        return gapAnalysisOperateExcelList;
    }
}

