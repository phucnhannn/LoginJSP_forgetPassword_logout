package dao;

import model.User;

public interface UserDao {
    User get(String username);
}
