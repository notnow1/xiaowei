package net.qixiaowei.strategy.cloud.service.businessDesign;

import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;

import java.util.List;


/**
 * BusinessDesignService接口
 *
 * @author Graves
 * @since 2023-02-28
 */
public interface IBusinessDesignService {
    /**
     * 查询业务设计表
     *
     * @param businessDesignId 业务设计表主键
     * @return 业务设计表
     */
    BusinessDesignDTO selectBusinessDesignByBusinessDesignId(Long businessDesignId);

    /**
     * 查询业务设计表列表
     *
     * @param businessDesignDTO 业务设计表
     * @return 业务设计表集合
     */
    List<BusinessDesignDTO> selectBusinessDesignList(BusinessDesignDTO businessDesignDTO);

    /**
     * 新增业务设计表
     *
     * @param businessDesignDTO 业务设计表
     * @return 结果
     */
    BusinessDesignDTO insertBusinessDesign(BusinessDesignDTO businessDesignDTO);

    /**
     * 修改业务设计表
     *
     * @param businessDesignDTO 业务设计表
     * @return 结果
     */
    int updateBusinessDesign(BusinessDesignDTO businessDesignDTO);

    /**
     * 批量修改业务设计表
     *
     * @param businessDesignDtoS 业务设计表
     * @return 结果
     */
    int updateBusinessDesigns(List<BusinessDesignDTO> businessDesignDtoS);

    /**
     * 批量新增业务设计表
     *
     * @param businessDesignDtoS 业务设计表
     * @return 结果
     */
    int insertBusinessDesigns(List<BusinessDesignDTO> businessDesignDtoS);

    /**
     * 逻辑批量删除业务设计表
     *
     * @param businessDesignIds 需要删除的业务设计表集合
     * @return 结果
     */
    int logicDeleteBusinessDesignByBusinessDesignIds(List<Long> businessDesignIds);

    /**
     * 逻辑删除业务设计表信息
     *
     * @param businessDesignDTO dto
     * @return 结果
     */
    int logicDeleteBusinessDesignByBusinessDesignId(BusinessDesignDTO businessDesignDTO);

    /**
     * 批量删除业务设计表
     *
     * @param BusinessDesignDtoS 集合
     * @return 结果
     */
    int deleteBusinessDesignByBusinessDesignIds(List<BusinessDesignDTO> BusinessDesignDtoS);

    /**
     * 逻辑删除业务设计表信息
     *
     * @param businessDesignDTO dto
     * @return 结果
     */
    int deleteBusinessDesignByBusinessDesignId(BusinessDesignDTO businessDesignDTO);


    /**
     * 删除业务设计表信息
     *
     * @param businessDesignId 业务设计表主键
     * @return 结果
     */
    int deleteBusinessDesignByBusinessDesignId(Long businessDesignId);
}
