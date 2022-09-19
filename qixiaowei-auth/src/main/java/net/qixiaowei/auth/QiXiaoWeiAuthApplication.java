package net.qixiaowei.auth;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import net.qixiaowei.integration.security.annotation.EnableRyFeignClients;

/**
 * 认证授权中心
 */
@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class QiXiaoWeiAuthApplication {
    public static void main(String[] args) {
        SpringApplication.run(net.qixiaowei.auth.QiXiaoWeiAuthApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  认证授权中心启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
