package net.qixiaowei.system.manage.api.factory.basic;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.system.manage.api.dto.basic.PostDTO;
import net.qixiaowei.system.manage.api.remote.basic.RemotePostService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Map;

public class RemotePostFallbackFactory implements FallbackFactory<RemotePostService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteIndustryFallbackFactory.class);

    @Override
    public RemotePostService create(Throwable throwable) {
        log.error("岗位服务调用失败:{}", throwable.getMessage());
        return new RemotePostService() {

            @Override
            public R<List<PostDTO>> selectPostListByOfficialRank(Long officialRankSystemId, String source) {
                return R.fail("根据职级体系ID获取行业信息失败:" + throwable.getMessage());
            }

            @Override
            public R<PostDTO> selectPostByPostId(Long postId, String source) {
                return R.fail("查找部门根据部门ID失败:" + throwable.getMessage());
            }

            @Override
            public R<List<PostDTO>> postAdvancedSearch(Map<String, Object> params, String source) {
                return R.fail("查询岗位高级搜索:" + throwable.getMessage());
            }
        };
    }
}
