package org.example.dao;
import org.example.db.DBConnection;
import org.example.model.User;
import org.example.model.Mentor;
import org.example.model.Learner;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class UserDAO {
    public void insertUser(User user){
        String sql = "INSERT INTO users (name, email, role) VALUES (?, ?, ?)";
        try(Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){

            stmt.setString(1,user.getName());
            stmt.setString(2,user.getEmail());
            stmt.setString(3,user.getRole());
            stmt.executeUpdate();

            ResultSet generatedKeys = stmt.getGeneratedKeys();
            if(generatedKeys.next()){
                user.setId(generatedKeys.getInt(1));
            }
            System.out.println("User inserted with id: " + user.getId());
        }
        catch (SQLException e){
            throw new RuntimeException("Failed to insert user: " + e.getMessage(), e);
        }
    }
    public User getUserById(int id){
        String sql = "SELECT * FROM users WHERE id = ?";

        try (Connection conn = DBConnection.getConnection();
            PreparedStatement stmt = conn.prepareStatement(sql)){

            stmt.setInt(1,id);
            ResultSet rs = stmt.executeQuery();
            if(rs.next()){
                String role = rs.getString("role");
                User user;
                if(role.equals("MENTOR")){
                    user = new Mentor(rs.getString("name"), rs.getString("email"));
                }
                else {
                    user = new Learner(rs.getString("name"), rs.getString("email"));
                }
                user.setId(rs.getInt("id"));
                return user;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Failed to fetch user: " + e.getMessage(), e);
        }
        }
    }

