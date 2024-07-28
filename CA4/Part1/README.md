## Class Assignment 4 - Part1

- The goal of the Part 1 of this assignment is to practice with Docker, creating
docker images and running containers using the chat application from CA2. The project used in this tutorial is available 
in this [link](https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819)

### What is Docker? 

- Docker is a platform that enables developers to build, package, and deploy applications as lightweight, portable 
containers. These containers encapsulate everything an application needs to run, including the code, runtime, system 
tools, libraries, and settings, ensuring consistency across different environments.
- Docker uses containerization technology to achieve this, which allows multiple containers to run on the same machine, 
sharing the same OS kernel but isolated from each other.

### What is the Docker engine? 

The Docker Engine is essentially the core of Docker. It consists of two main parts:

- Docker Daemon: This is a background process that runs on a host machine. It manages Docker objects like containers, 
images, networks, and volumes. It listens for commands via the Docker Remote API.

- Docker Client: This is a command-line tool that users interact with to manage Docker. It sends commands to the 
Docker Daemon to build, manage, and control Docker containers and services.

### What is a docker image?

- In summary, an image is a portable and immutable snapshot of a containerized application, 
allowing developers to package and distribute software with all its dependencies in a standardized way.

### What is a container?

- Docker uses os-level virtualization for its containers, so it uses a linux kernel to run.
- In my case (Windows) I used WSL2, so the Hyper-v created a light-weight VM with a linux kernel.
- A Docker container is a lightweight, standalone, and portable software package that includes everything needed 
to run a piece of software, isolated from its environment.

### Class assigment set-up 

1. Enable WSL2
2. Install docker desktop 
3. type in PowerShell
```bash
  $ docker -v
  Docker version 25.0.2, build 29cf629
```
4. The goal of the Part 1 of this assignment is to practice with Docker, creating docker images and running containers 
using the chat application from [CA2](https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819)

### Create a Dockerfile to package and execute the chat server in a container

- Bellow there is a simple docker file 

```bash
 # Use an official OpenJDK image as the base image
FROM openjdk:11

# Set the working directory inside the container
WORKDIR /app

# Install Git to clone the repository
RUN apt-get update && apt-get install -y git

# Clone the repository
RUN git clone https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git .

# Navigate to the desired directory and give execute permissions to the Gradle wrapper
WORKDIR /app/CA2/Part1/gradle_basic_demo
RUN chmod +x ./gradlew && ./gradlew build

# Expose the port the server runs on
EXPOSE 8080

# Run the server using the Gradle task
CMD ["./gradlew", "runServer"]
```
1. **FROM openjdk:11**: This line specifies the base image for the Docker container. In this case, it's using the official 
OpenJDK image for Java 11.
2. **WORKDIR /app**: This sets the working directory inside the container to /app. Any later commands will be executed 
relative to this directory unless otherwise specified.
3. **RUN apt-get update && apt-get install -y git**: This line updates the package index and installs Git using the 
package manager (apt-get) within the container. Git is required to clone the repository in the next step.
4. **RUN git clone https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git .**: 
This command clones the specified Git repository into the current working directory (/app). 
The . at the end specifies that the repository should be cloned into the current directory.
5. **WORKDIR /app/CA2/Part1/gradle_basic_demo and RUN chmod +x ./gradlew && ./gradlew build**: This command changes the WORKDIR
to CA2/Part1/gradle_basic_demo. Gives execute permissions to gradle
wrapper script and builds the project
6. **EXPOSE 8080**: Exposes 8080 port
7. **CMD ["./gradlew", "runServer"]**: This specifies 
the default command to run when the container starts. It executes the Gradle wrapper with the runServer task, 
which starts the server for the application.

### Build and image from the previously created docker file 

1. Create a directory for your docker file: 
```bash
  $ mkdir docker
  $ cd /path/to/docker
  $ cp /path/to/Dockerfile .
```
2. Build the image command 

- The command is 
```bash
    $ docker build -t <image_name>:<tag> .
```
- I want to create it without a tag, so I will tag it later 
- When you don't provide a tag while building a Docker image, Docker automatically assigns the tag latest to the image.
```bash
    $ docker build -t my-server-chat .
```
3. Create a container from the newly created image 

- To list all images with detailed information, we can

```bash
    $ docker images -a
```
- To run a container from an image. To stop it, press CTRL +C.
- **docker run**: This is the command used to run a Docker container.

