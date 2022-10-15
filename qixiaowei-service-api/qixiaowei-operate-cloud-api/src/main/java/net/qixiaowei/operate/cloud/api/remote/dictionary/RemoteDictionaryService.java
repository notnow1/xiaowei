package net.qixiaowei.operate.cloud.api.remote.dictionary;

import net.qixiaowei.integration.common.constant.ServiceNameConstants;
import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.factory.RemoteDictionaryFallbackFactory;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 用户服务
 */
@FeignClient(contextId = "remoteDictionaryService", value = ServiceNameConstants.OPERATE_CLOUD_SERVICE, fallbackFactory = RemoteDictionaryFallbackFactory.class)
public interface RemoteDictionaryService {
    /**
     * 查询产品是否用到枚举
     */
    @PostMapping("/product/queryDictionaryType")
    R<List<ProductDTO>> queryDictionaryType(@RequestBody ProductDTO productDTO);
}
