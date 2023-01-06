package net.qixiaowei.system.manage.controller.basic;

import java.util.List;

import net.qixiaowei.integration.common.enums.message.BusinessType;
import net.qixiaowei.integration.log.annotation.Log;
import net.qixiaowei.integration.log.enums.OperationType;
import net.qixiaowei.integration.security.annotation.Logical;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import net.qixiaowei.integration.common.web.page.TableDataInfo;
import net.qixiaowei.integration.common.web.domain.AjaxResult;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.service.basic.IPostService;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;


/**
 * @author TANGMICHI
 * @since 2022-09-30
 */
@RestController
@RequestMapping("post")
public class PostController extends BaseController {


    @Autowired
    private IPostService postService;


    /**
     * 分页查询岗位表列表
     */
    @RequiresPermissions("system:manage:post:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PostDTO postDTO) {
        startPage();
        List<PostDTO> list = postService.selectPostList(postDTO);
        return getDataTable(list);
    }

    /**
     * 新增岗位表
     */
    @Log(title = "新增岗位", businessType = BusinessType.POST, businessId = "postId", operationType = OperationType.INSERT)
    @RequiresPermissions("system:manage:post:add")
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody @Validated(PostDTO.AddPostDTO.class) PostDTO postDTO) {
        return AjaxResult.success(postService.insertPost(postDTO));
    }

    /**
     * 修改岗位表
     */
    @Log(title = "编辑岗位", businessType = BusinessType.POST, businessId = "postId", operationType = OperationType.UPDATE)
    @RequiresPermissions("system:manage:post:edit")
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody @Validated(PostDTO.UpdatePostDTO.class) PostDTO postDTO) {
        return toAjax(postService.updatePost(postDTO));
    }

    /**
     * 查询岗位详情
     */
    @RequiresPermissions(value = {"system:manage:post:info", "system:manage:post:edit"}, logical = Logical.OR)
    @GetMapping("/info/{postId}")
    public AjaxResult list(@PathVariable Long postId) {
        PostDTO postDTO = postService.selectPostByPostId(postId);
        return AjaxResult.success(postDTO);
    }

    /**
     * 逻辑删除岗位表
     */
    @RequiresPermissions("system:manage:post:remove")
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody @Validated(PostDTO.DeletePostDTO.class) PostDTO postDTO) {
        return toAjax(postService.logicDeletePostByPostId(postDTO));
    }

    /**
     * 逻辑批量删除岗位表
     */
    @RequiresPermissions("system:manage:post:remove")
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<Long> postIds) {
        return toAjax(postService.logicDeletePostByPostIds(postIds));
    }

    /**
     * 查询岗位表列表
     */
    @GetMapping("/list")
    public AjaxResult list(PostDTO postDTO) {
        List<PostDTO> list = postService.selectPostList(postDTO);
        return AjaxResult.success(list);
    }

    /**
     * 根据部门查询岗位表列表
     */
    @GetMapping("/postList/{departmentId}")
    public AjaxResult selectBydepartmentId(@PathVariable Long departmentId) {
        List<PostDTO> list = postService.selectBydepartmentId(departmentId);
        return AjaxResult.success(list);
    }


}
