package org.example.model;

public abstract class User {
    protected int id;
    protected String name;
    protected String email;

    public User( String name , String email){
        this.name = name;
        this.email = email;
    }
    public abstract String getRole();

    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getName() {
        return name;
    }
    public void setEmail(String email) {
        this.email = email;
    }
    public String getEmail() {
        return email;
    }
}
