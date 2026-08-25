package org.example.service;
import org.example.dao.EnrollmentDAO;
import org.example.dao.SkillDAO;
import org.example.dao.UserDAO;
import org.example.model.Enrollment;
import org.example.model.Skill;
import org.example.model.User;
public class EnrollmentService {
    private EnrollmentDAO enrollmentDAO = new EnrollmentDAO();
    private UserDAO userDAO = new UserDAO();
    private SkillDAO skillDAO = new SkillDAO();

    public void enrollLearner(int learnerId, int skillId){
        User user = userDAO.getUserById(learnerId);
        if(user == null){
            throw new RuntimeException("User does not exist , id: " + learnerId);
        }
        if(!user.getRole().equals("LEARNER")){
            throw new RuntimeException("Only Learners can enroll. This User is " + user.getRole());
        }
        Skill skill = skillDAO.searchById(skillId);
        if (skill == null) {
            throw new RuntimeException("This skill does not Exist , id: " + skillId);
        }
        Enrollment enrollment = new Enrollment(learnerId, skillId);
        enrollmentDAO.insertEnrollment(enrollment);

        System.out.println(user.getName() + " Successfully enrolled in " + skill.getTitle());
    }
}
