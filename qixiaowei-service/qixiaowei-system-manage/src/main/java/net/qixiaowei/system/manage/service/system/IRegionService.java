package net.qixiaowei.system.manage.service.system;

import java.util.List;
import java.util.Set;

import cn.hutool.core.lang.tree.Tree;
import net.qixiaowei.system.manage.api.dto.system.RegionDTO;
import net.qixiaowei.system.manage.api.vo.system.RegionVO;


/**
 * RegionService接口
 *
 * @author hzk
 * @since 2022-10-20
 */
public interface IRegionService {
    /**
     * 查询区域表
     *
     * @param regionId 区域表主键
     * @return 区域表
     */
    RegionDTO selectRegionByRegionId(Long regionId);

    /**
     * 根据层级查询区域表
     *
     * @param level 区域表主键
     * @return 区域表
     */
    List<RegionDTO> selectRegionByLevel(Integer level);

    /**
     * 查询区域表列表
     *
     * @param regionIds 区域ids
     * @return 区域表集合
     */
    List<RegionDTO> getRegionsByIds(Set<Long> regionIds);

    /**
     * 查询区域表列表
     *
     * @param regionDTO 区域表
     * @return 区域表集合
     */
    List<RegionDTO> selectRegionList(RegionDTO regionDTO);

    /**
     * 查询区域表列表
     *
     * @param regionDTO 区域表
     * @return 区域表树集合
     */
    List<Tree<Long>> selectRegionTreeList(RegionDTO regionDTO);

    /**
     * 查询区域表列表
     *
     * @param regionDTO 区域表
     * @return 区域表集合
     */
    List<RegionVO> selectRegions(RegionDTO regionDTO);

    /**
     * 新增区域表
     *
     * @param regionDTO 区域表
     * @return 结果
     */
    RegionDTO insertRegion(RegionDTO regionDTO);

    /**
     * 修改区域表
     *
     * @param regionDTO 区域表
     * @return 结果
     */
    int updateRegion(RegionDTO regionDTO);

    /**
     * 批量修改区域表
     *
     * @param regionDtos 区域表
     * @return 结果
     */
    int updateRegions(List<RegionDTO> regionDtos);

    /**
     * 批量新增区域表
     *
     * @param regionDtos 区域表
     * @return 结果
     */
    int insertRegions(List<RegionDTO> regionDtos);

    /**
     * 逻辑批量删除区域表
     *
     * @param regionIds 需要删除的区域表集合
     * @return 结果
     */
    int logicDeleteRegionByRegionIds(List<Long> regionIds);

    /**
     * 逻辑删除区域表信息
     *
     * @param regionDTO
     * @return 结果
     */
    int logicDeleteRegionByRegionId(RegionDTO regionDTO);

    /**
     * 批量删除区域表
     *
     * @param RegionDtos
     * @return 结果
     */
    int deleteRegionByRegionIds(List<RegionDTO> RegionDtos);

    /**
     * 逻辑删除区域表信息
     *
     * @param regionDTO
     * @return 结果
     */
    int deleteRegionByRegionId(RegionDTO regionDTO);


    /**
     * 删除区域表信息
     *
     * @param regionId 区域表主键
     * @return 结果
     */
    int deleteRegionByRegionId(Long regionId);

    /**
     * 根据省份名称集合获取省份信息
     * @param regionNames
     * @return
     */
    List<RegionDTO> selectCodeList(List<String> regionNames);

    /**
     * 远程查询excel省份下拉框
     * @param regionDTO
     * @return
     */
    List<RegionDTO> getDropList(RegionDTO regionDTO);
}
