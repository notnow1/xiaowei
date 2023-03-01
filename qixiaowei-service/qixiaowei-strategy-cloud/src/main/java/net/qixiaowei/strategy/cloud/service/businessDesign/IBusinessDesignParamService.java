package net.qixiaowei.strategy.cloud.service.businessDesign;

import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignParamDTO;

import java.util.List;


/**
 * BusinessDesignParamService接口
 *
 * @author Graves
 * @since 2023-02-28
 */
public interface IBusinessDesignParamService {
    /**
     * 查询业务设计参数表
     *
     * @param businessDesignParamId 业务设计参数表主键
     * @return 业务设计参数表
     */
    BusinessDesignParamDTO selectBusinessDesignParamByBusinessDesignParamId(Long businessDesignParamId);

    /**
     * 查询业务设计参数表列表
     *
     * @param businessDesignParamDTO 业务设计参数表
     * @return 业务设计参数表集合
     */
    List<BusinessDesignParamDTO> selectBusinessDesignParamList(BusinessDesignParamDTO businessDesignParamDTO);

    /**
     * 新增业务设计参数表
     *
     * @param businessDesignParamDTO 业务设计参数表
     * @return 结果
     */
    BusinessDesignParamDTO insertBusinessDesignParam(BusinessDesignParamDTO businessDesignParamDTO);

    /**
     * 修改业务设计参数表
     *
     * @param businessDesignParamDTO 业务设计参数表
     * @return 结果
     */
    int updateBusinessDesignParam(BusinessDesignParamDTO businessDesignParamDTO);

    /**
     * 批量修改业务设计参数表
     *
     * @param businessDesignParamDtos 业务设计参数表
     * @return 结果
     */
    int updateBusinessDesignParams(List<BusinessDesignParamDTO> businessDesignParamDtos);

    /**
     * 批量新增业务设计参数表
     *
     * @param businessDesignParamDtos 业务设计参数表
     * @return 结果
     */
    int insertBusinessDesignParams(List<BusinessDesignParamDTO> businessDesignParamDtos);

    /**
     * 逻辑批量删除业务设计参数表
     *
     * @param businessDesignParamIds 需要删除的业务设计参数表集合
     * @return 结果
     */
    int logicDeleteBusinessDesignParamByBusinessDesignParamIds(List<Long> businessDesignParamIds);

    /**
     * 逻辑删除业务设计参数表信息
     *
     * @param businessDesignParamDTO
     * @return 结果
     */
    int logicDeleteBusinessDesignParamByBusinessDesignParamId(BusinessDesignParamDTO businessDesignParamDTO);

    /**
     * 批量删除业务设计参数表
     *
     * @param BusinessDesignParamDtos
     * @return 结果
     */
    int deleteBusinessDesignParamByBusinessDesignParamIds(List<BusinessDesignParamDTO> BusinessDesignParamDtos);

    /**
     * 逻辑删除业务设计参数表信息
     *
     * @param businessDesignParamDTO
     * @return 结果
     */
    int deleteBusinessDesignParamByBusinessDesignParamId(BusinessDesignParamDTO businessDesignParamDTO);


    /**
     * 删除业务设计参数表信息
     *
     * @param businessDesignParamId 业务设计参数表主键
     * @return 结果
     */
    int deleteBusinessDesignParamByBusinessDesignParamId(Long businessDesignParamId);

    /**
     * 根绝业务设计ID查询参数信息
     *
     * @param businessDesignId 业务设计参数表主键
     * @return 结果
     */
    List<BusinessDesignParamDTO> selectBusinessDesignParamByBusinessDesignId(Long businessDesignId);

    /**
     * 根绝业务设计ID集合查询参数信息
     *
     * @param businessDesignIds 业务设计参数表主键集合
     * @return 结果
     */
    List<BusinessDesignParamDTO> selectBusinessDesignParamByBusinessDesignIds(List<Long> businessDesignIds);
}
