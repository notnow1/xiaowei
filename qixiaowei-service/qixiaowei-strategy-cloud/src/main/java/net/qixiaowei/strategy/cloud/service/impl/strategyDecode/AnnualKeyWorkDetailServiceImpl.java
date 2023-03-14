package net.qixiaowei.strategy.cloud.service.impl.strategyDecode;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.strategy.cloud.api.domain.strategyDecode.AnnualKeyWorkDetail;
import net.qixiaowei.strategy.cloud.api.dto.strategyDecode.AnnualKeyWorkDetailDTO;
import net.qixiaowei.strategy.cloud.mapper.strategyDecode.AnnualKeyWorkDetailMapper;
import net.qixiaowei.strategy.cloud.service.strategyDecode.IAnnualKeyWorkDetailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * AnnualKeyWorkDetailService业务层处理
 *
 * @author Graves
 * @since 2023-03-14
 */
@Service
public class AnnualKeyWorkDetailServiceImpl implements IAnnualKeyWorkDetailService {
    @Autowired
    private AnnualKeyWorkDetailMapper annualKeyWorkDetailMapper;

    /**
     * 查询年度重点工作详情表
     *
     * @param annualKeyWorkDetailId 年度重点工作详情表主键
     * @return 年度重点工作详情表
     */
    @Override
    public AnnualKeyWorkDetailDTO selectAnnualKeyWorkDetailByAnnualKeyWorkDetailId(Long annualKeyWorkDetailId) {
        return annualKeyWorkDetailMapper.selectAnnualKeyWorkDetailByAnnualKeyWorkDetailId(annualKeyWorkDetailId);
    }

    /**
     * 查询年度重点工作详情表列表
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return 年度重点工作详情表
     */
    @Override
    public List<AnnualKeyWorkDetailDTO> selectAnnualKeyWorkDetailList(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO) {
        AnnualKeyWorkDetail annualKeyWorkDetail = new AnnualKeyWorkDetail();
        BeanUtils.copyProperties(annualKeyWorkDetailDTO, annualKeyWorkDetail);
        return annualKeyWorkDetailMapper.selectAnnualKeyWorkDetailList(annualKeyWorkDetail);
    }

    /**
     * 新增年度重点工作详情表
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return 结果
     */
    @Override
    public AnnualKeyWorkDetailDTO insertAnnualKeyWorkDetail(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO) {
        AnnualKeyWorkDetail annualKeyWorkDetail = new AnnualKeyWorkDetail();
        BeanUtils.copyProperties(annualKeyWorkDetailDTO, annualKeyWorkDetail);
        annualKeyWorkDetail.setCreateBy(SecurityUtils.getUserId());
        annualKeyWorkDetail.setCreateTime(DateUtils.getNowDate());
        annualKeyWorkDetail.setUpdateTime(DateUtils.getNowDate());
        annualKeyWorkDetail.setUpdateBy(SecurityUtils.getUserId());
        annualKeyWorkDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        annualKeyWorkDetailMapper.insertAnnualKeyWorkDetail(annualKeyWorkDetail);
        annualKeyWorkDetailDTO.setAnnualKeyWorkDetailId(annualKeyWorkDetail.getAnnualKeyWorkDetailId());
        return annualKeyWorkDetailDTO;
    }

    /**
     * 修改年度重点工作详情表
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return 结果
     */
    @Override
    public int updateAnnualKeyWorkDetail(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO) {
        AnnualKeyWorkDetail annualKeyWorkDetail = new AnnualKeyWorkDetail();
        BeanUtils.copyProperties(annualKeyWorkDetailDTO, annualKeyWorkDetail);
        annualKeyWorkDetail.setUpdateTime(DateUtils.getNowDate());
        annualKeyWorkDetail.setUpdateBy(SecurityUtils.getUserId());
        return annualKeyWorkDetailMapper.updateAnnualKeyWorkDetail(annualKeyWorkDetail);
    }

    /**
     * 逻辑批量删除年度重点工作详情表
     *
     * @param annualKeyWorkDetailIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(List<Long> annualKeyWorkDetailIds) {
        return annualKeyWorkDetailMapper.logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(annualKeyWorkDetailIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除年度重点工作详情表信息
     *
     * @param annualKeyWorkDetailId 年度重点工作详情表主键
     * @return 结果
     */
    @Override
    public int deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(Long annualKeyWorkDetailId) {
        return annualKeyWorkDetailMapper.deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(annualKeyWorkDetailId);
    }

