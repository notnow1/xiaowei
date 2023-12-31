package net.qixiaowei.operate.cloud.api.factory.targetManager;


import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.domain.targetManager.TargetDecompose;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.remote.targetManager.RemoteDecomposeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;

import java.util.List;
import java.util.Map;

/**
 * 目标分解服务降级处理
 */
public class RemoteDecomposeFallbackFactory implements FallbackFactory<RemoteDecomposeService> {

    private static final Logger log = LoggerFactory.getLogger(RemoteDecomposeFallbackFactory.class);

    @Override
    public RemoteDecomposeService create(Throwable throwable) {
        log.error("目标分解服务调用失败:{}", throwable.getMessage());
        return new RemoteDecomposeService() {

            @Override
            public R<TargetDecomposeDTO> info(Long targetDecomposeId, String source) {
                return R.fail("根据目标分解id获取目标分解信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<TargetDecomposeDTO>> selectBytargetDecomposeIds(List<Long> targetDecomposeId, String source) {
                return R.fail("根据目标分解id集合获取目标分解信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<TargetDecomposeDetailsDTO>> selectDecomposeDetailsBytargetDecomposeId(Long targetDecomposeId, String source) {
                return R.fail("根据目标分解id获取目标分解详细信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<TargetDecomposeDetailsDTO>> getDecomposeDetails(TargetDecomposeDetailsDTO targetDecomposeDetailsDTO, String source) {
                return R.fail("根据表字段条件获取目标分解详细信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<TargetDecompose>> queryDeptDecompose(List<Long> departmentIds,String source) {
                return R.fail("根据表字段条件获取目标分解详细信息失败:" + throwable.getMessage());
            }

            @Override
            public R<List<TargetDecomposeDetailsDTO>> selectByIds(Map<Integer, List<Long>> map, String source) {
                return R.fail("根据分解维度ID集合查询目标分解数据失败:" + throwable.getMessage());
            }

            @Override
            public R<List<TargetDecomposeDTO>> selectByIndicatorIds(List<Long> indicatorIds, String source) {
                return R.fail("根据指标ID查询目标分解失败:" + throwable.getMessage());
            }

        };
    }
}
