## Class Assignment 3 - Part1 

#### What is Vagrant? 

- Vagrant is an open-source tool for building and managing virtualized development environments. It automates the 
process of setting up and configuring virtual machines (VMs) for development, making it easier to create reproducible 
and consistent development environments across different systems.

##### What is a box?
- In Vagrant, a "box" refers to a packaged and reusable environment that contains everything 
needed to create a virtual machine (VM) instance. It typically includes an operating system image,
configured with specific software, settings, and dependencies.

#### What is a provider? 

- In the context of Vagrant, a "provider" refers to the underlying virtualization platform or technology used to create 
and manage virtual machines (VMs). Vagrant supports multiple providers, each of which offers its own set of features, 
performance characteristics, and integration capabilities.
- For this class assigment we are going to use two type 2 hypervisors (VirtualBox and VmWare).

#### What is a provisioner? 
- Shell Provisioner: This is the simplest and most versatile provisioner, which allows you to execute shell 
scripts inside the VM. You can use shell provisioners to perform tasks such as installing packages using 
package managers (apt-get, yum, etc.), modifying configuration files, downloading files, and running custom setup scripts.

#### What is a Vagrantfile? 

- A Vagrantfile is a configuration file used in Vagrant, which is a tool for building and managing virtualized
development environments. The Vagrantfile is written in Ruby and defines the settings and configuration for a virtual 
machine (VM) that Vagrant will create and manage.

#### Class Assignment objective

- The overall goal of this class assignment is to explore Vagrant/VirtualBox and Vagrant/ChosenProvider box management.
- We should produce a Vagrantfile that creates two virtual machines:
- 1- Web - This virtual machine should act as a web server, clone an app from GitHub and serve it.
- 2 - Db - This virtual machine should act as an external H2 database in server mode. So the virtual machine is able to
connect with.
- After all, the configurations are set, we should be able to see our Spring app in the web browser. 

###### Initial Set up

- Make sure you have vagrant instaled in you machine so 
- To make our app work in this environment, we need to change some configurations in a previous commited app. 
- Take in account the repository referred here - [Devops Repository](https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819)
- Do the following commands in power shell:
```bash
$ cd Desktop/devops-23-24-JPE-PMS-1231819/CA2/Part2
```
- In this project, he needs to change a few things:

**1. Build.gradle**
- Add war plugin to allow the packaging of the app in a war file. (This will allow this app to be deployed in a tomcat server)
- Add "spring-boot-starter-tomcat" dependency, it's typically included in a Spring Boot application to embed the Apache Tomcat servlet container
within the application itself.
```bash
  plugins {
	id "war"
}
dependencies {
	providedRuntime 'org.springframework.boot:spring-boot-starter-tomcat'
}
```
- Add this task, so when ran locally, it overrides the database access configuration to an in memory H2 database.
```bash
bootRun {
	systemProperty 'spring.datasource.url', 'jdbc:h2:mem:jpadb' // Override the datasource URL
}
```

**2. application.properties**
- **server.servlet.context-path=/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT**: This line specifies the context path 
for the servlet container, in this case, it's /react-and-spring-data-rest-basic-0.0.1-SNAPSHOT. When your Spring Boot
application is deployed, it will be accessible under this context path. For example, if your server's URL 
is http://localhost:8080, then your application will be accessible 
at http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.
- **spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE**: 
This line configures the JDBC URL for connecting to an H2 database running in TCP server mode. It specifies the IP 
address 192.168.56.11, port 9092, and the database name jpadb. 
It also sets some additional properties related to database closing behavior.


```bash
server.servlet.context-path=/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT
spring.data.rest.base-path=/api
#spring.datasource.url=jdbc:h2:mem:jpadb
# In the following settings the h2 file is created in /home/vagrant folder
spring.datasource.url=jdbc:h2:tcp://192.168.56.11:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
# So that spring will no drop de database on every execution.
spring.jpa.hibernate.ddl-auto=update
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console
spring.h2.console.settings.web-allow-others=true
```

3. ServletInitializer class
- Add the following class to the project
```bash
package com.greglturnquist.payroll;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

public class ServletInitializer extends SpringBootServletInitializer {

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(ReactAndSpringDataRestApplication.class);
    }

}
```
- This class, ServletInitializer, is used in Spring Boot applications when you want to deploy your application 
as a WAR (Web Application Archive) file to a standalone servlet container like Tomcat, rather than using the 
embedded servlet container provided by Spring Boot.

4. App.js file 
- Add the following code so the get request is made to the previously configured context path "react-and-spring-data-rest-basic-0.0.1-SNAPSHOT"
```bash
componentDidMount() { // <2>
		client({method: 'GET', path: '/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/api/employees'}).done(response => {
			this.setState({employees: response.entity._embedded.employees});
		});
	}
```
5. index.html file

