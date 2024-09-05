package com.exercise.Controller;


import com.exercise.Repository.MyUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;



import java.util.UUID;


@Controller

public class ForgotPasswordController {

//    private final JavaMailSender mailSender;
//
//    public ForgotPasswordController(JavaMailSender mailSender) {
//        this.mailSender = mailSender;
//    }
//
//    @PostMapping("/forgot-password")
//    public String processForgotPassword(@RequestParam("email") String email, Model model) {
//        if (!checkIfEmailExists(email)) {
//            model.addAttribute("error", "Email không tồn tại!");
//            return "forgot-password";
//        }
//
//        String token = UUID.randomUUID().toString();
//        try {
//            sendResetPasswordEmail(email, token);
//            model.addAttribute("message", "Đã gửi email reset mật khẩu! Vui lòng kiểm tra hộp thư.");
//        } catch (Exception e) {
//            model.addAttribute("error", "Đã xảy ra lỗi khi gửi email reset mật khẩu.");
//            System.err.println("Error sending email: " + e.getMessage());
//        }
//
//        return "forgot-password";
//    }
//
//    private boolean checkIfEmailExists(String email) {
//        return true; // Giả sử email tồn tại
//    }
//
//    private void sendResetPasswordEmail(String email, String token) throws Exception {
//        String resetPasswordLink = "http://localhost:8080/reset-password?token=" + token;
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(email);
//        message.setSubject("Reset Password");
//        message.setText("Để đặt lại mật khẩu, vui lòng nhấn vào đường dẫn sau: " + resetPasswordLink);
//
//        try {
//            mailSender.send(message);
//        } catch (Exception e) {
//            System.err.println("Error sending email: " + e.getMessage());
//            throw e;
//        }
//    }
}

