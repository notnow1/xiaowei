package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.util.List;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankDecomposeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

import org.springframework.transaction.annotation.Transactional;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.operate.cloud.api.domain.targetManager.Area;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import net.qixiaowei.operate.cloud.mapper.targetManager.AreaMapper;
import net.qixiaowei.operate.cloud.service.targetManager.IAreaService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * AreaService业务层处理
 *
 * @author Graves
 * @since 2022-10-07
 */
@Service
public class AreaServiceImpl implements IAreaService {
    @Autowired
    private AreaMapper areaMapper;

    @Autowired
    private RemoteOfficialRankSystemService officialRankSystemService;

    /**
     * 查询区域表
     *
     * @param areaId 区域表主键
     * @return 区域表
     */
    @Override
    public AreaDTO selectAreaByAreaId(Long areaId) {
        return areaMapper.selectAreaByAreaId(areaId);
    }

    /**
     * 查询区域表列表
     *
     * @param areaDTO 区域表
     * @return 区域表
     */
    @Override
    public List<AreaDTO> selectAreaList(AreaDTO areaDTO) {
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        return areaMapper.selectAreaList(area);
    }

    /**
     * 新增区域表
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int insertArea(AreaDTO areaDTO) {
        String areaCode = areaDTO.getAreaCode();
        String areaName = areaDTO.getAreaName();
        String regionIds = areaDTO.getRegionIds();
        String regionNames = areaDTO.getRegionNames();
        if (StringUtils.isEmpty(areaCode)) {
            throw new ServiceException("区域编码不能为空");
        }
        if (StringUtils.isEmpty(areaName)) {
            throw new ServiceException("区域名称不能为空");
        }
        if (StringUtils.isEmpty(regionIds) && StringUtils.isEmpty(regionNames)) {
            throw new ServiceException("省份不可以为空");
        }
        AreaDTO areaByCode = areaMapper.checkUnique(areaCode);
        if (StringUtils.isNotNull(areaByCode)) {
            throw new ServiceException("区域编码不可重复");
        }
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        area.setCreateBy(SecurityUtils.getUserId());
        area.setCreateTime(DateUtils.getNowDate());
        area.setUpdateTime(DateUtils.getNowDate());
        area.setUpdateBy(SecurityUtils.getUserId());
        area.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        return areaMapper.insertArea(area);
    }

    /**
     * 修改区域表
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int updateArea(AreaDTO areaDTO) {
        Long areaId = areaDTO.getAreaId();
        String areaCode = areaDTO.getAreaCode();
        String areaName = areaDTO.getAreaName();
        String regionIds = areaDTO.getRegionIds();
        String regionNames = areaDTO.getRegionNames();
        if (StringUtils.isEmpty(areaCode)) {
            throw new ServiceException("区域编码不能为空");
        }
        if (StringUtils.isEmpty(areaName)) {
            throw new ServiceException("区域名称不能为空");
        }
        if (StringUtils.isEmpty(regionIds) || StringUtils.isEmpty(regionNames)) {
            throw new ServiceException("省份不可以为空");
        }
        AreaDTO areaByCode = areaMapper.checkUnique(areaCode);
        if (StringUtils.isNotNull(areaByCode)) {
            if (!areaByCode.getAreaId().equals(areaId)) {
                throw new ServiceException("区域编码不可重复");
            }
        }
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        area.setUpdateTime(DateUtils.getNowDate());
        area.setUpdateBy(SecurityUtils.getUserId());
        return areaMapper.updateArea(area);
    }

    /**
     * 物理删除区域表信息
     *
     * @param areaId 区域表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAreaByAreaId(Long areaId) {
        return areaMapper.deleteAreaByAreaId(areaId);
    }

    /**
     * 查询分解维度区域下拉列表
     *
     * @param areaDTO
     * @return ID, Name
     */
    @Override
    public List<AreaDTO> selectDropList(AreaDTO areaDTO) {
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        return areaMapper.dropList(area);
    }

    /**
     * 查询区域配置列表通过IDS
     *
     * @param areaIds 区域表集合
     * @return 结果
     */
    @Override
    public List<AreaDTO> selectAreaListByAreaIds(List<Long> areaIds) {
        return areaMapper.selectAreaListByAreaIds(areaIds);
    }

