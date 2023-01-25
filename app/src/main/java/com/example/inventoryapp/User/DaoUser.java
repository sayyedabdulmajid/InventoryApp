package com.example.inventoryapp.User;

import java.util.Set;

public interface DaoUser {
    User getUser(int id) ;
    Set<User> getAllUsers();
    User getUserByUserNameAndPassword(String user, String pass);
    /*
    boolean insertUser(User user);
    boolean updateUser(User user);
    boolean deleteUser(int id);
    */
}
