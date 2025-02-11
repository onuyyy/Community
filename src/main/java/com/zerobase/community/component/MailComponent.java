package com.zerobase.community.component;

import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.mail.javamail.MimeMessagePreparator;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MailComponent {

    private final JavaMailSender javaMailSender;

    public boolean sendMail(SignUpMail mail) {

        boolean result = false;

        MimeMessagePreparator msg = new MimeMessagePreparator() {
            @Override
            public void prepare(MimeMessage mimeMessage) throws Exception {
                MimeMessageHelper mimeMessageHelper =
                        new MimeMessageHelper(mimeMessage, true, "utf-8");
                mimeMessageHelper.setTo(mail.getTo());
                mimeMessageHelper.setSubject("Community 사이트 가입 인증을 해주세요!");
                mimeMessageHelper.setText(
                        "<p>아래 코드를 입력하셔서 가입을 완료하세요!</p>" +
                        "<h1>" + mail.getText() + "/h1", true);
            }
        };

        try {
            javaMailSender.send(msg);
            result = true;
        } catch (Exception e) {
            // to do
        }
        return result;
    }

}
