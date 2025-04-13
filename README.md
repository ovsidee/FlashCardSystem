# Flash Card System

A simple **Flashcard-based Library Management System**, built with Java and Spring Boot.

This project allows users to manage a collection of **entries** (flashcards), each containing translations in **English**, **German**, and **Polish**. It provides features for interacting with the flashcards through various functionalities such as:

- **Find** entries by any translation
- **Add** new flashcards
- **Remove** entries
- **View** all entries in the database
- **Sort** entries
- **Test yourself** with random words
- **Profile-based viewing** of flashcards:
  - `original` – Show words as entered
  - `upperCase` – Show all in uppercase
  - `lowerCase` – Show all in lowercase

## Features

- Spring Boot application with H2 database support
- Backend using Spring Data JPA
- Profiles for dynamic word presentation
- H2 Console enabled for easy DB inspection

## Technologies Used

- Spring Boot
- Spring Data JPA
- Hibernate
- H2 Database
- Gradle

## Running the Application

To run the application locally:

1. Clone the repository:
   - Run the command:
     ```git clone https://github.com/your-username/LibraryManagementSystem.git && cd LibraryManagementSystem```
2. Run with your preferred IDE or with the command:
   - Run the command:
     ```./gradlew bootRun```
3. Access the H2 console (if enabled):  
   - Run the command:
     ```http://localhost:8080/my-h2-console```

> Default H2 credentials:  
> - **Username**: `libSystem`  
> - **Password**: `1234`  

## Profile Configuration

In `application.yml`, you can activate profiles to change how words are displayed:

```yaml
spring:
  profiles:
    active: upperCase # Change to lowerCase or original
