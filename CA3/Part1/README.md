## Class Assignment 3 - Part1 

For this class assignment, it was introduced some concepts about virtualization, client server applications architecture, 
and basic networking concepts.

##### First of all, what is virtualization? 

- Server virtualization allows running multiple *OS* instances at the same time,
on the same physical hardware. 
- Virtualization software exposes CPU, memory, and I/O. It dynamically manages its usage between
“guest” *OS* and solves concurrency and other problems regarding resources.
- The most common form of virtualization nowadays is by *Hardware-assisted virtualization*;

##### What are the advantages? 

- More flexible than "bare metal";
- Portable;
- Programmatically managed;
- Underlying hardware is used more efficiently because it can serve multiple guests at the same time;
- Cloud computing and containers run in a virtualized infrastructure;

#### Hypervisors 

- A hypervisor is a software layer between hardware and the VM(s). They allow resource sharing between guests
  (they are isolated), and make them access hardware only through the hypervisor.

##### Types of hypervisors: 

- Type 1 (bare-metal): runs directly in the hardware without a supporting *OS*;
- Type 2: It's a user space app that runs on top of an *OS*;

#### Virtual Box

- For this class assigment the chosen hypervisor is Virtual Box (Oracle). It's a type 2 hypervisor.
- Easy to use and install. Consumer grade product. 
- Cross-platform;
- Not the best option for "production" for performance and hardware support issues. 

#### 1. Create a virtual machine.

- First, we must install virtual box;
- After that, we must download Linux (Ubuntu) iso;
```
 Ubuntu 22.04.3 was the chosen version
````
- On virtual box. Press create VM and connect it with the previously downloaded ubuntu image;
- Regarding the network configuration:
```
  Virtual box creates by default, for all virtual machines, a network adapter in NAT mode;
  It also creates an interface for a host only network. Later in this tutorial we will give our newly
  created virtual machine a static IP in the range of the host only network created by virtual box (192.168.56.1/24). 
  This way we can connect directly from our host to guest without having to port foward. 
````
- Start the VM and install Ubuntu (this may take several minutes);
- Start the VM and login;
- Let's define a static ip in this ip range (192.168.56.1/24) (host-only network);
````
  1. sudo apt update - to update pakage repositories
  2. sudo apt install net-tools - install the network tools
  3. sudo vi /etc/netplan/00-installer-config.yaml
  4. add to the file:
    enp0s8:
      addresses:
        - 192.168.56.5/24;
  5. Apply the changes - sudo netplan apply          
````
- Install openssh-server and enable password authentication; 
- (Optional) Start a secure shell session to the VM using ssh. In my case, the command is: 
````
  ssh afonso@192.168.56.5       
````
- Install java 
```` 
  sudo apt install openjdk-18-jdk-headless - java without GUI tools as we are in a ubuntu-server
````
- Install git 

```` 
  sudo apt install git 
````

- Installing gradle and Maven? 
```` 
  Depends, we can always use gradle wrapper or maven wrapper. (More on that later. But if, for some reason we
  want gradle and maven installed locally we can type: 
  - sudo apt install maven - for maven;
  - sudo apt install gradle - for gradle (gradle may not be available as a package in all linux distros default repositories)
````

2. Clone the individual devops repository inside the VM. 

- To clone using ssh. Generate public, private ssh key in your VM. Add the public key to your GH account so that 
your VM gets registered as a host that may clone repositories in your account. 
````
  git clone git@github.com:Departamento-de-Engenharia-Informatica/devops-23-24-JPE-PMS-1231819.git
````

3. Build and execute *spring-boot* tutorial basic project:

````
  - cd CA1/basic
  - chmod +x mvnw - give execute permmitions to maven wrapper script 
  - -/mvnw compile
  - ./mvnw spring-boot:run
````
- The embedded Tomcat server inside spring boot app initializes in port 8080—Tomcat started on port 8080 (http) with a context path ''.

````
  - cd CA2/Part2/react-and-spring-data-rest-basic
  - chmod +x gradlew - give execute permmitions to gradle wrapper script 
  - ./gradlew build
  -./gradlew bootRun
````
- I did not have any difficulties running the different tasks specified in build.gradle file 
because none of them required a graphical environment or specific filesystem permissions that may not be available 
in a headless server environment. The same does not apply in for the simple chat app.
- Finally, to check if the web app is accessible to my machine through the web browser, we must type in the search bar:

````
  192.168.56.5:8080
````
- By typing this, we are making an http Get request to the host with the 192.168.56.5 (it is reachable to us due to 
the previous host only network creation steps) in port 8080 (default Tomcat server port).

4. Build and execute gradle_basic_demo (chat app):
- In the VM type:
````
  - cd CA2/Part1/gradle_basic_demo
  - chmod +x gradlew - give execute permmitions to gradle wrapper script 
  - ./gradlew runServer - this task lauches a server for this app in 59001 port;
````

- In our physical machine launch clients, typing:
- Before that, we have to remember that we no longer want to connect to localhost but to a reachable VM 
with the 192.168.56.5 ip. We must change the build.gradle file from *args 'localhost', '59001' to args '192.168.56.5', '59001'*.
- Alternative we can also run the task with the command commands 

````
  gradlew.bat runClient --args="192.168.56.5 59001"

````
##### Running the client 
````
  - cd CA2/Part1/gradle_basic_demo
  - ./gradlew runClient - this runs a client that is going to connect to a server with 192.168.56.5 in
59001 port.  
````

##### Why should we launch the client side in host and server side in guest? 

- Headless Environment: Servers are often accessed remotely or operated in a headless environment 
(without a graphical display). Attempting to run GUI applications on a server without a display may
result in errors or unexpected behavior.
- In this particular case, the client side is a Swing-based GUI application, we won't be able to see the 
graphical interface directly on the server itself. 
- If we try to run client side in ubuntu-server, then a Headless Exception is thrown:

``````
A HeadlessException in Java typically occurs when a program tries to use functionality that requires a graphical 
environment, such as AWT or Swing components, in a headless environment where no graphical display is available. 
This often happens when running Java applications on a server operating system.
``````

- For that, we must run the client side on our host machine and server side in guest machine.

##### Conclusion 

``````
Virtualization technologies, including virtual machines (VMs) and tools like Vagrant, have revolutionized software 
development and deployment practices. By abstracting hardware resources and providing efficient management of 
virtual environments, these technologies enable developers to create consistent, reproducible development environments. 
Vagrant, in particular, streamlines the process of provisioning and managing VMs, allowing developers to automate 
setup tasks and collaborate effectively across teams.
``````
