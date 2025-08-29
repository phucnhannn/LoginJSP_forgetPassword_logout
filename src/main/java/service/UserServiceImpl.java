package service;

import dao.UserDao;
import dao.UserDaoImpl;
import model.User;

import java.time.Instant;
import java.util.UUID;
import java.sql.Timestamp;

public class UserServiceImpl implements UserService {
    UserDao userDao = new UserDaoImpl();

    @Override
    public User login(String username, String password) {
        System.out.println("[DEBUG] Call login with username: " + username + ", password: " + password);
        User user = this.get(username);
        System.out.println("[DEBUG] User object from DB: " + (user == null ? "null" : user.getUserName() + ", pass: " + user.getPassWord()));
        if (user != null) {
            System.out.println("[DEBUG] Password in DB: " + user.getPassWord());
            if (password.equals(user.getPassWord())) {
                System.out.println("[DEBUG] Login success!");
                return user;
            } else {
                System.out.println("[DEBUG] Password mismatch!");
            }
        } else {
            System.out.println("[DEBUG] User not found!");
        }
        return null;
    }

    @Override
    public User get(String username) {
        return userDao.get(username);
    }

    @Override
    public String createResetToken(String usernameOrEmail, Instant expireAt) {
        User u = userDao.getByUsernameOrEmail(usernameOrEmail);
        if (u == null) return null;

        String token = UUID.randomUUID().toString().replace("-", "");
        Timestamp ts = Timestamp.from(expireAt);
        boolean saved = userDao.saveResetToken(u.getId(), token, ts);
        if (!saved) return null;
        return token;
    }

    @Override
    public User getByResetToken(String token) {
        return userDao.getByResetToken(token);
    }

    @Override
    public boolean resetPassword(String token, String newPassword) {
        User u = userDao.getByResetToken(token);
        if (u == null || u.getResetExpiry() == null) {
            return false;
        }
        if (u.getResetExpiry().toInstant().isBefore(Instant.now())) {
            return false;
        }
        boolean ok = userDao.updatePassword(u.getId(), newPassword);
        if (!ok) return false;
        userDao.clearResetToken(u.getId());
        return true;
    }

    @Override
    public User getByUsernameOrEmail(String term) {
        return userDao.getByUsernameOrEmail(term);
    }
}