package net.qixiaowei.system.manage;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import net.qixiaowei.integration.security.annotation.EnableCustomConfig;
import net.qixiaowei.integration.security.annotation.EnableRyFeignClients;

/**
 * 系统模块
 */
@EnableCustomConfig
@EnableRyFeignClients
@SpringBootApplication(scanBasePackages = {"net.qixiaowei.system.manage", "net.qixiaowei.integration.common.config"})
public class QiXiaoWeiSystemManageApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiXiaoWeiSystemManageApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  系统管理模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
