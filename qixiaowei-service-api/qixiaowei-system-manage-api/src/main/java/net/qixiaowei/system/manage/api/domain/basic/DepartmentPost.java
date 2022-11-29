package net.qixiaowei.system.manage.api.domain.basic;


import lombok.Data;
import lombok.experimental.Accessors;
import net.qixiaowei.integration.common.domain.tenant.TenantEntity;

/**
* 部门岗位关联表
* @author TANGMICHI
* @since 2022-09-27
*/
@Data
@Accessors(chain = true)
public class DepartmentPost extends TenantEntity {

    private static final long serialVersionUID = 1L;

     /**
     * ID
     */
     private  Long  departmentPostId;
     /**
     * 部门ID
     */
     private  Long  departmentId;
     /**
     * 岗位ID
     */
     private  Long  postId;
     /**
     * 部门排序
     */
     private  Integer  departmentSort;
     /**
     * 岗位排序
     */
     private  Integer  postSort;


}

