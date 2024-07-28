## Class Assignment 4 - Part2

- The goal of this assignment is to use Docker to set up a containerized environment to execute a version of the 
version of the spring basic tutorial application. The project used in this tutorial is available 
in this [link](https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819)

#### What is docker compose? 

- Docker Compose is a tool for defining and running multi-container Docker applications. With Compose, you can define a
multi-container Docker application using a YAML file (usually named docker-compose.yml) and then use a single command 
to create and start all the services defined in your configuration.

#### What is a docker volume? 
- In Docker, a volume is a way to persist data generated by and used by Docker containers. It allows data to be shared 
between containers and between a container and the host system. 
- Volumes are especially useful for storing data that needs to persist beyond the lifecycle of a container, 
such as database files, configuration files, or user uploads in a web application.

1. **"You should produce a solution similar to the one of the part 2 of the previous CA but now using Docker instead of Vagrant."**

- As we can remember, in the last assignment, the goal is to clone a basic spring web app to a docker. This app will
connect with another docker that has the database.

2. **"You should use docker-compose to produce 2 services/containers"**

- web: this container is used to run Tomcat and the spring application
- db: this container is used to execute the H2 server database

3. **Let's analyse db DockerFile**

```bash
# Use the official Ubuntu image
FROM ubuntu

# Install OpenJDK 17, unzip, and wget
RUN apt-get update && apt-get install -y openjdk-17-jdk-headless unzip wget

# Create a directory for the application
RUN mkdir -p /usr/src/app
WORKDIR /usr/src/app/

# Download the H2 database JAR
RUN wget https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar

# Expose ports 8082 and 9092
EXPOSE 8082
EXPOSE 9092

# Start the H2 database server when the container starts
CMD java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists
```
- This Dockerfile sets up an Ubuntu-based environment, installs OpenJDK 17, unzip, and wget, creates a directory 
for the application, downloads the H2 database JAR, exposes ports 8082 and 9092, and starts the H2 database server 
when the container starts.

4. **Let's analyse web DockerFile**

```bash
# Use the Tomcat 10 image with JDK 17 based on Temurin
FROM tomcat:10.0.20-jdk17-temurin

# Update package lists and install necessary dependencies
RUN apt-get update -y && \
    apt-get install sudo nano git nodejs npm -f -y && \
    apt-get clean && rm -rf /var/lib/apt/lists/*

# Create a directory for building the application
RUN mkdir -p /tmp/build
WORKDIR /tmp/build/

# Clone the application repository
RUN git clone https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git

# Navigate to the React and Spring Data REST project directory
WORKDIR /tmp/build/devops-23-24-JPE-PMS-1231819/CA2/Part2/react-and-spring-data-rest-basic

# Make the Gradle wrapper executable
RUN chmod +x gradlew

# Build the Spring Boot application with Gradle and copy the WAR file to Tomcat's webapps directory
RUN ./gradlew clean build && \
    cp build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ && \
    rm -Rf /tmp/build/

# Expose port 8080
EXPOSE 8080
```
5. **Let's analyse docker-compose.yml file**
```bash
version: '3'
services:
  web:
    build: ./web
    ports:
      - "8080:8080"
    networks:
      default:
        ipv4_address: 192.168.33.10
    depends_on:
      - db
    environment:
      SPRING_DATASOURCE_URL: jdbc:h2:tcp://192.168.33.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE

  db:
    build: ./db
    ports:
      - "8082:8082"
      - "9092:9092"
    volumes:
      - ./data:/usr/src/data-backup
    networks:
      default:
        ipv4_address: 192.168.33.11
networks:
  default:
    ipam:
      driver: default
      config:
        - subnet: 192.168.33.0/24
```
- **Version**: Specifies the version of the Docker Compose file format being used. In this case, it's version 3.

- **Services**
- web: Configure the web service. It builds the Docker image from the ./web directory, exposes port 8080 on the host 
machine, assigns a static IPv4 address of 192.168.33.10 within the custom network, specifies that it depends on the 
db service, and sets an environment variable SPRING_DATASOURCE_URL to connect to the H2 database running in the db service.

- Db: Configures the db service. It builds the Docker image from the ./db directory, exposes ports 8082 and 9092 on 
the host machine, mounts a volume from the host machine's ./data directory to /usr/src/data-backup in the container, 
assigns a static IPv4 address of 192.168.33.11 within the custom network.