    /**
     * 逻辑删除年度重点工作详情表信息
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return 结果
     */
    @Override
    public int logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO) {
        AnnualKeyWorkDetail annualKeyWorkDetail = new AnnualKeyWorkDetail();
        annualKeyWorkDetail.setAnnualKeyWorkDetailId(annualKeyWorkDetailDTO.getAnnualKeyWorkDetailId());
        annualKeyWorkDetail.setUpdateTime(DateUtils.getNowDate());
        annualKeyWorkDetail.setUpdateBy(SecurityUtils.getUserId());
        return annualKeyWorkDetailMapper.logicDeleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(annualKeyWorkDetail);
    }

    /**
     * 物理删除年度重点工作详情表信息
     *
     * @param annualKeyWorkDetailDTO 年度重点工作详情表
     * @return 结果
     */

    @Override
    public int deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO) {
        AnnualKeyWorkDetail annualKeyWorkDetail = new AnnualKeyWorkDetail();
        BeanUtils.copyProperties(annualKeyWorkDetailDTO, annualKeyWorkDetail);
        return annualKeyWorkDetailMapper.deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailId(annualKeyWorkDetail.getAnnualKeyWorkDetailId());
    }

    /**
     * 物理批量删除年度重点工作详情表
     *
     * @param annualKeyWorkDetailDtos 需要删除的年度重点工作详情表主键
     * @return 结果
     */

    @Override
    public int deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDtos) {
        List<Long> stringList = new ArrayList<>();
        for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDtos) {
            stringList.add(annualKeyWorkDetailDTO.getAnnualKeyWorkDetailId());
        }
        return annualKeyWorkDetailMapper.deleteAnnualKeyWorkDetailByAnnualKeyWorkDetailIds(stringList);
    }

    /**
     * 批量新增年度重点工作详情表信息
     *
     * @param annualKeyWorkDetailDtos 年度重点工作详情表对象
     */

    public int insertAnnualKeyWorkDetails(List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDtos) {
        List<AnnualKeyWorkDetail> annualKeyWorkDetailList = new ArrayList<>();

        for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDtos) {
            AnnualKeyWorkDetail annualKeyWorkDetail = new AnnualKeyWorkDetail();
            BeanUtils.copyProperties(annualKeyWorkDetailDTO, annualKeyWorkDetail);
            annualKeyWorkDetail.setCreateBy(SecurityUtils.getUserId());
            annualKeyWorkDetail.setCreateTime(DateUtils.getNowDate());
            annualKeyWorkDetail.setUpdateTime(DateUtils.getNowDate());
            annualKeyWorkDetail.setUpdateBy(SecurityUtils.getUserId());
            annualKeyWorkDetail.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            annualKeyWorkDetailList.add(annualKeyWorkDetail);
        }
        return annualKeyWorkDetailMapper.batchAnnualKeyWorkDetail(annualKeyWorkDetailList);
    }

    /**
     * 批量修改年度重点工作详情表信息
     *
     * @param annualKeyWorkDetailDtos 年度重点工作详情表对象
     */

    public int updateAnnualKeyWorkDetails(List<AnnualKeyWorkDetailDTO> annualKeyWorkDetailDtos) {
        List<AnnualKeyWorkDetail> annualKeyWorkDetailList = new ArrayList<>();

        for (AnnualKeyWorkDetailDTO annualKeyWorkDetailDTO : annualKeyWorkDetailDtos) {
            AnnualKeyWorkDetail annualKeyWorkDetail = new AnnualKeyWorkDetail();
            BeanUtils.copyProperties(annualKeyWorkDetailDTO, annualKeyWorkDetail);
            annualKeyWorkDetail.setCreateBy(SecurityUtils.getUserId());
            annualKeyWorkDetail.setCreateTime(DateUtils.getNowDate());
            annualKeyWorkDetail.setUpdateTime(DateUtils.getNowDate());
            annualKeyWorkDetail.setUpdateBy(SecurityUtils.getUserId());
            annualKeyWorkDetailList.add(annualKeyWorkDetail);
        }
        return annualKeyWorkDetailMapper.updateAnnualKeyWorkDetails(annualKeyWorkDetailList);
    }

}

