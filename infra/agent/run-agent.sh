#!/bin/bash

image=codeandrew/scanner:agent-trivy
NAME="agent-0"
docker build -t $image .

docker run -d --restart always\
 --name $NAME \
 --env-file .env \
 -v /home/jenkins:/var/jenkins_home \
 -v /var/run/docker.sock:/var/run/docker.sock \
 -p 2222:22 \
 -e JENKINS_AGENT_SSH_PUBKEY="$PUB_KEYS" $image