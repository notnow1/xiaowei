package net.qixiaowei.message.service.backlog;

import java.util.List;
import net.qixiaowei.message.api.dto.backlog.BacklogDTO;
import net.qixiaowei.message.api.vo.backlog.BacklogCountVO;


/**
* BacklogService接口
* @author hzk
* @since 2022-12-11
*/
public interface IBacklogService{
    /**
    * 查询待办事项表
    *
    * @param backlogId 待办事项表主键
    * @return 待办事项表
    */
    BacklogDTO selectBacklogByBacklogId(Long backlogId);
    /**
     * 查询待办统计数据
     *
     * @return 待办统计数据
     */
    BacklogCountVO count();
    /**
    * 查询待办事项表列表
    *
    * @param backlogDTO 待办事项表
    * @return 待办事项表集合
    */
    List<BacklogDTO> selectBacklogList(BacklogDTO backlogDTO);

    /**
    * 新增待办事项表
    *
    * @param backlogDTO 待办事项表
    * @return 结果
    */
    BacklogDTO insertBacklog(BacklogDTO backlogDTO);

    /**
     * 修改待办事项表
     *
     * @param backlogDTO 待办事项表
     * @return 结果
     */
    void handled(BacklogDTO backlogDTO);

    /**
    * 修改待办事项表
    *
    * @param backlogDTO 待办事项表
    * @return 结果
    */
    int updateBacklog(BacklogDTO backlogDTO);

    /**
    * 批量修改待办事项表
    *
    * @param backlogDtos 待办事项表
    * @return 结果
    */
    int updateBacklogs(List<BacklogDTO> backlogDtos);

    /**
    * 批量新增待办事项表
    *
    * @param backlogDtos 待办事项表
    * @return 结果
    */
    int insertBacklogs(List<BacklogDTO> backlogDtos);

    /**
    * 逻辑批量删除待办事项表
    *
    * @param backlogIds 需要删除的待办事项表集合
    * @return 结果
    */
    int logicDeleteBacklogByBacklogIds(List<Long> backlogIds);

    /**
    * 逻辑删除待办事项表信息
    *
    * @param backlogDTO
    * @return 结果
    */
    int logicDeleteBacklogByBacklogId(BacklogDTO backlogDTO);
    /**
    * 批量删除待办事项表
    *
    * @param BacklogDtos
    * @return 结果
    */
    int deleteBacklogByBacklogIds(List<BacklogDTO> BacklogDtos);

    /**
    * 逻辑删除待办事项表信息
    *
    * @param backlogDTO
    * @return 结果
    */
    int deleteBacklogByBacklogId(BacklogDTO backlogDTO);


    /**
    * 删除待办事项表信息
    *
    * @param backlogId 待办事项表主键
    * @return 结果
    */
    int deleteBacklogByBacklogId(Long backlogId);

}
