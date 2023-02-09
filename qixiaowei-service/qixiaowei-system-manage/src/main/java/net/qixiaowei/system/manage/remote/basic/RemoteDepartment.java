package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.EmployeeDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteDepartmentService;
import net.qixiaowei.system.manage.service.basic.IDepartmentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/department")
public class RemoteDepartment implements RemoteDepartmentService {
    @Autowired
    private IDepartmentService departmentService;

    /**
     * 查询根据code集合查询部门数据
     *
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

    /**
     * 通过id查找部门信息
     *
     * @param departmentId
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/departmentId")
    public R<DepartmentDTO> selectdepartmentId(@RequestParam("departmentId") Long departmentId, String source) {
        return R.ok(departmentService.selectDepartmentByDepartmentId(departmentId));
    }

    /**
     * 通过id集合查找部门信息
     *
     * @param departmentIds
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/departmentIds")
    public R<List<DepartmentDTO>> selectdepartmentIds(@RequestBody List<Long> departmentIds, String source) {
        return R.ok(departmentService.selectDepartmentByDepartmentIds(departmentIds));
    }

    /**
     * 远程查找部门列表
     * @param departmentDTO
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @PostMapping("/selectAll")
    public R<List<DepartmentDTO>> selectDepartment(DepartmentDTO departmentDTO, String source) {
        return R.ok(departmentService.selectDepartmentAll(departmentDTO));
    }

    /**
     * 查看所有一级部门
     *
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/getParentAll")
    public R<List<DepartmentDTO>> getParentAll(String source) {
        return R.ok(departmentService.getParentAll());
    }

    /**
     * 远程查询一级部门及子级部门
     *
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/selectSublevelDepartment")
    public R<List<DepartmentDTO>> selectSublevelDepartment(@RequestParam("departmentId") Long departmentId, String source) {
        return R.ok(departmentService.selectParentDepartment(departmentId));
    }

    /**
     * 远程查询所有部门
     *
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/getAll")
    public R<List<DepartmentDTO>> getAll(String source) {
        return R.ok(departmentService.getAll());
    }

    /**
     * 根据等级查询部门
     *
     * @param inner
     * @return
     */
    @Override
    @GetMapping("/selectByLevel")
    public R<List<DepartmentDTO>> selectDepartmentByLevel(@RequestParam("level") Integer level, String inner) {
        return R.ok(departmentService.selectDepartmentByLevel(level));
    }

    /**
     * 组织远程高级搜索
     *
     * @param params 查询条件
     * @return R
     */
    @Override
    @PostMapping("/selectByLevel")
    public R<List<DepartmentDTO>> depAdvancedSearch(@RequestBody Map<String, Object> params, String inner) {
        DepartmentDTO departmentDTO = new DepartmentDTO();
        if (StringUtils.isNotNull(params)) {
            departmentDTO.setParams(params);
        }
        return R.ok(departmentService.selectDepartmentList(departmentDTO));
    }
}
