package net.qixiaowei.system.manage.controller.basic;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.integration.log.annotation.Log;
import org.springframework.stereotype.Controller;
import net.qixiaowei.integration.log.enums.BusinessType;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentPostDTO;
import net.qixiaowei.system.manage.service.basic.IDepartmentPostService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-09-27
*/
@RestController
@RequestMapping("departmentPost")
public class DepartmentPostController extends BaseController
{


    @Autowired
    private IDepartmentPostService departmentPostService;

    /**
    * 分页查询部门岗位关联表列表
    */
    //@RequiresPermissions("system:manage:departmentPost:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(DepartmentPostDTO departmentPostDTO){
    startPage();
    List<DepartmentPostDTO> list = departmentPostService.selectDepartmentPostList(departmentPostDTO);
    return getDataTable(list);
    }

    /**
    * 查询部门岗位关联表列表
    */
    //@RequiresPermissions("system:manage:departmentPost:list")
    @GetMapping("/list")
    public AjaxResult list(DepartmentPostDTO departmentPostDTO){
    List<DepartmentPostDTO> list = departmentPostService.selectDepartmentPostList(departmentPostDTO);
    return AjaxResult.success(list);
    }

    /**
     * 查询部门岗位关联表详情
     */
    //@RequiresPermissions("system:manage:departmentPost:list")
    @GetMapping("/info/{departmentPostId}")
    public AjaxResult info(@PathVariable  Long departmentPostId){
        DepartmentPostDTO departmentPostDTO = departmentPostService.selectDepartmentPostByDepartmentPostId(departmentPostId);
        return AjaxResult.success(departmentPostDTO);
    }

    /**
    * 新增部门岗位关联表
    */
    //@RequiresPermissions("system:manage:departmentPost:add")
    @Log(title = "新增部门岗位关联表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody DepartmentPostDTO departmentPostDTO) {
    return toAjax(departmentPostService.insertDepartmentPost(departmentPostDTO));
    }


    /**
    * 修改部门岗位关联表
    */
    //@RequiresPermissions("system:manage:departmentPost:edit")
    @Log(title = "修改部门岗位关联表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody DepartmentPostDTO departmentPostDTO)
    {
    return toAjax(departmentPostService.updateDepartmentPost(departmentPostDTO));
    }

    /**
    * 逻辑删除部门岗位关联表
    */
    //@RequiresPermissions("system:manage:departmentPost:remove")
    @Log(title = "删除部门岗位关联表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody DepartmentPostDTO departmentPostDTO)
    {
    return toAjax(departmentPostService.logicDeleteDepartmentPostByDepartmentPostId(departmentPostDTO));
    }
    /**
    * 批量修改部门岗位关联表
    */
    //@RequiresPermissions("system:manage:departmentPost:edits")
    @Log(title = "批量修改部门岗位关联表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<DepartmentPostDTO> departmentPostDtos)
    {
    return toAjax(departmentPostService.updateDepartmentPosts(departmentPostDtos));
    }

    /**
    * 批量新增部门岗位关联表
    */
    //@RequiresPermissions("system:manage:departmentPost:insertDepartmentPosts")
    @Log(title = "批量新增部门岗位关联表", businessType = BusinessType.INSERT)
    @PostMapping("/insertDepartmentPosts")
    public AjaxResult insertDepartmentPosts(@RequestBody List<DepartmentPostDTO> departmentPostDtos)
    {
    return toAjax(departmentPostService.insertDepartmentPosts(departmentPostDtos));
    }

    /**
    * 逻辑批量删除部门岗位关联表
    */
    //@RequiresPermissions("system:manage:departmentPost:removes")
    @Log(title = "批量删除部门岗位关联表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<DepartmentPostDTO>  DepartmentPostDtos)
    {
    return toAjax(departmentPostService.logicDeleteDepartmentPostByDepartmentPostIds(DepartmentPostDtos));
    }
}
