package net.qixiaowei.file;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;


/**
 * 文件服务
 */
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
public class QiXiaoWeiFileApplication {
    public static void main(String[] args) {
        SpringApplication.run(QiXiaoWeiFileApplication.class, args);
        System.out.println("(♥◠‿◠)ﾉﾞ  文件服务模块启动成功   ლ(´ڡ`ლ)ﾞ  \n");
    }
}
