package service;

import model.User;

import java.time.Instant;

public interface UserService {
    User login(String username, String password);
    User get(String username);

    // Forgot password
    String createResetToken(String usernameOrEmail, Instant expireAt);
    User getByResetToken(String token);
    boolean resetPassword(String token, String newPassword);

    // NEW: để lấy email người dùng khi nhập username hoặc email
    User getByUsernameOrEmail(String term);
}