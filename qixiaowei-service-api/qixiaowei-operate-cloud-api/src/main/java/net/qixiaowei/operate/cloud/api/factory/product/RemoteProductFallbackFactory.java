package net.qixiaowei.operate.cloud.api.factory.product;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 服务降级处理
 */
@Component
public class RemoteProductFallbackFactory implements FallbackFactory<RemoteProductService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteProductFallbackFactory.class);

    @Override
    public RemoteProductService create(Throwable throwable) {
        log.error("服务调用失败:{}", throwable.getMessage());
        return new RemoteProductService() {

            @Override
            public R<List<ProductDTO>> queryProductQuote(ProductDTO productDTO, String source) {
                return R.fail("获取产品是否被引用失败:" + throwable.getMessage());
            }

            @Override
            public R<List<ProductDTO>> dropList(ProductDTO productDTO, String source) {
                return R.fail("获取产品列表失败:" + throwable.getMessage());
            }

            @Override
            public R<List<ProductDTO>> getName(List<Long> decomposeDimensions, String source) {
                return R.fail("获取产品列表失败:" + throwable.getMessage());
            }

            @Override
            public R<ProductDTO> remoteSelectById(Long productId, String source) {
                return R.fail("根据ID查找产品信息失败:" + throwable.getMessage());
            }
        };
    }
}
