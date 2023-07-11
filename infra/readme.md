# Jenkin Infra as Code

## Agent Checklist

- sudoers
- python3
- python3-pip
- docker
  - login-creds
- docker-compose
- kubectl
  - login creds 
  
## Setting up VM agent

```
sudo apt-get update
sudo apt install -y --no-install-recommends openjdk-17-jdk-headless

```

## Overrides
Never do this in Production, only for Debugging Purposes 
``` 
docker exec -ti -u 0 jenkins-blueocean /bin/bash      
apt -y install sudo vim

vi /etc/sudoers
jenkins ALL=(ALL) NOPASSWD:ALL

apt-get install python3-pip
```


## Create Jenkins User in ubuntu 
```
sudo useradd -m -d /home/jenkins jenkins
sudo usermod -aG docker jenkins
```

## Containerized Jenkins Agent 

ssh-keygen -t rsa
docker run -d --restart always --name agent -v /home/jenkins:/var/jenkins_home -p 2222:22 -e JENKINS_AGENT_SSH_PUBKEY="ssh-rsa *************  user@email" jenkins/ssh-agent:jdk11


## Jenkins Working Directories and Files System

`/var/lib/jenkins/`
inside is owned by user and group `jenkins`
```
drwxr-xr-x  29 jenkins jenkins      4096 May 28 11:25 users
drwxr-xr-x 109 jenkins jenkins      8192 Jul 11 04:29 workspace
```


```
# To view job builds
/var/lib/jenkins/jobs/<pipeline-script-name>/builds/367
# 
/var/lib/jenkins/workspace/<pipeline-script-name>/
```

