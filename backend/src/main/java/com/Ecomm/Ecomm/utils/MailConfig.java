package com.Ecomm.Ecomm.utils;

import java.io.File;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Properties;

import jakarta.activation.DataHandler;
import jakarta.activation.DataSource;
import jakarta.activation.FileDataSource;
import jakarta.mail.Authenticator;
import jakarta.mail.Message;
import jakarta.mail.Multipart;
import jakarta.mail.PasswordAuthentication;
import jakarta.mail.Session;
import jakarta.mail.Transport;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import jakarta.mail.util.ByteArrayDataSource;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@EnableConfigurationProperties(MailConfig.class)
@ConfigurationProperties
// @ConfigurationProperties(prefix = "mail")
public class MailConfig {

  @Value("${host}")
  private String onlyHost;

  @Value("${mail.host}")
  private String mailDotHost;

  private String host;
  private Integer port;
  private boolean proxySet;
  private String proxihost;
  private Integer proxiport;
  private String auth;
  private String starttls;
  private String user = "ncog@digitalindia.gov.in";
  private String password = "Ncog@1948";
  private String subject;
  private InternetAddress[] portal;

  DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

  public String getHost() {
    return host;
  }

  public void setHost(String host) {
    this.host = host;
  }

  public int getPort() {
    return port;
  }

  public void setPort(int port) {
    this.port = port;
  }

  public String getAuth() {
    return auth;
  }

  public void setAuth(String auth) {
    this.auth = auth;
  }

  public String getStarttls() {
    return starttls;
  }

  public void setStarttls(String starttls) {
    this.starttls = starttls;
  }

  public String getUser() {
    return user;
  }

  public void setUser(String user) {
    this.user = user;
  }

  public String getPassword() {
    return password;
  }

  public void setPassword(String password) {
    this.password = password;
  }

  public String getSubject() {
    return subject;
  }

  public void setSubject(String subject) {
    this.subject = subject;
  }

  public InternetAddress[] getPortal() {
    return portal;
  }

  public void setPortal(InternetAddress[] portal) {
    this.portal = portal;
  }

  public String getProxihost() {
    return proxihost;
  }

  public void setProxihost(String proxihost) {
    this.proxihost = proxihost;
  }

  public Integer getProxiport() {
    return proxiport;
  }

  public void setProxiport(Integer proxiport) {
    this.proxiport = proxiport;
  }

  public String sendMailMultipleReceipent(String subject, InternetAddress[] to,
      String msgcontent,
      List<String> files) {
    try {
      Properties properties = new Properties();
      properties.put("mail.smtp.host", host);
      properties.put("mail.smtp.port", port);
      properties.put("mail.smtp.auth", auth);

      if (proxySet) {
        properties.put("proxySet", true);
        properties.put("socksProxyHost", proxihost);
        properties.put("socksProxyPort", proxiport);
      }

      properties.put("mail.smtp.starttls.enable", starttls);
      properties.put("mail.user", user);
      properties.put("mail.password", password);

      Authenticator auth = new Authenticator() {
        public PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(user, password);
        }
      };
      Session session = Session.getInstance(properties, auth);
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(user));
      msg.setRecipients(Message.RecipientType.TO, to);
      msg.setSubject(subject);
      msg.setSentDate(new Date());
      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(msgcontent, "text/html");
      Multipart multipart = new MimeMultipart();

      if (files != null && files.size() > 1) {
        for (String filepath : files) {
          MimeBodyPart attachmentPart = new MimeBodyPart();
          DataSource source = new FileDataSource(new File(filepath));
          attachmentPart.setDataHandler(new DataHandler(source));
          attachmentPart
              .setFileName("CMSMS_DPR_" + LocalDate.now().minusDays(1).format(formatter)
                  + ".pdf");
          multipart.addBodyPart(attachmentPart);
        }
      }

      multipart.addBodyPart(messageBodyPart);
      msg.setContent(multipart);
      Transport.send(msg);
      return "SUCCESS";
    } catch (Exception e) {
      e.printStackTrace();
      return "FAIL";
    }
  }

  public String sendMail(String subject, InternetAddress[] to, String msgcontent,
      String filename,
      byte[] files) {
    // System.out.println("host: " + host);
    // System.out.println("onlyHost: " + onlyHost);
    // System.out.println("mailDotHost: " + mailDotHost);
    try {
      Properties properties = new Properties();
      properties.put("mail.smtp.host", "10.194.81.10");
      properties.put("mail.smtp.port", "25");
      properties.put("mail.smtp.auth", true);

      // if (proxySet) {
      properties.put("proxySet", true);
      properties.put("socksProxyHost", "10.194.81.10");
      properties.put("socksProxyPort", "8080");
      // }

      properties.put("mail.smtp.starttls.enable", false);
      properties.put("mail.user", "ncog@digitalindia.gov.in");
      properties.put("mail.password", "Ncog@1948");

      Authenticator auth = new Authenticator() {
        public PasswordAuthentication getPasswordAuthentication() {
          return new PasswordAuthentication(user, password);
        }
      };
      Session session = Session.getInstance(properties, auth);
      Message msg = new MimeMessage(session);
      msg.setFrom(new InternetAddress(user));
      msg.setRecipients(Message.RecipientType.TO, to);
      msg.setSubject(subject);
      msg.setSentDate(new Date());
      Multipart multipart = new MimeMultipart();

      MimeBodyPart messageBodyPart = new MimeBodyPart();
      messageBodyPart.setContent(msgcontent, "text/html");

      if (files != null && files.length > 1) {
        // for (String filepath : files) {
        MimeBodyPart attachmentPart = new MimeBodyPart();
        ByteArrayDataSource bds = new ByteArrayDataSource(files, "application/pdf");
        attachmentPart.setDataHandler(new DataHandler(bds));
        attachmentPart
            .setFileName(
                filename + ".pdf");
        attachmentPart.setHeader("Content-Type", "application/pdf");
        // attachmentPart
        multipart.addBodyPart(attachmentPart);
        // }
      }

      multipart.addBodyPart(messageBodyPart);
      msg.setContent(multipart);
      msg.saveChanges();
      Transport.send(msg);
      return "SUCCESS";
    } catch (Exception e) {
      e.printStackTrace();
      return "FAIL";
    }
  }
}
