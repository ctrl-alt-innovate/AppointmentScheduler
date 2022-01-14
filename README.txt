Title: Appointment Scheduler Application
Author: Evan Wahrmund
Date: 1/10/2022
Contact: ewahrm1@wgu.edu
Purpose: This application is intended to allow users to manage appointments and customers, display appointment schedules,
         and provide reports for business purposes. Customers and appointments can be added, updated, or deleted. Appointment
         schedules can be displayed by month, week, or all. Different statistical reports are generated within the application.

IDE: IntelliJ IDEA Community Edition 2021.1.1 x64
JDK Version: Java SE 11.0.11
JavaFX Version: javafx-sdk-11.0.2

SceneBuilder Version: 16.0.0
MySQL Driver Version: mysql-connector-java-8.0.25

INSTRUCTIONS TO RUN:
1. Download Zip File.
2. If not already downloaded, download MYSQL Driver file and JavaFX Library
3. Extract zip file and open in desired IDE
4. Go to the project structure for the project and add 2 new libraries: 1 for JavaFX and another for MYSQL Driver
5. Select the MYSQL Driver Library to point to the location mysql-connector-java-8.0.25.jar
6. Select the JavaFX Library to point to all javafx.***.jar (not src.zip) files in the lib folder of the downloaded library
7. Create a run configuration named Main and set main class to com.evanwahrmund.appointmentscheduler.AppointmentScheduler
8. Enable VM options for the run configuration and set Java Version to Java 11.0.11
9. Under VM options add "--module-path ${PATH_TO_FX} --add-modules javafx.fxml,javafx.controls,javafx.graphics,javafx.base,javafx.media"
10. Run Project!

Additional Report: The additional report provides a count of the number of Appointments for each country
    and another count of appointments for each first level division.  It also provides a count of the number
    of customers for each country and another count of customers for each first level division.
