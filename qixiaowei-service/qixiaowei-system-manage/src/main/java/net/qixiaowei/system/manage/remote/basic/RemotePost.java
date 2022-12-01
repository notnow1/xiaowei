package net.qixiaowei.system.manage.remote.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.system.manage.api.dto.basic.OfficialRankSystemDTO;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemoteOfficialRankSystemService;
import net.qixiaowei.system.manage.api.remote.basic.RemotePostService;
import net.qixiaowei.system.manage.service.basic.IOfficialRankSystemService;
import net.qixiaowei.system.manage.service.basic.IPostService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/post")
public class RemotePost implements RemotePostService {

    @Autowired
    IPostService postService;

    @Override
    @InnerAuth
    @GetMapping("/selectAll")
    public R<List<PostDTO>> selectPostListByOfficialRank(@RequestParam Long officialRankSystemId, String source) {
        return R.ok(postService.selectPostListByOfficialRank(officialRankSystemId));
    }

}
