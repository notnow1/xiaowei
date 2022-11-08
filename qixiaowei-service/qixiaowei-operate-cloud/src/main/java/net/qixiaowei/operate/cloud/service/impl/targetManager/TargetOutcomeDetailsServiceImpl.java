package net.qixiaowei.operate.cloud.service.impl.targetManager;

import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetOutcomeDetails;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetOutcomeDetailsDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.TargetOutcomeDetailsMapper;
import net.qixiaowei.operate.cloud.service.targetManager.ITargetOutcomeDetailsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


/**
 * TargetOutcomeDetailsService业务层处理
 *
 * @author Graves
 * @since 2022-11-07
 */
@Service
public class TargetOutcomeDetailsServiceImpl implements ITargetOutcomeDetailsService {
    @Autowired
    private TargetOutcomeDetailsMapper targetOutcomeDetailsMapper;

    /**
     * 查询目标结果详情表
     *
     * @param targetOutcomeDetailsId 目标结果详情表主键
     * @return 目标结果详情表
     */
    @Override
    public TargetOutcomeDetailsDTO selectTargetOutcomeDetailsByTargetOutcomeDetailsId(Long targetOutcomeDetailsId) {
        return targetOutcomeDetailsMapper.selectTargetOutcomeDetailsByTargetOutcomeDetailsId(targetOutcomeDetailsId);
    }

    /**
     * 查询目标结果详情表列表
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 目标结果详情表
     */
    @Override
    public List<TargetOutcomeDetailsDTO> selectTargetOutcomeDetailsList(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
        return targetOutcomeDetailsMapper.selectTargetOutcomeDetailsList(targetOutcomeDetails);
    }

    /**
     * 新增目标结果详情表
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 结果
     */
    @Override
    public TargetOutcomeDetailsDTO insertTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
        targetOutcomeDetails.setCreateBy(SecurityUtils.getUserId());
        targetOutcomeDetails.setCreateTime(DateUtils.getNowDate());
        targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
        targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
        targetOutcomeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        targetOutcomeDetailsMapper.insertTargetOutcomeDetails(targetOutcomeDetails);
        targetOutcomeDetailsDTO.setTargetOutcomeDetailsId(targetOutcomeDetails.getTargetOutcomeDetailsId());
        return targetOutcomeDetailsDTO;
    }

    /**
     * 修改目标结果详情表
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 结果
     */
    @Override
    public int updateTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
        targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
        targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
        return targetOutcomeDetailsMapper.updateTargetOutcomeDetails(targetOutcomeDetails);
    }

    /**
     * 逻辑批量删除目标结果详情表
     *
     * @param targetOutcomeDetailsIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(List<Long> targetOutcomeDetailsIds) {
        return targetOutcomeDetailsMapper.logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(targetOutcomeDetailsIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除目标结果详情表信息
     *
     * @param targetOutcomeDetailsId 目标结果详情表主键
     * @return 结果
     */
    @Override
    public int deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(Long targetOutcomeDetailsId) {
        return targetOutcomeDetailsMapper.deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(targetOutcomeDetailsId);
    }

    /**
     * 逻辑删除目标结果详情表信息
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 结果
     */
    @Override
    public int logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsId(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        targetOutcomeDetails.setTargetOutcomeDetailsId(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
        targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
        targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
        return targetOutcomeDetailsMapper.logicDeleteTargetOutcomeDetailsByTargetOutcomeDetailsId(targetOutcomeDetails);
    }

    /**
     * 物理删除目标结果详情表信息
     *
     * @param targetOutcomeDetailsDTO 目标结果详情表
     * @return 结果
     */

    @Override
    public int deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
        return targetOutcomeDetailsMapper.deleteTargetOutcomeDetailsByTargetOutcomeDetailsId(targetOutcomeDetails.getTargetOutcomeDetailsId());
    }

    /**
     * 物理批量删除目标结果详情表
     *
     * @param targetOutcomeDetailsDtos 需要删除的目标结果详情表主键
     * @return 结果
     */

    @Override
    public int deleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDtos) {
        List<Long> stringList = new ArrayList<>();
        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDtos) {
            stringList.add(targetOutcomeDetailsDTO.getTargetOutcomeDetailsId());
        }
        return targetOutcomeDetailsMapper.deleteTargetOutcomeDetailsByTargetOutcomeDetailsIds(stringList);
    }

    /**
     * 批量新增目标结果详情表信息
     *
     * @param targetOutcomeDetailsDtos 目标结果详情表对象
     */

    public int insertTargetOutcomeDetailss(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDtos) {
        List<TargetOutcomeDetails> targetOutcomeDetailsList = new ArrayList<>();

        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDtos) {
            TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
            BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
            targetOutcomeDetails.setCreateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setCreateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            targetOutcomeDetailsList.add(targetOutcomeDetails);
        }
        return targetOutcomeDetailsMapper.batchTargetOutcomeDetails(targetOutcomeDetailsList);
    }

    /**
     * 批量修改目标结果详情表信息
     *
     * @param targetOutcomeDetailsDtos 目标结果详情表对象
     */

    public int updateTargetOutcomeDetailss(List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDtos) {
        List<TargetOutcomeDetails> targetOutcomeDetailsList = new ArrayList<>();

        for (TargetOutcomeDetailsDTO targetOutcomeDetailsDTO : targetOutcomeDetailsDtos) {
            TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
            BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
            targetOutcomeDetails.setCreateBy(SecurityUtils.getUserId());
            targetOutcomeDetails.setCreateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
            targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
            targetOutcomeDetailsList.add(targetOutcomeDetails);
        }
        return targetOutcomeDetailsMapper.updateTargetOutcomeDetailss(targetOutcomeDetailsList);
    }

