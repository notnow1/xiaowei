package net.qixiaowei.file;

import net.qixiaowei.integration.security.annotation.EnableCustomConfig;
import net.qixiaowei.integration.security.annotation.EnableRyFeignClients;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


/**
 * 文件服务
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication(scanBasePackages = {"net.qixiaowei.file", "net.qixiaowei.integration.common.config"})
public class QiXiaoWeiFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiXiaoWeiFileApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  文件服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