- Make sure that index.html is correctly referencing the main.css
```bash
	<!DOCTYPE html>
<html xmlns:th="https://www.thymeleaf.org">
<head lang="en">
    <meta charset="UTF-8"/>
    <title>ReactJS + Spring Data REST</title>
    <link rel="stylesheet" href="main.css" />
</head>
<body>

    <div id="react"></div>

    <script src="built/bundle.js"></script>

</body>
</html>
```

###### Commiting changes 

- After all changes are made, commit them to the remote repository in GitHub

### Creating the vagrant file

```bash
$ vagrant init - to generate a vagrant file
```

##### Network configurations 

- For this part of the class assignment, we are going to use Vagrant with virtual box as provider and regarding network:

```bash
In the host you can open the spring web application using one of the following options:

http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/
http://192.168.56.10:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/

Also open the H2 console using one of the following urls:

http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console
http://192.168.56.10:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console

- The connection string use: jdbc:h2:tcp://192.168.56.11:9092/./jpadb
```
- Remember that 

#### Vagrant file analysis 

1. Database Box setup, time-out, and package provisioning
   
- Overall, this section of the Vagrantfile sets up the initial configuration for the VMs, specifying the base box to 
use and defining common provisioning tasks to be executed during VM setup. These provisioning tasks ensure that the 
necessary packages are installed within the VMs to support later configurations and software installations.
```bash
Vagrant.configure("2") do |config|
config.vm.box = "ubuntu/bionic64"
config.vm.boot_timeout = 100

# This provision is common for both VMs
config.vm.provision "shell", inline: <<-SHELL
sudo apt-get update -y
sudo apt-get install -y iputils-ping avahi-daemon libnss-mdns unzip \
openjdk-17-jdk-headless
# ifconfig
       SHELL
```
2. Database virtual machine setup, ip assignment and port forwarding

- In this configuration, we are assigning an ip to the vm in the configured private network and giving a name to it;
- We are also port forwarding making this vm reachable for h2 console using port 8082 and to the server in 9092
- This will allow us to connect with this virtual machine using port forward and ip

```bash
# Configurations specific to the database VM
config.vm.define "db" do |db|
db.vm.box = "ubuntu/bionic64"
db.vm.hostname = "db"
db.vm.network "private_network", ip: "192.168.56.11"

# We want to access H2 console from the host using port 8082
# We want to connet to the H2 server using port 9092
db.vm.network "forwarded_port", guest: 8082, host: 8082
db.vm.network "forwarded_port", guest: 9092, host: 9092
```
3. Database downloading and running of the server

- So, in summary, this script downloads the H2 Database JAR file and then starts the H2 Database server process,
allowing remote connections to it. The output of the server startup process is redirected to a file named out.txt.
```bash
# We need to download H2
db.vm.provision "shell", inline: <<-SHELL
wget https://repo1.maven.org/maven2/com/h2database/h2/2.2.224/h2-2.2.224.jar
    SHELL

# The following provision shell will run ALWAYS so that we can execute the H2 server process
# This could be done in a different way, for instance, setiing H2 as as service, like in the following link:
# How to setup java as a service in ubuntu: http://www.jcgonzalez.com/ubuntu-16-java-service-wrapper-example
#
# To connect to H2 use: jdbc:h2:tcp://192.168.33.11:9092/./jpadb
db.vm.provision "shell", :run => 'always', inline: <<-SHELL
java -cp ./h2*.jar org.h2.tools.Server -web -webAllowOthers -tcp -tcpAllowOthers -ifNotExists > ~/out.txt &
SHELL
    end

```
4. Web Box setup, time-out, and package provisioning

- It sets up a private network with the IP address "192.168.56.10". This allows communication between the 
host machine and the VM.
- Port forwarding is configured to forward port 8080 from the host machine to port 8080 on the VM. 
This enables accessing services running on port 8080 within the VM from the host machine.
```bash
# Configurations specific to the webserver VM
config.vm.define "web" do |web|
web.vm.box = "ubuntu/bionic64"
web.vm.hostname = "web"
web.vm.network "private_network", ip: "192.168.56.10"

# We set more ram memmory for this VM
web.vm.provider "virtualbox" do |v|
v.memory = 1024
    end

# We want to access tomcat from the host using port 8080
web.vm.network "forwarded_port", guest: 8080, host: 8080

```
5. Downloading tom cat and setting necessary permissions

- Downloading Apache Tomcat: It uses wget to download Apache Tomcat version 10.1.23 from the Apache archives.

