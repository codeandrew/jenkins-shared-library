# Docker Jenkins Agent 

ssh-keygen -t rsa

```
vi /etc/bashrc
PYOTIR_KEYS=#Save here
```

image=agent/trivy:jdk-11
NAME="agent-0"

docker run -d --restart always\
 --name $NAME \
 --env-file .env \
 -v /home/jenkins:/var/jenkins_home \
 -v /var/run/docker.sock:/var/run/docker.sock \
 -p 2222:22 \
 -e JENKINS_AGENT_SSH_PUBKEY="$PUB_KEYS" $image
