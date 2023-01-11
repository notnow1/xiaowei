package net.qixiaowei.message.util;

import cn.hutool.extra.mail.MailAccount;
import cn.hutool.extra.mail.MailUtil;
import lombok.extern.slf4j.Slf4j;
import net.qixiaowei.integration.common.utils.StringUtils;

import java.util.Collection;


/**
 * @description 邮件发送工具类
 * @Author hzk
 * @Date 2023-01-10 17:55
 **/
@Slf4j
public class SendMailUtils {

    /**
     * @description: 发送邮件
     * @Author: hzk
     * @date: 2023/1/11 9:25
     * @param: [account, tos, subject, content, isHtml]
     * @return: boolean
     **/
    public static boolean sendEmail(MailAccount account, Collection<String> tos, String subject, String content, Boolean isHtml) {
        if (StringUtils.isEmpty(tos)) {
            log.error("发送邮件失败，收件人为空");
            return false;
        }
        boolean isSend = false;
        try {
            MailUtil.send(account, tos, subject, content, isHtml);
            isSend = true;
        } catch (Exception ex) {
            log.error("发送邮件失败", ex);
        }
        return isSend;
    }


}
