package org.example.service;

import org.example.dao.UserDAO;
import org.example.model.Mentor;
import org.example.model.Learner;
import org.example.model.User;


public class UserService {
    private UserDAO userDAO = new UserDAO();
    public User registerUser(String name, String email, String role){
        if(name == null || name.trim().isEmpty()){
            throw new RuntimeException("Name Should not be Empty");
        }
        if (email == null || !email.contains("@") || !email.contains(".")) {
            throw new RuntimeException("Email format is Wrong");
        }
        if (!role.equals("MENTOR") && !role.equals("LEARNER")) {
            throw new RuntimeException("Role can be MENTOR or LEARNER");
        }
        User user;
        if (role.equals("MENTOR")) {
            user = new Mentor(name, email);
        } else {
            user = new Learner(name, email);
        }
        userDAO.insertUser(user);
        System.out.println(role + " successfully registered: " + name);
        return user;
    }
}
