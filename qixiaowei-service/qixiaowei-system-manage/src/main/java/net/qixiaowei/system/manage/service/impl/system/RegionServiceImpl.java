package net.qixiaowei.system.manage.service.impl.system;

import java.util.List;

import net.qixiaowei.integration.common.constant.Constants;
import net.qixiaowei.integration.common.utils.DateUtils;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.system.manage.api.vo.system.RegionVO;
import org.springframework.beans.factory.annotation.Autowired;
import net.qixiaowei.integration.common.utils.bean.BeanUtils;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Set;

import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.domain.system.Region;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.mapper.system.RegionMapper;
import net.qixiaowei.system.manage.service.system.IRegionService;
import net.qixiaowei.integration.common.constant.DBDeleteFlagConstants;


/**
 * RegionService业务层处理
 *
 * @author hzk
 * @since 2022-10-20
 */
@Service
public class RegionServiceImpl implements IRegionService {
    @Autowired
    private RegionMapper regionMapper;

    /**
     * 查询区域表
     *
     * @param regionId 区域表主键
     * @return 区域表
     */
    @Override
    public RegionDTO selectRegionByRegionId(Long regionId) {
        return regionMapper.selectRegionByRegionId(regionId);
    }

    @Override
    public List<RegionDTO> getRegionsByIds(Set<Long> regionIds) {
        return regionMapper.selectRegionByRegionIds(regionIds);
    }

    /**
     * 查询区域表列表
     *
     * @param regionDTO 区域表
     * @return 区域表
     */
    @Override
    public List<RegionDTO> selectRegionList(RegionDTO regionDTO) {
        Region region = new Region();
        BeanUtils.copyProperties(regionDTO, region);
        return regionMapper.selectRegionList(region);
    }

    /**
     * 查询区域表列表
     *
     * @param regionDTO 区域表
     * @return 区域表
     */
    @Override
    public List<RegionVO> selectRegions(RegionDTO regionDTO) {
        Region region = new Region();
        BeanUtils.copyProperties(regionDTO, region);
        if (StringUtils.isNull(region.getParentRegionId())) {
            region.setParentRegionId(Constants.TOP_PARENT_ID);
        }
        return regionMapper.selectRegions(region);
    }

    /**
     * 新增区域表
     *
     * @param regionDTO 区域表
     * @return 结果
     */
    @Override
    public RegionDTO insertRegion(RegionDTO regionDTO) {
        Region region = new Region();
        BeanUtils.copyProperties(regionDTO, region);
        region.setCreateBy(SecurityUtils.getUserId());
        region.setCreateTime(DateUtils.getNowDate());
        region.setUpdateTime(DateUtils.getNowDate());
        region.setUpdateBy(SecurityUtils.getUserId());
        region.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
        regionMapper.insertRegion(region);
        regionDTO.setRegionId(region.getRegionId());
        return regionDTO;
    }

    /**
     * 修改区域表
     *
     * @param regionDTO 区域表
     * @return 结果
     */
    @Override
    public int updateRegion(RegionDTO regionDTO) {
        Region region = new Region();
        BeanUtils.copyProperties(regionDTO, region);
        region.setUpdateTime(DateUtils.getNowDate());
        region.setUpdateBy(SecurityUtils.getUserId());
        return regionMapper.updateRegion(region);
    }

    /**
     * 逻辑批量删除区域表
     *
     * @param regionIds 主键集合
     * @return 结果
     */
    @Override
    public int logicDeleteRegionByRegionIds(List<Long> regionIds) {
        return regionMapper.logicDeleteRegionByRegionIds(regionIds, SecurityUtils.getUserId(), DateUtils.getNowDate());
    }

    /**
     * 物理删除区域表信息
     *
     * @param regionId 区域表主键
     * @return 结果
     */
    @Override
    public int deleteRegionByRegionId(Long regionId) {
        return regionMapper.deleteRegionByRegionId(regionId);
    }

    /**
     * 逻辑删除区域表信息
     *
     * @param regionDTO 区域表
     * @return 结果
     */
    @Override
    public int logicDeleteRegionByRegionId(RegionDTO regionDTO) {
        Region region = new Region();
        region.setRegionId(regionDTO.getRegionId());
        region.setUpdateTime(DateUtils.getNowDate());
        region.setUpdateBy(SecurityUtils.getUserId());
        return regionMapper.logicDeleteRegionByRegionId(region);
    }

    /**
     * 物理删除区域表信息
     *
     * @param regionDTO 区域表
     * @return 结果
     */

    @Override
    public int deleteRegionByRegionId(RegionDTO regionDTO) {
        Region region = new Region();
        BeanUtils.copyProperties(regionDTO, region);
        return regionMapper.deleteRegionByRegionId(region.getRegionId());
    }

    /**
     * 物理批量删除区域表
     *
     * @param regionDtos 需要删除的区域表主键
     * @return 结果
     */

    @Override
    public int deleteRegionByRegionIds(List<RegionDTO> regionDtos) {
        List<Long> stringList = new ArrayList();
        for (RegionDTO regionDTO : regionDtos) {
            stringList.add(regionDTO.getRegionId());
        }
        return regionMapper.deleteRegionByRegionIds(stringList);
    }

    /**
     * 批量新增区域表信息
     *
     * @param regionDtos 区域表对象
     */

    public int insertRegions(List<RegionDTO> regionDtos) {
        List<Region> regionList = new ArrayList();

        for (RegionDTO regionDTO : regionDtos) {
            Region region = new Region();
            BeanUtils.copyProperties(regionDTO, region);
            region.setCreateBy(SecurityUtils.getUserId());
            region.setCreateTime(DateUtils.getNowDate());
            region.setUpdateTime(DateUtils.getNowDate());
            region.setUpdateBy(SecurityUtils.getUserId());
            region.setDeleteFlag(DBDeleteFlagConstants.DELETE_FLAG_ZERO);
            regionList.add(region);
        }
        return regionMapper.batchRegion(regionList);
    }

    /**
     * 批量修改区域表信息
     *
     * @param regionDtos 区域表对象
     */

    public int updateRegions(List<RegionDTO> regionDtos) {
        List<Region> regionList = new ArrayList();

        for (RegionDTO regionDTO : regionDtos) {
            Region region = new Region();
            BeanUtils.copyProperties(regionDTO, region);
            region.setCreateBy(SecurityUtils.getUserId());
            region.setCreateTime(DateUtils.getNowDate());
            region.setUpdateTime(DateUtils.getNowDate());
            region.setUpdateBy(SecurityUtils.getUserId());
            regionList.add(region);
        }
        return regionMapper.updateRegions(regionList);
    }
}

