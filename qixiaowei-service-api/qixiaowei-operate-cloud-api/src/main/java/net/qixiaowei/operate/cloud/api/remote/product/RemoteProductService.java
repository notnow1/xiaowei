package net.qixiaowei.operate.cloud.api.remote.product;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.factory.RemoteProductFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

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
    R<List<ProductDTO>> queryProductQuote(@RequestBody ProductDTO productDTO);

    /**
     * 查询产品列表
     */
    @PostMapping("/product/dropList")
    R<List<ProductDTO>> dropList(@RequestBody ProductDTO productDTO);

    /**
     * 查询产品列表根绝productIds
     */
    @PostMapping("/product/getName")
    R<List<ProductDTO>> getName(List<Long> decomposeDimensions);
}
