package org.example.dao;
import org.example.db.DBConnection;
import org.example.model.Enrollment;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EnrollmentDAO {
    public void insertEnrollment(Enrollment enrollment){
        String sql = "INSERT INTO enrollments (learner_id, skill_id) VALUES (?,?) ";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql , Statement.RETURN_GENERATED_KEYS)){
            stmt.setInt(1,enrollment.getLearnerId());
            stmt.setInt(2,enrollment.getSkillId());

            stmt.executeUpdate();
            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next()){
                enrollment.setId(generatedKeys.getInt(1));
            }
            System.out.println("Enrollment created with id: " + enrollment.getId());
        }
        catch (SQLException e) {
            throw new RuntimeException("Failed to insert enrollment: " + e.getMessage(), e);
        }
        }
        public List<Enrollment> getEnrollmentsByLearner(int learnerId){
            String sql =  "SELECT enrollments.*, skills.title AS skill_title " +
                    "FROM enrollments JOIN skills ON enrollments.skill_id = skills.id " +
                    "WHERE enrollments.learner_id = ?";
            List<Enrollment> enrollments = new ArrayList<>();

            try (Connection conn = DBConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)){
                 stmt.setInt(1,learnerId);
                 ResultSet rs = stmt.executeQuery();
                 while (rs.next()){
                     Enrollment e = new Enrollment(
                       rs.getInt("learner_id"),
                       rs.getInt("skill_id")
                     );
                     e.setId(rs.getInt("id"));
                     e.setEnrolledAt(rs.getString("enrolled_at"));
                     e.setSkillTitle(rs.getString("skill_title"));
                     enrollments.add(e);
                 }
            } catch (SQLException e) {
                throw new RuntimeException("Failed to fetch enrollments: " + e.getMessage(), e);
            }

            return enrollments;
            }
    public List<Enrollment> getEnrollmentsBySkill(int skillId){
        String sql = "SELECT enrollments.*, users.name AS learner_name " +
                "FROM enrollments JOIN users ON enrollments.learner_id = users.id " +
                "WHERE enrollments.skill_id = ?";
        List<Enrollment> enrollments = new ArrayList<>();

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){
            stmt.setInt(1,skillId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()){
                Enrollment e = new Enrollment(
                        rs.getInt("learner_id"),
                        rs.getInt("skill_id")
                );
                e.setId(rs.getInt("id"));
                e.setEnrolledAt(rs.getString("enrolled_at"));
                e.setLearnerName(rs.getString("learner_name"));
                enrollments.add(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch enrollments: " + e.getMessage(), e);
        }
        return enrollments;
    }
    public boolean deleteEnrollment(int id){
        String sql = "DELETE FROM enrollments WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,id);
            int rowAffected = stmt.executeUpdate();
            return rowAffected>0;
        }
        catch (SQLException e){
            throw new RuntimeException("Failed to delete enrollment: " + e.getMessage(), e);
        }
    }
        }


