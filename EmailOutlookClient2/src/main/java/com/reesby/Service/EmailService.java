package com.reesby.Service;

import com.microsoft.graph.models.extensions.Message;
import com.reesby.Model.Email;

import java.net.MalformedURLException;

public interface EmailService {
    public Message sendEmail(Email email);
    public String getAuthenticate() throws MalformedURLException;
}
