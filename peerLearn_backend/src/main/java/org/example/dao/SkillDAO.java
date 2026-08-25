package org.example.dao;

import org.example.db.DBConnection;
import org.example.model.Skill;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SkillDAO {
    public void insertSkill(Skill skill){
        String sql = "INSERT INTO skills (title, category, description, mentor_id) VALUES (?, ?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1,skill.getTitle());
            stmt.setString(2,skill.getCategory());
            stmt.setString(3,skill.getDescription());
            stmt.setInt(4,skill.getMentorId());

            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next()){
                skill.setId(generatedKeys.getInt(1));
            }
            System.out.println("Skill inserted with id: " + skill.getId());

        } catch (SQLException e) {
            throw new RuntimeException("Failed to insert skill: " + e.getMessage(), e);
        }
        }
        // Read from MySQL
        public List<Skill> getAllSkills(){
            String sql = "SELECT skills.*, users.name AS mentor_name " +
                    "FROM skills JOIN users ON skills.mentor_id = users.id";
            List<Skill> skills = new ArrayList<>();

            try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql);
                ResultSet rs = stmt.executeQuery()) {

                while (rs.next()){
                    Skill skill = new Skill(
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getInt("mentor_id")
                    );
                    skill.setId(rs.getInt("id"));
                    skill.setMentorName(rs.getString("mentor_name"));
                    skills.add(skill);
                }
            }
            catch (SQLException e){
                throw new RuntimeException("Failed to fetch skills: " + e.getMessage(), e);
            }
            return skills;
        }
        public List<Skill> searchByTitle(String keyword){
            String sql =  "SELECT skills.*, users.name AS mentor_name " +
                    "FROM skills JOIN users ON skills.mentor_id = users.id " +
                    "WHERE skills.title LIKE ?";
            List<Skill> skills = new ArrayList<>();

            try (Connection conn = DBConnection.getConnection();
                PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setString(1,"%" + keyword + "%");
                ResultSet rs = stmt.executeQuery();
                while (rs.next()){
                    Skill skill = new Skill(
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getInt("mentor_id")
                    );
                    skill.setId(rs.getInt("id"));
                    skills.add(skill);
                }
            }
            catch (SQLException e){
                throw new RuntimeException("Failed to search skills: " + e.getMessage(), e);
            }
            return skills;
        }
        public Skill searchById(int id){
            String sql = "SELECT * FROM skills WHERE id = ?";
            List<Skill> skills = new ArrayList<>();
            try(Connection conn = DBConnection.getConnection();
               PreparedStatement stmt = conn.prepareStatement(sql)){
                stmt.setInt(1,id);
                ResultSet rs  =stmt.executeQuery();
                while (rs.next()){
                    Skill skill = new Skill(
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getString("description"),
                            rs.getInt("mentor_id")
                    );
                    skill.setId(rs.getInt("id"));
                    return skill;
                }
                return null;
                }
            catch (SQLException e){
                throw new RuntimeException("Failed to fetch skill: " + e.getMessage(), e);
            }
        }
    public boolean updateSkill(int id, Skill skill) {
        String sql = "UPDATE skills SET title=?, category=?, description=? WHERE id =?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setString(1, skill.getTitle());
            stmt.setString(2, skill.getCategory());
            stmt.setString(3, skill.getDescription());
            stmt.setInt(4, id);

            int rowAffected = stmt.executeUpdate();
            return rowAffected>0;
        }
        catch (SQLException e){
            throw new RuntimeException("Failed to update skill: " + e.getMessage(), e);
        }
    }
    public boolean deleteSkill(int id) {
        String sql = "DELETE FROM skills WHERE id =?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1, id);

            int rowAffected = stmt.executeUpdate();
            return rowAffected>0;
        }
        catch (SQLException e){
            throw new RuntimeException("Failed to delete skill: " + e.getMessage(), e);
        }
    }
    }