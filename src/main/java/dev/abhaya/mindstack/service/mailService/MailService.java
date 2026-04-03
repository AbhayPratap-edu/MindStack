package dev.abhaya.mindstack.service.mailService;

import com.sendgrid.Method;
import com.sendgrid.Request;
import com.sendgrid.Response;
import com.sendgrid.SendGrid;
import com.sendgrid.helpers.mail.Mail;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class MailService {

    private static final Logger log = LoggerFactory.getLogger(MailService.class);

    @Value("${SENDGRID_API_KEY}")//environment variable comes from os/render
    private String sendGridApiKey;

    @Async
    public void sendMail(Mail mail) {

        try{
            SendGrid sendGrid = new SendGrid(sendGridApiKey);

            Request request = new Request();
            request.setMethod(Method.POST);
            request.setEndpoint("mail/send");
            request.setBody(mail.build());

            Response response = sendGrid.api(request);

            log.info("SendGrid status: {}, body: {}",
                    response.getStatusCode(), response.getBody());
        }
        catch (Exception e){
            log.error("Error while sending email via SendGrid", e);
        }


    }
}
