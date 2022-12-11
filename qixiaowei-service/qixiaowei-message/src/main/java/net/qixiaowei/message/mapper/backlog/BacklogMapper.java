package net.qixiaowei.message.mapper.backlog;

import java.util.List;

import net.qixiaowei.message.api.domain.backlog.Backlog;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import org.apache.ibatis.annotations.Param;

import java.util.Date;


/**
 * BacklogMapper接口
 *
 * @author hzk
 * @since 2022-12-11
 */
public interface BacklogMapper {
    /**
     * 查询待办事项表
     *
     * @param backlogId 待办事项表主键
     * @return 待办事项表
     */
    BacklogDTO selectBacklogByBacklogId(@Param("backlogId") Long backlogId);

    /**
     * 统计待办事项
     * @param userId          用户ID
     * @return 待办事项总数
     */
    Integer countBacklog(@Param("userId") Long userId);

    /**
     * 查询用户的待办事项
     *
     * @param businessType    业务类型
     * @param businessSubtype 业务子类型
     * @param businessId      业务ID
     * @param userId          用户ID
     * @return 待办事项表
     */
    BacklogDTO selectUserBacklog(@Param("businessType") Integer businessType, @Param("businessSubtype") Integer businessSubtype, @Param("businessId") Long businessId, @Param("userId") Long userId);


    /**
     * 批量查询待办事项表
     *
     * @param backlogIds 待办事项表主键集合
     * @return 待办事项表
     */
    List<BacklogDTO> selectBacklogByBacklogIds(@Param("backlogIds") List<Long> backlogIds);

    /**
     * 查询待办事项表列表
     *
     * @param backlog 待办事项表
     * @return 待办事项表集合
     */
    List<BacklogDTO> selectBacklogList(@Param("backlog") Backlog backlog);

    /**
     * 新增待办事项表
     *
     * @param backlog 待办事项表
     * @return 结果
     */
    int insertBacklog(@Param("backlog") Backlog backlog);

    /**
     * 修改待办事项表
     *
     * @param backlog 待办事项表
     * @return 结果
     */
    int updateBacklog(@Param("backlog") Backlog backlog);

    /**
     * 批量修改待办事项表
     *
     * @param backlogList 待办事项表
     * @return 结果
     */
    int updateBacklogs(@Param("backlogList") List<Backlog> backlogList);

    /**
     * 逻辑删除待办事项表
     *
     * @param backlog
     * @return 结果
     */
    int logicDeleteBacklogByBacklogId(@Param("backlog") Backlog backlog);

    /**
     * 逻辑批量删除待办事项表
     *
     * @param backlogIds 需要删除的数据主键集合
     * @return 结果
     */
    int logicDeleteBacklogByBacklogIds(@Param("backlogIds") List<Long> backlogIds, @Param("updateBy") Long updateBy, @Param("updateTime") Date updateTime);


    /**
     * 物理删除待办事项表
     *
     * @param backlogId 待办事项表主键
     * @return 结果
     */
    int deleteBacklogByBacklogId(@Param("backlogId") Long backlogId);

    /**
     * 物理批量删除待办事项表
     *
     * @param backlogIds 需要删除的数据主键集合
     * @return 结果
     */
    int deleteBacklogByBacklogIds(@Param("backlogIds") List<Long> backlogIds);

    /**
     * 批量新增待办事项表
     *
     * @param Backlogs 待办事项表列表
     * @return 结果
     */
    int batchBacklog(@Param("backlogs") List<Backlog> Backlogs);
}
