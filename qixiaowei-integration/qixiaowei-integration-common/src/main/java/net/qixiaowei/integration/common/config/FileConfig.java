package net.qixiaowei.integration.common.config;

import lombok.Data;
import net.qixiaowei.integration.common.utils.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

/**
 * 文件配置
 *
 * @author hzk
 * @since 2022-10-31
 */
@Data
@Component
@ConfigurationProperties("qxw.file")
@RefreshScope
public class FileConfig {

    @Value("${domain:http://127.0.0.1:9000}")
    private String domain;

    /**
     * @param url
     * @return fullUrl
     */
    public String getFullDomain(String url) {
        return StringUtils.isEmpty(url) ? this.getDomain() : url.toLowerCase().startsWith("http") ? url : this.getDomain() + url;
    }

    /**
     * @param fullUrl
     * @return path
     */
    public String getPathOfRemoveDomain(String fullUrl) {
        return StringUtils.isEmpty(fullUrl) ? "" : fullUrl.toLowerCase().startsWith("http") ? fullUrl.replace(this.getDomain(), "") : fullUrl;
    }

}
