package net.qixiaowei.message.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.context.annotation.Configuration;

/**
 * @description 邮件配置
 * @Author hzk
 * @Date 2023-01-11 17:50
 **/

@Data
@Configuration
@RefreshScope
public class MailboxConfig {

    /**
     * host
     */
    @Value("${mailbox.host:smtp.163.com}")
    private String host;

    /**
     * 协议端口
     */
    @Value("${mailbox.port:465}")
    private Integer port;

    /**
     * 账号
     */
    @Value("${mailbox.account:企小微SaaS平台<qixiaoweiqxw@163.com>}")
    private String account;

    /**
     * 密码
     */
    @Value("${mailbox.password:JXJDTZJRDRMVEDYG}")
    private String password;


}
