package net.brubio.model;

import com.mailjet.client.ClientOptions;
import com.mailjet.client.MailjetClient;
import com.mailjet.client.MailjetRequest;
import com.mailjet.client.MailjetResponse;
import com.mailjet.client.resource.Emailv31;
import jakarta.mail.internet.MimeMessage;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessagePreparator;

import java.io.InputStream;

public class MailjetJavaMailSender implements JavaMailSender {

    private final String apiKey;
    private final String secretKey;

    public MailjetJavaMailSender(String apiKey, String secretKey) {
        this.apiKey = apiKey;
        this.secretKey = secretKey;
    }

    @Value("${MAILJET_SENDER_EMAIL}")
    private String senderEmail;

    @Override
    public void send(SimpleMailMessage message) {
        try {
            MailjetClient client = new MailjetClient(apiKey, secretKey);
            System.err.println(senderEmail);
            //String senderEmail = System.getenv("MAILJET_SENDER_EMAIL");
            if (senderEmail == null || senderEmail.isEmpty()) {
                throw new IllegalStateException("MAILJET_SENDER_EMAIL no está definida en el entorno.");
            }
//            String senderEmail = "bryamrubio312@gmail.com";
//            if (senderEmail == null || senderEmail.isEmpty()) {
//                throw new IllegalStateException("MAILJET_SENDER_EMAIL no está definida en el entorno.");
//            }
            MailjetRequest request = new MailjetRequest(Emailv31.resource)
                    .property(Emailv31.MESSAGES, new JSONArray()
                            .put(new JSONObject()
                                    .put(Emailv31.Message.FROM, new JSONObject()
                                            .put("Email", senderEmail)
                                            .put("Name", "EmpleosApp"))
                                    .put(Emailv31.Message.TO, new JSONArray()
                                            .put(new JSONObject()
                                                    .put("Email", message.getTo()[0])
                                                    .put("Name", "Usuario")))
                                    .put(Emailv31.Message.SUBJECT, message.getSubject())
                                    .put(Emailv31.Message.TEXTPART, message.getText())
                            ));

            MailjetResponse response = client.post(request);
            System.out.println("Estado: " + response.getStatus());
            System.out.println("Respuesta: " + response.getData());
        } catch (Exception e) {
            throw new MailSendException("Error al enviar correo con Mailjet", e);
        }
    }

    // Métodos no usados, puedes dejarlos vacíos
    @Override public void send(SimpleMailMessage... messages) {}
    @Override public void send(MimeMessage mimeMessage) {}
    @Override public void send(MimeMessage... mimeMessages) {}
    @Override public void send(MimeMessagePreparator mimeMessagePreparator) {}
    @Override public void send(MimeMessagePreparator... mimeMessagePreparators) {}
    @Override public MimeMessage createMimeMessage() { return null; }
    @Override public MimeMessage createMimeMessage(InputStream contentStream) { return null; }

}
