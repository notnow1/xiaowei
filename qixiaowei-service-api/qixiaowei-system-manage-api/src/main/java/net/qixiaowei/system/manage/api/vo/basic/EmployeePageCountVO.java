package net.qixiaowei.system.manage.api.vo.basic;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 人员页页统计VO
 *
 * @author hzk
 * @since 2023-05-24
 */
@Data
@Accessors(chain = true)
public class EmployeePageCountVO {

    /**
     * 总人数
     */
    private Integer total;
    /**
     * 待分配人数
     */
    private Integer  toBeAllocatedNum;


}

