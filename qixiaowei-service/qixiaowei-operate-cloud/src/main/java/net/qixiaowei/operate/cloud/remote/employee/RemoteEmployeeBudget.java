package net.qixiaowei.operate.cloud.remote.employee;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.employee.EmployeeBudgetDTO;
import net.qixiaowei.operate.cloud.api.remote.employee.RemoteEmployeeBudgetService;
import net.qixiaowei.operate.cloud.service.employee.IEmployeeBudgetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author Graves
 * @since 2022-11-18
 */
@RestController
@RequestMapping("employeeBudget")
public class RemoteEmployeeBudget implements RemoteEmployeeBudgetService {


    @Autowired
    private IEmployeeBudgetService employeeBudgetService;

    /**
     * 根据职级体系ID集合查询预算表
     *
     * @param officialRankSystemIds 职级体系ID集合
     * @return R
     */
    @Override
    @InnerAuth
    @PostMapping("/selectBySystemIds")
    public R<List<EmployeeBudgetDTO>> selectBySystemIds(@RequestBody List<Long> officialRankSystemIds, String source) {
        return R.ok(employeeBudgetService.selectBySystemIds(officialRankSystemIds));
    }
}
