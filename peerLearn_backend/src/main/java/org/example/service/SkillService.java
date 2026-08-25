package org.example.service;
import org.example.dao.SkillDAO;
import org.example.dao.UserDAO;
import org.example.model.Skill;
import org.example.model.User;


public class SkillService {

    SkillDAO skillDAO = new SkillDAO();
    UserDAO userDAO = new UserDAO();

    public void postSkill(String title, String category, String description, int mentorId) {
        if(title==null || title.trim().length()<5){
            throw new RuntimeException("Title Minimum Length is 5");
        }
        User user =  userDAO.getUserById(mentorId);
        if(user == null){
            throw new RuntimeException("User does not  exist.  id: " + mentorId);
        }
        if (!user.getRole().equals("MENTOR")) {
            throw new RuntimeException("Only Mentor can Post Skills. This user is  " + user.getRole());
        }
        Skill skill = new Skill(title,category,description,mentorId);
        skillDAO.insertSkill(skill);
        System.out.println("Skill posted by " + user.getName() + ": " + title);
    }
}
