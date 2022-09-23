package net.qixiaowei.wisdom.cloud;

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
public class QiXiaoWeiWisdomCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiXiaoWeiWisdomCloudApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  智慧云模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
