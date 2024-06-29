# Tasks API - Amazon Dynamo DB

![Java 17](https://img.shields.io/badge/java_17-%23ED8B00.svg?style=for-the-badge&logo=openjdk&logoColor=white)
![Spring Boot 3](https://img.shields.io/badge/spring_boot_3-%236DB33F.svg?style=for-the-badge&logo=spring-boot&logoColor=white)
![AmazonDynamoDB](https://img.shields.io/badge/Amazon%20DynamoDB-4053D6?style=for-the-badge&logo=Amazon%20DynamoDB&logoColor=white)
![Docker](https://img.shields.io/badge/docker-%230db7ed.svg?style=for-the-badge&logo=docker&logoColor=white)

**Tasks API** is a simple Spring Boot REST API concept to demonstrate connecting to Amazon DynamoDB and performing operations against a DynamoDB. This application contains basic create, read, update, and delete operations for tasks.

## Requirements to Run
This section assumes the user is familiar with installing Java 17 and running Maven-based applications. It also assumes that the user is knowledgeable of installing Docker.

### Creating the DynamoDB local instance
This application connects to a local Amazon DynamoDB offered by the [amazon/dynamodb-local](https://hub.docker.com/r/amazon/dynamodb-local) Docker image and also creates a simple DynamoDB admin interface through [aaronshaf/dynamodb-admin](https://hub.docker.com/r/aaronshaf/dynamodb-admin). This application utilizes the [amazon/aws-cli](https://hub.docker.com/r/amazon/aws-cli) to create the Tasks table and populate the table with a few entries on startup. All of these details are wrapped in the [dynamo/docker-compose.yml](https://github.com/narlock/dynamo-tasks-api/blob/main/dynamo/docker-compose.yml) file.

To start this, navigate to the `dynamo` directory in this project and run `docker-compose up`. This will create the DynamoDB local instance.

### Running the REST API
To run the Tasks API, you can either build the project and execute the JAR file, or run the application using an IDE.

To build the project, navigate to the main app directory (where the `pom.xml` file exists). From here, run the command `mvn install`. This will create a JAR file that can be executed within the `target` directory.

To run the project, simply navigate to the `target` directory containing the JAR file, then run `java -jar dynamo-tasks-api-1.0.0.jar`. The application will start on port 8080.

## High-level component view

![Component Diagram](readme%20assets/componentDiagram.png)