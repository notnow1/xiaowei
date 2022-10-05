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
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.service.basic.IPostService;
import net.qixiaowei.integration.security.annotation.RequiresPermissions;
import net.qixiaowei.integration.common.web.controller.BaseController;
import org.springframework.web.bind.annotation.*;




/**
*
* @author TANGMICHI
* @since 2022-09-30
*/
@RestController
@RequestMapping("post")
public class PostController extends BaseController
{


    @Autowired
    private IPostService postService;

    /**
    * 分页查询岗位表列表
    */
    @RequiresPermissions("system:manage:post:pageList")
    @GetMapping("/pageList")
    public TableDataInfo pageList(PostDTO postDTO){
    startPage();
    List<PostDTO> list = postService.selectPostList(postDTO);
    return getDataTable(list);
    }

    /**
    * 查询岗位表列表
    */
    @RequiresPermissions("system:manage:post:list")
    @GetMapping("/list")
    public AjaxResult list(PostDTO postDTO){
    List<PostDTO> list = postService.selectPostList(postDTO);
    return AjaxResult.success(list);
    }


    /**
    * 新增岗位表
    */
    @RequiresPermissions("system:manage:post:add")
    @Log(title = "新增岗位表", businessType = BusinessType.INSERT)
    @PostMapping("/add")
    public AjaxResult addSave(@RequestBody PostDTO postDTO) {
    return toAjax(postService.insertPost(postDTO));
    }


    /**
    * 修改岗位表
    */
    @RequiresPermissions("system:manage:post:edit")
    @Log(title = "修改岗位表", businessType = BusinessType.UPDATE)
    @PostMapping("/edit")
    public AjaxResult editSave(@RequestBody PostDTO postDTO)
    {
    return toAjax(postService.updatePost(postDTO));
    }

    /**
    * 逻辑删除岗位表
    */
    @RequiresPermissions("system:manage:post:remove")
    @Log(title = "删除岗位表", businessType = BusinessType.DELETE)
    @PostMapping("/remove")
    public AjaxResult remove(@RequestBody PostDTO postDTO)
    {
    return toAjax(postService.logicDeletePostByPostId(postDTO));
    }
    /**
    * 批量修改岗位表
    */
    @RequiresPermissions("system:manage:post:edits")
    @Log(title = "批量修改岗位表", businessType = BusinessType.UPDATE)
    @PostMapping("/edits")
    public AjaxResult editSaves(@RequestBody List<PostDTO> postDtos)
    {
    return toAjax(postService.updatePosts(postDtos));
    }

    /**
    * 批量新增岗位表
    */
    @RequiresPermissions("system:manage:post:insertPosts")
    @Log(title = "批量新增岗位表", businessType = BusinessType.INSERT)
    @PostMapping("/insertPosts")
    public AjaxResult insertPosts(@RequestBody List<PostDTO> postDtos)
    {
    return toAjax(postService.insertPosts(postDtos));
    }

    /**
    * 逻辑批量删除岗位表
    */
    @RequiresPermissions("system:manage:post:removes")
    @Log(title = "批量删除岗位表", businessType = BusinessType.DELETE)
    @PostMapping("/removes")
    public AjaxResult removes(@RequestBody List<PostDTO>  PostDtos)
    {
    return toAjax(postService.logicDeletePostByPostIds(PostDtos));
    }
}
