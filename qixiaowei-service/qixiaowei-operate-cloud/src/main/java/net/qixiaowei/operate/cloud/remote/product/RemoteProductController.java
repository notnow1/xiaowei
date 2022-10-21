package net.qixiaowei.operate.cloud.remote.product;

import net.qixiaowei.integration.common.domain.R;
import net.qixiaowei.integration.security.annotation.InnerAuth;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDTO;
import net.qixiaowei.operate.cloud.api.remote.product.RemoteProductService;
import net.qixiaowei.operate.cloud.service.product.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;


/**
 * @author TANGMICHI
 * @since 2022-10-08
 */
@RestController
@RequestMapping("product")
public class RemoteProductController implements RemoteProductService {

    @Autowired
    private IProductService productService;
    @Override
    @InnerAuth
    @PostMapping("/queryProductQuote")
    public R<List<ProductDTO>> queryProductQuote(ProductDTO productDTO) {
        return R.ok(productService.queryProductQuote(productDTO));
    }
}
