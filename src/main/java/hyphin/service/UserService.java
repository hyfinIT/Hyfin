package hyphin.service;

import hyphin.dto.NewPasswordRequest;
import hyphin.model.PasswordRestoreLink;
import hyphin.model.User;
import hyphin.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.servlet.http.HttpSession;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

@Service
@Slf4j
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    MailService mailService;

    private Timer timer = new Timer();

    private static final SimpleDateFormat jdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    private ConcurrentHashMap<String, PasswordRestoreLink> activeRestoreLinks = new ConcurrentHashMap<>();
    private ConcurrentHashMap<String, PasswordRestoreLink> activeClickedRestoreLinks = new ConcurrentHashMap<>();

    private static final String RESTORE_PASSWORD_EMAIL_SUBJECT = "Hyfin: reset password";
    private static final String RESTORE_PASSWORD_EMAIL_TEXT = "Click here to reset your password: http://hyfin.com/restore-password?hash=";

    PasswordEncoder passwordEncoder = new BCryptPasswordEncoder(8);

    public User findByUserNameAndPassword(String userName, String password) {
        List<User> users = (List<User>) userRepository.findAll();
        for (User user : users) {
            if (user != null && user.getEmail() != null && user.getEmail().equalsIgnoreCase(userName) && user.getPassword() != null &&
                    (user.getPassword().equals(password) || passwordEncoder.matches(password, user.getPassword())))
                return user;
        }
        return null;
    }

    public User save(User user) {
        user.setClientType("MVP");
        if (userRepository.findMax() == 0)
            user.setUid(1);
        else
            user.setUid(userRepository.findMax() + 1);
        user.setDateTime(jdf.format(new Date()));
        user.setActive(true);
        user.setRegion("EMEA");
        user.setPreferenceType("Global");
        user.setDisplayPriority("Default");


        userRepository.saveAndFlush(user);
        return user;
    }


    {
        checkExpired();
    }

    public void checkExpired() {
        timer.schedule(new TimerTask() {
            public void run() {
                log.info("Active restore links count before cleaning: {}", activeRestoreLinks.size());
                for (PasswordRestoreLink passwordRestoreLink : activeRestoreLinks.values()) {
                    if (isTimeOut(passwordRestoreLink)) {
                        activeRestoreLinks.remove(passwordRestoreLink.getHash());
                    }
                }
                log.info("Active restore links count after cleaning: {}", activeRestoreLinks.size());
            }
        }, 0, 1000 * 60 * 60);

        timer.schedule(new TimerTask() {
            public void run() {
                log.info("Active clicked restore links count before cleaning: {}", activeClickedRestoreLinks.size());
                for (PasswordRestoreLink passwordRestoreLink : activeClickedRestoreLinks.values()) {
                    if (isTimeOut(passwordRestoreLink)) {
                        activeClickedRestoreLinks.remove(passwordRestoreLink.getHash());
                    }
                }
                log.info("Active clicked restore links count after cleaning: {}", activeClickedRestoreLinks.size());
            }
        }, 0, 1000 * 60 * 60);
    }

    private boolean isTimeOut(PasswordRestoreLink passwordRestoreLink) {
        return System.currentTimeMillis() - passwordRestoreLink.getTimestamp() > 1000 * 60 * 30;
    }

    public boolean sendRestorePasswordLinkEmail(String email) {
        User userByEmail = userRepository.findByEmail(email);

        if (Objects.nonNull(userByEmail)) {
            String hash = generateUniqueHash();
            PasswordRestoreLink passwordRestoreLink = new PasswordRestoreLink();
            passwordRestoreLink.setUser(userByEmail);
            passwordRestoreLink.setHash(hash);
            passwordRestoreLink.setTimestamp(System.currentTimeMillis());
            activeRestoreLinks.put(hash, passwordRestoreLink);

            try {
                mailService.sendMail(email, RESTORE_PASSWORD_EMAIL_SUBJECT, RESTORE_PASSWORD_EMAIL_TEXT + hash);
            } catch (MessagingException e) {
                log.error("Error while sending password restore email");
                return false;
            }

            return true;
        } else {
            return false;
        }

    }

    public boolean restorePassword(HttpSession session, String hash) {
        log.info("Try to restore by hash {}", hash);
        PasswordRestoreLink passwordRestoreLink = activeRestoreLinks.get(hash);
        if (Objects.isNull(passwordRestoreLink) || isTimeOut(passwordRestoreLink)) {
            log.info("Fail tro restore. No active link by hash {}", hash);
            return false;
        }
        log.info("Success, restore link is active. Hash {}", hash);
        session.setAttribute("reset-password-hash", hash);
        passwordRestoreLink.setTimestamp(System.currentTimeMillis());
        activeClickedRestoreLinks.put(hash, passwordRestoreLink);
        return true;
    }

    public boolean applyNewPassword(HttpSession session, NewPasswordRequest request){
        PasswordRestoreLink passwordRestoreLink = activeClickedRestoreLinks.get((String) session.getAttribute("reset-password-hash"));

        if (!request.getNewPassword().equals(request.getConfirmNewPassword()) || isTimeOut(passwordRestoreLink)){
            return false;
        }

        User user = passwordRestoreLink.getUser();

        if (Objects.nonNull(user)) {
            user.setPassword(request.getNewPassword());
            userRepository.save(user);

            return true;
        }
        return false;
    }

    private String generateUniqueHash() {
        return Long.toHexString(Double.doubleToLongBits(Math.random())) +
                Long.toHexString(Double.doubleToLongBits(Math.random())) +
                Long.toHexString(Double.doubleToLongBits(Math.random()));
    }

}

