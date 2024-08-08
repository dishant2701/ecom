package com.Ecomm.Ecomm.utils;

// import jakarta.mail.internet.InternetAddress;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import jakarta.mail.internet.InternetAddress;

@Component
public class SmsThread {

  // @Autowired
  // MailConfig mailer;

  public void send(String otp, String mobile) {
    new Thread() {
      @Override
      public void run() {
        System.out.println("sending SMS & email with OTP: " + otp);
        try {

          String smsResponse = new SmsService()
              .sendOtpSMS("Your OTP is " + otp + " - Digital India Corporation",
                  mobile);
          System.out.println("SMS API response: " + smsResponse);
        } catch (Exception e) {
          System.out.println("SMS API error: " + e);
        }
      }
    }.start();
  }

  public void sendEmail(String otp, String email) throws Exception {
    var text = "Your OTP is " + otp + " for Young Professional Law EPFO";
    InternetAddress[] addresses = new InternetAddress[1];
    addresses[0] = new InternetAddress(email);
    var mailer = new MailConfig();
    new Thread(() -> {
      try {
        System.out.println("sending email with OTP: " + otp);
        var emailResponse = mailer.sendMail("One-Time Password for Young Professional Law EPFO",
            addresses, text, null, null);
        if (emailResponse.equals("SUCCESS")) {
          System.out.println("Email sent successfully");
        } else {
          System.err.println("email response error: " + emailResponse);
        }
      } catch (Exception ex) {
        System.err.println("--- Error sending email ---");
        ex.printStackTrace();
        System.err.println("--- x ---");
      }
    }).start();

    // new Thread(() -> {
    // System.out.println("sending SMS with OTP: " + otp);
    // String smsresponse = new SmsService().sendOtpSMS(text);
    // System.out.println("SMS API response: " + smsresponse);
    // }).start();
  }

  public void sendPdfEmail(String email, byte[] attachmentFile) throws Exception {
    var text = "Kindly review the attached PDF summary report for Young Professional Law EPFO.";
    InternetAddress[] addresses = new InternetAddress[1];
    addresses[0] = new InternetAddress(email);
    var mailer = new MailConfig();

    new Thread(() -> {
      try {
        System.out.println("sending email with pdf: ");
        var emailResponse = mailer.sendMail("Report for Young Professional Law | EPFO",
            addresses, text, "SummaryReport", attachmentFile);
        if (emailResponse.equals("SUCCESS")) {
          System.out.println("Email sent successfully");
        } else {
          System.err.println("email response error: " + emailResponse);
        }
      } catch (Exception ex) {
        System.err.println("--- Error sending email ---");
        ex.printStackTrace();
        System.err.println("--- x ---");
      }
    }).start();

    // new Thread(() -> {
    // System.out.println("sending SMS with OTP: " + otp);
    // String smsresponse = new SmsService().sendOtpSMS(text);
    // System.out.println("SMS API response: " + smsresponse);
    // }).start();
  }
}