- Extracting Tomcat Archive: After downloading, it extracts the downloaded .tar.gz file using tar. 
The -xzvf flags specify the operation (extract and decompress), the verbose mode, and the file to extract.

- Setting Ownership: It sets the ownership of the extracted Apache Tomcat directory and its contents to the user
vagrant. This is done using chown -R vagrant:vagrant.

- Setting Permissions: It grants execute permissions recursively (-R) to the user (u) for all files and directories 
within the extracted Apache Tomcat directory using chmod.
```bash
#Shell provisioner downloads Apache Tomcat 10.1.23 and extracted into the current directory.

web.vm.provision "shell", inline: <<-SHELL

wget https://archive.apache.org/dist/tomcat/tomcat-10/v10.1.23/bin/apache-tomcat-10.1.23.tar.gz

sudo tar xzvf apache-tomcat-10*tar.gz -C .

# Setting ownership of the Apache Tomcat directory and its contents to the user vagrant and granting execute permissions recursively.

sudo chown -R vagrant:vagrant apache-tomcat-10*

sudo chmod -R u+x apache-tomcat-10*

SHELL
```
6. Start up the tom cat server script 
```bash
# This shell provisioner guarantees that the Apache Tomcat server is automatically initiated upon provisioning the web VM, executing the startup.sh script.

web.vm.provision "shell", :run => 'always', inline: <<-SHELL
   ./apache-tomcat-10*/bin/startup.sh
SHELL
```
7. Cloning repository, giving Gradle wrapper execute permissions, build and deploy

- Cloning Git Repository: It clones a Git repository from the specified URL 
(https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git).

- Navigating to Project Directory: It navigates to the directory of the cloned project 
(devops-23-24-JPE-PMS-1231819/CA2/Part2/react-and-spring-data-rest-basic).

- Setting Execute Permission: It grants execute permission to the Gradle wrapper script (gradlew) using chmod.

- Building Project with Gradle: It cleans the project and builds it using the Gradle wrapper script 
(./gradlew clean build).

- Deploying WAR File to Tomcat: After building the project, it deploys the resulting WAR (Web Application Archive) 
file to the Tomcat directory. This is done by copying the WAR file (react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war) 
to the Tomcat's webapps directory (~/apache-tomcat-10*/webapps/). The * is used to match any version of Tomcat 
10 present in the directory.

```bash
# This block configures a shell provisioner for the web VM, executing a series of commands including cloning a Git repository,
# building a project with Gradle, and deploying the resulting WAR file to a Tomcat directory, all without elevated privileges.

web.vm.provision "shell", inline: <<-SHELL, privileged: false

 git clone https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git

 cd devops-23-24-JPE-PMS-1231819/CA2/Part2/react-and-spring-data-rest-basic

 sudo chmod u+x gradlew

 ./gradlew clean build

# To deploy the war file to tomcat9 do the following command:

 sudo cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war ~/apache-tomcat-10*/webapps/
```

#### Vagrant up
- Here's what vagrant up typically does:

- Start VMs: If the VMs defined in the Vagrantfile are not already running, vagrant upstarts them.

- Provisioning: It executes any provisioning scripts specified in the Vagrantfile. These scripts can include 
tasks such as software installation, configuration, and setup necessary for your development environment.

- Networking: Vagrant sets up networking as configured in the Vagrantfile, such as port forwarding and private 
networks, to enable communication between the host and the virtual machines.

- Shared Folders: It mounts any shared folders configured in the Vagrantfile, allowing files on the host machine 
to be accessible within the virtual machines.

#### Browsing our web application 

- Our app should now be reachable in:
```bash
http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT
```
- To see H2-console 
```bash
http://localhost:8080/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT/h2-console 
```
##### To reload and provision
```bash
$ vagrant reload --provision
```

##### Now we have a web vm connecting with a database vm
- This is possible because they are in the same network and the app is configured to connect with the database vm.

##### Alternative solution

#### What is Vmware (Workstation)? 

- Vmware Workstation is a desktop virtualization product that enables users to create, run, and manage virtual 
machines on their local desktop or laptop computers. It supports a wide range of operating systems, including 
various versions of Windows, Linux, and macOS.

#### Why Vmware? 

- VMware's virtualization software revolutionizes infrastructure by enabling the creation and management of virtual 
machines (VMs) on a single physical server. This technology optimizes resource utilization, reduces hardware costs, 
and enhances scalability and flexibility. With VMware, organizations can isolate workloads for improved security, 
streamline testing and development with virtualized environments, and ensure business continuity through features
like high availability and disaster recovery. Additionally, VMware simplifies IT management by centralizing 
administration and integrating with cloud environments, empowering businesses to innovate and adapt to evolving 
technology landscapes.

