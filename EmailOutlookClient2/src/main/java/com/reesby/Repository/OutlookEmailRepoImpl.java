package com.reesby.Repository;

import com.microsoft.graph.models.extensions.*;
import com.microsoft.graph.models.generated.BodyType;
import com.microsoft.graph.requests.extensions.AttachmentCollectionPage;
import com.reesby.Config.MicrosoftConfig;
import com.reesby.Config.SimpleAuthProvider;
import com.reesby.Model.Email;
import org.springframework.stereotype.Repository;



import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Paths;

import java.util.LinkedList;


@Repository
public class OutlookEmailRepoImpl implements EmailRepo {

    private static SimpleAuthProvider authProvider = null;

    @Override
    public Message sendEmail(Email email) {

        // Create the auth provider
        //authProvider = new SimpleAuthProvider(MicrosoftConfig.getAccessToken());
        Message message = new Message();
        message.subject = email.getSubject();
        ItemBody body = new ItemBody();
        body.contentType = BodyType.TEXT;
        body.content = email.getBody();
        message.body = body;
        LinkedList<Recipient> toRecipientsList = new LinkedList<Recipient>();
        Recipient toRecipients = new Recipient();
        EmailAddress emailAddress = new EmailAddress();

        emailAddress.address = email.getToRecipient();
        toRecipients.emailAddress = emailAddress;
        toRecipientsList.add(toRecipients);
        message.toRecipients = toRecipientsList;
        


        LinkedList<Recipient> ccRecipientsList = new LinkedList<Recipient>();
        Recipient ccRecipients = new Recipient();
        EmailAddress emailAddress1 = new EmailAddress();
        emailAddress1.address = email.getCcRecipient();
        ccRecipients.emailAddress = emailAddress1;
        ccRecipientsList.add(ccRecipients);
        message.ccRecipients = ccRecipientsList;

        boolean saveToSentItems = true;
        MicrosoftConfig.sendMail(message);

        return message;
    }

    @Override
    public String getAuthenticate() throws MalformedURLException {

        return MicrosoftConfig.getAccessToken();
    }

    @Override
    public void sendAttachment(Email email) throws IOException {

        FileAttachment attachment = new FileAttachment();
        attachment.name = "smile";
        attachment.contentBytes = Files.readAllBytes(Paths.get("/path/to/file"));



    }


}
