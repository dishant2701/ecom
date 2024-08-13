package com.Ecomm.Ecomm.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.text.ParseException;
import java.util.concurrent.TimeUnit;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class OtpLimiter {

  private static final int OTP_COUNT_LIMIT = 3;
  private static final int OTP_RESET_DURATION_MINS = 24 * 60;

  @Autowired
  private ApplicationContext appContext;

  public boolean shouldSendOtp(String mobile, String otp) {
    DataSource ds = (DataSource) appContext.getBean("dataSource");

    try (Connection conn = ds.getConnection()) {
      conn.setAutoCommit(false);
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT * FROM register_otps WHERE mobile_no = ? FOR UPDATE",
          ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stmt.setString(1, mobile);
      ResultSet results = stmt.executeQuery();
      if (!results.next()) {
        return false;
      }

      String otpFromDb = results.getString("otp");
      int otpCount = results.getInt("otp_count");
      Long otpTimestamp = results.getLong("otp_timestamp");

      // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
      if (otpTimestamp == null
          || minutesElapsedSince(otpTimestamp) >= OTP_RESET_DURATION_MINS) {
        results.updateString("otp", otp);
        results.updateInt("otp_count", 1);
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateLong("otp_timestamp", System.currentTimeMillis());
      } else if (otpCount < OTP_COUNT_LIMIT) {
        results.updateString("otp", otp);
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateInt("otp_count", otpCount + 1);
      } else {
        System.out.println("most recent OTP: " + otpFromDb);

        conn.rollback();
        return false;
      }

      results.updateRow();
      conn.commit();
      return true;
    } catch (Exception ex) {
      System.out.println("--- Caught error in OTP limiter ---");
      ex.printStackTrace();
      System.out.println("--- x ---");
      return false;
    }

  }

  public boolean shouldSendAlternateOtp(String alternateMobile, String alternateOtp) {
    DataSource ds = (DataSource) appContext.getBean("dataSource");

    try (Connection conn = ds.getConnection()) {
      conn.setAutoCommit(false);
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT * FROM young_professional WHERE alternate_mobile = ? FOR UPDATE",
          ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stmt.setString(1, alternateMobile);
      ResultSet results = stmt.executeQuery();
      if (!results.next()) {
        return false;
      }

      String otpFromDb = results.getString("alternate_otp");
      int otpCount = results.getInt("alternate_otp_count");
      Long otpTimestamp = results.getLong("alternate_otp_timestamp");

      // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
      if (otpTimestamp == null
          || minutesElapsedSince(otpTimestamp) >= OTP_RESET_DURATION_MINS) {
        results.updateString("alternate_otp", alternateOtp);
        results.updateInt("alternate_otp_count", 1);
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateLong("alternate_otp_timestamp", System.currentTimeMillis());
      } else if (otpCount < OTP_COUNT_LIMIT) {
        results.updateString("alternate_otp", alternateOtp);
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateInt("alternate_otp_count", otpCount + 1);
      } else {
        System.out.println("most recent OTP: " + otpFromDb);

        conn.rollback();
        return false;
      }

      results.updateRow();
      conn.commit();
      return true;
    } catch (Exception ex) {
      System.out.println("--- Caught error in OTP limiter ---");
      ex.printStackTrace();
      System.out.println("--- x ---");
      return false;
    }

  }

  public boolean shouldSendOtpEmail(String email, String otp) {
    DataSource ds = (DataSource) appContext.getBean("dataSource");

    try (Connection conn = ds.getConnection()) {
      conn.setAutoCommit(false);
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT * FROM register_otps WHERE email = ? FOR UPDATE",
          ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stmt.setString(1, email);
      ResultSet results = stmt.executeQuery();
      if (!results.next()) {
        return false;
      }

      String otpFromDb = results.getString("otp_email");
      int otpCount = results.getInt("otp_count_email");
      Long otpTimestamp = results.getLong("otp_timestamp_email");

      // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
      if (otpTimestamp == null
          || minutesElapsedSince(otpTimestamp) >= OTP_RESET_DURATION_MINS) {
        results.updateString("otp_email", otp);
        results.updateInt("otp_count_email", 1);
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateLong("otp_timestamp_email", System.currentTimeMillis());
      } else if (otpCount < OTP_COUNT_LIMIT) {
        results.updateString("otp_email", otp);
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateInt("otp_count_email", otpCount + 1);
      } else {
        System.out.println("most recent OTP: " + otpFromDb);

        conn.rollback();
        return false;
      }

      results.updateRow();
      conn.commit();
      return true;
    } catch (Exception ex) {
      System.out.println("--- Caught error in OTP limiter ---");
      ex.printStackTrace();
      System.out.println("--- x ---");
      return false;
    }

  }

  public boolean shouldSendFinalOtp(String mobile, String otp) {
    DataSource ds = (DataSource) appContext.getBean("dataSource");

    try (Connection conn = ds.getConnection()) {
      conn.setAutoCommit(false);
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT * FROM young_professional WHERE mobile = ? FOR UPDATE",
          ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stmt.setString(1, mobile);
      ResultSet results = stmt.executeQuery();
      if (!results.next()) {
        return false;
      }

      String otpFromDb = results.getString("otp_final_verification");
      int otpCount = results.getInt("otp_count_final_verification");
      Long otpTimestamp = results.getLong("otp_timestamp_final_verification");

      // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
      if (otpTimestamp == null
          || minutesElapsedSince(otpTimestamp) >= OTP_RESET_DURATION_MINS) {
        results.updateString("otp_final_verification", otp);
        results.updateInt("otp_count_final_verification", 1);
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateLong("otp_timestamp_final_verification",
            System.currentTimeMillis());
      } else if (otpCount < OTP_COUNT_LIMIT) {
        results.updateString("otp_final_verification", otp);
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateInt("otp_count_final_verification", otpCount + 1);
      } else {
        System.out.println("most recent OTP: " + otpFromDb);

        conn.rollback();
        return false;
      }

      results.updateRow();
      conn.commit();
      return true;
    } catch (Exception ex) {
      System.out.println("--- Caught error in OTP limiter ---");
      ex.printStackTrace();
      System.out.println("--- x ---");
      return false;
    }

  }

  private static long minutesElapsedSince(Long timestamp) throws ParseException {
    return TimeUnit.MILLISECONDS.toMinutes(System.currentTimeMillis() - timestamp);
  }

  private static final int OTP_COUNT_LIMIT_FORGOT_PASS = 3;
  private static final int OTP_RESET_DURATION_MINS_FORGOT_PASS = 24 * 60;

  public boolean shouldForgotPasswordOtp(String mobile, String otp) {
    DataSource ds = (DataSource) appContext.getBean("dataSource");

    try (Connection conn = ds.getConnection()) {
      conn.setAutoCommit(false);
      PreparedStatement stmt = conn.prepareStatement(
          "SELECT * FROM users WHERE username = ? FOR UPDATE",
          ResultSet.TYPE_SCROLL_SENSITIVE, ResultSet.CONCUR_UPDATABLE);
      stmt.setString(1, mobile);
      ResultSet results = stmt.executeQuery();
      if (!results.next()) {
        return false;
      }

      String otpFromDb = results.getString("forgot_otp");
      Integer otpCount = results.getInt("forgot_otp_count");
      Long otpTimestamp = results.getLong("forgot_otp_timestamp");
      System.out.println("otpFromDb: " + otpFromDb);
      System.out.println("otpCount: " + otpCount);
      System.out.println("otpTimestamp: " + otpTimestamp);

      // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
      if (otpTimestamp == null
          || minutesElapsedSince(otpTimestamp) >= OTP_RESET_DURATION_MINS_FORGOT_PASS) {
        results.updateInt("forgot_otp_count", 1);
        results.updateLong("forgot_otp_timestamp", System.currentTimeMillis());
        results.updateString("forgot_otp", otp);
        System.out.println("first cond");
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
      } else if (otpCount < OTP_COUNT_LIMIT_FORGOT_PASS) {
        results.updateString("forgot_otp", otp);
        System.out.println("second cond");
        // results.updateString("password", (new BCryptPasswordEncoder()).encode(otp));
        // results.updateString("username", mobile);
        results.updateInt("forgot_otp_count", otpCount + 1);
      } else {
        System.out.println("most recent OTP: " + otpFromDb);

        conn.rollback();
        return false;
      }

      results.updateRow();
      conn.commit();
      System.out.println("committed");
      return true;
    } catch (Exception ex) {
      System.out.println("--- Caught error in OTP limiter ---");
      ex.printStackTrace();
      System.out.println("--- x ---");
      return false;
    }

  }
}
