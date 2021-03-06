
# Human Resources Management

This project is intended to manage the human resources of an organization.

## Modules:
### Credits Management

This module is intended to manage the credits assigned to departments of an organization. 
Each department has a balance of credits that are used to add or promote employees.

![](./documentation/doc01.png)
![](./documentation/doc02.png)
![](./documentation/doc03.png)

## Technologies
### Frontend

- AngularJS 1.5.0 
- Bootstrap 3.3.6
- Supported browsers: Internet Explorer 9+, Firefox, Chrome

### Backend
- Java 1.8
- Maven 3 
- Spring MVC 4.2.1.RELEASE 
- Hibernate 3.6.8.Final 
- Database Oracle 11g 
- HSQLDB for integration testing

## Profiles

In order to have different environment this project uses spring profiles. Using properties by profile: 

 - Development (dev)
 - Production (prod)
 - Testing (test)

## Testing


Integration tests are ran using HSQLDB, an in-memory dababase.

 - Database schema
test-data/schema.sql

 - Test Database
test-data/data.sql

## Development

For development purposes we can run EmbeddedTomcatLauncher.java with this JVM property

```
-Dspring.profiles.active=dev
```
This launches the application using the embedded Tomcat, no need to have an external Tomcat instance installed.


## Production

Set the Java System Property when starting the servlet container for the production profile.


```
-Dspring.profiles.active=prod
```

## Set environment Java properties in Production
 - On Windows 7 64x
For the Tomcat service, startup settings are stored in the registry under 'Options' key at:
HKEY_LOCAL_MACHINE\SOFTWARE\Apache Software Foundation\Procrun 2.0\Tomcat<X>\Parameters\Java
(substitute appropriate Tomcat version where needed).
On 64-bit Windows, the registry key is:
HKEY_LOCAL_MACHINE\SOFTWARE\Wow6432Node\Apache Software Foundation\Procrun 2.0\Tomcat<X>\Parameters\Java
even if Tomcat is running under a 64-bit JVM.


## ChangeLog

2016 - June

 - Using angular-ui-grid 3.1.1 to enhance retained credits visualization
 - Upgraded Bootstrap to version 3.3.6
 - Calculating retained creadits with an iterative algorithm instead of recursively

2016 - March

 - Search employess in all departments

2015 - October

 - Implemented DepartmentCreditsEntries
  
2015 - August

 - Added support for Java 8, Tomcat 8 and Spring 4.2.1

2015 - April

 - Added Unit Tests for CreditsEntry canStatusBeChanged
 - Applying Domain Driven Design. Moved canStatusBeChanged from service to CreditsEntry entity
 - Using "seams" to break dependencies in unit tests
 - Applying Builder pattern in unit tests

2015 - February

 
 - REST API: added method findOccupationalGroups
 - AngularJS: added select for modifying the occupationalGroup of an employment.

2015 - January

 - Added Hibernate mapping for DepartmentAccount. This is an association class that keeps the relation between a Department and an Account
 
2014 - December

 - Added parentId to Department class in order to build a hierarchical structure.
 - Added calculation of retained credits traversing and summing up credits from bottom to top.

2014 - October

 - Added draft version of the documentation about clustering Tomcat and **Load Balancing** using **Apache** and **mod_jk**
 - Added documentation about the balancer web console using the **jkmanager** monitor

 
2014 - September

 - **Concurrency and Thread Safety:** String formatters in JDK are not thread-safe so 
a ThreadLocal was used to ensure only one copy of DATE_FORMATTER
 - Listing CreditsEntries using AngularJS
 - Added REST API for finding CreditsEntries and CreditsEntriesPeriods
 - Using **AngularJS Promises** for asynchronous calls
 - Added **AngularJS** drop-down with search capability for searching for departments.  [**ui-select**](https://github.com/angular-ui/ui-select)
 - Added REST API for retrieving available departments

2014 - August
 
 - Added pagination in the front-end using **Bootstrap UI pagination** and in the backend for searching employments
 - Added first version of business requirement: Handle retention of credits by department.

2014 - July
 
 - Refactored employment editor as an **Angular directive**, in order to be a **reusable web component**
 - Organized AngularJS code into scalable folders. Grouped files by feature.
 - Added **AngularJS validator** for verifying the employee CUIL in the form
 - Added **AngularJS Growl notifications**. Feedback about operations results.
 - Replaced underscore.js by lodash.js
 - Backend: Added Spring MVC **GlobalControllerExceptionHandler**
 - Backend: Calculating credits period balance for departments recursively
 - Backend: Ordering by employee name, case insensitive.

2014 - June

 - Added **REST API** to retrieve employments of a department
 - Using **Restangular** to call server REST API 
 - **JSON support**: migrated Jackson from Codehaus to Fasterxml
 - Started migration to **AngularJS**. Showing the grid of employments
 - Introduced **Boostrap CSS Framework**
 
2014 - May

 - Added testing infrastructure

2014 - April

 - Added profiles for development, test and production
 - Added reporting functionality
 - Added i18n support



## Credits

- http://stackoverflow.com/questions/8609998/how-to-pass-a-system-property-to-a-web-application-hosted-in-tomcat-7-running-as
- Spring by Example - David Winterfeldt

### AngularJS Plugins

- Alerts and Messages - 
https://github.com/mateu-aguilo-bosch/message-center

- Alerts Growl - https://github.com/JanStevens/angular-growl-2

- Drop-down - https://github.com/angular-ui/ui-select