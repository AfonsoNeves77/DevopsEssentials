FROM jenkins/jenkins:2.452.1-jdk17

USER root

# Install lsb-release
RUN apt-get update && apt-get install -y lsb-release

# Add Docker's official GPG key
RUN curl -fsSLo /usr/share/keyrings/docker-archive-keyring.asc https://download.docker.com/linux/debian/gpg

# Set up the Docker stable repository
RUN echo "deb [arch=$(dpkg --print-architecture) signed-by=/usr/share/keyrings/docker-archive-keyring.asc] https://download.docker.com/linux/debian $(lsb_release -cs) stable" > /etc/apt/sources.list.d/docker.list

# Update the package index and install docker-ce-cli
RUN apt-get update && apt-get install -y docker-ce-cli

# Switch back to the jenkins user
USER jenkins

# Install Jenkins plugins
RUN jenkins-plugin-cli --plugins "blueocean docker-workflow"
