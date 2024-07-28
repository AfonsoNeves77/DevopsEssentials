FROM tomcat:10.0.20-jdk17-temurin

RUN apt-get update -y

RUN apt-get install sudo nano git nodejs npm -f -y

RUN apt-get clean && rm -rf /var/lib/apt/lists/*

RUN mkdir -p /tmp/build

WORKDIR /tmp/build/

RUN git clone https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git

WORKDIR /tmp/build/devops-23-24-JPE-PMS-1231819/CA2/Part2/react-and-spring-data-rest-basic

RUN chmod +x gradlew

RUN ./gradlew clean build && cp build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ && rm -Rf /tmp/build/

EXPOSE 8080
