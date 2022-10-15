package net.qixiaowei.operate.cloud.api.factory;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.remote.dictionary.RemoteDictionaryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.openfeign.FallbackFactory;
import org.springframework.stereotype.Component;

import java.util.List;


/**
 * 服务降级处理
 */
@Component
public  class RemoteDictionaryFallbackFactory implements FallbackFactory<RemoteDictionaryService> {
    private static final Logger log = LoggerFactory.getLogger(RemoteDictionaryFallbackFactory.class);

    @Override
    public RemoteDictionaryService create(Throwable throwable) {
        log.error("服务调用失败:{}", throwable.getMessage());
        return new RemoteDictionaryService() {

            @Override
            public R<List<ProductDTO>> queryDictionaryType(ProductDTO productDTO) {
                return null;
            }
        };
    }
}
