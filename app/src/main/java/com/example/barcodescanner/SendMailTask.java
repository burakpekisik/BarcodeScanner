package com.example.barcodescanner;

import android.os.AsyncTask;
import android.util.Log;

import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class SendMailTask extends AsyncTask<Void, Void, Void> {

    private static final String TAG = "SendMailTask";
    private String email;
    private String subject;
    private String message;

    public SendMailTask(String email, String subject, String message) {
        this.email = email;
        this.subject = subject;
        this.message = message;
    }

    @Override
    protected Void doInBackground(Void... params) {
        if (email.isEmpty()) {
            // E-posta adresi boş ise işlem yapma
            Log.e(TAG, "E-posta adresi boş.");
            return null;
        }

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.smtp.host", "smtp.gmail.com");
        props.put("mail.smtp.port", "587");

        // Gmail hesabınızın kimlik bilgileri
        final String username = BuildConfig.EMAIL_USERNAME;
        final String password = BuildConfig.EMAIL_PASSWORD;

        Session session = Session.getInstance(props,
                new javax.mail.Authenticator() {
                    protected PasswordAuthentication getPasswordAuthentication() {
                        return new PasswordAuthentication(username, password);
                    }
                });

        try {
            MimeMessage mimeMessage = new MimeMessage(session);
            mimeMessage.setFrom(new InternetAddress(username));
            mimeMessage.addRecipient(Message.RecipientType.TO, new InternetAddress(email));
            mimeMessage.setSubject(subject);
            mimeMessage.setText(message);

            Transport.send(mimeMessage);
        } catch (MessagingException e) {
            Log.e(TAG, "Exception: " + e.getMessage(), e);
        }
        return null;
    }

    /*
    String toEmail = "receiver_email@example.com";
String emailSubject = "Konu";
String emailMessage = "Merhaba, bu bir test mesajıdır.";

SendMailTask sendMailTask = new SendMailTask(toEmail, emailSubject, emailMessage);
sendMailTask.execute();
     */
}
