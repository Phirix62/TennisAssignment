#  Tennis Tournament Management System

This is a full-stack web application for managing tennis tournaments, developed as part of a software design assignment. It supports player registration, tournament creation, match scheduling, and referee assignments.

---

##  Features

###  User Roles
- **Administrator**: Manages user accounts.
- **Tennis Player**:
  - Registers an account
  - Joins tournaments
  - Views their scheduled matches
  - Edits their account information
- **Referee**:
  - Views matches assigned to them
  - Edits their account information

###  Tournament System
- Players can join tournaments.
- Matches are auto-generated in pairs each time a player joins.
- Referees are assigned automatically and fairly to each match.

---

##  Technologies Used

### Backend
- Java 17
- Spring Boot
- Spring Data JPA (Hibernate)
- MySQL
- Lombok

### Frontend
- React (with Vite)
- Axios for API communication
- Vanilla CSS

---

##  Getting Started

### Requirements
- Java 17
- Node.js
- MySQL Server
- Maven

### Setup Instructions

#### Backend
# Navigate to backend folder
cd backend
./mvnw spring-boot:run || or click button run java on any java file

#### Frontend
# Navigate to frontend folder
cd frontend
npm install
npm run dev

## Authors
Nathan (@Phirix62)
Part of Erasmus semester at UTCN, class assignment

