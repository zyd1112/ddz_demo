package com.zyd.ddz.common.utils;

import com.sun.mail.util.MailSSLSocketFactory;
import com.zyd.ddz.common.entity.UserMail;
import com.zyd.zgame.common.utils.RandomUtils;
import com.zyd.zgame.common.utils.TimeUtils;
import lombok.Getter;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zyd
 * @date 2023/5/26 15:31
 */
public class MailUtils {
    private static final String NUMBERS = "0123456789";
    private static final String HOST = "smtp.qq.com";

    private static final String FROM = "3375135980@qq.com";
    private static final String USERNAME = "3375135980@qq.com";

    @Getter
    private static final Map<String, UserMail> MAILS_MAP = new ConcurrentHashMap<>();

    /**
     * 授权码
     */
    private static final String PASSWORD = "fsrvfhemymsidbec";

    public static String generateCode(int len){
        StringBuilder builder = new StringBuilder();
        for (int i = 0; i < len; i++) {
            builder.append(NUMBERS.charAt(RandomUtils.random(0, NUMBERS.length())));
        }
        return builder.toString();
    }

    public static void sendMail(UserMail userMail) {
        try {

            Properties prop = new Properties();
            prop.setProperty("mail.host", HOST);
            prop.setProperty("mail.transport.protocol", "smtp");
            prop.setProperty("mail.smtp.auth", "true");

            MailSSLSocketFactory sf = new MailSSLSocketFactory();
            sf.setTrustAllHosts(true);
            prop.put("mail.smtp.ssl.enable", "true");
            prop.put("mail.smtp.ssl.socketFactory", sf);

            Session session = Session.getDefaultInstance(prop, new Authenticator() {
                @Override
                public PasswordAuthentication getPasswordAuthentication() {
                    // 发件人邮箱 用户名和授权码
                    return new PasswordAuthentication(USERNAME, PASSWORD);
                }
            });

            session.setDebug(true);

            // 通过 Session得到 transport 对象
            Transport transport = session.getTransport();

            // 使用邮箱用户名和授权码连上邮件服务器 (登陆)
            transport.connect(HOST, USERNAME, PASSWORD);

            // 创建邮件: 写邮件
            MimeMessage message = new MimeMessage(session);

            // 设置邮件的发件人
            message.setFrom(new InternetAddress(FROM));
            // 设置邮件的收件人
            message.setRecipient(Message.RecipientType.TO,new InternetAddress(userMail.getMail()));

            // 邮件标题
            message.setSubject("斗地主用户验证码");
            // 邮件文本内容
            String content = "<div>" +
                    "<span>您的验证码为: " + userMail.getCode() + " </span></div>";
            message.setContent(content, "text/html; charset=UTF-8");

            transport.sendMessage(message, message.getAllRecipients());

            transport.close();
            MAILS_MAP.putIfAbsent(userMail.getMail(), userMail);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public static boolean checkTimeout(UserMail userMail){
        UserMail mail = MAILS_MAP.get(userMail.getMail());
        return mail != null && mail.getEnd() > TimeUtils.getNowTimeMillis();
    }

}
