package org.example;
import io.javalin.Javalin;
import io.javalin.plugin.bundled.CorsContainer;
import org.example.dao.SkillDAO;
import org.example.model.Enrollment;
import org.example.model.User;
import org.example.service.EnrollmentService;
import org.example.service.SkillService;
import org.example.model.Skill;
import org.example.dao.UserDAO;
import org.example.dao.EnrollmentDAO;
import org.example.service.UserService;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public class Main {
    public static void main(String[] args) {
        int port = Integer.parseInt(System.getenv().getOrDefault("PORT", "7000"));
        Javalin app = Javalin.create(Config -> {
                Config.plugins.enableCors(Cors ->{
                    Cors.add(it ->{
                        it.anyHost();
                    });
                });
        }).start(port);
        SkillDAO skillDAO = new SkillDAO();
        SkillService skillService = new SkillService();
        UserService userService = new UserService();
        EnrollmentService enrollmentService = new EnrollmentService();
        UserDAO userDAO = new UserDAO();
        EnrollmentDAO enrollmentDAO = new EnrollmentDAO();


        System.out.println("🚀 Server is Running : http://localhost:7000");
        // Get all Skills
        app.get("/skills",ctx->{
            List<Skill> skills = skillDAO.getAllSkills();
            ctx.json(skills);
        });
        // Post a Skill
        app.post("/skills",ctx->{
            Map<String , Object> body = ctx.bodyAsClass(Map.class);
            String title = (String) body.get("title");
            String category = (String) body.get("category");
            String description = (String) body.get("description");
            int mentorId = (Integer) body.get("mentorId");

            skillService.postSkill(title,category,description,mentorId);
            ctx.status(201).result("Skill created successfully!");
        });
        app.get("/skills/search", ctx -> {
            String keyword = ctx.queryParam("title");
            List<Skill> results = skillDAO.searchByTitle(keyword);
            ctx.json(results);
        });
        // GET Skill by ID
        app.get("/skills/{id}",ctx->{
            int id = Integer.parseInt(ctx.pathParam("id"));
            Skill skill = skillDAO.searchById(id);

            if(skill==null){
                ctx.status(404).result("Skill not found");
            }
            else {
                ctx.json(skill);
            }
        });
        // New User Register
        app.post("/users",ctx->{
           Map<String,Object> body = ctx.bodyAsClass(Map.class);
            String name = (String) body.get("name");
            String email = (String) body.get("email");
            String role = (String) body.get("role");
            User newUser = userService.registerUser(name,email,role);


            ctx.status(201).json(newUser);
        });
        // Enroll Learner
        app.post("/enrollments",ctx->{
            Map<String , Object> body = ctx.bodyAsClass(Map.class);
            int learnerId = (Integer) body.get("learnerId");
            int skillId = (Integer) body.get("skillId");
            enrollmentService.enrollLearner(learnerId,skillId);
            ctx.status(201).result("Enrolled successfully!");
        });
        // Update Skills
        app.put("/skills/{id}", ctx->{
           int id  = Integer.parseInt(ctx.pathParam("id"));

           Map<String , Object> body = ctx.bodyAsClass(Map.class);
            String title = (String) body.get("title");
            String category = (String) body.get("category");
            String description = (String) body.get("description");

            Skill updatedSkill = new Skill(title,category,description,0);
            boolean success = skillDAO.updateSkill(id, updatedSkill);

            if(success){
                ctx.result("Skill updated successfully!");
            }
            else {
                ctx.status(404).result("Skill not found");
            }
        });
        // Delete a Skill
        app.delete("/skills/{id}",ctx->{
           int id = Integer.parseInt(ctx.pathParam("id"));
            boolean success = skillDAO.deleteSkill(id);

            if (success) {
                ctx.result("Skill deleted successfully!");
            } else {
                ctx.status(404).result("Skill not found");
            }
            });
        app.get("/users/{id}",ctx->{
           int id = Integer.parseInt(ctx.pathParam("id"));
            User user = userDAO.getUserById(id);
            if(user==null){
                ctx.status(404).result("User not found");
            }
            else {
                ctx.json(user);
            }
        });
        app.get("/enrollments/learner/{learnerId}", ctx -> {
        int learnerId = Integer.parseInt(ctx.pathParam("learnerId"));
        List<Enrollment> enrollments  = enrollmentDAO.getEnrollmentsByLearner(learnerId);
            ctx.json(enrollments);
        });
        app.get("/enrollments/skill/{skillId}", ctx -> {
            int skillId = Integer.parseInt(ctx.pathParam("skillId"));
            List<Enrollment> enrollments = enrollmentDAO.getEnrollmentsBySkill(skillId);
            ctx.json(enrollments);
        });
        // Delete Enrollment
        app.delete("/enrollments/{id}", ctx -> {
          int id = Integer.parseInt(ctx.pathParam("id"));
          boolean success = enrollmentDAO.deleteEnrollment(id);
          if(success){
              ctx.result("Enrollment deleted successfully!");
          }
          else {
              ctx.status(404).result("Enrollment not found");
          }
        });
        app.exception(RuntimeException.class , (e,ctx)->{
            ctx.status(400).json(Map.of("error", e.getMessage()));
        });
    }
}