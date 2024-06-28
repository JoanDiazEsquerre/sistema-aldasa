package com.model.aldasa.general.bean;

import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.context.FacesContext;
import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.util.Properties;

@ManagedBean
public class EmailController {

    private final String username = "sistemas@aldasainmobiliaria.com";  // Reemplaza con tu dirección de correo electrónico de Outlook
    private final String password = "2022@@sisinfor";          // Reemplaza con tu contraseña

    public void enviarCorreo(String destinatario, String asunto, String cuerpo) {
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.office365.com"); // Servidor SMTP de Outlook para Office 365
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(username, password);
            }
        });

        try {
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(username));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destinatario));
            message.setSubject(asunto);
            message.setText(cuerpo);

            Transport.send(message);

            FacesMessage facesMessage = new FacesMessage("Correo enviado con éxito");
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);

        } catch (MessagingException e) {
            FacesMessage facesMessage = new FacesMessage("Error al enviar el correo: " + e.getMessage());
            FacesContext.getCurrentInstance().addMessage(null, facesMessage);
            e.printStackTrace();
        }
    }
}
