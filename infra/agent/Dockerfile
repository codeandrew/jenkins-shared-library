FROM jenkins/ssh-agent:latest-bullseye-jdk11

RUN apt-get update && apt-get install -y apt-transport-https ca-certificates curl gnupg2 software-properties-common curl sudo kubectl helm doctl azure-cli
RUN curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -

RUN add-apt-repository "deb [arch=amd64] https://download.docker.com/linux/debian $(lsb_release -cs) stable"
RUN apt-get update && apt-get install -y docker-ce docker-ce-cli containerd.io python3 python3-pip

RUN curl -L "https://github.com/docker/compose/releases/download/1.26.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
RUN chmod +x /usr/local/bin/docker-compose

RUN curl -sfL https://raw.githubusercontent.com/aquasecurity/trivy/main/contrib/install.sh | sh -s -- -b /usr/local/bin v0.36.1
RUN trivy -v && trivy image --download-db-only 
