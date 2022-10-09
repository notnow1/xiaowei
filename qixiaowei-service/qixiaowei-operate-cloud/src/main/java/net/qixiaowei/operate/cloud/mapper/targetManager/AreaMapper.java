package net.qixiaowei.operate.cloud.mapper.targetManager;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.targetManager.Area;
import net.qixiaowei.operate.cloud.api.dto.targetManager.AreaDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * AreaMapper接口
 *
 * @author Graves
 * @since 2022-10-07
 */
public interface AreaMapper {
    /**
     * 查询区域表
     *
     * @param areaId 区域表主键
     * @return 区域表
     */
    AreaDTO selectAreaByAreaId(@Param("areaId") Long areaId);

    /**
     * 查询区域表列表
     *
     * @param area 区域表
     * @return 区域表集合
     */
    List<AreaDTO> selectAreaList(@Param("area") Area area);

    /**
     * 新增区域表
     *
     * @param area 区域表
     * @return 结果
     */
    int insertArea(@Param("area") Area area);

    /**
     * 修改区域表
     *
     * @param area 区域表
     * @return 结果
     */
    int updateArea(@Param("area") Area area);

    /**
     * 批量修改区域表
     *
     * @param areaList 区域表
     * @return 结果
     */
    int updateAreas(@Param("areaList") List<Area> areaList);

    /**
     * 逻辑删除区域表
     *
     * @param area
     * @return 结果
     */
    int logicDeleteAreaByAreaId(@Param("area") Area area, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除区域表
     *
     * @param areaIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteAreaByAreaIds(@Param("areaIds") List<Long> areaIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除区域表
     *
     * @param areaId 区域表主键
     * @return 结果
     */
    int deleteAreaByAreaId(@Param("areaId") Long areaId);

    /**
     * 物理批量删除区域表
     *
     * @param areaIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteAreaByAreaIds(@Param("areaIds") List<Long> areaIds);

    /**
     * 批量新增区域表
     *
     * @param Areas 区域表列表
     * @return 结果
     */
    int batchArea(@Param("areas") List<Area> Areas);

    /**
     * 区域编码重复性校验
     *
     * @param areaCode
     * @return
     */
    int checkUnique(@Param("areaCode") String areaCode);

    /**
     * 根据id集合判断是否存在
     *
     * @param areaIds
     * @return
     */
    int isExist(@Param("areaIds") List<Long> areaIds);
}
