# Sam Solutions test task

URL shortener

## Task Requirements

Implement URL shortener, that, with the help of a theoretical frontend (not a part of this task) will generate a shortened URL for a long URL. When creating the URL, user has to be able to set his custom alias to it. If the alias is taken, an appropriate response has to be sent to the user. 

The URL has to have a parameter called Time-to-Live (TTL). It determines how long a short URL can be reached until it is completely deleted from the database. If TTL is not set, the URL remains in the database forever (unless it is manually deleted). 

For example, GET request to http://localhost:8080/{shortUrl} shall make the application redirect the request to the long URL. 
Manual deletion of short URLs has to be possible.

Runtime application information has to be logged in a file and console.

## Technology stack

**Spring Boot**
**Spring Data JPA**
**Relational database of any choice**
**SLF4J logging**

## Usage

Build and run application:

* git clone SamSolutionsTestTask

* Navigate your terminal to the root of the project

* Set environmental variables (for **application.yml** and **logback.xml**)

* Run **mvn clean package**

* Go to the **target** directory and run **java -jar SamSolutionsTestTask-0.0.1-SNAPSHOT.jar** 

* Open **http://localhost:8080/api/sam-solutions-test-task-endpoints** to see all the available endpoints and entities