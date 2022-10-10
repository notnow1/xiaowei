package net.qixiaowei.operate.cloud.api.dto.product;

import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.List;

/**
 * 产品数据
 * @author TANGMICHI
 * @since 2022-10-09
 */
@Data
@Accessors(chain = true)
public class ProductDataDTO {
    /**
     * 产品ID
     */
    private  Long productId;
    /**
     * 产品规格ID
     */
    private  Long productSpecificationId;
    /**
     * 产品规格参数ID
     */
    private  Long productSpecificationParamId;
    /**
     * 规格名称
     */
    private  String specificationName;
    /**
     * 目录价,单位元
     */
    private BigDecimal listPrice;
    /**
     * 产品数据
     */
    private List<ProductSpecificationDataDTO> productSpecificationDataDTOList;
}