- **p 8080:59001**: This option (-p or --publish) is used to publish a container's port(s) to the host. In this case,
  it's mapping port 59001 on the container to port 8080 on the host. This means that any traffic sent to port 8080 on the
  host machine will be forwarded to port 59001 on the container. It's specially important to remember that server side in this app
  is listening on port **59001**

###### Detached mode. Allows you to start the container and then continue using the same terminal for other commands or tasks.

```bash
    $ docker run -d -p 8080:59001 my-chat-server:latest
```
- To stop a controller in detached mode, we must type in terminal "docker stop containerID"
- To start a controller in detached mode, we must type in terminal "docker start containerID"

#### The app should print in the console that the server is running !!!

##### List Running Containers
- To see which containers are currently running, use the following command:

```bash
    $ docker ps
```

- Right now we have the server side on our app running in a container with a mapping between port 8080 to 59001.
- So, to run the client side of our app we must specify the ip and port we want to connect with. 
- In command-line type: 
```bash
    $ cd /path/to/clientside/project
    $ ./gradlew runClient --args="localhost 8080"
```
- Because any traffic sent to port 8080 on the host machine will be forwarded to port 59001 on the container,
by running the task with these arguments, we are connecting the client side on our host to the app (server side)
in the container.

#### Access the Running Container
- To start an interactive shell session, we must type in the terminal 
```bash
    $ docker exec -it containerID /bin/bash
```
or 
```bash
    $ docker exec -it my-chat-server /bin/bash
```
- After typing this command, we should see a terminal that is the "inside" of our docker container!
- If we navigate to our WORKDIR "/app" we should see the different class assignments cloned from the
https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git repository.

#### How to tag an image?

- As referred to before, we may tag it in the first build or later. 
- In the terminal type:
```bash
   $ docker tag <existing_image>:latest <your_dockerhub_username>/<repository_name>:latest
```
or in my case 

```bash
   $ docker tag my-chat-server:latest afonsomaria1271819/firstrepo:clonedRepository
```

#### How to push an image? 

- First, we must log in via terminal to push our image:
```bash
   $ docker login
```
and after 
```bash
   $ docker push afonsomaria1271819/firstrepo:clonedRepository
```
— After that, check if your docker hub profile already has the pushed image [profile](https://hub.docker.com/u/afonsomaria1271819)

### How to delete a container and an image

- First, we have to have in mind that docker will not allow deleting and in use image, so first, we need to delete all containers
associated with an image!
- Docker might not allow deleting of a running container, so stop it first.
- To delete our container:
```bash
   $ docker rm my-chat-server
```
- If we want, we can also delete all stopped containers by typing: 
```bash
   $ docker container prune
```

## Second approach (build the chat server on your host computer and copy the jar file “into” the Dockerfile)

- This approach is very similar to the last one the difference is in the way our chat app is "passed" to the container 
to run. 
1. Build the chat server in host computer, and copy the jar to the directory where are your dockerfile 
```bash
   $ cd path/to/chat/app
   $ ./gradlew build
   $ cp build/libs/generatedJar.jar path/to/doker/dir
```
2. Let's analyze the docker file for this scenario:
```bash
# Use an official OpenJDK image as the base image
FROM openjdk:21

# Set the working directory inside the container
WORKDIR /app

# Copy JAR files to the working directory
COPY basic_demo-0.1.0.jar /app

# Expose the port the server runs on (replace 8080 with the correct port if needed)
EXPOSE 8080

# Run the server using the specified command
CMD ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]
```
- **FROM openjdk:21**: This line specifies the base image for the Docker container. In this case, it's using the official
   OpenJDK image for Java 21.
- **WORKDIR /app**: This sets the working directory inside the container to /app. Any later commands will be executed
   relative to this directory unless otherwise specified.
- **COPY basic_demo-0.1.0.jar /app**: The COPY command copies the JAR file (basic_demo-0.1.0.jar) from the host machine 
to the /app directory inside the Docker container. The JAR file is expected to be in the same directory as the Dockerfile 
on the host machine (or you specify the relative or absolute path to it).
- **EXPOSE 8080**: Exposes 8080 port
- **CMD ["java", "-cp", "basic_demo-0.1.0.jar", "basic_demo.ChatServerApp", "59001"]**: he CMD command specifies the 
command that should be executed when the Docker container starts. In this case, it runs the Java application using the 
java command, specifying the classpath (-cp option) and the main class (basic_demo.ChatServerApp). It also provides 
the port number (59001) as an argument to the application.

3. How to run a container from an image, tag the image and push it to docker hub is already explained in the previous steps.
4. This alternative image was also pushed to docker hub [profile](https://hub.docker.com/repositories/afonsomaria1271819)


