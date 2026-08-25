package org.example.model;

public class Enrollment {
    private int id;
    private int learnerId;   // Which Person Enrolled (users table ka id)
    private int skillId;  // This Tells us in which skill the Person Enrolled (skills table ka id)
    private String enrolledAt;
    private String skillTitle;
    private String learnerName;

    public Enrollment(int learnerId, int skillId) {
        this.learnerId = learnerId;
        this.skillId = skillId;
    }
    public int getId() {
        return id;
    }

    public String getLearnerName() {
        return learnerName;
    }

    public void setLearnerName(String learnerName) {
        this.learnerName = learnerName;
    }

    public String getSkillTitle() {
        return skillTitle;
    }

    public void setSkillTitle(String skillTitle) {
        this.skillTitle = skillTitle;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLearnerId() {
        return learnerId;
    }

    public void setLearnerId(int learnerId) {
        this.learnerId = learnerId;
    }

    public int getSkillId() {
        return skillId;
    }

    public String getEnrolledAt() {
        return enrolledAt;
    }

    public void setEnrolledAt(String enrolledAt) {
        this.enrolledAt = enrolledAt;
    }

    public void setSkillId(int skillId) {
        this.skillId = skillId;
    }
}
