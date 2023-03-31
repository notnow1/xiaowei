package net.qixiaowei.integration.datascope.aspect;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import cn.hutool.core.util.StrUtil;
import net.qixiaowei.integration.common.domain.dto.BaseDTO;
import net.qixiaowei.integration.common.enums.system.RoleDataScope;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.datascope.annotation.DataScope;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.system.manage.api.dto.system.RoleDTO;
import net.qixiaowei.system.manage.api.dto.user.UserDTO;
import net.qixiaowei.system.manage.api.vo.LoginUserVO;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import net.qixiaowei.integration.common.web.domain.BaseEntity;

/**
 * 数据过滤处理
 */
@Aspect
@Component
public class DataScopeAspect {
    /**
     * 数据权限过滤关键字
     */
    public static final String DATA_SCOPE = "dataScope";

    @Before("@annotation(controllerDataScope)")
    public void doBefore(JoinPoint point, DataScope controllerDataScope) throws Throwable {
        clearDataScope(point);
        handleDataScope(point, controllerDataScope);
    }

    protected void handleDataScope(final JoinPoint joinPoint, DataScope controllerDataScope) {
        // 获取当前的用户
        LoginUserVO loginUserVO = SecurityUtils.getLoginUser();
        if (StringUtils.isNotNull(loginUserVO)) {
            UserDTO currentUser = loginUserVO.getUserDTO();
            // 如果是超级管理员，则不过滤数据
            if (StringUtils.isNotNull(currentUser) && !SecurityUtils.isAdmin()) {
                dataScopeFilter(joinPoint, currentUser, controllerDataScope.deptAlias(),
                        controllerDataScope.userAlias(), controllerDataScope.businessAlias());
            }
        }
    }

    /**
     * 数据范围过滤
     *
     * @param joinPoint     切点
     * @param user          用户
     * @param deptAlias     部门别名
     * @param userAlias     用户别名
     * @param businessAlias 业务别名
     */
    public static void dataScopeFilter(JoinPoint joinPoint, UserDTO user, String deptAlias, String userAlias, String businessAlias) {
        if (StringUtils.isEmpty(deptAlias) && StringUtils.isEmpty(userAlias) && StringUtils.isEmpty(businessAlias)) {
            return;
        }
        StringBuilder sqlString = new StringBuilder();
        Set<Integer> dataScope = new HashSet<>();
        Long employeeId = user.getEmployeeId();
        Set<Long> userIds = user.getUserIds();
        Long departmentId = user.getDepartmentId();
        boolean isBusiness = StringUtils.isNotEmpty(businessAlias);
        List<RoleDTO> roles = user.getRoles();
        roles.forEach(roleDTO -> dataScope.add(roleDTO.getDataScope()));
        if (isBusiness) {
            if (!dataScope.contains(RoleDataScope.ALL.getCode())) {
                getUserIdsSql(businessAlias, userAlias, sqlString, userIds);
            }
        } else {
            if (dataScope.contains(RoleDataScope.ALL.getCode())) {
                return;
            }
            if (StringUtils.isNotEmpty(userAlias)) {
                getUserIdsSql(businessAlias, userAlias, sqlString, userIds);
            } else {
                if (dataScope.contains(RoleDataScope.ALL_SUB_DEPARTMENT.getCode())) {
                    sqlString.append(StringUtils.format(
                            " {}.department_id IN ( SELECT department_id FROM department WHERE department_id = {} OR find_in_set( {} , ancestors ) ) ", deptAlias,
                            deptAlias));
                } else if (dataScope.contains(RoleDataScope.DEPARTMENT.getCode()) || dataScope.contains(RoleDataScope.SELF.getCode())) {
                    sqlString.append(StringUtils.format(
                            " {}.department_id = {} ", deptAlias,
                            departmentId));
                } else if (dataScope.contains(RoleDataScope.SELF_AND_SUBORDINATE.getCode())) {
                    sqlString.append(StringUtils.format(
                            " {}.department_id = {} OR {}.department_leader_id = {}", deptAlias,
                            departmentId, deptAlias, employeeId));
                }
            }
        }
        if (StringUtils.isNotBlank(sqlString.toString())) {
            Object params = joinPoint.getArgs()[0];
            if (StringUtils.isNotNull(params) && params instanceof BaseDTO) {
                BaseDTO baseDTO = (BaseDTO) params;
                baseDTO.getParams().put(DATA_SCOPE, " AND (" + sqlString + ")");
            }
        }
    }

    /**
     * 拼接权限sql前先清空params.dataScope参数防止注入
     */
    private void clearDataScope(final JoinPoint joinPoint) {
        Object params = joinPoint.getArgs()[0];
        if (StringUtils.isNotNull(params) && params instanceof BaseEntity) {
            BaseEntity baseEntity = (BaseEntity) params;
            baseEntity.getParams().put(DATA_SCOPE, "");
        }
    }

    private static void getUserIdsSql(String businessAlias, String userAlias, StringBuilder sqlString, Set<Long> userIds) {
        boolean isBusiness = StringUtils.isNotEmpty(businessAlias);
        String alias = isBusiness ? businessAlias : userAlias;
        String template = isBusiness ? " {}.create_by IN ({} ) " : " {}.user_id IN ( {} ) ";
        StringBuilder userIdsSqlString = new StringBuilder();
        if (StringUtils.isNotEmpty(userIds)) {
            for (Long id : userIds) {
                userIdsSqlString.append(id).append(StrUtil.COMMA);
            }
        }
        String userIdsSql = userIdsSqlString.substring(0, userIdsSqlString.lastIndexOf(StrUtil.COMMA));
        sqlString.append(StringUtils.format(
                template, alias,
                userIdsSql));
    }
}