//    /**
//     * 导入Excel
//     *
//     * @param list
//     */
//    @Override
//    public void importTargetOutcomeDetails(List<TargetOutcomeDetailsExcel> list) {
//        List<TargetOutcomeDetails> targetOutcomeDetailsList = new ArrayList<>();
//        list.forEach(l -> {
//            TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
//            BeanUtils.copyProperties(l, targetOutcomeDetails);
//            targetOutcomeDetails.setCreateBy(SecurityUtils.getUserId());
//            targetOutcomeDetails.setCreateTime(DateUtils.getNowDate());
//            targetOutcomeDetails.setUpdateTime(DateUtils.getNowDate());
//            targetOutcomeDetails.setUpdateBy(SecurityUtils.getUserId());
//            targetOutcomeDetails.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
//            targetOutcomeDetailsList.add(targetOutcomeDetails);
//        });
//        try {
//            targetOutcomeDetailsMapper.batchTargetOutcomeDetails(targetOutcomeDetailsList);
//        } catch (Exception e) {
//            throw new ServiceException("导入目标结果详情表失败");
//        }
//    }
//
//    /**
//     * 导出Excel
//     *
//     * @param targetOutcomeDetailsDTO
//     * @return
//     */
//    @Override
//    public List<TargetOutcomeDetailsExcel> exportTargetOutcomeDetails(TargetOutcomeDetailsDTO targetOutcomeDetailsDTO) {
//        TargetOutcomeDetails targetOutcomeDetails = new TargetOutcomeDetails();
//        BeanUtils.copyProperties(targetOutcomeDetailsDTO, targetOutcomeDetails);
//        List<TargetOutcomeDetailsDTO> targetOutcomeDetailsDTOList = targetOutcomeDetailsMapper.selectTargetOutcomeDetailsList(targetOutcomeDetails);
//        List<TargetOutcomeDetailsExcel> targetOutcomeDetailsExcelList = new ArrayList<>();
//        return targetOutcomeDetailsExcelList;
//    }
}

