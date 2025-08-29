package dao;

import model.User;

import java.sql.Timestamp;

public interface UserDao {
    User get(String username);

    // New for forgot password
    User getByUsernameOrEmail(String term);
    User getByResetToken(String token);
    boolean saveResetToken(int userId, String token, Timestamp expiry);
    boolean clearResetToken(int userId);
    boolean updatePassword(int userId, String newPassword);
}