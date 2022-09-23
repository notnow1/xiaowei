package net.qixiaowei.talents.cloud;

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
public class QiXiaoWeiTalentsCloudApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiXiaoWeiTalentsCloudApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  人才云模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
