package net.qixiaowei.operate.cloud.api.remote.product;

import net.qixiaowei.integration.common.constant.SecurityConstants;
import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.factory.product.RemoteProductFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 用户服务
 */
@FeignClient(contextId = "remoteProductService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteProductFallbackFactory.class)
public interface RemoteProductService {

    /**
     * 查询产品是否用到枚举
     */
    @PostMapping("/product/queryProductQuote")
    R<List<ProductDTO>> queryProductQuote(@RequestBody ProductDTO productDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查询产品列表
     */
    @PostMapping("/product/dropList")
    R<List<ProductDTO>> dropList(@RequestBody ProductDTO productDTO, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 查询产品列表根绝productIds
     */
    @PostMapping("/product/getName")
    R<List<ProductDTO>> getName(@RequestBody List<Long> productIds, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);

    /**
     * 根据ID查找产品信息
     */
    @GetMapping("/product/remoteSelectById")
    R<ProductDTO> remoteSelectById(@RequestParam("productId") Long productId, @RequestHeader(SecurityConstants.FROM_SOURCE) String source);
}
