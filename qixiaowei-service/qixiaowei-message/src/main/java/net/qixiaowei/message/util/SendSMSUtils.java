package net.qixiaowei.message.util;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.aliyun.dysmsapi20170525.Client;
import com.aliyun.dysmsapi20170525.models.SendSmsRequest;
import com.aliyun.dysmsapi20170525.models.SendSmsResponse;
import com.aliyun.tea.TeaException;
import com.aliyun.teaopenapi.models.Config;
import com.aliyun.teautil.models.RuntimeOptions;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.utils.StringUtils;
import net.qixiaowei.message.config.AliyunSMSConfig;

import java.util.Collection;


/**
 * @description 短信发送工具类
 * @Author hzk
 * @Date 2023-02-15 15:55
 **/
@Slf4j
public class SendSMSUtils {

    /**
     * @description: 发送短信
     * @Author: hzk
     * @date: 2023/2/16 10:20
     * @param: [aliyunSMSConfig, tos, templateCode, templateParam]
     * @return: boolean
     **/
    public static boolean sendSMS(AliyunSMSConfig aliyunSMSConfig, Collection<String> tos, String templateCode, String templateParam) {
        if (StringUtils.isEmpty(tos)) {
            log.error("发送阿里短信失败，收件人为空");
            return false;
        }
        String signName = aliyunSMSConfig.getSignName();
        String phoneNumbers = CollUtil.join(tos, StrUtil.COMMA);
        boolean isSend = false;
        try {
            Client client = SendSMSUtils.createClient(aliyunSMSConfig);
            SendSmsRequest sendSmsRequest = new SendSmsRequest()
                    .setPhoneNumbers(phoneNumbers)
                    .setSignName(signName)
                    .setTemplateCode(templateCode)
                    .setTemplateParam(templateParam);
            RuntimeOptions runtimeOptions = new RuntimeOptions();
            // 开启自动重试机制
            runtimeOptions.autoretry = true;
            // 设置自动重试次数
            runtimeOptions.maxAttempts = 3;
            //执行发送
            log.info("阿里短信发送前 phoneNumbers {} templateCode {} templateParam {} ", phoneNumbers, templateCode, templateParam);
            SendSmsResponse sendSmsResponse = client.sendSmsWithOptions(sendSmsRequest, runtimeOptions);
            log.info("阿里短信发送后 sendSmsResponse {}", JSON.toJSONString(sendSmsResponse));
            isSend = true;
        } catch (TeaException error) {
            log.error("阿里短信发送失败" + JSON.toJSONString(error));
        } catch (Exception _error) {
            log.error("阿里短信发送失败" + _error.getMessage());
        }
        return isSend;
    }

    /**
     * 使用AK&SK初始化账号Client
     *
     * @param aliyunSMSConfig
     * @return Client
     * @throws Exception
     */
    public static Client createClient(AliyunSMSConfig aliyunSMSConfig) throws Exception {
        String accessKeyId = aliyunSMSConfig.getAccessKeyId();
        String accessKeySecret = aliyunSMSConfig.getAccessKeySecret();
        String endpoint = aliyunSMSConfig.getEndpoint();
        Config config = new Config()
                .setAccessKeyId(accessKeyId)
                .setAccessKeySecret(accessKeySecret)
                .setEndpoint(endpoint);
        return new Client(config);
    }

}
