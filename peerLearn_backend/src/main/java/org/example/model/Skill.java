package org.example.model;

public class Skill {
    private int id;
    private String title;
    private String category;
    private String description;
    private int mentorId;
    private String mentorName;

    public Skill(String title,String category, String description, int mentorId){
        this.title = title;
        this.category= category;
        this.description = description;
        this.mentorId = mentorId;
    }
    public void setId(int id) {
        this.id = id;
    }
    public int getId() {
        return id;
    }
    public void setCategory(String category) {
        this.category = category;
    }
    public String getCategory() {
        return category;
    }
    public void setDescription(String description) {
        this.description = description;
    }
    public String getDescription() {
        return description;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public String getTitle() {
        return title;
    }

    public String getMentorName() {
        return mentorName;
    }

    public void setMentorName(String mentorName) {
        this.mentorName = mentorName;
    }

    public void setMentorId(int mentorId) {
        this.mentorId = mentorId;
    }
    public int getMentorId() {
        return mentorId;
    }
}
