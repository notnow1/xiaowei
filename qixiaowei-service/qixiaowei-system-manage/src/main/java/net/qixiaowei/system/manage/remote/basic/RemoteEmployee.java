package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteEmployeeService;
import net.qixiaowei.system.manage.service.basic.IEmployeeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/employee")
public class RemoteEmployee implements RemoteEmployeeService {
    @Autowired
    private IEmployeeService employeeService;

    /**
     * 查询人员数据
     * @param employeeCodes
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/codeList")
    public R<List<EmployeeDTO>> selectCodeList(@RequestBody List<String> employeeCodes, String source) {
        return R.ok(employeeService.selectCodeList(employeeCodes));
    }
}
