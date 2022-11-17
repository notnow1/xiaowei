package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/department")
public class RemoteDepartment implements RemoteDepartmentService {
    @Autowired
    private IDepartmentService departmentService;

    /**
     * 查询根据code集合查询部门数据
     * @param departmentCodes
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/codeList")
    public R<List<DepartmentDTO>> selectCodeList(@RequestBody List<String> departmentCodes, String source) {
        return R.ok(departmentService.selectCodeList(departmentCodes));
    }
}
