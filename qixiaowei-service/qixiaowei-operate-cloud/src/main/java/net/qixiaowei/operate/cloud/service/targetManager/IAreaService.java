package net.qixiaowei.operate.cloud.service.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;


/**
 * AreaService接口
 *
 * @author Graves
 * @since 2022-10-07
 */
public interface IAreaService {
    /**
     * 查询区域表
     *
     * @param areaId 区域表主键
     * @return 区域表
     */
    AreaDTO selectAreaByAreaId(Long areaId);

    /**
     * 查询区域表列表
     *
     * @param areaDTO 区域表
     * @return 区域表集合
     */
    List<AreaDTO> selectAreaList(AreaDTO areaDTO);

    /**
     * 生成区域编码
     *
     * @return 区域编码
     */
    String generateAreaCode();

    /**
     * 新增区域表
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    int insertArea(AreaDTO areaDTO);

    /**
     * 修改区域表
     *
     * @param areaDTO 区域表
     * @return 结果
     */
    int updateArea(AreaDTO areaDTO);

    /**
     * 批量修改区域表
     *
     * @param areaDtos 区域表
     * @return 结果
     */
    int updateAreas(List<AreaDTO> areaDtos);

    /**
     * 批量新增区域表
     *
     * @param areaDtos 区域表
     * @return 结果
     */
    int insertAreas(List<AreaDTO> areaDtos);

    /**
     * 逻辑批量删除区域表
     *
     * @param areaIds 需要删除的区域表集合
     * @return 结果
     */
    int logicDeleteAreaByAreaIds(List<Long> areaIds);

    /**
     * 逻辑删除区域表信息
     *
     * @param areaDTO
     * @return 结果
     */
    int logicDeleteAreaByAreaDTO(AreaDTO areaDTO);

    /**
     * 逻辑批量删除区域表
     *
     * @param AreaDtos 需要删除的区域表集合
     * @return 结果
     */
    int deleteAreaByAreaIds(List<AreaDTO> AreaDtos);

    /**
     * 逻辑删除区域表信息
     *
     * @param areaDTO
     * @return 结果
     */
    int deleteAreaByAreaId(AreaDTO areaDTO);


    /**
     * 删除区域表信息
     *
     * @param areaId 区域表主键
     * @return 结果
     */
    int deleteAreaByAreaId(Long areaId);

    /**
     * 查询分解维度区域下拉列表
     *
     * @param areaDTO
     * @return
     */
    List<AreaDTO> selectDropList(AreaDTO areaDTO);

    /**
     * 查询区域配置列表通过IDS
     *
     * @param areaIds 区域表集合
     * @return 结果
     */
    List<AreaDTO> selectAreaListByAreaIds(List<Long> areaIds);
}
