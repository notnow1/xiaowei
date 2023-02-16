package net.qixiaowei.message.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @description 阿里云短信配置
 * @Author hzk
 * @Date 2023-02-15 17:50
 **/
@Data
@Configuration
@RefreshScope
public class AliyunSMSConfig {

    /**
     * endpoint
     */
    @Value("${aliyunSMS.endpoint:dysmsapi.aliyuncs.com}")
    private String endpoint;

    /**
     * accessKeyId
     */
    @Value("${aliyunSMS.accessKeyId:}")
    private String accessKeyId;

    /**
     * accessKeySecret
     */
    @Value("${aliyunSMS.accessKeySecret:}")
    private String accessKeySecret;

    /**
     * 短信签名名称
     */
    @Value("${aliyunSMS.signName:}")
    private String signName;


}
