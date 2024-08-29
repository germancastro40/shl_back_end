package com.consultorio.casos_judiciales.services;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
    private JavaMailSender javaMailSender;
    private UsersCodeService usersCodeService;
    @Autowired
    public void setJavaMailSender(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Autowired
    @Lazy
    public void setUsersCodeService(UsersCodeService usersCodeService) {
        this.usersCodeService = usersCodeService;
    }

    private void sendEmail(String email, String subject, String title, String bodyMessage, String code) throws MessagingException {
        MimeMessage mimeMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mimeMessage);

        mimeMessageHelper.setFrom("germancastro40@hotmail.com");
        mimeMessageHelper.setTo(email);
        mimeMessageHelper.setSubject(subject);

        String htmlContent = """
                <style>
                    h1, h2, h3, h4, h5, h6 { font-family: 'Inter', sans-serif; --font-sans-serif: 'Inter'; }
                    body { font-family: 'Inter', sans-serif; --font-sans-serif: 'Inter'; }
                </style>
                <div style="background-color: #fff; color: #333; font-family: Inter, sans-serif; max-width: 600px; margin: auto; padding: 16px;">
                  <div style="display: flex; align-items: center; justify-content: space-between; margin-bottom: 24px;">
                  </div>
                  <div style="background-color: #ffd700; border-radius: 8px; padding: 24px;">
                    <h1 style="font-size: 28px; font-weight: bold; margin-bottom: 16px;">%s</h1>
                    <p style="margin-bottom: 24px;">
                      %s
                    </p>
                    <div style="background-color: #fff; border-radius: 8px; padding: 16px; text-align: center;">
                      <span style="font-size: 32px; font-weight: bold;">%s</span>
                    </div>
                    <p style="text-align: center;">El código expira en 15 minutos.</p>
                    <div style="text-align: center;">SLH Legal App - Todos los derechos reservados </div>
                  </div>
                </div>
            """.formatted(title, bodyMessage, code);

        mimeMessageHelper.setText(htmlContent, true);

        javaMailSender.send(mimeMessage);
    }

    public void sendEmailWithCodeToResetPassword(String email, String code) throws MessagingException {
        sendEmail(
                email,
                "Restablecer contraseña - SLH legal app.",
                "Restablecer contraseña",
                "Hemos recibido una solicitud para restablecer la contraseña de tu cuenta. Ingresa el siguiente código para continuar:",
                code
        );
    }

    public void sendEmailToActivateAccount(String email, String code) throws MessagingException {
        sendEmail(
                email,
                "Verificacion cuenta - SLH legal app.",
                "Activacion de cuenta",
                "Ingresa este código para verificar su cuenta:",
                code
        );
    }

}
