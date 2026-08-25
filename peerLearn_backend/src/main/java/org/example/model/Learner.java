package org.example.model;

public class Learner extends User{
    public Learner(String name, String email) {
        super(name, email);
    }

    @Override
    public String getRole() {
        return "LEARNER";
    }
}
