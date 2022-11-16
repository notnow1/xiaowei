package net.qixiaowei.system.manage.mapper.system;

import java.util.List;

import net.qixiaowei.system.manage.api.domain.system.Region;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.vo.system.RegionVO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.Set;


/**
 * RegionMapper接口
 *
 * @author hzk
 * @since 2022-10-20
 */
public interface RegionMapper {
    /**
     * 查询区域表
     *
     * @param regionId 区域表主键
     * @return 区域表
     */
    RegionDTO selectRegionByRegionId(@Param("regionId") Long regionId);


    /**
     * 批量查询区域表
     *
     * @param regionIds 区域表主键集合
     * @return 区域表
     */
    List<RegionDTO> selectRegionByRegionIds(@Param("regionIds") Set<Long> regionIds);

    /**
     * 查询区域表列表
     *
     * @param region 区域表
     * @return 区域表集合
     */
    List<RegionDTO> selectRegionList(@Param("region") Region region);

    /**
     * 查询区域表列表
     *
     * @param region 区域表
     * @return 区域表集合
     */
    List<RegionVO> selectRegions(@Param("region") Region region);

    /**
     * 新增区域表
     *
     * @param region 区域表
     * @return 结果
     */
    int insertRegion(@Param("region") Region region);

    /**
     * 修改区域表
     *
     * @param region 区域表
     * @return 结果
     */
    int updateRegion(@Param("region") Region region);

    /**
     * 批量修改区域表
     *
     * @param regionList 区域表
     * @return 结果
     */
    int updateRegions(@Param("regionList") List<Region> regionList);

    /**
     * 逻辑删除区域表
     *
     * @param region
     * @return 结果
     */
    int logicDeleteRegionByRegionId(@Param("region") Region region);

    /**
     * 逻辑批量删除区域表
     *
     * @param regionIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteRegionByRegionIds(@Param("regionIds") List<Long> regionIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除区域表
     *
     * @param regionId 区域表主键
     * @return 结果
     */
    int deleteRegionByRegionId(@Param("regionId") Long regionId);

    /**
     * 物理批量删除区域表
     *
     * @param regionIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteRegionByRegionIds(@Param("regionIds") List<Long> regionIds);

    /**
     * 批量新增区域表
     *
     * @param Regions 区域表列表
     * @return 结果
     */
    int batchRegion(@Param("regions") List<Region> Regions);

    /**
     * 根据层级查询区域表
     *
     * @param level 区域表主键
     * @return 区域表
     */
    List<RegionDTO> selectRegionByLevel(@Param("level") Integer level);

    /**
     * 根据省份名称集合获取省份信息
     * @param regionNames
     * @return
     */
    List<RegionDTO> selectCodeList(@Param("regionNames") List<String> regionNames);
}
