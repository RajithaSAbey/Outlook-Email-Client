package com.reesby.Repository;

import com.microsoft.graph.models.extensions.Message;
import com.reesby.Model.Email;

import java.io.IOException;
import java.net.MalformedURLException;

public interface EmailRepo {

    Message sendEmail(Email email);
    String getAuthenticate() throws MalformedURLException;
    void sendAttachment(Email email) throws IOException;
}
