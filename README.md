# PeerLearn — Skill Exchange Platform

A peer-to-peer learning platform where students teach and learn skills from each other. No fees, no ads — just campus-based knowledge sharing between Mentors and Learners.

---

## 🎯 Overview

PeerLearn connects **Mentors** (students who want to teach a skill) with **Learners** (students who want to learn one). Mentors post skills they can teach; Learners browse, search, and enroll in the ones they're interested in.

Built as a full-stack learning project to practice **Java OOP, REST API design, SQL, and vanilla JavaScript** — end to end, from database schema to a working UI.

---

## ✨ Features

**For Everyone**
- User registration with role selection (Mentor / Learner)
- Simple ID-based sign-in and logout
- Browse all posted skills
- Search skills by title
  
**For Mentors**
- Post a new skill (title, category, description)
- Edit an existing skill
- View all skills they've posted (**My Skills**)
- View which learners have enrolled in each skill

**For Learners**
- Enroll in a skill
- Unenroll from a skill
- View all their enrollments with enrollment date (**My Enrollments**)

**Backend Engineering**
- Role-based validation (only Mentors can post skills, only Learners can enroll)
- SQL `JOIN` queries used so the frontend receives mentor names and skill titles directly — avoiding the N+1 query problem
- Centralized exception handling — validation errors return clean JSON responses instead of server crashes

---

## 🛠️ Tech Stack

| Layer | Technology |
|---|---|
| Backend | Java, Javalin (REST API framework), JDBC |
| Database | MySQL |
| Frontend | HTML, CSS, Vanilla JavaScript (Fetch API, async/await) |
| Architecture | Layered: Model → DAO → Service → Controller |

---

## 🏗️ Project Structure
PeerLearn/
├── backend/
│ ├── pom.xml
│ ├── database/
│ │ └── schema.sql
│ └── src/main/java/org/example/
│ ├── model/ # User (abstract), Mentor, Learner, Skill, Enrollment
│ ├── dao/ # Database access layer (JDBC)
│ ├── service/ # Business logic & validation
│ ├── db/ # DBConnection
│ └── Main.java # Javalin API routes
│
└── frontend/
├── index.html # Browse & search skills
├── register.html # Sign up
├── post-skill.html # Post / edit a skill
├── my-skills.html # Mentor's posted skills
├── my-enrollments.html # Learner's enrollments
├── style.css
└── script.js

## 🧩 Object-Oriented Design

The backend models the Mentor/Learner relationship using core OOP principles:

- **Abstraction** — `User` is an abstract class defining the shared contract (`getRole()`)
- **Inheritance** — `Mentor` and `Learner` both extend `User`
- **Polymorphism** — calling `getRole()` on a `User` reference returns different results depending on whether the actual object is a `Mentor` or `Learner`
- **Encapsulation** — all model fields are private/protected, accessed only through getters and setters

---

## 🔌 API Endpoints

| Method | Endpoint | Description |
|---|---|---|
| GET | `/skills` | Get all skills |
| GET | `/skills/search?title=` | Search skills by title |
| GET | `/skills/{id}` | Get a single skill |
| POST | `/skills` | Post a new skill *(Mentor only)* |
| PUT | `/skills/{id}` | Update a skill |
| POST | `/users` | Register a new user |
| GET | `/users/{id}` | Get user by ID |
| POST | `/enrollments` | Enroll in a skill *(Learner only)* |
| DELETE | `/enrollments/{id}` | Unenroll from a skill |
| GET | `/enrollments/learner/{id}` | Get a learner's enrollments |
| GET | `/enrollments/skill/{id}` | Get learners enrolled in a skill |


## 🔮 Future Improvements

- Password-based authentication (currently uses simplified ID-based sign-in for learning-focused simplicity)
- A dedicated skill detail page
- Editable user profiles

---

## 👨‍💻 Author

Built by Sanjay Kumar  — a student project to practice full-stack development: Java backend design, REST APIs, SQL, and frontend integration with vanilla JavaScript.
