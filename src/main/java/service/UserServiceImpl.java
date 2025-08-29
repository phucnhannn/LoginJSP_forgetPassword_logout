package service;

import dao.UserDao;
import dao.UserDaoImpl;
import model.User;

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
}