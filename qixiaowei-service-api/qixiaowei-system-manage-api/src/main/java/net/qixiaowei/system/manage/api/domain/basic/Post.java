package net.qixiaowei.system.manage.api.domain.basic;

import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;


/**
 * 岗位表
 *
 * @author TANGMICHI
 * @since 2022-09-30
 */
@Data
@Accessors(chain = true)
public class Post extends TenantEntity {

    private static final long serialVersionUID = 1L;

    /**
     * ID
     */
    private Long postId;
    /**
     * 岗位编码
     */
    private String postCode;
    /**
     * 岗位名称
     */
    private String postName;
    /**
     * 职级体系ID
     */
    private Long officialRankSystemId;
    /**
     * 岗位职级下限
     */
    private Integer postRankLower;
    /**
     * 岗位职级
     */
    private Integer postRank;
    /**
     * 岗位职级上限
     */
    private Integer postRankUpper;
    /**
     * 岗位说明书URL路径
     */
    private String postDescription;
    /**
     * 状态:0失效;1生效
     */
    private Integer status;
    /**
     * 职级体系名称
     */
    private String officialRankSystemName;
    /**
     * 岗位职级下限名称
     */
    private String postRankLowerName;
    /**
     * 岗位职级上限名称
     */
    private String postRankUpperName;
    /**
     * 岗位职级名称
     */
    private String postRankName;
    /**
     * 部门ID
     */
    private Long departmentId;
    /**
     * 部门编码
     */
    private  String departmentCode;
    /**
     * 部门名称
     */
    private  String departmentName;
}

