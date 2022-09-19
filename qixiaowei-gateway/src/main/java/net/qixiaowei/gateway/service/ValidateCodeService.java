package net.qixiaowei.gateway.service;

import java.io.IOException;

import net.qixiaowei.integration.common.exception.CaptchaException;
import net.qixiaowei.integration.common.web.domain.AjaxResult;

/**
 * 验证码处理
 */
public interface ValidateCodeService {
    /**
     * 生成验证码
     */
    public AjaxResult createCaptcha() throws IOException, CaptchaException;

    /**
     * 校验验证码
     */
    public void checkCaptcha(String key, String value) throws CaptchaException;
}
