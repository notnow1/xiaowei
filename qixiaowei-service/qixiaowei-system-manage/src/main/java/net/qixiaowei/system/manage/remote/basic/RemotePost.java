package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.DepartmentDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemotePostService;
import net.qixiaowei.system.manage.service.basic.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/post")
public class RemotePost implements RemotePostService {

    @Autowired
    IPostService postService;

    @Override
    @InnerAuth
    @GetMapping("/selectPostListByOfficialRank")
    public R<List<PostDTO>> selectPostListByOfficialRank(@RequestParam("officialRankSystemId") Long officialRankSystemId, String source) {
        return R.ok(postService.selectPostListByOfficialRank(officialRankSystemId));
    }

    /**
     * 查找部门根据部门ID
     *
     * @param postId 部门ID
     * @param source
     * @return
     */
    @Override
    @InnerAuth
    @GetMapping("/selectPostListByPostId")
    public R<PostDTO> selectPostByPostId(@RequestParam("postId") Long postId, String source) {
        return R.ok(postService.selectPostByPostId(postId));
    }

    /**
     * 岗位远程高级搜索
     *
     * @param params 查询条件
     * @return R
     */
    @Override
    @PostMapping("/postAdvancedSearch")
    public R<List<PostDTO>> postAdvancedSearch(@RequestBody Map<String, Object> params, String source) {
        PostDTO postDTO = new PostDTO();
        if (StringUtils.isNotNull(params)) {
            postDTO.setParams(params);
        }
        return R.ok(postService.selectPostList(postDTO));
    }

}
