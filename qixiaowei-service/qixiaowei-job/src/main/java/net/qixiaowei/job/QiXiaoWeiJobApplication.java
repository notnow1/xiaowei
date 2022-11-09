package net.qixiaowei.job;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import net.qixiaowei.integration.security.annotation.EnableRyFeignClients;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

/**
 * 系统模块
 */
@EnableRyFeignClients
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class QiXiaoWeiJobApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiXiaoWeiJobApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ 定时器模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
