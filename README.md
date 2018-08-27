
## Contents
1.	What this project is about	
2.	Application/Development Features	
3.	How to run the application

 &nbsp;
## 1. WHAT THIS PROJECT IS ABOUT
This project showcases some of the skills picked up during my Computer Science master's degree and my own personal study. It is an online product catalogue system that allows users to log in and carry out tasks based on their role privileges, such as adding products, editing product details or images, and adding new users.



&nbsp;
## 2.	APPLICATION/DEVELOPMENT FEATURES
Design/coding features include:
* MVC pattern, loose coupling, cohesive classes, inheritance and the use of pagination to ease server load 
* Front-end and back-end validation for editing/adding of products and new users
* Exception handling and logging
* Unit testing 
* Security features such as password encryption and restricted access to URLs (based on role privileges)
* File uploads
* Easy to navigate and scalable UI 

Technologies used include:
* Spring Boot/MVC
* Spring Data JPA
* Java
* JUnit 5 and Mockito
* Spring Security
* Thymeleaf
* JavaScript
* Bootstrap 4
* Maven


Available User Activities:
* Search Catalogue
* Add/Edit/Delete Products (Admin and Super-Admin users only – login required*)
* Add Users (\'Super Admin\' only)

\**Login emails and passwords are provided on the login page to allow you to log in to an Admin or a \'Super Admin\' account.*



&nbsp;
## 3.	HOW TO RUN THE APPLICATION


* Simply run the jar file from the command prompt (java -jar andrew-demo.jar) on a machine with a Java runtime environment.
* Go to http://localhost:8080/home in a web browser. Choose from the options on the page, or from the navigation menu at the top of the page.
* Options in the Admin and Super zones require a login (with an Admin or Super account respectively). Login details of sample Admin and Super accounts are provided on the login page. They are:
    - Admin Account: (email: admin@gmail.com, password: admin)
     - Super Account: (email: super@gmail.com, password: super)

* To log in, click ‘login’ in the top right corner of the navigation bar. To log off, click the dropdown menu to the right of the navigation bar (where it says ‘Welcome, (your email)’ and select ‘logoff’.

  ### *Notes:*
* The application uses an embedded H2 database. A folder named ‘database’ will be created in whatever folder the jar file is located. On start-up, this will be initialized with sample data.

* Another folder, named ‘dynamic-resources’, will also be created. This will act as the image ‘server’ and will allow for image uploads, as well as providing images for the sample products.