- **Volumes**:
- Specify a volume named data mounted to /usr/src/data-backup in the db service. This allows data to persist even if
the container is stopped or removed.

- **Networks**:
- Define a custom network, default with an IP address from the subnet 192.168.33.0/24 to containers within the network.

6. **Let's docker-compose**
- In terminal type:
```bash
    $ docker-compose up
```
- docker-compose up command does several things: builds image from the Dockerfiles, pulls images from registries, 
creates and starts containers, configures a bridge network for the containers and handles logs.
- This operation may take several minutes. After that, we should see in 
[http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/](http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/)
our web app. And in [http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console](http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console)
we should see our h2 web console. 

#### Some important commands regarding docker compose:

- This command is used to run a bash shell (or any command) inside a service defined in a docker-compose.yml file.
It is part of the Docker Compose toolset:
```bash
    $ docker-compose exec web bash
```
- To stop and remove containers, networks, volumes, and images created by docker-compose up:
```bash
    $ docker compose down
```
- This command stops the containers that already exist and are running and are in a stopped state:
```bash
    $ docker compose stop
```
- This command starts the containers that were previously stopped with docker compose stop. It only works on containers 
that already exist and are in a stopped state:
```bash
    $ docker compose start 
```
- This command is used to clean up your Docker environment by removing unused data:
```bash
    $ docker system prune
```

### 7. Tag db image and web image and push them
1. Tagging
- Given that when I type:
```bash
    $ docker ps 
```
- This is the output:
```bash
    CONTAINER ID   IMAGE               COMMAND                  CREATED              STATUS              PORTS                                            NAMES
    63e346998c12   dockercompose-web   "catalina.sh run"        About a minute ago   Up About a minute   0.0.0.0:8080->8080/tcp                           dockercompose-web-1
    83e8dbfa6eab   dockercompose-db    "/bin/sh -c 'java -c…"   About a minute ago   Up About a minute   0.0.0.0:8082->8082/tcp, 0.0.0.0:9092->9092/tcp   dockercompose-db-1
```
- To tag web image:
```bash
   $ docker tag dockercompose-web afonsomaria1271819/compose-web:web
```
- To tag db image:
```bash
   $ docker tag dockercompose-db afonsomaria1271819/compose-db:db
```
- To check the recently tagged images

```bash
   $ docker images
```
- The output should be:
```bash
   REPOSITORY                       TAG       IMAGE ID       CREATED             SIZE
  afonsomaria1271819/compose-web   web       a607c3f71563   About an hour ago   1.53GB
  dockercompose-web                latest    a607c3f71563   About an hour ago   1.53GB
  afonsomaria1271819/compose-db    db        4e4390c05574   About an hour ago   466MB
  dockercompose-db                 latest    4e4390c05574   About an hour ago   466MB
```
2. Pushing

- First let's login
```bash
    $ docker login
```
— After let's push our tags:

```bash
    $ docker push afonsomaria1271819/compose-web:web
    
    $ docker push afonsomaria1271819/compose-db:db
```
- To check in my docker hub profile the pushed images - [profile](https://hub.docker.com/repositories/afonsomaria1271819)

### 8. Volumes (copy of the database file by using the exec to run a shell in the container and copying the database file to the volume)
- To understand this part of the class assignment, we must further analyze what is happening in the docker compose file:
```bash
   db:
  build: ./db
  [...]
  volumes:
  - ./data:/usr/src/data-backup
```
- A volume named data is mounted to the /usr/src/data-backup directory inside the container.
- Ensure that the ./data directory exists in the same location as your docker-compose.yml file on your host machine.
  This is where the backup will be stored.
- To start a bash session inside the db container type:

```bash
 $ docker-compose exec db bash
```
- Inside the container check for the database file:
```bash
 $ root@83e8dbfa6eab:/usr/src/app# ls
```
- The output should be something like this:
```bash
  h2-2.2.224.jar  jpadb.mv.db
```
- To copy the database file to the volume type:
```bash
  $ cp *.db /usr/src/data-backup
```
- Exit the container:
```bash
  $ exit
```
- **Then, on your host machine, navigate to the ./data directory (which corresponds to /usr/src/data-backup in the 
container) and check if the .db file is there**