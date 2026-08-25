package org.example.model;

public class Mentor extends User {
    public Mentor(String name, String email){
        super(name,email);
    }

    @Override
    public String getRole() {
        return "MENTOR";
    }
}
