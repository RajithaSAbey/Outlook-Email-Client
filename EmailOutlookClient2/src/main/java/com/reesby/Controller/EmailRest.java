package com.reesby.Controller;

import com.microsoft.graph.models.extensions.Message;
import com.microsoft.graph.requests.extensions.IMessageCollectionPage;
import com.reesby.Config.MicrosoftConfig;
import com.reesby.Model.Email;
import com.reesby.Service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.net.MalformedURLException;


@RestController
@RequestMapping("/api/v1")

public class EmailRest {

    @Autowired
    MsalAuthHelper msalAuthHelper;
    @Autowired
    EmailService emailService;

    @RequestMapping("/graphMeApi")
    public String graphMeApi() throws MalformedURLException {

        String oboAccessToken = msalAuthHelper.getOboToken("https://graph.microsoft.com/.default");

        return callMicrosoftGraphMeEndpoint(oboAccessToken);
    }


    private String callMicrosoftGraphMeEndpoint(String accessToken){
        RestTemplate restTemplate = new RestTemplate();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        headers.set("Authorization", "Bearer " + accessToken);

        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        String result = restTemplate.exchange("https://graph.microsoft.com/v1.0/me", HttpMethod.GET,
                entity, String.class).getBody();

        return result;
    }




    @PostMapping("/Outlook/sendEmail/")
    public Message sendEmail(@RequestBody Email email){
        return emailService.sendEmail(email);


    }

    @GetMapping("/Outlook/Authenticate/")
    public String getAuthenticate() throws MalformedURLException {

        return emailService.getAuthenticate();


    }

    @GetMapping("/Outlook/getTopMessages")
    public IMessageCollectionPage getTopMessages(){
        return MicrosoftConfig.getTopMessages();
    }



}
