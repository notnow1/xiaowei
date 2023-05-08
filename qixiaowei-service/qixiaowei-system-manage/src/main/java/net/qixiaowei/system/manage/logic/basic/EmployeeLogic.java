package net.qixiaowei.system.manage.logic.basic;


import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.enums.PrefixCodeRule;
import net.qixiaowei.integration.common.exception.ServiceException;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.utils.SecurityUtils;
import net.qixiaowei.sales.cloud.api.dto.sync.SyncUserDTO;
import net.qixiaowei.sales.cloud.api.remote.sync.RemoteSyncAdminService;
import net.qixiaowei.system.manage.api.domain.basic.Employee;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.mapper.basic.EmployeeMapper;
import net.qixiaowei.system.manage.mapper.basic.PostMapper;
import net.qixiaowei.system.manage.mapper.user.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * @description 人员相关逻辑处理
 * @Author hzk
 * @Date 2023-05-05 15:38
 **/
@Component
@Slf4j
public class EmployeeLogic {

    @Autowired
    private UserMapper userMapper;
    @Autowired
    private EmployeeMapper employeeMapper;
    @Autowired
    private RemoteSyncAdminService remoteSyncAdminService;
    @Autowired
    private PostMapper postMapper;

    public String generateEmployeeCode() {
        String employeeCode;
        int number = 1;
        String prefixCodeRule = PrefixCodeRule.EMPLOYEE.getCode();
        List<String> employeeCodes = employeeMapper.getEmployeeCodes(prefixCodeRule);
        if (StringUtils.isNotEmpty(employeeCodes)) {
            for (String code : employeeCodes) {
                if (StringUtils.isEmpty(code) || code.length() != 8) {
                    continue;
                }
                code = code.replaceFirst(prefixCodeRule, "");
                try {
                    int codeOfNumber = Integer.parseInt(code);
                    if (number != codeOfNumber) {
                        break;
                    }
                    number++;
                } catch (Exception ignored) {
                }
            }
        }
        if (number > 1000000) {
            throw new ServiceException("流水号溢出，请联系管理员");
        }
        employeeCode = "000000" + number;
        employeeCode = prefixCodeRule + employeeCode.substring(employeeCode.length() - 6);
        return employeeCode;
    }

    /**
     * @description: 同步销售云用户
     * @Author: hzk
     * @date: 2023/4/12 18:13
     * @param: [userId, userAccount, password, employee]
     * @return: void
     **/
    public void syncSalesAddUser(Long userId, String userAccount, String password, Employee employee) {
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncUserDTO syncUserDTO = new SyncUserDTO();
            String userName = employee.getEmployeeName() + "（" + employee.getEmployeeCode() + "）";
            syncUserDTO.setUserId(userId);
            syncUserDTO.setRealname(userName);
            syncUserDTO.setUsername(Optional.ofNullable(userAccount).orElse(employee.getEmployeeMobile()));
            syncUserDTO.setSex(employee.getEmployeeGender());
            syncUserDTO.setMobile(employee.getEmployeeMobile());
            syncUserDTO.setPassword(password);
            syncUserDTO.setEmail(employee.getEmployeeEmail());
            syncUserDTO.setDeptId(employee.getEmployeeDepartmentId());
            syncUserDTO.setStatus(1);
            syncUserDTO.setNum(employee.getEmployeeCode());
            Long employeePostId = employee.getEmployeePostId();
            //处理岗位
            if (StringUtils.isNotNull(employeePostId)) {
                PostDTO postDTO = postMapper.selectPostByPostId(employeePostId);
                if (StringUtils.isNotNull(postDTO)) {
                    syncUserDTO.setPost(postDTO.getPostName());
                }
            }
            R<?> r = remoteSyncAdminService.syncUserAdd(syncUserDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云用户新增失败:{}", r.getMsg());
                throw new ServiceException("人员新增失败");
            }
        }
    }

    /**
     * @description: 销售云同步编辑用户
     * @Author: hzk
     * @date: 2023/4/12 18:14
     * @param: [userId, employee]
     * @return: void
     **/
    public void syncSaleEditUser(Long userId, Employee employee) {
        String salesToken = SecurityUtils.getSalesToken();
        if (StringUtils.isNotEmpty(salesToken)) {
            SyncUserDTO syncUserDTO = new SyncUserDTO();
            syncUserDTO.setUserId(userId);
            syncUserDTO.setSex(employee.getEmployeeGender());
            syncUserDTO.setDeptId(employee.getEmployeeDepartmentId());
            Long employeePostId = employee.getEmployeePostId();
            //处理岗位
            if (StringUtils.isNotNull(employeePostId)) {
                PostDTO postDTO = postMapper.selectPostByPostId(employeePostId);
                if (StringUtils.isNotNull(postDTO)) {
                    syncUserDTO.setPost(postDTO.getPostName());
                }
            }
            R<?> r = remoteSyncAdminService.syncUserEdit(syncUserDTO, salesToken);
            if (0 != r.getCode()) {
                log.error("同步销售云用户编辑失败:{}", r.getMsg());
                throw new ServiceException("人员编辑失败");
            }
        }
    }

}
