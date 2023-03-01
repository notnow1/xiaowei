package net.qixiaowei.strategy.cloud.mapper.businessDesign;

import java.util.List;

import net.qixiaowei.strategy.cloud.api.domain.businessDesign.BusinessDesignParam;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * BusinessDesignParamMapper接口
 *
 * @author Graves
 * @since 2023-02-28
 */
public interface BusinessDesignParamMapper {
    /**
     * 查询业务设计参数表
     *
     * @param businessDesignParamId 业务设计参数表主键
     * @return 业务设计参数表
     */
    BusinessDesignParamDTO selectBusinessDesignParamByBusinessDesignParamId(@Param("businessDesignParamId") Long businessDesignParamId);


    /**
     * 批量查询业务设计参数表
     *
     * @param businessDesignParamIds 业务设计参数表主键集合
     * @return 业务设计参数表
     */
    List<BusinessDesignParamDTO> selectBusinessDesignParamByBusinessDesignParamIds(@Param("businessDesignParamIds") List<Long> businessDesignParamIds);

    /**
     * 查询业务设计参数表列表
     *
     * @param businessDesignParam 业务设计参数表
     * @return 业务设计参数表集合
     */
    List<BusinessDesignParamDTO> selectBusinessDesignParamList(@Param("businessDesignParam") BusinessDesignParam businessDesignParam);

    /**
     * 新增业务设计参数表
     *
     * @param businessDesignParam 业务设计参数表
     * @return 结果
     */
    int insertBusinessDesignParam(@Param("businessDesignParam") BusinessDesignParam businessDesignParam);

    /**
     * 修改业务设计参数表
     *
     * @param businessDesignParam 业务设计参数表
     * @return 结果
     */
    int updateBusinessDesignParam(@Param("businessDesignParam") BusinessDesignParam businessDesignParam);

    /**
     * 批量修改业务设计参数表
     *
     * @param businessDesignParamList 业务设计参数表
     * @return 结果
     */
    int updateBusinessDesignParams(@Param("businessDesignParamList") List<BusinessDesignParam> businessDesignParamList);

    /**
     * 逻辑删除业务设计参数表
     *
     * @param businessDesignParam
     * @return 结果
     */
    int logicDeleteBusinessDesignParamByBusinessDesignParamId(@Param("businessDesignParam") BusinessDesignParam businessDesignParam);

    /**
     * 逻辑批量删除业务设计参数表
     *
     * @param businessDesignParamIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteBusinessDesignParamByBusinessDesignParamIds(@Param("businessDesignParamIds") List<Long> businessDesignParamIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除业务设计参数表
     *
     * @param businessDesignParamId 业务设计参数表主键
     * @return 结果
     */
    int deleteBusinessDesignParamByBusinessDesignParamId(@Param("businessDesignParamId") Long businessDesignParamId);

    /**
     * 物理批量删除业务设计参数表
     *
     * @param businessDesignParamIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBusinessDesignParamByBusinessDesignParamIds(@Param("businessDesignParamIds") List<Long> businessDesignParamIds);

    /**
     * 批量新增业务设计参数表
     *
     * @param businessDesignParams 业务设计参数表列表
     * @return 结果
     */
    int batchBusinessDesignParam(@Param("businessDesignParams") List<BusinessDesignParam> businessDesignParams);

    /**
     * 根绝设计ID获取业务设计参数表
     *
     * @param businessDesignId 业务设计ID
     * @return List
     */
    List<BusinessDesignParamDTO> selectBusinessDesignParamByBusinessDesignId(@Param("businessDesignId") Long businessDesignId);

    /**
     * 根绝业务设计ID集合查询参数信息
     *
     * @param businessDesignIds 业务设计参数表主键集合
     * @return 结果
     */
    List<BusinessDesignParamDTO> selectBusinessDesignParamByBusinessDesignIds(@Param("businessDesignIds") List<Long> businessDesignIds);
}
