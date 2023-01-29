package net.qixiaowei.system.manage.mapper.basic;

import net.qixiaowei.system.manage.api.domain.basic.Indicator;
import net.qixiaowei.system.manage.api.dto.basic.IndicatorDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;


/**
 * IndicatorMapper接口
 *
 * @author Graves
 * @since 2022-09-28
 */
public interface IndicatorMapper {
    /**
     * 查询指标表
     *
     * @param indicatorId 指标表主键
     * @return 指标表
     */
    IndicatorDTO selectIndicatorByIndicatorId(@Param("indicatorId") Long indicatorId);

    /**
     * 查询指标表列表
     *
     * @param indicatorDTO 指标表
     * @return 指标表集合
     */
    List<IndicatorDTO> selectIndicatorList(@Param("indicator") IndicatorDTO indicatorDTO);

    /**
     * 查询指标树状表
     *
     * @param indicator
     * @return
     */
    List<IndicatorDTO> selectIndicatorTree(@Param("indicator") Indicator indicator);

    /**
     * 查询指标编码集合
     *
     * @param prefixCodeRule 编码前缀
     * @return 指标编码集合
     */
    List<String> getIndicatorCodes(@Param("prefixCodeRule") String prefixCodeRule);

    /**
     * 根据父级id查找子级
     *
     * @param indicatorId
     * @return
     */
    List<IndicatorDTO> selectSon(@Param("indicatorId") Long indicatorId);

    /**
     * 根据父级id查找子级的最大排序值
     *
     * @param parentIndicatorId
     * @return
     */
    Integer selectSortByIndicatorId(@Param("indicatorId") Long parentIndicatorId);

    /**
     * 根据父级id批量查找子级
     *
     * @param indicatorIds
     * @return
     */
    List<Long> selectSons(@Param("indicatorIds") List<Long> indicatorIds);

    /**
     * 新增指标表
     *
     * @param indicator 指标表
     * @return 结果
     */
    int insertIndicator(@Param("indicator") Indicator indicator);

    /**
     * 修改指标表
     *
     * @param indicator 指标表
     * @return 结果
     */
    int updateIndicator(@Param("indicator") Indicator indicator);

    /**
     * 批量修改指标表
     *
     * @param indicatorList 指标表
     * @return 结果
     */
    int updateIndicators(@Param("indicatorList") List<Indicator> indicatorList);

    /**
     * 逻辑删除指标表
     *
     * @param indicator
     * @return 结果
     */
    int logicDeleteIndicatorByIndicatorId(@Param("indicator") Indicator indicator, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);

    /**
     * 逻辑批量删除指标表
     *
     * @param indicatorIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteIndicatorByIndicatorIds(@Param("indicatorIds") List<Long> indicatorIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除指标表
     *
     * @param indicatorId 指标表主键
     * @return 结果
     */
    int deleteIndicatorByIndicatorId(@Param("indicatorId") Long indicatorId);

    /**
     * 物理批量删除指标表
     *
     * @param indicatorIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteIndicatorByIndicatorIds(@Param("indicatorIds") List<Long> indicatorIds);

    /**
     * 批量新增指标表
     *
     * @param Indicators 指标表列表
     * @return 结果
     */
    int batchIndicator(@Param("indicators") List<Indicator> Indicators);

    /**
     * 指标编码唯一性校验
     *
     * @param indicatorCode
     * @return
     */
    IndicatorDTO getIndicatorByCode(@Param("indicatorCode") String indicatorCode);

    /**
     * 新增时修改指标祖级列表ID
     *
     * @param indicator 行业
     * @return 结果
     */
    int updateAncestors(@Param("indicator") Indicator indicator);

    /**
     * 被引用校验
     *
     * @param indicatorCategoryIds 指标类型集合
     * @return
     */
    List<IndicatorDTO> selectIndicatorCountByIndicatorCategoryId(@Param("indicatorCategoryIds") List<Long> indicatorCategoryIds);

    /**
     * 获取指标最大层级
     */
    List<Integer> selectLevel();

    /**
     * 通过CodeList查找指标列表
     *
     * @param indicatorCodes
     * @return
     */
    List<IndicatorDTO> selectIndicatorByCodeList(@Param("indicatorCodes") List<String> indicatorCodes);

    /**
     * 查找目标制定指标列表
     *
     * @param indicatorDTO
     * @return
     */
    List<IndicatorDTO> selectTargetIndicatorList(@Param("indicatorDTO") IndicatorDTO indicatorDTO);

    /**
     * 通过指标IDS查找指标
     *
     * @param indicatorIds
     * @return
     */
    List<IndicatorDTO> selectIndicatorByIds(@Param("indicatorIds") List<Long> indicatorIds);

    /**
     * 通过指标名称获取指标列表
     *
     * @param indicatorNames
     * @return
     */
    List<IndicatorDTO> selectIndicatorByNames(@Param("indicatorNames") List<String> indicatorNames);

    /**
     * 根据id查询所有子级数据
     *
     * @param indicatorId 指标ID
     * @return
     */
    List<IndicatorDTO> selectAncestors(@Param("indicatorId") Long indicatorId);
}
