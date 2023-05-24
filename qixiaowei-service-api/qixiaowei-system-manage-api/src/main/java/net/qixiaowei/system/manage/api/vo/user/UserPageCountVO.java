package net.qixiaowei.system.manage.api.vo.user;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 用户页统计VO
 *
 * @author hzk
 * @since 2023-05-24
 */
@Data
@Accessors(chain = true)
public class UserPageCountVO {

    /**
     * 总人数
     */
    private Integer total;
    /**
     * 未激活人数
     */
    private Integer unactivated;


}

