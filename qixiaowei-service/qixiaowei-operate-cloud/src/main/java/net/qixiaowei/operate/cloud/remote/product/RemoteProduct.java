package net.qixiaowei.operate.cloud.remote.product;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;


/**
 * @author TANGMICHI
 * @since 2022-10-08
 */
@RestController
@RequestMapping("product")
public class RemoteProduct implements RemoteProductService {

    @Autowired
    private IProductService productService;

    @Override
    @InnerAuth
    @PostMapping("/queryProductQuote")
    public R<List<ProductDTO>> queryProductQuote(@RequestBody ProductDTO productDTO, String source) {
        return R.ok(productService.queryProductQuote(productDTO));
    }

    @Override
    @InnerAuth
    @PostMapping("/dropList")
    public R<List<ProductDTO>> dropList(@RequestBody ProductDTO productDTO, String source) {
        return R.ok(productService.selectProduct(productDTO));
    }

    @Override
    @InnerAuth
    @PostMapping("/getName")
    public R<List<ProductDTO>> getName(@RequestBody List<Long> productIds, String source) {
        return R.ok(productService.selectProductList(productIds));
    }
}
