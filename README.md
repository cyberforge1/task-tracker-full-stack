# Task Tracker - Full-Stack Web App

## Project Overview
A comprehensive full-stack task tracking application with complete CRUD functionality, utilizing a Spring Boot API, a TypeScript React UI, and a MySQL database.

## Screenshot
![Project Screenshot](project-screenshot.png)

## Goals & MVP
- The project aims to develop a Spring-based backend for managing todos in a MySQL database, paired with a React frontend for seamless user interaction. 

- The MVP features a polished UI for adding, completing, deleting, and editing todos with comprehensive error handling and toast notifications, while the backend ensures robust error management, request logging with Log4j, and API documentation via Spring Swagger.

## Table of Contents
- [Frontend](#Frontend)
- [Backend](#Backend)
- [General](#General)

# Frontend - React Typescript

## Tech Stack

- JSX
- SCSS
- Typescript
- React 
- Git 


## Build Steps
1. Clone the project from GitHub:
   ```bash
   git clone git@github.com:cyberforge1/task-tracker-full-stack.git

2. Create, initialize and link a MySQL database to the backend API

3. Run the backend API locally

4. Navigate to the frontend directory
   ```bash
   cd task-tracker-full-stack/frontend

5. Run the frontend locally 
   ```bash
   npm run dev

## How To Use
- Users can create a task by filling out the form inputs and submitting the 'Add Task' button. Tasks can then be completed, edited, or deleted with a button click. Features for sorting, searching and changing the user interface appearance are also available.


## Design Goals
- The objective for the frontend was to create a responsive, appealing and intuitive user interface that allows users to easily access the backend features. 


## Project Features
- [x] An efficient user interface to interact with the backend API to access CRUD functionality
- [x] Dynamic filtering for specific task status
- [x] Instantaneous searching for matching tasks 
- [x] Toggling capability between list and tile components for tasks



# Backend - Spring Boot API

## Tech Stack

- Java
- Spring Boot
- Spring Swagger
- Log4j
- JUnit
- MySQL


## Build Steps
1. How to build/run the project (use proper code snippets)
ex.
  ```bash
  git clone git@github.com:cyberforge1/some-project.git
```

## How To Use

To access this backend, first follow the build steps above. The application frontend provides a user interface to interact with this backend automatically, endpoints can also be accessed and tested through an applications like Postman, or Swagger UI.

## Design Goals
- The aim of this application was to create a robust API to create, read, update and delete task data. 


## Project Features
- [x] Comprehensive CRUD functionality to interact with the database
- [x] A MySQL database to store task data  
- [x] User error notification for incorrect endpoint access
- [x] Enhanced logging features provided by Log4j
- [x] Project document generation by Spring Swagger
- [x] Unit testing provided by JUnit

# General

## Additions & Improvements
- [ ] Addition of GitHub Workflow Badges
- [ ] User authentication and login functionality
- [ ] Integration of React Testing Library for frontend
- [ ] Providing toast notifications for error handling
- [ ] Troubleshooting backend integration and end-to-end testing
- [ ] Creating a Docker container for deployment


## Learning Highlights
- Practiced and enhanced skills with React by creating components, adding display toggling, dynamic filtering and searching to my application
- Applied various tests and testing libraries to the frontend and backend
- Building in different libraries to increase application scope and functionality 
- Gained valuable experience by independently building a Spring Boot application
- Creating and integrating a MySQL database for desired data

## Challenges
- Learning how to apply unit-testing, integration testing and end-to end testing throughout the application
- Building previously unknown features into the application with new libraries


## Contact Me
- Visit my [LinkedIn](https://www.linkedin.com/in/obj809/) for more details.
- Check out my [GitHub](https://github.com/cyberforge1) for more projects.
- Or send me an email at obj809@gmail.com
<br />
Thanks for your interest in this project. Feel free to reach out with any thoughts or questions.
<br />
<br />
Oliver Jenkins © 2024
