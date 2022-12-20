package net.qixiaowei.operate.cloud.excel.product;


import net.qixiaowei.integration.common.web.domain.BaseEntity;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import com.alibaba.excel.annotation.format.DateTimeFormat;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import lombok.Data;
import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import net.qixiaowei.operate.cloud.api.dto.product.ProductDataDTO;
import net.qixiaowei.operate.cloud.api.dto.product.ProductSpecificationParamDTO;

/**
 * 产品表
 *
 * @author TANGMICHI
 * @since 2022-12-19
 */
@Data
@ColumnWidth(25)
@HeadRowHeight(20)
@ContentRowHeight(18)
public class ProductExcel {

    /**
     * 产品编码
     */
    @ExcelIgnore
    @ExcelProperty("产品编码")
    private String productCode;
    /**
     * 产品名称
     */
    @ExcelIgnore
    @ExcelProperty("产品名称")
    private String productName;
    /**
     * 父级产品编码
     */
    @ExcelIgnore
    @ExcelProperty("父级产品编码")
    private String parentProductCode;

    /**
     * 产品单位
     */
    @ExcelIgnore
    @ExcelProperty("产品单位")
    private String productUnitName;
    /**
     * 产品类别
     */
    @ExcelIgnore
    @ExcelProperty("产品类别")
    private String  productCategoryName;
    /**
     * 上架标记：0下架;1上架
     */
    @ExcelIgnore
    @ExcelProperty("上架标记：0下架;1上架")
    private String listingFlag;

    /**
     * 产品描述
     */
    @ExcelIgnore
    @ExcelProperty("产品描述")
    private String productDescription;


    /**
     * 产品规格参数表集合
     */
    @ExcelIgnore
    private List<ProductSpecificationParamDTO> productSpecificationParamDTOList;

    /**
     * 产品规格数据表集合
     */
    @ExcelIgnore
    private List<ProductDataDTO> productDataDTOList;

}