#### Initial setup 

- 1. Download Vmware workstation;
- 2. Install vagrant-vmware-desktop plugin allows vmware workstation as the provider for your Vagrant-managed virtual machines.
```bash
$ vagrant plugin install vagrant-vmware-desktop
```

#### Adapt vagrant file 

- Some things are similar to the previously explained vagrant file, but as we changed provider, configuration must change.

1. Set the provider as Vmware workstation and configure ip of the database vm 
```bash
config.vm.define "db" do |db|
    db.vm.box = "hashicorp/bionic64"
    db.vm.hostname = "db"
    db.vm.network "private_network", ip: "192.168.30.24"

    # We set more ram memory for this VM
    db.vm.provider "vmware_desktop" do |v|
      v.memory = 1024
      v.gui = true
    end
```
2. Set the provider as Vmware workstation and configure ip of the web vm
- Notice that both vm's are in the same private network, so they can communicate with each-other.
```bash
config.vm.define "web" do |web|
    web.vm.box = "hashicorp/bionic64"
    web.vm.hostname = "web"
    web.vm.network "private_network", ip: "192.168.30.28"

    # We set more ram memory for this VM
    web.vm.provider "vmware_desktop" do |v|
      v.memory = 1024
      v.gui = true
    end
```

3. Bash provisioning script clones a Git repository, modifies its configuration, builds the project using Gradle, 
and deploys the generated WAR file to Apache Tomcat 10.
```bash
 web.vm.provision "shell", inline: <<-SHELL, privileged: false
      # Change the following command to clone your own repository!

      git clone https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git
      cd devops-23-24-JPE-PMS-1231819/CA2/Part2
      sudo chmod -R a+rwx ./react-and-spring-data-rest-basic
      cd ./react-and-spring-data-rest-basic
      
      sed -i 's|spring.datasource.url=.*|spring.datasource.url=jdbc:h2:tcp://192.168.30.24:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE|' src/main/resources/application.properties
      
      sudo chmod u+x gradlew
      ./gradlew clean build
      # To deploy the war file to tomcat10??? do the following command:
      sudo cp ./build/libs/react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war ~/apache-tomcat-10*/webapps/
    SHELL
  end
end
```
- 1. It clones a Git repository from the specified URL 
(https://github.com/Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git) into the local machine.
- 2. It navigates into the cloned repository's directory (devops-23-24-JPE-PMS-1231819/CA2/Part2).
- 3. It grants read, write, and execute permissions recursively (-R) to all users (a+rwx) on the directory named 
react-and-spring-data-rest-basic within the repository.
- 4. It navigates into the react-and-spring-data-rest-basic directory.
- 5. It modifies a property in the application.properties file. Specifically, it replaces the value of 
spring.datasource.url with jdbc:h2:tcp://192.168.30.24:9092/./jpadb;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE.
- 6. It grants execute permission to the gradlew script.
- 7. It cleans the project and builds it using ./gradlew clean build.
- 8. Finally, it copies the generated WAR (Web Application Archive) file (react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war) 
into the webapps directory of Apache Tomcat 10.

#### Vagrant up and Networking

- Remember to vagrant up and access the web app as described before
- Both Virtual machines can connect with each other, but as they are in a private network in NAT mode, the host cannot reach them
directly. (Only via port forwarding)

##### Establishing an ssh connection

```bash
$ vagrant ssh web
or
$ vagrant ssh db
```
- When you run vagrant up, Vagrant sets up SSH connections by generating SSH key pairs on the host machine and 
injecting the public key into the guest machine during the provisioning process. This allows secure communication 
between the host and guest machines via SSH, enabling Vagrant to manage the guest machine remotely. 
Additionally, Vagrant configures SSH settings in the Vagrantfile to establish and manage SSH connections automatically.

#### Exploring the file system

- In web virtual machine

```bash
$ cd ~/apache-tomcat-10*/webapps/
$ ls 
```
- We should see the react-and-spring-data-rest-basic-0.0.1-SNAPSHOT.war and react-and-spring-data-rest-basic-0.0.1-SNAPSHOT
files, as proof that the app was deployed.

#### Some common commands 

```bash
$ vagrant halt - To stop the vms
$ vagrant destroy -f - To forcefully stop and delete all traces of the guest machine without asking confirmation.
$ vagrant suspend - Suspends the guest machine - persisting its current state to disk.
$ vagrant resume - Resumes a suspended Vagrant environment.
$ vagrant status - Shows the current status of the Vagrant environment.
$ vagrant provision - Reruns the provisioning scripts specified in the Vagrantfile.
```
