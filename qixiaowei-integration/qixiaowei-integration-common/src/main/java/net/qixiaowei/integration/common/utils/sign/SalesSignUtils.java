package net.qixiaowei.integration.common.utils.sign;

import cn.hutool.core.date.DateUtil;
import cn.hutool.crypto.SecureUtil;
import net.qixiaowei.integration.common.utils.DateUtils;

import java.util.Date;

/**
 * 销售云签名工具类
 */
public class SalesSignUtils {

    private static final String SALES_ACCESS_KEY = "sync_register_qixiaowei";

    private static final String SALES_ACCESS_SECRET = "sync_register_zenglve_20230324";

    /**
     * @description: 构建销售云签名
     * @Author: hzk
     * @date: 2023/4/4 11:18
     * @param: [account, time]
     * @return: java.lang.String
     **/
    public static String buildSaleSign(String account, Date time) {
        return SecureUtil.md5(account + "@" + SALES_ACCESS_KEY + "@" + DateUtil.formatDateTime(time) + "@" + SALES_ACCESS_SECRET);
    }


    /**
     * @description: 构建销售云签名
     * @Author: hzk
     * @date: 2023/4/4 11:18
     * @param: [account, formatDateTime]
     * @return: java.lang.String
     **/
    public static String buildSaleSign(String account, String formatDateTime) {
        return SecureUtil.md5(account + "@" + SALES_ACCESS_KEY + "@" + formatDateTime + "@" + SALES_ACCESS_SECRET);
    }

    public static void main(String[] args) {
        System.out.printf(SalesSignUtils.buildSaleSign("admin", "2099-12-31 23:59:59"));
    }
}
