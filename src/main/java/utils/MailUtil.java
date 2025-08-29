package utils;

import jakarta.mail.Message;
import jakarta.mail.MessagingException;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeMessage;

import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class MailUtil {

    // Cấu hình cho Gmail
    private static final String SMTP_HOST = "smtp.gmail.com";
    private static final int SMTP_PORT = 587;           // STARTTLS
    private static final boolean SMTP_STARTTLS = true;  // dùng TLS
    private static final boolean SMTP_SSL = false;      // không dùng SSL 465

    // Tài khoản gửi (phải là Gmail có App Password)
    private static final String SMTP_USER = "buiphucnhanbt@gmail.com"; // TODO: đổi thành Gmail của bạn
    private static final String SMTP_PASS = "ckpbrzjmjvgmhoia"; // TODO: dán App Password
    private static final String FROM_NAME = "Support Team";

    // BẬT gửi email thực
    private static final boolean ENABLE_SEND = true;

    public static boolean sendHtml(String to, String subject, String htmlContent) {
        if (!ENABLE_SEND) {
            System.out.println("[MAIL MOCK] To: " + to);
            System.out.println("[MAIL MOCK] Subject: " + subject);
            System.out.println("[MAIL MOCK] Content:\n" + htmlContent);
            return false;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", String.valueOf(SMTP_STARTTLS));
        props.put("mail.smtp.ssl.enable", String.valueOf(SMTP_SSL));
        props.put("mail.smtp.host", SMTP_HOST);
        props.put("mail.smtp.port", String.valueOf(SMTP_PORT));
        props.put("mail.smtp.ssl.trust", SMTP_HOST);

        // Bật log debug để xem dòng lệnh SMTP, rất hữu ích khi lỗi
        props.put("mail.debug", "true");

        Session session = Session.getInstance(props,
            new jakarta.mail.Authenticator() {
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(SMTP_USER, SMTP_PASS);
                }
            });
        session.setDebug(true);

        try {
            MimeMessage message = new MimeMessage(session);
            message.setFrom(new InternetAddress(SMTP_USER, FROM_NAME, StandardCharsets.UTF_8.name()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(to));
            message.setSubject(subject, StandardCharsets.UTF_8.name());
            message.setContent(htmlContent, "text/html; charset=UTF-8");
            Transport.send(message);
            return true;
        } catch (MessagingException | java.io.UnsupportedEncodingException e) {
            e.printStackTrace(); // Xem stacktrace trong console
            return false;
        }
    }
}