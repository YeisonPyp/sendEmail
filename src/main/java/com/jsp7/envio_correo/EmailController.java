package com.jsp7.envio_correo;


import jakarta.mail.internet.MimeMessage;
import jakarta.mail.MessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.*;
import org.springframework.mail.javamail.MimeMessageHelper;


@RestController
@RequestMapping("/api/email")
public class EmailController {

    @Autowired
    private JavaMailSender emailSender;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        MimeMessage message = emailSender.createMimeMessage();
        try {

            MimeMessageHelper helper = new MimeMessageHelper(message, true); // true indicates multipart message
            String tipoMensaje = "";

            if (emailRequest.getTipoCorreo().equals("G")) {
                tipoMensaje = "generado";
            } else if (emailRequest.getTipoCorreo().equals("R")) {
                tipoMensaje = "resuelto";
            }
            String asunto = "Ticket #" + emailRequest.getNumTicket() + " " + tipoMensaje + " - " + emailRequest.getTema();
            String mensajeP1 = String.format(
                    "<div style='font-family: Arial, sans-serif; font-size: 14px; color: #333;'>"
                            + "<p style='font-size: 16px;'>Señor Usuario,</p>"
                            + "<p>Le informamos que se ha %s el siguiente ticket de soporte:</p>"
                            + "<div style='border: 2px solid #656565; padding: 10px; background-color: #f9f9f9;'>"
                            + "<table style='font-size: 16px; border-collapse: collapse;'>"
                            + "<tr><td style='padding: 5px;'><b>Número de Ticket:</b></td><td style='padding: 5px;'>%s</td></tr>"
                            + "<tr><td style='padding: 5px;'><b>Fecha de Registro:</b></td><td style='padding: 5px;'>%s</td></tr>"
                            + "<tr><td style='padding: 5px;'><b>Módulo:</b></td><td style='padding: 5px;'>%s</td></tr>"
                            + "<tr><td style='padding: 5px;'><b>Tema:</b></td><td style='padding: 5px;'>%s</td></tr>"
                            + "<tr><td style='padding: 5px;'><b>Solicitado por:</b></td><td style='padding: 5px;'>%s</td></tr>"
                            + "</table><br>"
                            + "<p style='font-size: 16px;'><b>Detalles del problema:</b></p>"
                            + "<div style='border: 1px solid #656565; padding: 10px; background-color: #f9f9f9;'>%s</div><br>",
                    tipoMensaje, emailRequest.getNumTicket(), emailRequest.getFechaRegistro(),
                    emailRequest.getModulo(), emailRequest.getTema(), emailRequest.getUsuario(), emailRequest.getDetalle());

            String mensajeP2 = String.format(
                    "<table style='font-size: 16px; border-collapse: collapse;'>"
                            + "<tr><td style='padding: 5px;'><b>Respondido por:</b></td><td style='padding: 5px;'>%s</td></tr>"
                            + "<tr><td style='padding: 5px;'><b>Fecha de respuesta:</b></td><td style='padding: 5px;'>%s</td></tr>"
                            + "<tr><td style='padding: 5px;'><b>Estado:</b></td><td style='padding: 5px;'>%s</td></tr>"
                            + "</table><br>"
                            + "<p style='font-size: 16px;'><b>Respuesta:</b></p>"
                            + "<div style='border: 1px solid #656565; padding: 10px; background-color: #f9f9f9;'>%s</div><br>",
                    emailRequest.getUsuarioRespuesta(), emailRequest.getFechaRespuesta(), emailRequest.getEstado(), emailRequest.getRespuesta());

            String mensajeP3 = "</div>"
                    + "<br><br>"
                    + "<p style='font-size: 13px; color: #777;'>Le recordamos que este correo es generado automáticamente, por lo tanto, no debe responder al mismo. "
                    + "Para cualquier consulta adicional, por favor utilice los canales de comunicación oficiales.</p>"
                    + "<br>"
                    + "<p style='font-size: 16px;'>Atentamente,<br>"
                    + "<b>Equipo de Soporte Técnico</b><br>"
                    + "Mesa de Ayuda</p>"
                    + "</div>";

            String mensaje = emailRequest.getTipoCorreo().equals("G") ? mensajeP1 + mensajeP3 : mensajeP1 + mensajeP2 + mensajeP3;


            helper.setFrom("helpdesknova@eaav.gov.co");
            helper.setTo(emailRequest.getCorreo());
            helper.setSubject(asunto);
            helper.setText(mensaje, true); // El segundo parámetro indica que el texto es HTML

            emailSender.send(message);
            return "Correo enviado con éxito!";
        } catch (MessagingException e) {

            return "Error al enviar el correo: " + e.getMessage();
        }
    }
}
