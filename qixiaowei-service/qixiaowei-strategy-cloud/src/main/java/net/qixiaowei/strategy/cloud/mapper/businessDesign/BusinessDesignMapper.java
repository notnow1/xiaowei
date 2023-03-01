package net.qixiaowei.strategy.cloud.mapper.businessDesign;

import java.util.List;
import net.qixiaowei.strategy.cloud.api.domain.businessDesign.BusinessDesign;
import net.qixiaowei.strategy.cloud.api.dto.businessDesign.BusinessDesignDTO;
import org.apache.ibatis.annotations.Param;
import java.util.Date;


/**
* BusinessDesignMapper接口
* @author Graves
* @since 2023-02-28
*/
public interface BusinessDesignMapper{
    /**
    * 查询业务设计表
    *
    * @param businessDesignId 业务设计表主键
    * @return 业务设计表
    */
    BusinessDesignDTO selectBusinessDesignByBusinessDesignId(@Param("businessDesignId")Long businessDesignId);


    /**
    * 批量查询业务设计表
    *
    * @param businessDesignIds 业务设计表主键集合
    * @return 业务设计表
    */
    List<BusinessDesignDTO> selectBusinessDesignByBusinessDesignIds(@Param("businessDesignIds") List<Long> businessDesignIds);

    /**
    * 查询业务设计表列表
    *
    * @param businessDesign 业务设计表
    * @return 业务设计表集合
    */
    List<BusinessDesignDTO> selectBusinessDesignList(@Param("businessDesign")BusinessDesign businessDesign);

    /**
    * 新增业务设计表
    *
    * @param businessDesign 业务设计表
    * @return 结果
    */
    int insertBusinessDesign(@Param("businessDesign")BusinessDesign businessDesign);

    /**
    * 修改业务设计表
    *
    * @param businessDesign 业务设计表
    * @return 结果
    */
    int updateBusinessDesign(@Param("businessDesign")BusinessDesign businessDesign);

    /**
    * 批量修改业务设计表
    *
    * @param businessDesignList 业务设计表
    * @return 结果
    */
    int updateBusinessDesigns(@Param("businessDesignList")List<BusinessDesign> businessDesignList);
    /**
    * 逻辑删除业务设计表
    *
    * @param businessDesign
    * @return 结果
    */
    int logicDeleteBusinessDesignByBusinessDesignId(@Param("businessDesign")BusinessDesign businessDesign);

    /**
    * 逻辑批量删除业务设计表
    *
    * @param businessDesignIds 需要删除的数据主键集合
    * @return 结果
    */
    int logicDeleteBusinessDesignByBusinessDesignIds(@Param("businessDesignIds")List<Long> businessDesignIds,@Param("updateBy")Long updateBy,@Param("updateTime")Date updateTime);


    /**
    * 物理删除业务设计表
    *
    * @param businessDesignId 业务设计表主键
    * @return 结果
    */
    int deleteBusinessDesignByBusinessDesignId(@Param("businessDesignId")Long businessDesignId);

    /**
    * 物理批量删除业务设计表
    *
    * @param businessDesignIds 需要删除的数据主键集合
    * @return 结果
    */
    int deleteBusinessDesignByBusinessDesignIds(@Param("businessDesignIds")List<Long> businessDesignIds);

    /**
    * 批量新增业务设计表
    *
    * @param businessDesigns 业务设计表列表
    * @return 结果
    */
    int batchBusinessDesign(@Param("businessDesigns")List<BusinessDesign> businessDesigns);
}
