
package com.dialca.recommender.controller;

import com.dialca.recommender.dao.UsersDao;
import com.dialca.recommender.model.Users;

public class UserController {
    private final UsersDao userDao = new UsersDao();
    public boolean register(String name, String email, String password) {
        Users user = new Users(name, email, password);
        return userDao.create(user);
    }
    public Users login(String email, String password){
        return userDao.findByEmailAndPassword(email, password);
    }
}
