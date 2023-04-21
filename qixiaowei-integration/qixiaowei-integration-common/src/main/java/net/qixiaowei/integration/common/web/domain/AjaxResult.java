package net.qixiaowei.integration.common.web.domain;

import net.qixiaowei.integration.common.constant.HttpStatus;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.apache.poi.ss.formula.functions.T;

import java.util.*;

/**
 * 操作消息提醒
 */
public class AjaxResult extends HashMap<String, Object> {
    private static final long serialVersionUID = 1L;

    /**
     * 状态码
     */
    public static final String CODE_TAG = "code";

    /**
     * 返回内容
     */
    public static final String MSG_TAG = "msg";

    /**
     * 数据对象
     */
    public static final String DATA_TAG = "data";
    /**
     * 成功条数
     */
    public static final String SUCCESS_TOTAL = "successTotal";
    /**
     * 失败条数
     */
    public static final String ERROR_TOTAL = "errorTotal";
    /**
     * 成功list
     */
    public static final String SUCCESS_LIST = "successList";
    /**
     * 失败list
     */
    public static final String ERROR_LIST = "errorList";

    /**
     * 初始化一个新创建的 AjaxResult 对象，使其表示一个空消息。
     */
    public AjaxResult() {
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     */
    public AjaxResult(int code, String msg) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
    }

    /**
     * 初始化一个新创建的 AjaxResult 对象
     *
     * @param code 状态码
     * @param msg  返回内容
     * @param data 数据对象
     */
    public AjaxResult(int code, String msg, Object data) {
        super.put(CODE_TAG, code);
        super.put(MSG_TAG, msg);
        if (StringUtils.isNotNull(data)) {
            super.put(DATA_TAG, data);
        }
    }

    /**
     * 返回成功消息
     *
     * @return 成功消息
     */
    public static AjaxResult success() {
        return AjaxResult.success("操作成功");
    }

    /**
     * 返回成功数据
     *
     * @return 成功消息
     */
    public static AjaxResult success(Object data) {
        return AjaxResult.success("操作成功", data);
    }

    /**
     * 返回成功excel数据
     *
     * @return 成功消息
     */
    public static <T> AjaxResult successExcel(List<T> successExcel, List<T> errorExcel, String msg,boolean excelFlag,String errorExcelId) {
        Map<Object, Object> data = new HashMap<>();
        if (StringUtils.isEmpty(successExcel)) {
            data.put(SUCCESS_TOTAL, 0);
        } else {
            data.put(SUCCESS_TOTAL, successExcel.size());
        }
        if (StringUtils.isEmpty(errorExcel)) {
            data.put(ERROR_TOTAL, 0);
        } else {
            data.put(ERROR_TOTAL, errorExcel.size());
            if (StringUtils.isNotNull(errorExcelId)){
                data.put(errorExcelId,12 );
            }
        }
        if (excelFlag){
            data.put(SUCCESS_LIST, successExcel);
            data.put(ERROR_LIST, errorExcel);
        }

        return AjaxResult.success(Optional.ofNullable(msg).orElse("操作成功").length() > 1 ? msg : "操作成功", data);
    }

    /**
     * 返回成功消息
     *
     * @param msg 返回内容
     * @return 成功消息
     */
    public static AjaxResult success(String msg) {
        return AjaxResult.success(msg, null);
    }

    /**
     * 返回成功消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 成功消息
     */
    public static AjaxResult success(String msg, Object data) {
        return new AjaxResult(HttpStatus.SUCCESS, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @return
     */
    public static AjaxResult error() {
        return AjaxResult.error("操作失败");
    }

    /**
     * 返回错误消息
     *
     * @param msg 返回内容
     * @return 警告消息
     */
    public static AjaxResult error(String msg) {
        return AjaxResult.error(msg, null);
    }

    /**
     * 返回错误消息
     *
     * @param msg  返回内容
     * @param data 数据对象
     * @return 警告消息
     */
    public static AjaxResult error(String msg, Object data) {
        return new AjaxResult(HttpStatus.ERROR, msg, data);
    }

    /**
     * 返回错误消息
     *
     * @param code 状态码
     * @param msg  返回内容
     * @return 警告消息
     */
    public static AjaxResult error(int code, String msg) {
        return new AjaxResult(code, msg, null);
    }

    /**
     * 是否为成功消息
     *
     * @return 结果
     */
    public boolean isSuccess() {
        return Objects.equals(HttpStatus.SUCCESS, this.get(CODE_TAG));
    }

    /**
     * 是否为错误消息
     *
     * @return 结果
     */
    public boolean isError() {
        return !isSuccess();
    }

    /**
     * 方便链式调用
     *
     * @param key
     * @param value
     * @return
     */
    @Override
    public AjaxResult put(String key, Object value) {
        super.put(key, value);
        return this;
    }
}
