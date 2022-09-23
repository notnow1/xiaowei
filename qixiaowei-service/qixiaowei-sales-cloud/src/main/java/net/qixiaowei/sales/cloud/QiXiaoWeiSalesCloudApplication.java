package net.qixiaowei.sales.cloud;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import net.qixiaowei.integration.security.annotation.EnableCustomConfig;
import net.qixiaowei.integration.security.annotation.EnableRyFeignClients;

/**
 * 系统模块
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication
public class QiXiaoWeiSalesCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiXiaoWeiSalesCloudApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  销售云模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
