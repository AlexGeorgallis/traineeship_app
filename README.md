# Traineeship Management System (Re-engineered)

A Spring Boot web application designed to manage the complete lifecycle of university traineeships.
The project is a **re-engineering** of a legacy system, focusing on architectural restructuring, code decoupling 
and the implementation of EAA patterns to improve maintainability and extensibility. 

# Project Overview

The application facilitates collaboration between **Students**, **Companies**, **Professors** and the **Traineeship Committee**.
It handles position announcements, applications, algorithm-based matching, supervision and final evaluations. 

## Key Refactoring Achievements

The primary goal was to modernize the legacy codebase by addressing specific "code smells" and architectural flaws.

1. **Deconstruction of the "God Class"**:

**The Problem**: The legacy `TraineeshipAppController` was a monolithic "God Class" responsible for the entire application. 
It handled HTTP requests for all the entities simultaneously, while also containing business logic and direct database queries.

<img width="687" height="357" alt="god-class" src="https://github.com/user-attachments/assets/4d0148da-c114-4fe7-ae00-a7ed17ac61c1" />

**The solution**: The application of Single Responsibility Principle by splitting the functionality into four distinct controllers.

<img width="687" height="357" alt="refactored1" src="https://github.com/user-attachments/assets/b615d76f-f52b-43cc-be43-4e5199337fca" />

2. **Service Layer Extraction**:
   * **The Problem**: In the legacy version, business rules were mixed directly with UI logic inside the controller.
  
   * **The Solution**: The introduction of a strict **Service Layer**.
     * **Controllers** now strictly handle HTTP requests and view navigation.
     * **Services** contain all business logic and transaction management. 

3. **Design Pattern Implementation (Strategy & Template Method)**:
   * **Strategy Pattern**: Maintained and refined the use of interchangeable algorithms for **Traineeship Search** (Interests vs Location) and
     **Supervisor Assignment** (Expertise vs Workload).
   * **Template Method Pattern**:
       * **The Problem**: The legacy strategies contained significant code duplication, repeatedly fetching entities in every strategy class.
        <img width="715" height="367" alt="duplicate" src="https://github.com/user-attachments/assets/0295fe64-b0a9-4abe-8e05-d394f4a89107" />

       * **The Solution**: 
         * Created an Abstract Base Class to define the skeleton of the algorithm.
         * Common steps are handled in the base class. 
         <img width="1150" height="298" alt="ref1" src="https://github.com/user-attachments/assets/8eef3cb5-e1bc-409a-b874-5b4708fd6235" />
         <img width="815" height="298" alt="ref2" src="https://github.com/user-attachments/assets/58d0b45b-032e-4a53-9c08-0a85dcb3e005" />

## Tech Stack 

* **Language**: Java 17
* **Framework**: Spring Boot (Spring MVC, Spring Security, Spring Data JPA)
* **Template** Engine: Thymeleaf
* **Database**: MySQL
* **Build** **tool**: Maven
* **Testing**: JUnit 5 & Mockito

## Functional Modules

**Student**
  * **Profile** Management: Create and update profile with skills, interests and preferred locations.
  * **Search**: Find traineeships based on interests or location.
  * **Logbook**: Fill in logbooks to report progress during the traineeship.

**Company**
  * **Position** **Management**: Create, delete and announce traineeship positions with required skills.
  * **Applicant** **Review**: View students assigned to their positions.
  * **Evaluation**: Rate student performance (motivation, effectiveness, efficiency) upon completion.

**Professor**
  * **Supervision**: View a list of supervised traineeships.
  * **Evaluation**: Rate student performance.

**Traineeship Committee**
  * **Matchmaking**: Assign students to positions based on distinct criteria.
  * **Supervisor** Allocation: Assign professors to traineeships based on research interests or workload.
  * **Final** **Grading**: Monitor the status of all traineeships and finalize them with Pass/Fail grades based on evaluations.

## Installation & Setup
1. **Clone the repository**
   ```git clone https://github.com/AlexGeorgallis/traineeship_app.git```
2. Database Configuration
   * Ensure **MySQL** is running
   * Import the database schema using the provided script: ```traineeship_app/myy803_traineeship_app-static.sql```
   * Open ```src/main/resources/application.properties``` and update your MySQL credentials
     ```spring.datasource.username=your_username```
     ```spring.datasource.password=your_password```
3. Run the Application
   * Run via Maven:
     ```mvn spring-boot:run```
   * Access the dashboard at `http://localhost:8080`.
  
   **Quick Start / Default Login**: The database script automatically creates an admin account for the committee.
   * **Username**: ```committee```
   * **Password**: ```committee```
   
## Testing
The project includes a suite of unit and integration tests using JUnit and Mockito to ensure the reliability of the refactored services and strategy patterns. 
``` mvn test ```
