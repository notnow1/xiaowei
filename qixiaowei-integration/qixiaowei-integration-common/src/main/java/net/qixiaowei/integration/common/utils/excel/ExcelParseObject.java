package net.qixiaowei.integration.common.utils.excel;

import lombok.Data;
import org.apache.poi.ss.formula.functions.T;

import java.util.List;

@Data
public class ExcelParseObject<T> {
    /**
     * 成功条数
     */
    private  Integer successTotal;
    /**
     * 失败条数
     */
    private  Integer errorTotal;
    /**
     * 成功list
     */
    private List<? extends T>  successList ;
    /**
     * 失败list
     */
    private List<?  extends T>  errorList ;
}
