package net.qixiaowei.operate.cloud.api.remote.targetManager;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDTO;
import net.qixiaowei.operate.cloud.api.dto.targetManager.TargetDecomposeDetailsDTO;
import net.qixiaowei.operate.cloud.api.factory.targetManager.RemoteDecomposeFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 目标分解主表服务
 */
@FeignClient(contextId = "remoteDecomposeService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteDecomposeFallbackFactory.class)
public interface RemoteDecomposeService {
    /**
     * 根据目标分解id查询目标分解数据
     * @param targetDecomposeId
     * @param source
     * @return
     */
    @GetMapping("/targetDecompose/remote/targetDecomposeId")
    R<TargetDecomposeDTO> info(Long targetDecomposeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
    /**
     * 根据目标分解id查询目标分解数据
     * @param targetDecomposeId
     * @param source
     * @return
     */
    @PostMapping("/targetDecompose/remote/decomposeDetails/targetDecomposeIds")
    R<List<TargetDecomposeDTO>> selectBytargetDecomposeIds(@RequestBody  List<Long> targetDecomposeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);


    /**
     * 根据目标分解id查询目标分解详情数据
     * @param targetDecomposeId
     * @param source
     * @return
     */
    @GetMapping("/targetDecompose/remote/decomposeDetails/targetDecomposeId")
    R<List<TargetDecomposeDetailsDTO>> selectDecomposeDetailsBytargetDecomposeId(Long targetDecomposeId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 传入实体类根据条件查询
     * @param targetDecomposeDetailsDTO
     * @param source
     * @return
     */
    @PostMapping("/targetDecompose/remote/decomposeDetails/getDecomposeDetails")
    R<List<TargetDecomposeDetailsDTO>> getDecomposeDetails(@RequestBody TargetDecomposeDetailsDTO targetDecomposeDetailsDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

}