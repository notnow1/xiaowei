package net.qixiaowei.message.api.vo.backlog;

import lombok.Data;
import lombok.experimental.Accessors;


/**
 * 待办事项统计VO
 *
 * @author hzk
 * @since 2022-12-11
 */
@Data
@Accessors(chain = true)
public class BacklogCountVO {

    /**
     * 是否有待处理事项
     */
    private Boolean havingBacklog;
    /**
     * 待办事项总数-待办事项+待读消息
     */
    private Integer totalBacklog;
    /**
     * 待办事项统计
     */
    private Integer countBacklog;
    /**
     * 待读消息统计
     */
    private Integer countUnreadMessage;


}

