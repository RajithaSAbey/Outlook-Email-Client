package com.reesby.Model;


public class Email {

    private  String toRecipient;
    private String ccRecipient;
    private String bccRecipient;
    private String subject;
    private String body;

    public Email() {
    }

    public String getToRecipient() {
        return toRecipient;
    }

    public void setToRecipient(String toRecipient) {
        this.toRecipient = toRecipient;
    }

    public String getCcRecipient() {
        return ccRecipient;
    }

    public void setCcRecipient(String ccRecipient) {
        this.ccRecipient = ccRecipient;
    }

    public String getBccRecipient() {
        return bccRecipient;
    }

    public void setBccRecipient(String bccRecipient) {
        this.bccRecipient = bccRecipient;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
