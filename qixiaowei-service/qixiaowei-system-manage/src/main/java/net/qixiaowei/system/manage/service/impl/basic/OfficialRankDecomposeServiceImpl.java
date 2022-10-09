package net.qixiaowei.system.manage.service.impl.basic;

import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankSystem;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.basic.OfficialRankDecompose;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.mapper.basic.OfficialRankDecomposeMapper;
import net.qixiaowei.system.manage.service.basic.IOfficialRankDecomposeService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * OfficialRankDecomposeService业务层处理
 *
 * @author Graves
 * @since 2022-10-07
 */
@Service
public class OfficialRankDecomposeServiceImpl implements IOfficialRankDecomposeService {
    @Autowired
    private OfficialRankDecomposeMapper officialRankDecomposeMapper;

    /**
     * 查询职级分解表
     *
     * @param officialRankDecomposeId 职级分解表主键
     * @return 职级分解表
     */
    @Override
    public OfficialRankDecomposeDTO selectOfficialRankDecomposeByOfficialRankDecomposeId(Long officialRankDecomposeId) {
        return officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecomposeId);
    }

    /**
     * 查询职级分解表列表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 职级分解表
     */
    @Override
    public List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeList(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        return officialRankDecomposeMapper.selectOfficialRankDecomposeList(officialRankDecompose);
    }

    /**
     * 新增职级分解表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertOfficialRankDecompose(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        officialRankDecompose.setCreateBy(SecurityUtils.getUserId());
        officialRankDecompose.setCreateTime(DateUtils.getNowDate());
        officialRankDecompose.setUpdateTime(DateUtils.getNowDate());
        officialRankDecompose.setUpdateBy(SecurityUtils.getUserId());
        officialRankDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return officialRankDecomposeMapper.insertOfficialRankDecompose(officialRankDecompose);
    }

    /**
     * 修改职级分解表
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateOfficialRankDecompose(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        officialRankDecompose.setUpdateTime(DateUtils.getNowDate());
        officialRankDecompose.setUpdateBy(SecurityUtils.getUserId());
        return officialRankDecomposeMapper.updateOfficialRankDecompose(officialRankDecompose);
    }

    /**
     * 逻辑批量删除职级分解
     *
     * @param officialRankDecomposeDtos 需要删除的职级分解表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos) {
        List<Long> officialRankDecomposeIds = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDtos) {
            officialRankDecomposeIds.add(officialRankDecomposeDTO.getOfficialRankDecomposeId());
        }
        return officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(officialRankDecomposeIds, officialRankDecomposeDtos.get(0).getUpdateBy(), DateUtils.getNowDate());
    }

    /**
     * 物理删除职级分解表信息
     *
     * @param officialRankDecomposeId 职级分解表主键
     * @return 结果
     */

    @Transactional
    @Override
    public int deleteOfficialRankDecomposeByOfficialRankDecomposeId(Long officialRankDecomposeId) {
        return officialRankDecomposeMapper.deleteOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecomposeId);
    }

    /**
     * 更新操作
     *
     * @param officialRankDecomposeDTOS
     * @return
     */
    @Override
    public int operateOfficialRankDecompose(List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS) {


        return 0;
    }

    /**
     * 通过officialRankSystemId查找职级分解
     *
     * @param officialRankDecomposeDTO
     * @return
     */
    @Override
    public List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeByOfficialRankSystemDTO(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        String officialRankSystemId = officialRankDecomposeDTO.getOfficialRankSystemId();
        if (StringUtils.isEmpty(officialRankSystemId)) {
            throw new ServiceException("职级体系ID不能为空");
        }
        return officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId);
    }

    /**
     * 通过officialRankSystemId查找职级分解
     *
     * @param officialRankSystemId
     * @return
     */
    @Override
    public List<OfficialRankDecomposeDTO> selectOfficialRankDecomposeByOfficialRankSystemId(String officialRankSystemId) {
        if (StringUtils.isEmpty(officialRankSystemId)) {
            throw new ServiceException("职级体系ID不能为空");
        }
        return officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankSystemId(officialRankSystemId);
    }

    /**
     * 通过officialRankSystemId，rankDecomposeDimensionBefore删除rankDecomposeDimension
     *
     * @param officialRankSystemId
     * @param rankDecomposeDimensionBefore
     * @return
     */
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankDecompose(Long officialRankSystemId, Integer rankDecomposeDimensionBefore) {
        return officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialRankDecompose(officialRankSystemId, rankDecomposeDimensionBefore);
    }

    /**
     * 根据officialRankSystemIds判断该职级体系是否存在
     *
     * @param officialRankSystemIds
     * @return
     */
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankSystemIds(List<Long> officialRankSystemIds) {
        List<Long> officialRankDecomposeIds =
                officialRankDecomposeMapper.selectOfficialRankDecomposeByOfficialRankSystemIds(officialRankSystemIds);
        officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialRankDecomposeIds(officialRankDecomposeIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
        return 0;
    }

    /**
     * 逻辑删除职级分解表信息
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteOfficialRankDecomposeByOfficialRankDecomposeId(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        return officialRankDecomposeMapper.logicDeleteOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecompose, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除职级分解表信息
     *
     * @param officialRankDecomposeDTO 职级分解表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankDecomposeByOfficialRankDecomposeId(OfficialRankDecomposeDTO officialRankDecomposeDTO) {
        OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
        BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
        return officialRankDecomposeMapper.deleteOfficialRankDecomposeByOfficialRankDecomposeId(officialRankDecompose.getOfficialRankDecomposeId());
    }

    /**
     * 物理批量删除职级分解表
     *
     * @param officialRankDecomposeDtos 需要删除的职级分解表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteOfficialRankDecomposeByOfficialRankDecomposeIds(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos) {
        List<Long> officialRankDecomposeIds = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDtos) {
            officialRankDecomposeIds.add(officialRankDecomposeDTO.getOfficialRankDecomposeId());
        }
        if (officialRankDecomposeMapper.isExist(officialRankDecomposeIds) != officialRankDecomposeIds.size()) {
            throw new ServiceException("当前的职级分解已不存在");
        }
        return officialRankDecomposeMapper.deleteOfficialRankDecomposeByOfficialRankDecomposeIds(officialRankDecomposeIds);
    }

    /**
     * 批量新增职级分解表信息
     *
     * @param officialRankDecomposeDtos
     * @param officialRankSystem
     */
    @Transactional
    public int insertOfficialRankDecomposes(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos, OfficialRankSystem officialRankSystem) {
        List<OfficialRankDecompose> officialRankDecomposeList = new ArrayList<>();
        Long officialRankSystemId = officialRankSystem.getOfficialRankSystemId();
        Integer rankDecomposeDimension = officialRankSystem.getRankDecomposeDimension();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDtos) {
            OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
            BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
            officialRankDecompose.setOfficialRankSystemId(officialRankSystemId.toString());
            officialRankDecompose.setRankDecomposeDimension(rankDecomposeDimension);
            officialRankDecompose.setCreateBy(SecurityUtils.getUserId());
            officialRankDecompose.setCreateTime(DateUtils.getNowDate());
            officialRankDecompose.setUpdateTime(DateUtils.getNowDate());
            officialRankDecompose.setUpdateBy(SecurityUtils.getUserId());
            officialRankDecompose.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            officialRankDecomposeList.add(officialRankDecompose);
        }
        return officialRankDecomposeMapper.batchOfficialRankDecompose(officialRankDecomposeList);
    }

    /**
     * 批量修改职级分解表信息
     *
     * @param officialRankDecomposeDtos 职级分解表对象
     */
    @Transactional
    public int updateOfficialRankDecomposes(List<OfficialRankDecomposeDTO> officialRankDecomposeDtos, OfficialRankSystem officialRankSystem) {
        Long officialRankSystemId = officialRankSystem.getOfficialRankSystemId();
        Integer rankDecomposeDimension = officialRankSystem.getRankDecomposeDimension();
        List<OfficialRankDecompose> officialRankDecomposeList = new ArrayList<>();
        for (OfficialRankDecomposeDTO officialRankDecomposeDTO : officialRankDecomposeDtos) {
            OfficialRankDecompose officialRankDecompose = new OfficialRankDecompose();
            BeanUtils.copyProperties(officialRankDecomposeDTO, officialRankDecompose);
            officialRankDecompose.setOfficialRankSystemId(officialRankSystemId.toString());
            officialRankDecompose.setRankDecomposeDimension(rankDecomposeDimension);
            officialRankDecompose.setCreateBy(SecurityUtils.getUserId());
            officialRankDecompose.setCreateTime(DateUtils.getNowDate());
            officialRankDecompose.setUpdateTime(DateUtils.getNowDate());
            officialRankDecompose.setUpdateBy(SecurityUtils.getUserId());
            officialRankDecomposeList.add(officialRankDecompose);
        }
        return officialRankDecomposeMapper.updateOfficialRankDecomposes(officialRankDecomposeList);
    }
}