    /**
     * 逻辑删除区域表信息
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteAreaByAreaDTO(AreaDTO areaDTO) {
        Long areaId = areaDTO.getAreaId();
        if (StringUtils.isNull(areaId)) {
            throw new ServiceException("区域ID不能为空");
        }
        AreaDTO areaById = areaMapper.selectAreaByAreaId(areaId);
        if (StringUtils.isNull(areaById)) {
            throw new ServiceException("当前区域配置不存在");
        }
        R<List<OfficialRankDecomposeDTO>> listR = officialRankSystemService.selectOfficialDecomposeByDimension(areaId, 2, SecurityConstants.INNER);
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用职级分解失败 请联系管理员");
        }
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = listR.getData();
        if (StringUtils.isNotEmpty(officialRankDecomposeDTOS)) {
            throw new ServiceException("当前区域配置正在被职级分解引用 无法删除");
        }
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        return areaMapper.logicDeleteAreaByAreaId(area, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 逻辑批量删除区域表
     *
     * @param areaIds 需要删除的区域表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteAreaByAreaIds(List<Long> areaIds) {
        if (StringUtils.isEmpty(areaIds)) {
            throw new ServiceException("区域id不能为空");
        }
        List<AreaDTO> areaByIds = areaMapper.selectAreaListByAreaIds(areaIds);
        if (areaByIds.size() < areaIds.size()) {
            throw new ServiceException("区域配置已不存在");
        }
        R<List<OfficialRankDecomposeDTO>> listR = officialRankSystemService.selectOfficialDecomposeByDimensions(areaIds, 2, SecurityConstants.INNER);
        if (listR.getCode() != 200) {
            throw new ServiceException("远程调用职级分解失败 请联系管理员");
        }
        List<OfficialRankDecomposeDTO> officialRankDecomposeDTOS = listR.getData();
        if (StringUtils.isNotEmpty(officialRankDecomposeDTOS)) {
            throw new ServiceException("当前区域配置正在被职级分解引用 无法删除");
        }
        return areaMapper.logicDeleteAreaByAreaIds(areaIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除区域表信息
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAreaByAreaId(AreaDTO areaDTO) {
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        return areaMapper.deleteAreaByAreaId(area.getAreaId());
    }

    /**
     * 物理批量删除区域表
     *
     * @param areaDtos 需要删除的区域表主键
     * @return 结果
     */
    @Transactional
    @Override
    public int deleteAreaByAreaIds(List<AreaDTO> areaDtos) {
        List<Long> stringList = new ArrayList<>();
        for (AreaDTO areaDTO : areaDtos) {
            stringList.add(areaDTO.getAreaId());
        }
        return areaMapper.deleteAreaByAreaIds(stringList);
    }

    /**
     * 批量新增区域表信息
     *
     * @param areaDtos 区域表对象
     */
    @Transactional
    public int insertAreas(List<AreaDTO> areaDtos) {
        List<Area> areaList = new ArrayList<>();

        for (AreaDTO areaDTO : areaDtos) {
            Area area = new Area();
            BeanUtils.copyProperties(areaDTO, area);
            area.setCreateBy(SecurityUtils.getUserId());
            area.setCreateTime(DateUtils.getNowDate());
            area.setUpdateTime(DateUtils.getNowDate());
            area.setUpdateBy(SecurityUtils.getUserId());
            area.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            areaList.add(area);
        }
        return areaMapper.batchArea(areaList);
    }

    /**
     * 批量修改区域表信息
     *
     * @param areaDtos 区域表对象
     */
    @Transactional
    public int updateAreas(List<AreaDTO> areaDtos) {
        List<Area> areaList = new ArrayList<>();

        for (AreaDTO areaDTO : areaDtos) {
            Area area = new Area();
            BeanUtils.copyProperties(areaDTO, area);
            area.setCreateBy(SecurityUtils.getUserId());
            area.setCreateTime(DateUtils.getNowDate());
            area.setUpdateTime(DateUtils.getNowDate());
            area.setUpdateBy(SecurityUtils.getUserId());
            areaList.add(area);
        }
        return areaMapper.updateAreas(areaList);
    }
}

