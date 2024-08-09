package com.Ecomm.Ecomm.services;

import java.awt.AlphaComposite;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.text.DecimalFormat;
import java.util.Random;

import org.apache.commons.lang3.StringUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.Ecomm.Ecomm.exceptions.BadRequestException;
import com.Ecomm.Ecomm.models.User;
import com.Ecomm.Ecomm.repositories.UserRepo;
import com.Ecomm.Ecomm.security.jwt.Jwt;
import com.Ecomm.Ecomm.security.jwt.JwtActions;
import com.Ecomm.Ecomm.utils.OtpLimiter;
import com.Ecomm.Ecomm.utils.SmsThread;

import kotlin.text.Regex;

@Service
public class UserService implements UserDetailsService {

  @Autowired
  @Lazy
  AuthenticationManager authenticationManager;

  @Autowired
  BCryptPasswordEncoder encoder;

  @Autowired
  UserRepo userRepo;

  @Autowired
  JwtActions jwtActions;

  @Autowired
  JdbcTemplate jdbcTemplate;

  @Value("${spring.profiles.active}")
  private String activeProfile;
  @Autowired
  OtpLimiter otpLimiter;

  @Autowired
  SmsThread smsThread;

  // @Autowired
  // OtpLimiter otpLimiter;

  static final String USERNAME_REGEX = "^(?=.{3,20}$)(?![_.])(?!.*[_.]{2})[a-zA-Z0-9._]+(?<![_.])$";
  static final String OTP_REGEX = "^[0-9]{6}$";
  static final String CAPTCHA_REGEX = "^[0-9a-zA-Z]{6}$";
  static final String CAPTCHA_ALPHABET = "123456789ABCDEFGHJKLMNPQRSTUVWXYZabcdefghijkmnopqrstuvwxyz";

  public void validateUsername(String username) throws Exception {
    if (StringUtils.isBlank(username) || !isValidRegex(username, USERNAME_REGEX)) {
      throw new BadRequestException("Invalid username");
    }
  }

  public void validatePassword(String password) throws Exception {
    if (StringUtils.isBlank(password) || password.length() < 8) {
      throw new BadRequestException("Invalid password");
    }
  }

  public boolean isValidRegex(String inputString, String pattern) {
    var regex = new Regex(pattern);
    return regex.matches(inputString);
  }

  public User authenticateUser(String username, String password)
      throws Exception {

    Authentication authentication = null;
    try {

      authentication = authenticationManager
          .authenticate(new UsernamePasswordAuthenticationToken(username, password));

      SecurityContextHolder.getContext().setAuthentication(authentication);
      return userRepo.findFirstByUsername(username);
    } catch (Exception ex) {
      ex.printStackTrace();
      throw new BadRequestException("Invalid username or password");
    }
  }

  // public void verifyOtp(String otp, User user) throws Exception {
  // if (StringUtils.isBlank(otp) || !isValidRegex(otp, OTP_REGEX)) {
  // throw new BadRequestException("Invalid OTP");
  // }
  // if (!user.getOtp().equals(otp)) {
  // throw new BadRequestException("Incorrect OTP");
  // }
  // }

  public void verifyCaptcha(String captcha, User user) throws Exception {
    if (StringUtils.isBlank(captcha) || !isValidRegex(captcha, CAPTCHA_REGEX)) {
      throw new BadRequestException("Invalid captcha");
    }

  }

  public String assignNewCaptcha(User user) {
    String captcha = activeProfile.equals("dev") ? generateCaptcha(6) : generateCaptcha(6);
    // generateCaptcha(6);
    jdbcTemplate
        .update("UPDATE users SET captcha = ? WHERE id = ?", captcha, user.getId());
    return captcha;
  }

  public String generateJwt(User user, String fp) {
    long now = System.currentTimeMillis();
    String jwt = jwtActions.generate(new Jwt(user.getUsername(), fp, now));
    jdbcTemplate.update("""
        UPDATE users SET jwt_timestamp = ? WHERE id = ?
        """, now, user.getId());
    return jwt;
  }

  private String generateCaptcha(int captchaLength) {
    StringBuffer captchaBuffer = new StringBuffer();
    Random random = new Random();
    while (captchaBuffer.length() < captchaLength) {
      int index = random.nextInt(CAPTCHA_ALPHABET.length());
      captchaBuffer.append(CAPTCHA_ALPHABET.charAt(index));
    }
    return captchaBuffer.toString();
  }

  public BufferedImage makeCaptchaImage(String captcha) {
    int width = 200, height = 60;
    var image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
    Graphics2D graphics = image.createGraphics();
    graphics.setRenderingHint(
        RenderingHints.KEY_TEXT_ANTIALIASING,
        RenderingHints.VALUE_TEXT_ANTIALIAS_GASP);
    graphics.setComposite(AlphaComposite.Clear);
    graphics.fillRect(0, 0, width, height);
    graphics.setComposite(AlphaComposite.Src);
    graphics.setFont(FONT);
    graphics.setColor(TEXT_COLOR);
    FontMetrics metrics = graphics.getFontMetrics(FONT);
    int x = (width - metrics.stringWidth(captcha)) / 2;
    int y = ((height - metrics.getHeight()) / 2) + metrics.getAscent() - 4;
    graphics.drawString(captcha, x, y);
    return image;
  }

  private static final Font FONT = new Font(Font.MONOSPACED, Font.BOLD | Font.ITALIC, 50);
  private static final Color TEXT_COLOR = new Color(255, 255, 255);

  @Override
  public UserDetails loadUserByUsername(String username)
      throws UsernameNotFoundException {
    User user = userRepo.findByUsername(username);
    if (user == null) {
      throw new UsernameNotFoundException("User not found.");
    }
    return (UserDetails) user;
  }

  public String changePassword(String username, String newPassword) {
    User user = userRepo.findFirstByUsername(username);
    if (user != null) {
      user.setPassword(newPassword);
      userRepo.save(user);
      return "Password changed";
    } else {
      return "User not found";
    }
  }

  public String generateOtp() {
    return activeProfile.equals("dev")
        ? "000000"
        : new DecimalFormat("000000").format(new Random().nextInt(999999));

  }

  public void verifyOtp(String otp, User user) throws Exception {
    if (StringUtils.isBlank(otp) || !isValidRegex(otp, OTP_REGEX)) {
      throw new BadRequestException("Invalid OTP");
    }
    if (!user.getOtp().equals(otp)) {
      throw new BadRequestException("Incorrect OTP");
    }
  }

  public String createRegisterCapcha(int capchaLength) {
    String captcha = activeProfile.equals("dev") ? "capcha" : generateCaptcha(capchaLength);
    return captcha;
  }
}
