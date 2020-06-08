package com.reesby.Service;

import com.microsoft.graph.models.extensions.Message;
import com.reesby.Model.Email;
import com.reesby.Repository.EmailRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.net.MalformedURLException;

@Service
public class OutlookEmailServiceImpl implements EmailService {


    @Autowired
    EmailRepo emailRepo;


    @Override
    public Message sendEmail(Email email) {

        return emailRepo.sendEmail(email);
    }

    @Override
    public String getAuthenticate() throws MalformedURLException {
        return emailRepo.getAuthenticate();
    }
}
