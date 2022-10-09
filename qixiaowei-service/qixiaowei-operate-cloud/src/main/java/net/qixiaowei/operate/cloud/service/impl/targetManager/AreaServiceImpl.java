package net.qixiaowei.operate.cloud.service.impl.targetManager;

import java.util.List;

import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
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
        int areaCodeCount = areaMapper.checkUnique(areaCode);
        if (areaCodeCount > 0) {
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
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        area.setUpdateTime(DateUtils.getNowDate());
        area.setUpdateBy(SecurityUtils.getUserId());
        return areaMapper.updateArea(area);
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
        int exist = areaMapper.isExist(areaIds);
        if (exist != areaIds.size()) {
            throw new ServiceException("区域配置已不存在");
        }
        return areaMapper.logicDeleteAreaByAreaIds(areaIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
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
     * 逻辑删除区域表信息
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    @Transactional
    @Override
    public int logicDeleteAreaByAreaId(AreaDTO areaDTO) {
        Area area = new Area();
        BeanUtils.copyProperties(areaDTO, area);
        return areaMapper.logicDeleteAreaByAreaId(area, SecurityUtils.getUserId(), DateUtils.getNowDate());
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

