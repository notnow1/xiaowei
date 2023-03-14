package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.AnnualKeyWork;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.AnnualKeyWorkMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IAnnualKeyWorkService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * AnnualKeyWorkService业务层处理
 *
 * @author Graves
 * @since 2023-03-14
 */
@Service
public class AnnualKeyWorkServiceImpl implements IAnnualKeyWorkService {
    @Autowired
    private AnnualKeyWorkMapper annualKeyWorkMapper;

    /**
     * 查询年度重点工作表
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 年度重点工作表
     */
    @Override
    public AnnualKeyWorkDTO selectAnnualKeyWorkByAnnualKeyWorkId(Long annualKeyWorkId) {
        AnnualKeyWorkDTO annualKeyWorkDTO = annualKeyWorkMapper.selectAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkId);
        return annualKeyWorkDTO;
    }

    /**
     * 查询年度重点工作表列表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 年度重点工作表
     */
    @Override
    public List<AnnualKeyWorkDTO> selectAnnualKeyWorkList(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        List<AnnualKeyWorkDTO> annualKeyWorkDTOS = annualKeyWorkMapper.selectAnnualKeyWorkList(annualKeyWork);
        return annualKeyWorkDTOS;
    }

    /**
     * 新增年度重点工作表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    public AnnualKeyWorkDTO insertAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        annualKeyWork.setCreateBy(SecurityUtils.getUserId());
        annualKeyWork.setCreateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        annualKeyWork.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        annualKeyWorkMapper.insertAnnualKeyWork(annualKeyWork);
        annualKeyWorkDTO.setAnnualKeyWorkId(annualKeyWork.getAnnualKeyWorkId());
        return annualKeyWorkDTO;
    }

    /**
     * 修改年度重点工作表
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    public int updateAnnualKeyWork(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        return annualKeyWorkMapper.updateAnnualKeyWork(annualKeyWork);
    }

    /**
     * 逻辑批量删除年度重点工作表
     *
     * @param annualKeyWorkIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(List<Long> annualKeyWorkIds) {
        return annualKeyWorkMapper.logicDeleteAnnualKeyWorkByAnnualKeyWorkIds(annualKeyWorkIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除年度重点工作表信息
     *
     * @param annualKeyWorkId 年度重点工作表主键
     * @return 结果
     */
    @Override
    public int deleteAnnualKeyWorkByAnnualKeyWorkId(Long annualKeyWorkId) {
        return annualKeyWorkMapper.deleteAnnualKeyWorkByAnnualKeyWorkId(annualKeyWorkId);
    }

    /**
     * 逻辑删除年度重点工作表信息
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */
    @Override
    public int logicDeleteAnnualKeyWorkByAnnualKeyWorkId(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        annualKeyWork.setAnnualKeyWorkId(annualKeyWorkDTO.getAnnualKeyWorkId());
        annualKeyWork.setUpdateTime(DateUtils.getNowDate());
        annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
        return annualKeyWorkMapper.logicDeleteAnnualKeyWorkByAnnualKeyWorkId(annualKeyWork);
    }

    /**
     * 物理删除年度重点工作表信息
     *
     * @param annualKeyWorkDTO 年度重点工作表
     * @return 结果
     */

    @Override
    public int deleteAnnualKeyWorkByAnnualKeyWorkId(AnnualKeyWorkDTO annualKeyWorkDTO) {
        AnnualKeyWork annualKeyWork = new AnnualKeyWork();
        BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
        return annualKeyWorkMapper.deleteAnnualKeyWorkByAnnualKeyWorkId(annualKeyWork.getAnnualKeyWorkId());
    }

    /**
     * 物理批量删除年度重点工作表
     *
     * @param annualKeyWorkDtos 需要删除的年度重点工作表主键
     * @return 结果
     */

    @Override
    public int deleteAnnualKeyWorkByAnnualKeyWorkIds(List<AnnualKeyWorkDTO> annualKeyWorkDtos) {
        List<Long> stringList = new ArrayList<>();
        for (AnnualKeyWorkDTO annualKeyWorkDTO : annualKeyWorkDtos) {
            stringList.add(annualKeyWorkDTO.getAnnualKeyWorkId());
        }
        return annualKeyWorkMapper.deleteAnnualKeyWorkByAnnualKeyWorkIds(stringList);
    }

    /**
     * 批量新增年度重点工作表信息
     *
     * @param annualKeyWorkDtos 年度重点工作表对象
     */

    public int insertAnnualKeyWorks(List<AnnualKeyWorkDTO> annualKeyWorkDtos) {
        List<AnnualKeyWork> annualKeyWorkList = new ArrayList<>();

        for (AnnualKeyWorkDTO annualKeyWorkDTO : annualKeyWorkDtos) {
            AnnualKeyWork annualKeyWork = new AnnualKeyWork();
            BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
            annualKeyWork.setCreateBy(SecurityUtils.getUserId());
            annualKeyWork.setCreateTime(DateUtils.getNowDate());
            annualKeyWork.setUpdateTime(DateUtils.getNowDate());
            annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
            annualKeyWork.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            annualKeyWorkList.add(annualKeyWork);
        }
        return annualKeyWorkMapper.batchAnnualKeyWork(annualKeyWorkList);
    }

    /**
     * 批量修改年度重点工作表信息
     *
     * @param annualKeyWorkDtos 年度重点工作表对象
     */

    public int updateAnnualKeyWorks(List<AnnualKeyWorkDTO> annualKeyWorkDtos) {
        List<AnnualKeyWork> annualKeyWorkList = new ArrayList<>();

        for (AnnualKeyWorkDTO annualKeyWorkDTO : annualKeyWorkDtos) {
            AnnualKeyWork annualKeyWork = new AnnualKeyWork();
            BeanUtils.copyProperties(annualKeyWorkDTO, annualKeyWork);
            annualKeyWork.setCreateBy(SecurityUtils.getUserId());
            annualKeyWork.setCreateTime(DateUtils.getNowDate());
            annualKeyWork.setUpdateTime(DateUtils.getNowDate());
            annualKeyWork.setUpdateBy(SecurityUtils.getUserId());
            annualKeyWorkList.add(annualKeyWork);
        }
        return annualKeyWorkMapper.updateAnnualKeyWorks(annualKeyWorkList);
    }

}

