package net.qixiaowei.operate.cloud.mapper.performance;

import java.util.List;

import net.qixiaowei.operate.cloud.api.domain.performance.PerformAppraisalObjectSnap;
import net.qixiaowei.operate.cloud.api.dto.performance.PerformAppraisalObjectSnapDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * PerformAppraisalObjectSnapMapper接口
 *
 * @author Graves
 * @since 2022-12-05
 */
public interface PerformAppraisalObjectSnapMapper {
    /**
     * 查询绩效考核对象快照表
     *
     * @param appraisalObjectSnapId 绩效考核对象快照表主键
     * @return 绩效考核对象快照表
     */
    PerformAppraisalObjectSnapDTO selectPerformAppraisalObjectSnapByAppraisalObjectSnapId(@Param("appraisalObjectSnapId") Long appraisalObjectSnapId);


    /**
     * 批量查询绩效考核对象快照表
     *
     * @param appraisalObjectSnapIds 绩效考核对象快照表主键集合
     * @return 绩效考核对象快照表
     */
    List<PerformAppraisalObjectSnapDTO> selectPerformAppraisalObjectSnapByAppraisalObjectSnapIds(@Param("appraisalObjectSnapIds") List<Long> appraisalObjectSnapIds);

    /**
     * 查询绩效考核对象快照表列表
     *
     * @param performAppraisalObjectSnap 绩效考核对象快照表
     * @return 绩效考核对象快照表集合
     */
    List<PerformAppraisalObjectSnapDTO> selectPerformAppraisalObjectSnapList(@Param("performAppraisalObjectSnap") PerformAppraisalObjectSnap performAppraisalObjectSnap);

    /**
     * 新增绩效考核对象快照表
     *
     * @param performAppraisalObjectSnap 绩效考核对象快照表
     * @return 结果
     */
    int insertPerformAppraisalObjectSnap(@Param("performAppraisalObjectSnap") PerformAppraisalObjectSnap performAppraisalObjectSnap);

    /**
     * 修改绩效考核对象快照表
     *
     * @param performAppraisalObjectSnap 绩效考核对象快照表
     * @return 结果
     */
    int updatePerformAppraisalObjectSnap(@Param("performAppraisalObjectSnap") PerformAppraisalObjectSnap performAppraisalObjectSnap);

    /**
     * 批量修改绩效考核对象快照表
     *
     * @param performAppraisalObjectSnapList 绩效考核对象快照表
     * @return 结果
     */
    int updatePerformAppraisalObjectSnaps(@Param("performAppraisalObjectSnapList") List<PerformAppraisalObjectSnap> performAppraisalObjectSnapList);

    /**
     * 逻辑删除绩效考核对象快照表
     *
     * @param performAppraisalObjectSnap
     * @return 结果
     */
    int logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapId(@Param("performAppraisalObjectSnap") PerformAppraisalObjectSnap performAppraisalObjectSnap);

    /**
     * 逻辑批量删除绩效考核对象快照表
     *
     * @param appraisalObjectSnapIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(@Param("appraisalObjectSnapIds") List<Long> appraisalObjectSnapIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除绩效考核对象快照表
     *
     * @param appraisalObjectSnapId 绩效考核对象快照表主键
     * @return 结果
     */
    int deletePerformAppraisalObjectSnapByAppraisalObjectSnapId(@Param("appraisalObjectSnapId") Long appraisalObjectSnapId);

    /**
     * 物理批量删除绩效考核对象快照表
     *
     * @param appraisalObjectSnapIds 需要删除的数据主键集合
     * @return 结果
     */
    int deletePerformAppraisalObjectSnapByAppraisalObjectSnapIds(@Param("appraisalObjectSnapIds") List<Long> appraisalObjectSnapIds);

    /**
     * 批量新增绩效考核对象快照表
     *
     * @param PerformAppraisalObjectSnaps 绩效考核对象快照表列表
     * @return 结果
     */
    int batchPerformAppraisalObjectSnap(@Param("performAppraisalObjectSnaps") List<PerformAppraisalObjectSnap> PerformAppraisalObjectSnaps);

    /**
     * 通过对象ID集合查询快招标
     *
     * @param performanceObjectIds 通过对象ID集合查询快照
     * @return
     */
    List<PerformAppraisalObjectSnapDTO> selectPerformAppraisalObjectSnapByAppraisalObjectsIds(List<Long> performanceObjectIds);
}
